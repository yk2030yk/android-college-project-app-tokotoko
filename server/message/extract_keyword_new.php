<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link rel="stylesheet" href="top.css" type="text/css" />
<title>キーワード抽出</title>
</head>
<body>

<p>
<?php
    
    class Keyword {
        public $keyword;
        public $id;
        
        public $cnt;
        public $per;
        
        public $area;
        public $garea;
        public $cate;
        public $gcate;
        
        public $kind;
        
        function __construct($key, $cnt, $id, $cate, $gcate, $area, $garea, $kind) {
            $this->keyword = $key;
            $this->cnt = $cnt;
            $this->id = $id;
            
            $this->cate = $cate;
            $this->gcate = $gcate;
            $this->area = $area;
            $this->garea = $garea;
            
            $this->kind = $kind;
            
            $len = mb_strlen($key, 'utf-8');
            $per = round(($cnt + 1) / $len * 100);
            $this->per = $per;
        }
    }
    
    class GCM {
        
        function __construct() {
        }
        
        public function send_notification($registatoin_ids, $send_content) {
            $randnum = rand(1, 1000);
            $collapse_key = "extract".$randnum;
            
            // GOOGLE API KEY
            define("GOOGLE_API_KEY","API KEY");
            
            $url = "https://android.googleapis.com/gcm/send";
            
            $fields = array(
                            "collapse_key" => $collapse_key,
                            "registration_ids" => $registatoin_ids,
                            "data" => $send_content,
                            "delay_while_idle" => false,
                            "time_to_live" => 3600
                            );
            
            $headers = array(
                             "Authorization: key=".GOOGLE_API_KEY,
                             "Content-Type: application/json"
                             );
            
            $ch = curl_init();
            curl_setopt($ch, CURLOPT_URL, $url);
            curl_setopt($ch, CURLOPT_POST, true);
            curl_setopt($ch, CURLOPT_HTTPHEADER, $headers);
            curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
            curl_setopt($ch, CURLOPT_SSL_VERIFYPEER, false);
            curl_setopt($ch, CURLOPT_POSTFIELDS,json_encode($fields));
            
            $result1 = curl_exec($ch);
            
            if ($result1 == false) {
                die("Curl failed: ".curl_error($ch));
            }
            
            curl_close($ch);
            echo $result1;
        }
    }
    
    function quote_smart($value) {
        if (!is_numeric($value)) {
            $value = "'" . mysql_real_escape_string($value) . "'";
        }
        return $value;
    }
    
    //%で降順
    function cmp_per( $a , $b) {
        if ($a->per < $b->per) {
            return true;
        } else {
            return false;
        }
    }
    
    //提供ログにあるか調べる
    function exist_in_db( $id ) {
        $sql = 'select * from log where spot_id='.$id.';';
        $result = mysql_query($sql);
        
        if (mysql_num_rows($result) == 0) {
            return false;
        } else {
            return true;
        }
    }
    
    //カテゴリーの種類を調べる
    function check_category( $list ) {
        $countNomal = 0;
        $countGourmet = 0;
        
        foreach($list as $item) {
            if ($item->gcate < 10) {
                $countNomal++;
            } else {
                $countGourmet++;
            }
        }
        
        //グルメの方が多ければtrue、普通のカテゴリが多ければfalseを返す
        if ($countGourmet > $countNomal) {
            return true;
        } else {
            return false;
        }
    }
    
    //グルメカテゴリーIDを返す
    //どのカテゴリーを返すのか・条件は？
    function get_gourmet_category( $list ) {
        $cate = null;
        
        foreach($list as $item) {
            if ($item->gcate < 10) {
            
            } else {
                $cate = $item;
            }
        }
        
        return $cate->gcate;
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
        
        //観光地名と比較する
        $result = mysql_query('select spot.id, spot.name as name, spot.area_id, area.gourmet_area_id from spot left join area on spot.area_id = area.id;');
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
            
            //一致カウントが1以上のものを配列に追加
            if ($find_cnt > 0) {
                $key = new Keyword($keyword, $find_cnt, $row['id'], -1, -1, $row['area_id'], $row['gourmet_area_id'], 0);
                
                //一致率が35%以上なら配列に追加  per > 1 / 3
                if ($key->per > 35) {
                    array_push($extract_spot, $key);
                }
            }
        }
        
        //キーワードと比較
        $result = mysql_query('select keyword.name as name, keyword.sub_cate_id, sub_category.gourmet_cate_id from keyword left join sub_category on keyword.sub_cate_id = sub_category.id;');
        while ($row = mysql_fetch_assoc($result)) {
            //キーワードで完全一致するもの配列に追加
            $keyword = $row['name'];
            
            if (strstr($text, $keyword)) {
                $find_cnt = mb_strlen($keyword, 'utf-8') - 1;
                $data = new Keyword($keyword, $find_cnt, $row['id'], $row['sub_cate_id'], $row['gourmet_cate_id'], -1,  -1, 1);
                array_push($extract_noun, $data);
            }
            
        }
        
        //一致率順で並び替え
        usort($extract_spot, "cmp_per");
        
        $first_spot = null;
        $second_spot = null;
        foreach ($extract_spot as $spot) {
            //一致率が高くて、提供ログにないものをfirst_spotにする
            if (!exist_in_db($spot->id)) {
                $first_spot = $spot;
                break;
            //提供ログになかったら、一致率の高いものをsecond_spotにする
            } else {
                $second_spot = $spot;
                break;
            }
        }
        
        $sql = 'select * from spot';
        
        //first_spotのデータを提供sql生成
        if ($first_spot != null) {
            $sql.=' where id='.$first_spot->id;
        
        //second_spotのエリアかつ、抽出カテゴリーのデータを提供sql生成
        } else if ($second_spot != null) {
            $sql.=' where area_id='.$second_spot->area;
            
            $i = 0;
            
            foreach ($extract_noun as $noun) {
                if ($i == 0) {
                    $sql.=' and (sub_cate_id='.$noun->cate;
                } else {
                    $sql.=' or sub_cate_id='.$noun->cate;
                }
                $i++;
            }
            
            if ($i != 0) {
                $sql.=')';
            }
            
            $sql.=' and id not in(select spot_id from log) order by id limit 1';
        
        //抽出カテゴリーのデータを提供sql生成
        } else {
            $i=0;
            
            foreach ($extract_noun as $noun) {
                if ($i == 0) {
                    $sql.=' where (sub_cate_id='.$noun->cate;
                } else {
                    $sql.=' or sub_cate_id='.$noun->cate;
                }
                $i++;
            }
            
            if ($i == 0) {
                $sql.=' where id=0';
            } else {
                $sql.=')';
            }
            
            $sql.=' and id not in(select spot_id from log) order by id limit 1';
        }
                
    
        //++++++送信するデータの値を変数に入れていく++++++++
        $exist_data = false;
    
        //生成したsqlからデータを見つける
        $result = mysql_query($sql.';');
        while ($row = mysql_fetch_assoc($result)){
            $exist_data = true;
            $result_message = $row['name'];
            $spot_id = $row['id'];
        }
    
        if ($send_time == null) {
            $send_time = "1999:01:01:01:01:01";
        }
    
        //++++++送信するデータの値を変数に入れていく++++++++
    
        $registatoin_ids = array();
        $result = mysql_query('select regiid from gcmid where kind = 1;');
        while ($row = mysql_fetch_assoc($result)) {
            array_push($registatoin_ids, $row['regiid']);
        }

        $result2 = mysql_query('select gourmet_offer_count from application where id = 1;');
        while ($row2 = mysql_fetch_assoc($result2)) {
            $goCnt = $row2['gourmet_offer_count'];
        }     
    
        $gcm = new GCM();
        //抽出データの送信
        if ($first_spot != null) {
        
            $send_content = array("message"=>$result_message, "senderId"=>"-1", "kind"=>"2", "spotId"=>$spot_id, "time"=>$send_time);
            if ($exist_data) {
                mysql_query('insert into kproject_myproject.log(spot_id) values('.$spot_id.');');
                $result_android = $gcm->send_notification($registatoin_ids, $send_content, $ttl);
            }
        
        } else {
            //グルメ以外の普通のキーワードが多ければ
            if (!check_category($extract_noun)) {
            
                $send_content = array("message"=>$result_message, "senderId"=>"-1", "kind"=>"2", "spotId"=>$spot_id, "time"=>$send_time);
                if ($exist_data) {
                    mysql_query('insert into kproject_myproject.log(spot_id) values('.$spot_id.');');
                    $result_android = $gcm->send_notification($registatoin_ids, $send_content, $ttl);
                }
            
            //グルメに関するキーワードが多ければ
            } else {
                if ($goCnt < 10) {
                    $goCnt++;
                } else {
                    $goCnt=0;
                }
                mysql_query('update application set gourmet_offer_count = "'.$goCnt.'" where id = 1;');
                
                //second_spotがあれば
                $pos = rand(1, 9);
                if ($second_spot != null) {
                    $gcid = get_gourmet_category( $extract_noun );
                    $send_content = array("gourmetCateId"=>$gcid, "gourmetAreaId"=>$second_spot->garea, "senderId"=>"-1", "kind"=>"3", "time"=>$send_time, "position"=>$pos);
                    $result_android = $gcm->send_notification($registatoin_ids, $send_content, $ttl);
                    //second_spotがなければ
                } else {
                    $gcid = get_gourmet_category( $extract_noun );
                    $send_content = array("gourmetCateId"=>$gcid,  "gourmetAreaId"=>"421", "senderId"=>"-1", "kind"=>"3", "time"=>$send_time, "position"=>$pos);
                    $result_android = $gcm->send_notification($registatoin_ids, $send_content, $ttl);
                }
            }
        }
    }
    
    
    $time_end = microtime(true);
    $time = $time_end - $time_start;
    $close_flag = mysql_close($link);
?>

</body>
</html>



