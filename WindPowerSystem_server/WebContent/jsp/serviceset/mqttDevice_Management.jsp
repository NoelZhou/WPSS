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
<title>服务设置-mqtt设备管理</title>
<link rel="stylesheet" type="text/css"
	href="<%=basePath%>/css/common.css">
<link rel="stylesheet" type="text/css"
	href="<%=basePath%>/css/style.css">

<!-- 共同引用jquery js -->
<script type="text/javascript" src="<%=basePath%>/js/jquery1124.js"></script>
<script type="text/javascript" src="<%=basePath%>/js/js.js"></script>
<!-- 共同引用jquery js -->
<script type="text/javascript" src="<%=basePath%>js/jquery-1.8.3.min.js"></script>
<script type="text/javascript" src="<%=basePath%>js/opts.js"></script>
<script type="text/javascript" src="<%=basePath%>js/spin.js"></script>
<style type="text/css">
.edit1 {
	background: url(<%=basePath%>/images/icon-.png) no-repeat;
	overflow: hidden;
	color: #ffffff;
	padding-left: 20px;
	background-position: 0 -62px;
}

.delete1 {
	background: url(<%=basePath%>/images/icon-.png) no-repeat;
	overflow: hidden;
	color: #ffffff;
	padding-left: 20px;
	background-position: 0 -84px;
}
</style>
<script type="text/javascript">
		$(function(){
			$(".W_width").hide();
			$("#loading").show(); 
			$.ajax({
				type:'post',
				url:'<%=basePath%>Mqtt_device/list.do',
				success:function(data){
					$(".W_width").show();
					$("#loading").hide(); 
					var result = JSON.parse(data);
					var msg=result.message;
					if(msg=="获取成功"){
						var str="";
						str+="<thead><tr><th style='width:5%;'>序号</th><th style='width:10%;'>设备编号</th><th style='width:13%;'>风机编号</th><th style='width:10%;'>风机名称</th><th style='width:10%;'>设备类型编码</th><th>设备厂家</th><th>设备型号</th><th>新增时间</th><th>操作</th></tr></thead><tbody>";
						var Mqtt_deviceList=result.responseData.Mqtt_deviceList;
						if(Mqtt_deviceList.length>0){
							$.each(Mqtt_deviceList,function(i,Mqtt_deviceList){
								str+="<tr><td>"+(i+1)+"</td><td>"+Mqtt_deviceList.sn+"</td><td>"+Mqtt_deviceList.device_name+"</td><td>"+Mqtt_deviceList.name+"</td><td>"+Mqtt_deviceList.device_lx_mh+"</td><td>"+Mqtt_deviceList.device_CJ+"</td><td>"+Mqtt_deviceList.device_xh+"</td><td>"+Mqtt_deviceList.create_time.substring(0,10)+"</td><td> <a class='edit1' style='cursor: pointer;' onclick=toEdit('"+Mqtt_deviceList.id+"','"+Mqtt_deviceList.device_id+"')>编辑</a><a class='delete1' style='cursor: pointer;' onclick=deleteMqtt('"+Mqtt_deviceList.id+"')>删除</a></td></tr>";
							});
						}else{
	 			    		str += "<tr>"
								+"<td colspan='9'>没有相关数据</td>"
								+"</tr>";
	     			    	}
						str+="</tbody>";
						$('#table').html(str);
						$('#table tbody tr:even').css("backgroundColor", "#edf2f6");
					}else{
						alert(msg);
					}
				}});
			autoHeight();
		});
		
		//新增之前
    	function toSave(){
    		$.ajax({
    			type:'post',
    			url:'<%=basePath%>/Mqtt_device/listDevice.do',
    			success:function(data){
    				var result = JSON.parse(data);
    				var msg=result.message;
    				if(msg=="获取成功"){
	    					 var str="";
	    					 str+="<ul class=\"ul_style  ul_W clearfix\"><li><span>风机编号</span><select id='device_id'>";
	    						 $.each(result.responseData.deviceList,function(i,deviceList){
	    	     				 		str+="<option value="+deviceList.id+">"+deviceList.name+"</option>";
	    	     				 });
	    					 str+="</select></li><li><span>设备编号</span><input type='text' id='sn'></li>"
	    					 +"<li><span>风机名称</span><input type='text' id='name'></li>"
	    					 +"<li><span>设备编码管理</span><input type='number' id='device_lx_mh'></li><li><span>设备厂家</span><input type='text' id='device_CJ'></li>"
	    					 +"<li><span>设备型号</span><input type='text' id='device_xh'></li></ul>";
	    					 str+="<a class='a_button' style='cursor: pointer;' onclick=document.getElementById('iframe').contentWindow.toDeviceSave(0);>保存</a>";
	    					 alertwindow("新增",str);
	     				}else{
    						alert(msg);
    					}
    			}});
    	}
		//修改之前
		function toEdit(id,device_id){
			$.ajax({
    			type:'post',
    			url:'<%=basePath%>/Mqtt_device/toEditMqtt_device.do',
    			data:{"id":id,"device_id":device_id},
    			success:function(data){
    				var result = JSON.parse(data);
    				var msg=result.message;
    				var mqtt_deviceList=result.responseData.mqtt_deviceList;
    				if(msg=="查询成功"){
	    					 var str="";
	    					 str+="<ul class=\"ul_style  ul_W clearfix\"><li><span>风机编号</span><select id='device_id'>";
	    						 $.each(result.responseData.deviceList,function(i,deviceList){
	    	     				 		str+="<option value="+deviceList.id+" ";
	    	     				 		if(deviceList.id==mqtt_deviceList.device_id){
	    	     				 			str+="selected";
	    	     				 		}
	    	     				 		str+=">"+deviceList.name+"</option>";
	    	     				 });
	    					 str+="</select></li><li><span>设备编号</span><input type='text' id='sn' value='"+mqtt_deviceList.sn+"'></li>"
	    					 +"<li><span>风机名称</span><input type='text' id='name' value='"+mqtt_deviceList.name+"'></li>"
	    					 +"<li><span>设备编码管理</span><input type='number' id='device_lx_mh' value='"+mqtt_deviceList.device_lx_mh+"'></li><li><span>设备厂家</span><input type='text' id='device_CJ' value='"+mqtt_deviceList.device_CJ+"'></li>"
	    					 +"<li><span>设备型号</span><input type='text' id='device_xh' value='"+mqtt_deviceList.device_xh+"'></li></ul>";
	    					 str+="<a class='a_button' style='cursor: pointer;' onclick=document.getElementById('iframe').contentWindow.toDeviceSave('"+mqtt_deviceList.id+"');>保存</a>";
	    					 alertwindow("编辑",str);
	     				}else{
    						alert(msg);
    					}
    			}});
		}
		//删除
		function deleteMqtt(id){
			if(confirm("您确定删除mqtt设备吗？")){
				$.ajax({
					type:'post',
					url:'<%=basePath%>/Mqtt_device/deleteMqtt_device.do',
					data:{"id":id},
					success:function(data){
						var result = JSON.parse(data);
						var msg=result.message;
						if(msg=="删除成功"){
							window.location.reload();
						}else{
							alert(msg);
						}
					}});
			 }	
		}
    	//保存
    	function save(id){
    		var wtd = window.top.document;
    		var device_id=$("#device_id",wtd).val();
    		var sn=$("#sn",wtd).val();
    		var name=$("#name",wtd).val();
    		var device_lx_mh=$("#device_lx_mh",wtd).val();
    		var device_CJ=$("#device_CJ",wtd).val();
    		var device_xh=$("#device_xh",wtd).val();
    		var reg =/^[0-9a-zA-Z]*$/;
    		if(device_id==null){
    			alert("风机编号已全部添加，暂无风机编号！");
    			return false;
    		}
    		if(sn==""){
    			alert("设备编号不能为空！");
    			return false;
    		}else if(!reg.test(sn)){
    			alert("设备编号不能为汉字！");
    		    return false;
    	    }
    	    if(name==""){
    			alert("风机名称不能为空 ");
    			return false;
    		}else if(!reg.test(name)){
    			alert("风机名称不能为汉字！");
    		    return false;
    	    }
    		if(device_lx_mh==""){
    			alert("设备编码管理不能为空！");
    			return false;
    		}
    		if(device_CJ==""){
    			alert("设备厂家不能为空！");
    			return false;
    		}else if(!reg.test(device_CJ)){
    			alert("设备厂家不能为汉字！");
    		    return false;
    	    }
    		if(device_xh==""){
    			alert("设备型号不能为空！");
    			return false;
    		}else if(!reg.test(device_xh)){
    			alert("设备型号不能为汉字！");
    		    return false;
    	    }
    		var dataValue={"device_id":device_id,"sn":sn,"name":name,"device_lx_mh":device_lx_mh,"device_CJ":device_CJ,"device_xh":device_xh};
    		if(id==0){ 
    			$.ajax({
    				type:'post',
    				url:'<%=basePath%>/Mqtt_device/insertMqtt_device.do',
    				data:dataValue,
    				success:function(data){
    					var result = JSON.parse(data);
    					var msg=result.message;
    					if(msg=="添加成功"){
    						$("#gray",wtd).hide();
							$("#alertwindow",wtd).hide();
						    window.location.reload();
    					}else{
    						alert(msg);
    					}
    				}
    			});
    	}
    		var dataEditValue={"id":id,"device_id":device_id,"sn":sn,"name":name,"device_lx_mh":device_lx_mh,"device_CJ":device_CJ,"device_xh":device_xh};
    		if(id!=0){ //修改
    			$.ajax({
    				type:'post',
    				url:'<%=basePath%>/Mqtt_device/editMqtt_device.do',
    				data:dataEditValue,
    				success:function(data){
    					var result = JSON.parse(data);
    					var msg=result.message;
    					if(msg=="更新成功"){
    						$("#gray",wtd).hide();
							$("#alertwindow",wtd).hide();
						    window.location.reload();
    					}
    				}});
    		}
    	}
		
    	function alertwindow(title,str){
    		var wtd = window.top.document;
    	   	$("#gray",wtd).show();
    	   	$("#alertwindow",wtd).show();
    	   	$("#titletxt",wtd).html(title);
    	   	$("#contentdiv",wtd).html(str);
    	   	$('#table tbody tr:even',wtd).css("backgroundColor", "#edf2f6");
    	}
		function autoHeight(){
	    	var autoheight = $(window).height()-210;
		    $("#autoarea").attr("style","min-height:"+autoheight+"px;max-height:"+autoheight+"px");
		}
	</script>
</head>
<body>

	<!-- 头部内容home -->
	<div id="loading" style="margin: 300px 600px;"></div>
	<!-- 头部内容end -->
	<div class="main_box clearfix">
		<!-- 左侧菜单 -->

		<!-- 左侧菜单end -->
		<!-- 右侧内容 -->
		<div class="right_main_div0">
			<div class="W_width">
				<!-- 编辑标签 -->
				<p id="aac" class="P_Label clearfix">
					<a class="a_size01 tc" style="cursor: pointer;" onclick="toSave();"><span>新增</span></a>
				</p>
				<!-- 编辑标签end -->
				<!-- table内容 -->
				<div class="table_box" id="autoarea">
					<table id="table">
					</table>
				</div>
				<!-- table内容end -->
			</div>
		</div>
		<!-- 右侧内容end -->
	</div>
</body>
</html>