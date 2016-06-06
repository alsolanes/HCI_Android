<?php

	if($_SERVER['REQUEST_METHOD']=='POST'){

		$image = $_POST['image'];

		require_once('dbConnect.php');

		$sql ="SELECT id FROM photos ORDER BY id ASC";

		$res = mysqli_query($con,$sql);

		$id = 0;

		while($row = mysqli_fetch_array($res)){
				$id = $row['id'];
		}

		$path = "uploads/$id.png";

		$actualpath = "http://keepmytown.esy.es/PhotoUpload/$path";

		$sql = "INSERT INTO photos (image) VALUES ('$actualpath')";

		if(mysqli_query($con,$sql)){
			file_put_contents($path,base64_decode($image));
			echo "Successfully Uploaded";
		} else {
			echo "Error on upload";
		}

		mysqli_close($con);
	}else{
		echo "Error";
	}
