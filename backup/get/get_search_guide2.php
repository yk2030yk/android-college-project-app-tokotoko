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
    $season = $_POST['Season'];
    $stay = $_POST['StayCount'];
    
    $sql = "";
    $sqlMain = "select guide.guide_id, guide.name, guide.option_id from guide where guide_id = any(select guide_day.guide_id from guide_day where guide_day_id =  any(select route.guide_day_id from route where route.spot_id = any(select spot.id as spot_id from spot ";
    $sqlMain2 = "select guide.guide_id, guide.name, guide.option_id from guide where 1=1 ";
    $sqlClose = "))) ";
    $keyOption = "";
    $seasonOption = "";
    $stayOption = "";
    $defaultOption = "group by guide_id;";
    
    if ($keywords != null) {
        foreach ($keywords as $index=>$keyword) {
            if ($index == 0) {
                $keyOption = "where spot.name like '%".$keyword."%' ";
            } else {
                $keyOption .= "or spot.name like '%".$keyword."%' ";
            }
        }
    }
    
    if ($season != null) {
        $seasonOption = "and option_id = ".$season." ";
    }
        
    if($stay != null) {
        if ($stay < 5) {
            $stayOption = "and stay_count = ".$stay." ";
        } else {
            $stayOption = "and stay_count >= ".$stay." ";
        }
    }
    
    if ($keywords != null) {
        $sql = $sqlMain.$keyOption.$sqlClose.$seasonOption.$stayOption.$defaultOption;
    } else {
        $sql = $sqlMain2.$seasonOption.$stayOption.$defaultOption;
    }
    $result = mysql_query($sql);
    //print"</br>".$sql;
        
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
?>
