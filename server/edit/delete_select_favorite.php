<?php
    $sid = $_POST['spotId'];
    $gid = $_POST['gourmetId'];
    $hid = $_POST['hotelId'];
    
    $link = mysql_connect('****', 'name', 'pass');
    if (!$link) {
        die('接続失敗です。'.mysql_error());
    }
    
    $db_selected = mysql_select_db('****', $link);
    if (!$db_selected) {
        die('データベース選択失敗です。'.mysql_error());
    }
    mysql_set_charset('utf8');
    
    if ($sid != null) {
        foreach ($sid as $id) {
            $result = mysql_query("delete from favorite where spot_id = '".$id."';");
        }
    }
    
    if ($gid != null) {
        foreach ($gid as $id) {
            $result = mysql_query("delete from favorite_gourmet where gourmet_id = '".$id."';");
        }
    }
    
    if ($hid != null) {
        foreach ($hid as $id) {
            $result = mysql_query("delete from favorite_hotel where hotel_id = '".$id."';");
        }
    }
?>

