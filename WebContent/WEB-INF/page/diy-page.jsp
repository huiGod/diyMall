<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html lang="en">
<head>
	<meta charset="UTF-8">
	<meta content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0" name="viewport">
	<meta content="yes" name="apple-mobile-web-app-capable"/>
	<title>唯优品</title>
	<link rel="stylesheet" href="//g.alicdn.com/msui/sm/0.6.2/css/??sm.min.css,sm-extend.min.css">
	<link rel="stylesheet" href="${ctx}/css/css.css">
	<style>
		.infinite-scroll-preloader{
     
    margin-left: -20px; }
 	</style>
</head>
<body>
<input type="hidden" value="${goodsPageInit.dn}" id="deviceNumber">
<input type="hidden" value="${goodsPageInit.sys}" id="appSys">
	    <div class="page-group">
		    <!-- tab1：diy -->
	        <div class="page page-current" id="index">
				<!-- 标题 -->
	           <header class="bar bar-nav noPadding">
	                <div class="swiper-container" data-space-between='10'>
					    <div class="swiper-wrapper">
					    </div>
					</div>
              </header>
		        <!-- 内容块 -->                  
	            <div class="content  infinite-scroll infinite-scroll-bottom" style="top:3rem;"  data-distance="0"></div>	            
				
	        </div>
		</div>
	<script type='text/javascript' src='//g.alicdn.com/sj/lib/zepto/zepto.min.js' charset='utf-8'></script>
	<script type='text/javascript' src='//g.alicdn.com/msui/sm/0.6.2/js/??sm.min.js,sm-extend.min.js' charset='utf-8'></script>
	<script>
	 	var swiperBox = $(".swiper-wrapper");
		$.ajax({
	    	url: '${ctx}/goods/adImgList.do',
	    	type: 'GET',
	    	dataType: 'json',
		    cache:false,
		    timeout:"60000",
		    success:function(data){
		    	var _data = data.data;
		    	var str="";	
	    		$.each(_data, function(index, val) {
	        		str="<div class='swiper-slide'><a class='external' href="+_data[index].url+"><img src="+_data[index].img	+"></a></div>"
	        		swiperBox.append(str);	        		
	       		});       	 
		    }
		});
	</script>
	<script>$.init()</script>
	<script>
	var  page=2;
		// 添加'refresh'监听器
		//$.initPullToRefresh(".pull-to-refresh-content");//在调用之前，初始化下拉刷新
		var  handle=function(e) {
			
			page=2;
		    // 模拟2s的加载过程
		    setTimeout(function() {
		    	var $content=$(".content");
		    	//var  $insert_after=$(".pull-to-refresh-layer");
				var  showdata =function(){
						    $.ajax({
						        	url: '${ctx}/home/home.do',
						        	type: 'POST',
						        	dataType: 'json',
						    	    cache:false,
						    	    timeout:"60000",
						    	    success:function(data){
						    	    	var str="";
					        			
							        	if(data.code==200){
											$(".diy-content").remove();
							        		$.each(data.data, function(index, val) {
								        		str="<div class='diy-content linec fl' onclick='sending("+data.data[index].id+")'><div class='diy-goods'><div class='diy-pic-box'><img class='fix-h' src='"+data.data[index].icoUrl+"'>							</div><div class='diy-details'><h2 class='clearfix fc5 line-h22'><i class='fl fs12 line-h24 fb'>¥</i><span class='fl fs17 fb'>"+data.data[index].nowPrice+"</span> <i class='fr fs11 fc2'>"+data.data[index].feightNote+"</i></h2><p class='fs12 fc-b ov-hidden'>"+data.data[index].name+"</p></div></div></div>"
								        		
												$content.append(str);	        		
								       		});
							        	}
							        	else{
							        			$.toast("没有更多...",1000);
							        		}
								    	    }
								        })};
				showdata();
		        // 加载完毕需要重置
		        //$.pullToRefreshDone('.pull-to-refresh-content');
		    }, 200);
		};
		//$(document).on('refresh', '.pull-to-refresh-content');
	
		$(document).ready(function() {

			handle();
			  var loading = false;
			  var lastIndex = 6;
			  // $.attachInfiniteScroll('.infinite-scroll');
		      // 注册'infinite'事件处理函数
		      $(document).on('infinite', '.infinite-scroll-bottom',function() {

		          // 如果正在加载，则退出
		          if (loading) return;

		          // 设置flag
		          loading = true;

		          // 模拟1s的加载过程
		          setTimeout(function() {
		              // 重置加载flag
		              loading = false;
		                var $content=$(".content");
				    	
						var  showdata =function(){
								    $.ajax({
								        	url: '${ctx}/home/home.do',
								        	type: 'POST',
								        	dataType: 'json',
								    	    cache:false,
								    	    timeout:"60000",
								    	    data:{page:page},//传入标记位tag
								    	    success:function(data){
								    	    	var str="";
							        			page++;
									        	if(data.code==200){
									        		$.each(data.data, function(index, val) {
										        		str="<div class='diy-content linec fl' onclick='sending("+data.data[index].id+")'><div class='diy-goods'><div class='diy-pic-box'><img class='fix-h' src="+data.data[index].icoUrl+">							</div><div class='diy-details'><h2 class='clearfix fc5 line-h22'><i class='fl fs12 line-h24 fb'>¥</i><span class='fl fs17 fb'>"+data.data[index].nowPrice+"</span> <i class='fr fs11 fc2'>"+data.data[index].feightNote+"</i></h2><p class='fs12 fc-b ov-hidden'>"+data.data[index].name+"</p></div></div></div>"
										        		$content.append(str);	        		
										       		});
									        	}
									        	else{
									        			$.toast("没有更多...",1000);
									        		}
										    	    }
										        })};
						showdata();		              
		                $.refreshScroller();
		          }, 700);
		      });
		      $(function() {
					$(".swiper-container").swiper({
						speed: 1000,
						spaceBetween: 10,
						observer:true,
						autoplay:3000
					});
				}); 
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
		
		var cn ="${goodsPageInit.cartNum}",on="${goodsPageInit.orderNum}",add="${goodsPageInit.add}";
		var load =cn+","+on+","+add;
		var data = {"load":load};
		connectNZOCJSBridge(function(bridge) {
			bridge.send(data, function(responseData) {})
		});
		
		function sending(id){
			data = {"click":id};
			connectNZOCJSBridge(function(bridge) {
				bridge.send(data, function(responseData) {})
			});
		}
	</script>
</body>
</html>