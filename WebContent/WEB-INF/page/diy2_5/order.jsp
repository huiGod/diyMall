<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
    String path=request.getContextPath();
%>
<!DOCTYPE html>
<html lang="en">
<head>
	<meta charset="UTF-8">
	<meta name="viewport" content="width=device-width, initial-scale=1, minimum-scale=1, maximum-scale=1, user-scalable=no">
	<meta name="apple-mobile-web-app-capable" content="yes">
	<meta name="apple-mobile-web-app-status-bar-style" content="black">
    <link rel="stylesheet" href="<%=path %>/css/diy2_5/swiper-3.3.1.min.css">
	<link rel="stylesheet" href="<%=path %>/css/diy2_5/wsp-order-old.css">
	<title>定制页</title>
</head>
<body class="order">
	<!-- 标题栏 -->
	<header class="custom-bar">
		<a href="javascript:void(0);" class="custom-tab-link toggle active">图印</a>
		<a href="javascript:void(0);" class="custom-tab-link toggle">刻字</a>
	</header>
	
	<div class="main-container">
		<!-- 主要数据内容 -->
		<div class="custom-page-group">
			<!-- 图印屏 -->
			<div id="tab1" class="custom-page impression">
				<div class="page-inner">
					<!-- 顶部轮播 -->
					<section class="carousel">
    					<div class="swiper-container swiper-container-1">
							<div class="swiper-wrapper" id="bannerSwiper">
								<!-- <div class="swiper-slide">
									<a href="javascript:;"><img src="img/1-3.png"></a>
								</div> -->
							</div>
							<div class="swiper-pagination swiper-pagination-1 "></div>
						</div>
					</section>
					<!-- 图印定制商品列表 -->
					<section class="goods-container">
						<ul class="goods-list clearfix">
							<!-- <li class="goods-card">
								<a href="javascript:;">
									<div class="img-box">
										<img src="img/2-1.png" alt="">
									</div>
									<div class="text-box">
										<h3 class="goods-desc">定制个性T恤</h3>
										<p class="goods-price">￥<span>99.9</span></p>
									</div>
								</a>
							</li> -->
						</ul>
					</section>
					<div class="load-more">
						即将加载更多...
					</div>
				</div>
			</div>
			<!-- 刻字屏 -->
			<div id="tab2" class="custom-page inscribe">
				<div class="page-inner content pull-to-refresh-content infinite-scroll infinite-scroll-bottom">
					<!-- 下拉刷新 -->
					<div class="pull-to-refresh-layer">
            		    <div class="preloader"></div>
            		    <div class="pull-to-refresh-arrow"></div>
            		</div>
					<!-- 顶部轮播 -->
					<section class="carousel">
						<div class="carousel-head">
							<p>字字为你<a href="javascript:;" class="show-all" onclick="sending('2,http://192.168.1.249:8081/diyMall/index/orderInscribeCategory.do')">查看全部>></a></p>
						</div>
						<div class="swiper-container swiper-container-2">
							<div class="swiper-wrapper" id="bannerSwiper">
								 <!-- <div class="swiper-slide">
									<ul class="swiper-list clearfix">
										<li class="swiper-card"><a href=""><img src="img/3-1.png" alt=""></a></li>
									</ul> -->
							</div>
							<div class="swiper-pagination swiper-pagination-2 "></div>
						</div>
					</section>
					<!-- 图印定制商品列表 -->
					<section class="goods-container">
						<ul class="goods-list clearfix">
							<!-- <li class="goods-card">
								<a href="javascript:;">
									<div class="img-box">
										<img src="img/2-1.png" alt="">
									</div>
									<div class="text-box">
										<h3 class="goods-desc">定制个性T恤</h3>
										<p class="goods-price">￥<span>99.9</span></p>
									</div>
								</a>
							</li> -->
						</ul>
					</section>
					<div class="load-more">
						即将加载更多...
					</div>
				</div>
			</div>
		</div>
	</div>
	<!-- 吐司提示 -->
	<!-- <div class="toast"><span></span></div> -->

	<script type='text/javascript' src='//g.alicdn.com/sj/lib/zepto/zepto.min.js' charset='utf-8'></script>
	<script type="text/javascript" src="<%=path %>/js/diy2_5/fastclick.js"></script>
    <script type='text/javascript' src='<%=path %>/js/diy2_5/swiper.min.js' charset='utf-8'></script>
	<script type="text/javascript" src="<%=path %>/js/diy2_5/wsp-order-old.js"></script>
</body>
</html>