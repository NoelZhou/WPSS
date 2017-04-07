<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
 	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>服务设置-邮箱服务设置</title>
<link rel="stylesheet" type="text/css"
	href="<%=basePath%>/css/common.css">
<link rel="stylesheet" type="text/css"
	href="<%=basePath%>/css/style.css">
<script type="text/javascript" src="<%=basePath%>js/jquery-1.8.3.min.js"></script>
<script type="text/javascript" src="<%=basePath%>js/opts.js"></script>
<script type="text/javascript" src="<%=basePath%>js/spin.js"></script>
<script type="text/javascript">
		$(function(){
			$("#loading").show(); 
			$.ajax({
				type:'post',
				url:'<%=basePath%>/ErrorWarnSet/getEmailServer.do',
				success:function(data){
					$("#loading").hide(); 
					var result = JSON.parse(data);
					var msg=result.message;
					var emaliService=result.responseData.emailServer;
					if(msg=="获取成功"){
						str="";
						str+="<div class='right_main_div'><div class='W_width'><div class='height_H'><div class='W_w'><ul>"
						+"<li class='clearfix' ><span>SMTP邮件服务器</span><input class='input03' type='text'id='serveraddr' value="+emaliService.serveraddr+"></input></li>"
						+"<li class='clearfix'><span>发邮件账号</span><input class='input03' type='text' id='emailid' value="+emaliService.emailid+"></input></li>"
						+"<li class='clearfix'><span>发邮件密码</span><input class='input03' type='password' id='passwd' value="+emaliService.passwd+"></input></li>"
						+"<li class='clearfix'><span>发件人签字</span><textarea rows='5'  id='assgin'>"+emaliService.assgin+"</textarea></li>"
						+"</ul><a class='A_button' style='cursor:pointer;margin: 10px auto 0;' onclick='save()'>保存</a></div></div></div></div>";
						$("#content").html(str);
					}else{
						 alert(msg);
					}
				}});
		});
		function save(){
			var serveraddr=$("#serveraddr").val();
			var emailid=$("#emailid").val();
			var passwd=$("#passwd").val();
			var assgin=$("#assgin").val();
			if(serveraddr==""){
				alert("请输入SMTP邮件服务器");
				return false;
			}
			if(emailid==""){
				alert("请输入发邮件账号");
				return false;
			}
			if(passwd==""){
				alert("请输入发邮件密码");
				return false;
			}
			if(assgin==""){
				alert("请输入发件人签字");
				return false;
			}
			$.ajax({
				type:'post',
				url:'<%=basePath%>/ErrorWarnSet/updateEmailServer.do',
				data:{"serveraddr":serveraddr,"emailid":emailid,"passwd":passwd,"assgin":assgin},
				success:function(data){
					var result = JSON.parse(data);
					var msg=result.message;
					if(msg=="更新成功"){
						window.location.reload();
					}else{
						alert(msg);
					}
				}});
		}
	</script>
</head>
<body style="min-height: 450px; max-height: 450px; overflow: auto">
	<!-- 头部内容home -->
	<!-- 头部内容end -->
	<div class="main_box clearfix">
		<!-- 左侧菜单 -->
		<!-- 左侧菜单end -->
		<!-- 右侧内容 -->
		<div id="loading" style="margin: 150px 600px;"></div>
		<div id="content"></div>
		<!-- 右侧内容end -->
	</div>
</body>
</html>