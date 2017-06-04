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
<link rel="stylesheet" href="https://cdn.bootcss.com/bootstrap/3.3.7/css/bootstrap-theme.min.css">
<script src="https://cdn.bootcss.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
<link rel="stylesheet" type="text/css" href = "css/index.css"> 
<link rel="stylesheet" type="text/css" href = "css/PlatePre.css">
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
			<h2>板块简介</h2>
		</div>
		<div class="basicmess">
			<div class="name">
				<h2>北京</h2>
			</div>
			<div class="messtable">
				<table>
					<tr>
						<td>今开:</td>
						<td>昨收:</td>
						<td>最低:</td>
						<td>最高:</td>
						<td>均价:</td>
					</tr>
					<tr>
						<td>成交量:</td>
						<td>成交额:</td>
						<td>板块涨跌幅:</td>
						<td>上涨家数:</td>
						<td>下跌家数:</td>
					</tr>
				</table>
			</div>
		</div>
		<div class="kgraphpre">
		</div>
		<div class="head">
			<h2>成分股涨跌排行榜</h2>
		</div>
		<div class="table-container">
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
							<th style="text-align:center;">振幅(%)</th>
							<th style="text-align:center;">交易量</th>
							<th style="text-align:center;">加股票池</th>
					</tr>
				</thead>
				<tbody>
				</tbody>
			</table>
			<div class="m-page">
			</div>
		</div>
	</div>

</body>
</html>