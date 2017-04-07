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
<script type="text/javascript" src="<%=basePath%>js/opts.js"></script>
<script type="text/javascript" src="<%=basePath%>js/spin.js"></script>
<title>运行报表</title>
<script type="text/javascript">
var choosetime;
$(function(){
	$("#W_width").hide();
	$("#loading").show();
	var url ="<%=basePath%>/ReportSet/listFormTime.do?form_type=2&showin=1";
	$.post(url,function(data){
		var result = JSON.parse(data);
		var timelist = result.responseData.formTimeList;
		var firsttime;
		var timeStr = "";
		for (var i = 0; i < timelist.length; i++) {
			timeStr += "<li style='width: 12%;'";
			if(i == 0){
				timeStr += "class='li_style'";
				firsttime = timelist[i].time_name;
				choosetime = firsttime;
			}
			timeStr += "><a style='cursor:pointer;' onclick=chooseTime('"+timelist[i].time_name+"');>"+timelist[i].time_name+"</a></li>";
		}
		$("#W_width").show();
		$("#loading").hide();
		document.getElementById("timeul").innerHTML = timeStr;
		chooseTime(firsttime);
	});
	
	$('li').live('click',function(){
		$(this).addClass('li_style').siblings('li').removeClass('li_style');
	}); 
	autoHeight();
});

function chooseTime(timename){
	choosetime = timename;
	$("#autoarea").hide();
	$("#loading").show();
	$.ajax({
		type:'post',
		url:'<%=basePath%>/ReportSta/listRunData.do',
		data:{"timename":timename},
		success:function(data){
			$("#loading").hide();
			$("#autoarea").show();
			var result = JSON.parse(data);
			var rowlist = result.responseData.colnumList;
			var context = "";
			var codeStr = "";
			if(rowlist.length > 0){
				context += "<thead><tr><th>时间</th><th>风机号</th>";
				$.each(rowlist, function(i,list){     
				    context += "<th>"+list.errorcode+"</th>";
				    if(codeStr == ""){
				    	codeStr += list.codesx;
				    }else{
				    	codeStr += ","+list.codesx;
				    }
				}); 
				context += "</tr></thead><tbody>";
				var runlist = result.responseData.runList;
				$.each(runlist, function(i,rlist){  
				   context += "<tr><td>"+rlist.createtime+"</td><td>"+rlist.device_name+"</td>";
				   var codeArray = codeStr.split(",");
				   for(var j=0;j<codeArray.length;j++){
					   var value = eval("rlist."+codeArray[j]);
					   context += "<td>"+value+"</td>";
				   }
				   context += "</tr>";
				}); 
				context += "</tbody>";
			}else{
				context = "<thead><tr><th>没有相关数据</th></tr></thead>";
			}
						
			$("#table").html(context);
			$('#table tbody tr:even').css("backgroundColor", "#edf2f6");
		}
	});
	
	
}

function exportExcel(){
	window.location.href = "<%=basePath%>/ReportSta/exportExcel.do?form_type=2&timename="+encodeURI(encodeURI(choosetime));
}

function autoHeight(){
	var autoheight = $(window).height()-110;
    $("#autoarea").attr("style","min-height:"+autoheight+"px;max-height:"+autoheight+"px");
}
</script>
</head>
<body>
	
	<div id="W_width">
	<!-- 头部切换标签 -->
	<div class="main_title_box ">
	    <ul class="text_box" id="timeul"> 
	    </ul>
	</div>
	<!-- 头部切换标签end -->
	
	<!-- 编辑标签 -->
	<p class="P_Label clearfix" style="margin:5px 0 5px">
		<span onclick="exportExcel()"><a class="a_size01" href="javascript:;" >导出</a></span>
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