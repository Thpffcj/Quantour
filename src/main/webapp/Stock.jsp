<%@ page language="java" import="java.util.*"
	contentType="text/html; charset=utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>个股展示</title>
<link rel="stylesheet" href="https://cdn.bootcss.com/bootstrap/3.3.7/css/bootstrap.min.css">
<script src="http://cdn.static.runoob.com/libs/jquery/2.1.1/jquery.min.js"></script>
<script src="https://cdn.bootcss.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath }/css/index.css">
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath }/css/Stock.css">
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
		
		$("#begindate1").datepicker({
			minDate: new Date(2015, 1 - 1 , 1),
			maxDate: new Date(2017, 6 - 1 , 13),
			onSelect: function(selected) {	          
	        	$("#enddate1").datepicker("option","minDate", selected)
		    }
		});
		$("#enddate1").datepicker({ 
			minDate: new Date(2015, 1 - 1 , 1),
			maxDate: new Date(2017, 6 - 1 , 13),
			onSelect: function(selected) {
		        $("#begindate1").datepicker("option","maxDate", selected)
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
			<h2>股票简介</h2>
		</div>
		<div class="basicmess">
			<div class="namecode">
				<h3 id="LName"></h3>
				<h4 id="LCode"></h4>
			</div>
			<div class="messtable">
				<table>
					<tr>
						<td id="LOpen"></td>
						<td id="LHighest"></td>
						<td id="LVolumn"></td>
						<td id="LFluct"></td>
					</tr>
					<tr>
						<td id="LClose"></td>
						<td id="LLowest"></td>
						<td id="LMoney"></td>
					</tr>
				</table>
			</div>
			<div class="butt">
				<button type="button" class="selfstock" onclick="addSelfStock()">加入自选股</button>
			</div>
		</div>
		<div class="stockpre">
			<form action="">
				<label>调整K线图时间:</label>
					<input type="text" id="begindate" value = "2016-06-13" placeholder = "请选择开始日期">
				<label>---</label>
					<input type="text" id="enddate" value = "2017-06-13" placeholder = "请选择结束日期">
				<button type = "button" class = "btn" id="searchKGraph">确定</button>
			</form>
			<script type="text/javascript">
			$(document).ready(function(){ 
				showKGraph()
				$("#searchKGraph").click(function(){ 
					var begindate = $("#begindate").val();
					var enddate = $("#enddate").val();
					if(begindate==""||enddate==""){
						$("#search-modal-prompt").html("输入框不能为空!");
						$("#modal-yes").attr("href","#");
						$("#modal-yes").html("<button type=\"submit\" class=\"btn btn-primary\" data-dismiss=\"modal\">确定</button>");
						$("#search-modal").modal('show');
						return;
					}
					showKGraph()
				});
				$("#searchTrendGraph").click(function(){ 
					var begindate = $("#begindate1").val();
					var enddate = $("#enddate1").val();
					var daynum = $("#daynum").val();
					if(begindate==""||enddate==""||daynum==""){
						$("#search-modal-prompt").html("输入框不能为空!");
						$("#modal-yes").attr("href","#");
						$("#modal-yes").html("<button type=\"submit\" class=\"btn btn-primary\" data-dismiss=\"modal\">确定</button>");
						$("#search-modal").modal('show');
						return;
					}
					showTrendgraphGraph()
				});
			});
			</script>
		</div>
		<div class="Kdiv" id="Kgraph"></div>
		<div class="head">
			<h2>趋势预测</h2>
		</div>
		<div class="trend-input">
			<label>基础时间:</label>
				<input type="text" id="begindate1" value = "2017-05-13" placeholder = "请选择开始日期">
			<label>---</label>
				<input type="text" id="enddate1" value = "2017-06-13" placeholder = "请选择结束日期">
			<label>预测天数:</label>
				<input type="text" id="daynum" value="10" placeholder="请选择预测的天数">
			<button type = "button" class = "btn" id="searchTrendGraph">确定</button>
			
		</div>
		<div class="trend" id="Trendgraph"></div>
	</div>
	
	<script>
	function addSelfStock(){
		$.ajax({
			type: "POST",
			url: "addSelfStock",
			data: {
				code: $("#LCode").text(),
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
	
	<script src="theme/dark.js"></script>
	<script type="text/javascript">

	function showTrendgraphGraph(){

		var ups = new Array;
		var downs = new Array;
		var date = new Array;
		var colors = [ '#5793f3', '#d14a61', '#675bba' ];

		$.ajax({
			type : "GET",
			url : "Trendgraph",
			data: {
				code: $("#LCode").text(),
				begin: $("#begindate1").val(), 
				end: $("#enddate1").val(),
				fday: $("#daynum").val()
			},
			dataType : "json",
			success : function(obj) {
				var resultJSONData = JSON.parse(obj);

				ups = resultJSONData.Ups;
				downs = resultJSONData.Downs;
				date = resultJSONData.Date;
				
					
					var myChart = echarts.init(document
							.getElementById('Trendgraph'));
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
							data : ['最高值', '最低值']
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
						} ],
						series : [
								{
									name : '最高价',
									type : 'line',
									xAxisIndex: 1,
									smooth : true,
									data : ups
								},
								{
									name : '最低价',
									type : 'line',
									smooth : true,
									data : downs
								} ]
					}
					myChart.setOption(option);
				}
		});
		}
	</script>


	<script src="theme/dark.js"></script>
	<script type="text/javascript">

		function showKGraph(){

			var KData = new Array;

			function splitData(rawData) {
				var categoryData = [];
				var values = [];
				var volumns = [];
				for (var i = 0; i < rawData.length; i++) {
					categoryData.push(rawData[i].splice(0, 1)[0]);
					values.push(rawData[i]);
					volumns.push(rawData[i][4]);
				}
				return {
					categoryData : categoryData,
					values : values,
					volumns : volumns
				};
			}

			function calculateMA(dayCount, data) {
				var result = [];
				for (var i = 0, len = data.values.length; i < len; i++) {
					if (i < dayCount) {
						result.push('-');
						continue;
					}
					var sum = 0;
					for (var j = 0; j < dayCount; j++) {
						sum += data.values[i - j][1];
					}
					result.push(+(sum / dayCount).toFixed(3));
				}
				return result;
			}

			
			$.ajax({
				type : "POST",
				url : "KGraph",
				data: {
					begindate: $("#begindate").val(), 
					enddate: $("#enddate").val(),
					code: $("#code").val()
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
					}
					if(result=="success"){
						$("#LName").html(resultJSONData.LName);
						$("#LCode").html(resultJSONData.LCode);
						$("#LClose").html("昨收:"+resultJSONData.LClose);
						$("#LOpen").html("今开:"+resultJSONData.LOpen);
						$("#LHighest").html("最高:"+resultJSONData.LHighest);
						$("#LLowest").html("最低:"+resultJSONData.LLowest);
						$("#LVolumn").html("成交量:"+resultJSONData.LVolumn);
						$("#LMoney").html("成交额:"+resultJSONData.LMoney+"万元");
						$("#LFluct").html("涨跌幅:"+resultJSONData.LFluct+"%");
						
						for (i = 0; i < resultJSONData.Date.length; i++) {
							KData[i] = new Array;
							KData[i][0] = resultJSONData.Date[i];
							KData[i][1] = resultJSONData.Open[i];
							KData[i][2] = resultJSONData.Close[i];
							KData[i][3] = resultJSONData.Lowest[i];
							KData[i][4] = resultJSONData.Highest[i];
							KData[i][5] = resultJSONData.Volumn[i];
						}

						var data = splitData(KData);

						var myChart = echarts.init(document
								.getElementById('Kgraph'));

						var option = {
							backgroundColor : '#eee',
							animation : false,
							legend : {
								top : 10,
								left : 'center',
								data : [ 'Dow-Jones index', 'MA5', 'MA10',
										'MA20', 'MA30' ]
							},
							tooltip : {
								trigger : 'axis',
								axisPointer : {
									type : 'cross'
								},
								backgroundColor : 'rgba(245, 245, 245, 0.8)',
								borderWidth : 1,
								borderColor : '#ccc',
								padding : 10,
								textStyle : {
									color : '#000'
								},
								position : function(pos, params, el, elRect,
										size) {
									var obj = {
										top : 10
									};
									obj[[ 'left', 'right' ][+(pos[0] < size.viewSize[0] / 2)]] = 30;
									return obj;
								},
								extraCssText : 'width: 170px'
							},
							axisPointer : {
								link : {
									xAxisIndex : 'all'
								},
								label : {
									backgroundColor : '#777'
								}
							},
							toolbox : {
								feature : {
									dataZoom : {
										yAxisIndex : false
									},
									brush : {
										type : [ 'lineX', 'clear' ]
									}
								}
							},
							brush : {
								xAxisIndex : 'all',
								brushLink : 'all',
								outOfBrush : {
									colorAlpha : 0.1
								}
							},
							grid : [ {
								left : '10%',
								right : '8%',
								height : '50%'
							}, {
								left : '10%',
								right : '8%',
								top : '63%',
								height : '16%'
							} ],
							xAxis : [
									{
										type : 'category',
										data : data.categoryData,
										scale : true,
										boundaryGap : false,
										axisLine : {
											onZero : false
										},
										splitLine : {
											show : false
										},
										splitNumber : 20,
										min : 'dataMin',
										max : 'dataMax',
										axisPointer : {
											z : 100
										}
									},
									{
										type : 'category',
										gridIndex : 1,
										data : data.categoryData,
										scale : true,
										boundaryGap : false,
										axisLine : {
											onZero : false
										},
										axisTick : {
											show : false
										},
										splitLine : {
											show : false
										},
										axisLabel : {
											show : false
										},
										splitNumber : 20,
										min : 'dataMin',
										max : 'dataMax',
										axisPointer : {
											label : {
												formatter : function(params) {
													var seriesValue = (params.seriesData[0] || {}).value;
													return params.value
															+ (seriesValue != null ? '\n'
																	+ echarts.format
																			.addCommas(seriesValue)
																	: '');
												}
											}
										}
									} ],
							yAxis : [ {
								scale : true,
								splitArea : {
									show : true
								}
							}, {
								scale : true,
								gridIndex : 1,
								splitNumber : 2,
								axisLabel : {
									show : false
								},
								axisLine : {
									show : false
								},
								axisTick : {
									show : false
								},
								splitLine : {
									show : false
								}
							} ],
							dataZoom : [ {
								type : 'inside',
								xAxisIndex : [ 0, 1 ],
								start : 50,
								end : 100
							}, {
								show : true,
								xAxisIndex : [ 0, 1 ],
								type : 'slider',
								top : '85%',
								start : 50,
								end : 100
							} ],
							series : [
									{
										name : 'Dow-Jones index',
										type : 'candlestick',
										data : data.values,
										itemStyle : {
											normal : {
												borderColor : null,
												borderColor0 : null
											}
										},
										tooltip : {
											formatter : function(param) {
												param = param[0];
												return [
														'Date: '
																+ param.name
																+ '<hr size=1 style="margin: 3px 0">',
														'Open: '
																+ param.data[0]
																+ '<br/>',
														'Close: '
																+ param.data[1]
																+ '<br/>',
														'Lowest: '
																+ param.data[2]
																+ '<br/>',
														'Highest: '
																+ param.data[3]
																+ '<br/>' ]
														.join('');
											}
										}
									}, {
										name : 'MA5',
										type : 'line',
										data : calculateMA(5, data),
										smooth : true,
										lineStyle : {
											normal : {
												opacity : 0.5
											}
										}
									}, {
										name : 'MA10',
										type : 'line',
										data : calculateMA(10, data),
										smooth : true,
										lineStyle : {
											normal : {
												opacity : 0.5
											}
										}
									}, {
										name : 'MA20',
										type : 'line',
										data : calculateMA(20, data),
										smooth : true,
										lineStyle : {
											normal : {
												opacity : 0.5
											}
										}
									}, {
										name : 'MA30',
										type : 'line',
										data : calculateMA(30, data),
										smooth : true,
										lineStyle : {
											normal : {
												opacity : 0.5
											}
										}
									}, {
										name : 'Volumn',
										type : 'bar',
										xAxisIndex : 1,
										yAxisIndex : 1,
										data : data.volumns
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