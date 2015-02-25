<?php
$response = array();

require_once __DIR__ . '/db_connect.php';
$db = new DB_CONNECT();

if (isset($_GET["isbn"])) {
    $isbn = $_GET['isbn'];
    $result = mysql_query("SELECT *FROM book WHERE isbn = '$isbn'");
 
    if (!empty($result)) {
	
        if (mysql_num_rows($result) > 0) {
			$response["succ_book"] = 1;
 
            $response["book"] = array();
 
			$row = mysql_fetch_array($result);
			
				$book = array();
				$book["title"] = $row["title"];
				$book["author"] = $row["author"];
				$book["co_author"] = $row["co_author"];
				$book["publisher"] = $row["publisher"];
				$book["isbn"] = $row["isbn"];
				$book["call_no"] = $row["call_no"];
				$book["total_pages"] = $row["total_pages"];

            array_push($response["book"], $book);
			

        } else {
            $response["succ_book"] = 0;
            $response["msg_book"] = "No book found";

        }
    } else {
        $response["succ_book"] = 0;
        $response["msg_book"] = "No book found";

    }
	
	$result = mysql_query("SELECT AVG(r.rating) as avg_rating from social_db.rnr AS r WHERE r.isbn = '$isbn'");
	if(!empty($result)){
		$row = mysql_fetch_array($result);
		$response["succ_rating"] = 1;
		$response["rating"] = $row["avg_rating"];
	}
	else
		$response["succ_rating"] = 0;
    
    $result = mysql_query("SELECT c.comments, c.user_id, m.name FROM social_db.recommendations AS c INNER JOIN lib_db.member AS m WHERE c.isbn = '$isbn'");
 
    if (!empty($result)) {
    
        if (mysql_num_rows($result) > 0) {
            $response["succ_recomm"] = 1;
 
            $response["recomm"] = array();
 
            while ($row = mysql_fetch_array($result)) {
                $recomm = array();
                $recomm["comments"] = $row["comments"];
                $recomm["user_id"] = $row["name"];

                array_push($response["recomm"], $recomm);
            }

        } else {
            $response["succ_recomm"] = 0;
            $response["msg_recomm"] = "No recommendations found";

        }
    } else {
        $response["succ_recomm"] = 0;
        $response["msg_recomm"] = "No recommendations found";

    }
	$result = mysql_query("SELECT r.review, r.user_id, m.name FROM social_db.rnr AS r INNER JOIN lib_db.member AS m WHERE r.isbn = '$isbn'");
 
    if (!empty($result)) {
    
        if (mysql_num_rows($result) > 0) {
            $response["succ_rnr"] = 1;
 
            $response["rnr"] = array();
 
            while ($row = mysql_fetch_array($result)) {
                $rnr = array();
                $rnr["review"] = $row["review"];
                $rnr["user_id"] = $row["name"];

                array_push($response["rnr"], $rnr);
            }

        } else {
            $response["succ_rnr"] = 0;
            $response["msg_rnr"] = "No review found";

        }
    } else {
        $response["succ_rnr"] = 0;
        $response["msg_rnr"] = "No review found";

    }
    echo json_encode($response);
} else {
    $response["success"] = 0;
    $response["message"] = "Required field(s) is missing";
 
    echo json_encode($response);
}
?>