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
    <title>事件记录</title>
    <link rel="stylesheet" type="text/css" href="<%=basePath%>/css/common.css">
    <link rel="stylesheet" type="text/css" href="<%=basePath%>/css/style.css">
    <script type="text/javascript" src="<%=basePath%>js/jquery-1.8.3.min.js"></script>
    <script type="text/javascript" src="<%=basePath%>js/My97DatePicker/WdatePicker.js"></script>
    <script type="text/javascript" src="<%=basePath%>js/opts.js"></script>
	<script type="text/javascript" src="<%=basePath%>js/spin.js"></script>
    <script type="text/javascript">
    	$(function(){
    		searchtest();
    		editLabel();
    		autoHeight();
    	});
    	
    	//检索
    	function searchtest(){
    		var loadValue=$("#loadValue").val();
    		if(loadValue=="0"){
    			$(".W_width").hide();
    			$("#loading").show(); 
    			$("#loadValue").val(1);
    		}
    		var device_name=$("#device_name").val();
    		var errorname=$("#errorname").val();
    		var start_time=$("#start").val();
    		var end_time=$("#end").val();
    		$.ajax({
    			type:'post',
    			url:'<%=basePath%>/ErrorDataParse/list.do',
    			data:{"device_name":device_name,"errorname":errorname,"start_time":start_time,"end_time":end_time},
    			success:function(data){
    				$(".W_width").show();
    				$("#loading").hide();
    				var result = JSON.parse(data);	
     			   	var msg=result.message;
     			   	if(msg=="获取成功"){
     			   	var list=result.responseData.errorDataParseList;
 			   		str="";
 			   		str+="<thead><tr><th style='width:5%;'>序号</th><th style='width:14%;'>变流器名称</th><th style='width:12%;'>事件类型</th><th style='width:12%;'>发生时间</th><th style='width:20%;'>参数值</th><th style='width:15%;'>参数解析</th><th style='width:17%;'>操作</th></tr></thead><tbody>";
 			   		if(list.length>0){
 			   			$.each(list,function(i,list){
 			   				str+="<tr><td>"+(i+1)+"</td><td>"+list.device_name+"</td><td>"+list.errorname+"</td><td>"+list.cometime+"</td><td>"+list.error_column+"</td><td>"+list.error_name+"</td>";
 			   				if(list.error_type!=5){
 			   					str+="<td><a style='cursor:pointer;display:none;' onclick=viewFaultHistory('"+list.errordata_id+"')>查看故障历史</a>"
 			   					+"<a  style='cursor:pointer' onclick=downwj('"+list.device_name+"','"+list.error_excel+"')>下载记录文件</a></td></tr>";
 			   				}else{
 			   					str+="<td></td></tr>";
 			   				}
 			   			});
 			   		}else{
 			   		str += "<tr>"
							+"<td colspan='7'>没有相关数据</td>"
							+"</tr>";
 			   		}
 			   		str+="</tbody>";
 			   		$("#table").html(str);
		   			$('#table tbody tr:even').css("backgroundColor", "#edf2f6");
     			   	}else{
     			   		alert(msg);
     			   	}
    			}
    		});
    	}
    	
    	//编辑标签
    	function editLabel(){
    		$.ajax({
    			type:'post',
    			url:'<%=basePath%>/ErrorDataParse/editLabel.do',
    			//data:'',
    			success:function(data){
    				var result = JSON.parse(data);	
     			   	var msg=result.message;
     			   	var list=result.responseData.errorTypeList;
     			   	if(msg=="获取成功"){
     			   		str="";
     			   		str+="<p class='P_Label01 clearfix input_style'>变流器名称<input type='text' id='device_name' name='device_name'>"
     			   		+"事件类型 &nbsp<select id='errorname' name='errorname' style='width:150px;'>"
     			   		+"<option value='0' selected>请选择事件类型</option>";
     			   		$.each(list,function(i,list){
     			   			str+="<option value="+list.id+">"+list.errorname+"</option>"; 
     			   		});
     			   		str+="</select>&nbsp&nbsp时间段<input  type='text' id='start'  onclick=Wdate(); style='width:200px; margin-right:10px;' />---&nbsp"
     			   		+" <input type='text' id='end' onclick=Wdate(); style='width:200px;'/>"
     	                +"<a class='A_a' style='cursor: pointer;' onclick='searchtest()'>查询</a></p>";
     			   		$("#editLabel").html(str);
     			   	}else{
     			   		alert(msg);
     			   	}
    			}});
    	}
    	
    	function Wdate(){
    		WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'});
    	}
    	
    	//下载
    	function downwj(device_name,error_excel){
    		if(device_name!=''){
    			device_name=encodeURI(encodeURI(device_name));
    			error_excel=encodeURI(encodeURI(error_excel));
  		   }
    		window.location.href="<%=basePath%>/ErrorDataParse/downloadLogFile.do?device_name="+device_name+"&error_excel="+error_excel; 
    	}
    	//查看故障历史
    	function viewFaultHistory(errordata_id){
    		$.ajax({
    			type:'post',
    			url:'<%=basePath%>/ErrorDataParse/viewFaultHistory.do',
    			data:{"errordata_id":errordata_id},
    			success:function(data){
    				var result = JSON.parse(data);	
     			   	var msg=result.message;
     			   	if(msg=="获取成功"){
     			   		var sixlist=result.responseData.modbusAddlist;
     			   		var list=result.responseData.otherList;
     			   		str="";
     			   		str+="<p class='p_style'>日期：";
     			   		$.each(sixlist,function(i,sixlist){
     			   			if(i==0){
     			   				str+=""+sixlist.shortvalue+'-'+"";
     			   			}
     			   			if(i==1){
 			   				str+=""+sixlist.shortvalue+'-'+"";
 			   				}
     			   			if(i==2){
 			   				str+=""+sixlist.shortvalue+"";
 			   				}
     			   		});
     			   		str+="<span>时间：";
     			   		$.each(sixlist,function(i,sixlist){
	     			   		if(i==3){
	 			   				str+=""+sixlist.shortvalue+':'+"";
	 			   			}
	 			   			if(i==4){
				   				str+=""+sixlist.shortvalue+':'+"";
				   				}
	 			   			if(i==5){
				   				str+=""+sixlist.shortvalue+"";
				   				}
     			   		});
     			   		str+="</span></p>"
                   		+" <table class='table_S' id='table'><thead><tr><th>序号</th><th>协议地址</th><th>参数类型</th><th>值</th></tr></thead><tbody>";
                   		$.each(list,function(i,list){
                   			str+="<tr><td>"+(i+1)+"</td><td>"+list.addr+"</td><td>"+list.name+"</td><td>"+list.shortvalue+"</td></tr>";
                   		});
     			   		str+="</tbody></table>";
     			   		alertwindow("故障历史",str);
     			   	}else{
     			   		alert(msg);
     			   	}
    			}});
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
    		var autoheight = $(window).height()-188;
    	    $("#autoarea").attr("style","min-height:"+autoheight+"px;max-height:"+autoheight+"px");
    	}
    </script>
    
    
</head>
<body style="background-color:#edf2f6">
<div id="loading" style="margin:300px 600px;"></div>
		 <!-- 头部内容home -->
		 
    <!-- 头部内容end -->
    <div class="main_box clearfix">
        <!-- 右侧内容 -->
        <div class="W_width">
            <!-- 编辑标签 -->
            <div id="editLabel"></div>
            
            <input type="hidden" id="loadValue" value="0" />
   		 	
           <!--  <p class="P_Label01 clearfix input_style">
                变流器名称
                <input type="text" id="device_name" name="device_name">
                事件类型
                <input type="text" id="errorname" name="errorname">
                时间段：	<input  type="text"  class="laydate-icon"  id="start" onclick="laydate({istime: true, format: 'YYYY-MM-DD hh:mm:ss'})" style="width:200px; margin-right:10px;" />
                 ---&nbsp
                 <input type="text" class="laydate-icon"  id="end"  onclick="laydate({istime: false, format: 'YYYY-MM-DD hh:mm:ss'})" style="width:200px;" />
                <span class="inline laydate-icon" id="start" style="width:200px; margin-right:10px;"></span>
               ---&nbsp
                <sapn class="inline laydate-icon" id="end" style="width:200px;"></sapn>
                <a class="A_a" style="cursor: pointer;" onclick="searchtest()">查询</a>
            </p> -->
            <!-- 编辑标签end -->
            <!-- table内容 -->
            <div class="table_box" id="autoarea">
            <table id="table">
                       
            </table>
			</div>
            <!-- table内容end -->
        </div>
        <!-- 右侧内容end -->
    </div>
    <div id="gray"></div>
    <!-- 新增/编辑弹窗 -->
    <!-- 新增/编辑弹窗end -->

<!-- 日期js -->
<script type="text/javascript" src="<%=basePath%>/js/laydate.js"></script>
<!-- 日期js -->
<!-- 共同引用jquery js -->
 <script type="text/javascript" src="<%=basePath%>/js/jquery1124.js"></script>
 <script type="text/javascript" src="<%=basePath%>/js/js.js"></script>
 <!-- 共同引用jquery js -->

</body>
</html>