(function($){
    modifyWithdrawBtcAddr = function(){
        dialogBoxShadow(false);
        // $("#withdrawBtcAddrDiv").show();
        // center_size("withdrawBtcAddrDiv");

        // dialogBoxShadow(false);
        var $dialog = $("#withdrawBtcAddrDiv");
        var windowWidth = $(window).width();
        var windowHeight = getClientHeight();
        var dialogWidth = $dialog.width();
        var dialogHeight = $dialog.height();
        var left = (windowWidth-dialogWidth)/2;
        var top =  (windowHeight-dialogHeight)/2;
        $dialog.css({left:left,top:top});
        $dialog.show();

        // $("#withdrawBtcAddrDiv").show();
        document.getElementById("withdrawBtcAddr").focus();
        document.getElementById("withdrawBtcAddr").value="";
        if(document.getElementById("withdrawBtcAddrCode") != null){
            document.getElementById("withdrawBtcAddrCode").value="";
        }
        document.getElementById("withdrawBtcAddr").focus();
        callbackEnter(submitWithdrawBtcAddrForm);

        //窗口resize
        $(window).resize(function(){
            tm_center_dialog($dialog);
        });
    }

    submitWithdrawBtcAddrForm = function(coinName){
        var withdrawAddr = trim(document.getElementById("withdrawBtcAddr").value);
        var  withdrawBtcAddrTotpCode = 0;
        var  withdrawBtcAddrPhoneCode = 0;
        var symbol = document.getElementById("symbol").value;
        if(withdrawAddr == ""){
            alert('请设输入提现地址');
            return;
        }
        var start = withdrawAddr.substring(0,1);
        if(withdrawAddr.length < 10 || withdrawAddr.length > 50){
            alert("输入的地址不是一个合法的"+coinName+"地址");
            return;
        }
        if(document.getElementById("withdrawBtcAddrTotpCode") != null){
            withdrawBtcAddrTotpCode = trim(document.getElementById("withdrawBtcAddrTotpCode").value);
            if(!withdrawBtcAddrTotpCode){
                alert("请输入验证码");
                return;
            }
            if(!/^[0-9]{6}$/.test(withdrawBtcAddrTotpCode)){
                alert("谷歌验证码格式不正确");
                return;
            }
        }
        if(document.getElementById("withdrawBtcAddrPhoneCode") != null){
            withdrawBtcAddrPhoneCode = trim(document.getElementById("withdrawBtcAddrPhoneCode").value);
            if(!withdrawBtcAddrPhoneCode){
                alert("请输入验证码");
                return;
            }
            if(!/^[0-9]{6}$/.test(withdrawBtcAddrPhoneCode)){
                alert("短信验证码格式不正确");
                return;
            }
        }
        var url = "/account/modifyWithdrawBtcAddr.html?random="+Math.round(Math.random()*100);
        var param={withdrawAddr:withdrawAddr,totpCode:withdrawBtcAddrTotpCode,phoneCode:withdrawBtcAddrPhoneCode,symbol:symbol};
        jQuery.post(url,param,function(data){
            var result = eval('(' + data + ')');
            if(result!=null){
                if(200 == result.code){
                    window.location.reload(true);
                }else if(101 == result.code){
                    alert("无法绑定提现地址！");
                }else if(102 == result.code){
                    alert("验证码错误多次，请2小时后再试！");
                }else if(103 == result.code){
                    alert("验证码错误！您还有"+result.leftCount+"次机会");
                }else if(104 == result.code){
                    alert('不能绑定平台内提现地址');
                }else if(105 == result.code){
                    alert('您还没发送验证码');
                }else{
                    alert("绑定提现地址失败！");
                }
            }
        });
    }

    closeModAddressDialog = function(){
        dialogBoxHidden();
        $("#withdrawBtcAddrDiv").hide();
    }

    var onClick = false;

    $('#withdrawAmount').click(function(){
        onClick = true;
    })
    withdrawBtcAmountBlur = function(tipElement_id,withdrawAmount_id,symbol){
        if (onClick === false) {return}

        var tipElement = document.getElementById(tipElement_id) ;
        var withdrawAmount = parseFloat(document.getElementById(withdrawAmount_id).value);

        var reg = new RegExp("^[0-9]+\.{0,1}[0-9]{0,8}$");
        if(!reg.test(withdrawAmount) ){
            alert("请输入提现数量");
            return;
        }
        var url = "/account/withdrawBtcAmountBlur.html?random="+Math.round(Math.random()*100);
        var param={withdrawAmount:withdrawAmount,symbol:symbol};
        jQuery.post(url,param,function(data){
            if(data == -3){
                alert("最小提现数量为：0.1个");
            }else if(data == -5){
                alert("您的余额不足！");
            }else if(data == -1){
                alert("网络超时，请稍后重试！");
            }
        });
    }

    submitWithdrawBtcForm = function(coinName){
        var withdrawAddr = trim(document.getElementById("withdrawAddr").value);
        var withdrawAmount = trim(document.getElementById("withdrawAmount").value);
        var tradePwd = trim(document.getElementById("tradePwd").value);
        var  totpCode = 0;
        var  phoneCode = 0;

        var symbol = document.getElementById("symbol").value;

        if(document.getElementById("btcbalance")!=null && document.getElementById("btcbalance").value==0){
            alert("您的余额不足！");
            return;
        }
        if(withdrawAddr == ""){
            alert("请设置提现地址");
            return;
        }
        var reg = new RegExp("^[0-9]+\.{0,1}[0-9]{0,8}$");
        if(!reg.test(withdrawAmount) ){
            alert("请输入提现金额");
            return;
        }
        if(withdrawAmount*1 < 0.0001){
            // alert("最小提现金额为：0.001"+coinName);
            alert('提现数量需大于0.0001');
            return;
        }
        if(tradePwd == ""){
            alert("请输入交易密码");
            return;
        }
        if(document.getElementById("withdrawTotpCode") != null){
            totpCode = trim(document.getElementById("withdrawTotpCode").value);
            if(!/^[0-9]{6}$/.test(totpCode)){
                alert("请输入谷歌验证码");
                return;
            }
        }
        if(document.getElementById("withdrawPhoneCode") != null){
            phoneCode = trim(document.getElementById("withdrawPhoneCode").value);
            if(!/^[0-9]{6}$/.test(phoneCode)){
                alert("请输入验证码");
                return;
            }
        }
        if(document.getElementById("withdrawTotpCode") == null && document.getElementById("withdrawPhoneCode") == null){
            alert("您没有绑定手机或谷歌验证，请去安全中心绑定手机或谷歌验证后提现。");
            return;
        }
        var url = "/account/withdrawBtc.html?random="+Math.round(Math.random()*100);
        var param={withdrawAddr:withdrawAddr,withdrawAmount:withdrawAmount,tradePwd:tradePwd,totpCode:totpCode,phoneCode:phoneCode,symbol:symbol};
        jQuery.post(url,param,function(data){
            var result = eval('(' + data + ')');
            if(result!=null){
                if(200 == result.code){
                    document.getElementById("withdrawBtcButton").disabled="true";
                    window.location.href="/account/withdrawBtc.html?symbol="+symbol+"&success=1";
                }else if(101 == result.code){
                    alert("非法操作");
                }else if(102 == result.code){
                    alert("最小提现数量为："+result.minbtcWithdraw);
                }else if(103 == result.code){
                    alert("最大提现数量为："+result.maxbtcWithdraw);
                }else if(104 == result.code){
                    alert("您的余额不足！");
                }else if(105 == result.code){
                    alert("每日虚拟币提现最多"+result.count+"次，请明天提现");
                }else if(106 == result.code){
                    alert("验证码错误多次，请2小时后再试！");
                }else if(107 == result.code){
                    alert("验证码错误！您还有"+result.leftCount+"次机会");
                }else if(108 == result.code){
                    alert("请设置提现地址！");
                }else if(109 == result.code){
                    alert('不可在平台内进行虚拟币提现');
                }else if(110 == result.code){
                    alert('你还没发送验证码');
                }else if(111 == result.code){
                    alert('提现需要手持身份认证，您还未上传手持身份证照片或者还未通过审核');
                }else if(112 == result.code){
                    alert('交易密码错误');
                }else{
                    alert('提现失败');
                }
            }
        });
    }

    cancelWithdrawBtc = function(id){
        if(confirm("您确定要撤销提现吗？")){
            var url = "/account/cancelWithdrawBtc.html?random="+Math.round(Math.random()*100);
            var param={id:id};
            jQuery.post(url,param,function(data){
                window.location.reload(true) ;
            });
        }
    }
})(jQuery);



