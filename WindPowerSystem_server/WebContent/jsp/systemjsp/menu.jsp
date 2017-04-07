<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<base href="<%=basePath%>">
<!-- jsp文件头和头部 -->
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>系统设置-菜单管理</title>
<link rel="stylesheet" type="text/css"
	href="<%=basePath%>/css/common.css">
<link rel="stylesheet" type="text/css"
	href="<%=basePath%>/css/style.css">
<script type="text/javascript" src="<%=basePath%>js/jquery-1.8.3.min.js"></script>
<script type="text/javascript" src="<%=basePath%>js/opts.js"></script>
<script type="text/javascript" src="<%=basePath%>js/spin.js"></script>
<script type="text/javascript">
    	$(function(){
    		$(".W_width").hide();
    		$("#loading").show(); 
    		$.ajax({
    			type:"post",
    			url:"<%=basePath%>/Menu/list.do",
    			//data:""
    			success:function(data){
    				$(".W_width").show();
    				$("#loading").hide(); 
    				var result = JSON.parse(data);	
     			   	var msg=result.message;
     			    if(msg == "获取成功"){
     			    	 var str="";
      			    	str +="<table id='table'>" 
      			    	+"<thead>"
      			    	+"<tr>"
      			    	+"<th>序号</th>"
      			    	+"<th>菜单名</th>"
      			    	+"<th>状态</th>"
      			    	+"</tr>"
      			    	+"</thead>"
      			    	+"<tbody>";
      			    	if(result.responseData.menuList.length>0){
      			    		$.each(result.responseData.menuList, function(i, menuList){
 							 	str += "<tr>"
 							 	+"<td>"+(i+1)+"</td>"
 								+"<td>"+menuList.menu_name+"</td>"
 								+"<td>";
 								if(menuList.state==0){
 									str +="<a class='a_01' >启用</a>"
	 								+"<a style='cursor: pointer;' onclick=stop('"+menuList.id+"','"+1+"')>禁用</a>";
 								}
 								if(menuList.state==1){
 									str +="<a style='cursor: pointer;' onclick=startup('"+menuList.id+"','"+0+"')>启用</a>"
 	 								+"<a class='a_02' >禁用</a>";
 	 							}
 								+"</td>"
 								+"</tr>";
 							 });
      			    	}else{
      			    		str += "<tr>"
 							+"<td colspan='3'>没有相关数据</td>"
 							+"</tr>";
      			    	}
      			    	+"</tbody>"
      			    	+"</table>"; 
      			    	document.getElementById("table_box").innerHTML=str;
      			    	$('#table tbody tr:even').css("backgroundColor", "#edf2f6");
     			    	
     			    }else{
     			    	alert(msg);
     			    }
    			}});
    		autoHeight();
    	});
    </script>
<script type="text/javascript">
    	function startup(id,state){
    		if(confirm("确定要启用吗？")){
    			$.ajax({
    				type:'post',
    				url:'<%=basePath%>/Menu/updateState.do',
    				data:{"id":id,"state":state},
    				success:function(data){
    					var result = JSON.parse(data);	
         			   	var msg=result.message;
         			   	if(msg == "更新成功"){
         			    	window.location.reload();
         			    }else{
         			    	alert(msg);
         			    }
         			    
         			    
    				}
    			
    			});
    		}
    	}
    	
    	function stop(id,state){
    		if(confirm("确定要禁用吗？")){
    			$.ajax({
    				type:'post',
    				url:'<%=basePath%>/Menu/updateState.do',
				data : {
					"id" : id,
					"state" : state
				},
				success : function(data) {
					var result = JSON.parse(data);
					var msg = result.message;
					if (msg == "更新成功") {
						window.location.reload();
					} else {
						alert(msg);
					}

				}

			});
		}
	}

	function autoHeight() {
		var height = $(window).height() - 225;
		$("#table_box").attr("style",
				"min-height:" + height + "px;max-height:" + height + "px");
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

				<!-- 头部切换标签 -->
				<div class="main_title_box">
					<ul>
						<li class="li_style"><a>菜单管理</a></li>

					</ul>
				</div>
				<!-- 头部切换标签end -->

				<!-- 编辑标签 -->
				<p class="P_Label clearfix"></p>
				<!-- 编辑标签end -->

				<!-- table内容 -->
				<div class="table_box" id="table_box"></div>

				<script type="text/javascript" src="<%=basePath%>/js/jquery.js"></script>

				<script>
					$(function() { // dom元素加载完毕
						$('#table tbody tr:even').css("backgroundColor",
								"#edf2f6");
						//获取id为tb的元素,然后寻找他下面的tbody标签，再寻找tbody下索引值是偶数的tr元素,改变它的背景色.
						//tr:odd为奇数行，索引从0开始，0算偶数。
					})
				</script>
				<!-- table内容end -->
			</div>
		</div>
		<!-- 右侧内容end -->


	</div>

</body>
</html>