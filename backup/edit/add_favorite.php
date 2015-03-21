<?php
    $sid = $_POST['spotId'];
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
    
    $sql = "insert into favorite(spot_id, favorite_spot_name, lat, lng) value('$sid', '$name', '$lat', '$lng');";
    $result_flag = mysql_query($sql);
    
    if (!$result_flag) {
        die('INSERT•Ø•®•Í°º§¨º∫«‘§∑§ﬁ§∑§ø°£'.mysql_error());
    }
?>
