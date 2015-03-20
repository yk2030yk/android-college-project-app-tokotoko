<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>プッシュ通知</title>
</head>
<body>

<?php
    $id = $_POST['regi_id'];
    
    function quote_smart($value)
    {
        if (!is_numeric($value)) {
            $value = "'" . mysql_real_escape_string($value) . "'";
        }
        return $value;
    }
    
    
    $link = mysql_connect('mysql1.php.xdomain.ne.jp', 'kproject_negishi', 'negishi');
    if (!$link) {
        die('接続失敗です。'.mysql_error());
    }
    
    $db_selected = mysql_select_db('kproject_myproject', $link);
    if (!$db_selected){
        die('データベース選択失敗です。'.mysql_error());
    }
    print('<p>myprojectを選択しました。</p>');
    
    mysql_set_charset('utf8');
    
    $sql = "INSERT INTO gcmid (regiid) VALUES ('$id')";
    $result_flag = mysql_query($sql);
    
    if (!$result_flag) {
        die('INSERTクエリーが失敗しました。'.mysql_error());
    }
    
    $close_flag = mysql_close($link);
    
    if ($close_flag){
        print('<p>切断に成功しました。</p>');
    }
    echo "データの登録に成功しました.";
    
?>
</body>
</html>

