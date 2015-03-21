<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />

<title>プッシュ通知</title>
</head>
<body>


<?php
    $link = mysql_connect('****', 'name', 'pass');
    if (!$link) {
        die('接続失敗です。'.mysql_error());
    }
    
    $db_selected = mysql_select_db('****', $link);
    if (!$db_selected) {
        die('データベース選択失敗です。'.mysql_error());
    }
    mysql_set_charset('utf8');
    
    $registatoin_ids = array();
    $result = mysql_query('select regiid from gcmid where kind = 1;');

    while ($row = mysql_fetch_assoc($result)){
        array_push($registatoin_ids, $row['regiid']);
        print "id : ".$row['regiid']."<br/><br/>";
    }
    
    $message = $_POST['message'];
    $sid = $_POST['regi_id'];
    $time = $_POST['time'];

    $checkNumber1 = 100;
    mysql_query('insert into extract_log(spot_id, kind) values('.$checkNumber1.', 0);');
    
   if ($message == null) {
        $message = "ブラウザから送ってます。";
    }
    if ($time == null) {
        $time = "1999:01:01:01:01:01";
    }
    
    $gcm = new GCM();
    $send_content = array("message"=> $message, "senderId"=>$sid, "kind"=>"1", "spotId"=>"-1", "time"=>$time);
    $result_android = $gcm->send_notification($registatoin_ids, $send_content, $ttl);
    
    class GCM {
        
        function __construct() {
        }
        
        public function send_notification($registatoin_ids, $send_content) {
            $randnum = rand(1, 1000);
            $collapse_key = "update".$randnum;
            print "<br/>".$collapse_key."<br/>";
            
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
            curl_setopt($ch, CURLOPT_POSTFIELDS, json_encode($fields));
            $result = curl_exec($ch);
            
            if($result != true){
                die("Curl failed: ".curl_error($ch));
            } else {
            }
            
            curl_close($ch);
            print "<br/>".$result."<br/>";
            
        }
    }
    
    print'<br/>name: '.$name.'<br/>';
    print'title: '.$title.'<br/>';
    print'message: '.$message.'<br/>';
    print$sid;
    
    ?>

</body>
</html>