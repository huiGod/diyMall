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
			<div class="pop-imgBox"><img id="gImg" src=" ${gbp.pre_url}" alt=""></div>
			<div class="pop-info-Box">
				<h2 class="fs20 fc5" >¥ <em id="gNowPrice">${gbp.now_price}</em></h2>
			</div>
			<div class="closeSelect" ><img style="width:100%;" src="${ctx}/images/closeSelect.png" alt=""></div>
		</div>		
	</div>
	<!-- 属性选择 -->
	<div id="pop-GoodsContent">
		<div class="pop-selects fc00">
			<ul>
					<li>
						<h3 class="fs14 pop-cont-h"><span>${gbp.listA.title}</span></h3>
						<p class="fs12 pop-opts ">
							<c:forEach items="${gbp.listA.list}" var="la">
								<span class="listA" id="${la.id }">${la.gname}</span>
							</c:forEach>
						</p>
					</li>
					<li>
						<h3 class="fs14 pop-cont-h"><span>${gbp.listB.title}</span></h3>
						<p class="fs12 pop-opts ">
							<c:forEach items="${gbp.listB.list}" var="lb">
								<span class="listB" id="${lb.id }">${lb.gname}</span>
							</c:forEach>
						</p>
					</li>
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
	
		<c:forEach items="${gbp.texture }" var="t">
			<div style="display:none" class="${t.texture_ids }">
				<span class="org_price">${t.org_price}</span>
				<span class="now_price">${t.now_price}</span>
				<span class="pre_url">${t.pre_url}</span>
				<span class="box_url">${t.box_url}</span>
				<span class="saveSize">${t.save_size}</span>
				<span class="texture_ids">${t.texture_ids}</span>
			</div>
		</c:forEach>
	
<script src="${ctx}/js/lib/zepto.min.js"></script>
<script src="${ctx}/js/lib/touch.js"></script>
<script>
var id="${gbp.id}";
//var gtId ="${gbp.gtId}";
var storeId ="${gbp.storeId}";
var price ="${gbp.now_price}";
var org_price = "${gbp.org_price}";
var gType ="${gbp.goodsType}";
var saveSize = "${gbp.save_size}";
var texture = "${gbp.texture }";
var json = '${json}';

$(document).ready(function() {
	var arr=["","","1",id,price,storeId,gType],
		$addBtn=$(".add"),
		$rdcBtn=$(".reduce"),
		$num=$(".input-num"),
		$nowPrice=$("#gNowPrice");
		var sendObj = {
				goodsId:id,
				gType:gType,
				price:price,
				org_price:org_price,
				storeId:storeId,
				num:"1",
				paramId:"",
				paramName:"",
				boxUrl:"",
				saveSize:saveSize
		};

		function closeKeyboard(){
			$(".pop-btn-selected").eq(0).trigger("click");
		}
		// 购买数量
		$addBtn.on("tap",function(){
			var num=parseInt($num.val());
			(num==1)?($rdcBtn.removeClass('aaa2'),$num.val(num+1)):$num.val(num+1);
			var numArr =++num;
			sendObj.num=numArr.toString();
			//arrStr=arr[3]+","+arr[6]+","+arr[4]+","+arr[5]+","+arr[2]+","+arr[0]+arr1;
			//商品id,gType,价格,storeId,数量,参数
			sendOC();
		});
		$rdcBtn.on("tap",function(){
			var num=parseInt($num.val());
			(num==2)?($rdcBtn.addClass('aaa2'),$num.val(num-1),sendObj.num=--num):((num==1)?(i=1,sendObj.num=1):($num.val(num-1),sendObj.num=--num));	
			sendObj.num=sendObj.num+"";
			sendOC();
		});
		// 输入事件，发送一次数据 $num.val("1"),
		$num.on("input blur",function(e){
			var _type = e.type;
			var num=parseInt($num.val());
			var val_length = $num.val().length;
			if(_type == "input"){
				(val_length==0)?($rdcBtn.addClass('aaa2'),sendObj.num=1):((num==0)?($num.val("1"),$rdcBtn.addClass('aaa2'),sendObj.num=1):((num==1)?($rdcBtn.addClass('aaa2'),sendObj.num=num):(sendObj.num=num)));
			}else if(_type == "blur"){
				(val_length==0)?($num.val("1"),$rdcBtn.addClass('aaa2'),sendObj.num=1):((num==0)?($num.val("1"),$rdcBtn.addClass('aaa2'),sendObj.num=1):((num==1)?($rdcBtn.addClass('aaa2'),sendObj.num=num):(sendObj.num=num)));
			}
			sendOC();
		});
		$(".pop-selects").on("tap",".pop-selects p span",function(){
			
			$(this).addClass('pop-btn-selected').siblings().removeClass('pop-btn-selected');
			$(".pop-btn-selected").each(handle);
			paramf();
			
			
			var checked = $(".pop-btn-selected");
			var checkedIds ="";
			
			$.each(checked, function(i){//循环得到不同的id的值
				checkedIds += checked.eq(i).attr("id")+"_";
			});
			
			var ids = checkedIds.substring(0,checkedIds.length-1);
			ids = "."+ids;
			
		    var pre_url = $(ids).find(".pre_url").text();
		    var now_price = $(ids).find(".now_price").text();
		    var orgPrice = $(ids).find(".org_price").text();
		    var box_url = $(ids).find(".box_url").text();
		    var newsaveSize = $(ids).find(".saveSize").text();
		    $("#gNowPrice").html(now_price);
			$("#gImg").attr("src",pre_url);
			
			sendObj.price = now_price;
			sendObj.org_price = orgPrice;
			sendObj.boxUrl = box_url;
			sendObj.saveSize = newsaveSize;
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
			sendObj.paramName = arr[0]+arr1;
		}
		function paramf(){
			var checked = $(".pop-btn-selected");
			var checkedIds ="";
			$.each(checked, function(i){//循环得到不同的id的值
				checkedIds += checked.eq(i).attr("id")+"_";
			});
			var ids = checkedIds.substring(0,checkedIds.length-1);
			sendObj.paramId=ids;
		}
		$(".pop-selects p span:first-child").addClass('pop-btn-selected');
		$(".pop-btn-selected").each(handle);
		paramf();
		
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
			bridge.send(sendObj, function(responseData) {});
			android.onDataChanged(JSON.stringify(sendObj ));
		});
		function sendOC(){
			//console.log(sendObj);
			connectNZOCJSBridge(function(bridge) {
				bridge.send(sendObj, function(responseData) {})
			});
			android.onDataChanged(JSON.stringify(sendObj ));
		}

		// 启用内容区溢出滚动
		var contentObj = $("#pop-GoodsContent")[0];
		overscroll(contentObj);
		
		$(".listB").on("tap",function(){
			var checkId = $(this).attr("id");
			var checkOneId = $("ul li").eq(0).find("p .pop-btn-selected").attr("id");
			var texture_ids = $(".texture_ids");
			var jsonarr = eval("("+json+")");
			var ppp = $("ul li").eq(0).find("p");
			var ishave = false;
			$.each(texture_ids, function(i){
				var textureid = texture_ids[i].innerHTML;
				var lastId = textureid.substr(textureid.indexOf('_')+1,textureid.length-1);
				if(checkId==lastId){
					var firstId = textureid.substr(0,textureid.indexOf('_'));
					var listA = jsonarr.listA.list;
					$.each(listA, function(k){
						if(listA[k].id==firstId){
							if(checkOneId==listA[k].id){
								ishave=true;
							}
						}
					});
				}
			});
			ppp.empty();
			$.each(texture_ids, function(i){
				var textureid = texture_ids[i].innerHTML;
				var lastId = textureid.substr(textureid.indexOf('_')+1,textureid.length-1);
				if(checkId==lastId){
					var firstId = textureid.substr(0,textureid.indexOf('_'));
					var listA = jsonarr.listA.list;
					$.each(listA, function(j){
						if(listA[j].id==firstId){
							if(ishave){
								if(checkOneId==listA[j].id)
									ppp.append('<span class="listA pop-btn-selected" id="'+listA[j].id+'">'+listA[j].gname+'</span>');
								else
									ppp.append('<span class="listA" id="'+listA[j].id+'">'+listA[j].gname+'</span>');
							}else{
								if(j==0)
									ppp.append('<span class="listA pop-btn-selected" id="'+listA[j].id+'">'+listA[j].gname+'</span>');
								else
									ppp.append('<span class="listA" id="'+listA[j].id+'">'+listA[j].gname+'</span>');
							}
						}
					});
				}
				
			});
		});
		
		$(".listB").eq(0).trigger("tap");
		
		/* $(".listA").on("tap",function(){
			console.log("listA");
			var checkId = $(this).attr("id");
			var checkOneId = $("ul li").eq(1).find("p .pop-btn-selected").attr("id");
			var texture_ids = $(".texture_ids");
			var jsonarr = eval("("+json+")");
			var ppp = $("ul li").eq(1).find("p");
			ppp.empty();
			$.each(texture_ids, function(i){
				var textureid = texture_ids[i].innerHTML;
				var firstId = textureid.substr(0,textureid.indexOf('_'));
				if(checkId==firstId){
					var lastId = textureid.substr(textureid.indexOf('_')+1,textureid.length-1);
					var listB = jsonarr.listB.list;
					$.each(listB, function(j){
						if(listB[j].id==lastId){
							if(checkOneId==listB[j].id)
								ppp.append('<span class="listB pop-btn-selected" id="'+listB[j].id+'">'+listB[j].gname+'</span>');
							else
								ppp.append('<span class="listB" id="'+listB[j].id+'">'+listB[j].gname+'</span>');
						}
					});
				}
				
			});
		}); */
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
	

</script>
</body>
</html>