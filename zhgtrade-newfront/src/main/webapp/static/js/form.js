(function($){
    function Form(){};
    formObj = new Form();

    function getTipsBox(_input){
        var $sel = $(_input).closest("span.selector");
        if($sel.length > 0){
            return $sel.nextAll(".info");
        }
        return $(_input).nextAll(".info");
    }

    Form.prototype.setRedIconTips = function(_input){
        getTipsBox(_input).find(".iconfont").removeClass("c_blue").removeClass("c_green").addClass("c_red").html("&#xe609;");
    }

    Form.prototype.setGreenIconTips = function(_input){
        getTipsBox(_input).find(".iconfont").removeClass("c_red").removeClass("c_blue").addClass("c_green").html("&#xe612;");
    }

    Form.prototype.setFormTips = function(_input, txt){
        getTipsBox(_input).find("span").text(txt);
    }

    Form.prototype.cleanFormTips = function(_input){
        formObj.setFormTips(_input, "");
    }

    Form.prototype.cleanFormIcon = function(_input){
        getTipsBox(_input).find(".iconfont").removeClass("c_blue").removeClass("c_green").removeClass("c_red").html("");
    }

    Form.prototype.isMobileNumber = function(number){
        if(!number) return false;
        return /^1+\d{10}$/.test(number);
    }

    Form.prototype.isEmail = function(email){
        if(!email) return false;
        return /^[a-z0-9]+([._\-]*[a-z0-9])*@([a-z0-9]+[-a-z0-9]*[a-z0-9]+.){1,63}[a-z0-9]+$/.test(email);
    }

    var options = {
        valFun : null,
        alertTips : null,
        ajaxCallback : null
    }

    Form.prototype.showTips = function(_input, msg){
        if(!_input && msg){
            alert(msg);
            return;
        }

        if('function' === typeof options.alertTips){
            options.alertTips(_input, msg);
        }else if(msg){
            formObj.setFormTips(_input, msg);
            formObj.setRedIconTips(_input);
        }else{
            formObj.cleanFormTips(_input);
            formObj.setGreenIconTips(_input);
        }
    }

    function baseValidate(_input, val){
        var name = _input.data("name");
        var minLen = _input.data("min");
        var maxLen = _input.data("max");

        if(!val){
            formObj.showTips(_input, "请输入" + name);
            return false;
        }else if(minLen && val.length < minLen * 1){
            formObj.showTips(_input, name + "的长度不能小于" + minLen);
            return false;
        }else if(maxLen && val.length > maxLen * 1){
            formObj.showTips(_input, name + "的长度不能大于" + maxLen);
            return false;
        }else{
            _input.val(val);
        }
        return true;
    }
    var handler=true;
    Form.prototype.submitForm = function(form, opts){
        if(opts){
            $.extend(options, opts);
        }

        var flag = true;
        var _form = $(form);

        _form.find("input, select").each(function(){
            var _this = $(this);
            var required = _this.attr("require");
            if('true' !== required) return;

            var _flag = true;
            var type = _this.attr("type");
            var val = $.trim(_this.val());
            var tagName = _this[0].tagName.toLowerCase();
            // true为遇错中断
            var ret = _this.data("ret");
            if('select' === tagName){
                if(!val){
                    var name = _this.data("name");
                    formObj.showTips(_this, "请选择" + name);
                    _flag &= false;
                }else{
                    formObj.showTips(_this);
                }
                _flag &= true;
            }else if('input' === tagName){
                switch(type){
                    case "email":
                        _flag &= baseValidate(_this, val);
                        if(_flag && !formObj.isEmail(val)){
                            formObj.showTips(_this, "邮箱格式不正确");
                            _flag = false;
                        }
                        break;
                    case "tel":
                        _flag &= baseValidate(_this, val);
                        if(_flag && !formObj.isMobileNumber(val)){
                            formObj.showTips(_this, "手机号格式不正确");
                            _flag = false;
                        }
                        break;
                    default:
                        _flag &= baseValidate(_this, val);
                        break;
                }
            }

            if(_flag && 'function' === typeof options.valFun){
                // 不处理需返回true
                _flag &= options.valFun(_this);
            }
            if(_flag){
                // success
                formObj.showTips(_this);
            }
            flag &= _flag;
            if(true == ret && !_flag){
                return false;
            }
        });
        if(flag && 'function' === typeof options.ajaxCallback){
            if(handler==false){
                return;
            }
            handler=false;
            $.post(_form.attr("action"), _form.serialize(), function(data){
                handler=true;
                options.ajaxCallback(data);
            }, "json");
        }else if(flag){
            _form.submit();
        }
    }

})(jQuery);