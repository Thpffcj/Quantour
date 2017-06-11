<%@ page language="java" import="java.util.*"
	contentType="text/html; charset=utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<title>行情中心</title>
<link rel="stylesheet" href="https://cdn.bootcss.com/bootstrap/3.3.7/css/bootstrap.min.css">
<script
	src="http://cdn.static.runoob.com/libs/jquery/2.1.1/jquery.min.js"></script>
<script
	src="https://cdn.bootcss.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
<link rel="stylesheet" type="text/css" href="css/index.css">
<link rel="stylesheet" href="css/Market.css">
<link rel="stylesheet"
	href="//apps.bdimg.com/libs/jqueryui/1.10.4/css/jquery-ui.min.css">
<script src="//apps.bdimg.com/libs/jqueryui/1.10.4/jquery-ui.min.js"></script>
<script src="theme/echarts.js"></script>
<script type="text/javascript">
	$(document).ready(function() {
		$('#date').datepicker();
	});
</script>
<script>
	jQuery(function($) {
		$.datepicker.regional['zh-CN'] = {
			closeText : '关闭',
			prevText : '&#x3c;上月',
			nextText : '下月&#x3e;',
			currentText : '今天',
			monthNames : [ '一月', '二月', '三月', '四月', '五月', '六月', '七月', '八月',
					'九月', '十月', '十一月', '十二月' ],
			monthNamesShort : [ '一', '二', '三', '四', '五', '六', '七', '八', '九',
					'十', '十一', '十二' ],
			dayNames : [ '星期日', '星期一', '星期二', '星期三', '星期四', '星期五', '星期六' ],
			dayNamesShort : [ '周日', '周一', '周二', '周三', '周四', '周五', '周六' ],
			dayNamesMin : [ '日', '一', '二', '三', '四', '五', '六' ],
			weekHeader : '周',
			dateFormat : 'yy-mm-dd',
			firstDay : 1,
			isRTL : false,
			showMonthAfterYear : true,
			yearSuffix : '年'
		};
		$.datepicker.setDefaults($.datepicker.regional['zh-CN']);
	});
</script>
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
				<li class="firstli"><a href="javascript:void(0)" id="loginUserName">${sessionScope.loginUserName}</a>
					<ul class="drop">
						<li><a href="User.jsp">个人中心</a></li>
						<li><a href="Main.jsp" onclick="logout()">退出登录</a></li>
					</ul></li>
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
			<h2>沪深市场</h2>
		</div>
		<div class="title">
			<img src="images/icon1.png" style="width: 28px; height: 28px;">
			<label>涨跌分布</label>
		</div>

		<div>
				<label style="color: white; font-weight: 100;">请选择日期:</label>
				<input type="text" id="date" value="2017-05-25" name="date">
				<button type="submit" class="btn" id="SearchUpDownGraph">确认</button>
		</div>
		
		<script type="text/javascript">
			$(document).ready(function(){ 
				showUpDownGraph()
				$("#SearchUpDownGraph").click(function(){ 
					showUpDownGraph()
				});
			});
			</script>
			
		<div class="clearfix">
			<div class="updown" id="echarts1"></div>
			<div class="uptop5">
				<p>涨幅最大的5支股票:</p>
				<table class="table table-hover table-striped table-bordered">
					<thead>
						<tr>
							<th style="text-align: center;">股票名称</th>
							<th style="text-align: center;">涨幅</th>
						</tr>
					</thead>
					<tbody id="upList">
						
					</tbody>
				</table>
			</div>
			<div class="downtop5">
				<p>跌幅最大的5支股票:</p>
				<table class="table table-hover table-striped table-bordered">
					<thead>
						<tr>
							<th style="text-align: center;">股票名称</th>
							<th style="text-align: center;">跌幅</th>
						</tr>
					</thead>
					<tbody id="downList">
			
					</tbody>
				</table>
			</div>
		</div>
		<div class="title" style="padding-top:30px;">
			<img src="images/icon2.png" style="width: 28px; height: 28px;">
			<label>基准股票</label>
		</div>
		<div class="others">
			<!-- 上证指数 -->
			<div class="flash" id="benchmark1"></div>
			<!-- 深证指数 -->
			<div class="flash" id="benchmark2"></div>
			<!-- 创业板指数 -->
			<div class="flash" id="benchmark3"></div>
		</div>
		
		<div class="head">
			<h2>个股行情</h2>
		</div>
		<div class="single-stock-pre">
			<div class="plate-choose">
				<ul>
					<li><a class="active" id="plate1" onclick = "shiftplate1()">全部股票</a></li>
					<li><a id="plate2" onclick = "shiftplate2()">中小板</a></li>
					<li><a id="plate3" onclick = "shiftplate3()">创业板</a></li>
				</ul>
			</div>
			<div class="prestation">
				<table class="table table-hover table-striped">
					<thead>
						<tr>
							<th style="text-align:center;">序号</th>
							<th style="text-align:center;">代码</th>
							<th style="text-align:center;">名称</th>
							<th style="text-align:center;">开盘价</th>
							<th style="text-align:center;">最低值</th>
							<th style="text-align:center;">最高值</th>
							<th style="text-align:center;">现价</th>
							<th style="text-align:center;">涨跌幅(%)</th>
							<th style="text-align:center;">交易量</th>
							<th style="text-align:center;">加自选股</th>
						</tr>
					</thead>
					<tbody id="listbody">
					</tbody>
				</table>
				<div class="m-page">
					<ul id="i-page">
						<li onclick="changepage(1)"><a>首页</a></li>
						<li onclick="changepage(0)" class="disable"><a>上一页</a></li>
						<li onclick="changepage(1)" class="active"><a>1</a></li>
						<li onclick="changepage(1)"><a>2</a></li>
						<li onclick="changepage(1)"><a>3</a></li>
						<li onclick="changepage(1)"><a>4</a></li>
						<li onclick="changepage(1)"><a>5</a></li>
						<li onclick="changepage(1)"><a>下一页</a></li>
						<li onclick="changepage(1)"><a>尾页</a></li>
					</ul>
				</div>
			</div>
		</div>
	</div>
	<br><br><br>
	
	<script type="text/javascript">
	function shiftplate1() {
		document.getElementById("plate1").className = "active";
		document.getElementById("plate2").className = "";
		document.getElementById("plate3").className = "";
		changepage(0);
	}
	function shiftplate2() {
		document.getElementById("plate1").className = "";
		document.getElementById("plate2").className = "active";
		document.getElementById("plate3").className = "";
		Smechangepage(0);
	}
	function shiftplate3() {
		document.getElementById("plate1").className = "";
		document.getElementById("plate2").className = "";
		document.getElementById("plate3").className = "active";
		Gemchangepage(0);
	}
	</script>
	
	<script type="text/javascript">
	$(document).ready(function(){ 
		changepage(0);
	});
	</script>
	
	<script type="text/javascript">
	function changepage(p){
		console.log(p);
		$.ajax({
			type : "GET",
			url : "StockMessage",
			data: {
				page: p,
			},
			dataType : "json",
			success: function(obj){
				var result = JSON.parse(obj);
				var index = result.index;
				var code = result.code;
				var name = result.name;
				var open = result.open;
				var high = result.high;
				var low = result.low;
				var close = result.close;
				var fluctuation = result.fluctuation;
				var volume = result.volume;
				
				var now = result.now;
				
				var s = "";
				for(var i=0;i<10;i++){
					if(index[i]!=null){
						s = s+"<tr><td>"+index[i]+"</td><td><a href=\"Stock.jsp\" target=\"_blank\" onclick=\"SearchStock('"+
							code[i]+"')\">"+code[i]+"</a></td><td><a href=\"Stock.jsp\" target=\"_blank\" onclick=\"SearchStock('"+
							code[i]+"')\">"+name[i]+"</a></td><td>"+open[i]+"</td><td>"
							+high[i]+"</td><td>"+low[i]+"</td><td>"+close[i]+"</td><td>"+fluctuation[i]+"</td><td>"+volume[i]
							+"</td><td><a class=\"plus\" onclick=\"addSelfStock('"+code[i]+"')\"><span class=\"glyphicon glyphicon-plus\"></span></a></td><tr>";
					}
				}
				$("#listbody").html(s);
				var page = (now-1)-(now-1)%5+1;
				if(page>(result.length-4)){
					page = result.length-4;
				}
				var s1 = "<li><a onclick=\"changepage(0)\">首页</a></li>";
				if(p==0){
					s1 = s1+"<li class=\"disable\"><a onclick=\"changepage(-1)\">上一页</a></li>";
				}else{
					s1 = s1+"<li><a onclick=\"changepage(-1)\">上一页</a></li>";
				}
				for(var i=0;i<5;i++){
					var k = page+i;
					if(k==now){
						var j = k-1;
						s1 = s1+"<li class=\"active\"><a onclick=\"changepage("+j+")\">"+k+"</a></li>";
					}else{
						var j = k-1;
						s1 = s1+"<li><a onclick=\"changepage("+j+")\">"+k+"</a></li>";
					}
				}
				if(now==(result.length)){
					s1 = s1+"<li class=\"disable\"><a onclick=\"changepage(-2)\">下一页</a></li>";
				}else{
					s1 = s1+"<li><a onclick=\"changepage(-2)\">下一页</a></li>";
				}
				length = result.length-1;
				s1 = s1+"<li><a onclick=\"changepage("+length+")\">尾页</a></li>";
				$("#i-page").html(s1);
			}
		});
	}
	
	function Smechangepage(p){
		console.log(p);
		$.ajax({
			type : "GET",
			url : "SmeStockMessage",
			data: {
				page: p,
			},
			dataType : "json",
			success: function(obj){
				var result = JSON.parse(obj);
				var index = result.index;
				var code = result.code;
				var name = result.name;
				var open = result.open;
				var high = result.high;
				var low = result.low;
				var close = result.close;
				var fluctuation = result.fluctuation;
				var volume = result.volume;
				
				var now = result.now;
				
				var s = "";
				for(var i=0;i<10;i++){
					if(index[i]!=null){
						s = s+"<tr><td>"+index[i]+"</td><td><a href=\"Stock.jsp\" target=\"_blank\" onclick=\"SearchStock('"+
							code[i]+"')\">"+code[i]+"</a></td><td><a href=\"Stock.jsp\" target=\"_blank\" onclick=\"SearchStock('"+
							code[i]+"')\">"+name[i]+"</a></td><td>"+open[i]+"</td><td>"
							+high[i]+"</td><td>"+low[i]+"</td><td>"+close[i]+"</td><td>"+fluctuation[i]+"</td><td>"+volume[i]
							+"</td><td><a class=\"plus\" onclick=\"addSelfStock('"+code[i]+"')\"><span class=\"glyphicon glyphicon-plus\"></span></a></td><tr>";
					}
				}
				$("#listbody").html(s);
				var page = (now-1)-(now-1)%5+1;
				if(page>(result.length-4)){
					page = result.length-4;
				}
				var s1 = "<li><a onclick=\"Smechangepage(0)\">首页</a></li>";
				if(p==0){
					s1 = s1+"<li class=\"disable\"><a onclick=\"Smechangepage(-1)\">上一页</a></li>";
				}else{
					s1 = s1+"<li><a onclick=\"Smechangepage(-1)\">上一页</a></li>";
				}
				for(var i=0;i<5;i++){
					var k = page+i;
					if(k==now){
						var j = k-1;
						s1 = s1+"<li class=\"active\"><a onclick=\"Smechangepage("+j+")\">"+k+"</a></li>";
					}else{
						var j = k-1;
						s1 = s1+"<li><a onclick=\"Smechangepage("+j+")\">"+k+"</a></li>";
					}
				}
				if(now==(result.length)){
					s1 = s1+"<li class=\"disable\"><a onclick=\"Smechangepage(-2)\">下一页</a></li>";
				}else{
					s1 = s1+"<li><a onclick=\"Smechangepage(-2)\">下一页</a></li>";
				}
				length = result.length-1;
				s1 = s1+"<li><a onclick=\"Smechangepage("+length+")\">尾页</a></li>";
				$("#i-page").html(s1);
			}
		});
	}
	
	function Gemchangepage(p){
		console.log(p);
		$.ajax({
			type : "GET",
			url : "GemStockMessage",
			data: {
				page: p,
			},
			dataType : "json",
			success: function(obj){
				var result = JSON.parse(obj);
				var index = result.index;
				var code = result.code;
				var name = result.name;
				var open = result.open;
				var high = result.high;
				var low = result.low;
				var close = result.close;
				var fluctuation = result.fluctuation;
				var volume = result.volume;
				
				var now = result.now;
				
				var s = "";
				for(var i=0;i<10;i++){
					if(index[i]!=null){
						s = s+"<tr><td>"+index[i]+"</td><td><a href=\"Stock.jsp\" target=\"_blank\" onclick=\"SearchStock('"+
							code[i]+"')\">"+code[i]+"</a></td><td><a href=\"Stock.jsp\" target=\"_blank\" onclick=\"SearchStock('"+
							code[i]+"')\">"+name[i]+"</a></td><td>"+open[i]+"</td><td>"
							+high[i]+"</td><td>"+low[i]+"</td><td>"+close[i]+"</td><td>"+fluctuation[i]+"</td><td>"+volume[i]
							+"</td><td><a class=\"plus\" onclick=\"addSelfStock('"+code[i]+"')\"><span class=\"glyphicon glyphicon-plus\"></span></a></td><tr>";
					}
				}
				$("#listbody").html(s);
				var page = (now-1)-(now-1)%5+1;
				if(page>(result.length-4)){
					page = result.length-4;
				}
				var s1 = "<li><a onclick=\"Gemchangepage(0)\">首页</a></li>";
				if(p==0){
					s1 = s1+"<li class=\"disable\"><a onclick=\"Gemchangepage(-1)\">上一页</a></li>";
				}else{
					s1 = s1+"<li><a onclick=\"Gemchangepage(-1)\">上一页</a></li>";
				}
				for(var i=0;i<5;i++){
					var k = page+i;
					if(k==now){
						var j = k-1;
						s1 = s1+"<li class=\"active\"><a onclick=\"Gemchangepage("+j+")\">"+k+"</a></li>";
					}else{
						var j = k-1;
						s1 = s1+"<li><a onclick=\"Gemchangepage("+j+")\">"+k+"</a></li>";
					}
				}
				if(now==(result.length)){
					s1 = s1+"<li class=\"disable\"><a onclick=\"Gemchangepage(-2)\">下一页</a></li>";
				}else{
					s1 = s1+"<li><a onclick=\"Gemchangepage(-2)\">下一页</a></li>";
				}
				length = result.length-1;
				s1 = s1+"<li><a onclick=\"Gemchangepage("+length+")\">尾页</a></li>";
				$("#i-page").html(s1);
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
	
	function addSelfStock(c){
		$.ajax({
			type: "POST",
			url: "addSelfStock",
			data: {
				code: c,
				username: $("#loginUserName").text(),
			},
			dataType: "json",
			success: function(obj){
				var result = JSON.parse(obj);
				if(result.result=="success"){
					$("#search-modal-prompt").html("添加成功!");
					$("#modal-yes").attr("href","#");
					$("#modal-yes").html("<button type=\"submit\" class=\"btn btn-primary\">确定</button>");
					$("#search-modal").modal('show');
				}else if(result.result=="same"){
					$("#search-modal-prompt").html("股票已存在!");
					$("#modal-yes").attr("href","#");
					$("#modal-yes").html("<button type=\"submit\" class=\"btn btn-primary\" data-dismiss=\"modal\">确定</button>");
					$("#search-modal").modal('show');
				}else if(result.result=="null") {
					$("#search-modal-prompt").html("股票名称/代码不能为空!");
					$("#modal-yes").attr("href","#");
					$("#modal-yes").html("<button type=\"submit\" class=\"btn btn-primary\" data-dismiss=\"modal\">确定</button>");
					$("#search-modal").modal('show');
				}else if(result.result=="failure"){
					$("#search-modal-prompt").html("添加失败!");
					$("#modal-yes").attr("href","#");
					$("#modal-yes").html("<button type=\"submit\" class=\"btn btn-primary\" data-dismiss=\"modal\">确定</button>");
					$("#search-modal").modal('show');
				}else if(result.result=="unknow"){
					$("#search-modal-prompt").html("无效股票名称/代码!");
					$("#modal-yes").attr("href","#");
					$("#modal-yes").html("<button type=\"submit\" class=\"btn btn-primary\" data-dismiss=\"modal\">确定</button>");
					$("#search-modal").modal('show');
				}
			}
		});
	}
	</script>

	<script src="http://echarts.baidu.com/build/dist/echarts.js"></script>
	<script src="theme/dark.js"></script>
	<script type="text/javascript">

		function showUpDownGraph(){

		var datas = new Array;
		var upsName = new Array;
		var upsAmplitude = new Array;
		var downsName = new Array;
		var downsAmplitude = new Array;

		$.ajax({
			type : "GET",
			url : "UpsAndDownsGraph",
			data: {
				date: $("#date").val(), 
			},
			dataType : "json",
			success : function(data) {
				var resultJSONData = JSON.parse(data);
				for (i = 0; i < resultJSONData.Data.length; i++) {
					datas[i] = resultJSONData.Data[i];
				}
				for(i = 0; i<resultJSONData.UpName.length; i++){
					upsName[i] = resultJSONData.UpName[i];
					upsAmplitude[i] = resultJSONData.UpAmplitude[i];
					downsName[i] = resultJSONData.DownName[i];
					downsAmplitude[i] = resultJSONData.DownAmplitude[i];
				}

				for(i=0; i<resultJSONData.UpName.length; i++){
					 $('#upList').append('<tr><td><a href="Stock.jsp" target="_blank" onclick="SearchStock(\''+upsName[i]+'\')">'+upsName[i]+'</a></td><td>'+upsAmplitude[i]+'</td></tr>');
					 $('#downList').append('<tr><td><a href="Stock.jsp" target="_blank" onclick="SearchStock(\''+downsName[i]+'\')">'+downsName[i]+'</a></td><td>'+downsAmplitude[i]+'</td></tr>');
				}

				var myChart = echarts.init(document.getElementById('echarts1'),
						'dark');

				var option = {
					backgroundColor : '#eee',
					tooltip : {
						show : true
					},
					legend : {
						data : [ '数量' ]
					},
					xAxis : [ {
						type : 'category',
						data : ['跌停','-8%','-6%','-4%','-2%','0','2%','-4%','6%','8%','涨停']
					} ],
					yAxis : [ {
						type : 'value'
					} ],
					series : [ {
						"name" : "数量",
						"type" : "bar",
						"data" : datas
					} ]
				}

				// 为echarts对象加载数据 
				myChart.setOption(option);
			}
		});
		}
	</script>

	<script src="theme/dark.js"></script>
	<script type="text/javascript">
		
		var date = new Array;
		var adjClose = new Array;

		var colors = [ '#5793f3', '#d14a61', '#675bba' ];

		$.ajax({
			type : "GET",
			url : "BenchmarkGraph",
			data: {
				type : "hs300", 
			},
			dataType : "json",
			success : function(obj) {
				var resultJSONData = JSON.parse(obj);
				for (i = 0; i < resultJSONData.Date.length; i++) {
					date[i] = resultJSONData.Date[i];
					adjClose[i] = resultJSONData.AdjClose[i];
				}

				var myChart = echarts.init(document
						.getElementById('benchmark1'));
				option = {
					color : colors,
					backgroundColor : '#eee',
					tooltip : {
						trigger : 'none',
						axisPointer : {
							type : 'cross'
						}
					},
					legend : {
						top : 10,
						left : 'center',
					},
					grid : {
						top : 70,
						bottom : 50,
						left: 50
					},
					xAxis : [
							{
								type : 'category',
								axisTick : {
									alignWithLabel : true
								},
								axisLine : {
									onZero : false,
									lineStyle : {
										color : colors[1]
									}
								},
								axisPointer : {
									label : {
										formatter : function(params) {
											return ' '
													+ params.value
													+ (params.seriesData.length ? '：'
															+ params.seriesData[0].data
															: '');
										}
									}
								},
								data : date
							},

					],
					yAxis : [ {
						scale : true,
						type : 'value'
					} ],
					series : [  {
						name : "",
						type : 'line',
						smooth : true,
						data : adjClose
					} ]
				}
				myChart.setOption(option);
			}
		});
	</script>
	
	<script src="theme/dark.js"></script>
	<script type="text/javascript">
		
		var date = new Array;
		var adjClose = new Array;

		var colors = [ '#5793f3', '#d14a61', '#675bba' ];

		$.ajax({
			type : "GET",
			url : "BenchmarkGraph",
			data: {
				type : "399005", 
			},
			dataType : "json",
			success : function(obj) {
				var resultJSONData = JSON.parse(obj);
				for (i = 0; i < resultJSONData.Date.length; i++) {
					date[i] = resultJSONData.Date[i];
					adjClose[i] = resultJSONData.AdjClose[i];
				}

				var myChart = echarts.init(document
						.getElementById('benchmark2'));
				option = {
					color : colors,
					backgroundColor : '#eee',
					tooltip : {
						trigger : 'none',
						axisPointer : {
							type : 'cross'
						}
					},
					legend : {
						top : 10,
						left : 'center',
					},
					grid : {
						top : 70,
						bottom : 50,
						left: 50
					},
					xAxis : [
							{
								type : 'category',
								axisTick : {
									alignWithLabel : true
								},
								axisLine : {
									onZero : false,
									lineStyle : {
										color : colors[1]
									}
								},
								axisPointer : {
									label : {
										formatter : function(params) {
											return ' '
													+ params.value
													+ (params.seriesData.length ? '：'
															+ params.seriesData[0].data
															: '');
										}
									}
								},
								data : date
							},

					],
					yAxis : [ {
						scale : true,
						type : 'value'
					} ],
					series : [  {
						name : "",
						type : 'line',
						smooth : true,
						data : adjClose
					} ]
				}
				myChart.setOption(option);
			}
		});
	</script>
	
	<script src="theme/dark.js"></script>
	<script type="text/javascript">
		
		var date = new Array;
		var adjClose = new Array;

		var colors = [ '#5793f3', '#d14a61', '#675bba' ];

		$.ajax({
			type : "GET",
			url : "BenchmarkGraph",
			data: {
				type : "399006", 
			},
			dataType : "json",
			success : function(obj) {
				var resultJSONData = JSON.parse(obj);
				for (i = 0; i < resultJSONData.Date.length; i++) {
					date[i] = resultJSONData.Date[i];
					adjClose[i] = resultJSONData.AdjClose[i];
				}

				var myChart = echarts.init(document
						.getElementById('benchmark3'));
				option = {
					color : colors,
					backgroundColor : '#eee',
					tooltip : {
						trigger : 'none',
						axisPointer : {
							type : 'cross'
						}
					},
					legend : {
						top : 10,
						left : 'center',
					},
					grid : {
						top : 70,
						bottom : 50,
						left: 50
					},
					xAxis : [
							{
								type : 'category',
								axisTick : {
									alignWithLabel : true
								},
								axisLine : {
									onZero : false,
									lineStyle : {
										color : colors[1]
									}
								},
								axisPointer : {
									label : {
										formatter : function(params) {
											return ' '
													+ params.value
													+ (params.seriesData.length ? '：'
															+ params.seriesData[0].data
															: '');
										}
									}
								},
								data : date
							},

					],
					yAxis : [ {
						scale : true,
						type : 'value'
					} ],
					series : [  {
						name : "",
						type : 'line',
						smooth : true,
						data : adjClose
					} ]
				}
				myChart.setOption(option);
			}
		});
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