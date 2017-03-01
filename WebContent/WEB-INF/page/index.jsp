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
    <meta content="yes" name="apple-mobile-web-app-capable"/>
    <link rel="stylesheet" href="//g.alicdn.com/msui/sm/0.6.2/css/??sm.min.css,sm-extend.min.css">
    <link rel="stylesheet" href="${ctx}/css/css.css">
    <title>51app</title>
</head>
<body>
	<div class="page-group" app=${app } deviceNo=${deviceNo } vapp=${vapp } >
	    <!-- tab1：diy -->
        <div class="page page-current" id="index" style="top:62px;">
			<!-- 广告条 -->
			<img src="${ctx}/images/unload.png" style="display: none">
            <header class="bar bar-nav  noPadding">
                 <div class="swiper-container" data-space-between='10'>
			    <div class="swiper-wrapper">
			    </div>
			</div>
            </header>	            
	        <!-- diy产品 -->                  
            <div class="content  infinite-scroll infinite-scroll-bottom"  data-distance="0" style="top:26vw;bottom:105px;padding-top:5px;-webkit-overflow-scrolling : touch;">
             	<c:forEach items="${list}" var="gpi">
	             	<div class="diy-content linec fl" onclick="sending(${gpi.id})" id="goods_${gpi.id}">
						<img src="${ctx}/images/mid-autumn2.png" class="fes-active" style="z-index:10"> 	
						<div class="diy-goods">
							<div class="diy-pic-box1">
								<img class="fix-h border-fff block" src="${gpi.icoUrl}">
								<!-- <img src="${ctx}/images/summer-coupon.png" class="summer-coupon"> -->
							</div>
							<div class="diy-details">
								<h2 class="clearfix fc5 line-h22"><i class="fl fs12 line-h24 fb">¥</i><span class="fl fs17 fb">${gpi.nowPrice}</span> <i class="fr fs9 fc2">已售<i class="fb fc5 fs12">${gpi.sell}</i>件</i></h2>
								<p class="fs12 fc-b ov-hidden">${gpi.name}</p>
							</div>
						</div>
					</div>
				</c:forEach>             	
            </div>	            
		</div>
	</div>

<script type='text/javascript' src='//g.alicdn.com/sj/lib/zepto/zepto.min.js' charset='utf-8'></script>
<script type='text/javascript' src='//g.alicdn.com/msui/sm/0.6.2/js/??sm.min.js,sm-extend.min.js' charset='utf-8'></script>
<script type="text/javascript">
		// 加载图片边框             ${app}
		var imgNum = $(".fix-h").length;
		$(".fix-h").on("load",function(){if((--imgNum)==0){$(".fix-h").addClass("border-f5");}});
		var swiperBox = $(".swiper-wrapper");
		$.ajax({
	    	url: '${ctx}/goods/adImgList.do',
	    	type: 'GET',
	    	dataType: 'json',
		    cache:false,
		    timeout:"60000",
		    success:function(data){
		    	var _data = data.data;
		    	var str="";	
	    		$.each(_data, function(index, val) {
	    			var href ="'"+_data[index].url+"'";
	        		str='<div class="swiper-slide"><a onclick="sendingHref('+href+')"><img src="'+_data[index].img+'"></a></div>';
	        		swiperBox.append(str);	        		
	    		});       	 
		    }
		});
		$.init();
		$(function() {
			$(".swiper-container").swiper({
				speed: 300,
				spaceBetween: 10,
				autoplayDisableOnInteraction : false,
				observer:true,
			    autoplay:3000,
			});
		}); 
		function connectNZOCJSBridge(callback) {
			if (window.NZOCJSBridge) {
				callback(NZOCJSBridge)
			} else {
				document.addEventListener('NZOCJSBridgeReady', function() {
					callback(NZOCJSBridge)
				}, false)
			}
		}
		function sending(id){
			data = {"click":id};
			connectNZOCJSBridge(function(bridge) {
				bridge.send(data, function(responseData) {})
			});
		}
		function sendingHref(href){
			//href 为空时不与手机端交互
			if(href=='') return;	
			data = {"href":href};
			connectNZOCJSBridge(function(bridge) {
				bridge.send(data, function(responseData) {})
			});
		}
		
	</script>
</body>
</html>