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

    $result = mysql_query('select * from gcmid;');
    if (!$result) {
        die('クエリーが失敗しました。'.mysql_error());
    }

    $dom = new DOMDocument('1.0', 'UTF-8');
    $dom->preserveWhiteSpace = false;
    $dom->formatOutput = true;
    
    $root = $dom->appendChild($dom->createElement("result"));
    
    while ($row = mysql_fetch_assoc($result)){
        $content = $root->appendChild($dom->createElement("member"));
        $content->appendChild($dom->createElement("regi_id", $row['regiid']));
        $content->appendChild($dom->createElement("my_id", $row['my_id']));
        $content->appendChild($dom->createElement("name", $row['name']));
        $content->appendChild($dom->createElement("image", $row['image']));
    }
    
    
    $content = $dom->saveXML();
    header("Content-Type: text/xml; charset=utf-8");
    echo $content;
?>
