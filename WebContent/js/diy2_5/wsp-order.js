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
    try{
      uqWyp.notifyInteraction(id);
    }catch(e){
      console.log('uqWyp no load');
    }
}

function sendOC(sendObj) {
    // console.log(sendObj)
    connectNZOCJSBridge(function(bridge) {
      bridge.send(sendObj, function(responseData) {})
    });
}

// function lazyLoad(){
//     $('img').lazyload({
//       data_attribute: 'echo',
//       container: $('body'),
//       load: function($elements, elements_left, options){
//       }
//     });
// }

$(function(){

  var loading = false,
      page = 0,
      place = /impression/i.test(location.href) ? 1 : 2;

  // 处理300ms点击延迟
  FastClick.attach(document.body);

  // 轮播初始化
  var mySwiper = new Swiper('.swiper-container', {
    direction: 'horizontal',
    pagination: '.swiper-pagination',
    observer:true,    //修改swiper自己或子元素时，自动初始化swiper  
    observeParents:true  //修改swiper的父元素时，自动初始化swiper  
  });

  // 处理有点击切换效果的
  $('.toggle').click(function(){
    $(this).addClass('active').siblings().removeClass('active');
  });

  // 数据加载
  function ajaxInit(url){
    $.ajax({
      async: true,
      url: url,
      type: 'POST',
      dataType: 'json',
      timeout: 60000,
      success: function(json){
        if(json.code == 200 && (json.data.length > 0 || json.data != '')){
          var typeNum = 0;    // 统计type为4的数据数量
          json.data.forEach(function(data, index, dataArray){
            data.type === 4 && typeNum++;
          });
          paddingData(json, typeNum);
        }else{
          $('.load-more').html('<i class="left-grad-line"></i>到底啦<i class="right-grad-line"></i>');
          $(document).off('scroll');
        }
      },
      error: function(a, b, c){
        console.log('error');
      }
    });
  }
  ajaxInit('http://120.26.112.213:8082/diyMall/commodity/production.do?place=' + place);

  // 数据请求成功，填充数据
  function paddingData(json, typeNum){
    // console.table(json.data);
    var length = json.data.length, 
      numPerPage = 6,   // 每页轮播包含的个数
      fullPage = parseInt(typeNum/numPerPage),   // 总共设置几页轮播
      cntType = 0,
      swiperList1 = '',
      swiperList2 = (page == 0 && place == 2) ? '<div class="swiper-slide"><ul class="swiper-list clearfix">' : '',
      swiperContainer = $('.swiper-wrapper'),
      goodsList = '',    // 商品
      goodsContainer = $('.goods-list'),
      sendData = '';

    json.data.forEach(function(data, index, dataArray){
      if(data.type === 1){    // 类型为导航
        sendData = '\'' + data.gType + ',' + data.goods+'\'';
        swiperList1 += '<div class="swiper-slide">'+
                                      '<a href="javascript:;" onclick="sending(' + sendData + ')">'+
                                        '<img src="'+data.img+'">'+
                                      '</a>'+
                                    '</div>';
      }else if(data.type === 4){    // 类型为刻字页导航
        sendData = '\'' + 3 + ',http://120.26.112.213:8082/diyMall/commodity/orderSpecial.do?id=' + data.id + '&title=' + data.name + '\'';
        if(parseInt(cntType/numPerPage) < fullPage){   // 剩余内容不显示
          cntType%numPerPage==0 && cntType!=0 ? swiperList2 += '</ul></div> <div class="swiper-slide"><ul class="swiper-list clearfix">' : '';
          swiperList2 += '<li class="swiper-card">'+
                          '<a href="javascript:;" onclick="sending(' + sendData + ')">'+
                            '<img src="'+data.img+'" alt="">'+
                          '</a>'+
                        '</li>';
        }
        cntType++;
      }else if(data.type === 3){   // 类型为专题
        sendData = '\'' + 3 + ',http://120.26.112.213:8082/diyMall/commodity/orderSpecial.do?id=' + data.id + '&title=' + data.name + '\'';
        goodsList += '<li class="goods-card special">'+
                      '<a href="javascript:;"  onclick="sending(' + sendData + ')">'+
                      '  <div class="img-box">'+
                      '    <img src="' + data.img + '" alt="">'+
                      '  </div>'+
                      '  <div class="text-box">'+
                      '    <div class="text-box-inner">'+
                      '      <h3 class="special-name ellipsis">' + data.name + '</h3>'+
                      '      <p class="special-desc ellipsis">' + (data.subtitle ? data.subtitle : '让你个性十足') + '</p>'+
                      '      <p class="click-enter">点击进入<span>^</span></p>'+
                      '    </div>'+
                      '  </div>'+
                      '</a>'+
                      '</li>';
      }else if(data.type === 2){   // 类型为商品
        var imgLabel;
        page < 0 ? imgLabel = '<img src="http://file.diy.51app.cn/uu20/placeholder.png" data-echo="'+data.img+'" alt="">' : 
                   imgLabel = '<img src="'+data.img+'" alt="">';
        sendData = '\'' + data.gType + ',' + data.goods+'\'';
        if(data.custom_id){
          sendData = '\'' + data.gType + ',' + data.custom_id + '\'';
        }
        goodsList += '<li class="goods-card">'+
                        '<a href="javascript:;" onclick="sending(' + sendData + ')">'+
                          '<div class="img-box">'+ imgLabel +
                          '</div>'+
                          '<div class="text-box">'+
                            '<h3 class="goods-desc">'+data.name+'</h3>'+
                            '<p class="goods-price">￥<span><b>'+(data.price ? data.price.split('.')[0] : '-')+'.</b>' + (data.price ? data.price.split('.')[1] : '-') + '</span></p>'+
                          '</div>'+
                        '</a>'+
                      '</li>';
      }
    });

    (place-1) == 0 && swiperContainer.append(swiperList1);
    (place-1) == 1 && swiperContainer.append(swiperList2);
    goodsContainer.append(goodsList);
    loading = false;
    $('.load-more').css('visibility', 'visible');
    $(window).height() > $('.main-container').height() && $('.load-more').html('<i class="left-grad-line"></i>到底啦<i class="right-grad-line"></i>');
  }

  // 无限加载更多..==============================================================================================================================================
  function loadMore(){
      page++;
      ajaxInit('http://120.26.112.213:8082/diyMall/commodity/production.do?place=' + place + '&page=' + page);
  }

   // 加载更多
  $(document).scroll(function(e){
    if(loading) return;
    var pageHeight = $(this).find('.main-container').height(),
        windowHeight = $('body').height();
    if(pageHeight - $('body').scrollTop() <= windowHeight + 50){
        loading = true;
        loadMore();
    }
  });

});