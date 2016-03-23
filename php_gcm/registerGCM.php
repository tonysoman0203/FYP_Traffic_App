<?php
include_once "./Config.php";
include_once "./DBConnect.php";
include_once "sendGCM.php";

$mysqli = new mysqli(DB_HOST,USERNAME,PASSWORD,DB_NAME);
if (!$mysqli->connect_errno){
    echo "Connect to MYSQL Server";
}else{
    echo $mysqli->connect_error;
}
$client_id = $_POST["client_id"];
$db = new DBConnect();

if ($client_id !=null ){
    if (!$db->isUserExists($mysqli,$client_id)) {
        $query = "INSERT INTO client(client_id) VALUES('$client_id')";
        echo $query;
        if ($mysqli->query($query)) {
            echo "Register GCM complete";
            $gcm = new GCM();
            //$arr = array("Result Code"=>"1","Message"=>"Register GCM success with client id="+$client_id);
            //echo json_encode($arr);
            $registration_ids = array($client_id);
            $message = array("title" =>"Traffic News ","success" => "register GCM success");
            $result = $gcm->send($client_id, $message);
            echo $result;
        } else {
            echo $mysqli->error;
        }
    }else{
        $query = "INSERT INTO client(client_id) VALUES('$client_id')";
        if ($mysqli->query($query)) {
            echo "Register GCM complete";
            $gcm = new GCM();
            //$arr = array("Result Code"=>"1","Message"=>"Register GCM success with client id="+$client_id);
            //echo json_encode($arr);
            $registration_ids = array($client_id);
            $message = array("title" =>"Traffic News ","success" => "register GCM success");
            $result = $gcm->send($client_id, $message);
            echo $result;
        } else {
            echo $mysqli->error;
        }
    }
}

?>      