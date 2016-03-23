<?php
 class DBConnect{
     
     public $conn;

     function __construct()
     {
     }

     function connect() {
         include_once dirname(__FILE__) . '/config.php';

         // Connecting to mysql database
         $this->conn = new mysqli(DB_HOST, USERNAME, PASSWORD, DB_NAME);

         // Check for database connection error
         if (mysqli_connect_errno()) {
             echo "Failed to connect to MySQL: " . mysqli_connect_error();
         }

         // returing connection resource
         return $this->conn;
     }

     /**
      * Checking for duplicate user by email address
      * @param String $email email to check in db
      * @return boolean
      */
     function isUserExists($mysqli,$client_id) {
     $stmt = $mysqli->prepare("SELECT client_id from client WHERE client_id = ?");
     $stmt->bind_param("s", $client_id);
     $stmt->execute();
     $stmt->store_result();
     $num_rows = $stmt->num_rows;
     $stmt->close();
     return $num_rows > 0;
 }
     
     
     // updating user GCM registration ID
     public function updateGcmID($user_id, $gcm_registration_id) {
         $response = array();
         $stmt = $this->conn->prepare("UPDATE users SET gcm_registration_id = ? WHERE user_id = ?");
         $stmt->bind_param("si", $gcm_registration_id, $user_id);

         if ($stmt->execute()) {
             // User successfully updated
             $response["error"] = false;
             $response["message"] = 'GCM registration ID updated successfully';
         } else {
             // Failed to update user
             $response["error"] = true;
             $response["message"] = "Failed to update GCM registration ID";
             $stmt->error;
         }
         $stmt->close();

         return $response;
     }
 }