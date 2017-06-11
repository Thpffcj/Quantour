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
				<li class="firstli"><img id="photo"></li>
				<li class="firstli"><a href="javascript:void(0)" id="loginUserName">${sessionScope.loginUserName}</a>
					<ul class="drop">
						<li><a href="User.jsp">个人中心</a></li>
						<li><a href="Main.jsp" onclick="logout()">退出登录</a></li>
					</ul></li>
			</ul>
		</div>
	</nav>

	<div class="w1200">
		<div class="navigation">
			<ul class="tab">
				<li><span class="glyphicon glyphicon-star"></span>&ensp;股票研究</li>
				<li id="id4" onclick="click4()" class="less active">&emsp;自选股&emsp;</li>
				<li><span class="glyphicon glyphicon-cog"></span>&ensp;账号设置</li>
				<li id="id1" onclick="click1()" class="less normal">&emsp;编辑昵称</li>
				<li id="id2" onclick="click2()" class="less normal">&emsp;修改密码</li>
				<li id="id3" onclick="click3()" class="less normal">&emsp;上传头像</li>
			</ul>
		</div>
		<div class="operate-area">
			<div id="self" class="selfstock active">
				<div class="header">
					<div class="stock-drop-list">
						<input type="text" placeholder="输入股票代码/名称" id="code">
						<ul id="match-list">
						</ul>
					</div>
					<button type="button" id="addselfstock">添加</button>
				</div>
				<div class="list">
					<table class="table table-hover table-striped">
						<thead>
							<tr>
								<th style="text-align:center;">序号</th>
								<th style="text-align:center;">股票代码</th>
								<th style="text-align:center;">股票名称</th>
								<th style="text-align:center;">最新价</th>
								<th style="text-align:center;">涨跌幅</th>
								<th style="text-align:center;">删除</th>
							</tr>
						</thead>
						<tbody id="selfstock-list">
						</tbody>
					</table>
					<div class="m-page">
						<ul id="i-page">
						</ul>
					</div>
				</div>
			</div>
			<div class="input-form" id="input1">
				<!-- 修改昵称 -->
				<form>
					<br> 
					<label>昵称:</label> 
					<input type="text" value=${sessionScope.loginUserName} id="username" placeholder="请输入昵称"> 
					<br>
					<button type="button" class="btn" id="change-name"
						style="position: relative; left: 100px;">确认更新</button>
				</form>
			</div>
			<div class="input-form" id="input2">
				<form>
					<label>当前密码:&emsp; </label> 
					<input type="password" id="oldpassword"
						placeholder="请输入当前密码"> 
					<br> 
					<label>&ensp;新密码:&ensp;&emsp;</label> 
					<input type="password" id="newpassword1" placeholder="请输入新密码">
					<br> 
					<label>确认新密码:</label> 
					<input type="password"
						id="newpassword2" placeholder="请再次输入新密码">
					<br>
					<button type="button" class="btn" id="change-password"
						style="position: relative; left: 120px;">确认修改</button>
				</form>
			</div>
			<div class="input-form" id="input3">
				<form action="UploadFile.ashx" method="post" enctype="multipart/form-data">
					<br> 
					<input type="file" id="image" onchange="selectImage(this)"
						style="color: white;width:auto;">
					<button type="button" class="btn" id="change-image" onclick="uploadImage()"
						style="position: relative; left: 50px;">确认上传</button>
				</form>
			</div>
		</div>
	</div>
	
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
					<button type="button" class="btn btn-primary" id="IsDelete">确定</button>
				</div>
			</div>
			<!-- /.modal-content -->
		</div>
		<!-- /.modal-dialog -->
	</div>
	
	<div class="modal fade" id="change-modal" tabindex="-1" role="dialog"
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
					<strong id="change-modal-prompt">修改成功!</strong>
				</div>
				<div class="modal-footer">
					<a href="User.jsp" id="modal-yes"><button type="submit" class="btn btn-primary">确定</button></a>
				</div>
			</div>
			<!-- /.modal-content -->
		</div>
		<!-- /.modal-dialog -->
	</div>

	<script>
		$(document).ready(function(){
			changepage(0);
		});
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
			changepage(0);
		}
		
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
	</script>
	
	<script>
	$("#change-name").click(function(){
		$.ajax({
			type : "POST",
			url : "ChangeName",
			data: {
				name: $("#username").val(),
			},
			dataType : "json",
			success: function(obj) {
				var result = JSON.parse(obj);
				
				if(result.result=="success"){
					$("#change-modal-prompt").html("修改成功!");
					$("#modal-yes").attr("href","User.jsp");
					$("#modal-yes").html("<button type=\"submit\" class=\"btn btn-primary\">确定</button>");
					$("#change-modal").modal('show');
				}else if(result.result=="same"){
					$("#change-modal-prompt").html("昵称尚未修改!");
					$("#modal-yes").attr("href","#");
					$("#modal-yes").html("<button type=\"submit\" class=\"btn btn-primary\" data-dismiss=\"modal\">确定</button>");
					$("#change-modal").modal('show');
				}else if(result.result=="null"){
					$("#change-modal-prompt").html("昵称不能为空!");
					$("#modal-yes").attr("href","#");
					$("#modal-yes").html("<button type=\"submit\" class=\"btn btn-primary\" data-dismiss=\"modal\">确定</button>");
					$("#change-modal").modal('show');
				}else{
					$("#change-modal-prompt").html("该昵称已被占用!");
					$("#modal-yes").attr("href","#");
					$("#modal-yes").html("<button type=\"submit\" class=\"btn btn-primary\" data-dismiss=\"modal\">确定</button>");
					$("#change-modal").modal('show');
				}
			}
		});
	});
	
	$("#change-password").click(function(){
		$.ajax({
			type : "POST",
			url : "ChangePassword",
			data: {
				oldpassword: $("#oldpassword").val(),
				newpassword1: $("#newpassword1").val(),
				newpassword2: $("#newpassword2").val(),
			},
			dataType : "json",
			success: function(obj) {
				var result = JSON.parse(obj);
				console.log(result.result);
				if(result.result=="success"){
					$("#change-modal-prompt").html("修改成功!");
					$("#modal-yes").attr("href","User.jsp");
					$("#modal-yes").html("<button type=\"submit\" class=\"btn btn-primary\">确定</button>");
					$("#change-modal").modal('show');
				}else if(result.result=="notsame"){
					$("#change-modal-prompt").html("两次密码不同!");
					$("#modal-yes").attr("href","#");
					$("#modal-yes").html("<button type=\"submit\" class=\"btn btn-primary\" data-dismiss=\"modal\">确定</button>");
					$("#change-modal").modal('show');
				}else if(result.result=="null"){
					$("#change-modal-prompt").html("密码不能为空!");
					$("#modal-yes").attr("href","#");
					$("#modal-yes").html("<button type=\"submit\" class=\"btn btn-primary\" data-dismiss=\"modal\">确定</button>");
					$("#change-modal").modal('show');
				}else if(result.result=="failure"){
					$("#change-modal-prompt").html("修改失败!");
					$("#modal-yes").attr("href","#");
					$("#modal-yes").html("<button type=\"submit\" class=\"btn btn-primary\" data-dismiss=\"modal\">确定</button>");
					$("#change-modal").modal('show');
				}else if(result.result=="falseold"){
					$("#change-modal-prompt").html("原密码错误!");
					$("#modal-yes").attr("href","#");
					$("#modal-yes").html("<button type=\"submit\" class=\"btn btn-primary\" data-dismiss=\"modal\">确定</button>");
					$("#change-modal").modal('show');
				}
			}
		});
	});
	
	function changepage(p){
		$.ajax({
			type: "POST",
			url: "getSelfStock",
			data: {
				page: p,
				username: $("#loginUserName").text(),
			},
			dataType: "json",
			success: function(obj){
				var result = JSON.parse(obj);
				var index = result.index;
				var code = result.code;
				var name = result.name;
				var close = result.close;
				var fluct = result.fluct;
				var length = result.length;
				
				var s = "";
				for(var i=0;i<10;i++){
					if(index[i]!=null){
						s = s+"<tr><td>"+index[i]+"</td><td><a href=\"Stock.jsp\" target=\"_blank\" onclick=\"SearchStock('"+
							code[i]+"')\">"+code[i]+"</a></td><td><a href=\"Stock.jsp\" target=\"_blank\" onclick=\"SearchStock('"+
							code[i]+"')\">"+name[i]+"</a></td><td>"+close[i]+"</td><td>"+fluct[i]+
							"</td><td style=\"cursor: pointer;\" onclick=\"deleteStock('"+code[i]+"')\"><span class=\"glyphicon glyphicon-trash\"></span></td><tr>";
						$("#selfstock-list").html(s);
					}
				}
				if(length!=1){
					var s1 = "<li><a onclick=\"changepage(0)\">首页</a></li>";
					var last = p-1;
					if(last<0){
						s1 = s1+"<li class=\"disable\"><a onclick=\"changepage("+last+")\">上一页</a></li>";
					}else{
						s1 = s1+"<li><a onclick=\"changepage("+last+")\">上一页</a></li>";
					}
					var page = p-p%3+1;
					if((page>(length-2))&&(length>3)){
						page = length-2;
					}
					if(length<3){
						for(var i=0;i<length;i++){
							var k = page+i;
							if((k-1)==p){
								var j = k-1;
								s1 = s1+"<li class=\"active\"><a onclick=\"changepage("+j+")\">"+k+"</a></li>";
							}else{
								var j = k-1;
								s1 = s1+"<li><a onclick=\"changepage("+j+")\">"+k+"</a></li>";
							}
						}
					}else{
						for(var i=0;i<3;i++){
							var k = page+i;
							if((k-1)==p){
								var j = k-1;
								s1 = s1+"<li class=\"active\"><a onclick=\"changepage("+j+")\">"+k+"</a></li>";
							}else{
								var j = k-1;
								s1 = s1+"<li><a onclick=\"changepage("+j+")\">"+k+"</a></li>";
							}
						}
					}
					var next = p+1;
					if(p==(length-1)){
						s1 = s1+"<li class=\"disable\"><a onclick=\changepage("+next+")\">下一页</a></li>";
					}else{
						s1 = s1+"<li><a onclick=\"changepage("+next+")\">下一页</a></li>";
					}
					console.log(length);
					var x = length-1;
					s1 = s1+"<li><a onclick=\"changepage("+x+")\">尾页</a></li>";
					
					$("#i-page").html(s1);
				}
			}
		});
	}
	
	$("#addselfstock").click(function(){
		$.ajax({
			type: "POST",
			url: "addSelfStock",
			data: {
				code: $("#code").val(),
				username: $("#loginUserName").text(),
			},
			dataType: "json",
			success: function(obj){
				var result = JSON.parse(obj);
				if(result.result=="success"){
					$("#change-modal-prompt").html("添加成功!");
					$("#modal-yes").attr("href","User.jsp");
					$("#modal-yes").html("<button type=\"submit\" class=\"btn btn-primary\">确定</button>");
					$("#change-modal").modal('show');
				}else if(result.result=="same"){
					$("#change-modal-prompt").html("股票已存在!");
					$("#modal-yes").attr("href","#");
					$("#modal-yes").html("<button type=\"submit\" class=\"btn btn-primary\" data-dismiss=\"modal\">确定</button>");
					$("#change-modal").modal('show');
				}else if(result.result=="null") {
					$("#change-modal-prompt").html("股票名称/代码不能为空!");
					$("#modal-yes").attr("href","#");
					$("#modal-yes").html("<button type=\"submit\" class=\"btn btn-primary\" data-dismiss=\"modal\">确定</button>");
					$("#change-modal").modal('show');
				}else if(result.result=="failure"){
					$("#change-modal-prompt").html("添加失败!");
					$("#modal-yes").attr("href","#");
					$("#modal-yes").html("<button type=\"submit\" class=\"btn btn-primary\" data-dismiss=\"modal\">确定</button>");
					$("#change-modal").modal('show');
				}else if(result.result=="unknow"){
					$("#change-modal-prompt").html("无效股票名称/代码!");
					$("#modal-yes").attr("href","#");
					$("#modal-yes").html("<button type=\"submit\" class=\"btn btn-primary\" data-dismiss=\"modal\">确定</button>");
					$("#change-modal").modal('show');
				}
			}
		});
	});
	
	function deleteStock(c){
		$("#IsDelete").attr("onclick","deleteSelfStock('"+c+"')");
		$("#deletestock").modal('show');
	}
	
	function deleteSelfStock(c){
		$.ajax({
			type: "POST",
			url: "deleteSelfStock",
			data: {
				code: c,
				username: $("#loginUserName").text(),
			},
			dataType: "json",
			success: function(obj){
				var result = JSON.parse(obj);
				if(result.result=="success"){
					$("#change-modal-prompt").html("删除成功!");
					$("#modal-yes").attr("href","User.jsp");
					$("#modal-yes").html("<button type=\"submit\" class=\"btn btn-primary\">确定</button>");
					$("#change-modal").modal('show');
				}else {
					$("#change-modal-prompt").html("删除失败!");
					$("#modal-yes").attr("href","#");
					$("#modal-yes").html("<button type=\"submit\" class=\"btn btn-primary\" data-dismiss=\"modal\">确定</button>");
					$("#change-modal").modal('show');
				}
			}
		});
	}
	</script>
	
	<script>
	var image = '';
	
	function selectImage(file){
		if(!file.files || !file.files[0]){
			return;
		}
		var reader = new FileReader();
		reader.onload = function(evt){
 			document.getElementById('image').src = evt.target.result;
 			image = evt.target.result;
		}
		reader.readAsDataURL(file.files[0]);
	}
	
 	function uploadImage(){
		$.ajax({
			type:'POST',
			url: 'UploadImage', 
			data: {
				image: image,
			},
			async: false,
			dataType: 'json',
			success: function(obj){
				var result = JSON.parse(obj);
				if(result.result=="success"){
					$("#change-modal-prompt").html("上传成功!");
					$("#modal-yes").attr("href","User.jsp");
					$("#modal-yes").html("<button type=\"submit\" class=\"btn btn-primary\">确定</button>");
					$("#change-modal").modal('show');
				}else if(result.result=="failure"){
					$("#change-modal-prompt").html("上传失败!");
					$("#modal-yes").attr("href","#");
					$("#modal-yes").html("<button type=\"submit\" class=\"btn btn-primary\" data-dismiss=\"modal\">确定</button>");
					$("#change-modal").modal('show');
				}else{
					$("#change-modal-prompt").html("请先选择文件!");
					$("#modal-yes").attr("href","#");
					$("#modal-yes").html("<button type=\"submit\" class=\"btn btn-primary\" data-dismiss=\"modal\">确定</button>");
					$("#change-modal").modal('show');
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