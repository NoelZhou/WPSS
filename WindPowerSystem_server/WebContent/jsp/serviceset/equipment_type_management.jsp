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
    <title>服务设置-设备类型管理</title>
    <link rel="stylesheet" type="text/css" href="<%=basePath%>/css/common.css">
    <link rel="stylesheet" type="text/css" href="<%=basePath%>/css/style.css">
    <script type="text/javascript" src="<%=basePath%>js/jquery-1.8.3.min.js"></script>
    <script type="text/javascript" src="<%=basePath%>js/opts.js"></script>
	<script type="text/javascript" src="<%=basePath%>js/spin.js"></script>
	<style type="text/css">
			.edit1{background: url(<%=basePath%>/images/icon-.png) no-repeat;
					overflow: hidden;
					color: #ffffff;
					padding-left: 20px;
					background-position: 0 -62px;
				}
			.delete1{background: url(<%=basePath%>/images/icon-.png) no-repeat;
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
    			url:'<%=basePath%>/CollectSet/listDeviceType.do',
    			success:function(data){
    				$(".W_width").show();
    				$("#loading").hide(); 
    				var result = JSON.parse(data);
    				var msg=result.message;
    				if(msg=="获取成功"){
    					var str="";
    					str+="<thead><tr><th>序号</th><th>设备类型</th><th>操作</th></tr></thead>"
    					+"<tbody>";
    					if(result.responseData.deviceTypeList.length>0){
    						$.each(result.responseData.deviceTypeList,function(i,deviceTypeList){
	    						str+="<tr><td>"+(i+1)+"</td>"
	    						+"<td>"+deviceTypeList.name+"</td>"
	    						+"<td><a class='edit1'style='cursor: pointer;' onclick=toEditDtype('"+deviceTypeList.id+"')>编辑</a><a class='delete1' style='cursor: pointer;' onclick=deleteDtype('"+deviceTypeList.id+"')>删除</a></td></tr>";
	    					});
    					}else{
     			    		str += "<tr>"
							+"<td colspan='3'>没有相关数据</td>"
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
    	
    	//修改之前
    	function toEditDtype(id){
    		$.ajax({
    			type:'post',
    			url:'<%=basePath%>/CollectSet/toUpdateDeviceType.do',
    			data:{"id":id},
    			success:function(data){
    				var result=JSON.parse(data);
    				var msg=result.message;
    				if(msg="获取成功"){
    					var str="";
     				 	str+="<ul class='ul_style'><li><span>协议类型</span><select class='xtype' id='xtype'>";
     				 	$.each(result.responseData.modbustcptypeList,function(i,modbustcptypeList){
     				 		str+="<option value='"+modbustcptypeList.id+"'";
     				 		if(modbustcptypeList.id==result.responseData.deviceType.modbus_type){
     				 			str+= "selected=selected";
     				 		}
     				 		str+=">"+modbustcptypeList.name+"</option>";
     				 	});
     				 	str+="</select></li><li><span>风机类型名称</span><input type='text' value='"+result.responseData.deviceType.name+"' id='typeName'></input></li></ul><a class='a_button' style='cursor: pointer;' onclick=document.getElementById('iframe').contentWindow.toChildEdit('"+result.responseData.deviceType.id+"');>保存</a>";
     				 	alertwindow("编辑",str);
     				}else{
    					alert(msg);
    				}
    			}});
    	}
    	//修改之后
    	function edit(id){
    		var wtd = window.top.document;
    		var xtype=$("#xtype",wtd).val();
    		var typeName=$("#typeName",wtd).val();
    		if(typeName==""){
    			alert("请输入风机类型名称");
    			typeName.focus();
    			return false;
    		}
    		$.ajax({
    			type:'post',
    			url:'<%=basePath%>/CollectSet/updateDeviceType.do',
    			data:{"id":id,"modbus_type":xtype,"name":typeName},
    			success:function(data){
    				var result = JSON.parse(data);
    				var msg=result.message;
    				if(msg=="更新成功"){
    					 $("#xtype",wtd).empty(); 
    		    		 $("#typeName",wtd).val("");
    		    		 $("#gray",wtd).hide();
    					 $("#alertwindow",wtd).hide();
    		    		 window.location.reload();
    				}else{
    					alert(msg);	
    				}
    				
    			}});
    		
    	}
    	
    	//保存之前
    	function toSave(){
    		$.ajax({
    			type:'post',
    			url:'<%=basePath%>/CollectSet/listModbustcpType.do',
    			//data:'',
    			success:function(data){
    				var result = JSON.parse(data);
    				var msg=result.message;
    				if(msg=="获取成功"){
    				 var str="";
    				 	str+="<ul class='ul_style'><li><span>协议类型</span><select id='xtype'>";
    				 	$.each(result.responseData.modbustcptypeList,function(i,modbustcptypeList){
    				 		str+="<option value="+modbustcptypeList.id+">"+modbustcptypeList.name+"</option>";
    				 	});
    				 	str+="</select></li><li><span>风机类型名称</span><input type='text' id='typeName'></input></li></ul><a class='a_button' style='cursor: pointer;' onclick=document.getElementById('iframe').contentWindow.toChildSave();>保存</a>";
    				 	alertwindow("新增",str);
    				 }else{
    					alert(msg);
    				}
    				
    			}});
    	}
    	
    	
    	//保存之后
    	function save(){
    		var wtd = window.top.document;
    		var xtype=$("#xtype",wtd).val();
    		var typeName=$("#typeName",wtd).val();
    		if(typeName==""){
    			alert("请输入风机类型名称");
    			typeName.focus();
    			return false;
    		}
    		$.ajax({
    			type:'post',
    			url:'<%=basePath%>/CollectSet/insertDeviceType.do',
    			data:{"modbus_type":xtype,"name":typeName},
    			success:function(data){
    				var result = JSON.parse(data);
    				var msg=result.message;
    				if(msg=="添加成功"){
    					 $("#xtype",wtd).empty(); 
    		    		 $("#typeName",wtd).val("");
    		    		 $("#gray",wtd).hide();
    					 $("#alertwindow",wtd).hide();
    		    		 window.location.reload();
    				}else{
    					alert(msg);	
    				}
    				
    			}});
    	}
    	//删除
    	function deleteDtype(id){
    		if(confirm("您确定要删除吗?")){
    			$.ajax({
    				type:'post',
    				url:'<%=basePath%>/CollectSet/deleteDeviceType.do',
    				data:{'id':id},
    				success:function(data){
    					var result = JSON.parse(data);
        				var msg=result.message;
        				if(msg=="删除成功"){
        					window.location.reload();
        					}else{
        						alert("msg");
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
     <div id="loading" style="margin:300px 600px;"></div>
    <!-- 头部内容end -->
     <div class="main_box clearfix"> 
        <!-- 左侧菜单 -->
        
        <!-- 左侧菜单end -->
        <!-- 右侧内容 -->
        <div class="right_main_div0">
            <div class="W_width">

                <!-- 编辑标签 -->
                <p class="P_Label clearfix">
                   <a class="a_size01" style="cursor: pointer;" onclick="toSave()"> <span>新增</span></a>
                </p>
                <!-- 编辑标签end -->
               
                <!-- table内容 -->
                <div class="table_box" id="autoarea">
                    <table id="table">
                       
                    </table>
                     <!-- 新增/编辑弹窗 -->
                   <div id="alertWindow">
	                  
                  </div>  
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
     <!-- 服务设置左侧菜单 -->
    
     <!-- 服务设置左侧菜单 -->

</body>
</html>