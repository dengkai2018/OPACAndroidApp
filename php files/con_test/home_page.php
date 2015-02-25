<?php
$response = array();

require_once __DIR__ . '/db_connect.php';
$db = new DB_CONNECT();

if (isset($_POST["user_id"])) 
	$user_id = $_POST["user_id"];
else
	$user_id = 'Y11UC156';
	$result = mysql_query("SELECT i.acc_no, i.user_id, b.call_no, b.title, b.author, i.issue_date, i.due_date, c.item_type FROM book as b INNER JOIN book_copies as c ON b.isbn = c.isbn INNER JOIN issue_details AS i ON c.acc_no = i.acc_no WHERE i.user_id = '$user_id'");
 
    if (!empty($result)) {
	
        if (mysql_num_rows($result) > 0) {
			$response["succ_issue"] = 1;
 
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

        } else {
            $response["succ_issue"] = 0;
            $response["msg_issue"] = "No books issued";

        }
    } else {
        $response["succ_issue"] = 0;
        $response["msg_issue"] = "No books issued";

    }
	
	$result = mysql_query("SELECT t.acc_no, t.user_id, b.call_no, b.title, b.author, t.issue_date, t.due_date, t.return_date, c.item_type FROM book as b INNER JOIN book_copies as c ON b.isbn = c.isbn INNER JOIN transaction_details AS t ON c.acc_no = t.acc_no WHERE t.user_id = '$user_id'");
 
    if (!empty($result)) {
	
        if (mysql_num_rows($result) > 0) {
 
			$response["succ_trans"] = 1;
            $response["trans"] = array();
 
			while ($row = mysql_fetch_array($result)) {
				$trans = array();
				$trans["acc_no"] = $row["acc_no"];
				$trans["call_no"] = $row["call_no"];
				$trans["title"] = $row["title"];
				$trans["author"] = $row["author"];
				$trans["issue_date"] = $row["issue_date"];
				$trans["due_date"] = $row["due_date"];
				$trans["return_date"] = $row["return_date"];
				$trans["item_type"] = $row["item_type"];

				array_push($response["trans"], $trans);
			}

        } else {
            $response["succ_trans"] = 0;
            $response["msg_trans"] = "No transaction found";

        }
    } else {
        $response["succ_trans"] = 0;
        $response["mag_trans"] = "No transaction found";

    }
	
    echo json_encode($response);
	

?>