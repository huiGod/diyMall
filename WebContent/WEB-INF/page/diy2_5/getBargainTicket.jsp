<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
    String path=request.getContextPath();
%>

<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, minimum-scale=1, user-scalable=no">
    <meta name="apple-mobile-web-app-capable" content="yes">
    <meta name="apple-mobile-web-app-status-bar-style" content="black">
    <meta content="telephone=no,email=no" name="format-detection">
    <meta name="App-Config" content="fullscreen=yes,useHistoryState=yes,transition=yes">
    <title>抽取砍价券</title>
     <link type="text/css" rel="stylesheet" href="<%=path %>/css/diy2_5/hd-index-rem.css"/>
    <style type="text/css">
        img{width: 100%}
        .text{
            padding:0 10.9375vw;
            color: #817778;
            line-height: 1.5;
            word-wrap:break-word;word-break:break-all;
        }
        .scratchArea{
            position: relative;
        }
        .scratchArea .main{
            color:#d76d71;
            text-align: center;
            position: absolute;
            top:9.6875vw;
            left:16.5625vw;
            right: 16.5625vw;
            bottom:8.6875vw;
            overflow: hidden;
        }
        .scratchArea .inside{
            position: absolute;
            top:0;
            left:0;
            right:0;
            bottom:0;
            z-index: 22;
            /*display: none;*/
        }
        .scratchArea .outer h3{
            font-size: 22px;
        }
        .trophy{
            position: absolute;
            top:0;
            left:0;
            right:0;
            bottom:0;
            width: 100%;
            /*display: flex;*/
            /*align-items: center;*/
            /*justify-content: center;*/
            color: #ec6676;
            font-size: 22px;
            font-weight: 600;
            background: transparent;
            font-family: "Microsoft yahei",Helvetica,Arial,sans-serif;
        }
        @media screen and (max-width: 321px){  .trophy{font-size: 20px}  }

        #c1{
            position:absolute;
            top:0;
            left:0;
            width: 100%;
            height:100%;
            text-align:center;
            z-index: 55;
        }

    </style>

</head>
<body>
    <div class="containerBox" data-url="http://120.26.112.213:8082/diyMall/cutPrice/getCutCoupon.do?userId="  data-check="http://api.diy.51app.cn/diyMall/cutPrice/checkCutCoupon.do?userId=">
        <div class="page-cont">
            <img src="<%=path %>/images/diy2_5/getBargainTicket01.jpg"/>

            <div class="scratchArea">

                <img src="<%=path %>/images/diy2_5/getBargainTicket02.jpg"/>
                <div class="main" id="top">
                    <div class="inside">
                        <button class="trophy"></button>
                        <img src="<%=path %>/images/diy2_5/getBargainTicket03.jpg" />
                    </div>
                    <canvas id="c1" class="canvas"></canvas>

                </div>
            </div>

            <div class="text">
                <p>规则说明：</p>
                <p>1.每个用户，每周仅能抽取一次。</p>
                <p>2.砍价券仅能帮好友的0元购作品砍价，不能帮自己的作品砍价。</p>
            </div>

        </div>

    </div>





    <script type='text/javascript' src='//g.alicdn.com/sj/lib/zepto/zepto.min.js' charset='utf-8'></script>
    <script src="//cdn.bootcss.com/fastclick/1.0.6/fastclick.min.js"></script>

    <script>
        //获取地址栏参数
        function GetQueryString(name)
        {
            var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");
            var r = window.location.search.substr(1).match(reg);
            if(r!=null)return  unescape(r[2]); return null;
        }



        var c1;             //画布
        var ctx;            //画笔
        var ismousedown;    //标志用户是否按下鼠标或开始触摸
        var isOk=0;         //标志用户是否已经刮开了一半以上
        var fontem = parseInt(window.getComputedStyle(document.documentElement, null)["font-size"]);//这是为了不同分辨率上配合@media自动调节刮的宽度
        var guakai = false;
        /* 页面加载后开始初始化画布 */
        window.onload = function(){

            //初始化涂抹面积
            isOk = 0;

            c1 = document.getElementById("c1");

            //这里很关键，canvas自带两个属性width、height,我理解为画布的分辨率，跟style中的width、height意义不同。
            //最好设置成跟画布在页面中的实际大小一样
            //不然canvas中的坐标跟鼠标的坐标无法匹配
            c1.width=c1.clientWidth;
            c1.height=c1.clientHeight;
            ctx = c1.getContext("2d");

            //PC端的处理
            c1.addEventListener("mousemove",eventMove,false);
            c1.addEventListener("mousedown",eventDown,false);
            c1.addEventListener("mouseup",eventUp,false);

            //移动端的处理
            c1.addEventListener('touchstart', eventDown,false);
            c1.addEventListener('touchend', eventUp,false);
            c1.addEventListener('touchmove', eventMove,false);

            //初始化
            initCanvas();
            $('#c1').hide();
            checkCoupon();
        };

        //初始化画布，画灰色的矩形铺满
        function initCanvas(){
            //网上的做法是给canvas设置一张背景图片，我这里的做法是直接在canvas下面另外放了个div。
//        c1.style.backgroundImage="url(404.jpg)";
            ctx.globalCompositeOperation = "source-over";
            ctx.fillStyle = '#edeaea';
            ctx.fillRect(0,0,c1.clientWidth,c1.clientHeight);
            ctx.fill();

            ctx.font = "22px 'Microsoft yahei',Helvetica,Arial,sans-serif";
            ctx.textAlign = "center";
            ctx.fillStyle = "#d76d71";
            ctx.fillText("刮刮试试手气",c1.width/2,(c1.height/2+10));

            //把这个属性设为这个就可以做出圆形橡皮擦的效果
            //有些老的手机自带浏览器不支持destination-out,下面的代码中有修复的方法
            ctx.globalCompositeOperation = 'destination-out';
        }

        //鼠标按下 和 触摸开始
        function eventDown(e){
            e.preventDefault();
            ismousedown=true;
            $('.scratchArea .inside').show();
            if(!guakai){
                getCoupon();
                guakai = true;
            }
        }

        //鼠标抬起 和 触摸结束
        function eventUp(e){
            e.preventDefault();

            //得到canvas的全部数据
            var a = ctx.getImageData(0,0,c1.width,c1.height);
            var j=0;
            for(var i=3;i<a.data.length;i+=4){
                if(a.data[i]==0)j++;
            }

            //当被刮开的区域等于一半时，则可以开始处理结果
            if(j>=a.data.length/16){
                isOk = 1;
            }
            ismousedown=false;
        }

        //鼠标移动 和 触摸移动

        function eventMove(e){
            e.preventDefault();
            if(ismousedown) {
                if(e.changedTouches){
                    e=e.changedTouches[e.changedTouches.length-1];
                }

                var topY = document.getElementById("top").offsetTop;
                var oX = $('#c1').offset().left,
                        oY = $('#c1').offset().top;

                var x = (e.clientX + document.body.scrollLeft || e.pageX) - oX || 0,
                        y = (e.clientY + document.body.scrollTop || e.pageY) - oY || 0;
                //画360度的弧线，就是一个圆，因为设置了ctx.globalCompositeOperation = 'destination-out';
                //画出来是透明的
                ctx.beginPath();
                ctx.arc(x, y, fontem*1.2, 0, Math.PI * 2,true);

                //下面3行代码是为了修复部分手机浏览器不支持destination-out
                //我也不是很清楚这样做的原理是什么
                c1.style.display = 'none';
                c1.offsetHeight;
                c1.style.display = 'inherit';
                ctx.fill();
            }

            if(isOk){


            }
        }

        //请求砍价券地址
        function getCoupon() {
            var userId = GetQueryString('userId');
            $.ajax({
                url: $('.containerBox').attr('data-url')+userId,
                type: 'GET',
                timeout: 60000,
                dataType: 'json',
                data: {},
                success: function (data) {
                    if(data.code == 200){
                        $('.trophy').text('恭喜获得'+data.data.money+'元砍价券');
                    }else if(data.code == 300){
                        $('.trophy').text(data.message);
                    }

                },
                error:function (error) {
                    console.log('冒的数据 搞毛呀');
                    console.log(error);

                }
            });

        }

        //请求砍价券地址
        function checkCoupon() {
            var userId = GetQueryString('userId');
            $.ajax({
                url: $('.containerBox').attr('data-check')+userId,
                type: 'GET',
                timeout: 60000,
                dataType: 'json',
                data: {},
                success: function (data) {
                    console.log(data.code);

                    if(data.code == 200){
                        $('#c1').show();
                    }else if(data.code == 300){
                        console.log(123456)

                        $('#c1').hide();
                        $('.trophy').text(data.message);
                    }

                },
                error:function (error) {
                    console.log('冒的数据 搞毛呀');
                    console.log(error);

                }
            });

        }


    </script>


</body>
</html>