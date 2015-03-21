<?php
    $groupId = $_POST['GroupId'];
    $hotelId = $_POST['HotelId'];
    $title = $_POST['Title'];
    $stay = $_POST['StayCount'];
    $creationDate = $_POST['CreationDate'];
    $days = $_POST['Days'];
        
    $link = mysql_connect('****', 'name', 'pass');
    if (!$link) {
        die('接続失敗です。'.mysql_error());
    }
    
    $db_selected = mysql_select_db('****', $link);
    if (!$db_selected) {
        die('データベース選択失敗です。'.mysql_error());
    }
    mysql_set_charset('utf8');
    
    $springStart = date("m", strtotime("2012-03-01"));
    $springEnd = date("m", strtotime("2012-05-01"));
    $summerStart = date("m", strtotime("2012-06-01"));
    $summerEnd = date("m", strtotime("2012-08-01"));
    $autumnStart = date("m", strtotime("2012-09-01"));
    $autumnEnd = date("m", strtotime("2012-11-01"));
    $winterStart = date("m", strtotime("2012-12-01"));
    $winterMiddle = date("m", strtotime("2012-01-01"));
    $winterEnd = date("m", strtotime("2012-02-01"));
    
    foreach ($days as $day) {
        $myday = date("m", strtotime($day));
        if(($springStart <= $myday) && ($myday <= $springEnd)) {
            $option = 100;
        } else if(($summerStart <= $myday) && ($myday <= $summerEnd)) {
            $option = 200;
        } else if(($autumnStart <= $myday) && ($myday <= $autumnEnd)) {
            $option = 300;
        } else if(($winterStart == $myday) || ($myday == $winterMiddle) || ($myday == $winterEnd)) {
            $option = 400;
        } else {
            $option = 800;
        }
    }
    
    $result = mysql_query("INSERT INTO guide(group_id , hotel_id ,name, stay_count, creation_date, option_id) VALUES('".$groupId."', '".$hotelId."', '".$title."', '".$stay."', '".$creationDate."', '".$option."');");
    
    if (!$result) {
        die('クエリーが失敗しました。'.mysql_error());
    }
    
    $result = mysql_query("select guide_id from guide where group_id = '".$groupId."' and hotel_id = '".$hotelId."'and name = '".$title."' and creation_date = '".$creationDate."';");
    
    if (!$result) {
        die('クエリーが失敗しました。'.mysql_error());
    }
    
    while ($row = mysql_fetch_assoc($result)) {
        $guideId = $row['guide_id'];
    }
    
    foreach ($days as $day) {
        $result = mysql_query("insert into guide_day(guide_id, day) values('".$guideId."', '".$day."');");
        if (!$result) {
            die('クエリーが失敗しました。'.mysql_error());
        }
    }
?>
