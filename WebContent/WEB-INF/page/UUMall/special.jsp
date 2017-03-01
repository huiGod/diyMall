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
    <title>唯优品</title>
    <link rel="stylesheet" href="<%=path %>/css/UUMall/framework7.ios.min.css">
    <link rel="stylesheet" href="<%=path %>/css/UUMall/my-app.css">
  </head>
  <body class="themePage"> 
    <div class="views">
      <div class="view view-main">
        <div class="pages navbar-through toolbar-through">
          <div data-page="themePage" class="page">
            <div class="navbar">
              <div class="navbar-inner">
                <div class="left">
                  <a href="<%=path%>/UGoods/tohome.do" class="link icon-only external"><i class="icon icon-back"></i></a>
                </div>
                <div class="center themeCenter" style="">${special[0].navName}</div>
              </div>
            </div>
            <div class="themeList page-content">
              <div class="themeListScroll scrollTouch">
                <ul>
                	<c:forEach items="${special}" var="s">
                		<c:if test="${s.type == 1}">
  	              		<li class="themeLi clearfix">
  		              		<a href="<%=path%>/UGoods/goodsInfo.do?id=${s.pid}" class="external">
  			                  <div class="picBox"><img src="${s.img_url}"" ></div>
  			                  <div class="desc">
  			                    <h3 class="themeTitle">${s.name}</h3>
  			                    <p class="themeDesc">${s.text}</p>
  			                  </div>
  			                </a>
  	                	</li>
                  	</c:if>
                  	<c:if test="${s.type == 2}">
  	              		<li class="themeLi clearfix">
  		              		<a href="<%=path%>/UGoods/toSpecial.do?type=2&special_ids=${s.pid}">
  			                  <div class="picBox"><img src="${s.img_url}" ></div>
  			                  <div class="desc">
  			                    <h3 class="themeTitle">${s.name}</h3>
  			                    <p class="themeDesc">${s.text}</p>
  			                  </div>
  			                </a>
  	                	</li>
                  	</c:if>
                	</c:forEach>
                </ul>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
    <script type="text/javascript" src="<%=path %>/js/UUMall/framework7.min.js"></script>
    <script type="text/javascript" src="<%=path %>/js/UUMall/my-app.js"></script>
  </body>
</html>