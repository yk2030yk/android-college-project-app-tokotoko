<?php
    $gid = $_POST['GuideId'];
    
    $link = mysql_connect('****', 'name', 'pass');
    if (!$link) {
        die('接続失敗です。'.mysql_error());
    }
    
    $db_selected = mysql_select_db('****', $link);
    if (!$db_selected) {
        die('データベース選択失敗です。'.mysql_error());
    }
    mysql_set_charset('utf8');
    
    $result = mysql_query("select day from guide_day where guide_id = '".$gid."' order by day asc;");
    $sday = null;
    $eday = null;
    while ($row = mysql_fetch_assoc($result)) {
        if ($sday == null) {
            $sday = $row["day"];
        }
        $eday = $row["day"];
    }
    
    $result = mysql_query("select * from guide where guide_id = '".$gid."';");
    
    $dom = new DOMDocument('1.0', 'UTF-8');
    $dom->preserveWhiteSpace = false;
    $dom->formatOutput = true;
    
    $root = $dom->appendChild($dom->createElement("result"));
    
    while ($row = mysql_fetch_assoc($result)){
        $content = $root->appendChild($dom->createElement("Guide"));
        $content->appendChild($dom->createElement("GuideId", $row['guide_id']));
        $content->appendChild($dom->createElement("GuideName", $row['name']));
        $content->appendChild($dom->createElement("HotelId", $row['hotel_id']));
        $content->appendChild($dom->createElement("Memo", $row['memo']));
        $content->appendChild($dom->createElement("StartDay", $sday));
        $content->appendChild($dom->createElement("EndDay", $eday));
        $content->appendChild($dom->createElement("Season", $row['option_id']));
    }
    
    
    $content = $dom->saveXML();
    header("Content-Type: text/xml; charset=utf-8");
    echo $content;
?>
