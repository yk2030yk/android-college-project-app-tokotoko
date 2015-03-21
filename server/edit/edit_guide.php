<?php
    $groupId = $_POST['GroupId'];
    $guideId = $_POST['GuideId'];
    $hotelId = $_POST['HotelId'];
    $title = $_POST['Title'];
    $stay = $_POST['StayCount'];
    $newDay = $_POST['Days'];
    
    $springStart = date("m", strtotime("2012-03-01"));
    $springEnd = date("m", strtotime("2012-05-01"));
    $summerStart = date("m", strtotime("2012-06-01"));
    $summerEnd = date("m", strtotime("2012-08-01"));
    $autumnStart = date("m", strtotime("2012-09-01"));
    $autumnEnd = date("m", strtotime("2012-11-01"));
    $winterStart = date("m", strtotime("2012-12-01"));
    $winterMiddle = date("m", strtotime("2012-01-01"));
    $winterEnd = date("m", strtotime("2012-02-01"));
    
    foreach ($newDay as $day) {
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
    
    $link = mysql_connect('****', 'name', 'pass');
    if (!$link) {
        die('接続失敗です。'.mysql_error());
    }
    
    $db_selected = mysql_select_db('****', $link);
    if (!$db_selected) {
        die('データベース選択失敗です。'.mysql_error());
    }
    mysql_set_charset('utf8');
    
    $result = mysql_query("update guide set hotel_id = '".$hotelId."', name = '".$title."', stay_count = '".$stay."', option_id = '".$option."' where guide_id = '".$guideId."';");
    
    if (!$result) {
        die('クエリーが失敗しました。'.mysql_error());
    }
    
    $result = mysql_query("select day from guide_day where guide_id = '".$guideId."';");
    
    if (!$result) {
        die('クエリーが失敗しました。'.mysql_error());
    }
    
    $oldDay = array();
    while ($row = mysql_fetch_assoc($result)) {
        array_push($oldDay, $row["day"]);
    }
    
    foreach ($newDay as $nDay) {
        $exist = false;
        foreach ($oldDay as $oDay) {
            if (strcmp($nDay, $oDay) == 0) {
                $exist = true;
            }
        }
        
        if (!$exist) {
            $result = mysql_query("insert into guide_day(guide_id, day) values('".$guideId."', '".$nDay."');");
        }
    }
    
    foreach ($oldDay as $oDay) {
        $exist = false;
        foreach ($newDay as $nDay) {
            if (strcmp($nDay, $oDay) == 0) {
                $exist = true;
            }
        }
        
        if (!$exist) {
            $result = mysql_query("delete from guide_day where guide_id = '".$guideId."' and day = '".$oDay."';");
        }
    }
?>
