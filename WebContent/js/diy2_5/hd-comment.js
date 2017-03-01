
FastClick.attach(document.body);    //初始化fastclick

//获取地址栏参数
function GetQueryString(name)
{
    var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");
    var r = window.location.search.substr(1).match(reg);
    if(r!=null)return  unescape(r[2]); return null;
}

var outsetNav = new Swiper ('.swiper-outsetNav', {
//        onlyExternal : true,
    pagination : '.headerNav',
    paginationClickable :true,

    paginationBulletRender: function (swiper,index, className) {
        switch (index) {
            case 0: name='全部';break;
            case 1: name='好评';break;
            case 2: name='中评';break;
            case 3: name='差评';break;
            case 4: name='晒图';break;
            default: name='';
        }
        return '<span class="item ' + className + '">' + name + '<br/><i>0</i></span>';
    }

});




//加载数据
function loadData(url, async, successFunc) {
    console.log("请求数据了")
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


var id= GetQueryString('goodsId');

//获取评论数量
function getCommentNum() {
    var url =   $('.main').attr('data-num')+'?goodsId='+ id ;
    var successFunc =function (data) {
        console.log(data.data);
        if(data.code == 200){
            $.each(data.data,function (i) {
                $('.headerNav .item').eq(i).find('i').text(data.data[i].number)
            })
        }

    };
    loadData(url, true, successFunc);

}
getCommentNum();

//获取评论图片
function getCommentImg() {
    var url =   $('.main').attr('data-img')+'?goodsId='+ id ;
    var successFunc =function (data) {
        console.log(data.data);
        if(data.code == 200){
            var dataBox ='';
            $.each(data.data,function (i) {
                dataBox += '<div class="showImgBox"><img src="'+  data.data[i].imgUrl+'"/></div>'
            });
            $('.showImg').empty().append(dataBox);
        }

    };
    loadData(url, true, successFunc);

}
getCommentImg();

//    $('.headerNav .item').eq(4).click(function () {
//        getCommentImg();
//    });



//获得首屏评论的数据
function getdata(evalType,where) {
    var url =  $('.main').attr('data-url')+'?goodsId='+ id +'&evalType='+ evalType +'&page=1';
    var successFunc = function (data) {
        var dataBox = '';

        if(data.code == 200){

            $.each(data.data,function (i) {
                var imgBox ='';
                var imgArr;

                if(data.data[i].imgUrl){

                    imgArr = data.data[i].imgUrl.split(",");
                    for(var j=0;j<imgArr.length;j++){
                        imgBox += '<span><img src="'+ imgArr[j] +'"/></span>';
                    }

                    dataBox += '<li>' +
                        '<div class="user wordBreak"><i><img src="http://test.diy.51app.cn//diyMall2/images/default-logo.png"/></i>'+ data.data[i].mobile +'</div>' +
                        '<div class="text wordBreak">'+ data.data[i].content +'</div>' +
                        '<div class="time wordBreak">'+ data.data[i].creatTime +' </div><div class="imgBox">'+ imgBox +'</div>' +
                        '</li>';
                }else{
                    dataBox += '<li>' +
                        '<div class="user wordBreak"><i><img src="http://test.diy.51app.cn//diyMall2/images/default-logo.png"/></i>'+ data.data[i].mobile +'</div>' +
                        '<div class="text wordBreak">'+ data.data[i].content +'</div>' +
                        '<div class="time wordBreak">'+ data.data[i].creatTime +' </div>' +
                        '</li>';
                }

            });
            $(where).empty().append(dataBox);

        }

    };
    loadData(url, true, successFunc);



}
//全部的数据
getdata(1,'.all .comment-details');
//好评的数据
getdata(1,'.good .comment-details');
//中评的数据
getdata(2,'.medium .comment-details');
//差评的数据
getdata(2,'.bad .comment-details');

//    $('.headerNav .item').eq(1).click(function () {
//
//    });
//    $('.headerNav .item').eq(2).click(function () {
//
//    });
//    $('.headerNav .item').eq(3).click(function () {
//
//    });



//无限加载

function infinite(container,content,distance,loadMore) {
    var loading = false;
    $(container).on('scroll',function () {
        if (loading) return;
        loading = true;
        setTimeout(function () {
            var that = $(container),
                pageHeight = that.find(content).height(),
                containerHeight = that.height();

            if(pageHeight - that.scrollTop() < containerHeight + distance){
                loadMore();
            }
            loading = false;

        },1000);

    });

}

function moreData(container,content,evalType,where) {
    var page=2;
    infinite(container,content,10,function () {
        url =  $('.main').attr('data-url')+'?goodsId='+ id +'&evalType='+ evalType +'&page='+page;
        successFunc = function (data) {
            var dataBox = '';

            if(data.code == 200){

                $.each(data.data,function (i) {
                    var imgBox ='';
                    var imgArr;

                    if(data.data[i].imgUrl){

                        imgArr = data.data[i].imgUrl.split(",");
                        for(var j=0;j<imgArr.length;j++){
                            imgBox += '<span><img src="'+ imgArr[j] +'"/></span>';
                        }

                        dataBox += '<li>' +
                            '<div class="user wordBreak"><i><img src="http://test.diy.51app.cn//diyMall2/images/default-logo.png"/></i>'+ data.data[i].mobile +'</div>' +
                            '<div class="text wordBreak">'+ data.data[i].content +'</div>' +
                            '<div class="time wordBreak">'+ data.data[i].creatTime +' </div><div class="imgBox">'+ imgBox +'</div>' +
                            '</li>';
                    }else{
                        dataBox += '<li>' +
                            '<div class="user wordBreak"><i><img src="http://test.diy.51app.cn//diyMall2/images/default-logo.png"/></i>'+ data.data[i].mobile +'</div>' +
                            '<div class="text wordBreak">'+ data.data[i].content +'</div>' +
                            '<div class="time wordBreak">'+ data.data[i].creatTime +' </div>' +
                            '</li>';
                    }

                });
                $(where).append(dataBox);
                page++;
            }

        };
        loadData(url, true, successFunc);

    })

}

moreData('.containerBox.good','.containerBox.good .comment-details',1,'.good .comment-details');
moreData('.containerBox.medium','.containerBox.medium .comment-details',2,'.medium .comment-details');
moreData('.containerBox.bad','.containerBox.bad .comment-details',3,'.bad .comment-details');


//全部评论的加载
function moreDataAll() {
    var pageG=2,pageM=1,pageB=1;
    infinite('.containerBox.all','.containerBox.all .comment-details',10,function () {

        url =  $('.main').attr('data-url')+'?goodsId='+ id +'&evalType=1&page='+pageG;

        successFunc = function (data) {
            var dataBox = '';

            if (data.code == 200) {

                $.each(data.data, function (i) {
                    var imgBox = '';
                    var imgArr;

                    if (data.data[i].imgUrl) {

                        imgArr = data.data[i].imgUrl.split(",");
                        for (var j = 0; j < imgArr.length; j++) {
                            imgBox += '<span><img src="' + imgArr[j] + '"/></span>';
                        }

                        dataBox += '<li>' +
                            '<div class="user wordBreak"><i><img src="http://test.diy.51app.cn//diyMall2/images/default-logo.png"/></i>' + data.data[i].mobile + '</div>' +
                            '<div class="text wordBreak">' + data.data[i].content + '</div>' +
                            '<div class="time wordBreak">' + data.data[i].creatTime + ' </div><div class="imgBox">' + imgBox + '</div>' +
                            '</li>';
                    } else {
                        dataBox += '<li>' +
                            '<div class="user wordBreak"><i><img src="http://test.diy.51app.cn//diyMall2/images/default-logo.png"/></i>' + data.data[i].mobile + '</div>' +
                            '<div class="text wordBreak">' + data.data[i].content + '</div>' +
                            '<div class="time wordBreak">' + data.data[i].creatTime + ' </div>' +
                            '</li>';
                    }

                });
                $('.all .comment-details').append(dataBox);
                pageG++;
            }else if(data.code == 400){

                url =  $('.main').attr('data-url')+'?goodsId='+ id +'&evalType=2&page='+pageM;

                var successFunc = function (data) {

                    var dataBox = '';

                    if (data.code == 200) {

                        $.each(data.data, function (i) {
                            var imgBox = '';
                            var imgArr;

                            if (data.data[i].imgUrl) {

                                imgArr = data.data[i].imgUrl.split(",");
                                for (var j = 0; j < imgArr.length; j++) {
                                    imgBox += '<span><img src="' + imgArr[j] + '"/></span>';
                                }

                                dataBox += '<li>' +
                                    '<div class="user wordBreak"><i><img src="http://test.diy.51app.cn//diyMall2/images/default-logo.png"/></i>' + data.data[i].mobile + '</div>' +
                                    '<div class="text wordBreak">' + data.data[i].content + '</div>' +
                                    '<div class="time wordBreak">' + data.data[i].creatTime + ' </div><div class="imgBox">' + imgBox + '</div>' +
                                    '</li>';
                            } else {
                                dataBox += '<li>' +
                                    '<div class="user wordBreak"><i><img src="http://test.diy.51app.cn//diyMall2/images/default-logo.png"/></i>' + data.data[i].mobile + '</div>' +
                                    '<div class="text wordBreak">' + data.data[i].content + '</div>' +
                                    '<div class="time wordBreak">' + data.data[i].creatTime + ' </div>' +
                                    '</li>';
                            }

                        });
                        $('.all .comment-details').append(dataBox);
                        pageM++;
                    }else if(data.code == 400){
                        url =  $('.main').attr('data-url')+'?goodsId='+ id +'&evalType=3&page='+pageB;

                        var successFunc = function (data) {

                            var dataBox = '';

                            if (data.code == 200) {

                                $.each(data.data, function (i) {
                                    var imgBox = '';
                                    var imgArr;

                                    if (data.data[i].imgUrl) {

                                        imgArr = data.data[i].imgUrl.split(",");
                                        for (var j = 0; j < imgArr.length; j++) {
                                            imgBox += '<span><img src="' + imgArr[j] + '"/></span>';
                                        }

                                        dataBox += '<li>' +
                                            '<div class="user wordBreak"><i><img src="http://test.diy.51app.cn//diyMall2/images/default-logo.png"/></i>' + data.data[i].mobile + '</div>' +
                                            '<div class="text wordBreak">' + data.data[i].content + '</div>' +
                                            '<div class="time wordBreak">' + data.data[i].creatTime + ' </div><div class="imgBox">' + imgBox + '</div>' +
                                            '</li>';
                                    } else {
                                        dataBox += '<li>' +
                                            '<div class="user wordBreak"><i><img src="http://test.diy.51app.cn//diyMall2/images/default-logo.png"/></i>' + data.data[i].mobile + '</div>' +
                                            '<div class="text wordBreak">' + data.data[i].content + '</div>' +
                                            '<div class="time wordBreak">' + data.data[i].creatTime + ' </div>' +
                                            '</li>';
                                    }

                                });
                                $('.all .comment-details').append(dataBox);
                                pageB++;
                            }else if(data.code == 400){
                                console.log("好评，中评，差评的都加载完了");

                            }
                        };
                        loadData(url, true, successFunc);

                    }
                };
                loadData(url, true, successFunc);
            }

        };
        loadData(url, true, successFunc);

    })

}
moreDataAll();


//图片预览
$(document).on("click",".showImgBox,.imgBox span",function(){
    var _this = $(this);
    var index = $(this).index();
    var commentImgBox = ($(this).parent().hasClass("imgBox")) ? ($(this).parent(".imgBox")):($(this).parent(".showImg"));
    var img = commentImgBox.find("img");
    var data = commentImgBox.find("img").attr("src");

    $.each(img,function(i,d){

        var _this = $(this);
        var src = _this.attr("src");
        $(".swiper-imgShow .swiper-wrapper").append("<div class='swiper-slide'><div class='swiper-zoom-container'><img src='"+src+"'/></div></div>");
    });
    $(".popLayer").removeClass("hide photo-browser-out").addClass("photoBrowserIn");
    var imgShow = new Swiper('.swiper-imgShow', {
        zoom : true,
        initialSlide:index,
        observeParents:true,
        observer:true,
        pagination : '.swiper-imgShow .swiper-pagination',
        paginationType : 'fraction',
        onClick: function(swiper){
            $(".popLayer").find(".swiper-slide").not(".swiper-slide-active").remove();
            $(".popLayer").removeClass("photoBrowserIn").addClass('photo-browser-out');
            imgShow.destroy(false,true);
            setTimeout(function(){ $(".swiper-imgShow .swiper-wrapper,.swiper-pagination").empty();$(".popLayer").addClass("hide").removeClass("photo-browser-out")},400);
            return false;
        }
    });
});


//回到顶部
function goTop() {

    $(".containerBox").on("scroll",function(){
        var top = $(".containerBox").scrollTop();
        if(top>500)  $(".gotoTop").show();
        if(top<500)  $(".gotoTop").hide();
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
        $(".containerBox").scrollTo(1);
    });


}
goTop();

