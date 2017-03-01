<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
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
    <title>绑定手机号码</title>
    <!-- Path to Framework7 Library CSS-->
    <link rel="stylesheet" href="<%=path %>/css/UUMall/framework7.ios.min.css">
    <!-- Path to your custom app styles-->
    <link rel="stylesheet" href="<%=path %>/css/UUMall/my-app.css?r="+Math.random()>
  </head>
  <body class="indexBody-kwj">  
  
    <!-- Status bar overlay for fullscreen mode-->
    <div class="statusbar-overlay"></div>
    <!-- Views-->
    <div class="views">
      <!-- Your main view, should have "view-main" class-->
      <div class="view view-main">
        <!--   Navbar-->
        <div class="navbar">
          <div class="navbar-inner">
            <div class="left">
              <a href="personal-center.html" class="link icon-only back"><i class="icon icon-back"></i></a>
            </div>
            <div class="center bold">绑定手机号码</div>
          </div>
        </div>
        <!-- Pages, because we need fixed-through navbar and toolbar, it has additional appropriate classes-->
        <div class="pages">
          <!-- Page, data-page contains page name-->
          <div data-page="banding" class="page banding">
            <!-- 绑定表单-->
            <div class="page-content">
              <div class="banding-form">
                <ul>
                  <li>
                    <input type="tel" class="tel" placeholder="输入手机号码" maxlength="11">
                  </li>
                  <li>
                    <input type="tel" class="v-code" placeholder="输入验证码" maxlength="6">
                    <input class="send-Vcode" type="button" value="发送验证码" id="bindSendVCodeBtn">
                  </li>
                  <li>
                    <input id="bindingBtn" type="button" class="bandBtn" value="绑定" disabled>
                  </li>
                </ul>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
    <!-- Path to Framework7 Library JS-->
    <script type="text/javascript" src="<%=path %>/js/UUMall/framework7.min.js"></script>
    <!-- Path to your app js-->
    <script type="text/javascript" src="<%=path %>/js/UUMall/my-app.js?r="+Math.random()></script>
  </body>
</html>