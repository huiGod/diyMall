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
    <title>作品展示- 投票</title>
</head>
<body class="blue-bg">
	<main class="vote" >
		<header>
			<img src="<%=path %>/images/diy20/vote-banner.jpg" alt="">
		</header>
		<section class="preview-area center">
			<header>
				<img src="<%=path %>/images/diy20/preview.jpg" alt="" class="pos-rel">
			</header>
			<article>
				<p><img src="<%=path %>/images/diy20/heart.png" alt=""><span class="pink fs36">10</span></p>
				<button class="vote-btn opacity btn-h bg-cover">投票</button>
				<img src="<%=path %>/images/diy20/segline.png" alt="">
			</article>
		</section>
		<section class="operation-area center">
			<header>
				<button dat="直接购买" class="buy-btn opacity btn-h bg-cover btn-senddat">直接购买</button>
				<button dat="我要参与" class="join-btn opacity btn-h bg-cover btn-senddat">我要参与</button>
			</header>
			<article class="bg-bitwhite fs28 color1 text-left lineh40 vote-production">
				<p>作品名称：<span class="pink">微微一笑很倾城</span></p>
				<p>简&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;介：<span class="color9">微微一笑很倾城微微一笑很倾城微微一笑很倾城微微一笑很倾城</span></p>
			</article> 
		</section>
	</main>
	<script src="<%=path %>/js/diy20/zepto.min.js"></script>
    <script src="<%=path %>/js/diy20/touch.js"></script>
    <script src="<%=path %>/js/diy20/myCustom.js"></script>
</body>
</html>