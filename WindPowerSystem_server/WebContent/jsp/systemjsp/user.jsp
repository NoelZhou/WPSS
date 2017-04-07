<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
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
<title>系统设置-用户管理</title>
<script type="text/javascript"> 

window.onload=function(){
	reload();
	autoHeight();
}
       <!--删除用户-->
   function deleteUser(){ 
		var len = document.getElementsByName("ids").length;
		var id = '';
		for(var i=0;i<len;i++){
			if(document.getElementsByName("ids")[i].checked){
				if(id == ''){
					id += document.getElementsByName("ids")[i].value;
				}else{
					id += "," +document.getElementsByName("ids")[i].value;
				}
			}
		}
		 if(id.length==0){
			   alert("请最少选择一条数据删除");
			   return false;
		   }
	  /*   if(id!=""){
		   if(id.split(",").length > 1){
			   alert("勾选太多，只能删除一条数据删除");
			   return false;
		   }  */
		   if(confirm("确定要删除数据吗?")){
			   var del = "<%=basePath%>User/delete.do";
			   $.ajax({
				   	type:"post",
				   	url:"<%=basePath%>User/delete.do",
				   	data:{"id":id},
				   	success:function(data){
				   	var result = JSON.parse(data);	
					  var msg=result.code;
				    if(msg=="1001"){
				    	window.location.reload();
				   	}else{
				   		alert(msg);
				   	}
			   } }); 
		   }
		  
	  
   } 
   <!--保存之前-->
   function addUserDialog(){
	   $.ajax({
		   	type:"post",
		   	url:"<%=basePath%>User/selectAllUser.do",
		   	success:function(data){
		     var result = JSON.parse(data);	
			  var msg=result.code;
		      if(msg =="1001"){
		    	  var users=result.responseData.userlist;
		    	  var contStr = "";
		    	contStr +="<ul class='ul_style' style='padding-left:80px'>"
		    	contStr +="<li><span>账号</span><input type='text' name='employeeId' id='employeeId' onblur='check()'/></li>"	
		    	contStr +="<li><span>密码</span><input type='password' name='passwd' id='passwd'/></li>"
		    	contStr +="<li><span>重复密码</span><input type='password' name='repasswd' id='repasswd'/></li>"
		    	contStr +="<li><span>角色</span><select id='roleId' name='roleId'>"
		    		$.each(result.responseData.rolelist,function(i,rolelist){
		    			contStr+="<option value="+rolelist.id+">"+rolelist.role_name+"</option>"
				 	});
		    	contStr +="</select></li>"
		    	contStr +=" <li><span>姓名</span><input type='text'name='name' id='name'/></li>"
		    	contStr +=" <li><span>公司名</span><input type='text'name='employeeName' id='employeeName'/></li>"
		    	contStr +=" <li><span>电话号码</span><input type='text' name='telephone' id='telephone'/></li>"
		    	contStr +=" <li><span>状态</span><select name='state' id='state'><option  value='0'>正常</option>"
		    	contStr +=" <option value='1'>禁用</option> </select></li></ul> "
		    	contStr +=" <a class='a_button' href='javascript:;' onclick=document.getElementById('iframe').contentWindow.toChildSave();>保存</a>"
		    	alertwindow("新增",contStr)	
		   	}else{
		   		alert(msg);
		   	}
	   } 
		   	});
	   
   }
   function usermanger(){
		 $("#usermanger").show();
	   }
  
   <!--保存之后-->
   function save(){ 
	   var wtd = window.top.document;
	   var employeeId=$("#employeeId",wtd).val();//账户名
	   var passwd=$("#passwd",wtd).val();
	   var repasswd=$("#repasswd",wtd).val();
	   var roleId = $("#roleId",wtd).val();
	   var name=$("#name",wtd).val();
	   var employeeName=$("#employeeName",wtd).val();
	   var telephone=$("#telephone",wtd).val();
	   var state=$("#state",wtd).val();
		if(employeeId==null||employeeId==""){
			alert("账号不为空");
			return;
			}
		if(passwd==null||passwd==""){
			alert("密码不能为空");
			return;
			}
		 if(repasswd!=passwd){
			alert("两次输入的密码不一致");
			return;

			} 
		if(name==null||name==""){
			alert("姓名不能为空");
			return;
			}
		if(employeeName==null||employeeName==""){
			alert("公司名不能为空");
			return;
			}
		if(telephone==null||telephone==""){
			alert("电话号码不能为空");
			return;
			}
		$.ajax({
			 type:"post",
			 data:{"employeeId":employeeId},
			  url:"<%=basePath%>User/selectAllUser.do",
			  success:function(data){
				  var result = JSON.parse(data);
				  var msg=result.code;
				  if(msg="1001"){
					  var iscz=false;
					  var useslist=result.responseData.userlist;
					     for(var i=0;i<useslist.length;i++){
					    	 var user= useslist[i];
					    	 if(user.employeeId==employeeId){
					    		 iscz=true; 
					    	 }
					     }
						  if(iscz==true){
								alert("帐号已存在，请重新输入");
								return;
							  }else{
								  $.ajax({
									   	type:"post",
									   	url:"<%=basePath%>User/insert.do",
									  	data:{"employeeId":employeeId,"passwd":passwd,"roleId":roleId,"name":name,"employeeName":employeeName,"telephone":telephone,"state":state},
									  	success:function(data){
									  	var result = JSON.parse(data);	
										  var msg=result.message;
									      if(msg =="添加成功"){
									        $("#employeeId",wtd).val("");
									        $("#roleId",wtd).empty();
									        $("#passwd",wtd).val("");
									        $("#name",wtd).val("");
									        $("#employeeName",wtd).val("");
									        $("#telephone",wtd).val("");
									        $("#state",wtd).empty();
									        $("#gray",wtd).hide();
											$("#alertwindow",wtd).hide();
									        window.location.reload();
									   	}else{
									   		alert(msg);
									   	}
								   } 
									   	});
							   
						  
					  }
					 
				  }
			  }
			  
		 });
	  
   }
   
   
   
   <!--修改用户之前-->
   function updateUserDialog(){
	   var len = document.getElementsByName("ids").length;
		var id = '';
		for(var i=0;i<len;i++){
			if(document.getElementsByName("ids")[i].checked){
				if(id == ''){
					id += document.getElementsByName("ids")[i].value;
				}else{
					id += "," +document.getElementsByName("ids")[i].value;
				}
			}	
		}
		if(id.length==0){
			alert("请选择一条数据编辑");
		}
		
	   if(id.split(",").length > 1){
		   alert("勾选太多，只能选择一条数据编辑");
		   return false;
	   }
	   $.ajax({
		   	type:"post",
		   	url:"<%=basePath%>User/selectUserById.do",
		   	data:{"id":id},
		   	success:function(data){
		   		var result = JSON.parse(data);	
				var msg=result.code;
		    	if(msg == "1001"){
	    		   var users = result.responseData.user;
	    		   var rList = result.responseData.rolelist;
	    		   var contStr = "";
			    	contStr +="<ul class='ul_style' style='padding-left:80px'>"
			    	contStr +="<li><span>账号</span><input type='text' name='employeeId' value='"+users.employeeId+"' id='employeeId'/></li>"
			    	contStr +="<li><span>角色</span><select id='roleId' name='roleId'>"
			    		$.each(rList,function(i,rolelist){
			    			contStr+="<option value='"+rolelist.roleId+"'";
			    			if(rolelist.roleId==users.roleId){
			    				contStr+= "selected=selected";
			    			}
			    			contStr+=">"+rolelist.role_name+"</option>";
					 	});
			    	contStr +="</select></li>"
			    	contStr +=" <li><span>姓名</span><input type='text'name='name'value='"+users.name+"' id='name'/></li>"
			    	contStr +=" <li><span>公司名</span><input type='text'name='employeeName'value='"+users.employeeName+"' id='employeeName'/></li>"
			    	contStr +=" <li><span>电话号码</span><input type='text' name='telephone' value='"+users.telephone+"' id='telephone'/></li>"
			    	contStr +=" <li><span>状态</span><select name='state' id='state'><option  value='0'"
			    	
			    	if(users.state==0){
			    		contStr +="selected"
			    	}
			    	contStr +=">正常</option>"
			    		
			    	contStr +=" <option value='1' "
			    	
			    		if(users.state==1){
				    		contStr +="selected"
				    	}
				    	contStr +=">禁用</option> </select> </li></ul>"
			    	
			    	contStr +=" <a class='a_button' href='javascript:;' onclick=document.getElementById('iframe').contentWindow.toChildEdit('"+users.id+"');>保存</a>"
			    	alertwindow("编辑",contStr)
			}else{
		   		   alert(msg);
		   		   window.close();
		   	}
	   }
		   	});
	   
   }
   <!--修改用户后-->
   function edit(id){
	   var wtd = window.top.document;
	   var employeeId=$("#employeeId",wtd).val();
	   var roleId = $("#roleId",wtd).val();
	   var name=$("#name",wtd).val();
	   var employeeName=$("#employeeName",wtd).val();
	   var telephone=$("#telephone",wtd).val();
	   var state=$("#state",wtd).val();
		if(employeeId==null||employeeId==""){
			alert("账号不能为空");
			return;
			}
		if(name==null||name==""){
			alert("姓名不能为空");
			return;
			}
		if(employeeName==null||employeeName==""){
			alert("公司名不能为空");
			return;
			}
		if(telephone==null||telephone==""){
			alert("电话号码不能为空");
			return;
			} 
		var flag=true;
		$.ajax({
			async: false, 
			type:"post",
			url:'<%=basePath%>User/updateCheck.do',
			cache: "false",
			data:{"id":id,"employeeId":employeeId},
			success:function(data){
				var result = JSON.parse(data);	
				var msg=result.code;
				var message=result.responseData.message;
				if(msg=="1001"){
					 if(message!=""){
						 alert(message);
						 flag=false;
					 }else{
						 $.ajax({
					  		   	type:"post",
					  		   	url:"<%=basePath%>User/update.do",
					  		   	data:{"name":name,"employeeId":employeeId,"employeeName":employeeName,"roleId":roleId,"telephone":telephone,"state":state,"id":id},
					  		   	success:function(data){
					  		   	var result = JSON.parse(data);	
								 var msg=result.code;
					  		    if(msg == "1001"){
					  		    	$("#employeeId",wtd).val("");
					 		        $("#roleId",wtd).empty();
					 		        $("#name",wtd).val("");
					 		        $("#employeeName",wtd).val("");
					 		        $("#telephone",wtd).val("");
					 		        $("#state",wtd).empty();
					 		        $("#gray",wtd).hide();
									$("#alertwindow",wtd).hide();
									window.location.reload();
					  		   	}else{
					  		   		alert("更新数据失败");
					  		   		window.close();
					  		   	   }
					  	      }
					  	  }); 
						 
					 }
				}
				
			}});
			if(flag==false){
				return false;
			}
	}

   function stop(state,id,num){
	  //进行启动禁止
	   var hanzi="";
	   var test="";
	   if(state==1){
		   hanzi="启用";
		   test="正常";
	   }
	   if(state==0){
		   hanzi="禁用";
		   test="禁用"; 
	   }
	   if(confirm("您确定要"+hanzi+"吗？")){
	   $.ajax({
		   type:"post",
		   data:{"state":state,"id":id},
		   url:"<%=basePath%>User/updateState.do",
		   success:function(data){
			 	var result = JSON.parse(data);	
				 var msg=result.code; 
					if(msg=="1001"){
					 /*  reload();  */
					$.ajax({   
						type:'post',
						url:'<%=basePath%>User/selectAllUser.do',
						success:function(data){
							var result = JSON.parse(data);	
							 var msg=result.code; 
							 var varList = result.responseData.userlist;
							 if(msg=="1001"){
								 $.each(varList,function(i,varList){
									if(i==num){
										 $("#table tr:gt('"+num+"') td:eq(6)").html(test);
										var str="";
										str+="<a onclick=retPasswd('"+varList.passwd+"','"+varList.id+"'); style='cursor:pointer;'>密码重置</a><a style='cursor:pointer;' onclick=stop('"+varList.state+"','"+varList.id+"','"+num+"')>";
					    				   if(varList.state==0){
					    					   str +="禁用";
					     				  }else{
					     					 str +="启用";
					     				  }
					    				   str +="</a>";
										 $("#table tr:gt('"+num+"') td:eq(7)").html(str);  
									}     
									
										
									 });
								
							 }
						}}); 
					
					
					
					
					
				 }
				 else{
					 alert(msg);
				 }
			   
		   }
	   });	
	   }
	   
	   
   }
   function retPasswd(passwd,id){//密码重置
	if(confirm("确定要恢复初始密码吗?")){
	   $.ajax({
		   type:"post",
		   data:{"passwd":passwd,"id":id},
		   url:"<%=basePath%>User/updatePass.do",
		   success:function(data){
			 	var result = JSON.parse(data);	
				 var msg=result.code; 
				 if(msg=="1001"){
					alert("密码重置成功，初始密码为123456");
				 }
				 else{
					 alert(msg);
				 }
			   
		   }
	   });	
	   }
	   
	   
   }
   
   function reload(){
	   $("#table tbody").html("");
	   $(".W_width").hide();
		$("#loading").show();
 <!--查询数据列表-->
 var sel = "<%=basePath%>User/selectAllUser.do";
   $.ajax({  
       url:sel,  
       type:"post",  
       success:function(data){  
    	    $(".W_width").show();
    		$("#loading").hide();
    	   var result = JSON.parse(data);
    	   if("1001" == result.code){
    		   var varList = result.responseData.userlist;
    		   var rList = result.responseData.rolelist;
    		   var contStr = "<tbody>";
    		   
    		   for(var i=0;i<varList.length;i++){
    			   contStr += "<tr><td><label class='label_input01'><input class='input_chk' id='bid' name='ids' type='checkbox' value='"+varList[i].id+"'></input>"
    				   contStr += "<span></span></label></td>"
    				   contStr += "<td>"+varList[i].employeeId+"</td>"
    				  /*  if(varList[i].role_name!="" || varList[i].role_name!=null){
    			       for(var j=0;j<rList.length;j++){
    				      if(varList[i].roleId==rList[j].roleId){
    				    	  contStr += "<td>"+rList[j].role_name+"</td>";  
    				        }
    			          } 
    			        }else{
    			    	 contStr += "<td>"+管理员已删除+"</td>";  
    			        } */
    			        if(varList[i].role_name=="" || varList[i].role_name==null){
    			        	contStr += "<td>角色已删除</td>";  
    			        }else{
    			        	contStr += "<td>"+varList[i].role_name+"</td>";  
    			        }
    				   contStr +=  "<td>"+varList[i].name+"</td>";
    				   contStr +=  "<td>"+varList[i].employeeName+"</td>";
    				   contStr +=  "<td>"+varList[i].telephone+"</td>";
    				  //  contStr +=  "<td>"+varList[i].state+"</td>"; 
    				  if(varList[i].state==0){
    					  contStr +=  "<td>正常</td>"; 
    				  }else{
    					  contStr +=  "<td>禁用</td>";  
    				  }
    				   contStr +=  "<td ><a onclick=retPasswd('"+varList[i].passwd+"','"+varList[i].id+"'); style='cursor:pointer;'>密码重置</a><a style='cursor:pointer;' onclick=stop('"+varList[i].state+"','"+varList[i].id+"','"+i+"')>";
    				   if(varList[i].state==0){
    					  contStr +="禁用";
    				  }else{
    					  contStr +="启动";
    				  }
    				  contStr +="</a></td></tr>";
    				  
    				   
    		   }
    		   contStr += "</tbody>";
    		   $("#table").append(contStr);
    		   
    		   $('#table tbody tr:even').css("backgroundColor", "#edf2f6");
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
	   	$('#table tbody tr:even',wtd).css("backgroundColor", "#edf2f6");
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
    

        <!-- 左侧菜单 -->
        
        <!-- 左侧菜单end -->

        <!-- 右侧内容 -->
        <div class="right_main_div0" style=" margin-top: 115px;">
            <div class="W_width">

                <!-- 头部切换标签 -->
                <div class="main_title_box">
                    <ul>
                        <li class="li_style" id="usermanger"><a>用户管理</a></li>
                    </ul>
                </div>
                <!-- 头部切换标签end -->

                <!-- 编辑标签 -->
                <p class="P_Label clearfix">
                    <span onclick="addUserDialog()"><a class="a_size01" href="javascript:;" >新增</a></span>
                    <span onclick="updateUserDialog()"><a class="a_size02" href="javascript:;" >编辑</a></span>
                    <span onclick="deleteUser()"><a class="a_size03" href="javascript:;" >删除</a></span>
                </p>
                <!-- 编辑标签end -->
				
                <!-- table内容 -->
                <div class="table_box" id="table_box">
                    <table id="table">
                        <thead>
                            <tr>
                                <th width="6%;">
                                    <label class="label_input01">
                                        <!-- <input class="input_chk" type="checkbox" name="selectAllUser"></input> -->
                                        <span></span>
                                    </label>
                                </th>
                                <th width=130px;>账号</th>
                                <th width=110px;>角色</th>
                                <th width=110px;>姓名</th>
                                <th width=190px;>公司名</th>
                                <th width=200px;>电话号码</th>
                                <th width=70px;>状态</th>
                                <th width=190px;>操作</th>
                            </tr>
                        </thead>
                    </table>
   
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