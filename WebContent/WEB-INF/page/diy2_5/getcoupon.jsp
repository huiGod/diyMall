<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
    String path=request.getContextPath();
%>
<!DOCTYPE html>
<html lang="en">
<head>
	<meta charset="UTF-8">
	<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, minimum-scale=1, user-scalable=no">
    <meta name="apple-mobile-web-app-capable" content="yes">
    <meta name="apple-mobile-web-app-status-bar-style" content="black">
	<link rel="stylesheet" href="<%=path %>/css/diy2_5/wsp-activity.css">
	<title>领取优惠券</title>
</head>
<body class="coupons-body">
	<main class="_js-coupons coupons-container">
		<header class="enter-coupons">
			<input type="text" maxlength="10" placeholder="输入您的优惠券激活码">
			<button class="act-btn">激活</button>
		</header>
		<ul class="coupons-list">
			<!-- <li class="coupons-card clearfix">
				<div class="coupons-price">
					<h3 class="coupons-orgprice ellipsis">￥<span>200</span></h3>
					<p class="coupons-about">满400减200</p>
				</div>
				<div class="coupons-desc">
					<h3 class="coupons-title">定制商品专用定制商品专用</h3>
					<p class="coupons-valid">有效日期&nbsp&nbsp<span>2016 11.13</span></p>
				</div>
				<div class="coupons-img">
					<img src="<%=path %>/images/diy2_5/getcoupons.jpg" alt="">
				</div>
			</li> -->
		</ul>
	</main>
	<script type='text/javascript' src='//g.alicdn.com/sj/lib/zepto/zepto.min.js' charset='utf-8'></script>
	<script src="//cdn.bootcss.com/fastclick/1.0.6/fastclick.min.js"></script>
	<script type="text/javascript" src="<%=path %>/js/diy2_5/wsp-activity.js"></script>
</body>
</html>