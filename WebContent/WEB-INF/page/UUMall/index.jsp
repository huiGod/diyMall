<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
	String path = request.getContextPath();
%>
<!DOCTYPE html>
<html lang="zh-CN">
  <head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, minimum-scale=1, user-scalable=no, minimal-ui">
    <meta name="apple-mobile-web-app-capable" content="yes">
    <meta name="apple-mobile-web-app-status-bar-style" content="black">
    <title>唯优品</title>
    <link rel="shortcut icon" href="<%=path%>/images/UUMall/favicon.ico">
    <link rel="stylesheet" href="<%=path %>/css/UUMall/framework7.ios.min.css">
    <link rel="stylesheet" href="<%=path %>/css/UUMall/my-app.css">
  </head>
  <body class="indexBody-kwj">  
    <div class="statusbar-overlay"></div>
    <div class="views">
      <div class="view view-main">
        <!-- Top Navbar-->
        <div class="navbar">
          <div class="navbar-inner">
            <div class="left">
              <a href="<%=path%>/UMallUser/personalCenter.do" class="link icon-only external"><i class="icon icon-menu"></i></a>
            </div>
            <div class="center bold">唯优品</div>
            <div class="right"></div> 
            <!-- 二级导航栏 主题 -->
            <div class="subnavbar theme" >
              <!-- slide4 -->
              <div class="swiper-container themeTitleList">
                <div class="swiper-pagination"></div>
                <div class="swiper-wrapper" id="themeTitleList">
                  <div class="swiper-slide"><a href="#" class="switchTheme "><span class="active">热门推荐</span></a></div>
                  <c:forEach items="${nav}" var="n">
                    <div class="swiper-slide"><a href="<%=path%>/UGoods/toSpecial.do?special_ids=${n.id}" class="link col-auto switchTheme no-animation"><span >${n.name}</span></a></div>
                  </c:forEach>
                </div>
              </div>
              <img src="<%=path %>/images/UUMall/tip-right.png" id="tip-right" class='tip-right'>
            </div>
          </div>
        </div>
        <div class="pages navbar-through toolbar-through">
          <div data-page="index" class="page">
            <div class="page-content padding82">
              <div id="index-content">                
                <div class="swiper-container banner">
                  <div class="swiper-wrapper " id="bannerSwiper">
                    <!-- ajax加载 -->
                  </div>
                  <!-- Pagination, if required -->
                  <div class="swiper-pagination banner-pagination"></div>
                </div>
                <!-- tip 上下公告条 -->
                <!-- <div class="swiper-container swiper-vertical tips">
                  <div class="swiper-pagination"></div>
                  <div class="swiper-wrapper">
                    <div class="swiper-slide swiper-no-swiping"><span>浪漫赴七夕 ①全场同款定制商品第二件半价</span></div>
                    <div class="swiper-slide swiper-no-swiping"><span>浪漫赴七夕 ②爱情大转盘满减活动</span></div>
                  </div>
                </div> -->
                 <!-- 专题商品 -->
                <div class="specialGoods">
                  <ul class="specialGoodsUl" id="customGoods">
           				<!-- ajax加载 -->
                  </ul>
                </div>
                <!-- 活动banner -->
                <!-- <div class="act-banner" id="act_banner" >
                   
                </div> -->
                <!-- 精品专区 -->
                <div class="content-block-title niceProduct-title"><span>精品专区</span></div>
                <div class="content-block niceProduct-content" id="niceGoods">
                  <!-- ajax加载 -->
                </div>

                 <!-- 团体定制专区 -->
                <div class="content-block-title niceProduct-title teamBooking-title"><span>定制专区</span></div>
                <div class="content-block teamBooking-content" id="teamGoods">
                  <!-- ajax加载 -->
                </div>
              </div>   
            </div>
          </div>
        </div>
        <!-- 数据存放 微信 -->
        <div id="wxData">
          <span class="appId">${share.appId}</span>
          <span class="timestamp">${share.timestamp}</span>
          <span class="nonceStr">${share.noncestr}</span>
          <span class="signature">${share.sign}</span>
        </div>
      </div>
    </div>
    <script type="text/javascript" src="<%=path %>/js/UUMall/framework7.min.js"></script>
    <script type="text/javascript" src="<%=path %>/js/UUMall/my-app.js"></script>
    <script src="<%=path %>/js/UUMall/weixin.js"></script>
    <script src="<%=path %>/js/UUMall/wxConfig.js"></script>
  </body>
</html>