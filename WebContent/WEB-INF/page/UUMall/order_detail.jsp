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

<body class="order-detail">
    <div class="statusbar-overlay"></div>
    <div class="views">
        <div class="view view-main _js-orderDetail">
            <div class="navbar">
                <div class="navbar-inner _fm-block _fm-txtcenter">
                    <span class="center">订单详情</span>
                    <span class="left left-txt _fm-abs _fm-tz _fm-lz _fm-f-14 _js-historyback"><i class="_fm-back-left">&nbsp;</i>返回</span>
                </div>
            </div>
            <div class="pages navbar-through">
                <div class="page" data-page="index">
                    <div class="page-content _js-limitdrag">
                        <!-- 页面主体 -->
                        <div class="cus-page">
                            <div class="cus-page-inner">
                                <div class="main-content order-elem">
                                    <h1 class="order-num _fm-f-14">订单号：<span class="num">${order.orderNo}</span></h1>
                                    <p class="division _fm-rel"></p>
                                    <div class="order-address _fm-block _fm-overhide">
                                        <div class="left-ico _fm-left">
                                            <i class="_fm-rel _fm-inline _fm-txtcenter">&nbsp;</i>
                                        </div>
                                        <div class="msg">
                                            <p class="_fm-overhide _fm-f-15">
                                                <span class="name _fm-left _fm-ellipsis">收货人: ${order.consignee}</span>
                                                <span class="_fm-right">${order.mobile}</span>
                                            </p>
                                            <div class="address _fm-overhide">收货地址：${order.province}${order.area}</div>
                                        </div>
                                    </div>
                                    <p class="division _fm-rel"></p>
                                    <p class="title _fm-overhide _fm-f-14">
                                        <span class="_fm-left">DIY商城</span>
                                        <span class="_fm-right">${order.expressMsg}</span>
                                    </p>
                                    <p class="division _fm-rel"></p>
                                    <section class="goods-detail">
                                      <c:forEach items="${order.goodinfos}" var="goods">
                                        <div class="goods-content _fm-clearfix">
                                            <div class="_fm-img _fm-rel _fm-left">
                                                <div class="img-inner"><img class="lazy" data-src="${goods.imgUrl}" alt=""></div>
                                            </div>
                                            <div class="goods-msg _fm-rel">
                                                <h2 class="_fm-f-14 _fm-ellipsis">${goods.name}</h2>
                                                <div class="price-count _fm-abs _fm-bz _fm-lz">
                                                    <span class="count-num _fm-left _js-formatGoodsPrice" data-size="16px">￥${goods.nowPrice}</span>
                                                    <div class="total-num _fm-right">x<span class="num">${goods.num}</span></div>
                                                </div>
                                                <p class="style _fm-overhide">${goods.textureName}</p>
                                            </div>
                                        </div>
                                      </c:forEach>
                                    </section>
                                    <p class="division _fm-rel"></p>
                                    <p class="item _fm-overhide _fm-f-14">
                                        <span class="_fm-left">支付方式</span>
                                      <c:if test="${order.payType==1}">
                                        <span class="_fm-right">支付宝</span>
                                      </c:if>
                                      <c:if test="${order.payType==2}">
                                        <span class="_fm-right">微信</span>
                                      </c:if>
                                    </p>
                                    <p class="division _fm-rel"></p>
                                    <p class="item _fm-overhide _fm-f-14">
                                        <span class="_fm-left">运费</span>
                                        <span class="_fm-right _js-formatGoodsPrice" data-size="14px">${order.transportfee}</span>
                                    </p>
                                    <p class="division _fm-rel"></p>
                                    <div class="other-msg _fm-overhide">
                                        <p class="_fm-f-14 _fm-right">实付款：<span class="_fm-f-12 _js-formatGoodsPrice" data-size="16px">￥${order.feeTotal}</span></p>
                                        <p class="_fm-clearfix"></p>
                                        <p class="date _fm-f-12 _fm-right">下单时间：${order.creatTime}</p>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <!-- 页面主体/END -->
                    </div>
                </div>
            </div>
            <div class="toolbar _fm-f-14">
                <div class="toolbar-inner _fm-block _fm-rel _fm-overhide">
                  <c:if test="${order.status==1}">
                    <span class="do or" data-addr="<%=path%>/UOrder/orderPay.do">立即支付</span>
                  </c:if>
                  <c:if test="${order.status!=1}">
                    <span class="dis" data-addr="<%=path%>/UOrder/orderPay.do">立即支付</span>
                  </c:if>
                    <span class="do" data-addr="<%=path%>/UOrder/cancel4Detail.do">取消订单</span>
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
