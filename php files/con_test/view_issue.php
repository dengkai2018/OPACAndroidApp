<?php
$response = array();

require_once __DIR__ . '/db_connect.php';
$db = new DB_CONNECT();

if (isset($_GET["user_id"])) 
    $user_id = $_GET['user_id'];
else $user_id = 'y11uc156';
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
        $response["succe_issue"] = 0;
        $response["msg_issue"] = "No books issued";

    }
	    echo json_encode($response);

?>