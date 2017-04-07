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
    <title>录波管理</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" type="text/css" href="<%=basePath%>/css/common.css">
    <link rel="stylesheet" type="text/css" href="<%=basePath%>/css/style.css">
    <script type="text/javascript" src="<%=basePath%>/js/jquery-1.8.3.min.js"></script>
    <script type="text/javascript" src="<%=basePath%>/js/My97DatePicker/WdatePicker.js"></script>
    <script type="text/javascript" src="<%=basePath%>js/opts.js"></script>
	<script type="text/javascript" src="<%=basePath%>js/spin.js"></script>
    <script type="text/javascript">
    	$(function(){
    		fuzzyquery();
    		searchtest();
    		autoHeight();
    	});
    	
    	//查询
    	function searchtest(){
    		var loadValue=$("#loadValue").val();
    		if(loadValue=="0"){
    			$(".W_width").hide();
    			$("#loading").show(); 
    			$("#loadValue").val(1);
    		}
    		var device_name=$("#device_name").val();
    		var errorname=$("#errorname").val();
    		var start_time=$("#start").val();
    		var end_time=$("#end").val();
    		$.ajax({
    			type:'post',
    			url:'<%=basePath%>/RecordingManagement/list.do',
    			data:{"device_name":device_name,"errorname":errorname,"start_time":start_time,"end_time":end_time},
    			success:function(data){
    				$(".W_width").show();
    				$("#loading").hide();
    				var result = JSON.parse(data);	
     			   	var msg=result.message;
     			   	var list=result.responseData.recordingManagementList;
     			   	if(msg == "获取成功"){
     			   		var str="";
     			    		str+="<thead><tr><th style='width:5%;'><label class='label_input01'><span></span></label></th><th style='width:5%;'>序号</th><th style='width:18%;'>变流器名称</th><th style='width:15%;'>故障类型</th><th style='width:15%;'>发生时间</th><th style='width:15%;'>入库时间</th>"
     			    		+"<th style='width:10%;'>操作</th></tr></thead><tbody>";
     			    	if(list.length>0){
     			    		$.each(list,function(i,list){
     			    			str+="<tr><td><label class='label_input01'><input id='checkTo'  name='checkTo' title="+list.device_id+','+list.error_excel+" class='input_chk' type='checkbox'></input><span></span></label></td><td>"+(i+1)+"</td><td>"+list.device_name+"</td><td>"+list.errorname+"</td><td>"+list.error_time+"</td><td>"+list.create_time+"</td><td>";
     			    			if(list.error_excel!=""){
     			    				str+="<a class='a_icon a_icon_0a' style='cursor:pointer;' onclick=downfile('"+list.error_excel+"','"+list.device_name+"');>下载</a>";
     			    			}
     			    			str+="</td></tr>";
     			    		});
     			    	}else{
     			    		str += "<tr>"
    						+"<td colspan='7'>没有相关数据</td>"
    						+"</tr>";
     			    	}
     			    	$("#table").html(str);
     			    	$('#table tbody tr:even').css("backgroundColor", "#edf2f6");
     			    }else{
     			    	alert(msg);	
     			    }
     			    
    			}
    		});
    	}
    	//模糊查询
    	function fuzzyquery(){
    		var str="";
    		str+="<p class='P_Label01 clearfix input_style'>变流器名称<input type='text' id='device_name' name='device_name'>"
			   		+"事件类型 &nbsp<select id='errorname' name='errorname' style='width:150px;'>"
			   		+"<option value='0' selected>请选择事件类型</option>"
			   		+"<option value='1'>网侧故障</option>" 
			   		+"<option value='2'>机侧故障</option>" 
			   		+"</select>&nbsp&nbsp时间段<input  type='text' id='start'  onclick=Wdate(); style='width:190px; margin-right:10px;' />---"
			   		+" <input type='text' id='end' onclick=Wdate(); style='width:190px;'/>"
	                +"<a class='A_a' style='cursor: pointer;margin-left: 1%;' onclick='searchtest()'>查询</a><a class='A_a' style='cursor: pointer;margin-left: 1%;' onclick='boxfx()'>波形分析</a></p> "
					$("#editLabel").html(str);
    	}
    	function Wdate(){
    		WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'});
    	}
    	function boxfx(){
    		var id='';  //菜单id
     		for(var i=0;i < document.getElementsByName('checkTo').length;i++){
				  if(document.getElementsByName('checkTo')[i].checked){    //选中
					  if(id=='') id +=document.getElementsByName('checkTo')[i].title;
					  	else id += ',' + document.getElementsByName('checkTo')[i].title; 
					}
			}
    		if(id!=''){
	     		 var  num =id.split(',');
				  if(num.length>2){
					  alert("勾选的太多,无法编辑,请选择一个在继续查看波形分析!");
					  return false;
				  }
				  if(num.length==2){
					  window.location.href ="<%=basePath%>jsp/recordingManagement/waveanalyze.jsp?device_id="+num[0]+"&filepath="+encodeURI(encodeURI(num[1]));
					}
    		}else{
    			window.location.href ="<%=basePath%>jsp/recordingManagement/waveanalyzenoparm.jsp";
    		}
    	}
    	//下载
    	function downfile(file,device_name){
    		if(device_name!=''){
    			device_name=encodeURI(encodeURI(device_name));
    			file=encodeURI(encodeURI(file));
  		   }
    		window.location.href="<%=basePath%>/RecordingManagement/downfile.do?file="+file+"&device_name="+device_name;
    	}
    	
    	function autoHeight(){
    		var autoheight = $(window).height()-188;
    	    $("#autoarea").attr("style","min-height:"+autoheight+"px;max-height:"+autoheight+"px");
    	}
    </script>
    
</head>
<body>
	<div id="loading" style="margin:300px 600px;"></div>
	 <!-- 头部内容home -->
    
    <!-- 头部内容end -->
    <div class="main_box clearfix">
        <!-- 右侧内容 -->
        <div class="W_width">
            <!-- 编辑标签 -->
			
			<div id="editLabel">
			
			</div>
			<input type="hidden" id="loadValue" value="0" />
   		 	
           <!--  <p class="P_Label01 clearfix input_style">
                变流器名称
                <input type="text" value="10"> 事件类型
                select 下拉菜单
                <strong class="stronta">
                <input onclick="hide('HMF-1')" type="text" readonly="readonly" value="请选择" id="am" class="am" />
        <i  onclick="hide('HMF-1')"  class="i_"></i>
        <i id="HMF-1" style="display: none " class="bm">
        <span id="a1" onclick="pick('测试一')" onMouseOver="bgcolor('a1')" onMouseOut="nocolor('a1')" class="cur">测试一</span>
        <span id="a2" onclick="pick('测试二')" onMouseOver="bgcolor('a2')" onMouseOut="nocolor('a2')" class="cur">测试二</span>
        <span id="a3" onclick="pick('测试三')" onMouseOver="bgcolor('a3')" onMouseOut="nocolor('a3')" class="cur">测试三</span>
        <span id="a4" onclick="pick('测试四')" onMouseOver="bgcolor('a4')" onMouseOut="nocolor('a4')" class="cur">测试四</span>
        <span id="a5" onclick="pick('测试五')" onMouseOver="bgcolor('a5')" onMouseOut="nocolor('a5')" class="cur">测试五</span>
        <span id="a6" onclick="pick('测试六')" onMouseOver="bgcolor('a6')" onMouseOut="nocolor('a6')" class="cur">测试六</span>
        </i></strong>
                select 下拉菜单
                时间段：
                <span class="inline laydate-icon" id="start" style="width:200px; margin-right:10px;"></span> ---&nbsp
                <sapn class="inline laydate-icon" id="end" style="width:200px;"></sapn>
            </p> -->


           <!--  <p class="p_style02  clearfix">
                <label class="label_input02">
                    <input class="input_chk" type="checkbox"></input>
                    <span class="style01"></span>
                </label><a>一天内</a>
                <label class="label_input02">
                    <input class="input_chk" type="checkbox"></input>
                    <span class="style01"></span>
                </label><a>一周内</a>
                <label class="label_input02">
                    <input class="input_chk" type="checkbox"></input>
                    <span class="style01"></span>
                </label><a>一月内</a>
                <label class="label_input02">
                    <input class="input_chk" type="checkbox"></input>
                    <span class="style01"></span>
                </label><a>自定义</a>
                <a style="background: #01ab63; color:#ffffff;" class="a_style02" href="#">自定义时间</a>
                <a style="background: #01ab63; color:#ffffff; float: right;" class="a_style02" href="#">波形分析</a>
            </p> -->
            <!-- 编辑标签end -->
            <!-- table内容 -->
            <div class="table_box" id="autoarea">
                <table id="table">
                   <!--  <thead>
                        <tr>
                            <th>序号</th>
                            <th>变流器编号</th>
                            <th>故障类型</th>
                            <th>故障字</th>
                            <th>发生时间</th>
                            <th>参数解析</th>
                            <th>操作</th>
                        </tr>
                    </thead>
                    <tbody>
                        <tr>
                            <td>1</td>
                            <td>变流器1</td>
                            <td>故障</td>
                            <td>2016.06-23:15:28</td>
                            <td>B0</td>
                            <td>直流过压</td>
                            <td>
                                <a class="a_icon a_icon_0a" href="#">下载</a>
                            </td>
                        </tr>
                        <tr>
                            <td>1</td>
                            <td>管理员</td>
                            <td>1</td>
                            <td>管理员</td>
                            <td>1</td>
                            <td>管理员</td>
                            <td>
                                <a class="a_icon a_icon_0a" href="#">下载</a>
                            </td>
                        </tr>
                        <tr>
                            <td>1</td>
                            <td>管理员</td>
                            <td>1</td>
                            <td>管理员</td>
                            <td>1</td>
                            <td>管理员</td>
                            <td>
                                <a class="a_icon a_icon_0a" href="#">下载</a>
                            </td>
                        </tr>
                        <tr>
                            <td>1</td>
                            <td>管理员</td>
                            <td>1</td>
                            <td>管理员</td>
                            <td>1</td>
                            <td>管理员</td>
                            <td>
                                <a class="a_icon a_icon_0a" href="#">下载</a>
                            </td>
                        </tr>
                        <tr>
                            <td>1</td>
                            <td>管理员</td>
                            <td>1</td>
                            <td>管理员</td>
                            <td>1</td>
                            <td>管理员</td>
                            <td>
                                <a class="a_icon a_icon_0a" href="#">下载</a>
                            </td>
                        </tr>
                        <tr>
                            <td></td>
                            <td></td>
                            <td></td>
                            <td></td>
                            <td></td>
                            <td></td>
                            <td>
                                <a class="" href="#"></a>
                            </td>
                        </tr>
                        <tr>
                            <td></td>
                            <td></td>
                            <td></td>
                            <td></td>
                            <td></td>
                            <td></td>
                            <td>
                                <a class="" href="#"></a>
                            </td>
                        </tr>
                        <tr>
                            <td></td>
                            <td></td>
                            <td></td>
                            <td></td>
                            <td></td>
                            <td></td>
                            <td>
                                <a class="" href="#"></a>
                            </td>
                        </tr>
                        <tr>
                            <td></td>
                            <td></td>
                            <td></td>
                            <td></td>
                            <td></td>
                            <td></td>
                            <td>
                                <a class="" href="#"></a>
                            </td>
                        </tr>
                        <tr>
                            <td></td>
                            <td></td>
                            <td></td>
                            <td></td>
                            <td></td>
                            <td></td>
                            <td>
                                <a class="" href="#"></a>
                            </td>
                        </tr>
                        <tr>
                            <td></td>
                            <td></td>
                            <td></td>
                            <td></td>
                            <td></td>
                            <td></td>
                            <td>
                                <a class="" href="#"></a>
                            </td>
                        </tr>
                        <tr>
                            <td></td>
                            <td></td>
                            <td></td>
                            <td></td>
                            <td></td>
                            <td></td>
                            <td>
                                <a class="" href="#"></a>
                            </td>
                        </tr>
                        <tr>
                            <td></td>
                            <td></td>
                            <td></td>
                            <td></td>
                            <td></td>
                            <td></td>
                            <td>
                                <a class="" href="#"></a>
                            </td>
                        </tr>
                        <tr>
                            <td></td>
                            <td></td>
                            <td></td>
                            <td></td>
                            <td></td>
                            <td></td>
                            <td>
                                <a class="" href="#"></a>
                            </td>
                        </tr>
                    </tbody> -->
                </table>
            </div>
            <!-- table内容end -->
        </div>
        <!-- 右侧内容end -->
    </div>

 <!-- 日期js -->
<script type="text/javascript" src="<%=basePath%>/js/laydate.js"></script>
<!-- 日期js -->

<!-- 共同引用jquery js -->
 <script type="text/javascript" src="<%=basePath%>/js/jquery1124.js"></script>
 <script type="text/javascript" src="<%=basePath%>/js/js.js"></script>
 <!-- 共同引用jquery js -->
	
	
</body>
</html>