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
<title>调试面板——海上风电</title>
<link rel="stylesheet" type="text/css"
	href="<%=basePath%>/css/common.css">
<link rel="stylesheet" type="text/css"
	href="<%=basePath%>/css/style.css">

<!-- 共同引用jquery js -->
<script type="text/javascript" src="<%=basePath%>/js/jquery1124.js"></script>
<script type="text/javascript" src="<%=basePath%>/js/js.js"></script>
<!-- 共同引用jquery js -->
<script type="text/javascript">
 window.onload=function(){
	//debugger;
	 var urlstr=window.location.href;
	 var device=urlstr.split("?")[1].split("&")[0].split("=")[1]
	 var modbus_type=urlstr.split("?")[1].split("&")[1].split("=")[1]
	 $("#inid").attr("value",device);
	 $("#modbusid").attr("value",modbus_type);
	/*   showalert();
	  showztkz(); */
	 getmodel(1);
	 autoHeight();
	}


//window.setInterval(showztkz, 1000); 
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
					//debugger;
					//故障数据
					var ControlModel = result.responseData.ControlModel;
					if (ControlModel.length > 0) {
						var controlobj= $("#control_id");
						//功能模式设置
						  $("#control_id").html("");
			          for(var i=0;i<ControlModel.length;i++){
			        	 //功能模式设置中循环添加数据
						var listr="<li class='mskz'  onclick='sendcodereq(this);' title='00"+ControlModel[i].split(":")[2]+"'><a>"+ControlModel[i].split(":")[0]+"</a></li>";
						controlobj.append(listr);
						var listr="<li class='listyle' >"+ControlModel[i].split(":")[1]+"</li>";
						controlobj.append(listr); 
						}
					}else{
	                            $("#control_id").html("");
								var str = "<div style='text-align:center';>没有相关内容</div>";
								$("#control_id").append(str);
						}
					
					//debugger;
					var ControlModel = result.responseData.modellist;
					if (ControlModel.length > 0) {
						var controlobj= $("#run_model");
						  $("#run_model").html("");
			          for(var i=0;i<ControlModel.length;i++){
			        	  var listr="";
			        	  
			        	  var one=ControlModel[i].split(",");
			        	  var name=one[1];
			        	  if(one[1]=="整流器模式"){
			        		  name="整流并网模式";
			        	  }
			        	  
			        	  if(one[1]=="风场运行"){
			        		  name="风场运行模式";
			        	  }
			        	  if(one[2]==1){
			        		  listr="<li    class='listyle' style=' background: #488fd2;'>"+name+"</li>";  
			        	  }else{
			        		  listr="<li class='listyle' >"+name+"</li>";  
			        	  }
						      controlobj.append(listr);
						}
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
					//debugger;
					var ControlModel = result.responseData.ControlModel;
					var controlobj= $("#modestate_id");
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
					},
					error:function (e){
					  alert("数据获取失败！");
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
	 code= obj.title;
	 $("#codeid").attr("value",code);
	 var device_id=$("#inid").val();
	 $.ajax({
		   	type:"post",
		   	url:"<%=basePath%>DeviceTest/setControlCodeReq.do",
			data : {
				"device_id" : device_id
			},
			success : function(data) {
				var result = JSON.parse(data);
				var msg = result.code;
				if (msg == "1001") {
					//debugger;
					gtecodereq();
					// timer1=window.setInterval(gtecodereq,1000);
					//故障数据
					//alert("请求写指令发送成功");
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
		   	type:"post",
		   	url:"<%=basePath%>DeviceTest/getControlCodeReq.do",
			data : {
				"device_id" : device_id
			},
			success : function(data) {
				var result = JSON.parse(data);
				var msg = result.code;
				if (msg == "1001"){
					var Device = result.responseData.Device;
					/* debugger; */
					var req=Device.rw_role_req;
					var res=Device.rw_role_res;
					if(req=="W"&&res=="OK"){
						//地址12789
						sendcode("31f4",code,1);	
						//clearInterval(timer1);
					}else if(req=="R"&&res=="OK"){
						sendcodereq();
					}else{
						sendcode("31f4",code,1);	
						//sendcode(code);	
						//learInterval(timer1)
					}
					//故障数据
					//alert("请求写指令发送成功");
				}else{
					alert("请求写指令发送失败");
				}
				}
		});
 }
 function sendcode(addr,code,type){
	 //debugger;
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
				if (msg == "1001") {
					//debugger;
					//故障数据
					//alert("写指令成功");
					
				}else{
					alert("写指令失败");
				}
				}
				});

	}

 function js_method(type){
		//alert(1);
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
		//debugger
		 var urlstr=window.location.href;
		 var device=urlstr.split("?")[1].split("&")[0].split("=")[1]
		 if (model == 1) {
	      	document.getElementById("fcmscsdd").style.display = "block";

			document.getElementById("ztkz").style.display = "none";
			document.getElementById("zzms").style.display = "none";
			$("#head_1").addClass("li_style");
			$("#head_3").removeClass("li_style");
			$("#head_4").removeClass("li_style");
			window.clearInterval(time1);
			window.clearInterval(time2);
		} else if (model == 3) {

			document.getElementById("ztkz").style.display = "block";
			document.getElementById("fcmscsdd").style.display = "none";
			document.getElementById("zzms").style.display = "none";
			showztkz();
			window.clearInterval(time2);
		   time1=window.setInterval(showztkz, 1000); 
			$("#head_3").addClass("li_style");
			$("#head_1").removeClass("li_style");
			$("#head_4").removeClass("li_style");

		} else if (model == 4) {
			//控制模式
			document.getElementById("fcmscsdd").style.display = "none";
			document.getElementById("ztkz").style.display = "none";
			document.getElementById("zzms").style.display = "block";
			showalert();
			window.clearInterval(time1);

			time2= window.setInterval(showalert, 1000); 
			$("#head_4").addClass("li_style");
			$("#head_1").removeClass("li_style");
			$("#head_3").removeClass("li_style");
		}
	}


	//风场参数给定
	function setfcms(type) {
	/* 	网侧参数输入框:
			网侧Iq(A)			对应协议地址12791
			网侧Id(A)			对应协议地址12792
			Udc(V)				对应协议地址12793
			网侧”确定”按钮			没有控制代码,按钮作为触发条件而已

			机侧参数输入框:
			机侧Iq(A)			对应协议地址12799
			机侧Id(A)			对应协议地址12800
			转矩(Nm)			        对应协议地址12802
			机侧”确定”按钮			没有控制代码,按钮作为触发条件而已 */

		//全功率没有控制代码只有参数代码
		//debugger;
		var code_one = "";
		var flag=true;
		if (type == "wc") {
			
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
			
				var wg_value = $("#wg_value_hsfd").val(); //12791
				var wdy_value = $("#wdy_value_hsfd").val();
				var zl_value = $("#zl_value_hsfd").val();
				//if (wg_value > 0) {
					sendcode("31f6", wg_value,1);
			//	}
				//if (wdy_value > 0) {
					sendcode("31f7", wdy_value,1);
				//}
				//if (zl_value > 0) {
					sendcode("31f8", zl_value,1);
				//}
		} else if (type == "jc") {
			
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

			var zj_value = $("#zj_value_hsfd").val();
			var wgdl_value = $("#wgdl_value_hsfd").val();
			var wlyw_value = $("#wlyw_value_hsfd").val();
			//;if (zj_value > 0) {
			sendcode("31fe", zj_value, 1);
			//}
			//if (wgdl_value > 0) {
			sendcode("31ff", wgdl_value, 1);
			//	}
			//if (wlyw_value > 0) {
			sendcode("3201", wlyw_value, 1);
			//	}
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
			<hr />
		</div>
		<hr />
	</div>
	<!-- 头部内容end -->
<!-- 	<div class="main_box clearfix">
		<div class="right_main_div W_90"> -->
			<div class="W_width clearfix" id="W_width">
				<!-- 头部切换标签 -->
				<div class="main_title_box ">
					<ul class="text_box">
						<li id="head_1" onclick="getmodel(1)" class="li_style"><a
							href="javascript:void();">参数给定</a></li>
						<li id="head_3" onclick="getmodel(3)"><a
							href="javascript:void();">状态控制</a></li>
						<li id="head_4" onclick="getmodel(4)"><a
							href="javascript:void();">控制模式</a></li>
					</ul>
				</div>
				<!-- 头部切换标签end -->
				<!-- 风场模式参数给定:海上风电-->
				<div class="table_box" id="fcmscsdd" style="display: block;">
					<!-- 网侧参数 -->
					<div class="w_five">
						<p class="P_Label clearfix">网侧参数</p>

						<ul class="P_Label01 clearfix input_style ul_a">
							<li><span>网侧Iq(A)</span> <input
								style="width: 40% !important;" id="wg_value_hsfd" type="text"
								value="0"></li>
							<li><span>网侧Id(A)</span> <input
								style="width: 40% !important;" id="wdy_value_hsfd" type="text"
								value="0"></li>
							<li><span>Udc(V)</span> <input
								style="width: 40% !important;" id="zl_value_hsfd" type="text"
								value="0"></li>
						</ul>

					</div>
					<!-- 网侧参数end -->

					<!-- 机侧参数 -->
					<div class="w_five">
						<p class="P_Label clearfix">机侧参数</p>
						<ul class="P_Label01 clearfix input_style ul_a">
							<li><span>机侧Iq(A)</span> <input
								style="width: 40% !important;" id="zj_value_hsfd" type="text"
								value="0"></li>
							<li><span>机侧Id(A)</span> <input
								style="width: 40% !important;" id="wgdl_value_hsfd" type="text"
								value="0"></li>
							<li><span>转矩(Nm)</span> <input
								style="width: 40% !important;" id="wlyw_value_hsfd" type="text"
								value="0"></li>
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
				<div class="table_box" id="ztkz"
					style="display: none; min-height: 380px; max-height: 580px; overflow: auto;">
					<div class="w_100">
						<div class="title_A">状态</div>
						<ul class="UL_A clearfix" id="modestate_id">

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
							<li class="mskz" title="0075H" onclick="sendcodereq(this);"><a>复位</a></li>
							<li class="mskz" title="0074H" onclick="sendcodereq(this);"><a>机侧停机</a></li>
							<li class="mskz" title="0076H" onclick="sendcodereq(this);"><a>PLC复位</a></li>
							<li class="mskz" title="0077H" onclick="sendcodereq(this);"><a>PLC关机</a></li>
							<li class="mskz" title="0078H" onclick="sendcodereq(this);"><a>PLC重启</a></li>

						</ul>
					</div>
				</div>

				<!-- 控制模式 :海上风电-->
				<div class="table_box" id="zzms"
					style="display: none; min-height: 380px; max-height: 580px; overflow: auto;">

					<div class="w_100">
						<div class="title_A">功能模式设置</div>
						<ul class="UL_A clearfix" id="control_id">
							<!-- 	<li class="listyle"><a>本地</a></li> -->

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
							<a class="A_button01" title="0079H" href="javascript:void()"
								onclick="sendcodereq(this);">模式选择切换</a> <a class="A_button01"
								href="javascript:void()" style="margin-left: 5%;"
								onclick="sendcodereq(this);" title="0089H">模式设置确认</a>
						</div>
					</div>
					<!-- 运行模式设置end -->

				</div>

			</div>
</body>

</html>
