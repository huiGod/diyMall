var page = 0;

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
    console.log(id)
    data = {
      "click": id
    };
    connectNZOCJSBridge(function(bridge) {
      bridge.send(data, function(responseData) {})
    });
    try{
        uqWyp.notifyInteraction(id);
    }catch(e){
        // console.log(e);
    }
}

function sendOC(sendObj) {
    // console.log(sendObj)
    connectNZOCJSBridge(function(bridge) {
      bridge.send(sendObj, function(responseData) {})
    });
}

$(function(){
    
    var loading = false;

    // 处理300ms点击延迟
    FastClick.attach(document.body);

    function paddingData(json){
        var linkHtml = '',
            discoveryHtml = '',
            linkContainer = $('.link-block'),
            discoveryContainer = $('.discovery-list'),
            sendData = '';
        json.data.forEach(function(data, index, dataArray){
            if(data.type === 1){        // 顶部链接
                sendData = '\'' + 3 + ',' + data.url+'\'';
                linkHtml += '<a href="javascript:;" class="custom-link" onclick="sending(' + sendData + ')">' +
                                '<img src="' + data.img + '" alt="">' +
                                '<span class="link-title">' + data.title + '</span>' +
                            '</a>';
            }else if(data.type === 2){      // 发现内容
                sendData = '\'' + 3 + ',http://120.26.112.213:8082/diyMall/index/discoveryDetail.do?id=' + data.id + '\'';
                discoveryHtml += '<li class="discovery-card">'+
                                    '<a href="javascript:;" class="discovery-link" onclick="sending(' + sendData + ')" data-id="' + data.id + '">'+
                                        '<div class="img-box">'+
                                            '<img src="' + data.img + '" alt="">'+
                                        '</div>'+
                                        '<div class="desc-box">'+
                                            '<h2 class="desc-title">' + data.title + '</h2>'+
                                            '<p class="desc-info">阅读 <span class="read-time">' + data.view + '</span> | <span class="release-date">' + data.ctime + '</span></p>'+
                                            '<p class="desc-author"><img src="' + data.headImg + '" alt="">' + data.author + '</p>'+
                                        '</div>'+
                                    '</a>'+
                                '</li>'
            }else if(data.type === 3){      // 广告
                sendData = '\'' + 3 + ',' + data.url + '\'';
                discoveryHtml += '<li class="discovery-card recommend-card" onclick="sending(' + sendData + ')">'+
                                    '<a href="javascript:;" class="recommend-link">'+
                                        '<img src="' + data.img + '" alt="">'+
                                    '</a>'+
                                '</li>'
            }else if(data.type === 4){      // 视频
                discoveryHtml += '<li class="discovery-card recommend-card">'+
                                    '<div class="recommend-video">'+
                                        '<video src="' + data.img + '" controls preload="auto" poster="http://file.diy.51app.cn/wp/findList/e2be01b0.jpg"></div>'+
                                    '</div>'+
                                '</li>'
            }
        });
        linkContainer.append(linkHtml);
        discoveryContainer.append(discoveryHtml);
        loading = false;
        for(var i = 0; i < $('video').length; i++){
            var video = $('video')[i];
            var canvas = document.createElement('canvas');
            video.addEventListener('loadeddata', function(){
                canvas.width = video.offsetWidth;
                canvas.height = video.offsetHeight;
                canvas.getContext('2d').drawImage(video,0,0,canvas.width,canvas.height);
                // video.setAttribute('poster', canvas.toDataURL('image/png'));
            });
        }
        $('.discovery-link').each(function(index, elem){
            var that = $(elem),
                articleId = that.attr('data-id'),
                url = 'http://120.26.112.213:8082/diyMall/commodity/getView/' + articleId + '.do'
            $.ajax({
                async: false,
                url: url,
                type: 'POST',
                dataType: 'json',
                timeout: 60000,
                success: function(json){
                    that.find('.read-time').text(json.data);
                }
            });
        });
    }
    
    function ajaxInit(url){
        $.ajax({
            async: false,
            url: url,
            type: 'POST',
            dataType: 'json',
            timeout: 60000,
            success: function(json){
                if(json.code === 200 && (json.data.length > 0 || json.data != '')){
                    paddingData(json);
                }else if(json.message === '没有数据'){
                    $('.main-container').off('scroll');
                    $('.load-more').html('<i class="left-grad-line"></i>到底啦<i class="right-grad-line"></i>');
                }
            },
            error: function(xhr, status, error){
                console.log(status);
            }
        });
    };
    ajaxInit('http://120.26.112.213:8082/diyMall/commodity/findList.do?page=0');

    // 无限加载更多..==============================================================================================================================================
    function loadMore(){
        page++;
        ajaxInit('http://120.26.112.213:8082/diyMall/commodity/findList.do?page=' + page);
    }
    // 页面滑到底部时处理，包括加载更多等..========================================================================================================================
    
    $(document).scroll(function(e){
        if(loading) return;
        var pageHeight = $(this).find('.main-container').height(),
            windowHeight = $('body').height();
        if(pageHeight - $('body').scrollTop() <= windowHeight + 50){
            loading = true;
            setTimeout(loadMore, 10);
        }
    });

    $('.discovery-link').click(function(e){
        var articleId = $(this).attr('data-id'),
            url = 'http://120.26.112.213:8082/diyMall/commodity/addView/' + articleId + '.do'
        $.ajax({
            async: false,
            url: url,
            type: 'POST',
            dataType: 'json',
            timeout: 60000,
            success: function(json){
            }
        });
        $(this).find('.read-time').text(parseInt($(this).find('.read-time').text()) + 1);
    });

});