/**
 * Created by huangdian on 2016/12/12.
 */
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
    uqWyp.notifyInteraction(id);

}
function sendOC(sendObj) {
    console.log(sendObj);
    connectNZOCJSBridge(function(bridge) {
        bridge.send(sendObj, function(responseData) {})
    });
}

//获取地址栏参数
function GetQueryString(name)
{
    var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");
    var r = window.location.search.substr(1).match(reg);
    if(r!=null)return  unescape(r[2]); return null;
}

//加载数据
function loadData(url, async, successFunc) {
    console.log("请求数据了");
    $.ajax({
        url: url,
        type: 'GET',
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
//无限加载  window为容器的时候
function infinite(content,distance,loadMore) {
    var loading = false;
    $(window).on('scroll',function () {
        if (loading) return;
        loading = true;
        setTimeout(function () {
            var pageHeight = $(content).height(),
                containerHeight = $(window).height();

            if(pageHeight - $(window).scrollTop() < containerHeight + distance){
                loadMore();
            }
            loading = false;

        },1000);

    });
}

//加减计算
function accAdd(arg1, arg2) {
    var r1, r2, m, c;
    try {
        r1 = arg1.toString().split(".")[1].length;
    }
    catch (e) {
        r1 = 0;
    }
    try {
        r2 = arg2.toString().split(".")[1].length;
    }
    catch (e) {
        r2 = 0;
    }
    c = Math.abs(r1 - r2);
    m = Math.pow(10, Math.max(r1, r2));
    if (c > 0) {
        var cm = Math.pow(10, c);
        if (r1 > r2) {
            arg1 = Number(arg1.toString().replace(".", ""));
            arg2 = Number(arg2.toString().replace(".", "")) * cm;
        } else {
            arg1 = Number(arg1.toString().replace(".", "")) * cm;
            arg2 = Number(arg2.toString().replace(".", ""));
        }
    } else {
        arg1 = Number(arg1.toString().replace(".", ""));
        arg2 = Number(arg2.toString().replace(".", ""));
    }
    return (arg1 + arg2) / m;
}
function accSub(arg1, arg2) {
    var r1, r2, m, n;
    try {
        r1 = arg1.toString().split(".")[1].length;
    }
    catch (e) {
        r1 = 0;
    }
    try {
        r2 = arg2.toString().split(".")[1].length;
    }
    catch (e) {
        r2 = 0;
    }
    m = Math.pow(10, Math.max(r1, r2)); //last modify by deeka //动态控制精度长度
    n = (r1 >= r2) ? r1 : r2;
    return ((arg1 * m - arg2 * m) / m).toFixed(n);
}

//自动消失的弹窗
function autoPopup(isOk) {

    $('.autoPopupBox').show();
    $('.autoPopup').show();
    setTimeout(function () {
        $('.autoPopup').addClass('act');
        setTimeout(function () {
            if(isOk == 'true'){

                $('.autoPopup span').text('添加到购物车成功');
                $('.autoPopup .ok').show();
                $('.autoPopup .no').hide();
            }else{
                $('.autoPopup span').text('添加到购物车失败');
                $('.autoPopup .no').show();
                $('.autoPopup .ok').hide();
            }
        },200);
    },200);
    setTimeout(function () {
        setTimeout(function () {
            $('.autoPopupBox').hide();
            $('.autoPopup').hide().removeClass('act');
            $('.autoPopup span').text('');
            $('.autoPopup i img').hide();
        },200);
    },1000)
}



var page = 1;
var offScroll=false;
//店铺id
var id= GetQueryString('id');
//用户id
var userId= GetQueryString('userId');
//用户刚进来时已有的金额或者数量
var alreadySumStart= GetQueryString('alreadySum');
//满足活动的条件
var condition= GetQueryString('condition');
//满足条件后的优惠
var privilege= GetQueryString('privilege');
//活动id，传给后台和店铺id一起获取商品列表 0就是免运费的
var activityId= GetQueryString('activityId');

//活动的类型 有1,2,4,8    0就是免运费的
//1是满100减10元，2是第二件半价，4是三件99元，8是满2件元8折
var type= GetQueryString('type');
var lackSum = accSub(condition,alreadySumStart);

if(type==0){
    $('.bottomTips .already').html('商品共计： <i>'+alreadySumStart+'</i> 元');
    if(lackSum > 0){
        $('.bottomTips .lack').html('还差：<i>'+lackSum+'</i> 元即可享免运费服务');
    }else{
        $('.bottomTips .lack').text('已免邮费');
    }
}else if(type==1){
    $('.bottomTips .already').html('满减商品共计： <i>'+alreadySumStart+'</i> 元');
    if(lackSum > 0){
        $('.bottomTips .lack').html('还差：<i>'+lackSum+'</i> 元即可享减'+ privilege +'元优惠');
    }else{
        $('.bottomTips .lack').text('已享满'+ condition +'减'+ privilege +'优惠');
    }

}else if(type==2){
    privilege = privilege*10;
    $('.bottomTips .already').html('折扣商品共计： <i>'+alreadySumStart+'</i> 件');
    if(lackSum > 0){
        $('.bottomTips .lack').html('还差：<i>'+lackSum+'</i> 件即可享第'+ condition +'件'+ privilege +'折优惠');
    }else{
        $('.bottomTips .lack').text('已享第'+ condition +'件'+ privilege +'折优惠');
    }


}else if(type==4){
    $('.bottomTips .already').html('活动商品共计： <i>'+alreadySumStart+'</i> 件');
    if(lackSum > 0){
        $('.bottomTips .lack').html('还差：<i>'+lackSum+'</i> 件即可享'+ condition +'件'+ privilege +'元优惠');
    }else{
        $('.bottomTips .lack').text('已享'+ condition +'件'+ privilege +'元优惠');
    }

}else if(type==8){
    privilege = privilege*10;
    $('.bottomTips .already').html('活动商品共计： <i>'+alreadySumStart+'</i> 件');
    if(lackSum > 0){
        $('.bottomTips .lack').html('还差：<i>'+lackSum+'</i> 件即可享'+ privilege +'折优惠');
    }else{
        $('.bottomTips .lack').text('已享'+ privilege +'折优惠');
    }
}




var goodsUrl =$('.containerBox').attr('data-goods')+'?id='+id+'&activityId='+activityId+'&page=0';

var successFuncGoods=function (data) {

    if(data.code == 200){
        var dataBox='';
        var sendData = '';
        var priceBig ,priceSmall;
        $.each(data.data,function (i) {
            priceBig = String(data.data[i].nowPrice).split('.')[0];
            priceSmall = String(data.data[i].nowPrice).split('.')[1];
            if(!priceSmall){priceSmall = 0}
            sendData = '\'1,'+ data.data[i].goodsId+'\'';

            dataBox += '<div class=" item clearfix">' +
                '<div class="imgBox" onclick="sending('+ sendData +')"><img src="'+ data.data[i].icoUrl +'"/></div>' +
                '<div class="info"><p onclick="sending('+ sendData +')">'+ data.data[i].name +'</p>' +
                '<div class="price">&yen; <i>'+ priceBig +'.</i><b>'+ priceSmall +'</b></div>' +
                '<div class="buy" data-goodsId="'+data.data[i].goodsId +'" data-price="'+ data.data[i].nowPrice +'" ><img src="http://file.diy.51app.cn/uu20/makeUp-buy.png"/></div>' +
                '</div>' +
                '</div>' +
                '<div class="line"></div>';
        });
        $('.makeUpIndent').empty().append(dataBox);

        //数据加载完成后显示界面
        $('.containerBox .hd-noLoad').hide();
        $('.containerBox .hd-hasLoad').show();
        $('.bottomTips').show();

        jisuan();


    }

};
loadData(goodsUrl, true, successFuncGoods);



//点击加入购物车
function jisuan() {

    var alreadySum,lackSum,compute;

    $('.makeUpIndent .item .info .buy').off();
    $('.makeUpIndent .item .info .buy').click(function () {
        var goodsId = $(this).attr('data-goodsId');
        var thisSum = Number($(this).attr('data-price'));

        var joinCarUrl = $('.containerBox').attr('data-join')+userId+'&id='+goodsId;

        var joinCarSuccess=function (data) {

            if(data.code == 200){
                alreadySum = Number($('.bottomTips .already i').text());
                //1是满100减10元，2是第二件半价，4是三件99元，8是满2件元8折
                if(type==0){
                    compute = accAdd(alreadySum,thisSum);
                    lackSum = accSub(condition,compute);
                    if(lackSum > 0){
                        $('.bottomTips .lack i').text(lackSum);
                    }else{
                        $('.bottomTips .lack').text('已免邮费');
                    }
                }else if(type==1){
                    compute = accAdd(alreadySum,thisSum);

                    lackSum = accSub(condition,compute);
                    if(lackSum > 0){
                        $('.bottomTips .lack i').text(lackSum);
                    }else{
                        $('.bottomTips .lack').text('已享满'+ condition +'减'+ privilege +'优惠');
                    }
                }else if(type==2){
                    compute = ++alreadySum;
                    console.log(compute);
                    lackSum = accSub(condition,compute);
                    if(lackSum > 0){
                        $('.bottomTips .lack i').text(lackSum);
                    }else{
                        $('.bottomTips .lack').text('已享第'+ condition +'件'+ privilege +'折优惠');
                    }
                }else if(type==4){
                    compute = ++alreadySum;
                    lackSum = accSub(condition,compute);
                    if(lackSum > 0){
                        $('.bottomTips .lack i').text(lackSum);
                    }else{
                        $('.bottomTips .lack').text('已享'+ condition +'件'+ privilege +'元优惠');
                    }
                }else if(type==8){
                    compute = ++alreadySum;
                    lackSum = accSub(condition,compute);
                    if(lackSum > 0){
                        $('.bottomTips .lack i').text(lackSum);
                    }else{
                        $('.bottomTips .lack').text('已享'+ privilege +'折优惠');
                    }
                }


                $('.bottomTips .already i').text(compute);
                autoPopup('true')


            }else{
                autoPopup('false')
            }
        };
        loadData(joinCarUrl, true, joinCarSuccess);


    })
}



//加载下一页商品
function loadMore() {
    goodsUrl = $('.containerBox').attr('data-goods')+'?id='+id+'&activityId='+activityId+'&page='+page;
    successFuncGoods=function (data) {

        if(data.code == 200){
            var dataBox='';
            var priceBig ,priceSmall;
            $.each(data.data,function (i) {
                priceBig = String(data.data[i].nowPrice).split('.')[0];
                priceSmall = String(data.data[i].nowPrice).split('.')[1];
                if(!priceSmall){priceSmall = 0}
                var sendData = '\'1,'+ data.data[i].goodsId+'\'';

                dataBox += '<div class="item clearfix">' +
                    '<div class="imgBox" onclick="sending('+ sendData +')"><img src="'+ data.data[i].icoUrl +'"/></div>' +
                    '<div class="info"><p onclick="sending('+ sendData +')">'+ data.data[i].name +'</p>' +
                    '<div class="price">&yen; <i>'+ priceBig +'.</i><b>'+ priceSmall +'</b></div>' +
                    '<div class="buy" data-goodsId="'+data.data[i].goodsId +'" data-price="'+ data.data[i].nowPrice +'" ><img src="http://file.diy.51app.cn/uu20/makeUp-buy.png"/></div>' +
                    '</div>' +
                    '</div>' +
                    '<div class="line"></div>';
            });
            $('.makeUpIndent').append(dataBox);
            page++;
            jisuan();
        }else{
            if(!offScroll){
//                        $('.containerBox').append("<div class='lastTips'>"+  data.code+data.message+"(测试文字)</div>");
                $('.infinite-scroll-preloader').remove();
                offScroll =true;
            }

        }

    };
    loadData(goodsUrl, true, successFuncGoods);

}
infinite('.page-cont',10,loadMore);


var ScrollFix = function(elem) {
    // Variables to track inputs
    var startY, startTopScroll;
    elem = elem || document.querySelector(elem);
    // If there is no element, then do nothing
    if(!elem)
        return;
    // Handle the start of interactions
    elem.addEventListener('touchstart', function(event){
        startY = event.touches[0].pageY;
        startTopScroll = elem.scrollTop;

        if(startTopScroll <= 0)
            elem.scrollTop = 1;

        if(startTopScroll + elem.offsetHeight >= elem.scrollHeight)
            elem.scrollTop = elem.scrollHeight - elem.offsetHeight - 1;
    }, false);
};

var scrollable = document.getElementById("scrollable");
new ScrollFix(scrollable);


