<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
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
    <title>产品攻略</title>
    <link rel="stylesheet" href="<%=path %>/css/UUMall/framework7.ios.min.css">
    <link rel="stylesheet" href="<%=path %>/css/UUMall/my-app.css?r="+Math.random()>
  </head>
  <body class="themeGoods">  
    <div class="statusbar-overlay"></div>
    <div class="views">
      <div class="view view-main">
        <div class="navbar">
          <div class="navbar-inner">
            <div class="left">
              <a href="" class="link icon-only back"><i class="icon icon-back"></i></a>
            </div>
            <div class="center bold">产品攻略</div>
          </div>
        </div>
        <div class="pages navbar-through toolbar-through">
          <div data-page="themeGoodsPage" class="page">
            <div class="goodsList page-content">
              <div class="goodsListScroll" style="position:absolute;top:34px;bottom:0;width:100%;left:0;box-sizing:border-box;overflow:auto;-webkit-overflow-scrolling:touch;">
              <ul>
              <c:forEach items="${special}" var="s">
              	<li class="goodsLi">
	                 <div class="Desc">
	                   <h3 class="goodsHead">
                      <span class="color_orange"></span>${s.name}
                      <a class="goToBookBtn external" href="<%=path%>/UGoods/goodsInfo.do?id=${s.pid}">去定制</a>
                      <a href="http://a.app.qq.com/o/simple.jsp?pkgname=wcl.com.yqshop" class="external toDownloadBtn">客户端定制</a>
                    </h3>
	                   <p class="someTip">${s.text}</p>
	                 </div>
	                 <div class="picBox"><img src="${s.img_url}"></div>
               </li>
              </c:forEach>
              </ul>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
    <script type="text/javascript" src="<%=path %>/js/UUMall/framework7.min.js"></script>
    <script type="text/javascript" src="<%=path %>/js/UUMall/my-app.js?r="+Math.random()></script>
  </body>
</html>