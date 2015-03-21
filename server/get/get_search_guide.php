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
    
    $keywords = $_POST['keyword'];
    
    if ($keywords != null) {
        
        $sql = "";
        $main = "select guide.guide_id, guide.name, guide.option_id from guide  left join ( select guide_id, spot_name from guide_day left join ( select route.guide_day_id, spot_name from route  left join ( select spot.id as spot_id, spot.name as spot_name from spot ) as spot on route.spot_id = spot.spot_id ) as route on guide_day.guide_day_id = route.guide_day_id ) as guide_day on guide.guide_id = guide_day.guide_id ";
        $option = "group by guide_id;";
        
        foreach ($keywords as $index=>$keyword) {
            if ($index == 0) {
                $sql .= $main."where spot_name like '%".$keyword."%' ";
            } else {
                $sql .= "or spot_name like '%".$keyword."%' ";
            }
        }
        $sql .= $option;
        
        $result = mysql_query($sql);
        
        if (!$result) {
            die('クエリーが失敗しました。'.mysql_error());
        }
        
        $dom = new DOMDocument('1.0', 'UTF-8');
        $dom->preserveWhiteSpace = false;
        $dom->formatOutput = true;
    
        $root = $dom->appendChild($dom->createElement("result"));
    
        while ($row = mysql_fetch_assoc($result)){
            $content = $root->appendChild($dom->createElement("Guide"));
            $content->appendChild($dom->createElement("GuideId", $row['guide_id']));
            $content->appendChild($dom->createElement("GuideName", $row['name']));
            $content->appendChild($dom->createElement("Season", $row['option_id']));
        }
    
    
        $content = $dom->saveXML();
        header("Content-Type: text/xml; charset=utf-8");
        echo $content;
    }
?>
