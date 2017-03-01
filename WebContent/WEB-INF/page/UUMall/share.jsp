<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
	<meta charset="UTF-8">
	<meta content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0" name="viewport">
	<meta content="yes" name="apple-mobile-web-app-capable"/>
	<title>分享</title>
	<link rel="stylesheet" href="<%=path %>/css/UUMall/share.css">
	<script src="http://cdn.bootcss.com/zepto/1.0rc1/zepto.min.js"></script>
</head>
<body>
	<header class="width100 shareHeader"><img src="<%=path %>/images/UUMall/topBanner_min.png" class="width100"></header>	
	<footer class="shareGift">
		<section class="width100"><img src="<%=path %>/images/share.png" class="width100"></section>
		<table class="shareWays ">
			<tr class="shareWayline ">
				<td class="padding1"><a href="#" id="wxChat"><img src="<%=path %>/images/UUMall/shareIcon1.png" class="width18_75vw"></a></td>
				<td class="padding1"><a href="#" id="wxCircle"><img src="<%=path %>/images/UUMall/shareIcon2.png" class="width18_75vw"></a></td>
				<td class="padding1"><a href="#" id="qq"><img src="<%=path %>/images/UUMall/shareIcon3.png" class="width18_75vw"></a></td>
			</tr>
			<tr class="shareWayline">
				<td class="padding2"><a href="#" id="qqZone"><img src="<%=path %>/images/UUMall/shareIcon5.png" class="width18_75vw"></a></td>
				<td class="padding2"></td>				
				<td class="padding2"></td>
			</tr>
		</table>
	</footer>
	<div id="mcover"><img src="<%=path %>/images/UUMall/guide.PNG"></div>
	<script src="http://res.wx.qq.com/open/js/jweixin-1.0.0.js"></script>	
	<script>
		// 微信配置
		wx.config({
		      debug: false,
		      appId: "${share.appId}",
		      timestamp: "${share.timestamp}",
		      nonceStr: "${share.noncestr}",
		      signature: "${share.sign}",
		      jsApiList: [
		        'checkJsApi',
		        'onMenuShareTimeline',
		        'onMenuShareAppMessage',
		        'onMenuShareQQ',
		        'onMenuShareWeibo',
		        'onMenuShareQZone'
		    ]
		});

		// 微信接口
		wx.ready(function () {	  
		  // 分享内容
		    var shareData = {
			    title: '优品汇',
			    desc: '明星，卡通创意定制精品店，高清模板，零经验秒变设计师',
			    link: 'http://test.diy.51app.cn/diyMall2/UGoods/initHome.do',
			    imgUrl: 'http://file.diy.51app.cn/diymall_logo@3x.png',
			    success:function(){$("#mcover").hide();},
			    cancel: function (res) {alert('已取消');},
			    fail: function (res) {alert(JSON.stringify(res));}
		    };
		    // 1 监听“分享给朋友”，按钮点击、自定义分享内容及分享结果接口
		  document.querySelector('#wxChat').onclick = function () {
		    wx.onMenuShareAppMessage(shareData);
		  };

		  // 2.2 监听“分享到朋友圈”按钮点击、自定义分享内容及分享结果接口
		  document.querySelector('#wxCircle').onclick = function () {
		   wx.onMenuShareTimeline(shareData);
		  };

		  // 2.3 监听“分享到QQ”按钮点击、自定义分享内容及分享结果接口
		  document.querySelector('#qq').onclick = function () {
		    wx.onMenuShareQQ(shareData);
		  };
		  
		  // 2.4 监听“分享到微博”按钮点击、自定义分享内容及分享结果接口
		  document.querySelector('#wb').onclick = function () {
		    wx.onMenuShareWeibo(shareData);
		  };

		  // 2.5 监听“分享到QZone”按钮点击、自定义分享内容及分享接口
		  document.querySelector('#qqZone').onclick = function () {
		    wx.onMenuShareQZone(shareData);
		  };	  
		});

		wx.error(function(res){
			alert(res.errMsg);
		});

		$(".shareWays").click(function(){
			$("#mcover").show();
		});
		$("#mcover").click(function(){
			$(this).hide();
		});
	</script>	
</body>
</html>
