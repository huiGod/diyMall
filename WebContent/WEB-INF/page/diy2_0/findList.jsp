<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
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
    <link rel="stylesheet" href="<%=path %>/css/diy20/custom.css">
    <title>唯优品- 发现</title>
    <style type="text/css">
    	.articleList li img{height:28vw;}
    	.top-nav ul li{    padding: 0 6.2vw 15px;}
    </style>
</head>
<body class="_js-findList body-bg">
	<!-- 发现列表  -->
	<main class="page-content" style="top:68px;width:96%;left:2%;" data-url='http://api.diy.51app.cn/diyMall/commodity/findList.do'>
		<!--  导航条 -->
		<nav class="top-nav" style="background-color:#fff;">
			<ul>
				
			</ul>
		</nav>
		<!--  内容区 -->
		<section class="articleList">
			<ul>
				
			</ul>
		</section>
		<!-- 活动banner -->
		<section class="actBanner">
			
		</section>
	</main>
	<script src="<%=path %>/js/diy20/zepto.min.js"></script>
	<script src="<%=path %>/js/diy20/touch.js"></script>
	<script src="<%=path %>/js/diy20/myCustom.js"></script>
</body>
</html>