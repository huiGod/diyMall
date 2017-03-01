<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
	String path=request.getContextPath();
%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, minimum-scale=1, user-scalable=no"> 
    <!-- <meta name="viewport" content="width=640,user-scalable=no">-->
    <meta name="apple-mobile-web-app-capable" content="yes">
    <meta name="apple-mobile-web-app-status-bar-style" content="black">
    <link rel="shortcut icon" href="images/favicon.ico">
    <link rel="stylesheet" href="<%=path %>/css/diy20/custom.css">
    <title>一起来定制</title>
</head>
<body class="blue-bg">
	<main class="customize" >
		<header class="customize-banner w100">
			<img src="<%=path %>/images/diy20/customize-banner.jpg" alt="" class="w100">
		</header>
		<section class="customize-join">
			<header>
				<span class="disblk bg-blue fcwhite fs31 radius5 lineh50 joinway center pos-rel ptb5">参赛方式</span>
			</header>
			<article class="blk-center bg-bitwhite radius10 center box-size join-way mt20">
				<img src="<%=path %>/images/diy20/steps.png" alt="" class="mt30 ml20">
				<a dat="我要设计" class="design2-btn opacity btn-h bg-cover btn-w210 bg-cover mt30 btn-senddat">我要设计</a>
				<a href="rank.html" class="rank2-btn opacity btn-h bg-cover btn-w210 bg-cover mt30">排名情况</a>
			</article>
		</section>
		<section class="customize-rewards">
			<header>
				<span class="mt20 disblk bg-blue fcwhite fs31 radius5 lineh50 joinway center pos-rel ptb5">活动奖品</span>
			</header>
			<article class="w612 blk-center box-size pl10 fs21 lineh30"> 
				<p>1.一等奖：背夹式移动电源+你所设计的商品实物一份（1-10名）</p>
				<p>2.二等奖：你所设计的商品实物一份（前11-50名）</p>
				<p>1.一等奖：得满30票，立即获得全场任意消费10元现金券，同时享受折扣。</p>
			</article>
		</section>
		<footer class="customize-tips fs18 fcred lineh28 w612 blk-center box-size pl10">
			<h5 class="fs18">温馨提示</h5>
			<p>1.获奖时间：没定</p>
			<p>2.每个用户只能参加一次，不可重复参与！</p>
			<p>3.凡在活动期间购买了自己DIY商品，并同时获得二等奖以上的，可选择以59新金泉代替DIY。</p>
		</footer>
	</main>
	<script src="<%=path %>/js/diy20/zepto.min.js"></script>
	<script src="<%=path %>/js/diy20/touch.js"></script>
	<script src="<%=path %>/js/diy20/myCustom.js"></script>
</body>
</html>