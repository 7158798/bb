function initPage(){
    /*$("#title_lis li.db").on("click", function(){
        var $this = $(this);

        $("#title_lis li.cur").removeClass('cur');
        $this.addClass('cur');

        var index = $this.data('index');
        var $divs = $("#contents");
        $divs.find('.tm_account:visible').addClass('dn');
        $divs.find('.tm_account:eq(' + index + ')').removeClass('dn');
    });*/
    $("#money_input").blur(function(){
        var value=$(this).val();
        if(isEmpty(value)){
            $("#money_show").text(0);
        }else{
            $("#money_show").text($(this).val());
        }
    });
    $("#ali_money_input").blur(function(){
        var value=$(this).val();
        if(isEmpty(value)){
            $("#ali_money_show").text(0);
        }else{
            $("#ali_money_show").text($(this).val());
        }
    });
    $("#money_input").focus(function(){
        $("#money_show").text(0);
    });
    $("#ali_money_input").focus(function(){
        $("#ali_money_show").text(0);
    });
    $("#epaySuccessBtn").on("click", function(){
        window.location.reload(true);
    });
    $("#ebanks i").on("click", function(){
        var $this = $(this);
        var bank = $this.data("bank");
        var ebankInput = $("#ebank_input");
        var curBank = ebankInput.val();

        $("#ebanks i.cur").removeClass("cur");
        $this.addClass("cur");
        ebankInput.val(bank);
        $("#seled_ebank").removeClass("bank_" + curBank.toLowerCase()).addClass("bank_" + bank.toLowerCase());
        closeWindow('ebank_float_window');
    });
    $(".chargeAgain").on("click", function(){
        var $this = $(this);
        var type = $this.data("type");
        if('1' == type){
            $("#ebanks i[data-bank='" + $this.data("bank") + "']").trigger("click");
            $("#ebank_money_input").val($this.data("num"));
            $("#ebank_fir_confirm").trigger("click");
        }else if('2' == type){
            $("#weixin_money_input").val($this.data("num"));
            $("#weixin_cut_fee").text($this.data("actual"));
            $("#weixin_fir_confirm").click();
        }
    });
    $("#sel_bank").on('mouseover', function(){
        $(this).addClass('sel_bank_hover');
        $("#banks_list").show();
    });
    $("#sel_bank").on('mouseleave', function(){
        $(this).removeClass('sel_bank_hover');
        $("#banks_list").hide();
    });
    $("#banks_list").on('mouseover', function(){
        $("#sel_bank").addClass('sel_bank_hover');
        $(this).show();
    });
    $("#banks_list").on('mouseleave', function(){
        $("#sel_bank").removeClass('sel_bank_hover');
        $(this).hide();
    });
    $("#banks_list i").on("click", function(){
        var $this = $(this);
        var bank = $this.data("bank");
        var ebankInput = $("#ebank_input");
        var curBank = ebankInput.val();

        $("#banks_list i.cur").removeClass("cur");
        $this.addClass("cur");
        ebankInput.val(bank);
        $("#seled_ebank").removeClass("bank_" + curBank.toLowerCase()).addClass("bank_" + bank.toLowerCase());
        $("#sel_bank").removeClass('sel_bank_hover');
        $("#banks_list").hide();
    });

    $(".selector").custom_selector({boxClass:"selector_height"});

    function showResultDialog(result){
        if(!result) return;
        if(result.fbankName.indexOf("支付宝") > -1){
            $("#ali_bankMoney").text(result.money);
            $("#ali_bankInfo").text(result.tradeId);
            $("#ali_bankDesc").text(result.tradeId);
            $("#ali_ownerName").text(result.fownerName);
            $("#ali_bankNumber").text(result.fbankNumber);
            center_size("ali_tm_float_window");
            $(window).resize(function(){
                center_size("ali_tm_float_window");
            });
            $("#ali_tm_float_window,#tm_yy").show();
        }else{
            $("#bankAddressDetail").text(result.fbankAddress);
            $("#bankMoney").text(result.money);
            $("#bankInfo").text(result.tradeId);
            $("#bankDesc").text(result.tradeId);
            $("#ownerName").text(result.fownerName);
            $("#bankNumber").text(result.fbankNumber);
            $("#bankAddress").text(result.fbankName);
            center_size("tm_float_window");
            $(window).resize(function(){
                center_size("tm_float_window");
            });
            $("#tm_float_window,#tm_yy").show();
        }
    }

    /* 银行卡确认充值 */
    $("#fir_confirm").click(function(){
        if(isEmpty($("#money_input").val())){
            alert("充值金额不能为空");
            return;
        }
        if($("#money_input").val()*1<$("#minMoney").val()*1){
            alert("最小充值金额为"+$("#minMoney").val()+"元");
            return;
        }
        if(isEmpty($("#bank_num").val())){
            alert("银行卡号不能为空");
            return;
        }
        var money=$("#random_money").text();
        var sbank = $("#bank_sel").val();
        var account = $("#bank_num").val();
        var payee = $("#payee").val();
        var bankType = $("#fir_bank_type").val();
        var url = "/account/chargermb.html?random="+Math.round(Math.random()*100);
        var param={money:money,sbank:sbank,account:account,payee:payee,bankType:bankType};
        jQuery.post(url,param,function(data){
            var result = data ;
            if(result!=null){
                if(result == -1){
                    alert("最小充值金额为"+minMoney+"元");
                }else if(result == -2){
                    alert("充值金额不合法");
                }else if(result == -30){
                    alert('充值额度已超出' + $("#maxRMB").val() + '，需要手持身份认证哦！');
                    window.location.href = '/account/uploadIdentifyPic.html';
                }else if(result.isHasBankAccount==false){
                    alert("管理员未设置汇入银行账号，请联系客服！");
                }else{
                    showResultDialog(result);
                }
            }
        }, 'json');
    });

    $("#ebank_fir_confirm").click(function(){
        var $this = $("#ebank_money_input");
        if(isEmpty($this.val())){
            alert("充值金额不能为空");
            return;
        }
        if($this.val()*1<$this.data("minval")*1){
            alert("最小充值金额为"+$this.data("minval")+"元");
            return;
        }
        showWindow("charge_confirm");
        $this.closest("form").submit();
    });
    $("#weixin_fir_confirm").click(function(){
        var $this = $("#weixin_money_input");
        if(isEmpty($this.val())){
            alert("充值金额不能为空");
            return;
        }
        if($this.val()*1<$this.data("minval")*1){
            alert("最小充值金额为"+$this.data("minval")+"元");
            return;
        }

        $form = $this.closest("form");
        $.post($form.attr('action'), $form.serialize(), function(data){
            if(data.code == 101){
                alert("最小充值金额为"+$("#ali_minMoney").val()+"元");
            }else if(data.code == 102){
                alert('充值额度已超出' + $("#maxRMB").val() + '，需要手持身份认证哦！');
                window.location.href = '/account/uploadIdentifyPic.html';
            }else if(data.code == 200){
                // 成功
                showPayQrcode(data.codeUrl, data.orderId);
                paidToReloadPage(data.orderId);
            }else{
                alert('下单失败，请稍后再试！');
            }
        }, 'json');
        showPayQrcodeLoading();
    });
    $('#weixin_money_input').keyup(function(){
        var $this = $(this);
        $this.val($this.val().replace(/\D/g,''));
        
        var amount = $this.val() * 1;
        $('#weixin_cut_fee').text(amount - (amount * 0.005).toFixed(2));
    });
    /* 支付宝确认 */
    $("#ali_fir_confirm").click(function(){
        if(isEmpty($("#ali_money_input").val())){
            alert("充值金额不能为空");
            return;
        }
        if($("#ali_money_input").val()*1<$("#ali_minMoney").val()*1){
            alert("最小充值金额为"+$("#ali_minMoney").val()+"元");
            return;
        }
        if(isEmpty($("#ali_bank_num").val())){
            alert("支付宝账号不能为空");
            return;
        }
        var money=$("#ali_random_money").text();
        var sbank = "支付宝";
        var account = $("#ali_bank_num").val();
        var payee = $("#ali_payee").val();
        var bankType = $("#ali_bank_type").val();
        var url = "/account/chargermb.html?random="+Math.round(Math.random()*100);
        var param={money:money,sbank:sbank,account:account,payee:payee,bankType:bankType};
        jQuery.post(url,param,function(data){
            var result = data ;
            if(result!=null){
                if(result == -1){
                    alert("最小充值金额为"+$("#ali_minMoney").val()+"元");
                }else if(result == -2){
                    alert("充值金额不合法");
                }else if(result == -30){
                    alert('充值额度已超出' + $("#maxRMB").val() + '，需要手持身份认证哦！');
                    window.location.href = '/account/uploadIdentifyPic.html';
                }else if(result == -31){
                    alert('您的手持身份认证还未审核！');
                }else if(result == -32){
                    alert('您的手持身份认证审核不通过，请上传您本人规范证件照！');
                    window.location.href = '/account/uploadIdentifyPic.html';
                }else if(result.isHasBankAccount==false){
                    alert("管理员未设置汇入支付宝账号，请联系客服！");
                }else{
                    showResultDialog(result);
                }
            }
        }, 'json');
    });
    $("#Tenbody").find(".look_again").each(function(){
        $(this).click(function(){
            var fbank=$(this).data("fbank");
            var bankname=$(this).data("bankname");
            var bankaddress=$(this).data("bankaddress");
            var ownername=$(this).data("ownername");
            var banknum=$(this).data("banknum");
            var famount=$(this).data("famount");
            var fid=$(this).data("fid");
            if(fbank=="支付宝"){
                $("#ali_bankMoney").text(famount);
                $("#ali_bankInfo").text(fid);
                $("#ali_bankDesc").text(fid);
                $("#ali_ownerName").text(ownername);
                $("#ali_bankNumber").text(banknum);
                center_size("ali_tm_float_window");
                $(window).resize(function(){
                    center_size("ali_tm_float_window");
                });
                $("#ali_tm_float_window,#tm_yy").show();
            }else{
                $("#bankAddressDetail").text(bankaddress);
                $("#bankMoney").text(famount);
                $("#bankInfo").text(fid);
                $("#bankDesc").text(fid);
                $("#ownerName").text(ownername);
                $("#bankNumber").text(banknum);
                $("#bankAddress").text(bankname);
                center_size("tm_float_window");
                $(window).resize(function(){
                    center_size("tm_float_window");
                });
                $("#tm_float_window,#tm_yy").show();
            }
        });
    });
}

(function($){
    cancelChargeCny = function(id){
        if(confirm("您确定要撤销充值吗？")){
            var url = "/account/cancelChargeCny.html?random="+Math.round(Math.random()*100);
            var param={id:id};
            jQuery.post(url,param,function(data){
                window.location.reload(true) ;
            });
        }
    }
    initPage();
    $(document).on('pjax:end', function() {
        initPage();
    })
})(jQuery);