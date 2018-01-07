/**
 * 显示充值地址和数量
 */
function changeQuestionType() {
    var questionType = document.getElementById("questionType").value;
}
/**
 * 提交问题
 */
function submitQuestion() {
    var questionType = document.getElementById("questionType").value;
    var address = document.getElementById("address").value;
    var amount = document.getElementById("amount").value;
    var desc = document.getElementById("desc").value;
    var name = document.getElementById("name").value;
    var phone = document.getElementById("phone").value;

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
/**
 * 提交回复
 */
function submitQuestionReply(id) {
    var reply = document.getElementById("reply").value;
    if (reply == "") {
        document.getElementById("errorMsg").innerHTML = "请输入回复。";
        return;
    } else {
        document.getElementById("errorMsg").innerHTML = "&nbsp;";
    }
    if (reply.length > 200) {
        document.getElementById("errorMsg").innerHTML = "回复不能超过200个字符。";
        return;
    } else {
        document.getElementById("errorMsg").innerHTML = "&nbsp;";
    }
    var url = "/question/submitQuestionReply.html?random=" + Math.round(Math.random() * 100);
    var param = {qid: id, reply: reply};
    jQuery.post(url, param, function (data) {
        if (data == -1) {
            document.getElementById("errorMsg").innerHTML = "请输入回复。";
        } else if (data == -2) {
            document.getElementById("errorMsg").innerHTML = "未找到该问题或者该问题已经完成。";
        } else if (data == 0) {
            //成功
            window.location.href = "/question/seeQuestion.html?qid=" + id;
        } else if (data == 2) {
            document.getElementById("errorMsg").innerHTML = "账户出现安全隐患已被冻结，请尽快联系客服。";
        } else if (data == -3) {
            document.getElementById("errorMsg").innerHTML = "回复不能超过200个字符。";
        } else if (data == -4) {
            document.getElementById("errorMsg").innerHTML = "同一条问题的回复不能超过50条。";
        }
    });
}
/**
 * 撤销
 */
function cancelQuestion(id) {
    var currentPage = document.getElementById("currentPage").value;
    var type = document.getElementById("type").value;
    var url = "/account/cancelQuestion.html?qid=" + id + "&type=" + type + "&currentPage=" + currentPage + "&random=" + Math.round(Math.random() * 100);
    window.location.href = url;
}

/**
 * 提交评价
 */
function getRadioValue(name) {
    var obj = document.getElementsByName(name);
    var value = "";
    for (var i = 0; i < obj.length; i++) {
        if (obj.item(i).checked) {
            value = obj.item(i).getAttribute("value");
            break;
        } else {
            continue;
        }
    }
    return value;
}

function submitSatisfaction() {
    var satisfaction = getRadioValue("satisfaction");
    var id = document.getElementById("questionId").value;
    var comment = document.getElementById("comment").value;
    if (comment.length > 200) {
        document.getElementById("errorMsg").innerHTML = "回复不能超过200个字符。";
        return;
    } else {
        document.getElementById("errorMsg").innerHTML = "&nbsp;";
    }
    var url = "/question/submitSatisfaction.html?random=" + Math.round(Math.random() * 100);
    var param = {qid: id, comment: comment, satisfaction: satisfaction};
    jQuery.post(url, param, function (data) {
        if (data == -1) {
            document.getElementById("errorMsg").innerHTML = "回复不能超过200个字符。";
        } else if (data == -2) {
            document.getElementById("errorMsg").innerHTML = "问题不存在或已被评价!";
        } else if (data == 0) {
            window.location.href = "/question/questionColumn.html?type=1";
        }
    });
}
function seeQuestion(qid, type) {
    window.location.href = "/question/seeQuestion.html?qid=" + qid + "&type=" + type;
}
function showEvaluationPre(id) {
    dialogBoxShadow();
    document.getElementById("seeQuestion").style.display = "";
    document.getElementById("questionId").value = id;
}
function closeWithdraw() {
    dialogBoxHidden();
    document.getElementById('seeQuestion').style.display = 'none';
}

function messagedetail(id) {
    var $dialog = jQuery("#dialog");
    var url = "/account/messagedetail?random=" + Math.round(Math.random() * 100);
    var param = {id: id};
    jQuery.post(url, param, function (data) {
        if (data != null && data.result == true) {
            $dialog.html(data.content);
            $dialog.dialog({height: 260, width: 830, resizable: false, title: data.title, draggable: false});
        }
        $("#message" + id + " i:last").removeClass("c_blue").addClass("c_gray");
    }, "json");
}


$(function () {

    //全选
    $("#msg-list").on('click', '#selectAll i', function () {
        var $this = $(this);
        var isSelect = $this.css("color");
        if (isSelect == 'rgb(150, 150, 150)') {
            $(".noselect").attr("class", "noselect iconfont c_gray cp dn");
            $(".select").attr("class", "select iconfont c_blue cp");
        } else {
            $(".noselect").attr("class", "noselect iconfont c_gray cp");
            $(".select").attr("class", "select iconfont c_blue cp dn");
        }
        $this.addClass("dn").siblings().removeClass("dn");
    });

    $("#msg-list").on('click', ".selectDeleteNew i", function () {
        var $this = $(this);
        $this.addClass("dn").siblings().removeClass("dn");
        $(".selectDeleteNew").each(function () {
            if ($(this).find("i:last").attr("class") == "select iconfont c_blue cp") {
                $("#selectAll i:first").addClass("dn");
                $("#selectAll i:last").removeClass("dn");
            } else {
                $("#selectAll i:first").removeClass("dn");
                $("#selectAll i:last").addClass("dn");
                return false;

            }
        })

    });

    $("#msg-list").on('click', "#delete_news", function () {
        var $this=$(this);
        if($this.text()=="删除中"){
            alert("删除中，请稍候");
            return;
        }
        var ids = "";
        $(".selectDeleteNew").each(function () {
            var isSelect = $(this).find("i:last").attr("class");
            if (isSelect == "select iconfont c_blue cp dn") {
                return true;
            }
            var id = $(this).find("span").text();
            ids += id + ","
        })
        ids = ids.substring(0, ids.length - 1);
        if (ids == "") {
            alert("请选中要删除的信息")
        } else {
            $this.text("删除中");
            deleteBundleMessage(ids, function () {
                window.location.reload();//刷新当前页面.
            });
        }
    });
    $("#msg-list").on('click', "#flag_all", function () {
        var $this=$(this);
        if($this.text()=="操作中"){
            alert("操作中，请稍候");
            return;
        }
        var ids = "";
        $(".selectDeleteNew").each(function () {
            var isSelect = $(this).find("i:last").attr("class");
            if (isSelect == "select iconfont c_blue cp dn") {
                return true;
            }
            var id = $(this).find("span").text();
            ids += id + ","
        })
        ids = ids.substring(0, ids.length - 1);
        if (ids == "") {
            alert("选择要标记的信息")
        } else {
            $this.text("操作中");
            remarkReaded(ids, function () {
                window.location.reload();//刷新当前页面.
            });
        }


    });

    function deleteBundleMessage(ids, callback) {
        var params = {"params": ids};
        var url = "/account/deletebundlemessage";
        $.post(url, params, callback);
    }

    //把消息标记为已读
    function remarkReaded(ids, callback) {
        var url = "/account/remarkmessage";
        var params = {"params": ids};
        $.post(url, params, callback);
    }


    //工单列表点击展开
    $("#question-list").on('click', '.singlerow', function () {
        var $parent = $(this).parents("tr");
        $parent.find(".singlerow").addClass("dn");
        $parent.find(".multirow").removeClass("dn");
    })


    //工单列表点击collapse
    $("#question-list").on('click', '.multirow', function () {
        var $parent = $(this).parents("tr");
        $parent.find(".multirow").addClass("dn");
        $parent.find(".singlerow").removeClass("dn");
    })

    //

    //异步刷新工单列表
    $("#question-list").on('click', '.unsolved', function () {

        var $this = $(this);
        $this.addClass("cur");
        $this.siblings(".solved").removeClass("cur");

        var url = '/account/question-list';
        $.get(url,function (data) {
            $("#question-list").html(data);
        });

    })


    $("#question-list").on('click', '.solved', function () {

        var $this = $(this);
        $this.addClass("cur");
        $this.siblings(".unsolved").removeClass("cur");
        var url = "/account/question-list?type=2";
        $.get(url, function (data) {
            $("#question-list").html(data);
        });

    })

    //分页
    $("#msg-list").on('click', '.page a', function (e) {
        e.preventDefault();
        var url = $(this).attr('href');
        console.log(url)
        $.get(url, function (data) {
            $('#msg-list').html(data);
        })
    })

    $("#question-list").on('click', '.page a', function (e) {
        e.preventDefault();
        var url = $(this).attr('href');
        console.log(url)
        $.get(url, function (data) {
            $('#question-list').html(data);
        })
    })

    function insertTr(fid, ftype_s, fdesc, fanswer, fcreateTime, fstatus_s, $tr) {

        $tr.append("<td class='gray' width='80'>" + fid + "</td>");
        $tr.append("<td class='gray' width='110'>" + ftype_s + "</td>");

        $tr.append("<td class='gray desc singlerow' width='150'>" + fdesc + "</td>");
        $tr.append("<td class='gray answer singlerow' width='150'>" + fanswer + "</td>");
        $tr.append("<td class='gray desc multirow dn' width='150'>" + fdesc + "</td>");
        $tr.append("<td class='gray answer multirow dn' width='150'>" + fanswer + "</td>");

        $tr.append("<td class='gray' width='150'>" + fcreateTime + "</td>");
        $tr.append("<td class='gray' width='80'>" + fstatus_s + "</td>");
        $tr.append("<td class='gray' width='60'><a onclick = 'javascript:cancelQuestion(" + fid + ")'>删除</a></td>");
    }
    
    function ajaxCallback(data) {
        console.info(data)
        $(".questionlist tr:gt(0)").remove();
        var $table = $(".questionlist");
        for (var i = 0; i < data.list.length; i++) {
            var trClass = "tr" + i;
            $table.append("<tr class= trClass></tr>");
            var $tr = $table.find("tr").last();
            var question = data.list[i];
            insertTr(question.fid, question.ftype_s, question.fdesc, question.fanswer, question.fcreateTime, question.fstatus_s, $tr)
        }

        if (data.list.length == 0) {
            $table.append("<tr><td colspan='7'>您暂时没有提问记录</td></tr>");
        }

        $(".page ul").html(data.pagin);
        $(".page .currentpage").text(data.currentPage);
        $(".page #currentPage").val(data.currentPage);
        $(".page #type").val(data.type);


        $(".singlerow").on('click', function () {
            var $parent = $(this).parents("tr");
            $parent.find(".singlerow").addClass("dn");
            $parent.find(".multirow").removeClass("dn");
            // $(this).removeClass("singlerow").addClass("multirow");
        })


        //工单列表点击collapse
        $(".multirow").on('click', function () {
            // $(this).removeClass("multirow").addClass("singlerow");
            var $parent = $(this).parents("tr");
            $parent.find(".multirow").addClass("dn");
            $parent.find(".singlerow").removeClass("dn");
        })

        $(".page a").on('click', function () {
            var type = $("#type").val();
            var currentPage = $(this).text();
            var param = {'type':type, 'currentPage':currentPage};
            var url = '/account/questionlist';
            $.get(url, param, function (data) {
                ajaxCallback(data);
            })
            return false;
        })

    }


})

function isContains(before, after) {
    return new RegExp(after).test(before);
}
