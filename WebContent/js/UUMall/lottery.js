/*!
 * lottery v1.0.3
 * by blacksnail2015 2015-03-30
 */
/*
 * 这里对速度做一下说明：
 *     这里的速度其实就是切换样式的间隔时间，也就是setTimeout(functionName, time)中的time值；
 *     因此，速度值越小，间隔越短，转的越快。
 */
var defaults = {
	selector:     '#lottery',
	width:        3,    // 转盘宽度
	height:       3,    // 转盘高度
	initSpeed:    300,	// 初始转动速度
	speed:        0,	// 当前转动速度
	upStep:       50,   // 加速滚动步长
	upMax:        50,   // 速度上限
	downStep:     30,   // 减速滚动步长
	downMax:      500,  // 减速上限
	waiting:      500, // 匀速转动时长
	index:        0,    // 初始位置
	target:       0,    // 中奖位置，可通过后台算法来获得，默认值：最便宜的一个奖项或者"谢谢参与"
	isRunning:    false, // 当前是否正在抽奖
	hasDraw:    0
}

 // 弹窗模块
var toastWarning = function() {
    var fn = {},  body = $('body'), handle
      , bubble = $('<div class="warn-bubble">' +
                        '<span>优品汇</span>' +
                    '</div>');

    fn.say = function(str) {
        bubble.detach().find('span').text(str).parent().appendTo(body);

        clearTimeout(handle);
        handle = setTimeout(function() {
            bubble.detach();
        }, 1700);
    };
    return fn;
}();

var lottery = {
	// 初始化用户配置
	lottery: function (options) {
		this.options = $.extend(true, defaults, options);
		this.options.speed = this.options.initSpeed;
		this.container = $(this.options.selector);
		this._setup();
	},

	// 开始装配转盘
	_setup: function () {

		// 这里为每一个奖项设置一个有序的下标，方便lottery._roll的处理
		// 初始化第一行lottery-group的序列
		for (var i = 0; i < this.options.width; ++i) {
			this.container.find('.lottery-group:first .lottery-unit:eq(' + i + ')').attr('lottery-unit-index', i);
		}

		// 初始化最后一行lottery-group的序列
		for (var i = lottery._count() - this.options.height + 1, j = 0, len = this.options.width + this.options.height - 2; i >= len; --i, ++j) {
			this.container.find('.lottery-group:last .lottery-unit:eq(' + j + ')').attr('lottery-unit-index', i);
		}

		// 初始化两侧lottery-group的序列
		for (var i = 1, len = this.options.height - 2; i <= len; ++i) {
			this.container.find('.lottery-group:eq(' + i + ') .lottery-unit:first').attr('lottery-unit-index', lottery._count() - i);
			this.container.find('.lottery-group:eq(' + i + ') .lottery-unit:last').attr('lottery-unit-index', this.options.width + i - 1);
		}
		this._enable();
	},

	// 开启抽奖
	_enable: function () {
		// if(hadDrawTimes==3){
		// 	this.container.find('a').unbind('click', this.beforeRoll);
		// 	$('.td_5 img').attr('src','images/shop.png');
		// 	$('.td_5 a').attr('href','#shop');
		// }else{
		// 	this.container.find('a').bind('click', this.beforeRoll);
		// 	this.hadDrawTimes++;
		// }
		this.container.find('a').bind('click', this.beforeRoll);

	},

	// 禁用抽奖
	_disable: function () {
		this.container.find('a').unbind('click', this.beforeRoll);
	},

	// 转盘加速
	_up: function () {
		var _this = this;
		if (_this.options.speed <= _this.options.upMax) {
			_this._constant();
		} else {
			_this.options.speed -= _this.options.upStep;
			_this.upTimer = setTimeout(function () { _this._up(); }, _this.options.speed);
		}
	},

	// 转盘匀速
	_constant: function () {
		var _this = this;
		clearTimeout(_this.upTimer);
		setTimeout(function () { _this.beforeDown(); }, _this.options.waiting);
	},

	// 减速之前的操作，支持用户追加操作（例如：从后台获取中奖号）
	beforeDown: function () {
		var _this = this;
		// _this.aim();
		if (_this.options.beforeDown) {
			_this.options.beforeDown.call(_this);
		}
		_this._down();
	},

	// 转盘减速
	_down: function () {
		var _this = this;
		if (_this.options.speed > _this.options.downMax && _this.options.target == _this._index()) {
			_this._stop();
		} else {
			_this.options.speed += _this.options.downStep;
			_this.downTimer = setTimeout(function () { _this._down(); }, _this.options.speed);
		}
	},

	// 转盘停止，还原设置
	_stop: function () {
		var _this = this;
		clearTimeout(_this.downTimer);
		clearTimeout(_this.rollerTimer);
		_this.options.speed = _this.options.initSpeed;
		_this.options.isRunning = false;
		_this._enable();

		_this._configInfo(_this.options.target,_this.options.hasDraw);	
		$('.publicInfo').addClass('hidden');
		$('.winning').removeClass('hidden');
		if(this.options.hasDraw>=2){
			$('.td_5 img').attr('src','../images/shop.png');
			$('.td_5 a').attr('href','#shop');
		}
	},

	// 抽奖之前的操作，支持用户追加操作
	beforeRoll: function () {
		var _this = lottery;
		var url = $('.td_5').attr("data-drawResult");
		$.ajax({
         	url: url,
        	type: 'POST',
        	dataType: 'json',
        	cache:false,
            async: false,
            timeout:"60000",
        	success:function(data){
        		if(data.code==200){
        			_this.options.target  = data.data.level;
        			_this.options.hasDraw = data.data.hasDraw;
        			var hasDraw =  data.data.hasDraw;
        			$("#couponId").val(data.data.couponId);
        			_this._disable();
        			if( hasDraw < 3){
						_this._roll();
					}	
        		}else if(data.code == 400){
        			var pathname = window.location.pathname;
                	var url = pathname.substring(0, pathname.indexOf('/',1))+"/UMallUser/toLogin.do";
        			window.location.href=url;
        		}
        	}
        });
	},
	// 配置文件
	_configInfo:function(awardIndex,hasDraw){
		var lease = 2-hasDraw;
		if(lease<0)  lease = 0;
		switch(awardIndex){
				case 0:
				case 3:
				case 6:
						$('.prize-level').text("四等奖");
						$('.awardObj').text("满50减5优惠券");
						$('.p2').text("在【唯优品】购买任意商品，下单时，选择已有的优惠券，您将参加系统的满减活动噢~");
						$('.leaseChance').text(lease);
						break;
				case 1:
						$('.prize-level').text("一等奖");
						$('.awardObj').text("9朵玫瑰鲜花");
						$('.p2').text("先验证您的手机号码，小优客服会马上联系您噢~");
						$('.clickGet').css("display","block");
						$('.leaseChance').text(lease);
						break;
				case 2:
						$('.prize-level').text("四等奖");
						$('.awardObj').text("满100减10优惠券");
						$('.p2').text("在【唯优品】购买任意商品，下单时，选择已有的优惠券，您将参加系统的满减活动噢~");
						$('.leaseChance').text(lease);
						break;
				case 4:
						$('.prize-level').text("四等奖");
						$('.awardObj').text("满200减30优惠券");
						$('.p2').text("在【唯优品】购买任意商品，下单时，选择已有的优惠券，您将参加系统的满减活动噢~");
						$('.leaseChance').text(lease);
						break;
				case 5:
						$('.prize-level').text("三等奖");
						$('.awardObj').text("免费设计DIY商品");
						$('.p2').text("在【唯优品】购买任意DIY商品后，发送您的图片到'小优来了'微信公众号，即可获得高级设计师免费设计噢~");
						$('.leaseChance').text(lease);
						break;
				case 7:
						$('.prize-level').text("二等奖");
						$('.awardObj').text("定制DIY手机壳");
						$('.p2').text("在【唯优品】购买定制手机壳后，另赠送定制手机壳一个，发送您需要的手机壳图片到'小优来了'微信公众号即可！");
						$('.leaseChance').text(lease);
						break;
		}
	},

	// 转盘滚动
	_roll: function () {
		var _this = this;
		_this.container.find('[lottery-unit-index=' + _this._index() + ']').removeClass("active");
		++_this.options.index;
		_this.container.find('[lottery-unit-index=' + _this._index() + '].lottery-unit').addClass("active");
		_this.rollerTimer = setTimeout(function () { _this._roll(); }, _this.options.speed);
		if (!_this.options.isRunning) {
			_this._up();
			_this.options.isRunning = true;
		}
	},

	// 转盘当前格子下标
	_index: function () {
		return this.options.index % this._count();
	},

	// 转盘总格子数
	_count: function () {
		return this.options.width * this.options.height - (this.options.width - 2) * (this.options.height - 2);
	},

	// 获取中奖号，用户可重写该事件，默认随机数字
	aim: function () {
		var _this = this;
		// if (this.options.aim) {
		// 	this.options.aim.call(this);
		// } else {
		// 	this.options.target = parseInt(parseInt(Math.random() * 10) * this._count() / 10);
		// 	console.log(this.options.target);
		// } 
		// this.options.target = this.options.json[this.hadDrawTimes];
		// console.log(this.options.target)
	}
};

window.onload = function () {
	// 获取中奖名单
	var getAwardList = new (function(){
		var url = $('.publicInfo').attr('data-infoList');
		$.ajax({
         	url: url,
	        	type: 'POST',
	        	dataType: 'json',
	        	cache:false,
                async: false,
                timeout:"60000",
	        	success:function(data){
	        		if(data.code==200){
	        			var dataList = data.data.list;
	        			var times    =  data.data.hasDraw;
	        			var award   = data.data.awards;
	        			if(times>2){
	        				$('.td_5 img').attr('src','../images/shop.png');
							$('.td_5 a').attr('href','#shop');
							$('.publicInfo').hide();
							$(".winning").show();
							if(award == 1){$('.clickGet').css("display","block");}
							lottery._configInfo(award,3);
	        			}else{
	        				var list ="";
		        			$.each(dataList,function(i,d){
		        				list += '<li class="clearfix">'+
											'<span class="name">'+dataList[i].name+'</span>'+
											'<span class="middle">抽中</span>'+
											'<span class="award awardObj">'+dataList[i].about+'</span>'+
										'</li>';
		        			});
		        			$('.list').append(list);
	        			}	        			
	        		}
	        	}
        });
	})();

	lottery.lottery({
		selector:     '#lottery',
		width:        3,
		height:       3,
		index:        7,    // 初始位置
		initSpeed:    500,  // 初始转动速度
		// upStep:       50,   // 加速滚动步长
		// upMax:        50,   // 速度上限
		downStep:     50   // 减速滚动步长
		// downMax:      500,  // 减速上限
		// waiting:      5000, // 匀速转动时长
	});
	
}