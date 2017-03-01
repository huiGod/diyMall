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
	<link rel="stylesheet" href="<%=path %>/css/diy2_5/swiper-3.3.1.min.css">
	<link rel="stylesheet" href="<%=path %>/css/diy2_5/wsp-order-share.css">
	<title>定制作品分享</title>
</head>
<body>
	<main class="share">
		<div class="view-container">
			<section class="swiper-container">
				<div class="swiper-wrapper" id="bannerSwiper">
				</div>
				<div class="swiper-pagination "></div>
			</section>
			<section class="describe-container">
				<h1><img src="" alt="">&nbsp<span></span></h1>
				<h2></h2>
				<p></p>
			</section>
		</div>
		<div class="toolbar">
			<div class="download-bar"><a class="" href="http://a.app.qq.com/o/simple.jsp?pkgname=com.wcl.market">下载<span>唯乐购</span>，一起来设计</a></div>	
		</div>
	</main>
	
	<script type='text/javascript' src='//g.alicdn.com/sj/lib/zepto/zepto.min.js' charset='utf-8'></script>
    <script type='text/javascript' src='<%=path %>/js/diy2_5/swiper.min.js' charset='utf-8'></script>
	<script type="text/javascript" src="<%=path %>/js/diy2_5/fastclick.js"></script>
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

	$(function(){

		// 处理300ms点击延迟
		FastClick.attach(document.body);

		var mySwiper = new Swiper('.swiper-container', {
			direction: 'horizontal',
			pagination: '.swiper-pagination',
			paginationType : 'fraction',
			observer:true,        //修改swiper自己或子元素时，自动初始化swiper  
    		observeParents:true  //修改swiper的父元素时，自动初始化swiper
		});

		(function loadData(url){
			var successFunc = function(json){
				if(json.code == 200 && json.data){
					$('.describe-container h1 span').text(json.data.nickName);	
					$('.describe-container h1 img').attr('src', (json.data.head_url && json.data.head_url != 'http://file.diy.51app.cn/') ? json.data.head_url : 'http://file.diy.51app.cn/userHead.png');
					$('.describe-container h2').text(json.data.name);
					$('.describe-container p').text(json.data.cont);
					var swiperContainer = $('.swiper-wrapper'),
						swiperList = '';
					json.data.imgurl.forEach(function(data, index, array){
						if(/\_[\w]+\@cut/.test(data)){		// 链接里有_**@cut的应去掉  照片书台历
							swiperList += '<div class="swiper-slide">'+
										  '<img src="' + data.replace(/@cut/, '') + '">'+
										  '</div>';
						}else if(/\_[\w]+\@b/.test(data)){		// 链接里有下划线 _**@b 的  打火机
							swiperList += '<div class="swiper-slide">'+
										  '<img src="' + data.replace(/@b/, '') + '">'+
										  '</div>';
						}else if(/\@/.test(data)){		// 普通定制商品
							swiperList += '<div class="swiper-slide">'+
										  '<img src="' + data + '">'+
										  '</div>';
						}	// 不包含@的链接是用户图片，不显示
					});
					swiperContainer.append(swiperList);
				}else{
					$('.share').hide();
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
					console.log('error：' + status);
				}
			});
		}('http://120.26.112.213:8082/diyMall/user/worksInfo/' + getQueryString('id') + '.do'));

		$('.download-bar a').click (function(e){	// 微信浏览器
        	if(navigator.userAgent.toLowerCase().match(/MicroMessenger/i) == 'micromessenger'){
        		location.href = 'http://a.app.qq.com/o/simple.jsp?pkgname=com.wcl.market';
        		// $(this).attr('href', 'https://itunes.apple.com/cn/app/id1105250240?mt=8');
        		// alert('下载唯乐购请点击右上角，选择“在浏览器中打开”后继续操作！');
        	}else {	// 非微信浏览器
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