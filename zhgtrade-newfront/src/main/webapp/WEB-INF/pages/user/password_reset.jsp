<!-- 重设密码页面 author:yujie 2016-04-25 -->
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="../common/includes.jsp" %>
<!DOCTYPE HTML>
<html>
<head>
    <title>${fns:getProperty('site_title')}</title>
    <meta http-equiv=X-UA-Compatible content="IE=edge,chrome=1">
    <meta content=always name=referrer>
    <meta name='renderer' content='webkit' />
    <meta name="keywords" content="${fns:getProperty('site_keywords')}">
    <meta name="description" content="${fns:getProperty('site_description')}">
    <link rel="icon" href="/favicon.ico"/>
    <link rel="shortcut icon" href="/favicon.ico"/>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8"/>
    <link rel="stylesheet" type="text/css" href="${resources}/static/css/common/reset.css">
    <link rel="stylesheet" type="text/css" href="${resources}/static/css/common/style.css">
    <link rel="stylesheet" href="${resources}/static/css/register.css"/>
</head>
<body>
<%@include file="../common/header.jsp" %>
<div class="center_page register_wrapper f16">
    <div class="f24 title">重设密码</div>
    <div class="sec">
        <div class="item">
            <form id="resetForm" action="/user/reset_pwd.html" method="post">
                <input type="hidden" name="token" value="${sessionScope.form_token}">
                <p class="mt20">
                    <span class="db fl fir">登录名：</span>
                    <input type="text" class="db fl" name="loginName" require="true", data-name="登录名"/>
                    <span class="info f12 db fl ml10">
						<i class="iconfont"></i>
						<span class="c_gray"></span>
					</span>
                </p>
                <p class="mt20">
                    <span class="db fl fir">新密码：</span>
                    <input type="password" class="db fl" name="password" require="true" data-name="密码" data-min="6" data-max="20" />
                    <span class="info f12 db fl ml10">
                        <i class="iconfont c_blue"></i>
                        <span class="c_gray"></span>
                    </span>
                </p>
                <p class="mt20">
                    <span class="db fl fir">确认密码：</span>
                    <input type="password" class="db fl" name="repassword" require="true" data-name="确认密码" onkeydown="onEnterClick('reset_pwd_btn');"/>
                    <span class="info f12 db fl ml10">
                        <i class="iconfont c_blue"></i>
                        <span class="c_gray"></span>
                    </span>
                </p>
                <a id="reset_pwd_btn" class="confirm bg_blue db mt20" onclick="doReset(document.getElementById('resetForm'));" href="javascript:void(0);">确定</a>
            </form>
        </div>
    </div>
</div>
<script >
    function doReset(form){
        formObj.submitForm(form, {valFun : function(_input){
            if("repassword" !== _input.attr("name")) return true;
            var _form = $(form);
            var _re = _form.find("input[name='repassword']");
            var pwd = _form.find("input[name='password']").val();
            var repwd = _re.val();
            if(pwd === repwd){
                return true;
            }
            formObj.setRedIconTips(_re);
            formObj.setFormTips(_re, "确认密码不一致");
            return false;
        }, ajaxCallback : function(data){
            if(200 == data.code){
                window.location.href = "/user/reset_ok.html";
            }else if(-1 == data.code){
                window.location.href = "/user/find_pwd.html";
            }else if(data.msg){
                alert(data.msg);
            }else{
                alert('服务器繁忙');
            }
        }});
    }
</script>
<%@ include file="../common/footer.jsp" %>
</body>
</html>

