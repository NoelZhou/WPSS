<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
  	<title>系统设置主页</title>
    <link rel="stylesheet" type="text/css" href="../css/common.css">
    <link rel="stylesheet" type="text/css" href="../css/style.css">
    <script type="text/javascript" src="../js/jquery.js"></script>
    <script type="text/javascript" src="../js/jquery-1.8.3.min.js"></script>
     <script type="text/javascript">
    	$(function(){
			onclickUrl();
			$('li').live('click',function(){
				$(this).addClass('li_style01').siblings('li').removeClass('li_style01');
			}); 
			$("#iframe").attr("style","width:99%;height:"+$(window).height()+"px");
		});    
    	function onclickUrl(objUrl){
    		var url="";
    		if(objUrl==null){
    			 url="../jsp/systemjsp/user.jsp";
    		}
    		if(objUrl==1){
      			 url="../jsp/systemjsp/user.jsp";
      			}
    		if(objUrl==2){
   			 url="../jsp/systemjsp/role.jsp";
   			}
    		if(objUrl==3){
      			 url="../jsp/systemjsp/menu.jsp";
      		}
    		$("#iframe").attr("src",url);
    	}
    	
    	function toChildSave(){
    		document.getElementById('iframe').contentWindow.save();
    	}
    	function toChildEdit(id){
    		document.getElementById('iframe').contentWindow.edit(id);
    	}
    </script>
    
</head>
<body>
	<!-- 左侧菜单 -->
        <div class="left_list_ul">
            <ul>
                <li class="li_style01"><a style="cursor: pointer" onclick="onclickUrl(1)">用户管理</a></li>
                <li><a style="cursor: pointer" onclick="onclickUrl(2)">角色权限</a></li>
                <li><a style="cursor: pointer" onclick="onclickUrl(3)">菜单管理</a></li>
                <!-- <li><a style="cursor: pointer" onclick="onclickUrl(4)">数据字典管理</a></li> -->
            </ul>
        </div>
    <!-- 左侧菜单end -->
    <!-- 系统设置右侧内容 start-->
		<div>
		 <iframe id="iframe" scrolling="no" frameborder="0"  src=""></iframe>
		  </div>
	<!-- 系统设置右侧内容 stop-->     
</body>
</html>