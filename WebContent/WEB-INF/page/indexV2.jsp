<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<%
	String path = request.getContextPath();
%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="apple-mobile-web-app-capable" content="yes">
    <meta name="apple-mobile-web-app-status-bar-style" content="black">
    <link rel="shortcut icon" href="<%=path%>/images/favicon.ico">
    <link rel="stylesheet" href="//g.alicdn.com/msui/sm/0.6.2/css/??sm.min.css,sm-extend.min.css">
    <link rel="stylesheet" href="${ctx}/css/css.css">
    <link rel="stylesheet" href="${ctx}/css/index-alter.css">
    <title>优品汇-主页</title>
</head>
<body>
	<div class="page-group">
	    <!-- tab1：diy -->
        <div class="page page-current" id="index" style="top:62px;">
			<!-- 广告条 -->
			<img src="${ctx}/images/unload.png" style="display: none">
            <header class="bar bar-nav theme">
            	<!-- slide0	 -->
                <div class="swiper-container themeTitleList" id="themeTitleList">
				    <div class="swiper-wrapper" id="themesNav">
	                        
				    </div>
				</div>
            </header>	            
	        <!-- diy产品 -->   
	        <div id="index-content" class="diyMall-index"> 
             	<!-- Slider1 -->               
                <div class="swiper-container banner" id="bannerk" data-space-between='10'>
                  <div class="swiper-wrapper" id="bannerSwiper">

                  </div>
                  <!-- Pagination  -->
                  <div class="swiper-pagination bannerPagination"></div>
                </div>
                <!-- Slider2  -->
                <div class="swiper-container swiper-vertical tips" id="tips">
                  <div class="swiper-wrapper">
                  	<!-- EL表达式 初始化加载数据 -->
                    <c:forEach items="${privilege}" var="p">
                      <div class="swiper-slide"><span>年中钜惠：${p.about}</span></div>
                    </c:forEach>
                  </div>
                </div>
                <!-- 定制商品 -->
                <div class="specialGoods">
	                <ul class="specialGoodsUl" id="specialGoodsUL">

	                </ul>
                </div>
                <!-- 专题 -->
                <div class="content-block specialTopic" id="specialTopic">
                  	                  
                </div>
                 <!-- 团体定制 -->
                <div class="content-block teamBooking-content" id="teamGoods">
					 
                </div>
            </div> 	            
		</div>
	</div>

	<script type='text/javascript' src='//g.alicdn.com/sj/lib/zepto/zepto.min.js' charset='utf-8'></script>
	<script type='text/javascript' src='//g.alicdn.com/msui/sm/0.6.2/js/??sm.min.js,sm-extend.min.js' charset='utf-8'></script>
	<script src="<%=path%>/js/indexAlter.js"></script>
</body>
</html>