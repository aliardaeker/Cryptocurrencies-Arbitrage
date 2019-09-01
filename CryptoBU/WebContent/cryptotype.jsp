<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html lang="en">
<head>
	<meta http-equiv="Content-Type" content="text/html;charset=UTF-8">
	<title>CryptoBU</title>
	<link rel="stylesheet" type="text/css" href="style.css">
	
	<meta charset="utf-8"/>
 <script src="js/Chart.js"></script>
 <script src="js/angular.min.js"></script>
 <script src=js/jquery.js></script>
</head>
<style>
body {
    background-color: #ffffff;
   
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
    width: 90%;
    
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

<%
String name=session.getAttribute("name").toString();
 %>
<h1><%=name.toUpperCase() %></h1>




<p><strong>Search: </strong> <input type="text" ng-model="search">

<br><br>
<strong>Sort Price :</strong>
<select ng-model="sort">
<option value="none">None</option>
<option value="price">Low to High</option>
<option value="-price">High to Low</option>
</select>


&nbsp;<strong>Filter Pair :</strong>
<select ng-model="pairFilter">
<option value="">None</option>
<option ng-repeat="x in currencyData | unique : 'pair'" value="{{x.pair}}">{{x.pair}}</option>
</select>
</p>
<br>
<table border="2" cellpadding="2" cellspacing="1" id="marketdata">
<thead>
<th>Market Place</th>
<th>Pair</th>
<th>Price</th>
</thead>
<tbody>
<tr ng-repeat="x in currencyData | filter: search | orderBy: sort | filter:pairFilter ">
<td>{{x.market_place}}&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
<td>{{x.pair}}&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
<td>{{x.price}}&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
</tr>
</tbody>

</table>




</div>



<script>
var app = angular.module('myApp', []);
app.controller('myCtrl', function($scope, $http) {
  $http.get("servletControllerType")
  .then(function(response) {
      $scope.currencyData = response.data;
  });
 $scope.sort="none";
$scope.pairFilter="";
});
app.filter('unique', function() {
	   return function(collection, keyname) {
	      var output = [], 
	          keys = [];

	      angular.forEach(collection, function(item) {
	          var key = item[keyname];
	          if(keys.indexOf(key) === -1) {
	              keys.push(key);
	              output.push(item);
	          }
	      });

	      return output;
	   };
	});
</script>
			</div> 
			<!-- end content -->

	</div>
        <div id="RightPanel" style="width: 200; height: 100%; text-align: left; vertical-align: bottom; float: left;">
        
           <div id="chartContainer" style="height: 400px; width: 700px;"> 
           
          <div >
          <br>
         
    <canvas id="bar-chart" style="height:100px;width: content-box"></canvas>
</div>
			
            </div>
        </div>

			
			<div style="clear:both"></div>

		</div> <!-- end main container -->
		

	</div><!-- end main -->		

	<div id="footer">
		<div class="container">
			<p>CryptoBU 2017<br>
				
			</p>
			
		</div>
	</div><!-- end footer -->
<script>
var labels1=[];
var backgroundColor1=[];
var data1=[];
var xhr = new XMLHttpRequest();
xhr.open('GET', 'chartDataServlet');
xhr.onload = function() {
    if (xhr.status === 200) {
      //  alert('User\'s name is ' + xhr.responseText);
    	var records = JSON.parse(xhr.responseText);
       
        for(var i = 0; i<records.length; i++){
            
            labels1.push(records[i].pair);
            backgroundColor1.push(records[i].color);
            data1.push(records[i].percent);
        }
        //alert(labels1);
        //alert(labels1);
        new Chart(document.getElementById("bar-chart"), {
            type: 'bar',
            data: {
              labels: labels1,
              datasets: [
                {
                  label: "Profit %",
                  backgroundColor: backgroundColor1,
                  data: data1
                }
              ]
            },
            options: {
              legend: { display: false },
              title: {
                display: true,
                text: 'Profit % for different pairs'
              }

            }
        });
    }
    else {
        alert('Request failed.  Returned status of ' + xhr.status);
    }
};
xhr.send();


</script>

	
</body>
</html>