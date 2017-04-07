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
<html lang="en">

<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>服务设置-通讯设置</title>
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
	
	//页面加载时显示首页
	$(function(){
		$(".W_width").hide();
		$("#loading").show(); 
		$.ajax({
			type:'post',
			url:'<%=basePath%>/CollectSet/listDevice.do',
			//data:'',
			success:function(data){
				$(".W_width").show();
				$("#loading").hide(); 
				var result = JSON.parse(data);
				var msg=result.message;
				if(msg=="获取成功"){
					var str="";
					str+="<thead><tr><th>序号</th><th>风机编号</th><th>设备类型</th><th>新增时间</th><th>操作</th></tr></thead><tbody>";
					var deviceList=result.responseData.deviceList;
					if(deviceList.length>0){
						$.each(deviceList,function(i,deviceList){
							str+="<tr><td>"+(i+1)+"</td><td>"+deviceList.name+"</td><td>"+deviceList.modName+"</td><td>"+deviceList.create_time.substring(0,10)+"</td><td><a class='a_size01 tc'  style='cursor: pointer;' onclick=look('"+deviceList.id+"','"+deviceList.name+"','"+deviceList.device_type_id+"'); >查看</a> <a class='edit1' style='cursor: pointer;' onclick=toEditDtype('"+deviceList.id+"','"+deviceList.name+"','"+deviceList.device_type_id+"')>编辑</a><a class='delete1' style='cursor: pointer;' onclick=deleteDtype('"+deviceList.id+"')>删除</a></td></tr>";
						});
					}else{
 			    		str += "<tr>"
							+"<td colspan='5'>没有相关数据</td>"
							+"</tr>";
     			    	}
					str+="</tbody>";
					document.getElementById("table").innerHTML=str;
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
    			url:'<%=basePath%>/CollectSet/listDeviceType.do',
    			//data:'',
    			success:function(data){
    				var result = JSON.parse(data);
    				var msg=result.message;
    				if(msg=="获取成功"){
	    					 var str="";
	    					 str+="<ul class=\"ul_style  ul_W clearfix\"><li><span>设备类型</span><select id='xtype'>";
	    						 $.each(result.responseData.deviceTypeList,function(i,deviceTypeList){
	    	     				 		str+="<option value="+deviceTypeList.id+">"+deviceTypeList.name+"</option>";
	    	     				 });
	    					 str+="</select></li><li><span>风机编号</span><input type='text' id='typeName'></li></ul>"
	    					 +"<div class='table_w01'><table class=\"table_S table_input\" id='table'><thead><tr><th>设备</th><th>iP地址</th><th>端口号</th></tr></thead><tbody>"
	     				 	 +"<tr><td>ARM</td><td><label><input type='text'id='armIp'></label></td><td><input type='text' id='armAddr'></td></tr><tr><td>网侧DSP</td><td><input type='text' id='wdspIp'></td><td><input type='text'id='wdspAddr'></td></tr>"
	                         +"<tr><td>机侧DSP</td><td><input type='text' id='jdspIp'></td><td><input type='text' id='jdspAddr'></td></tr></tbody></table></div><a class='a_button' style='cursor: pointer;' onclick=document.getElementById('iframe').contentWindow.toDeviceSave(0);>保存</a>";
	                         alertwindow("新增",str);
	     				}else{
    						alert("msg");
    					}
    			}});
    	}
	
	//保存
	function save(id){
		var wtd = window.top.document;
		var xtype=$("#xtype",wtd).val();
		var typeName=$("#typeName",wtd).val();
		var armIp=$("#armIp",wtd).val();
		var armAddr=$("#armAddr",wtd).val();
		var wdspIp=$("#wdspIp",wtd).val();
		var wdspAddr=$("#wdspAddr",wtd).val();
		var jdspIp=$("#jdspIp",wtd).val();
		var jdspAddr=$("#jdspAddr",wtd).val();
		
		if(typeName==""){
			alert("风机编号不能为空！");
			return false;
		}
		if(armIp==""){
			alert("arm ip地址不能为空！");
			return false;
		}
		if(armAddr==""){
			alert("arm端口号不能为空！");
			return false;
		}
		if(wdspIp==""){
			alert("网侧dsp ip地址不能为空！");
			return false;
		}
		if(wdspAddr==""){
			alert("网侧dsp 端口号不能为空！");
			return false;
		}
		if(jdspIp==""){
			alert("机侧dsp ip地址不能为空！");
			return false;
		}
		if(jdspAddr==""){
			alert("机侧dsp 端口号不能为空！");
			return false;
		}
		if(armIp==wdspIp||armIp==jdspIp||wdspIp==jdspIp){
			alert("ip地址不能相同！");
			return false;
		}
		
		if(id==0){ //增加
			//1.ip地址是独一无二的
			//2.端口号：不同的ip地址可以有相同的端口号
			$.ajax({
				type:'post',
				url:'<%=basePath%>/CollectSet/checkIp.do',
				data:{"armIp":armIp,"wdspIp":wdspIp,"jdspIp":jdspIp},
				success:function(data){
					var result = JSON.parse(data);
					var msg=result.message;
					var adeviceInfoList=result.responseData.adeviceInfoList;
					var wdeviceInfoList=result.responseData.wdeviceInfoList;
					var jdeviceInfoList=result.responseData.jdeviceInfoList;
					if(msg=="查询成功"){
						if(adeviceInfoList!=0){
							alert("arm ip地址已拥有，请重新输入ip地址");
							return false;
						}
						if(wdeviceInfoList!=0){
							alert("网侧dsp ip地址已拥有，请重新输入ip地址");
							return false;
						}
						if(jdeviceInfoList!=0){
							alert("机侧dsp ip地址已拥有，请重新输入ip地址");
							return false;
						}
						
						$.ajax({
							type:'post',
							url:'<%=basePath%>/CollectSet/insertDeviceMangInfo.do',
							data:{"xtype":xtype,"typeName":typeName,"armIp":armIp,"armAddr":armAddr,"wdspIp":wdspIp,"wdspAddr":wdspAddr,"jdspIp":jdspIp,"jdspAddr":jdspAddr},
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
							}});
						
					}else{
						alert(msg);
					}
				}
			});
	}
		
		if(id!=0){ //修改
			var flag=true;
			$.ajax({
				async: false, 
				type:'post',
				url:'<%=basePath%>/CollectSet/editCheckIp.do',
				cache: "false", 
				data:{"id":id,"armIp":armIp,"wdspIp":wdspIp,"jdspIp":jdspIp},
				success:function(data){
					var result = JSON.parse(data);
					var msg=result.message;
					var alertMessage=result.responseData.checkIp;
					if(msg=="更新成功"){
						if(alertMessage!=""){
							alert(alertMessage);
							flag=false;
						}else{
							$.ajax({
								type:'post',
								url:'<%=basePath%>/CollectSet/editDeviceMangInfo.do',
								data:{"id":id,"xtype":xtype,"typeName":typeName,"armIp":armIp,"armAddr":armAddr,"wdspIp":wdspIp,"wdspAddr":wdspAddr,"jdspIp":jdspIp,"jdspAddr":jdspAddr},
								success:function(data){
									var result = JSON.parse(data);
									var msg=result.message;
									if(msg=="更新成功"){
										$("#gray",wtd).hide();
										$("#alertwindow",wtd).hide();
									    window.location.reload();
									}else{
										alert(msg);
									}
								}});
						}
					}
				}});
			if(flag==false){
				return false;
			}
		}
	}
	
	//查看
	function look(id,name,dataTyeId){
		$.ajax({
			type:'post',
			url:'<%=basePath%>/CollectSet/updateDeviceInfoMang.do',
			data:{"id":id},
			success:function(data){
				var result = JSON.parse(data);
				var msg=result.message;
				var list=result.responseData.deviceInfoList;
				if(msg=="添加成功"){
					 var str="";
					 str+="<ul class=\"ul_style  ul_W clearfix\"><li><span>设备类型</span><select  id='xtype'>";
						$.each(result.responseData.deviceTypeList,function(i,deviceTypeList){
	     				 		str+="<option  value="+deviceTypeList.id+" ";
	     				 		 if(dataTyeId==deviceTypeList.id){
	     				 			str+="selected";
	     				 		} 
	     				 		str+=">"+deviceTypeList.name+"</option>";
	     				 }); 
					 str+="</select></li><li><span>风机编号</span><input type='text' id='typeName' readonly value="+name+"></li></ul>"
					 +"<div class='table_w01'><table class=\"table_S table_input\" id='table'><thead><tr><th>设备</th><th>iP地址</th><th>端口号</th></tr></thead><tbody>";
					 if(list.length>0){
						 $.each(list,function(i,list){
							if(list.dsp_type==0){
								str+="<tr><td>ARM</td><td><label><input type='text'id='armIp' readonly value="+list.ip+"></label></td><td><input type='text' id='armAddr' readonly value="+list.port+"></td></tr>";
							}
							if(list.dsp_type==1){
								str+="<tr><td>网侧DSP</td><td><input type='text' id='wdspIp' readonly value="+list.ip+"></td><td><input type='text'id='wdspAddr' readonly value="+list.port+"></td></tr>";
							}
							if(list.dsp_type==2){
								str+="<tr><td>机侧DSP</td><td><input type='text' id='jdspIp'readonly value="+list.ip+" ></td><td><input type='text' id='jdspAddr'readonly value="+list.port+"></td></tr>";
							}
						});
					 }else{
						 str += "<tr>"
								+"<td colspan='3'>没有相关数据</td>"
								+"</tr>";
					 }
					 str+="</tbody></table></div>";
					 alertwindow("查看",str);
				}else{
					alert(msg);
				}
			}});
	}
	//编辑之前
	function toEditDtype(id,name,dataTyeId){
	$.ajax({
		type:'post',
		url:'<%=basePath%>/CollectSet/updateDeviceInfoMang.do',
		data:{"id":id},
		success:function(data){
			var result = JSON.parse(data);
			var msg=result.message;
			var list=result.responseData.deviceInfoList;
			if(msg=="添加成功"){
				 var str="";
				 str+="<ul class=\"ul_style  ul_W clearfix\"><li><span>设备类型</span><select  id='xtype'>";
					$.each(result.responseData.deviceTypeList,function(i,deviceTypeList){
     				 		str+="<option  value="+deviceTypeList.id+" ";
     				 		 if(dataTyeId==deviceTypeList.id){
     				 			str+="selected";
     				 		} 
     				 		str+=">"+deviceTypeList.name+"</option>";
     				 }); 
				 str+="</select></li><li><span>风机编号</span><input type='text' id='typeName' value="+name+"></li></ul>"
				 +"<div class='table_w01'><table class=\"table_S table_input\" id='table'><thead><tr><th>设备</th><th>iP地址</th><th>端口号</th></tr></thead><tbody>";
				 if(list.length>0){
					 $.each(list,function(i,list){
						if(list.dsp_type==0){
							str+="<tr><td>arm</td><td><label><input type='text' id='armIp'  value="+list.ip+"></label></td><td><input type='text' id='armAddr' value="+list.port+"></td></tr>";
						}
						if(list.dsp_type==1){
							str+="<tr><td>网侧dsp</td><td><input type='text' id='wdspIp'  value="+list.ip+"></td><td><input type='text'id='wdspAddr' value="+list.port+"></td></tr>";
						}
						if(list.dsp_type==2){
							str+="<tr><td>机侧dsp</td><td><input type='text' id='jdspIp'  value="+list.ip+" ></td><td><input type='text' id='jdspAddr' value="+list.port+"></td></tr>";
						}
					});
				 }else{
					 str += "<tr>"
							+"<td colspan='3'>没有相关数据</td>"
							+"</tr>";
				 }
				 str+="</tbody></table></div><a class='a_button' style='cursor: pointer;' onclick=document.getElementById('iframe').contentWindow.toDeviceSave('"+id+"');>保存</a>";
				 alertwindow("编辑",str);
			}else{
				alert(msg);
			}
		}});
	}
	
	//删除
	function deleteDtype(id){
	 if(confirm("您确定删除设备和通讯设备吗？")){
		$.ajax({
			type:'post',
			url:'<%=basePath%>/CollectSet/deleteDeviceInfoMang.do',
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
