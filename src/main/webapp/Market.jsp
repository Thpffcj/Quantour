<%@ page language="java" import="java.util.*"
	contentType="text/html; charset=utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<title>行情中心</title>
<link rel="stylesheet"
	href="https://cdn.bootcss.com/bootstrap/3.3.7/css/bootstrap.min.css">
<link rel="stylesheet"
	href="https://cdn.bootcss.com/bootstrap/3.3.7/css/bootstrap-theme.min.css">
<script
	src="https://cdn.bootcss.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
<script src="http://cdn.static.runoob.com/libs/bootstrap/3.3.7/js/bootstrap.min.js"></script>
<link rel="stylesheet" type="text/css" href="css/index.css">
<link rel="stylesheet" href="css/Market.css">
<link rel="stylesheet"
	href="//apps.bdimg.com/libs/jqueryui/1.10.4/css/jquery-ui.min.css">
<script src="//apps.bdimg.com/libs/jquery/1.10.2/jquery.min.js"></script>
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
				<li class="firstli"><img src="images/photo.png"></li>
				<li class="firstli"><a href="javascript:void(0)">${sessionScope.loginUserName}</a>
					<ul class="drop">
						<li><a href="User.jsp">个人中心</a></li>
						<li><a href="Main.jsp">退出登录</a></li>
					</ul></li>
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
			<h2>沪深市场</h2>
		</div>
		<div class="title">
			<img src="images/icon1.png" style="width: 28px; height: 28px;">
			<label>涨跌分布</label>
		</div>

		<div>
				<label style="color: white; font-weight: 100;">请选择日期:</label>
				<input type="text" id="date" value="2015-05-05" name="date">
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
			<div class="flash"></div>
			<!-- 深证指数 -->
			<div class="flash"></div>
			<!-- 创业板指数 -->
			<div class="flash"></div>
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
				<table class="table table-hover table-striped table-bordered">
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
					<tbody id="listbody">
					</tbody>
				</table>
			</div>
		</div>
	</div>
	<br><br><br>
	
	<script type="text/javascript">
	function shiftplate1() {
		document.getElementById("plate1").className = "active";
		document.getElementById("plate2").className = "";
		document.getElementById("plate3").className = "";
	}
	function shiftplate2() {
		document.getElementById("plate1").className = "";
		document.getElementById("plate2").className = "active";
		document.getElementById("plate3").className = "";
	}
	function shiftplate3() {
		document.getElementById("plate1").className = "";
		document.getElementById("plate2").className = "";
		document.getElementById("plate3").className = "active";
	}
	</script>
	
	<!--  
	<script type="text/javascript">
	$(document).ready(function(){ 
		$.ajax({
			type : "GET",
			url : "StockMessage",
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
				
				var s = "";
				for(var i=0;i<10;i++){
					s = s+"<tr><td>"+index[i]+"</td><td>"+code[i]+"</td><td>"+name[i]+"</td><td>"+open[i]+"</td><td>"
						+high[i]+"</td><td>"+low[i]+"</td><td>"+close[i]+"</td><td>"+fluctuation[i]+"</td><td>"+volume[i]
						+"</td><tr>";
				}
				$("#listbody").html(s);
			}
		});
	});
	</script>
-->
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
					 $('#upList').append('<tr><td>'+upsName[i]+'</td><td>'+upsAmplitude[i]+'</td></tr>');
					 $('#downList').append('<tr><td>'+downsName[i]+'</td><td>'+downsAmplitude[i]+'</td></tr>');
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
</body>
</html>