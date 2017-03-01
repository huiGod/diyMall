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
    <meta name="apple-mobile-web-app-capable" content="yes">
    <meta name="apple-mobile-web-app-status-bar-style" content="black">
    <meta name="format-detection" content="telephone=no">
    <link rel="shortcut icon" href="images/favicon.ico">
    <link rel="stylesheet" href="<%=path %>/css/diy20/custom.css">
    <title>唯优品 - 属性</title>
</head>
<body class="_js-goodsPop">
	<main class="page-content goods-pop-main" data-url="${url }">
        <header class="pop-head">
            <div class="pop-head-content">
                
                <div class="closeSelect" onclick="sending(11111)"></div>
            </div>      
        </header> 
        <section id="pop-GoodsContent">
            <!-- 活动提示 -->
            <div class="remindActive">
                优惠活动：
            </div>
            <!-- 属性选择 -->
            <div class="pop-selects fc00">
                <ul>
                    
                </ul>
            </div>      
            <!-- 数量选择 -->
            <div class="clearfix pop-selNum">
                <span class="fl fc00" id="goumaiNum">购买数量</span> 
                <div class="rdc-add clearfix fr rdc-add2">
                    <span class="iconfont com-r-a icon-jianhao1 aaa2 reduce"></span>
                    <i class="numbers"><input type="tel" maxlength="3" value="1" class="input-num"></i>  
                    <span class="iconfont com-r-a icon-jiahao add"></span>
                </div>
                <button style="display:none;color:#fff;" id="closeKeyboardBtn">close</button>
            </div>
        </section>
        <section class="data-deposit">
            
        </section>
    </main>
     <script src="<%=path %>/js/diy20/zepto.min.js"></script>
    <script src="<%=path %>/js/diy20/touch.js"></script>
    <script src="<%=path %>/js/diy20/myCustom.js"></script>
</body>
</html>