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
	<link rel="stylesheet" href="<%=path %>/css/diy2_5/wsp-order.css">
	<title>定制-刻字</title>
</head>
<body class="order">
	<div class="main-container inscribe">
		<div class="container-inner">
			<div class="container-data">
				<section class="carousel">
					<div class="carousel-head">
						<p>字字为你<a href="javascript:;" class="show-all" onclick="sending('3,http://120.26.112.213:8082/diyMall/index/orderInscribeCategory.do')">查看全部<img class="more-ico" src="<%=path %>/images/diy2_5/goodsDetails-choose.png" alt=""></a></p>
					</div>
					<div class="swiper-container swiper-container-2">
						<div class="swiper-wrapper" id="bannerSwiper">
						</div>
						<div class="swiper-pagination swiper-pagination-2 "></div>
					</div>
				</section>
				<!-- 图印定制商品列表 -->
				<section class="goods-container">
					<ul class="goods-list clearfix">
					</ul>
				</section>
				<div class="load-more">
					<img src="<%=path %>/images/diy2_5/loading.gif" alt="" style="height: 20px;">
				</div>
			</div>
		</div>
	</div>

	<script type='text/javascript' src='//g.alicdn.com/sj/lib/zepto/zepto.min.js' charset='utf-8'></script>
	<script type="text/javascript" src="<%=path %>/js/diy2_5/fastclick.js"></script>
    <script type='text/javascript' src='<%=path %>/js/diy2_5/swiper.min.js' charset='utf-8'></script>
	<script type="text/javascript" src="<%=path %>/js/diy2_5/wsp-order.js"></script>
</body>
</html>