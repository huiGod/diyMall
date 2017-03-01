var wxData = {
  appId: document.querySelector('.appId').innerHTML,
  timestamp: document.querySelector('.timestamp').innerHTML,
  nonceStr: document.querySelector('.nonceStr').innerHTML,
  signature: document.querySelector('.signature').innerHTML
}

wx.config({
    debug: false,
    appId: wxData.appId,
    timestamp: wxData.timestamp,
    nonceStr: wxData.nonceStr,
    signature: wxData.signature,
    jsApiList: [
      'checkJsApi',
      'onMenuShareTimeline',
      'onMenuShareAppMessage',
      'onMenuShareQQ',
      'onMenuShareWeibo',
      'onMenuShareQZone'
  ]
});

// 微信接口
wx.ready(function () {
  var shareData = {
    title: '唯优品',
    desc: '明星，卡通创意定制精品店，高清模板，零经验秒变设计师',
    link: 'http://test.diy.51app.cn/diyMall2/UGoods/initHome.do',
    imgUrl: 'http://file.diy.51app.cn/diymall_logo@3x.png',
    success:function(){$("#mcover").hide();},
    cancel: function (res) {toastWarning.say("已取消");},
    fail: function (res) {toastWarning.say(JSON.stringify(res));}
  };        
  wx.onMenuShareAppMessage(shareData);
  wx.onMenuShareTimeline(shareData);
  wx.onMenuShareQQ(shareData);
  wx.onMenuShareWeibo(shareData);
  wx.onMenuShareQZone(shareData);   
});

wx.error(function(res){
  toastWarning.say(res.errMsg);
});