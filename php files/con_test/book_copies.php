<?php
$response = array();

require_once __DIR__ . '/db_connect.php';
$db = new DB_CONNECT();

if (isset($_GET["isbn"]))
    $isbn= $_GET['isbn'];
else
	$isbn = '978-0-521-19395-5';
    $result = mysql_query("Select * from (SELECT * FROM book_copies WHERE isbn = '$isbn') as a left join (SELECT acc_no,COUNT(*) as ct FROM reserve_book group by acc_no) as b using (acc_no);");
 
    if (!empty($result)) {
		
        if (mysql_num_rows($result) > 0) {
 
			
			$response["success"] = 1;
			
            $response["book"] = array();
 
			while ($row = mysql_fetch_array($result)) {
				$book = array();
				$book["acc_no"] = $row["acc_no"];
				$book["status"] = $row["status"];
				$book["item_type"] = $row["item_type"];
				$book["count"] = $row["ct"];
				/*
				$acc_no = $book["acc_no"];
				echo $acc_no;
				
				$count = mysql_query("SELECT COUNT(*) FROM reserve_book WHERE acc_no = '$acc_no'");
				$book["count"] = $count;
				
				*/

				array_push($response["book"], $book);
			}
			
			
			
			
			echo json_encode($response);
        } else {
            $response["success"] = 0;
            $response["message"] = "No book found";

            echo json_encode($response);
        }
    } else {
        $response["success"] = 0;
        $response["message"] = "No book found";

        echo json_encode($response);
    }

?>