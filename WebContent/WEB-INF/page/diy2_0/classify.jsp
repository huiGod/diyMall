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
    <link rel="shortcut icon" href="<%=path %>/diy20/images/favicon.ico">
    <link rel="stylesheet" href="<%=path %>/css/diy20/custom.css">
    <title>唯优品 - 分类</title>
    <style type="text/css">
    	*{user-select:none;-webkit-user-select:none;}
    </style>
</head>
<body class="_js-classify body-bg">
	<!-- 分类页	 -->
	<main class="page-content" data-url='http://api.diy.51app.cn/diyMall/commodity/goodsList.do'>
		<!-- 左边 导航条 -->
		<nav class="aside-nav" style="position: fixed;top:62px;left:0;">
			<em style="font-size:0;line-height:1px;display:block;height:1px;width:100%;margin:0;padding:0;">标题</em>
			<ul>
				
			</ul>
		</nav>
		<!-- 右边 内容区 -->
		<article class="goods-list-area" style="padding-bottom: 50px;">
			
		</article>
	</main>

	<script src="<%=path %>/js/diy20/zepto.min.js"></script>
	<script src="<%=path %>/js/diy20/touch.js"></script>
	<script src="<%=path %>/js/diy20/myCustom.js"></script>
</body>
</html>