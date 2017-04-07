<%@ page language="java" import="java.net.*" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
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
<link rel="stylesheet" type="text/css" href="<%=basePath%>css/style.css">
<script type="text/javascript" src="<%=basePath%>js/jquery-1.8.3.min.js"></script>
<script src="<%=basePath%>js/echarts/echarts.js"></script>  
<title>波形分析</title>
<script type="text/javascript">
var dataList = null;
var dataList1 = null;
var wavenames = '';
var filepath;
var tcp_type = '';
var errortype = '';
$(function(){
	autoHeight();
});

function getWaveNames(id,tcp_type,errortype){
	var url = "<%=basePath%>/WaveAnalyze/listWaveNames.do?tcp_type="+tcp_type+"&device_id="+id;
	$.get(url,function(data){
		var result = JSON.parse(data);
		if("1001" == result.code){
			var wcnameList = result.responseData.wcnameList;
			var jcnameList = result.responseData.jcnameList;
			var str = '';
			if(wcnameList.length > 0 && errortype == "wc"){
				str += "<thead><tr><th><span style='font-weight: bold;'>网侧波形</span></th></tr></thead><tbody>";
				$.each(wcnameList,function(i,wclist){
					str += "<tr><td><label class='label_input label_style'>"
					+ "<input class='input_chk' onclick='checkwave()' name='waves'  type='checkbox' id='"+i+"' value ='"+wclist+"'"
					+ "><span></span></label>"+wclist+"</td></tr>";
				});
				str += "</tbody>";
			}
			if(jcnameList.length > 0 && errortype == "jc"){
				str += "<thead><tr><th><span style='font-weight: bold;'>机侧波形</span></th></tr></thead><tbody>"
				$.each(jcnameList,function(i,jclist){
					str += "<tr><td><label class='label_input label_style'>"
					+ "<input class='input_chk' onclick='checkwave()' name='waves' type='checkbox' id='"+i+"' value ='"+jclist+"'"
					+ "><span></span></label>"+jclist+"</td></tr>";
				});
				str += "</tbody>";
			}
			$("#table").html(str);
			$('#table tbody tr:even').css("backgroundColor", "#edf2f6");
		}else{
			alert("获取数据失败");
		}
	});
}

function startload(){
	var wtd = window.top.document;
	var excelPath = $("#textfield",wtd).val();
	var suffixname = excelPath.substr(excelPath.lastIndexOf(".")).toLowerCase();
	if(suffixname!='.xls'){
        alert("请上传后缀名为xls的文件");
        return false;
    } 
	
	errortype = $("input[name='wavetype']:checked",wtd).val();
	tcp_type = $("input[name='tcptype']:checked",wtd).val();
	if(errortype == undefined){
		alert("请选择波形类型");
        return false;
	}
	if(tcp_type == undefined){
		alert("请选择协议类型");
        return false;
	}
    var formData = new FormData($("#Form",wtd)[0]);  
    $.ajax({  
         url: '<%=basePath%>/WaveAnalyze/loadwaves.do' ,  
         type: 'POST',  
         data: formData,  
         async: false,  
         cache: false,  
         contentType: false,  
         processData: false,  
         success: function (returndata) { 
             var result = JSON.parse(returndata); 
			 if("1001" == result.code){
				 dataList = result.responseData.datalist;
	           	 getWaveNames(0,tcp_type,errortype);
	           	 $("#textfield",wtd).val("");
	           	 $("#gray",wtd).hide();
				 $("#alertwindow",wtd).hide();
				 $("#container").hide();
        	 }else{
        		 alert("加载波形文件有误");
        	 }
         }
    });   
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
		var idsArr = ids.split(",");
		dataList1 = new Array();
		for(var i=0;i < dataList.length;i++){
			for(var j=0;j < idsArr.length;j++){
				if(i == idsArr[j]){
					dataList1.push(dataList[i]);
				}
			}
		}
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
    	var listlen = dataList1.length;
    	var wavenameArr = wavenames.split(",");
    	for(var i=0;i<listlen;i++){
    		var itm = {
    				name:wavenameArr[i],
    				type:'line',
    				symbol:'none',
    				data:dataList1[i]
    		}
    		options.series.push(itm);
    	}
    	var firstdata = dataList1[0];
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

function showDialog(){
	var context = "<div style='padding-left: 28%;'>"
	+ "<form class='P_Label' id='Form' method='post' enctype='multipart/form-data'>"
	+ "波形文件 &nbsp;<input type='text' name='textfield' id='textfield' class='txt' />"
	+ "<input type='button' class='btn' value='浏览...' />"
	+ "<input type='file' name='file' class='file' id='fileField' style='cursor: pointer;width:338px' onchange=document.getElementById('textfield').value=this.value />"
	+ "</form><div>波形类型 &nbsp;<input type='radio' name='wavetype' value='wc' checked='checked'/>网侧波形&nbsp; &nbsp; &nbsp;"
	+ "<input type='radio' name='wavetype' value='jc'/>机侧波形</div>"
	+ "<div style='padding-top: 20px;'>协议类型 &nbsp;<input type='radio' name='tcptype' value='0' checked='checked'/>双馈&nbsp; &nbsp; &nbsp;"
	+ "<input type='radio' name='tcptype' value='1'/>全功率</div></div>"
	+ "<a class='a_button' style='margin-top: 20px;' href='javascript:;' onclick=document.getElementById('iframe').contentWindow.startload();>加载</a>"
	;
	alertwindow("选择文件",context);
}

function alertwindow(title,str){
	var wtd = window.top.document;
   	$("#gray",wtd).show();
   	$("#alertwindow",wtd).show();
   	$("#titletxt",wtd).html(title);
   	$("#contentdiv",wtd).html(str);
}

function autoHeight(){
	var height = $(window).height()-200;
    $("#autoarea").attr("style","min-height:"+height+"px;max-height:"+height+"px;overflow:hidden;");
    $("#Ttable").attr("style","height:"+height+"px");
    $("#bj_box").attr("style","width:73%;height:"+height+"px;overflow: hidden;");
    $("#container").attr("style","width:100%;height:"+(height-40)+"px;");
}
</script>
</head>
<body>
<div class="main_box clearfix">
	<!-- 右侧内容 -->
	<div class="W_width">
	    <!-- 编辑标签 -->
	        <p style="width:60%; float: left;" class="P_Label clearfix">
	            <span onclick='showDialog()'><a class="a_size02f" href="javascript:;">加载波形文件</a></span>
	        </p>
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
	        </div>
	        <!-- 右侧风机故障/告警信息end -->
	    </div>
	    <!-- table内容end -->
	</div>
	<!-- 右侧内容end -->
</div>

</body>
</html>