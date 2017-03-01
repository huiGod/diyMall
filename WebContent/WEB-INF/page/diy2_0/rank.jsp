<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
	String path=request.getContextPath();
%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <!-- <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, minimum-scale=1, user-scalable=no"> -->
    <meta name="viewport" content="width=640,user-scalable=no">
    <meta name="apple-mobile-web-app-capable" content="yes">
    <meta name="apple-mobile-web-app-status-bar-style" content="black">
    <link rel="shortcut icon" href="images/favicon.ico">
    <link rel="stylesheet" href="<%=path %>/css/diy20/custom.css">
    <title>排名情况</title>
</head>
<body class="blue-bg">
	<main class="rank" >
		<header>
			<img src="<%=path %>/images/diy20/rank-banner.jpg" alt="">
		</header>
		<section class="my-works radius20">
			<aside class="fl imgBox">
				<img src="<%=path %>/images/diy20/preview.jpg" alt="" class="pos-rel radius10 pre-img">
			</aside>
			<article class="fr works-des fs24 lineh40 pos-rel">
				<p>我的作品</p>
				<p class="mt10">我的作品名称</p>
				<p class="mt10 mb20">我的作品简介我的作品简介内容</p>
				<p class="mt10">
					<img src="<%=path %>/images/diy20/heart.png" alt=""><i class="pink">1156</i>
					第<span class=" ">10</span>名
				</p>
			</article>
		</section>
		<section class="rank-list fs24 clearfix mb30">
			<!--<a href="vote.html" class="fl list-item radius20 center mt30 pos-rel">-->
				<!--<img src="./images/preview.jpg" alt="" class="radius10 pre-img">-->
				<!--<p class="left"><span>微微一笑很倾城</span><em><img src="./images/heart.png" alt="">1356</em></p>-->
			<!--</a>-->
			<!--<a href="vote.html" class="fl list-item radius20 center mt30 pos-rel">-->
				<!--<img src="./images/preview.jpg" alt="" class="radius10 pre-img">-->
				<!--<p class="left"><span>微微一笑很倾城</span><em><img src="./images/heart.png" alt="">1356</em></p>-->
			<!--</a>-->
			<!--<a href="vote.html" class="fl list-item radius20 center mt30 pos-rel">-->
				<!--<img src="./images/preview.jpg" alt="" class="radius10 pre-img">-->
				<!--<p class="left"><span>微微一笑很倾城</span><em><img src="./images/heart.png" alt="">1356</em></p>-->
			<!--</a>-->
			<!--<a href="vote.html" class="fl list-item radius20 center mt30 pos-rel">-->
				<!--<img src="./images/preview.jpg" alt="" class="radius10 pre-img">-->
				<!--<p class="left"><span>微微一笑很倾城</span><em><img src="./images/heart.png" alt="">1356</em></p>-->
			<!--</a>-->

		</section>
	</main>
	<script src="<%=path %>/js/diy20/zepto.min.js"></script>
    <script src="<%=path %>/js/diy20/touch.js"></script>
    <script src="<%=path %>/js/diy20/myCustom.js"></script>
</body>
</html>