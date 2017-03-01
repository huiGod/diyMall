var activity = function(){

	var fn = {};

/* 处理页面脚本元素
==============================*/
	fn.execute = function(i, d) {
		$.each(d.className.match(/\_js\-[a-zA-Z]+/gi), function (k, v) {
			(fn[v.slice(4)] || function() {
				console.log('未找到对应'+ v +'事件，请检查"_js-"处的命名是否正确');
			}).call($(d));
		});
	};

	fn.index = function(){
		$('.see-sort').click(function(e){
			sending('3,http://120.26.112.213:8082/diyMall/activitePage/activitySort.do?userId=' + getQueryString('userId'));
		});
	}

	fn.goods = function(){
		var page = 0,
			loading = false,
		successFunc = function(json){
			if(json.code != 200 || json.data.length < 1){
				$('.load-more').html('<i class="left-grad-line"></i>到底啦<i class="right-grad-line"></i>');
				$(document).off('scroll');
				return false;
			}
			var goodsContainer = $('.goods-list'),
				html = '',
				sendData = '',
				sendObj = {};
			json.data.forEach(function(data, index, dataArray){
				if(data.type == 2){
					sendData = '7,' + data.goods;
					sendObj = data;
					sendObj.click = sendData;
					sendObj = JSON.stringify(sendObj);
					sendObj = '\'' + sendObj + '\'';
					html += '<li class="list-item">'+
							'		<div class="img-box">'+
							'			<img src="' + data.img + '" alt="">'+
							'		</div>'+
							'		<div class="desc-box">'+
							'			<h3 class="goods-name">' + data.name + '</h3>'+
							'			<p class="pink-btn"><a href="javascript:;" onclick=sendingObj(' + sendObj +') class="design-btn">去设计</a></p>'+
							'		</div>'+
							'</li>';
				}
			});
			goodsContainer.append(html);
			loading = false;
		}
		loadData('http://120.26.112.213:8082/diyMall/commodity/production.do?place=1&page=0', successFunc);

		$(document).scroll(function(e){
			if(loading) return;
			var pageHeight = $('.main-container').height(),
				windowHeight = $('body').height(),
				scrollTop = $('body').scrollTop();
			if(pageHeight - scrollTop <= windowHeight + 50){
				loading = true;
				page++;
				setTimeout(function(){
					loadMore('http://120.26.112.213:8082/diyMall/commodity/production.do?place=1&page='+page, successFunc);
				}, 10);
			}
		});
	};
	
	fn.sortPage = function(){
		var dataUrl = 'http://api.diy.51app.cn/diyMall/workActivity/workListForApp.do?userId=' + getQueryString('userId') + '&page=0';
			page = 0,
			loading = false,
		successFunc = function(json){
			if(json.code != 200 || json.data.other.length < 1){
				$('.load-more').html('<i class="left-grad-line"></i>到底啦<i class="right-grad-line"></i>');
				return false;
			}
			var listContainer = $('.sort-list'),
				html = '',
				sendData = '';
			json.data.other.forEach(function(data, index, dataArray){
				if(index == 0 && page == 0){
					$('.first-info a').attr({
						'data-id': data.id,
						'data-goodsId': data.goodsId,
						'data-userId': data.userId,
						onclick: 'sending("2,http://120.26.112.213:8082/diyMall/activitePage/activityVote.do?authorId=' + data.userId + '&userId=' + getQueryString('userId') + '&workId=' + data.id + '&app=1")'
					});
					$('.author').text(data.name);
					$('.autograph').text(data.cont);
					$('.sort-first-img img').attr('src', data.preImg[0]);
					$('.vote-number').text(data.heart);
				}else {
					sendData = '\'' + '2,http://120.26.112.213:8082/diyMall/activitePage/activityVote.do?authorId=' + data.userId + '&userId=' + getQueryString('userId') + '&workId=' + data.id + '&app=1' + '\'';
					html += '<li class="list-item">'+
							'	<a href="javascript:;" onclick="sending(' + sendData + ')">'+
							'		<div class="sort-item">'+
							'			<div class="sort-img">'+
							'				<img src="' + data.preImg[0].replace(/@pb@/,'@') + '" class="lazy" alt="">'+
							'			</div>'+
							'			<div class="sort-desc">'+
							'				<p class="list-author">'+
							'					<i class="left-riband"></i>'+
							'					<i class="right-riband"></i>'+
							'					<span class="ellipsis">' + data.name + '</span>'+
							'				</p>'+
							'				<p class="list-ballot"><img src="/diyMall/images/diy2_5/sort_04.png" alt="">&nbsp&nbsp<span>' + data.heart + '</span></p>'+
							'			</div>'+
							'		</div>'+
							'	</a>'+
							'</li>';
				}
			});
			listContainer.append(html);
			loading = false;
		}
		loadData(dataUrl, successFunc);

		$(document).scroll(function(e){
			if(loading) return;
			var pageHeight = $('.main-container').height(),
				windowHeight = $('body').height(),
				scrollTop = $('body').scrollTop();
			if(pageHeight - scrollTop <= windowHeight + 50){
				loading = true;
				page++;
				loadMore('http://api.diy.51app.cn/diyMall/workActivity/workListForApp.do?userId=' + getQueryString('userId') + '&page=' +page, successFunc);
			}
		});

		function updateData(){
			var successFunc = function(json){
				json.data.other.forEach(function(data, index, dataArray){
				if(index == 0){
					$('.vote-number').text(data.heart);
				}else {
					$('.list-ballot span').eq(index-1).text(data.heart);
				}
				});
			},
			loopLoad = function(){
				setTimeout(function(){
					loadData(dataUrl, successFunc);
					loopLoad();
				}, 3000);
			}
			loopLoad();
		}
		// updateData();
	};

	fn.worksdetail = function(){
		var imgArray = [];
		var that = $(this),
			url = '',
			imgIndex = 0,
			successFunc = function(json){
			if(json.code == 200 || json.data){
				imgArray = json.data.preImg.slice(0);	// 复制图片数组
				$('.vote-inner').attr({
					'data-id': json.data.id,
					'data-goodsId': json.data.goodsId,
					'data-userId': json.data.userId
				});
				$('.work-image').attr('src', json.data.preImg[0].replace(/@pb@/, "@"));
				$('#shareLogo').text(json.data.preImg[0].replace(/@pb@/, "@"));
				$('.work-name h3, #shareText').text(json.data.name || "未命名作品");
				$('.work-ballot span').text(json.data.heart);
				$('.work-desc span').text(json.data.cont || "这家伙很懒，什么都没写");
				json.data.isVote != 0 && $('.vote-btn').addClass('has-vote');
				json.data.preImg.forEach(function(data, index, dataArray){
					$('.swiper-wrapper').append('<div class="swiper-slide"><img src="'+data.replace(/@pb@/, "@")+'"/></div>');
				});
				var mySwiper = new Swiper('.swiper-container', {
					initialSlide: imgIndex,
					preloadImages: false,
					lazyLoading: true,
					lazyLoadingInPrevNext: true,
					spaceBetween : 20,
					pagination : '.swiper-pagination',
					paginationType : 'fraction',
					observer:true,    //修改swiper自己或子元素时，自动初始化swiper  
    				observeParents:true,   //修改swiper的父元素时，自动初始化swiper 
					onClick: function(swiper){
						$('.popLayer').removeClass("photoBrowserIn").addClass('photo-browser-out');
						// mySwiper.destroy(false,true);
						imgIndex = swiper.activeIndex;
						$('.work-image').attr('src', imgArray[imgIndex].replace(/@pb@/, "@"));
						// $(".swiper-wrapper,.swiper-pagination").empty();
					}
				});
			}else{
				console.log('该ID没有作品');
			}
		};
		if(getQueryString('app')){
			url = 'http://api.diy.51app.cn/diyMall/workActivity/workInforApp.do?userId=' + getQueryString('userId') + '&workId=' + getQueryString('workId');
		}else{
			url = 'http://api.diy.51app.cn/diyMall/workActivity/workInfo.do?openid=' + getQueryString('openid') + '&workId=' + getQueryString('workId')
		}
		loadData(url, successFunc);
		// 设置分享的链接
		$('#shareUrl').text('http://api.diy.51app.cn/diyMall/workActivity/initActivity/' + getQueryString('workId') + '/' + getQueryString('authorId') +'.do');
		// $('#shareUrl').text('http://api.diy.51app.cn/diyMall/activitePage/activityVote.do?workId=' + getQueryString('workId'));
		that.find('.work-image').on('click', function(e){
			$('.swiper-slide').each(function(index, elem){
				if($(this).find('img')[0].complete && !CheckImgExists($(this).find('img').attr('src'))){
					$(this).remove();
				}
			});
			$(".popLayer").removeClass("hide photo-browser-out").addClass("photoBrowserIn");
		});
	};

	fn.voteworks = function(){
		var that = $(this),
			voteNumber = parseInt($('.work-ballot span').text()),
			successFunc = function(json){
				if(json.code == 300){
					toastWarning(json.message);
				}else if(json.code == 200){
					voteNumber++;
					$('.work-ballot span').text(voteNumber);
					that.addClass('has-vote');
					toastWarning('点赞成功！');
				}else{
					toastWarning(json.message);
				}
			}
		that.click(function(e){
			var url = '';
			if(that.is('.has-vote')){
				toastWarning('您已给该作品投过票');
				return false;
			}
			if(getQueryString('app')){
				url = 'http://api.diy.51app.cn/diyMall/workActivity/userVoteforApp.do?userId=' + getQueryString('userId') + '&workId=' + getQueryString('workId');
			}else {
				url = 'http://api.diy.51app.cn/diyMall/workActivity/userVote.do?openid=' + getQueryString('openid') + '&workId=' + getQueryString('workId');
			}
			$.ajax({
				async: false,
				url: url,
				data: '',
				dataType: 'json',
				type: 'POST',
				timeout: 60000,
				success: successFunc,
				error: function(xhr, status, error){
					console.log(status);
				}
			});
		});
	};

	fn.jointype = function(){
		$(this).on('click', function(e){
			if(getQueryString('app')){
				sending('3,http://api.diy.51app.cn/diyMall/activitePage/activityGoods.do');
			}else {
				$(this).attr('href', 'http://api.diy.51app.cn/diyMall/activitePage/activityDownload.do');
			}
		});
	};

	fn.coupons = function(){
		var url = 'http://api.diy.51app.cn/diyMall/coupon2/centerForId.do?userId=' + getQueryString('userId'),
			srcNormal = '../images/diy2_5/getcoupons.jpg',
			srcGetted = '../images/diy2_5/gettedcoupons.jpg',
			successFunc = function(json){
				var container = $('.coupons-list'),
					html = '';
				if(json.code == 200 && (json.data.length > 0 || json.data)){
					json.data.forEach(function(data, index, dataArray){
						html += '<li class="coupons-card clearfix ' + (data.isGet == 0 ? 'get' : 'use') + '">'+
								'	<div class="coupons-price">'+
								'		<h3 class="coupons-orgprice ellipsis">￥<span>' + (data.desPrice ? data.desPrice.split('.')[0] : '--') + '</span></h3>'+
								'		<p class="coupons-about">' + data.title + '</p>'+
								'	</div>'+
								'	<div class="coupons-desc">'+
								'		<h3 class="coupons-title">' + data.storeName + '</h3>'+
								'		<p class="coupons-valid">有效日期&nbsp<span>' + data.valid + '</span></p>'+
								'	</div>'+
								'	<div class="coupons-img" data-store-id="' + data.store_id + '" data-id="http://api.diy.51app.cn/diyMall/coupon2/getCenterCouponForId.do?userId=' + getQueryString('userId') + '&valid=' + data.valid + '&id=' + data.id + '">'+
								'		<img src="' + (data.isGet == 0 ? srcNormal : srcGetted) + '" alt="">'+
								'	</div>'+
								'</li>';
					});
				}
				container.append(html);
				$('.use .coupons-img').click(function(e){
					sending('3,http://api.diy.51app.cn/diyMall/index/storeGoods.do?id=' + $(this).attr('data-store-id'));
				});
			};
		loadData(url, successFunc);

		$('.act-btn').click(function(e){
			var val = $('.enter-coupons input').val(),
				url = 'http://api.diy.51app.cn/diyMall/coupon2/codeCouponForId.do?userId=' + getQueryString('userId') + '&code=' + val;
			var successFunc = function(data) {
				if (data.code == 300) {
					toastWarning("请输入正确的激活码");
				} else if (data.code == 200) {
					toastWarning("激活成功！");
				}else if(data.code == 301){
					toastWarning("激活码已失效！");
				}else{
					toastWarning("激活失败！");
				}
			}
			if (val == '') {
				toastWarning("请输入激活码");
			} else {
				loadData(url, successFunc);
			}
		});

		$('.get .coupons-img').click(function(e){
			var that = $(this),
				url = that.attr('data-id'),
				successFunc = function(json){
					if(json.code == 200){
						that.find('img').attr('src', srcGetted);
						that.parent().removeClass('get').addClass('use');
						that.off('click');
						that.on('click', function(e){
							sending('3,http://api.diy.51app.cn/diyMall/index/storeGoods.do?id=' + that.attr('data-store-id'));
						});
						toastWarning("领取成功！");
					}
				};
			loadData(url, successFunc);
		});
	};

	function loadData(url, successFunc){
		$.ajax({
			async: false,
			url: url,
			type: 'GET',
			dataType: 'json',
			timeout: 60000,
			success: successFunc,
			error: function(xhr, status, error){
				console.log(status);
			}
		});
	};

	function loadMore(url, successFunc){
		$.ajax({
			async: false,
			url: url,
			type: 'POST',
			dataType: 'json',
			timeout: 60000,
			success: successFunc,
			error: function(xhr, status, error){
				console.log(error);
			}
		});
	};

	function toastWarning(str){
		var body = $('body'),
			handle,
			bubble = $('<div class="warn-bubble">' +
						'<span></span>' +
						'</div>'),
			str = str;
		bubble.detach().find('span').text(str).parent().appendTo(body);
		clearTimeout(handle);
		handle = setTimeout(function(){
			bubble.detach();
		}, 1500);
	};

/* 处理300ms点击延迟
==============================*/
	FastClick.attach(document.body);
	$('body [class*=_js-]').each(fn.execute);
}

$(activity);

/*   获取url参数值
==============================*/
function getQueryString(name) {
    var reg = new RegExp('(^|&)' + name + '=([^&]*)(&|$)', 'i');
    var r = window.location.search.substr(1).match(reg);
    if (r != null) {
        return unescape(r[2]);
    }
    return null;
}
/*    判断图片资源是否存在
==============================*/
function CheckImgExists(imgurl) {  
	var ImgObj = new Image();
	ImgObj.src = imgurl;
	if (ImgObj.fileSize > 0 || (ImgObj.width > 0 && ImgObj.height > 0)) {
		return true;
	} else {
		return false;
	}
}
/*   js传值ios  
==============================*/
function connectNZOCJSBridge(callback) {
	if (window.NZOCJSBridge) {
		callback(NZOCJSBridge)
	} else {
		document.addEventListener('NZOCJSBridgeReady', function() {
			callback(NZOCJSBridge)
		}, false)
	}
}
function sending(id) {
	console.log(id);
	data = {
		"click": id
	};
	connectNZOCJSBridge(function(bridge) {
		bridge.send(data, function(responseData) {})
	});
	try{
    	uqWyp.notifyInteraction(id);
    }catch(e){
    	console.log('uqWyp no load');
    }
}
function sendOC(sendObj) {
	console.log(sendObj);
	connectNZOCJSBridge(function(bridge) {
		bridge.send(sendObj, function(responseData) {})
	});
}
function sendingObj(sendObj){
	sendObj = JSON.parse(sendObj);
	// console.log(sendObj);
	connectNZOCJSBridge(function(bridge) {
	    bridge.send(sendObj, function(responseData) {})
	});
	sendObj = JSON.stringify(sendObj);
	// console.log(sendObj);
	try{
		uqWyp.notifyInteraction(sendObj);
	}catch(e){
    	console.log('uqWyp no load');
    }	// 把所有数据传给原生，其中需要加上click
}