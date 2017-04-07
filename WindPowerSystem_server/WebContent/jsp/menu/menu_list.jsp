<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script type="text/javascript" src="../js/jquery-1.8.3.min.js"></script>
<title>菜单管理</title>
<script type="text/javascript">
function stopUse(id){
	url = "../Menu/updateState.do?id="+id+"&state=1";
	$.get(url,function(data){
		var result = JSON.parse(data);
		if("1001" == result.code){
			alert("已禁用");
			$("#Form").submit();
		}else if("1002" == result.code){
			alert("禁用失败");
		}
	});
}

function startUse(id){
	url = "../Menu/updateState.do?id="+id+"&state=0";
	$.get(url,function(data){
		var result = JSON.parse(data);
		if("1001" == result.code){
			alert("已启用");
			$("#Form").submit();
		}else if("1002" == result.code){
			alert("启用失败");
		}
	});
}
</script>
</head>
<body>
<form action="../Menu/list.do" id="Form" method="post">
	<table style="border:1px solid #a0c6e5;">
		<tr>
			<td style="width:200px;">菜单名</td>
			<td style="width:400px;">状态</td>
		</tr>
		<c:forEach items="${menuList}" var="menu">
			<tr>
				<td>${menu.menu_name}</td>
				<td>
					<c:if test="${menu.state == '0'}">启用   &nbsp; <a style="color:red;cursor:pointer ;" onclick="stopUse('${menu.id}')">禁用</a></c:if>
					<c:if test="${menu.state == '1'}"><a style="color:green;cursor:pointer ;" onclick="startUse('${menu.id}')">启用</a> &nbsp; 禁用</c:if>
				</td>
			</tr>	
		</c:forEach>
	</table>
</form>
</body>
</html>