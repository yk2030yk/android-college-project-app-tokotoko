<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link rel="stylesheet" href="top.css" type="text/css" />
<title>キーワード抽出</title>
</head>
<body>

<p>
<?php
    // 試作品　旧バージョン
    class keyword {
        public $keyword;
        public $id;
        public $cnt;
        public $per;
        public $kind;
        public $area;
        public $cate;
        
        function __construct($key, $cnt, $id, $cate, $area, $kind) {
            $this->keyword = $key;
            $this->cnt = $cnt;
            $this->id = $id;
            $this->cate = $cate;
            $this->area = $area;
            $this->kind = $kind;
            $len = mb_strlen($key, 'utf-8');
            $per = round(($cnt+1)/$len*100);
            $this->per = $per;
        }
    }
    
    function cmp_per( $a , $b) {
        if ($a->per < $b->per) {
            return true;
        } else {
            return false;
        }
    }
    
    function exist_in_db( $id ) {
        $sql = 'select * from log where spot_id='.$id.';';
        $result = mysql_query($sql);
        if (mysql_num_rows($result) == 0) {
            return false;
        } else {
            return true;
        }
    }
    
    $text = $_POST['message'];
    $send_time = $_POST['time'];
    
    if ($text != null) {
        $time_start = microtime(true);
        
        $link = mysql_connect('****', 'name', 'pass');
        if (!$link) {
            die('接続失敗です。'.mysql_error());
        }
        
        $db_selected = mysql_select_db('****', $link);
        if (!$db_selected) {
            die('データベース選択失敗です。'.mysql_error());
        }
        mysql_set_charset('utf8');
    
        $extract_spot = array();
        $extract_noun = array();
        
        $result = mysql_query('select * from spot;');
            
        while ($row = mysql_fetch_assoc($result)) {
            $keyword = $row['name'];
                
            $find_cnt = 0;
            $wordlen = mb_strlen($keyword, 'utf-8');
                
            for ($i = 0 ; $i < ($wordlen - 1); $i++) {
                $str = mb_substr($keyword, $i, 2, 'utf-8');
                if (strstr($text, $str)) {
                    $find_cnt++;
                }
            }
                
            if ($find_cnt > 0) {
                $key = new keyword($keyword, $find_cnt, $row['id'], -1, $row['area_id'], 0);
                array_push($extract_spot, $key);
                
                if ($key->per == 100) {
                    mysql_query('insert into extract_log(spot_id, kind) values('.$row['id'].', 0);');
                }
            }
        }
                   
        $result = mysql_query('select * from keyword;');
        while ($row = mysql_fetch_assoc($result)) {
            $keyword = $row['name'];
            if (strstr($text, $keyword)) {
                array_push($extract_noun, new keyword($keyword, mb_strlen($keyword, 'utf-8')-1, $row['id'], $row['sub_cate_id'], -1, 1));
                mysql_query('insert into extract_log(keyword_id, kind) values('.$row['id'].', 1);');
            }
        }
        
        
        usort($extract_spot, "cmp_per" );
        $first_spot = null;
        $second_spot = null;
        foreach ($extract_spot as $spot) {
            if (!exist_in_db($spot->id)) {
                $first_spot = $spot;
                break;
            } else {
                $second_spot = $spot;
                break;
            }
        }
        
        $sql = 'select * from spot';
        if ($first_spot != null) {
            $sql.=' where id='.$first_spot->id;
        } else if ($second_spot != null) {
            $sql.=' where area_id='.$second_spot->area;
            $i = 0;
            foreach ($extract_noun as $noun) {
                if ($i == 0) {
                    $sql.=' and (sub_cate_id='.$noun->cate;
                }else{
                    $sql.=' or sub_cate_id='.$noun->cate;
                }
                $i++;
            }
            if ($i!=0) {
                $sql.=')';
            }
            $sql.=' and id not in(select spot_id from log) order by id limit 1';
        } else {
            $i=0;
            foreach ($extract_noun as $noun) {
                if ($i==0) {
                    $sql.=' where (sub_cate_id='.$noun->cate;
                } else {
                    $sql.=' or sub_cate_id='.$noun->cate;
                }
                $i++;
            }
            if ($i==0) {
                $sql.=' where id=0';
            } else {
                $sql.=')';
            }
            $sql.=' and id not in(select spot_id from log) order by id limit 1';
        }
        print$sql.'<br/>'.'<br/>'.'<br/>';
        
    }
    
    $exist_data = false;
    $result = mysql_query($sql.';');
    while ($row = mysql_fetch_assoc($result)){
        $exist_data = true;
        mysql_query('insert into kproject_myproject.log(spot_id) values('.$row['id'].');');
        $result_message = $row['name'];
        $spot_id = $row['id'];
    }
        
    $time_end = microtime(true);
    $time = $time_end - $time_start;
        
        
    $registatoin_ids = array();
    $result = mysql_query('select regiid from gcmid;');
    while ($row = mysql_fetch_assoc($result)){
        array_push($registatoin_ids, $row['regiid']);
        print $row['regiid'];
    }
            
    
    if ($send_time == null) {
        $senf_time = "1999:01:01:01:01:01";
    }
    $gcm = new GCM();
    $send_content = array("message"=> $result_message, "senderId"=>"-1", "kind"=>"2", "spotId"=>$spot_id, "time"=>$send_time);
    if($exist_data){
        $result_android = $gcm->send_notification($registatoin_ids,$send_content,$ttl);
    }
        
    class GCM{
        function __construct(){}
        public function send_notification($registatoin_ids,$send_content){
            // GOOGLE API KEY
            define("GOOGLE_API_KEY","API KEY");
            $url = "https://android.googleapis.com/gcm/send";
            $fields = array(
                            "collapse_key" => "score_update",                            
                            "registration_ids" => $registatoin_ids,
                            "data" => $send_content
                            );
                
            $headers = array(
                            "Authorization: key=".GOOGLE_API_KEY,
                            "Content-Type: application/json"
                            );
            $ch = curl_init();
            curl_setopt($ch,CURLOPT_URL,$url);
            curl_setopt($ch,CURLOPT_POST,true);
            curl_setopt($ch,CURLOPT_HTTPHEADER,$headers);
            curl_setopt($ch,CURLOPT_RETURNTRANSFER,true);
            curl_setopt($ch,CURLOPT_SSL_VERIFYPEER,false);
            curl_setopt($ch,CURLOPT_POSTFIELDS,json_encode($fields));
            $result1 = curl_exec($ch);
            if($result1 === FALSE){
                die("Curl failed: ".curl_error($ch));
            }
            curl_close($ch);
            echo $result1;
        }
    }

    $close_flag = mysql_close($link);
?>

</body>
</html>






