/**
 * Created by sunpeng on 2016/5/17 0017.
 */
$(function () {
    /*切换电脑版，手机版相关开始*/
    if(isPC()){
        $("#tools").show();
    }else{
        $("#change_to_mobile").show();
    }



    $("#change_to_mobile").click(function(){
        var date = new Date();
        date.setTime(date.getTime()+365*24*60*60*1000);
        $.cookie('visit_way', "mobile", {expires: date});
        location.href = "/";
    });
    /*切换电脑版，手机版相关结束*/
    $("body").one('click', '.send_message', function () {
        var url = "/account/questionJson";
        $.get(url, function (data) {
            var questionTypeContent = "";
            var name = "";
            var phone = "";
            $.each(data.questionObject, function (index, obj) {

                questionTypeContent += "<option value=" + index + ">" + obj + "</option>"
            });
            $.each(data.userObject, function (index, obj) {
                if (index == 'name') {
                    name = obj;
                }
                if (index == 'telephone') {
                    phone = obj;
                }
            })
            $("#send_message_box").find("select").append(questionTypeContent);
            $("#send_message_box").find("[name=name]").val(name);
            $("#send_message_box").find("[name=telephone]").val(phone);
        }, 'json');
    });
    /*群*/
    $("#tools").find(".show_group").hover(function(){
        $(this).find(".group_tab").show();
    },function(){
        $(this).find(".group_tab").hide();
    })
})


/*改变问题类型*/
function changeQuestionType() {
    var questionType = document.getElementById("questionType").value;
}

/* 提交问题 */
function submitQuestionOnPop() {

    // var name = document.getElementById("name").value;
    // var phone = document.getElementById("phone").value;
    // var address = document.getElementById("address").value;
    // var amount = document.getElementById("amount").value;
    var questionType = $("#send_message_box").find("select").val();
    var desc = $("#send_message_box [name=desc]").val();
    var name = $("#send_message_box [name=name]").val();
    var phone = $("#send_message_box [name=telephone]").val();

    if (questionType == -1) {
        document.getElementById("errorMsg").innerHTML = "请选择问题类型。";
        return;
    } else {
        document.getElementById("errorMsg").innerHTML = "&nbsp;";
    }

    if (desc == "") {
        document.getElementById("errorMsg").innerHTML = "请输入问题描述。";
        return;
    } else {
        document.getElementById("errorMsg").innerHTML = "&nbsp;";
    }
    if (desc.length > 200) {
        document.getElementById("errorMsg").innerHTML = "问题描述不能超过200个字符。";
        return;
    } else {
        document.getElementById("errorMsg").innerHTML = "&nbsp;";
    }
    if (name == "") {
        document.getElementById("errorMsg").innerHTML = "请输入姓名。";
        return;
    } else {
        document.getElementById("errorMsg").innerHTML = "&nbsp;";
    }
    if (phone == "") {
        document.getElementById("errorMsg").innerHTML = "请输入正确的电话号码。";
        return;
    } else {
        document.getElementById("errorMsg").innerHTML = "&nbsp;";
    }
    var url = "/account/questionSubmit";
    var param = {questionType: questionType, desc: desc, name: name, phone: phone};
    jQuery.post(url, param, function (data) {
        if (data == -3) {
            document.getElementById("errorMsg").innerHTML = "请输入问题描述。";
        } else if (data == -4) {
            document.getElementById("errorMsg").innerHTML = "请输入姓名。";
        } else if (data == -5) {
            document.getElementById("errorMsg").innerHTML = "请输入正确的电话号码";
        } else if (data == 0) {
            //成功
            window.location.href = "/account/questionColumn.html";
        } else if (data == -8) {
            document.getElementById("errorMsg").innerHTML = "一个用户一天只能提交50次。";
        }
    });

}

function chatClick() {
    let display = $('#chatRoom').css('display');
    if(display == 'none'){
        $('#chatRoom').css('display','block');
    }else {
        $('#chatRoom').css('display','none');
    }
}
