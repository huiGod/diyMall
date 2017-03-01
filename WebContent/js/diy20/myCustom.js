/* 2.0改版
============================================================*/
var myCustom = function() {
	var fn = {},
		loadData = function(url, async, successFunc) {
			$.ajax({
				url: url,
				type: 'POST',
				timeout: 60000,
				async: async,
				dataType: 'json',
				data: {},
				success: successFunc
			});
		},
		execute = function(i, d) {
			$.each(d.className.match(/\_js\-[a-zA-Z]+/g), function(k, v) {
				(fn[v.slice(4)] || function() {
					console.log("未能找到" + v + '事件');
				}).call(d);
			});
		},
		toastWarning = function() {
			var fn = {},
				body = $('body'),
				handle, bubble = $('<div class="warn-bubble">' +
					'<span>优品汇</span>' +
					'</div>');

			fn.say = function(str) {
				var str = arguments[0] ? arguments[0] : "请输入激活码";
				bubble.detach().find('span').text(str).parent().appendTo(body);

				clearTimeout(handle);
				handle = setTimeout(function() {
					bubble.detach();
				}, 1700);
			};
			return fn;
		}();

	/*  溢出滚动  */
	fn.touchScroll = function(parentEle) {
		var el = this,
			parentEle = $(parentEle);
		el.addEventListener('touchstart', function() {
			var top = el.scrollTop,
				totalScroll = el.scrollHeight,
				currentScroll = top + el.offsetHeight;
			if (top === 0) {
				el.scrollTop = 1;
			} else if ((currentScroll + 1) === totalScroll) {
				el.scrollTop = top - 1;
			} else if (currentScroll == totalScroll) {
				el.scrollTop = top - 1;
			} else if ((currentScroll - 1) == totalScroll) {
				el.scrollTop = top - 1;
			}
		})
		el.addEventListener('touchmove', function(e) {
			if (el.offsetHeight < el.scrollHeight)
				e._isScroller = true;
		})
		parentEle.on('touchmove', function(e) {
			if (!e._isScroller) {
				e.preventDefault();
			}
		})
	};
	/* 分类页  */
	fn.classify = function() {
		var url = $('.page-content').attr('data-url'),
			json = {},
			successFunc = function(data) {
				if (data.code == 200) {
					var dataD = data.data,
						dataDd = dataD[0].goods,
						navlist = '',
						goodslist = '';
					$.each(dataD, function(i, d) {
						navlist += '<li>' + dataD[i].sortName + '</li>';
					});
					goodslist = eachData(dataDd);
					$('.aside-nav ul').append(navlist);
					$('.goods-list-area').append('<section class="section" data-cIndex="0">' + goodslist + '</section>');
					$('.aside-nav ul li').eq(0).addClass('active');
				}
			};

		loadData(url, true, successFunc);
		$(document).on("tap", ".aside-nav ul li", function() {
			var clickIndex = $(this).index(),
				isExist = false,
				successFunc = function(data) {
					if (data.code == 200) {
						var dataD = data.data,
							dataDd = dataD[clickIndex].goods,
							goodslist = '';
						goodslist = eachData(dataDd);
						$('.goods-list-area').append('<section class="section" data-cIndex="' + clickIndex + '">' + goodslist + '</section>');
					}
				},
				handle = function(i, d) {
					var temp = $(this).attr('data-cindex');
					if (temp == clickIndex) {
						$(this).show().siblings(".section").hide();
						isExist = true;
						return true;
					}
				};

			$(this).addClass('active').siblings().removeClass('active');
			$.each($('.section'), handle);
			if (!isExist) {
				$('.section').hide();
				loadData(url, true, successFunc);
			}
		});

		function eachData(dataDd) {
			var goodslist = '';
			$.each(dataDd, function(i, d) {
				goodslist += '<div class="single-goods" onclick="sending(' + dataDd[i].goodsId + ')">' +
					'<div class="pic-box">' +
					'<img src="' + dataDd[i].icoUrl + '">' +
					'</div>' +
					'<p class="info-box">' +
					'<i class="price">￥<i class="price-num">' + dataDd[i].nowPrice + '</i></i> ' +
					'<span class="sold-info">已售<em class="number">' + dataDd[i].sell + '</em>件</span>' +
					'</p>' +
					'<p class="goods-name">' + dataDd[i].name + '</p>' +
					'</div>';
			});
			return goodslist;
		}
	};
	/* 发现列表 */
	fn.findList = function() {
		var url = $('.page-content').attr('data-url'),
			successFunc = function(data) {
				if (data.code == 200) {
					var dataArr = data.data,
						str1 = '',
						str2 = '',
						str3 = '';
					$.each(dataArr, function(i, d) {
						(dataArr[i].type == 1) &&
						(str1 += '<li onclick="sending(\'' + dataArr[i].url + '\')""><img src="' + dataArr[i].img + '"><h3>' + dataArr[i].title + '</h3></li>');
						(dataArr[i].type == 2) &&
						(str2 += '<li class="clearfix" onclick="sending(\'' + dataArr[i].url + '\')">' +
							'<aside class="fl"><img src="' + dataArr[i].img + '"></aside>' +
							'<article class="fr articleAbstract">' +
							'<h3>' + dataArr[i].title + '</h3>' +
							'<p>' + dataArr[i].count + '</p>' +
							'<p>编辑：<span>' + dataArr[i].author + '</span></p>' +
							'</article>' +
							'</li>');
						(dataArr[i].type == 3) &&
						(str3 += '<img onclick="sending(\'' + dataArr[i].url + '\')"" src="' + dataArr[i].img + '">');
					});
					$('.top-nav ul').append(str1);
					$('.articleList ul').append(str2);
					$('.actBanner').append(str3);
				}
			};

		loadData(url, true, successFunc);
	};
	/* 发现文章 */
	fn.findArticle = function() {
		var url = $('.page-content').attr('data-url'),
			successFunc = function(data) {
				if (data.code == 200) {
					var header = '',
						article = '',
						dt = data.data;
					$.each(dt, function(i, d) {
						var text = '',
							pic = '',
							link = '';
						(dt[i].type == 4) &&
						(header = '<aside class="fl">' +
							'<img src="' + dt[i].img + '">' +
							'<h4>' + dt[i].author + '</h4>' +
							'</aside>' +
							'<aside class="fr right-art">' +
							'<h3>' + dt[i].title + '</h3>' +
							'<p><span class="time">' + dt[i].ctime + '</span> <i>' + dt[i].cview + '</i>阅读</p>' +
							'</aside>');
						(dt[i].type == 1) && (text = '<p>' + dt[i].title + '</p>');
						(dt[i].type == 2) && (pic = '<img src="' + dt[i].img + '">');
						(dt[i].type == 3) && (link = '<a href="' + dt[i].url + '"><img src="' + dt[i].img + '"></a>');
						article += (text + pic + link);
					});
					$('.article-head').append(header);
					$('.article-content').append(article);
				}
			};

		loadData(url, true, successFunc);
	};
	/* 商品底部 */
	fn.goods = function() {
		var url = $('.page-content').attr('data-url'),
			section1 = '',
			section2 = '',
			section3 = '',
			section4 = '',
			successFunc = function(data) {
				if (data.code == 200) {
					var dt = data.data;
					section1 = '<h3 class="swicthTab" onclick="sending(22222)">' + dt.title + '<i class="right-arrow"></i></h3>' +
						'<p class="activity">' + dt.activity + '</p>' +
						'<p class="goodsDetails">' +
						'<span class="now-price"><i class="fs12">¥&nbsp;</i><span class="now-price-num" id="nowPrice">' + dt.now_price + '</span></span>' +
						'<span class="ori-price">¥<span id="original_price">' + dt.org_price + '</span></span>' +
						'<span class="selled">已售<i class="selled-num">' + dt.sell + '</i>件</span>' +
						'<span class="feetips">' + dt.transportfee + '</span>' +
						'</p>';
					section2 = '<span>已选&nbsp;</span>' +
						'<span class="selectedParam" id="selectedParam">' + dt.paramList + '</span>' +
						'<i class="right-arrow"></i>';
					section3 = '<span class="bgcheck padd">正品保证</span>' +
						'<span class="bgcheck padd">支持七天无理由退换货</span>';
					$.each(dt.recommend, function(i, d) {
						var temp = dt.recommend;
						section4 += '<li class="recommentLi" onclick="sending(' + temp[i].id + ')">' +
							'<div class="goodPicBox"><img src="' + temp[i].previewImgUrl + '"></div>' +
							'<div class="describ">' +
							'<h3 class="goodDescribH3">' + temp[i].title + ' </h3>' +
							'<p class="price">¥<span class="price-num">' + temp[i].now_price + '</span></p>' +
							'</div>' +
							'</li>';
					});
					$('.goodsInfo-desc').append(section1);
					$('.goodsInfo-option').append(section2).attr("onclick", "sending(33333)");
					$('.tips').append(section3);
					$('.recommentForU ul').append(section4);
				}
			};

		loadData(url, true, successFunc);
	};
	/* 商品弹窗 */
	fn.goodsPop = function() {
		var url = $('.page-content').attr('data-url'),
			sendObj = {},
			$num = $(".input-num"),
			successFunc = function(data) {
				if (data.code == 200) {
					var dt = data.data,
						deposit = '',
						defHead = '',
						list = '';
					defHead = '<div class="pop-imgBox" data-size="' + dt.save_size + '" data-gType="' + dt.type + '" data-storeId="' + dt.storeId + '" data-goodsId="' + dt.id + '"><img id="gImg" src="' + dt.pre_url + '"></div>' +
						'<div class="pop-info-Box">' +
						'<h2 class="fs20 fc5">¥ <em id="gNowPrice" data-oriPrice="' + dt.org_price + '">' + dt.now_price + '</em></h2>' +
						'<a href="#" onclick="sending(10000)" id="goDetail">查看商品详情</a>' +
						'</div>';
					for (var i in dt) {
						if (i.match(/list[A|B|C]/g)) {
							var subList = '';
							$.each(dt[i].list, function(k, v) {
								var d = dt[i].list;
								subList += '<span class="' + i + '" id="' + d[k].id + '">' + d[k].gname + '</span>';
							});
							list += '<li data-list="' + i + '">' +
								'<h3 class="fs14 pop-cont-h"><span>' + dt[i].title + '</span></h3>' +
								'<p class="fs12 pop-opts ">' + subList +
								'</p>' +
								'</li>';
						}
					}
					$.each(dt.texture, function(i, d) {
						deposit += '<div style="display:none" class="' + dt.texture[i].texture_ids + '">' +
							'<span class="org_price">' + dt.texture[i].org_price + '</span>' +
							'<span class="now_price">' + dt.texture[i].now_price + '</span>' +
							'<span class="pre_url">' + dt.texture[i].pre_url + '</span>' +
							'<span class="box_url">' + dt.texture[i].box_url + '</span>' +
							'</div>';
					});
					$('.data-deposit').append(deposit);
					$('.pop-head-content').prepend(defHead);
					$('.pop-selects ul').append(list);
					$('.remindActive').append('<span>' + dt.activity + '</span>');
					// 脚本处理页面
					if (dt.type == 1) {
						$('#goDetail,.remindActive').css("display", "none");
					}
					$('.pop-opts span:first-child').addClass('pop-btn-selected');
				}
			};

		loadData(url, false, successFunc);
		$('.add,.reduce').on('tap', function(e) {
			var target = e.target,
				$addBtn = $(".add"),
				$rdcBtn = $(".reduce"),
				num = parseInt($num.val());
			if ($(target).hasClass('add')) {
				(num == 1) ? ($rdcBtn.removeClass('aaa2'), $num.val(num + 1)) : $num.val(num + 1);
			} else {
				(num == 2) ? ($rdcBtn.addClass('aaa2'), $num.val(num - 1)) : ((num == 1) ? (i = 1) : ($num.val(num - 1))); 
			}
			collectInfo();
			sendOC(sendObj);
		});
		// 输入事件 
		$num.on("input blur", function(e) {
			var _type = e.type;
			var num = parseInt($num.val());
			var val_length = $num.val().length;
			if (_type == "input") {
				(val_length == 0) ? ($rdcBtn.addClass('aaa2')) : ((num == 0) ? ($num.val("1"), $rdcBtn.addClass('aaa2')) : ((num == 1) ? ($rdcBtn.addClass('aaa2')) : (k = 1)));
			} else if (_type == "blur") {
				(val_length == 0) ? ($num.val("1"), $rdcBtn.addClass('aaa2')) : ((num == 0) ? ($num.val("1"), $rdcBtn.addClass('aaa2')) : ((num == 1) ? ($rdcBtn.addClass('aaa2')) : (k = 1)));
			}
			collectInfo();
			sendOC(sendObj);
		});
		// 选择材质
		$(".pop-selects").on("tap", ".pop-selects p span", function() {
			$(this).addClass('pop-btn-selected').siblings().removeClass('pop-btn-selected');
			updatePreview();
			collectInfo();
			sendOC(sendObj);
		});

		// 修改预览图
		function updatePreview() {
			var id = '.' + getCombineId(),
				$id = $(id);
			$('#gImg').attr('src', $id.find('.pre_url').text());
			$('#gNowPrice').text($id.find('.now_price').text())
				.attr('data-oriprice', $id.find('.org_price').text());
		}
		// 获取联合id
		function getCombineId(){
			var checked = $(".pop-btn-selected"),
				CheckId = '',
				id;
			$.each(checked, function(i) {
				CheckId += checked.eq(i).attr("id") + "_";
			});
			id = '' + CheckId.substring(0, CheckId.length - 1);
			return id;
		}
		// 获取页面值
		function collectInfo() {
			var arr = [];
			$(".pop-btn-selected").each(function(i, d) {
				arr.push($(d).text());
			});
			sendObj.goodsId = $('.pop-imgBox').attr('data-goodsId');
			sendObj.price = $('#gNowPrice').text();
			sendObj.org_price = $('#gNowPrice').attr('data-oriPrice');
			sendObj.num = $('.input-num').val();
			sendObj.storeId = $('.pop-imgBox').attr('data-storeId');
			sendObj.gType = $('.pop-imgBox').attr('data-gType');
			sendObj.paramName = arr;
			sendObj.combineId = getCombineId();
			sendObj.save_size = $('.pop-imgBox').attr('data-size');
		}
		collectInfo();
	};
	/* 优惠券 */
	fn.coupons = function() {
		var url = $('.getCoupons').attr('data-url'),
			successFunc = function(data) {
				if (data.code == 200) {
					var dt = data.data,
						couponList = '';
					$.each(dt, function(i, d) {
						var coupon = '';
						coupon = '<section class="coupon active" data-id="' + dt[i].getCenterCoupon + '">' +
							'<div class="picBox">' +
							'<img src="' + dt[i].couponImg + '">' +
							'</div>' +
							'<div class="couponInfo">' +
							'<h4>' + dt[i].title + '</h4>' +
							'<p>' + dt[i].about + '</p>' +
							'<p>有效期至' + dt[i].valid + '</p>' +
							'</div>' +
							'<div class="getBtn">' +
							'<h3>领取</h3>' +
							'<p>还有<span>' + dt[i].count + '</span>张</p>' +
							'</div>' +
							'<div class="left-saw">' +
							'<span></span><span></span><span></span><span></span><span></span><span></span><span></span><span></span><span></span><span></span>' +
							'</div>' +
							'<div class="right-saw">' +
							'<span></span><span></span><span></span><span></span><span></span><span></span><span></span><span></span><span></span><span></span>' +
							'</div>' +
							'</section>';
						if (dt[i].isGet == 1) {
							couponList += (coupon.replace(/coupon active/, 'coupon')).replace(/领取/, '已领取');
						} else if (dt[i].count == 0) {
							var pathname = document.location.pathname,
							    path = pathname.replace(/\/coupon2\/getCenterPage\.do/,''),
							    img = '<img class=img-over  src="'+path+'/images/diy20/getover.png">';
							couponList += (coupon.replace(/coupon active/, 'coupon')).replace(/<div class="getBtn">/, '<div class="getBtn">' + img);;
						} else {
							couponList += coupon;
						}
					});
					$('.getCoupons').append(couponList);
				}
			};

		loadData(url, false, successFunc);

		$('.enableBtn').click(function() {
			var val = $('.enableCoupons input').val();
			var url = $('.enableBtn').attr('data-url') + '&code=' + val;
			var successFunc = function(data) {
				if (data.code == 300) {
					toastWarning.say("请输入正确的激活码");
				} else if (data.code == 200) {
					toastWarning.say("激活成功！");
				}else if(data.code == 301){
					toastWarning.say("激活码已失效！");
				}else{
					toastWarning.say("激活失败！");
				}
			}
			if (val == '') {
				toastWarning.say("请输入激活码");
			} else {
				loadData(url, false, successFunc);
			}
		});

		$(document).on('tap', '.active .getBtn', function() {
			var url = $(this).closest('.active').attr('data-id');
			var num = parseInt($(this).find('span').text());
			var successFunc = function(data) {
				if (data.code == 200) {
					$('.active .getBtn').find('h3').text("已领取").closest('.getBtn').find('span').text(num - 1).closest('.coupon').removeClass('active');
					toastWarning.say("领取成功！");
				}
			};
			loadData(url, false, successFunc);
		});
	};
	$('[class*=_js-]:not(.haveDone)').each(execute).addClass('haveDone');
}
document.addEventListener('DOMContentLoaded', myCustom);
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
		// console.log(1)
		data = {
			"click": id
		};
		connectNZOCJSBridge(function(bridge) {
			bridge.send(data, function(responseData) {})
		});
	}

	function sendOC(sendObj) {
		// console.log(sendObj)
		connectNZOCJSBridge(function(bridge) {
			bridge.send(sendObj, function(responseData) {})
		});
	}