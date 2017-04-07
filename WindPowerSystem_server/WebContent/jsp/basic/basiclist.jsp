<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
      <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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
    <link rel="stylesheet" type="text/css" href="<%=basePath%>/css/common.css">
    <link rel="stylesheet" type="text/css" href="<%=basePath%>/css/style.css">
<!-- 共同引用jquery js -->
    <script type="text/javascript" src="<%=basePath%>js/jquery-1.8.3.min.js"></script>
 	<script type="text/javascript" src="<%=basePath%>/js/js.js"></script>
 	<script type="text/javascript" src="<%=basePath%>js/opts.js"></script>
	<script type="text/javascript" src="<%=basePath%>js/spin.js"></script>
 <!-- 共同引用jquery js -->
 <title>基本参数</title>
 <script type="text/javascript">
window.onload=function(){
	 //设置首次进入刷新
	  var urlstr=window.location.href;
	  var str=null;
	  jq(str);  
	}

/**
 * 定时刷新
 */
$(function(){ 
	 //设置定时刷新函数
	 setInterval(frdata,2000); 
});
 
/**
 * 切换标签
 */
function jq(str){
	// debugger;
	 $("#table").hide();
	 $("#loading").show();
	 //设置提交类型的函数
	 if(str==0||str==null){
		 $("#l1").eq($(this).index()).addClass("li_style").siblings().removeClass("li_style");
		 $("#table tr").remove();
	 }else if(str==1){
		 $("#l2").eq($(this).index()).addClass("li_style").siblings().removeClass("li_style");
		 $("#table tr").remove();
	 }else{
		 $("#l3").eq($(this).index()).addClass("li_style").siblings().removeClass("li_style");
		 $("#table tr").remove();
	 }
	 if(str==null){
		 modbus_type=0; 
	 }else{
		 modbus_type=str;
	 }
	 $("#inid").attr("value",modbus_type);
	 if(ajaxGet) {ajaxGet.abort();}
	 frdata();	 
//	 $("#inid").value=modbus_type;
}
 var ajaxGet;

function  frdata(){
	 var st = [1,3,5,7,8,9,10,11,12,13];
	 var random = st[Math.floor(Math.random()*st.length)];
	 var result = st[random];
	 var modbus_type=$("#inid").val();
	 var rid=$("#rid").val();
	 var loadValue=$("#loadValue").val();
	 if(loadValue=="0"){
		 	$("#table").hide();
			$("#loading").show(); 
			$("#loadValue").val(1);
		}
	 
	 ajaxGet=$.ajax({
		   type:"post",
		   data:{"modbus_type":modbus_type},
		   //获取设备实时基本参数数据
		   url:"<%=basePath%>Parameter/Basiclist.do",
		   success:function(data){
		       $("#table").show();
			   $("#loading").hide();
			   var result = JSON.parse(data);
			   if("1001" == result.code){
				  /*  debugger; */
				   $("#table tr").remove();
					var trlist=result.responseData.trlist;
					var headstr=result.responseData.headstr;
					//获取列数
					var xcol=headstr.split("?").length;
					 $("#rid").attr("value",xcol);
					//获取有多少行
					var ycol=trlist.length;
					
					if (trlist.length > 0) {
						var tbodyObj = document.getElementById("table");
						//var =$("#errortable");
						 var rows= tbodyObj.rows;
						// var rows=tbodyObjdocum.rows;
						var tableleth=tbodyObj.rows.length-1;
						//更新的数据没有更加和减少tr和查询的列相等
						if(tableleth==trlist.length){
							 for(var i=0;i<trlist.length;i++){
								 var r = tbodyObj.insertRow(); 
								 //添加列数
								 if(rid!=xcol){
									 for(var j=0;j<xcol;j++){
									    	//判断需要多少列
									    	  var c = r.insertCell(); 
									    	//对添加的列插入为空
										       c.innerHTML="";	
									    }
								 }else{
								    for(var j=0;j<rid;j++){
								    	//判断需要多少列
								    	  var c = r.insertCell(); 
								    	//对添加的列插入为空
									       c.innerHTML="";	
								   	  }
								 }
							}
							
					   } else if(tableleth>trlist.length){
						   for(var i=trlist.length;i<tableleth;i++){
							   tbodyObj.deleteRow(rows[i]);
						   }
					   }else{
						 for(var i=tableleth;i<trlist.length;i++){
							 var r = tbodyObj.insertRow(); 
							 //添加列数
							 if(rid!=xcol){
								 for(var j=0;j<xcol;j++){
								    	//判断需要多少列
								    	  var c = r.insertCell(); 
								    	//对添加的列插入为空
									       c.innerHTML="";	
								    }
							 }else{
							    for(var j=0;j<rid;j++){
							    	//判断需要多少列
							    	  var c = r.insertCell(); 
							    	//对添加的列插入为空
								       c.innerHTML="";	
							    }
							    }
						    }
					    }
						for (var i = 0; i < trlist.length+1; i++) {
							if(i==0){
								//debugger;
								var headstr=headstr.split("?");
								for(var j=0;j<xcol;j++){
									//输出头的内容
									rows[0].cells[j].innerHTML= headstr[j];
								}
							}else{
								
								var error = trlist[i-1];
								if(error.indexOf("#")>=0){
									
									var errormore = error.split("#");
									for(var j=0;j<xcol;j++){
										//输出列的内容
										rows[i].cells[j].innerHTML=errormore[j];
										//rows[i].cells[j].innerHTML=random ;
									}
								}else{
										rows[i].cells[0].innerHTML= error;
								}
								
							}
							
						}
					   }
					  //frdata(); 
					  autoHeight();
					} else{
				   alert(msg);  
			   }
			   $('#table tbody tr:even').css("backgroundColor", "#edf2f6");
		   }
	});
 }

/**
 * 自适应高度
 */
 function autoHeight(){
 	var autoheight = $(window).height()-228;
    $("#autoarea").attr("style","min-height:"+autoheight+"px;max-height:"+autoheight+"px");
}
</script>
</head>
<body>
<input type="hidden" value="${param.device_id}" id="inid">
<input type="hidden" id="rid">
<input type="hidden" id="loadValue" value="0" />

    <div class="main_box clearfix">
        <!-- 右侧内容 -->
        <div class="W_90" style="padding:10px 0;">
            <div class="W_width" style="width: 100%;">

                <!-- 头部切换标签 -->
                <div class="main_title_box ">

                    <ul class="text_box" id="uid">
                        <li id="l1"><a onclick="jq('<%=0%>')" style="cursor:pointer;">双馈变流器</a></li>
                        <li id="l2"><a onclick="jq('<%=1%>')" style="cursor:pointer;">全功率变流器</a></li>
                        <li id="l3"><a onclick="jq('<%=2%>')" style="cursor:pointer;">海上风电变流器</a></li>
                    </ul>
                </div>
                <!-- 头部切换标签end -->

                <!-- 编辑标签 -->
                <p class="P_Label clearfix">

                </p>
                <!-- 编辑标签end -->

                <!-- table内容 -->
                <div class="table_box" id="autoarea" >
                <div id="loading" style="margin:150px 600px;"></div>
                    <table class="table_W" id="table" >
                       <thead >
                            <tr id="tid">
                                
                            </tr>
                        </thead>
                         <tbody >
                            <tr>
                                
                            </tr>
                        </tbody>
                    </table>
                </div>
                <!-- table内容end -->
            </div>
        </div>
        <!-- 右侧内容end -->
    </div>
</body>
</html>