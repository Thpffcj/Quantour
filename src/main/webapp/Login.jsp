<%@ page language="java" import="java.util.*" contentType="text/html; charset=utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<!DOCTYPE html>
<html lang = "en">
<head>
<meta charset="UTF-8">
<title>登录</title>
<link rel="stylesheet" href="https://cdn.bootcss.com/bootstrap/3.3.7/css/bootstrap.min.css">
<script src="http://cdn.static.runoob.com/libs/jquery/2.1.1/jquery.min.js"></script>
<script src="https://cdn.bootcss.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
<link rel="stylesheet" type="text/css" href="css/Login.css">
<link rel="stylesheet" type="text/css" href="css/index.css">
</head>
<body>
	<nav id = "navbar">
		<div>
			<div>
			<ul id = "navul">
				<li><a><img src="images/Icon.jpg"></a></li>
				<li><a href="Main.jsp">首页</a></li>
				<li><a href="#">行情中心</a></li>
				<li><a href="#">个股展示</a></li>
				<li><a href="#">股票比较</a></li>
				<li><a href="#">策略回测</a></li>
				<li><a href="#">板块</a>	</li>
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
						<li><a href="#">个人中心</a></li>
						<li><a href="#">退出登录</a></li>
					</ul>
				</li>
			</ul>
		</div>
	</nav>

	<p>欢迎回来</p>

	<form>
		<div id="input">
			<a href="Register.jsp">没有账号？立即注册</a>
			<br> <br> <br>
			<label>用户名:</label>
			<input type="text" id="username" placeholder="请输入用户名"> <br> <br>
			<label>密码:</label> 
			<input type="password" id="password" placeholder="请输入密码"> <br> <br>
			<button type="button" class = "btn" onclick="Login()">登录</button>
		</div>
	</form>
	
	<div class="modal fade" id="login-modal" tabindex="-1" role="dialog"
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
					<strong id="login-modal-prompt">登录成功!</strong>
				</div>
				<div class="modal-footer">
					<a href="Main.jsp" id="modal-yes"><button type="submit" class="btn btn-primary">确定</button></a>
				</div>
			</div>
			<!-- /.modal-content -->
		</div>
		<!-- /.modal-dialog -->
	</div>
	
	<script>
	function Login(){
		$.ajax({
			type : "POST",
			url : "Login",
			data: {
				username: $("#username").val(),
				password: $("#password").val(),
			},
			dataType : "json",
			success: function(obj) {
				var result = JSON.parse(obj);
				
				if(result.result=="success"){
					$("#login-modal-prompt").html("登录成功!");
					$("#modal-yes").attr("href","Main.jsp");
					$("#modal-yes").html("<button type=\"submit\" class=\"btn btn-primary\">确定</button>");
					$("#login-modal").modal('show');
				}else if(result.result=="failure"){
					$("#login-modal-prompt").html("用户名或密码错误!");
					$("#modal-yes").attr("href","#");
					$("#modal-yes").html("<button type=\"submit\" class=\"btn btn-primary\" data-dismiss=\"modal\">确定</button>");
					$("#login-modal").modal('show');
				}else{
					$("#login-modal-prompt").html("用户名与密码不能为空!");
					$("#modal-yes").attr("href","#");
					$("#modal-yes").html("<button type=\"submit\" class=\"btn btn-primary\" data-dismiss=\"modal\">确定</button>");
					$("#login-modal").modal('show');
				}
			}
		});
	}
	</script>
</body>
</html>
