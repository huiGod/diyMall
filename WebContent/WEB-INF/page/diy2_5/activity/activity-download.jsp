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
	<title>下载唯乐购</title>
</head>
<body>
	<div class="fullwidth-img">
		<!-- <img src="<%=path %>/images/diy2_5/fixed.png" alt="" class="fixed-head _js-affix"> -->
		<img src="<%=path %>/images/diy2_5/dl_01.png" alt="">
	</div>
	<div class="fullwidth-img">
		<img src="<%=path %>/images/diy2_5/dl_02.png" alt="">
		<img src="<%=path %>/images/diy2_5/dl_03.png" alt="">
		<a href="https://itunes.apple.com/cn/app/id1105250240?mt=8"><img src="<%=path %>/images/diy2_5/dl_04.png" alt=""></a>
		<img src="<%=path %>/images/diy2_5/dl_05.png" alt="">
	</div>

	<script type='text/javascript' src='//g.alicdn.com/sj/lib/zepto/zepto.min.js' charset='utf-8'></script>
	<script src="//cdn.bootcss.com/fastclick/1.0.6/fastclick.min.js"></script>
	<script type="text/javascript" src="<%=path %>/js/diy2_5/wsp-activity.js"></script>
</body>
</html>