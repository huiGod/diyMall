<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="gbp" value="${goodsBuyParam}" />
<%
	String path = request.getContextPath();
%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
	<meta charset="UTF-8">
	<meta content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0" name="viewport">
	<meta content="yes" name="apple-mobile-web-app-capable"/>
	<meta name="format-detection" content="telephone= no">
	<title>商务合作</title>
	<style>
		*{padding:0;margin:0;}
		.box{width:100%;position:relative;}
		.box img{display:block;width:100%;}
		.back{position:absolute;top:0;left:0;width:15.6vw;height:15.6vw;}
	</style>
</head>
<body>
	 <div class="box">
	 	<img src="<%=path %>/images/UUMall/business_01.jpg" alt=""><img src="<%=path %>/images/UUMall/business_02.jpg" alt=""><img src="<%=path %>/images/UUMall/business_03.jpg" alt=""><img src="<%=path %>/images/UUMall/business_04.jpg" alt="">
	 	<img src="<%=path %>/images/UUMall/business_05.jpg" alt=""><img src="<%=path %>/images/UUMall/business_06.jpg" alt="">
	 	<a href="javascript:history.go(-1)" class="back"> </a>
	  </div>
</body>
</html>
