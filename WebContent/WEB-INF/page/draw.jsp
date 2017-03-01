<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="apple-mobile-web-app-capable" content="yes">
    <meta name="apple-mobile-web-app-status-bar-style" content="black">
    <meta content="yes" name="apple-mobile-web-app-capable"/>
    <link rel="stylesheet" href="${ctx}/css/drawLottery.css">
    <title>51app</title>
</head>
<body style="background-color:rgba(0,0,0,.85);position:absolute;height:100%;">
	<div class="popDrawLottery">
        <div class="drawLottery-mask"></div>
        <header>
            <span class="drawLotteryBanner"><img src="${ctx}/images/drawLotteryBanner.png" class="bannerImg"></span>
            <span class="drawLotteryClose"><img src="${ctx}/images/drawLot-closeBtn.png" ></span>
        </header>
        <main class="drawLotteryMain">
            <section class="turnPlate pos-rel">
                <img src="${ctx}/images/drawer.png" id="rotate" class="drawer">
                <img src="${ctx}/images/pointer.png" class="pointer">
            </section>
            <section class="awardPool">
                <article class="hide tips">
                    <h3 id="H-tips">恭喜您！</h3>
                    <p class="award-p"></p>
                    <p class="autoTips">提示：支付时会自动抵扣现金。</p>
                </article>  
                <button class="accessBtn hide"></button>
            </section>
        </main> 
    </div>
    <script src="${ctx}/js/lib/jquery-2.2.3.min.js"></script>
    <script src="${ctx}/js/awardRotate.js"></script>
    <script>
	    function connectNZOCJSBridge(callback) {
	 		if (window.NZOCJSBridge) {
	 			callback(NZOCJSBridge)
	 		} else {
	 			document.addEventListener('NZOCJSBridgeReady', function() {
	 				callback(NZOCJSBridge)
	 			}, false)
	 		}
	 	} 

	    var valid='${dlvM.valid}';
   	 	var item=parseInt('${dlvM.level}');
   	 	var count = 1;
   	 	var tips_2 = $("#H-tips");
        var tips_3 = $("#autoTips");
	    
    	$(function (){
        	 
        	 
            var rotateTimeOut = function (){
                $('#rotate').rotate({
                    angle:0,
                    animateTo:2160,
                    duration:8000,
                    callback:function (){
                        alert('网络超时，请检查您的网络设置！');
                    }
                });
            };
            var bRotate = false;
            var rotateFn = function (awards, angles, txt){
                bRotate = !bRotate;
                $('#rotate').stopRotate();
                var tips=$(".tips");
                var text=$('.award-p');                
                $('#rotate').rotate({
                    angle:0,
                    animateTo:angles+1800,
                    duration:6000,
                    callback:function (){
                        text.text(txt);
                        item == 0 && (tips_2.text("很遗憾") ,tips_3.text("亲，下次中奖的概率翻倍哦"));
                        tips.removeClass("hide").addClass("animal");
                        $(".accessBtn").removeClass("hide").addClass("animal2");
                        bRotate=!bRotate;
                        
                        	 $.ajax({
                             	url: 'addCouponUser.do',
     				        	type: 'GET',
     				        	data:{
     				        		valid:valid,
     				        		item:item,
     				        		mobile:"${dlvM.mobile}"
     				        	},
     				        	dataType: 'json',
     				        	success:function(data){
     				        		if(data.code==300){
     				        			text.text("");
     				        			tips_2.text("");
     				        			$(".autoTips").text("");
     				        			alert('抽奖次数已用完或当前不是抽奖时间!!!');
     				        		}else if(data.code==500){
     				        			text.text("");
     				        			tips_2.text("");
     				        			$(".autoTips").text("");
     				        			alert('服务器错误');
     				        		}
     				        	}
                             });
                       
                    }
                });
            };
            $('.pointer').click(function (){
                if(bRotate)return;
                if(count>1) {
                	alert('您的抽奖次数已经用完了');
                	return;
                }
                count+=1;
                connectNZOCJSBridge(function(bridge) {
             		bridge.init(function(message, responseCallback) {
             			 //alert(message);
                         var data ={'data':'数据回调'};
             			responseCallback(data);
             		});
             	})
                
                switch (item) {
                    case 0:
                        rotateFn(0, 90, '差一点就中了!');
                        break;
                    case 1:
                        rotateFn(1, 315, '抽中一等奖【80元现金券】');
                        break;
                    case 2:
                        rotateFn(2, 225, '抽中二等奖【首单免单】');
                        break;
                    case 3:
                        rotateFn(3, 180, '抽中三等奖【40元现金券】');
                        break;
                    case 4:
                        rotateFn(4, 270, '抽中四等奖【20元现金券】');
                        break;
                    case 5:
                        rotateFn(5, 135, '抽中五等奖【10元现金券】');
                        break;
                    case 6:
                        rotateFn(6, 360, '抽中六等奖【5元现金券】');
                        break;
                    case 7:
                        rotateFn(7, 45, '抽中特等奖【100元现金券】');
                        break;
                    default:
                        break;
                }
            });
            $(".drawLotteryClose,.accessBtn").click(function(){
            	//$(".popDrawLottery").addClass("hide");
            	var rdata = {"click":"close"};
    			connectNZOCJSBridge(function(bridge) {
    				bridge.send(rdata, function(responseData) {})
    			});
            });
        });
    </script>

</body>
</html>