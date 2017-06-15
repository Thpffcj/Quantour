<%@ page language="java" import="java.util.*"
	contentType="text/html; charset=utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<title>股票比较</title>
<link rel="stylesheet" href="https://cdn.bootcss.com/bootstrap/3.3.7/css/bootstrap.min.css">
<script src="http://cdn.static.runoob.com/libs/jquery/2.1.1/jquery.min.js"></script>
<script src="https://cdn.bootcss.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
<link rel="stylesheet" type="text/css" href="css/index.css">
<link rel="stylesheet" href="css/StockVS.css">
<link rel="stylesheet"
	href="//apps.bdimg.com/libs/jqueryui/1.10.4/css/jquery-ui.min.css">
<script src="//apps.bdimg.com/libs/jqueryui/1.10.4/jquery-ui.min.js"></script>
<script src="theme/echarts.js"></script>
<script type="text/javascript">
	$(document).ready(function() {
		$("#begindate").datepicker({
			minDate: new Date(2015, 1 - 1 , 1),
			maxDate: new Date(2017, 6 - 1 , 13),
	        onSelect: function(selected) {	          
	        	$("#enddate").datepicker("option","minDate", selected)
		    }
		});
		$("#enddate").datepicker({ 
			minDate: new Date(2015, 1 - 1 , 1),
			maxDate: new Date(2017, 6 - 1 , 13),
		    onSelect: function(selected) {
		        $("#begindate").datepicker("option","maxDate", selected)
		    }
		});
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
</head>
<body>
	<nav id = "navbar">
		<div>
			<div>
				<ul id = "navul">
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
		<div class="stockvs">
			<form action="">
				<div class="input-left">
					<label>时间:</label>
					<input type="text" id="begindate" value = "2017-01-01" placeholder = "请选择开始日期">
					<label>---</label>
					<input type="text" id="enddate" value = "2017-06-13" placeholder = "请选择结束日期">
					<label>股票:</label>
				</div>
				<div class="stock-drop-list1">
					<input type="text" id="code1" value = "000001" placeholder = "请输入股票代码/名称">
					<label>VS</label>
					<ul id="match-list1">
					</ul>
				</div>
				<div class="stock-drop-list2">
					<input type="text" id="code2" value="000010" placeholder = "请输入股票代码/名称">
					<ul id="match-list2">
					</ul>
				</div>
				<button type = "button" class = "btn" id="searchVSGraph">比较</button>
			</form>
		</div>
		<script type="text/javascript">
		$("#code1").bind('input propertychange', function() {
			$.ajax({
				type: "POST",
				url: "getMatch",
				data: {
					enter: $("#code1").val(),
				},
				dataType: "json",
				success: function(obj){
					var result = JSON.parse(obj);
					var s = "";
					for(var i=0;i<5;i++){
						if(result.name[i]!=null){
							s = s+"<li onclick=\"getStock1('"+result.code[i]+"')\">"+result.code[i]+"&emsp;&emsp;&emsp;"+result.name[i]+"</li>";
						}
					}
					$("#match-list1").attr("style","");
					$("#match-list1").html(s);
				}
			});
		});
		
		function getStock1(code){
			$("#code1").val(code);
			$("#match-list1").attr("style","display:none;");
		}
		
		$("#code2").bind('input propertychange', function() {
			$.ajax({
				type: "POST",
				url: "getMatch",
				data: {
					enter: $("#code2").val(),
				},
				dataType: "json",
				success: function(obj){
					var result = JSON.parse(obj);
					var s = "";
					for(var i=0;i<5;i++){
						if(result.name[i]!=null){
							s = s+"<li onclick=\"getStock2('"+result.code[i]+"')\">"+result.code[i]+"&emsp;&emsp;&emsp;"+result.name[i]+"</li>";
						}
					}
					$("#match-list2").attr("style","");
					$("#match-list2").html(s);
				}
			});
		});
		
		function getStock2(code){
			$("#code2").val(code);
			$("#match-list2").attr("style","display:none;");
		}
		
		$(document).ready(function(){ 
			showVSGraph();
			$("#searchVSGraph").click(function(){ 
				$("#match-list1").attr("style","display:none;");
				$("#match-list2").attr("style","display:none;");
				showVSGraph();
			});
		});
		</script>
		
		<p>走势对比:</p>
		<div class="vsdiv" id="VSGraph"></div>
		<div class="vsmess">
			<p>基本指标对比:</p>
			<table class="table table-hover table-striped table-bordered">
				<thead>
					<tr>
						<th style = "text-align:center;">对比项</th>
						<th style = "text-align:center;">平安银行<br>(000001)</th>
						<th style = "text-align:center;">美丽生态<br>(000010)</th>
					</tr>
				</thead>
				<tbody id="vs-message-list">
				</tbody>
			</table>
		</div>
	</div>
	
	<script src="http://echarts.baidu.com/build/dist/echarts.js"></script>
	<script src="theme/dark.js"></script>
	<script type="text/javascript">

	function showVSGraph(){
		var colors = [ '#5793f3', '#d14a61', '#675bba' ];

		$.ajax({
			type : "GET",
			url : "VSGraph",
			data: {
				begindate: $("#begindate").val(),
				enddate: $("#enddate").val(),
				code1: $("#code1").val(),
				code2: $("#code2").val()
			},
			dataType : "json",
			success : function(obj) {
				var resultJSONData = JSON.parse(obj);
				var result = resultJSONData.result;
				if(result=="null"){
					$("#search-modal-prompt").html("输入框不能为空!");
					$("#modal-yes").attr("href","#");
					$("#modal-yes").html("<button type=\"submit\" class=\"btn btn-primary\">确定</button>");
					$("#search-modal").modal('show');
				}else if(result=="wrongdate"){
					$("#search-modal-prompt").html("错误日期!");
					$("#modal-yes").attr("href","#");
					$("#modal-yes").html("<button type=\"submit\" class=\"btn btn-primary\">确定</button>");
					$("#search-modal").modal('show');
				}else if(result=="unknow"){
					$("#search-modal-prompt").html("无效的股票名称/代码!");
					$("#modal-yes").attr("href","#");
					$("#modal-yes").html("<button type=\"submit\" class=\"btn btn-primary\">确定</button>");
					$("#search-modal").modal('show');
				}else if(result=="success"){
					var names = resultJSONData.Name;
					var date = resultJSONData.Date;
					var stock1 = resultJSONData.Stock1;
					var stock2 = resultJSONData.Stock2;
					var parameter1 = resultJSONData.parameter1;
					var parameter2 = resultJSONData.parameter2;
					
					var s = "<tr><td>一周涨跌</td><td>"+parameter1[0]+"</td><td>"+parameter2[0]+"</td></tr>"
							+"<tr><td>一月涨跌</td><td>"+parameter1[1]+"</td><td>"+parameter2[1]+"</td></tr>"
							+"<tr><td>三月涨跌</td><td>"+parameter1[2]+"</td><td>"+parameter2[2]+"</td></tr>"
							+"<tr><td>半年涨跌</td><td>"+parameter1[3]+"</td><td>"+parameter2[3]+"</td></tr>"
							+"<tr><td>一年涨跌</td><td>"+parameter1[4]+"</td><td>"+parameter2[4]+"</td></tr>";
					$("#vs-message-list").html(s);
					
					var myChart = echarts.init(document
							.getElementById('VSGraph'));
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
							data : names
						},
						grid : {
							top : 70,
							bottom : 50
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
								{
									type : 'category',
									axisTick : {
										alignWithLabel : true
									},
									axisLine : {
										onZero : false,
										lineStyle : {
											color : colors[0]
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
							scale: true,
							type : 'value',
							axisLabel : {
								formatter: function(value){
									return value+"%";
								}
							},
						} ],
						series : [
								{
									name : names[0],
									type : 'line',
									xAxisIndex: 1,
									smooth : true,
									data : stock1
								},
								{
									name : names[1],
									type : 'line',
									smooth : true,
									data : stock2
								} ]
					}
					myChart.setOption(option);
				}
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