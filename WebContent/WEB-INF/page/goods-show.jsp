<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<c:set var="gbp" value="${goodsBuyParam}" />
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0" name="viewport">
    <meta name="apple-mobile-web-app-capable" content="yes">
    <meta name="apple-mobile-web-app-status-bar-style" content="black">
    <meta name="apple-mobile-web-app-capable" content="yes">    
    <link rel="stylesheet" href="${ctx}/css/css.css">
    <title>51app</title>
</head>
<body class="bodyBg">
	<!-- 商品信息 -->
	<div class="pop-head">
		<div class="pop-head-content">
			<div class="pop-imgBox"><img id="gImg" src=" ${gbp.img}" alt=""></div>
			<div class="pop-info-Box">
				<h2 class="fs20 fc5" >¥ <em id="gNowPrice">${gbp.nowPrice}</em></h2>
			</div>
			<div class="closeSelect" ><img style="width:100%;" src="${ctx}/images/closeSelect.png" alt=""></div>
		</div>		
	</div>
	<!-- 属性选择 -->
	<div id="pop-GoodsContent">
		<div class="pop-selects fc00">
			<ul>
				<c:forEach items="${gbp.listA}" var="la">
					<li>
						<h3 class="fs14 pop-cont-h"><span>${la.title}</span></h3>
						<p class="fs12 pop-opts ">
							<c:forEach items="${la.list}" var="la_l">
								<span onclick="resetImgPrice('${la_l.nowPrice}','${la_l.img}','${la_l.mUrl}')">${la_l.name}</span>
							</c:forEach>
						</p>
					</li>
				</c:forEach>
				<c:forEach items="${gbp.listB}" var="lb">
					<li>
						<h3 class="fs14 pop-cont-h"><span>${lb.title}</span></h3>
						<p class="fs12 pop-opts ">
							<c:forEach items="${lb.list}" var="lb_l">
								<span >${lb_l}</span>
							</c:forEach>
						</p>
					</li>
				</c:forEach>
			</ul>
		</div>		
		<!-- 数量选择 -->
		<div class="clearfix pop-selNum">
			<span class="fl fc00" id="goumaiNum">购买数量</span> 
			<div class="rdc-add clearfix fr rdc-add2">
				<span class="iconfont com-r-a icon-jianhao1 aaa2 reduce"></span>
				<i class="numbers"><input type="tel" maxlength="3" value="1" class="input-num"/></i>  
				<span class="iconfont com-r-a icon-jiahao add"></span>
			</div>
			<button style="display:none;color:#fff;" id="closeKeyboardBtn">close</button>
		</div>
	</div>

<script src="${ctx}/js/lib/zepto.min.js"></script>
<script src="${ctx}/js/lib/touch.js"></script>
<script>
var id="${gbp.id}";
var gtId ="${gbp.gtId}";
var storeId ="${gbp.storeId}";
var price ="${gbp.nowPrice}";
var gType ="${gbp.goodsType}";

$(document).ready(function() {
	var arr=["","","1",id,price,storeId,gType],
		arrStr="",
		$addBtn=$(".add"),
		$rdcBtn=$(".reduce"),
		$num=$(".input-num"),
		$nowPrice=$("#gNowPrice");

		function closeKeyboard(){
			$(".pop-btn-selected").eq(0).trigger("click");
		}
		// 购买数量
		$addBtn.on("tap",function(){
			var num=parseInt($num.val());
			(num==1)?($rdcBtn.removeClass('aaa2'),$num.val(num+1)):$num.val(num+1);
			var numArr =++num;
			arr[2]=numArr.toString();
			//arr[2]=++num;
			var arr1="";
			if(arr[1]!=""){
				arr1=","+arr[1];
			}
			arrStr=arr[3]+","+arr[6]+","+arr[4]+","+arr[5]+","+arr[2]+","+arr[0]+arr1;
			sendOC();
		});
		$rdcBtn.on("tap",function(){
			var num=parseInt($num.val());
			(num==2)?($rdcBtn.addClass('aaa2'),$num.val(num-1),arr[2]=--num):((num==1)?(i=1,arr[2]=1):($num.val(num-1),arr[2]=--num));	
			var arr1="";
			if(arr[1]!=""){
				arr1=","+arr[1];
			}
			arrStr=arr[3]+","+arr[6]+","+arr[4]+","+arr[5]+","+arr[2]+","+arr[0]+arr1;
			sendOC();
		});
		// 输入事件，发送一次数据 $num.val("1"),
		$num.on("input blur",function(e){
			var _type = e.type;
			var num=parseInt($num.val());
			var val_length = $num.val().length;
			if(_type == "input"){
				(val_length==0)?($rdcBtn.addClass('aaa2'),arr[2]=1):((num==0)?($num.val("1"),$rdcBtn.addClass('aaa2'),arr[2]=1):((num==1)?($rdcBtn.addClass('aaa2'),arr[2]=num):(arr[2]=num)));
			}else if(_type == "blur"){
				(val_length==0)?($num.val("1"),$rdcBtn.addClass('aaa2'),arr[2]=1):((num==0)?($num.val("1"),$rdcBtn.addClass('aaa2'),arr[2]=1):((num==1)?($rdcBtn.addClass('aaa2'),arr[2]=num):(arr[2]=num)));
			}			
			var arr1="";
			if(arr[1]!=""){
				arr1=","+arr[1];
			}
			arrStr=arr[3]+","+arr[6]+","+arr[4]+","+arr[5]+","+arr[2]+","+arr[0]+arr1;
			sendOC();
		});
		$(".pop-selects p span").on("tap",function(){
			arr[4]=$nowPrice.text();
			$(this).addClass('pop-btn-selected').siblings().removeClass('pop-btn-selected');
			$(".pop-btn-selected").each(handle);
			var arr1="";
			if(arr[1]!=""){
				arr1=","+arr[1];
			}
			arrStr=arr[3]+","+arr[6]+","+arr[4]+","+arr[5]+","+arr[2]+","+arr[0]+arr1;
			sendOC();
		});	
		var handle=function(i,d){
			var that=$(d);
			var text=that.text();
			arr[i]=text;
			var arr1="";
			if(arr[1]!=""){
				arr1=","+arr[1];
			}
			arrStr=arr[3]+","+arr[6]+","+arr[4]+","+arr[5]+","+arr[2]+","+arr[0]+arr1;
		}
		$(".pop-selects p span:first-child").addClass('pop-btn-selected');
		$(".pop-btn-selected").each(handle);
		
		
		/*
		加入购物车或立即购买 执行下面函数
		*/
		function connectNZOCJSBridge(callback) {
			if (window.NZOCJSBridge) {
				callback(NZOCJSBridge)
			} else {
				document.addEventListener('NZOCJSBridgeReady', function() {
					callback(NZOCJSBridge)
				}, false)
			}
		}
		connectNZOCJSBridge(function(bridge) {
			bridge.send(arrStr, function(responseData) {})
		});
		function sendOC(){
			connectNZOCJSBridge(function(bridge) {
				bridge.send(arrStr, function(responseData) {})
			});
		}

		// 启用内容区溢出滚动
		var contentObj = $("#pop-GoodsContent")[0];
		overscroll(contentObj);
		
});
	function overscroll(el) {
		el.addEventListener('touchstart', function() {
		    var top = el.scrollTop,
		        totalScroll = el.scrollHeight,
		        currentScroll = top + el.offsetHeight;
		    if(top === 0) {
		      el.scrollTop = 1
		    } else if(currentScroll === totalScroll) {
		      el.scrollTop = top - 1
		    }
		})
		el.addEventListener('touchmove', function(e) {
		    if(el.offsetHeight < el.scrollHeight)
		      e._isScroller = true;
		})
		$(document).on('touchmove', function(e) {
		  if(!e._isScroller) { e.preventDefault(); }
		})
	}
	
	function connectNZOCJSBridge(callback) {
		if (window.NZOCJSBridge) {
			callback(NZOCJSBridge)
		} else {
			document.addEventListener('NZOCJSBridgeReady', function() {
				callback(NZOCJSBridge)
			}, false)
		}
	}
	
	function resetImgPrice(nowPrice,img,mUrl){
		
		$("#gNowPrice").html(nowPrice);
		$("#gImg").attr("src",img);
		
		var tmp =nowPrice+","+mUrl;
		var data = {"nm":tmp};
		connectNZOCJSBridge(function(bridge) {
			bridge.send(data, function(responseData) {})
		});
		
	}

</script>
</body>
</html>