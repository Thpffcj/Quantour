<%@ page language="java" import="java.util.*" contentType="text/html; charset=utf-8" %>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html>
<html  lang="en">
<head>
<meta charset="UTF-8">
<title>策略回测</title>
<link rel="stylesheet" href="https://cdn.bootcss.com/bootstrap/3.3.7/css/bootstrap.min.css">
<script
	src="http://cdn.static.runoob.com/libs/jquery/2.1.1/jquery.min.js"></script>
<script
	src="https://cdn.bootcss.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
<link rel="stylesheet" type="text/css" href = "css/index.css"> 
<link rel = "stylesheet" href = "css/Strategy.css">
<link rel="stylesheet" href="//apps.bdimg.com/libs/jqueryui/1.10.4/css/jquery-ui.min.css">
<script src="//apps.bdimg.com/libs/jquery/1.10.2/jquery.min.js"></script>
<script src="//apps.bdimg.com/libs/jqueryui/1.10.4/jquery-ui.min.js"></script>
<script src="theme/echarts.js"></script>
<script>
$(document).ready(function(){
    $("#begindate1").datepicker({
    	onSelect: function(selected) {
          $("#enddate1").datepicker("option","minDate", selected)
        }
    });
    $("#begindate1").datepicker( "option", "showAnim", "clip");
    $("#enddate1").datepicker({ 
        onSelect: function(selected) {
           $("#begindate1").datepicker("option","maxDate", selected)
        }
    });  
    $("#enddate1").datepicker( "option", "showAnim", "clip");
    $("#begindate2").datepicker({
    	onSelect: function(selected) {
          $("#enddate2").datepicker("option","minDate", selected)
        }
    });
    $("#begindate2").datepicker( "option", "showAnim", "clip");
    $("#enddate2").datepicker({ 
        onSelect: function(selected) {
           $("#begindate2").datepicker("option","maxDate", selected)
        }
    });  
    $("#enddate2").datepicker( "option", "showAnim", "clip");
    $("#slider1").slider({
        min: 1,
        max: 100,
        value: 1,
        slide: function( event, ui ) {
          $( "#formation" ).val( ui.value );
        }
    });
    $( "#formation" ).val( $( "#slider1" ).slider( "value" ) );
    $("#slider2").slider({
        min: 1,
        max: 100,
        value: 1,
        slide: function( event, ui ) {
          $( "#holding" ).val( ui.value );
        }
    });
    $( "#holding" ).val( $( "#slider2" ).slider( "value" ) );
    $("#slider3").slider({
        min: 1,
        max: 100,
        value: 1,
        slide: function( event, ui ) {
          $( "#holding2" ).val( ui.value );
        }
    });
    $( "#holding2" ).val( $( "#slider3" ).slider( "value" ) );
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
		<input type="text" placeholder="输入股票代码/名称">
		<button type="submit" class="search">搜索</button>
	</div>
	
	<div class="w1200">
		<div class="head">
			<a class="active" id="strategy1" onclick="way1()">动量策略</a>
			<a class="normal" id="strategy2" onclick="way2()">均值回归</a>
			<br>
		</div>
		<div id="inputdiv1" class="input-form show">
			<form>
				<label>选择股票池:</label>
				<select id = "stockpool">
					<option value = "全部">全部</option>
				</select>
				<button type="button" class="btn-plus" data-toggle="modal" data-target="#addpool">
					<span class="glyphicon glyphicon-plus"></span></button>
				<br><br>
				<label>回测时间:&emsp;</label>
				<input type = "text" id = "begindate1" value="04/18/2017" placeholder="请选择开始日期">
				<label>---</label>
				<input type = "text" id = "enddate1" value="05/18/2017" placeholder="请选择结束日期">
				<label>持有股票数:</label>
				<input type = "number" id = "number" placeholder="请输入持有股票数">
				<br><br>
				<label>形成期:</label>
				<label id="hold">持有期:</label>
				<div id="slider1"></div>
				<output id="formation" style="color:#f6931f; font-weight:bold;position:relative;left:265px;top:-44px;width:100px;"></output>
				<div id="slider2"></div>
				<output id="holding"  style="color:#f6931f; font-weight:bold;position:relative;left:565px;top:-85px;width:100px;"></output>
				<br>
				<button type="submit" class="btn" style="position:relative;left: 700px;top: -134px;">开始回测</button>
			</form>
		</div>
		<div id="inputdiv2" class="input-form">
			<form>
				<label>选择股票池:</label>
				<select id = "stockpool">
					<option value = "全部">全部</option>
				</select>
				<button type="button" class="btn-plus" data-toggle="modal" data-target="#addpool"><span class="glyphicon glyphicon-plus"></span></button>
				<label>回测时间:</label>
				<input type = "text" id = "begindate2" value="04/18/2017" placeholder="请选择开始日期">
				<label>---</label>
				<input type = "text" id = "enddate2" value="05/18/2017" placeholder="请选择结束日期">
				<br><br>
				<label>&emsp;均&emsp;线:&emsp;</label>
				<select id = "stockpool">
					<option value = "5日">5日</option>
					<option value = "10日">10日</option>
					<option value = "20日">20日</option>
				</select>
				<label>持有期:</label>
				<div id="slider3"></div>
				<output id="holding2" style="color:#f6931f;font-weight:bold;position:relative;left:565px;top:-45px;width:100px;"></output>
				<br>
				<button type="submit" class="btn" style="position:relative;left: 700px;top: -94px;">开始回测</button>
			</form>
			<br><br>
		</div>
		<div class="graph1">
		<!-- 此处显示回测图标 -->
			<div class="tablist">
				<div class="item active" id="item1" onMouseOver="mouse1()">
					<h3>累计收益</h3>
				</div>
				<div class="item" id="item2" onMouseOver="mouse2()">
					<h3>超额收益</h3>
				</div>
				<div class="item" id="item3" onMouseOver="mouse3()">
					<h3>策略胜率</h3>
				</div>
				<div class="item" id="item4" onMouseOver="mouse4()">
					<h3>收益分布</h3>
				</div>
			</div>
			<div class="result">
				<div class="graph active" id="back-flow-graph1">
				</div>
				<div class="graph" id="back-flow-graph2">
				</div>
				<div class="graph" id="back-flow-graph3">
				</div>
				<div class="graph" id="back-flow-graph4">
				</div>
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
					<h4 class="modal-title" id="myModalLabel">新增股票池</h4>
				</div>
				<div class="modal-body">
					<label>按下 ESC 按钮退出。</label> <br> 
					股票池名称:&emsp;
					<input type="text" value=""
						placeholder="输入股票池的名称">
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
	
	<script type="text/javascript">
	function way1(){
		document.getElementById("strategy1").className = "active";
		document.getElementById("strategy2").className = "normal";
		document.getElementById("inputdiv1").className = "input-form show";
		document.getElementById("inputdiv2").className = "input-form";
	}
	function way2(){
		document.getElementById("strategy1").className = "normal";
		document.getElementById("strategy2").className = "active";
		document.getElementById("inputdiv1").className = "input-form";
		document.getElementById("inputdiv2").className = "input-form show";
	}
	function mouse1(){
		document.getElementById("item1").className = "item active";
		document.getElementById("item2").className = "item";
		document.getElementById("item3").className = "item";
		document.getElementById("item4").className = "item";
		document.getElementById("back-flow-graph1").className = "graph active";
		document.getElementById("back-flow-graph2").className = "graph";
		document.getElementById("back-flow-graph3").className = "graph";
		document.getElementById("back-flow-graph4").className = "graph";
	}
	function mouse2(){
		document.getElementById("item1").className = "item";
		document.getElementById("item2").className = "item active";
		document.getElementById("item3").className = "item";
		document.getElementById("item4").className = "item";
		document.getElementById("back-flow-graph1").className = "graph";
		document.getElementById("back-flow-graph2").className = "graph active";
		document.getElementById("back-flow-graph3").className = "graph";
		document.getElementById("back-flow-graph4").className = "graph";
	}
	function mouse3(){
		document.getElementById("item1").className = "item";
		document.getElementById("item2").className = "item";
		document.getElementById("item3").className = "item active";
		document.getElementById("item4").className = "item";
		document.getElementById("back-flow-graph1").className = "graph";
		document.getElementById("back-flow-graph2").className = "graph";
		document.getElementById("back-flow-graph3").className = "graph active";
		document.getElementById("back-flow-graph4").className = "graph";
	}
	function mouse4(){
		document.getElementById("item1").className = "item";
		document.getElementById("item2").className = "item";
		document.getElementById("item3").className = "item";
		document.getElementById("item4").className = "item active";
		document.getElementById("back-flow-graph1").className = "graph";
		document.getElementById("back-flow-graph2").className = "graph";
		document.getElementById("back-flow-graph3").className = "graph";
		document.getElementById("back-flow-graph4").className = "graph active";
	}
	</script>
	<script>
		$(function () { $("[data-toggle='tooltip']").tooltip(); });
	</script>
	
	<script src="http://echarts.baidu.com/build/dist/echarts.js"></script>
	<script src="theme/dark.js"></script>
	<script type="text/javascript">

		var date = new Array;
		var value = new Array;
		var sname;

		var colors = [ '#5793f3', '#d14a61', '#675bba' ];

		$.ajax({
			type : "GET",
			url : "RSIGraph",
			dataType : "json",
			success : function(obj) {
				var resultJSONData = JSON.parse(obj);
				for (i = 0; i < resultJSONData.Dates.length; i++) {
					date[i] = resultJSONData.Dates[i];
					value[i] = resultJSONData.Values[i];
				}
				name =  resultJSONData.Name;
				
				var myChart = echarts.init(document
						.getElementById('back-flow-graph1'));
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
						data : sname
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

					],
					yAxis : [ {
						scale: true,
						type : 'value'
					} ],
					series : [
							{
								name : sname,
								type : 'line',
								smooth : true,
								data : value
							} ]
				}
				myChart.setOption(option);
			}
		});
	</script>
	
	<script src="http://echarts.baidu.com/build/dist/echarts.js"></script>
	<script src="theme/dark.js"></script>
	<script type="text/javascript">

		var date = new Array;
		var K = new Array;
		var D = new Array;
		var J = new Array;
		var sname;

		var colors = [ '#FFFFFF', '#FFFF00', '#A020F0' ];

		$.ajax({
			type : "GET",
			url : "KDJStochasticGraph",
			dataType : "json",
			success : function(obj) {
				var resultJSONData = JSON.parse(obj);
				for (i = 0; i < resultJSONData.Dates.length; i++) {
					date[i] = resultJSONData.Dates[i];
					K[i] = resultJSONData.K[i];
					D[i] = resultJSONData.D[i];
					J[i] = resultJSONData.J[i];
				}
				name =  resultJSONData.Name;
				
				var myChart = echarts.init(document
						.getElementById('back-flow-graph2'));
				option = {
					color : colors,
					backgroundColor : '#eee',
					 title: {
					        text: sname,
					        x: 'center'
					    },
					tooltip : {
						trigger : 'none',
						axisPointer : {
							type : 'cross'
						}
					},
					legend : {
						top : 10,
						left : 'center',
						data : ['K','D','J']
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
									color : colors[0]
								}
							},
							axisPointer : {
								label : {
									formatter : function(params) {
										return ' ' + params.value + (params.seriesData.length ? '：' 
												+ params.seriesData[0].data : '')
												+ ' ' + params.value + (params.seriesData.length ? '：' 
														+ params.seriesData[1].data : '')
												+ ' ' + params.value + (params.seriesData.length ? '：' 
														+ params.seriesData[2].data : '');
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
								name : 'K',
								type : 'line',
								smooth : true,
								data : K
							},
							{
								name : 'D',
								type : 'line',
								smooth : true,
								data : D
							},
							{
								name : 'J',
								type : 'line',
								smooth : true,
								data : J
							} 
					]
				}
				myChart.setOption(option);
			}
		});
	</script>
	
</body>
</html>