<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
    String path=request.getContextPath();
%>


<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, minimum-scale=1, user-scalable=no">
    <meta name="apple-mobile-web-app-capable" content="yes">
    <meta name="apple-mobile-web-app-status-bar-style" content="black">
    <meta content="telephone=no,email=no" name="format-detection">
    <meta name="App-Config" content="fullscreen=yes,useHistoryState=yes,transition=yes">
    <title>商品详情</title>
    <link rel="stylesheet" href="<%=path %>/css/diy2_5/swiper-3.3.1.min.css">
    <link type="text/css" rel="stylesheet" href="<%=path %>/css/diy2_5/hd-index.css"/>
    <style type="text/css">
       /* .main,.swiper-outsetNav,.swiper-outsetNav .swiper-wrapper{
            height:100%;
        }*/
        .swiper-container{  overflow-y: visible; overflow-x:hidden;  }

        .headerNav{
            width: 100%;
            background: #fff;
            overflow: hidden;
            border-bottom: 1px solid #dfdfdf;
            padding:0 10px;
            position: fixed;
            top:0;
            z-index: 888;
        }
        .headerNav .item{
            float: left;
            width: 33%;
            text-align: center;
            height:40px;
            line-height: 40px;
            border-radius: 0;
            background: transparent;
            opacity:1;
            color: #222222;
        }
        .headerNav .item.swiper-pagination-bullet-active{
            color: rgb(254,62,101);
        }
        .info .info-video{
            padding:0 25px 20px;
        }
        .info h4{
            margin: 5px 0;
            color: #c8c8c8;
        }
        .info video{
            width: 100%;
            margin: 0 auto;
            display: block;
        }
        .info img{
         width: 100%;
        }

        .p-item{
            border-bottom: 1px solid #dfdfdf;
            padding:10px 10px;
            overflow: hidden;
        }
        .p-item .left{
            float: left;
            width: 20%;
            color: #a1a1a1;
            word-wrap:break-word;word-break:break-all;

        }
        .p-item .right{
            float: left;
            width: 80%;
            color: #121212;
            word-wrap:break-word;word-break:break-all;
            box-sizing: border-box;
            padding:0 10px;
        }

    </style>


</head>
<body>


<div class="main" data-url="http://120.26.112.213:8082/diyMall/commodity/details.do">

        <div class="headerNav">
            <div class="indexNaV-pagination"></div>
        </div>


        <div class="swiper-container swiper-outsetNav ">
            <div class="swiper-wrapper">

                <div class="swiper-slide ">
                    <div class="containerBox info">
                        <div class="info-video">
                            <h4>视频介绍</h4>
                            <video controls>
                                <!--<source src="images/movie.mp4" type="video/mp4">-->
                                <!--<source src="images/movie.ogg" type="video/ogg">-->
                                <!--<source src="images/movie.webm" type="video/webm">-->
                                <!--<object data="images/movie.mp4" >-->
                                    <!--<embed src="images/movie.swf">-->
                                <!--</object>-->
                            </video>
                        </div>

                        <div class="info-img">
                            <!--<img src="http://file.diy.51app.cn/wp/IntegralMall/8af52c31.jpg" alt=""/>-->
                        </div>
                    </div>

                </div>

                <div class="swiper-slide ">
                    <div class="containerBox parameter">

                    </div>

                </div>

                <div class="swiper-slide ">
                    <div class="containerBox pack">

                    </div>

                </div>


            </div>
        </div>




</div>





<script type='text/javascript' src='//g.alicdn.com/sj/lib/zepto/zepto.min.js' charset='utf-8'></script>
<script src="//cdn.bootcss.com/fastclick/1.0.6/fastclick.min.js"></script>
<script type='text/javascript' src='<%=path %>/js/diy2_5/swiper.min.js' charset='utf-8'></script>
<script type='text/javascript' src='<%=path %>/js/diy2_5/lazyload.min.js' charset='utf-8'></script>
<!--<script type='text/javascript' src='js/hd-main.js' ></script>-->
<script type="text/javascript">
    FastClick.attach(document.body);    //初始化fastclick

    //获取地址栏参数
    function GetQueryString(name)
    {
        var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");
        var r = window.location.search.substr(1).match(reg);
        if(r!=null)return  unescape(r[2]); return null;
    }

    var indexNav = new Swiper ('.swiper-outsetNav', {
       onlyExternal : true,
        pagination : '.indexNaV-pagination',
        paginationClickable :true,

        paginationBulletRender: function (index, className) {
            switch (index) {
                case 0: name='定制介绍';break;
                case 1: name='规格参数';break;
                case 2: name='包装售后';break;
                default: name='';
            }
            return '<span class="item ' + className + '">' + name + '</span>';
        }

    });

    var url = $('.main').attr('data-url');
    var type= GetQueryString('type');
    var id= GetQueryString('id');

    $.ajax({
        url: url+'?type='+ type +'&id='+id,
        type: 'GET',
        success: function (data) {
            data = JSON.parse(data);

            var dataBox = '',
                priceNote = data.data.priceNote,
                introduceList= data.data.introduceList  ,
                packAfterSaleList= data.data.packAfterSaleList,
                parameterList = data.data.parameterList;

            if(data.code == 200){
                dataBox='';
                $.each(introduceList,function (i) {
                    dataBox += '<img src="'+ introduceList[i] +'" alt=""/>'
                });
                $('.info .info-img').empty().append(dataBox);
                $('.info .info-img').append('<div class="lastTips">到底了哟~</div>');

                dataBox='';
                $.each(parameterList,function (i) {
                    dataBox += '<div class="p-item"><div class="left">'+ parameterList[i].title +'</div><div class="right">'+ parameterList[i].txt +'</div></div>'
                });
                $('.parameter').empty().append(dataBox);



                dataBox='';
                $.each(packAfterSaleList,function (i) {
                    dataBox += '<div class="p-item"><div class="left">'+ packAfterSaleList[i].title +'</div><div class="right">'+ packAfterSaleList[i].txt +'</div></div>'
                });
                dataBox += '<div class="p-item"><div class="left">'+ priceNote.title +'</div><div class="right">'+ priceNote.txt +'</div></div>';
                $('.pack').empty().append(dataBox);


                //视频介绍
                dataBox='';
                dataBox += '<source src="'+ data.data.vedio +'" type="video/mp4">';
                $('.info .info-video video').empty().append(dataBox);

                 $('.swiper-pagination-clickable .item').click(function () {
                    $(window).scrollTop(0);
                })

            }



        },
        error:function (error) {
            console.log('冒的数据 搞毛呀');
            console.log(error);
        }
    });


</script>


</body>
</html>