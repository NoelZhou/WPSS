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
    <title>服务设置-海上风电通讯协议</title>
    <link rel="stylesheet" type="text/css" href="<%=basePath%>/css/common.css">
    <link rel="stylesheet" type="text/css" href="<%=basePath%>/css/style.css">
    <script type="text/javascript" src="<%=basePath%>js/jquery-1.8.3.min.js"></script>
    <script type="text/javascript" src="<%=basePath%>js/opts.js"></script>
	<script type="text/javascript" src="<%=basePath%>js/spin.js"></script>
	
   	<script type="text/javascript">
   		$(function(){
   			$("#loading").show(); 
   			$(".W_width").hide();  
   			$.ajax({
   				type:'post',
   				url:'<%=basePath%>/ProtocolSettings/list.do',
   				data:{"modbus_type":2},
   				success:function(data){
   					$("#loading").hide();
   					$(".W_width").show(); 
   					var result = JSON.parse(data);
					var msg=result.message;
					var list=result.responseData.modbusSkapplist;
					if(msg=="获取成功"){
						var str="";
						str+=" <thead><tr><th width=130px;>category</th><th>id</th><th>type</th><th width=250px;>name</th><th>addr</th><th>datalen</th>"
							+"<th>datetype</th><th>cof</th><th>unit</th><th width=90px;>remark</th><th>enumeration</th></tr></thead><tbody>";
						if(list.length>0){
							$.each(list,function(i,list){
								str+="<tr><td  title="+list.category+">"+getSubStr(list.category,7)+"</td><td>"+list.id+"</td><td>"+list.type+"</td>"
								+"<td  title="+list.name+">"+getSubStr(list.name,10)+"</td><td>"+list.addr+"</td>"
								+"<td>"+list.datalen+"</td><td>"+list.datatype+"</td><td>"+list.cof+"</td><td>"+list.unit+"</td>"
								+"<td  title="+list.remark+">"+getSubStr(list.remark,4)+"</td>"
								+"<td><a class='a_style01' style='cursor:pointer;background: #488fd2;' onclick=enumtest('"+list.addr+"','"+list.name+"')>枚举值</a></td></tr>";
							});
						}else{
							str += "<tr>"
    							+"<td colspan='11'>没有相关数据</td>"
    							+"</tr>";
						}
						str+="</tbody>";
						$("#table_id").html(str);
						tabletd();
						$('#table_id tbody tr:even').css("backgroundColor", "#edf2f6");
					}else{
						alert(msg);
					}
   				}
   			});
   			autoHeight();
   			});
   		//相同td合并
   		function  tabletd(){
   			debugger;
   			var tbody = table_id.tBodies[0];
   			var trs = tbody.rows;
   			var tmp = {};
   			for(var i = 0; i < trs.length; i++){
   				var tds = trs[i].cells;
   				var html = tds[0].title;//获取td中的title
   				if(tmp[html]==null){
   		            tmp[html] = html;
   					//tds[0].style.backgroundColor = "#f9f9f9";
   		        }else{
   					//tds[0].style.backgroundColor = "#f9f9f9";
   					tds[0].style.borderTop = "none";
   					tds[0].innerHTML = "";
   		        }
   				
   			}
   		}
   		//解析xml
   		function importXml(){
    		var xmlUrl=$("#textfield").val();
    		var photoExt=xmlUrl.substr(xmlUrl.lastIndexOf(".")).toLowerCase();//获得文件后缀名
    		    if(photoExt!='.xml'){
    		        alert("请上传后缀名为xml的文件");
    		        return false;
    		    }
    		<%-- $.ajax({
    			type:'post',
    			url:'<%=basePath%>/ProtocolSettings/importXml.do',
    			data:{"xmlUrl":xmlUrl},
    			success:function(data){
    				var result = JSON.parse(data);
					var msg=result.message;
					if(msg=="解析成功"){
						alert(msg);
	    				window.location.reload();
					}else{
						alert(msg);
					}
    			}}); --%>
    			 var fileObj = document.getElementById("fileField").files[0]; // 获取文件对象
 				var FileController = "<%=basePath%>/ProtocolSettings/importXml.do";                    // 接收上传文件的后台地址 
 				// FormData 对象
 				var form = new FormData();
 				form.append("author", "hooyes");                        // 可以增加表单数据
 				form.append("xmlUrl", fileObj);                           // 文件对象
 				// XMLHttpRequest 对象
 				var xhr = new XMLHttpRequest();
 				xhr.open("post", FileController, true);
 				xhr.onload = function () {
 				alert("解析成功!");
 				$("#fileField").val("");
 				window.location.reload();
 				};
 				xhr.send(form);
    	}
   		//查看枚举值
   		function enumtest(addr,name){
   			$.ajax({
   				type:'post',
   				url:'<%=basePath%>/ProtocolSettings/findByAddr.do',
   				data:{"addr":addr,"name":name,"modbus_type":2},
   				success:function(data){
   					var result = JSON.parse(data);
					var msg=result.message;
					var mskAppameList=result.responseData.mskAppameList;
					var mskIappameList=result.responseData.mskIappameList;
					var listnull=result.responseData.listnull;
					if(msg=="获取成功"){
						if(listnull!=null){
							alert("该项没有枚举值!");
							return false;
						} 
						var str="";
							str+="<p class='p_style'>addr:"+result.responseData.addr+"<span>name："+result.responseData.name+"</span></p>"
							+" <table class='table_S' id='table'><thead><tr><th>命令代码</th><th>含义</th></tr></thead><tbody>";
						if(mskAppameList!=null){	
							$.each(mskAppameList,function(i,mskAppameList){
								str+="<tr><td>"+mskAppameList.bit_id+"</td><td>"+mskAppameList.var1+"</td></tr>";
							});
						}
						if(mskIappameList!=null){	
							$.each(mskIappameList,function(i,mskIappameList){
								str+="<tr><td>"+mskIappameList.varId+"</td><td>"+mskIappameList.name+"</td></tr>";
							});
						}
						str+="</tbody></table>";
						alertwindow("查看枚举值",str);
					}else{
						alert(msg);
					}
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
   		
   		function autoHeight(){
   			var autoheight = $(window).height()-200;
    	    $("#autoarea").attr("style","min-height:"+autoheight+"px;max-height:"+autoheight+"px");
    	}
   	//字符多余隐藏
   		function getSubStr(obj,num){
   			var len = obj.length;
   			var subval = obj;
   			if(len > num){
   				subval = obj.substring(0,num)+"...";
   			}
   			return subval;
   		}
    </script>
</head>
<body>
	 <div id="loading" style="margin:300px 600px;"></div>
	 <!-- 头部内容home -->
    
    <!-- 头部内容end -->
    <div class="main_box clearfix">
        <!-- 左侧菜单 -->
       
        <!-- 左侧菜单end -->
        <!-- 右侧内容 -->
        <div class="right_main_div0">
            <div class="W_width">
                <!-- 编辑标签 -->
                <form class="P_Label" action="" id="Form" method="post" enctype="multipart/form-data">
                    <input type='text' name='textfield' id='textfield' class='txt' />
                    <input type='button' class='btn' value='浏览...' />
                    <input type="file" name="fileField" class="file" style="cursor: pointer;" id="fileField" size="28" onchange="document.getElementById('textfield').value=this.value" />
                    <input type="button" name="button" onclick="importXml()" class="btn" style="cursor: pointer;" value="开始解析" />
                   <!--  <input type="submit" name="submit" class="btn" value="解析" /> -->
                </form>
                <!-- 编辑标签end -->
               
                <!-- table内容 -->
                <div class="table_box" id="autoarea">
                    <table id="table_id">
                        
                    </table>
                    <!-- 新增/编辑弹窗 -->
                    <div id="alertwindow"></div>
                    
                    <!-- 新增/编辑弹窗end -->
                </div>
                <!-- table内容end -->
            </div>
        </div>
        <!-- 右侧内容end -->
    </div>

</body>
</html>