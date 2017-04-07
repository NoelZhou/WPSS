<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%
 	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!DOCTYPE HTML>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>Highstock Example</title>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/highcharts/jquery-1.8.2.min.js"></script>
<script src="${pageContext.request.contextPath}/jsp/windFieldPreview/oscilloscope/canvasjs.min.js"></script>

<script type="text/javascript">
var context_ = '<%=basePath%>';
var socketUrl = context_.replace("http", "ws");
var app = {};
var chart;
var webSocket;
var updateChart;

var allDatas = new Array(); //数据集合
var allArr = []; //波形曲线对象数组
var updateInterval = 20; // 刷新速率
var dataLength = 2500; // X轴数据点数量
var count = 1200; // 刷新加载点数
var xVal = 0; //X轴值

$(function() {
	initWebsocket();
});

/**
 * 初始化Websocket
 */
function initWebsocket(){
	webSocket = new WebSocket(socketUrl + 'oscilloscopeWebSocket');
	webSocket.onerror = function(event) {};
	webSocket.onopen = function() {};
	webSocket.onclose = function() {};

	webSocket.onmessage = function(event) {
		onMessage(event)
	};
}

/**
 * 获取webSocket后台传输过来的DSP数据
 */
function onMessage(event) {
// 	document.getElementById('messages').innerHTML += '<br />' + event.data;
	
	var datas = event.data.split("=");
	for(var i = 0; i < datas.length; i++)
	{
		allDatas.push(datas[i]);
	}
	
}

/**
 * 开始
 */
function startSync() {
	if(webSocket == null){
		initWebsocket();
	}
	webSocket.send('开始');
	var moveformNames = "1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16";
	initChart(moveformNames);
	
	clearInterval(app.timeTicket);
	app.timeTicket = setInterval(function(){updateChart()}, 30); 
	return false;
}

/**
 * 停止
 */
function stopSync(){
	clearInterval(app.timeTicket);
	webSocket.close();
}

/**
 * 初始化线
 */
function initLine(moveformNames){
	allArr = [];
	var mfn = moveformNames.split(",");
	var data = []
	for(var i=0; i<mfn.length; i++){
		
		var tempArr = [];
		allArr.push(tempArr);
		
		var dt = new Object();
		dt.name = mfn[i];
		dt.showInLegend = true;
		dt.type = "spline";
		dt.dataPoints = allArr[i];
		data[i] = dt;
	}
	return data;
}

/**
 * 初始化图标控件
 */
function initChart(lineNum){
	var data = initLine(lineNum);
	
	chart = new CanvasJS.Chart("container",{
		legend: {
            cursor: "pointer",
            itemclick: function (e) {
                //console.log("legend click: " + e.dataPointIndex);
                //console.log(e);
                if (typeof (e.dataSeries.visible) === "undefined" || e.dataSeries.visible) {
                    e.dataSeries.visible = false;
                } else {
                    e.dataSeries.visible = true;
                }

                e.chart.render();
            }
        },
		zoomEnabled: true,
		title :{
			text: "示波器"
		},			
		data: data
	});
	chart.render();
}

/**
 * 加载图表数据
 */
function updateChart() {
	if(allDatas.length <= 0 ){
		return;
	}

	for (var j = 0; j < count; j++) {
		if(j >= allDatas.length){
			break;
		}
		var temparr = allDatas[j].split(",");
		for(var i = 0; i< temparr.length; i++)
		{
			var dps = allArr[i];
			var yVal = parseFloat(temparr[i]);
			dps.push({
				x: xVal,
				y: yVal
			});
			if (dps.length > dataLength)
			{
				dps.shift();
			};
		}
		xVal++;
	};
	chart.render();		
	allDatas.splice(0, count);
};

/**
 * 放大
 */
function enlarge() {
	if(dataLength <= 100){
		return null
	}
	dataLength = dataLength - 100;
	for(var i = 0; i < allArr.length;i++)
	{
		var dps = allArr[i];
		dps.splice(0, 100);
	}
}

/**
 *  缩小
 */
function reduce() {
	if(dataLength >= 25000){
		return null
	}
	dataLength = dataLength + 100;
}

function getData(){
	var options = chart.options;
	var data = options.data[0];
	var dataPoints = data.dataPoints;
	var dataPoint;
	var datas = "";
	for(var i = 0; i < dataPoints.length; i++){
		dataPoint = dataPoints[i];
		datas += dataPoint.y + "," + dataPoint.x + "<br />";
	}
	document.getElementById('messages').innerHTML += '<br />' + datas;
}
</script>
</head>
<body>

	<div id="container" style="height: 600px; min-width: 310px; margin-top: "></div>
	<br/><br/>
	<input type="button" value="开始" onclick="startSync();"/>
	<input type="button" value="停止" onclick="stopSync();"/>
	<input type="button" value="放大" onclick="enlarge();"/>
	<input type="button" value="缩小" onclick="reduce();"/>
	<input type="button" value="获取数据" onclick="getData();"/>
	<div id="messages"></div>
</body>
</html>