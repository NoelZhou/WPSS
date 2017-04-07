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
    <title>服务设置-采集频率设置</title>
    <link rel="stylesheet" type="text/css" href="<%=basePath%>/css/common.css">
    <link rel="stylesheet" type="text/css" href="<%=basePath%>/css/style.css">
     <script type="text/javascript" src="<%=basePath%>js/jquery-1.8.3.min.js"></script>
    <script type="text/javascript" src="<%=basePath%>js/opts.js"></script>
	<script type="text/javascript" src="<%=basePath%>js/spin.js"></script>
    <script type="text/javascript">
    	$(function (){
    		$("#loading").show();
    		$.ajax({
    			type:'post',
    			url:'<%=basePath%>/CollectSet/getCollectTimes.do',
    			success:function(data){
    				$("#loading").hide();
    				var result = JSON.parse(data);
    				var msg=result.message;
    				if(msg=="获取成功"){
    					var timeAll=result.responseData.collectTimes;
						str="";
						str+="<div class='right_main_div'><div class='W_width'><div class='height_H'><div class='W_w'><ul><li><span>实时数据采集频率</span><select onmouseout='loseFocus(1)' id='real_time' class='select01'>";
						for (var i = 1; i <=60; i++) {
								str+="<option value="+i+" ";
									if(timeAll.real_time==i){
										str+="selected";
									}
								str+=">"+i+'秒'+"</option>";	
						}
						str+="</select></li>"
						+"<li><span>历史数据保存时间</span><select onmouseout='loseFocus(2)' id='history' class='select01'>";
						for (var j = 1; j <=60; j++) {
							str+="<option value="+j+" ";
								if(timeAll.history==j){
									str+="selected";
								}
							str+=">"+j+'天'+"</option>";
						}
						str+="</select></li></ul><a class='A_button' style='cursor: pointer' onclick='save()'>保存</a></div></div></div></div>";
						$("#right_main_div").html(str);
    				}else{
    					alert(msg);
    				}
    			}
    		});
    	});
    	
    	function save(){
    		var real_time=$("#real_time").val();
    		var history=$("#history").val();
    		$.ajax({
    			type:'post',
    			url:'<%=basePath%>/CollectSet/updateCollectTimes.do',
    			data:{"real_time":real_time,"history":history},
    			success:function(data){
    				var result = JSON.parse(data);
    				var msg=result.message;
    				if(msg=="更新成功"){
    					 window.location.reload();
    				}else{
    					alert(msg);
    				}
    				
    			}
    		});
    	}
    	//下拉菜单鼠标移开失去焦点
    	function loseFocus(idName){
    		if(idName==1){
    			document.getElementById("real_time").blur();
    		}else if(idName==2){
    			document.getElementById("history").blur();
    		}
        	
        }
    </script>
</head>

<body>
    <!-- 头部内容home -->
    <div id="loading" style="margin:250px 660px;"></div>
    <!-- 头部内容end -->
    <div class="main_box clearfix">
        <!-- 左侧菜单 -->
      
        <!-- 左侧菜单end -->
        <!-- 右侧内容 -->
        <div id="right_main_div"></div>
       
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
    
</body>

</html>