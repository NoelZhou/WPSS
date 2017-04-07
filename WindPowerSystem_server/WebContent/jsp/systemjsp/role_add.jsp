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
    <title>系统设置-角色权限-新增</title>
    <link rel="stylesheet" type="text/css" href="<%=basePath%>/css/common.css">
    <link rel="stylesheet" type="text/css" href="<%=basePath%>/css/style.css">
     <script type="text/javascript" src="<%=basePath%>js/jquery-1.8.3.min.js"></script>
     <script type="text/javascript">
     	function save(){
     		var str=null;
     		str=$(".a_size01").attr("disabled");
     		if(str!=null && str=="disabled"){
     			return false;
     		}
     		if($("#role_name").val()==""){
     			alert("请输入角色名称！");
     			$("#role_name").focus();
     			return false;
     		}
     		if($("#describes").val()==""){
     			alert("请输入角色描述！");
     			$("#describes").focus();
     			return false;
     		}
     		
     		var menuId='';  //菜单id
     		for(var i=0;i < document.getElementsByName('read_p').length;i++){
				  if(document.getElementsByName('read_p')[i].checked){    //选中
					if(menuId=='') menuId +=document.getElementsByName('read_p')[i].title;
				  	else menuId += ',' + document.getElementsByName('read_p')[i].title;
				  }else{                                                 //未选中
					  	if(menuId=='') menuId +=document.getElementsByName('read_p')[i].title;
					  	else menuId += ','+document.getElementsByName('read_p')[i].title;
					}
			}
     		$("#MENUID").val(menuId);
     		
     		var read_p = '';   //读权限和菜单id
			for(var i=0;i < document.getElementsByName('read_p').length;i++){
				  if(document.getElementsByName('read_p')[i].checked){    //选中
					document.getElementsByName('read_p')[i].value=0;
				  	if(read_p=='') read_p += document.getElementsByName('read_p')[i].value;
				  	else read_p += ',' + document.getElementsByName('read_p')[i].value;
				  }else{                                                 //未选中
					  document.getElementsByName('read_p')[i].value=1;
					  	if(read_p=='') read_p += document.getElementsByName('read_p')[i].value;
					  	else read_p += ',' + document.getElementsByName('read_p')[i].value;
					}
			}
     		$("#READ_P").val(read_p);
     		
			var write_p ='';   //写权限和菜单id
			for(var i=0;i < document.getElementsByName('write_p').length;i++){
				  if(document.getElementsByName('write_p')[i].checked){    //选中
					document.getElementsByName('write_p')[i].value=0;
				  	if(write_p=='') write_p += document.getElementsByName('write_p')[i].value;
				  	else write_p += ',' + document.getElementsByName('write_p')[i].value;
				  }else{                                                 //未选中
					  document.getElementsByName('write_p')[i].value=1;
					  	if(write_p=='') write_p += document.getElementsByName('write_p')[i].value;
					  	else write_p += ',' + document.getElementsByName('write_p')[i].value;
					}
			}
			$("#WRITE_P").val(write_p);
			
     		var MENUID=$("#MENUID").val();
     		var READ_P=$("#READ_P").val();
     		var WRITE_P=$("#WRITE_P").val();
     		var role_name=$("#role_name").val();
     		var describes=$("#describes").val();
     		var create_user=$("#create_user").val();
     		$.ajax({
			   	type:"POST",
			   	url: '<%=basePath%>Role/${msg}.do',
			   	data:{"role_name":role_name,"create_user":create_user,"describes":describes,"MENUID":MENUID,"READ_P":READ_P,"WRITE_P":WRITE_P},
			   	success:function(data){
			   	var result = JSON.parse(data);	
			   	var msg=result.message;
			    if(msg == "获取成功"){
			    	window.location.href="<%=basePath%>/jsp/systemjsp/role.jsp";
			   	}else{
			   		alert(msg);
			   	}
		   } });
     		$(".a_size01").attr("disabled","disabled");
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

                <!-- 头部切换标签 -->
                <div class="main_title_box">
                    <ul>
                        <li class="li_style"><a >新增</a></li>

                    </ul>
                </div>
                <!-- 头部切换标签end -->
				<form action="" method="post" id="Form" name="Form">
				<input type="hidden" id="MENUID" name="MENUID"/>
				<input type="hidden" id="READ_P" name="READ_P"/>
				<input type="hidden" id="WRITE_P" name="WRITE_P"/>
				<c:if test="${roleAll.role_name==null}">
				<input type="hidden" id="create_user" name="create_user" value="0"/>
				</c:if>
				<c:if test="${roleAll.role_name!=null}">
				<input type="hidden" id="create_user" name="create_user" value="${roleAll.id}"/>
				</c:if>
                <!-- 编辑标签 -->
                <p class="P_Label clearfix input_style">
                	角色名称<input type="text" id="role_name" name="role_name" value="${roleAll.role_name}"></input>
                 	角色描述<input type="text" id="describes" name="describes" value="${roleAll.describes}"></input>
                 	<a class="a_size01" onclick="save()" style="cursor: pointer;"><span>保存</span></a>
                </p>
                
                <!-- 编辑标签end -->

                <!-- table内容 -->
                <div class="table_box" style="min-height:330px;max-height:330px;overflow: auto">
                   <table id="table">
                        <thead>
                            <tr>
                            	<th style="width:50px;">序号</th>
                                <th>菜单</th>
                                <th>读权限</th>
                                <th>写权限</th>
							</tr>
                        </thead>
                        <tbody>
                        <c:if test="${roleAll.role_name==null}">
                        <c:choose>
                        	<c:when test="${not empty MenuList}">
                        		<c:forEach items="${MenuList}" var="var" varStatus="vs">
                        			<tr > 
                        				<td style="width:50px;">${vs.index+1}</td>
		                                <td>${var.menu_name}</td>
		                                <td>
		                                    <label class="label_input">
		                                        <input title="${var.id}" class="input_chk" type="checkbox" id="read_p" name="read_p"></input>
		                                        <span></span>
		                                    </label>
		                                </td>
		                                <td>
		                                    <label class="label_input">
		                                        <input title="${var.id}" class="input_chk" type="checkbox" id="write_p" name="write_p"></input>
		                                        <span></span>
		                                    </label>
		                                </td>
                            	</tr>
                        		</c:forEach>
                        		
                        	</c:when>
                        	<c:otherwise>
						<tr>
							<td colspan="100">没有相关数据</td>
						</tr>
					</c:otherwise>
                        </c:choose>	
                   </c:if>
                     
                   <c:if test="${roleAll.role_name!=null}">
                   
                     <c:choose>
                        	<c:when test="${not empty role_menuList}">
                        		<c:forEach items="${role_menuList}" var="var" varStatus="vs">
                        			<tr > 
                        				<td style="width:50px;">${vs.index+1}</td>
		                                <td>${var.menu_name}</td>
		                                <td>
		                                    <label class="label_input">
		                                        <input title="${var.menu_id}" value="${var.read_p}"  <c:if test="${var.read_p==0}">checked</c:if>   class="input_chk" type="checkbox" id="read_p" name="read_p"></input>
		                                        <span></span>
		                                    </label>
		                                </td>
		                                <td>
		                                    <label class="label_input">
		                                        <input title="${var.menu_id}" value="${var.write_p}" <c:if test="${var.write_p==0}">checked</c:if> class="input_chk" type="checkbox" id="write_p" name="write_p"></input>
		                                        <span></span>
		                                    </label>
		                                </td>
                            	</tr>
                        		</c:forEach>
                        		
                        	</c:when>
                        	<c:otherwise>
						<tr>
							<td colspan="100">没有相关数据</td>
						</tr>
					</c:otherwise>
                        </c:choose>	
                        
                   </c:if>
                          
                        </tbody>
                    </table>
                </div>
				</form>
                <script type="text/javascript" src="<%=basePath%>js/jquery.js"></script>

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
