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
<script src="http://cdn.static.runoob.com/libs/jquery/2.1.1/jquery.min.js"></script>
<script src="https://cdn.bootcss.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
<link rel="stylesheet" type="text/css" href = "css/index.css"> 
<link rel="stylesheet" type="text/css" href = "css/Plate.css">
<link rel="stylesheet"
	href="//apps.bdimg.com/libs/jqueryui/1.10.4/css/jquery-ui.min.css">
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
				<li class="firstli"><img id="photo"></li>
				<li class="firstli"><a href="javascript:void(0)">${sessionScope.loginUserName}</a>
					<ul class="drop">
						<li><a href="User.jsp">个人中心</a></li>
						<li><a href="Main.jsp" onclick="logout()">退出登录</a></li>
					</ul>
				</li>
			</ul>
		</div>
	</nav>
	
	<div class="divinput">
		<div class="stock-drop-list">
			<input type="text" placeholder="输入股票代码/名称" id="code">
			<ul id="match-list">
			</ul>
		</div>
		<a href="javascript:void(0)" target="_blank" id="searchcode"><button type="button" class="search">搜索</button></a>
		
		<script type="text/javascript">
		$("#code").bind('input propertychange', function() {
			$.ajax({
				type: "POST",
				url: "getMatch",
				data: {
					enter: $("#code").val(),
				},
				dataType: "json",
				success: function(obj){
					var result = JSON.parse(obj);
					var s = "";
					for(var i=0;i<5;i++){
						if(result.name[i]!=null){
							s = s+"<li onclick=\"getStock('"+result.code[i]+"')\">"+result.code[i]+"&emsp;&emsp;"+result.name[i]+"</li>";
						}
					}
					$("#match-list").attr("style","");
					$("#match-list").html(s);
				}
			});
		});
		
		function getStock(code){
			$("#code").val(code);
			$("#match-list").attr("style","display:none;");
		}
		
		$("#searchcode").click(function(){ 
			$.ajax({ 
				type : "POST",
				url : "SaveSearch",
				data: {
					code: $("#code").val()
				},
				dataType : "json",
				success : function(obj) {
					var result = JSON.parse(obj);
					console.log(result.result);
					if(result.result=="unknow"){
						$("#search-modal-prompt").html("无效的股票代码/名称!");
						$("#search-modal").modal('show');
					}else if(result.result=="null"){
						$("#search-modal-prompt").html("搜索内容为空!");
						$("#search-modal").modal('show');
					}else{
						window.open("Stock.jsp");
					}
				}
			});
		});
		</script>
	</div>
	
	<div class="modal fade" id="search-modal" tabindex="-1" role="dialog"
		aria-labelledby="myModalLabel" aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-hidden="true">×</button>
					<h4 class="modal-title" id="myModalLabel">提示</h4>
				</div>
				<div class="modal-body">
					<label>按下 ESC 按钮退出。</label>
					<br>
					<strong id="search-modal-prompt">无效的股票代码/名称!</strong>
				</div>
				<div class="modal-footer">
					<button type="submit" class="btn btn-primary" data-dismiss="modal">确定</button>
				</div>
			</div>
		</div>
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
			<div class="m-page">
				<ul id="concept-page">
					<li><a>首页</a></li>
					<li><a>上一页</a></li>
					<li class="active"><a>1</a></li>
					<li><a>2</a></li>
					<li><a>3</a></li>
					<li><a>4</a></li>
					<li><a>5</a></li>
					<li><a>下一页</a></li>
					<li><a>尾页</a></li>
				</ul>				
			</div>
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
			<div class="m-page">
				<ul id="industry-page">
					<li><a>首页</a></li>
					<li><a>上一页</a></li>
					<li class="active"><a>1</a></li>
					<li><a>2</a></li>
					<li><a>3</a></li>
					<li><a>4</a></li>
					<li><a>5</a></li>
					<li><a>下一页</a></li>
					<li><a>尾页</a></li>
				</ul>				
			</div>
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
			<div class="m-page">
				<ul id="area-page">
					<li><a>首页</a></li>
					<li><a>上一页</a></li>
					<li class="active"><a>1</a></li>
					<li><a>2</a></li>
					<li><a>3</a></li>
					<li><a>4</a></li>
					<li><a>5</a></li>
					<li><a>下一页</a></li>
					<li><a>尾页</a></li>
				</ul>				
			</div>
		</div>
	</div>

	<script type="text/javascript">
	$(document).ready(function(){
		getConceptPlate(0);
		getIndustryPlate(0);
		getAreaPlate(0);
	});
	</script>
	
	<script type="text/javascript">
	function getConceptPlate(p){
		$.ajax({
			type : "GET",
			url : "ConceptPlate",
			data: {
				page: p,
			},
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
				var length = resultJSONData.length;
				
				var s = "";
				for(var i=0;i<10;i++){
					if(index[i]!=null){
						s = s+"<tr><td>"+index[i]+"</td><td><a href=\"PlatePre.jsp\" target=\"_blank\" onclick=\"SearchPlate('concept','"+
							platename[i]+"')\">"+platename[i]+"</a></td><td>"+fluctuation[i]+"</td><td>"+volume[i]+
							"</td><td>"+up_num[i]+"</td><td>"+down_num[i]+"</td><td>"+price[i]+
							"</td><td><a href=\"Stock.jsp\" target=\"_blank\" onclick=\"SearchStock('"+max_code[i]+"')\">"+max_code[i]+
							"</a></td><td>"+max_price[i]+"</td><td>"+max_fluctuation[i]+"</td></tr>";
					}
				}
				$("#concept-plate-list").html(s);
				
				var s1 = "<li><a onclick=\"getConceptPlate(0)\">首页</a></li>";
				var last = p-1;
				if(last<0){
					s1 = s1+"<li class=\"disable\"><a onclick=\"getConceptPlate("+last+")\">上一页</a></li>";
				}else{
					s1 = s1+"<li><a onclick=\"getConceptPlate("+last+")\">上一页</a></li>";
				}
				var page = p-p%5+1;
				if((page>(length-4))&&(length>5)){
					page = length-4;
				}
				if(length<5){
					for(var i=0;i<length;i++){
						var k = page+i;
						if((k-1)==p){
							var j = k-1;
							s1 = s1+"<li class=\"active\"><a onclick=\"getConceptPlate("+j+")\">"+k+"</a></li>";
						}else{
							var j = k-1;
							s1 = s1+"<li><a onclick=\"getConceptPlate("+j+")\">"+k+"</a></li>";
						}
					}
				}else{
					for(var i=0;i<5;i++){
						var k = page+i;
						if((k-1)==p){
							var j = k-1;
							s1 = s1+"<li class=\"active\"><a onclick=\"getConceptPlate("+j+")\">"+k+"</a></li>";
						}else{
							var j = k-1;
							s1 = s1+"<li><a onclick=\"getConceptPlate("+j+")\">"+k+"</a></li>";
						}
					}
				}
				var next = p+1;
				if(p==(length-1)){
					s1 = s1+"<li class=\"disable\"><a onclick=\"getConceptPlate("+next+")\">下一页</a></li>";
				}else{
					s1 = s1+"<li><a onclick=\"getConceptPlate("+next+")\">下一页</a></li>";
				}
				console.log(length);
				var x = length-1;
				s1 = s1+"<li><a onclick=\"getConceptPlate("+x+")\">尾页</a></li>";
				
				$("#concept-page").html(s1);
			}
		});
	}
	
	function getIndustryPlate(p){
		$.ajax({
			type : "GET",
			url : "IndustryPlate",
			data: {
				page: p,
			},
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
				var length = resultJSONData.length;
				
				var s = "";
				for(var i=0;i<10;i++){
					if(index[i]!=null){
						s = s+"<tr><td>"+index[i]+"</td><td><a href=\"PlatePre.jsp\" target=\"_blank\" onclick=\"SearchPlate('industry','"+
							platename[i]+"')\">"+platename[i]+"</a></td><td>"+fluctuation[i]+"</td><td>"+volume[i]+
							"</td><td>"+up_num[i]+"</td><td>"+down_num[i]+"</td><td>"+price[i]+
							"</td><td><a href=\"Stock.jsp\" target=\"_blank\" onclick=\"SearchStock('"+max_code[i]+"')\">"+max_code[i]+
							"</a></td><td>"+max_price[i]+"</td><td>"+max_fluctuation[i]+"</td></tr>";
					}
				}
				$("#industry-plate-list").html(s);
				
				var s1 = "<li><a onclick=\"getIndustryPlate(0)\">首页</a></li>";
				var last = p-1;
				if(last<0){
					s1 = s1+"<li class=\"disable\"><a onclick=\"getIndustryPlate("+last+")\">上一页</a></li>";
				}else{
					s1 = s1+"<li><a onclick=\"getIndustryPlate("+last+")\">上一页</a></li>";
				}
				var page = p-p%5+1;
				if((page>(length-4))&&(length>5)){
					page = length-4;
				}
				if(length<5){
					for(var i=0;i<length;i++){
						var k = page+i;
						if((k-1)==p){
							var j = k-1;
							s1 = s1+"<li class=\"active\"><a onclick=\"getIndustryPlate("+j+")\">"+k+"</a></li>";
						}else{
							var j = k-1;
							s1 = s1+"<li><a onclick=\"getIndustryPlate("+j+")\">"+k+"</a></li>";
						}
					}
				}else{
					for(var i=0;i<5;i++){
						var k = page+i;
						if((k-1)==p){
							var j = k-1;
							s1 = s1+"<li class=\"active\"><a onclick=\"getIndustryPlate("+j+")\">"+k+"</a></li>";
						}else{
							var j = k-1;
							s1 = s1+"<li><a onclick=\"getIndustryPlate("+j+")\">"+k+"</a></li>";
						}
					}
				}
				var next = p+1;
				if(p==(length-1)){
					s1 = s1+"<li class=\"disable\"><a onclick=\"getIndustryPlate("+next+")\">下一页</a></li>";
				}else{
					s1 = s1+"<li><a onclick=\"getIndustryPlate("+next+")\">下一页</a></li>";
				}
				console.log(length);
				var x = length-1;
				s1 = s1+"<li><a onclick=\"getIndustryPlate("+x+")\">尾页</a></li>";
				
				$("#industry-page").html(s1);
			}
		});
	}
	
	function getAreaPlate(p){
		$.ajax({
			type : "GET",
			url : "AreaPlate",
			data: {
				page: p,
			},
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
				var length = resultJSONData.length;
				
				var s = "";
				for(var i=0;i<10;i++){
					if(index[i]!=null){
						s = s+"<tr><td>"+index[i]+"</td><td><a href=\"PlatePre.jsp\" target=\"_blank\" onclick=\"SearchPlate('area','"+
							platename[i]+"')\">"+platename[i]+"</a></td><td>"+fluctuation[i]+"</td><td>"+volume[i]+
							"</td><td>"+up_num[i]+"</td><td>"+down_num[i]+"</td><td>"+price[i]+
							"</td><td><a href=\"Stock.jsp\" target=\"_blank\" onclick=\"SearchStock('"+max_code[i]+"')\">"+max_code[i]+
							"</a></td><td>"+max_price[i]+"</td><td>"+max_fluctuation[i]+"</td></tr>";
					}
				}
				$("#area-plate-list").html(s);
				
				var s1 = "<li><a onclick=\"getAreaPlate(0)\">首页</a></li>";
				var last = p-1;
				if(last<0){
					s1 = s1+"<li class=\"disable\"><a onclick=\"getAreaPlate("+last+")\">上一页</a></li>";
				}else{
					s1 = s1+"<li><a onclick=\"getAreaPlate("+last+")\">上一页</a></li>";
				}
				var page = p-p%5+1;
				if((page>(length-4))&&(length>5)){
					page = length-4;
				}
				if(length<5){
					for(var i=0;i<length;i++){
						var k = page+i;
						if((k-1)==p){
							var j = k-1;
							s1 = s1+"<li class=\"active\"><a onclick=\"getAreaPlate("+j+")\">"+k+"</a></li>";
						}else{
							var j = k-1;
							s1 = s1+"<li><a onclick=\"getAreaPlate("+j+")\">"+k+"</a></li>";
						}
					}
				}else{
					for(var i=0;i<5;i++){
						var k = page+i;
						if((k-1)==p){
							var j = k-1;
							s1 = s1+"<li class=\"active\"><a onclick=\"getAreaPlate("+j+")\">"+k+"</a></li>";
						}else{
							var j = k-1;
							s1 = s1+"<li><a onclick=\"getAreaPlate("+j+")\">"+k+"</a></li>";
						}
					}
				}
				var next = p+1;
				if(p==(length-1)){
					s1 = s1+"<li class=\"disable\"><a onclick=\"getAreaPlate("+next+")\">下一页</a></li>";
				}else{
					s1 = s1+"<li><a onclick=\"getAreaPlate("+next+")\">下一页</a></li>";
				}
				console.log(length);
				var x = length-1;
				s1 = s1+"<li><a onclick=\"getAreaPlate("+x+")\">尾页</a></li>";
				
				$("#area-page").html(s1);
			}
		});
	}
	
	function SearchStock(c){
		$.ajax({ 
			type : "POST",
			url : "SaveSearch",
			data: {
				code: c,
			},
			dataType : "json",
			success : function(obj) {
				
			}
		});
		
	}
	
	function SearchPlate(pt,p){
		$.ajax({ 
			type : "POST",
			url : "SavePlate",
			data: {
				type: pt,
				plate: p,
			},
			dataType : "json",
			success : function(obj) {
				
			}
		});
	}
	</script>
	
	<script>
	function logout(){
		$.ajax({
			type : "GET",
			url : "Logout",
			dataType : "json",
			success: function(obj) {
				
			}
		});
	}
	
	$(document).ready(function(){
		$.ajax({
			Type: "POST",
			url: "getPhoto",
			dataType: "json",
			success: function(obj){
				var result = JSON.parse(obj);
				var image = result.image;
				if(image!=""){
					$("#photo").attr("src", image);
				}else{
					$("#photo").attr("src", "images/photo.png");
				}
			}
		});
	});
	</script>
</body>
</html>