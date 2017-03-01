<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
	<meta charset="UTF-8">
	<title>微信支付</title>
</head>
<body>


<script>
	function onBridgeReady(){
   WeixinJSBridge.invoke(
       'getBrandWCPayRequest', {
           "appId" : "${pay.appId}",     //公众号名称，由商户传入     
           "timeStamp": "${pay.timeStamp}",         //时间戳，自1970年以来的秒数     
           "nonceStr" : "${pay.nonceStr}", //随机串     
           "package" : "prepay_id=${pay.prepayId}",     
           "signType" : "${pay.signType}",         //微信签名方式:     
           "paySign" : "${pay.paySign}" //微信签名 
       },
       function(res){     
           if(res.err_msg == "get_brand_wcpay_request：ok" ) {}     // 使用以上方式判断前端返回,微信团队郑重提示：res.err_msg将在用户支付成功后返回    ok，但并不保证它绝对可靠。 
       }
   ); 
}
if (typeof WeixinJSBridge == "undefined"){
   if( document.addEventListener ){
       document.addEventListener('WeixinJSBridgeReady', onBridgeReady, false);
   }else if (document.attachEvent){
       document.attachEvent('WeixinJSBridgeReady', onBridgeReady); 
       document.attachEvent('onWeixinJSBridgeReady', onBridgeReady);
   }
}else{
   onBridgeReady();
} 

</script>
</body>
</html>