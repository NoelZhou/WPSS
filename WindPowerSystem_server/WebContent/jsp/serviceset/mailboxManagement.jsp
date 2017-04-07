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
<title>服务设置-接收邮件管理</title>
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
    		type:'post',
    		url:'<%=basePath%>/ErrorWarnSet/listUserEmail.do',
    		success:function(data){
    			$(".W_width").show();
    			$("#loading").hide(); 
    			var result = JSON.parse(data);
				var msg=result.message;
				if(msg=="获取成功"){
					var list=result.responseData.varList;
					str="";
					str+="<thead><tr><th><label class='label_input01'><span></span>"
					+"</label></th><th>序号</th><th>姓名</th><th>角色</th><th>邮箱</th></tr></thead><tbody>";
					if(list.length>0){
						$.each(list,function(i,list){
							str+="<tr><td><label class='label_input01'><input class='input_chk' title='"+list.id+"' name='checkTo' id='checkTo' type='checkbox'></input><span></span></label></td>"
                        	+"<td>"+(i+1)+"</td><td>"+list.name+"</td><td>"+list.role_name+"</td><td>"+list.emailaddr+"</td></tr>";
						});
					}else{
						str += "<tr>"
							+"<td colspan='5'>没有相关数据</td>"
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
    	autoHeight();
    });
    
    //新增之前
    function toSave(){
    	$.ajax({
    		type:'post',
    		url:'<%=basePath%>/User/selectAllUser.do',
    		success:function (data){
    			var result = JSON.parse(data);
				var msg=result.message;
				var list=result.responseData.userlist;
				if(msg=="查询成功"){
					var str="";
					str+="<ul class='ul_style'>"
					+"<li><span>姓名</span> <select id='user_id'>";
                   		$.each(list,function(i,list){
                   			str+="<option value="+list.id+">"+list.name+"</option>";
						});
					str+="</select></li><li><span>邮箱</span><input type='text' id='emailaddr'></input></li></ul><a class='a_button' style='cursor: pointer;' onclick=document.getElementById('iframe').contentWindow.toChildSave();>保存</a>";
					alertwindow("新增",str)
				}else{
					alert(msg);
				}
    		}
    	});
    }
    //新增之后
    function save(){
    	var wtd = window.top.document;
    	var user_id=$("#user_id",wtd).val();
    	var emailaddr=$("#emailaddr",wtd).val();
    	if(emailaddr==""){
    		alert("请输入邮箱地址");
    		return false;
    	}
    	$("#loading").show(); 
    	$.ajax({
    		type:'post',
    		url:'<%=basePath%>/ErrorWarnSet/insertUserEmail.do',
    		data:{"user_id":user_id,"emailaddr":emailaddr},
    		success:function(data){
    			var result = JSON.parse(data);
				var msg=result.message;
				if(msg=="添加成功"){
					$("#gray",wtd).hide();
					$("#alertwindow",wtd).hide();
					$("#loading").hide(); 
		    		window.location.reload();
				}else{
					$("#loading").hide(); 
					alert(msg);
				}
    		}
    	});
    }
    //修改之前
    function toEdit(){
    		var id='';  //菜单id
     		for(var i=0;i < document.getElementsByName('checkTo').length;i++){
				  if(document.getElementsByName('checkTo')[i].checked){    //选中
					  if(id=='') id +=document.getElementsByName('checkTo')[i].title;
					  	else id += ',' + document.getElementsByName('checkTo')[i].title; 
					}
			}
    		if(id!=''){
	     		 var  num =id.split(',');
				  if(num.length>1){
					  alert("勾选的太多,无法编辑,请选择一个在继续编辑!");
					  return false;
				  }
				  if(num.length==1){
					  $.ajax({
						  type:'post',
						  url:'<%=basePath%>/ErrorWarnSet/toUpdateUserEmail.do',
						  data:{"id":id},
						  success:function(data){
							  var result = JSON.parse(data);
			    				var msg=result.message;
			    				var list=result.responseData.userlist;
			    				var maillist=result.responseData.userEmail;
			    				if(msg=="更新成功"){
			    					var str="";
			    					str+="<ul class='ul_style'>"
			    					+"<li><span>姓名</span> <select id='user_id'>";
			                       		$.each(list,function(i,list){
			                       			str+="<option value="+list.id+" ";
			                       			if(list.id==maillist.user_id){
			                       				str+="selected";
			                       			}
			                       			str+=">"+list.name+"</option>";
			    						});
			    					str+="</select></li><li><span>邮箱</span><input type='text' id='emailaddr' value="+maillist.emailaddr+"></input></li></ul><a class='a_button' style='cursor: pointer;' onclick=document.getElementById('iframe').contentWindow.toChildEdit('"+maillist.id+"');>保存</a>";
			    					alertwindow("编辑",str);
			    				}else{
			    					alert(msg);
			    				}
							}});
					}
    		}else{
    			alert("请选择一个在继续编辑!");
    		}
    	}
    //修改之后
    function edit(id){
    	var wtd = window.top.document;
    	var user_id=$("#user_id",wtd).val();
    	var emailaddr=$("#emailaddr",wtd).val();
    	if(emailaddr==""){
    		alert("请输入邮箱地址");
    		return false;
    	}
    	$("#loading").show(); 
    	$.ajax({
    		type:'post',
    		url:'<%=basePath%>/ErrorWarnSet/updateUserEmail.do',
    		data:{"id":id,"user_id":user_id,"emailaddr":emailaddr},
    		success:function(data){
    			var result = JSON.parse(data);
				var msg=result.message;
				if(msg=="更新成功"){
					$("#gray",wtd).hide();
					$("#alertwindow",wtd).hide();
					$("#loading").hide(); 
		    		window.location.reload();
				}else{
					$("#loading").hide(); 
					alert(msg);
				}
    		}
    	});
    	
    }
    //删除
    function deletemail(){
    	var id='';  //菜单id
 		for(var i=0;i < document.getElementsByName('checkTo').length;i++){
			  if(document.getElementsByName('checkTo')[i].checked){    //选中
				  if(id=='') id +=document.getElementsByName('checkTo')[i].title;
				  	else id += ',' + document.getElementsByName('checkTo')[i].title; 
				}
		}
		if(id!=''){
     		 var  num =id.split(',');
			  if(num.length>1){
				  alert("勾选的太多,无法删除,请选择一个在继续删除!");
				  return false;
			  }
			  if(num.length==1){
				  if(confirm("您确定删除吗?")){
					  $("#loading").show(); 
					  $.ajax({
					  type:'post',
					  url:'<%=basePath%>/ErrorWarnSet/deleteUserEmail.do',
					  data:{"id":id},
					  success:function(data){
						  var result = JSON.parse(data);
		    				var msg=result.message;
		    				if(msg=="删除成功"){
		    					$("#loading").hide(); 
		    					window.location.reload();
		    				}else{
		    					$("#loading").hide(); 
		    					alert(msg);
		    				}
						}});
				  }
				}
		}else{
			alert("请选择一个在继续删除!");
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
	<div id="loading" style="margin: 300px 600px;"></div>
	<!-- 头部内容home -->

	<!-- 头部内容end -->
	<div class="main_box clearfix">
		<!-- 左侧菜单 -->

		<!-- 左侧菜单end -->
		<!-- 右侧内容 -->
		<div class="right_main_div0">
			<div class="W_width">
				<!-- 编辑标签 -->
				<p class="P_Label clearfix">
					<a class="a_size01" style="cursor: pointer" onclick="toSave()"><span>新增</span></a>
					<a class="a_size02" style="cursor: pointer" onclick="toEdit()"><span>编辑</span></a>
					<a class="a_size03" style="cursor: pointer" onclick="deletemail()"><span>删除</span></a>
				</p>
				<!-- 编辑标签end -->

				<!-- table内容 -->
				<div class="table_box" id="autoarea">
					<table id="table" id="table">

					</table>
					<!-- 新增/编辑弹窗 -->
					<div id="edit"></div>

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