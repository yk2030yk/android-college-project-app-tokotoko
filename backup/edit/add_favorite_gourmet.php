<?php
    $id = $_POST['GourmetId'];
    $name = $_POST['Name'];
    $lat = $_POST['Lat'];
    $lng = $_POST['Lng'];
    
    $link = mysql_connect('****', 'name', 'pass');
    if (!$link) {
        die('接続失敗です。'.mysql_error());
    }
    
    $db_selected = mysql_select_db('****', $link);
    if (!$db_selected) {
        die('データベース選択失敗です。'.mysql_error());
    }
    mysql_set_charset('utf8');
    
    mysql_query("insert into favorite_gourmet(gourmet_id, name, lat, lng) values('".$id."', '".$name."', '".$lat."', '".$lng."');");
    
    if (!$result) {
        die('クエリーが失敗しました。'.mysql_error());
    }
?>
