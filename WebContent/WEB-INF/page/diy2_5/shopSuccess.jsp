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
	 <title>唯乐购</title>
	 <link rel="stylesheet" href="<%=path %>/css/shopSuccess.css">
</head>
<body>
	<header class="width100 shareHeader"><img src="<%=path %>/images/topBanner_min.png" class="width100"></header>	
	<footer class="shareGift">
		<section class="width100"><img src="<%=path %>/images/share.png" class="width100"></section>
		<table class="shareWays ">
			<tr class="shareWayline ">
				<td class="padding1"><a href="javascript:sending(1);"><img src="<%=path %>/images/shareIcon1.png" class="width18_75vw"></a></td>
				<td class="padding1"><a href="javascript:sending(2);"><img src="<%=path %>/images/shareIcon2.png" class="width18_75vw"></a></td>
				<td class="padding1"><a href="javascript:sending(3);"><img src="<%=path %>/images/shareIcon3.png" class="width18_75vw"></a></td>
			</tr>
			<tr class="shareWayline">
				<td class="padding2"><a href="javascript:sending(4);"><img src="<%=path %>/images/shareIcon4.png" class="width18_75vw"></a></td>
				<td class="padding2"><a href="javascript:sending(5);"><img src="<%=path %>/images/shareIcon5.png" class="width18_75vw"></a></td>
				<td class="padding2"><a href="javascript:sending(6);"><img src="<%=path %>/images/shareIcon6.png" class="width18_75vw"></a></td>
			</tr>
		</table>
	</footer>
	<script type="text/javascript">
		function connectNZOCJSBridge(callback) {
			if (window.NZOCJSBridge) {
				callback(NZOCJSBridge)
			} else {
				document.addEventListener('NZOCJSBridgeReady', function() {
					callback(NZOCJSBridge)
				}, false)
			}
		}
		function sending(tag){
			var title = "唯乐购";
			var msg = "明星，卡通创意定制精品店，高清模板，零经验秒变设计师";
			var imgUrl = "http://file.diy.51app.cn/diymall_logo@3x.png";
			var url = "http://api.diy.51app.cn/diyMall/focus/toshare.do";
			data = {"tag":tag,
					"title":title,
					"msg":msg,
					"imgUrl":imgUrl,
					"url":url 
					};
			connectNZOCJSBridge(function(bridge) {
				bridge.send(data, function(responseData) {});
			});
		}
	</script>
</body>
</html>
