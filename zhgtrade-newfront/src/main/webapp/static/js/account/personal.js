// changeProvinceSelect = function (select) {
//     var index = select.selectedIndex;
//     var idStr = select.options[index].attributes['data-id'].value;
//     if (!idStr) return;
//     var id = parseInt(idStr);
//
//     var pKey = "r" + id;
//     var cityList = regionConf[pKey]["c"];
//
//     var citySelect = document.getElementById("cityAddress");
//     citySelect.innerHTML = "";
//     citySelect.options.add(new Option("请选择城市", ""));
//     for (var key in cityList) {
//         var val = cityList[key];
//         var name = val['n'];
//         var opt = new Option(name, name);
//         if ("${city}" === name) {
//             opt.selected = true;
//         }
//         citySelect.options.add(opt);
//     }
// }

$(function () {
    /*$("#provinceAddr").selectOrDie();
    $("#cityAddress").selectOrDie();*/
    var _this = $("#provinceAddr");
    if (_this.val()) {
        $("#provinceAddr").trigger("change");
    }
});

/*检查修改信息表单是否填写完整，onkeyup和onclick事件触发此函数*/
function checkForm() {
    var username = $("[name=nickName]").val();
    var province = $("[name=province]").val();
    var city = $("[name=city]").val();
    var address = $("[name=address]").val();
    if (!(username == "" || province == "" || city == "" || address == "")) {
        $("#personalForm a").removeClass("bg_verygray");
        $("#personalForm a").addClass("bg_blue");
    } else {
        $("#personalForm a").removeClass("bg_blue");
        $("#personalForm a").addClass("bg_verygray");
    }
}

(function($){

    updateUserInfo = function(form){
        formObj.submitForm(form, {ajaxCallback : function(data){
            alertAjaxTips(data);
        }});
    }

    submitAuthInfo = function(form){
        formObj.submitForm(form, {valFun : function(_input){
            /*var _re = $(_input);
            var name = _re.attr("name");
            if("reRealName" !== name) return true;
            var _name = $("#realName").val();
            var _reval = _re.val();
            if(_name !== _reval){
                showTips(_input, "确认真实姓名不一致");
                return false;
            }*/
            return true;
        }, ajaxCallback : function(data){
            alertAjaxTips(data);
        }});
    }
})(jQuery);