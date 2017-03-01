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
  <body style="font-family: 'Microsoft yahei';">  
    <div class="views">
      <div class="view view-main">   
        <div class="pages">
          <div data-page="personal-center" class="page pernalCenter">
            <!--   Navbar-->
            <div class="navbar">
              <div class="navbar-inner">
                <div class="left">
                  <a href="<%=path%>/UGoods/tohome.do" class="link icon-only external"><i class="icon icon-back"></i></a>
                </div>
                <div class="center bold" style="font-weight: 700">个人中心</div>
              </div>
            </div> 
            <!-- Scrollable page content-->
            <div class="page-content">
              <!-- 头像 -->
              <div class="WX-heading">
                <span class="heading"><img src="${user.head_url }" alt=""></span>
                <span class="nickname">${user.name }</span>
              </div>
              <!-- 列表块 -->
              <div class="content-block list-link">
                <ul>
                  <c:if test="${empty user.mobile}"><li><a href="<%=path%>/UMallUser/toBinding.do">绑定手机号码<span class="query">（方便查询追踪订单）</span></a></li></c:if>
                  <li><a class="external" href="<%=path%>/UOrder/toOrderList.do">我的订单</a></li>
                  <li><a class="external" href="<%=path%>/UOrder/toShop.do">购物车</a></li>
                  <!-- <li><a class="external" href="<%=path%>/UMallUser/todayRecomment.do?token=${user.device_no}&app=${user.app}">今日推荐</a></li>
                  <li><a class="external" href="<%=path%>/UMallUser/diyring.do">彩铃</a></li> -->
                  <li class="marginTop15 android-no" ><a class="external" href="http://a.app.qq.com/o/ioslink.jsp?id=1105250240">下载定制APP，亲手定制个性产品</a></li>
                </ul>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
    <script type="text/javascript" src="<%=path%>/js/UUMall/framework7.min.js"></script>
    <script type="text/javascript" src="<%=path%>/js/UUMall/my-app.js"></script>
  </body>
</html>