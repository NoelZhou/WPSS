
    $(function() { // dom元素加载完毕
        $('table tr:odd').css("backgroundColor", "#ffffff");
        // $('#tableq tbody tr:even').css("backgroundColor", "#ffffff");
        // $('#tableA tbody tr:even').css("backgroundColor", "#ffffff");
        // $('#tableS tbody tr:even').css("backgroundColor", "#ffffff");
        //获取id为tb的元素,然后寻找他下面的tbody标签，再寻找tbody下索引值是偶数的tr元素,改变它的背景色.
        //tr:odd为奇数行，索引从0开始，0算偶数。
    })

//    table背景颜色end


//   服务设置左侧菜单
    $(function() {
    	/*
    	 * 获取当前服务器路径
    	 * 王
    	 */
    	 //获取当前网址，如： http://localhost:8083/myproj/view/my.jsp
    	   var curWwwPath=window.document.location.href;
    	   //获取主机地址之后的目录，如： myproj/view/my.jsp
    	  var pathName=window.document.location.pathname;
    	  var pos=curWwwPath.indexOf(pathName);
    	  //获取主机地址，如： http://localhost:8083
    	  var localhostPaht=curWwwPath.substring(0,pos);
    	  //获取带"/"的项目名，如：/myproj
    	  var projectName=pathName.substring(0,pathName.substr(1).indexOf('/')+1);
    	 //得到了 http://localhost:8083/myproj
    	  var contextPath=localhostPaht+projectName;
        // 为菜单初始化状态值
        $(".leftsidebar_box dt").each(function(index,value) {
            $(this).attr("state",0);
        });

        $(".leftsidebar_box dd").hide();
        $(".leftsidebar_box dt").click(function() {
            // 当前菜单状态
            var state = $(this).attr("state");
            var imgUrl = $(this).parent().find('img').attr("src");
            if((imgUrl == contextPath+"/images/left/select_xl.png")&&state==0){
            	$(this).parent().find('img').attr("src", contextPath+"/images/left/select_xl01.png");
            	$(this).parent().find('dd').removeClass("menu_chioce");
                $(this).parent().find('dt').removeClass("inactives");
                $(".menu_chioce").slideUp();
                $(this).parent().find('dd').slideToggle();
                $(this).parent().find('dd').addClass("menu_chioce");
            }else{
            	// 重置所有菜单状态
                $(".leftsidebar_box dt").each(function(index,value) {
                    $(this).attr("state",0);
                });	
                $(this).parent().find('dd').removeClass("menu_chioce");
                $(this).parent().find('dt').removeClass("inactives");
                $(".leftsidebar_box dt img").attr("src", contextPath+"/images/left/select_xl01.png");
                //$(this).parent().find('img').attr("src", "../images/left/select_xl.png");
                $(".menu_chioce").slideUp();
                $(this).parent().find('dd').slideToggle();
                $(this).parent().find('dd').addClass("menu_chioce");

                // 根据菜单状态，变更图标
                if (state == 0){
                	debugger;
                    $(this).parent().find('img').attr("src", contextPath+"/images/left/select_xl.png");
                    $(this).attr("state",1);
                } else {
                	debugger;
                    $(this).parent().find('img').attr("src", contextPath+"/images/left/select_xl01.png");
                    $(this).attr("state",0);
                }
            }
            
        });

        var cotrs = $(".leftsidebar_box dd");
        cotrs.click(function() {
            $(this).addClass("thisclass").siblings().removeClass("thisclass");
        });
    })
//   服务设置左侧菜单end



// 事件记录  日期选择
!function(){
    laydate.skin('molv');//切换皮肤，请查看skins下面皮肤库
    laydate({elem: '#demo'});//绑定元素
}();

//日期范围限制
var start = {
    elem: '#start',
    format: 'YYYY-MM-DD',
    min: laydate.now(), //设定最小日期为当前日期
    max: '2099-06-16', //最大日期
    istime: true,
    istoday: false,
    choose: function(datas){
         end.min = datas; //开始日选好后，重置结束日的最小日期
         end.start = datas //将结束日的初始值设定为开始日
    }
};

var end = {
    elem: '#end',
    format: 'YYYY-MM-DD',
    min: laydate.now(),
    max: '2099-06-16',
    istime: true,
    istoday: false,
    choose: function(datas){
        start.max = datas; //结束日选好后，充值开始日的最大日期
    }
};
laydate(start);
laydate(end);

// 事件记录  日期选择end



// select下拉菜单
 function $$$$$(_sId) {
        return document.getElementById(_sId);
    }

    function hide(_sId) {
        $$$$$(_sId).style.display = $$$$$(_sId).style.display == "none" ? "" : "none";
    }

    function pick(v) {
        document.getElementById('am').value = v;
        hide('HMF-1')
    }

    function picks(v) {
        document.getElementById('em').value = v;
        hide('HMF-2')
    }

    function bgcolor(_sId) {
        document.getElementById(_sId).style.background = "#F7FFFA";
        document.getElementById(_sId).style.color = "#000";
    }


    function nocolor(_sId) {
        document.getElementById(_sId).style.background = "";
        document.getElementById(_sId).style.color = "#788F72";
    }

    // select下拉菜单end















