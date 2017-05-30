<%@ page language="java" import="java.util.*" contentType="text/html; charset=utf-8" %>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html>
<html lang = "en">
<head>
<meta charset="UTF-8">
<title>注册</title>
<link rel="stylesheet" href="https://cdn.bootcss.com/bootstrap/3.3.7/css/bootstrap.min.css">
<link rel="stylesheet" href="https://cdn.bootcss.com/bootstrap/3.3.7/css/bootstrap-theme.min.css">
<script src="https://cdn.bootcss.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
<link rel="stylesheet" type="text/css" href = "${pageContext.request.contextPath }/css/index.css">
<link rel="stylesheet" type="text/css" href = "${pageContext.request.contextPath }/css/Register.css"> 
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
				</ul>
			</div>
			<ul id = "rightul">
				<li><a href="Register.jsp"><span class="glyphicon glyphicon-user"></span>注册</a></li>
				<li><a href="Login.jsp"><span class="glyphicon glyphicon-log-in"></span>登录</a></li>
			</ul>
			<ul id="userul" style = "display:none;">
				<li class="firstli"><img src="images/photo.png"></li>
				<li class="firstli"><a href="javascript:void(0)">用户名</a>
					<ul class="drop">
						<li><a href="User.jsp">个人中心</a></li>
						<li><a href="Main.jsp">退出登录</a></li>
					</ul>
				</li>
			</ul>
		</div>
	</nav>
	
	<p>加入我们</p>

	<s:form action="Users_register" method="post" namespace="/">
		<div id="input">
			<a href="Login.jsp">已有账号？前往登录</a> <br> <br> <br>
			<label>用户名:</label> 
			<input type="text" id="username" name="username" placeholder = "请输入用户名"> <br> <br>
			<label>密码:</label> 
			<input type="password" id="password1" name="password" placeholder = "请输入密码"> <br> <br>
			<label>确认密码:</label> 
			<input type="password" id="password2" placeholder = "请再次输入密码"> <br>
			<button type="submit" class = "btn">确认注册</button>
		</div>
	</s:form>
</body>
</html>