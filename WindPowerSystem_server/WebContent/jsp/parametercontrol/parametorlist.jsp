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
<link rel="stylesheet" type="text/css"
	href="<%=basePath%>/css/common.css">
<link rel="stylesheet" type="text/css"
	href="<%=basePath%>/css/style.css">
<!-- 共同引用jquery js -->
<script type="text/javascript" src="<%=basePath%>js/jquery-1.8.3.min.js"></script>
<script type="text/javascript" src="<%=basePath%>/js/js.js"></script>
<!-- 共同引用jquery js -->
<title>参数监控</title>
</head>
<script type="text/javascript">
var ids;
window.onload=function(){
	  var urlstr=window.location.href;
	 var device=urlstr.split("?")[1].split("=")[1]
	  var id=null;
	  jq(id,device);
	  jqid(id,device);
	  
	}
	function jq(id,device){
		var device=$("#inid").val(); 
		//获取设备故障数据
		$(".cid").remove();
		if(id==null){
			ids=1;
		}else{
			ids=id;
		}
		 $.ajax({
			   type:"post",
			   data:{"parent_type":ids,"device_id":device},
			   url:"<%=basePath%>Parameter/parameterByType.do",
			   success:function(data){
				   var result = JSON.parse(data);
					 if("1001" == result.code){
						var parametelist = result.responseData.parametelist;
						if(ids==1){
							for(var i=0;i<parametelist.length;i++){
							    $("#wid").append("<a onclick=jqid('"+parametelist[i].array_type+"')><dd class='cid'>"+parametelist[i].name+"</dd></a>");  
						}	
						}else if(ids==2){
							for(var i=0;i<parametelist.length;i++){
								$("#jid").append("<a onclick=jqid('"+parametelist[i].array_type+"')><dd class='cid'>"+parametelist[i].name+"</dd></a>");
							}
						}else{
							for(var i=0;i<parametelist.length;i++){
								$("#wwid").append("<a onclick=jqid('"+parametelist[i].array_type+"')><dd class='cid'>"+parametelist[i].name+"</dd></a>");
							}
							}
					 }else{
						 alert(msg);
					 }
			   }
		 });
		
	}
	function jqid(id){
		//设置查询右侧故障详细数据
		debugger;
		var device=$("#inid").val(); 
		if(id==null){
			ids=1;
		}else{
			ids=id;
		}
		$.ajax({
			   type:"post",
			   data:{"id":ids,"device_id":device},
			   url:"<%=basePath%>Parameter/parameterchildid.do",
			   success:function(data){
				   var result = JSON.parse(data);
				   if("1001" == result.code){
					   $(".tid").remove();
					   var parametelist = result.responseData.parametelist;
					   var j=1;
					   for(var i=0;i<parametelist.length;i++){
						 $("#table").append("<tr class='tid' ondblclick=showbitvalue('"+parametelist[i].bitString+"')><td>"+j+"</td><td>"+parametelist[i].addr+"</td><td>"+parametelist[i].name+"</td><td>"+parametelist[i].hvalue+"</td><td>"+parametelist[i].unit+"</td><td>"+parametelist[i].category+"</td><td>"+parametelist[i].remark+"</td></tr>");
						 j++
					   }
				   }else{
					   alert(msg);  
				   }
			   }
		});
	}

	function showbitvalue(bitstr) {
		//设置弹窗
		if(bitstr==""||bitstr==null||bitstr=="null"||bitstr=="0"){
			return;
		} 
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
			contStr1 += "</tr>";
		} 
		contStr1 += "</tbody>";
		$("#bit_table").append(contStr1);
		$('#bit_table tbody tr:even').css("backgroundColor", "#edf2f6");
		document.getElementById("bit_div").style.display = "block";
	}
	function closebit(){
		document.getElementById("bit_div").style.display = "none";
	}

</script>
<body>
	<input type="hidden" value="${param.device_id}" id="inid">
	<!-- 头部内容home -->
	<div class="hd_box">
		<div class="logo_box">
			<a href="#"><img src="<%=basePath%>/images/logo.png"></a>
		</div>
		<div class="nav_box">
			<ul>
				<li><a href="#"><i><strong class="S_10"></strong></i><span>系统信息</span></a></li>
				<li class="li_icon"><a href="#"><i><strong class="S_11"></strong></i><span>参数监控</span></a></li>
				<li><a href="#"><i><strong class="S_12"></strong></i><span>示波器</span></a></li>
				<li><a href="#"><i><strong class="S_13"></strong></i><span>调试面板</span></a></li>
				<li class="style_li"><a href="#"><i><strong
							class="S_14"></strong></i><span>首页</span></a></li>
			</ul>
			<hr />
		</div>
	</div>
	<!-- 头部内容end -->
	<!-- <div class="main_box clearfix"> -->
		<!-- 左侧菜单 -->
		<div class="leftsidebar_box">
			<dl id="wid">
				<dt>
					<a onclick="jq('<%=1%>')"><span>网侧</span><img
						src="<%=basePath%>/images/left/select_xl01.png"></a>
				</dt>
			</dl>
			<dl id="jid">
				<dt>
					<a onclick="jq('<%=2%>')"><span>机侧<img
							src="<%=basePath%>/images/left/select_xl01.png"></span></a>
				</dt>
			</dl>
			<dl id="wwid">
				<dt>
					<a onclick="jq('<%=3%>')"><span>外围接口<img
							src="<%=basePath%>/images/left/select_xl01.png"></span></a>
				</dt>
			</dl>


		</div>
		<!-- 左侧菜单end -->
		<!-- 右侧内容 -->
		<!-- <div class="right_main_div"> -->
			<div class="W_width" id="oneid">

				<!-- table内容 -->
				<div class="table_box">
					<table id="table">
						<tr>
							<td>序号</td>
							<td>地址</td>
							<td>参数名称</td>
							<td>参数值</td>
							<td>单位</td>
							<td>描述</td>
							<td>备注</td>
						</tr>
					</table>
					<!-- 新增/编辑弹窗 -->
					<div style="display: none;" class="poput_box_b" id="bit_div">
						<p class="poput_title">
							ARM辅助控制状态字 <a href="javascript:void(0)" onclick="closebit();"></a>
						</p>
						<div class="table_w table_height">
							<table class="table_S" id="bit_table">
								<thead>
									<tr>
										<th>位号</th>
										<th>名称</th>
										<th>状态</th>
									</tr>
								</thead>
								<tbody>
									<tr>
										<td>1</td>
										<td>通讯故障需停机</td>
										<td><strong class=""></strong></td>
									</tr>
									<tr>
										<td>2</td>
										<td>温度检测板通讯</td>
										<td><strong class="strong_S"></strong></td>
									</tr>
									<tr>
										<td>2</td>
										<td>温度检测板通讯</td>
										<td><strong class="strong_S"></strong></td>
									</tr>
									<tr>
										<td>2</td>
										<td>温度检测板通讯</td>
										<td><strong class="strong_S"></strong></td>
									</tr>
									<tr>
										<td>2</td>
										<td>温度检测板通讯</td>
										<td><strong class=""></strong></td>
									</tr>

								</tbody>
							</table>

							<!-- 新增/编辑弹窗end -->

						</div>

						<!-- table内容end -->
					</div>
				</div>
				<!-- 右侧内容end -->
			</div>
</body>
</html>