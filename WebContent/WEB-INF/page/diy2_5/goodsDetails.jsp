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
    <title>商品详情</title>
    <link type="text/css" rel="stylesheet" href="<%=path %>/css/diy2_5/hd-index-rem.css"/>
<style type="text/css">
        .containerBox{min-height:665px}
        @media screen and (min-width: 321px){.containerBox{min-height:745px}}
         @media screen and (min-width: 376px){.containerBox{min-height:805px}}
        .goodsInfo{
            overflow: hidden;
            background: #fff;
        }
        .goodsInfo .g-title{
            padding:0 10px;
        }
        .goodsInfo .g-title h3{
            color: #000000;
            margin-top: .2rem;
            word-wrap:break-word;
            word-break:break-all;
            text-align: left;
        }
        .goodsInfo .g-active{
            height:.88rem;
            line-height:.88rem;
            color: #969696;
            margin-top: 5px;
            margin-left: 10px;
            /*border-top: 1px solid #e5e5e5;*/
            font-size: 14px;
            position: relative;
        }
        .goodsInfo .g-active:after {
            content: '';
            position: absolute;
            top: 0;
            left: 0;
            border-top: 1px solid #e5e5e5;
            -webkit-box-sizing: border-box;
            box-sizing: border-box;
            width: 200%;
            height: 200%;
            -webkit-transform: scale(0.5);
            transform: scale(0.5);
            -webkit-transform-origin: left top;
            transform-origin: left top;
        }
        .goodsInfo .g-active i{
            color: #ff4040;
            margin-left: 5px;
        }
        .goodsInfo .g-num{
            padding:0 10px;
            height:.8rem;
            line-height: .8rem;
        }
        .goodsInfo .g-num .n1{
            color: #fe3f56;
            font-weight: 600;
            font-size: 16px;
        }
        .goodsInfo .g-num .n1 i{
            font-size: 23px;

        }
        .goodsInfo .g-num .n2{
            color: #868686;
        }
        .goodsInfo .g-num .n2 s{
            margin:0 10px;
        }
        .goodsInfo .g-num .n2 b{
            font-weight: normal;
        }
        .goodsInfo .g-num .n3{
            color: #868686;
            float: right;
            line-height: .8rem;
        }
        .goodsInfo .g-merit{
            font-size: 12px;
            padding:0 10px;
            height:.88rem;
            line-height: .88rem;
            background: #fafafa;

        }
        .goodsInfo .g-merit span{
            margin-right: 10px;
        }
        .goodsInfo .g-merit i{
            display: inline-block;
            width: 12px;
            height:12px;
            margin-right: 3px;
            vertical-align: -1px;
        }
        .goodsInfo .g-merit i img{
            width: 100%;
        }
         .g-choose{
             padding:0 10px;
            color: #000;
            height:.88rem;
            line-height: .88rem;
             border-top:.2rem solid #f5f5f5;
             font-size: 14px;
        }

         .g-choose i{
            display: inline-block;
            float: right;
            margin-top: .3rem;
             width: 7px;
        }
        .recommend{
            padding:0 8px;
            margin-top: 15px;
        }

        .recommend h3{
            text-align: center;
            color: #5b5b5b;
            line-height: 15px;
            margin-bottom: 15px;
        }
        .recommend h3 i{
            display: inline-block;
            width: 15px;
            height:15px;
            vertical-align: -2px;
            margin-right: 5px;
        }
        .recommend i img{
            width: 100%;
        }

    </style>


</head>
<body>


<div class="containerBox" data-url="http://120.26.112.213:8082/diyMall/commodity/goods.do">

    <div class="page-cont">

        <!--商品信息开始-->
        <div class="goodsInfo">
            <div class="g-title"><h3 id='goodsName'></h3></div>
            <div class="g-num">
                <span class="n1" >&yen; <i id="newPrice"></i></span>
                <span class="n2" ><s>&yen; <b id="oldPrice"></b></s><i></i></span>
                <span class="n3"></span>
            </div>
            <div class="g-active">促销 <i></i></div>
            <div class="g-merit">
              
            </div>
            <div class="g-choose" onclick="sending('选材质')">
                已选 <span id="paramList"></span><i><img src="<%=path %>/images/diy2_5/goodsDetails-choose.png"/></i>
            </div>
        </div>

        <!--商品信息结束-->
        <!--推荐商品开始-->
        <div class="recommend">
            <h3><i><img src="<%=path %>/images/diy2_5/goodsDetails-tui.png"/></i>为您推荐</h3>
            <div class="index-goods clearfix">
            </div>
        </div>

        <!--推荐商品结束-->




    </div>

</div>





<script type='text/javascript' src='//g.alicdn.com/sj/lib/zepto/zepto.min.js' charset='utf-8'></script>
<script src="//cdn.bootcss.com/fastclick/1.0.6/fastclick.min.js"></script>
<script type='text/javascript' src='<%=path %>/js/diy2_5/lazyload.min.js' charset='utf-8'></script>
<!--<script type='text/javascript' src='js/hd-main.js' ></script>-->
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

    //获取地址栏参数
    function GetQueryString(name)
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

    function sendOC(sendObj) {
        console.log(sendObj);
        connectNZOCJSBridge(function(bridge) {
            bridge.send(sendObj, function(responseData) {})
        });
    }

    function lazyLoad() {

        $("img").lazyload({
            data_attribute     :'echo',
            load:function ($elements, elements_left, options) {
                $elements.removeClass('placeholder')
            },
            container:$('.containerBox'),
        });

    }

    //加载数据
    function loadData(url, async, successFunc) {
        console.log("请求数据了");
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
    }




    var id= GetQueryString('id');
    var url = $('.containerBox').attr('data-url')+'?id='+id;
    var successFunc = function (data) {
        var dataBox = '',
            sendData = '',
            recommendGoods=data.data.recommend;

        $('.g-title h3').text(data.data.name);
        $('.g-active i').text(data.data.activity);

        var priceBig = String(data.data.nowPrice).split('.')[0];
        var priceSmall = String(data.data.nowPrice).split('.')[1];
        if(!priceSmall){priceSmall = 0}


        $('.g-num .n1').html('&yen; <i id="newPriceBig">'+ priceBig +'</i>.<span id="newPriceSmall">'+ priceSmall +'</span>');
        $('.g-num .n2').empty().append('<s>&yen; <b id="oldPrice">'+ data.data.org_price +'</b></s><i>已售'+ data.data.sell +'件</i>');
        if(data.data.ispostage == 2){
            $('.g-num .n3').text('');
        }
        $('.g-choose span').text(data.data.paramList);



        if(data.data.lable == ''){$('.goodsInfo .g-merit').remove()}
        dataBox = '';
        $.each(data.data.lable,function (i) {
            dataBox +='<span><i><img src="<%=path %>/images/diy2_5/goodsDetails-right.png"/></i>'+ data.data.lable[i] +'</span>' ;
        });
        $('.goodsInfo .g-merit').empty().append(dataBox);

        //商品推荐数据
        dataBox = '';
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


        $('.recommend .index-goods').empty().append(dataBox);
        lazyLoad();
    };

    loadData(url, true, successFunc);


</script>


</body>
</html>