<?php
    $id = $_POST['HotelId'];
    
    $link = mysql_connect('****', 'name', 'pass');
    if (!$link) {
        die('接続失敗です。'.mysql_error());
    }
    
    $db_selected = mysql_select_db('****', $link);
    if (!$db_selected) {
        die('データベース選択失敗です。'.mysql_error());
    }
    mysql_set_charset('utf8');
    
    $result = mysql_query('select * from favorite_hotel where hotel_id = '.$id.';');
    if (mysql_num_rows($result) == 0) {
        $res = false;
    } else {
        $res = true;
    }
    
    
    $dom = new DOMDocument('1.0', 'UTF-8');
    $dom->preserveWhiteSpace = false;
    $dom->formatOutput = true;
    
    if ($res) {
        $root = $dom->appendChild($dom->createElement("results"));
        $content = $root->appendChild($dom->createElement("result", "success"));
    } else {
        $root = $dom->appendChild($dom->createElement("results"));
        $content = $root->appendChild($dom->createElement("result", "failure"));
    }
    
    $content = $dom->saveXML();
    header("Content-Type: text/xml; charset=utf-8");
    echo $content;
?>
