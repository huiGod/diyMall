<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
%>
<!DOCTYPE html>
<html lang="zh-CN">
  <head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, minimum-scale=1, user-scalable=no">
    <meta name="apple-mobile-web-app-capable" content="yes">
    <meta name="apple-mobile-web-app-status-bar-style" content="black">
    <title>唯优品</title>
    <link rel="shortcut icon" href="<%=path %>/images/favicon.ico">
    <link rel="stylesheet" href="<%=path %>/css/UUMall/lottery.css">
  </head>
<body>
	<header>
		<a href="http://test.diy.51app.cn/diyMall2/UGoods/tohome.do" class="back"></a>
		<img class="topBanner" src="<%=path %>/images/topBanner.png" >
	</header>
	<!-- 抽奖区 -->
	<div class="location">
		<!-- 抽奖区 -->
		<div id="lottery" class="drawLottArea flash">
			<table border="0" cellpadding="0" cellspacing="0">
				<tr class="lottery-group">
					<td class="lottery-unit td_1" ><img src="<%=path %>/images/5.png" /></td>
					<td class="lottery-unit td_2"><img src="<%=path %>/images/rose.png" /></td>
					<td class="lottery-unit td_3"><img src="<%=path %>/images/10.png" /></td>
				</tr>
				<tr class="lottery-group">
					<td class="lottery-unit td_4"><img src="<%=path %>/images/diy.png" /></td>
					<td class="td_5" data-drawResult="getLottery.do"><a href="#"><img src="<%=path %>/images/draw.png" /></a></td>
					<td class="lottery-unit td_6"><img src="<%=path %>/images/5-2.png" /></td>
				</tr>
		        <tr class="lottery-group">
					<td class="lottery-unit td_7"><img src="<%=path %>/images/5-3.png" /></td>
					<td class="lottery-unit td_8"><img src="<%=path %>/images/design.png" /></td>
					<td class="lottery-unit td_9"><img src="<%=path %>/images/30.png" /></td>
				</tr>
			</table>
		</div>
		<!-- 信息区 -->
		<div class="infoArea" >
			<!-- 1.公布信息 -->
			<div class="publicInfo " data-infoList = "winnerList.do">
				<h1 class="title"><span class="line"></span>大奖公布<span class="line"></span></h1>
				<marquee direction="up" behavior="scroll" scrollamount="2" loop="-1" width="100%"  class="marquee">
					<ul class="list">
						<!-- ajax数据 -->
					</ul>
				</marquee>
			</div>
			<!-- 2.中奖信息 -->
			<div class="winning hidden">
				<input id="couponId" type="hidden" value=""/>
				<h1 class="title"><span class="line"></span>恭喜您<span class="line"></span></h1>
				<p class="p1">恭喜您获得<span class="prize-level"></span><span class="awardObj"></span></p>
				<p class="p2"></p>
				<a href="javascript:toPhone();" class="clickGet">点击领取</a>
				<p class="p3">提示：您还剩<span class="leaseChance"></span>次机会哦~</p>
			</div>
		</div>
	</div>
	<!-- 定制商品 -->
	<footer>
		<h2><a href="#" name="shop"><img src="<%=path %>/images/bookBanner.png" alt=""></a></h2>
		<div class="content-block teamBooking-content" id="teamGoods">
            <div class="teamBooking-single clearfix"><div class="teamBooking-picBox"><a href="http://test.diy.51app.cn/diyMall2/UGoods/goodsInfo.do?id=1" class="external link"><img src="http://file.diy.51app.cn/uu/special/89d426bd.jpg"></a></div><div class="teamBooking-info"><div class="teamBooking-info-inner"><h3>创意定制DIY马克杯</h3><p class="price">¥<span class="special-num"><span class="special-font-large">39</span>.<span class="special-font-small">00</span></span><i class="had-sell">已售<i class="sell-num">1253</i>件</i></p><div class="specialAreaBtn"><a href="javascript:void(0);" onclick="window.location.href='http://a.app.qq.com/o/simple.jsp?pkgname=wcl.com.yqshop'"><span class="downloadBtn android-no">客户端定制</span></a><a href="http://test.diy.51app.cn/diyMall2/UGoods/goodsInfo.do?id=1" class="external link"><span class="toGoodsInfo">立即购买</span></a></div></div></div></div><div class="teamBooking-single clearfix"><div class="teamBooking-picBox"><a href="http://test.diy.51app.cn/diyMall2/UGoods/goodsInfo.do?id=2" class="external link"><img src="http://file.diy.51app.cn/uu/special/d61bfb8d.jpg"></a></div><div class="teamBooking-info"><div class="teamBooking-info-inner"><h3>DIY定制舒适抱枕</h3><p class="price">¥<span class="special-num"><span class="special-font-large">59</span>.<span class="special-font-small">00</span></span><i class="had-sell">已售<i class="sell-num">1785</i>件</i></p><div class="specialAreaBtn"><a href="javascript:void(0);" onclick="window.location.href='http://a.app.qq.com/o/simple.jsp?pkgname=wcl.com.yqshop'"><span class="downloadBtn android-no">客户端定制</span></a><a href="http://test.diy.51app.cn/diyMall2/UGoods/goodsInfo.do?id=2" class="external link"><span class="toGoodsInfo">立即购买</span></a></div></div></div></div><div class="teamBooking-single clearfix"><div class="teamBooking-picBox"><a href="http://test.diy.51app.cn/diyMall2/UGoods/goodsInfo.do?id=3" class="external link"><img src="http://file.diy.51app.cn/uu/special/3f3ee673.jpg"></a></div><div class="teamBooking-info"><div class="teamBooking-info-inner"><h3>DIY定制手机壳</h3><p class="price">¥<span class="special-num"><span class="special-font-large">56</span>.<span class="special-font-small">50</span></span><i class="had-sell">已售<i class="sell-num">1638</i>件</i></p><div class="specialAreaBtn"><a href="javascript:void(0);" onclick="window.location.href='http://a.app.qq.com/o/simple.jsp?pkgname=wcl.com.yqshop'"><span class="downloadBtn android-no">客户端定制</span></a><a href="http://test.diy.51app.cn/diyMall2/UGoods/goodsInfo.do?id=3" class="external link"><span class="toGoodsInfo">立即购买</span></a></div></div></div></div><div class="teamBooking-single clearfix"><div class="teamBooking-picBox"><a href="http://test.diy.51app.cn/diyMall2/UGoods/goodsInfo.do?id=7" class="external link"><img src="http://file.diy.51app.cn/uu/special/3f621195.jpg"></a></div><div class="teamBooking-info"><div class="teamBooking-info-inner"><h3>定制单面图案纯棉T恤</h3><p class="price">¥<span class="special-num"><span class="special-font-large">59</span>.<span class="special-font-small">00</span></span><i class="had-sell">已售<i class="sell-num">2530</i>件</i></p><div class="specialAreaBtn"><a href="javascript:void(0);" onclick="window.location.href='http://a.app.qq.com/o/simple.jsp?pkgname=wcl.com.yqshop'"><span class="downloadBtn android-no">客户端定制</span></a><a href="http://test.diy.51app.cn/diyMall2/UGoods/goodsInfo.do?id=7" class="external link"><span class="toGoodsInfo">立即购买</span></a></div></div></div></div><div class="teamBooking-single clearfix"><div class="teamBooking-picBox"><a href="http://test.diy.51app.cn/diyMall2/UGoods/goodsInfo.do?id=20" class="external link"><img src="http://file.diy.51app.cn/uu/special/b3add6a9.jpg"></a></div><div class="teamBooking-info"><div class="teamBooking-info-inner"><h3>个性定制鼠标垫</h3><p class="price">¥<span class="special-num"><span class="special-font-large">29</span>.<span class="special-font-small">00</span></span><i class="had-sell">已售<i class="sell-num">1206</i>件</i></p><div class="specialAreaBtn"><a href="javascript:void(0);" onclick="window.location.href='http://a.app.qq.com/o/simple.jsp?pkgname=wcl.com.yqshop'"><span class="downloadBtn android-no">客户端定制</span></a><a href="http://test.diy.51app.cn/diyMall2/UGoods/goodsInfo.do?id=20" class="external link"><span class="toGoodsInfo">立即购买</span></a></div></div></div></div></div>
	</footer>
	<script type="text/javascript" src="<%=path %>/js/UUMall/jquery-1.8.3.min.js"></script>
	<script type="text/javascript" src="<%=path %>/js/UUMall/lottery.js"></script>
	<script type="text/javascript">
		function toPhone(){
			var couponId = $("#couponId").val();
			window.location.href="<%=path %>/UCoupon/toPhone.do?couponId="+couponId;
		}
	</script>
</body>
</html>