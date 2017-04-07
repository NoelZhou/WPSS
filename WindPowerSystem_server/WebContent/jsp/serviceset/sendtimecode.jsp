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
<title>服务设置-对时设置</title>
<link rel="stylesheet" type="text/css"
	href="<%=basePath%>/css/common.css">
<link rel="stylesheet" type="text/css"
	href="<%=basePath%>/css/style.css">
<script type="text/javascript" src="<%=basePath%>js/jquery-1.8.3.min.js"></script>
<script type="text/javascript" src="<%=basePath%>js/opts.js"></script>
<script type="text/javascript" src="<%=basePath%>js/spin.js"></script>
<script type="text/javascript">
   $(function(){
	   $("#loading").show();
	   var t = null;
	    t = setTimeout(time,1000);//开始执行
	    function time()
	    {
	       clearTimeout(t);//清除定时器
	       dt = new Date();
	       
	       var y=dt.getFullYear().toString();
	       var mo=dt.getMonth()+1;
	       var d=dt.getDate();
	       var h=dt.getHours();
	       var m=dt.getMinutes();
	       var s=dt.getSeconds();
	       var timeShow="现在的时间为："+h+"时"+m+"分"+s+"秒";
	       y=y.substring(2,5);
	       var allTimeShow=y+","+mo+","+d+","+h+","+m+","+s;
	       $("#timeList").val(allTimeShow);
	       $("#loading").hide();
	       $("#right_main_div").show();
	       $("#timeShow").val(timeShow);
	       t = setTimeout(time,1000); //设定定时器，循环执行            
	    } 
	});
   
    	
    	function save(){
    		alertwindow();
    		var timeList=$("#timeList").val();
    		$.ajax({
    			type:'post',
    			url:'<%=basePath%>/DeviceTest/sendtimecode.do',
    			data:{"timeList":timeList},
    			success:function(data){
    				var result = JSON.parse(data);
    				var msg=result.message;
    				if(msg=="兑时成功"){
    					closewindow(); 
    					alert("对时成功！");
    				}else{
    					closewindow();
    					alert(msg);
    				}
    				
    			}
    		});
    	}
    	
   	 function alertwindow(){
			var wtd = window.top.document;
		   	$("#grayT",wtd).show();
			$("#loading",wtd).show();
   	}
   	 function closewindow(){
   		var wtd = window.top.document;
   		$("#grayT",wtd).hide();
   		$("#loading",wtd).hide();
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

		<div id="right_main_div" style="display: none">
			<div class="W_width">
				<div class="height_H">
					<div class="W_w">
						<ul>
							<li class="clearfix"><span>定时对时设置</span> <input type="text"
								id="timeShow" class="select02" readonly /></li>
						</ul>
						<input type="hidden" id="timeList"> <a class="A_button"
							style="cursor: pointer;" onclick="save()">对时</a>

					</div>
				</div>

			</div>


		</div>
		<div id="loading" style="margin: 250px 600px;"></div>
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