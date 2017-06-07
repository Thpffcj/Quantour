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
<script src="http://cdn.static.runoob.com/libs/bootstrap/3.3.7/js/bootstrap.min.js"></script>
<link rel="stylesheet" type="text/css" href="css/index.css">
<link rel="stylesheet" href="css/StockVS.css">
<link rel="stylesheet"
	href="//apps.bdimg.com/libs/jqueryui/1.10.4/css/jquery-ui.min.css">
<script src="//apps.bdimg.com/libs/jqueryui/1.10.4/jquery-ui.min.js"></script>
<script src="theme/echarts.js"></script>
<script type="text/javascript">
	$(document).ready(function() {
		$("#begindate").datepicker({
	        onSelect: function(selected) {	          
	        	$("#enddate").datepicker("option","minDate", selected)
		    }
		});
		$("#enddate").datepicker({ 
		    onSelect: function(selected) {
		        $("#begindate").datepicker("option","maxDate", selected)
		    }
		});
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
				<li class="firstli"><img src="images/photo.png"></li>
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
				<label>时间:</label><input type="text" id="begindate" value = "2005-02-01" placeholder = "请选择开始日期">
				<label>---</label><input type="text" id="enddate" value = "2005-07-01" placeholder = "请选择结束日期">
				<label>股票:</label><input type="text" id="code1" value = "000001" placeholder = "请输入股票代码/名称">
				<label>VS</label><input type="text" id="code2" value="000010" placeholder = "请输入股票代码/名称">
				<button type = "button" class = "btn" id="searchVSGraph">比较</button>
			</form>
		</div>
		<script type="text/javascript">
			$(document).ready(function(){ 
				showVSGraph()
				$("#searchVSGraph").click(function(){ 
					showVSGraph()	
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
				<tbody>
					<tr>
						<td>最低值</td>
						<td>6.42</td>
						<td>5.43</td>
					</tr>
					<tr>
						<td>最高值</td>
						<td>8.42</td>
						<td>7.43</td>
					</tr>
					<tr>
						<td>涨跌幅</td>
						<td>6.42%</td>
						<td>5.43%</td>
					</tr>
					<tr>
						<td>收盘价</td>
						<td>6.42</td>
						<td>5.43</td>
					</tr>
					<tr>
						<td>对数收益率</td>
						<td>6.42</td>
						<td>5.43</td>
					</tr>
				</tbody>
			</table>
		</div>
	</div>
	
	<script src="http://echarts.baidu.com/build/dist/echarts.js"></script>
	<script src="theme/dark.js"></script>
	<script type="text/javascript">

	function showVSGraph(){
		
		var names = new Array;
		var date = new Array;
		var stock1 = new Array;
		var stock2 = new Array;

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
				for (i = 0; i < resultJSONData.Name.length; i++) {
					names[i] = resultJSONData.Name[i];
				}
				for (i = 0; i < resultJSONData.Date.length; i++) {
					date[i] = resultJSONData.Date[i];
				}
				for (i = 0; i < resultJSONData.Stock1.length; i++) {
					stock1[i] = resultJSONData.Stock1[i];
				}
				for (i = 0; i < resultJSONData.Stock2.length; i++) {
					stock2[i] = resultJSONData.Stock2[i];
				}
				
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
						type : 'value'
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
	</script>

</body>
</html>