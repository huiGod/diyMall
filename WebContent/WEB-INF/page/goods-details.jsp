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
	<meta name="format-detection" content="telephone=no" />
    <link rel="stylesheet" href="${ctx}/css/goods-details.css">
    <title>51app</title>
</head>
<body class="details-abs">
	<div id="gotoTop" class="hide" ><img src="${ctx}/images/goToTop.png"></div>
	<div class="nav ">
		<ul class="clearfix">
			<li class="clicked">${goodsDetails.title}</li>
			<li>规格参数</li>
			<li>包装售后</li>
		</ul>
	</div>
	<div class="wrapper" id="lazyLoad">		
		<div class="details detail-1">		
			<c:forEach items="${goodsDetails.introduceList}" var="details">
				<img src="http://www.dowebok.com/demo/2013/53/images/blank.gif" data-echo="${details}"  >
			</c:forEach>		
		</div> 
		<div class="details detail-2 hide">
			<ul>
				<c:forEach items="${goodsDetails.parameterList}" var="parameter">
					<li><span>${parameter.title}</span><div>${parameter.txt}</div></li>
				</c:forEach>
			</ul>
		</div>
		<div class="details detail-3 hide">
			<ul>
				<c:forEach items="${goodsDetails.packAfterSaleList}" var="pas">
						<li><span>${pas.title}</span><input type="text" value="${pas.txt}" readonly> </li>
				</c:forEach>
				<li><span>${goodsDetails.priceNote.title}</span><div class="price-D">${goodsDetails.priceNote.txt}</div>
			</ul>	
		</div>
	</div>	
	<script src="${ctx}/js/lib/zepto.min.js"></script>
	<script src="${ctx}/js/lib/touch.js"></script>
	<script src="${ctx}/js/lib/echo.min.js"></script>
	<script>		
		 echo.init({
			offset: 100,
			throttle: 0
		});
		$(document).ready(function(){
			var $li=$(".nav li");
	        $li.on("tap",function(){
	        	var tabIndex = $(this).index();
 		        $(this).addClass("clicked").siblings().removeClass('clicked');
				$(".wrapper .details").hide().eq(tabIndex).show();
			});

	        // 溢出滚动
			var contentObj = $(".wrapper")[0];
			overscroll(contentObj);
			$(".wrapper").on("scroll",function(){
				var top = contentObj.scrollTop;
				// console.log(top)
				if(top>500)  $("#gotoTop").show();
				if(top<500)  $("#gotoTop").hide();
			});
			var totopBtn = $("#gotoTop");
			totopBtn.on("tap",function(){
				$(".wrapper").scrollTo(1);
			});
			
			function overscroll(el) {
				el.addEventListener('touchstart', function() {
				    var top = el.scrollTop,
				        totalScroll = el.scrollHeight,
				        currentScroll = top + el.offsetHeight-1;
				    if(top === 0) {
				      el.scrollTop = 1;
				    } else if(currentScroll === totalScroll) {
				      el.scrollTop = top - 1;
				    }
				})
				el.addEventListener('touchmove', function(e) {
				    if(el.offsetHeight < el.scrollHeight)
				      e._isScroller = true;
				})
				$(document).on('touchmove', function(e) {
				  if(!e._isScroller) { e.preventDefault(); }
				})
			}
			(function($){
				$.extend($.fn, {
				    scrollTo: function(m){        
				        var n = this.scrollTop(), timer = null, that = this;
				        var constN = n;
				        var smoothScroll = function(m){
				            var per = Math.round(constN / 20);
				            n = n - per;
				            if(n<=per){
				            	n=m;
				            	if(n == m){
				            		that.scrollTop(n);
					            	$("#gotoTop").hide();
					                window.clearInterval(timer);				               
					                return false;
					            }
				            }
				            that.scrollTop(n);
				        };
				        timer = window.setInterval(function(){
				            smoothScroll(m);
				        }, 20);
				    }
				})
			})(Zepto)
		});
	</script>
</body>
</html>