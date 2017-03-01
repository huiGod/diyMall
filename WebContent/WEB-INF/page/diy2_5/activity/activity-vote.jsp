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
	<link rel="stylesheet" href="<%=path %>/css/diy2_5/wsp-activity.css">
	<title>投票</title>
</head>
<body>
	<div class="vote-container">
		<header class="vote-head"></header>
		<div class="vote-main _js-worksdetail">
			<div class="vote-inner">
			 	<div class="work-img">
			 		<img src="" alt="" class="work-image">		<!-- <%=path %>/images/diy2_5/mengwa.jpg -->
			 	</div>
			 	<div class="work-name">
			 			<div class="fullwidth-line"></div>
			 			<h3 class="ellipsis"></h3>
			 			<p class="work-ballot">
			 				<img src="<%=path %>/images/diy2_5/sort_04.png" alt="">&nbsp<span></span>
			 			</p>
			 		</div>
				<div class="work-desc">
					<span></span>
				</div>
			</div>
		</div>
		<footer class="vote-foot">
			<div class="foot-inner">
				<a href="javascript:;" class="vote-btn _js-voteworks"></a>
				<a href="javascript:;" class="join-btn _js-jointype"></a>
			</div>
		</footer>
		<div id="shareText" hidden="hidden">唯乐购</div>
    	<div id="shareUrl" hidden="hidden">分享的链接</div>
    	<div id="shareLogo" hidden="hidden">分享的logo</div>
	</div>
	<div class="popLayer hide">
		<div class="swiper-container">
			<div class="swiper-wrapper">
			</div>
			<div class="swiper-pagination"></div>
		</div>		
	</div>

	<script type='text/javascript' src='<%=path %>/js/diy2_5/zepto.min.js' charset='utf-8'></script>
    <script type='text/javascript' src='<%=path %>/js/diy2_5/swiper.min.js' charset='utf-8'></script>
	<script src="//cdn.bootcss.com/fastclick/1.0.6/fastclick.min.js"></script>
	<script type="text/javascript" src="<%=path %>/js/diy2_5/wsp-activity.js"></script>
</body>
</html>