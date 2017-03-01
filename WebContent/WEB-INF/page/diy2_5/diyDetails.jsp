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
	<meta name="format-detection" content="telephone=no">
	<link rel="stylesheet" href="<%=path %>/css/diy2_5/wsp-order-detail.css">
	<title>定制详情</title>
</head>
<body class="order-detail">

	<div class="main-container">
		<div class="page-group">
			<!-- 详情介绍页 -->
			<div id="tab1" class="page detail">
				<nav class="tab-nav">
					<a href="javascript:;" class="tab-item toggle active">详情介绍</a>
					<a href="javascript:;" class="tab-item toggle">规格参数</a>
					<a href="javascript:;" class="tab-item toggle">包装售后</a>
				</nav>
				<div class="detail-page-group">
					<div class="detail-page-group-inner">
						<!-- 定制介绍 -->
						<section class="detail-page introduce">
							<!-- <div class="video-container">
								<p>视频展示</p>
								<video src="" controls="controls">您的手机不支持视频播放</video>
							</div> -->
							<div class="img-container">
							</div>
						</section>
						<!-- 规格参数 -->
						<section class="detail-page spec">
						</section>
						<!-- 包装售后 -->
						<section class="detail-page pack">
						</section>
					</div>
				</div>
			</div>
		</div>
	</div>
   	<div id="goodsId" hidden="hidden">商品ID</div>
   	<div id="goodsName" hidden="hidden">商品名称</div>
	
	<script type='text/javascript' src='//g.alicdn.com/sj/lib/zepto/zepto.min.js' charset='utf-8'></script>
    <script type='text/javascript' src='<%=path %>/js/diy2_5/lazyloadImg.min.js' charset='utf-8'></script>
	<script src="//cdn.bootcss.com/fastclick/1.0.6/fastclick.js"></script>
	<script type="text/javascript">
	// 获取参数名
	function getQueryString(name) {
	    var reg = new RegExp('(^|&)' + name + '=([^&]*)(&|$)', 'i');
	    var r = window.location.search.substr(1).match(reg);
	    if (r != null) {
	        return unescape(r[2]);
	    }
	    return null;
	}
	function lazyLoad(){
    	var lazyloadImg = new LazyloadImg({
    	    el: '[data-echo]', //匹配元素
    	    top: 50, //元素在顶部伸出长度触发加载机制
    	    right: 50, //元素在右边伸出长度触发加载机制
    	    bottom: 50, //元素在底部伸出长度触发加载机制
    	    left: 50, //元素在左边伸出长度触发加载机制
    	    qriginal: false, // true，自动将图片剪切成默认图片的宽高；false显示图片真实宽高
    	    load: function(el) { //图片加载成功后执行的回调方法，传入一个参数,即元素本身
    	    }
    	});
  	}
$(function(){

	// 处理300ms点击延迟
	FastClick.attach(document.body);

	function paddingData(json){
        var imgHtml = '',
        	specHtml = '',
        	packHtml = '',
        	imgContainer = $('.img-container'),
        	specContainer = $('.spec'),
        	packContainer = $('.pack');
        $('#goodsId').text(json.data.id);
        $('#goodsName').text(json.data.name);
        // 视频
        // json.data.vedio == '' ? $('.video-container').hide() : $('video').attr('src',json.data.vedio);
        // 图文详情
        json.data.introduceList.forEach(function(data, index, dataArray){
        	imgHtml += '<img src="http://file.diy.51app.cn/uu20/placeholder.png" data-echo="' + data + '" alt="">';
        });
        imgContainer.append(imgHtml);
        // 规格参数
        json.data.parameterList.forEach(function(data, index, dataArray){
        	specHtml += '<p><span class="detail-title">' + data.title + '</span><span class="detail-desc">' + data.txt + '</span></p>'
        });
        specContainer.append(specHtml);
        // 包装售后
        json.data.packAfterSaleList.forEach(function(data, index, dataArray){
        	packHtml += '<p><span class="detail-title">' + data.title + '</span><span class="detail-desc">' + data.txt + '</span></p>'
        });
        packHtml += '<p><span class="detail-title">' + json.data.priceNote.title + '</span><span class="detail-desc">' + json.data.priceNote.txt + '</span></p>'
        packContainer.append(packHtml);
        lazyLoad();
    }

	function ajaxInit(url){
		$.ajax({
            async: false,
            url: url,
            type: 'GET',
            dataType: 'json',
            timeout: 60000,
            success: function(json){
                paddingData(json);
            },
            error: function(xhr, status, error){
                console.log(status)
            }
        });
	}
    ajaxInit('http://120.26.112.213:8082/diyMall/commodity/details.do?type=' + getQueryString('type') + '&id=' + getQueryString('id'));

	// 有切换效果的按键组
	$('.toggle').click(function(){
		$(this).addClass('active').siblings().removeClass('active');
    });

    $('.tab-item').click(function(event){
		var that = $(this),
			index = that.index(),
			container = $('.detail-page-group-inner');
		var translateX = 0;
		translateX = index * 100;
		container.css({
			right: translateX + '%',
			transition: 'right .3s'
		});
	});

	$('.pack .detail-desc').each(function(index, elem){
		var that = $(elem),
			regExp = /(\d{3,4}\-(\d{7,8}))|(\d{3,4})\-(\d{3,4})\-(\d{3,4})/ig,
			text = that.text();
		text.match(regExp) && that.html('<a href="tel:' + text + '">' + text + '</a>');
	});

});
	</script>
</body>
</html>