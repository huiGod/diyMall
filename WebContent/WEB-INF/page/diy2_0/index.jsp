<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
	String path=request.getContextPath();
%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, minimum-scale=1, user-scalable=no">
    <meta name="apple-mobile-web-app-capable" content="yes">
    <meta name="apple-mobile-web-app-status-bar-style" content="black">
    <link rel="shortcut icon" href="images/favicon.ico">
    <link rel="stylesheet" href="<%=path %>/css/diy20/swiper.min.css">
    <link rel="stylesheet" href="<%=path %>/css/diy20/index.css">
    <title>唯优品-主页</title>
</head>
<body class="index body-bg">
  <main class="page-content" data-url='http://api.diy.51app.cn/diyMall/homeNav2/home.do'>
		<!-- 广告条 -->
		<img src="./images/unload.png" class="dis-no">
    <!-- <header class="bar bar-nav theme">
        <div class="swiper-container themeTitleList" id="themeTitleList">
				    <div class="swiper-wrapper" id="themesNav">
	                        
				    </div>
				</div>
    </header>	 -->            
    <!-- diy产品 -->   
    <div id="tab-content" class="_js-touchScroll tab-content" style="top:0">
      <div class="tabContent" data-index="0">
        <!-- 轮播图  -->
        <div class="swiper-container banner">
          <div class="swiper-wrapper " id="bannerSwiper">
            
          </div>
          <!-- 轮播分页符-->
          <div class="swiper-pagination banner-pagination"></div>
        </div>
        <!--公告条 -->
        <!-- <div class="swiper-container swiper-vertical tips">
          <div class="swiper-pagination"></div>
          <div class="swiper-wrapper">
            <c:forEach items="${privilege}" var="i">
            	<div class="swiper-slide swiper-no-swiping"><span>${i.about}</span></div>
            </c:forEach>
          </div>
        </div> -->
        <!-- 专题商品 -->
        <div class="specialGoods">
          <ul class="specialGoodsUl" id="customGoods">
          
          </ul>
        </div>
        <!-- 活动banner 如有活动再添加 -->
        <!-- <div class="act-banner" id="act_banner" ></div> -->
        <!-- 精品专区 -->
        <div class="content-block niceProduct-content" id="niceGoods">
          <ul class="themeList">
            
          </ul>
        </div>
        <!-- 团体定制 -->
        <div class="content-block teamBooking-content" id="teamGoods">
          <ul>
            
          </ul>
        </div>
      </div>  
    </div>         
	</main>
	<script src="<%=path %>/js/diy20/zepto.min.js"></script>
  <script src="<%=path %>/js/diy20/touch.js"></script>
  <script src="<%=path %>/js/diy20/swiper.min.js"></script>
	<script src="<%=path %>/js/diy20/index.js"></script>
</body>
</html>