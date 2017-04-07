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
<meta http-equiv="Content-Type" content="text/html; charset=gbk">
<link href="../favicon.ico" rel="icon" type="image/x-icon" />
<link href="../favicon.ico" rel="shortcut icon" type="image/x-icon" />
<link rel="stylesheet" type="text/css"
	href="<%=basePath%>css/common.css">
<link rel="stylesheet" type="text/css"
	href="<%=basePath%>/css/style.css">
<script type="text/javascript" src="<%=basePath%>js/jquery-1.8.3.min.js"></script>
<script type="text/javascript" src="<%=basePath%>js/popup.js"></script>
<script type="text/javascript" src="<%=basePath%>js/opts.js"></script>
<script type="text/javascript" src="<%=basePath%>js/spin.js"></script>
<title>风场预览</title>
<script type="text/javascript">
$(function(){
	url = "<%=basePath%>Menu/listRoleMenu.do";
	$.post(url,function(data){
		var result = JSON.parse(data);
		var menuList = result.responseData.rolemenuList;
		var login=result.responseData.login;
		if(login!=null){
			alert("您长时间没有操作系统，请重新登录!");
			window.location.href="<%=basePath%>login.jsp";
		}
		var liStr = "";
		var menuid = "";
		var hasqx;
		var liname = "";
		for(var i=0;i < menuList.length;i++){
			liStr += "<li id='"+menuList[i].menu_id+"'";
			if(i == 0){
				liStr += "class='li_icon'";
				menuid = menuList[i].menu_id;
				hasqx = menuList[i].menu_qx;
				liname = menuList[i].menu_name;
			}
			liStr += "onclick=choose('"+menuList[i].menu_id+"','"+menuList[i].menu_name+"');><a href='#'><i><strong class='S_0"+menuList[i].menu_id+"'></strong></i><span>"+menuList[i].menu_name+"</span></a></li>";
		}
		var exitName="退出系统";
		liStr +="<li onclick=exitSystem();><a href='#'><i><strong class='exit'></strong></i><span>"+exitName+"</span></a></li>";
		document.getElementById("indexul").innerHTML = liStr;
		if(menuList.length > 0){
			if(hasqx == 1){
				$("#iframe").attr("src","<%=basePath%>Menu/chooseMenu.do?flag="+menuid);
			}else{
				$("#iframe").attr("src","<%=basePath%>jsp/noqx.jsp");
			}
			document.title = liname;
		}
	});
	tc_center();
	loadingTop();
    $("#iframe").attr("style","width:100%;height:"+$(window).height()+"px");
});
function choose(menu_id,menu_name){
	$("li").removeAttr("class");
	var lid = menu_id;
	var lname = menu_name;
	$("#"+lid).addClass("li_icon");
	var menu_url = "<%=basePath%>Menu/listRoleMenu.do";
	$.post(menu_url,function(data){
		var result = JSON.parse(data);
		var menuList = result.responseData.rolemenuList;
		var login=result.responseData.login;
		if(login!=null){
			alert("您长时间没有操作系统，请重新登录!");
			window.location.href="<%=basePath%>login.jsp";
		}
		var qx = menuList[(lid-1)].menu_qx;
		var url = "";
		if(qx == 1){
			url = "<%=basePath%>Menu/chooseMenu.do?flag="+lid;
		}else{
			url = "<%=basePath%>jsp/noqx.jsp";
		}
		$("#iframe").attr("style","width:100%;height:"+$(window).height()+"px");
		$("#iframe").attr("src",url);
		document.title = lname;
	});
}

//关闭div浮层
function clsdiv(){
	$("#gray").hide();
	$("#alertwindow").hide();
	debugger;
	var deviceid=window.frames["iframe"].frames["serviceFrame"].document.getElementById("deviceIds").value;
	var endCodeStatus = window.frames["iframe"].frames["serviceFrame"].document.getElementById("endCode").value;
	if(endCodeStatus !=0){
		if(deviceid!=""){
			updatedevicestate(deviceid);
		}
	}
	
	
}

function updatedevicestate(deviceId){
	<%--  // url = "<%=basePath%>Menu/listRoleMenu.do"; --%>
	  $.ajax({  
	         url: '<%=basePath%>wfp/firmwareUpgrade/updatesjjs.do' ,  
	         type: 'POST',  
	         data: {
	 			"deviceId" : deviceId
			}, 
	         async: true,  
	         cache: false,  
	         success: function (returndata) {
	        	 debugger;
	             var result = JSON.parse(returndata); 
	             if(result == null){
	            	  return;
	             }
				 
	         },
	         error:function(e){
	        	 debugger;
	        	 var ss=e;
	         }
	    });
}
//关闭查看专家库
function clsdiv2(){
	$("#alertwindow2").hide();
	$("#alertwindow").show();
}
function loadingTop(){
	var _top=($(window).height()-$(".loadingTop").height())/1.7;
	var _left=($(window).width()-$(".loadingTop").width())/2;
	$(".loadingTop").css({top:_top,left:_left});
}
//退出系统
function exitSystem(){
	window.location.href="<%=basePath%>Menu/exitSystem.do";
}
</script>
</head>
<body>
	<div id="loading" class="loadingTop"
		style="top: 50%; left: 50%; position: relative; display: none;"></div>
	<input type="hidden" id="loginSession" />
	<!-- <input type="hidden" id="deviceIds" name="deviceIds" value=""/> -->
	<!-- 头部内容home -->
	<div class="hd_box">
		<div class="logo_box">
			<a href="#"><img src="<%=basePath%>/images/logo.png"></a>
		</div>
		<div class="nav_box">
			<ul id="indexul">

			</ul>
		</div>
	</div>
	<iframe id="iframe" name="iframe" scrolling="no" frameborder="0" src=""></iframe>
	<div id="gray"></div>
	<div id="grayT"></div>
	<!-- 弹出框 -->
	<div class="popup poput_box_b Min_w tablee" id="alertwindow"
		style="display: none;">
		<p class='poput_title'>
			<span id="titletxt"></span><a style='cursor: pointer;'
				onclick='clsdiv()'></a>
		</p>
		<div class="poput_box_height" id="contentdiv"></div>
	</div>
	<!-- 弹出框 end-->
	<!-- 弹出框 专家库 -->
	<div class="popup poput_box_b Min_w tablee" id="alertwindow2"
		style="display: none;">
		<p class='poput_title'>
			<span id="titletxt2"></span><a style='cursor: pointer;'
				onclick='clsdiv2()'></a>
		</p>
		<div class="poput_box_height" id="contentdiv2"></div>
	</div>
	<!-- 弹出框 专家库end-->

</body>
</html>