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

<body class="shoppingcar-confirm">
    <div class="statusbar-overlay"></div>
    <div class="views">
        <div class="view view-main _js-orderConfirm">
            <div class="navbar">
                <div class="navbar-inner _fm-block _fm-txtcenter" data-page="index">
                    <span class="center">确认订单</span>
                    <span class="left left-txt _fm-abs _fm-tz _fm-lz _fm-f-14 _js-historyback"><i class="_fm-back-left">&nbsp;</i>返回</span>
                </div>
                <div class="navbar-inner cached _fm-block _fm-txtcenter _fm-link-1" data-page="address">
                    <span class="center">收货地址</span>
                    <a href="#" class="left back left-txt _fm-abs _fm-tz _fm-lz _fm-f-14"><i class="_fm-back-left">&nbsp;</i>返回</a>
                </div>
                <div class="navbar-inner cached _fm-block _fm-txtcenter _fm-link-1" data-page="goodsList">
                    <span class="center">商品详情</span>
                    <a href="#" class="left back left-txt _fm-abs _fm-tz _fm-lz _fm-f-14"><i class="_fm-back-left">&nbsp;</i>返回</a>
                    <span class="right right-txt _fm-abs _fm-tz _fm-rz _fm-f-14">共${shopSize}件</span>
                </div>
                <div class="navbar-inner cached _fm-block _fm-txtcenter _fm-link-1" data-page="editAddress">
                    <span class="center">新增收货地址</span>
                    <a href="#" class="left back left-txt _fm-abs _fm-tz _fm-lz _fm-f-14"><i class="_fm-back-left">&nbsp;</i>返回</a>
                    <span class="right right-txt do _fm-abs _fm-tz _fm-rz _fm-f-14" data-op="delete" data-addr="<%=path%>/UOrder/deleteAdress.do">删除</span>
                </div>
                <div class="navbar-inner cached _fm-block _fm-txtcenter _fm-link-1" data-page="coupon">
                    <span class="center">现金优惠券</span>
                    <a href="#" class="left back left-txt _fm-abs _fm-tz _fm-lz _fm-f-14"><i class="_fm-back-left">&nbsp;</i>返回</a>
                </div>
            </div>
            <div class="pages navbar-through toolbar-through">
                <div class="page " data-page="index">
                    <div class="page-content _js-limitdrag">
                        <!-- 页面主体 -->
                        <div class="cus-page">
                            <div class="cus-page-inner">
                                <!-- 订单确认页 -->
                                <div class="main-content">
                                    <div class="order-msg">
                                       <c:if test="${adress == null}">
                                    	<a class="order-address _fm-block _fm-overhide" href="#address" data-id="">
                                            <div class="left-ico _fm-left">
                                                <i class="_fm-rel _fm-inline _fm-txtcenter">&nbsp;</i>
                                            </div>
                                            <div class="msg">
                                                <p class="_fm-overhide _fm-f-15">
                                                    <span class="name _fm-left _fm-ellipsis"></span>
                                                    <span class="_fm-right"></span>
                                                </p>
                                                <div class="right-ico _fm-right">
                                                    <span class="_fm-back-right">&nbsp;</span>
                                                </div>
                                                <div class="address _fm-overhide"></div>
                                            </div>
                                          </a>
                                         </c:if>
                                    	<c:forEach items="${adress}" var="d" begin="0" end="0">
                                          <a class="order-address _fm-block _fm-overhide" href="#address" data-id="${d.id }">
                                            <div class="left-ico _fm-left">
                                                <i class="_fm-rel _fm-inline _fm-txtcenter">&nbsp;</i>
                                            </div>
                                            <div class="msg">
                                                <p class="_fm-overhide _fm-f-15">
                                                    <span class="name _fm-left _fm-ellipsis">收货人: ${d.name}</span>
                                                    <span class="_fm-right">${d.mobile}</span>
                                                </p>
                                                <div class="right-ico _fm-right">
                                                    <span class="_fm-back-right">&nbsp;</span>
                                                </div>
                                                <div class="address _fm-overhide">收货地址：${d.province} ${d.area}</div>
                                            </div>
                                          </a>
                                        </c:forEach>
                                       <!-- 多件商品 -->
                                       <c:if test="${isOne==2}">
                                        <p class="division _fm-rel"></p>
                                        <p class="one-item _fm-f-13">共${shopSize}件商品</p>
                                        <p class="division _fm-rel"></p>
                                        <a class="one-item img-show _fm-block" href="#goodsList">
                                            <div class="right-ico _fm-right">
                                                <span class="_fm-back-right">&nbsp;</span>
                                            </div>
                                            <div class="img-show-area">
                                              <c:forEach items="${shop}" var="s">
                                              <input name="isBoutique" value="${s.isBoutique}" type="hidden"/>
                                                <div class="area-elem _fm-inline">
                                                    <div class="_fm-img">
                                                        <div class="img-inner" src=""><img src="${s.img_url}" alt=""></div>
                                                    </div>
                                                </div>
                                              </c:forEach>
                                            </div>
                                        </a>
                                        <p class="division _fm-rel"></p>
                                        <p class="one-item _fm-f-13"><span class="gray">配送方式</span><span class="_fm-right">快递 免邮</span></p>
                                       </c:if>
                                       <!-- 多件商品/END -->
                                        
                                       <!-- 单件商品 -->
                                       <c:if test="${isOne==1}">
                                        <p class="division _fm-rel"></p>
                                        <p class="one-item _fm-f-13">DIY商城</p>
                                        <p class="division _fm-rel"></p>
                                        <section class="goods-detail">
                                            <div class="goods-content _fm-clearfix">
                                                <input name="isBoutique" value="${goods.isBoutique}" class="isBoutique" type="hidden"/>
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
                                        </section>
                                        <p class="division _fm-rel"></p>
                                        <p class="one-item edit-num _fm-f-13 _js-formatminus">
                                            <span class="gray">购买数量</span>
                                            <span class="_fm-right op-r do" data-op="editNum">+</span>
                                            <span class="_fm-right num">${goods.num}</span>
                                            <span class="_fm-right op-l do" data-op="editNum">-</span>
                                        </p>
                                       </c:if>
                                       <!-- 单件商品/END -->
                                        <p class="division _fm-rel"></p>
                                        <p class="one-item _fm-f-13 _fm-clearfix _fm-hidden"><span class="_fm-right yellow">尊享个性定制，全场满100减10</span></p>
                                        <p class="division _fm-rel _fm-hidden"></p>
                                        <a class="coupon one-item _fm-f-13 _fm-block _fm-overhide" href="#coupon">
                                            <span class="_fm-left">选择优惠券</span>
                                            <div class="right-ico _fm-right">
                                                <span class="_fm-back-right">&nbsp;</span>
                                            </div>
                                            <div class="msg _fm-overhide">
                                                <span class="_fm-right _fm-ellipsis">未使用优惠券</span>
                                            </div>
                                        </a>
                                        <p class="division _fm-rel"></p>
                                        <ul class="pay-method _js-wechatFit">
                                            <li class="method-elem _fm-overhide" data-id="2">
                                                <img class="_fm-left ico" src="<%=path%>/images/UUMall/wechat.png" alt="">
                                                <div class="right-ico _fm-right active do" data-op="payMethod">
                                                    <span class="_fm-check-1">&nbsp;</span>
                                                </div>
                                                <div class="msg">
                                                    <h1 class="_fm-f-14">微信支付</h1>
                                                    <p class="_fm-f-12">支持微信、网银的用户</p>
                                                </div>
                                            </li>
                                            <li class="method-elem _fm-overhide" data-id="1">
                                                <img class="_fm-left ico" src="<%=path%>/images/UUMall/zhifubao.png" alt="">
                                                <div class="right-ico _fm-right  do" data-op="payMethod">
                                                    <span class="_fm-check-1">&nbsp;</span>
                                                </div>
                                                <div class="msg">
                                                    <h1 class="_fm-f-14">支付宝支付</h1>
                                                    <p class="_fm-f-12">支持支付宝、网银的用户</p>
                                                </div>
                                            </li>
                                        </ul>
                                        <p class="division _fm-rel"></p>
                                        <div class="remarkInfo one-item _fm-f-13">
	                                            <h3>备注：</h3>
	                                            <input type="text" class="remarkTextarea" placeholder="定制商品请填写微信或QQ，以便设计师和您联系。" maxlength="20">
	                                    </div>
                                        <c:if test="${isOne==1}">
                                         <!--  <span class="submit do _fm-right _fm-txtcenter _fm-f-18" data-addr="<%=path%>/UOrder/formOrderOne.do" data-op="submit">提交订单</span> -->
                                          <form method='post'  class="_fm-hidden main-form" action="<%=path%>/UOrder/formOrderOne.do">
                                        </c:if>
                                        <c:if test="${isOne==2}">
                                          <!-- <span class="submit do _fm-right _fm-txtcenter _fm-f-18" data-addr="<%=path%>/UOrder/createOrderByShops.do" data-op="submit">提交订单</span> -->
                                          <form method='post'  class="_fm-hidden main-form" action="<%=path%>/UOrder/createOrderByShops.do">
                                        </c:if>                                        
                                            <input type="hidden" name="addressId" value="">
                                            <input type="hidden" name="payId" value="">
                                            <input type="hidden" name="num" value="">
                                            <input type="hidden" name="couponId" value="">
                                            <input type="hidden" name="remark" value="" >
                                            <c:if test="${isOne==1}">
                                              <input type="hidden" name="orderNo" value="${goods.orderNo}">
                                            </c:if>
                                            <c:if test="${isOne==2}">
                                              <input type="hidden" name="shopNos" value="${shopNos}">
                                            </c:if>                                            
                                        </form>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <!-- 页面主体/END -->
                    </div>
                </div>
                <div class="page cached no-toolbar" data-page="address">
                    <div class="page-content _js-limitdrag">
                        <!-- 页面主体 -->
                        <div class="cus-page">
                            <div class="cus-page-inner">
                                <!-- 收货地址页 -->
                                <div class="main-content address-list">
                                  <c:forEach items="${adress}" var="d" varStatus="i">
                                  <p class="division _fm-rel"></p>
                                  <c:if test="${i.index==0 }">
                                    <section class="order-address do active _fm-block _fm-overhide" data-id="${d.id}" data-op="useAddr">
                                  </c:if>
                                  <c:if test="${i.index!=0 }">
                                    <section class="order-address do _fm-block _fm-overhide" data-id="${d.id}" data-op="useAddr">
                                  </c:if>
                                        <div class="left-ico _fm-left">
                                            <span class="_fm-check-1">&nbsp;</span>
                                        </div>
                                        <div class="msg">
                                            <p class="_fm-overhide _fm-f-15">
                                                <span class="name _fm-left _fm-ellipsis">${d.name}</span>
                                                <span class="_fm-right">${d.mobile}</span>
                                            </p>
                                            <div class="right-ico _fm-right do" data-op="editAddr">
                                                <span class="_fm-inline _fm-rel _fm-txtcenter">&nbsp;</span>
                                            </div>
                                            <div class="address _fm-overhide">收货地址：${d.province} ${d.area}</div>
                                        </div>
                                    </section>
                                   
                                  </c:forEach>
                                  
                                </div>
                            </div>
                        </div>
                        <!-- 页面主体/END -->
                    </div>
                    <div class="add-address _fm-abs _fm-bz _fm-lz _fm-txtcenter">
                        <a class="_fm-inline _fm-f-13 do" href="#" data-op="addAddr">添加收货地址</a>
                    </div>
                </div>
                <div class="page cached no-toolbar" data-page="goodsList">
                    <div class="page-content _js-limitdrag">
                        <!-- 页面主体 -->
                        <div class="cus-page">
                            <div class="cus-page-inner">
                                <!-- 商品详情页 -->
                                <div class="main-content goods-detail">
                                  <c:forEach items="${shop}" var="s">
                                    <section class="goods-content _fm-clearfix">
                                        <input name="isBoutique" class="isBoutique" value="${s.isBoutique}" type="hidden"/>
                                        <div class="_fm-img _fm-rel _fm-left">
                                            <div class="img-inner"><img class="lazy" data-src="${s.img_url}" alt=""></div>
                                        </div>
                                        <div class="goods-msg _fm-rel">
                                            <h2 class="_fm-f-14 _fm-ellipsis">${s.name }</h2>
                                            <div class="price-count _fm-abs _fm-bz _fm-lz">
                                                <span class="count-num _fm-left _js-formatGoodsPrice" data-size="16px">￥${s.now_price}</span>
                                                <div class="total-num _fm-right">x<span class="num">${s.num}</span></div>
                                            </div>
                                            <p class="style _fm-overhide">${s.texture_name}</p>
                                        </div>
                                    </section>
                                  </c:forEach>
                                </div>
                            </div>
                        </div>
                        <!-- 页面主体/END -->
                    </div>
                </div>
                <div class="page cached no-toolbar" data-page="editAddress">
                    <div class="page-content _js-limitdrag">
                        <!-- 页面主体 -->
                        <div class="cus-page">
                            <div class="cus-page-inner">
                                <!-- 新增收货地址 -->
                                <form class="main-content edit-address" action="">
                                    <input class="_fm-f-14" name="name" type="text" placeholder="收货人">
                                    <input class="_fm-f-14" name="mobile" type="number" placeholder="手机号码">
                                    <input class="_fm-f-13 _js-provincePicker" data-addr="<%=path%>/js/UUMall/addr.json" name="province" type="text" placeholder="省、市、区(请点击选择)">
                                    <textarea class="_fm-f-13" name="area" placeholder="详细地址(请勿重复输入省、市、区)"></textarea>
                                    <div class="check-box _fm-overhide">
                                        <p class="_fm-left _fm-f-13">
                                            设为默认地址
                                            <br>
                                            <span class="gray _fm-f-12">注：每次下单时都会使用该地址</span>
                                        </p>
                                        <div class="_fm-right do" data-op="isDefault">
                                            <span class="_fm-check-3">&nbsp;</span>
                                        </div>
                                    </div>
                                    <input name="addressId" type="hidden" value="new">
                                    <input class="_fm-hidden" name="isDefaultAddr" type="checkbox" value="1">
                                    <input type="hidden" name="operation" value="edit">
                                </form>
                            </div>
                        </div>
                        <!-- 页面主体/END -->
                    </div>
                    <div class="save-use disabled do _fm-f-14 _fm-abs _fm-bz _fm-lz _fm-txtcenter" data-op="saveAndUse" data-addr="<%=path%>/UOrder/updateAdress.do">保存并使用</div>
                </div>
                <div class="page cached no-toolbar" data-page="coupon">
                    <div class="page-content _js-limitdrag">
                        <!-- 页面主体 -->
                        <div class="cus-page">
                            <div class="cus-page-inner">
                                <!-- 现金优惠券 -->
                                <div class="main-content coupon-list">
                                  <c:forEach items="${coupon}" var="c">
                                    <section class="coupon-elem do _fm-rel" data-num="${c.orgPrice}-${c.desPrice}" data-op="useCoupon" data-id="${c.id}">
                                        <div class="deno _fm-abs _fm-img _fm-tz _fm-lz">
                                            <div class="img-inner">
                                                <span class="num _fm-block">${c.desPrice}<span class="_fm-f-16">元</span></span>
                                                <span class="_fm-block">现金券</span>
                                            </div>
                                        </div>
                                        <div class="left-saw _fm-abs _fm-tz"><span></span><span></span><span></span><span></span><span></span><span></span><span></span><span></span><span></span><span></span><span></span><span></span></div>
                                        <div class="right-saw _fm-abs _fm-tz"><span></span><span></span><span></span><span></span><span></span><span></span><span></span><span></span><span></span><span></span><span></span><span></span></div>
                                        <div class="elem-inner _fm-rel">
                                            <span class="check-ico _fm-right"><i class="_fm-check-1">&nbsp;</i></span>
                                            <h1 class="_fm-f-13 _fm-ellipsis">唯优品现金优惠券</h1>
                                            <p class="_fm-overhide">${c.about}</p>
                                            <div>有效期至${c.valid}</div>
                                        </div>
                                    </section>
                                  </c:forEach>
                                    
                                </div>
                            </div>
                        </div>
                        <!-- 页面主体/END -->
                    </div>
                </div>
            </div>
            <div class="toolbar _fm-f-18">
                <div class="toolbar-inner _fm-block _fm-rel _fm-overhide">
                    <div class="_fm-left">
                        <p class="total _fm-f-18">合计:<span class="count-num _fm-f-14" data-size="18px">￥<span class="_fm-f-18">0.</span>00</p>
                        <p class="detail _fm-f-12">原价:<span>￥0.00</span>已优惠:<span>￥0.00</span></p>
                    </div>
                    <c:if test="${isOne==1}">
                      <span class="submit do _fm-right _fm-txtcenter _fm-f-18" data-addr="<%=path%>/UOrder/formOrderOne.do" data-op="submit">提交订单</span>
                    </c:if>
                    <c:if test="${isOne==2}">
                      <span class="submit do _fm-right _fm-txtcenter _fm-f-18" data-addr="<%=path%>/UOrder/createOrderByShops.do" data-op="submit">提交订单</span>
                    </c:if>
                </div>
            </div>
        </div>
    </div>
    <!-- 脚本文件导入
==================================================-->
    <script src="<%=path %>/js/UUMall/custom/framework7.min.js"></script>
    <script src="<%=path %>/js/UUMall/custom.js?r="+Math.random()></script>
</body>

</html>
