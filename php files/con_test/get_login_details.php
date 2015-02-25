<?php
 
/*
 * Following code will list all the products
 */
 
// array for JSON response
$response = array();
 
 if(isset($_POST['username']) && isset($_POST['password']) ){
	$username = $_POST['username'];
	$password = $_POST['password'];
	require_once __DIR__ . '/db_connect.php';
	$db = new DB_CONNECT();
	$result = mysql_query("Select * from social_db.login where user_id = '$username' and password = '$password'") or die(mysql_error());
	if(mysql_num_rows($result)>0){
		$response["success"]=1;
		echo json_encode($response);
	} else{
		$response["success"]=0;
		$response["message"]="Wrong Credentials";
		echo json_encode($response);
	}
 }
 
?>