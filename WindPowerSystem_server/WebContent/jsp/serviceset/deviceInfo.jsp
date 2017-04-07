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
<title>服务设置-通讯设置</title>
<link rel="stylesheet" type="text/css"
	href="<%=basePath%>/css/common.css">
<link rel="stylesheet" type="text/css"
	href="<%=basePath%>/css/style.css">
<script type="text/javascript" src="<%=basePath%>js/jquery-1.8.3.min.js"></script>

<script type="text/javascript">
    	$(function(){
    		$.ajax({
    			type:'post',
    			url:'<%=basePath%>/CollectSet/listDeviceInfo.do',
    			//data:'',
    			success:function(data){
    				var result = JSON.parse(data);
    				var msg=result.message;
    				if(msg=="获取成功"){
    					str="";
    					str+="<thead><tr><th><label class='label_input01'><input id='checkAll' class='input_chk' type='checkbox'></input><span></span></label></th><th>序号</th><th>设备编号</th><th>设备</th><th>iP地址</th><th>端口号</th></tr></thead><tbody>";
    					var list=result.responseData.deviceInfoList;
    					if(list.length>0){
    						$.each(list,function(i,list){
    							str+="<tr><td><label class='label_input01'><input id='checkTo' title="+list.id+" name='checkTo' class='input_chk' type='checkbox'></input><span></span></label></td><td>"+(i+1)+"</td><td>"+list.name+"</td><td>"+list.d_type+"</td><td>"+list.ip+"</td><td>"+list.port+"</td></tr>";
    						});
    					}else{
    						str += "<tr>"
    							+"<td colspan='6'>没有相关数据</td>"
    							+"</tr>";
    					}
    						str+="</tbody>";
    						$("#table").html(str);
    						$('#table tbody tr:even').css("backgroundColor", "#edf2f6");
    				}else{
    					alert(msg);
    				}
    				
    			}});
    	});
    	
    	//修改之前
    	function toEdit(){
    		var id='';  //菜单id
     		for(var i=0;i < document.getElementsByName('checkTo').length;i++){
				  if(document.getElementsByName('checkTo')[i].checked){    //选中
					  if(id=='') id +=document.getElementsByName('checkTo')[i].title;
					  	else id += ',' + document.getElementsByName('checkTo')[i].title; 
					}
			}
    		if(id!=''){
	     		 var  num =id.split(',');
				  if(num.length>1){
					  alert("勾选的太多,无法编辑,请选择一个在继续编辑!");
					  return false;
				  }
				  if(num.length==1){
					  $.ajax({
						  type:'post',
						  url:'<%=basePath%>/CollectSet/toUpdateDeviceInfo.do',
						  data:{"id":id},
						  success:function(data){
							  var result = JSON.parse(data);
			    				var msg=result.message;
			    				if(msg=="获取成功"){
			    					str="";
			    					str+="<div  class='poput_box_b'><p class='poput_title'>新增<a href='#'onclick=closeWindow()></a></p><ul class='ul_style'><li><span>设备编号</span><select id='device_id'>";
			    					var list=result.responseData.deviceList;
			    					var deviceinfo=result.responseData.deviceInfo;
			    					$.each(list,function(i,list){
			    						str+="<option value="+list.id+" ";
			    						if(deviceinfo.device_id==list.id){
			    							str+="selected=selected";
			    						}
			    						str+=">"+list.name+"</option>";
			    					});
			    						str+="</select></li><li><span>设备</span><select id='d_type'>"
			    						+"<option value='arm'";
			    						if(deviceinfo.d_type='arm'){
			    							str+="selected";
			    						}
			    						str+=">arm</option>"
			    						+"<option value='dsp' ";
			    						if(deviceinfo.d_type='dsp'){
			    							str+="selected";
			    						}
			    						str+=">dsp</option></select>"
			    						+"</li><li><span>ip地址</span><input type='text' id='ip' value='"+deviceinfo.ip+"'/></li><li><span>端口号</span><input type='text' id='port' value='"+deviceinfo.port+"'/></li></ul><a class='a_button' style='cursor: pointer' onclick=edit('"+deviceinfo.id+"')>保存</a></div>";
			                   			$("#alertWindow").html(str);
			    				}else{
			    					alert(msg);
			    				}
							}});
					}
    		}else{
    			alert("请选择一个在继续编辑!");
    		}
    		
    	}
    	//修改之后
    	function edit(id){
    		var device_id=$("#device_id").val();
    		var d_type=$("#d_type").val();
    		var ip=$("#ip").val();
    		var port=$("#port").val();
    		 if(ip==""){
    			 alert("请输入ip地址");
    			 return false;
    		 }
    		 if(port==""){
    			 alert("请输入端口号");
    			 return false;
    		 }
    		$.ajax({
    			type:'post',
    			url:'<%=basePath%>/CollectSet/updateDeviceInfo.do',
    			data:{"id":id,"device_id":device_id,"d_type":d_type,"ip":ip,"port":port},
    			success:function(data){
    				var result = JSON.parse(data);
    				var msg=result.message;
    				if(msg=="更新成功"){
    					$(".poput_box_b").hide();
   		    		 	window.location.reload();
    				}else{
    					alert(msg);
    				}
    				
    			}
    		});
    		
    	}
    	 //新增之前
    	 function toSave(){
    		$.ajax({
    			type:'post',
    			url:'<%=basePath%>/CollectSet/listDevice.do',
    			//data:'',
    			success:function(data){
    				var result = JSON.parse(data);
    				var msg=result.message;
    				if(msg=="获取成功"){
    					str="";
    					str+="<div  class='poput_box_b'><p class='poput_title'>新增<a href='#'onclick=closeWindow()></a></p><ul class='ul_style'><li><span>设备编号</span><select id='device_id'>";
    					var list=result.responseData.deviceList;
    					$.each(list,function(i,list){
    						str+="<option value="+list.id+">"+list.name+"</option>";
    					});
    						str+="</select></li><li><span>设备</span><select id='d_type'><option value='arm'>arm</option><option value='dsp'>dsp</option></select>"
    						+"</li><li><span>ip地址</span><input type='text' id='ip'/></li><li><span>端口号</span><input type='text' id='port'/></li></ul><a class='a_button' style='cursor: pointer' onclick='save()'>保存</a></div>";
                   			$("#alertWindow").html(str);
    				}else{
    					alert(msg);
    				}
    			}});
    	} 
    	 
    	 //新增之后
    	 function save(){
    		 var device_id=$("#device_id").val();
    		 var d_type=$("#d_type").val();
    		 var ip=$("#ip").val();
    		 var port=$("#port").val();
    		 if(ip==""){
    			 alert("请输入ip地址");
    			 return false;
    		 }
    		 if(port==""){
    			 alert("请输入端口号");
    			 return false;
    		 }
    		 $.ajax({
    			type:'post',
    			url:'<%=basePath%>/CollectSet/insertDeviceInfo.do',
    			data:{"device_id":device_id,"d_type":d_type,"ip":ip,"port":port},
    			success:function(data){
    				var result = JSON.parse(data);
    				var msg=result.message;
    				if(msg=="添加成功"){
    					$(".poput_box_b").hide();
    		    		 window.location.reload();
    				}else{
    					alert(msg);	
    				}
    			}});
    	}
    	//删除
    	 function delete_dinfo(){
    		var id='';  //菜单id
      		for(var i=0;i < document.getElementsByName('checkTo').length;i++){
 				  if(document.getElementsByName('checkTo')[i].checked){    //选中
 					  if(id=='') id +=document.getElementsByName('checkTo')[i].title;
 					  	else id += ',' + document.getElementsByName('checkTo')[i].title; 
 					}
 			}
     		if(id!=''){
 	     		 var  num =id.split(',');
 				  if(num.length>1){
 					  alert("勾选的太多,无法删除,请选择一个在继续删除!");
 					  return false;
 				  }
 				  if(num.length==1){
 					 if(confirm("您确定删除吗?")){
 					 $.ajax({
 						type:'post',
 						url:'<%=basePath%>/CollectSet/deleteDeviceInfo.do',
 						data:{"id":id},
 						success:function(data){
 							var result = JSON.parse(data);
 		    				var msg=result.message;
 		    				if(msg=="删除成功"){
 		    					$(".poput_box_b").hide();
 		    		    		 window.location.reload();
 		    				}else{
 		    					alert(msg);
 		    				}
 						} 
 					 });
				}
 			  }
     		}else{
     			alert("请选择一个在继续删除!");
     		}
    	}
    	//关闭窗口
     	function closeWindow(){
     		 $("#xtype").empty(); 
     		 $("#typeName").val("");
     		 $(".poput_box_b").hide();
     	}
    </script>
</head>
<body>
	<!-- 头部内容home -->
	<!-- 头部内容end -->
	<div class="main_box clearfix">
		<!-- 左侧菜单 -->
		<!-- 左侧菜单end -->
		<!-- 右侧内容 -->
		<div class="right_main_div">
			<div class="W_width">
				<!-- 编辑标签 -->
				<p class="P_Label clearfix">
					<a class="a_size01" style="cursor: pointer" onclick="toSave()"><span>新增</span></a>
					<a class="a_size02" style="cursor: pointer" onclick="toEdit()"><span>编辑</span></a>
					<a class="a_size03" style="cursor: pointer" onclick="delete_dinfo()"><span>删除</span></a>
				</p>
				<!-- 编辑标签end -->
				<!-- table内容 -->
				<div class="table_box">
					<table id="table">
					</table>
					<!-- 新增/编辑弹窗 -->
					<div id="alertWindow"></div>
					<!-- 新增/编辑弹窗end -->
				</div>
				<!-- table内容end -->
			</div>
		</div>
		<!-- 右侧内容end -->
	</div>
	<script type="text/javascript" src="<%=basePath%>/js/jquery.js"></script>
	<script>
          $(function() { // dom元素加载完毕
              $('#table tbody tr:even').css("backgroundColor", "#edf2f6");
              //获取id为tb的元素,然后寻找他下面的tbody标签，再寻找tbody下索引值是偶数的tr元素,改变它的背景色.
              //tr:odd为奇数行，索引从0开始，0算偶数。
          })
    </script>


</body>
</html>