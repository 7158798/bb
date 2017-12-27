$(function(){
    changeProvinceSelect = function(select){
        var index = select.selectedIndex;
        var id = parseInt(select.options[index].value);

        var pKey = "r"+id;
        var cityList = regionConf[pKey]["c"];

        var citySelect = document.getElementById("openBankCityAddr");
        citySelect.innerHTML = "";
        citySelect.options.add(new Option("请选择城市",-1));
        for(var key in cityList){
            var val = cityList[key];
            citySelect.options.add(new Option(val["n"],val["i"]));
        }
    }

    $("#provinceSelector").custom_selector({onChange : function (_input) {
        var $curItem = $('#provinceSelector .selector_items a.cur');
        var idStr = $curItem.data("id");
        var opts = '<a data-val="-1">请选择城市</a>';
        var $citySelector = $("#citySelector");

        if (idStr){
            var id = parseInt(idStr);
            var pKey = "r" + id;
            var cities = regionConf[pKey]["c"];
            for (var key in cities) {
                var val = cities[key];
                var name = val['n'];
                opts += '<a data-val="' + val['i'] + '">' + name + '</a>';
            }
        }
        $citySelector.find(".selector_items").html(opts);
        $citySelector.custom_selector();
    }, boxClass : "selector_width110"});
    $("#selectBanks").custom_selector();

    $(".choose").click(function(){
        var flag=$(this).data("flag");
        var $this=$(this);
        $(".choose").html("&#xe602;").removeClass("c_blue").addClass("c_gray").data("flag", 0);
        if(!flag){
            $this.data("flag", true);
            $("#bankCardId").val($this.data("id"));
            $this.html("&#xe600;").removeClass("c_gray").addClass("c_blue");
        }else{
            $this.data("flag", false);
            $("#bankCardId").val('');
        }
    });
    $("#add_bank_card").click(function(){
        if($("#bank_table tr.bank_li").length >= 3){
            alert("提款银行卡最多不能超过3张");
            return;
        }
        center_box("bank_card_box");
        $("#tm_yy").show();
        $("#bank_card_box").show();
    });
    $("#bank_table").on("click",".delete",function(){
        $(".float_box i.close").trigger('click');
        var $this=$(this);
        if(confirm("确定删除吗？")){
            $.post("/account/del_bank.html", {"id" : $this.data("id")}, function(data){
                $this.closest("tr.bank_li").remove();
                if('ok' === data){
                    $this.closest("tr").slideUp("slow");
                }else{
                    $(".float_box i.close").trigger('click');
                    alert("删除失败");
                }
            }, "text");
        };
    });

    submitWithdrawCnyAddress = function(type){
        //$(".float_box i.close").trigger('click');
        var cnyOutType = parseFloat(document.getElementById("cnyOutType").value);
        var openBankTypeAddr = document.getElementById("openBankTypeAddr").value;
        var withdrawAccount = $.trim(document.getElementById("withdrawAccountAddr").value);
        var pId = parseInt(document.getElementById("openBankProvinceAddr").value);
        var cId = parseInt(document.getElementById("openBankCityAddr").value);
        var branch = $.trim(document.getElementById("openBankBranchAddr").value);
        var totpCode = 0;
        var phoneCode = 0;
        var type =  document.getElementById("addressType").value;

        var regType = new RegExp("^[0-9]*$");
        if(!regType.test(cnyOutType) ){
            alert("请选择提现方式");
            return;
        }
        if(cnyOutType == 1 && openBankTypeAddr == -1){
            alert("请选择银行类型");
            return;
        }
        if( pId == -1 || cId == -1){
            alert("请选择开户所在地");
            return;
        }
        if(branch == "" || branch.length>200){
            alert("请填写开户网点，限制200字内");
            return;
        }
        if(withdrawAccount == "" || withdrawAccount.length>200 || withdrawAccount == "银行卡账号"){
            alert("请输入提现帐户");
            return;
        }
        var withdrawAccount2 = $.trim(document.getElementById("withdrawAccountAddr2").value);
        if(withdrawAccount != withdrawAccount2){
            alert("两次输入的帐户不一致");
            return;
        }

        var url = "/account/add_bank.html?random="+Math.round(Math.random()*100);
        var param={type:type,cnyOutType:cnyOutType,account:withdrawAccount,openBankType:openBankTypeAddr,pId:pId,cId:cId,branch:branch};
        jQuery.post(url,param,function(data){
            var result = eval('(' + data + ')');
            if(result!=null){
                if(result.resultCode == -2){
                    alert("非法提交");
                }else if(result.resultCode == -8){
                    if(result.errorNum == 0){
                        alert("谷歌验证码错误多次，请2小时后再试！");
                    }else{
                        alert("谷歌验证码错误！您还有"+result.errorNum+"次机会");
                    }
                }else if(result.resultCode == -9){
                    if(result.errorNum == 0){
                        alert("短信验证码错误多次，请2小时后再试！");
                    }else{
                        alert("短信验证码错误！您还有"+result.errorNum+"次机会");
                    }
                }else if(result.resultCode == -1){
                    alert("提款银行卡最多只能绑定3张");
                }else if(result.resultCode == 0){
                    window.location.href="/account/withdrawCny.html?outCnyType="+cnyOutType;
                }else if(result.resultCode == -13){
                    alert("您没有绑定手机或谷歌验证，请去安全中心绑定手机或谷歌验证后提现。");
                }
            }
        });
    }

    var onClick = false;

    $('#withdrawBalance').click(function(){
        onClick = true;
    })

    withdrawAmountBlur = function(withdrawAmount_id){
        if (onClick === false) {return}
        var withdrawAmount = parseFloat(document.getElementById(withdrawAmount_id).value);

        var reg = new RegExp("^[0-9]+\.{0,1}[0-9]{0,8}$");
        if(!reg.test(withdrawAmount) ){
            alert("请输入提现金额");
            return;
        }
        var url = "/account/withdrawAmountBlur.html?random="+Math.round(Math.random()*100);
        var min = document.getElementById("min_double").value ;
        var max = document.getElementById("max_double").value ;
        var param={withdrawAmount:withdrawAmount};
        jQuery.post(url,param,function(data){
            if(data == -3){
                alert("最小提现金额为："+min+"元");
            }else if(data == -5){
                alert("您的余额不足！");
            }else if(data == -200){
                alert("最大提现金额为"+max+"元！");
            }
        });
    }

    submitWithdrawCnyForm = function(){
        var withdrawBalance = $.trim(document.getElementById('withdrawBalance').value) ;
        var tradePwd = $.trim(document.getElementById("tradePwd").value);
        var cardId = $.trim(document.getElementById("bankCardId").value);
        var  totpCode = 0;
        var  phoneCode = 0;
        var min = document.getElementById("min_double").value ;
        var max = document.getElementById("max_double").value ;
        if(!cardId){
            alert("请选择提款银行卡");
            return;
        }
        if(document.getElementById("cnyadd") != null){
            alert("请设置提现信息");
            return;
        }
        var reg = new RegExp("^[0-9]+\.{0,1}[0-9]{0,8}$");
        if(!reg.test(withdrawBalance) ){
            alert("请输入提现金额");
            return;
        }

        if(parseFloat(withdrawBalance) < parseFloat(min)){
            alert("最小提现金额为：￥"+min);
            return;
        }

        if(parseFloat(withdrawBalance) > parseFloat(max)){
            alert("最大提现金额为：￥"+max);
            return;
        }

        if(tradePwd == ""  || tradePwd.length>200){
            alert("请输入交易密码");
            return;
        }

        if(document.getElementById("withdrawTotpCode") != null){
            totpCode = $.trim(document.getElementById("withdrawTotpCode").value);
            if(!/^[0-9]{6}$/.test(totpCode)){
                alert("请输入正确谷歌验证码");
                return;
            }

        }
        if(document.getElementById("withdrawPhoneCode") != null){
            phoneCode = $.trim(document.getElementById("withdrawPhoneCode").value);
            if(!/^[0-9]{6}$/.test(phoneCode)){
                alert("请输入正确的验证码");
                return;
            }

        }
        if(document.getElementById("withdrawTotpCode") == null && document.getElementById("withdrawPhoneCode") == null){
            alert("您没有绑定手机或谷歌验证，请去安全中心绑定手机或谷歌验证后提现");
            return;
        }
        var url = "/account/withdrawCnySubmit.html?random="+Math.round(Math.random()*100);
        var param={tradePwd:tradePwd,withdrawBalance:withdrawBalance,phoneCode:phoneCode,totpCode:totpCode,cardId:cardId};
        jQuery.post(url,param,function(data){
            var result = eval('(' + data + ')');
            if(result!=null){
                if(200 == result.code){
                    document.getElementById("withdrawCnyButton").disabled="true";
                    window.location.href="/account/withdrawCny.html?success";
                }else if(101 == result.code){
                    alert("最小提现金额为："+min+"元");
                }else if(102 == result.code){
                    alert("最大提现金额为："+max+"元");
                }else if(103 == result.code){
                    alert("您的余额不足！");
                }else if(104 == result.code){
                    alert("请先设置您的提现银行卡信息");
                }else if(105 == result.code){
                    alert("交易密码错误");
                }else if(106 == result.code){
                    alert('提现需要手持身份认证，您还未上传手持身份证照片或者还未通过审核');
                    window.location.href = '/account/uploadIdentifyPic.html';
                }else if(107 == result.code){
                    alert("每日人民币提现最多"+result.count+"次，请明天提现");
                }else if(110 == result.code){
                    alert("验证码错误多次，请2小时后再试！");
                }else if(111 == result.code){
                    alert("验证码错误！您还有"+result.leftCount+"次机会");
                }else if(108 == result.code){
                    alert("您还没有发送验证码");
                } else{
                    alert("提款失败");
                }
            }
        });
    }

    cancelWithdrawCny = function(id){
        if(confirm("您确定要撤销提现吗？")){
            var url = "/account/cancelWithdrawCny.html?random="+Math.round(Math.random()*100);
            var param={id:id};
            jQuery.post(url,param,function(data){
                window.location.reload(true) ;
            });
        }
    }
});



