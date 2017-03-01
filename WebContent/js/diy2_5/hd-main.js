

/*   js传值ios
 ==============================*/
var connectNZOCJSBridge = function (callback) {
    if (window.NZOCJSBridge) {
        callback(NZOCJSBridge)
    } else {
        document.addEventListener('NZOCJSBridgeReady', function() {
            callback(NZOCJSBridge)
        }, false)
    }
};
var sending = function (id) {

    console.log(id);
    data = {
        "click": id
    };
    connectNZOCJSBridge(function(bridge) {
        bridge.send(data, function(responseData) {})
    });

    uqWyp.notifyInteraction(id);
};
var sendOC = function (sendObj) {
    console.log(sendObj);
    connectNZOCJSBridge(function(bridge) {
        bridge.send(sendObj, function(responseData) {})
    });
};



var lazyLoad = function lazyLoad() {

    // $("img").lazyload({
    //     data_attribute     :'echo',
    //     load:function ($elements, elements_left, options) {
    //         $elements.removeClass('placeholder')
    //     },
    //     // container:$('.containerBox'),
    // });

}
//获取地址栏参数
function GetQueryString(name)
{
    var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");
    var r = window.location.search.substr(1).match(reg);
    if(r!=null)return  unescape(r[2]); return null;
}


//加载数据
var loadData = function (url, async, successFunc) {
    $.ajax({
       url: url,
       type: 'POST',
       timeout: 60000,
       async: async,
       dataType: 'json',
       data: {},
       success: successFunc, 
        error:function (error) {
            console.log('冒的数据 搞毛呀');
            console.log(error);

        }
   });
}

// 无限加载 局部滚动
// function infinite(container,content,distance,loadMore) {
//     var loading = false;
//     $(container).on('scroll',function () {
//         if (loading) return;
//         loading = true;
//         setTimeout(function () {
//             var that = $(container),
//                 pageHeight = that.find(content).height(),
//                 containerHeight = that.height();
//
//             if(pageHeight - that.scrollTop() < containerHeight + distance){
//                 loadMore();
//             }
//             loading = false;
//
//         },1000);
//
//     });
//
// }



//无限加载  window为容器的时候
var infinite = function (container,content,distance,loadMore) {
    var loading = false;
    $(window).on('scroll',function () {
        if (loading) return;
        loading = true;
        setTimeout(function () {
            var that = $(container),
                pageHeight = that.find(content).height(),
                containerHeight = $(window).height();

            if(pageHeight - $(window).scrollTop() < containerHeight + distance){
                loadMore();
            }
            loading = false;

        },0);

    });

}

var topSwiper = function () {
    var conBanner = new Swiper ('.swiper-conBanner', {

        loop: true,
        autoplay:2000,
        speed:1000,
        autoplayDisableOnInteraction:false,
        pagination: '.swiper-pagination',
        spaceBetween : 20,
        observer:true,//修改swiper自己或子元素时，自动初始化swiper
        // observeParents:true,//修改swiper的父元素时，自动初始化swiper

    });
};

//回到顶部
var goTop = function () {
    $(window).on("scroll",function(){
        var top = $(window).scrollTop();
        if(top>300)  $(".gotoTop").show();
        if(top<=300)  $(".gotoTop").hide();
    });

    $.extend($.fn, {
        scrollTo: function(m){
            var n = this.scrollTop(), timer = null, that = this;
            var constN = n;
            var smoothScroll = function(m){
                var per = Math.round(constN / 20);
                n = n - per;
                if(n<=per){
                    n=m;
                    if(n == m){
                        that.scrollTop(n);
                        $(".gotoTop").hide();
                        window.clearInterval(timer);
                        return false;
                    }
                }
                that.scrollTop(n);
            };
            timer = window.setInterval(function(){
                smoothScroll(m);
            }, 20);
        }
    });
    $('.gotoTop').on("click",function(){
        $(window).scrollTo(1);
    });

};








//加载首屏数据
var loadFirst = function () {
    var url,successFunc;
    var pageName = $('.containerBox').attr('data-page');
    // var pageId = $('.containerBox').attr('data-id');
    var pageId = GetQueryString('id');

    //首页精选开始
    if(pageName == 'index' && pageId == 1){
        goTop();
        $('#other').remove();
         url = $('#first').attr('data-firstUrl');

         successFunc = function (data) {
            var dataBox = '';
            var sendData = '';
            var banners          = data.data.banners,
                midNav           = data.data.midNav,
                todayGoods       = data.data.todayGoods,
                specials         = data.data.specials,
                recommendGoods   = data.data.recommendGoods;

             //专题最多为4个
             if(specials.length >4){specials.length = 4}

            if(data.code == 200){

                //page1的轮播数据
                dataBox = '';
                $.each(banners,function (i) {
                    sendData = '\''+banners[i].type+','+ banners[i].about+'\'';
                    dataBox += '<div class="swiper-slide" onclick="sending('+ sendData +')"><div class="bannerItem" ><img src="'+ banners[i].imgUrl +'"/></div></div>';
                });
            $('.containerBox .cb-banner .swiper-wrapper').empty().append(dataBox);

                //图标分类数据
                dataBox = '';
                $.each(midNav,function (i) {
                    sendData = '\''+midNav[i].key+','+ midNav[i].about+'\'';
                    dataBox += '<div class="item" onclick="sending('+ sendData+')"><i><img  src="'+ midNav[i].imgUrl +'"/></i><span>'+ midNav[i].name +'</span></div>';
                });

                $('.containerBox .cb-classify').empty().append(dataBox);


                //今日特价数据
                dataBox = '';
                $.each(todayGoods,function (i) {
                    sendData = '\''+todayGoods[i].type+','+ todayGoods[i].goodId+'\'';
                    dataBox += '  <div class="item" onclick="sending('+ sendData+')"><b><img src="'+ todayGoods[i].icoUrl +'"/></b><p><i>&yen; '+ todayGoods[i].nowPrice +'</i> <s>&yen; '+ todayGoods[i].orgPrice +'</s></p></div>';
                });

                $('.containerBox .cb-sale .itemBox').css('width',todayGoods.length*2.2+'rem').empty().append(dataBox);

                //专题推荐数据
                dataBox = '';

                $.each(specials,function (i) {
                    sendData = '\''+'1,http://120.26.112.213:8082/diyMall/index/toSpecial.do?id='+specials[i].id +'&type='+specials[i].type+'\'';
                    dataBox += '<div class="item"  onclick="sending('+ sendData+')"><div class="imgBox"><img src="'+ specials[i].imgUrl +'"/></div><p>'+  specials[i].title+'</p></div>';
                });

                $('.containerBox .cb-special').empty().append(dataBox);
               //商品推荐数据
                dataBox = '';
                $('.containerBox .hd-noLoad').hide();
                var priceBig ,priceSmall;
                $.each(recommendGoods,function (i) {
                    priceBig = String(recommendGoods[i].nowPrice).split('.')[0];
                    priceSmall = String(recommendGoods[i].nowPrice).split('.')[1];
                    if(!priceSmall){priceSmall = 0}

                    sendData = '\''+recommendGoods[i].type+','+ recommendGoods[i].id+'\'';

                    var strA1 = '<div class="item" onclick="sending('+ sendData+')">';
                    var strA2 = '<div class="item noPostage" onclick="sending('+ sendData+')">';
                    var strB = '<div class="imgBox"><img src="'+ recommendGoods[i].icoUrl +'"/></div><div class="name"><i><img src="http://file.diy.51app.cn/uu20/noPostage.png"/></i><span>'+ recommendGoods[i].name +'</span></div><div class="price"><i>&yen; <b>'+ priceBig +'.</b>'+ priceSmall +'</i><span>已售'+ recommendGoods[i].sell +'件</span></div>';
                   var strC1 = '<div class="label"><img src="http://file.diy.51app.cn/wp/icoImg/label_tuyin.png"/></div></div>';
                    var strC2 = '<div class="label"><img src="http://file.diy.51app.cn/wp/icoImg/label_kezi.png"/></div></div>';
                    var strC3 = '<div class="label"><img src="http://file.diy.51app.cn/wp/icoImg/label_keyin.png"/></div></div>';
                    var strC4 = '</div>';

                    if(recommendGoods[i].ispostage == 1){

                        if(recommendGoods[i].type == 2){
                            dataBox += strA1+strB+strC1;
                        }else if(recommendGoods[i].type == 4){
                            dataBox += strA1+strB+strC2;
                        }else if(recommendGoods[i].type == 5){
                            dataBox += strA1+strB+strC3;
                        }else{
                            dataBox += strA1+strB+strC4;
                        }

                    }else{
                        if(recommendGoods[i].type == 2){
                            dataBox += strA2+strB+strC1;
                        }else if(recommendGoods[i].type == 4){
                            dataBox += strA2+strB+strC2;
                        }else if(recommendGoods[i].type == 5){
                            dataBox += strA2+strB+strC3;
                        }else{
                            dataBox += strA2+strB+strC4;
                        }
                    }
                });

                $('.containerBox .cb-recommend').empty().append(dataBox);


                //数据加载完成后显示界面
                $('.containerBox .hd-hasLoad').show();
                topSwiper();


                lazyLoad();



            }else{
                console.log("不是200的我不要")
            }

        };
    //首页精选结束
    //首页其他页面开始
    }else if(pageName == 'index' && pageId != 1){
        $('#first').remove();
        url = $('#other').attr('data-firstUrl')+pageId;

         successFunc = function (data) {
            var dataBox = '';
            var sendData = '';
            var banners          = data.data.banners,
                recommendGoods   = data.data.recommendGoods;


            if(data.code == 200){

                //轮播
                dataBox = '';
                $.each(banners,function (i) {
                    sendData = '\''+banners[i].type+','+ banners[i].about+'\'';
                    dataBox += '<div class="swiper-slide" onclick="sending('+ sendData +')"><div class="bannerItem"><img src="'+ banners[i].imgUrl +'"/></div></div>';
                });
                $('.containerBox .cb-banner .swiper-wrapper').empty().append(dataBox);



                //商品推荐数据
                dataBox = '';
                var priceBig ,priceSmall;
                $.each(recommendGoods,function (i) {
                    priceBig = String(recommendGoods[i].nowPrice).split('.')[0];
                    priceSmall = String(recommendGoods[i].nowPrice).split('.')[1];
                    if(!priceSmall){priceSmall = 0}

                    sendData = '\''+recommendGoods[i].type+','+ recommendGoods[i].id+'\'';

                    var strA1 = '<div class="item" onclick="sending('+ sendData+')">';
                    var strA2 = '<div class="item noPostage" onclick="sending('+ sendData+')">';
                    var strB = '<div class="imgBox"><img src="'+ recommendGoods[i].icoUrl +'"/></div><div class="name"><i><img src="http://file.diy.51app.cn/uu20/noPostage.png"/></i><span>'+ recommendGoods[i].name +'</span></div><div class="price"><i>&yen; <b>'+ priceBig +'.</b>'+ priceSmall +'</i><span>已售'+ recommendGoods[i].sell +'件</span></div>';
                    var strC1 = '<div class="label"><img src="http://file.diy.51app.cn/wp/icoImg/label_tuyin.png"/></div></div>';
                    var strC2 = '<div class="label"><img src="http://file.diy.51app.cn/wp/icoImg/label_kezi.png"/></div></div>';
                    var strC3 = '<div class="label"><img src="http://file.diy.51app.cn/wp/icoImg/label_keyin.png"/></div></div>';
                    var strC4 = '</div>';

                    if(recommendGoods[i].ispostage == 1){

                        if(recommendGoods[i].type == 2){
                            dataBox += strA1+strB+strC1;
                        }else if(recommendGoods[i].type == 4){
                            dataBox += strA1+strB+strC2;
                        }else if(recommendGoods[i].type == 5){
                            dataBox += strA1+strB+strC3;
                        }else{
                            dataBox += strA1+strB+strC4;
                        }

                    }else{
                        if(recommendGoods[i].type == 2){
                            dataBox += strA2+strB+strC1;
                        }else if(recommendGoods[i].type == 4){
                            dataBox += strA2+strB+strC2;
                        }else if(recommendGoods[i].type == 5){
                            dataBox += strA2+strB+strC3;
                        }else{
                            dataBox += strA2+strB+strC4;
                        }
                    }
                });

                $('.containerBox .cb-recommend').empty().append(dataBox);

                if(recommendGoods.length < 6){
                    $('.infinite-scroll-preloader').remove();
                }

                //数据加载完成后显示界面
                $('.containerBox .hd-noLoad').hide();
                $('.containerBox .hd-hasLoad').show();

                lazyLoad();



            }else{
                console.log("不是200的我不要")
            }

        };

    //首页其他页面结束
    //分类页面开始
    }else if(pageName == 'classify'){
        url = $('.containerBox').attr('data-url')+pageId+"&page=0";
        console.log(url);
        successFunc = function (data) {
            var dataBox = '';
            var sendData = '';
            var recommendGoods = data.data;


            if (data.code == 200) {


                //商品推荐数据
                dataBox = '';
                var priceBig ,priceSmall;
                $.each(recommendGoods,function (i) {
                    priceBig = String(recommendGoods[i].nowPrice).split('.')[0];
                    priceSmall = String(recommendGoods[i].nowPrice).split('.')[1];
                    if(!priceSmall){priceSmall = 0}

                    sendData = '\''+recommendGoods[i].type+','+ recommendGoods[i].id+'\'';

                    var strA1 = '<div class="item" onclick="sending('+ sendData+')">';
                    var strA2 = '<div class="item noPostage" onclick="sending('+ sendData+')">';
                    var strB = '<div class="imgBox"><img src="'+ recommendGoods[i].icoUrl +'"/></div><div class="name"><i><img src="http://file.diy.51app.cn/uu20/noPostage.png"/></i><span>'+ recommendGoods[i].name +'</span></div><div class="price"><i>&yen; <b>'+ priceBig +'.</b>'+ priceSmall +'</i><span>已售'+ recommendGoods[i].sell +'件</span></div>';
                    var strC1 = '<div class="label"><img src="http://file.diy.51app.cn/wp/icoImg/label_tuyin.png"/></div></div>';
                    var strC2 = '<div class="label"><img src="http://file.diy.51app.cn/wp/icoImg/label_kezi.png"/></div></div>';
                    var strC3 = '<div class="label"><img src="http://file.diy.51app.cn/wp/icoImg/label_keyin.png"/></div></div>';
                    var strC4 = '</div>';

                    if(recommendGoods[i].ispostage == 1){

                        if(recommendGoods[i].type == 2){
                            dataBox += strA1+strB+strC1;
                        }else if(recommendGoods[i].type == 4){
                            dataBox += strA1+strB+strC2;
                        }else if(recommendGoods[i].type == 5){
                            dataBox += strA1+strB+strC3;
                        }else{
                            dataBox += strA1+strB+strC4;
                        }

                    }else{
                        if(recommendGoods[i].type == 2){
                            dataBox += strA2+strB+strC1;
                        }else if(recommendGoods[i].type == 4){
                            dataBox += strA2+strB+strC2;
                        }else if(recommendGoods[i].type == 5){
                            dataBox += strA2+strB+strC3;
                        }else{
                            dataBox += strA2+strB+strC4;
                        }
                    }
                });

                $('.containerBox .cb-recommend').empty().append(dataBox);


                //数据加载完成后显示界面
                $('.containerBox .hd-noLoad').hide();
                $('.containerBox .hd-hasLoad').show();

                lazyLoad();


            } else {
                console.log("不是200的我不要")
            }

        };
    //分类页面结束
    }else {
        //店铺商品
        url = $('.containerBox').attr('data-url')+"?storeId="+pageId+"&page=0";
        successFunc = function (data) {
            var dataBox = '';
            var sendData = '';
            var recommendGoods = data.data;


            if (data.code == 200) {


                //商品推荐数据
                dataBox = '';
                var priceBig ,priceSmall;
                $.each(recommendGoods,function (i) {
                    priceBig = String(recommendGoods[i].nowPrice).split('.')[0];
                    priceSmall = String(recommendGoods[i].nowPrice).split('.')[1];
                    if(!priceSmall){priceSmall = 0}

                    sendData = '\''+recommendGoods[i].type+','+ recommendGoods[i].id+'\'';

                    var strA1 = '<div class="item" onclick="sending('+ sendData+')">';
                    var strA2 = '<div class="item noPostage" onclick="sending('+ sendData+')">';
                    var strB = '<div class="imgBox"><img src="'+ recommendGoods[i].icoUrl +'"/></div><div class="name"><i><img src="http://file.diy.51app.cn/uu20/noPostage.png"/></i><span>'+ recommendGoods[i].name +'</span></div><div class="price"><i>&yen; <b>'+ priceBig +'.</b>'+ priceSmall +'</i><span>已售'+ recommendGoods[i].sell +'件</span></div>';
                    var strC1 = '<div class="label"><img src="http://file.diy.51app.cn/wp/icoImg/label_tuyin.png"/></div></div>';
                    var strC2 = '<div class="label"><img src="http://file.diy.51app.cn/wp/icoImg/label_kezi.png"/></div></div>';
                    var strC3 = '<div class="label"><img src="http://file.diy.51app.cn/wp/icoImg/label_keyin.png"/></div></div>';
                    var strC4 = '</div>';

                    if(recommendGoods[i].ispostage == 1){

                        if(recommendGoods[i].type == 2){
                            dataBox += strA1+strB+strC1;
                        }else if(recommendGoods[i].type == 4){
                            dataBox += strA1+strB+strC2;
                        }else if(recommendGoods[i].type == 5){
                            dataBox += strA1+strB+strC3;
                        }else{
                            dataBox += strA1+strB+strC4;
                        }

                    }else{
                        if(recommendGoods[i].type == 2){
                            dataBox += strA2+strB+strC1;
                        }else if(recommendGoods[i].type == 4){
                            dataBox += strA2+strB+strC2;
                        }else if(recommendGoods[i].type == 5){
                            dataBox += strA2+strB+strC3;
                        }else{
                            dataBox += strA2+strB+strC4;
                        }
                    }
                });

                $('.containerBox .cb-recommend').empty().append(dataBox);


                //数据加载完成后显示界面
                $('.containerBox .hd-noLoad').hide();
                $('.containerBox .hd-hasLoad').show();

                lazyLoad();


            } else {
                console.log("不是200的我不要")
            }

        };
    }


       loadData(url, true, successFunc);

}



//无限滚动
var infiniteScroll = function () {
    var page=1,url,successFunc;
    var pageName = $('.containerBox').attr('data-page');
    var pageId = GetQueryString('id');
    var offScroll = false;

    infinite('.containerBox','.page-cont',10,function () {

        //首页精选无限滚动开始
        if(pageName == 'index' && pageId == 1){

            url = $('.containerBox').attr('data-otherurl')+page;
            successFunc = function (data) {
                var dataBox = '';
                var recommendGoods = data.data;



                if(data.code == 200){

                    if( recommendGoods.length == 0){
                        if(!offScroll){
                            $('.containerBox').append("<div class='lastTips'><i class='left-line'></i>到底啦<i class='right-line'></i></div>");
                            $('.infinite-scroll-preloader').remove();
                            offScroll = true;
                        }
                    }


                    //商品推荐数据
                    dataBox = '';
                    var priceBig ,priceSmall;
                    $.each(recommendGoods,function (i) {
                        priceBig = String(recommendGoods[i].nowPrice).split('.')[0];
                        priceSmall = String(recommendGoods[i].nowPrice).split('.')[1];
                        if(!priceSmall){priceSmall = 0}

                        sendData = '\''+recommendGoods[i].type+','+ recommendGoods[i].id+'\'';

                        var strA1 = '<div class="item" onclick="sending('+ sendData+')">';
                        var strA2 = '<div class="item noPostage" onclick="sending('+ sendData+')">';
                        var strB = '<div class="imgBox"><img src="'+ recommendGoods[i].icoUrl +'"/></div><div class="name"><i><img src="http://file.diy.51app.cn/uu20/noPostage.png"/></i><span>'+ recommendGoods[i].name +'</span></div><div class="price"><i>&yen; <b>'+ priceBig +'.</b>'+ priceSmall +'</i><span>已售'+ recommendGoods[i].sell +'件</span></div>';
                        var strC1 = '<div class="label"><img src="http://file.diy.51app.cn/wp/icoImg/label_tuyin.png"/></div></div>';
                        var strC2 = '<div class="label"><img src="http://file.diy.51app.cn/wp/icoImg/label_kezi.png"/></div></div>';
                        var strC3 = '<div class="label"><img src="http://file.diy.51app.cn/wp/icoImg/label_keyin.png"/></div></div>';
                        var strC4 = '</div>';

                        if(recommendGoods[i].ispostage == 1){

                            if(recommendGoods[i].type == 2){
                                dataBox += strA1+strB+strC1;
                            }else if(recommendGoods[i].type == 4){
                                dataBox += strA1+strB+strC2;
                            }else if(recommendGoods[i].type == 5){
                                dataBox += strA1+strB+strC3;
                            }else{
                                dataBox += strA1+strB+strC4;
                            }

                        }else{
                            if(recommendGoods[i].type == 2){
                                dataBox += strA2+strB+strC1;
                            }else if(recommendGoods[i].type == 4){
                                dataBox += strA2+strB+strC2;
                            }else if(recommendGoods[i].type == 5){
                                dataBox += strA2+strB+strC3;
                            }else{
                                dataBox += strA2+strB+strC4;
                            }
                        }
                    });


                    $('.containerBox .cb-recommend').append(dataBox);
                    lazyLoad();
                    page ++;


                }else{
                    console.log("不是200的我不要");
                    if(!offScroll){
                        $('.containerBox').append("<div class='lastTips'><i class='left-line'></i>到底啦<i class='right-line'></i></div>");
                        $('.infinite-scroll-preloader').remove();
                        offScroll =true;
                    }
                }


            };

            loadData(url, true, successFunc);


        //首页精选无限滚动结束
        //首页其他页面无限滚动开始

        } else if(pageName == 'index' && pageId != 1){
            url = $('.containerBox').attr('data-otherurl')+"?id="+pageId+"&page="+page;
            successFunc = function (data) {


                if(data.code == 200){
                    var dataBox = '';
                    var recommendGoods = data.data;

                    if( recommendGoods.length == 0){
                        if(!offScroll){
                            $('.containerBox').append("<div class='lastTips'><i class='left-line'></i>到底啦<i class='right-line'></i></div>");
                            $('.infinite-scroll-preloader').remove();
                            offScroll = true;
                        }
                    }

                    //商品推荐数据
                    dataBox = '';
                    var priceBig ,priceSmall;
                    $.each(recommendGoods,function (i) {
                        priceBig = String(recommendGoods[i].nowPrice).split('.')[0];
                        priceSmall = String(recommendGoods[i].nowPrice).split('.')[1];
                        if(!priceSmall){priceSmall = 0}

                        sendData = '\''+recommendGoods[i].type+','+ recommendGoods[i].id+'\'';

                        var strA1 = '<div class="item" onclick="sending('+ sendData+')">';
                        var strA2 = '<div class="item noPostage" onclick="sending('+ sendData+')">';
                        var strB = '<div class="imgBox"><img src="'+ recommendGoods[i].icoUrl +'"/></div><div class="name"><i><img src="http://file.diy.51app.cn/uu20/noPostage.png"/></i><span>'+ recommendGoods[i].name +'</span></div><div class="price"><i>&yen; <b>'+ priceBig +'.</b>'+ priceSmall +'</i><span>已售'+ recommendGoods[i].sell +'件</span></div>';
                        var strC1 = '<div class="label"><img src="http://file.diy.51app.cn/wp/icoImg/label_tuyin.png"/></div></div>';
                        var strC2 = '<div class="label"><img src="http://file.diy.51app.cn/wp/icoImg/label_kezi.png"/></div></div>';
                        var strC3 = '<div class="label"><img src="http://file.diy.51app.cn/wp/icoImg/label_keyin.png"/></div></div>';
                        var strC4 = '</div>';

                        if(recommendGoods[i].ispostage == 1){

                            if(recommendGoods[i].type == 2){
                                dataBox += strA1+strB+strC1;
                            }else if(recommendGoods[i].type == 4){
                                dataBox += strA1+strB+strC2;
                            }else if(recommendGoods[i].type == 5){
                                dataBox += strA1+strB+strC3;
                            }else{
                                dataBox += strA1+strB+strC4;
                            }

                        }else{
                            if(recommendGoods[i].type == 2){
                                dataBox += strA2+strB+strC1;
                            }else if(recommendGoods[i].type == 4){
                                dataBox += strA2+strB+strC2;
                            }else if(recommendGoods[i].type == 5){
                                dataBox += strA2+strB+strC3;
                            }else{
                                dataBox += strA2+strB+strC4;
                            }
                        }
                    });

                    $('.containerBox .cb-recommend').append(dataBox);
                    lazyLoad();
                    page ++;

                }else{
                    console.log("不是200的我不要");
                    if(!offScroll){
                        $('.containerBox').append("<div class='lastTips'><i class='left-line'></i>到底啦<i class='right-line'></i></div>");
                        $('.infinite-scroll-preloader').remove();
                        offScroll = true;
                    }
                }

            };


            console.log('第'+pageId+'页的添加数据');
            loadData(url, true, successFunc);

        //首页其他页面无限滚动结束
        //分类页面无限滚动开始

        }else if(pageName == 'classify'){
            url = $('.containerBox').attr('data-url')+pageId+"&page="+page;
            successFunc = function (data) {

                if(data.code == 200){
                    var dataBox = '';
                    var recommendGoods = data.data;

                    if( recommendGoods.length == 0){
                        if(!offScroll){
                            $('.containerBox').append("<div class='lastTips'><i class='left-line'></i>到底啦<i class='right-line'></i></div>");
                            $('.infinite-scroll-preloader').remove();
                            offScroll = true;
                        }
                    }else{
                        //商品推荐数据
                        dataBox = '';
                        var priceBig ,priceSmall;
                        $.each(recommendGoods,function (i) {
                            priceBig = String(recommendGoods[i].nowPrice).split('.')[0];
                            priceSmall = String(recommendGoods[i].nowPrice).split('.')[1];
                            if(!priceSmall){priceSmall = 0}
                            sendData = '\''+recommendGoods[i].type+','+ recommendGoods[i].id+'\'';

                            var strA1 = '<div class="item" onclick="sending('+ sendData+')">';
                            var strA2 = '<div class="item noPostage" onclick="sending('+ sendData+')">';
                            var strB = '<div class="imgBox"><img src="'+ recommendGoods[i].icoUrl +'"/></div><div class="name"><i><img src="http://file.diy.51app.cn/uu20/noPostage.png"/></i><span>'+ recommendGoods[i].name +'</span></div><div class="price"><i>&yen; <b>'+ priceBig +'.</b>'+ priceSmall +'</i><span>已售'+ recommendGoods[i].sell +'件</span></div>';
                            var strC1 = '<div class="label"><img src="http://file.diy.51app.cn/wp/icoImg/label_tuyin.png"/></div></div>';
                            var strC2 = '<div class="label"><img src="http://file.diy.51app.cn/wp/icoImg/label_kezi.png"/></div></div>';
                            var strC3 = '<div class="label"><img src="http://file.diy.51app.cn/wp/icoImg/label_keyin.png"/></div></div>';
                            var strC4 = '</div>';

                            if(recommendGoods[i].ispostage == 1){

                                if(recommendGoods[i].type == 2){
                                    dataBox += strA1+strB+strC1;
                                }else if(recommendGoods[i].type == 4){
                                    dataBox += strA1+strB+strC2;
                                }else if(recommendGoods[i].type == 5){
                                    dataBox += strA1+strB+strC3;
                                }else{
                                    dataBox += strA1+strB+strC4;
                                }

                            }else{
                                if(recommendGoods[i].type == 2){
                                    dataBox += strA2+strB+strC1;
                                }else if(recommendGoods[i].type == 4){
                                    dataBox += strA2+strB+strC2;
                                }else if(recommendGoods[i].type == 5){
                                    dataBox += strA2+strB+strC3;
                                }else{
                                    dataBox += strA2+strB+strC4;
                                }
                            }
                        });

                        $('.containerBox .cb-recommend').append(dataBox);
                        lazyLoad();
                        page ++;
                    }



                }else{
                    console.log("不是200的我不要");
                    if(!offScroll){
                        $('.containerBox').append("<div class='lastTips'><i class='left-line'></i>到底啦<i class='right-line'></i></div>");
                        $('.infinite-scroll-preloader').remove();
                        offScroll = true;
                    }
                }

            };



            console.log('第'+pageId+'页的添加数据');
            loadData(url, true, successFunc);

        //分类页面无限滚动结束

        }else {
            url = $('.containerBox').attr('data-url')+"?storeId="+pageId+"&page="+page;
            successFunc = function (data) {

                if(data.code == 200){
                    var dataBox = '';
                    var recommendGoods = data.data;

                    if( recommendGoods.length == 0){
                        if(!offScroll){
                            $('.containerBox').append("<div class='lastTips'><i class='left-line'></i>到底啦<i class='right-line'></i></div>");
                            $('.infinite-scroll-preloader').remove();
                            offScroll = true;
                        }
                    }

                    //商品推荐数据
                    dataBox = '';
                    var priceBig ,priceSmall;
                    $.each(recommendGoods,function (i) {
                        priceBig = String(recommendGoods[i].nowPrice).split('.')[0];
                        priceSmall = String(recommendGoods[i].nowPrice).split('.')[1];
                        if(!priceSmall){priceSmall = 0}
                        sendData = '\''+recommendGoods[i].type+','+ recommendGoods[i].id+'\'';

                        var strA1 = '<div class="item" onclick="sending('+ sendData+')">';
                        var strA2 = '<div class="item noPostage" onclick="sending('+ sendData+')">';
                        var strB = '<div class="imgBox"><img src="'+ recommendGoods[i].icoUrl +'"/></div><div class="name"><i><img src="http://file.diy.51app.cn/uu20/noPostage.png"/></i><span>'+ recommendGoods[i].name +'</span></div><div class="price"><i>&yen; <b>'+ priceBig +'.</b>'+ priceSmall +'</i><span>已售'+ recommendGoods[i].sell +'件</span></div>';
                        var strC1 = '<div class="label"><img src="http://file.diy.51app.cn/wp/icoImg/label_tuyin.png"/></div></div>';
                        var strC2 = '<div class="label"><img src="http://file.diy.51app.cn/wp/icoImg/label_kezi.png"/></div></div>';
                        var strC3 = '<div class="label"><img src="http://file.diy.51app.cn/wp/icoImg/label_keyin.png"/></div></div>';
                        var strC4 = '</div>';

                        if(recommendGoods[i].ispostage == 1){

                            if(recommendGoods[i].type == 2){
                                dataBox += strA1+strB+strC1;
                            }else if(recommendGoods[i].type == 4){
                                dataBox += strA1+strB+strC2;
                            }else if(recommendGoods[i].type == 5){
                                dataBox += strA1+strB+strC3;
                            }else{
                                dataBox += strA1+strB+strC4;
                            }

                        }else{
                            if(recommendGoods[i].type == 2){
                                dataBox += strA2+strB+strC1;
                            }else if(recommendGoods[i].type == 4){
                                dataBox += strA2+strB+strC2;
                            }else if(recommendGoods[i].type == 5){
                                dataBox += strA2+strB+strC3;
                            }else{
                                dataBox += strA2+strB+strC4;
                            }
                        }
                    });

                    $('.containerBox .cb-recommend').append(dataBox);
                    lazyLoad();
                    page ++;

                }else{
                    console.log("不是200的我不要");
                    if(!offScroll){
                        $('.containerBox').append("<div class='lastTips'><i class='left-line'></i>到底啦<i class='right-line'></i></div>");
                        $('.infinite-scroll-preloader').remove();
                        offScroll = true;
                    }
                }

            };
        }




    });


}


$(document).ready(function () {

    (function changeFontSize() {
        var screenWidth = $(document).width();
        var htmlFontSize = screenWidth/6.4;
        $("html").css("font-size",htmlFontSize);
        $(window).resize(function(){
            screenWidth = $(document).width();
            htmlFontSize = screenWidth/6.4;
            $("html").css("font-size",htmlFontSize);
        });
    })();

    FastClick.attach(document.body);    //初始化fastclick



    loadFirst();                        //加载第一个精品页面

    // topSwiper();                         //初始化swiper

    infiniteScroll();                   //无限加载

});








