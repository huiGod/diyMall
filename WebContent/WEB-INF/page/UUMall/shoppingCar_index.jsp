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

<body class="shoppingcar-index">
    <div class="statusbar-overlay"></div>
    <div class="views">
        <div class="view view-main _js-shoppingCarList" data-addr="<%=path %>/UOrder/editShop.do">
            <div class="navbar">
                <div class="navbar-inner _fm-block _fm-txtcenter">
                    <span class="center">购物车</span>
                    <span class="right right-txt _fm-abs _fm-tz _fm-rz _fm-f-14 do" data-op="editAll">编辑</span>
                    <span class="left left-txt _fm-abs _fm-tz _fm-lz _fm-f-14 _js-historyback"><i class="_fm-back-left">&nbsp;</i>返回</span>
                </div>
            </div>
            <div class="pages navbar-through toolbar-through">
                <div class="page" data-page="index">
                    <div class="page-content pull-to-refresh-content _js-dropdownRefresh _js-limitdrag" data-ptr-distance="55">
                        <!-- 下拉刷新 -->
                        <div class="pull-to-refresh-layer">
                            <div class="preloader"></div>
                            <div class="pull-to-refresh-arrow"></div>
                        </div>
                        <!-- 页面主体 -->
                        <div class="cus-page">
                            <div class="cus-page-inner">
                                <ul class="car-list" data-addr="<%=path%>/UGoods/tohome.do">
                                    <c:forEach items="${shop}" var="shop">
                                            <li class="list-elem" data-secondHalfPrice="${shop.isBoutique}">
                                                <h1 class="_fm-f-14">
                                                    <span>唯优品</span>
                                                    <span class="_fm-right _fm-rel edit do" data-op="editOne">编辑</span>
                                                </h1>
                                                <p class="division _fm-rel"></p>
                                                <div class="goods-content _fm-clearfix">
                                                    <div class="check-box _fm-left  do" data-op="selectOne"><i class="_fm-check-1">&nbsp;</i></div>
                                                    <div class="_fm-img _fm-rel _fm-left">
                                                    	<c:if test="${shop.fileType eq 'xxx'}">
                                                        	<div class="img-inner"><img class="lazy" data-src="${shop.imgUrl}" alt=""></div>
                                                        </c:if>
                                                        <c:if test="${shop.fileType ne 'xxx'}">
                                                        	<div class="img-inner"><img class="lazy" data-src="${shop.imgUrl}${shop.fileType}" alt=""></div>
                                                        </c:if>
                                                    </div>
                                                    <div class="goods-msg  _fm-rel">
                                                        <h2 class="_fm-f-14 _fm-ellipsis">${shop.name}</h2>
                                                        <div class="price-count _fm-abs _fm-bz _fm-lz">
                                                            <span class="count-num _fm-left" data-size="16px">￥${shop.nowPrice}</span>
                                                            <div class="total-num _fm-right _js-formatminus">
                                                                <span class="num-op op-l _fm-left _fm-rel min do" data-op="editNum">-</span>
                                                                <span class="x">x</span><span class="num">${shop.num}</span>
                                                                <span class="num-op op-r _fm-right _fm-rel do" data-op="editNum">+</span>
                                                            </div>
                                                        </div>
                                                        <p class="style _fm-overhide"><span class="_fm-hidden">款式:</span>${shop.textureName}</p>
                                                        <div class="btn _fm-f-17 _fm-abs _fm-tz _fm-rz _fm-hidden _fm-txtcenter do" data-op="deleteOne">删除</div>
                                                    </div>
                                                </div>
                                                <form class="_fm-hidden" action="">
                                                    <input name="shopNo" type="hidden" value="${shop.shopNo}">
                                                    <input name="operation" type="hidden" value="">
                                                    <!-- 增、减、选中、未选中、删除 -->
                                                    <input name="total_number" type="hidden" value="${shop.num}">
                                                </form>
                                            </li>
                                    </c:forEach>
                                </ul>
                            </div>
                        </div>
                        <!-- 页面主体/END -->
                    </div>
                </div>
            </div>
            <div class="toolbar _fm-f-18">
                <div class="toolbar-inner _fm-block _fm-rel _fm-overhide">
                    <span class="check-box _fm-left do" data-op="selectAll"><i class="_fm-check-1">&nbsp;</i>全选</span>
                    <span class="count-all _fm-right _fm-txtcenter _fm-f-18 do" data-op="deleteMulti" data-addr="" data-next="<%=path%>/UOrder/shopBuy.do">结算</span>
                    <div class="_fm-right"><span class="count-txt _fm-f-16">合计:</span><span class="count-num _fm-f-14" data-size="18px">￥<span class="_fm-f-18">0.</span>00</span>
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
