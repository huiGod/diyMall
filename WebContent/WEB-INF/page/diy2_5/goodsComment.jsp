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
    <title>商品评论</title>
    <link rel="stylesheet" href="<%=path %>/css/diy2_5/swiper-3.4.0.min.css">
    <link type="text/css" rel="stylesheet" href="<%=path %>/css/diy2_5/hd-index.css"/>
    <style type="text/css">
        img{width: 100%}
        .overEllipsis{overflow: hidden;white-space: nowrap; text-overflow: ellipsis;}
        .wordBreak{word-wrap:break-word;word-break:break-all; }
        .main,.swiper-outsetNav,.swiper-outsetNav .swiper-wrapper{
            height:100%;
        }
        .main{
            padding: 0 10px;
            background: #ebebeb;
            position: relative;
        }
        .containerBox{
            top:71px;
        }
        .headerNav{
            background: #fff;
            overflow: hidden;
            border-bottom: 1px dashed #FE3E65;
            position: fixed;
            top:0;
            left:10px;
            right:10px;
            z-index: 888;
            display: -webkit-flex;
            display: flex;
            justify-content:space-between;
            padding:10px 5px;
            font-size: 13px;
        }
        .headerNav .item{
            text-align: center;
            width: 30px;
            height:30px;
            padding:10px;
            border-radius: 50%;
            background: #ffd1d7;
            opacity:1;
            color: #a3a3a3;
        }
        .headerNav .item.swiper-pagination-bullet-active{
            color: #FFFFFF;
            background: #FE3E65;
        }

        .comment-details{
            padding:0 10px;
        }
        .comment-details li{
            line-height: 1.5;
            border-bottom: 1px solid #e4e4e4;
            padding-bottom: 10px;
        }
        .comment-details .user{
            padding:10px 0;
            font-size: 16px;
        }
        .comment-details .user i{
            display: inline-block;
            width: 22px;
            height:22px;
            vertical-align: -5px;
            margin-right: 5px;
        }

        .comment-details .time{
            color: #a3a3a3;
        }
        .comment-details .imgBox{
            margin-top: 10px;
            overflow: hidden;
        }
        .comment-details .imgBox span{
            float: left;
            width: 21vw;
            height:28vw;
            overflow: hidden;
            margin-right: 1vw;
            margin-bottom: 1vw;
        }
        .showImgBox{
            float: left;
            width: 30vw;
            height:40vw;
            margin-right: 1vw;
            overflow: hidden;
            margin-top: 1vw;
        }
        .popLayer{
            position: fixed;
            top: 0;
            left: 0;
            width: 100%;
            height: 100%;
            background-color: #000;
            z-index: 1000;
        }
        .popLayer .swiper-slide {
            background-color: #000;
            color: #fff;
            width: 100vw;
            height: 90vh;
            line-height: 90vh;
            margin-top: 5vh;
            overflow: hidden;
        }
        .popLayer .swiper-slide img {
            width: 100%;
            /*max-height: 100vw;*/
            vertical-align: middle;
            display: inline-block;
        }
        .swiper-pagination-fraction{
            color: #fff;
        }
        .hide {
            display: none;
        }
        /*评价预览动画*/
        .photoBrowserIn{display:block;-webkit-animation:photoBrowserIn .4s forwards;animation:photoBrowserIn .4s forwards}
        @-webkit-keyframes photoBrowserIn{0%{-webkit-transform:translate3d(0,0,0) scale(.5);transform:translate3d(0,0,0) scale(.5);opacity:0}100%{-webkit-transform:translate3d(0,0,0) scale(1);transform:translate3d(0,0,0) scale(1);opacity:1}}@keyframes photoBrowserIn{0%{-webkit-transform:translate3d(0,0,0) scale(.5);transform:translate3d(0,0,0) scale(.5);opacity:0}100%{-webkit-transform:translate3d(0,0,0) scale(1);transform:translate3d(0,0,0) scale(1);opacity:1}}
        .photo-browser-out{display:block;-webkit-animation:photoBrowserOut .4s forwards;animation:photoBrowserOut .4s forwards}
        @-webkit-keyframes photoBrowserOut{0%{-webkit-transform:translate3d(0,0,0) scale(1);transform:translate3d(0,0,0) scale(1);opacity:1}100%{-webkit-transform:translate3d(0,0,0) scale(.5);transform:translate3d(0,0,0) scale(.5);opacity:0}}@keyframes photoBrowserOut{0%{-webkit-transform:translate3d(0,0,0) scale(1);transform:translate3d(0,0,0) scale(1);opacity:1}100%{-webkit-transform:translate3d(0,0,0) scale(.5);transform:translate3d(0,0,0) scale(.5);opacity:0}}
        .gotoTop {
            position: fixed;
            bottom: 40px;
            right: 20px;
            width: 30px;
            height: 30px;
            background: #fff;
            z-index: 999;
            border-radius: 50%;
        }
    </style>


</head>
<body>


<div class="main" data-url="http://120.26.112.213:8082/diyMall/evaluation/evalPage.do" data-num ='http://192.168.1.249:8081/diyMall/evaluation/evalNum.do' data-img = 'http://120.26.112.213:8082/diyMall/evaluation/evalPic.do'>

        <div class="headerNav"></div>

        <div class="swiper-container swiper-outsetNav ">
            <div class="swiper-wrapper">

                <div class="swiper-slide ">
                    <div class="containerBox all">
                        <ul class="comment-details"></ul>
                    </div>
                </div>

                <div class="swiper-slide ">
                    <div class="containerBox good">
                        <ul class="comment-details"></ul>
                    </div>
                </div>

                <div class="swiper-slide ">
                    <div class="containerBox medium">
                        <ul class="comment-details"></ul>
                    </div>
                </div>

                <div class="swiper-slide ">
                    <div class="containerBox bad">
                        <ul class="comment-details"></ul>
                    </div>
                </div>

                <div class="swiper-slide ">
                    <div class="containerBox showImg"></div>
                </div>

            </div>
        </div>


    <div class="popLayer hide">
        <div class="swiper-container swiper-imgShow">
            <div class="swiper-wrapper"></div>
            <div class="swiper-pagination"></div>
        </div>
    </div>
    <div class="gotoTop hide"><img src="http://192.168.1.249:8081/diyMall/images/goToTop.png"/></div>

</div>





<script type='text/javascript' src='//g.alicdn.com/sj/lib/zepto/zepto.min.js' charset='utf-8'></script>
<script src="//cdn.bootcss.com/fastclick/1.0.6/fastclick.min.js"></script>
<script type='text/javascript' src='<%=path %>/js/diy2_5/swiper-3.4.0.jquery.min.js'></script>
<script type='text/javascript' src='<%=path %>/js/diy2_5/lazyload.min.js' charset='utf-8'></script>
<script type='text/javascript' src='<%=path %>/js/diy2_5/hd-comment.js' ></script>




</body>
</html>