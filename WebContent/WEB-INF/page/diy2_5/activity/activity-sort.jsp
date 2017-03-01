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
	<title>人气排名</title>
</head>
<body class="activity-goods">
	<div class="main-container _js-sortPage">
		<div class="fullwidth-img">
			<img src="<%=path %>/images/diy2_5/sort_01.png" alt="">
		</div>
		<div class="sort-container">
			<div class="sort-first">
				<div class="fullwidth-img">
					<img src="<%=path %>/images/diy2_5/sort_02.png" alt="">
				</div>
				<div class="first-info">
					<a href="javascript:;">
						<div class="pink-card">
							<div class="sort-first-img">
								<img src="" alt="">
							</div>
							<div class="sort-first-desc">
								<h3 class="author"></h3>
								<p class="autograph"></p>
								<span class="first-ballot"><img src="<%=path %>/images/diy2_5/sort_04.png" alt="">&nbsp&nbsp<span class="vote-number"></span></span>
							</div>
						</div>
					</a>
				</div>
			</div>
			<div class="fullwidth-img">
				<img src="<%=path %>/images/diy2_5/sort_03.png" alt="">
			</div>
			<ul class="sort-list clearfix">
			</ul>
		</div>
		<div class="load-more">
			<img src="<%=path %>/images/diy2_5/loading.gif" alt="" style="height: 20px;">
		</div>
	</div>

	<script type="text/javascript" src="//g.alicdn.com/sj/lib/zepto/zepto.min.js" charset="utf-8"></script>
	<script src="//cdn.bootcss.com/fastclick/1.0.6/fastclick.min.js"></script>
	<script src="//cdn.bootcss.com/echo.js/1.7.3/echo.min.js"></script>
	<script type="text/javascript" src="<%=path %>/js/diy2_5/wsp-activity.js"></script>
</body>
</html>