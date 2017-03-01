<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="gbp" value="${goodsBuyParam}" />
<%
	String path = request.getContextPath();
%>
<!DOCTYPE html>
<html lang="zh-CN">
  <head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, minimum-scale=1, user-scalable=no, minimal-ui">
    <meta name="apple-mobile-web-app-capable" content="yes">
    <meta name="apple-mobile-web-app-status-bar-style" content="black">
    <meta name="format-detection" content="telephone=no" />
    <title>唯优品</title>
    <link rel="stylesheet" href="<%=path %>/css/UUMall/framework7.ios.min.css">
    <link rel="stylesheet" href="<%=path %>/css/UUMall/goods-info.css">
  </head>
  <body class="goodsInfoBody-kwj">  
    <div class="statusbar-overlay"></div>
    <div class="views">
      <div class="view view-main">
        <div class="navbar">
          <div class="navbar-inner">
            <div class="left">
              <!-- <a href="<%=path%>/UGoods/tohome.do" class="link icon-only external"><i class="icon icon-back"></i></a> -->
              <a href="javascript:history.go(-1);" class="link icon-only external" id="no-history"><i class="icon icon-back"></i></a>
            </div>
            <div class="center" style="height:44px;line-height: 44px;">
              <input type="hidden" value="${goods.id}" id="goodsId" />
              <div class="buttons-row" style="height: 42px;line-height: 42px;">
                <a href="#tab1" class="tab-link active button fs15">商品</a>
                <a href="#tab2" class="tab-link button fs15">详情</a>
                <a href="#tab3" class="tab-link button fs15">评价</a>
              </div>
            </div>
            <div class="right">
              <a href="<%=path%>/UOrder/toShop.do" class="link icon-only external"><i class="icon icon-car"><span class="badge bg-red" id="shopCarNum"></span></i></a>
            </div>
          </div>
        </div>
        <!-- Pages 内容区-->
        <div class="pages navbar-through toolbar-through">
          <div data-page="goods-info" class="page">
            <div class="page-content tabs">
              <div class="tabs-animated-wrap">
                <div class="tabs">
                  <!-- Tab 1，默认激活 -->
                  <div id="tab1" class="tab active">  
                    <div id="tab1contain" class="tab1contain">                  
                      <div class="goodsInfo-picBox">
                      	<c:if test="${goods.isBoutique==1}">
                        <img id="box_url" src="${goods.previewImgUrl }" >
                        </c:if>
                        <c:if test="${goods.isBoutique==2}">
                        <img id="box_url" src="${goods.h5url }" >
                        </c:if>
                        <a href="javascript:;" onclick="window.location.href='http://a.app.qq.com/o/ioslink.jsp?id=1105250240'" class="external">
                          <div class="paddingTween15 android-no">
                            <div class="downloadApptip ">
                              <img class="downloadApplogo" src="<%=path %>/images/UUMall/logo-wyp.png" ><span>下载定制APP，亲手定制个性产品。</span>
                            </div>
                          </div>
                        </a>
                      </div>
                      <div class="goodsInfo-desc">
                        <h3 class="swicthTab">${goods.title }<i class="right-arrow"></i></h3>
                        <p class="goodsDetails">
                          <span class="now-price"><i class="fs12">¥&nbsp;</i><span class="now-price-num" id="nowPrice">${goods.now_price }</span></span>
                          <span class="ori-price">¥<span id="original_price">${goods.org_price }</span></span>
                          <span class="selled">已售<i class="selled-num">186</i>件</span>
                          <span class="feetips">${goods.transportfee }</span>
                        </p>
                      </div>
                      <a href="#" class="open-popup" data-popup="popup-material">
                        <div class="paddingTop10">
                          <div class="goodsInfo-option">
                              <span>已选&nbsp;</span>
                              <span class="selectedParam"><c:forEach items="${goods.paramList}" var="pl"> ${pl}&nbsp;</c:forEach>,1件</span>
                              <i class="right-arrow"></i>
                          </div>
                        </div> 
                      </a>
                      <!-- 为您推荐 -->
                      <div class="recommentForU">
                        <div class="ForUTitle"><span class="recommentForU-icon"></span>&nbsp;为您推荐</div>
                        <ul>
                        	<c:forEach items="${goods.recommend}" var="rec">
                          <li class="recommentLi">
                            <a class="external" href="<%=path%>/UGoods/goodsInfo.do?id=${rec.id}"><div class="goodPicBox"><img src="${rec.previewImgUrl}"></div>
  	                          <div class="describ">
  	                            <h3 class="goodDescribH3">${rec.title} </h3>
  	                            <p class="price">¥<span class="price-num">${rec.now_price}</span></p>
  	                          </div>
               						  </a>                       
              						</li>
                          </c:forEach>
                        </ul>
                      </div>
                    </div>
                  </div>
                  <!-- Tab 2 -->
                  <div id="tab2" class="tab">
                    <div class="tab2nav">
                      <ul class="clearfix">
                        <li class="clicked">图文详情</li>
                        <li>规格参数</li>
                        <li>包装售后</li>
                      </ul>
                    </div>
                    <div class="wrapper" id="lazyLoad">   
                      <div class="details detail-1">  
                        <div class="picBox-detail">
                          <c:forEach items="${goodsDetails.introduceList}" var="details">
                            <img src="http://www.dowebok.com/demo/2013/53/images/blank.gif" data-echo="${details}"  >
                          </c:forEach>
                        </div>       
                      </div> 
                      <div class="details detail-2 hide">
                        <ul>
                          <c:forEach items="${goodsDetails.parameterList}" var="parameter">
                            <li><span>${parameter.title}</span><div>${parameter.txt}</div></li>
                          </c:forEach>
                        </ul>
                      </div>
                      <div class="details detail-3 hide">
                        <ul>
                          <c:forEach items="${goodsDetails.packAfterSaleList}" var="pas">
                              <li><span>${pas.title}</span><div style="color:#000">${pas.txt}</div> </li>
                          </c:forEach>
                          <li><span>${goodsDetails.priceNote.title}</span><div class="price-D">${goodsDetails.priceNote.txt}</div>
                        </ul> 
                      </div>
                    </div>  
                  </div>
                  <!-- Tab 3 :商品评价 -->
                  <div id="tab3" class="tab" style="height:100%;overflow:hidden;"> 
                    <iframe src="<%=path %>/UGoods/toevaluation.do?id=${goods.id}" height="100%" width="100%" frameborder="0"></iframe>
                  </div>
                </div>
              </div>     
            </div>
          </div>
        </div>
        <!-- 加入购物车 toolbar -->
        <div class="toolbar">
          <div class="toolbar-inner">
            <div class="row">
              <div class="col-50">
                <a href="#" class="button button-big addCar">加入购物车</a>
              </div>
              <div class="col-50">
                <a href="#" class="button button-big buyNow">立即购买</a>
              </div>
            </div> 
          </div>
        </div>
      </div>
    </div>
    <!-- 弹窗 -->
    <div class="popup popup-material">
      <div class="clickToExit"></div>
      <div class="content-block">        
        <div class="selectPopLayer">         
              <!-- 0.弹窗头部 -->
              <div class="pop-head">
                <div class="pop-head-content">
                  <div class="pop-imgBox"><img id="gImg" src="${gbp.pre_url}" alt=""></div>
                  <div class="pop-info-Box">
                    <h2 class="price">¥ <em id="gNowPrice">${gbp.now_price}</em></h2>
                  </div>
                </div>  
              </div>
              <div class="pop-body">
                <div id="pop-GoodsContent">
                  <!-- 1.选择材质 -->
                  <div class="pop-selects fc00">
                    <ul>
                        <li>
            							<h3 class="fs14 pop-cont-h"><span>${gbp.listA.title}</span></h3>
            							<p class="fs12 pop-opts ">
            								<c:forEach items="${gbp.listA.list}" var="la">
            									<span class="listA" id="_${la.id }">${la.gname}</span>
            								</c:forEach>
            							</p>
            						</li>
            						<li>
            							<h3 class="fs14 pop-cont-h"><span>${gbp.listB.title}</span></h3>
            							<p class="fs12 pop-opts ">
            								<c:forEach items="${gbp.listB.list}" var="lb">
            									<span class="listB" id="_${lb.id }">${lb.gname}</span>
            								</c:forEach>
            							</p>
            						</li>
                    </ul>
                    <c:if test="${goods.isBoutique==2}">
                    <!-- 添加客户联系方式 -->
                    <div class="customerConnect">
                      <!-- <span class="define">自定义图案<img src="<%=path %>/images/UUMall/jian.png" class='recommendIcon'></span> -->
                      <span style="font-size:14px;font-weight:700">图案<span style="font-size: 12px;font-weight:300">（设计师免费提供设计）</span></span>
                      <p class="connectTips" style="margin-top:10px;text-align:center;">
                        <img style="width:100%;" src="<%=path %>/images/UUMall/self-define2.png">
                      </p>
                    </div>
                    </c:if>
                  </div>    
                  <!-- 2.数量选择 -->
                  <div class="clearfix pop-selNum">
                    <span class="fl fc00" id="goumaiNum">购买数量</span> 
                    <div class="rdc-add clearfix fr rdc-add2">
                      <span class=" com-r-a reduce"></span>
                      <i class="numbers"><input type="tel" maxlength="3" value="1" class="input-num"></i>  
                      <span class=" com-r-a add"></span>
                    </div>
                    <button style="display:none;color:#fff;" id="closeKeyboardBtn">close</button>
                  </div>
                </div>
              </div>
        </div>
        <!-- 数据存放 -->
        <c:forEach items="${gbp.texture }" var="t">
    			<div style="display:none" class="_${t.texture_ids }">
    				<span class="org_price">${t.org_price}</span>
    				<span class="now_price">${t.now_price}</span>
    				<span class="pre_url">${t.pre_url}</span>
    				<span class="box_url">${t.box_url}</span>
    				<span class="saveSize">${t.save_size}</span>
    				<span class="texture_ids">${t.texture_ids}</span>
    			</div>
    		</c:forEach>
        <!-- 数据存放 微信 -->
        <div id="wxData">
          <span class="appId">${share.appId}</span>
          <span class="timestamp">${share.timestamp}</span>
          <span class="nonceStr">${share.noncestr}</span>
          <span class="signature">${share.sign}</span>
        </div>
        <span style="display: none" class="defaultMetarialIds">${goods.texture_ids}</span>
        <a href="javascript:void(0)" class="closePopBtn"></a> 
        <a href="javascript:void(0)" class="ensureBtn">确定</a>
      </div>
    </div>
    <script src="<%=path %>/js/UUMall/weixin.js"></script>
    <script src="<%=path %>/js/UUMall/wxConfig.js"></script>
    <script type="text/javascript" src="<%=path %>/js/UUMall/framework7.min.js"></script>
    <script type="text/javascript" src="<%=path %>/js/UUMall/my-app.js"></script>
    <script src="<%=path %>/js/UUMall/echo.js"></script>
  </body>
</html>