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
	<link rel="stylesheet" href="<%=path %>/css/diy2_5/wsp-order.css">
	<title>刻字分类</title>
</head>
<body style="padding:0;background:#fff;">
	<div class="inscribe-container">
		<ul class="swiper-list clearfix">
			<!-- <li class="swiper-card"><a href=""><img src="img/3-1.png" alt=""></a></li> -->
		</ul>
	</div>

	<script type='text/javascript' src='//g.alicdn.com/sj/lib/zepto/zepto.min.js' charset='utf-8'></script>
	<script type="text/javascript" src="<%=path %>/js/diy2_5/fastclick.js"></script>
	<script type="text/javascript">
	// js传值ios
	function connectNZOCJSBridge(callback) {
		if (window.NZOCJSBridge) {
			callback(NZOCJSBridge)
		} else {
			document.addEventListener('NZOCJSBridgeReady', function() {
	    		callback(NZOCJSBridge)
			}, false)
		}
	}

	function sending(id) {
  		console.log(id);
  		data = {
    		"click": id
  		};
  		connectNZOCJSBridge(function(bridge) {
    		bridge.send(data, function(responseData) {})
  		});
    	try{
    		uqWyp.notifyInteraction(id);
    	}catch(e){
    		console.log(e);
    	}
	}

	function sendOC(sendObj) {
		console.log(sendObj);
		connectNZOCJSBridge(function(bridge) {
		bridge.send(sendObj, function(responseData) {})
		});
	}

	$(function(){
		// 处理300ms点击延迟
		FastClick.attach(document.body);
		
		// 获取参数名
		function getQueryString(name) {
		    var reg = new RegExp('(^|&)' + name + '=([^&]*)(&|$)', 'i');
		    var r = window.location.search.substr(1).match(reg);
		    if (r != null) {
		        return unescape(r[2]);
		    }
		    return null;
		}

		// 加载数据
		function ajaxMethod(url){
			$.ajax({
				async: 'false',
				url: url,
				type: 'POST',
				dataType: 'json',
				timeout: 6000,
				success: function(json){
					var specialList = $('.inscribe-container .swiper-list'),
						sendData = '',
						html = '';
					json.data.forEach(function(data, index, dataArray){
						if(data.type === 4){
							sendData = '\'' + 3 + ',http://120.26.112.213:8082/diyMall/commodity/orderSpecial.do?id=' + data.id + '&title=' + data.name + '\'';
							html += '<li class="swiper-card"><a href="javascript:;" onclick="sending(' + sendData + ')"><img src="' + data.img + '" alt=""></a></li>';
						}
					});
					specialList.append(html);
				},
				error: function(xhr, status, error){
					console.log('error');
				}
			});
		}
		var sortID = getQueryString('id');
		ajaxMethod('http://120.26.112.213:8082/diyMall/commodity/production.do?place=2');

	});
	</script>
</body>
</html>