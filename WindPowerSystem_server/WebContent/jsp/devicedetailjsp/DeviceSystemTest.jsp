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
<base href="<%=basePath%>">
<!-- jsp文件头和头部 -->
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link href="<%=basePath%>/favicon.ico" rel="icon" type="image/x-icon" />
<link href="<%=basePath%>/favicon.ico" rel="shortcut icon"
	type="image/x-icon" />
<title>调试面板</title>
<link rel="stylesheet" type="text/css"
	href="<%=basePath%>/css/common.css">
<link rel="stylesheet" type="text/css"
	href="<%=basePath%>/css/style.css">

<!-- 共同引用jquery js -->
<script type="text/javascript" src="<%=basePath%>/js/jquery1124.js"></script>
<%-- <script type="text/javascript" src="<%=basePath%>/js/js.js"></script> --%>
<script type="text/javascript" src="<%=basePath%>js/opts.js"></script>
<script type="text/javascript" src="<%=basePath%>js/spin.js"></script>
<!-- 共同引用jquery js -->
<script type="text/javascript">
 window.onload=function(){
	/* debugger; */
	 var urlstr=window.location.href;
	 var device=urlstr.split("?")[1].split("&")[0].split("=")[1]
	 var modbus_type=urlstr.split("?")[1].split("&")[1].split("=")[1]
	 $("#inid").attr("value",device);
	 $("#modbusid").attr("value",modbus_type);
	 getmodel(1);
	 autoHeight();
	}

 //获取控制模式界面
 function showalert(){
	 var str = [1,3,5,7,8,9,10,11,12,13];
	 var random = str[Math.floor(Math.random()*str.length)];
	 var result = str[random];
		var device_id=$("#inid").val();
	 $.ajax({
		   	type:"post",
		   	url:"<%=basePath%>DeviceTest/getControlModel.do",
			data : {
				"device_id" : device_id
			},
			success : function(data) {
				var result = JSON.parse(data);
				var msg = result.code;
				if (msg == "1001") {
					/* debugger; */
					var ControlModel = result.responseData.ControlModel;
					if (ControlModel.length > 0) {
						var controlobj= $("#control_id");
						  $("#control_id").html("");
			          for(var i=0;i<ControlModel.length;i++){
						var listr="<li class='mskz'   onclick='sendcodereq(this);' title='00"+ControlModel[i].split(":")[2]+"'><a>"+ControlModel[i].split(":")[0]+"</a></li>";
						controlobj.append(listr);
						var listr="<li class='listyle' >"+ControlModel[i].split(":")[1]+"</li>";
						controlobj.append(listr); 
						}
					}else{
	                            $("#control_id").html("");
								var str = "<div style='text-align:center';>没有相关内容</div>";
								$("#control_id").append(str);
						}
					/* debugger; */
					var ControlModel = result.responseData.modellist;
					var modelstr = result.responseData.modelstr;
					if (ControlModel.length > 0) {
						var controlobj= $("#run_model");
						  $("#run_model").html("");
			          for(var i=0;i<ControlModel.length;i++){
			        	  var listr="";
			        	  var one=ControlModel[i].name;
			        	  if(ControlModel[i].name=="功率循环"){
			        		  one="整流并网模式";
			        	  }
			        	  
			        	  if(ControlModel[i].name=="风场运行"){
			        		  one="风场运行模式";
			        	  }
			        	  if(ControlModel[i].name!="模块测试"&&ControlModel[i].name!="撬棒测试"){
			        	  if(ControlModel[i].name==modelstr){
			        		  listr="<li    class='listyle' style='background:#488fd2;' onclick='alert(1);'>"+one+"</li>";  
			        	  }else{
			        		  listr="<li class='listyle' >"+one+"</li>";  
			        	  }
						      controlobj.append(listr);
						}}
					}else{
	                            $("#run_model").html("");
								var str = "<div style='text-align:center';>没有相关内容</div>";
								$("#run_model").append(str);
						}

						}
					}
				});

	}
 function showztkz(){
	 var urlstr=window.location.href;
	 var device=urlstr.split("?")[1].split("&")[0].split("=")[1]
	 var modbus_type=urlstr.split("?")[1].split("&")[1].split("=")[1]
	var modbus_id = modbus_type;
	 var str = [1,3,5,7,8,9,10,11,12,13];
	 var random = str[Math.floor(Math.random()*str.length)];
	 var result = str[random];
		var device_id=$("#inid").val();
	 $.ajax({
		   	type:"post",
		   	url:"<%=basePath%>DeviceTest/getControlState.do",
			data : {
				"device_id" : device_id
			},
			success : function(data) {
				var result = JSON.parse(data);
				var msg = result.code;
				if (msg == "1001") {
					/* debugger; */
					var ControlModel = result.responseData.ControlModel;
					var controlobj= $("#modestate_id");
					if(modbus_id==0){
						controlobj= $("#modestate_id_sk");
					}else{
						controlobj= $("#modestate_id_qgl");
					}
					   controlobj.html("");
					if (ControlModel.length > 0) {
			          for(var i=0;i<ControlModel.length;i++){
			        	  var listr="";
			        	  if(ControlModel[i].split(",")[1]==0){
			        		  listr="<li  class='listyle02'><a style='cursor: default;' class='abj'>"+ControlModel[i].split(",")[0]+"</a></li>";    
			        	  }else if(ControlModel[i].split(",")[0]=="故障状态"||ControlModel[0].split(",")[1]=="掉电复位"){
			        		  listr=" <li class='listyle02'><a style='cursor: default;'   class='error'>"+ControlModel[i].split(",")[0]+"</a></li>"; 
			        	  }else{
			        		  listr=" <li class='listyle02'><a style='cursor: default;' >"+ControlModel[i].split(",")[0]+"</a></li>"; 
  
			        	  };
						      controlobj.append(listr);
						}
					}else{
						        controlobj.html("");
								var str = "<div style='text-align:center';>没有相关内容</div>";
								controlobj.append(str);
						}

						}
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
	 code= obj.title
	 $("#codeid").attr("value",code);
	 var device_id=$("#inid").val();
	 $.ajax({
		    async: false,  
		   	type:"post",
		   	url:"<%=basePath%>DeviceTest/setControlCodeReq.do",
			data : {
				"device_id" : device_id
			},
			success : function(data) {
				var result = JSON.parse(data);
				var msg = result.code;
				if (msg == "1001") {
					gtecodereq();
				}else{
					alert("请求写指令发送失败");
				}
				}
		});
 }
 
 function gtecodereq(){
	var code=$("#codeid").val();
	 var device_id=$("#inid").val();
	 $.ajax({
			async: false, 
		   	type:"post",
		   	url:"<%=basePath%>DeviceTest/getControlCodeReq.do",
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
						sendcode("0000",code,1);	
					}else if(req=="SJ"&&res=="OK"){
						alert("改设备正在升级,无法写入数据！");
					}else if(req=="R"&&res=="OK"){
						sendcodereq();
					}else {
						sendcode("0000",code,1);	
					}
				}else{
					alert("请求写指令发送失败");
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
				"type" : type
			},
			success : function(data) {
				var result = JSON.parse(data);
				var msg = result.code;
				var msgONE = result.message;
				if (msg == "1001") {
				}else{
					alert("写指令失败:"+msgONE);
				}
				}
				});

	}

 function js_method(type){
		var device_id=$("#inid").val();
		var modbusid=$("#modbusid").val();
		var url="";
		if(type==1){
			url="<%=basePath%>Devicetail/chooseMenu.do?device_id="+ device_id+"&modbus_type="+modbusid+"&type="+type+"&modbusid=-1";
		}else if(type==2){
			url="<%=basePath%>Devicetail/chooseMenu.do?device_id="+ device_id+"&modbus_type="+modbusid+"&type="+type+"&modbusid=-1";

		}
        else if(type==3){
        	url="<%=basePath%>Devicetail/chooseMenu.do?device_id="+ device_id+"&modbus_type="+modbusid+"&type="+type+"&modbusid=-1";

		}
       	else if(type==4){
        	 if(modbusid==2){
        		 url="<%=basePath%>Devicetail/chooseMenu.do?device_id="+ device_id+"&modbus_type="+modbusid+"&type="+type+"&modbusid="+modbusid;

        	 }else{
        		 url="<%=basePath%>Devicetail/chooseMenu.do?device_id="+ device_id+"&modbus_type="+modbusid+"&type="+type+"&modbusid="+modbusid;
	 
        	 }
        }
		if(type!=3){
			window.location.href=url;
		}else{
			window.open(url);
		}
	}
	var time1 = "";
	var time2 = "";
	function getmodel(model) {
		$("#W_width").hide();
		$("#loading").show();
		
		//alert(model);
		 var urlstr=window.location.href;
		 var device=urlstr.split("?")[1].split("&")[0].split("=")[1]
		 var modbus_type=urlstr.split("?")[1].split("&")[1].split("=")[1]
		 var modbus_id = modbus_type;
		;
		if (model == 1) {
			//参数给定
			if (modbus_id == 0) {
				document.getElementById("csgd_sk").style.display = "block";
				document.getElementById("csgd_qgl").style.display = "none";
			} else if (modbus_id == 1) {
				document.getElementById("csgd_qgl").style.display = "block";
				document.getElementById("csgd_sk").style.display = "none";
			}

			document.getElementById("fcmscsdd_sk").style.display = "none";
			document.getElementById("ztkz_sk").style.display = "none";

			document.getElementById("fcmscsdd_qgl").style.display = "none";
			document.getElementById("ztkz_qgl").style.display = "none";
			document.getElementById("zzms").style.display = "none";
			$("#head_1").addClass("li_style");
			$("#head_2").removeClass("li_style");
			$("#head_3").removeClass("li_style");
			$("#head_4").removeClass("li_style");
			window.clearInterval(time1);
			window.clearInterval(time2);
		} else if (model == 2) {
			//风场模式
			if (modbus_id == 0) {
				document.getElementById("fcmscsdd_sk").style.display = "block";
				document.getElementById("fcmscsdd_qgl").style.display = "none";
			} else if (modbus_id == 1) {
				document.getElementById("fcmscsdd_qgl").style.display = "block";
				document.getElementById("fcmscsdd_sk").style.display = "none";
			}
			document.getElementById("csgd_sk").style.display = "none";
			document.getElementById("ztkz_sk").style.display = "none";

			document.getElementById("csgd_qgl").style.display = "none";
			document.getElementById("ztkz_qgl").style.display = "none";
			document.getElementById("zzms").style.display = "none";
			$("#head_2").addClass("li_style");
			$("#head_1").removeClass("li_style");
			$("#head_3").removeClass("li_style");
			$("#head_4").removeClass("li_style");
			window.clearInterval(time1);
			window.clearInterval(time2);
		} else if (model == 3) {
			//控制状态

			if (modbus_id == 0) {
				document.getElementById("ztkz_sk").style.display = "block";
				document.getElementById("ztkz_qgl").style.display = "none";
			} else if (modbus_id == 1) {
				document.getElementById("ztkz_qgl").style.display = "block";
				document.getElementById("ztkz_sk").style.display = "none";
			}
			document.getElementById("csgd_sk").style.display = "none";
			document.getElementById("fcmscsdd_sk").style.display = "none";

			document.getElementById("csgd_qgl").style.display = "none";
			document.getElementById("fcmscsdd_qgl").style.display = "none";
			document.getElementById("zzms").style.display = "none";
			showztkz();
			window.clearInterval(time2);
		 //定时刷新
			time1=window.setInterval(showztkz, 1000); 
			$("#head_3").addClass("li_style");
			$("#head_1").removeClass("li_style");
			$("#head_2").removeClass("li_style");
			$("#head_4").removeClass("li_style");

		} else if (model == 4) {
			//控制模式
			document.getElementById("csgd_sk").style.display = "none";
			document.getElementById("fcmscsdd_sk").style.display = "none";
			document.getElementById("ztkz_sk").style.display = "none";
			document.getElementById("csgd_qgl").style.display = "none";
			document.getElementById("fcmscsdd_qgl").style.display = "none";
			document.getElementById("ztkz_qgl").style.display = "none";
			document.getElementById("zzms").style.display = "block";
			showalert();
			//关闭前面的刷新
			window.clearInterval(time1);
           //定时刷新
		    time2= window.setInterval(showalert, 1000); 
			$("#head_4").addClass("li_style");
			$("#head_1").removeClass("li_style");
			$("#head_2").removeClass("li_style");
			$("#head_3").removeClass("li_style");
		}
		 $("#W_width").show();
		$("#loading").hide();  
	}

	//参数给定指令发送
	function setcsgd(type) {
		/* debugger; */
		var modbus_id = $("#modbusid").val();
		var code_one = "";
		var flag=true;
		if (type == "wc") {
			if(modbus_id==0){
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
				
				   code_one = "008EH"
					var val = $("#wc_selectid_sk").val();
					code_one = code_one + "," + val
					var value = $("#wc_value_sk").val();
					code_one = code_one + "," + value
					sendcode("0000", code_one,1);
			}else if(modbus_id==1){
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
						code_one = ""
						var val = $("#wc_selectid_qgl").val();
						code_one =  val
						var value = $("#wc_value_qgl").val();
						code_one = code_one + "," + value
						//13,14  进行16进制转换
						sendcode("000D", code_one,1);
			}
			

		} else if (type == "jc") {
			if(modbus_id==0){
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
					code_one = "008FH"
					var val = $("#jc_selectid_sk").val();
					code_one = code_one + "," + val
					var value = $("#jc_value_sk").val();
					code_one = code_one + "," + value
					sendcode("0000", code_one,1);
			}else if(modbus_id==1){
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
				    code_one = ""
					var val = $("#jc_selectid_qgl").val();
					code_one =  val
					var value = $("#jc_value_qgl").val();
					code_one = code_one + "," + value
					//16,17地址
					sendcode("0010", code_one,1);
			}
		

		}
		$("#codeid").attr("value", code_one);
		
	}

	//风场参数给定
	function setfcms(type) {
		//全功率没有控制代码只有参数代码
		/* debugger; */
		var modbus_id = $("#modbusid").val();
		var code_one = "";
		var flag=true;
		if (type == "wc") {
			if(modbus_id==0){
					
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
				
				    code_one = "00A2H"
					var wg_value = $("#wg_value_sk").val();
					var wdy_value = $("#wdy_value_sk").val();
					var zl_value = $("#zl_value_sk").val();
						//修改指令发送
						code_one=code_one+",11,21,31,"+wg_value+","+wdy_value+","+zl_value
					//确定指令
					sendcode("0000", code_one,1);
			}else if(modbus_id==1){
				
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
				
				var wg_value = $("#wg_value_qgl").val();
				var wdy_value = $("#wdy_value_qgl").val();
				var zl_value = $("#zl_value_qgl").val();
				//if (wg_value > 0) {
					sendcode("0004", wg_value,1);
				//}
				//if (wdy_value > 0) {
					sendcode("0005", wdy_value,1);
				//}
				//if (zl_value > 0) {
					sendcode("0006", zl_value,1);
				//}
			}
			
			

		} else if (type == "jc") {
			if(modbus_id==0){
				
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
				
				code_one = "00A3H"
				    /*  debugger; */
					var CS1 = $("#jc_sk_cs1").val();
				    var CS2 = $("#jc_sk_cs2").val();
			     	var CS3 =61;
					var zj_value = $("#zj_value_sk").val();
					var wgdl_value = $("#wgdl_value_sk").val();
					var wlyw_value = $("#wlyw_value_sk").val();
					code_one=code_one+","+CS1+","+CS2+","+CS3+","+zj_value+","+wgdl_value+","+wlyw_value;
				//确定指令
					sendcode("000A", code_one,1);
			}else if(modbus_id==1){
				
				$.ajax({
					async: false, 
					type:'post',
					url:'<%=basePath%>DeviceTest/selectMenu_writpP.do',
					cache : "false",
					//data:'',
					success : function(data) {
						var result = JSON.parse(data);
						var msg = result.message;
						if (msg == "获取成功") {
							var write_p = result.responseData.write_p;
							if (write_p != 0) {
								flag = false;
							} else {
								flag = true;
							}
						} else {
							alert(msg);
						}
					}
				});
				if (flag == false) {
					alert("没有该权限");
					return false;
				}

				var zj_value = $("#zj_value_qgl").val();
				var wgdl_value = $("#wgdl_value_qgl").val();
				var wlyw_value = $("#wlyw_value_qgl").val();
				//	if (zj_value > 0) {
				sendcode("0007", zj_value, 1);
				//	}
				//	if (wgdl_value > 0) {
				sendcode("0008", wgdl_value, 1);
				//	}
				//	if (wlyw_value > 0) {
				sendcode("0009", wlyw_value, 1);
				//	}
			}

		}
	}
	function autoHeight() {
		var autoheight = $(window).height() - 128;
		$("#W_width").attr(
				"style",
				"min-height:" + autoheight + "px;max-height:" + autoheight
						+ "px;overflow: auto;");
	}
</script>
</head>

<body style="overflow: hidden">
	<input type="hidden" value="${param.device_id}" id="inid">
	<input type="hidden" value="${param.modbus_type}" id="modbusid">
	<input type="hidden" value="" id="codeid">
	<input type="hidden" id="write_p" />
	<!-- 头部内容home -->
	<div class="hd_box">
		<div class="logo_box">
			<a href="#"><img src="<%=basePath%>/images/logo.png"></a>
		</div>
		<div class="nav_box">
			<ul>
				<li><a href="javascript:js_method(1);"><i><strong
							class="S_10"></strong></i><span>系统信息</span></a></li>
				<li><a href="javascript:js_method(2);"><i><strong
							class="S_11"></strong></i><span>参数监控</span></a></li>
				<li><a href="javascript:js_method(3);"><i><strong
							class="S_12"></strong></i><span>示波器</span></a></li>
				<li class="li_icon"><a href="javascript:js_method(4);"><i><strong
							class="S_13"></strong></i><span>调试面板</span></a></li>
				<li class="style_li"><a href="<%=basePath%>jsp/index.jsp"><i><strong
							class="S_14"></strong></i><span>首页</span></a></li>
			</ul>
		</div>
	</div>
	<!-- 头部内容end -->
	<div id="loading" style="margin: 300px 600px;"></div>
	<div class="main_box clearfix">
		<div class="right_main_div W_90">
			<div class="W_width clearfix" id="W_width">
				<!-- 头部切换标签 -->
				<div class="main_title_box ">
					<ul class="text_box">
						<li id="head_1" onclick="getmodel(1)" class="li_style"><a
							href="javascript:void();">参数给定</a></li>
						<li id="head_2" onclick="getmodel(2)"><a
							href="javascript:void();">风场模式参数给定</a></li>
						<li id="head_3" onclick="getmodel(3)"><a
							href="javascript:void();">状态控制</a></li>
						<li id="head_4" onclick="getmodel(4)"><a
							href="javascript:void();">控制模式</a></li>
					</ul>
				</div>
				<!-- 头部切换标签end -->
				<!-- 风场模式参数给定:双馈-->
				<div class="table_box" id="fcmscsdd_sk" style="display: none;">
					<!-- 网侧参数 -->
					<div class="w_five">
						<p class="P_Label clearfix">网侧参数</p>

						<ul class="P_Label01 clearfix input_style ul_a">
							<li><span>无功电流</span> <input style="width: 40% !important;"
								id="wg_value_sk" type="text" value="0"></li>
							<li><span>未定义</span> <input style="width: 40% !important;"
								id="wdy_value_sk" type="text" value="0"></li>
							<li><span>直流电压</span> <input style="width: 40% !important;"
								id="zl_value_sk" type="text" value="0"></li>
						</ul>

					</div>
					<!-- 网侧参数end -->

					<!-- 机侧参数 -->
					<div class="w_five">
						<p class="P_Label clearfix">机侧参数</p>
						<ul class="P_Label01 clearfix input_style ul_a">
							<li><span><select id="jc_sk_cs1"><option
											value="41">转矩给定</option>
										<option value="42">有功电流</option></select></span> <input
								style="width: 40% !important;" id="zj_value_sk" type="text"
								value="0"></li>

							<!-- 	<li><span>转矩给定</span> <input style="width: 40% !important;"
								id="zj_value_sk" type="text" value="0"></li> -->

							<li><span><select id="jc_sk_cs2"><option
											value="51">无功电流</option>
										<option value="52">无功功率</option>
										<option value="53">功率因素</option>
								</select></span> <input style="width: 40% !important;" id="wgdl_value_sk"
								type="text" value="0"></li>
							<!-- <li><span>无功电流</span> <input style="width: 40% !important;"
								id="wgdl_value_sk" type="text" value="10"></li> -->
							<li><span>未定义</span> <input style="width: 40% !important;"
								id="wlyw_value_sk" type="text" value="0"></li>
						</ul>
					</div>
					<div class="clearfix"></div>

					<div>
						<a class="A_button"
							style="margin-left: 200px; margin-top: 10px; position: absolute;"
							href="javascript:void();" onclick="setfcms('wc')">确定</a><a
							class="A_button"
							style="margin-left: 800px; margin-top: 1px; position: absolute;"
							href="javascript:void();" onclick="setfcms('jc')">确定</a>
					</div>

					<!-- 机侧参数end -->
				</div>
				<!-- 风场模式参数给定:全功率-->
				<div class="table_box" id="fcmscsdd_qgl" style="display: none;">
					<!-- 网侧参数 -->
					<div class="w_five">
						<p class="P_Label clearfix">网侧参数</p>

						<ul class="P_Label01 clearfix input_style ul_a">
							<li><span>网侧无功电流</span> <input
								style="width: 40% !important;" id="wg_value_qgl" type="text"
								value="0"></li>
							<li><span>网侧功率因素</span> <input
								style="width: 40% !important;" id="wdy_value_qgl" type="text"
								value="0"></li>
							<li><span>直流电压补偿</span> <input
								style="width: 40% !important;" id="zl_value_qgl" type="text"
								value="0"></li>
						</ul>

					</div>
					<!-- 网侧参数end -->

					<!-- 机侧参数 -->
					<div class="w_five">
						<p class="P_Label clearfix">机侧参数</p>
						<ul class="P_Label01 clearfix input_style ul_a">
							<li><span>转矩给定</span> <input style="width: 40% !important;"
								id="zj_value_qgl" type="text" value="0"></li>
							<li><span>电流补偿</span> <input style="width: 40% !important;"
								id="wgdl_value_qgl" type="text" value="0"></li>
							<li><span>角度补偿</span> <input style="width: 40% !important;"
								id="wlyw_value_qgl" type="text" value="0"></li>
						</ul>
					</div>
					<div class="clearfix"></div>

					<div>
						<a class="A_button"
							style="margin-left: 200px; margin-top: 10px; position: absolute;"
							href="javascript:void();" onclick="setfcms('wc')">确定</a><a
							class="A_button"
							style="margin-left: 800px; margin-top: 1px; position: absolute;"
							href="javascript:void();" onclick="setfcms('jc')">确定</a>
					</div>

					<!-- 机侧参数end -->
				</div>
				<!-- 状态 控制:双馈-->
				<div class="table_box" id="ztkz_sk"
					style="display: none; min-height: 420px; max-height: 580px; overflow: auto;">
					<div class="w_100">
						<div class="title_A">状态</div>
						<ul class="UL_A clearfix" id="modestate_id_sk">
							<!-- <li class="listyle02"><a>启动请求</a></li>
						<li class="listyle02"><a class="abj">网侧禁止</a></li> -->
							<!-- 动态数据加载-->
						</ul>
					</div>
					<div class="w_100">
						<div class="title_A">控制</div>
						<ul class="UL_A clearfix">
							<li class="mskz" title="0071H" onclick="sendcodereq(this);"><a>启动</a></li>
							<li class="mskz" title="009EH" onclick="sendcodereq(this);"><a>励磁</a></li>
							<li class="mskz" title="0072H" onclick="sendcodereq(this);"><a>加载</a></li>
							<li class="mskz" title="0075H" onclick="sendcodereq(this);"><a>机侧停机</a></li>
							<li class="mskz" title="0073H" onclick="sendcodereq(this);"><a>停机</a></li>
							<li class="mskz" title="0074H" onclick="sendcodereq(this);"><a>复位</a></li>
							<li class="mskz" title="00A8H" onclick="sendcodereq(this);"><a>Chopper测试</a></li>
							<li class="mskz" title="00A9H" onclick="sendcodereq(this);"><a>Crowbar测试</a></li>
						</ul>
					</div>
				</div>
				<!-- 状态 控制:全功率-->
				<div class="table_box" id="ztkz_qgl"
					style="display: none; min-height: 420px; max-height: 580px; overflow: auto;">
					<div class="w_100">
						<div class="title_A">状态</div>
						<ul class="UL_A clearfix" id="modestate_id_qgl">
							<!-- <li class="listyle02"><a>启动请求</a></li>
						<li class="listyle02"><a class="abj">网侧禁止</a></li> -->
							<!-- 动态数据加载-->
						</ul>
					</div>
					<div class="w_100">
						<div class="title_A">控制</div>
						<ul class="UL_A clearfix">
							<li class="mskz" title="0071H" onclick="sendcodereq(this);"><a>启动</a></li>
							<li class="mskz" title="0072H" onclick="sendcodereq(this);"><a>加载</a></li>
							<li class="mskz" title="0073H" onclick="sendcodereq(this);"><a>停机</a></li>
							<li class="mskz" title="0074H" onclick="sendcodereq(this);"><a>复位</a></li>
							<li class="mskz" title="009DH" onclick="sendcodereq(this);"><a>直流放电</a></li>
							<li class="mskz" title="0075H" onclick="sendcodereq(this);"><a>机侧停机</a></li>
						</ul>
					</div>
				</div>
				<!-- 控制模式 :双馈，全功率-->
				<div class="table_box" id="zzms"
					style="display: none; min-height: 420px; max-height: 580px; overflow: auto;">

					<div class="w_100">
						<div class="title_A">功能模式设置</div>
						<ul class="UL_A clearfix" id="control_id">
							<!-- <li class="listyle"><a>本地</a></li> -->

						</ul>
					</div>
					<!-- 功能模式设置内容end -->
					<!-- 运行模式设置 -->
					<div class="w_100">
						<div class="title_A">运行模式设置</div>
						<ul class="UL_A clearfix" id="run_model">
							<li style="color: #ffffff;" class="listyle01"><a>开环模式</a></li>
							<li class="listyle01"><a>闭环模式</a></li>
							<li class="listyle01"><a>前溃模式</a></li>
							<li class="listyle01"><a>整流并网模式</a></li>
							<li class="listyle01"><a>风场运行模式</a></li>

						</ul>
						<div class="A_style clearfix">
							<a class="A_button01" title="007EH" href="javascript:void()"
								onclick="sendcodereq(this);">模式选择切换</a> &nbsp; &nbsp; &nbsp; <a
								style="margin-left: 5%;" class="A_button01"
								href="javascript:void()" onclick="sendcodereq(this);"
								title="007AH">模式设置确认</a>
						</div>
					</div>
					<!-- 运行模式设置end -->

				</div>
				<!-- 参数给定 :双馈-->
				<div class="table_box" id="csgd_sk" style="display: block;">
					<!-- 网侧参数 -->
					<div class="w_five">
						<p class="P_Label clearfix">网侧参数</p>
						<p class="P_Label01 clearfix input_style">
							<span
								style="background: #ffffff !important; border: none !important; padding-right: 0 !important; margin-right: 0 !important;">
								参数名称</span> <select id="wc_selectid_sk"
								style="margin-top: 4px; width: 30% !important" class="stronta">
								<option value="1" onclick="picks('测试一')"
									onMouseOver="bgcolor('a1')" onMouseOut="nocolor('a1')"
									class="cur">开环占空比</option>
								<option value="2" onclick="picks('测试一')"
									onMouseOver="bgcolor('a1')" onMouseOut="nocolor('a1')"
									class="cur">开环频率</option>
								<option value="3" onclick="picks('测试一')"
									onMouseOver="bgcolor('a1')" onMouseOut="nocolor('a1')"
									class="cur">闭环无功电流</option>
								<option value="4" onclick="picks('测试一')"
									onMouseOver="bgcolor('a1')" onMouseOut="nocolor('a1')"
									class="cur">闭环频率</option>
								<option value="5" onclick="picks('测试一')"
									onMouseOver="bgcolor('a1')" onMouseOut="nocolor('a1')"
									class="cur">前馈角度补偿</option>
								<option value="6" onclick="picks('测试一')"
									onMouseOver="bgcolor('a1')" onMouseOut="nocolor('a1')"
									class="cur">前馈幅值补偿</option>
								<option value="7" onclick="picks('测试一')"
									onMouseOver="bgcolor('a1')" onMouseOut="nocolor('a1')"
									class="cur">整流器无功电流</option>
								<option value="8" onclick="picks('测试一')"
									onMouseOver="bgcolor('a1')" onMouseOut="nocolor('a1')"
									class="cur">整流器直流电压补偿</option>

							</select>
							<!-- select 下拉菜单 -->
							参数值 <input style="width: 20% !important;" type="text"
								id="wc_value_sk" value="0">

						</p>
					</div>

					<!-- 机侧参数 -->
					<div class="w_five">
						<p class="P_Label clearfix">机侧参数</p>
						<p class="P_Label01 clearfix input_style">
							<span
								style="background: #ffffff !important; border: none !important; padding-right: 0 !important; margin-right: 0 !important;">
								参数名称</span>
							<!-- select 下拉菜单 -->
							<select id="jc_selectid_sk"
								style="margin-top: 4px; width: 30% !important" class="stronta">
								<option value="1" onclick="picks('测试一')"
									onMouseOver="bgcolor('a1')" onMouseOut="nocolor('a1')"
									class="cur">开环占空比</option>
								<option value="2" onclick="picks('测试一')"
									onMouseOver="bgcolor('a1')" onMouseOut="nocolor('a1')"
									class="cur">开环频率</option>
								<option value="3" onclick="picks('测试一')"
									onMouseOver="bgcolor('a1')" onMouseOut="nocolor('a1')"
									class="cur">闭环无功电流</option>
								<option value="4" onclick="picks('测试一')"
									onMouseOver="bgcolor('a1')" onMouseOut="nocolor('a1')"
									class="cur">闭环频率</option>
								<option value="5" onclick="picks('测试一')"
									onMouseOver="bgcolor('a1')" onMouseOut="nocolor('a1')"
									class="cur">前馈角度补偿</option>
								<option value="6" onclick="picks('测试一')"
									onMouseOver="bgcolor('a1')" onMouseOut="nocolor('a1')"
									class="cur">前馈幅值补偿</option>
								<option value="7" onclick="picks('测试一')"
									onMouseOver="bgcolor('a1')" onMouseOut="nocolor('a1')"
									class="cur">整流器有功电流</option>
								<option value="8" onclick="picks('测试一')"
									onMouseOver="bgcolor('a1')" onMouseOut="nocolor('a1')"
									class="cur">整流器无功电流</option>

							</select>
							<!-- select 下拉菜单 -->
							参数值 <input style="width: 20% !important;" id="jc_value_sk"
								type="text" value="0">

						</p>
					</div>
					<!-- 机侧参数end -->
					<div class="clearfix"></div>

					<div>
						<a class="A_button"
							style="margin-left: 200px; margin-top: 10px; position: absolute;"
							href="javascript:void();" onclick="setcsgd('wc')">确定</a><a
							class="A_button"
							style="margin-left: 800px; margin-top: 1px; position: absolute;"
							href="javascript:void();" onclick="setcsgd('jc')">确定</a>
					</div>



				</div>
				<!-- 参数给定 :全功率-->
				<div class="table_box" id="csgd_qgl" style="display: none;">
					<!-- 网侧参数 -->
					<div class="w_five">
						<p class="P_Label clearfix">网侧参数</p>
						<p class="P_Label01 clearfix input_style">
							<span
								style="background: #ffffff !important; border: none !important; padding-right: 0 !important; margin-right: 0 !important;">
								参数名称</span> <select id="wc_selectid_qgl"
								style="margin-top: 4px; width: 30% !important" class="stronta">
								<option value="1" onclick="picks('测试一')"
									onMouseOver="bgcolor('a1')" onMouseOut="nocolor('a1')"
									class="cur">开环占空比</option>
								<option value="3" onclick="picks('测试一')"
									onMouseOver="bgcolor('a1')" onMouseOut="nocolor('a1')"
									class="cur">开环频率</option>
								<option value="5" onclick="picks('测试一')"
									onMouseOver="bgcolor('a1')" onMouseOut="nocolor('a1')"
									class="cur">闭环无功电流</option>
								<option value="7" onclick="picks('测试一')"
									onMouseOver="bgcolor('a1')" onMouseOut="nocolor('a1')"
									class="cur">闭环频率</option>
								<option value="9" onclick="picks('测试一')"
									onMouseOver="bgcolor('a1')" onMouseOut="nocolor('a1')"
									class="cur">直流电压利用系数</option>
								<option value="11" onclick="picks('测试一')"
									onMouseOver="bgcolor('a1')" onMouseOut="nocolor('a1')"
									class="cur">电压滤波补偿角度</option>
								<option value="13" onclick="picks('测试一')"
									onMouseOver="bgcolor('a1')" onMouseOut="nocolor('a1')"
									class="cur">整流器无功电流</option>
								<option value="15" onclick="picks('测试一')"
									onMouseOver="bgcolor('a1')" onMouseOut="nocolor('a1')"
									class="cur">直流电压给定</option>

							</select>
							<!-- select 下拉菜单 -->
							参数值 <input style="width: 20% !important;" type="text"
								id="wc_value_qgl" value="0">

						</p>
					</div>

					<!-- 机侧参数 -->
					<div class="w_five">
						<p class="P_Label clearfix">机侧参数</p>
						<p class="P_Label01 clearfix input_style">
							<span
								style="background: #ffffff !important; border: none !important; padding-right: 0 !important; margin-right: 0 !important;">
								参数名称</span>
							<!-- select 下拉菜单 -->
							<select id="jc_selectid_qgl"
								style="margin-top: 4px; width: 30% !important" class="stronta">
								<option value="2" onclick="picks('测试一')"
									onMouseOver="bgcolor('a1')" onMouseOut="nocolor('a1')"
									class="cur">开环占空比</option>
								<option value="4" onclick="picks('测试一')"
									onMouseOver="bgcolor('a1')" onMouseOut="nocolor('a1')"
									class="cur">开环频率</option>
								<option value="6" onclick="picks('测试一')"
									onMouseOver="bgcolor('a1')" onMouseOut="nocolor('a1')"
									class="cur">闭环无功电流</option>
								<option value="8" onclick="picks('测试一')"
									onMouseOver="bgcolor('a1')" onMouseOut="nocolor('a1')"
									class="cur">闭环频率</option>
								<option value="10" onclick="picks('测试一')"
									onMouseOver="bgcolor('a1')" onMouseOut="nocolor('a1')"
									class="cur">直流电压利用系数</option>
								<option value="12" onclick="picks('测试一')"
									onMouseOver="bgcolor('a1')" onMouseOut="nocolor('a1')"
									class="cur">电压滤波补偿系数</option>
								<option value="14" onclick="picks('测试一')"
									onMouseOver="bgcolor('a1')" onMouseOut="nocolor('a1')"
									class="cur">有功电流给定</option>
								<option value="16" onclick="picks('测试一')"
									onMouseOver="bgcolor('a1')" onMouseOut="nocolor('a1')"
									class="cur">无功电流给定</option>

							</select>
							<!-- select 下拉菜单 -->
							参数值 <input style="width: 20% !important;" id="jc_value_qgl"
								type="text" value="0">

						</p>
					</div>
					<!-- 机侧参数end -->
					<div class="clearfix"></div>

					<div>
						<a class="A_button"
							style="margin-left: 200px; margin-top: 10px; position: absolute;"
							href="javascript:void();" onclick="setcsgd('wc')">确定</a><a
							class="A_button"
							style="margin-left: 800px; margin-top: 1px; position: absolute;"
							href="javascript:void();" onclick="setcsgd('jc')">确定</a>
					</div>



				</div>

			</div>
		</div>
	</div>
</body>

</html>
