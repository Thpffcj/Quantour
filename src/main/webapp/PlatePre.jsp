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
<title>板块展示</title>
<link rel="stylesheet" href="https://cdn.bootcss.com/bootstrap/3.3.7/css/bootstrap.min.css">
<script
	src="http://cdn.static.runoob.com/libs/jquery/2.1.1/jquery.min.js"></script>
<script
	src="https://cdn.bootcss.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
<link rel="stylesheet" type="text/css" href = "css/index.css"> 
<link rel="stylesheet" type="text/css" href = "css/PlatePre.css">
<link rel="stylesheet"
	href="//apps.bdimg.com/libs/jqueryui/1.10.4/css/jquery-ui.min.css">
<script src="//apps.bdimg.com/libs/jqueryui/1.10.4/jquery-ui.min.js"></script>
<script src="theme/echarts.js"></script>
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
			<h2>板块简介</h2>
		</div>
		<div class="basicmess">
			<div class="name">
				<h2 id="LName"></h2>
			</div>
			<div class="messtable">
				<table>
					<tr>
						<td id="LOpen">今开:</td>
						<td id="Lowest">最低:</td>
						<td id="Average">均价:</td>
						<td id="Money">成交额:</td>
					</tr>
					<tr>
						<td id="LClose">昨收:</td>
						<td id="Highest">最高:</td>
						<td id="Volume">成交量:</td>
						<td id="Fluct">板块涨跌幅:</td>
					</tr>
				</table>
			</div>
		</div>
		<div class="kgraphpre" id="Kgraph">
		</div>
		<div class="head">
			<h2>成分股涨跌排行榜</h2>
		</div>
		<div class="table-container">
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
							<th style="text-align:center;">加股票池</th>
					</tr>
				</thead>
				<tbody id="children-list">
				</tbody>
			</table>
			<div class="m-page">
				<ul id="i-page">
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
	
	<!-- 模态框（Modal） -->
	<div class="modal fade" id="addpool" tabindex="-1" role="dialog"
		aria-labelledby="myModalLabel" aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-hidden="true">×</button>
					<h4 class="modal-title" id="myModalLabel">加入股票池</h4>
				</div>
				<div class="modal-body">
					<label>按下 ESC 按钮退出。</label> <br> 
					选择股票池:&emsp;
					<select>
						<option value="默认">默认</option>
					</select>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
					<a href="User.jsp"><button type="button" class="btn btn-primary">确定</button></a>
				</div>
			</div>
			<!-- /.modal-content -->
		</div>
		<!-- /.modal-dialog -->
	</div>
	<!-- /.modal -->
	
	<script src="http://echarts.baidu.com/build/dist/echarts.js"></script>
	<script src="theme/dark.js"></script>
	<script type="text/javascript">
	$(document).ready(function(){
		getChildrenMessage(0);
		
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
			url : "PlateKGraph",
			dataType : "json",
			success : function(obj) {
				var resultJSONData = JSON.parse(obj);
				$("#LName").html(resultJSONData.LName);
				$("#LClose").html("昨收:"+resultJSONData.LClose);
				$("#LOpen").html("今开:"+resultJSONData.LOpen);
				$("#Highest").html("最高:"+resultJSONData.LHighest);
				$("#Lowest").html("最低:"+resultJSONData.LLowest);
				$("#Volume").html("成交量:"+resultJSONData.LVolumn);
				$("#Money").html("成交额:"+resultJSONData.LMoney+"万元");
				$("#Fluct").html("涨跌幅:"+resultJSONData.LFluct+"%");
				$("#Average").html("涨跌幅:"+resultJSONData.Average+"%");
				
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

				var myChart = echarts.init(document.getElementById('Kgraph'));

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
	});
	</script>
	
	<script type="text/javascript">
	function getChildrenMessage(p){
		$.ajax({
			type : "POST",
			url : "ChildrenMessage",
			data: {
				page: p,
			},
			dataType : "json",
			success : function(obj) {
				var result = JSON.parse(obj);
				var index = result.index;
				var code = result.code;
				var name = result.name;
				var open = result.open;
				var high = result.high;
				var low = result.low;
				var close = result.close;
				var fluct = result.fluct;
				var volume = result.volume;
				var length = result.length;
				
				var s = "";
				for(var i=0;i<10;i++){
					if(index[i]!=null){
						s = s+"<tr><td>"+index[i]+"</td><td><a href=\"Stock.jsp\" target=\"_blank\" onclick=\"SearchStock('"+
							code[i]+"')\">"+code[i]+"</a></td><td><a href=\"Stock.jsp\" target=\"_blank\" onclick=\"SearchStock('"+
							code[i]+"')\">"+name[i]+"</a></td><td>"+open[i]+"</td><td>"
							+high[i]+"</td><td>"+low[i]+"</td><td>"+close[i]+"</td><td>"+fluct[i]+"</td><td>"+volume[i]
							+"</td><td><a class=\"plus\" data-toggle=\"modal\" data-target=\"#addpool\"><span class=\"glyphicon glyphicon-plus\"></span></a></td><tr>";
					}
				}
				$("#children-list").html(s);
				
				var s1 = "<li><a onclick=\"getChildrenMessage(0)\">首页</a></li>";
				var last = p-1;
				if(last<0){
					s1 = s1+"<li class=\"disable\"><a onclick=\"getChildrenMessage("+last+")\">上一页</a></li>";
				}else{
					s1 = s1+"<li><a onclick=\"getChildrenMessage("+last+")\">上一页</a></li>";
				}
				var page = p-p%5+1;
				if((page>(length-4))&&(length>5)){
					page = length-4;
				}
				if(length<5){
					for(var i=0;i<length;i++){
						var k = page+i;
						if(k==(p+1)){
							var j = k-1;
							s1 = s1+"<li class=\"active\"><a onclick=\"getChildrenMessage("+j+")\">"+k+"</a></li>";
						}else{
							var j = k-1;
							s1 = s1+"<li><a onclick=\"getChildrenMessage("+j+")\">"+k+"</a></li>";
						}
					}
				}else{
					for(var i=0;i<5;i++){
						var k = page+i;
						if((k==(p+1))){
							var j = k-1;
							s1 = s1+"<li class=\"active\"><a onclick=\"getChildrenMessage("+j+")\">"+k+"</a></li>";
						}else{
							var j = k-1;
							s1 = s1+"<li><a onclick=\"getChildrenMessage("+j+")\">"+k+"</a></li>";
						}
					}
				}
				var next = p+1;
				if(p==(length-1)){
					s1 = s1+"<li class=\"disable\"><a onclick=\"getChildrenMessage("+next+")\">下一页</a></li>";
				}else{
					s1 = s1+"<li><a onclick=\"getChildrenMessage("+next+")\">下一页</a></li>";
				}
				var x = length-1;
				s1 = s1+"<li><a onclick=\"getChildrenMessage("+x+")\">尾页</a></li>";
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