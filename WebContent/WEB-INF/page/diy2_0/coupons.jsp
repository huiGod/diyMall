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
    <link rel="shortcut icon" href="<%=path %>/images/favicon.ico">
    <link rel="stylesheet" href="<%=path %>/css/diy20/custom.css">
    <title>唯优品- 优惠券</title>
</head>
<body class="_js-coupons body-bg pos-body">
	<main class="getCoupons" data-url="${url1 }" >
		<header class="enableCoupons">
			<input type="text" maxlength="10" placeholder="输入您的优惠券激活码">
			<button class="enableBtn" data-url="${url2 }">激活</button>
		</header>

	</main>
	<script src="<%=path %>/js/diy20/zepto.min.js"></script>
	<script src="<%=path %>/js/diy20/touch.js"></script>
	<script src="<%=path %>/js/diy20/myCustom.js"></script>
</body>
</html>