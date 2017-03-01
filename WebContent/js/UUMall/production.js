$$(document).on('pageInit','.page[data-page="goods-info"]', function (e) {
    var $ = Dom7;
    var page=2;
	var goodsId='${id}';    //商品id
	var $wrap=$(".comments-detail");
	var $box=$(".comments-detail ul");  
	var $com_btn=$(".commentClass-btn"); 			
	var initData= new (function(){   // 进入页面加载评论数量
		var init=function(){
			var $i=$(".commentClass-btn i");
			$.ajax({
        	url: 'evalNum.do',
			data:{goodsId:goodsId},
        	type: 'POST',
        	dataType: 'json',
    	    cache:false,
    	    timeout:"60000",
    	    success:function(data){
    	    	 $.each(data.data,function(i,d){
						$i[i].innerHTML=data.data[i].number;
    	    	 });
    	    }
	    });
		}
		init();
	});	
	function getCommentInfo(){
			 	loading = true;
		      	var index=$('.commentClass-btn2').parent().index();		      	            
              	var $content=$(".content");				    	
				var showdata =function(){
				    $.ajax({
			        	url: "evalPage.do",
			        	type: 'POST',
			        	dataType: 'json',
			    	    cache:false,
			    	    timeout:"60000",
			    	    data:{page:page,goodsId:goodsId,evalType:index},
			    	    success:function(data){
			    	    	var str="";
		        			page++;
				        	if(data.code==200){
				        		$.each(data.data, function(index, val) {
				        		  var justify = data.data[index].imgUrl;
                                  var picArr;
                                  var picStr="";
                                  if(justify){ 
                                      var strEnd ;                                           
                                      picArr = data.data[index].imgUrl.split(",");
                                      for(var j=0;j<picArr.length;j++)
                                          {
                                              picStr += "<span class='commentImgContainer'><img src="+picArr[j]+"></span>"

                                          } 
                                  }          
                                  (justify)?
  					        			(str="<li><h2 class='commenter fs14 fc00 line-h24 pos-rel'><span class='comt-img'><img src='<%=path %>/images/default-logo.png' ></span> <i>"+data.data[index].mobile+"</i></h2><div class='comment-p fs12 fc00 line-h18'><p>"+data.data[index].content+"</p><p class='fc2 clearfix'><i class='fl'>"+data.data[index].creatTime+" </i></p><div class='commentImgBox'>",
                                              strEnd = "</div></div></li>",
                                              $box.append(str+picStr+strEnd),loading = false):
  					        			(str="<li><h2 class='commenter fs14 fc00 line-h24 pos-rel'><span class='comt-img'><img src='<%=path %>/images/default-logo.png' ></span> <i>"+data.data[index].mobile+"</i></h2><div class='comment-p fs12 fc00 line-h18'><p>"+data.data[index].content+"</p><p class='fc2 clearfix'><i class='fl'>"+data.data[index].creatTime+" </i></p></div></li>",$box.append(str),loading = false); 
                                });
				        	}
				        	else{
				        			loading = false; return false;
				        		}
					    }
					})
				};
				showdata();	
	};
	var clickAjax=new (function(){ 	// 点击title
		this.queryData=function(e){
			var _this = e.target;
			if(e.target){
				$com_btn.removeClass('commentClass-btn2');
			    $(_this).closest(".commentClass-btn").addClass('commentClass-btn2');
			}
		page=2;
		var i;
		if($(this).index()==-1){
			i=e;
		}else{
			i=$(this).index();
		}
	    $.ajax({
        	url: 'evalPage.do',
        	type: 'POST',
        	dataType: 'json',
			data:{goodsId:goodsId,evalType:i},
    	    cache:false,
    	    timeout:"60000",
    	    success:function(data){
    	    	$box.empty();
    	    	$box.siblings().remove();
    	    	var str="";
	        	if(data.code==200){
	        		$.each(data.data, function(index, val) {
	        			var justify = data.data[index].imgUrl;
                        var picArr;
                        var picStr="";
                        if(justify){ 
                            var strEnd ;                                           
                            picArr = data.data[index].imgUrl.split(",");
                            for(var j=0;j<picArr.length;j++)
                                {
                                    picStr += "<span class='commentImgContainer'><img src="+picArr[j]+"></span>";
                                }
                        }
                        (justify)?
	        			(str="<li><h2 class='commenter fs14 fc00 line-h24 pos-rel'><span class='comt-img'><img src='<%=path %>/images/default-logo.png' ></span> <i>"+data.data[index].mobile+"</i></h2><div class='comment-p fs12 fc00 line-h18'><p>"+data.data[index].content+"</p><p class='fc2 clearfix'><i class='fl'>"+data.data[index].creatTime+" </i></p><div class='commentImgBox'>",
                            strEnd = "</div></div></li>",
                            $box.append(str+picStr+strEnd)):
	        			(str="<li><h2 class='commenter fs14 fc00 line-h24 pos-rel'><span class='comt-img'><img src='<%=path %>/images/default-logo.png' ></span> <i>"+data.data[index].mobile+"</i></h2><div class='comment-p fs12 fc00 line-h18'><p>"+data.data[index].content+"</p><p class='fc2 clearfix'><i class='fl'>"+data.data[index].creatTime+" </i></p></div></li>",$box.append(str)); 
                            
		        	});
	        	}
	        	else{
	        			return false;
	        		}
		    }
		})};
		}
  	);
	clickAjax.queryData(0);
		$(".comments-class ul li:not(.commentPic)").on("tap",clickAjax.queryData);
		// 晒图接口
		$(".commentPic").on("tap",function(e){
			var _this = e.target;
			$com_btn.removeClass('commentClass-btn2');
		    $(_this).closest(".commentClass-btn").addClass('commentClass-btn2');
			$box.empty();
		$(".shaiTu").remove();
		$wrap.append("<div class='shaiTu'></div>")
		var $shaiTu=$(".shaiTu");
			$.ajax({
        	url: 'evalPic.do',
        	type: 'POST',
			data:{goodsId:goodsId},
        	dataType: 'json',	
    	    cache:false,
    	    timeout:"60000",
    	    success:function(data){
    	    	 $.each(data.data,function(i,d){
						var str="<span class='imgBox'><img class='' src="+data.data[i].imgUrl+"></span>";
						$shaiTu.append(str);
    	    	 }); 
    	    }
		});
	});
		// 无限滚动	
	var loading = false;
	var lastIndex = 6;
	$(".comments-detail").on("scroll",function(){
  		if (loading) return;			  		
	    var scroll_obj = $(".comments-detail")[0];
	    var top = scroll_obj.scrollTop,
	        totalScroll = scroll_obj.scrollHeight,
	        currentScroll = top + scroll_obj.offsetHeight ;
	    var comment_num = parseInt($(".commentClass-btn2").find("i").text());
	    var current_comment_num = $(".comments-detail ul li").length;

	   if(currentScroll >= totalScroll && current_comment_num < comment_num){
	   		getCommentInfo();
	   }
	});

  	
	$(function() {
		$(document).on("click",".commentImgContainer,.imgBox",function(){
			var _this = $(this);
        var index = $(this).index();
			var commentImgBox = ($(this).parent().hasClass("commentImgBox")) ? ($(this).parent(".commentImgBox")):($(this).parent(".shaiTu"));
			var img = commentImgBox.find("img");
        var data = commentImgBox.find("img").attr("src");

        $.each(img,function(i,d){
            var _this = $(this);
            var src = _this.attr("src");
            $(".swiper-wrapper").append("<div class='swiper-slide'><img src='"+src+"'/></div>");
        }); 
			$(".popLayer").removeClass("hide photo-browser-out").addClass("photoBrowserIn");	
        var mySwiper = new Swiper('.swiper-container', {
        	initialSlide:index,
        	observeParents:true,
			observer:true,
			pagination : '.swiper-pagination',
			paginationType : 'fraction',
			onClick: function(swiper){
			  $(".popLayer").find(".swiper-slide").not(".swiper-slide-active").remove();	
		      $(".popLayer").removeClass("photoBrowserIn").addClass('photo-browser-out');				     
		      mySwiper.destroy(false,true);
		      setTimeout(function(){ $(".swiper-wrapper,.swiper-pagination").empty();$(".popLayer").addClass("hide").removeClass("photo-browser-out")},400);
		      return false; 
		    }
		});
		});
	});
	var contentObj = $(".comments-detail")[0];
			var totopBtn = $("#gotoTop");
			
			overscroll(contentObj);
			$(".comments-detail").on("scroll",function(){
				var top = contentObj.scrollTop;
				if(top>500)  $("#gotoTop").show();
				if(top<500)  $("#gotoTop").hide();
			});			
			totopBtn.on("tap",function(){
				$(".comments-detail").scrollTo(1);
			});

  			function overscroll(el) {
				el.addEventListener('touchstart', function() {
				    var top = el.scrollTop,
				        totalScroll = el.scrollHeight,
				        currentScroll = top + el.offsetHeight;
				        // console.log(top,totalScroll,currentScroll)
				    if(top === 0) {
				      el.scrollTop = 1;
				    } else if(currentScroll === totalScroll) {
				      el.scrollTop = top - 1;
				    }
				})
				el.addEventListener('touchmove', function(e) {
				    if(el.offsetHeight < el.scrollHeight)
				      e._isScroller = true;
				})
				$(document).on('touchmove', function(e) {
				  if(!e._isScroller) { e.preventDefault(); }
				})
			}
			// 回到顶部
			(function($){
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
					            	$("#gotoTop").hide();
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
				})
			})(Dom7);
});
			
