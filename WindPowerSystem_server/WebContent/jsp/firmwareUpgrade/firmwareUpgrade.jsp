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
<title>固件升级</title>
<link rel="stylesheet" type="text/css" href="<%=basePath%>css/common.css">
<link rel="stylesheet" type="text/css" href="<%=basePath%>css/style.css">

<script type="text/javascript" src="<%=basePath%>js/jquery-1.8.3.min.js"></script>
<script type="text/javascript" src="<%=basePath%>js/opts.js"></script>
<script type="text/javascript" src="<%=basePath%>js/spin.js"></script>
<script type="text/javascript" src="<%=basePath%>js/jquery/ajaxfileupload.js"></script>
<script type="text/javascript" src="<%=basePath%>jsp/firmwareUpgrade/firmwareUpgrade.js"></script>
<script type="text/javascript">
var basePath = '<%=basePath%>';
</script>
</head>
<body>
	<!-- 头部内容home -->
	
	<!-- 头部内容end -->

	<div id="container" class="main_box clearfix">
		<!-- 右侧内容 -->
		<div class="right_main_div0">
			<div id="loading" style="margin: 300px 660px;"></div>
			<div class="W_width" id="W_width1">
				<!-- 头部切换标签 -->
				<div class="main_title_box" id="main_title_box"></div>
				<!-- 头部切换标签end -->
				
				<!-- table内容 -->
				<a id="btnUpdate" class="A_button" style="cursor: pointer; margin-bottom: 10px; margin-top: 10px; float: left;" onclick="toUpload();">升级</a>
				<div class="table_box" id="autoarea">
					<input type="hidden" id="mtypeid" name="mtypeid" />
					<input type="hidden" id="oldids" name="oldids" />

					<table id="mytableheader">
						<thead>
							<tr style="background-color: #edf2f6;">
								<th width="58px;">
									<label class="label_input padding_style">		
										<input type="checkbox" class="input_chk" id="chk_all">		
										<span class="style01"></span>	
									</label>
								</th>
								<th width="58px;"><b>序号</b></th>
								<th width="280px;"><b>名称</b></th>
								<th width="120px;"><b>IP地址</b></th>
								<th><b>端口</b></th>
							</tr>
						</thead>
					</table>
					<table id="mytable"></table>
				</div>
				<!-- table内容end -->
			</div>
		</div>
		<!-- 右侧内容end -->
		
		<!-- 上传文件界面 -->
		<div style="display:none" class="poput_box_b">
		    <p class="poput_title">
		        <a href="javascript:;" onclick="closediv()"></a>
		    </p>
		    <div style="padding-left: 80px;">
		        <form class="P_Label" id="Form" method="post" enctype="multipart/form-data">
		   			<input type="hidden" id="deviceIds" name="deviceIds" value=""/>
		   			<input type="hidden" id="endCode" name="endCode" value=""/>
		   			升级文件&nbsp;<input type='text' name='textfield' id='textfield' class='txt' />
					<input type='button' class='btn' value='浏览...' onclick="selectedFile();"/>
					<input type="file" name="file" class="file" id="fileField"  onchange="document.getElementById('textfield').value=this.value" />
					<br/>
		        </form>
	        </div>
		    <a class="a_button" href="javascript:;" onclick="uploadFile()">确定</a>
	   	</div>
	   	
	   	<!-- 右侧内容 -->
		<div class="right_main_div0" style="display: none;" id="poput_device_box_b">
			<div class="W_width" id="W_width2" style="width: 84%;">
				<p class="poput_title">设备信息
			        <a href="javascript:;" onclick="closedevicediv()"></a>
			    </p>
				<div class="table_box" id="device_box" style="max-height: 550px;">
					
				</div>
				<!-- table内容end -->
				<a class="a_button" href="javascript:;" onclick="closedevicediv()">关闭</a>
			</div>
		</div>
	</div>
</body>
</html>