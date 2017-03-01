<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    
    <meta name="apple-mobile-web-app-capable" content="yes">
    <meta name="apple-mobile-web-app-status-bar-style" content="black">
    <link rel="stylesheet" href="${ctx}/css/css.css">
    <title>51app</title>
</head>
<body>	
	<div class="show-good margin-bottom5">
		<div class="link-to-detail clearfix">
			<p class="show-desc fl fs15 line-h24 ov-hidden" >${goodsTitle.title}</p>
			
			<span class="fr iconfont icon-youbian fs12 fc00 line-h24"></span>
			
		</div>
		<div class="show-price">
			<span class="fc5 fb show-space"><i class="fs12">¥&nbsp;</i><span class="fs18 line-h24" id="nowPrice">${goodsTitle.nowPrice}</span></span>
			
			<i class="fc6 fs12 del-line">¥<span id="original_price">${goodsTitle.originalPrice}</span></i>
			<p class="fs13 fc6 showGood-forFree fr">${goodsTitle.transportfee}</p>
		
		</div>
		<div class="fatherDay-activity">
		  <c:if test="${goodsTitle.isBoutique==2}">
			<img src="${ctx}/images/mid-autumn.png" alt=""><span class="red">下单后，全场商品享八折优惠</span><span class="red"></span><span class="red"></span>
		  </c:if>
		  <c:if test="${goodsTitle.isBoutique==1}">
			<img src="${ctx}/images/mid-autumn.png" alt=""><span class="red">下单后，全场商品享八折优惠</span><span class="red"></span><span class="red"></span>
		  </c:if>
		</div>	
	</div>
	
	<div class="show-good selectOpt">
		<a href="javascript:void(0)" class="link-to-detail clearfix line-h24">
			<p class="show-desc fl fs15 ov-hidden" id="diyOption">
			已选&nbsp
			<c:forEach items="${goodsTitle.paramList}" var="pl">
						${pl}&nbsp
			</c:forEach>
			,1件
			</p>
			<span class="fr iconfont icon-youbian fs12 fc00"></span>
		</a>
	</div>
	
	

</body>
</html>