<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
    String path=request.getContextPath();
%>
<!DOCTYPE html>
<html lang="en">
<head>
	<meta charset="UTF-8">
	<meta name="viewport" content="width=device-width, initial-scale=1, minimum-scale=1, maximum-scale=1, user-scalable=no">
	<meta name="apple-mobile-web-app-capable" content="yes">
	<meta name="apple-mobile-web-app-status-bar-style" content="black">
	<link rel="stylesheet" href="<%=path %>/css/diy2_5/wsp-order-popup.css">
	<title>定制商品弹窗</title>
</head>
<body>
	<div class="popup">
		<div class="popup-container">
			<section class="pop-head clearfix">
				<div class="pop-image-box background-img">
					<img class="front-img" src="" alt="" id="user-image">
				</div>
				<div class="pop-good-desc">
					<h3 class="now-price"></h3>
					<!-- <p class="check-detail" style="padding:5px 0;font-size:15px;">查看商品详情</p> -->
				</div>
			</section>
			<section class="pop-body">
				<div class="activity"></div>
				<ul class="option-list"></ul>
				<div class="select-number clearfix">
					<span class="option-title">购买数量</span>
					<div class="number-edit">
						<i class="reduce-number disabled">-</i>
						<i class="show-number"><input class="input-number" type="tel" maxlength="3" value="1"></i>
						<i class="add-number">+</i>
					</div>
				</div>
			</section>
			<a href="#" class="btn-closepop" onclick="sending(11111)"></a>
		</div>
	</div>

	<script type='text/javascript' src='//g.alicdn.com/sj/lib/zepto/zepto.min.js' charset='utf-8'></script>
	<script src="//cdn.bootcss.com/fastclick/1.0.6/fastclick.js"></script>
	<script type="text/javascript">
	// js传值ios
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
	}
	function sendOC(sendObj) {
		console.log(sendObj);
		connectNZOCJSBridge(function(bridge) {
			bridge.send(sendObj, function(responseData) {})
		});
	}

	// 获取参数名
	function getQueryString(name) {
	    var reg = new RegExp('(^|&)' + name + '=([^&]*)(&|$)', 'i');
	    var r = window.location.search.substr(1).match(reg);
	    if (r != null) {
	        return unescape(r[2]);
	    }
	    return null;
	}

	$(function(){

		// 处理300ms点击延迟
		FastClick.attach(document.body);

		var textureArray = [];
		var textureObj = {};
		var sendObj = {};

		if(getQueryString('type') == 1){
			$('.check-detail, .front-img').hide();
		}

		function paddingData(json){
			$('.background-img').css({
				'background': 'url(' + json.data.pre_url + ') no-repeat',
				'background-size': 'cover'
			});
			$('.front-img').attr('src', json.data.pre_url);
			json.data.now_price && $('.now-price').html('￥<b>' + json.data.now_price.split('.')[0] + '</b>.' + json.data.now_price.split('.')[1]);
			$('.now-price').attr('data-orgprice', json.data.org_price);
			$('.activity').html(json.data.activity);
			var container = $('.option-list'),
				html = '';
			$.each(json.data.list, function(title, dataObj){
				var listHtml = '<li class="option-list-item">'+
								'<h3 class="option-title">' + dataObj.title + '</h3><p>';
				dataObj.list.forEach(function(data, index, dataArray){
					listHtml += '<span data-id="' + data.id + '">' + data.gname + '</span>';
				});
				listHtml += '</p></li>'
				html += listHtml;
			});
			container.append(html);
			if(getQueryString('dfTexture')){
				var dfTextureArray = getQueryString('dfTexture').split('_');
				for(var i = 0; i < dfTextureArray.length; i++){
					$('span[data-id="' + dfTextureArray[i] + '"]').addClass('active');
				}
			}
			// else {
			// 	var dfTextureArray = json.data.dfTexture.split('_');
			// 	for(var i = 0; i < dfTextureArray.length; i++){
			// 		$('span[data-id="' + dfTextureArray[i] + '"]').addClass('active');
			// 	}
			// }
			// $('.check-detail').click(function(e){
			// 	sending(json.data.type + ',' + json.data.id);
			// });
			// $('.option-list .active').length > 1 && checkStock();
		}

		function loadData(url){
			$.ajax({
				async: false,
				url: url,
				type: 'POST',
				dataType: 'json',
				timeout: 60000,
				success: function(json){
					if(json.code == 200 && json.data){
						textureObj = json.data.texture;
						json.data.texture.forEach(function(data, inde, dataArray){
							textureArray.push(data.texture_ids);
						});
						paddingData(json);
					}
				},
				error: function(xhr, status, error){
					console.log(error);
				}
			});
		}
		loadData('http://120.26.112.213:8082/diyMall/commodity/goodsProperty/' +  getQueryString('type') + '/' + getQueryString('id') + '.do');

		function sortNumber(a, b){
			return a - b;
		}

		// 判断ary1包含ary2，并返回ary1多出ary2的一个值
		function compareArray(ary1, ary2){
			if(!(ary1 instanceof Array) || !(ary2 instanceof Array)) return false;
			if(ary1.length < ary2.length) return false;
			ary1.sort(sortNumber);
			ary2.sort(sortNumber);
			var rev = NaN,
				index = NaN,
				flag = true,
				cuoweiflag = true;
			for(var i = 0; i < ary2.length; i++){
				if(ary1[i] != ary2[i] && ary1[i+1] != ary2[i]){
					flag = false;
				}
				if(ary1[i+1] == ary2[i] && cuoweiflag){
					cuoweiflag = false;
					index = i;
				}
				if(i == ary2.length-1 && ary1[i] == ary2[i]){
					index = i+1;
				}
			}
			if(flag){
				rev = ary1[index];
			}
			return rev;
		}

		// 检查库存
		function checkStock(){
			// console.log(textureArray.join('_').split('_').sort(sortNumber).length);
			if(textureArray.toString().indexOf('_') < 0) return false;
			var idArray = [];
			$('.option-list p .active').each(function(){
				idArray.push($(this).attr('data-id'));
			});
			if(idArray.length == 0){
				return false;
			}
			$('.option-list p span').not('.active').addClass('disabled');
			for(x in idArray){
				var cutArray = idArray.slice(0);
				cutArray.length > 1 && cutArray.splice(x, 1);		// 数组循环去掉一个元素
				textureObj.forEach(function(data, index, dataArray){
					var textureIdsArray = data.texture_ids.split('_');
					var rev = compareArray(textureIdsArray, cutArray);
					if(rev){
						$('span[data-id="' + rev + '"]').removeClass('disabled');
					}
				});
			}
		}

		function resetAll(){
			$('.option-list p span').removeClass('disabled');
		}

		function diffArray(ary1, ary2){
			if(!(ary1 instanceof Array) || !(ary2 instanceof Array)) return false;
			if(ary1.length < ary2.length) return false;
			ary1.sort(sortNumber);
			ary2.sort(sortNumber);
			var rev = [],
				aryFlag = [];
			for(var i = 0; i < ary1.length; i++){
				aryFlag[i] = 1;
				for(var j = 0; j < ary2.length; j++){
					if(ary2[j] == ary1[i]){
						aryFlag[i] = 0;
					}
				}
			}
			for(var k = 0; k < aryFlag.length; k++){
				if(aryFlag[k] == 1){
					rev.push(ary1[k]);
				}
			}
			if(rev.length + ary2.length == ary1.length){
				return rev;
			}
			return [];
		}
		
		function checkOthers(elem){
			if(elem.parents('.option-list-item').find('.active').length == 0){
				elem.siblings().addClass('disabled');
				elem.parents('.option-list-item').siblings().find('p span').removeClass('disabled');	// 权宜之计，仅适用两种材质选择
			}else{
				elem.parents('.option-list-item').siblings().find('p span').addClass('disabled');
			}
			var idArray = [];
			$('.option-list p .active').each(function(){
				idArray.push($(this).attr('data-id'));
			});
			textureObj.forEach(function(data, index, dataArray){
				var textureIdsArray = data.texture_ids.split('_');
				var rev = diffArray(textureIdsArray, idArray);
				if(rev.length > 0){
					for(i = 0; i < rev.length; i++){
						$('span[data-id="' + rev[i] + '"]').removeClass('disabled');
					}
				}
			});
		}

		$('.option-list p span').click(function(e){
			var that = $(this),
				idArray = [],
				length = $('.option-list-item').length;
			if(that.is('.disabled')){
				return false;
			}
			if(that.is('.active')){
				that.removeClass('active');
			}else {
				$(this).addClass('active').siblings().removeClass('active');
			}
			$('.option-list p .active').each(function(){
				idArray.push($(this).attr('data-id'));
			});
			if(0 == idArray.length){
				resetAll();
			}else if(length == idArray.length){
				checkStock();
				textureObj.forEach(function(data, index, dataArray){
					if(data.texture_ids.split('_').sort(sortNumber).toString() == idArray.sort(sortNumber).toString()){
						$('.background-img').css({
							'background': 'url(' + data.pre_url + ') no-repeat',
							'background-size': 'cover'
						});
						data.now_price && $('.now-price').html('￥<b>' + data.now_price.split('.')[0] + '</b>.' + data.now_price.split('.')[1]);
					}
				});
			}else{
				checkOthers(that);
			}
			collectInfo();
			sendOC(sendObj);
		});

		// 获取选择的内容数据
		function collectInfo(){
			var arrID = [],
				arrName = [];
			$('.option-list p .active').each(function(index, data){
				arrID.push($(data).attr('data-id'));
				arrName.push($(data).text());
			});
			sendObj.goodsId = getQueryString('id');
			sendObj.number = $('.input-number').val();
			sendObj.paramName = arrName.join('_');
			sendObj.combineId = arrID.join('_');
			sendObj.price = $('.now-price').html().replace(/￥/,'');
			sendObj.org_price = $('.now-price').attr('data-orgprice');
			sendObj.userimg_containerId = 'user-image';
		};

		;(function(){
			var addItem = $('.add-number'),
				reduceItem = $('.reduce-number'),
				showNumItem = $('.input-number');

			addItem.click(function(e){
				var number = parseInt(showNumItem.val());
				number++;
				number > 1 ? reduceItem.removeClass('disabled') : reduceItem.addClass('disabled');
				showNumItem.val(number);
				collectInfo();
				sendOC(sendObj);
			});

			reduceItem.click(function(e){
				var number = parseInt(showNumItem.val());
				if(number <= 1){
					showNumItem.html(1);
					return false;
				}
				number--;
				number > 1 ? reduceItem.removeClass('disabled') : reduceItem.addClass('disabled');
				showNumItem.val(number);
				collectInfo();
				sendOC(sendObj);
			});

			showNumItem.on('input blur', function(e){
				var type = e.type,
					number = parseInt($(this).val());
				number > 1 ? reduceItem.removeClass('disabled') : reduceItem.addClass('disabled');
				number == '' ? $(this).val(1) : '';
				if(type == 'blur' && ((/[^\d]/ig).test($(this).val()) || $(this).val().replace(/[^\d]/ig, '') == '')){
					$(this).val(1);
					reduceItem.addClass('disabled');
				}
				collectInfo();
				sendOC(sendObj);
			});
		}());

	});
	</script>
</body>
</html>