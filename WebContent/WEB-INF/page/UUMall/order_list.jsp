<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
	String path = request.getContextPath();
%>
<!DOCTYPE html>
<html lang="zh-CN">

<head>
    <meta charset="utf-8">
    <!-- 兼容性调整 -->
    <meta http-equiv="X-UA-Compatible" content="IE=edge, chrome=1.0">
    <meta name="renderer" content="webkit">
    <!-- 视口调整 -->
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, minimum-scale=1, user-scalable=no, minimal-ui">
    <meta name="apple-mobile-web-app-capable" content="yes">
    <meta name="apple-mobile-web-app-status-bar-style" content="black">
    <meta name="format-detection" content="telephone=no, email=no">
    <link rel="shortcut icon" href="img/favicon.ico">
    <title>唯优品</title>
    <!-- 样式表文件导入 -->
    <link rel="stylesheet" href="<%=path %>/css/UUMall/framework7.ios.min.css">
    <link rel="stylesheet" href="<%=path %>/css/UUMall/custom.css">
</head>

<body class="order-list">
    <div class="statusbar-overlay"></div>
    <div class="views">
        <div class="view view-main _js-orderList">
            <div class="navbar">
                <div class="navbar-inner _fm-block _fm-txtcenter">
                    <span class="center">我的订单</span>
                    <span class="left left-txt _fm-abs _fm-tz _fm-lz _fm-f-14 _js-historyback"><i class="_fm-back-left">&nbsp;</i>返回</span>
                </div>
            </div>
            <div class="pages navbar-through">
                <div class="page" data-page="index">
                    <ul class="sub-navi _fm-overhide _fm-abs _fm-lz _fm-txtcenter">
                        <li class="_fm-left do active" data-type="0" data-op="filter"><span>全部</span></li>
                        <li class="_fm-left do" data-type="1" data-op="filter"><span>待付款</span></li>
                        <li class="_fm-left do" data-type="2" data-op="filter"><span>待发货</span></li>
                        <li class="_fm-left do" data-type="3" data-op="filter"><span>待收货</span></li>
                        <li class="_fm-left do" data-type="4" data-op="filter"><span>待评价</span></li>
                    </ul>
                    <div class="page-content pull-to-refresh-content infinite-scroll _js-limitdrag" data-ptr-distance="55" data-addr="<%=path%>/UOrder/getOrderList.do">
                        <!-- 下拉刷新 -->
                        <div class="pull-to-refresh-layer">
                            <div class="preloader"></div>
                            <div class="pull-to-refresh-arrow"></div>
                        </div>
                        <!-- 页面主体 -->
                        <div class="cus-page">
                            <div class="cus-page-inner">
                                <div class="mian-content" data-addr="<%=path%>/UGoods/tohome.do" data-pay="<%=path%>/UOrder/orderPay.do" data-car="<%=path%>/UOrder/updateOrder.do" data-confirm="<%=path%>/UOrder/updateOrder.do" data-delete="<%=path%>/UOrder/updateOrder.do" data-cancel="<%=path%>/UOrder/updateOrder.do" data-flow="http://m.kuaidi100.com/index_all.html" data-evaluate="<%=path%>/UGoods/releaseComment.do" data-detail="<%=path%>/UOrder/orderDetail.do">
                                    <!--
                                        data-pay: 立即付款 地址 
                                        data-car: 加入购物车 地址 
                                        data-confirm: 确认收货 地址 
                                        data-delete: 删除订单地址 地址 
                                        data-cancel: 取消订单 地址 
                                        data-flow: 查看物流 地址 
                                        data-evaluate: 立即评价 地址 
                                        data-detail: 订单详情 地址 
                                    -->
                                </div>
                            </div>
                        </div>
                        <!-- 页面主体/END -->
                    </div>
                </div>
            </div>
        </div>
    </div>
    <!-- 脚本文件导入
==================================================-->
    <script src="<%=path %>/js/UUMall/custom/framework7.min.js"></script>
    <script src="<%=path %>/js/UUMall/custom.js"></script>
</body>

</html>
