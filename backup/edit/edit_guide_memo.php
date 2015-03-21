<?php
    $memo = $_POST['Memo'];
    $guideId = $_POST['GuideId'];
    
    $link = mysql_connect('****', 'name', 'pass');
    if (!$link) {
        die('接続失敗です。'.mysql_error());
    }
    
    $db_selected = mysql_select_db('****', $link);
    if (!$db_selected) {
        die('データベース選択失敗です。'.mysql_error());
    }
    mysql_set_charset('utf8');
    
    $result = mysql_query("update guide set memo = '".$memo."' where guide_id = '".$guideId."';");
    
    if (!$result) {
        die('クエリーが失敗しました。'.mysql_error());
    }
    
?>