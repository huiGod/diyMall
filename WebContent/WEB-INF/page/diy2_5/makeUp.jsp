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
    <title>凑单</title>
   <link type="text/css" rel="stylesheet" href="<%=path %>/css/diy2_5/hd-index-rem.css"/>
    <style type="text/css">
        body,{background: #fff;-webkit-overflow-scrolling: touch;}
        .containerBox{
            background: #fff;
            position: absolute;
            top:0;
            left:0;
            right:0;
            bottom:0;
            overflow: auto;
        }
        .page-cont{min-height: 101%}
        img{width: 100%}
        .makeUpIndent{padding-bottom: 15.625vw}
        .makeUpIndent .item{
            padding:10px 15px;
            height:26.5625vw;
        }
        .makeUpIndent .item .imgBox{
            float: left;
            width: 26.5625vw;
            height:100%;
            box-sizing: border-box;
            border:1px solid #e5e5e5;
            border-radius: 4px;
            overflow: hidden;
        }
        .makeUpIndent .item .info{
            float: left;
            width: 63vw;
            text-align: justify;
            box-sizing: border-box;
            padding-left:10px;
            padding-top: 5px;
            position: relative;
            height:100%;
            overflow: hidden;
        }
        .makeUpIndent .item .info p{
            height:70%;
            overflow: hidden;
        }
        .makeUpIndent .item .price{
            position: absolute;
            bottom: 5px;
            left: 10px;
            color: #fe3f56;
            font-size: 12px;
        }
        .makeUpIndent .item .price i{
            font-size: 14px;
        }
        .makeUpIndent .item .info .buy{
            width: 7.8125vw;
            height: 7.8125vw;
            position: absolute;
            bottom: 5px;
            right:0;
            z-index: 88;
            padding:10px 0 0 10px;
        }
         .makeUpIndent .line{
            height:1px;
            margin-left: 10px;
            position: relative;
        }
        .makeUpIndent .line:after {
            content: '';
            position: absolute;
            top: 0;
            left: 0;
            border-bottom: 1px solid #e5e5e5;
            -webkit-box-sizing: border-box;
            box-sizing: border-box;
            width: 200%;
            height: 200%;
            -webkit-transform: scale(0.5);
            transform: scale(0.5);
            -webkit-transform-origin: left top;
            transform-origin: left top;
        }
        .makeUpIndent .line:last-child{  display: none;  }
        .bottomTips{
            position: fixed;
            bottom: 0;
            left:0;
            z-index: 888;
            width: 100%;
            height:15.625vw;
            background: rgba(0,0,0,.7);
            color: #e5e4e5;
            padding:5px 15px;
            box-sizing: border-box;
            display: none;
        }
        .bottomTips .lack{margin-top: 5px}
        .autoPopupBox{
            position: fixed;
            top:0;
            left:0;
            right:0;
            bottom:0;
            opacity: 0;
            display: none;
        }
        .autoPopup{
            position: fixed;
            top:0;
            left:0;
            z-index: 9999;
            width: 100%;
            height:100%;
            line-height: 40px;
            border-radius: 10px;
            background: rgba(0,0,0,0);
            color: #fff;
            text-align: center;
            transition: all .2s ease;
            -webkit-transition:all .2s ease;
            display: none;
        }
        .autoPopup.act{
            top:50%;
            left:50%;
            width: 2.94rem;
            height:1.66rem;
            margin: -0.83rem 0 0 -1.47rem ;
            background: rgba(0,0,0,.6);
            display: block;
        }
        .autoPopup i{
            display: block;
            width: .6rem;
            height: .6rem;
            margin: .3rem auto 0;
        }
        .autoPopup i img{  display: none;  }
    </style>

</head>
<body>
    <div class="bottomTips">
        <div class="already">自营商品共计：<i>15</i> 元</div>
        <div class="lack">还差： <i></i> 元即享免运费服务</div>
    </div>
    <div  class="containerBox" id='scrollable' data-goods = 'http://120.26.112.213:8082/diyMall/shops/togetherGoods.do' data-join='http://120.26.112.213:8082/diyMall/shops/addShopByGood.do?userId='>
        <div class="page-cont">
            <!--loading页面-->
            <div class="hd-noLoad"><div class="hd-loading"></div></div>
            <div class="hd-hasLoad">

                <div class="makeUpIndent" data-price="135">

                    <div class="item clearfix">
                        <div class="imgBox"><img src="http://file.diy.51app.cn/uu20/special/2cce0150.jpg"/></div>
                        <div class="info">
                            <p>卡通移动电源卡通移动电源卡通移动电源</p>
                            <div class="price">&yen; <i>99.</i><b>0</b></div>
                            <div data-price="12.4" class="buy"><img src="http://file.diy.51app.cn/uu20/makeUp-buy.png"/></div>
                        </div>
                    </div>
                    <div class="line"></div>




                </div>
                <!-- <div class="infinite-scroll-preloader"><div class="preloader"></div></div> -->

                



            </div>


            </div>




    </div>

    <div class="autoPopup">
        <i><img class="ok" src="<%=path %>/images/diy2_5/chenggong_31x31@2x.png"/>
            <img class="no" src="<%=path %>/images/diy2_5/shibai_31x31@2x.png"/></i>

        <span></span>
    </div>
    <div class="autoPopupBox"></div>


    <script type='text/javascript' src='//g.alicdn.com/sj/lib/zepto/zepto.min.js' charset='utf-8'></script>
    <script src="//cdn.bootcss.com/fastclick/1.0.6/fastclick.min.js"></script>
    <script type='text/javascript' src='<%=path %>/js/diy2_5/lazyload.min.js' charset='utf-8'></script>
   <script type='text/javascript' src='<%=path %>/js/diy2_5/hd-makeUp.js' ></script>


</body>
</html>