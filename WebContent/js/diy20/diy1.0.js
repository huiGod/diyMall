<script type="text/javascript">
		// 加载图片边框
		var imgNum = $(".fix-h").length;
		$(".fix-h").on("load",function(){if((--imgNum)==0){$(".fix-h").addClass("border-f5");}});

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
	    			var href ="'"+_data[index].url+"'";
	        		str='<div class="swiper-slide"><a onclick="sendingHref('+href+')"><img src="'+_data[index].img+'"></a></div>';
	        		swiperBox.append(str);	        		
	    		});       	 
		    }
		});
		$.init();
		$(function() {
			$(".swiper-container").swiper({
				speed: 300,
				spaceBetween: 10,
				autoplayDisableOnInteraction : false,
				observer:true,
			    autoplay:3000,
			});
		}); 
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
				bridge.send(data, function(responseData) {})
			});
		}
		function sendingHref(href){
			//href 为空时不与手机端交互
			if(href=='') return;	
			data = {"href":href};
			connectNZOCJSBridge(function(bridge) {
				bridge.send(data, function(responseData) {})
			});
		}
</script>