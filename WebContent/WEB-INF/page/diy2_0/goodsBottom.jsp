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
    <meta name="format-detection" content="telephone=no">
    <link rel="shortcut icon" href="images/favicon.ico">
    <link rel="stylesheet" href="<%=path %>/css/diy20/custom.css">
    <title>唯优品 - 商品</title>
</head>
<body class="_js-goods body-bg">
	<main class="page-content goods-content" data-url="${url }">
		<section class="goodsInfo-desc">
        
		</section>
		<section class="goodsInfo-option">
         
		</section>
		<section class="tips">
			
		</section>
		<section class="recommentForU">
         <div class="ForUTitle"><span class="recommentForU-icon"></span>&nbsp;为您推荐</div>
         <ul>
            
         </ul>
		</section>
	</main>
	<script src="<%=path %>/js/diy20/zepto.min.js"></script>
	<script src="<%=path %>/js/diy20/touch.js"></script>
	<script src="<%=path %>/js/diy20/myCustom.js"></script>
</body>
</html>