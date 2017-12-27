$(function () {
    var min = 2;
    var max = 25;
    //$("#add_form").find(".api_name").focus();
    function input_focus(obj, txt) {
        $(obj).nextAll(".info").find(".iconfont").removeClass("c_red").removeClass("c_green").addClass("c_blue").html("&#xe601;");
        $(obj).nextAll(".info").find("span").text(txt);
    }
    function input_error(obj, txt) {
        $(obj).nextAll(".info").find(".iconfont").removeClass("c_blue").removeClass("c_green").addClass("c_red").html("&#xe609;");
        $(obj).nextAll(".info").find("span").text(txt);
    }
    function input_right(obj, txt) {
        $(obj).nextAll(".info").find(".iconfont").removeClass("c_red").removeClass("c_blue").addClass("c_green").html("&#xe612;");
        $(obj).nextAll(".info").find("span").text(txt);
    }
    function apiLine(data){
        return '<tr data-id="' + data.id + '">' +
            '  <td class="api_name">' + data.name + '</td>' +
            '  <td class="permission" data-permission="' + data.type + '">' + data.type_name + '</td>' +
            '  <td>' + data.create_time + '</td>' +
            '  <td>' + (data.update_time ? data.update_time : '-') + '</td>' +
            '  <td data-status="' + data.status + '">' + (0 == data.status ? '<span class="c_green">正常</span>' : '<span class="c_orange">禁用</span>') + '</td>' +
            '  <td><a href="javascript:void(0)" class="c_blue refresh">更新密钥</a> <a href="javascript:void(0)" class="c_blue edit">编辑</a> <a href="javascript:void(0)" class="c_blue look">查看</a></td>' +
            '</tr>';
    }
    $("#add_form,#edit_form,#look_form").find(".api_name").focus(function () {
        var $this = $(this);
        input_focus($this, min + "~" + max + "位");
    });
    $("#add_form,#edit_form,#look_form").find(".password").focus(function () {
        var $this = $(this);
        input_focus($this, "请输入您的交易密码");
    });
    $("#add_form,#edit_form,#look_form").find(".api_name").blur(function () {
        var $this = $(this);
        var name = $this.val();
        if (isEmpty(name)) {
            input_error($this, "API名称不能为空");
            return;
        }
        if (name.length < min || name.length > max) {
            input_error($this, "API名称位数不符");
            return;
        }
        input_right($this, "");
    });
    $("#add_form,#edit_form,#look_form").find(".password").blur(function () {
        var $this = $(this);
        var password = $this.val();
        if (isEmpty(password)) {
            input_error($this, "交易密码不能为空");
            return;
        }
        if(password.length<6){
            input_error($this, "交易密码不少于6位");
            return;
        }
        input_right($this, "");
    });
    /*弹出编辑框*/
    $("#Tenbody").on("click", ".edit", function () {
        $("#edit_form").find(".api_name").focus();
        var $parent = $(this).closest("tr");
        $("#edit_form").find(".api_name").val($parent.find(".api_name").text());
        var _index = $parent.find(".permission").data("permission");
        $("input[name='permission1']").eq(_index).prop("checked", "true").siblings().prop("checked", "false");
        $("#edit_box").data("id", $parent.data("id"));
        center_box("edit_box");
        $("#tm_yy").show();
        $("#edit_box").show();
        $("#edit_form").find(".api_name").focus();
    });
    /*刷新*/
    $("#Tenbody").on("click", ".refresh", function () {
        var $parent = $(this).closest("tr");
        $("#look_box1").data("id", $parent.data("id"));
        $("#look_box1").data("refresh",1);
        center_box("look_box1");
        $("#tm_yy").show();
        $("#look_box1").show();
        $("#look_form").find(".password").focus();
    });
    /*弹出查看框*/
    $("#Tenbody").on("click", ".look", function () {
        var $parent = $(this).closest("tr");
        $("#look_box1").data("id", $parent.data("id"));
        $("#look_box1").data("refresh",0);
        center_box("look_box1");
        $("#tm_yy").show();
        $("#look_box1").show();
        $("#look_form").find(".password").focus();
    });
    /*编辑*/
    $("#edit_btn").click(function () {
        var $this = $(this);
        if($this.text()=="修改中"){
            alert("请稍后");
            return;
        }
        var id = $this.closest('#edit_box').data("id");
        var $name=$("#edit_form").find(".api_name");
        var $password=$("#edit_form").find(".password");
        var name = $.trim($name.val());
        var permission = $("input[name='permission1']:checked").val();
        var password = $password.val();
        if (name.length < min || name.length > max) {
            input_error($name, "请输入"+min+"-"+max+"位的字符");
            return;
        }
        if (isEmpty(password)) {
            input_error($password, "交易密码不能为空");
            return;
        }
        if(password.length<6){
            input_error($password, "交易密码不少于6位");
            return;
        }
        $this.text("修改中");
        $.post("/account/update_api.html", {"id": id, "name": name, "password": password, "type": permission}, function (data) {
            if(data.code==401){
                $("#login").trigger("click");
            }else if (200 === data.code) {
                $("#tm_yy").hide();
                $("#edit_box").hide();
                $('#Tenbody tr[data-id="' + id + '"]').html($(apiLine(data)).html());
                $password.val("");
                $name.val("");
            } else if (201 === data.code) {
                input_error($password, "交易密码错误");
            } else if(101 === data.code) {
                alert("非法操作");
            }else if (203 === data.code) {
                alert('API分配过多');
            }else{
                alert('服务器异常');
            }
            $this.text("确认");
        }, "json");
    });

    /*添加*/
    $("#add_btn").click(function () {
        var $this = $(this);
        if($this.text()=="创建中"){
            alert("请稍后");
            return;
        }
        var $name=$("#add_form").find(".api_name");
        var $password=$("#add_form").find(".password");
        var name = $.trim($name.val());
        var permission = $("input[name='permission']:checked").val();
        var password = $password.val();
        if (name.length < min || name.length > max) {
            input_error($name, "请输入"+min+"-"+max+"位的字符");
            return;
        }
        if (isEmpty(password)) {
            input_error($password, "交易密码不能为空");
            return;
        }
        if(password.length<6){
            input_error($password, "交易密码不少于6位");
            return;
        }
        $this.text("创建中");
        $.post("/account/apply_api.html", {"name": name, "password": password, "type":permission}, function (data) {
            if(data.code==401){
                $("#login").trigger("click");
            }else  if (201 === data.code) {
                input_error($password, "交易密码错误");
            }else if (203 === data.code) {
                alert('API分配过多');
            }else if (200 === data.code) {
                $('#Tenbody').find(".nodata").remove();
                $('#Tenbody').find('table tbody tr:eq(0)').after(apiLine(data));
                $password.val("");
                $name.val("");
                showApi(data);
            }else if(101 === data.code) {
                alert("非法操作");
            } else {
                alert('系统异常');
            }
            $this.text("确认");
        }, "json");
    });
    /*查看*/
    $("#look_btn").click(function () {
        var $this=$(this);
        var $parent=$("#look_box1");
        if($this.text()=="请求中"){
            alert("请求中，请稍候");
            return;
        }
        var id = $(this).closest('#look_box1').data('id');
        var $password=$("#look_form").find(".password");
        var password = $password.val();
        if (isEmpty(password)) {
            input_error($password,"交易密码不能为空");
            return;
        }
        if(password.length<6){
            input_error($password, "交易密码不少于6位");
            return;
        }
        $this.text("请求中");
        var url="/account/view_api.html";
        var refresh=$parent.data("refresh");
        if(refresh==1){
            url="/account/refresh_api.html";
        }
        $.post(url,{"id": id, "password": password}, function (data) {
            if(data.code==401){
                $("#login").trigger("click");
            }else if (200 === data.code) {
                $("#look_box1").hide();
                showApi(data);
                if(refresh==1){
                    $('#Tenbody tr[data-id="' + id + '"]').html($(apiLine(data)).html());
                }
                $password.val("");
            } else if (201 === data.code) {
                input_error($password, "交易密码错误");
            } else if (101 === data.code) {
                alert("非法操作");
            } else {
                alert('系统异常');
            }
            $this.text("确认");
        }, "json");
    });
    /*关闭查看秘钥则清除*/
    $("#look_box2").on("click", ".close", function () {
        $(this).find(".f_message").text("");
    })

    /*显示秘钥*/
    function showApi(data){
        $("#f_message1").text(data.key);
        $("#f_message2").text(data.secret);
        center_box("look_box2");
        $("#tm_yy").show();
        $("#look_box2").show();
    }
    /*enter*/
    $("#add_form").find(".password").keyup(function(e){
       if(e.keyCode==13){
           $("#add_btn").trigger("click");
       }
    });
    $("#edit_form").find(".password").keyup(function(e){
       if(e.keyCode==13){
           $("#edit_btn").trigger("click");
       }
    });
    $("#look_form").find(".password").keyup(function(e){
       if(e.keyCode==13){
           $("#look_btn").trigger("click");
       }
    });
});