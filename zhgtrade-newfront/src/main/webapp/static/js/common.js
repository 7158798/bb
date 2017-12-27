//拦截所有ajax请求，并计算所有请求的时间。
(function ($) {
    var _ajax = $.ajax;
    $.ajax = function (opt) {
        opt.startTime = new Date().getTime();
        var fn = {success:function(data, textStatus, header){}};
        if(opt.success) {
        	fn.success = opt.success;
        }
        var _opt = $.extend(opt, {
            success: function (data, textStatus, header) {
                fn.success(data, textStatus, header);
                try{
                    var time = new Date().getTime() - opt.startTime;
                    var args = ['time=' + time,'url=' + location.protocol + "//" + location.hostname + opt.url, "app=emhndHJhZGU="];
                    var img = new Image(1,1);
                    img.src =location.protocol + '//www.zhgtrade.com/load.gif?' + args.join("&");
				}catch(e) {
                	console.log("上传时间失败!");
				}
            }
        });
        return _ajax(_opt);
    };
})(jQuery);


var keycount = 1;
function clearTig(obj){
	if(keycount==1){
		obj.value="";
		keycount++;
	}
}

var availableTagsDef = ["@qq.com","@163.com","@126.com","@sina.com","@gmail.com","@foxmail.com","@sohu.com","@vip.qq.com","@hotmail.com","@163.net","@sina.com.cn","@139.com","@189.cn"];
var availableTags = ["@qq.com","@163.com","@126.com","@sina.com","@gmail.com","@foxmail.com","@sohu.com","@vip.qq.com","@hotmail.com","@163.net","@sina.com.cn","@139.com","@189.cn"];
function emailOnkeyUp(obj){
	for ( var i = 0; i < availableTags.length; i++) {
		var reg = new RegExp(/^[a-zA-Z0-9_]{1,}$/);
		if(reg.test(obj.value)){
			availableTags[i] = obj.value+availableTagsDef[i];
		}
	}
}

var _const = {
	SMS_CAPTCHA_KEY : "_s_",
	VOICE_CAPTCHA_KEY : "_v_",
	EMAIL_CAPTCHA_KEY : "_e_",
	IMAGE_CAPTCHA_KEY : "_i_",
	CAPTCHA_SECONDS : 60,
	SUBMIT_FLAG : "val"
}

var _handler = {
	showHandlerTips : function(btn, secs,type){
		var _this = $("#" + btn);
        var _span = _this.find("span");
		if(type){
            _span = _this;
		}
		if(secs > 0){
			if(type){
                _span.css({"background":"#ccc","cursor":"text"});
			}
			_span.html(secs + "s");
		}else{
            if(type){
                $(".img_code").val('');
                $(".image_code").attr("src" ,'/servlet/ValidateImageServlet?' + new Date().getTime());
                _span.css({"background":"#fe741f","cursor":"pointer"});
            }
			var name = _span.data('name');
			if(name){
				_span.html(name);
			}else{
				_span.html("发送验证码");
			}
			_this.data(_const.SUBMIT_FLAG, true);
		}
	},
	updateTipsSeconds : function(btn, secs,type){
		if(secs <= 0){
			return;
		}
		var _this = $("#" + btn);
		_this.data(_const.SUBMIT_FLAG, false);
		secs = secs*1;
		for(var i=0; i<=secs; i++){
			setTimeout("_handler.showHandlerTips('" + btn + "', " + (secs - i) + ","+type+")", i * 1000);
		}
	},
	cleanTipsHandler : function(key){
		var strs = $.cookie(key);
		if(!strs || {} == strs){
			return;
		}
		var time = 0;
		var vals = strs.split("\|");
		for(var i=0; i<vals.length; i++){
			var val = vals[i];
			if(val){
				var btnVals = val.split("_");
				var _t = btnVals[1] * 1;
				var secs = _const.CAPTCHA_SECONDS - ((new Date().getTime() - (btnVals[1] * 1)) / 1000).toFixed(0);
				if(secs <= 0){
					strs.replace("\|" + val, '');
				}else if(_t > time){
					time = _t + (_const.CAPTCHA_SECONDS * 1000);
				}
			}
		}
		if("\|" == strs){
			$.removeCookie(key);
		}else{
			var date = new Date(time);
			$.cookie(key, strs, {expires : date, path : '/'});
		}
		return strs;
	},
	addTipsHandler : function(key, btn){
		_handler.cleanTipsHandler(key);
		var vals = $.cookie(key);
		if(!vals || {} == vals){
			vals = '|';
		}
		var date = new Date();
		var minsecs = date.getTime();
		vals += btn + "_" + minsecs + "|";
		date.setSeconds(date.getSeconds() + _const.CAPTCHA_SECONDS);
		$.cookie(key, vals, {expires : date, path : '/'});
	},
	recoverHandler : function(key){
		var strs = _handler.cleanTipsHandler(key);
		if(!strs){
			return;
		}

		var vals = strs.split("\|");
		for(var i=0; i<vals.length; i++){
			var val = vals[i];
			if(val){
				var btnVals = val.split("_");
				var secs = _const.CAPTCHA_SECONDS - ((new Date().getTime() - (btnVals[1] * 1)) / 1000).toFixed(0);
				_handler.updateTipsSeconds(btnVals[0], secs);
			}
		}
	}
}

$(function(){
	// 发送验证码读秒
	_handler.recoverHandler(_const.SMS_CAPTCHA_KEY);
	_handler.recoverHandler(_const.VOICE_CAPTCHA_KEY);
	_handler.recoverHandler(_const.EMAIL_CAPTCHA_KEY);


	// 判断是否登录
	$("#question-list").on('click', '.send_message', function(){
		center_box("send_message_box");
		$("#tm_yy").show();
		$("#send_message_box").show();
	});

	$(".footer .send_message").click(function(){
		center_box("send_message_box");
		$("#tm_yy").show();
		$("#send_message_box").show();
	});

	$(".loginbeforesend").click(function () {
		center_box("login_box");
		$("#tm_yy").show();
		$("#login_box").show();
	});

	$("#close").click(function(){
		$(this).closest(".warning").slideUp("slow");
	});
	/*$("#title").find("li").click(function(){
		var $this=$(this);
		var _index=$this.index();
		$this.addClass("cur").siblings().removeClass("cur");
		$("#content>li").eq(_index).show().siblings().hide();
	});*/
	$(".float_box").find(".close").on("click",function(){
		$(this).parents(".float_box").hide();
		$("#tm_yy").hide();
	});

	$("form input").on("focus", function(){
		formObj.cleanFormIcon(this);
		formObj.cleanFormTips(this);
	});

	/* 回到顶部 */
	$("#totop").click(function(){
		$('html,body').animate({'scrollTop':0},100);
	});
	// 悬浮客服
	sidebarFun();
	// 登出
	$("#logoutBtn").on("click", function(){
		$.post("/user/logout.html", {}, function(data){
			if(data){
				$("#loginBar").removeClass("dn");
				$("#userInfoBar").addClass("dn");
				window.location.reload(true);
			}
		}, "text");
	});
	$("#login, #reLogin").click(function () {
		center_box("login_box");
		$("#tm_yy").show();
		$("#login_box").show();
	});

	$("#weixin").hover(function () {
		$(this).find(".weixin").show();
	}, function () {
		$(this).find(".weixin").hide();
	});
	$("#user_info").hover(function () {
		$(this).find(".user_info").addClass("cur");
		$(".mywallet_list").show();
		var url = "/account/unreadinformation";
		var unReadInformationCount = $("#unReadInformation").text();
		if (unReadInformationCount == "") {
			$.get(url, function (data) {
				if(!isNaN(data) && data != 0){

					$("#unReadInformation").text(data);
				}
			})
		}
	}, function () {
		$(this).find(".user_info").removeClass("cur");
		$(".mywallet_list").hide();
	});

	//显示是否有未读信息
	var url = "/account/unreadinformation";
	$.get(url, function (data) {
		if(data != 0){
			$(".user_info .messagetip").removeClass("dn");
			$(".header .info .user_info").css("max-width", "330px");
			// $(".user_info .messagetip").addClass("db");
			// $(".user_info .messagetip").text(123164)
		}
	})

	//点击QQ图标
	$(".header .qq_line i").on("click", function () {
		var url = $(this).siblings("a").attr("href");
		window.open(url);
	})

	function getInfo() {
		$.get('/index/userinfo.html?' + new Date().getTime(), {}, function(data){
			if(data && 200 == data.code){
				$('#loginBar').hide();
				var $loginedBar = $('#userInfoBar');
				$loginedBar.find('span[name="loginName"]').attr('title', data.loginName).text(data.loginName);
				$loginedBar.find('span[name="userId"]').attr('title', data.id).text('ID:' + data.id);
				$loginedBar.find('span[name="totalRMB"]').text(data.totalRmb);
				$loginedBar.find('span[name="frozenRMB"]').text(data.frozenRmb);
				$loginedBar.show();

				$('#unloginBox').hide();
				var $loginedBox = $('#loginedBox');
				$loginedBox.find('a[name="loginName"]').attr('title', data.loginName).text(data.loginName);
				$loginedBox.find('a[name="userId"]').attr('title', data.id).text(data.id);
				$loginedBox.find('#assetSpan').text(data.totalCapital);
				$loginedBox.show();
			}else{
				$('#loginBar').removeClass('dn');
				$('#unloginBox').removeClass('dn');
			}
		}, 'json');
	}

	setInterval(getInfo, 60*1000);
	getInfo();

	$progressBar = $('#loading');
	if($.support.pjax){
		$(document).pjax('#container a[data-pjax="#container"]', '#container', {fragment:'#container', scrollTo:false, timeout:3000});
		$(document).on('pjax:send', function() {
			$progressBar.css('width', '0');
			$progressBar.show();
			for(var i=1; i<50; i++){
				setTimeout("$progressBar.animate({'width':'" + (i * 2) + "%'}, 0)", i * 17);
			}
		})
		$(document).on('pjax:complete', function() {
			$progressBar.hide();
		})
	}
});
// 悬浮客服
function sidebarFun(){
	$("#tools ul .slidebox").hover(function(){
		$(this).stop().animate({"marginLeft":"-126px"},200);
	},function(){
		$(this).stop().animate({"marginLeft":"0px"},200);
	});
	$("#tools ul .show_title").hover(function(){
		$(this).stop().animate({"marginLeft":"-45px"},200);
	},function(){
		$(this).stop().animate({"marginLeft":"0px"},200);
	});

	$("#tools .weixin").hover(function () {
		$(this).find("img").removeClass("dn");
	}, function () {
		$(this).find("img").addClass("dn");
	});

	var lastRmenuStatus=false;
	$(window).scroll(function(){
		var _top=$(window).scrollTop();
		if(_top>300){
			$("#tools").data("expanded",true);
		}else{
			$("#tools").data("expanded",false);
		}
		if($("#tools").data("expanded")!=lastRmenuStatus){
			lastRmenuStatus=$("#tools").data("expanded");
			if(lastRmenuStatus){
				$("#totop").animate({"height":"45px"},200);
			}else{
				$("#totop").animate({"height":"0"},200);
			}
		}
	});
}
/*function showLoginDialog(){
	var html = $('#login_box .f_content').html();
	popup_dialog({mask : true, confirmBtn : false, content : html});
}*/
(function($){
	// 发送短信验证码
	sendSmsCaptcha = function(_btn, _in, isBind, _code){
		var _this = $(_btn);
		var _input = '';
		var _span = _this.find("span");
		var mobile = '';
		var code = '';
		var id = _this.attr("id");
		var flag = _this.data(_const.SUBMIT_FLAG);
		if(false == flag){
			return;
		}
		if(true != isBind){
			isBind = false;
		}

		if(_in){
			_input = $("#" + _in);
			mobile = $.trim(_input.val());
			if(!mobile){
				formObj.showTips(_input, "请输入手机号");
				return;
			}
			if(!formObj.isMobileNumber(mobile)){
				formObj.showTips(_input, "手机号格式不正确");
				return;
			}
			formObj.showTips(_input);
		}
		if(_code){
			var $code = $("#" + _code);
			code = $.trim($code.val());
			if(!code){
				formObj.showTips($code, "请输入图形验证码");
				return;
			}
			if(4 != code.length){
				formObj.showTips($code, "图形验证码格式不正确");
				return;
			}
			formObj.showTips($code);
		}

		if(!id){
			alert('给个id呗');
			return;
		}
		_this.data(_const.SUBMIT_FLAG, false);
		$.post("/captcha/sendSMSAuthCode.html", {mobile : mobile, isBind : isBind, code : code}, function(data){
			if('200' === data){
				_handler.addTipsHandler(_const.SMS_CAPTCHA_KEY, id);
				_handler.updateTipsSeconds(id, _const.CAPTCHA_SECONDS);
				if(_code){
					$("#" + _code).next().find('img').trigger('click');
				}
			}else if('101' === data){
                formObj.showTips(_input, "请输入正确的手机号码");
				_this.data(_const.SUBMIT_FLAG, true);
			}else if('102' === data){
                formObj.showTips(_input, "非会员手机号");
				_this.data(_const.SUBMIT_FLAG, true);
			}else if('103' === data){
                formObj.showTips(_input, "您还未绑定手机号");
				_this.data(_const.SUBMIT_FLAG, true);
			}else if('104' === data){
                formObj.showTips(_input, "手机号已注册");
				_this.data(_const.SUBMIT_FLAG, true);
			}else if('105' === data){
				alert("验证码错误次数过多，请两小时后再试");
				_this.data(_const.SUBMIT_FLAG, true);
			}else if('106' === data){
				alert("图形验证码错误");
				_this.data(_const.SUBMIT_FLAG, true);
			}else{
				alert("发送短信验证码失败");
				_this.data(_const.SUBMIT_FLAG, true);
			}
		});
	}
	sendVoiceCaptcha = function(_btn, _in, isBind, _code){
		var _this = $(_btn);
		var _input = '';
		var _span = _this.find("span");
		var mobile = '';
		var code = '';
		var id = _this.attr("id");
		var flag = _this.data(_const.SUBMIT_FLAG);
		if(false == flag){
			return;
		}
		if(true != isBind){
			isBind = false;
		}

		if(_in){
			_input = $("#" + _in);
			mobile = $.trim(_input.val());
			if(!mobile){
				formObj.showTips(_input, "请输入手机号");
				return;
			}
			if(!formObj.isMobileNumber(mobile)){
				formObj.showTips(_input, "手机号格式不正确");
				return;
			}
			formObj.showTips(_input);
		}
		if(_code){
			var $code = $("#" + _code);
			code = $.trim($code.val());
			if(!code){
				formObj.showTips($code, "请输入图形验证码");
				return;
			}
			if(4 != code.length){
				formObj.showTips($code, "图形验证码格式不正确");
				return;
			}
			formObj.showTips($code);
		}

		if(!id){
			alert('给个id呗');
			return;
		}
		_this.data(_const.SUBMIT_FLAG, false);
		$.post("/captcha/sendVoiceAuthCode.html", {mobile : mobile, isBind : isBind, code : code}, function(data){
			if('200' === data){
				_handler.addTipsHandler(_const.VOICE_CAPTCHA_KEY, id);
				_handler.updateTipsSeconds(id, _const.CAPTCHA_SECONDS);
				if(_code){
					$("#" + _code).next().find('img').trigger('click');
				}
			}else if('101' === data){
				alert(_input, "请输入正确的手机号码");
				_this.data(_const.SUBMIT_FLAG, true);
			}else if('102' === data){
				alert(_input, "非会员手机号");
				_this.data(_const.SUBMIT_FLAG, true);
			}else if('103' === data){
				alert(_input, "您还未绑定手机号");
				_this.data(_const.SUBMIT_FLAG, true);
			}else if('104' === data){
				alert(_input, "手机号已注册");
				_this.data(_const.SUBMIT_FLAG, true);
			}else if('105' === data){
				alert("验证码错误次数过多，请两小时后再试");
				_this.data(_const.SUBMIT_FLAG, true);
			}else if('106' === data){
				alert("图形验证码错误");
				_this.data(_const.SUBMIT_FLAG, true);
			}else{
				alert("发送短信验证码失败");
				_this.data(_const.SUBMIT_FLAG, true);
			}
		});
	}

	sendEmailCaptcha = function(_btn, _in, isBind, _code){
		var _this = $(_btn);
		var _input = '';
		var _span = _this.find("span");
		var email = '';
		var code = '';
		var id = _this.attr("id");
		var flag = _this.data(_const.SUBMIT_FLAG);
		if(false == flag){
			return;
		}
		if(true != isBind){
			isBind = false;
		}
		if(_in){
			_input = $("#" + _in);
			email = $.trim(_input.val());
			if(!email){
				formObj.showTips(_input, "请输入邮箱");
				return;
			}
			if(!formObj.isEmail(email)){
				formObj.showTips(_input, "邮箱格式不正确");
				return;
			}
			formObj.showTips(_input);
		}
		if(_code){
			var $code = $("#" + _code);
			code = $.trim($code.val());
			if(!code){
				formObj.showTips($code, "请输入图形验证码");
				return;
			}
			if(4 != code.length){
				formObj.showTips($code, "图形验证码格式不正确");
				return;
			}
			formObj.showTips($code);
		}

		if(!id){
			alert('给个id呗');
			return;
		}

		_this.data(_const.SUBMIT_FLAG, false);
		$.post("/captcha/sendEmailAuthCode.html", {email : email, isBind : isBind, code : code}, function(data){
			if('200' === data){
				_handler.emailBtn = id;
				_handler.emailSeconds = _const.CAPTCHA_SECONDS;
				_handler.addTipsHandler(_const.EMAIL_CAPTCHA_KEY, id);
				_handler.updateTipsSeconds(id, _const.CAPTCHA_SECONDS);
				if(_code){
					$("#" + _code).next().find('img').trigger('click');
				}
			}else if('101' === data){
				alert(_input, "请输入正确的邮箱");
				_this.data(_const.SUBMIT_FLAG, true);
			}else if('102' === data){
				alert(_input, "非会员邮箱");
				_this.data(_const.SUBMIT_FLAG, true);
			}else if('103' === data){
				alert(_input, "您还未绑定邮箱");
				_this.data(_const.SUBMIT_FLAG, true);
			}else if('104' === data){
				alert(_input, "邮箱已注册");
				_this.data(_const.SUBMIT_FLAG, true);
			}else if('105' === data){
				alert("验证码错误次数过多，请两小时后再试");
				_this.data(_const.SUBMIT_FLAG, true);
			}else if('106' === data){
				alert("图形验证码错误");
				_this.data(_const.SUBMIT_FLAG, true);
			}else{
				alert("发送邮箱验证码失败");
				_this.data(_const.SUBMIT_FLAG, true);
			}
		});
	}

	alertAjaxTips = function(data){
		if(data){
			if(data.msg){
				alert(data.msg);
			}else if(200 === data.code){
				window.location.reload(true);
			}
		}else{
			alert("系统繁忙，请稍后再试");
		}
	}

	onEnterClick = function(id){
		document.onkeydown=function(event){// 回车
			var e = event || window.event || arguments.callee.caller.arguments[0];
			if(e && e.keyCode == 13){
				$("#" + id).trigger("click");
			}
		};
	}

	mobileIsExist = function(_in){
		var _input = $(_in);
		var val = _input.val();
		if(!val){
			formObj.showTips(_input, "请输入手机号");
			return;
		}
		if(!formObj.isMobileNumber(val)){
			formObj.showTips(_input, "手机号格式不正确");
			return;
		}
		$.post("/user/mobile_exist.html", {mobile : val}, function(data){
			if(data){
				if(200 === data.code){
					formObj.showTips(_input);
				}else if(data.msg){
					formObj.showTips(_input, data.msg);
				}
			}
		}, "json");
	}


	emailIsExist = function(_in){
		var _input = $(_in);
		var val = _input.val();
		if(!val){
			formObj.showTips(_input, "请输入邮箱号");
			return;
		}
		if(!formObj.isEmail(val)){
			formObj.showTips(_input, "邮箱格式不正确");
			return;
		}
		$.post("/user/email_exist.html", {email : val}, function(data){
			if(data){
				if(200 === data.code){
					formObj.showTips(_input);
				}else if(data.msg){
					formObj.showTips(_input, data.msg);
				}
			}
		}, "json");
	}

	mobileIsExistFindPwd = function(_in){
		var _input = $(_in);
		var val = _input.val();
		if(!val){
			formObj.showTips(_input, "请输入手机号");
			return;
		}
		if(!formObj.isMobileNumber(val)){
			formObj.showTips(_input, "手机号格式不正确");
			return;
		}
		$.post("/user/mobile_exist.html", {mobile : val}, function(data){
			if(data){
				if(200 === data.code){
					formObj.showTips(_input,"用户不存在");
				} else if("请输入正确的手机号" === data.msg){
					formObj.showTips(_input, data.msg);
				}else{
					formObj.showTips(_input);
				}
			}
		}, "json");
	}

	emailIsExistFindPwd = function(_in){
		var _input = $(_in);
		var val = _input.val();
		if(!val){
			formObj.showTips(_input, "请输入邮箱号");
			return;
		}
		if(!formObj.isEmail(val)){
			formObj.showTips(_input, "邮箱格式不正确");
			return;
		}
		$.post("/user/email_exist.html", {email : val}, function(data){
			if(data){
				if(200 === data.code){
					formObj.showTips(_input,"用户不存在");
				} else if("请输入正确的邮箱号" === data.msg){
					formObj.showTips(_input, data.msg);
				}else{
					formObj.showTips(_input);
				}
			}
		}, "json");
	}

    var placeholder = "手机验证码";
    var validate_type = "sms";

    var validate_controller = false;

    function validatePhone(obj,type){
        var $this = $(obj);
        var flag = $this.data(_const.SUBMIT_FLAG);
        if(false == flag){
            $("#boxLoginTips").text(_const.CAPTCHA_SECONDS+"秒内只能发送一次验证码！");
            return;
        }
        if($this.text()=="请求中"){
            $("#boxLoginTips").text("请求中，请稍候！");
            return;
        }
        var code = $("#box_img_code").val();
        if(isEmpty(code)){
            $("#boxLoginTips").text("请先填写图形验证码！");
            return;
        }
        if(code.length!=4){
            $("#boxLoginTips").text("图形验证码错误");
            return;
        }
        $this.text("请求中");
        sendSmsCaptcha1($this,type,code);
    }

    function sendSmsCaptcha1(_btn,type,code){
        var _this = $(_btn);
        var id = _this.attr("id");
        var flag = _this.data(_const.SUBMIT_FLAG);
        if(false == flag){
            return;
        }
        _this.data(_const.SUBMIT_FLAG, false);
        var url;
        if(type=="sms"){
            url = '/captcha/sendSMSAuthCode';
        }
        if(type=="email"){
            url = '/captcha/sendEmailAuthCode';
        }
        if(type=="voice"){
            url = '/captcha/sendVoiceAuthCode';
        }
        $.post(url, {code:code, isLogin:"true"}, function(res){
            if('101' == res){
                $("#boxLoginTips").text('请输入手机号');
                _this.data(_const.SUBMIT_FLAG, true);
            }else if('102' == res){
                $("#boxLoginTips").text('非会员手机号');
                _this.data(_const.SUBMIT_FLAG, true);
            }else if('103' == res){
                $("#boxLoginTips").text('您还没绑定手机号，去绑定');
                _this.data(_const.SUBMIT_FLAG, true);
            }else if('104' == res){
                $("#boxLoginTips").text('手机号已存在');
                _this.data(_const.SUBMIT_FLAG, true);
            }else if('105' == res){
                $("#boxLoginTips").text('验证码错误次数过多，请两小时后再试！');
                _this.data(_const.SUBMIT_FLAG, true);
            }else if('106' == res){
                $("#boxLoginTips").text('图形验证码错误！');
                _this.data(_const.SUBMIT_FLAG, true);
            }else if('200' == res){
                _handler.addTipsHandler(_const.SMS_CAPTCHA_KEY, id);
                _handler.updateTipsSeconds(id, _const.CAPTCHA_SECONDS,1);
            }else{
                $("#boxLoginTips").text('发送短信验证码失败');
                _this.data(_const.SUBMIT_FLAG, true);
            }
            _this.text("发送");
        }, 'json');
    }

    $("#box_image_code").click(function(){
        $(this).get(0).src = "/servlet/ValidateImageServlet?"+new Date().getTime();
    });
    $("#box_code").blur(function(){
        $("#boxLoginTips").text("");
    });
    $("#box_img_code").blur(function(){
        $("#boxLoginTips").text("");
    });
    $("#indexLoginName2").blur(function(){
        $("#boxLoginTips").text("");
    });
    $("#indexLoginPwd2").blur(function(){
        $("#boxLoginTips").text("");
    });

    $("#box_sendCode").on('click', function(){
        validatePhone(this,validate_type);
    });

    $("#box_sendVoiceCode").on('click', function(){
        validatePhone($("#box_sendCode"),'voice');
    });
	var box_login_controller = true;
    $("#box_login_btn").click(function(){
    	if(!box_login_controller){
            $("#boxLoginTips").text("请求中，请稍后");
    		return;
		}
    	if(validate_controller){
            var phoneCode = $("#box_code").val();
            if(phoneCode.length != 6){
                $("#boxLoginTips").text("验证码位数不符");
                return;
            }
            box_login_controller = false;
			$.post('/user/login',{phoneCode: phoneCode}, function(res){
                    box_login_controller = true;
                    if (res.resultCode == 1) {
                        jump(res);
                    }else if (res.code == "-15") {
                        $("#boxLoginTips").text("您没有发送验证码");
                    } else if (res.code == "111") {
                        $("#boxLoginTips").text("验证码错误！剩余验证次数"+res.leftCount+"次");
                    }
                },"json");
            return;
		}
        var loginName = $("#indexLoginName2").val();
        var password = $("#indexLoginPwd2").val();
        if(isEmpty(loginName)){
            $("#boxLoginTips").html("用户名不能为空！");
            return;
        }
        if(password.length < 6){
            $("#boxLoginTips").text("密码位数不小于6位数！");
            return;
        }
        box_login_controller = false;
        $.post('/user/login',{password: password,loginName: loginName },function(result){
            if(result!=null){
                box_login_controller = true;
                if(result.resultCode == 1){
                    jump(result);
                }else if(result.resultCode == -1){
                    $("#boxLoginTips").html("用户名或密码错误");
                }else if(result.resultCode == -2){
                    $("#boxLoginTips").html("此ip登录频繁，请2小时后再试");
                }else if(result.resultCode == -3){
                    if(result.errorNum == 0){
                        $("#boxLoginTips").html("此ip登录频繁，请2小时后再试");
                    }else{
                        $("#boxLoginTips").html("用户名或密码错误，您还有"+result.errorNum+"次机会");
                    }
                }else if(result.resultCode == -4){
                    $("#boxLoginTips").html("请设置启用COOKIE功能")
                }else if(result.resultCode == 1){
                    window.location.href = document.getElementById("coinMainUrl").value;
                }else if(result.resultCode == 2){
                    $("#boxLoginTips").html("账户出现安全隐患被冻结，请尽快联系客服");
                }else if(result.resultCode == -5){
                    $("#box_image_code").get(0).src = "/servlet/ValidateImageServlet?"+new Date().getTime();
                    $(".login_box_safe").show();
                    center_box("login_box");
                    $("#box_voice_tips").show();
                    validate_controller = true;
                    $("#boxLoginTips").html("非常用设备，需要校验手机验证码");
                }else if(result.resultCode == -6){
                    $("#box_image_code").get(0).src = "/servlet/ValidateImageServlet?"+new Date().getTime();
                    $(".login_box_safe").show();
                    center_box("login_box");
                    validate_controller = true;
                    placeholder = "邮箱验证码";
                    $("#box_code_title").text(placeholder);
                    $("#box_code").attr("placeholder" , placeholder);
                    validate_type = "email";
                    $("#boxLoginTips").html("非常用设备，需要校验邮箱验证码");
                }
            }
        },"json");
    });
})(jQuery);
function jump(res){
    if(res.code == 1){
        alert("登录成功！但您未绑定手机和邮箱，为了你的资金安全请前往绑定！");
        location.href = "/account/security.html";
    }else{
        var forward_url = $("#forward_url").val();
        if (!forward_url) {
            var args = location.search.substr(1).split('&');
            for (var i = 0; i < args.length; i++) {
                var ps = args[i].split('=');
                if (ps[0] == 'redirectUrl') {
                    forward_url = decodeURIComponent(ps[1]);
                    break;
                }
            }
        }
        if(forward_url){
            window.location.href = forward_url;
        }else{
            window.location.reload();
        }
    }
}