<%@ page language="java" import="java.util.*" contentType="text/html; charset=utf-8" %>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html>
<html lang = "en">
<head>
<meta charset="UTF-8">
<title>板块</title>
<link rel="stylesheet" href="https://cdn.bootcss.com/bootstrap/3.3.7/css/bootstrap.min.css">
<link rel="stylesheet" href="https://cdn.bootcss.com/bootstrap/3.3.7/css/bootstrap-theme.min.css">
<script src="https://cdn.bootcss.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
<link rel="stylesheet" type="text/css" href = "css/index.css"> 
<link rel="stylesheet" type="text/css" href = "css/Plate.css">
<link rel="stylesheet"
	href="//apps.bdimg.com/libs/jqueryui/1.10.4/css/jquery-ui.min.css">
<script src="//apps.bdimg.com/libs/jquery/1.10.2/jquery.min.js"></script>
<script src="//apps.bdimg.com/libs/jqueryui/1.10.4/jquery-ui.min.js"></script>
</head>
<body>
	<nav id="navbar">
		<div>
			<div>
				<ul id="navul">
					<li><a><img src="images/Icon.jpg"></a></li>
					<li><a href="Main.jsp">首页</a></li>
					<li><a href="Market.jsp">行情中心</a></li>
					<li><a href="Stock.jsp">个股展示</a></li>
					<li><a href="StockVS.jsp">股票比较</a></li>
					<li><a href="Strategy.jsp">策略回测</a></li>
					<li><a href="Plate.jsp">板块</a>	</li>
				</ul>
			</div>
			<ul id="userul">
				<li class="firstli"><img src="images/photo.png"></li>
				<li class="firstli"><a href="javascript:void(0)">${sessionScope.loginUserName}</a>
					<ul class="drop">
						<li><a href="User.jsp">个人中心</a></li>
						<li><a href="Main.jsp">退出登录</a></li>
					</ul>
				</li>
			</ul>
		</div>
	</nav>
	
	<div class="divinput">
		<input type="text" placeholder="输入股票代码/名称" id="code"> 
		<a href="Stock.jsp" target="_blank" id="searchcode"><button type="submit" class="search">搜索</button></a>
		<script type="text/javascript">
		$("#searchcode").click(function(){ 
			$.ajax({ 
				type : "POST",
				url : "SaveSearch",
				data: {
					code: $("#code").val()
				},
				dataType : "json",
				success : function(obj) {
					
				}
			});
		});
		</script>
	</div>
	
	<div class="w1200">
		<div class="head">
			<h2>概念板块</h2>
		</div>
		<div class="prelist">
			<table class="table table-hover table-striped">
				<thead>
					<tr>
						<th style="text-align:center;">序号</th>
						<th style="text-align:center;">板块</th>
						<th style="text-align:center;">板块涨跌幅(%)</th>
						<th style="text-align:center;">总成交量(万手)</th>
						<th style="text-align:center;">上涨家数</th>
						<th style="text-align:center;">下跌家数</th>
						<th style="text-align:center;">均价</th>
						<th style="text-align:center;">领涨股</th>
						<th style="text-align:center;">最新价</th>
						<th style="text-align:center;">涨跌幅(%)</th>
					</tr>
				</thead>
				<tbody id="concept-plate-list">
				</tbody>
			</table>
		</div>
		<div class="head">
			<h2>行业板块</h2>
		</div>
		<div class="prelist">
			<table class="table table-hover table-striped">
				<thead>
					<tr>
						<th style="text-align:center;">序号</th>
						<th style="text-align:center;">板块</th>
						<th style="text-align:center;">板块涨跌幅(%)</th>
						<th style="text-align:center;">总成交量(万手)</th>
						<th style="text-align:center;">上涨家数</th>
						<th style="text-align:center;">下跌家数</th>
						<th style="text-align:center;">均价</th>
						<th style="text-align:center;">领涨股</th>
						<th style="text-align:center;">最新价</th>
						<th style="text-align:center;">涨跌幅(%)</th>
					</tr>
				</thead>
				<tbody id="industry-plate-list">
				</tbody>
			</table>
		</div>
		<div class="head">
			<h2>地域板块</h2>
		</div>
		<div class="prelist">
			<table class="table table-hover table-striped">
				<thead>
					<tr>
						<th style="text-align:center;">序号</th>
						<th style="text-align:center;">板块</th>
						<th style="text-align:center;">板块涨跌幅(%)</th>
						<th style="text-align:center;">总成交量(万手)</th>
						<th style="text-align:center;">上涨家数</th>
						<th style="text-align:center;">下跌家数</th>
						<th style="text-align:center;">均价</th>
						<th style="text-align:center;">领涨股</th>
						<th style="text-align:center;">最新价</th>
						<th style="text-align:center;">涨跌幅(%)</th>
					</tr>
				</thead>
				<tbody id="area-plate-list">
				</tbody>
			</table>
		</div>
	</div>

	<script type="text/javascript">
	$(document).ready(function(){
		$.ajax({
			type : "GET",
			url : "ConceptPlate",
			dataType : "json",
			success : function(obj) {
				var resultJSONData = JSON.parse(obj);
				var index = resultJSONData.index;
				var platename = resultJSONData.name;
				var fluctuation = resultJSONData.fluctuation;
				var volume = resultJSONData.volume;
				var up_num = resultJSONData.up_num;
				var down_num = resultJSONData.down_num;
				var price = resultJSONData.price;
				var max_code = resultJSONData.max_code;
				var max_price = resultJSONData.max_price;
				var max_fluctuation = resultJSONData.max_fluctuation;
				var s = "";
				for(var i=0;i<10;i++){
					s = s+"<tr><td>"+index[i]+"</td><td>"+platename[i]+"</td><td>"+fluctuation[i]+"</td><td>"+volume[i]+
						"</td><td>"+up_num[i]+"</td><td>"+down_num[i]+"</td><td>"+price[i]+"</td><td>"+max_code[i]+
						"</td><td>"+max_price[i]+"</td><td>"+max_fluctuation[i]+"</td></tr>";
				}
				$("#concept-plate-list").html(s);
			}
		});
		$.ajax({
			type : "GET",
			url : "IndustryPlate",
			dataType : "json",
			success : function(obj) {
				var resultJSONData = JSON.parse(obj);
				var index = resultJSONData.index;
				var platename = resultJSONData.name;
				var fluctuation = resultJSONData.fluctuation;
				var volume = resultJSONData.volume;
				var up_num = resultJSONData.up_num;
				var down_num = resultJSONData.down_num;
				var price = resultJSONData.price;
				var max_code = resultJSONData.max_code;
				var max_price = resultJSONData.max_price;
				var max_fluctuation = resultJSONData.max_fluctuation;
				var s = "";
				for(var i=0;i<10;i++){
					console.log(s);
					s = s+"<tr><td>"+index[i]+"</td><td>"+platename[i]+"</td><td>"+fluctuation[i]+"</td><td>"+volume[i]+
						"</td><td>"+up_num[i]+"</td><td>"+down_num[i]+"</td><td>"+price[i]+"</td><td>"+max_code[i]+
						"</td><td>"+max_price[i]+"</td><td>"+max_fluctuation[i]+"</td></tr>";
				}
				$("#industry-plate-list").html(s);
			}
		});
		$.ajax({
			type : "GET",
			url : "AreaPlate",
			dataType : "json",
			success : function(obj) {
				var resultJSONData = JSON.parse(obj);
				var index = resultJSONData.index;
				var platename = resultJSONData.name;
				var fluctuation = resultJSONData.fluctuation;
				var volume = resultJSONData.volume;
				var up_num = resultJSONData.up_num;
				var down_num = resultJSONData.down_num;
				var price = resultJSONData.price;
				var max_code = resultJSONData.max_code;
				var max_price = resultJSONData.max_price;
				var max_fluctuation = resultJSONData.max_fluctuation;
				var s = "";
				for(var i=0;i<10;i++){
					s = s+"<tr><td>"+index[i]+"</td><td>"+platename[i]+"</td><td>"+fluctuation[i]+"</td><td>"+volume[i]+
						"</td><td>"+up_num[i]+"</td><td>"+down_num[i]+"</td><td>"+price[i]+"</td><td>"+max_code[i]+
						"</td><td>"+max_price[i]+"</td><td>"+max_fluctuation[i]+"</td></tr>";
				}
				$("#area-plate-list").html(s);
			}
		});
	});
	</script>
</body>
</html>