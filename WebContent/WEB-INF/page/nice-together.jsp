<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
%>
<!DOCTYPE html>
<html lang="en">
<head>
	<meta charset="UTF-8">
	<meta content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0" name="viewport">
	<meta content="yes" name="apple-mobile-web-app-capable"/>
	<title>51app</title>
	<link rel="stylesheet" href="//g.alicdn.com/msui/sm/0.6.2/css/sm.min.css">
	<link rel="stylesheet" href="<%=path %>/css/css.css">	
</head>
<body>
	<div class="page-group">
	    <!-- 首页：diy -->
        <div class="page page-current" style="top:62px;">              
            <div class="content pull-to-refresh-content infinite-scroll-bottom " style="bottom:105px;" data-distance="0">
            	<!-- 默认的下拉刷新层 -->
				    <div class="pull-to-refresh-layer">
				        <div class="preloader"></div>
				        <div class="pull-to-refresh-arrow"></div>
				    </div>
            	<div class="diy-wrapper wall infinite-scroll" id="AddMore">			
					<!-- 商品展示 -->					
				</div>
            </div>
        </div>
	</div>
	<script type='text/javascript' src='//g.alicdn.com/sj/lib/zepto/zepto.min.js' charset='utf-8'></script>	
	<script src="<%=path %>/js/jaliswall.js" type="text/javascript" charset="utf-8"></script>
	<script type='text/javascript' src='//g.alicdn.com/msui/sm/0.6.2/js/sm.min.js' charset='utf-8'></script>	
	<script>
	// 初始化布局
	$('.wall').jaliswall({ item: '.diy-content' });	
	// 初始化
	$.init();
	// 添加'refresh'监听器  下拉刷新
	$.initPullToRefresh(".pull-to-refresh-content"); 
	//查询页码
	var page=1;
	var loading = false;
	var queryData=new (function(){
		
		var showdata =function(e){
			var evt= e;
			var column0=$(".wall-column").eq(0);
			var column1=$(".wall-column").eq(1);
			if (loading) return;
			loading = true;
			//ajax请求精品汇数据
			$.ajax({
				url: "nice.do",
				type: 'POST',
				dataType: 'json',
			    cache:false,
			    timeout:"60000",
			    data:{page:page},
			    success:function(data){				    	    					    	    	
					//console.log(page);
			    	if(data.code==200){
			    		var p_name = column0.find(".P-Name").eq(0).text();
			    		var j_name = data.data[0].name;
			    		if(evt=="refresh"&& p_name==j_name){  return;}
			    		else if (evt=="refresh"){column0.empty();column1.empty();}
			    		
			    		//迭代商品
			    		$.each(data.data, function(index, val) {			    			
			        		var x=column0.height();
							var y=column1.height();
				        	var	str="<div class='diy-content linec nice-to' onclick='sending("+data.data[index].id+")'><div class='diy-goods'><div class='diy-pic-box'><img class='fix-w' src="+data.data[index].icoUrl+"></div><div class='diy-details'><h2 class='clearfix fc5 line-h22'><i class='fl fs12 line-h24 fb'>¥</i><span class='fl fs17 fb'>"+data.data[index].now_price+"</span> <i class='fr fs9 fc2'>已售<i class='fb fc5 fs12'>"+data.data[index].sell+"</i>件</i></h2><p class='fs12 fc-b ov-hidden P-Name'>"+data.data[index].name+"</p></div></div></div>"
				        	if(evt=="refresh"){
				        		var temp=(x>y)? column1.append(str):column0.append(str); 
				        		 loading = false;
				        	}else if(evt=="infinite"){
				        		var temp=(x>y)? column1.append(str):column0.append(str);
				        		loading = false;
				        	}else{loading = false;return false;}		        		       		
			       		});
			    	}else{
							loading=false;
			    			//$.toast("没有更多...",1000);
			    		 }
			    }
			});
		};
		var show=function(e){
			var evt=e.type; 
			if(evt=="refresh"){
				page=1;
        		setTimeout(function(){showdata(evt);$.pullToRefreshDone('.pull-to-refresh-content');},1000);
        	}else if(evt=="infinite"){
				page++;
        		 setTimeout(function(){showdata(evt); $.refreshScroller();},100);
        	}else{return false;}			
		}
		$(document).on('infinite', '.infinite-scroll-bottom',show);
		$(document).on('refresh', '.pull-to-refresh-content',show);	
		showdata("refresh");	
	});
	</script>
	<script type="text/javascript">
		function connectNZOCJSBridge(callback) {
			if (window.NZOCJSBridge) {
				callback(NZOCJSBridge)
			} else {
				document.addEventListener('NZOCJSBridgeReady', function() {
					callback(NZOCJSBridge)
				}, false)
			}
		}
		function sending(id){
			data = {"click":id};
			connectNZOCJSBridge(function(bridge) {
				bridge.send(data, function(responseData) {});
			});
		}
	</script>
</body>
</html>