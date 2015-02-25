<?php

$response = array();

if (isset($_POST['user_id']) && isset($_POST['acc_no']))) {
    $user_id = $_POST['user_id'];
    $acc_no = $_POST['acc_no'];

    require_once __DIR__ . '/db_connect.php';

    $db = new DB_CONNECT();

    $result = mysql_query("INSERT INTO reserve_book(acc_no, user_id, reserve_date) VALUES('$acc_no', '$user_id', CURDATE())");

    if ($result) {
        $response["success"] = 1;
        $response["message"] = "Product successfully created.";

        echo json_encode($response);
    } else {
        $response["success"] = 0;
        $response["message"] = "Oops! An error occurred.";

        echo json_encode($response);
    }
} else {
    $response["success"] = 0;
    $response["message"] = "Required field(s) is missing";

    echo json_encode($response);
}
?>