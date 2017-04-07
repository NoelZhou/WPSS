$(function() {
	modbuscpType();
	//loadPage();
	$('li').live('click', function() {
		$(this).addClass('li_style').siblings('li').removeClass('li_style');
	});
	autoHeight();
	initAllCheckBox("chk_all", "chkids");
});

$(function() { 
	// dom元素加载完毕
    $('#table tbody tr:even').css("backgroundColor", "#edf2f6");
    //获取id为tb的元素,然后寻找他下面的tbody标签，再寻找tbody下索引值是偶数的tr元素,改变它的背景色.
    //tr:odd为奇数行，索引从0开始，0算偶数。
})

/**
 * 加载数据
 * @param id
 */
function loadPage(modbusType) {
	if (modbusType == null) {
		modbusType = 0;
	}
	$("#loading").show();
	$("#W_width1").hide();
	$("#chk_all").attr("checked",false);
	
	$.ajax({
		type : 'post',
		url : basePath + 'wfp/firmwareUpgrade/selectAllList.do',
		data : {
			"modbusType" : modbusType
		},
		success : function(data) {
			$("#loading").hide();
			$("#W_width1").show();
			var result = JSON.parse(data);
			var varlist = result.responseData.firmwareUpgradeList;
			var checkedStr = '';
			var str = "<tbody><tr>";
			if (varlist.length > 0) {
				for (var i = 0; i < varlist.length; i++) {
					str += "<tr>";
					str += "<td width='58px;'>";
					str += "	<label class='label_input padding_style'>";
					str += "		<input type='checkbox' class='input_chk' name='chkids' onclick='allchk(\"chk_all\", \"chkids\")' value='" + varlist[i].id + "' />";
					str += "		<span class='style01'></span>";
					str += "	</label></td>";
					str += "<td style='width:58px;'>"+ (i + 1)+ "</td>";
					str += "<td style='width:280px;'><a href='javascript:showDevice(\""+ varlist[i].id +"\", \""+ varlist[i].ip +"\", \""+ varlist[i].port +"\");'>" + varlist[i].name + "</a></td>";
					str += "<td style='width:120px;'>" + varlist[i].ip + "</td>";
					str += "<td>" + varlist[i].port + "</td>";
					
					str += "</tr>";
				}

			} else {
				str += "<tr><td colspan='3'>没有相关数据</td></tr>";
			}
			str += "</tbody>";

			$("#oldids").val(checkedStr);
			document.getElementById("mytable").innerHTML = str;
			$('#mytable tbody tr:even').css("backgroundColor", "#edf2f6");
		}
	});

}

/**
 * 变流器类型加载
 */
function modbuscpType(){
	$.ajax({
		type:'post',	
		url:basePath + 'CollectSet/listModbustcpType.do',
		success:function(data){
			var result = JSON.parse(data);
			var msg=result.message;
			if(msg == "获取成功"){
				var firstid;
				var str="";
				str+="<ul>";
			   	$.each(result.responseData.modbustcptypeList,function(i,modbustcptypeList){
			   		if(modbustcptypeList.id != 2){
			   			str+="<li ";
				    	if(i==0){
				    		str+="class='li_style'";
				    		firstid = modbustcptypeList.id;
			    		} 
				    	str+="><a style='cursor: pointer;' onclick=loadPage('"+modbustcptypeList.id+"')>"+modbustcptypeList.name+"</a></li>";
			   		}
			   	});
			   	str+="</ul>";
			   	document.getElementById("main_title_box").innerHTML=str;
			   	loadPage(firstid);
			}else{
				alert("msg");
			}
			    
		}
	});
}

/**
 * 升级
 */
function toUpload(){
	var chkids = getValue('chkids');
	if(chkids.length == 0){
		alert('请选择要升级的变流器。');
		return;
	}
	$("#deviceIds").val(chkids);
	
//	alertwindow("选择文件", $(".poput_box_b").html());
	$(".poput_box_b").show();
//	$("#W_width1").hide();
	$("#loading").hide();
}

/**
 * 展示设备信息
 */
function showDevice(deviceId, ip, port){
//	$("#poput_device_box_b").show();
	$("#W_width1").hide();
//	$("#loading").hide();
	
	$("#loading").show();
	
	$.ajax({
		type : 'post',
		url : basePath + 'wfp/firmwareUpgrade/getDeviceInfo.do',
		data : {
			"deviceId" : deviceId,
			"ip" : ip,
			"port" : port
		},
		success : function(data) {
			$("#loading").hide();
			$("#W_width1").show();
			var result = JSON.parse(data);
			if(result.code == 1002){
				alert(result.message);
				return;
			}
			
			var detail = result.responseData.firmwareUpgradeDeviceInfoDetail;
			var str = '<div class="table_w01"><table>';
			if (detail != null) {
				str += '<tr>';
				str += '	<td colspan="4" style="text-align: left;">&nbsp;&nbsp;<b>应用属性信息</b></td>';
				str += '</tr>';
				str += '<tr>';
				str += '	<td style="text-align: right;" width="20%">序列号：</td>';
				str += '	<td style="text-align: left;" width="30%">'+detail.serialNumber+'</td>';
				str += '	<td style="text-align: right;" width="20%">设备类型：</td>';
				str += '	<td style="text-align: left;" width="30%">'+detail.deviceType+'</td>';
				str += '</tr>';
				str += '<tr>';
				str += '	<td style="text-align: right;">设备型号：</td>';
				str += '	<td style="text-align: left;">'+detail.deviceModule+'</td>';
				str += '	<td style="text-align: right;">设备版本：</td>';
				str += '	<td style="text-align: left;">'+detail.deviceVersion+'</td>';
				str += '</tr>';
				str += '<tr>';
				str += '	<td style="text-align: right;">MAC1：</td>';
				str += '	<td style="text-align: left;">'+detail.mac1+'</td>';
				str += '	<td style="text-align: right;">MAC2：</td>';
				str += '	<td style="text-align: left;">'+detail.mac2+'</td>';
				str += '</tr>';
				str += '<tr>';
				str += '	<td style="text-align: right;">属性协议号：</td>';
				str += '	<td style="text-align: left;">'+detail.propertyAgreementNumber+'</td>';
				str += '	<td style="text-align: right;">属性协议版本：</td>';
				str += '	<td style="text-align: left;">'+detail.propertyAgreementVersion+'</td>';
				str += '</tr>';
				str += '<tr>';
				str += '	<td style="text-align: right;">应用程序协议号：</td>';
				str += '	<td style="text-align: left;">'+detail.applicationProtocolNumber+'</td>';
				str += '	<td style="text-align: right;">应用程序协议版本：</td>';
				str += '	<td style="text-align: left;">'+detail.applicationProtocolVersion+'</td>';
				str += '</tr>';
				str += '<tr>';
				str += '	<td colspan="4" style="text-align: left;">&nbsp;&nbsp;<b>维护属性信息</b></td>';
				str += '</tr>';
				str += '<tr>';
				str += '	<td style="text-align: right;">维护协议号：</td>';
				str += '	<td style="text-align: left;">'+detail.maintenanceContractNumber+'</td>';
				str += '	<td style="text-align: right;">固件版本数量：</td>';
				str += '	<td style="text-align: left;">'+detail.firmwareVersionNumber+'</td>';
				str += '</tr>';
				str += '<tr>';
				str += '	<td style="text-align: right;">IAC协议号：</td>';
				str += '	<td style="text-align: left;">'+detail.iacAgreementNumber+'</td>';
				str += '	<td style="text-align: right;">IAC协议版本：</td>';
				str += '	<td style="text-align: left;">'+detail.iacAgreementVersion+'</td>';
				str += '</tr>';
				str += '<tr>';
				str += '	<td style="text-align: right;">IAD协议号：</td>';
				str += '	<td style="text-align: left;">'+detail.iadAgreementNumber+'</td>';
				str += '	<td style="text-align: right;">IAD协议版本：</td>';
				str += '	<td style="text-align: left;">'+detail.iadAgreementVersion+'</td>';
				str += '</tr>';
				str += '<tr>';
				str += '	<td style="text-align: right;">IAP协议号：</td>';
				str += '	<td style="text-align: left;">'+detail.iapAgreementNumber+'</td>';
				str += '	<td style="text-align: right;">IAP协议版本：</td>';
				str += '	<td style="text-align: left;">'+detail.iapAgreementVersion+'</td>';
				str += '</tr>';
				str += '<tr>';
				str += '	<td style="text-align: right;">IAT协议号：</td>';
				str += '	<td style="text-align: left;">'+detail.iatAgreementNumber+'</td>';
				str += '	<td style="text-align: right;">IAT协议版本：</td>';
				str += '	<td style="text-align: left;">'+detail.iatAgreementVersion+'</td>';
				str += '</tr>';
				str += '<tr>';
				str += '	<td style="text-align: right;">MCU固件版本号1：</td>';
				str += '	<td style="text-align: left;">'+detail.mcuFirmwareVersionNumber1+'</td>';
				str += '	<td style="text-align: right;">MCU固件版本号2：</td>';
				str += '	<td style="text-align: left;">'+detail.mcuFirmwareVersionNumber2+'</td>';
				str += '</tr>';
				str += '<tr>';
				str += '	<td style="text-align: right;">MCU固件版本号3：</td>';
				str += '	<td style="text-align: left;" colspan="3">'+detail.mcuFirmwareVersionNumber3+'</td>';
				str += '</tr>';
			} else {
				str += "<tr><td colspan='4'>没有相关数据</td></tr>";
			}
			str += "</table></div>";
			
			
			alertwindow("查看",str);
			
//			document.getElementById("device_box").innerHTML = str;
		}
	});
}

function alertwindow(title,str){
	var wtd = window.top.document;
   	$("#gray",wtd).show();
   	$("#alertwindow",wtd).show();
   	$("#titletxt",wtd).html(title);
   	$("#contentdiv",wtd).html(str);
   	$('#table tbody tr:even',wtd).css("backgroundColor", "#edf2f6");
}

/**
 * 更新固件
 */
var updateTimeout;
var updateMsg;
function uploadFile(){
	var excelPath = $("#textfield").val();
	var suffixname = excelPath.substr(excelPath.lastIndexOf(".")).toLowerCase();
	if(suffixname != '.sgu'){
        alert("请上传后缀名为sgu的文件");
        return;
    }
	
	$(".poput_box_b").hide();
	
	showProgress();
   updateMsg = setInterval(function(){updateSessionMsg()},2000);
   updateTimeout = setInterval(function(){updateProgress()}, 2000);
	
	var formData = new FormData($("#Form")[0]);  
    $.ajax({  
         url: basePath + 'wfp/firmwareUpgrade/fileUpload.do' ,  
         type: 'POST',  
         data: formData,  
         async: true,  
         cache: false,  
         contentType: false,  
         processData: false,  
         success: function (returndata) {
             var result = JSON.parse(returndata); 
             if(result == null){
            	 return;
             }
         }
    });
}

/**
 * 显示升级进度弹出框
 */
function showProgress(){
	var wtd = window.top.document;
	$("#gray", wtd).show();
   	$("#alertwindow", wtd).show();
   	$("#titletxt", wtd).html("升级进度");
   	$("#contentdiv", wtd).html("");
}

/**
 * 更新进度信息
 * @param deviceName
 * @param progressValue
 */
function updateProgress(){
    $.ajax({  
         url: basePath + "wfp/firmwareUpgrade/getProgress.do" ,  
         type: 'POST',  
         async: true,  
         cache: false,  
         contentType: false,  
         processData: false,  
         success : function(returndata) {
        	var obj = JSON.parse(returndata); 
        	var upgradeDeviceName = obj.responseData.upgradeDeviceName;
     		var upgradeDeviceProgress = obj.responseData.upgradeDeviceProgress;
     		var upgradeDeviceOver = obj.responseData.upgradeDeviceOver;
     		var result = "";
			if (upgradeDeviceName != null && upgradeDeviceName != '') {
				result += upgradeDeviceName;
			}
			
			if (upgradeDeviceProgress != null && upgradeDeviceProgress != '') {
				result += upgradeDeviceProgress;
			}
			
			if (upgradeDeviceOver != null && upgradeDeviceOver != '') {
				result += "<br/><br/>" + upgradeDeviceOver;
			}

			var wtd = window.top.document;
			$("#contentdiv", wtd).html(result);
		}
    });
}
function updateSessionMsg(){
    $.ajax({  
         url: basePath + "wfp/firmwareUpgrade/updateMsg.do" ,  
         type: 'POST',  
         async: true,  
         cache: false,  
         contentType: false,  
         processData: false,  
         success : function(returndata) {
        	 var obj = JSON.parse(returndata); 
        	 var code = obj.code;
        	 $("#endCode").val(obj.code);
        	 if(code == 0){
        		 clearInterval(updateMsg);
        		 clearInterval(updateTimeout);
 				 updateProgress();
        	 }
         }
    });
}

/**
 * 关闭选择上传文件DIV
 */
function closediv(){
	$(".poput_box_b").hide();
	$("#W_width1").show();
}

/**
 * 关闭选择上传文件DIV
 */
function closedevicediv(){
	$("#poput_device_box_b").hide();
	$("#W_width1").show();
}

/**
 * 弹出选择框
 */
function selectedFile(){
	$("#fileField").click();
}

/**
 * 初始化checkbox全选/取消事件
 * @param allId
 * @param childName
 */
function initAllCheckBox(allId, childName){
	//全选或全不选 
    $("#"+allId).click(function(){    
        if(this.checked){    
            $("input[name='"+childName+"']").attr("checked", true);   
        }else{    
            $("input[name='"+childName+"']").attr("checked", false); 
        }    
	});  
}

/**
 * 设置是否全选
 * @param allId
 * @param childName
 */
function allchk(allId, childName){ 
    var chknum = $("input[name='"+childName+"']").size();//选项总个数 
    var chk = 0; 
    
    var chks = document.getElementsByName(childName);
	for(var i = 0; i < chks.length; i++){
		if(chks[i].checked){
			chk++;
		}
	}
	
    if(chknum == chk){//全选 
        $("#"+allId).attr("checked",true); 
    }else{//不全选 
        $("#"+allId).attr("checked",false); 
    } 
}

/**
 * 获取复选框选中值
 * @param childName
 */
function getValue(childName){ 
    var valArr = new Array(); 
    var chks = document.getElementsByName(childName);
    var dataNum = 0;
	for(var i = 0; i < chks.length; i++){
		if(chks[i].checked){
			valArr[dataNum] = chks[i].value; 
			dataNum++;
		}
	}
    return valArr.join(',');
}

function autoHeight() {
	var height = $(window).height() - 310;
	$("#autoarea").attr("style","min-height:" + height + "px;max-height:" + height + "px");
	$("#loading").attr("style","margin:"+height/2+"px "+height+"px");
			
}