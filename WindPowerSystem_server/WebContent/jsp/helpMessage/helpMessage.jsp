<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>帮助信息</title>
<script type="text/javascript" src="<%=basePath%>js/jquery-1.8.3.min.js"></script>
<script type="text/javascript">
	$(function(){
		debugger;
		url = "<%=basePath%>jsp/documents/1058.htm";
		var height = $(window).height()-110;
		var width = $(window).width();
		$("#iframe1").attr("style","width:100%;height:"+height+"px;padding-top:110px;");
		$("#iframe1").attr("src",url);
	});
</script>
<style type="text/css">
	#iframe1{scrolling-x:hidden;}
</style>
</head>
<body>
	<iframe id="iframe1" scrolling="yes" frameborder="0"  src="" ></iframe>
</body>
</html>