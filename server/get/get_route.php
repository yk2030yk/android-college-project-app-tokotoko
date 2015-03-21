<?php
    $guide_day_id = $_POST['GuideDayId'];
    
    $link = mysql_connect('****', 'name', 'pass');
    if (!$link) {
        die('接続失敗です。'.mysql_error());
    }
    
    $db_selected = mysql_select_db('****', $link);
    if (!$db_selected) {
        die('データベース選択失敗です。'.mysql_error());
    }
    mysql_set_charset('utf8');

    $dom = new DOMDocument('1.0', 'UTF-8');
    $dom->preserveWhiteSpace = false;
    $dom->formatOutput = true;
    
    $root = $dom->appendChild($dom->createElement("result"));
    
    while ($row = mysql_fetch_assoc($result)){
        $content = $root->appendChild($dom->createElement("Route"));
        $content->appendChild($dom->createElement("RouteId", $row['route_id']));
        $content->appendChild($dom->createElement("Priority", $row['priority']));
        $content->appendChild($dom->createElement("StayTimeSec", $row['stay_time_sec']));
        $content->appendChild($dom->createElement("StartTime", $row['start_time']));
        $content->appendChild($dom->createElement("EndTime", $row['end_time']));
        $content->appendChild($dom->createElement("StartTimeSec", $row['start_time_sec']));
        $content->appendChild($dom->createElement("EndTimeSec", $row['end_time_sec']));
        $content->appendChild($dom->createElement("Duration", $row['duration']));
        $content->appendChild($dom->createElement("Distance", $row['distance']));
        $content->appendChild($dom->createElement("DurationSec", $row['duration_sec']));
        $content->appendChild($dom->createElement("SpotId", $row['spot_id']));
        $content->appendChild($dom->createElement("SpotName", $row['spot_name']));
        $content->appendChild($dom->createElement("SpotLat", $row['lat']));
        $content->appendChild($dom->createElement("SpotLng", $row['lng']));
        $content->appendChild($dom->createElement("Kind", $row['kind']));
    }
    
    
    $content = $dom->saveXML();
    header("Content-Type: text/xml; charset=utf-8");
    echo $content;
?>
