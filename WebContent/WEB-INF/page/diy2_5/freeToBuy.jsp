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
    <title>0元购</title>
    <link type="text/css" rel="stylesheet" href="<%=path %>/css/diy2_5/hd-index-rem.css"/>

</head>
<body class="freeToBuy">

    <div class="containerBox"  data-url = 'http://120.26.112.213:8082/diyMall/zero/zeroGoods.do'>
        <div class="page-cont">
            <!--loading页面-->
            <div class="hd-noLoad"><div class="hd-loading"></div></div>

            <div class="hd-hasLoad">


                <div class="banner"><img src="<%=path %>/images/diy2_5/freeToBuy-banner.jpg" alt=""/></div>
                <div class="rule">
                    <div class="topRectangle"></div>
                    <div class="title"><span class="t1">砍价规则</span><span class="t2">查看0元购规则</span><i><img src="<%=path %>/images/diy2_5/freeToBuy-close.png"/></i></div>
                    <div class="textBox">
                        <div class="text">
                            <p>1.选择一款0元购商品进行定制，并将设计作品分享给您的朋友。</p>
                            <p>2.您的朋友下载唯乐购后，打开应用即可帮您砍价。</p>
                            <p>3.作品自发布起24小时内砍价有效，您邀请的每位好友可帮您砍价5元，直至0元购买。</p>
                            <div class="lace"><img src="<%=path %>/images/diy2_5/freeToBuy-lace.jpg"/></div>
                        </div>
                    </div>

                </div>
                <div class="rooftop"><img src="<%=path %>/images/diy2_5/freeToBuy-roof.png" alt=""/></div>

                <div class="goods"></div>
                <!--<div class="infinite-scroll-preloader"><div class="preloader"></div></div>-->

            </div>


        </div>

    </div>



    <script type='text/javascript' src='//g.alicdn.com/sj/lib/zepto/zepto.min.js' charset='utf-8'></script>
    <script src="//cdn.bootcss.com/fastclick/1.0.6/fastclick.min.js"></script>
    <!--<script type='text/javascript' src='js/lazyload.min.js' charset='utf-8'></script>-->

<script type="text/javascript">
//        we are stupid but strong


        FastClick.attach(document.body);    //初始化fastclick

        /*   js传值ios
         ==============================*/
        function connectNZOCJSBridge(callback) {
            if (window.NZOCJSBridge) {
                callback(NZOCJSBridge)
            } else {
                document.addEventListener('NZOCJSBridgeReady', function() {
                    callback(NZOCJSBridge)
                }, false)
            }
        }

        function sending(sendObj) {

            sendObj = JSON.parse(sendObj);
            console.log(sendObj);

            connectNZOCJSBridge(function(bridge) {
                bridge.send(sendObj, function(responseData) {})
            });


             sendObj = JSON.stringify(sendObj);
            console.log(sendObj);

            uqWyp.notifyInteraction(sendObj);
        }

        var url = $('.containerBox').attr('data-url');
        $.ajax({
            url: url,
            type: 'GET',
            dataType: 'json',
            success: function (data) {
                var dataBox='';
                var sendData ={};

                $.each(data.data,function (i) {

                    sendData = data.data[i];
                    sendData.click = data.data[i].goodId+","+ data.data[i].orgPrice;
                    sendData = '\''+JSON.stringify(sendData)+'\'';

                    dataBox += "<div class='item' onclick=sending("+ sendData +")><img src='"+ data.data[i].icoUrl +"'/></div>";
                });
                $('.freeToBuy .goods').empty().append(dataBox);

                //数据加载完成后显示界面
                $('.containerBox .hd-noLoad').hide();
                $('.containerBox .hd-hasLoad').show();

                var roofTop =$('.freeToBuy .rooftop').offset().top;

                var textBoxHeight = $('.freeToBuy .rule .textBox').height();


                $('.freeToBuy .rule .title .t2').click(function () {
                    $('.freeToBuy .rule .title i').show();
                    $('.freeToBuy .rule .title .t2').hide();
                    $('.freeToBuy .rule .title .t1').show();
                    $('.freeToBuy .rule .textBox').removeClass('close');
                    roofTop = roofTop + textBoxHeight;
                });
                $('.freeToBuy .rule .title i').click(function () {
                    $(this).hide();
                    $('.freeToBuy .rule .title .t1').hide();
                    $('.freeToBuy .rule .title .t2').show();
                    $('.freeToBuy .rule .textBox').addClass('close');
                    roofTop = roofTop - textBoxHeight;
                });

                $(window).scroll(function () {
                    var top = $(window).scrollTop();
                    if(top >= roofTop){
                                        $('.freeToBuy .rooftop').addClass('fixed')
                    }else{
                                        $('.freeToBuy .rooftop').removeClass('fixed')

                    }
                });

            },
            error:function (error) {
                console.log('冒的数据 搞毛呀');
                console.log(error);

            }
        });




    </script>


</body>
</html>