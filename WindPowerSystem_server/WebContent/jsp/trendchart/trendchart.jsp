<%@ page language="java" import="java.util.*"
	contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
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
<script type="text/javascript" src="<%=basePath%>js/spin.js"></script>
<link rel="stylesheet" type="text/css"
	href="<%=basePath%>css/common.css">
<link rel="stylesheet" type="text/css"
	href="<%=basePath%>/css/style.css">
<script type="text/javascript" src="<%=basePath%>js/jquery-1.8.3.min.js"></script>
<script src="<%=basePath%>js/echarts/echarts.js"></script>
<title>趋势图</title>
<script type="text/javascript">
$(function(){
	var opts = {
	  lines: 13, // The number of lines to draw
	  length: 20, // The length of each line
	  width: 10, // The line thickness
	  radius: 17, // The radius of the inner circle
	  corners: 1, // Corner roundness (0..1)
	  rotate: 0, // The rotation offset
	  direction: 1, // 1: clockwise, -1: counterclockwise
	  color: '#488fd2', // #rgb or #rrggbb or array of colors
	  speed: 1, // Rounds per second
	  trail: 60, // Afterglow percentage
	  shadow: false, // Whether to render a shadow
	  hwaccel: false, // Whether to use hardware acceleration
	  className: 'spinner', // The CSS class to assign to the spinner
	  zIndex: 2e9, // The z-index (defaults to 2000000000)
	  top: 'auto', // Top position relative to parent in px
	  left: 'auto' // Left position relative to parent in px
	};
	var target = document.getElementById('loading');
	var spinner = new Spinner(opts).spin(target);
});


var timer,deviceids,addrs,type_id;
var myChart;  
var eCharts;
var options;
$(function(){
	modbuscpType();
	$('li').live('click',function(){
		$(this).addClass('li_style').siblings('li').removeClass('li_style');
	});
	loadChart();
   timer = setInterval(function(){
    	if(deviceids != null && addrs != null & type_id != null){
			getChartData(myChart,options,deviceids,addrs,type_id);
		}
    },2000); 
    autoHeight();
});

function loadChart(){
	require.config({  
        paths : {  
        	echarts: '<%=basePath%>js/echarts'
        }  
    }); 
	require(  
	        [ 'echarts',   
	          'echarts/chart/line'  
	        ], DrawEChart //异步加载的回调函数绘制图表  
	);
	  //创建ECharts图表方法  
    function DrawEChart(ec) {
        eCharts = ec;  
        myChart = eCharts.init(document.getElementById('container'));  
        myChart.showLoading({  
            text : "数据正在努力加载..."  
        });  
        //定义图表options  
        options = {  
            tooltip : {  
                trigger : 'axis'  
            },  
            legend : {  
                data : [] 
            },  
        
            xAxis : [ {  
                type : 'category',  
                boundaryGap : false,  
                data : []  
            } ],  
            yAxis : [ {  
                type : 'value',  
            } ],  
            grid : {  
            	x:'50px',
            	height:'70%',
                width : '90%',
            },  
            series : [],
            animation: false
        };  
        myChart.hideLoading(); 
    }
}

function modbuscpType(){
	$.ajax({
		type:'post',	
		url:'<%=basePath%>CollectSet/listModbustcpType.do',
		success:function(data){
			var result = JSON.parse(data);
			var modbustcptypeList = result.responseData.modbustcptypeList;
			if(result.code == "1001"){
					var str = "";
					var firstid;
			    	$.each(modbustcptypeList,function(i,list){
			    		str += "<li ";
			    		if(i==0){
			    			str += "class = 'li_style'";
			    			firstid = list.id;
			    		}
			    		var tmpname="";
			    		if(list.id==0){
			    			tmpname="双馈";
			    		}else  if(list.id==1){
			    			tmpname="全功率";
			    		}else{
			    			tmpname="海上风电";
			    		}
			    		str += "><a style='cursor: pointer;' onclick=loadPage('"+list.id+"')>"+tmpname+"变流器</a></li>";
			    	});
			    	$("#menuid").html(str);
			    	loadPage(firstid);
			    }else{
			    	alert("获取数据失败");
			    }
			    
		}
	});
	
}

function loadPage(id){
	deviceids = null; 
	addrs = null;
	type_id = null
	devicenum = 0;
	addrnum = 0;
	$("#container").hide();
	loadleft(id);
	loadright(id);
}

function loadleft(id){
	$(".table_box").hide();
	$("#loading").show();
	var url = "<%=basePath%>PageSet/listDeviceBaseData.do?modbustype="+id;
	$.get(url,function(data){
		var result = JSON.parse(data);
		var varlist = result.responseData.dbList;
		var str = "<tbody>";
		if(varlist.length>0){
		for(var i = 0;i < varlist.length;i++){
			str += "<tr><td><label class='label_input label_style'><input class='input_chk' name='addr' type='checkbox' value='"+varlist[i].paramter_id+"' onclick='chooseCk(this,"+id+")' />"
				+"<span></span></label>"+varlist[i].name+"</td></tr>";
		}
		}else{
			str += "<tr><td>没有相关数据</td></tr>";
		}
		str += "</tbody>";
		$(".table_box").show();
		$("#loading").hide();
		$("#table1").html(str);
		$('#table1 tbody tr:even').css("backgroundColor", "#edf2f6");
	});
}
function loadright(id){
	$(".table_box").hide();
	$("#loading").show();
	var url = "<%=basePath%>TrendChart/devicelist.do?modbustype_id="+id;
	$.get(url,function(data){
		var result = JSON.parse(data);
		var deviceList = result.responseData.deviceList;
		var str = "<tbody>";
		if(deviceList.length > 0){
			for(var i = 0;i < deviceList.length;i++){
				str += "<tr><td><label class='label_style_01'><input class='input_chk' name='device' type='checkbox' value='"+deviceList[i].id+"' onclick='chooseCk(this,"+id+")' />"
					+"<span></span></label>"+deviceList[i].name+"</td></tr>";
			}
		}else{
			str += "<tr><td>没有相关数据</td></tr>";
		}
		str += "</tbody>";
		$(".table_box").show();
		$("#loading").hide();
		$("#table2").html(str);
		$('#table2 tbody tr:even').css("backgroundColor", "#edf2f6");
	});	
}

var devicenum = 0;
var addrnum = 0;
function chooseCk(obj,id){
	if(obj.checked){
		if("device" == obj.name){
			devicenum ++;
		}else if("addr" == obj.name){
			addrnum ++;
		}
	}else{
		if("device" == obj.name){
			devicenum --;
		}else if("addr" == obj.name){
			addrnum --;
		}
	}
	//开始条件
	if(devicenum > 0 && addrnum > 0){
		deviceids = getDeviceids();
		addrs = getAddrs();
		type_id = id;
		loadChart();
		//getChartData(myChart,options,deviceids,addrs,type_id);
		$("#container").show();
	}
	//结束条件
	if(!obj.checked){
		if(devicenum == 0){
			deviceids = null;
			$("#container").hide();
		}
		if(addrnum == 0){
			addrs = null;
			$("#container").hide();
		}
	}
	
}
//获取设备选中ids
function getDeviceids(){
	var ids = '';
	for(var i=0;i < document.getElementsByName('device').length;i++){
		if(document.getElementsByName('device')[i].checked){
		  	if(ids=='') ids += document.getElementsByName('device')[i].value;
		  	else ids += ',' + document.getElementsByName('device')[i].value;
		 }
	}
	return ids;
}
//获取参数地址addrs
function getAddrs(){
	var addrs = '';
	for(var i=0;i < document.getElementsByName('addr').length;i++){
		if(document.getElementsByName('addr')[i].checked){
		  	if(addrs=='') addrs += document.getElementsByName('addr')[i].value;
		  	else addrs += ',' + document.getElementsByName('addr')[i].value;
		 }
	}
	return addrs;
}
function getChartData(myChart,options,deviceids,addrs,type_id){
	options.series = [];
    var url = "<%=basePath%>TrendChart/getChartData.do?deviceids="+deviceids+"&addrs="+addrs+"&modbustype_id="+type_id;
	$.ajax({ 
		url : url, 
		cache : false, 
		async : false, 
		type : "POST",
		success : function (data){ 
			var result = JSON.parse(data);
	    	if("1001" == result.code){
	    		options.legend.data = result.responseData.echartData.legend; 
	    		options.xAxis[0].data = result.responseData.echartData.category; 
	    		var serieslist = result.responseData.echartData.series;
	    		for(var i=0;i<serieslist.length;i++){
	    			var itm = {
	    				name:serieslist[i].name,
	    				type:'line',
	    				symbol:'none',
	    				data:serieslist[i].data
	    			}
	    			options.series.push(itm);
	    		}
	    		myChart.hideLoading();  
	    	  //  myChart.setOption(options);  
	            myChart.setOption(options);  
	    	}else{
	    		alert("获取数据失败");
	    		clearInterval(timer);
	    	}
		} 
		}); 
}
function autoHeight(){
	var height = $(window).height()-220;
    $("#autoarea").attr("style","min-height:"+height+"px;max-height:"+height+"px;overflow:hidden");
    $("#leftbox").attr("style","width: 14%;height:"+height+"px;overflow:auto");
    $("#rightbox").attr("style","width: 14%;height:"+height+"px;overflow:auto");
}
</script>
</head>
<body>
	<div class="main_box clearfix">
	<!-- 右侧内容 -->
	<div class="W_90" id="right_main">
		<div class="W_width" style="width: 100%;">
			<!-- 头部切换标签 -->
			<div class="main_title_box ">
				<ul id="menuid">
				</ul>
				<!-- 编辑标签 -->
				<p class="P_Label clearfix" style="margin: 10px 0 20px"></p>
				<!-- 编辑标签end -->
				<!-- table内容 -->
				<div id="loading" style="margin: 300px 600px;"></div>
				<div class="table_box table_boxa" id="autoarea">
					<div class="box_W" id="leftbox">
						<table id="table1">
						</table>
					</div>
					<div class="box_W01" style="width: 72%">
						<div class="W_m" style="width: 98%">
							<div id="container"
								style="width: 850px; height: 350px; margin: 10px auto 0"></div>
						</div>
					</div>
					<div class="box_W" id="rightbox">
						<table id="table2">

						</table>
					</div>
				</div>
				<!-- table内容end -->
			</div>
		</div>
		<!-- 右侧内容end -->
	</div>
	</div>
</body>
</html>