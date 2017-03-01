<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
    String path=request.getContextPath();
%>
<!DOCTYPE html>
<html lang="en">
<head>
	<meta charset="UTF-8">
	<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, minimum-scale=1, user-scalable=no">
	<meta name="apple-mobile-web-app-capable" content="yes">
	<meta name="apple-mobile-web-app-status-bar-style" content="black">
	<meta name='apple-itunes-app' content='app-id=1105250240'>
	<link rel="stylesheet" href="<%=path %>/css/diy2_5/swiper-3.3.1.min.css">
	<link rel="stylesheet" href="<%=path %>/css/diy2_5/wsp-order-share.css">
	<title>0元购分享</title>
</head>
<body>
	<main class="free-buy-share">
		<div class="view-container">
			<section class="swiper-container">
				<div class="swiper-wrapper" id="bannerSwiper">
				</div>
				<div class="swiper-pagination "></div>
			</section>
			<section class="work-desc">
				<div class="price-info">
					<p class="now-price">当前价格:￥<span></span></p>
					<p class="org-price">原价:￥<span></span></p>
				</div>
				<div class="author-info">
					<h3 class="author-name"><img src="http://file.diy.51app.cn/userHead.png" alt=""><span></span></h3>
					<p class="author-autograph"></p>
				</div>
			</section>
			<section class="tip-copy">
				<p class="tip-article">为了顺利找到朋友的作品，开始砍价前请务必先复制此信息 <i>作品编号：<span class="work-no">无商品</span></i></p>
			</section>
			<section class="bar-code">
				<div class="bar-code-inner">
					<h3>砍价流程:</h3>
					<p>第1步：<span>复制上方作品编号。</span></p>
					<p>第2步：<span>点击下方“帮TA砍价”前往APPstore下载唯乐购。</span></p>
					<p>第3步：<span>打开唯乐购，将自动跳转到作品页面，开始砍价。</span></p>
					<div class="bar-code-img">
						<img src="" alt="">
					</div>
					<p class="mark">注：如果没有成功跳转到作品页面，也可以用【唯乐购】内的扫一扫，识别作品二维码</p>
				</div>
			</section>
		</div>
		<div class="toolbar">
			<div class="help-bar"><a id="openApp" href="http://a.app.qq.com/o/simple.jsp?pkgname=com.wcl.market" data-clipboard-text="normal">帮TA砍价</a></div>
		</div>
	</main>

	<script type='text/javascript' src='//g.alicdn.com/sj/lib/zepto/zepto.min.js' charset='utf-8'></script>
    <script type='text/javascript' src='<%=path %>/js/diy2_5/swiper.min.js' charset='utf-8'></script>
    <script src="//cdn.bootcss.com/clipboard.js/1.5.16/clipboard.min.js"></script>
	<script type="text/javascript" src="<%=path %>/js/diy2_5/fastclick.js"></script>
	<script>
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

		var clipboard = new Clipboard('#openApp');

		// 处理300ms点击延迟
		FastClick.attach(document.body);
		
		var mySwiper = new Swiper('.swiper-container', {
			direction: 'horizontal',
			pagination: '.swiper-pagination',
			observer:true,        //修改swiper自己或子元素时，自动初始化swiper  
    		observeParents:true  //修改swiper的父元素时，自动初始化swiper  
		});

		function loadData(url){
			var successFunc = function(json){
				if(json.code == 200 && json.data != ''){
					$('.work-no').text(json.data.id);
					$('.help-bar a').attr('data-id', json.data.id);
					$('.now-price span').text(json.data.money);
					$('.org-price span').text(json.data.orgPrice);
					$('.author-name img').attr('src', json.data.headUrl || 'http://file.diy.51app.cn/userHead.png');
					$('.author-name span').text(json.data.name);
					$('.author-autograph').text(json.data.cont);
					$('.bar-code-img img').attr('src', json.data.barCodeUrl);
					$('#openApp').attr('data-clipboard-text', '作品编号：' + json.data.id);
					var swiperContainer = $('.swiper-wrapper');
					var swiperSlide = '';
					for( var i=1; i<json.data.imgurl.split('@').length; i++){
						var t = json.data.imgurl.split('@');
						var c = ['', 'b', 'pb'];
						if(/\_/.test(t[0])){	// 链接有下划线 _ 的不需要拼 @
							swiperSlide = 	'<div class="swiper-slide">'+
												'<img src="' + t[0] + json.data.suffix + '">'+
											'</div>';
							break;
						}else {		// 拼p和pb
							swiperSlide += '<div class="swiper-slide">'+
												'<img src="' + t[0] + '@' + c[i] + json.data.suffix + '">'+
											'</div>';
						}
					}
					swiperContainer.append(swiperSlide);
				}else{
					$('.free-buy-share').hide();
					$('body').append('<div class="nodata-tip"><p>该作品不存在，可能被作者删除了。</p></div>');
				}
			};
			$.ajax({
				async: false,
				url: url,
				type: 'POST',
				dataType: 'json',
				timeout: 60000,
				success: successFunc,
				error: function(xhr, status, error){
					console.log('error' + status);
				}
			});
		};
		loadData('http://120.26.112.213:8082/diyMall/zero/userWorkById.do?id=' + getQueryString('id'));

		$('#openApp').click (function(e){	// 微信浏览器
        	if(navigator.userAgent.toLowerCase().match(/MicroMessenger/i) == 'micromessenger'){
        		location.href = 'http://a.app.qq.com/o/simple.jsp?pkgname=com.wcl.market';
        		// alert('参与砍价请点击右上角，选择“在浏览器中打开”后继续操作！');
        	}else {		// 非微信浏览器
        		if(navigator.userAgent.match(/(iPhone|iPod|iPad);?/i)){
            		window.location.href = "customMallAPP://";	//ios app协议
            		window.setTimeout(function() {
            	    	window.location.href = "https://itunes.apple.com/cn/app/id1105250240?mt=8";	// ios 下载地址
            		}, 3000);
            	}
        		if(navigator.userAgent.match(/android/i)){
            		window.location.href = "";	//android app协议
           			window.setTimeout(function() {
            		    window.location.href = "http://a.app.qq.com/o/simple.jsp?pkgname=com.wcl.market";	//android 下载地址
            		}, 3000);
        		}
        	}

    	});

	});

	</script>
</body>
</html>