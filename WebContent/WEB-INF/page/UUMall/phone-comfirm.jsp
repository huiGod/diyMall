<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
%>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, minimum-scale=1, user-scalable=no">
  <title>唯优品</title>
  <link rel="shortcut icon" href="./images/favicon.ico">
  <style>
    *{padding: 0;margin:0;}
    ul{list-style: none;}
    #loginPageSendVCode,#loginNowBtn{-webkit-user-select:none;user-select:none;}
    input[type='tel']{height:16px;padding:12px 0;}
    input{-webkit-appearance:none;-webkit-tap-highlight-color:rgba(0,0,0,0);}
    .login-form{margin:25px 35px 0;}
    .login-form input{-webkit-appearance:normal;font-size:13px;outline:none 0;border:none 0;padding:10px 0;margin-bottom:20px;height:24px;line-height:24px;border:1px solid #f5f5f5;-webkit-border-radius:5px;border-radius:5px;font-family:"Microsoft yahei";}
    .login-form .tel{border:1px solid #ddd;margin-bottom:20px;-webkit-border-radius:5px;border-radius:5px;width:100%;text-indent:1.5em;color:#000;}
    .login-form .v-code{border:1px solid #ddd;width:-webkit-calc(100% - 8em);width:calc(100% - 8em);text-indent:1.5em;margin-bottom:30px;}
    .login-form .send-Vcode{border:1px solid #ddd;height:46px;float:right;width:7em;color:#ff6214;background-color:#fff;text-align:center;-webkit-border-radius:5px;border-radius:5px;}
    .login-form .bandBtn{height:46px;width:100%;text-align:center;color:#fff;background-color:#ff6214;font-size:16px;}
    .tip{font-size:12px;color:#ff6214;margin:0 35px;font-family: "Microsoft yahei";line-height: 18px;}
    /* 提示气泡 */
    .warn-bubble{position:fixed;top:50%;left:0;z-index:20000;margin-top:-27px;line-height:54px;text-align:center;width:100%;-webkit-perspective:100px;perspective:100px;-webkit-perspective-origin:50% 50%;perspective-origin:50% 50%;}
    .warn-bubble span{padding:0 24px;display:inline-block;-webkit-border-radius:12px;border-radius:12px;color:#fff;background-color:rgba(0,0,0,0.7);-webkit-animation:in-out 1.4s both;animation:in-out 1.4s both;}
    @-webkit-keyframes in-out{
      0%{opacity:0;-webkit-transform:translate3d(0,0,50px);transform:translate3d(0,0,50px);}
      21%,93%{opacity:1;-webkit-transform:translate3d(0,0,0);transform:translate3d(0,0,0);}
      100%{opacity:0;-webkit-transform:translate3d(0,0,-100px);transform:translate3d(0,0,-100px);}
    }
    @keyframes in-out{
      0%{opacity:0;-webkit-transform:translate3d(0,0,50px);transform:translate3d(0,0,50px);}
      21%,93%{opacity:1;-webkit-transform:translate3d(0,0,0);transform:translate3d(0,0,0);}
      100%{opacity:0;-webkit-transform:translate3d(0,0,-100px);transform:translate3d(0,0,-100px);}
    }
  </style>
</head>
<body>
  <div class="login-form">
    <ul>
      <li>
        <input type="tel" class="tel" placeholder="输入手机号码"  maxlength="11">
      </li>
      <li>
        <input type="tel" class="v-code" placeholder="输入验证码" maxlength="6">
        <input class="send-Vcode loginPageClass" id="loginPageSendVCode"  type="button" value="发送验证码">
      </li>
      <li>
        <input type="button" id="loginNowBtn" class="bandBtn loginBtn external" value="提交"> 
      </li>
    </ul>
  </div>
  <p class="tip"><b>温馨提示：</b>恭喜您获得一等奖9朵玫瑰花束。先验证您的手机号码，小优客服会马上联系您噢 </p>
  <script src="http://libs.baidu.com/jquery/1.10.2/jquery.min.js"></script>
  <script>
    // 弹窗模块
    var toastWarning = function() {
        var fn = {},  body = $('body'), handle
          , bubble = $('<div class="warn-bubble">' +
                            '<span>优品汇</span>' +
                        '</div>');

        fn.say = function(str) {
            bubble.detach().find('span').text(str).parent().appendTo(body);

            clearTimeout(handle);
            handle = setTimeout(function() {
                bubble.detach();
            }, 1700);
        };
        return fn;
    }();
    var sendVCode = new (function(){
        var initTime = 60;
        var countdown = function(){
            var $this = $(this);
            var _this = this;
            var telNum=$this.parent().parent().find(".tel").val();
            var reg_telNum = /^(13[0-9]|15[012356789]|17[678]|18[0-9]|14[57])[0-9]{8}$/;
            var IsValidTel = reg_telNum.test(telNum);
            // console.log(justify)
            if(IsValidTel){
              var url = "<%=path%>/UCoupon/sendMobileUser.do";
                var submit = $("#loginNowBtn")[0];
              $.ajax({
                url: url,
                data:{mobile:telNum},
                    type: 'POST',
                    dataType: 'json',
                    cache:false,
                    async: false,
                    timeout:"60000",
                    success:function(data){
                        if(data.code==200){
                          submit.removeAttribute("disabled");
                          _this.setAttribute("disabled", "true");
                            $this.addClass("clicked_get_number");
                            var cd = function(){
                                if(initTime==0){
                                    _this.removeAttribute("disabled");
                                    $this.removeClass("clicked_get_number");
                                    _this.value="发送验证码";
                                    initTime = 60;
                                }else{
                                  _this.value="重新发送(" + initTime + ")";
                                    initTime -- ;
                                    setTimeout(function(){cd();},1000);
                                }
                            }
                            cd();
                            toastWarning.say('验证码已发送');
                        }else if(data.code==300){
                            toastWarning.say(data.message);
                        }else{
                            toastWarning.say('失败');
                        }
                    }           
              });            
            }else{
                toastWarning.say('请输入正确的电话号码！');
            }
                   
        }
        $(document).on("click","#loginPageSendVCode",countdown);
    })();
    
    
 // 登录
    var handleLogin = new (function(){
        $(document).on("click",".loginBtn",function(){
        	var url = "<%=path%>/UCoupon/addMobileUser.do";
            var $this = $(this);
            var telNum=$this.parent().parent().find(".tel").val();
            var Vcode = $(".v-code").val();
    		$.ajax({
        		url: url,
        		data:{
        			mobile:telNum,
        			id:"${couponId}",
        			smscode:Vcode
        		},
                type: 'POST',
                dataType: 'json',
                cache:false,
                async: false,
                timeout:"60000",
                success:function(data){
                    if(data.code==200){
                    	// mainView.router.loadPage(pathname.substring(0, pathname.indexOf('/',1))+"/UMallUser/returnBeforePage.do");
                        // window.location.href="https://www.baidu.com/"; 
                        history.back();
                    }else{
                        // myApp.alert(data.message,'提示');
                        toastWarning.say(data.message);
                    }
                }
        	});
        })
    })();
  </script>

</body>
</html>