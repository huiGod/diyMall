/* 加载数据
============================================================*/
var init=function(){
    var title = $('#themesNav')
        , banner = $('#bannerSwiper')
        , iconBtn = $('#customGoods')
        , boutique = $('#niceGoods ul')
        , team = $('#teamGoods ul')
        , url = $('.page-content').attr('data-url');

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
                var T1='',T2='',T3='',T4='',T5='',T6='',T7='',T8='';
                var count=0;
                $.each(data, function(i, d) {
                    var type = data[i].type;
                    //1导航条2精品专区3广告滚动4团体定制5图标按钮6广告 7精品 8ios2.0定制商品
                    switch(type){
                        case 1:
                            T1 += '<div class="swiper-slide" data-jump="'+data[i].jump+'">'+
                                    '<a href="#" class="switchTheme"><span class="">'+data[i].name+'</span></a>'+
                                '</div>';
                            break;
                        case 3:
                        	var jump="";
                        	//根据isgoods决定传id还是url
                        	if(data[i].isgoods!=3){
                        		jump='onclick="sending('+data[i].goodId+')"';
                        	}
                        	else{
                        		jump='onclick="sendingHref(\''+data[i].url+'\')"';
                        	}
                        	
                            T3 += '<div class="swiper-slide"><a href="#" '+jump+' class="">'+
                            '<img src="'+data[i].cimg+'" /></a></div>';
                            break;
                        case 4:
                            T4 +='<li class="teamBookGoods" onClick="sending('+data[i].goodId+')">'+
                                '<div class="picBox"><img src="'+data[i].icoUrl+'" alt=""></div>'+
                                '<p class="describ">'+data[i].describe+'</p>'+
                              '</li>';
                            break;
                        case 5:
                        	if(count==0){
                        		T5 +='<li class="specialGoodsLi" onclick="sending('+data[i].goodId+')">';
                        	}else if(count==7){
                        		T5 +='<li class="specialGoodsLi" onclick="sendingHref(\''+data[i].url+'\')">';
                        	}else{
                        		T5 +='<li class="specialGoodsLi" onclick="sendingHref(\''+data[i].jump2+'\')">';
                        	}
                            T5 += '<a href="" class="switchTheme" >'+
                                        '<div class="picBox" style="background-color:'+data[i].color+'">'+
                                            '<img src="'+data[i].cimg+'">'+
                                        '</div>'+
                                        '<p class="specialGoodsLiName">'+data[i].name+'</p>'+
                                    '</a>'+
                                '</li>';
                            count++;
                            break;
                        case 2:
                            T7 += '<li class="themeLi clearfix" onClick="sending('+data[i].goodId+')">'+
                                    '<a href="#" class="external">'+
                                      '<div class="picBox"><img src="'+data[i].cimg+'"></div>'+
                                      '<div class="desc">'+
                                        '<h3 class="themeTitle">'+data[i].name+'</h3>'+
                                        '<p class="themeDesc">'+data[i].describe+'</p>'+
                                      '</div>'+
                                    '</a>'+
                                '</li>';
                            break;
                        case 8:
                            break;
                        default :
                            break;
                    }
                });
                title.append(T1);
                banner.append(T3);
                iconBtn.append(T5);
                boutique.append(T7);
                team.append(T4);
            }else{
                return false;
            }                   
        }
    });
}();
/* 初始化swiper
============================================================*/
$(function() {
    var title = new Swiper('#themeTitleList', {    
        speed: 300,
        slidesPerView: "auto",
        observer:true
    });
    var banner = new Swiper('.banner', { 
        speed: 300,
        spaceBetween: 10,
        autoplayDisableOnInteraction : false,
        observer:true,
        autoplay:3000,
        pagination:'.banner-pagination',
        loop:true
    }); 
    var ads = new Swiper('.tips', {
        height: 40,
        direction: 'vertical',
        autoplay:5000,
        speed: 300,
        observer:true,
        noSwiping : true,
        autoplayDisableOnInteraction : false
    });
    $('#themesNav div').eq(0).find('span').addClass('active');
    $('[class*=_js-]').each(execute);
});
/* 处理页面脚本元素
==================================================*/
var execute = function(i,d){
    $.each(d.className.match(/\_js\-[a-zA-Z]+/g),function(k,v){
        func[v.slice(4)].call(d,'body');
    });
}
/* 处理页面脚本元素方法
==================================================*/
var func = {
    touchScroll : function(parentEle){ 
        var el = this ,parentEle = $(parentEle);
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
    }
};
/*切换主题tab
============================================================*/
var switchTheme = function(){
	$(document).on('click','.switchTheme',function(){
        // 切换title
		$(this).find('span').addClass('active')
			.parent().parent().siblings().find('span')
			.removeClass('active');
        // 切换页面
        var clickIndex = $(this).closest('.swiper-slide').index();
        var isExist = false;
        var handle = function(i,d){
            var temp = $(this).attr('data-index');
            if(temp==clickIndex){ 
                $(this).show().siblings(".tabContent").hide();
                isExist = true; 
                return true;
            } 
        }
        $.each($('.tabContent'),handle);
        if(!isExist){
            var url = $(this).closest('.swiper-slide').attr('data-jump');
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
                        var beforeHtml = '<div class="special themeList tabContent" data-index="'+clickIndex+'"><ul>';
                        var afterHtml = '</ul></div>';
                        $.each(data, function(index, val) { 
                           htmlTxt += '<li class="themeLi clearfix" onClick="sending('+data[index].pid+')">'+
                                          '<a href="#">'+
                                            '<div class="picBox"><img src="'+data[index].img_url+'"></div>'+
                                            '<div class="desc">'+
                                              '<h3 class="themeTitle">'+data[index].name+'</h3>'+
                                              '<p class="themeDesc">'+data[index].text+'</p>'+
                                            '</div>'+
                                          '</a>'+
                                        '</li>';
                        });
                        $(beforeHtml+htmlTxt+afterHtml).appendTo('#tab-content').siblings('.tabContent').hide(); 
                    }else{
                        return false;
                    }                   
                }
            });
        }
	});
}();

/*js-OC交互
============================================================*/
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

