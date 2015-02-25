<?php
$response = array();

require_once __DIR__ . '/db_connect.php';
$db = new DB_CONNECT();

if (isset($_GET["key"]) && isset($_GET["column"])) {
    $key = $_GET['key'];
	$column = $_GET['column'];
    $result = mysql_query("SELECT * FROM book WHERE $column LIKE '%$key%'");
 
    if (!empty($result)) {
	
        if (mysql_num_rows($result) > 0) {
 
            $response["book"] = array();
 
			while ($row = mysql_fetch_array($result)) {
				$book = array();
				$book["title"] = $row["title"];
				$book["author"] = $row["author"];
				$book["publisher"] = $row["publisher"];
				$book["isbn"] = $row["isbn"];

				array_push($response["book"], $book);
			}
			$response["success"] = 1;

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
} else {
    $response["success"] = 0;
    $response["message"] = "Required field(s) is missing";
 
    echo json_encode($response);
}
?>