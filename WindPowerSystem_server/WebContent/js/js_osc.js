
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

        // 为菜单初始化状态值
        $(".leftsidebar_box dt").each(function(index,value) {
            $(this).attr("state",1);
        });

        $(".leftsidebar_box dd").hide();
        $(".leftsidebar_box dt").click(function() {
            // 当前菜单状态
            var state = $(this).attr("state");
            // 重置所有菜单状态
            $(".leftsidebar_box dt").each(function(index,value) {
                $(this).attr("state",0);
            });

            $(this).parent().find('dd').removeClass("menu_chioce");
            $(this).parent().find('dt').removeClass("inactives");
            $(".leftsidebar_box dt img").attr("src", "../../images/left/select_xl01.png");
            //$(this).parent().find('img').attr("src", "../images/left/select_xl.png");
            $(".menu_chioce").slideUp();
            $(this).parent().find('dd').slideToggle();
            $(this).parent().find('dd').addClass("menu_chioce");

            // 根据菜单状态，变更图标
            if (state == 0){
                $(this).parent().find('img').attr("src", "../../images/left/select_xl.png");
                $(this).attr("state",1);
            } else {
                $(this).parent().find('img').attr("src", "../../images/left/select_xl01.png");
                $(this).attr("state",0);
            }
        });

        var cotrs = $(".leftsidebar_box dd");
        cotrs.click(function() {
            $(this).addClass("thisclass").siblings().removeClass("thisclass");
        });
    })
//   服务设置左侧菜单end

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















