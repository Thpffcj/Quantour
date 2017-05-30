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
<link rel="stylesheet"
	href="https://cdn.bootcss.com/bootstrap/3.3.7/css/bootstrap.min.css">
<link rel="stylesheet"
	href="https://cdn.bootcss.com/bootstrap/3.3.7/css/bootstrap-theme.min.css">
<script
	src="https://cdn.bootcss.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath }/css/index.css">
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath }/css/Stock.css">
<link rel="stylesheet"
	href="//apps.bdimg.com/libs/jqueryui/1.10.4/css/jquery-ui.min.css">
<script src="//apps.bdimg.com/libs/jquery/1.10.2/jquery.min.js"></script>
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
		<input type="text" placeholder="输入股票代码/名称" id=> 
		<button type="submit" class="search">搜索</button>
	</div>

	<div class="w1200">
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
						<td>涨跌幅:</td>
					</tr>
					<tr>
						<td id="LClose"></td>
						<td id="LLowest"></td>
						<td id="LMoney"></td>
						<td>振幅:</td>
					</tr>
				</table>
			</div>
			<div class="butt">
				<button type="button" class="selfstock">加入股票池</button>
			</div>
		</div>
		<div class="stockpre">
			<form action="">
				<label>调整K线图时间:</label>
					<input type="text" id="begindate" value = "2005-04-28" placeholder = "请选择开始日期">
				<label>---</label>
					<input type="text" id="enddate" value = "2005-04-29" placeholder = "请选择结束日期">
				<button type = "button" class = "btn" id="searchKGraph">确定</button>
			</form>
			<script type="text/javascript">
			$(document).ready(function(){ 
				showKGraph()
				$("#searchKGraph").click(function(){ 
					$.ajax({ 
					    type: "POST", 	
						url: "KGraph",
						data: {
							begindate: $("#begindate").val(), 
							enddate: $("#enddate").val()
						},
						dataType: "json",
						success: function(data){
							showKGraph()
						},
					});
				});
			});
			</script>
		</div>
		<div class="Kdiv" id="Kgraph"></div>
	</div>
	
	<script src="http://echarts.baidu.com/build/dist/echarts.js"></script>
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
				type : "GET",
				url : "KGraph",
				data: {
					begindate: $("#begindate").val(), 
					enddate: $("#enddate").val()
				},
				dataType : "json",
				success : function(obj) {
					var resultJSONData = JSON.parse(obj);
					$("#LName").html(resultJSONData.LName);
					$("#LCode").html(resultJSONData.LCode);
					$("#LClose").html("昨收:"+resultJSONData.LClose);
					$("#LOpen").html("今开:"+resultJSONData.LOpen);
					$("#LHighest").html("最高:"+resultJSONData.LHighest);
					$("#LLowest").html("最低:"+resultJSONData.LLowest);
					$("#LVolumn").html("成交量:"+resultJSONData.LVolumn);
					$("#LMoney").html("成交额:"+resultJSONData.LMoney+"万元");
					
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
			});
			
			}
		
	</script>
</body>
</html>