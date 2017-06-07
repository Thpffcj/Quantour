<%@ page language="java" import="java.util.*"
	contentType="text/html; charset=utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!DOCTYPE html>
<html lang = "en">
<head>
<meta charset="UTF-8">
<title>首页</title>
<link rel="stylesheet" href="https://cdn.bootcss.com/bootstrap/3.3.7/css/bootstrap.min.css">
<script src="http://cdn.static.runoob.com/libs/jquery/2.1.1/jquery.min.js"></script>
<script src="https://cdn.bootcss.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
<link rel="stylesheet" type="text/css" href = "css/index.css"> 
<link rel="stylesheet" type="text/css" href = "css/Main.css">
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
			<ul id = "rightul">
				<li><a href="Register.jsp"><span class="glyphicon glyphicon-user"></span>注册</a></li>
				<li><a href="Login.jsp"><span class="glyphicon glyphicon-log-in"></span>登录</a></li>
			</ul>
			<ul id="userul" style = "display:none;">
				<li class="firstli"><img src="images/photo.png"></li>
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
		<input type="text" placeholder="输入股票代码/名称" id="code"> 
		<a href="javascript:void(0)" target="_blank" id="searchcode"><button type="button" class="search">搜索</button></a>
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
	
	<div id = "introduction">
		<p class = "text-center">一个为您精心打造的股票管家</p>
		<a href = "Login.jsp" id="begin"><button type = "button" class = "btn">登录使用</button></a>
	</div>
	
	
	
	<script>
	$(document).ready(function(){
		var name = $("#loginUserName").text();
		console.log(name);
		if(name==""||name==null){
			$("#rightul").attr("style","");
			$("#userul").attr("style","display:none;");
			$("#begin").attr("href","Login.jsp");
			$("#begin").html("<button type=\"button\" class=\"btn\">登录使用</button>");
			var s = "<li><a><img src=\"images/Icon.jpg\"></a></li>"+"<li><a href=\"Login.jsp\">首页</a></li>"
					+"<li><a href=\"Login.jsp\">行情中心</a></li>"+"<li><a href=\"Login.jsp\">个股展示</a></li>"
					+"<li><a href=\"Login.jsp\">股票比较</a></li>"+"<li><a href=\"Login.jsp\">策略回测</a></li>"
					+"<li><a href=\"Login.jsp\">板块</a></li>";
			$("#navul").html(s);
		}else{
			$("#rightul").attr("style","display:none;");
			$("#userul").attr("style","");
			$("#begin").attr("href","Market.jsp");
			$("#begin").html("<button type=\"button\" class=\"btn\">开始使用</button>");
			var s = "<li><a><img src=\"images/Icon.jpg\"></a></li>"+"<li><a href=\"Main.jsp\">首页</a></li>"
					+"<li><a href=\"Market.jsp\">行情中心</a></li>"+"<li><a href=\"Stock.jsp\">个股展示</a></li>"
					+"<li><a href=\"StockVS.jsp\">股票比较</a></li>"+"<li><a href=\"Strategy.jsp\">策略回测</a></li>"
					+"<li><a href=\"Plate.jsp\">板块</a></li>";
			$("#navul").html(s);
		}
	});

	function logout(){
		$.ajax({
			type : "GET",
			url : "Logout",
			dataType : "json",
			success: function(obj) {
			}
		});
		
		$("#rightul").attr("style","");
		$("#userul").attr("style","display:none;");
		$("#begin").attr("href","Login.jsp");
		$("#begin").html("<button type=\"button\" class=\"btn\">登录使用</button>");
		var s = "<li><a><img src=\"images/Icon.jpg\"></a></li>"+"<li><a href=\"Login.jsp\">首页</a></li>"
				+"<li><a href=\"Login.jsp\">行情中心</a></li>"+"<li><a href=\"Login.jsp\">个股展示</a></li>"
				+"<li><a href=\"Login.jsp\">股票比较</a></li>"+"<li><a href=\"Login.jsp\">策略回测</a></li>"
				+"<li><a href=\"Login.jsp\">板块</a></li>";
		$("#navul").html(s);
	}
	</script>
</body>
</html>