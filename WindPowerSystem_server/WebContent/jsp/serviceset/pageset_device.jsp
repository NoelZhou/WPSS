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
<script type="text/javascript" src="<%=basePath%>js/jquery-1.8.3.min.js"></script>
	<title>服务设置-风场概览</title>
    <link rel="stylesheet" type="text/css" href="<%=basePath%>/css/common.css">
    <link rel="stylesheet" type="text/css" href="<%=basePath%>/css/style.css">
    <script type="text/javascript" src="<%=basePath%>js/opts.js"></script>
	<script type="text/javascript" src="<%=basePath%>js/spin.js"></script>
<script type="text/javascript">
var name=0;
$(function(){
	modbuscpType();
	loadPage();
	$('li').live('click',function(){
			$(this).addClass('li_style').siblings('li').removeClass('li_style');
	}); 
	autoHeight();
});

function modbuscpType(){
	$.ajax({
		type:'post',	
		url:'<%=basePath%>CollectSet/listModbustcpType.do',
		//data:'',
		success:function(data){
			var result = JSON.parse(data);
			var msg=result.message;
			if(msg == "获取成功"){
					var str="";
			    	str+="<ul>";
			    	$.each(result.responseData.modbustcptypeList,function(i,modbustcptypeList){
			    		str+="<li ";
			    		 if(i==0){
			    			str+="class='li_style'";
			    		}
			    		str+="><a style='cursor: pointer;' onclick=loadPage('"+modbustcptypeList.id+"')>"+modbustcptypeList.name+"</a></li>"; 
			    	});
			    	str+="</ul>";
			    	document.getElementById("main_title_box").innerHTML=str;
			    }else{
			    	alert("msg");
			    }
			    
		}
	});
	
}

function loadPage(id){
	if(id==null){
		id=name;
	}
	var url = "<%=basePath%>PageSet/listDeviceBaseData.do?modbustype="+id;
	/* $.get(url,function(data){
		var result = JSON.parse(data);
		var varlist = result.responseData.dbList;
		var checkedStr = '';
		var str = "<tr><td style='width:50px;'>序号</td><td style='width:200px;'>基本参数名称</td> <td style='width:200px;''>显示设置</td></tr>";
		if(varlist.length>0){
		for(var i = 0;i < varlist.length;i++){
			str +="<tbody>"
			+"<tr><td style='width:50px;'>"+(i+1)+"</td><td>"+varlist[i].name+"</td><td> <label class='label_input padding_style'><input type='checkbox' class='input_chk' name='ids' value='"+varlist[i].id+"'"
			if(varlist[i].showtype == "1"){
				str += "checked = 'checked'";
				if(checkedStr=='') checkedStr += varlist[i].id;
			  	else checkedStr += ',' + varlist[i].id;
			}
			str +="/><span class='style01'></span></label></td></tr></tbody>";
		}
		}else{
			str += "<tr>"
				+"<td colspan='3'>没有相关数据</td>"
				+"</tr></tbody>";
		}
		$("#oldids").val(checkedStr);
		document.getElementById("mytable").innerHTML = str;
		$('#mytable tbody tr:even').css("backgroundColor", "#edf2f6");
	}); */
	$(".table_box").hide();
	$("#loading").show();
	$.ajax({
		type:'post',
		url:'<%=basePath%>PageSet/listDeviceBaseData.do',
		data:{"modbustype":id},
		success:function(data){
			$(".table_box").show();
			$("#loading").hide();
			var result = JSON.parse(data);
			var varlist = result.responseData.dbList;
			var checkedStr = '';
			var str = "<tr><td style='width:50px;'>序号</td><td style='width:100px;'>地址</td><td style='width:200px;'>基本参数名称</td> <td style='width:200px;''>显示设置</td></tr>";
			if(varlist.length>0){
			for(var i = 0;i < varlist.length;i++){
				str +="<tbody>"
				+"<tr><td style='width:50px;'>"+(i+1)+"</td><td>"+varlist[i].addr+"</td><td>"+varlist[i].name+"</td><td> <label class='label_input padding_style'><input type='checkbox' class='input_chk' name='ids' value='"+varlist[i].id+"'"
				if(varlist[i].showtype == "1"){
					str += "checked = 'checked'";
					if(checkedStr=='') checkedStr += varlist[i].id;
				  	else checkedStr += ',' + varlist[i].id;
				}
				str +="/><span class='style01'></span></label></td></tr></tbody>";
			}
			}else{
				str += "<tr>"
					+"<td colspan='4'>没有相关数据</td>"
					+"</tr></tbody>";
			}
			$("#oldids").val(checkedStr);
			document.getElementById("mytable").innerHTML = str;
			$('#mytable tbody tr:even').css("backgroundColor", "#edf2f6");
			
		}});
	
}

function save(){
	var isture=null;
	isture=$(".A_button").attr("disabled");
		if(isture!=null && isture=="disabled"){
			return false;
		}
	var str = '';
	for(var i=0;i < document.getElementsByName('ids').length;i++){
		if(document.getElementsByName('ids')[i].checked){
		  	if(str=='') str += document.getElementsByName('ids')[i].value;
		  	else str += ',' + document.getElementsByName('ids')[i].value;
		 }
	}
	var oldids = $("#oldids").val();
	var url = "<%=basePath%>PageSet/updateDeviceBaseData.do?paramStr="+str+"&oldparm="+oldids;
	$.post(url,function(data){
		var result = JSON.parse(data);
		if("1001" == result.code){
			alert("保存成功");
			window.location.reload();
		}
	});
	$(".A_button").attr("disabled","disabled");
}

function autoHeight(){
	var height = $(window).height()-310;
    $("#autoarea").attr("style","min-height:"+height+"px;max-height:"+height+"px");
}
</script>
</head>
<body>
	<!-- <table id="mytable" style="border:1px solid #a0c6e5;">
	</table>
	<input type="hidden" id="oldids" name="oldids"/>
	<input type="button" onclick="save();" value="保存"/> -->
	
	 <!-- 头部内容home -->
    
    <!-- 头部内容end -->
    <div class="main_box clearfix">
   
        <!-- 左侧菜单 -->
        
        <!-- 左侧菜单end -->
        <!-- 右侧内容 -->
        <div class="right_main_div0">
            <div class="W_width">
                <!-- 头部切换标签 -->
                <p class="P_Label clearfix input_style">风机显示参数设置</p>
                <div class="main_title_box" id="main_title_box">
                    <!-- <ul>
                        <li class="li_style"><a href="#">双馈</a></li>
                        <li class=""><a href="#">全功率</a></li>
                        <li class=""><a href="#">海上风电</a></li>
                    </ul> -->
                </div>
              
                <!-- 头部切换标签end -->
                <!-- 编辑标签 -->
                <!-- <p class="P_Label clearfix input_style"></p> -->
                <!-- 编辑标签end -->
                <!-- table内容 -->
                 <a class="A_button" style="cursor: pointer;margin-bottom: 10px;margin-top: 10px;float:left;" onclick="save();">保存</a>
                <div id="loading" style="margin:200px 500px;"></div>
                <div class="table_box" id="autoarea">
               
					<table id="mytable">
                       
                    </table>
                     <input type="hidden" id="oldids" name="oldids"/>
                </div>
                <!-- table内容end -->
            </div>
        </div>
        <!-- 右侧内容end -->
    </div>
   <script>
    $(function() { // dom元素加载完毕
        $('#table tbody tr:even').css("backgroundColor", "#edf2f6");
        //获取id为tb的元素,然后寻找他下面的tbody标签，再寻找tbody下索引值是偶数的tr元素,改变它的背景色.
        //tr:odd为奇数行，索引从0开始，0算偶数。
    })
    </script>

	
</body>
</html>