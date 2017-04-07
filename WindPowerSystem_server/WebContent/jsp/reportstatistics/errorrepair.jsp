<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>       
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
<script type="text/javascript" src="<%=basePath%>js/opts.js"></script>
<script type="text/javascript" src="<%=basePath%>js/spin.js"></script>
<title>故障派单</title>
<script type="text/javascript">
$(function(){
	chooseUrl(1);
	$('li').live('click',function(){
		$(this).addClass('li_style').siblings('li').removeClass('li_style');
	}); 
	autoHeight();
});
function chooseUrl(obj){
	$(".table_box").hide();
	$("#loading").show();
	var url = "<%=basePath%>/ReportSta/listErrorRepair.do?repair_state="+obj;
	$.post(url,function(data){
		$(".table_box").show();
		$("#loading").hide();
		var result = JSON.parse(data);
		var varList = result.responseData.varList;
		var context = "<thead><tr><th>变流器</th><th>事件类型</th>"
		+"<th>入库时间</th><th>发生时间</th><th>截止时间</th><th>操作</th></tr></thead>";
		if(varList.length > 0){
			context += "<tbody>";
			$.each(varList, function(i,list){     
				context += "<tr><td>"+list.device_name+"</td><td>"+list.errorname+"</td>"
				+"<td>"+list.createtime+"</td><td>"+list.cometime+"</td><td>"+list.endtime+"</td><td>";
				if(obj == "1"){
					context += "<a style='cursor:pointer;' onclick='resultEntry("+list.id+");'>处理结果录入</a>";
				}else if(obj == "2"){
					context += "<a style='cursor:pointer;' onclick='viewresult("+list.id+");'>查看处理详情</a>";
				}
				context += "</td></tr>";
			}); 
		}else{
			context += "<tbody><tr><td colspan='7'>没有相关数据</td></tr>";
		}
		context += "</tbody>";
		$("#table").html(context);
		$('#table tbody tr:even').css("backgroundColor", "#edf2f6");
	});
}

function resultEntry(id){
	var wtd = window.top.document;
	var url = "<%=basePath%>/ReportSta/getErrorRepair.do?id="+id;
	$.post(url,function(data){
		var result = JSON.parse(data);
		var obj = result.responseData.errorDataRepair;
		var str = "<input type='hidden' id='errorid' value='"+obj.id+"'/><ul class='ul_style  ul_W clearfix'>"
		+ "<li><span>变流器</span><input type='text' id='devicename' disabled='disabled' value='"+obj.device_name+"'/></li>"
		+ "<li><span>事件类型</span><input type='text' id='errorname' disabled='disabled' value='"+obj.errorname+"'/></li>"
		+ "<li style='display:none;'><span>故障记录</span><input type='text' id='errorcolumn' disabled='disabled' value='"+obj.error_column+"'/></li>"
		+ "<li><span>入库时间</span><input type='text' id='createtime' disabled='disabled' value='"+obj.createtime+"'/></li>"
		+ "<li><span>发生时间</span><input type='text' id='cometime' disabled='disabled' value='"+obj.cometime+"'/></li>"
		+ "<li><span>截止时间</span><input type='text' id='endtime' disabled='disabled' value='"+obj.endtime+"'/></li>"
		+ "<li><span>处理人</span><input type='text' id='repairuser'/></li>"
		+ "<li class='li_W'><span>处理结果</span><textarea id='repairresult'></textarea></li></ul>"
		+ "<a class='a_button' style='cursor:pointer;' onclick=document.getElementById('iframe').contentWindow.toChildSave();>提交</a>";
		alertwindow("处理结果录入",str)
	});
}

function viewresult(id){
	var wtd = window.top.document;
	var url = "<%=basePath%>/ReportSta/getErrorRepair.do?id="+id;
	$.post(url,function(data){
		var result = JSON.parse(data);
		var obj = result.responseData.errorDataRepair;
		var str = "<ul class='ul_style  ul_W clearfix'>"
		+ "<li><span>变流器</span><input type='text' disabled='disabled' value='"+obj.device_name+"'/></li>"
		+ "<li><span>事件类型</span><input type='text' disabled='disabled' value='"+obj.errorname+"'/></li>"
		+ "<li><span>故障记录</span><input type='text' disabled='disabled' value='"+obj.error_column+"'/></li>"
		+ "<li><span>入库时间</span><input type='text' disabled='disabled' value='"+obj.createtime+"'/></li>"
		+ "<li><span>发生时间</span><input type='text' disabled='disabled' value='"+obj.cometime+"'/></li>"
		+ "<li><span>截止时间</span><input type='text' disabled='disabled' value='"+obj.endtime+"'/></li>"
		+ "<li><span>处理人</span><input type='text' disabled='disabled' value='"+obj.repair_user+"'/></li>"
		+ "<li class='li_W'><span>处理结果</span><textarea disabled='disabled'>"+obj.repair_result+"</textarea></li></ul>"
		alertwindow("处理结果详情",str)
	});
}

//保存处理结果
function save(){
	var wtd = window.top.document;
	var id = $("#errorid",wtd).val();
	var repairuser = $("#repairuser",wtd).val();
	var repairresult = $("#repairresult",wtd).val();
	if(repairuser == ""){
		alert("处理人不能为空！");
		return false;
	}
	if(repairresult == ""){
		alert("处理结果不能为空！");
		return false;
	}
	$.ajax({
		type:'post',
		url:'<%=basePath%>/ReportSta/updateErrorRepair.do',
		data:{"id":id,"repair_user":repairuser,"repair_result":repairresult},
		success:function(data){
			var result = JSON.parse(data);
			if("更新成功" == result.message){
				$("#repairuser",wtd).val("");
				$("#repairresult",wtd).val("");
				$("#gray",wtd).hide();
				$("#alertwindow",wtd).hide();
				window.location.reload();
			}else{
				alert(msg);	
			}
		}
	});
}

function alertwindow(title,str){
	var wtd = window.top.document;
   	$("#gray",wtd).show();
   	$("#alertwindow",wtd).show();
   	$("#titletxt",wtd).html(title);
   	$("#contentdiv",wtd).html(str);
}
function autoHeight(){
	var autoheight = $(window).height()-100;
    $("#autoarea").attr("style","min-height:"+autoheight+"px;max-height:"+autoheight+"px");
}

</script>
</head>
<body>
	
	<!-- 头部切换标签 -->
	<div id="W_width">
    <div class="main_title_box ">
        <ul class="text_box">
            <li class="li_style"><a href="javascript:;" onclick="chooseUrl(1)">处理中故障</a></li>
            <li><a href="javascript:;" onclick="chooseUrl(2)">处理完毕故障</a></li>
        </ul>
    </div>
	<!-- 头部切换标签end -->
	
	<!-- 编辑标签 -->
	<p class="P_Label clearfix">
	
	</p>
	<!-- 编辑标签end -->
	
	<!-- table内容 -->
	<div id="loading" style="margin:100px 500px;"></div>
	<div class="table_box" id="autoarea">
	
	    <table class="table_W" id="table">
	        
	    </table>
	</div>
	</div>
	<!-- table内容end -->
</body>
</html>