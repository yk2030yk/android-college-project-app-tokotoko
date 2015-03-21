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

    $result = mysql_query('select * from favorite_hotel;');
    if (!$result) {
        die('クエリーが失敗しました。'.mysql_error());
    }

    
    $dom = new DOMDocument('1.0', 'UTF-8');
    $dom->preserveWhiteSpace = false;
    $dom->formatOutput = true;
    
    $root = $dom->appendChild($dom->createElement("result"));
    
    while ($row = mysql_fetch_assoc($result)){
        $content = $root->appendChild($dom->createElement("FavoriteHotel"));
        $content->appendChild($dom->createElement("FavoriteHotelId", $row['favorite_hotel_id']));
        $content->appendChild($dom->createElement("HotelId", $row['hotel_id']));
        $content->appendChild($dom->createElement("Name", $row['name']));
        $content->appendChild($dom->createElement("Lat", $row['lat']));
        $content->appendChild($dom->createElement("Lng", $row['lng']));
    }
    
    
    $content = $dom->saveXML();
    header("Content-Type: text/xml; charset=utf-8");
    echo $content;
?>
