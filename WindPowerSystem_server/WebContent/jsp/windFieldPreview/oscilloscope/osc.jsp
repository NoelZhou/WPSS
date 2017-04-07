<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%
 	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!DOCTYPE html>
<html lang="en">

<head>
<meta charset="UTF-8">
<link href="<%=basePath%>/favicon.ico" rel="icon" type="image/x-icon" />
<link href="<%=basePath%>/favicon.ico" rel="shortcut icon" type="image/x-icon" />
<title>示波器</title>
<!-- slider start-->
<link rel="stylesheet" href="<%=basePath%>slider/css/jqueryui.min.css" />
<link rel="stylesheet" href="<%=basePath%>slider/css/jquery-ui-slider-pips.min.css" />
<link rel="stylesheet" href="<%=basePath%>slider/css/app.min.css" />
<!-- slider end-->

<link rel="stylesheet" type="text/css" href="<%=basePath%>css/common.css">
<link rel="stylesheet" type="text/css" href="<%=basePath%>css/style.css">

<!-- hightcharts -->
<script type="text/javascript" src="<%=basePath%>js/highcharts/jquery-1.8.3.min.js"></script>
<!-- hightcharts -->

<!-- 共同引用jquery js -->
<script type="text/javascript" src="<%=basePath%>js/js_osc.js"></script>
<script type="text/javascript" src="<%=basePath%>js/popup.js"></script>
<script type="text/javascript" src="<%=basePath%>js/opts.js"></script>
<script type="text/javascript" src="<%=basePath%>js/spin.js"></script>
<!-- 共同引用jquery js -->
<script type="text/javascript">
$(function(){
	autoHeight();
});
function autoHeight(){
	var height = $(window).height()-190;
    $("#autoarea").attr("style","min-height:"+height+"px;max-height:"+height+"px;overflow:hidden;");
    $("#container").attr("style","margin: 0 auto; height:"+(height-100)+"px");
    $("#oscDsp1-2").attr("style","height:"+((height-10)/2 - 40)+"px");
    $("#oscDsp2-2").attr("style","height:"+((height-10)/2 - 40)+"px");  
    $("#defaultDsp1-2").attr("style","height:"+(height-40)+"px");
    $("#defaultDsp2-2").attr("style","height:"+(height-40)+"px");
}
function downFile(){
	$.ajax({
		type:'post',
		url:'<%=basePath%>wfp/osc/SelectDspDcq.do',
		//data:'',
		success:function(data){
			var result = JSON.parse(data);	
			var msg=result.message;
			var dspNum=result.responseData.dspNum;
			if(msg=="获取成功"){
				if(dspNum!='0'){
					window.location.href="<%=basePath%>wfp/osc/downFile.do";
				}else{
					alert("下载数据为空，请先采集！");
				}
			}else{
			   	alert(msg);
			}
		}});
}

var time1="";
function StartDspDcq(){
	var dspWaveformCodes=document.getElementById("dsp1WaveformCodes").value+","+document.getElementById("dsp2WaveformCodes").value;
	var dspWaveformName=document.getElementById("dsp1WaveformNames").value+","+document.getElementById("dsp2WaveformNames").value;
	 var del = '<%=basePath%>wfp/osc/DataAcq.do';
	   $.ajax({
		   	type:"post",
		   	url:del,
		   	data : {
				"dspWaveformCodes" : dspWaveformCodes,
				"dspWaveformName" : dspWaveformName
			},
		   	success:function(data){
		   
	   } }); 
	  
}
</script>

<style type="text/css">
.box_lef011 {
    width: 83%;
    float: left;
    height: 800px;
    overflow: auto;
    position: relative;
}

.box_right011 {
    width: 25% !important;
    float: right;
}

.label_style11{
    width: 30px !important;
    margin-top: 13px !important;
    float: left !important;
    padding-left: 4%;
    position:relative;
}
</style>
</head>

<body style="overflow: hidden;">
<!-- <body>	 -->
<!-- 头部内容home -->
	<div class="hd_box">
		<div class="logo_box">
			<a href="#"><img src="<%=basePath%>/images/logo.png" style="margin-top:-10.8%"></a>
		</div>
		<div class="nav_box">
			<ul>
				<li><a href="javascript:js_method(1);"><i><strong
							class="S_10"></strong></i><span>系统信息</span></a></li>
				<li><a href="javascript:js_method(2);"><i><strong
							class="S_11"></strong></i><span>参数监控</span></a></li>
				<li class="li_icon"><a href="javascript:js_method(3);"><i><strong
							class="S_12"></strong></i><span>示波器</span></a></li>
				<li><a href="javascript:js_method(4);"><i><strong
							class="S_13"></strong></i><span>调试面板</span></a></li>
				<li class="style_li"><a href="<%=basePath%>jsp/index.jsp"><i><strong
							class="S_14"></strong></i><span>首页</span></a></li>
			</ul>
			<hr />
		</div>
	</div>
	<!-- 头部内容home -->
	<%-- <div class="hd_box">
		<div class="logo_box">
			<a href="#"><img src="<%=basePath%>images/logo.png" style="margin-top: 14.5%;"></a>

 <a href="#"><img src="<%=basePath%>images/logo.png" ></a>		
 </div>
		<div class="nav_box">
			<ul>
				<li><a href="javascript:js_method(1);"><i><strong
							class="S_10"></strong></i><span>系统信息</span></a></li>
				<li><a href="javascript:js_method(2);"><i><strong
							class="S_11"></strong></i><span>参数监控</span></a></li>
				<li class="li_icon"><a href="javascript:js_method(3);"><i><strong
							class="S_12"></strong></i><span>示波器</span></a></li>
				<li><a href="javascript:js_method(4);"><i><strong
							class="S_13"></strong></i><span>调试面板</span></a></li>
				<li class="style_li"><a href="<%=basePath%>jsp/index.jsp"><i><strong
							class="S_14"></strong></i><span>首页</span></a></li>
			</ul>
			<hr />
		</div>
	</div> --%>
	<!-- 头部内容end -->
	<div class="main_box clearfix" id="main_box">
		<!-- 右侧内容 -->
		<div class="W_width">

			<!-- 编辑标签 -->
			<p style="width: 80%; float: left;" class="P_Label p_spanw clearfix">
				<span id="importSpan" onclick="exportExcel()"><a class="a_size02g" href="javascript:;">导出</a></span> 
				<span id="startSpan" onclick="startSync()"><a class="a_size01e" href="javascript:;">开始</a></span>
				<span id="stopSpan" onclick="stopSync()"><a class="a_size03d" href="javascript:;">停止</a></span>
				
				<span style="border: none !important; padding-right: 0 !important; margin-right: 0 !important;">
					模式
				</span>
				<!-- select 下拉菜单 -->
				<strong style="margin-top: 4px; float: left; width: 10% !important" class="stronta">
					<select id="modelType" name="modelType" style="width: 140px;" onchange="changeModel();">
						<option value="A8">示波器</option>
						<option value="A8">数据录波</option>
						<option value="A9">故障录波</option>
						
					</select>
				</strong>
				<!-- select 下拉菜单 -->

				<span id="samplingFrequencySpan" style="border: none !important; padding-right: 0 !important; margin-right: 0 !important;">
					采样频率
				</span>
				<!-- select 下拉菜单 -->
				<strong style="margin-top: 4px; float: left; width: 10% !important" class="stronta">
					<select id="samplingFrequency" name="samplingFrequency" style="width: 140px;" onchange="changeCypl();">
						<option value="2">2K</option>
						<option value="2.5">2.5K</option>
						<option value="3">3K</option>
						
					</select>
				</strong>
				<!-- select 下拉菜单 -->
				<!-- <span style="margin-left:45px;" onclick="StartDspDcq()" id="cj"><a class="a_size02g" href="javascript:;">采集</a></span> 
				<span style="margin-left:45px;display:none;" id="cjz" >采集中</span> 
				<span style="margin-left:5px;" onclick="downFile()"><a class="a_size02g" href="javascript:;">下载</a></span> --> 
				
					<span id="waveformTypeSpan" style="border: none !important; padding-right: 0 !important; margin-right: 0 !important;">
					波形
				</span>
				<!-- select 下拉菜单 -->
				<strong style="margin-top: 4px; float: left; width: 10% !important" class="stronta">
					<select id="waveformType" name="waveformType" style="width: 140px;" onchange="changeWaveformType();">
						<option value="1">网侧</option>
						<option value="2">机侧</option>
					</select>
				</strong>
				<!-- select 下拉菜单 -->
				
				<span id="defaultWaveformCodeSpan" style="border: none !important; padding-right: 0 !important; margin-right: 0 !important;">
					录波编号
				</span>
				<strong style="margin-top: 4px; float: left; width: 10% !important" class="stronta">
					<input id="defaultWaveformCode" name="defaultWaveformCode" type="text" style="width: 140px;" value="1"/>
				</strong>
			</p>
			<p style="width: 19%; float: right;" class="P_Label clearfix">
				<span style="border: none;float:right">波形名称</span>

			</p>
			<!-- 编辑标签end -->

			<!-- table内容 -->
			<div class="table_box" id="autoarea">
				<!-- 数据趋势图 -->
				<div class="box_lef011 bj_box" style="width: 73%; background-color: black;">
					<div class="w_w">
						<div id="container"></div>
						<div id="sliderDev" style="margin-top: 20px;">
							<div style="width: 5%; text-align: right; float: left;"><b style="font-size: 35px;" onclick="reduce(1);">－</b>&nbsp;&nbsp;&nbsp;&nbsp;</div>
							<div class="step-table-slider-15" style="width: 90%; float: left;"></div>
							<div style="width: 5%; text-align: left; float: left;">&nbsp;&nbsp;&nbsp;&nbsp;<b style="font-size: 35px;" onclick="enlarge(1);">＋</b></div>
						</div>
					</div>
				</div>
				<!-- 数据趋势图end -->
				
				<div class="box_right box_right011" style="width: 18%">
					<!-- 示波器网测 -->
					<div id="oscDsp1-1" class="T_table table_boxb" style="height: 40px;">
						<table id="table ">
							<tbody>
								<tr>
									<td style="width: 15%;padding-left: 3%;display:none;">显示</td>
									<td style="width: 15%;padding-left: 3%;">选择</td>
									<td>网侧波形</td>
								</tr>
							</tbody>
						</table>
					</div>
					<div id="oscDsp1-2" class="T_table table_boxb">
						<table id="table ">
							<tbody>
								<c:forEach items="${webList}" var="wl" varStatus="ind">
									<tr>
										<td style="width: 15%;padding-left: 4%; display:none;">
											<label class="label_input label_style11" style="position:relative;">
												<input class="input_chk" type="checkbox" name="dsp1IsShow" value="${wl.code}" onclick="showHideLine('dsp1WaveformCodes', '${wl.code}', 0, this);"/> 
												<span></span>
											</label>
										</td>
										<td style="width: 15%;padding-left: 4%;"><label class="label_input label_style11"> <input
												class="input_chk" type="checkbox" name="dsp1Code" value="${wl.code}" title="${wl.name}" onclick="checkedOscMoveform('dsp1IsShow', '${wl.code}', this)"/> <span></span>
										</label></td>
										<td style="padding-left: 4%;" title="${wl.name}">
											<c:if test="${wl.name.length() > 16}">
												${fn:substring(wl.name, 0, 16)}...
											</c:if>
											<c:if test="${wl.name.length() <= 16}">
												${wl.name}
											</c:if>
										</td>
									</tr>
								</c:forEach>
							</tbody>
						</table>
					</div>
					
					<!-- 示波器机测 -->
					<div id="oscDsp2-1" class="B_table table_boxb" style="height: 40px;margin-top:10px;">
						<table id="tableA">
							<tbody>
								<tr>
									<td style="width: 15%;padding-left: 3%; display:none;">显示</td>
									<td style="width: 15%;padding-left: 3%;">选择</td>
									<td>机侧波形</td>
								</tr>
							</tbody>
						</table>
					</div>
					<div id="oscDsp2-2" class="B_table table_boxb">
						<table id="tableA">
							<tbody>
								<c:forEach items="${machineList}" var="ml" varStatus="ind">
									<tr>
										<td style="width: 15%;padding-left: 4%; display:none;">
											<label class="label_input label_style11">
												<input class="input_chk" type="checkbox" name="dsp2IsShow" value="${ml.code}" onclick="showHideLine('dsp2WaveformCodes', '${ml.code}', 8, this);"/>
												<span></span>
											</label>
										</td>
										<td style="width: 15%;padding-left: 4%;"><label class="label_input label_style11"> <input
												class="input_chk" type="checkbox" name="dsp2Code" value="${ml.code}" title="${ml.name}" onclick="checkedOscMoveform('dsp2IsShow', '${ml.code}', this)"/> <span></span>
										</label></td>
										<td style="padding-left: 4%;" title="${ml.name}">
											<c:if test="${ml.name.length() > 16}">
												${fn:substring(ml.name, 0, 16)}...
											</c:if>
											<c:if test="${ml.name.length() <= 16}">
												${ml.name}
											</c:if>
										</td>
									</tr>
								</c:forEach>
							</tbody>
						</table>
					</div>
					<!-- 故障录波网测 -->
					<div id="defaultDsp1-1" class="T_table table_boxb" style="height: 40px;">
						<table>
							<thead>
								<tr>
									<!-- <th width="58px;">显示</th> -->
									<th>网侧波形</th>
								</tr>
							</thead>
						</table>
					</div>
					<div id="defaultDsp1-2" class="T_table table_boxb">
						<table>
							<tbody>
								<c:set var="dsp1WaveformCodes" /> 
								<c:set var="dsp1WaveformNames" />
								<c:forEach items="${webDefaultList}" var="wl" varStatus="ind">
									<c:set value="${dsp1WaveformCodes},${wl.code}" var="dsp1WaveformCodes" />
									<c:set value="${dsp1WaveformNames},${wl.name}" var="dsp1WaveformNames" />
									<tr>
										<td width="58px;" style="display:none;"><label class="label_input label_style11" style="position:relative;"> <input
												class="input_chk" type="checkbox" name="dspDefault1IsShow"  value="${wl.code}" title="${wl.name}" onclick="showHideLine('dsp1DefaultWaveformCodes', '${wl.code}', this);"/> <span></span>
										</label></td>
											<td style="width: 21%;padding-left: 6%; display:none;"><label class="label_input label_style11"> <input
												class="input_chk" type="checkbox" name="dspDefault1Code" value="${wl.code}" title="${wl.name}" onclick="checkedOscMoveform('dspDefault1IsShow', '${wl.code}', this)"/> <span></span>
										</label></td>
										<td title="${wl.name}">
											<c:if test="${wl.name.length() > 16}">
												${fn:substring(wl.name, 0, 16)}...
											</c:if>
											<c:if test="${wl.name.length() <= 16}">
												${wl.name}
											</c:if>
										</td>
									</tr>
								</c:forEach>
							</tbody>
						</table>
					</div>
					<!-- 故障录波机测 -->
					<div id="defaultDsp2-1" class="B_table table_boxb" style="height: 40px;">
						<table>
							<thead>
								<tr>
									<!-- <th width="58px;">显示</th> -->
									<th>机侧波形</th>
								</tr>
							</thead>
						</table>
					</div>
					<div id="defaultDsp2-2" class="B_table table_boxb">
						<table>
							<tbody>
								<c:set var="dsp2WaveformCodes" /> 
								<c:set var="dsp2WaveformNames" /> 
								<c:forEach items="${machineDefaultList}" var="ml" varStatus="ind">
									<c:set value="${dsp2WaveformCodes},${ml.code}" var="dsp2WaveformCodes" />
									<c:set value="${dsp2WaveformNames},${ml.name}" var="dsp2WaveformNames" />
									<tr>
										<td width="58px;" style="display:none;"><label class="label_input label_style11"><input
												class="input_chk" type="checkbox" name="dspDefault2IsShow"    value="${ml.code}" title="${ml.name}"  onclick="showHideLine('dsp2DefaultWaveformCodes', '${ml.code}', this);"/> <span></span>
										</label></td>
										<td title="${ml.name}">
											<c:if test="${ml.name.length() > 16}">
												${fn:substring(ml.name, 0, 16)}...
											</c:if>
											<c:if test="${ml.name.length() <= 16}">
												${ml.name}
											</c:if>
										</td>
									</tr>
								</c:forEach>
							</tbody>
						</table>
					</div>
				</div>
				<!-- 右侧风机故障/告警信息end -->
			</div>
			<!-- table内容end -->
		</div>
		<!-- 右侧内容end -->
	</div>
	
	
	<input type="hidden" id="slideNum" name="slideNum" value="0" />

	<input type="hidden" id="deviceId" name="deviceId" value="${deviceId}" />
	<input type="hidden" id="modbusid" name="modbusid" value="${modbusid}" />
	<input type="hidden" id="modbusType" name="modbusType" value="${modbusType}" />
	
	
	<input type="hidden" id="modelTypeLast" name="modelTypeLast" value="A8" />
	
	<input type="hidden" id="dsp1WaveformCodes" name="dsp1WaveformCodes" value="" />
	<input type="hidden" id="dsp1WaveformNames" name="dsp1WaveformNames" value="" />
	<input type="hidden" id="dsp2WaveformCodes" name="dsp2WaveformCodes" value="" />
	<input type="hidden" id="dsp2WaveformNames" name="dsp2WaveformNames" value="" />
	
	<input type="hidden" id="dsp1DefaultWaveformCodes" name="dsp1DefaultWaveformCodes" value="${fn:substring(dsp1WaveformCodes, 1, dsp1WaveformCodes.length())}" />
	<input type="hidden" id="dsp1DefaultWaveformNames" name="dsp1DefaultWaveformNames" value="${fn:substring(dsp1WaveformNames, 1, dsp1WaveformNames.length())}" />
	<input type="hidden" id="dsp2DefaultWaveformCodes" name="dsp2DefaultWaveformCodes" value="${fn:substring(dsp2WaveformCodes, 1, dsp2WaveformCodes.length())}" />
	<input type="hidden" id="dsp2DefaultWaveformNames" name="dsp2DefaultWaveformNames" value="${fn:substring(dsp2WaveformNames, 1, dsp1WaveformNames.length())}" />
		
	<!-- slider start-->
	<script src="<%=basePath%>slider/js/jquery-plus-ui.min.js"></script>
	<script src="<%=basePath%>slider/js/jquery-ui-slider-pips.js"></script>
	<!-- slider end-->
	
	<script type="text/javascript"  src="<%=basePath%>/jsp/windFieldPreview/oscilloscope/canvasjs.js"></script>
<%-- 	<script type="text/javascript"  src="<%=basePath%>/jsp/windFieldPreview/oscilloscope/canvasjs.min.js"></script> --%>
	<script type="text/javascript">var context_ = '<%=basePath%>';</script>
	<script type="text/javascript"  src="<%=basePath%>/jsp/windFieldPreview/oscilloscope/osc.js"></script>

	<form name="exportForm" action="<%=basePath%>wfp/osc/exportExcel.do" method="post">
		<input type="hidden" id="waveformDataFlags" name="waveformDataFlags" />
		<input type="hidden" id="waveformNames" name="waveformNames" />
		<input type="hidden" id="waveformData0" name="waveformData0" />
		<input type="hidden" id="waveformData1" name="waveformData1" />
		<input type="hidden" id="waveformData2" name="waveformData2" />
		<input type="hidden" id="waveformData3" name="waveformData3" />
		<input type="hidden" id="waveformData4" name="waveformData4" />
		<input type="hidden" id="waveformData5" name="waveformData5" />
		<input type="hidden" id="waveformData6" name="waveformData6" />
		<input type="hidden" id="waveformData7" name="waveformData7" />
		<input type="hidden" id="waveformData8" name="waveformData8" />
		<input type="hidden" id="waveformData9" name="waveformData9" />
		<input type="hidden" id="waveformData10" name="waveformData10" />
		<input type="hidden" id="waveformData11" name="waveformData11" />
		<input type="hidden" id="waveformData12" name="waveformData12" />
		<input type="hidden" id="waveformData13" name="waveformData13" />
		<input type="hidden" id="waveformData14" name="waveformData14" />
		<input type="hidden" id="waveformData15" name="waveformData15" />
		<input type="hidden" id="waveformData16" name="waveformData16" />
		<input type="hidden" id="waveformData17" name="waveformData17" />
		<input type="hidden" id="waveformData18" name="waveformData18" />
		<input type="hidden" id="waveformData19" name="waveformData19" />
		<input type="hidden" id="waveformData20" name="waveformData20" />
		<input type="hidden" id="waveformData21" name="waveformData21" />
		<input type="hidden" id="waveformData22" name="waveformData22" />
		<input type="hidden" id="waveformData23" name="waveformData23" />
		<input type="hidden" id="waveformData24" name="waveformData24" />
		<input type="hidden" id="waveformData25" name="waveformData25" />
		<input type="hidden" id="waveformData26" name="waveformData26" />
		<input type="hidden" id="waveformData27" name="waveformData27" />
		<input type="hidden" id="waveformData28" name="waveformData28" />
		<input type="hidden" id="waveformData29" name="waveformData29" />
		<input type="hidden" id="waveformData30" name="waveformData30" />
		<input type="hidden" id="waveformData31" name="waveformData31" />
	</form>
	<div id="messages"></div>
	<div id="maxXDataNum" style="position: relative; z-index: 1; color: gray; height: 35px; width: 50px; font-size: 24px; font-weight: 400; display: none;left:1088px;bottom:65px;">
		1000
	</div>
</body>
</html>