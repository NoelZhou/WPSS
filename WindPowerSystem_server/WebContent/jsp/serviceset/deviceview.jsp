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
<link rel="stylesheet" type="text/css" href="<%=basePath%>css/common.css">
<link rel="stylesheet" type="text/css" href="<%=basePath%>/css/style.css">
<script type="text/javascript" src="<%=basePath%>js/jquery-1.8.3.min.js"></script>
<script type="text/javascript" src="<%=basePath%>js/popup.js"></script>
<script type="text/javascript" src="<%=basePath%>js/opts.js"></script>
<script type="text/javascript" src="<%=basePath%>js/spin.js"></script>

<title>风场预览</title>
<script type="text/javascript">
	 var checkedid = null;
	 var checked_li;
	 var isOpen = true;
	 var clzid = null;
	 var device_id = null;
	 var list_i=null;
	 /* loadPage(); */
	 $('li').live('click',function(){
	 	$(this).addClass('li_bg').siblings('li').removeClass('li_bg');
	 });
var timer = setInterval(loadPage,2000); 
$.ajaxSetup({  
    async : false  
});
/**
 * 加载设备风机
 */
function loadPage(){
	var startTime = (new Date()).getTime();
	var loadValue=$("#loadValue").val();
	if(loadValue=="undefined"){
		$("#W_width").hide();
		$("#loading").show(); 
		$("#loadValue").val(1);
	}
	
	var url = "<%=basePath%>DataCq/list.do";
	$.post(url,function(data){
		var result = JSON.parse(data);
		var errornum = result.responseData.errornum;
		var waitnum = result.responseData.waitnum;
		var bwnum = result.responseData.bwnum;
		var totalnum = result.responseData.varList.length;
		var alarm = result.responseData.alarm;
		//$("#errornum").val(totalnum-waitnum-bwnum);
		$("#errornum").val(errornum);
		$("#waitnum").val(waitnum);
		$("#bwnum").val(bwnum);
		$("#totalnum").val(totalnum);
		$("#ycnum").val(totalnum - errornum - waitnum - bwnum);
		$("#alarm").val(alarm);
		var varList = result.responseData.varList;
		var firstid = varList[0].id;
		var device_Id=varList[0].device_id;
		var diviceName=varList[0].name;
		if(list_i != null){
			firstid = varList[list_i].id;
			device_Id=varList[list_i].device_id;
			diviceName=varList[list_i].name;
		}
		var deviceAll;
		var str = "";
		var errordiv = "";
		$.each(varList,function(i){
			//map = varList[i].map;
			deviceAll=varList[i].deviceAll;
			var runstate = varList[i].runstate;
			var stateVal = "";
			var hasData = true;
			if(runstate == "bw"){
				stateVal = "并网";
			}else if(runstate == "error"){
				stateVal = "故障";
			}else if(runstate == "wait"){
				stateVal = "待机";
			}else if(runstate == "alarm"){
				stateVal = "告警";
			}else if(runstate == "networkerror"){
				stateVal = "网络故障";
			}
			else{
				stateVal = "";
			}
			var data_id = varList[i].id;
			if(data_id == 0){
				hasData = false;
			}
			str += "<li id='li"+i+"' onclick = 'changeli(this);'><a ondblclick='viewDj("+varList[i].device_id+","+hasData+")'; onclick=viewDetai('"+varList[i].id+"','"+varList[i].device_id+"','"+varList[i].name+"','"+i+"');><div "
			if("error" == runstate){
				str += "class='top_box'";
			}else if("bw" == runstate){
				str += "class='top_box1'";
			}else{
				var djzsval = deviceAll["电机转速"];
				if(djzsval != null){
					var djzsnum = djzsval.split("rpm")[0];
					if((djzsnum == "0" || djzsnum == "0.0")){
						str += "class='top_box'";
					}else{
						str += "class='top_box1'";
					}
				}else{
					str += "class='top_box'";
				}
			}
			str += "><i></i><p>运行状态&nbsp&nbsp&nbsp:&nbsp&nbsp<span ";
			if(stateVal=="故障"||stateVal=="网络故障"){
				str +="style='color:red;font-weight:bold;'";
			}else if(stateVal=="告警"){
				str +="style='color:red;font-weight:bold;'";
			}
			str +=">"+stateVal+"</span></p>";
			/* for(var key in map){
				str += "<p title='"+key+":"+map[key]+"'>"+getSubStr(key,4)+":&nbsp&nbsp"+map[key]+"</p>";
			} */
			$.each(deviceAll,function(j,deviceAll){
				var deviceValue=deviceAll.split("$");
				str += "<p title='"+deviceValue[0]+":"+deviceValue[1]+"'>"+getSubStr(deviceValue[0],4)+":&nbsp&nbsp"+deviceValue[1]+"</p>";
			});
			str += "</div><p class='text_box'>"+varList[i].name+"</p></a></li>";
		
			
		});
		
		 $("#W_width").show();
		 $("#loading").hide(); 
	 	document.getElementById("deviceul").innerHTML = str;
		//$(".box_lef").append(errordiv);
		//页面加载完成之后，被选中的li背景色依然存在
		$("#"+checked_li).addClass('li_bg');
		viewDetai(firstid,device_Id,diviceName,list_i);
		//loadPage();
	});
}	
	
/**
 * 右侧选中风机详细信息
 */
function viewDetai(id,deviceid,deviceName,varList_i){
	
	if(varList_i !=null){
		list_i= varList_i;
	}
	
	var url = "<%=basePath%>DataCq/getThisDevice.do?device_id="+deviceid+"&id="+id;
	$.post(url,function(data){
		var result = JSON.parse(data);
		//var map = result.responseData.dataCq.map;
		var deviceAll = result.responseData.deviceAll;
		var errormap = result.responseData.errormap;
		var contStr = "";
		var errorStr = "";
		$.each(deviceAll,function(i,deviceAll){
			var deviceValue=deviceAll.split("$");
			contStr += "<tr><td title='"+deviceValue[0]+"'>"+getSubStr(deviceValue[0],8)+"</td><td title='"+deviceValue[1]+"'>"+getSubStr(deviceValue[1],8)+"</td><td>"+deviceValue[2]+"</td></tr>";
	 	});
		for(var errorkey in errormap){
			var errorArray = errorkey.split(",");//得到 状态码，地址
			errorStr += "<tr><td title='"+errormap[errorkey]+"'>"+getSubStr(errormap[errorkey],8)+"</td><td><a style='cursor:pointer' onclick=viewthis('"+id+"','"+errorArray[1]+"','"+deviceid+"','"+errormap[errorkey]+"');>"+errorArray[0]+"</a></td></tr>";
		}
		$("#T_tbody").html(contStr);
		$("#A_tbody").html(errorStr);
		$("#diveceName").html(deviceName);
	});
}	

function changeli(obj){
	checked_li = obj.id;
}

//字符多余隐藏
function getSubStr(obj,num){
	var len = obj.length;
	var subval = obj;
	if(len > num){
		subval = obj.substring(0,num)+"...&nbsp";
	}
	if(len==num){
		subval = obj.substring(0,num)+"&nbsp&nbsp&nbsp";
	}
	return subval;
}

/**
 * 查看故障错误信息
 */
function viewError(id,device_id){
	var url = "<%=basePath%>DataCq/getErrorInfo.do?id="+id;
	$.post(url,function(data){
		var result = JSON.parse(data);
		var errormap = result.responseData.errormap;
		var i = 0;
		var errorstr = "<table class='table_S' style='margin:0' id='table'> <thead><tr><th>序号</th><th>故障信息</th><th>状态</th><th>专家库</th></tr></thead><tbody>";
		for(var key in errormap){
			i++;
			errorstr += "<tr><td>"+i+"</td><td>"+key+"</td><td><strong class='strong_S'></strong></td><td><a style='cursor:pointer' onclick=document.getElementById('iframe').contentWindow.look('"+key+"','"+device_id+"');>查看</a></td></tr>";
		}
		errorstr += "</tbody></table>";
		alertwindow("查看",errorstr)
	});
}

/**
 * 查看专家库
 */
function look(errormessage,device_id){
	$.ajax({
		type:'post',
		url:'<%=basePath%>/ExpertDatabase/lookExpertDatabase.do',
		data:{"device_id":device_id,"errormessage":errormessage},
		success:function(data){
			var result = JSON.parse(data);
			var msg = result.code;
			var list=result.responseData.Expetrmessage;
			if(msg=="1001"){
				if(list.length==1){
					alert("专家库内容为空");
					return false;
				}
				var str="<br/>&nbsp;<textarea style='border:1px solid #488fd2' cols=95 rows=10 readonly>"+list+"</textarea>";
				alertwindow2("专家库",str)
			}else{
				alert("数据获取失败!");
			}
		}
	});
}

function viewthis(id,addr,device_id,errorName){
	var url = "<%=basePath%>DataCq/viewErrorCode.do?id="+id+"&addr="+addr;
	$.post(url,function(data){
		var result = JSON.parse(data);
		var errormap = result.responseData.errormap;
		var i = 0;
		var errorstr = "<table class='table_S' style='margin:0' id='table'> <thead><tr><th>序号</th><th>故障信息</th><th>状态</th><th>专家库</th></tr></thead><tbody>";
		for(var key in errormap){
			if(key!=""){
			i++;
			errorstr += "<tr><td>"+i+"</td><td>"+key+"</td>";
			if("1" == errormap[key]){
				if(errorName=="系统告警"){
					errorstr += "<td><strong style='background: #fff45c'></strong></td>";
				}else{
					errorstr += "<td><strong class='strong_S'></strong></td>";
				}
			}else{
				errorstr += "<td><strong></strong></td>";
			}
			errorstr += "<td><a style='cursor:pointer' onclick=document.getElementById('iframe').contentWindow.look('"+key+"','"+device_id+"');>查看</a></td>";
			errorstr += "</tr>";
			}
		}
		errorstr += "</tbody></table>";
		alertwindow("查看",errorstr)
	});
}

/**
 * 单机调试
 */
function viewDj(deviceid,hasData){
    var url="";
	url="<%=basePath%>Devicetail/chooseMenu.do?device_id="+deviceid+"&modbus_type=-1"+"&type=1"+"&modbusid=-1";
	top.location.href=url;
	<%-- top.location.href="<%=basePath%>jsp/devicedetailjsp/DeviceSystemParame.jsp?device_id="+deviceid; --%>
	/* if(hasData){
		
	}else{
		alert("该设备尚未采集数据，不可进行单机调试!");
	} */
	
}

function closediv(data){
	$("#errordiv"+data).remove();
	isOpen = false;
	clzid = data;
	window.setTimeout(function(){
		isOpen = true;
		clzid = null;
	},20000);
}

function alertwindow(title,str){
	var wtd = window.top.document;
   	$("#gray",wtd).show();
   	$("#alertwindow",wtd).show();
   	$("#titletxt",wtd).html(title);
   	$("#contentdiv",wtd).html(str);
   	$('#table tbody tr:even',wtd).css("backgroundColor", "#edf2f6");
}
//专家库弹框
function alertwindow2(title,str){
	var wtd = window.top.document;
	$("#alertwindow",wtd).hide();
	$("#alertwindow2",wtd).show();
   	$("#titletxt2",wtd).html(title);
   	$("#contentdiv2",wtd).html(str);
}
function autoHeight(){
	var height = $(window).height()-190;
	$("#loading").attr("style","margin:"+height/2+"px "+height+"px");
    $("#autoarea").attr("style","min-height:"+height+"px;max-height:"+height+"px");
    $(".box_lef").attr("style","height:"+height+"px");
    $(".T_table").attr("style","height:"+((height/2)-20)+"px");
    $(".B_table").attr("style","margin-top:10px;height:"+((height/2)-50)+"px"); 
}
</script>
</head>
<body>
	 		
	<div class="main_box clearfix" >
        <!-- 右侧内容 -->
        <div class="W_width" id="W_width">
            <!-- 编辑标签 -->
            <p class="P_Label clearfix input_style">
			                总风机数 <input type="text" id="totalnum" readonly="readonly"/>
			                故障风机数 <input type="text" id="errornum" readonly="readonly"/>
			 <!--   异常风机数 <input type="text" id="ycnum" readonly="readonly" /> -->
			                待机风机数 <input type="text" id="waitnum" readonly="readonly"/>
			                并网风机数 <input type="text" id="bwnum" readonly="readonly"/>
			               告警风机数 <input type="text" id="alarm" readonly="readonly"/>
            </p>
            <!-- 编辑标签end -->
             <input type="hidden" id="loadValue" value="0" />
	 		<div id="loading" style="margin:0 600px;"></div>
            
            <!-- table内容 -->
            <div class="table_box_deviceview" id="autoarea" >
                <!-- 左侧风机状态 -->
                <div class="box_lef">
                    <ul id="deviceul" class="clearfix">
                    </ul>
                </div>
                <!-- 左侧风机状态end -->
                <!-- 右侧风机故障/告警信息 -->
                <div class="box_right">
                <h4><span id="diveceName"></span></h4>
                    <div class="T_table">
                        <table id="table">
                            <thead>
                                <tr>
                                    <th style="width:45%">名称</th>
                                    <th style="width:30%">值</th>
                                    <th style="width:25%">单位</th>
                                </tr>
                            </thead>
                            <tbody id="T_tbody">
                               
                            </tbody>
                        </table>
                    </div>
                           <h4>故障/告警信息</h4>
                    <div class="B_table">
                     <!--    <h4>故障/告警信息</h4> -->
                        <table id="tableA">
                            <thead>
                                <tr>
                                	<th style="width:60%">描述</th>
                                    <th style="width:40%">故障码 </th>
                                </tr>
                            </thead>
                            <tbody id="A_tbody">
                                
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
    <script type="text/javascript" src="<%=basePath%>js/jquery.js"></script>
    <script>
    $(function() { // dom元素加载完毕
        $('#table tbody tr:even').css("backgroundColor", "#edf2f6");
        $('#tableA tbody tr:even').css("backgroundColor", "#edf2f6");
        autoHeight();
    })
    </script>
</body>
</html>