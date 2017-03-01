<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    
    <meta name="apple-mobile-web-app-capable" content="yes">
    <meta name="apple-mobile-web-app-status-bar-style" content="black">
    <link rel="stylesheet" href="${ctx}/css/css.css">
    <title>51app</title>
</head>
<body>
	<!-- 评价 
	<div class="header">
		<a href="javascript:void(0)" class="return-btn"><img src="${ctx}/images/return.png" alt=""></a>
		<span class="">评价</span>
	</div>
	-->
	<!-- 收货信息 -->
	<div class="wrapper" style="padding-top: 10px">
		<div class="comment-content">
			<!-- 评价分类 -->
			<div class=" seg-line pos-rel comments-class fc2 fs12 ">
				<ul class="clearfix">
					<li><span class="commentClass-btn commentClass-btn2">全部(<i>123</i>)</span></li>
					<li><span class="commentClass-btn ">很划算(<i>123</i>)</span></li>
					<li><span class="commentClass-btn ">快递很快(<i>123</i>)</span></li>
					<li><span class="commentClass-btn ">软硬度好(<i>123</i>)</span></li>
					<li><span class="commentClass-btn ">很划算(<i>123</i>)</span></li>
					<li><span class="commentClass-btn ">软硬度一般(<i>123</i>)</span></li>
				</ul>
			</div>
			<!-- 评价详细信息 -->
			<div class="comments-detail">
				<ul>
					<li>
						<h2 class="commenter fs14 fc00 line-h24 pos-rel"><span class="comt-img"><img src="${ctx}/imgTmp/commenter-img.jpg" alt=""></span> <i>名字</i></h2>
						<div class="comment-p fs12 fc00 line-h18">
							<p>不错不错，这样以后就不怕摔了</p>
							<p class="fc2 clearfix">
								<span  class="ov-hidden comment-Cai fl"><i>材质分类：</i><span>半包3D喷绘壳半包3D喷绘壳</span></span>
							    <i class="fr">2015.10.14 11:16:11</i>
							</p>
						</div>	
					</li>
					<li>
						<h2 class="commenter fs14 fc00 line-h24 pos-rel"><span class="comt-img"><img src="${ctx}/imgTmp/commenter-img.jpg" alt=""></span> <i>名字</i></h2>
						<div class="comment-p fs12 fc00 line-h18">
							<p>不错不错，这样以后就不怕摔了</p>
							<p class="fc2">
								<span><i>材质分类：</i><span>半包3D喷绘壳</span></span>
							    <i>2015.10.14 11:16:11</i>
							</p>
						</div>	
					</li>
				</ul>
			</div>
		</div>	
	</div>
	<script src="${ctx}/js/lib/jquery-2.2.3.min.js"></script>
  	<script>
  		$(document).ready(function() {
  			var $com_btn=$(".commentClass-btn");
  			$com_btn.on("click",function(){
  				$com_btn.removeClass('commentClass-btn2');
  				$(this).addClass('commentClass-btn2');
  			})
  		});
  	</script>
</body>
</html>