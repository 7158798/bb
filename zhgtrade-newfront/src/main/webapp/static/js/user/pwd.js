$(function () {
    $("#ul_fir").find("li").click(function () {
        var $this = $(this);
        var _index = $this.index();
        $this.addClass("cur").siblings().removeClass("cur");
        $("#ul_sec").find("li").eq(_index).show().siblings().hide();
    });
    $("#findPwdEmailBtn").on('click', function(){
        var $this = $(this);
        var flag = $this.data(_const.SUBMIT_FLAG);
        if(false == flag){
            return;
        }
        var _input = $('#mail_username');
        var val = _input.val();
        if(!val){
            formObj.showTips(_input, "请输入邮箱号");
            return;
        }
        if(!formObj.isEmail(val)){
            formObj.showTips(_input, "邮箱格式不正确");
            return;
        }
        sendEmailCaptcha($this, 'mail_username');
    });

    $('#findPwdSmsBtn').on('click', function(){
        var $this = $(this);
        var flag = $this.data(_const.SUBMIT_FLAG);
        if(false == flag){
            return;
        }
        var _input = $('#cell_username');
        var val = _input.val();
        if(!val){
            formObj.showTips(_input, "请输入手机号");
            return;
        }
        if(!formObj.isMobileNumber(val)){
            formObj.showTips(_input, "手机号格式不正确");
            return;
        }
        sendSmsCaptcha($this, 'cell_username');
    });
    $('#findPwdVoiceBtn').on('click', function(){
        var $this = $(this);
        var flag = $this.data(_const.SUBMIT_FLAG);
        if(false == flag){
            return;
        }
        var _input = $('#cell_username');
        var val = _input.val();
        if(!val){
            formObj.showTips(_input, "请输入手机号");
            return;
        }
        if(!formObj.isMobileNumber(val)){
            formObj.showTips(_input, "手机号格式不正确");
            return;
        }
        sendVoiceCaptcha($this, 'cell_username');
    });
});

(function($){
    toFindPwd = function(form){
        formObj.submitForm(form, {ajaxCallback : function(data){
            if(200 == data.code){
                window.location.replace("/user/reset_pwd.html");
            }else if(101 == data.code){
                alert('您还未发送验证码');
            }else if(102 == data.code){
                alert('非法操作');
            }else if(103 == data.code){
                alert('手机号不存在');
            }else if(104 == data.code){
                alert('邮箱不存在');
            }else if(105 == data.code){
                alert('验证码错误次数过多，请两小时后再试');
            }else if(106 == data.code){
                alert('验证码错误，你还有' + data.leftCount + '次机会');
            }else{
                alert('找回密码失败');
            }
        }});
    }
})(jQuery);