<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
	<meta charset="UTF-8">
	<meta content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0" name="viewport">
	<meta name="">
	<title>DIY分享</title>
	<style>
		*{padding:0;margin:0;}
		.diy-body{background-color:#ff294f;}
		.box{width:100%;position:relative;}
		.box img{display:block;width:100%;}
		/*.ToAppStoreBtn{position:absolute;top:78.3vh;left:23vw;width:55vw;height:10vh;z-index:1000;}*/
		.useBrowserOpen{position:fixed;width:100%;height:100%;top:0;left:0;display: none;z-index:9999;}
		.useBrowserOpen img{width:100%;}
		a{-webkit-tap-highlight-color:rgba(0,0,0,0);}
	</style>
</head>
<body class="diy-body">
	<!-- diy-share -->
	<div class="box">
	 	<img src="<%=path %>/images/DIYshare_01.jpg" alt="">
	 	<img src="<%=path %>/images/DIYshare_02.jpg" alt="">
	 	<img src="<%=path %>/images/DIYshare_03.jpg" alt="">
	 	<img src="<%=path %>/images/DIYshare_04.jpg" alt="">
	 		<a href="javascript:void(0);" onclick="window.location.href='http://a.app.qq.com/o/ioslink.jsp?id=1105250240'" class="ToAppStoreBtn">
	 		<img src="<%=path %>/images/DIYshare_05.jpg" alt="">
	 		</a>
	 	<img src="<%=path %>/images/DIYshare_06.jpg" alt="">
	</div>
	<!-- ios提示safari打开 -->
	<nav class="useBrowserOpen">
		<img src="<%=path %>/images/no_safari.png" >
	</nav>
	<script src="<%=path %>/js/lib/zepto.min.js"></script>
	<script src="<%=path %>/js/lib/touch.js"></script>
	<script>
		// $(document).ready(function() {
		// 	$(".ToAppStoreBtn").on("tap",function(){
		// 		var handle=function(){
		// 			var temp =navigator.userAgent;
		// 			var rel= temp.indexOf("Safari")>0;
		// 			return rel;
		// 		};
		// 		if(handle()){//如果是safari

		// 		}else{
		// 			$(".useBrowserOpen").show();
		// 		}				
		// 	});
		// 	$(".useBrowserOpen").on("tap",function(){
		// 		$(this).hide();
		// 	});
		// });
	</script>
</body>
</html>