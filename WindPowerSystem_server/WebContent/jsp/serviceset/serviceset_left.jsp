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
<title>服务设置-采集频率设置</title>
<link rel="stylesheet" type="text/css" href="../css/common.css">
<link rel="stylesheet" type="text/css" href="../css/style.css">
<script type="text/javascript" src="../js/jquery1124.js"></script>
<script type="text/javascript" src="../js/jquery-1.8.3.min.js"></script>
<script type="text/javascript">
    	$(function(){
			onclickUrl();
			$("#iframe").attr("style","width:99%;height:"+$(window).height()+"px");
		}); 
    	function onclickUrl(objUrl){
    		var url="";
    		if(objUrl==null){
    			 url="../jsp/serviceset/protocol_settings/fullpowertx.jsp";
    		}
    		if(objUrl==1){
      			 url="../jsp/serviceset/protocol_settings/fullpowertx.jsp";
      			}
    		if(objUrl==2){
     			 url="../jsp/serviceset/protocol_settings/doublefedtx.jsp";
     			}
    		if(objUrl==3){
     			 url="../jsp/serviceset/protocol_settings/offshorewindpowertx.jsp";
     			}
    		if(objUrl==4){
   			 url="../jsp/serviceset/mailboxManagement.jsp";
   			}
   			if(objUrl==5){
      			 url="../jsp/serviceset/emailServer.jsp";
      			}
   			if(objUrl==6){
     			 url="../jsp/serviceset/alarm_report.jsp";
     			}	
   			if(objUrl==7){
     			 url="../jsp/serviceset/fault_report.jsp";
     			}
   			if(objUrl==8){
    			 url="../jsp/serviceset/run_report.jsp";
    			}
    		if(objUrl==9){
    			 url="../jsp/serviceset/equipment_type_management.jsp";
    		}
    		if(objUrl==10){
   			 url="../jsp/serviceset/device_Management.jsp";
   			}
    		 if(objUrl==11){
   			 url="../jsp/serviceset/mqttDevice_Management.jsp";
   		    } 
    		if(objUrl==12){
   			 url="../jsp/serviceset/getCollectTimes.jsp";
   			}
    		if(objUrl==13){
      			 url="<%=basePath%>wfp/firmwareUpgrade/toFirmwareUpgrade.do";
		}
		if (objUrl == 14) {
			url = "../jsp/serviceset/sendtimecode.jsp";
		}
		if (objUrl == 15) {
			url = "../jsp/serviceset/pageset_device.jsp";
		}
		if (objUrl == 16) {
			url = "../jsp/serviceset/pageset_basic.jsp";
		}

		$("#iframe").attr("src", url);
	}

	function toChildSave() {
		document.getElementById('iframe').contentWindow.save();
	}
	function toChildEdit(id) {
		document.getElementById('iframe').contentWindow.edit(id);
	}
	function toDeviceSave(id) {
		document.getElementById('iframe').contentWindow.save(id);
	}
</script>

<!-- 服务设置左侧菜单 -->

<!-- 服务设置左侧菜单 -->
</head>
<body>
	<!-- 服务设置左侧菜单 -->

	<div class="leftsidebar_box" id="leftsidebar_box">
		<dl id="onedl">
			<dt id="onedt" onclick="showview()">
				协议解析设置<img src="../images/left/select_xl01.png">
			</dt>
			<dd id="dd1" class="thisclass" style="cursor: pointer"
				onclick="onclickUrl(1)">
				<a>全功率通讯协议(V6版)</a>
			</dd>
			<dd id="dd2" style="cursor: pointer" onclick="onclickUrl(2)">
				<a>双馈通讯协议(通用版)</a>
			</dd>
			<dd id="dd3" style="cursor: pointer" onclick="onclickUrl(3)">
				<a>海上风电通讯协议(通用版)</a>
			</dd>
		</dl>
		<dl id="twodl">
			<dt>
				故障告警设置<img src="../images/left/select_xl01.png">
			</dt>
			<dd style="cursor: pointer" onclick="onclickUrl(4)">
				<a>接收邮件管理</a>
			</dd>
			<dd style="cursor: pointer" onclick="onclickUrl(5)">
				<a>邮箱服务设置</a>
			</dd>
		</dl>
		<dl id="theredl">
			<dt>
				报表设置<img src="../images/left/select_xl01.png">
			</dt>
			<dd style="cursor: pointer" onclick="onclickUrl(6)">
				<a>告警报表</a>
			</dd>
			<dd style="cursor: pointer" onclick="onclickUrl(7)">
				<a>故障报表</a>
			</dd>
			<dd style="cursor: pointer" onclick="onclickUrl(8)">
				<a>运行报表</a>
			</dd>
		</dl>
		<dl id="fourdl">
			<dt>
				采集设置<img src="../images/left/select_xl01.png">
			</dt>
			<dd style="cursor: pointer" onclick="onclickUrl(9)">
				<a>设备类型管理</a>
			</dd>
			<dd style="cursor: pointer" onclick="onclickUrl(10)">
				<a>设备管理</a>
			</dd>
			<dd>
				<a style="cursor: pointer" onclick="onclickUrl(11)">mqtt设备管理</a>
			</dd>
			<dd style="cursor: pointer" onclick="onclickUrl(12)">
				<a>采集频率设置</a>
			</dd>
		</dl>
		<dl id="fivedl">
			<dt>
				固件升级<img src="../images/left/select_xl01.png">
			</dt>
			<dd style="cursor: pointer" onclick="onclickUrl(13)">
				<a>固件升级</a>
			</dd>
		</dl>
		<dl id="sixdl">
			<dt>
				对时指令<img src="../images/left/select_xl01.png">
			</dt>
			<dd style="cursor: pointer" onclick="onclickUrl(14)">
				<a>对时设置</a>
			</dd>
		</dl>
		<dl id="senverdl">
			<dt>
				页面设置<img src="../images/left/select_xl01.png">
			</dt>
			<dd style="cursor: pointer" onclick="onclickUrl(15)">
				<a>风场概览</a>
			</dd>
			<dd style="cursor: pointer" onclick="onclickUrl(16)">
				<a>基本参数配置</a>
			</dd>
		</dl>
	</div>
	<!-- 服务设置左侧菜单 -->
	<script type="text/javascript">
		$(function() {
			$(".leftsidebar_box dd").hide();
			// 为菜单初始化状态值
			$(".leftsidebar_box dt").each(
					function(index, value) {
						if (index == 0) {
							$("#dd1").show();
							$("#dd2").show();
							$("#dd3").show();
							$(this).parent().find('img').attr("src",
									"../images/left/select_xl.png");
							$(this).attr("state", 1);
						} else {
							$(this).attr("state", 0);
						}
					});

			$(".leftsidebar_box dt").click(
					function() {
						var idss = this.id;
						if (idss != "onedt") {
							$("#dd1").hide();
							$("#dd2").hide();
							$("#dd3").hide();
						}
						// 当前菜单状态
						var state = $(this).attr("state");
						// 重置所有菜单状态
						$(".leftsidebar_box dt").each(function(index, value) {
							$(this).attr("state", 0);
						});

						$(this).parent().find('dd').removeClass("menu_chioce");
						$(this).parent().find('dt').removeClass("inactives");
						$(".leftsidebar_box dt img").attr("src",
								"../images/left/select_xl01.png");
						//$(this).parent().find('img').attr("src", "../images/left/select_xl.png");

						$(".menu_chioce").slideUp();
						$(this).parent().find('dd').slideToggle();
						$(this).parent().find('dd').addClass("menu_chioce");

						// 根据菜单状态，变更图标
						if (state == 0) {
							$(this).parent().find('img').attr("src",
									"../images/left/select_xl.png");
							$(this).attr("state", 1);

						} else {
							$(this).parent().find('img').attr("src",
									"../images/left/select_xl01.png");
							$(this).attr("state", 0);

						}
					});

			var cotrs = $(".leftsidebar_box dd");
			cotrs.click(function() {
				$(cotrs).removeClass("thisclass");
				$(this).addClass("thisclass").siblings(cotrs).removeClass(
						"thisclass");
			});
		});
	</script>
	<!-- 服务设置右侧内容 start-->
	<div>

		<iframe id="iframe" name="serviceFrame" scrolling="no" frameborder="0"
			src=""></iframe>

	</div>
	<!-- 服务设置右侧内容 stop-->

</body>
</html>