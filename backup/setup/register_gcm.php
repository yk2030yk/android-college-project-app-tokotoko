<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>登録</title>
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
    
    
    $link = mysql_connect('****', 'name', 'pass');
    if (!$link) {
        die('接続失敗です。'.mysql_error());
    }
    
    $db_selected = mysql_select_db('****', $link);
    if (!$db_selected) {
        die('データベース選択失敗です。'.mysql_error());
    }
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

