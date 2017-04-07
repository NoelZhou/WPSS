<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html>
<html lang="en">

<head>
	<base href="<%=basePath%>"><!-- jsp文件头和头部 -->
    <meta charset="UTF-8">
    <title>系统设置-角色权限</title>
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
     		 var ajaxURL = "<%=basePath%>/Role/list.do";
     		   $.ajax({
     			   	type:"post",
     			   	url:ajaxURL,
     			   	//data:$('#login-form').serialize(),
     			   	success:function(data){
     			   	var result = JSON.parse(data);	
     			   	var msg=result.message;
     			    if(msg == "获取成功"){
     			    	$(".W_width").show();
     			    	$("#loading").hide(); 
     			    	 var str="";
     			    	str +="<table id='table'>" 
     			    	+"<thead>"
     			    	+"<tr>"
     			    	+"<th>序号</th>"
     			    	+"<th>角色</th>"
     			    	+"<th>操作</th>"
     			    	+"</tr>"
     			    	+"</thead>"
     			    	+"<tbody>";
     			    	if(result.responseData.roleList.length>0){
     			    		$.each(result.responseData.roleList, function(i, roleList){
							 	str += "<tr>"
							 	+"<td>"+(i+1)+"</td>"
								+"<td>"+roleList.role_name+"</td>"
								+"<td>"
								+"<a class='edit1' title='编辑' style='cursor: pointer;' onclick=toEdit('"+roleList.id+"')>编辑</a>"
								+"<a class='delete1' title='删除' style='cursor: pointer;' onclick=deleteRole('"+roleList.id+"')>删除</a>"
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
     		   } });
     		  autoHeight();
     	});
     </script>
     
     <script type="text/javascript">
 		function deleteRole(id){
	 		if(id!=""){
	 		   if(confirm("确定要删除数据吗？")){
	 		   		$.ajax({
	 		   			type:"POST",
	 		   			url:"<%=basePath%>/Role/delete.do",
	 		   			data:{"id":id},
	 		   			success:function(data){
	 		   			var result = JSON.parse(data);	
	 				   	var msg=result.message;	
	 		   				if(msg="获取成功"){
	 		   					window.location.href="<%=basePath%>/jsp/systemjsp/role.jsp";
	 		   				}else{
	 		   					alert(msg);
	 		   				}
	 		   				
	 		   			}});
	 			}
	 		}
	 	}
 		function toEdit(id){
 			if(id!=""){
 				window.location.href="<%=basePath%>/Role/toEdit.do?id="+id;		
			}
 		}
 		
 		function autoHeight(){
 			var height = $(window).height()-260;
 		    $("#table_box").attr("style","min-height:"+height+"px;max-height:"+height+"px");
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

                <!-- 头部切换标签 -->
                <div class="main_title_box">
                    <ul>
                        <li class="li_style"><a>角色权限</a></li>

                    </ul>
                </div>
                <!-- 头部切换标签end -->

                <!-- 编辑标签 -->
                <p class="P_Label clearfix">
                    <a class="a_size01" href="<%=basePath%>/Role/toInsert.do"><span>新增</span></a>

                </p>
                <!-- 编辑标签end -->
				
                <!-- table内容 -->
                <div class="table_box" id="table_box">
                 
                </div>

                <script type="text/javascript" src="<%=basePath%>/js/jquery.js"></script>

                <script>
                    $(function() { // dom元素加载完毕
                        $('#table tbody tr:even').css("backgroundColor", "#edf2f6");
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
