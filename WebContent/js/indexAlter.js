// 加载页面数据
var initial = function(){
	var $themesNav = $("#themesNav"),$banner = $('#bannerSwiper')
		,$specialGoodsUL = $("#specialGoodsUL"),$specialTopic = $("#specialTopic")
		,$teamGoods = $("#teamGoods");
	var pathname = window.location.pathname;
    var url = pathname.substring(0, pathname.indexOf('/',1))+"/home/homeV2.do";
    var urlPath = pathname.substring(0, pathname.indexOf('/',1));
    $.ajax({
        url: url,
        type: 'POST',
        dataType: 'json',
        cache:false,
        async: false,
        timeout:"60000",
        success:function(data){ 
            if(data.code==200){
                var data = data.data;
                var goodsType1="",
                    goodsType3="",
                    goodsType7="",
                	goodsType8="";
                $.each(data, function(index, val) {
                    var goods_type = data[index].type;
                    //1.导航条 3.banner 7.专题 8. 定制  <%=path%>/UGoods/toSpecial.do?special_ids=${n.id}
                    switch(goods_type){
                        case 1:
                            goodsType1 += ' <div class="swiper-slide">'+
			                                	'<a href="#" class="link col-auto switchTheme "><span>'+data[index].name+'</span></a>'+
			                                '</div>';
                            break;
                        case 3:
                        	if(data[index].isgoods==1){
                        		goodsType3 += '<div class="swiper-slide" >'+
                        		'<a href="javascript:sending('+data[index].goodsId+','+data[index].goods_type+','+data[index].isBoutique+');" class="">'+
                        		'<img src='+data[index].cimg+'></a></div>';
                        	}else{
                        		goodsType3 += "<div class='swiper-slide' ><a href='' class=''><img src="+data[index].cimg+"></a></div>";
                        	}
                            
                            break;
                        case 7:
                        	if(data[index].sp_type==1){
                        		goodsType7 += ' <div class="specialTopicItem">'+
                        		'<a href="javascript:sending('+data[index].goodsId+','+data[index].goods_type+','+data[index].isBoutique+');">'+
			                  	'<div class="picBox"><img src="'+data[index].img_url+'" alt=""></div>'+
			                  	'<div class="describ">'+
			                  		'<h3>'+data[index].name+'</h3>'+
			                  		'<p class="specialTopicDescrib">'+data[index].text+'</p>'+
			                  	'</div>'+
			                  	'</a>'+
			                  	'</div>';
                        	}else{
                        		goodsType7 += ' <div class="specialTopicItem">'+
			                  	'<div class="picBox"><img src="'+data[index].img_url+'" alt=""></div>'+
			                  	'<div class="describ">'+
			                  		'<h3>'+data[index].name+'</h3>'+
			                  		'<p class="specialTopicDescrib">'+data[index].text+'</p>'+
			                  	'</div>'+
			                  	'</div>';
                        	}
                        	break;
                        case 8:
                            goodsType8 += ' <li class="specialGoodsLi">'+
						                      '<a href="javascript:sending('+data[index].goodsId+','+data[index].goods_type+','+data[index].isBoutique+');">'+
						                        '<div class="picBox bgColor1"><img src='+data[index].cimg+'></div>'+
						                        '<p class="specialGoodsLiName">'+data[index].name+'</p>'+
						                      '</a>'+
						                    '</li>';
                            break;
                        default :
                            break;
                    }
                });
                $themesNav.append(goodsType1);
                $banner.append(goodsType3);
                $specialGoodsUL.append(goodsType8);
                $specialTopic.append(goodsType7);
                // $teamGoods.append(goodsType8_team);

                var handle = function(){//默认选中第一个主题
                	$("#themesNav").find('span').eq(0).addClass('active');
                	return true;
                }();      
			}else{
                return false;
            }                   
        }
    });
}(); $.init();

// 初始化swiper
$(function() {
	$('#themeTitleList').swiper({
	    speed: 300,
	    slidesPerView: "auto",
	    autoplay:5000,
	    observer:true
	});
	$('#tips').swiper({
	    height: 40,
	    direction: 'vertical',
	    autoplay:5000,
	    speed: 300,
	    observer:true,
	    autoplayDisableOnInteraction : false
	});
	$("#bannerk").swiper({
		speed: 300,
		spaceBetween: 10,
		autoplayDisableOnInteraction : false,
		observer:true,
	    autoplay:3000,
	    loop:true
	});
});


//切换主页专题tab
var switchTheme = function(){
	$(document).on('click','.switchTheme',function(){
		$(this).find('span').addClass('active')
			.parent().parent().siblings().find('span')
			.removeClass('active');

        // 加载次级页面
        var clickObjIndex = $(this).closest('.swiper-slide').index();
        var tabContentIndex = "";
        var isExist = false;
        var handle = function(i,d){
            var temp = $(this).attr('data-index');
            if(temp==clickObjIndex){ 
                $(this).show().siblings(".tabContent").hide();
                isExist = true; return true;
            } 
        }
        $.each($('.tabContent'),handle);
        if(!isExist){
            var pathname = window.location.pathname;
            var url = pathname.substring(0, pathname.indexOf('/',1))+"/js/special.json";
            $.ajax({
                url: url,
                type: 'POST',
                dataType: 'json',
                cache:false,
                async: false,
                timeout:"60000",
                success:function(data){ 
                    if(data.code==200){
                        var data = data.data;
                        var htmlTxt = "";
                        var beforeHtml = '<div class="special themeList tabContent" data-index="'+clickObjIndex+'"><ul>';
                        var afterHtml = '</ul></div>';
                        $.each(data, function(index, val) { 
                           htmlTxt += '<li class="themeLi clearfix">'+
                                          '<a href="/diyMallAPP/UGoods/toSpecial.do?type=2&amp;special_ids=11,12">'+
                                            '<div class="picBox"><img src="'+data[index].img+'"></div>'+
                                            '<div class="desc">'+
                                              '<h3 class="themeTitle">'+data[index].name+'</h3>'+
                                              '<p class="themeDesc">'+data[index].des+'</p>'+
                                            '</div>'+
                                          '</a>'+
                                        '</li>';
                        });
                        $(beforeHtml+htmlTxt+afterHtml).appendTo('.page').siblings('.tabContent').hide(); 
                    }else{
                        return false;
                    }                   
                }
            });
        }
	});
}();
// 固定主题nav
var fixedScroll = function(){
	var fn = {};
	fn.fixedScrollFn = overscroll("#index-content","body");
	return fn;
};
document.addEventListener('DOMContentLoaded',fixedScroll);
//js OC交互
function connectNZOCJSBridge(callback) {
	if (window.NZOCJSBridge) {
		callback(NZOCJSBridge)
	} else {
		document.addEventListener('NZOCJSBridgeReady', function() {
			callback(NZOCJSBridge)
		}, false)
	}
}
function sending(id,goodId,isBoutique){
	data = {"id":id,"goodType":goodId,"isBoutique":isBoutique};
	console.log(data);
	connectNZOCJSBridge(function(bridge) {
		bridge.send(data, function(responseData) {})
	});
}
function sendingHref(href){
	//href 为空时不与手机端交互
	if(href=='') return;	
	data = {"href":href};
	connectNZOCJSBridge(function(bridge) {
		bridge.send(data, function(responseData) {})
	});
}
//str='<div class="swiper-slide"><a onclick="sendingHref('+href+')"><img src="'+_data[index].img+'"></a></div>';

function overscroll(el,parentEle) {
	var el = $(el)[0] ,parentEle = $(parentEle);
    el.addEventListener('touchstart', function() {
        var top = el.scrollTop,
            totalScroll = el.scrollHeight,
            currentScroll = top + el.offsetHeight;
        if(top === 0) {
          el.scrollTop = 1;
        } else if((currentScroll+1) === totalScroll) {
          el.scrollTop = top - 1;
        }else if(currentScroll==totalScroll){
            el.scrollTop = top - 1;
        }else if((currentScroll-1)==totalScroll){
            el.scrollTop = top - 1;
        }
    })
    el.addEventListener('touchmove', function(e) {
        if(el.offsetHeight < el.scrollHeight)
          e._isScroller = true;
    })
    parentEle.on('touchmove',function(e) {
      if(!e._isScroller) { e.preventDefault(); }
    })
};
