


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

function lazyLoad() {

    $("img").lazyload({
        data_attribute     :'echo',
        load:function ($elements, elements_left, options) {
            $elements.removeClass('placeholder')
        },
        container:$('.containerBox'),
    });

}


//加载数据
function loadData(url, async, successFunc) {
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

//初始化swiper
function pageSwiper() {

    var indexNav = new Swiper ('.swiper-indexNav', {
        onlyExternal : true,
        pagination : '.indexNaV-pagination',
        paginationClickable :true,

        paginationBulletRender: function (index, className) {
            switch (index) {
                case 0: name='热荐热卖';break;
                case 1: name='定制商品';break;
                case 2: name='手机配件';break;
                case 3: name='家居装饰';break;
                case 4: name='衣服鞋帽';break;
                case 5: name='新奇玩具';break;
                case 6: name='电子数码';break;
                case 7: name='其他';break;
                default: name='';
            }
            return '<span isLoad="false" class="item ' + className + '">' + name + '</span>';
        }

    });


}



//加载首屏数据
function loadFirst() {

       var url = 'http://192.168.1.249:8081/diyMall/index/topNavInfoByPage.do?id=14&page=0';
       var successFunc = function (data) {
           var dataBox = '';
           var recommendGoods   = data.data;
           var sendData= '';

           if(data.code == 200){

               //商品数据
               dataBox = '';
               $.each(recommendGoods,function (i) {
                   sendData = '\''+recommendGoods[i].type+','+ recommendGoods[i].id+'\'';
                   dataBox += '<a href="javascript:void(0);" class="item" onclick="sending('+ sendData+')"><div class="imgBox"><img class="placeholder" src="'+ recommendGoods[i].icoUrl +'"/></div><div class="name"><i>包邮</i><span>'+ recommendGoods[i].name +'</span></div><div class="price"><i>&yen; <b>'+ recommendGoods[i].nowPrice +'.</b>0</i><span>已售'+ recommendGoods[i].sell +'件</span></div></a>';
               });

               $('.classify-page1 .index-goods').empty().append(dataBox);


           }else{
               console.log("不是200的我不要")
           }



           $('.indexNaV-pagination .item').eq(0).attr('isLoad','true');

       };

       loadData(url, true, successFunc);

}

//点解切换页面
function clickTab() {

    $('.nav-more').click(function () {

        $('.hideNav').css('top','0px');
    });
    $('.hideIcon').click(function () {

        $('.hideNav').css('top','-800px');
    });

    console.log($('.indexNaV-pagination .item').length);

    var timer;
    timer=setInterval(function () {


        if($('.indexNaV-pagination .item').length != 0){

            console.log("chulaile ");

            $('.indexNaV-pagination .item').click(function () {

                var slideIndex = $(this).index();

                //导航栏滚动条位置
                var scrollLeft = 50*slideIndex;

                $('.headerNav').scrollLeft(scrollLeft);

                $('.hideNav').css('top','-800px');
            });
            clearTimeout(timer);

        }else {
            console.log("meiyou")
        }


    },100);

    $(document).on('infinite', '.classify-page2',function() {
        console.log("第2屏")
    });



    // $('.indexNaV-pagination .item').click(function () {
    //
    //     var slideIndex = $(this).index();
    //     var This = $(this);
    //     var isLoad = $(this).attr('isLoad');
    //     var id =  slideIndex+1;
    //     //导航栏滚动条位置
    //     var scrollLeft = 10*slideIndex;
    //
    //     $('.headerNav').scrollLeft(scrollLeft);
    //
    //     $('.swiper-indexNav .index-page').eq(slideIndex).addClass('act').siblings().removeClass('act');
    //
    //         var url = 'http://192.168.1.247:8081/index/topNavInfo.do?id='+id;
    //
    //         var successFunc = function (data) {
    //             var dataBox = '';
    //             var banners = data.data.banners,
    //                 recommendGoods = data.data.recommendGoods;
    //
    //
    //             if(data.code == 200){
    //                 console.log("到这了");
    //
    //                 //page内的轮播数据
    //                 dataBox = '';
    //                 $.each(banners,function (i) {
    //                     dataBox += '<div class="swiper-slide"><a class="bannerItem" href="##"><img src="'+ banners[i].imgUrl +'"/></a></div>';
    //                 });
    //                 $('.swiper-indexNav .index-page.act .cb-banner .swiper-wrapper').empty().append(dataBox);
    //
    //                 topSwiper();
    //
    //
    //                 //商品推荐数据
    //                 dataBox = '';
    //                 $.each(recommendGoods,function (i) {
    //                     dataBox += '<a href="##" class="item"><div class="imgBox"><img src="'+ recommendGoods[i].icoUrl +'"/></div><div class="name"><i>包邮</i><span>'+ recommendGoods[i].name +'</span></div><div class="price"><i>&yen; <b>'+ recommendGoods[i].nowPrice +'.</b>0</i><span>已售'+ recommendGoods[i].sell +'件</span></div></a>';
    //                 });
    //
    //                 $('.swiper-indexNav .index-page.act .cb-recommend').empty().append(dataBox);
    //
    //
    //                 This.attr('isLoad','true');
    //
    //             }else{
    //                 console.log("不是200的我不要")
    //             }
    //
    //
    //         };
    //
    //         if(isLoad == 'false' ){
    //
    //             loadData(url, true, successFunc);
    //         }
    //
    //
    //         //解除之前绑定的refresh事件，然后再绑定一个
    //         $(document).off('refresh', '.keji');
    //         $(document).on('refresh', '.keji',function(e) {
    //             // 模拟2s的加载过程
    //             setTimeout(function() {
    //                 console.log('keji');
    //                 // 加载完毕需要重置
    //                 // $('.swiper-indexNav .index-page').eq(slideIndex).find('.containerBox').before("hahahah");
    //                 loadData(url, true, successFunc);
    //                 $.pullToRefreshDone('.keji');
    //             }, 2000);
    //
    //         });

    
    // });
}

//第一个页面的下拉刷新
function firstRefresh() {
    $(document).on('refresh', '.classify-page1',function(e) {
        // 模拟2s的加载过程
        setTimeout(function() {
            console.log("刷新了111111");
            // 加载完毕需要重置

            // $('.swiper-indexNav .index-page').eq(0).find('.containerBox').before("123456");
            // loadFirst();

            $.pullToRefreshDone('.classify-page1');
        }, 2000);


    });


    $(document).on('refresh', '.classify-page2',function(e) {
        // 模拟2s的加载过程
        setTimeout(function() {
            console.log("刷新222");
            // 加载完毕需要重置

            // $('.swiper-indexNav .index-page').eq(0).find('.containerBox').before("123456");
            // loadFirst();

            $.pullToRefreshDone('.classify-page2');
        }, 2000);


    });
}

//第一个页面的无限滚动
function firstInfiniteScroll() {


    var loading = false;
    var page = 1;
    // 注册'infinite'事件处理函数
    $(document).on('infinite', '.classify-page1',function() {
        console.log("第一页")
        // 如果正在加载，则退出
        if (loading) return;

        // 设置flag
        loading = true;

        // 模拟1s的加载过程
        setTimeout(function() {
            // 重置加载flag
            loading = false;
            var url = 'http://192.168.1.249:8081/diyMall/index/topNavInfoByPage.do?id=14&page='+page;
            var successFunc = function (data) {

                var dataBox = '';
                var recommendGoods = data.data;
                var sendData = '';

                if(data.code == 200){

                    //商品推荐数据
                    dataBox = '';
                    $.each(recommendGoods,function (i) {
                        sendData = '\''+recommendGoods[i].type+','+ recommendGoods[i].id+'\'';
                        dataBox += '<a href="javascript:void(0);" class="item" onclick="sending('+ sendData+')"><div class="imgBox"><img class="placeholder" src="//placeholdit.sinaapp.com/600?text=YouQi" data-echo="'+ recommendGoods[i].icoUrl +'"/></div><div class="name"><i>包邮</i><span>'+ recommendGoods[i].name +'</span></div><div class="price"><i>&yen; <b>'+ recommendGoods[i].nowPrice +'.</b>0</i><span>已售'+ recommendGoods[i].sell +'件</span></div></a>';
                    });

                    $('.classify-page1 .index-goods').append(dataBox);
                    lazyLoad();
                    page ++;

                }else{
                    console.log("不是200的我不要")
                }

            };

            if (page >= 4) {
                $('.classify-page1 .index-goods').append("<div class='lastTips'>我们是有底线的</div>");
                // 加载完毕，则注销无限加载事件，以防不必要的加载
                $.detachInfiniteScroll($('.infinite-scroll'));
                // 删除加载提示符
                $('.infinite-scroll-preloader').remove();
                return;
            }else{
                console.log('无限无限无爱心');
                loadData(url, true, successFunc);

            }

            //容器发生改变,如果是js滚动，需要刷新滚动
            $.refreshScroller();
        }, 1000);
    });
}







$(document).ready(function () {

    $.init();                           //初始化sui
    pageSwiper();                         //初始化swiper

    loadFirst();                        //加载第一个精品页面

    setTimeout(function () {            //点击切换页面
        clickTab();
    },100);


    firstRefresh();                     //第一个精品页面的下拉刷新
    firstInfiniteScroll();              //第一个精品页面的无限加载
});







