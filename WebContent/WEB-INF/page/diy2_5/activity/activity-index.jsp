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
	<title>设计大赛</title>
</head>
<body class="activity">
	<div class="fullwidth-img _js-index">
		<img src="<%=path %>/images/diy2_5/index_01.png" alt="">
		<img src="<%=path %>/images/diy2_5/index_02.png" alt="">
		<a href="javascript:;" onclick="sending('3,http://120.26.112.213:8082/diyMall/activitePage/activityGoods.do')"><img src="<%=path %>/images/diy2_5/index_03.png" alt=""></a>
		<img src="<%=path %>/images/diy2_5/index_04.png" alt="">
		<img src="<%=path %>/images/diy2_5/index_05.png" alt="">
		<a href="javascript:;" class="see-sort"><img src="<%=path %>/images/diy2_5/index_06.png" alt=""></a>
		<img src="<%=path %>/images/diy2_5/index_07.png" alt="">
	</div>
	
	<script type='text/javascript' src='//g.alicdn.com/sj/lib/zepto/zepto.min.js' charset='utf-8'></script>
	<script src="//cdn.bootcss.com/fastclick/1.0.6/fastclick.min.js"></script>
	<script type="text/javascript" src="<%=path %>/js/diy2_5/wsp-activity.js"></script>
</body>
</html>