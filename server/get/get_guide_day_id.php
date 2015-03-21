<?php
    $guide_id = $_POST['GuideId'];
    
    $link = mysql_connect('****', 'name', 'pass');
    if (!$link) {
        die('接続失敗です。'.mysql_error());
    }
    
    $db_selected = mysql_select_db('****', $link);
    if (!$db_selected) {
        die('データベース選択失敗です。'.mysql_error());
    }
    mysql_set_charset('utf8');
    
    $result = mysql_query('select guide_day_id, day from guide left join guide_day on guide.guide_id = guide_day.guide_id where guide.guide_id = '.$guide_id.' order by day asc;');
    if (!$result) {
        die('クエリーが失敗しました。'.mysql_error());
    }

    
    $dom = new DOMDocument('1.0', 'UTF-8');
    $dom->preserveWhiteSpace = false;
    $dom->formatOutput = true;
    
    $root = $dom->appendChild($dom->createElement("result"));
    
    while ($row = mysql_fetch_assoc($result)){
        $content = $root->appendChild($dom->createElement("GuideDay"));
        $content->appendChild($dom->createElement("GuideDayId", $row['guide_day_id']));
        $content->appendChild($dom->createElement("Day", $row['day']));
    }
    
    
    $content = $dom->saveXML();
    header("Content-Type: text/xml; charset=utf-8");
    echo $content;
?>
