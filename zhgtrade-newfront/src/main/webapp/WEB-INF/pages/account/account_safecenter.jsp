<!-- 安全中心页面author:yujie 2016-04-24 -->
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
    <link rel="stylesheet" href="${resources}/static/css/account.css" />
</head>
<body>
<c:set var="menu_index" value="4"/>
<%@include file="../common/header.jsp" %>
<div class="center_page">
    <div class="account_nav">
        <a href="/" class="f12 c_blue">首页</a>
        <i class="yjt">&gt;</i>
        <a href="/account/security.html" class="f12 c_blue">基本设置</a>
        <i class="yjt">&gt;</i>
        <a href="javascript:void(0)" class="f12 c_gray">安全中心</a>
    </div>
    <c:set var="dt_index" value="2"/>
    <c:set var="dd_index" value="8"/>
    <%@ include file="../common/account_left.jsp" %>
    <div class="account_right fl">
        <div class="finance_wrapper">
            <div class="safe_center fill_right content_wrapper">
                <!-- <h1 class="ml40">安全中心</h1> -->
                <h1 class="ml40">安全中心</h1>
                <%--<div class="title">
                    <ul id="title" class="clear">
                        <li class="cur fl f16 fb">安全中心</li>
                        <li class="fl f16 fb">个人信息</li>
                    </ul>
                </div>--%>
                <ul id="content">
                    <li>
                        <div class="content clear">
                            <!-- 短信验证 -->
                            <div class="item  fl">
                                <div class="safe_info" style="color:#999;position:absolute;top:40px;left:160px;font-size:12px;">
                                    PS:已绑定手机号无法使用，请联系<a class="c_blue" target="_blank" href="http://wpa.qq.com/msgrd?v=3&uin=${requestScope.constant['serviceQQ']}&site=qq&menu=yes">QQ客服</a>
                                </div>
                                <div class="border_blue_2 clear container">
                                <c:choose>
                                    <c:when test="${fuser.fisTelephoneBind}">
                                        <i class="iconfont c_blue db fl">&#xe612;</i>
                                    </c:when>
                                    <c:otherwise>
                                        <i class="iconfont c_red db fl">&#xe601;</i>
                                    </c:otherwise>
                                </c:choose>
                                <div class="text db fl">
                                    <p class="t_title">短信验证</p>
                                    <i class="icon db" style="background-position: -3px -15px"></i>
                                    <p class="f12 c_gray t_content tal">提现，修改密码，及安全设置时用以收取验证短信。</p>
                                </div>
                                </div>
                                <div class="fl db ml40 mt20 safe_status">
                                    <c:choose>
                                        <c:when test="${fuser.fisTelephoneBind}">
                                            <p class="s_status fl">已验证</p>
                                            <a href="javascript:void(0)" class="c_blue mt15 db fl" id="btn1">解绑</a>
                                        </c:when>
                                        <c:otherwise>
                                            <p class="s_status fl">未验证</p>
                                            <a href="javascript:void(0)" class="c_blue mt15 db fl" id="btn2">验证</a>
                                        </c:otherwise>
                                    </c:choose>
                                </div>
                            </div>
                            <!-- 邮箱验证 -->
                            <div class="item  fl">
                                <div class="border_blue_2 clear container">
                                <c:choose>
                                    <c:when test="${fuser.fisMailValidate}">
                                        <i class="iconfont c_blue db fl">&#xe612;</i>
                                    </c:when>
                                    <c:otherwise>
                                        <i class="iconfont c_red db fl">&#xe601;</i>
                                    </c:otherwise>
                                </c:choose>
                                <div class="text db fl">
                                    <p class="t_title">邮箱验证</p>
                                    <i class="icon db" style="background-position: -117px -15px"></i>
                                    <p class="f12 c_gray t_content">用于登陆和找回密码。</p>
                                </div>
                                </div>
                                <div class="fl db ml40 mt20 safe_status">
                                    <c:choose>
                                        <c:when test="${fuser.fisMailValidate}">
                                            <p class="s_status  fl">已验证</p>
                                            <a href="javascript:void(0)" class="c_blue mt15 db fl" id="btn11">解绑</a>
                                        </c:when>
                                        <c:otherwise>
                                            <p class="s_status fl">未验证</p>
                                            <a href="javascript:void(0)" class="c_blue mt15 db fl" id="btn3">验证</a>
                                        </c:otherwise>
                                    </c:choose>
                                </div>
                            </div>
                            <!-- 登录密码验证 -->
                            <div class="item  fl">
                                <div class="border_blue_2 clear container">
                                <c:choose>
                                    <c:when test="${!empty fuser.floginPassword}">
                                        <i class="iconfont c_blue db fl">&#xe612;</i>
                                    </c:when>
                                    <c:otherwise>
                                        <i class="iconfont c_red db fl">&#xe601;</i>
                                    </c:otherwise>
                                </c:choose>
                                <div class="text db fl">
                                    <p class="t_title">登录密码</p>
                                    <i class="icon db" style="background-position: -255px -15px"></i>
                                    <p class="f12 c_gray t_content tal">开通短信验证才能进行设置。</p>
                                </div>
                                </div>
                                <div class="fl db ml40 mt20 safe_status">
                                    <c:choose>
                                        <c:when test="${!empty fuser.floginPassword}">
                                            <p class="s_status fl">已设置</p>
                                            <a href="javascript:void(0)" class="c_blue mt15 db fl" id="btn5">修改</a>
                                        </c:when>
                                        <c:otherwise>
                                            <p class="s_status fl">未设置</p>
                                            <a href="javascript:void(0)" class="c_blue mt15 db fl" id="btn5">设置</a>
                                        </c:otherwise>
                                    </c:choose>
                                </div>
                            </div>
                            <!-- 交易密码验证 -->
                            <div class="item  fl">
                                <div class="border_blue_2 clear container">
                                <c:choose>
                                    <c:when test="${!empty fuser.ftradePassword}">
                                        <i class="iconfont c_blue db fl">&#xe612;</i>
                                    </c:when>
                                    <c:otherwise>
                                        <i class="iconfont c_red db fl">&#xe601;</i>
                                    </c:otherwise>
                                </c:choose>
                                <div class="text db fl">
                                    <p class="t_title">交易密码</p>
                                    <i class="icon db" style="background-position: -365px -15px"></i>
                                    <p class="f12 c_gray t_content tal">开通短信验证才能进行设置。</p>
                                </div>
                                </div>
                                <div class="fl db ml40 mt20 safe_status">
                                    <c:choose>
                                        <c:when test="${!empty fuser.ftradePassword}">
                                            <p class="s_status fl">已设置</p>
                                            <a href="javascript:void(0)" class="c_blue mt15 db fl" id="btn6">修改</a>
                                        </c:when>
                                        <c:otherwise>
                                            <p class="s_status fl">未设置</p>
                                            <a href="javascript:void(0)" class="c_blue mt15 db fl" id="btn6">设置</a>
                                        </c:otherwise>
                                    </c:choose>
                                </div>
                            </div>
                            <!-- 警告 -->
                            <%--<div id="warn" class="warning f12 tal c_red">
                                <i class="iconfont c_red">&#xe601;</i>
                                本站客服不会要求用QQ远程控制您的电脑,所有要求远程的都是骗子。<span>短信验证码非常重要，请勿透露给任何人，包括本站的客服。</span>
                            </div>--%>
                        </div>
                    </li>
                    <li class="dn">
                        <div class="content ml40 mt20">
                            <p>
                                <span class="fir db fl">UID：</span>
                                <span class="db fl">10010</span>
                            </p>
                            <p>
                                <span class="fir db fl">姓名：</span>
                                <span class="db fl">俞杰</span>
                            </p>
                            <p>
                                <span class="fir db fl">邮箱：</span>
                                <span class="db fl">945351749@qq.com</span>
                            </p>
                            <p>
                                <span class="fir db fl">用户等级：</span>
                                <i class="iconfont db fl c_blue">&#xe615;</i>
                                <span class="db fl pl5">vip1</span>
                                <span class="db fl pl40">积分:</span>
                                <span class="db fl pl5">0.0</span>
                            </p>
                            <p>
                                <span class="fir db fl">联系地址：</span>
                                <input type="text" class="db fl"/>
                                <a href="javascript:void(0)" class="db fl ml10 bg_blue c_white">确认修改</a>
                            </p>
                        </div>
                        <div class="content tal info_content">
                            <div class="c_title c_blue f18 ">
                                <i class="iconfont">&#xe612;</i>
                                <span>已经通过实名认证</span>
                            </div>
                            <p class="mt20">
                                <span>真实姓名：</span>
                                <span>俞杰 </span>
                            </p>
                            <p>
                                <span>证件类型：</span>
                                <span>身份证 </span>
                            </p>
                            <p>
                                <span>证件号码：</span>
                                <span>35018************ </span>
                            </p>
                        </div>
                    </li>
                </ul>
            </div>
        </div>
    </div>
    <!-- 弹出窗 -->
    <div id="tm_yy" class="dn"></div>
    <!-- 短信验证弹出窗 -->
    <div id="btn1_box" class="float_box dn">
        <div class="f_title pl10">
            <h3 class="db fl">手机解绑</h3>
            <i class="iconfont c_gray db fr close">&#xe609;</i>
        </div>
        <div class="f_message f12 c_red">为了您的账户安全，不建议您解绑手机</div>
        <div class="f_content">
            <form id="unBindMobileForm" action="/account/unbindMobile.html" method="post">
                <p>
                    <span class="db fl">验证码：</span>
                    <input type="text" name="code" require="true" data-name="验证码" data-min="4" data-max="6" class="pl5 db fl"/>
                    <span class="info f12 db fl ml10" style="text-align: left; width: 160px;">
                        <i class="iconfont"></i>
                        <span class="c_gray"></span>
                    </span>
                    <%--<a id="unBindMobileBtn" onclick="sendSmsCaptcha(this);" href="javascript:void(0);" class="db fl send bg_gray ml10 border_gray"><span>发送验证码</span></a>--%>
                    <%--<a href="javascript:void(0)" class="db fl send bg_gray ml10 border_gray">语音验证码</a>--%>
                    <div class="authorcode_wrapper authorcode_wrapper_page mt20">
                        <a id="unbindSmsBtn" onclick="sendSmsCaptcha(this);" href="javascript:;" class="messagecode_wrapper db fl c_blue mr10 pl5 pr5" style="margin-left: 165px">
                            <i class="iconfont db fl ml5 mr5 " style="width: 20px">&#xe645;</i>
                            <span class="db fl" style="width: 98px;text-align: center;" data-name="发送短信验证码">发送短信验证码</span>
                            <span class="dn">发送验证码(19)</span>
                        </a>
                        <a id="unbindVoiceBtn" onclick="sendVoiceCaptcha(this);" href="javascript:;" class="voicecode_wrapper db fl c_orange pl5 pr5">
                            <i class="iconfont db fl ml5 mr5" style="width: 20px">&#xe644;</i>
                            <span class="db fl" style="width: 98px;text-align: center;" data-name="发送语音验证码">发送语音验证码</span>
                            <span class="dn">发送验证码(19)</span>
                        </a>
                    </div>
                </p>
                <a href="javascript:unBindMobile(document.getElementById('unBindMobileForm'));" class="confirm bg_blue c_white">确认</a>
            </form>
        </div>
    </div>
    <div id="btn2_box" class="float_box dn">
        <div class="f_title pl10">
            <h3 class="db fl">手机绑定<!--手机解绑  --></h3>
            <i class="iconfont c_gray db fr close">&#xe609;</i>
        </div>
        <!-- <div class="f_message f12 c_gray">为了您的账户安全，不建议您解绑手机</div> -->
        <div class="f_content">
            <form id="bindMobileForm" action="/account/bindMobile_.html" method="post">
                <p>
                    <span class="db fl">手机号：</span>
                    <input id="bind_mobule_input" name="mobile" type="tel" class="pl5 db fl" require="true" data-name="手机号" data-min="11" data-max="15"/>
                    <span class="info f12 db fl ml10" style="text-align: left; width: 160px;">
                        <i class="iconfont"></i>
                        <span class="c_gray"></span>
                    </span>
                </p>
                <p>
                    <span class="db fl">验证码：</span>
                    <input name="code" type="text" class="pl5 db fl" require="true" data-name="验证码" data-min="4" data-max="6"/>
                    <%--<a id="sendBindSmsCode" href="javascript:void(0);"--%>
                       <%--class="db fl send bg_gray ml10 border_gray"><span>发送验证码</span></a>--%>
                    <span class="info f12 db fl ml10" style="text-align: left; width: 160px;">
                        <i class="iconfont"></i>
                        <span class="c_gray"></span>
                    </span>
                    <div class="authorcode_wrapper authorcode_wrapper_page mt20">
                        <a id="bindSmsBtn" onclick="sendSmsCaptcha(this, 'bind_mobule_input', true);" href="javascript:;" class="messagecode_wrapper db fl c_blue mr10 pl5 pr5" style="margin-left: 165px">
                            <i class="iconfont db fl ml5 mr5" style="width: 20px">&#xe645;</i>
                            <span class="db fl" style="width: 98px;text-align: center;" data-name="发送短信验证码">发送短信验证码</span>
                            <span class="dn">发送验证码(19)</span>
                        </a>
                        <a id="bindVoiceBtn" onclick="sendVoiceCaptcha(this, 'bind_mobule_input', true);" href="javascript:;" class="voicecode_wrapper db fl c_orange pl5 pr5">
                            <i class="iconfont db fl ml5 mr5" style="width: 20px">&#xe644;</i>
                            <span class="db fl" style="width: 98px;text-align: center;" data-name="发送语音验证码">发送语音验证码</span>
                            <span class="dn">发送验证码(19)</span>
                        </a>
                    </div>
                    <%--<a href="javascript:void(0)" class="db fl send bg_gray ml10 border_gray">语音验证码</a>--%>
                </p>
                <a href="javascript:bindMobile(document.getElementById('bindMobileForm'));" class="confirm bg_blue c_white">确认</a>
            </form>
        </div>
    </div>

    <!-- 修改邮箱 -->
    <div id="btn3_box" class="float_box dn">
        <div class="f_title pl10">
            <h3 class="db fl">邮箱绑定</h3>
            <i class="iconfont c_gray db fr close">&#xe609;</i>
        </div>
        <div class="f_content">
            <form id="bindEmailForm" action="/account/bindEmail_.html" method="post">
                <p>
                    <span class="db fl">新邮箱：</span>
                    <input id="bind_email_input" name="email" type="email" class="pl5 db fl" require="true" data-name="邮箱" data-max="30" style="width: 160px;"/>
                    <span class="info f12 db fl ml10" style="text-align: left; width: 160px;">
                        <i class="iconfont"></i>
                        <span class="c_gray"></span>
                    </span>
                </p>
                <p>
                    <span class="db fl">验证码：</span>
                    <input name="code" type="text" class="pl5 db fl" require="true" data-name="验证码" data-min="4" data-max="6" style="width: 68px;"/>
                    <a id="bindEmailBtn" onclick="sendEmailCaptcha(this, 'bind_email_input', true);" href="javascript:void(0);"
                       class="db fl send bg_gray ml10 border_gray"><span>发送验证码</span></a>
                    <span class="info f12 db fl ml10" style="text-align: left; width: 160px;">
                        <i class="iconfont"></i>
                        <span class="c_gray"></span>
                    </span>
                    <%--<a href="javascript:void(0)" class="db fl send bg_gray ml10 border_gray">语音验证码</a>--%>
                </p>
                <a href="javascript:bindEmail(document.getElementById('bindEmailForm'));" class="confirm bg_blue c_white">确认</a>
            </form>
        </div>
    </div>
    <!-- 解绑邮箱 -->
    <div id="btn11_box" class="float_box dn">
        <div class="f_title pl10">
            <h3 class="db fl">邮箱解绑</h3>
            <i class="iconfont c_gray db fr close">&#xe609;</i>
        </div>
        <div class="f_message f12 c_red">为了您的账户安全，不建议您解绑邮箱</div>
        <div class="f_content">
            <form id="unBindEmailForm" action="/account/unbindEmail.html" method="post">
                <p>
                    <span class="db fl">验证码：</span>
                    <input type="text" name="code" require="true" data-name="验证码" data-min="4" data-max="6" class="pl5 db fl"/>
                    <a id="unBindEmailBtn" onclick="sendEmailCaptcha(this);" href="javascript:void(0);" class="db fl send bg_gray ml10 border_gray"><span>发送验证码</span></a>
                    <%--<a href="javascript:void(0)" class="db fl send bg_gray ml10 border_gray">语音验证码</a>--%>
                    <span class="info f12 db fl ml10" style="text-align: left; width: 160px;">
                        <i class="iconfont"></i>
                        <span class="c_gray"></span>
                    </span>
                </p>
                <a href="javascript:unBindEmail(document.getElementById('unBindEmailForm'));" class="confirm bg_blue c_white">确认</a>
            </form>
        </div>
    </div>

    <!-- 设置登录密码弹出窗 -->
    <div id="btn5_box" class="float_box dn">
        <div class="f_title pl10">
            <h3 class="db fl">设置登录密码</h3>
            <i class="iconfont c_gray db fr close">&#xe609;</i>
        </div>
        <div class="f_content">
            <form id="modPwdForm" action="/account/modLoginPassword.html" method="post">
                <input type="hidden" name="type" value="0">
                <c:if test="${!empty fuser.floginPassword}">
                    <p>
                        <span class="db fl">原登录密码：</span>
                        <input type="password" class="db fl pl5" require="true" name="oldPassword" data-name="旧密码" data-min="6" data-max="20"/>
                        <span class="info f12 db fl ml10" style="text-align: left; width: 160px;">
                            <i class="iconfont"></i>
                            <span class="c_gray"></span>
                        </span>
                    </p>
                </c:if>
                <p>
                    <span class="db fl">新登录密码：</span>
                    <input type="password" class="db fl pl5" require="true" name="newPassword" data-name="新密码" data-min="6" data-max="20"/>
                    <span class="info f12 db fl ml10" style="text-align: left; width: 160px;">
                        <i class="iconfont"></i>
                        <span class="c_gray"></span>
                    </span>
                </p>
                <p>
                    <span class="db fl">确认密码：</span>
                    <input type="password" class="db fl pl5" require="true" name="rePassword" data-name="确认密码"/>
                    <span class="info f12 db fl ml10" style="text-align: left; width: 160px;">
                        <i class="iconfont"></i>
                        <span class="c_gray"></span>
                    </span>
                </p>
                <p>
                    <span class="db fl">验证码：</span>
                    <input id="mob_password_code" type="text" class="pl5 db fl" require="true" name="code" data-name="验证码" data-min="4" data-max="6"/>
                    <%--<a id="sendModPwdBtn" onclick="sendSmsCaptcha(this);" href="javascript:void(0);"--%>
                       <%--class="db fl send bg_gray ml10 border_gray"><span>发送验证码</span></a>--%>
                    <span class="info f12 db fl ml10" style="text-align: left; width: 160px;">
                        <i class="iconfont"></i>
                        <span class="c_gray"></span>
                    </span>
                    <div class="authorcode_wrapper authorcode_wrapper_page mt20">
                        <a id="modLoginPwdSmsBtn" onclick="sendSmsCaptcha(this);" href="javascript:;" class="messagecode_wrapper db fl c_blue mr10 pl5 pr5" style="margin-left: 165px">
                            <i class="iconfont db fl ml5 mr5" style="width: 20px">&#xe645;</i>
                            <span class="db fl" style="width: 98px;text-align: center;" data-name="发送短信验证码">发送短信验证码</span>
                            <span class="dn">发送验证码(19)</span>
                        </a>
                        <a id="modLoginPwdVoiceBtn" onclick="sendVoiceCaptcha(this);" href="javascript:;" class="voicecode_wrapper db fl c_orange pl5 pr5">
                            <i class="iconfont db fl ml5 mr5" style="width: 20px">&#xe644;</i>
                            <span class="db fl" style="width: 98px;text-align: center;" data-name="发送语音验证码">发送语音验证码</span>
                            <span class="dn">发送验证码(19)</span>
                        </a>
                    </div>
                    <%--<a href="javascript:void(0)" class="dn fl send bg_gray ml10 border_gray">短信验证码</a>
                    <a href="javascript:void(0)" class="dn fl send bg_gray ml10 border_gray">语音验证码</a>--%>
                </p>
                <a href="javascript:modPassword(document.getElementById('modPwdForm'));" class="confirm bg_blue c_white">确认</a>
            </form>
        </div>
    </div>
    <!-- 设置交易密码弹出窗 -->
    <div id="btn6_box" class="float_box dn" >
        <div class="f_title pl10">
            <h3 class="db fl">设置交易密码</h3>
            <i class="iconfont c_gray db fr close">&#xe609;</i>
        </div>
        <div class="f_message f12 c_red">敬告：交易密码不要与登录密码或其他网站密码一致，由此产生的账号被盗，本站概不负责。</div>
        <div class="f_content">
            <form id="modTradePwdForm" action="/account/modTradePassword.html" method="post">
                <input type="hidden" name="type" value="1">
                <p <c:if test="${empty fuser.ftradePassword}">style="display: none;" </c:if>>
                    <%--<span class="db fl">原交易密码：</span>--%>
                    <%--<input type="password" class="db fl pl5" <c:if test="${!empty fuser.ftradePassword}">require="true" </c:if> name="oldPassword" data-name="旧密码" data-min="6" data-max="20"/>--%>
                    <span class="info f12 db fl ml10" style="text-align: left; width: 160px;">
                        <i class="iconfont"></i>
                        <span class="c_gray"></span>
                    </span>
                </p>
                <p>
                    <span class="db fl">新交易密码：</span>
                    <input type="password" class="db fl pl5" require="true" name="newPassword" data-name="新密码" data-min="6" data-max="20"/>
                    <span class="info f12 db fl ml10" style="text-align: left; width: 160px;">
                        <i class="iconfont"></i>
                        <span class="c_gray"></span>
                    </span>
                </p>
                <p>
                    <span class="db fl">确认密码：</span>
                    <input type="password" class="db fl pl5" require="true" name="rePassword" data-name="确认密码"/>
                    <span class="info f12 db fl ml10" style="text-align: left; width: 160px;">
                        <i class="iconfont"></i>
                        <span class="c_gray"></span>
                    </span>
                </p>
                <p>
                    <span class="db fl">验证码：</span>
                    <input id="mob_password_code" type="text" class="pl5 db fl" require="true" name="code" data-name="验证码" data-min="4" data-max="6"/>
                    <%--<a id="sendModTpwdBtn" onclick="sendSmsCaptcha(this);" href="javascript:void(0);"--%>
                       <%--class="db fl send bg_gray ml10 border_gray"><span>发送验证码</span></a>--%>
                    <span class="info f12 db fl ml10" style="text-align: left; width: 160px;">
                        <i class="iconfont"></i>
                        <span class="c_gray"></span>
                    </span>
                    <div class="authorcode_wrapper authorcode_wrapper_page mt20">
                        <a id="modTradePwdSmsBtn" onclick="sendSmsCaptcha(this);" href="javascript:;" class="messagecode_wrapper db fl c_blue mr10 pl5 pr5" style="margin-left: 165px">
                            <i class="iconfont db fl ml5 mr5" style="width: 20px">&#xe645;</i>
                            <span class="db fl" style="width: 98px;text-align: center;" data-name="发送短信验证码">发送短信验证码</span>
                            <span class="dn">发送验证码(19)</span>
                        </a>
                        <a id="modTradePwdVoiceBtn" onclick="sendVoiceCaptcha(this);" href="javascript:;" class="voicecode_wrapper db fl c_orange pl5 pr5">
                            <i class="iconfont db fl ml5 mr5" style="width: 20px">&#xe644;</i>
                            <span class="db fl" style="width: 98px;text-align: center;" data-name="发送语音验证码">发送语音验证码</span>
                            <span class="dn">发送验证码(19)</span>
                        </a>
                    </div>
                    <%--<a href="javascript:void(0)" class="dn fl send bg_gray ml10 border_gray">短信验证码</a>
                    <a href="javascript:void(0)" class="dn fl send bg_gray ml10 border_gray">语音验证码</a>--%>
                </p>
                <a href="javascript:modTradePassword(document.getElementById('modTradePwdForm'));" class="confirm bg_blue c_white">确认</a>
            </form>
        </div>
    </div>
    <div class="cb"></div>
</div>
<script  src="${resources}/static/js/account/safe.js"></script>
<script>
    $(function () {
        show_box("btn1");
        show_box("btn2");
        show_box("btn3");
        show_box("btn4");
        show_box("btn5");
        show_box("btn6");
        show_box("btn11");
        function show_box(id) {
            $("#" + id).click(function () {
                center_box(id + "_box");
                $("#tm_yy").show();
                $("#" + id + "_box").show();
            });
        }
    });
</script>
<%@ include file="../common/footer.jsp" %>
</body>
</html>
