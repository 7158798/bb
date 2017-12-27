<!-- 找回密码页面 author:yujie 2016-04-25 -->
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
    <ul class="f24 fir" id="ul_fir">
        <li class="fl cur">通过手机</li>
        <li class="fr">通过邮箱</li>
    </ul>
    <ul class="sec" id="ul_sec">
        <li>
            <div class="item">
                <form id="mobileForm" action="/user/findPassword.html" method="post">
                    <input type="hidden" name="token" value="${sessionScope.form_token}">
                    <p class="mt20">
                        <span class="db fl fir">手机号码：</span>
                        <select name="areaCode" class="db fl">
                            <option value="0086">中国(+86)</option>
                            <%--<option>海外</option>--%>
                        </select>
                        <input id="cell_username" type="tel" name="mobile" require="true", data-name="手机号" data-min="11" data-max="15" class="fir db fl"/>
					<span class="info f12 db fl ml10">
						<i class="iconfont"></i>
						<span class="c_gray" data-val=""></span>
					</span>
                    </p>
                    <p class="mt20">
                        <span class="db fl fir">验证码：</span>
                        <input type="text" name="captcha" require="true" data-name="验证码" data-min="4" data-max="6" class="db fl" onkeydown="onEnterClick('find_mpwd_btn');"/>
                        <%--<a id="findPwdSmsBtn" class="db fl time ml10" href="javascript:void(0)"><span>发送验证码</span></a>--%>
                        <span class="info f12 db fl ml10">
                            <i class="iconfont"></i>
                            <span class="c_gray" data-val=""></span>
                        </span>

                        <div class="authorcode_wrapper authorcode_wrapper_page mt20">
                            <a id="findPwdSmsBtn" href="javascript:;" class="messagecode_wrapper db fl c_blue mr10 pl5 pr5" style="margin-left: 100px">
                                <i class="iconfont db fl ml5 mr5" style="width: 20px">&#xe645;</i>
                                <span class="db fl" style="width: 98px" data-name="发送短信验证码">发送短信验证码</span>
                                <span class="dn">发送验证码(19)</span>
                            </a>
                            <a id="findPwdVoiceBtn" href="javascript:;" class="voicecode_wrapper db fl c_orange pl5 pr5">
                                <i class="iconfont db fl ml5 mr5" style="width: 20px">&#xe644;</i>
                                <span class="db fl" style="width: 98px" data-name="发送语音验证码">发送语音验证码</span>
                                <span class="dn">发送验证码(19)</span>
                            </a>
                        </div>
                    </p>
                    <%--<p class="mt20 message_code">
                        <span class="db fl code">短信验证码</span>
                        <span class="db fl code ml10">语音验证码</span>
                    </p>--%>
                    <a id="find_mpwd_btn" class="confirm bg_blue db mt20" onclick="toFindPwd(document.getElementById('mobileForm'));" href="javascript:void(0);">找回密码</a>
                </form>
            </div>
        </li>
        <li class="dn">
            <div class="item">
                <form id="emailForm" action="/user/findPassword.html" method="post">
                    <input type="hidden" name="token" value="${sessionScope.form_token}">
                    <p class="mt20">
                        <span class="db fl fir">邮箱：</span>
                        <input id="mail_username" type="email" name="email" class="db fl" require="true" data-name="邮箱号"/>
                        <span class="info f12 db fl ml10">
                            <i class="iconfont"></i>
                            <span class="c_gray" data-val=""></span>
                        </span>
                    </p>
                    <p class="mt20">
                        <span class="db fl fir">验证码：</span>
                        <input type="text" name="captcha" class="db fl" require="true" data-name="验证码" data-min="4" data-max="6" onkeydown="onEnterClick('find_epwd_btn');"/>
                        <a id="findPwdEmailBtn" class="db fl time ml10" href="javascript:void(0)"><span>发送验证码</span></a>
                        <span class="info f12 db fl ml10">
                            <i class="iconfont"></i>
                            <span class="c_gray" data-val=""></span>
                        </span>
                    </p>
                    <a id="find_epwd_btn" class="confirm bg_blue db mt20" onclick="toFindPwd(document.getElementById('emailForm'));" href="javascript:void(0);">找回密码</a>
                </form>
            </div>
        </li>
    </ul>
</div>
<!-- 找回密码窗口-->
<div id="tm_yy" class="dn"></div>
<div class="float_box f14 dn" style="top:200px;left:500px;">
    <div class="f_title pl10">
        <h3 class="db fl tac f16">提示</h3>
        <i class="iconfont c_gray db fr">&#xe609;</i>
    </div>
    <div class="f_content">
        <p class="tac">
            重置密码邮件已发送到您的邮箱请注意查收
        </p>
        <a href="javascript:void(0)" class="confirm bg_blue c_white">确认</a>
    </div>
</div>
<script  src="${resources}/static/js/user/pwd.js"></script>
<%@ include file="../common/footer.jsp" %>
</body>
</html>

