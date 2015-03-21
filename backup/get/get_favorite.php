<?php
    
    $link = mysql_connect('****', 'name', 'pass');
    if (!$link) {
        die('接続失敗です。'.mysql_error());
    }
    
    $db_selected = mysql_select_db('****', $link);
    if (!$db_selected) {
        die('データベース選択失敗です。'.mysql_error());
    }
    mysql_set_charset('utf8');
    
    $result = mysql_query('select favorite.id as id, favorite.spot_id as sid, spot.name as sname, spot.exp as exp, spot.lat as lat, spot.lng as lng , spot.image as image from favorite left join spot on favorite.spot_id=spot.id;');
    if (!$result) {
        die('クエリーが失敗しました。'.mysql_error());
    }

    
    $dom = new DOMDocument('1.0', 'UTF-8');
    $dom->preserveWhiteSpace = false;
    $dom->formatOutput = true;
    
    $root = $dom->appendChild($dom->createElement("result"));
    
    while ($row = mysql_fetch_assoc($result)){
        $content = $root->appendChild($dom->createElement("spot"));
        $content->appendChild($dom->createElement("favoriteId", $row['id']));
        $content->appendChild($dom->createElement("id", $row['sid']));
        $content->appendChild($dom->createElement("name", $row['sname']));
        $content->appendChild($dom->createElement("exp", $row['exp']));
        
        $content->appendChild($dom->createElement("lat", $row['lat']));
        $content->appendChild($dom->createElement("lng", $row['lng']));
$content->appendChild($dom->createElement("image", $row['image']));
    }
    
    
    $content = $dom->saveXML();
    header("Content-Type: text/xml; charset=utf-8");
    echo $content;
?>
