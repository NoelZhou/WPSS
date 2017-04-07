<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" type="text/css" href="<%=basePath%>/css/common.css">
<link rel="stylesheet" type="text/css" href="<%=basePath%>/css/style.css">
<link href="<%=basePath%>/favicon.ico" rel="icon" type="image/x-icon" />
<link href="<%=basePath%>/favicon.ico" rel="shortcut icon" type="image/x-icon" />
<!-- 共同引用jquery js -->
<script type="text/javascript" src="<%=basePath%>js/jquery-1.8.3.min.js"></script>
<script type="text/javascript" src="<%=basePath%>/js/js.js"></script>
<script type="text/javascript" src="<%=basePath%>js/popup.js"></script>
<script type="text/javascript" src="<%=basePath%>js/opts.js"></script>
<script type="text/javascript" src="<%=basePath%>js/spin.js"></script>
 <!-- 共同引用jquery js -->
 <!-- 共同引用jquery js -->
<title>参数监控</title>
</head>
<script type="text/javascript">
window.onload=function(){
	//首次进入页面的刷新函数
	 var urlstr=window.location.href;
	/* alert(urlstr); */
	 var device=urlstr.split("?")[1].split("&")[0].split("=")[1]
	 var modbus_type=urlstr.split("?")[1].split("&")[1].split("=")[1]
	 $("#inid").attr("value",device);
	 $("#modbusid").attr("value",modbus_type);
	  var id=null
	  jq(id,device);
	  jqid(id,device);
	  $("#tableid").hide();
	 
	}
	
$(function(){ 
	 //设置定时刷新函数
 	iCount=setInterval(jqid,1000); 
	
});

 function jq(id,device){
	var device=$("#inid").val(); 
		//获取设备故障数据
		var ids;
		var id;
		if(id==null||id==""){
			ids=1;
		}else{
			ids=id;
		}
	  var modbusid=$("#modbusid").val();
		 $.ajax({
			   type:"post",
			   data:{"parent_type":ids,"device_id":device},
			   url:"<%=basePath%>Parameter/parameterByType.do",
			   success:function(data){
				   var result = JSON.parse(data);
					 if("1001" == result.code){
						var parametelist = result.responseData.parametelist;
						if(ids==1){
							if($("#net").children("").length==1){
								$(".cid").parent().remove();
								for(var i=0;i<parametelist.length;i++){
									var type_id;
									if((parametelist[i].array_type_list).indexOf(",")>=0){
										type_id=(parametelist[i].array_type_list).replace(",","");
									}else{
										type_id=parametelist[i].array_type_list;
									}
									
									var init_id=$("#id").val();
									if(init_id==null||init_id==""){
										$("#id").attr("value",(parametelist[0].array_type_list).replace(",",""));
									}
									
								    $("#net").append("<a onclick=checktype('"+parametelist[i].array_type_list+"')><dd class='cid' id='"+type_id+"' style='cursor:pointer;'>"+parametelist[i].name+"</dd></a>"); 
							}								
					    }
						}else if(ids==2){
							if($("#enjine").children("").length==1){//获取下面的有没有dd
								$(".cid").parent().remove();
								for(var i=0;i<parametelist.length;i++){
									var type_id;
									if((parametelist[i].array_type_list).indexOf(",")>=0){
										type_id=(parametelist[i].array_type_list).replace(",","");
									}else{
										type_id=parametelist[i].array_type_list;
									}
									var init_id=$("#id").val();
									if(init_id==null||init_id==""){
										$("#id").attr("value",(parametelist[0].array_type_list).replace(",",""));
									}
									$("#enjine").append("<a onclick=checktype('"+parametelist[i].array_type_list+"')><dd class='cid' id='"+type_id+"' style='cursor:pointer;' value="+i+">"+parametelist[i].name+"</dd></a>");
								}
							}	
						}else if(ids==3){
							if($("#outer").children("").length==1){
								$(".cid").parent().remove();
								for(var i=0;i<parametelist.length;i++){
									var type_id;
									if((parametelist[i].array_type_list).indexOf(",")>=0){
										type_id=(parametelist[i].array_type_list).replace(",","");
									}else{
										type_id=parametelist[i].array_type_list;
									}
									$("#outer").append("<a onclick=checktype('"+parametelist[i].array_type_list+"')><dd class='cid' id='"+type_id+"' style='cursor:pointer;'>"+parametelist[i].name+"</dd></a>");
								}
								//添加版权信息
								 if(modbusid!=2){
									 $("#outer").append("<a onclick=checktype('bq')><dd class='cid' id='bq'style='cursor:pointer;'>版本信息</dd></a>"); 
								 }		
							}						
							
							}else{
								alert("参数获取异常");
							}
						
					 }else{
						 alert(msg);
					 }
			   }
		 });
		
	}
	
	
	function jqbqid(id){
		//设置查询右侧版本详细数据
		
		var device=$("#inid").val();
		$("#id").attr("value",id);
		var id=$("#id").val();
		$.ajax({
			   type:"post",
			   data:{"device_id":device},
			   url:"<%=basePath%>DeviceTest/getbqxx.do",
			   success:function(data){
				  // debugger;
				   $(".tid").remove();
				   var result = JSON.parse(data);
				    if("1001" == result.code){
					   var bbstr = result.responseData.bbstr;
					   var  bb= bbstr.split(",");
					   $('table tr').hide(); 
					   $("#table1").html("");
					   if(bb.length>3){
							/* 网侧DSP软件版本:DSP_WGxxxxKDF_Vxx_GRID_Vx.x			地址:94
							机侧DSP软件版本:DSP_WGxxxxKDF_Vxx_ROT_Vx.x			地址:42
							通讯ARM软件版本: ARM_WGxxxxKDF_Vxx _Vx.x			地址:416,417,410
							网侧DSP参数版本: Vx.x								地址:95
							机侧DSP参数版本: Vx.x								地址:54
							通讯ARM参数版本: Vx.x								地址:468*/            
							$("#table1").append("<tr class='tid'><td>网侧DSP软件版本</td><td>"+bb[0]+"</td></tr>");
							$("#table1").append("<tr class='tid'><td>机侧DSP软件版本</td><td>"+bb[1]+"</td></tr>");
							$("#table1").append("<tr class='tid'><td>通讯ARM软件版本</td><td>"+bb[2]+"</td></tr>");
							$("#table1").append("<tr class='tid'><td>网侧DSP参数版本</td><td>"+bb[3]+"</td></tr>");
							$("#table1").append("<tr class='tid'><td>机侧DSP参数版本</td><td>"+bb[4]+"</td></tr>");
							$("#table1").append("<tr class='tid'><td>通讯ARM参数版本</td><td>"+bb[5]+"</td></tr>");
					   }else{
						 /*   网侧DSP软件版本:DSP_WGxxxxKDF_Vxx_GRID_Vx.x			地址:97
							机侧DSP软件版本:DSP_WGxxxxKDF_Vxx_ROT_Vx.x			地址:264
							通讯ARM软件版本: ARM_WGxxxxKDF_Vxx _Vx.x			地址:50 */
						   $("#table1").append("<tr class='tid'><td>网侧DSP软件版本</td><td>"+bb[0]+"</td></tr>");
							$("#table1").append("<tr class='tid'><td>机侧DSP软件版本</td><td>"+bb[1]+"</td></tr>");
							$("#table1").append("<tr class='tid'><td>通讯ARM软件版本</td><td>"+bb[2]+"</td></tr>");
					   }
				   }else{
					   alert("获取数据失败");  
				   }
				   $("#table1").show();
				   $("#loading").hide();
				   $('#table1 tbody tr:even').css("backgroundColor", "#edf2f6"); 
			   }
		});
	}
	
	
	
	function checktype(id){
			//左侧点击事件
			//clearInterval(iCount);
			ajaxGet.abort();
			var modbusid=$("#modbusid").val();
			$("#table1").hide(); 
			$("#oneid").hide();
			$("#table").hide(); 
			$("#tableid").hide();
			$("#loading").show();
			$("#id").attr("value",id);
	  	 var type_id=$("#typeid").val();
	  	var type_ids;
	  	 if(id.indexOf(",")>=0){//设置拆分海上风电的id
	  		type_ids=id.replace(",","");
	  	}else{
	  		type_ids=id;
	  	} 
		$("#checkid").attr("value","");
		$("#typeid").attr("value",type_ids);
		$("#"+type_id).removeClass("thisclass");
		$("#"+type_ids).addClass("thisclass");
		jqid(id);
		
	}
	var ajaxGet;
	function jqid(id){	
		//获取右侧详细数据，并左侧点击样式
		var id=$("#id").val();
		var loadValue=$("#loadValue").val();
		if(loadValue=="0"){
		 	$("#oneid").hide();
			$("#loading").show(); 
			$("#loadValue").val(1);
		}
		
		var device=$("#inid").val(); 
		var modbusid=$("#modbusid").val();
		var ids=null;
		var type_ids;
		if(id==null||id==""){
			if(modbusid=="2"){
				ids="105,107";
				type_ids=ids.replace(",","");
				$("#typeid").attr("value",type_ids);
				$("#id").attr("value",ids);
				}else{					
				ids=1;
				type_ids=ids;
				$("#typeid").attr("value",type_ids);
				}
		}else{
			ids=id;
			type_ids=ids;
		} 
		var type_id=$("#typeid").val();	//获取保存的id//设置样式
		$("#"+type_id).addClass("thisclass");
		if(id=="bq"){
			$("#table").hide(); 
			$("#oneid").hide();
			$("#typeid").attr("value","bq");
			//当点击为版本的时候，只执行jqbqid(id)方法
			jqbqid(id)
			return;
		}
		ajaxGet=$.ajax({
			   type:"post",
			   data:{"id":ids,"device_id":device},
			   url:"<%=basePath%>Parameter/parameterchildid.do",
			   success:function(data){
				   var result = JSON.parse(data);
				   if("1001" == result.code){
				   	   $(".tid").remove();
						var parametelist = result.responseData.parametelist;
						var codeLength = result.responseData.codeLength;
						$("#codeSize").val(codeLength);
						//设置
						if(codeLength>=1){
							$("#setup").show();
						}else{
							$("#setup").hide();
						}
						//查询
						if(codeLength==2){
							$("#codeLength").show();
						}else{
							$("#codeLength").hide();
						}
						var code=parametelist[0].rolecode;
					    if(ids=="128"){
							   document.getElementById("xb_id").style.display="block";
							   document.getElementById("Adderror_id").style.display="none";
							   document.getElementById("Write_id").style.display="none";
							   document.getElementById("Write_id_sz").style.display="block";
							   document.getElementById("Write_id_fw").style.display="none";
							   document.getElementById("Write_id_cx").style.display="block";
								 document.getElementById("snxt_id").style.display="none";
									document.getElementById("gzjcb_id_jc").style.display="none";
								  	document.getElementById("gzjcb_id_wc").style.display="none";
									document.getElementById("gzjcb_id_qb").style.display="none"
										document.getElementById("Write_id_sz_mr").style.display="none";


						}else if(ids=="3"){
							  document.getElementById("Adderror_id").style.display="block";
							  document.getElementById("xb_id").style.display="none";
							  document.getElementById("Write_id").style.display="none";	
							  document.getElementById("Write_id_sz").style.display="block";
							  document.getElementById("Write_id_fw").style.display="none";
							  document.getElementById("Write_id_cx").style.display="block";
								 document.getElementById("snxt_id").style.display="none";
								 document.getElementById("gzjcb_id_jc").style.display="none";
								  	document.getElementById("gzjcb_id_wc").style.display="none";
									document.getElementById("gzjcb_id_qb").style.display="none"
									
										document.getElementById("Write_id_sz_mr").style.display="none";
							  
						  }else if(ids=="118,119"||ids=="111,112"){
							  document.getElementById("Adderror_id").style.display="none";
						 	    document.getElementById("xb_id").style.display="none";
						   		document.getElementById("Write_id").style.display="block";
						   		document.getElementById("Write_id_sz").style.display="none";
						    	document.getElementById("Write_id_fw").style.display="block";
						    	 document.getElementById("Write_id_cx").style.display="block";
						    	 document.getElementById("snxt_id").style.display="none";
						    	 document.getElementById("gzjcb_id_jc").style.display="none";
								  	document.getElementById("gzjcb_id_wc").style.display="none";
									document.getElementById("gzjcb_id_qb").style.display="none"
										document.getElementById("Write_id_sz_mr").style.display="none";

						   		
						  }else if(ids=="127"||ids=="50"){
							  document.getElementById("Adderror_id").style.display="none";
						 	    document.getElementById("xb_id").style.display="none";
						   		document.getElementById("Write_id").style.display="block";
						   		document.getElementById("Write_id_sz").style.display="block";
						    	 document.getElementById("Write_id_fw").style.display="block";
						    	 document.getElementById("Write_id_cx").style.display="none";
						    	 document.getElementById("snxt_id").style.display="none";
						    	 document.getElementById("gzjcb_id_jc").style.display="none";
								  	document.getElementById("gzjcb_id_wc").style.display="none";
									document.getElementById("gzjcb_id_qb").style.display="none"
										document.getElementById("Write_id_sz_mr").style.display="none";

						    	 
						  }else if(ids=="123,124"){
							  //水能系统
							    document.getElementById("Adderror_id").style.display="none";
						 	    document.getElementById("xb_id").style.display="none";
						   		document.getElementById("Write_id").style.display="none";
						   		document.getElementById("snxt_id").style.display="block";
						   		document.getElementById("gzjcb_id_jc").style.display="none";
							  	document.getElementById("gzjcb_id_wc").style.display="none";
								document.getElementById("gzjcb_id_qb").style.display="none"

						   		
							  
						    }else if(ids=="99,101,103"){
						    	//故障检测板数据网侧
						    	 document.getElementById("Adderror_id").style.display="none";
							 	   document.getElementById("xb_id").style.display="none";
							   	document.getElementById("Write_id").style.display="none";
							   	document.getElementById("snxt_id").style.display="none";
							  	document.getElementById("gzjcb_id_jc").style.display="none";
							  	document.getElementById("gzjcb_id_wc").style.display="block";
								document.getElementById("gzjcb_id_qb").style.display="none"
									document.getElementById("Write_id_sz_mr").style.display="none";

							   	
						    }else if(ids=="100,102,104"){
						    	//故障检测板数据网侧
						    	document.getElementById("Adderror_id").style.display="none";
							 	document.getElementById("xb_id").style.display="none";
							   	document.getElementById("Write_id").style.display="none";
							   	document.getElementById("snxt_id").style.display="none";
							  	document.getElementById("gzjcb_id_jc").style.display="block";
							  	document.getElementById("gzjcb_id_wc").style.display="none";
								document.getElementById("gzjcb_id_qb").style.display="none"
									document.getElementById("Write_id_sz_mr").style.display="none";

							   	
						    }else if(ids=="49"||ids=="48"){
						    	 document.getElementById("Write_id_cx").style.display="none";
						    	 document.getElementById("Adderror_id").style.display="none";
							 	    document.getElementById("xb_id").style.display="none";
							   		document.getElementById("Write_id").style.display="block";
							   		document.getElementById("Write_id_sz").style.display="block";
									document.getElementById("Write_id_fw").style.display="none";
									document.getElementById("Write_id_cx").style.display="none";
									document.getElementById("snxt_id").style.display="none";
									document.getElementById("gzjcb_id_jc").style.display="none";
									document.getElementById("gzjcb_id_wc").style.display="none";
									document.getElementById("gzjcb_id_qb").style.display="none"
									document.getElementById("Write_id_sz_mr").style.display="none";
						    }else if(ids=="131"){
						    	
						    	document.getElementById("Write_id_cx").style.display="none";
						    	 document.getElementById("Adderror_id").style.display="none";
							 	    document.getElementById("xb_id").style.display="none";
							   		document.getElementById("Write_id").style.display="none";
							   		document.getElementById("Write_id_sz").style.display="none";
									document.getElementById("Write_id_fw").style.display="none";
									document.getElementById("Write_id_cx").style.display="none";
									document.getElementById("snxt_id").style.display="none";
									document.getElementById("gzjcb_id_jc").style.display="none";
									document.getElementById("gzjcb_id_wc").style.display="none";
									document.getElementById("gzjcb_id_qb").style.display="block";
									  document.getElementById("Write_id_sz_mr").style.display="none";
						    }
						    else if(ids=="14"||ids=="8"){
						    	    document.getElementById("Write_id_sz_mr").style.display="block";
						    	    document.getElementById("Adderror_id").style.display="none";
							 	    document.getElementById("xb_id").style.display="none";
							   		document.getElementById("Write_id").style.display="block";
							   		document.getElementById("Write_id_sz").style.display="block";
									document.getElementById("Write_id_fw").style.display="none";
									document.getElementById("Write_id_cx").style.display="none";
									document.getElementById("snxt_id").style.display="none";
									document.getElementById("gzjcb_id_jc").style.display="none";
									document.getElementById("gzjcb_id_wc").style.display="none";
									document.getElementById("gzjcb_id_qb").style.display="none"
						    }else{
							 if(code=="W"||ids=="4,13"){
						  	    document.getElementById("Adderror_id").style.display="none";
						 	    document.getElementById("xb_id").style.display="none";
						   		document.getElementById("Write_id").style.display="block";
						   		document.getElementById("Write_id_sz").style.display="block";
								document.getElementById("Write_id_fw").style.display="none";
								document.getElementById("Write_id_cx").style.display="block";
								document.getElementById("snxt_id").style.display="none";
								document.getElementById("gzjcb_id_jc").style.display="none";
								document.getElementById("gzjcb_id_wc").style.display="none";
								document.getElementById("gzjcb_id_qb").style.display="none"
								document.getElementById("Write_id_sz_mr").style.display="none";


								}else{ 
									 	 document.getElementById("Adderror_id").style.display="none";
										document.getElementById("xb_id").style.display="none";
									 	document.getElementById("Write_id").style.display="none";
									 	document.getElementById("Write_id_sz").style.display="none";
										  document.getElementById("Write_id_fw").style.display="none";
											 document.getElementById("snxt_id").style.display="none";
											 document.getElementById("gzjcb_id_jc").style.display="none";
											  	document.getElementById("gzjcb_id_wc").style.display="none";
												document.getElementById("gzjcb_id_qb").style.display="none"
													document.getElementById("Write_id_sz_mr").style.display="none";


								   }
						  }
					   var checkid=$("#checkid").val();
					    if(checkid==""){
						   checkid="text";
					   } 	
					   $("#oneid").show();
						
						   var j=1;
					   $("#tableid").show();
						   if(ids=="3"){
							   $(".tid").remove();
					    	//当点击变流器历史记录所需要执行的事件,并且对年月日，时分秒进行拼接
						  var addr=parametelist[0].addr+"－"+parametelist[2].addr;
						  var name="年-月-日"//拼接前三个地址
						  var hvalue=parametelist[0].hvalue+"-"+parametelist[1].hvalue+"-"+parametelist[2].hvalue;
						  var category=parametelist[0].category;
						  var unit=parametelist[0].unit;
						  var remark=parametelist[0].remark;
						  var name1="时:分:秒";//拼接三到六的地址
						  var addr1=parametelist[3].addr+"－"+parametelist[5].addr;
						  var hvalue1=parametelist[3].hvalue+":"+parametelist[4].hvalue+":"+parametelist[5].hvalue;
						  $("#table").append("<tr id='ttid' class='tid' ondblclick=showbitvalue('"+parametelist[0].bitString+"')><td><label class='label_input01'><input type='checkbox'  onclick='updateUserDialog(this)'  name='test' ></input><span></span></label></td><td class='overflow' >"+1+"</td><td class='overflow' title="+addr+">"+addr+"</td ><td class='overflow' title="+name+">"+name+"</td><td class='overflow' title="+hvalue+">"+hvalue+"</td><td class='overflow'>"+unit+"</td><td class='overflow' >"+category+"</td><td class='overflow'>"+remark+"</td></tr>");
						  $("#table").append("<tr id='ttid' class='tid' ondblclick=showbitvalue('"+parametelist[0].bitString+"')><td><label class='label_input01'><input type='checkbox'  onclick='updateUserDialog(this)'  name='test' ></input><span></span></label></td><td class='overflow' >"+2+"</td><td class='overflow' title="+addr1+">"+addr1+"</td ><td class='overflow' title="+name1+">"+name1+"</td><td class='overflow' title="+hvalue1+">"+hvalue1+"</td><td class='overflow'>"+unit+"</td><td class='overflow' >"+category+"</td><td class='overflow'>"+remark+"</td></tr>");
						   j=7;
						  for(var i=6;i<parametelist.length;i++){
							 	 var str=j+","+parametelist[i].addr+","+parametelist[i].name;
							 	 var code=parametelist[i].rolecode;
							  	 var id=ids+"_"+i+"_checkid";
								$("#table").append("<tr id='ttid' class='tid' ondblclick=showbitvalue('"+parametelist[i].name+"&"+parametelist[i].bitString+"')><td><label class='label_input01'><input type='checkbox'  onclick='updateUserDialog(this)' title="+code+" id="+ids+"_"+i+"_checkid' name='test' value="+str+"></input><span></span></label></td><td class='overflow' title="+j+">"+j+"</td><td class='overflow' title="+parametelist[i].addr+">"+parametelist[i].addr+"</td ><td class='overflow' title="+parametelist[i].name+">"+parametelist[i].name+"</td><td class='overflow' title="+parametelist[i].hvalue+">"+parametelist[i].hvalue+"</td><td class='overflow' tltle="+parametelist[i].unit+">"+parametelist[i].unit+"</td><td class='overflow'title="+parametelist[i].category+">"+parametelist[i].category+"</td><td class='overflow' title="+parametelist[i].remark+">"+parametelist[i].remark+"</td></tr>"); 						
								$('#table tbody tr:even').css("backgroundColor", "#edf2f6");
							  j++;
						  } 
				   } else{ 
					   $(".tid").remove();
					   for(var i=0;i<parametelist.length;i++){
						   var str=j+","+parametelist[i].addr+","+parametelist[i].name;
						   var code=parametelist[i].rolecode;
						   var id=ids+"_"+i+"_checkid";
						   if(parametelist[i].hvalue.indexOf("H")>=0){ 
							   if(checkid.indexOf(id)>=0){
								   $("#table").append("<tr  id='ttid'class='tid' ondblclick=showbitvalue('"+parametelist[i].name+"&"+parametelist[i].bitString+"')><td><label class='label_input01'><input type='checkbox' checked='true' onclick='updateUserDialog(this)' title="+code+" id="+ids+"_"+i+"_checkid' name='test' value="+str+"></input><span></span></label></td><td class='overflow' title="+j+">"+j+"</td><td class='overflow' title="+parametelist[i].addr+">"+parametelist[i].addr+"</td ><td class='overflow' title="+parametelist[i].name+">"+parametelist[i].name+"</td><td class='overflow' title="+parametelist[i].hvalue+">"+parametelist[i].hvalue+"</td><td class='overflow' tltle="+parametelist[i].unit+">"+parametelist[i].unit+"</td><td class='overflow'title="+parametelist[i].category+">"+parametelist[i].category+"</td><td class='overflow' title="+parametelist[i].remark+">"+parametelist[i].remark+"</td></tr>"); 
							   }else{
								   $("#table").append("<tr  id='ttid' class='tid' ondblclick=showbitvalue('"+parametelist[i].name+"&"+parametelist[i].bitString+"')><td><label class='label_input01'><input type='checkbox' onclick='updateUserDialog(this)' title="+code+" id="+ids+"_"+i+"_checkid' name='test' value="+str+"></input><span></span></label></td><td class='overflow' title="+j+">"+j+"</td><td class='overflow' title="+parametelist[i].addr+">"+parametelist[i].addr+"</td><td class='overflow' title="+parametelist[i].name+">"+parametelist[i].name+"</td><td class='overflow'><p title="+parametelist[i].hvalue+">"+parametelist[i].hvalue+"</td><td class='overflow' tltle="+parametelist[i].unit+">"+parametelist[i].unit+"</td><td class='overflow' title="+parametelist[i].category+">"+parametelist[i].category+"</td><td class='overflow' title="+parametelist[i].remark+">"+parametelist[i].remark+"</td></tr>");   
							   }
						   }else{
								   if(checkid.indexOf(id)>=0){
									   $("#table").append("<tr  id='ttid' class='tid'><td><label class='label_input01'><input type='checkbox' checked='true' onclick='updateUserDialog(this)' title="+code+" id="+ids+"_"+i+"_checkid' name='test' value="+str+"></input><span></span></label></td><td class='overflow' title="+j+">"+j+"</td><td class='overflow' title="+parametelist[i].addr+">"+parametelist[i].addr+"</td><td class='overflow' title="+parametelist[i].name+">"+parametelist[i].name+"</td><td class='overflow' title="+parametelist[i].bitString+">"+parametelist[i].bitString+"</td><td class='overflow' tltle="+parametelist[i].unit+">"+parametelist[i].unit+"</td><td class='overflow' title="+parametelist[i].category+">"+parametelist[i].category+"</td><td class='overflow' title="+parametelist[i].remark+">"+parametelist[i].remark+"</td></tr>"); 
								   }else{
									   $("#table").append("<tr  id='ttid' class='tid' ><td><label class='label_input01'><input type='checkbox' onclick='updateUserDialog(this)' title="+code+" id="+ids+"_"+i+"_checkid' name='test' value="+str+"></input><span></span></label></td><td class='overflow' title="+j+">"+j+"</td><td class='overflow' title="+parametelist[i].addr+">"+parametelist[i].addr+"</td><td class='overflow' title="+parametelist[i].name+">"+parametelist[i].name+"</td><td class='overflow' title="+parametelist[i].bitString+">"+parametelist[i].bitString+"</td><td class='overflow' tltle="+parametelist[i].unit+">"+parametelist[i].unit+"</td><td class='overflow' title="+parametelist[i].category+">"+parametelist[i].category+"</td><td class='overflow' title="+parametelist[i].remark+">"+parametelist[i].remark+"</td></tr>");   
								   } 
						   } 
						   $('#table tbody tr:even').css("backgroundColor", "#edf2f6");
						 j++
					   }
				   }
						  
						   $("#table").show(); 
							  $("#loading").hide();
					       var maxwidth=10;
							//限制字符个数，设置最大长度为10个长度，多的。。。代替
							$(".overflow").each(function(){
								if($(this).text().length>maxwidth){
										$(this).text($(this).text().substring(0,maxwidth));
										$(this).html($(this).html()+'…');
							}
							});	
							autoHeight();
							tc_center(); 
				   }else{
					   
					   alert("获取数据失败");  
				   }
				   
				//  iCount=setInterval(jqid,1000); 
			   }	   
		});
	}
	
	function showbitvalue(bitstr) {
		debugger; 
		var tmpone="运行状态";
		var tmptwo="运行状态解析";
		$(".a_button").hide();
		var butstrtmp=bitstr.split("&");
		var tmpstr=butstrtmp[0];

		if(butstrtmp[1].indexOf("|")>0){
			var bit = butstrtmp[1].split("|");
			//设置弹窗
			if(bitstr==""||bitstr==null||bitstr=="null"||bitstr=="0"){
				return;
			} 
			if(bitstr.indexOf("|")<0){
				//var bit = bitstr.split("|");
				$("#bit_table tbody").html("");
				//清除此Id下的tbody标签
				   var contStr1 = "<tbody>";
					var one=1;
					contStr1 += "<tr >"
					contStr1 += "<td>"+1+"</td>"
					contStr1 += "<td>"+bitstr+"</td>"
					contStr1 +="<td><strong class='strong_S'></td>"
			}else{
			var bit = bitstr.split("|");
			$("#bit_table tbody").html("");
			//清除此Id下的tbody标签
			var contStr1 = "<tbody>";
			for (var i = 0; i < bit.length; i++) {
				var value=bit[i];
				var valueone=value.split(",");
				var bitvalue=valueone[2];
				var one=1;
				contStr1 += "<tr >"
				contStr1 += "<td>"+i+"</td>"
				contStr1 += "<td>"+valueone[1]+"</td>"
				if(bitvalue==0){
					contStr1 +="<td><strong class=''></td>"
				}else{
					contStr1 +="<td><strong class='strong_S'></td>"
				}
				
			} 
		}
			contStr1 += "</tbody>";
			$("#bit_table").append(contStr1);
			$('#bit_table tbody tr:even').css("backgroundColor", "#edf2f6");
			document.getElementById("bit_div").style.display = "block";
			document.getElementById("bit_bt").innerHTML=butstrtmp[0];
			$("#gray").show();
		}else if(tmpstr==tmpone||tmpstr==tmptwo){
			  
			 if(butstrtmp[1]!=""&&butstrtmp[1]!="不解析"){
				 $("#bit_table tbody").html("");
					//清除此Id下的tbody标签
					   var contStr1 = "<tbody>";
						var one=1;
						contStr1 += "<tr >"
						contStr1 += "<td>"+1+"</td>"
						contStr1 += "<td>"+butstrtmp[1]+"</td>"
						contStr1 +="<td><strong class='strong_S'></td>"
							contStr1 += "</tbody>";
						$("#bit_table").append(contStr1);
						$('#bit_table tbody tr:even').css("backgroundColor", "#edf2f6");
						document.getElementById("bit_div").style.display = "block";
						document.getElementById("bit_bt").innerHTML=butstrtmp[0];
						$("#gray").show(); 
			 }
			
	      }
	
	}
	
	function closebit(){
		$("#bit_bt").html("参数设置");
		document.getElementById("bit_div").style.display = "none";
		$("#gray").hide();
	}
	
	function js_method(type){
		//alert(1);
		var device_id=$("#inid").val();
		var modbusid=$("#modbusid").val();
		var url="";
		if(type==1){
			url="<%=basePath%>Devicetail/chooseMenu.do?device_id="+ device_id+"&modbus_type="+modbusid+"&type="+type+"&modbusid=-1";
			<%-- window.location.href="<%=basePath%>jsp/devicedetailjsp/DeviceSystemParame.jsp?device_id="+ device_id+"&modbus_type="+modbusid; --%>
		}else if(type==2){
			<%-- window.location.href="<%=basePath%>jsp/devicedetailjsp/parametorlist.jsp?device_id="+ device_id+"&modbus_type="+modbusid; --%>
			url="<%=basePath%>Devicetail/chooseMenu.do?device_id="+ device_id+"&modbus_type="+modbusid+"&type="+type+"&modbusid=-1";
		}
        else if(type==3){
			<%-- window.location.href="<%=basePath%>wfp/osc/toPage.do?device_id="+ device_id+"&modbus_type="+modbusid; --%>
			url="<%=basePath%>Devicetail/chooseMenu.do?device_id="+ device_id+"&modbus_type="+modbusid+"&type="+type+"&modbusid=-1";
		}
       	else if(type==4){
        	 if(modbusid==2){
      			<%-- window.location.href="<%=basePath%>jsp/devicedetailjsp/DeviceSystemTest_hsfd.jsp?device_id="+ device_id+"&modbus_type="+modbusid; --%>
      			url="<%=basePath%>Devicetail/chooseMenu.do?device_id="+ device_id+"&modbus_type="+modbusid+"&type="+type+"&modbusid="+modbusid;
        	 }else{
      			<%-- window.location.href="<%=basePath%>jsp/devicedetailjsp/DeviceSystemTest.jsp?device_id="+ device_id+"&modbus_type="+modbusid; --%>
      			url="<%=basePath%>Devicetail/chooseMenu.do?device_id="+ device_id+"&modbus_type="+modbusid+"&type="+type+"&modbusid="+modbusid;
        	 }
        }
	
		if(type!=3){
			window.location.href=url;
		}else{
			window.open(url);
		}
	}
	
	
	function updateUserDialog(obj){
		//设置获取复选框是否被选中，页面刷新关闭还是开启
		var checkid="";
		 var coded=obj.title;
		if(coded=="W"){
			var obj=$("input[name='test']:checked");
			check_val = [];
			for(k in obj){
				if(obj[k].checked){
					check_val.push(obj[k].id);
					checkid+=obj[k].id+","
				}
			}			
			 $("#checkid").attr("value",checkid);	
		}else{
			 alert("此参数状态仅为可读");
			 $(obj).removeAttr("checked");	
      } 
	}
	
	
	function updateDialog(){
		//修改参数弹出框设置
		$("#bit_table").html("");
		var obj = $("input[name='test']:checked");
		check_val = [];
		for(k in obj){
			if(obj[k].checked){
				check_val.push(obj[k].value);
			}
		}
		if(check_val.length==0){
			   alert("请最少选择一条数据修改");
		   }else{
			   
			   var flag=true;
				 $.ajax({
						async: false, 
						type:'post',
						url:'<%=basePath%>DeviceTest/selectMenu_writpP.do',
						cache: "false",  
						//data:'',
						success:function(data){
							var result = JSON.parse(data);	
						   	var msg=result.message;
						   	if(msg == "获取成功"){
						   		var write_p=result.responseData.write_p;
			  			   	if(write_p!=0){
									flag=false;
								}else{
									flag=true;
								}
						    }else{
						    	alert(msg);
						    }
						}});
						if(flag==false){
							alert("没有该权限");
							return false;
						}
		   		
				var contStr1 = "<tbody>";
				for(var i=0;i<check_val.length;i++){
					//通过弹窗绘制表格
					var val=check_val[i].split(",");
						contStr1 += "<tr>"
						contStr1 += "<td style='display:none'>"+val[0]+"</td>"
						contStr1 += "<td>"+val[1]+"</td>"
						contStr1 += "<td>"+val[2]+"</td>"
						contStr1 += "<td><input type='text' name='value' id='vid'></td>"
						contStr1 += "</tr>";
				}
				$("#bit_table").append(contStr1);
				$('#bit_table tbody tr:even').css("backgroundColor", "#edf2f6");
				document.getElementById("bit_div").style.display = "block";
				$("#gray").show();
				if($("#aid").length>0){
					//删除已经有的提交按钮
					$("#aid").remove();
				}
				$("#div_id").append("<a class='a_button' onclick='submit()' id='aid' style='cursor:pointer;'>保存</a>");
				$('#bit_table tbody tr:even').css("backgroundColor", "#edf2f6");
		 	  }
		   }
	function submit(){
		//debugger;
		sendcodereq("update");
		var device_id=$("#inid").val(); 
		var id=$("#id").val();
		//点击保存需要提交的方法
		obj = $("input[name='test']:checked");//获取选中的checkbox的值
		check_val = [];
		for(k in obj){
			if(obj[k].checked){
				check_val.push(obj[k].value);
			}
		}  
		var numArr = [];
		var str ="";
		var strs="";
		$("input[type=text").each(function(i){
			  str += $(this).val() + ",";//获取输入文本框的值
			});
		var value=str.split(",");
			for(var i=0;i<check_val.length;i++){//对checkbox进行遍历
			var val=check_val[i].split(",");
			//如果需要序号，在加上个val[0]
			strs+=val[0]+","+val[1]+","+val[2]+","+value[i]+"|";//对需要的字段进行拼接
			}
			
			//提交ajax方法
		$.ajax({
			   type:"post",
			   async: false,
			   data:{"paramestr":strs,"device_id":device_id,"ids":id},
			   url:"<%=basePath%>DeviceTest/sendcodeall.do",
			   success:function(data){
				   var result = JSON.parse(data);
					 if("1001" == result.code){
						 alert("修改成功");
						 parent.$("#bit_div").hide();
						 parent.$("#gray").hide();
						 }else{
							 alert(result.message);  
							 }
						 },
					error:function(e){
					}
		  }); 
		
			
	}
	 function sendcodereq(obj){
		 
		 var flag=true;
		 $.ajax({
				async: false, 
				type:'post',
				url:'<%=basePath%>DeviceTest/selectMenu_writpP.do',
				cache: "false",  
				//data:'',
				success:function(data){
					var result = JSON.parse(data);	
				   	var msg=result.message;
				   	if(msg == "获取成功"){
				   		var write_p=result.responseData.write_p;
	  			   	if(write_p!=0){
							flag=false;
						}else{
							flag=true;
						}
				    }else{
				    	alert(msg);
				    }
				}});
				if(flag==false){
					alert("没有该权限");
					return false;
				}
		 
		 var device_id=$("#inid").val();
		 $.ajax({
			   	type:"post",
			   	url:"<%=basePath%>DeviceTest/setControlCodeReq.do",
				async: false,
				data : {
					"device_id" : device_id
				},
				success : function(data) {
					var result = JSON.parse(data);
					var msg = result.code;
					if (msg == "1001") {
						gtecodereq(obj);
					}else{
						alert("请求写指令发送失败");
					}
					}
			});
	 }
	function sendcodereqAdd(obj){
		 var device_id=$("#inid").val();
		 $.ajax({
			   	type:"post",
			   	url:"<%=basePath%>DeviceTest/setControlCodeReq.do",
				async: false,
				data : {
					"device_id" : device_id
				},
				success : function(data) {
					var result = JSON.parse(data);
					var msg = result.code;
					if (msg == "1001") {
						gtecodereq(obj);
					}else{
						alert("请求写指令发送失败");
					}
					}
			});
	 }
	 
	 function gtecodereq(obj){
		 var device_id=$("#inid").val();
		 $.ajax({
			   	type:"post",
			   	url:"<%=basePath%>DeviceTest/getControlCodeReq.do",
				async: false,
				data : {
					"device_id" : device_id
				},
				success : function(data) {
					var result = JSON.parse(data);
					var msg = result.code;
					if (msg == "1001") {
						var Device = result.responseData.Device;
						var req=Device.rw_role_req;
						var res=Device.rw_role_res;
						
						if(req=="W"&&res=="OK"){
							if(obj!="update"){
								selectone();
							}
						}else if(req=="R"&&res=="OK"){
							sendcodereq();
						}else{
							if(obj!="update"){
								selectone();
							}
						}
						
						//故障数据
					}else{
						alert("请求写指令发送失败");
					}
					}
			});
	 }
	function selectone(){
		var device=$("#inid").val(); 
		var id=$("#id").val();
		$.ajax({
			   type:"post",
			   data:{"device_id":device,"ids":id},
				async: false,
			   url:"<%=basePath%>DeviceTest/sendcodeselect.do",
			   success:function(data){
				   var result = JSON.parse(data);
					 if("1001" == result.code){
						 }else{
							 alert("服务连接失败");  
							}
					 }
		 });   
	}
	
	function sendcode(addr,code,type){
			var device_id=$("#inid").val();
		 $.ajax({
			   	type:"post",
			   	url:"<%=basePath%>DeviceTest/setControlCode.do",
			   	async: false,
				data : {
					"device_id" : device_id,
					"code" : code,
					"addr" : addr,
					"type":type
				},
				success : function(data) {
					var result = JSON.parse(data);
					var msg = result.code;
					if (msg == "1001") {
						//故障数据
					}else{
						alert("写指令失败");
					}
					}
					});

		}

  function sendrealcode(value){
	  
  var flag=true;
	 $.ajax({
			async: false, 
			type:'post',
			url:'<%=basePath%>DeviceTest/selectMenu_writpP.do',
			cache: "false",  
			//data:'',
			success:function(data){
				var result = JSON.parse(data);	
			   	var msg=result.message;
			   	if(msg == "获取成功"){
			   		var write_p=result.responseData.write_p;
  			   	if(write_p!=0){
						flag=false;
					}else{
						flag=true;
					}
			    }else{
			    	alert(msg);
			    }
			}});
			if(flag==false){
				alert("没有该权限");
				return false;
			}  
  
	  sendcodereq("update");
	  //打开写指令  实时
	  if(value=="ss"){
		  var id = $("#xb_select_id").val();
		  var getURL = "<%=basePath%>Parameter/harmonicUnit.do?id="+id;
		  $.get(getURL);
		   //编号值
		  sendcode("000A", id,0);
		  //确认指令
		  sendcode("0000", "00A5H",1);
	  }else if(value=="gz"){
		  //故障
		  var id = $("#xb_select_id").val();
		  var getURL = "<%=basePath%>Parameter/harmonicUnit.do?id="+id;
		  $.get(getURL);
		  var value = $("#xb_input").val();
		  var val=id+","+value;
		   //编号值
		  sendcode("000A", val,0);
		  //确认指令
		  sendcode("0000", "00A6H",1);
	  }
	  
  }
  function adderrorct(type){
	  
	  <%-- var flag=true;
		 $.ajax({
				async: false, 
				type:'post',
				url:'<%=basePath%>DeviceTest/selectMenu_writpP.do',
				cache: "false",  
				//data:'',
				success:function(data){
					var result = JSON.parse(data);	
				   	var msg=result.message;
				   	if(msg == "获取成功"){
				   		var write_p=result.responseData.write_p;
	  			   	if(write_p!=0){
							flag=false;
						}else{
							flag=true;
						}
				    }else{
				    	alert(msg);
				    }
				}});
				if(flag==false){
					alert("没有该权限");
					return false;
				}
  --%>
	 // debugger;
	    //发送写指令请求
	    sendcodereqAdd("update");
		var device_id=$("#inid").val();
	    var code="";
	    var  addr="0000";
		if(type=="zj"){
			code ="0085H";
		}else{
			code ="0086H";
		}
	  var type=1;
	  sendcode(addr, code,1);
}
function autoHeight(){
	var codeSize=$("#codeSize").val();
	var topheight = $("#oneid").height();
	var autoheight = 0;
	if(topheight == 0){
		autoheight = $(window).height()-120;
	}else{
		autoheight = $(window).height()-160 - topheight;
	}
	if(codeSize!=1 && codeSize!=2 && topheight == 0){
		$("#oneid").hide();
		autoheight = $(window).height()-120;
	}
	$(".right_main_div").attr("style","min-height:"+$(window).height()+"px;max-height:"+$(window).height()+"px");
	$("#autoarea").attr("style","min-height:"+autoheight+"px;max-height:"+autoheight+"px");
}


function fw_send(){
	var flag=true;
	 $.ajax({
			async: false, 
			type:'post',
			url:'<%=basePath%>DeviceTest/selectMenu_writpP.do',
			cache: "false",  
			//data:'',
			success:function(data){
				var result = JSON.parse(data);	
			   	var msg=result.message;
			   	if(msg == "获取成功"){
			   		var write_p=result.responseData.write_p;
  			   	if(write_p!=0){
						flag=false;
					}else{
						flag=true;
					}
			    }else{
			    	alert(msg);
			    }
			}});
			if(flag==false){
				alert("没有该权限");
				return false;
			}
	
	sendcodereq("update");
	var device=$("#inid").val(); 
	var id=$("#id").val();
		//提交ajax方法
	$.ajax({
		   type:"post",
		   data:{"device_id":device,"ids":id},
			async: false,
		   url:"<%=basePath%>DeviceTest/sendcodefw_code.do",
		   success:function(data){
			   var result = JSON.parse(data);
				 if("1001" == result.code){
					// iCount=setInterval(jqid,1000); 
					 }else{
						 alert("查询指令设置失败！");  
						// iCount=setInterval(jqid,1000); 
						}
				 }
	 });  
}
function snsendcode(str){
	
	 var flag=true;
	 $.ajax({
			async: false, 
			type:'post',
			url:'<%=basePath%>DeviceTest/selectMenu_writpP.do',
			cache: "false",  
			//data:'',
			success:function(data){
				var result = JSON.parse(data);	
			   	var msg=result.message;
			   	if(msg == "获取成功"){
			   		var write_p=result.responseData.write_p;
  			   	if(write_p!=0){
						flag=false;
					}else{
						flag=true;
					}
			    }else{
			    	alert(msg);
			    }
			}});
			if(flag==false){
				alert("没有该权限");
				return false;
			}
	
	
	  sendcodereq("update");
	    var code=str;
	    var  addr="31f4";
	  sendcode(addr, code,1)
}
function mrsz_send(str){
	var flag=true;
	 $.ajax({
			async: false, 
			type:'post',
			url:'<%=basePath%>DeviceTest/selectMenu_writpP.do',
			cache: "false",  
			//data:'',
			success:function(data){
				var result = JSON.parse(data);	
			   	var msg=result.message;
			   	if(msg == "获取成功"){
			   		var write_p=result.responseData.write_p;
 			   	if(write_p!=0){
						flag=false;
					}else{
						flag=true;
					}
			    }else{
			    	alert(msg);
			    }
			}});
			if(flag==false){
				alert("没有该权限");
				return false;
			}
	
	sendcodereq("update");
	var device=$("#inid").val(); 
	var id=$("#id").val();
		//提交ajax方法
	$.ajax({
		   type:"post",
		   data:{"device_id":device,"ids":id},
			async: false,
		   url:"<%=basePath%>DeviceTest/sendcodemrsz_code.do",
		   success:function(data){
			   var result = JSON.parse(data);
				 if("1001" == result.code){
					 }else{
						 alert("设置指令失败!");  
						}
				 }
	 });
}

</script>
<body  style="overflow: hidden">

<input type="hidden" value="${param.device_id}" id="inid">
<input type="hidden" value="${param.modbus_type}" id="modbusid">
<input type="hidden"  id="id">
<input type="hidden"  id="typeid">
<input type="hidden"  id="checkid">
<input type="hidden" id="loadValue" value="0" />
  <!-- 头部内容home -->
  <!-- <div id="loading" style="margin:250px 600px;"></div> -->
  
    <div class="hd_box">
        <div class="logo_box">
            <a href="#"><img src="<%=basePath%>/images/logo.png" ></a>
        </div>
      <div class="nav_box">
			<ul>
				<li ><a  href="javascript:js_method(1);"><i><strong class="S_10"></strong></i><span>系统信息</span></a></li>
				<li class="li_icon"><a href="javascript:js_method(2);"><i><strong class="S_11"></strong></i><span>参数监控</span></a></li>
				<li><a  href="javascript:js_method(3);"><i><strong class="S_12"></strong></i><span>示波器</span></a></li>
				<li><a  href="javascript:js_method(4);"><i><strong class="S_13"></strong></i><span>调试面板</span></a></li>

				<li class="style_li"><a href="<%=basePath%>jsp/index.jsp"><i><strong
							class="S_14"></strong></i><span>首页</span></a></li>
			</ul>
			<hr />
		</div>
    </div>
    <!-- 头部内容end -->
    <div class="main_box clearfix">
        <!-- 左侧菜单 -->
        <div class="leftsidebar_box" style="overflow:auto;">
            <dl id="net">
                <dt  onclick="jq(1)"><a ><span>网侧</span><img src="<%=basePath%>images/left/select_xl.png"></a></dt>
                   </dl>
            <dl id="enjine">
               <dt  onclick="jq(2)"><a ><span>机侧<img src="<%=basePath%>images/left/select_xl01.png"></span></a></dt>
            </dl>
            <dl id="outer">
                <dt onclick="jq(3)" ><a ><span>外围接口<img src="<%=basePath%>images/left/select_xl01.png"></span></a></dt>
            </dl>


        </div>
        <!-- 左侧菜单end -->
        <!-- 右侧内容 -->
        <div class="right_main_div" style="overflow:auto;">
             <div class="W_width" id="oneid"  style="margin-top: -10px;">
             
                 <p class="P_Label clearfix"    style="display:none" id="gzjcb_id_qb"> 
                    <a class="a_size02"  onclick="snsendcode('00BFH')" style="cursor:pointer;" ><span >增查询</span></a>
                    <a class="a_size02"  onclick="snsendcode('00C1H')" style="cursor:pointer;"><span >减查询</span></a>
                    <a class="a_size02"  onclick="snsendcode('00C2H')" style="cursor:pointer;"><span >当前查询</span></a>
                </p>
                
                 <p class="P_Label clearfix"    style="display:none" id="snxt_id"> 
                    <a class="a_size02"  onclick="snsendcode('00A9H')" style="cursor:pointer;" ><span >水泵启动</span></a>
                    <a class="a_size02"  onclick="snsendcode('00A8H')" style="cursor:pointer;"><span >水泵停止</span></a>
                    <a class="a_size02"  onclick="snsendcode('00AAH')" style="cursor:pointer;"><span >内循环</span></a>
                   <a class="a_size02"  onclick="snsendcode('00ABH')" style="cursor:pointer;"> <span >外循环</span></a>
                </p>
                
                 <p class="P_Label clearfix"    style="display:none;" id="gzjcb_id_jc">
                    <a class="a_sizeList"  onclick="snsendcode('00AFH')" style="cursor:pointer;" ><span  style="cursor:pointer;margin:0px 5px">RotFA1增查询</span></a>
                    <a class="a_sizeList"  onclick="snsendcode('00B1H')" style="cursor:pointer;" ><span  style="cursor:pointer;margin:0px 5px ">RotFA1减查询</span></a>
                    <a class="a_sizeList"  onclick="snsendcode('00B2H')" style="cursor:pointer;"><span  style="cursor:pointer;margin:0px 5px">RotFA1当前</span></a>
                    
                    <a class="a_sizeList"  onclick="snsendcode('00B6H')" style="cursor:pointer;" ><span style="cursor:pointer;margin:0px 5px">RotFA2增查询</span></a>
                    <a class="a_sizeList"  onclick="snsendcode('00B7H')" style="cursor:pointer;"><span style="cursor:pointer;margin:0px 5px">RotFA2减查询</span></a>
                    <a class="a_sizeList"  onclick="snsendcode('00B8H')" style="cursor:pointer;"><span style="cursor:pointer;margin:0px 5px">RotFA2当前</span></a>
                    
                    <a class="a_sizeList"  onclick="snsendcode('00BCH')" style="cursor:pointer;"><span style="cursor:pointer;margin:0px 5px">RotFA3增查询</span></a>
                    <a class="a_sizeList"  onclick="snsendcode('00BDH')" style="cursor:pointer;"><span style="cursor:pointer;margin:0px 5px">RotFA3减查询</span></a>
                   <a class="a_sizeList"  onclick="snsendcode('00BEH')" style="cursor:pointer;"> <span style="cursor:pointer;margin:0px 5px">RotFA3当前</span></a>
                </p>
               <p class="P_Label clearfix"    style="display:none;" id="gzjcb_id_wc"> 
                    <a class="a_sizeList"  onclick="snsendcode('00ACH')" style="cursor:pointer;" ><span class="spanChange" style="cursor:pointer;margin:0px 5px">NetFA1增查询</span></a>
                    <a class="a_sizeList"  onclick="snsendcode('00ADH')" style="cursor:pointer;" ><span style="cursor:pointer;margin:0px 5px">NetFA1减查询</span></a>
                    <a class="a_sizeList"  onclick="snsendcode('00AEH')" style="cursor:pointer;"><span style="cursor:pointer;margin:0px 5px">NetFA1当前</span></a>
                    
                    <a class="a_sizeList"  onclick="snsendcode('00B3H')" style="cursor:pointer;" ><span style="cursor:pointer;margin:0px 5px">NetFA2增查询</span></a>
                    <a class="a_sizeList"  onclick="snsendcode('00B4H')" style="cursor:pointer;"><span style="cursor:pointer;margin:0px 5px">NetFA2减查询</span></a>
                    <a class="a_sizeList"  onclick="snsendcode('00B5H')" style="cursor:pointer;"><span style="cursor:pointer;margin:0px 5px">NetFA2当前</span></a>
                   
                    <a class="a_sizeList"  onclick="snsendcode('00B9H')" style="cursor:pointer;"><span style="cursor:pointer;margin:0px 5px">NetFA3增查询</span></a>
                    <a class="a_sizeList"  onclick="snsendcode('00BAH')" style="cursor:pointer;"><span style="cursor:pointer;margin:0px 5px">NetFA3减查询</span></a>
                    <a class="a_sizeList"  onclick="snsendcode('00BBH')" style="cursor:pointer;"><span style="cursor:pointer;margin:0px 5px">NetFA3当前</span></a>
                </p>
                
                
                
                <input type="hidden" id="codeSize" >
			    <p class="P_Label clearfix"    style="display:none" id="Write_id"> 
                    <a class="a_size02"  id="setup" onclick="updateDialog()" style="cursor:pointer;" ><span id="Write_id_sz">设置</span></a>
                    <a class="a_size02" id="codeLength" onclick="sendcodereq()" style="cursor:pointer;"><span id="Write_id_cx">查询</span></a>
                    <a class="a_size02"  onclick="fw_send()" style="cursor:pointer;"><span id="Write_id_fw">复位</span></a>
                    <a class="a_size02"  onclick="sendcodereq()" style="cursor:pointer;"><span id="Write_id_sz_mr">默认设置</span></a>
                </p>
                
                 <p class="P_Label clearfix"    style="display:none" id="Adderror_id"> 
                    <a class="a_size02"  onclick="adderrorct('zj')" style="cursor:pointer;"><span>故障次数增</span></a>
                    <a class="a_size02"  onclick="adderrorct('js')" style="cursor:pointer;"><span>故障次数减</span></a>
                   
                </p>
                <p class="P_Label clearfix" id="xb_id" style="display:none">
                 <span><select id="xb_select_id" style="cursor:pointer;">
                     <option value="1">电网电压Uab</option>
                     <option value="2">电网电压Ubc</option>
                     <option value="3">电网电流Ia1</option>
                     <option value="4">电网电流Ib1</option>
                     <option value="4">电网电流Ic1</option>
                     <option value="5">电网电流Ia2</option>
                     <option value="6">电网电流Ib2</option>
                     <option value="7">电网电流Ic2</option>
                   </select>
                   </span>
                   <span>故障次数：<input id="xb_input"></input></span>
                     <a class="a_size02"  onclick="sendrealcode('ss')" style="cursor:pointer;"><span>实时查询按钮</span></a>
                       <a class="a_size02"  onclick="sendrealcode('gz')" style="cursor:pointer;"><span>故障查询按钮</span></a>
                </p>
                </div>
                 <div id="loading" style="margin:250px 600px;display:none"></div> 
                <!-- table内容 -->
                <div class="table_box" id="autoarea">  
                           
                    <table id="table" class="tableclass">
                            <tr id="tableid">
                            	<td style="width:4%"></td>
                                <td style="width:5%">序号</td>
                                <td style="width:10%">地址</td>
                                <td style="width:20%">参数名称</td>
                                <td style="width:13%">参数值</td>
                                <td style="width:10%">单位</td>
                                <td style="width:20%">描述</td>
                                <td style="width:18%">备注</td>
                            </tr>
                    </table>
                  
                    <table id="table1" class="tableclass">
                    </table>
  		<!-- 新增/编辑弹窗 -->
  		   
        </div>
        <!-- 右侧内容end -->
    
</div>
</div>    
<div id="gray"></div>    
<!-- 弹框 -->
<div style="display:none ;" class="popup poput_box_b Min_w tablee" id="bit_div">
    <p class="poput_title"><span id="bit_bt">参数设置</span>
       <a href="javascript:void(0)"  onclick="closebit();"></a>
    </p>
    <div class="table_w poput_box_height" id="div_id">
				<table class="table_S" id="bit_table">
                 <thead>
                     <tr>
                         <th>位号</th>
                         <th>名称</th>
                         <th>状态</th>
                     </tr>
                 </thead>
                 <tbody>
                    
                 </tbody>
             	</table>
	</div>
</div>    
<!-- 弹框 end-->
</body>

</html>