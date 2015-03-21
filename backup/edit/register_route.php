<?php
    $id = $_POST['guideDayId'];
    $spotid = $_POST['spotId'];
    $name = $_POST['Name'];
    $lat = $_POST['Lat'];
    $lng = $_POST['Lng'];
    $stime = $_POST['startTime'];
    $etime = $_POST['endTime'];
    $stime_s = $_POST['startTimeSec'];
    $etime_s = $_POST['endTimeSec'];
    $priority = $_POST['priority'];
    $distance = $_POST['distance'];
    $duration = $_POST['duration'];
    $duration_s = $_POST['durationSec'];
    $stay_s = $_POST['stayTimeSec'];
    $kind = $_POST['kind'];
  
    $link = mysql_connect('****', 'name', 'pass');
    if (!$link) {
        die('接続失敗です。'.mysql_error());
    }
    
    $db_selected = mysql_select_db('****', $link);
    if (!$db_selected) {
        die('データベース選択失敗です。'.mysql_error());
    }
    mysql_set_charset('utf8');
    
    mysql_query("delete from route where guide_day_id = '".$id."'");
    
    for($i = 0 ; $i < count($priority); $i++) {
        $result = mysql_query("insert into route(guide_day_id, spot_id, name, lat, lng, priority, start_time, end_time, start_time_sec, end_time_sec, duration, distance, duration_sec, stay_time_sec, kind) values('".$id."', '".$spotid[$i]."', '".$name[$i]."', '".$lat[$i]."', '".$lng[$i]."', '".$priority[$i]."', '".$stime[$i]."', '".$etime[$i]."', '".$stime_s[$i]."', '".$etime_s[$i]."', '".$duration[$i]."', '".$distance[$i]."', '".$duration_s[$i]."', '".$stay_s[$i]."', '".$kind[$i]."');");
    }
    
    if (!$result) {
        die('クエリーが失敗しました。'.mysql_error());
    }
?>
