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
			<img src="${ctx}/images/tips-summer.png" alt=""><span class="red">任性送，满减活动同步进行中，见详情</span><span class="red"></span><span class="red"></span>
		  </c:if>
		  <c:if test="${goodsTitle.isBoutique==1}">
			<img src="${ctx}/images/tips-summer.png" alt=""><span class="red">满100减10,满200减30,满300减50</span><span class="red"></span><span class="red"></span>
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
	
	<!-- 为您推荐 -->
    <div class="recommentForU">
      <div class="ForUTitle"><span class="recommentForU-icon"></span>&nbsp;为您推荐</div>
      <ul>
      	<c:forEach items="${goodsTitle.recommend}" var="rec">
        <li class="recommentLi">
         <a href="javascript:sending(${rec.id},${rec.good_id},${rec.isBoutique})">
          <div class="goodPicBox"><img src="${rec.previewImgUrl}"></div>
          <div class="describ">
            <h3 class="goodDescribH3">${rec.title} </h3>
            <p class="price">¥<span class="price-num">${rec.now_price}</span></p>
          </div>
         </a>
        </li>
        </c:forEach>
      </ul>
    </div>
    <script type="text/javascript">
    function connectNZOCJSBridge(callback) {
		if (window.NZOCJSBridge) {
			callback(NZOCJSBridge)
		} else {
			document.addEventListener('NZOCJSBridgeReady', function() {
				callback(NZOCJSBridge)
			}, false)
		}
	}
	function sending(id,goodId,isBoutique){
		data = {"id":id,"goodType":goodId,"isBoutique":isBoutique};
		console.log(data);
		connectNZOCJSBridge(function(bridge) {
			bridge.send(data, function(responseData) {})
		});
	}
    </script>
</body>
</html>