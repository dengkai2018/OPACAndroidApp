<?php
$response = array();

require_once __DIR__ . '/db_connect.php';
$db = new DB_CONNECT();

if (isset($_GET["user_id"]))
	$user_id = $_GET['user_id'];
else
	$user_id = 'a';
    $result = mysql_query("SELECT * FROM member WHERE name like '%$user_id%' or user_id = '$user_id'");
 
    if (!empty($result)) {
	
        if (mysql_num_rows($result) > 0) {
 
				$response["member"] = array();
 
				while ($row = mysql_fetch_array($result)) {
 

					$member = array();
					$member["name"] = $row["name"];
					$member["user_id"] = $row["user_id"];
					$member["group_name"] = $row["group_name"];
			
 
					array_push($response["member"], $member);
				}
				$response["success"] = 1;
			
        } else {
            $response["success"] = 0;
            $response["message"] = "No member found";

        }
    } else {
        $response["success"] = 0;
        $response["message"] = "No member found";

    }
	echo json_encode($response);
	


?>
