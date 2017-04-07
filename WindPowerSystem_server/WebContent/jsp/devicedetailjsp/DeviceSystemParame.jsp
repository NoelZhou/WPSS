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
<link href="<%=basePath%>/favicon.ico" rel="shortcut icon" type="image/x-icon" />
<title>系统信息</title>
<link rel="stylesheet" type="text/css"
	href="<%=basePath%>/css/common.css">
<link rel="stylesheet" type="text/css"
	href="<%=basePath%>/css/style.css">

<!-- 共同引用jquery js -->
<script type="text/javascript" src="<%=basePath%>/js/jquery1124.js"></script>
<script type="text/javascript" src="<%=basePath%>/js/js.js"></script>
<script type="text/javascript" src="<%=basePath%>js/popup.js"></script>
<script type="text/javascript" src="<%=basePath%>js/opts.js"></script>
<script type="text/javascript" src="<%=basePath%>js/spin.js"></script>
<!-- 共同引用jquery js -->
<script type="text/javascript">
 window.onload=function(){
	//debugger;
	 var urlstr=window.location.href;
	 var device=urlstr.split("?")[1].split("&")[0].split("=")[1]
	 getdevicetype(device);
	 showalert();
	 //autoHeight();
	}


window.setInterval(showalert, 1000); 
//查看专家库
function getdevicetype(device_id){
	//debugger;
	$.ajax({
		type:'post',
		url:'<%=basePath%>DeviceTest/getDeviceType.do',
		data:{"device_id":device_id},
		async: false,
		success:function(data){
			//debugger;
			var result = JSON.parse(data);
			var msg = result.code;
			var modbustcp_type=result.responseData.modbustcp_type;
			var str="";
			if (msg == "1001"){
				//alert(modbustcp_type);
				 $("#modbusid").attr("value",modbustcp_type);
			}else{
				alert("数据获取失败!");
			}
		}});
}
 function showalert(){
	 
	 debugger;
	 $("#dw_li").hide();
	 $("#dj_li").hide();
	 $("#nb_li").hide();
	 $(".W_width").hide();
	 $("#loading").show();
	 var str = [1,3,5,7,8,9,10,11,12,13];
	 var random = str[Math.floor(Math.random()*str.length)];
	 var result = str[random];
		var device_id=$("#inid").val();
	 $.ajax({
		   	type:"post",
		   	url:"<%=basePath%>Devicetail/getSystemParame.do",
			data : {
				"device_id" : device_id
			},
			success : function(data) {
				var result = JSON.parse(data);
				var msg = result.code;
				//debugger;
				var modbusid=$("#modbusid").val();
				if(modbusid==0){
					document.getElementById("head_sk").style.display="block";
					document.getElementById("head_qgl").style.display="none";
					document.getElementById("head_hsfd").style.display="none";
				}else if(modbusid==1){
					if(msg == "1001"){
						var qgl_zc = result.responseData.qgl_zc;
						if(qgl_zc.length>1){
							//主柜
						   if(qgl_zc.substring(0,1)==1){
							   $("#zg_id").removeClass("i01");
							   $("#zg_id").addClass("i02");
						   }
						   //从柜
						   if(qgl_zc.substring(1,2)==1){
							   $("#cg_id").removeClass("i01");
							   $("#cg_id").addClass("i02");
							 
						   }
						 }
					}
					document.getElementById("head_sk").style.display="none";
					document.getElementById("head_qgl").style.display="block";
					document.getElementById("head_hsfd").style.display="none";
				}else{
					document.getElementById("head_sk").style.display="none";
					document.getElementById("head_qgl").style.display="none";
					document.getElementById("head_hsfd").style.display="block";
				}
				
				if (msg == "1001") {
					//38位值
					var errorValue38 = result.responseData.value38;
					//故障数据
					var errorlist = result.responseData.errorstr;
					var tbodyObj = document.getElementById("errortable");
					var rows= tbodyObj.rows;
					var tableleth=tbodyObj.rows.length-1;
					if(errorlist.length>0){
						debugger;
						//更新的数据没有更加和减少tr和查询的列相等
						if(tableleth==errorlist.length){
							//列相等不做操作
					   	} else if(tableleth>errorlist.length){
						    for(var i=0;i<(tableleth-errorlist.length);i++)
							   {
								   tbodyObj.deleteRow(tableleth-i);
						  	   }
					   	}else if(tableleth<errorlist.length){
					   		for(var i=tableleth;i<errorlist.length;i++){
								var r = tbodyObj.insertRow(); 
							 	var c = r.insertCell(); 
						        c.innerHTML=""; 
						        var c1 = r.insertCell(); 
						        c1.innerHTML=""; 
						        var c2 = r.insertCell(); 
						        c2.innerHTML=""; 
							   }
					   	}
						 for (var i = 0; i < errorlist.length; i++) {
								//设置故障告警，故障是否显示
								if(errorlist[i].indexOf("告警")>=0){
									$("#spid").remove();
									$("#aid").prepend(" <span id='spid'><i class='i_block01'></i>告警</span> ");
								}
								if(errorlist[i].indexOf("故障")>=0){
									$("#sid").remove();
									$("#aid").prepend("<span id='sid'><i class='i_block'></i>故障</span>" );
								}
								var error = errorlist[i];
								var errormore = error.split("&");
								var bitvar = errormore[4];
								//table下面的缩影行
						    	rows[i+1].cells[0].innerHTML= errormore[0];
						    	rows[i+1].cells[1].innerHTML=errormore[2] ;
						    	//添加事件记录参数
						    	rows[i+1].cells[2].innerHTML=errormore[0]+"%"+errormore[4] ;
						    	rows[i+1].cells[2].style.display="none";
						    	rows[i+1].ondblclick=function(){opendetail(this)}; 
						    	$('#errortable tbody tr:even').css("backgroundColor","#edf2f6");
						}
					}else{
						debugger;
						if($("#spid")!=undefined){
							$("#spid").remove();
						}
						if($("#sid")!=undefined){
							$("#sid").remove();
						}
						if(errorValue38!="0"){
							$("#sid").remove();
							$("#aid").prepend("<span id='sid'><i class='i_block'></i>故障</span>" );
						}
						for(var i=0;i<tableleth;i++)
						{
						   tbodyObj.deleteRow(tableleth-i);
					  	}
						for(var i=1;i<3;i++){
							var r = tbodyObj.tBodies[0].insertRow(); 
						 	var c = r.insertCell(); 
					        c.innerHTML=""; 
					        var c1 = r.insertCell(); 
					        c1.innerHTML=""; 
					        var c2 = r.insertCell(); 
					        c2.innerHTML="";
					        c2.style.display="none";
					        $('#errortable tbody tr:even').css("backgroundColor","#edf2f6");
						}
					}
				
					//基本参数    下边表格 数据1
					var dzparamelistgai = result.responseData.dzparamelistgai;
					if (dzparamelistgai.length > 0) {
						var tbodyObj = document.getElementById("dzgai_table");
						//var =$("#errortable");
						var rows= tbodyObj.rows;
						// var rows=tbodyObjdocum.rows;
						var tableleth=tbodyObj.rows.length-1;
						//更新的数据没有更加和减少tr和查询的列相等
						if(tableleth==dzparamelistgai.length){
						} else if(tableleth>dzparamelistgai.length){
						   for(var i=dzparamelistgai.length;i<tableleth;i++){
							   tbodyObj.deleteRow(rows[i]);
						   }
						}else{
						 	for(var i=tableleth;i<dzparamelistgai.length;i++){
								 var r = tbodyObj.insertRow(); 
							  	 var c = r.insertCell(); 
						         c.innerHTML="暂无主题列表"; 
						      	 var c1 = r.insertCell(); 
						       	 c1.innerHTML="暂无主题列表"; 
						         var c2 = r.insertCell(); 
						         c2.innerHTML="暂无主题列表"; 
						         $('#dzgai_table tbody tr:even').css("backgroundColor","#edf2f6");
						    }
						}
					}
					for (var i = 0; i < dzparamelistgai.length; i++) {
						var error = dzparamelistgai[i];
						var errormore = error.split(",");
						//table下面的缩影行
						// if(errormore[4]=="1"||errormore[4]==""){
								rows[i+1].cells[0].innerHTML= errormore[0];
							    rows[i+1].cells[1].innerHTML=errormore[1] ;
							    rows[i+1].cells[2].innerHTML= errormore[2];
							//}else{
								//当remark为2的时候把突然隐藏
							//} 
					}
					
					//中间变量监控		   下边表格 数据2
					var nbparamelistgai = result.responseData.nbparamelistgai;
					if (nbparamelistgai.length > 0) {
						var tbodyObj = document.getElementById("nbgai_table");
						//var =$("#errortable");
						var rows= tbodyObj.rows;
						// var rows=tbodyObjdocum.rows;
						var tableleth=tbodyObj.rows.length-1;
						//更新的数据没有更加和减少tr和查询的列相等
						if(tableleth==nbparamelistgai.length){
					   } else if(tableleth>nbparamelistgai.length){
						   for(var i=nbparamelistgai.length;i<tableleth;i++){
							   tbodyObj.deleteRow(rows[i]);
						   }
					   }else{
						 for(var i=tableleth;i<nbparamelistgai.length;i++){
							 var r = tbodyObj.insertRow(); 
							    var c = r.insertCell(); 
						        c.innerHTML="暂无主题列表"; 
						        var c1 = r.insertCell(); 
						        c1.innerHTML="暂无主题列表"; 
						        var c2 = r.insertCell(); 
						        c2.innerHTML="暂无主题列表"; 
						        $('#nbgai_table tbody tr:even').css("backgroundColor","#edf2f6");
						    }
					    }
					}
					for (var i = 0; i < nbparamelistgai.length; i++) {
						var error = nbparamelistgai[i];
						var errormore = error.split(",");
						//table下面的缩影行
						// if(errormore[4]=="1"||errormore[4]==""){
							 rows[0].cells[0].innerHTML= errormore[5];
								rows[i+1].cells[0].innerHTML= errormore[0];
							    rows[i+1].cells[1].innerHTML=errormore[1] ;
							    rows[i+1].cells[2].innerHTML= errormore[2];
							//}else{
								//当remark为2的时候把突然隐藏
							//} 
				 	}
				    
					//内部数据
					var nbparamelist = result.responseData.nbparamelist;
					if (nbparamelist.length > 0) {
						var tbodyObj = document.getElementById("nb_table");
						//var =$("#errortable");
						 var rows= tbodyObj.rows;
						// var rows=tbodyObjdocum.rows;
						var tableleth=tbodyObj.rows.length-1;
						//更新的数据没有更加和减少tr和查询的列相等
						if(tableleth==nbparamelist.length){
					   } else if(tableleth>nbparamelist.length){
						   for(var i=nbparamelist.length;i<tableleth;i++){
							   tbodyObj.deleteRow(rows[i]);
						   }
					   }else{
						 
						 for(var i=tableleth;i<nbparamelist.length;i++){
							 var r = tbodyObj.insertRow(); 
							    var c = r.insertCell(); 
						        c.innerHTML="暂无主题列表"; 
						        var c1 = r.insertCell(); 
						        c1.innerHTML="暂无主题列表"; 
						        var c2 = r.insertCell(); 
						        c2.innerHTML="暂无主题列表"; 
						    }
					    }
					}
					//debugger;
					for (var i = 0; i < nbparamelist.length; i++) {
						var error = nbparamelist[i];
						var errormore = error.split(",");
						var random = str[Math.floor(Math.random()*str.length)];
						rows[i+1].cells[0].innerHTML= errormore[0];
						rows[i+1].cells[1].innerHTML=errormore[1] ;
					    rows[i+1].cells[2].innerHTML= errormore[2]; 
					    $('#nb_table tbody tr:even').css("backgroundColor","#edf2f6");
				    	//填写头数据
				    	//debugger;
				    	var addr=errormore[3];
				    	if(modbusid==1){
					    	var obj= document.getElementById("qgl_"+addr);
					    	if(obj!=undefined){
					    		$("#qgl_"+addr).html(errormore[1]);
					         }
					     }else{
					    		var obj= document.getElementById(addr);
						    	if(obj!=undefined){
						    		$("#"+addr).html(errormore[1]);
						        }
					    			
					     }
					}
					var imgurl="<%=basePath%>"+"images/"+result.responseData.pararmimg;
					document.getElementById("parameimg").src=imgurl;
					document.getElementById("parameimg_qgl").src=imgurl;
					document.getElementById("parameimg_hsfd").src=imgurl;
					 
					$("#write_p").val(result.responseData.write_p);
					
					$('#dj_table tbody tr:even').css("backgroundColor","#edf2f6");
					$('#dw_table tbody tr:even').css("backgroundColor","#edf2f6");
					$('#nb_table tbody tr:even').css("backgroundColor","#edf2f6");
					autoHeight(modbusid);
			}else{
				alert("获取数据失败！");
			}},
			error : function(e) {
				//debugger;
		}
	});
	$(".W_width").show();
	$("#loading").hide(); 
 }
	function opendetail(errorstr) {
		debugger;
		var errorstr=errorstr.cells[2].innerHTML;
		var name=errorstr.split("%");
		var joneName=null;
		for(var j=0;j<name.length;j++){
			if(j==0){
				joneName=name[j];
				break;
			}
		}
		var errorstr=name[1];
		var bit = errorstr.split("|");
		$("#bit_table tbody").html("");
		var contStr1 = "<tbody>";
		for (var i = 0; i < bit.length; i++) {
		   // debugger;
			var value=bit[i];
			var valueone=value.split(",");
			var bitvalue=valueone[2];
			var one=1;
			if(bitvalue!=""){
				if(bitvalue!=one||valueone[1]!=""){
					contStr1 += "<tr >"
					contStr1 += "<td>"+(i+1)+"</td>"
					contStr1 += "<td>"+valueone[1]+"</td>"
					if(bitvalue==one){
						if(joneName.indexOf("系统告警")>=0){
							contStr1 +="<td><i class='i_a i_block01'></i></td>"
						}else{
							contStr1 +="<td><i class='i_a i_block'></i></td>"
						}
					}else{
						contStr1 +="<td><i class='i_a i_block02'></i></td>"
					}
					contStr1+="<td><a style='cursor:pointer;' onclick=Look('"+valueone[1]+"')>查看</a></td>"
					contStr1 += "</tr>";
				} 
			  }
			}
		contStr1 += "</tbody>";
		$("#bit_table").append(contStr1);

		$('#bit_table tbody tr:even').css("backgroundColor", "#edf2f6");
		document.getElementById("bit_div").style.display = "block";
		$("#gray").show();
		tc_center();
	}
	//查看专家库
	function Look(errormessage){
		var device_id=$("#inid").val();
		$.ajax({
			type:'post',
			url:'<%=basePath%>/ExpertDatabase/lookExpertDatabase.do',
			data:{"device_id":device_id,"errormessage":errormessage},
			success:function(data){
				var result = JSON.parse(data);
				var msg = result.code;
				var list=result.responseData.Expetrmessage;
				var str="";
				if (msg == "1001"){
					if(list.length==1){
						alert("专家库内容为空!");
						return false;
					}
					str+="<div class='popup poput_box_b Min_w tablee' id='expert'><p class='poput_title'><span>专家库内容 </span><a href='javascript:void(0)' onclick=closewindow()></a></p><div class='poput_box_height'>"
					+"<br/>&nbsp;<textarea style='border:1px solid #488fd2' cols=95 rows=10 readonly>"+list+"</textarea>"
					+"</div></div>";
					$("#expertDatabase").html(str);
					$("#bit_div").hide();
					tc_center();
				}else{
					alert("数据获取失败!");
				}
			}});
	}
	//关闭窗口
	function closewindow(){
		$("#expert").hide();
		$("#bit_div").show();
	}
	function closebit(){
		document.getElementById("bit_div").style.display = "none";
		$("#gray").hide();
	}
	function js_method(type){
		//alert(1);
		var device_id=$("#inid").val();
		var modbusid=$("#modbusid").val();
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
	
function restart(){
	 var write_p=$("#write_p").val();
		if(write_p!=0){
			alert("没有该权限！");
			return false;
		}
	sendcodereq();
	gtecodereq();
}

function sendcodereq(){
	 var device_id=$("#inid").val();
	 $.ajax({
		   	type:"post",
		   	async: false,
		   	url:"<%=basePath%>DeviceTest/setControlCodeReq.do",
			data : {
				"device_id" : device_id
			},
			success : function(data) {
				var result = JSON.parse(data);
				var msg = result.code;
				if (msg == "1001") {
				}else{
					alert("请求写指令发送失败");
				  }
				}
		});
}

function gtecodereq(){
	//debugger;
	var code="";
	var addr="";
	var device_id=$("#inid").val();
	 var modbusid=$("#modbusid").val();
	 if(modbusid==2){
		 code="0075H";
		 addr="31f4";
	  }else{
		 code="0074H" ; 
		 addr="0000";
	  }
	 $.ajax({
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
						sendcode(addr,code,1);	
					}else if(req=="SJ"&&res=="OK"){
						alert("设备正在升级中,无法写入数据!");
					}else{
						sendcodereq();
					}
					//故障数据
					//("请求写指令发送成功");
				}else{
					alert("请求写指令发送失败");
				}
				}
		});
}
function sendcode(addr,code,type){
	// debugger;
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
				//	alert("写指令成功");
					
				}else{
					alert("写指令失败");
				}
				}
				});

	}

function autoHeight(modbustype_id){
	var errortable_height = $("#errortable").height()+76;
	var dw_table_height = $("#dw_table").height();
	var dj_table_height = $("#dj_table").height();
	var nb_table_height = $("#nb_table").height();
	var dzgai_table_height = $("#dzgai_table").height();
	var nbgai_table_height = $("#nbgai_table").height();
	var bottom_height = 0;
	if(modbustype_id == "0"){
		bottom_height = $(window).height() - 120 - 305;	
	}else if(modbustype_id == "1"){
		bottom_height = $(window).height() - 120 - 385;
	}else if(modbustype_id == "2"){
		bottom_height = $(window).height() - 120 - 256;
	}
	var autoheight = $(window).height()-120;
 	if(errortable_height < bottom_height && dw_table_height < bottom_height && dj_table_height < bottom_height
 			&& nb_table_height < bottom_height && dzgai_table_height < bottom_height && nbgai_table_height < bottom_height){
 		$("#main_box").attr("style","min-height:"+autoheight+"px;max-height:"+autoheight+"px;overflow: hidden;");
	}else{
		$("#main_box").attr("style","min-height:"+autoheight+"px;max-height:"+autoheight+"px;overflow: auto;");
	} 
	
}
</script>
</head>

<body  style="overflow: hidden;">
 <!-- 传递38位值 -->	
	<input type="hidden" value="" id="value38">
	<input type="hidden" value="${param.device_id}" id="inid">
	<input type="hidden" value="${param.modbus_type}" id="modbusid">
	<!-- 头部内容home -->
	<div class="hd_box">
		<div class="logo_box">
			<a href="#"><img src="<%=basePath%>/images/logo.png" ></a>
		</div>
		<div class="nav_box">
			<ul>
				<li class="li_icon"><a  href="javascript:js_method(1);"><i><strong class="S_10"></strong></i><span>系统信息</span></a></li>
				<li><a href="javascript:js_method(2);"><i><strong
							class="S_11"></strong></i><span>参数监控</span></a></li>
				<li><a  href="javascript:js_method(3);"><i><strong class="S_12"></strong></i><span>示波器</span></a></li>
				<li><a  href="javascript:js_method(4);"><i><strong class="S_13"></strong></i><span>调试面板</span></a></li>

				<li class="style_li"><a href="<%=basePath%>jsp/index.jsp"><i><strong
							class="S_14"></strong></i><span>首页</span></a></li>
			</ul>
			<hr />
		</div>
	</div>
  <!-- 头部内容end -->
  <div id="loading" style="margin:300px 600px;"></div>
    <div class="main_box clearfix" id="main_box">
        <div class="W_width" >
         <!-- 双馈-->
         
            <div class="pic_box" id="head_sk" style="display:block">
                <!-- 状态 -->
                <div class="bj_pic03 margin_bottom01" style="font-size: 13px;margin-bottom: 35px;">

                    <!-- 分隔线 -->
                    <div class="text0001">
                        <p class="p01">电网Va(V): <a id="82">0</a></p>
                        <p class="p02">电网Vb(V): <a id="83">0</a></p>
                        <p class="p03">电网Vc(V): <a   id="84">0</a></p>
                        <p class="p04">电网频率(Hz): <a   id="85">0</a></p>
                        <p class="p05">有功功率(kW): <a  id="490">0</a></p>
                        <!-- <p class="p06">断路器动作次数:<a  id="56">aaaa</a></p>
                        <p class="p07">总发电量(KWh):<a  id="30">aaaa</a></p> -->
                    </div>
                     <div class="text0002">
                        <p class="p01">有功电流(A): <a  id="80">0</a></p>
                        <p class="p02">无功电流(A): <a  id="79">0</a></p>
                        <p class="p03">电流Ia(A): <a  id="86">0</a></p>
                        <p class="p04">电流Ib(A): <a id="87">0</a></p>
                        <p class="p05">电流Ic(A): <a id="88">0</a></p>
                      <!--   <p class="p06">网侧模组最高温度:<a id="525">aaaa</a></p> -->
                    </div>

                    <div class="text0003">
                        <p class="p01">有功电流(A): <a id="238" >0</a></p>
                        <p class="p02">无功电流(A): <a id="239">0</a></p>
                        <p class="p03">电流Ik(A): <a id="246">0</a></p>
                        <p class="p04">电流Il(A): <a id="247" >0</a></p>
                        <p class="p05">电流Im(A): <a id="248">0</a></p>
                      <!--   <p class="p06">机侧模组最高温度:<a id="526">aaaa</a></p> -->
                    </div>
                    <div class="text0004">
                        <p class="p01">电机转速(rpm): <a id="259">0</a></p>
                        <p class="p02">当前转矩(Nm): <a id="519">0</a></p>
                        <p class="p03">当控转矩(Nm): <a id="505">0</a></p>
                    </div>
                    <div class="text0005">
                        <!-- <p class="p01">定子有功功率(KW):<a id="250"> aaaa</a></p>
                        <p class="p02">定子无功功率(Kvar):<a id="251">aaaa</a></p> -->
                        <p class="p03">电流Iu(A): <a id="241">0</a></p>
                        <p class="p04">电流Iv(A): <a id="245">0</a></p>
                        <p class="p05">电流Iw(A): <a id="252">0</a></p>
                    </div>
                    <div class="text0006">
                        <p class="p01">直流电压(V):<a id="502">0</a></p>

                    </div>
              <img  id="parameimg"  src="<%=basePath%>images/sk/sk_0000.png">
                </div>
            </div>
            <!-- 全功率 -->
            <div class="pic_box"  id="head_qgl" style="display:none">
            <!-- 状态 -->
                <div class="state_p">
                    <p><i class="i01" id="zg_id"></i>主柜激活状态</p>
                    <p><i class="i01" id="cg_id"></i>从柜激活状态</p>
                </div>
                <!-- 状态 -->
                <div class="bj_pic03 margin_bottom" style="font-size: 13px;margin-bottom: 80px;">
                    <div class="text1">
                        <p class="p01">Iq(A): <a id="qgl_80">0</a></p>
                        <p class="p02">Id(A): <a id="qgl_79">0</a></p>
                    </div>
                     <div class="text2">
                        <p class="p01">主机网侧</p>
                        <p class="p02">Ia(A): <a id="qgl_86">0</a></p>
                        <p class="p03">Ib(A): <a id="qgl_87">0</a></p>
                        <p class="p04">Ic(A): <a id="qgl_88">0</a></p>
                    </div>
                    <div class="text3">
                        <p class="p01">Vdc(V): <a id="qgl_78">0</a></p>
                    </div>
                    <div class="text4">
                        <p class="p01">Iu(A): <a id="qgl_250">0</a></p>
                        <p class="p02">Iv(A): <a id="qgl_251">0</a></p>
                        <p class="p03">Iw(A): <a id="qgl_252">0</a></p>
                    </div>
                    <div class="text5">
                        <p class="p01">主机机侧</p>
                        <p class="p02">Iq(A): <a id="qgl_238">0</a></p>
                        <p class="p03">Id(A): <a id="qgl_239">0</a></p>
                    </div>
                    <div class="text6">
                        <p class="p01">电机转速(rpm): <a id="qgl_510">0</a></p>
                        <p class="p02">主控给定转矩(NM): <a id="qgl_519">0</a></p>
                        <p class="p03">当前总转矩: <a id="qgl_507">0</a></p>
                    </div>
                    <!-- 分隔线 -->
                    <div class="text001">
                        <p class="p01">Vab(V): <a id="qgl_82">0</a></p>
                        <p class="p02">Vbc(V): <a id="qgl_83">0</a></p>
                        <p class="p03">Pd(kW): <a id="qgl_490">0</a></p>
                        <p class="p04">Pd(kVar): <a id="qgl_491">0</a></p>
                    </div>
                     <div class="text002">
                        <p class="p01">从机网侧</p>
                        <p class="p02">Ia(A): <a id="qgl_94">0</a></p>
                        <p class="p03">Ib(A): <a id="qgl_95">0</a></p>
                        <p class="p04">Ic(A): <a id="qgl_96">0</a></p>
                    </div>
                   <!--  <div class="text003">
                        <p class="p01">Vdc(V):<a id="502">0</a></p>
                    </div> -->
                    <div class="text004">
                        <p class="p01">Iu(A): <a id="qgl_254">0</a></p>
                        <p class="p02">Iv(A): <a id="qgl_255">0</a></p>
                        <p class="p03">Iw(A): <a id="qgl_256">0</a></p>
                    </div>
                    <div class="text005">
                        <p class="p01">从机机侧</p>
                        <p class="p02">Iq(A): <a id="qgl_242">0</a></p>
                        <p class="p03">Id(A): <a id="qgl_243">0</a></p>
                    </div>
                    <div class="text006">
                        <p class="p01">电机电压Uuv1(V): <a id="qgl_246">0</a></p>
                        <p class="p02">电机电压Uvw1(V): <a id="qgl_247">0</a></p>
                        <p class="p03">电机电压Uuv2(V): <a id="qgl_248">0</a></p>
                        <p class="p04">电机电压Uvw2(V): <a id="qgl_249">0</a></p>
                    </div>


                <img id="parameimg_qgl" src="<%=basePath%>images/qgl/qgl_00.png">
                </div>
       </div> 
        
        <!-- 海上风电 -->
           <div class="pic_box padding01"  id="head_hsfd"  style="display:none;padding: 10px 0 10px 0;"  >
            <!-- 状态 -->
                <!-- <div class="state_p">
                    <p><i class="i01"></i>主柜激活状态</p>
                    <p><i class="i02"></i>从柜激活状态</p>
                </div> -->
                <!-- 状态 -->

                <div class="text_H">
                    <ul class="clearfix">
                        <li>
                        <p><span class="span01">有功功率P(kW):</span><span><a id="13009">0</a></span></p>
                            <p><span>无功功率Q(kVar): </span><span><a id="13010">0</a></span></p>
                        </li>
                        <li>
                            <p><span>功率因数： </span><a id="13017">0</a></p>
                            <p><span>电网频率(Hz): </span><span><a id="13008">0</a></span></p>
                        </li>

                        <li>
                            <p><span>给定无功(kVar): </span><span><a id="13016">0</a></span></p>
                            <p></p>
                        </li>

                        <li>
                            <p><span>给定转矩(kN.m): </span><span><a id="12992">0</a></span></p>
                            <p></p>
                        </li>
                        <li>
                            <p><span>电机转速(rpm): </span><span><a id="12989">0</a></span></p>
                            <p><span>实际转矩(kN.m): </span><span><a id="12993">0</a></span></p>
                        </li>
                        <li>
                            <p><span>有功功率P(kW): </span><span><a id="12990">0</a></span></p>
                            <p><span>无功功率Q(kVar): </span><span><a id="12991">0</a></span></p>
                        </li>

                    </ul>
                </div>

                <div class="bj_pic01" style="font-size: 13px;">
                    <img  id="parameimg_hsfd"  src="<%=basePath%>images/hsfd/hsfd_00.png">
                    <div class="text01">
                        <p>电压U ab(V):<a id="13011">0</a></p>
                        <p>电压U bc(V):<a id="13012">0</a></p>
                    </div>
                   <div class="text02">
                        <p class="p01">电流Ia(A): <a id="13013">0</a></p>
                        <p class="p02">电流Ib(A): <a id="13014">0</a></p>
                        <p class="p03">电流Ic(A): <a id="13015">0</a></p>
                    </div>
                   <div class="text03">
                        <p class="p01">母线电压U pz(V): <a id="13023">0</a></p>
                        <p class="p02">母线电压U zn(V): <a id="13024">0</a></p>
                    </div>
                     <div class="text04">
                        <p class="p01">电流I u(A): <a id="12998">0</a></p>
                        <p class="p02">电流I v(A): <a id="12999">0</a></p>
                        <p class="p03">电流I w(A): <a id="13000">0</a></p>
                    </div>
                     <div class="text05">
                        <p>电机U uv(V): <a id="12994">0</a></p>
                        <p>电机U vw(V): <a id="12995">0</a></p>
                    </div>
                </div>
            </div>
            
			<div class="table_box" style="min-height:480px;max-height:480px;overflow: hidden;">
				<ul class="ul_table_box" style="min-height:480px;max-height:480px">
					<li>
						<p class="pp" style="text-align:right">	
							<input type="hidden" id="write_p" />			
							 <span id="aid" style="padding:7%"><a style="cursor:pointer;margin-left: 10%;" onclick="restart();" >故障复位</a></span>
						</p>
						<table id="errortable">
							<thead>
								<tr>
									<th>来源</th>
									<th>故障告警</th>
								</tr>
							</thead>
							<tbody>
								<tr>
								   <td></td>
									<td></td>
									 <td style="display:none"></td>
								</tr>
								<tr>
								   <td></td>
									<td></td>
									 <td style="display:none"></td>
								</tr>				
							</tbody>
						</table>
					</li>
					<li style="min-height:480px;max-height:480px" id="dw_li"><table id="dw_table">
							<thead>
								<tr>
									<th>电网参数</th>
									<th>数值</th>
									<th>单位</th>
								</tr>
							</thead>
							<tbody>
								<tr>
									<td></td>
									<td></td>
									<td></td>
								</tr>
								<tr>
									<td></td>
									<td></td>
									<td></td>
								</tr>
								<tr>
									<td></td>
									<td></td>
									<td></td>
								</tr>
								<tr>
									<td></td>
									<td></td>
									<td></td>
								</tr>
							</tbody>
						</table></li>
					<li style="min-height:480px;max-height:480px" id="dj_li"><table id="dj_table">
							<thead>
								<tr>
									<th>电机参数</th>
									<th>数值</th>
									<th>单位</th>
								</tr>
							</thead>
							<tbody>

								<tr>
									<td></td>
									<td></td>
									<td></td>
								</tr>
								<tr>
									<td></td>
									<td></td>
									<td></td>
								</tr>
								<tr>
									<td></td>
									<td></td>
									<td></td>
								</tr>
								<tr>
									<td></td>
									<td></td>
									<td></td>
								</tr>
								<tr>
									<td></td>
									<td></td>
									<td></td>
								</tr>
							</tbody>
						</table></li>
					<li style="min-height:480px;max-height:480px" id="nb_li"><table id="nb_table">
							<thead>
								<tr>
									<th>变流器内部参数</th>
									<th>数值</th>
									<th>单位</th>
								</tr>
							</thead>
							<tbody>
								<tr>
									<td></td>
									<td></td>
									<td></td>
								</tr>
							</tbody>
						</table></li>
						<li style="width:37%;height:30% " ><table id="dzgai_table">
							<thead>
								<tr>
									<th>其他参数</th>
									<th>数值</th>
									<th>单位</th>
								</tr>
							</thead>
							<tbody>
								<tr>
									<td></td>
									<td></td>
									<td></td>
								</tr>
							</tbody>
						</table></li>
				<li style="width:37%;height:30%" ><table id="nbgai_table">
							<thead>
								<tr>
									<th></th>
									<th>数值</th>
									<th>单位</th>
								</tr>
							</thead>
							<tbody>
								<tr>
									<td></td>
									<td></td>
									<td></td>
								</tr>
							</tbody>
						</table></li>		
						
				</ul>
			</div>

		</div>
	</div>

<div id="gray"></div>
<!-- 来源 弹窗end -->
<div style="display: none;" class="popup poput_box_b Min_w tablee" id="bit_div">
	<p class="poput_title">
		<span>来源</span> <a style="cursor:pointer;" href="javascript:void(0)" onclick="closebit();"></a>
	</p>
	<div class="poput_box_height">
		<!--  <p class="p_style">id：32432<span>name：控制参数</span></p> -->
		<table class="table_S" id="bit_table">
			<thead>
				<tr>
					<th>序号</th>
					<th>故障信息</th>
					<th>状态</th>
					<th>专家库</th>
				</tr>
			</thead>
			<tbody>

			</tbody>
		</table>
	</div>
</div>
<!-- 来源 弹窗end -->
<!-- 专家库弹框start -->
<div id="expertDatabase"></div>
<!-- 专家库弹框end -->
</body>

</html>
