<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
    String path=request.getContextPath();
%>
<!DOCTYPE html>
<html lang="zh">
<head>
	<meta charset="UTF-8">
	<meta name="viewport" content="width=device-width, initial-scale=1, minimum-scale=1, maximum-scale=1, user-scalable=no">
	<meta name="apple-mobile-web-app-capable" content="yes">
	<meta name="apple-mobile-web-app-status-bar-style" content="black">
	<link rel="stylesheet" href="<%=path %>/css/diy2_5/wsp-order.css">
	<title></title>
	<style>
	/*取消滚动条*/
	::-webkit-scrollbar {
    	display: none;
	}
	html,body {
		width: 100%;
		height: 100%;
		padding: 0;
	}
	.special-list {
		width: 100%;
		overflow-x: hidden;
		overflow-y: scroll;
		overflow-scrolling: touch;
		-webkit-overflow-scrolling: touch;
	}
	</style>
</head>
<body class="order-special">
	<div class="special-container">
		<ul class="special-list goods-list clearfix">
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
    	uqWyp.notifyInteraction(id);
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
		// 设置title
		$('title').text(decodeURI(location.href).match(/(^|&)title=([^&]*)(&|$)/i)[2]);

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
					if(json.code != 200 || json.data == '' || json.data.length ==0){
						return false;
					}
					var specialList = $('.special-list'),
						sendData = '',
						html = '';
					json.data.forEach(function(data, index, dataArray){
						sendData = '\'' + data.gType + ',' + data.goods+'\'';
						if(data.custom_id){
							sendData = '\'' + data.gType + ',' + data.custom_id+'\'';
						};
						html += '<li class="goods-card">'+
									'<a href="javascript:;" onclick="sending(' + sendData + ')">'+
										'<div class="img-box">'+
											'<img src="'+data.img+'" alt="">'+
										'</div>'+
										'<div class="text-box">'+
											'<h3 class="goods-desc">'+data.name+'</h3>'+
											'<p class="goods-price">￥<span><b>'+(data.price ? data.price.split('.')[0] : '-')+'.</b>' + (data.price ? data.price.split('.')[1] : '-')+'</span></p>'+
										'</div>'+
									'</a>'+
								'</li>';
					});
					specialList.append(html);
				},
				error: function(xhr, status, error){
					console.log('error');
				}
			});
		}
		var sortID = getQueryString('id');
		ajaxMethod('http://120.26.112.213:8082/diyMall/commodity/getMakeSort.do?sortId=' + sortID);

	});
	</script>
</body>
</html>