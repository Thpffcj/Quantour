<%@ page language="java" import="java.util.*"
	contentType="text/html; charset=utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<title>账号设置</title>
<link rel="stylesheet"
	href="https://cdn.bootcss.com/bootstrap/3.3.7/css/bootstrap.min.css">
<script
	src="http://cdn.static.runoob.com/libs/jquery/2.1.1/jquery.min.js"></script>
<script
	src="https://cdn.bootcss.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath }/css/index.css">
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath }/css/User.css">
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
				<li class="firstli"><a href="javascript:void(0)">用户名</a>
					<ul class="drop">
						<li><a href="User.jsp">个人中心</a></li>
						<li><a href="Main.jsp">退出登录</a></li>
					</ul></li>
			</ul>
		</div>
	</nav>

	<div class="w1200">
		<div class="navigation">
			<ul class="tab">
				<li><span class="glyphicon glyphicon-star"></span>&ensp;股票研究</li>
				<li id="id4" onclick="click4()" class="less active">&emsp;股票池&emsp;</li>
				<li><span class="glyphicon glyphicon-cog"></span>&ensp;账号设置</li>
				<li id="id1" onclick="click1()" class="less normal">&emsp;编辑昵称</li>
				<li id="id2" onclick="click2()" class="less normal">&emsp;修改密码</li>
				<li id="id3" onclick="click3()" class="less normal">&emsp;上传头像</li>
			</ul>
		</div>
		<div class="operate-area">
			<div id="self" class="selfstock active">
				<div class="header">
					<label>请选择股票池:&ensp;</label> <select>
						<option value="默认">默认</option>
					</select> 
					<a data-toggle="modal" data-target="#rename">重命名</a> 
					<a data-toggle="modal" data-target="#addpool"> <span
						class="glyphicon glyphicon-plus"></span>新增</a>
					<a data-toggle="modal" data-target="#deletepool"> <span
						class="glyphicon glyphicon-trash"></span>删除</a> 
					<input type="text" placeholder="输入股票代码/名称">
					<button type="button">添加</button>
					>
				</div>
				<div class="list">
					<table class="table table-hover table-striped">
						<thead>
							<tr>
								<th>序号</th>
								<th>股票代码</th>
								<th>股票名称</th>
								<th>最新价</th>
								<th>涨跌幅</th>
								<th>删除</th>
							</tr>
						</thead>
						<tbody>
							<tr>
								<th>1</th>
								<th>000001</th>
								<th>平安银行</th>
								<th>9.03</th>
								<th>2.5%</th>
								<th style="cursor: pointer;" data-toggle="modal"
									data-target="#deletestock"><span
									class="glyphicon glyphicon-trash"></span></th>
							</tr>
						</tbody>
					</table>
				</div>
			</div>
			<div class="input-form" id="input1">
				<!-- 修改昵称 -->
				<form>
					<br> 
					<label>昵称:</label> 
					<input type="text" value="用户名"
						id="username" placeholder="请输入昵称"> 
					<br>
					<button type="submit" class="btn"
						style="position: relative; left: 100px;">确认更新</button>
				</form>
			</div>
			<div class="input-form" id="input2">
				<form>
					<label>当前密码:&emsp; </label> 
					<input type="password" id="oldpassword"
						placeholder="请输入当前密码"> 
					<br> 
					<label>&ensp;新密码:&emsp;</label> 
					<input type="password" id="newpassword1"
						style="position: relative; left: 3px;" placeholder="请输入新密码">
					<br> 
					<label>确认新密码:</label> 
					<input type="password"
						id="newpassword2" placeholder="请再次输入新密码">
					<br>
					<button type="submit" class="btn"
						style="position: relative; left: 120px;">确认修改</button>
				</form>
			</div>
			<div class="input-form" id="input3">
				<form action="UploadFile.ashx" method="post" enctype="multipart/form-data">
					<br> 
					<input type="file" accept="image/*" id="image"
						style="color: white;width:auto;">
					<button type="submit" class="btn"
						style="position: relative; left: 50px;">确认上传</button>
				</form>
			</div>
		</div>
	</div>

	<!-- 模态框（Modal） -->
	<div class="modal fade" id="rename" tabindex="-1" role="dialog"
		aria-labelledby="myModalLabel" aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-hidden="true">×</button>
					<h4 class="modal-title" id="myModalLabel">重命名您的股票池</h4>
				</div>
				<div class="modal-body">
					<label>按下 ESC 按钮退出。</label>
					<br> 
					<input type="text" value="默认"
						placeholder="输入股票池的新名称">
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
					<button type="button" class="btn btn-primary">提交更改</button>
				</div>
			</div>
			<!-- /.modal-content -->
		</div>
		<!-- /.modal-dialog -->
	</div>
	<!-- /.modal -->
	
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
					<button type="button" class="btn btn-primary">确定</button>
				</div>
			</div>
			<!-- /.modal-content -->
		</div>
		<!-- /.modal-dialog -->
	</div>
	<!-- /.modal -->

	<!-- 模态框（Modal） -->
	<div class="modal fade" id="deletepool" tabindex="-1" role="dialog"
		aria-labelledby="myModalLabel" aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-hidden="true">×</button>
					<h4 class="modal-title" id="myModalLabel">您确定删除该股票池吗？</h4>
				</div>
				<div class="modal-body">
					<label>按下 ESC 按钮退出。</label>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
					<button type="button" class="btn btn-primary">确定</button>
				</div>
			</div>
			<!-- /.modal-content -->
		</div>
		<!-- /.modal-dialog -->
	</div>
	<!-- /.modal -->

	<!-- 模态框（Modal） -->
	<div class="modal fade" id="deletestock" tabindex="-1" role="dialog"
		aria-labelledby="myModalLabel" aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-hidden="true">×</button>
					<h4 class="modal-title" id="myModalLabel">您确定删除该股票吗？</h4>
				</div>
				<div class="modal-body">
					<label>按下 ESC 按钮退出。</label>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
					<button type="button" class="btn btn-primary">确定</button>
				</div>
			</div>
			<!-- /.modal-content -->
		</div>
		<!-- /.modal-dialog -->
	</div>
	<!-- /.modal -->

	<script>
		function click1() {
			document.getElementById("id1").className = "less active";
			document.getElementById("id2").className = "less";
			document.getElementById("id3").className = "less";
			document.getElementById("id4").className = "less";
			document.getElementById("input1").className = "input-form active";
			document.getElementById("input2").className = "input-form";
			document.getElementById("input3").className = "input-form";
			document.getElementById("self").className = "selfstock";
		}
		function click2() {
			document.getElementById("id1").className = "less";
			document.getElementById("id2").className = "less active";
			document.getElementById("id3").className = "less";
			document.getElementById("id4").className = "less";
			document.getElementById("input1").className = "input-form";
			document.getElementById("input2").className = "input-form active";
			document.getElementById("input3").className = "input-form";
			document.getElementById("self").className = "selfstock";
		}
		function click3() {
			document.getElementById("id1").className = "less";
			document.getElementById("id2").className = "less";
			document.getElementById("id3").className = "less active";
			document.getElementById("id4").className = "less";
			document.getElementById("input1").className = "input-form";
			document.getElementById("input2").className = "input-form";
			document.getElementById("input3").className = "input-form active";
			document.getElementById("self").className = "selfstock";
		}
		function click4() {
			document.getElementById("id1").className = "less";
			document.getElementById("id2").className = "less";
			document.getElementById("id3").className = "less";
			document.getElementById("id4").className = "less active";
			document.getElementById("input1").className = "input-form";
			document.getElementById("input2").className = "input-form";
			document.getElementById("input3").className = "input-form";
			document.getElementById("self").className = "selfstock active";
		}
	</script>
</body>
</html>