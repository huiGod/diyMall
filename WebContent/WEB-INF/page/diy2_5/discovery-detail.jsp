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
	<link rel="stylesheet" href="<%=path %>/css/diy2_5/wsp-discovery.css">
	<title></title>
</head>
<body class="discovery-detail">
	<div class="main-container">
		<div class="container-inner">
			<h1 class="discovery-head"></h1>
			<h3 class="related-info clearfix ">
				<p class="text-left">
					<img src="" alt="" class="headimg"><span class="author"></span>
				</p>
				<p class="text-right">
					阅读<span class="read-time"></span>&nbsp|&nbsp<span class="release-date"></span>
				</p>
			</h3>
			<ul class="article-list">
			</ul>
		</div>
	</div>
	<!-- <div id="shareText" hidden="hidden">分享的文字</div>
    <div id="shareUrl" hidden="hidden">分享的链接</div>
   	<div id="shareLogo" hidden="hidden">分享的logo</div> -->
   	<div id="specialTitle" hidden="hidden">文章标题</div>

	<script type='text/javascript' src='//g.alicdn.com/sj/lib/zepto/zepto.min.js' charset='utf-8'></script>
	<script type="text/javascript" src="<%=path %>/js/diy2_5/fastclick.js"></script>
	<script type="text/javascript" src="<%=path %>/js/diy2_5/lazyloadImg.min.js"></script>
	<script>
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
	    console.log(id)
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
	
	function lazyLoad(){
	    var lazyloadImg = new LazyloadImg({
	        el: '[data-echo]', //匹配元素
	        top: -50, //元素在顶部伸出长度触发加载机制
	        right: 0, //元素在右边伸出长度触发加载机制
	        bottom: 0, //元素在底部伸出长度触发加载机制
	        left: 0, //元素在左边伸出长度触发加载机制
	        qriginal: false, // true，自动将图片剪切成默认图片的宽高；false显示图片真实宽高
	        load: function(el) { //图片加载成功后执行的回调方法，传入一个参数,即元素本身
	            el.style.cssText += 'animation: gradIn .5s;';
	        }
	    });
	}
	
	// 获取参数名
	function getQueryString(name) {
	    var reg = new RegExp('(^|&)' + name + '=([^&]*)(&|$)', 'i');
	    var r = window.location.search.substr(1).match(reg);
	    if (r != null) {
	        return unescape(r[2]);
	    }
	    return null;
	}
	
	$(function(){
	    // 处理300ms点击延迟
	    FastClick.attach(document.body);
	
	    function paddingData(json){
	        $('.discovery-head').html(json.data.topCont.title);
	        $('.headimg').attr('src', json.data.topCont.headImg);
	        $('.author').html(json.data.topCont.author);
	        // $('.read-time').html(json.data.topCont.view);
	        $('.release-date').html(json.data.topCont.ctime);
	        $('#specialTitle').text(json.data.topCont.title);
	        var html = '',
	            container = $('.article-list'),
	            sendData = '';
	        json.data.centerCont.forEach(function(data, index, dataArray){
	            sendData = '\'' + data.isBoutique + ',' + data.good_id+'\'';
	            var img = (data.img && data.img !="http://file.diy.51app.cn/" ) ? '<img class="lazyloadimg" src="../images/diy2_5/placeholder.png" data-echo="' + data.img + '">' : '',
	            	article = (data.text) ? '<p class="article-text">' + data.text + '</p>' : '';
	            html += '<li class="article-card">'+
	                        article +
	                        '<div class="img-box">'+ img +
	                        '</div>';
	            if(data.good_id){
	                html += '<div class="goods-info clearfix">'+
	                            '<div class="goods-desc">'+
	                                '<p class="goods-name">' + data.title + '</p>'+
	                                '<p class="goods-price">￥' + data.now_price + '</p>'+
	                            '</div>'+
	                            '<a class="goods-buy" data-goodstype="' + data.isBoutique + '" data-goodsid="' + data.good_id + '" href="javascript:;" onclick="sending(' + sendData +')">立即购买</a>'+
	                        '</div>'+
	                    '</li>';
	                }
	        });
	        container.append(html);
	        lazyLoad();
	        $.ajax({
	            async: false,
	            url: 'http://120.26.112.213:8082/diyMall/commodity/getView/' + id + '.do',
	            type: 'POST',
	            dataType: 'json',
	            timeout: 60000,
	            success: function(json){
	                $('.read-time').text(json.data);
	            }
	        });
	    }
	
	    function ajaxInit(url){
	        $.ajax({
	            async: false,
	            url: url,
	            type: 'POST',
	            dataType: 'json',
	            timeout: 60000,
	            success: function(json){
	                paddingData(json);
	            },
	            error: function(xhr, status, error){
	                console.log('error');
	            }
	        });
	    }
	    var id = getQueryString('id');
	    ajaxInit('http://120.26.112.213:8082/diyMall/commodity/findDetail/' + id + '.do');
	    // 设置分享的链接
	    // $('#shareUrl').text(location.href + '\&share=1');
	    // 分享出去的页面商品购买链接处理
	    // if(getQueryString('share')){
	    // 	var that = $('.goods-buy');
	    // 	if(that.attr('data-goodstype') == 1){
	    // 		that.attr('href', 'http://test.diy.51app.cn/diyMall2/UGoods/goodsInfo.do?id=' + that.attr('data-goodsid'));
	    // 	}else{
	    // 		that.click(function(){
	    // 			confirm("该商品为定制商品。\n下载唯优品APP，一起来定制？") && that.attr('href','https://itunes.apple.com/cn/app/id1105250240?mt=8');
	    // 		});
	    // 	}
	    // }
	});
	</script>
</body>
</html>