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
    <title></title>
    <link type="text/css" rel="stylesheet" href="<%=path %>/css/diy2_5/hd-index-rem.css"/>
    <style type="text/css">
        img{width: 100%}
        .special .main{
            width: 100%;
            overflow: hidden;
            background: #fff;
            box-sizing: border-box;
        }
        .special .topImg{
            width: 100%;
            height:45vw;
            overflow: hidden;
        }
        .special h3{
            color: #1a1a1a;
            font-size: 15px;
            margin-top: .3rem;
            text-align: left;
            padding:0 25px;
        }
        .special p{
            color: #646464;
            margin-top: 15px;
            text-align: justify;
            font-size: 13px;
            line-height: 1.6;
            /*letter-spacing: 1px;*/
        }
        .special p.cont{
            padding:0 25px;

        }
        .special .type4 p.cont{
            padding-bottom: .3rem;
            border-bottom: .06rem solid #f5f5f5;
        }
        .special .main.type4 .goods{
            padding:0 25px .3rem;
            overflow: hidden;
            border-bottom: 1px solid #f5f5f5;
        }
     
        .special .goods img{
            width: 100%;
            margin-top: .2rem;
        }
        .special .goods .info{
            float: left;
            overflow: hidden;
            color: #000;
            font-size: 13px;
            margin-top: 10px;
            width: calc(100vw - 130px);
        }
        .goods .info>div{
            height:20px;
            overflow: hidden;white-space: nowrap; text-overflow: ellipsis;
        }
        .goods .info .price{
            color: #fe3f56;
            font-size: 13px;
        }
        .goods .info .price b{
            font-size: 15px;
            font-weight: normal;
        }
        .goods .buy{
            float: right;
            width: 75px;
            height:30px;
            line-height: 30px;
            text-align: center;
            border: 1px solid #fe3f56;
            color: #fe3f56;
            border-radius: 3px;
            margin-top: 15px;
            font-size: 13px;
        }
        .type3 .index-goods{
            margin-top: .4rem;
            padding:.2rem 8px;
            background: #f5f5f5;
        }
        .index-goods .item .name span{
            width: calc(50vw - 70px);
        }
        #shareText,#shareUrl,#shareLogo{display: none}
    </style>



</head>
<body>
    <div id="shareText">分享的文字</div>
    <div id="shareUrl">分享的链接</div>
    <div id="shareLogo">分享的logo</div>

    <div class="containerBox special" data-url="http://120.26.112.213:8082/diyMall/index/specialInfo.do">

        <div class="page-cont">
            <div class="topImg"><img src=""/></div>

            <!--第一种方案开始-->
            <div class="main type4">
                <h3 id="specialTitle"></h3>
                <p class="cont"></p>
                <div class="goodsBox"></div>

            </div>
            <!--第一种方案结束-->

            <!--第二种方案开始-->
            <div class="main type3">
                <h3 id="specialTitle"></h3>
                <p class="cont"></p>

                <div class="index-goods goodsBox">

                </div>

            </div>
            <!--第二种方案结束-->

        </div>


    </div>

    <script type='text/javascript' src='//g.alicdn.com/sj/lib/zepto/zepto.min.js' charset='utf-8'></script>
    <script src="//cdn.bootcss.com/fastclick/1.0.6/fastclick.min.js"></script>

        <script type="text/javascript">
        (function changeFontSize() {
            var screenWidth = $(document).width();
            var htmlFontSize = screenWidth/6.4;
            $("html").css("font-size",htmlFontSize);
            $(window).resize(function(){
                screenWidth = $(document).width();
                htmlFontSize = screenWidth/6.4;
                $("html").css("font-size",htmlFontSize);
            });
        })();
        FastClick.attach(document.body);    //初始化fastclick

        var GetQueryString = function (name)
        {
            var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");
            var r = window.location.search.substr(1).match(reg);
            if(r!=null)return  unescape(r[2]); return null;
        }

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

        function sending(id) {
            console.log(id);
            data = {
                "click": id
            };
            connectNZOCJSBridge(function(bridge) {
                bridge.send(data, function(responseData) {})
            });
            uqWyp.notifyInteraction(id);
        }

        var sendOC = function (sendObj) {
            console.log(sendObj);
            connectNZOCJSBridge(function(bridge) {
                bridge.send(sendObj, function(responseData) {})
            });
        };

        //加载数据
        var loadData = function (url, async, successFunc) {
            $.ajax({
                url: url,
                type: 'POST',
                timeout: 60000,
                async: async,
                dataType: 'json',
                data: {},
                success: successFunc,
                error:function (error) {
                    console.log('冒的数据 搞毛呀');
                    console.log(error);

                }
            });
        };

        var id=GetQueryString('id');
        var type=GetQueryString('type');
       var url=$('.containerBox').attr('data-url')+'?id='+id+'&type='+type;
        var successFunc = function (data) {
            var recommendGoods = data.data.recommendGoods;
            var article= data.data.banners;
            var dataBox = '';
            var sendData ='';
            var priceBig ,priceSmall;
            if(data.code == 200){
                if(type == 4){
                    $('.type3').hide();
                    $('.type4 h3').text(article[0].title);
                    $('.type4 .cont').text(article[0].cont);
                    $('.topImg').empty().append('<img src="'+ article[0].imgUrl +'" alt=""/>');

                    $.each(recommendGoods,function (i) {
                        priceBig = String(recommendGoods[i].nowPrice).split('.')[0];
                        priceSmall = String(recommendGoods[i].nowPrice).split('.')[1];
                        if(!priceSmall){priceSmall = 0}
                        sendData = '\''+recommendGoods[i].type+','+ recommendGoods[i].id+'\'';
                        dataBox += '<div class="goods">' +
                                '<p>'+ recommendGoods[i].content +'</p>' +
                                '<img src="'+ recommendGoods[i].icoUrl +'"/>' +
                                '<div class="info"><div class="name">'+ recommendGoods[i].name +'</div>' +
                                '<div class="price">&yen; <b>'+ priceBig +'.</b>'+ priceSmall +'</div></div>' +
                                '<div class="buy" onclick="sending('+ sendData+')" >立即购买</div></div>';
                    });
                    $('.type4 .goodsBox').empty().append(dataBox);
                    $('.type4').show();

                }else if(type == 3){
                    $('.type4').hide();
                    $('.type3 h3').text(article[0].title);
                    $('.type3 .cont').text(article[0].cont);
                    $('.topImg').empty().append('<img src="'+ article[0].imgUrl +'" alt=""/>');



                    $.each(recommendGoods,function (i) {
                        priceBig = String(recommendGoods[i].nowPrice).split('.')[0];
                        priceSmall = String(recommendGoods[i].nowPrice).split('.')[1];
                        if(!priceSmall){priceSmall = 0}

                        sendData = '\''+recommendGoods[i].type+','+ recommendGoods[i].id+'\'';
                        var strA1 = '<div class="item" onclick="sending('+ sendData+')">';
                        var strA2 = '<div class="item noPostage" onclick="sending('+ sendData+')">';
                        var strB = '<div class="imgBox"><img src="'+ recommendGoods[i].icoUrl +'"/></div><div class="name"><i><img src="http://file.diy.51app.cn/uu20/noPostage.png"/></i><span>'+ recommendGoods[i].name +'</span></div><div class="price"><i>&yen; <b>'+ priceBig +'.</b>'+ priceSmall +'</i><span>已售'+ recommendGoods[i].sell +'件</span></div>';
                        var strC1 = '<div class="label"><img src="http://file.diy.51app.cn/wp/icoImg/label_tuyin.png"/></div></div>';
                        var strC2 = '<div class="label"><img src="http://file.diy.51app.cn/wp/icoImg/label_kezi.png"/></div></div>';
                        var strC3 = '<div class="label"><img src="http://file.diy.51app.cn/wp/icoImg/label_keyin.png"/></div></div>';
                        var strC4 = '</div>';

                        if(recommendGoods[i].ispostage == 1){

                            if(recommendGoods[i].type == 2){
                                dataBox += strA1+strB+strC1;
                            }else if(recommendGoods[i].type == 4){
                                dataBox += strA1+strB+strC2;
                            }else if(recommendGoods[i].type == 5){
                                dataBox += strA1+strB+strC3;
                            }else{
                                dataBox += strA1+strB+strC4;
                            }

                        }else{
                            if(recommendGoods[i].type == 2){
                                dataBox += strA2+strB+strC1;
                            }else if(recommendGoods[i].type == 4){
                                dataBox += strA2+strB+strC2;
                            }else if(recommendGoods[i].type == 5){
                                dataBox += strA2+strB+strC3;
                            }else{
                                dataBox += strA2+strB+strC4;
                            }
                        }

                    });
                    $('.type3 .goodsBox').empty().append(dataBox);
                    $('.type3').show();

                }

            }else{
                console.log("不是200的我不要")
            }
        };

        loadData(url, true, successFunc);


    </script>

</body>
</html>