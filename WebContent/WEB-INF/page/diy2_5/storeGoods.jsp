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
    <title>店铺商品</title>
    <link type="text/css" rel="stylesheet" href="<%=path %>/css/diy2_5/hd-index-rem.css"/>

</head>
<body>



   <div class="containerBox" data- data-url="http://api.diy.51app.cn/diyMall/index/goodRecommend.do">
        <div class="page-cont">
        
            <!--loading页面-->
            <!-- <div class="hd-noLoad"><div class="hd-loading"></div></div> -->

            <div class="hd-hasLoad">


                <!--商品推荐开始-->
                <div class="recommendBox clearfix">
                    <div class="cb-recommend index-goods clearfix"></div>
                </div>
                <!--商品推荐结束-->

                <!--<div class="infinite-scroll-preloader"><div class="preloader"></div></div>-->

            </div>


        </div>

    </div>



    <script type='text/javascript' src='//g.alicdn.com/sj/lib/zepto/zepto.min.js' charset='utf-8'></script>
    <script src="//cdn.bootcss.com/fastclick/1.0.6/fastclick.min.js"></script>
    <script type='text/javascript' src='<%=path %>/js/diy2_5/swiper.min.js' charset='utf-8'></script>
    <script type='text/javascript' src='<%=path %>/js/diy2_5/lazyload.min.js' charset='utf-8'></script>
    <script type='text/javascript' src='<%=path %>/js/diy2_5/hd-main.js' ></script>


</body>
</html>