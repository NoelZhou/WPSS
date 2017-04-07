<%@ page language="java" import="java.net.*" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
 	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<%
	String pathparam = URLDecoder.decode(request.getParameter("filepath"),"utf-8");
	String idparam = URLDecoder.decode(request.getParameter("device_id"),"utf-8");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

<script type="text/javascript" src="<%=basePath%>js/spin.js"></script>
<link rel="stylesheet" type="text/css" href="<%=basePath%>css/common.css">
<link rel="stylesheet" type="text/css" href="<%=basePath%>/css/style.css">
<script type="text/javascript" src="<%=basePath%>js/jquery-1.8.3.min.js"></script>
<script src="<%=basePath%>js/echarts/echarts.js"></script>    
<title>波形分析</title>
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


var dataList = null;
var wavenames = '';
var filepath;
var tcp_type = '';
$(function(){
	filepath = "<%=pathparam %>";
	var device_id = "<%=idparam %>";
	var errortype;
	if(filepath.indexOf("网侧故障") > -1){
		errortype = "wc";
	}else if(filepath.indexOf("机侧故障") > -1){
		errortype = "jc";
	}
	getWaveNames(device_id,tcp_type,errortype);
	autoHeight();
});

function getWaveNames(id,tcp_type,errortype){
	$(".W_width").hide();
	$("#loading").show();
	var url = "<%=basePath%>/WaveAnalyze/listWaveNames.do?tcp_type="+tcp_type+"&device_id="+id;
	$.get(url,function(data){
		var result = JSON.parse(data);
		var wcStr = "<thead><tr><th><span ";
		if(errortype == "wc"){
			wcStr += "style='font-weight: bold;'";
			$("#Btable").hide();
		}
		wcStr += ">网侧波形</span></th></tr></thead>";
		var jcStr = "<thead><tr><th><span ";
		if(errortype == "jc"){
			jcStr += "style='font-weight: bold;'";
			$("#Ttable").hide();
		}
		jcStr += ">机侧波形</span></th></tr></thead>";
		if("1001" == result.code){
			var wcnameList = result.responseData.wcnameList;
			var jcnameList = result.responseData.jcnameList;
			if(wcnameList.length > 0 && errortype == "wc"){
				wcStr += "<tbody>";
				$.each(wcnameList,function(i,wclist){
					wcStr += "<tr><td><label class='label_input label_style'>"
					+"<input class='input_chk' onclick='checkwave()' name='waves'  type='checkbox' id='"+i+"' value ='"+wclist+"'";
					if(errortype != "wc"){
						wcStr += "disabled='disabled'";
					}
					wcStr += "><span></span></label>"+wclist+"</td></tr>";
				});
				wcStr += "</tbody>";
			}
			if(jcnameList.length > 0 && errortype == "jc"){
				jcStr += "<tbody>"
				$.each(jcnameList,function(i,jclist){
					jcStr += "<tr><td><label class='label_input label_style'>"
						+"<input class='input_chk' onclick='checkwave()' name='waves' type='checkbox' id='"+i+"' value ='"+jclist+"'";
					if(errortype != "jc"){
						jcStr += "disabled='disabled'";
					}
					jcStr += "><span></span></label>"+jclist+"</td></tr>";
				});
				jcStr += "</tbody>";
			}
			$("#table").html(wcStr);
			$("#tableA").html(jcStr);
			$('#table tbody tr:even').css("backgroundColor", "#edf2f6");
			$('#tableA tbody tr:even').css("backgroundColor", "#edf2f6");
		}else{
			alert("获取数据失败");
		}
		$(".W_width").show();
		$("#loading").hide();
	});
}

function closediv(){
	$(".poput_box_b").hide();
}

//点击加载波形
function checkwave(){
	$("#container").show();
	var ids = '';
	wavenames = '';
	for(var i=0;i < document.getElementsByName('waves').length;i++){
		if(document.getElementsByName('waves')[i].checked){
		  	if(ids=='') ids += document.getElementsByName('waves')[i].id;
		  	else ids += ',' + document.getElementsByName('waves')[i].id;
		  	if(wavenames=='') wavenames += document.getElementsByName('waves')[i].value;
		  	else wavenames += ',' + document.getElementsByName('waves')[i].value;
		 }
	}
	if(ids != ''){
		$.ajax({
			type:'post',
			url:'<%=basePath%>/WaveAnalyze/getWaveData.do',
			data:{"filepath":filepath,"ids":ids},
			async: false,
			success:function(data){
				var result = JSON.parse(data);
				if("1001" == result.code){
					dataList = result.responseData.datalist;
				}else{
					alert("获取数据失败");	
				}
			}
		});
	}else{
		$("#container").hide();
		return false;
	}
	
	var myChart;  
    var eCharts; 
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
        var options = {  
            tooltip : {  
                trigger : 'axis'  
            },  
            legend : {  
                data : ["0"] 
            },
            dataZoom:{
            	orient:"horizontal",
            	show:true,
            	start:0,
            	end:100
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
            	y:'12%',
            	height:'70%',
                width :'94%',
            },  
            series : []  
        };  
    	var listlen = dataList.length;
    	var wavenameArr = wavenames.split(",");
    	for(var i=0;i<listlen;i++){
    		var itm = {
    				name:wavenameArr[i],
    				type:'line',
    				symbol:'none',
    				data:dataList[i]
    		}
    		options.series.push(itm);
    	}
    	var firstdata = dataList[0];
    	var legendArr = [];
    	for(var j=0;j<firstdata.length;j++){
    		legendArr.push(j);
    	}
    	options.legend.data = wavenameArr; 
    	options.xAxis[0].data = legendArr;
    	myChart.hideLoading();  
        myChart.setOption(options); 		
    }
}
function autoHeight(){
	var height = $(window).height()-200;
    $("#autoarea").attr("style","min-height:"+height+"px;max-height:"+height+"px;overflow:hidden;");
    $("#Ttable").attr("style","height:"+height+"px");
    $("#Btable").attr("style","height:"+height+"px");
    $("#bj_box").attr("style","width:73%;height:"+height+"px;overflow: hidden;");
    $("#container").attr("style","width:100%;height:"+(height-40)+"px;");
}
</script>
</head>
<body>
<div id="loading" style="margin:300px 600px;"></div>
<div class="main_box clearfix">
	<!-- 右侧内容 -->
	<div class="W_width">
	    <!-- 编辑标签 -->
	        <p style="width:25%; float: right;" class="P_Label clearfix">
	            <span style="background: #edf2f6; border:none;">波形筛选</span>
	        </p>
	        <!-- 编辑标签end -->
	
	    <!-- table内容 -->
	    <div class="table_box" id="autoarea">
	        <!-- 数据趋势图 -->
	        <div class="box_lef01 bj_box" id="bj_box">
	            <div class="w_w">
	                <div id="container"></div>
	            </div>
	
	        </div>
	        <!-- 数据趋势图end -->
	        <!-- 右侧风机故障/告警信息 -->
	        <div class="box_right box_right01" style="width:25% !important">
	            <div class="T_table table_boxb" id="Ttable">
	                <table id="table">
	                </table>
	            </div>
	            <div class="B_table table_boxb" id="Btable">
	                <table id="tableA">
	                </table>
	            </div>
	        </div>
	        <!-- 右侧风机故障/告警信息end -->
	    </div>
	    <!-- table内容end -->
	</div>
	<!-- 右侧内容end -->
	<div style="display:none" class="poput_box_b">
	    <p class="poput_title">选择文件
	        <a href="javascript:;" onclick="closediv()"></a>
	    </p>
	    <div style="padding-left: 80px;">
	        <form class="P_Label" action="<%=basePath%>/WaveAnalyze/loadwaves.do" id="Form" method="post" enctype="multipart/form-data">
	          	         波形文件 &nbsp;<input type='text' name='textfield' id='textfield' class='txt' />
	               <input type='button' class='btn' value='浏览...' />
	               <input type="file" name="file" class="file" id="fileField"  onchange="document.getElementById('textfield').value=this.value" />
	               <br/>
	               <div style="padding-top: 30px;">
	               	波形类型 &nbsp;<input type="radio" name="wavetype" value="wc" checked="checked"/>网侧波形&nbsp;
	               <input type="radio" name="wavetype" value="jc"/>机侧波形
	               </div>
	               <div style="padding-top: 30px;">
	               	协议类型 &nbsp;<input type="radio" name="tcptype" value="0" checked="checked"/>双馈&nbsp;
	               <input type="radio" name="tcptype" value="1"/>全功率
	               </div>
	        </form>
        </div>
	    <a class="a_button" href="javascript:;" onclick="startload()">加载</a>
   </div>
</div>

</body>
</html>