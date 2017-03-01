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
    <title>今日推荐</title>
    <style>
        *{margin:0;padding:0;}
        body{position: absolute;top: 0;left: 0;width: 100%;height: 100%;box-sizing: border-box;}
        .commonNav{position:absolute;top:0;left:0;display: block;width:100%;height:44px;line-height: 44px;background-color:#fff;box-sizing: border-box;padding:0 8px;}
        .back{display:inline-block;width:44px;height:44px;background:no-repeat center;background-image:url("data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABUAAAAkCAYAAABmMXGeAAAAGXRFWHRTb2Z0d2FyZQBBZG9iZSBJbWFnZVJlYWR5ccllPAAAAyJpVFh0WE1MOmNvbS5hZG9iZS54bXAAAAAAADw/eHBhY2tldCBiZWdpbj0i77u/IiBpZD0iVzVNME1wQ2VoaUh6cmVTek5UY3prYzlkIj8+IDx4OnhtcG1ldGEgeG1sbnM6eD0iYWRvYmU6bnM6bWV0YS8iIHg6eG1wdGs9IkFkb2JlIFhNUCBDb3JlIDUuMy1jMDExIDY2LjE0NTY2MSwgMjAxMi8wMi8wNi0xNDo1NjoyNyAgICAgICAgIj4gPHJkZjpSREYgeG1sbnM6cmRmPSJodHRwOi8vd3d3LnczLm9yZy8xOTk5LzAyLzIyLXJkZi1zeW50YXgtbnMjIj4gPHJkZjpEZXNjcmlwdGlvbiByZGY6YWJvdXQ9IiIgeG1sbnM6eG1wPSJodHRwOi8vbnMuYWRvYmUuY29tL3hhcC8xLjAvIiB4bWxuczp4bXBNTT0iaHR0cDovL25zLmFkb2JlLmNvbS94YXAvMS4wL21tLyIgeG1sbnM6c3RSZWY9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC9zVHlwZS9SZXNvdXJjZVJlZiMiIHhtcDpDcmVhdG9yVG9vbD0iQWRvYmUgUGhvdG9zaG9wIENTNiAoV2luZG93cykiIHhtcE1NOkluc3RhbmNlSUQ9InhtcC5paWQ6QTNCREYzQTAzNkFCMTFFNjk3RTdCMzI1Q0QwN0JGNDQiIHhtcE1NOkRvY3VtZW50SUQ9InhtcC5kaWQ6QTNCREYzQTEzNkFCMTFFNjk3RTdCMzI1Q0QwN0JGNDQiPiA8eG1wTU06RGVyaXZlZEZyb20gc3RSZWY6aW5zdGFuY2VJRD0ieG1wLmlpZDpBM0JERjM5RTM2QUIxMUU2OTdFN0IzMjVDRDA3QkY0NCIgc3RSZWY6ZG9jdW1lbnRJRD0ieG1wLmRpZDpBM0JERjM5RjM2QUIxMUU2OTdFN0IzMjVDRDA3QkY0NCIvPiA8L3JkZjpEZXNjcmlwdGlvbj4gPC9yZGY6UkRGPiA8L3g6eG1wbWV0YT4gPD94cGFja2V0IGVuZD0iciI/PjlP38oAAABtSURBVHjaYmCgDjAH4jIGKgKQgR+A+D8Q11PbQBiuZ6LQwJ1AzI8mzkhNF4Jww6iBowaOGjhqIFUNZMJTVP2ntLC1wOHa+lGDRw0eNXjU4AE1mJJW3wkg9gDij9Rq9eFycQMDFQHI4HIYByDAABBArSioQtoBAAAAAElFTkSuQmCC");background-size:12px 20px;}
        .center{font-size:17px;font-weight:700;color:#000;position:absolute;top:0;left:50%;margin-left:-2em;display: inline-block;height: 42px;line-height: 45px;}
        .iframecontain{position:absolute;top:44px;left:0;right:0;bottom:0;display: block;width:100%;}
        .iframeStyle{position: absolute;top:0;left: 0;width:100%;overflow:auto;-webkit-overflow-scrolling:touch;}
    </style>
  </head>
  <body>
        <nav class="commonNav">
            <!-- <a href="http://test.diy.51app.cn/diyMall2/UMallUser/personalCenter.do" class="back"></a> -->
            <a href="javascript:history.go(-1);" class="back"></a>
            <span class="center">今日推荐</span>
        </nav> 
        <div class="iframecontain">
            <iframe src="http://api.app.51app.cn/recommend/toRecommend.do?token=${token}&appId=${app}" height="100%" width="100%" frameborder="0" class="iframeStyle"></iframe>
        </div>  
        <script>
            var ua = window.navigator.userAgent.toLowerCase();
            if(ua.match(/MicroMessenger/i) == 'micromessenger'){
                document.querySelector('.commonNav').style.display = "none";
                document.querySelector('.iframecontain').style.top = '0';
            }
        </script>     
  </body>
</html>