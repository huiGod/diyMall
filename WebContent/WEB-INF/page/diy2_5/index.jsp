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
    <title>首页</title>
    <link rel="stylesheet" href="<%=path %>/css/diy2_5/swiper-3.3.1.min.css">
    <!-- <link type="text/css" rel="stylesheet" href="<%=path %>/css/diy2_5/hd-index.css"/> -->
    <link type="text/css" rel="stylesheet" href="<%=path %>/css/diy2_5/hd-index-rem.css"/>

</head>
<body>

    <div id="first" class="containerBox" data-page="index" data-firstUrl = 'http://120.26.112.213:8082/diyMall/index/home.do' data-otherUrl="http://120.26.112.213:8082/diyMall/index/homeRecommend.do?page=">
        <div class="page-cont">
            <!--loading页面-->
            <div class="hd-noLoad"><div class="hd-loading"></div></div>

            <div class="hd-hasLoad">

                <!--精选轮播开始-->
                <div class="cb-banner">
                <div class="swiper-container swiper-conBanner">
                <div class="swiper-wrapper"></div>
                <!-- 如果需要分页器 -->
                <div class="swiper-pagination"></div>
                </div>
                </div>
                <!--精选轮播结束-->

                <!--八个图标分类开始-->
                <div class="cb-classify"></div>
                <!--八个图标分类结束-->

                <!--今日特价开始-->
                <div class="cb-title">今日特价<i>厂/家/特/供/价/格/最/低</i></div>
                <div class="cb-sale"><div class="itemBox"></div></div>
                <!--今日特价结束-->

                <!--专题推荐开始-->
                <div class="cb-title">专题推荐<i>新/奇/特/好/物/大/汇/总
</i></div>
                <div class="cb-special"></div>
                <!--专题推荐结束-->


                <!--商品推荐开始-->
                <div class="recommendBox clearfix">
                    <h3>商品推荐</h3>
                    <div class="cb-recommend index-goods clearfix"></div>
                </div>
                <!--商品推荐结束-->


              <div class="infinite-scroll-preloader"><div class="preloader"></div></div>

          </div>

   

        </div>


    </div>


        <!--首页其他页 开始-->
    <div id="other" class="containerBox" data-page="index" data-firstUrl="http://120.26.112.213:8082/diyMall/index/topNavInfo.do?id=" data-otherUrl="http://120.26.112.213:8082/diyMall/index/topNavInfoByPage.do">
        <div class="page-cont">
            <!--loading页面-->
            <div class="hd-noLoad"><div class="hd-loading"></div></div>

            <div class="hd-hasLoad">

                <!--精选轮播开始-->
                <div class="cb-banner">
                    <div class="swiper-container swiper-conBanner">
                        <div class="swiper-wrapper"></div>
                        <!-- 如果需要分页器 -->
                        <div class="swiper-pagination"></div>
                    </div>
                </div>
                <!--精选轮播结束-->


                <!--商品推荐开始-->
                <!-- <h3>商品推荐</h3> -->
                <div class="cb-recommend index-goods clearfix"></div>
                <!--商品推荐结束-->

                <div class="infinite-scroll-preloader"><div class="preloader"></div></div>

            </div>

        </div>

    </div>
    <!--首页其他页 结束-->

    <div class="gotoTop hide"><img src="<%=path %>/images/diy2_5/hd-gotoTop.png" /></div>

    
    <script src="//cdn.bootcss.com/fastclick/1.0.6/fastclick.min.js"></script>
    <script type='text/javascript' src='//g.alicdn.com/sj/lib/zepto/zepto.min.js' charset='utf-8'></script>
    <script type='text/javascript' src='<%=path %>/js/diy2_5/swiper.min.js' charset='utf-8'></script>
    <!-- <script type='text/javascript' src='<%=path %>/js/diy2_5/lazyload.min.js' charset='utf-8'></script> -->
    <!-- <script type='text/javascript' src='<%=path %>/js/diy2_5/hd-main.js'></script> -->
    <!-- <script type='text/javascript' src='<%=path %>/js/diy2_5/hd-main.js?v=2.503' ></script> -->

    <script type="text/javascript"> 
document.write("<s"+"cript type='text/javascript' src='<%=path %>/js/diy2_5/hd-main.js?t="+ Date.parse(new Date())+"'></s" + "cript>"); 
</script>



</body>
</html>