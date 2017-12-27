$(function(){
    $("#ul_fir").find("li").click(function(){
        var $this=$(this);
        var _index=$this.index();
        $this.addClass("cur").siblings().removeClass("cur");

        var $li = $("#ul_sec").find("li").eq(_index).show();
        $li.siblings().hide();
        $li.find('span.image_code_span').html('<img class="image_code" title="换一换" src="/servlet/ValidateImageServlet?' + new Date().getTime() + '">');
    });

    toRegister = function(form){
        formObj.submitForm(form, {valFun : function(_input){
            if("repassword" !== _input.attr("name")) return true;
            var _form = $(form);
            var reInput = _form.find("input[name='repassword']");
            var password = _form.find("input[name='password']").val();
            var repassword = reInput.val();
            if(repassword && password !== repassword){
                formObj.setRedIconTips(reInput);
                formObj.setFormTips(reInput, "确认密码不一致");
                return false;
            }
            return true;
        },ajaxCallback : function(data){
            if(200 == data.code){
                var forward_url;
                var args = location.search.substr(1).split('&');
                for (var i = 0; i < args.length; i++) {
                    var ps = args[i].split('=');
                    if (ps[0] == 'redirectUrl') {
                        forward_url = decodeURIComponent(ps[1]);
                        break;
                    }
                }
                if(!forward_url){
                    forward_url = '/user/auth.html';
                }
                window.location.replace(forward_url);
            }else if(101 == data.code){
                alert('您还未发送验证码');
            }else if(102 == data.code){
                alert('非法操作');
            }else if(103 == data.code){
                alert('手机号已注册');
            }else if(104 == data.code){
                alert('邮箱已注册');
            }else if(105 == data.code){
                alert('请设置登录密码');
            }else if(106 == data.code){
                alert('验证码错误次数过多，请两小时后再试');
            }else if(107 == data.code){
                alert('验证码错误，你还有' + data.leftCount + '次机会');
            }else{
                alert('注册失败');
            }
        }});
    }
    $("#regEmailBtn").on('click', function(){
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

        var $code = $('#email_image_code');
        var code = $.trim($code.val());
        if(!code){
            formObj.showTips($code, "请输入图形验证码");
            return;
        }
        if(4 != code.length){
            formObj.showTips($code, "图形验证码格式不正确");
            return;
        }

        $.post("/user/user_exist.html", {loginName : val}, function(data){
            if('false' === data){
                formObj.showTips(_input);
                sendEmailCaptcha($this, 'mail_username', true, 'email_image_code');
            }else{
                formObj.showTips(_input, "邮箱已存在");
            }
        });
    });

    $('#regSmsBtn').on('click', function(){
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

        var $code = $('#mobile_image_code');
        var code = $.trim($code.val());
        if(!code){
            formObj.showTips($code, "请输入图形验证码");
            return;
        }
        if(4 != code.length){
            formObj.showTips($code, "图形验证码格式不正确");
            return;
        }

        $.post("/user/user_exist.html", {loginName : val}, function(data){
            if('false' === data){
                formObj.showTips(_input);
                sendSmsCaptcha($this, 'cell_username', true, 'mobile_image_code');
            }else{
                formObj.showTips(_input, "手机号已存在");
            }
        });
    });
    $('#regVoiceBtn').on('click', function(){
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

        var $code = $('#mobile_image_code');
        var code = $.trim($code.val());
        if(!code){
            formObj.showTips($code, "请输入图形验证码");
            return;
        }
        if(4 != code.length){
            formObj.showTips($code, "图形验证码格式不正确");
            return;
        }

        $.post("/user/user_exist.html", {loginName : val}, function(data){
            if('false' === data){
                formObj.showTips(_input);
                sendVoiceCaptcha($this, 'cell_username', true, 'mobile_image_code');
            }else{
                formObj.showTips(_input, "手机号已存在");
            }
        });
    });
});