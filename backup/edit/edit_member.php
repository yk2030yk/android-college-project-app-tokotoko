<?php
    $regi_id = $_POST['regi_id'];
    $name = $_POST['name'];
    $my_id = $_POST['my_id'];
    
    $link = mysql_connect('****', 'name', 'pass');
    if (!$link) {
        die('接続失敗です。'.mysql_error());
    }
    
    $db_selected = mysql_select_db('****', $link);
    if (!$db_selected) {
        die('データベース選択失敗です。'.mysql_error());
    }
    mysql_set_charset('utf8');
    
    $sql = 'update gcmid';
    $sql.=" set name = '".$name."'";
    $sql.=", my_id = '".$my_id."'";
    $sql.=", image = '".$my_id."'";
    $sql.=" where regiid = '".$regi_id."';";
    
    if ($regi_id != null && $name != null) {
        $result = mysql_query($sql);
        
        if (!$result) {
            die('クエリーが失敗しました。'.mysql_error());
        }
        
        $result = mysql_query('select group_ver from application where id = 1');
        if ($row = mysql_fetch_assoc($result)) {
            if ($row['group_ver'] >= 20000000) {
                $version = 1;
            } else {
                $version = $row['group_ver'] + 1;
            }
            mysql_query('update application set group_ver = "'.$version.'";');
        }
        
    }
    
?>
