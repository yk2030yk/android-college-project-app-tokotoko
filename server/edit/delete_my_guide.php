<?php
    $id = $_POST['id'];
    
    $link = mysql_connect('****', 'name', 'pass');
    if (!$link) {
        die('接続失敗です。'.mysql_error());
    }
    
    $db_selected = mysql_select_db('****', $link);
    if (!$db_selected) {
        die('データベース選択失敗です。'.mysql_error());
    }
    mysql_set_charset('utf8');
    
    if ($id != null) {
        $result = mysql_query("delete from guide where guide_id = '".$id."';");
    }
?>
