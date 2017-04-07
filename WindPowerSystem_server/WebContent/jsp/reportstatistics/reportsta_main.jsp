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
<link rel="stylesheet" type="text/css" href="<%=basePath%>css/common.css">
<link rel="stylesheet" type="text/css" href="<%=basePath%>/css/style.css">
<script type="text/javascript" src="<%=basePath%>js/jquery-1.8.3.min.js"></script>
<title>报表统计</title>
<script type="text/javascript">
$(function(){
	$("#iframe").attr("src","<%=basePath%>jsp/reportstatistics/errorreport.jsp");
	var height = $(window).height()-120;
	$("#iframe").attr("style","width:99%;height:"+height+"px");
});
function chooseUrl(obj){
	var url = "";
	if(obj == 0){
		url = "<%=basePath%>jsp/reportstatistics/errorreport.jsp";
	}else if(obj == 1){
		url = "<%=basePath%>jsp/reportstatistics/warnreport.jsp";
	}else if(obj == 2){
		url = "<%=basePath%>jsp/reportstatistics/runreport.jsp";
	}
	$("#iframe").attr("src",url);
	
	$('li').live('click',function(){
		$(this).addClass('li_style01').siblings('li').removeClass('li_style01');
	}); 
}

function toChildSave(){
	document.getElementById('iframe').contentWindow.save();
}
</script>
</head>
<body>
    <div class="main_box clearfix">

        <!-- 左侧菜单 -->
        <div class="left_list_ul">
            <ul>
                <li class="li_style01"><a href="javascript:;" onclick="chooseUrl(0)">故障报表</a></li>
                <li><a href="javascript:;" onclick="chooseUrl(1)">告警报表</a></li>
                <li><a href="javascript:;" onclick="chooseUrl(2)">运行报表</a></li>
            </ul>
        </div>
        <!-- 左侧菜单end -->
        <!-- 右侧内容 -->
        <div class="right_main_div0">
		    <div class="W_width">
		    	<iframe id="iframe" scrolling="no" frameborder="0"  src=""></iframe>
		    </div>
	    </div>
		<!-- 右侧内容end -->
    </div>
</body>
</html>