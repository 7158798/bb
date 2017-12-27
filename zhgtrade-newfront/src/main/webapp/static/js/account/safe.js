(function($){

    function closeDialog(){
        $(".float_box").find(".close").trigger("click");
    }

    function resetHandler(){
        if(mobileTimeHandler){
            clearInterval(mobileTimeHandler);
            mobileTimeHandler = undefined;
        }
        if(emailTimeHandler){
            clearInterval(emailTimeHandler);
            emailTimeHandler = undefined;
        }
    }

    unBindMobile = function(form){
        formObj.submitForm(form, {ajaxCallback : function(result){
            if(result){
                if(101 == result.code){
                    alert('您还没发送验证码');
                }else if(102 == result.code){
                    closeDialog();
                    alert('非法操作');
                }else if(103 == result.code){
                    closeDialog();
                    alert('您还未绑定手机号');
                }else if(104 == result.code){
                    closeDialog();
                    alert("验证码错误过多，请2小时后再试！");
                }else if(105 == result.code){
                    alert("验证码错误！您还有"+result.leftCount+"次机会");
                }else if(200 == result.code){
                    closeDialog();
                    window.location.reload(true) ;
                }else{
                    closeDialog();
                    alert('解绑失败');
                }
            }
        }});
    }

    bindMobile = function(form){
        formObj.submitForm(form, {ajaxCallback : function(result){
            if(result){
                if(101 == result.code){
                    alert('您还没发送验证码');
                }else if(102 == result.code){
                    closeDialog();
                    alert('非法操作');
                }else if(103 == result.code){
                    closeDialog();
                    alert('手机号已被使用');
                }else if(104 == result.code){
                    closeDialog();
                    alert("验证码错误过多，请2小时后再试！");
                }else if(105 == result.code){
                    alert("验证码错误！您还有"+result.leftCount+"次机会");
                }else if(106 == result.code){
                    closeDialog();
                    alert('您已绑定手机号');
                }else if(200 == result.code){
                    closeDialog();
                    window.location.reload(true) ;
                }else{
                    closeDialog();
                    alert('绑定失败');
                }
            }
        }});
    }

    modPassword = function(form){
        formObj.submitForm(form, {valFun : function(_input){
            var _form = $(form);
            var _repwd = $(_input);
            if("rePassword" !== _repwd.attr("name")) return true;

            var repwd = _repwd.val();
            var pwd = _form.find("input[name='newPassword']").val();
            if(repwd !== pwd){
                formObj.showTips(_input, "确认密码不一致");
                return false;
            }
            return true;
        }, ajaxCallback : function(result){
            if(result){
                if(101 == result.code){
                    alert('您还没发送验证码');
                }else if(102 == result.code){
                    closeDialog();
                    alert('非法操作');
                }else if(103 == result.code){
                    alert('旧密码错误');
                }else if(104 == result.code){
                    alert("登录密码不能和交易密码一致");
                }else if(105 == result.code){
                    closeDialog();
                    alert("验证码错误过多，请2小时后再试！");
                }else if(106 == result.code){
                    alert("验证码错误！您还有"+result.leftCount+"次机会");
                }else if(200 == result.code){
                    closeDialog();
                    window.location.reload(true) ;
                }else{
                    closeDialog();
                    alert('修改失败');
                }
            }
        }});
    }

    modTradePassword = function(form){
        formObj.submitForm(form, {valFun : function(_input){
            var _form = $(form);
            var _repwd = $(_input);
            if("rePassword" !== _repwd.attr("name")) return true;

            var repwd = _repwd.val();
            var pwd = _form.find("input[name='newPassword']").val();
            if(repwd !== pwd){
                formObj.showTips(_input, "确认密码不一致");
                return false;
            }
            return true;
        }, ajaxCallback : function(result){
            if(result){
                if(101 == result.code){
                    alert('您还没发送验证码');
                }else if(102 == result.code){
                    closeDialog();
                    alert('非法操作');
                }else if(103 == result.code){
                    alert("交易密码不能和登录密码一致");
                }else if(104 == result.code){
                    closeDialog();
                    alert("验证码错误过多，请2小时后再试！");
                }else if(105 == result.code){
                    alert("验证码错误！您还有"+result.leftCount+"次机会");
                }else if(200 == result.code){
                    closeDialog();
                    window.location.reload(true) ;
                }else{
                    closeDialog();
                    alert('修改失败');
                }
            }
        }});
    }

    bindEmail = function(form){
        formObj.submitForm(form, {ajaxCallback : function(data){
            if(101 == data.code){
                alert('您还没发送验证码');
            }else if(102 == data.code){
                closeDialog();
                alert('非法操作');
            }else if(103 == data.code){
                closeDialog();
                alert('邮箱已被使用');
            }else if(104 == data.code){
                closeDialog();
                alert("验证码错误过多，请2小时后再试！");
            }else if(105 == data.code){
                alert("验证码错误！您还有"+data.leftCount+"次机会");
            }else if(106 == data.code){
                closeDialog();
                alert('您已绑定邮箱');
            }else if(200 == data.code){
                closeDialog();
                window.location.reload(true) ;
            }else{
                closeDialog();
                alert('绑定失败');
            }
        }});
    }
    unBindEmail = function(form){
        formObj.submitForm(form, {ajaxCallback : function(result){
            if(result){
                if(101 == result.code){
                    alert('您还没发送验证码');
                }else if(102 == result.code){
                    closeDialog();
                    alert('非法操作');
                }else if(103 == result.code){
                    closeDialog();
                    alert('您还未绑定邮箱');
                }else if(104 == result.code){
                    closeDialog();
                    alert("验证码错误过多，请2小时后再试！");
                }else if(105 == result.code){
                    alert("验证码错误！您还有"+result.leftCount+"次机会");
                }else if(200 == result.code){
                    closeDialog();
                    window.location.reload(true) ;
                }else{
                    closeDialog();
                    alert('解绑失败');
                }
            }
        }});
    }
})(jQuery);