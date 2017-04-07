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
<meta http-equiv="Content-Type"  content="text/html; charset=utf-8">
<link href="<%=basePath%>/favicon.ico" rel="icon" type="image/x-icon" />
<link href="<%=basePath%>/favicon.ico" rel="shortcut icon" type="image/x-icon" />
<title>设备首页</title>
 <script type="text/javascript" src="<%=basePath%>js/jquery-1.8.3.min.js"></script>
<script>

        //开始采集
        function startcj(){
        	setTimeout(function(){
	        	document.getElementById("cjid2").style.display="block";
	        	document.getElementById("cjid").style.display="none";
	        	var url="<%=basePath%>AcceptServlet";
	        	$.get(url);
        	},1000);
        }
        //停止采集
        function stopcj(){
        	setTimeout(function(){
        		document.getElementById("cjid").style.display="block";
            	document.getElementById("cjid2").style.display="none";
            	var url = "<%=basePath%>StopServlet";
            	$.get(url);
        	},1000);
        	
        }
        
 		//改变设备状态
 		function changeState(id,state){
 			var msg="";
 			if(state==0){
 				msg="启动";
 			}else{
 				msg="禁止";
 			}
 			if(confirm("您确定"+msg+"吗?")){
 				window.location.href="<%=basePath%>ChangeStateServlet?id="+id+"&state="+state;
			}
 		}
 </script>
 
 <style type="text/css">
table.gridtable {
	font-family: verdana,arial,sans-serif;
	font-size:14px;
	color:#333333;
	border-width: 1px;
	border-color: #666666;
	border-collapse: collapse;
}
table.gridtable th {	
	border-width: 1px;
	padding: 5px;
	border-style: solid;
	border-color: #666666;
	background-color: #dedede;
}
table.gridtable td {
	border-width: 1px;
	padding: 5px;
	border-style: solid;
	border-color: #666666;
	/* background-color: #ffffff; */
	text-align: center;
}
a.aCJ:hover{
	color:#488fd2;
}
</style>
</head>
<body>
			<table class="gridtable"  id="table" style="margin:0 auto;" width=90%;>
				<tr>
					<th>序号</th>
					<th>设备名称</th>
					<th>协议类型</th>
					<th>设备IP地址</th>
					<th>设备状态</th>
					<th>操作</th>
					<th>采集设备</th>
				</tr>
				<tbody>
				<c:forEach items="${DeviceAndInfoList}" var="var" varStatus="vs">
				   <tr >
						<td>${vs.index+1}</td>
						<td>${var.name}</td>
						<td>${var.mtname}</td>
						<td>${var.ip}</td>
						<td>
							<c:if test="${var.run_state==0}">启动</c:if>
							<c:if test="${var.run_state==1}">禁止</c:if>
						</td> 
						<td>
							<a style="cursor: pointer;color:red;" onclick="changeState('${var.id}','1')"><c:if test="${var.run_state==0}"><b>禁止</b></c:if></a>
							<a style="cursor: pointer;color:#488fd2;" onclick="changeState('${var.id}','0')"><c:if test="${var.run_state==1}"><b>启动</b></c:if></a>
						</td>
						<c:if test="${vs.index+1==1}"><td  rowspan="${DeviceAndInfoList.size()}"><a class="aCJ"  id="cjid2" style="display:none;cursor: pointer;" onclick="stopcj()">停止采集</a><a class="aCJ"  id="cjid" style="cursor: pointer;" onclick="startcj()">开始采集</a></td></c:if>
						
					</tr>
				</c:forEach>
				</tbody>
			</table>
			
			 <script>
				$(function() { // dom元素加载完毕
                        $('#table tbody tr:even').css("backgroundColor", "#edf2f6");
                        //获取id为tb的元素,然后寻找他下面的tbody标签，再寻找tbody下索引值是偶数的tr元素,改变它的背景色.
                        //tr:odd为奇数行，索引从0开始，0算偶数。
                    });
                </script>
</body>
</html>