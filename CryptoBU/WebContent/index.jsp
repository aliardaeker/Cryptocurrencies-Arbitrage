<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html lang="en">
<head>
	<meta http-equiv="Content-Type" content="text/html;charset=UTF-8">
	<title>CryptoBU</title>
	<link rel="stylesheet" type="text/css" href="style.css">
	
	<meta charset="utf-8"/>
 <script src="https://cdnjs.cloudflare.com/ajax/libs/Chart.js/2.5.0/Chart.min.js"></script>
 <script src="js/angular.min.js"></script>
</head>
<style>
body {
    background-color: #FFFFFF;
   
}
</style>
	<div id="main">
	<div class="container">
<div id="header">
				<div id="logo">
					<h1>Logo</h1>
				</div>
			  	<div id="tagline">
			  		
					<h3 style="text-align:left"><p><font face="Brush Script MT"><font color="black"><font size="30">CryptoBU </font></font></font></p></h3>
					 </div>  

				<div style="clear:both"></div>

			
				<div style="clear:both"></div>

			</div><!-- end header -->

		

			
		  	<div id="content">
<style>		  	
#marketdata {
    font-family: "Trebuchet MS", Arial, Helvetica, sans-serif;
    border-collapse: collapse;
    width: 100%;
    
}

#marketdata td, #marketdata th {
    border: 1px solid black;
    padding: 8px;
}

#marketdata tr:nth-child(even){background-color: #f2f2f2;}

#marketdata tr:hover {background-color: #ddd;}

#marketdata th {
    padding-top: 12px;
    padding-bottom: 12px;
    text-align: left;
    background-color: #009900;
    color: white;
}
</style>
</head>
<body>
 

<div ng-app="myApp" ng-controller="myCtrl"> 

<h1><b>CURRENCY DATA</b></h1>


<p><strong>Search: </strong> <input type="text" ng-model="search">
&nbsp;&nbsp;
<strong>Sort Price :</strong>
<select ng-model="sort">
<option value="none">None</option>
<option value="avg_price">Low to High</option>
<option value="-avg_price">High to Low</option>
</select>
</p>
<br><br>
<table border="5" cellpadding="5" cellspacing="5" id="marketdata">
<thead>
<th>Currency Name</th>
<th>Price</th>
</thead>
<tbody>
<tr ng-repeat="x in currency | filter: search | orderBy: sort">
<td><a href="cryptoType?name={{x.name}}">{{x.name}}</a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
<td>{{x.price}}$&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
</tr>
</tbody>

</table>




</div>



<script>
var app = angular.module('myApp', []);
app.controller('myCtrl', function($scope, $http) {
  $http.get("servletController")
  .then(function(response) {
      $scope.currency = response.data;
  });
 $scope.sort="none"
});
</script>

			</div> 
			<!-- end content -->

	</div>
       

			
			<div style="clear:both"></div>

		</div> <!-- end main container -->
		

	</div><!-- end main -->		

	<div id="footer">
		<div class="container">
			<p>CryptoBU 2017 <br>
				
			</p>
			
		</div>
	</div><!-- end footer -->


	
</body>
</html>