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
	<link rel="stylesheet" href="<%=path %>/css/diy2_5/wsp-discovery.css">
	<title>发现</title>
</head>
<body class="discovery">
	<!-- 发现页主内容 -->
	<div class="main-container">
		<div class="container-inner">
			<!-- 顶部广告 -->
			<div class="link-block">
			</div>
			<!-- 发现内容 -->
			<div class="discovery-container">
				<ul class="discovery-list clearfix">
				</ul>
			</div>
			<!-- 无限加载 -->
            <div class="load-more">
            	<img src="<%=path %>/images/diy2_5/loading.gif" alt="" style="height: 20px;">
            </div>
		</div>
	</div>

	<script type='text/javascript' src='//g.alicdn.com/sj/lib/zepto/zepto.min.js' charset='utf-8'></script>
	<script type="text/javascript" src="<%=path %>/js/diy2_5/fastclick.js"></script>
	<script type="text/javascript" src="<%=path %>/js/diy2_5/wsp-discovery.js"></script>
</body>
</html>