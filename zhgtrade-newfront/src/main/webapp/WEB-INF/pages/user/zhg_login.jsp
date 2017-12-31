<!-- 注册页面 author:xxp 2016-04-25 -->
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="../common/includes.jsp" %>
<!DOCTYPE HTML>
<html>
<head>
    <title>${fns:getProperty('site_title')}</title>
    <meta http-equiv=X-UA-Compatible content="IE=edge,chrome=1">
    <meta content=always name=referrer>
    <meta name='renderer' content='webkit'/>
    <meta name="keywords" content="${fns:getProperty('site_keywords')}">
    <meta name="description" content="${fns:getProperty('site_description')}">
    <link rel="icon" href="/favicon.ico"/>
    <link rel="shortcut icon" href="/favicon.ico"/>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8"/>
    <link rel="stylesheet" type="text/css" href="${resources}/static/css/common/reset.css">
    <link rel="stylesheet" type="text/css" href="${resources}/static/css/common/style.css">
    <style>
        .login_box ul li {
            cursor: auto;
            height: 50px;
            line-height: 50px;
            width: 340px;
        }

        .login_box .right ul li {
            width: 210px;
        }

        .login_box {
            border: 1px solid #ddd;
            color: #333;
            margin: 40px auto;
        }

        .login_box .inner_box {
            margin: 40px auto;
            width: 552px;
            height: 200px;
        }

        .login_box .inner_box .left {
            border-right: 1px dotted #ddd;
        }

        .login_box .inner_box input {
            border-radius: 2px;
            width: 180px;
            height: 30px;
            margin-top: 10px;
        }

        .login_box .inner_box span.fir {
            width: 100px;
            text-align: right;
        }

        .login_box .inner_box a.btn {
            width: 125px;
            height: 30px;
            line-height: 30px;
            color: #FFF;
            border-radius: 2px;
            -moz-border-radius: 2px;
            -webkit-border-radius: 2px;
            margin: 10px 5px 10px 110px;
        }

        .login_box .inner_box a.rbtn {
            margin: 10px auto;
        }
    </style>
</head>
<body>
<%@include file="../common/header.jsp" %>
<div class="center_page login_box">
    <div class="inner_box">
        <div class="fl left">
            <form id="loginForm" action="/user/zhgLogin.html" method="post">
                <input type="hidden" name="token" value="${sessionScope.form_token}">
                <ul>
                    <li><span class="f16">我想绑定已有比特家账号</span></li>
                    <li><span class="fir db fl">登录名</span><input class="fl db ml10" type="text" name="loginName" value="${param.loginName}"></li>
                    <li><span class="fir db fl">密码</span><input class="fl db ml10" type="password" name="password" value="${param.password}"></li>
                    <li><a class="db bg_orange btn fl" onclick="return onSubmitForm();" href="javascript:void(0);">登录并绑定</a><a class="db fl" href="/user/find_pwd.html">忘记密码？</a></li>
                </ul>
            </form>
        </div>
        <div class="fl right">
            <form id="directLogin" action="/user/zhgLogin.html" method="post">
                <input type="hidden" name="token" value="${sessionScope.form_token}">
                <ul>
                    <li><span class="f16">还不是比特家会员？</span></li>
                    <li><a class="db bg_blue btn rbtn" onclick="document.getElementById('directLogin').submit();" href="javascript:void(0);">直接登录</a></li>
                </ul>
            </form>
        </div>
    </div>
</div>
<%@ include file="../common/footer.jsp" %>
<script>
    function onSubmitForm() {
        var $form = $('#loginForm');
        var loginName = $form.find('input[name="loginName"]').val();
        var password = $form.find('input[name="password"]').val();
        if(!loginName){
            alert('请输入登录名');
            return;
        }
        if(!password){
            alert('请输入密码');
            return;
        }
        if(password.length < 6){
            alert('密码的长度不能少于6个字符');
            return;
        }
        $form.submit();
    }
</script>
<c:choose>
    <c:when test="${102 == resultCode}">
        <script>window.location.href = '/';</script>
    </c:when>
    <c:when test="${103 == resultCode}">
        <script>alert('该用户已绑定过');</script>
    </c:when>
    <c:when test="${104 == resultCode}">
        <script>alert('请绑定已有比特家账号');</script>
    </c:when>
    <c:when test="${105 == resultCode}">
        <script>alert('请绑定已有比特家账号');</script>
    </c:when>
    <c:when test="${-1 == resultCode}">
        <script>alert('您的用户名或密码错误');</script>
    </c:when>
    <c:when test="${-2 == resultCode || (-3 == resultCode && 0 == errorNum)}">
        <script>alert('此ip登录频繁，请2小时后再试');</script>
    </c:when>
    <c:when test="${-3 == resultCode && errorNum > 0}">
        <script>alert('用户名或密码错误，您还有${errorNum}次机会');</script>
    </c:when>
    <c:when test="${-4 == resultCode}">
        <script>alert('请设置启用COOKIE功能');</script>
    </c:when>
    <c:when test="${2 == resultCode}">
        <script>alert("账户出现安全隐患被冻结，请尽快联系客服");</script>
    </c:when>
</c:choose>
</body>
</html>

