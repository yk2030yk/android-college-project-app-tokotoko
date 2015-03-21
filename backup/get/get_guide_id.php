<?php
    $gid = $_POST['group_id'];
    
    $link = mysql_connect('****', 'name', 'pass');
    if (!$link) {
        die('接続失敗です。'.mysql_error());
    }
    
    $db_selected = mysql_select_db('****', $link);
    if (!$db_selected) {
        die('データベース選択失敗です。'.mysql_error());
    }
    mysql_set_charset('utf8');

    $result = mysql_query('select * from guide;');
    
    $dom = new DOMDocument('1.0', 'UTF-8');
    $dom->preserveWhiteSpace = false;
    $dom->formatOutput = true;
    
    $root = $dom->appendChild($dom->createElement("result"));
    
    while ($row = mysql_fetch_assoc($result)){
        $content = $root->appendChild($dom->createElement("Guide"));
        $content->appendChild($dom->createElement("GuideId", $row['guide_id']));
        $content->appendChild($dom->createElement("GuideName", $row['name']));
        
        $dayResult = mysql_query("select day from guide_day where guide_id = '".$row['guide_id']."' order by day asc;");
        $sday = null;
        $eday = null;
        while ($row2 = mysql_fetch_assoc($dayResult)){
            if ($sday == null) {
                $sday = $row2["day"];
            }
            $eday = $row2["day"];
        }
        $content->appendChild($dom->createElement("HotelId", $row['hotel_id']));
        $content->appendChild($dom->createElement("StartDay", $sday));
        $content->appendChild($dom->createElement("EndDay", $eday));
        $content->appendChild($dom->createElement("Season", $row['option_id']));
        
    }
    
    
    $content = $dom->saveXML();
    header("Content-Type: text/xml; charset=utf-8");
    echo $content;
?>
