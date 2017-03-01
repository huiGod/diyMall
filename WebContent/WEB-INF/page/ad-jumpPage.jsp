<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
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
		.back{position:absolute;top:0;left:0;width:15.6vw;height:15.6vw;	;}
	</style>
</head>
<body>
	 <div class="box">
	 	<c:forEach items="${imgList}" var="pl">
			<img src="${pl}" alt="">
		</c:forEach>
	  </div>
</body>
</html>