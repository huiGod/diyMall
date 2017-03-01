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
	<link rel="stylesheet" href="<%=path %>/css/diy2_5/wsp-activity.css">
	<title>定制商品</title>
</head>
<body class="activity-goods">
	<div class="main-container _js-goods">
		<div class="fullwidth-img">
			<img src="<%=path %>/images/diy2_5/head_01.png" alt="">
		</div>
		<div class="goods-container">
			<ul class="goods-list clearfix">
				<!-- <li class="list-item">
					<a href="javascript:;">
						<div class="img-box">
							<img src="<%=path %>/images/diy2_5/mengwa.jpg" alt="">
						</div>
						<div class="desc-box">
							<h3 class="goods-name">定制马克杯</h3>
							<p class="pink-btn"><a href="javascript:;" class="design-btn">去设计</a></p>
						</div>
					</a>
				</li> -->
			</ul>
		</div>
		<div class="load-more">
			<img src="<%=path %>/images/diy2_5/loading.gif" alt="" style="height: 20px;">
		</div>
	</div>

	<script type='text/javascript' src='//g.alicdn.com/sj/lib/zepto/zepto.min.js' charset='utf-8'></script>
	<script src="//cdn.bootcss.com/fastclick/1.0.6/fastclick.min.js"></script>
	<script type="text/javascript" src="<%=path %>/js/diy2_5/wsp-activity.js"></script>
</body>
</html>