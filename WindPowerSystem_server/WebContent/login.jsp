<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link href="favicon.ico" rel="icon" type="image/x-icon" />
<link href="favicon.ico" rel="shortcut icon" type="image/x-icon" />
<title>欢迎登录后台管理系统</title>
<link href="<%=basePath%>/css/login.css" rel="stylesheet"
	type="text/css" />
<script type="text/javascript"
	src="<%=basePath%>/js/jquery-1.8.3.min.js"></script>
<script type="text/javascript">

	function keyDown(){
	    if (event.keyCode == "13") {//keyCode=13是回车键
	        $(".login-btn").click();
	    }
	}

   function mainView(){
	   var ajaxURL = "<%=basePath%>User/login.do";
	   $.ajax({
		   	type:"post",
		   	url:ajaxURL,
		   	data:$('#login-form').serialize(),//序列化表单值、创建URL编码文本字符串s
		   	success:function(data){
		   	var result = JSON.parse(data);	
		   	var msg=result.message;
		   	var state=result.responseData.state;
		    if(msg == "获取成功"){
		    	window.location.href="<%=basePath%>jsp/index.jsp"
				} else {
					if (state == '1') {
						alert("您输入的错误次数太多，账户已锁定，请明天重新登录解锁！");
					} else {
						alert(msg);
					}
				}
			}
		});
	}
</script>
</head>
<body onkeydown="keyDown()">
	<div class="main-login">

		<div class="login-content">
			<input type="hidden" id="path" value="<%=basePath%>" />
			<form action="" method="post" id="login-form" name="login-form">

				<div class="login-info">
					<img src="<%=basePath%>images/logo.png" style="width: 196px;">

				</div>
				<div class="login-info">
					<span class="span_01">用户名：</span> <input name="employeeId"
						id="employeeId" type="text" onblur="checkemployeeId()" value=""
						class="login-input" />
				</div>
				<div class="login-info">
					<span class="span_01">密&nbsp;&nbsp;码：</span> <input name="passwd"
						id="passwd" type="password" onblur="checkPasswd()" value=""
						class="login-input" />
				</div>

				<div class="login-oper">
					<input type="button" value="登&nbsp录" onclick="mainView()"
						class="login-btn" />
				</div>
			</form>
		</div>

	</div>
</body>
</html>