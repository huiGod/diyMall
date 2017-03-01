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
    <title>发表评论</title>
    <link rel="stylesheet" href="<%=path %>/css/UUMall/framework7.ios.min.css">
    <link rel="stylesheet" href="<%=path %>/css/UUMall/my-app.css">
  </head>
  <body class="">  
    <div class="statusbar-overlay"></div>
    <div class="views">
      <div class="view view-main">
        <div class="navbar">
          <div class="navbar-inner">
            <div class="left">
              <a href="index.html" class="link icon-only external"><i class="icon icon-back"></i></a>
            </div>
            <div class="center bold" style="font-weight: 700">发表评论</div>
          </div>
        </div>
        <div class="pages navbar-through toolbar-through">
          <div data-page="releaseComments" class="page">
            <div class="page-content">
             <form id="commentForm" action="<%=path%>/UGoods/saveComment.do" method="post">
             <input type="hidden" name="orderNo" value="${orderNo}"/>
              <div class="commentList">
                <ul>
                <c:forEach items="${com}" var="com">
                  <li class="commentLi">
                  <input name="info_id" type="hidden" value="${com.info_id}"/>
                    <div class="header clearfix">
                      <div class="goodPicBox">
                      	<img src="${com.img_url}${com.file_type}" >
                      </div>
                      <div class="commentStar clearfix">
                        <span style="float:left">描述相符</span>
                        <ul class="starBox" >
                          <li class="star"></li>
                          <li class="star"></li>
                          <li class="star"></li>
                          <li class="star"></li>
                          <li class="star"></li>
                        </ul>
                        <input id="starNum" style="display:none" class="starNum" name="starNum" value="5"/>
                      </div>
                    </div>
                    <div class="commentContent">
                      <h3 class="commentH3">评价</h3>
                      <textarea class="commentArea" name="commentArea"cols="30" rows="5" maxlength="300" placeholder="请写下对宝贝的感受吧，对他人帮助很大哦~"></textarea>
                    </div>
                  </li>
                </c:forEach>
                </ul>
              </div>
             </form>
            </div>
          </div>
        </div>
        <input type="button" value="提交评价" class="submitCommentBtn"/>
      </div>
    </div>
    <script type="text/javascript" src="<%=path %>/js/UUMall/framework7.min.js"></script>
    <script type="text/javascript" src="<%=path %>/js/UUMall/my-app.js"></script>
  </body>
</html>