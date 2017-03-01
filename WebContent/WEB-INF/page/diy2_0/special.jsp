<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
	String path=request.getContextPath();
%>
<!DOCTYPE html>
<html lang="zh-CN">
  <head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, minimum-scale=1, user-scalable=no, minimal-ui">
    <meta name="apple-mobile-web-app-capable" content="yes">
    <meta name="apple-mobile-web-app-status-bar-style" content="black">
    <title>${special[0].navName }</title>
    <link rel="stylesheet" href="<%=path %>/css/diy20/index.css">
  </head>
  <body class="themeP body-bg"> 
    <main class="themeM overauto _js-touchScroll">
      <ul class="themeList">
      <c:forEach items="${special}" var="ul" >
      	<li class="themeLi clearfix">
          <a href="javascript:oc.sending(${ul.pid});" class="external">
            <div class="picBox"><img src="${ul.img_url}" ></div>
            <div class="desc">
              <h3 class="themeTitle">${ul.name}</h3>
              <p class="themeDesc">${ul.text}</p>
            </div>
          </a>
        </li>
      </c:forEach>              
      </ul>
    </main>
  <script src="<%=path %>/js/diy20/zepto.min.js"></script>
  <script src="<%=path %>/js/diy20/touch.js"></script>
  <script>
    /* 初始化
    ============================================================*/
    $(function() {
        $('[class*=_js-]').each(execute);
    });
    /* 处理页面脚本元素
    ==================================================*/
    var execute = function(i,d){
        $.each(d.className.match(/\_js\-[a-zA-Z]+/g),function(k,v){
            func[v.slice(4)].call(d,'body');
        });
    }
    /* 处理页面脚本元素方法
    ==================================================*/
    var func = {
        touchScroll : function(parentEle){ 
            var el = this ,parentEle = $(parentEle);
            el.addEventListener('touchstart', function() {
                var top = el.scrollTop,
                    totalScroll = el.scrollHeight,
                    currentScroll = top + el.offsetHeight;
                if(top === 0) {
                  el.scrollTop = 1; 
                } else if((currentScroll+1) === totalScroll) {
                  el.scrollTop = top - 1;
                }else if(currentScroll==totalScroll){
                    el.scrollTop = top - 1;
                }else if((currentScroll-1)==totalScroll){
                    el.scrollTop = top - 1;
                }
            })
            el.addEventListener('touchmove', function(e) {
                if(el.offsetHeight < el.scrollHeight)
                  e._isScroller = true;
            })
            parentEle.on('touchmove',function(e) {
              if(!e._isScroller) { e.preventDefault(); }
            })
        }
    };
    /*js-OC交互
    ============================================================*/
    var oc={
    		connectNZOCJSBridge:function(callback) {
    	    	if (window.NZOCJSBridge) {
    	    		callback(NZOCJSBridge)
    	    	} else {
    	    		document.addEventListener('NZOCJSBridgeReady', function() {
    	    			callback(NZOCJSBridge)
    	    		}, false)
    	    	}
    	    },
    	     sending:function(id,goodId,isBoutique){
    	    	data = {"id":id,"goodType":goodId,"isBoutique":isBoutique};
    	    	oc.connectNZOCJSBridge(function(bridge) {
    	    		bridge.send(data, function(responseData) {})
    	    	});
    	    },
    	    sendingHref:function(href){
    	    	//href 为空时不与手机端交互
    	    	if(href=='') return;	
    	    	data = {"href":href};
    	    	oc.connectNZOCJSBridge(function(bridge) {
    	    		bridge.send(data, function(responseData) {})
    	    	});
    	    }	
    }
  </script>
  </body>
</html>