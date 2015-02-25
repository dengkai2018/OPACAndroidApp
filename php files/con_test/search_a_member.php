<?php
$response = array();

require_once __DIR__ . '/db_connect.php';
$db = new DB_CONNECT();
if (isset($_POST["user_id"]))
    $user_id = $_POST['user_id'];
else
	$user_id = 'y11uc156';
	
    $result = mysql_query("SELECT * FROM member WHERE user_id = '$user_id'");
 
    if (!empty($result)) {
	
        if (mysql_num_rows($result) > 0) {
 
				$response["member"] = array();
 
				$result = mysql_fetch_array($result);

				$member = array();
				$member["name"] = $result["name"];
				$member["user_id"] = $result["user_id"];
				$member["group_name"] = $result["group_name"];
				$member["dept"] = $result["dept"];
			
				$response["succ_mem"] = 1;
 
				array_push($response["member"], $member);
				$response["succ_mem"] = 1;
			
        } else {
            $response["succ_mem"] = 0;
            $response["msg_mem"] = "No member found";

        }
    } else {
        $response["succ_mem"] = 0;
        $response["msg_mem"] = "No member found";

    }
	
    $result = mysql_query("SELECT i.acc_no, i.user_id, b.call_no, b.title, b.author, i.issue_date, i.due_date, c.item_type FROM book as b INNER JOIN book_copies as c ON b.isbn = c.isbn INNER JOIN issue_details AS i ON c.acc_no = i.acc_no WHERE i.user_id = '$user_id'");
 
    if (!empty($result)) {
	
        if (mysql_num_rows($result) > 0) {
 
            $response["issue"] = array();
 
			while ($row = mysql_fetch_array($result)) {
				$issue = array();
				$issue["acc_no"] = $row["acc_no"];
				$issue["call_no"] = $row["call_no"];
				$issue["title"] = $row["title"];
				$issue["author"] = $row["author"];
				$issue["issue_date"] = $row["issue_date"];
				$issue["due_date"] = $row["due_date"];
				$issue["item_type"] = $row["item_type"];

				array_push($response["issue"], $issue);
			}
			$response["succ_issue"] = 1;

        } else {
            $response["succ_issue"] = 0;
            $response["msg_issue"] = "No books issued";

        }
    } else {
        $response["succ_issue"] = 0;
        $response["msg_issue"] = "No books issued";

    }   
	$result = mysql_query("SELECT c.comments, b.title FROM social_db.recommendations AS c INNER JOIN lib_db.book AS b WHERE c.isbn = b.isbn and c.user_id = '$user_id'");
 
    if (!empty($result)) {
    
        if (mysql_num_rows($result) > 0) {
            $response["succ_recomm"] = 1;
 
            $response["recomm"] = array();
 
            while ($row = mysql_fetch_array($result)) {
                $recomm = array();
                $recomm["comments"] = $row["comments"];
                $recomm["title"] = $row["title"];

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
	$result = mysql_query("SELECT r.review, b.title FROM social_db.rnr AS r INNER JOIN lib_db.book AS b WHERE r.isbn = b.isbn and r.user_id = '$user_id'");
 
    if (!empty($result)) {
    
        if (mysql_num_rows($result) > 0) {
            $response["succ_rnr"] = 1;
 
            $response["rnr"] = array();
 
            while ($row = mysql_fetch_array($result)) {
                $rnr = array();
                $rnr["review"] = $row["review"];
                $rnr["title"] = $row["title"];

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


?>