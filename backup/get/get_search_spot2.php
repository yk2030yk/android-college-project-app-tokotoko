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
    
    $keyword = $_POST['SpotName'];

    if (spot_id > 0 || spot_id != null) {
        mysql_set_charset('utf8');
        $result = mysql_query("select spot.id as spot_id, spot.name as spot_name, address, exp, tel, sample_rate, opening_time, sub_category.name as cate_name, area.name as area_name, lat, lng , image from spot left join sub_category on spot.sub_cate_id = sub_category.id left join area on spot.area_id = area.id where spot.name like '%".$keyword."%';");
        if (!$result) {
            die('クエリーが失敗しました。'.mysql_error());
        }
        
        $dom = new DOMDocument('1.0', 'UTF-8');
        $dom->preserveWhiteSpace = false;
        $dom->formatOutput = true;
    
        $root = $dom->appendChild($dom->createElement("result"));
    
        while ($row = mysql_fetch_assoc($result)){
            $content = $root->appendChild($dom->createElement("Spot"));
            $content->appendChild($dom->createElement("SpotId", $row["spot_id"]));
            $content->appendChild($dom->createElement("Name", $row["spot_name"]));
            $content->appendChild($dom->createElement("Exp", $row["exp"]));
            $content->appendChild($dom->createElement("Address", $row["address"]));
            $content->appendChild($dom->createElement("Tel", $row["tel"]));
            $content->appendChild($dom->createElement("SampleRate", $row["sample_rate"]));
            $content->appendChild($dom->createElement("OpeningHours", $row["opening_time"]));
            $content->appendChild($dom->createElement("Category", $row["cate_name"]));
            $content->appendChild($dom->createElement("Area", $row["area_name"]));
            $content->appendChild($dom->createElement("Lat", $row["lat"]));
            $content->appendChild($dom->createElement("Lng", $row["lng"]));
            $content->appendChild($dom->createElement("Image", $row["image"]));
        }
    
    
        $content = $dom->saveXML();
        header("Content-Type: text/xml; charset=utf-8");
        echo $content;
    }
?>
