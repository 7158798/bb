<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%--<!-- 登录窗口-->--%>
<div id="tm_yy" class="dn"></div>
<%--<!-- 登录弹出窗 -->--%>
<div class="float_box f16 dn" id="login_box">
    <div class="f_title pl10">
        <h3 class="db fl tac f20">登录</h3>
        <i class="iconfont c_gray db fr close">&#xe609;</i>
    </div>
    <div class="f_content">
        <form id="topLoginForm" action="/user/login.html" method="post">
            <p id="boxLoginTips" class="c_red f12 tac message"></p>
            <p>
                <span class="db fl">手机/邮箱：</span>
                <input type="text" id="indexLoginName2" placeholder="手机/邮箱"  class="db fl pl5"/>
            </p>
            <p>
                <span class="db fl">密码：</span>
                <input type="password" id="indexLoginPwd2" placeholder="密码" onkeydown="onEnterClick('box_login_btn');" class="db fl pl5"/>
            </p>
            <p class="login_box_safe dn">
                <span class="db fl">图形码：</span>
                <input name="text" type="text" placeholder="图形验证码" id="box_img_code" class="img_code code db fl pl5"/>
                <img class="image_code db fl"  id="box_image_code" title="换一换" />
            </p>
            <p class="login_box_safe dn">
                <span class="db fl"><span id="box_code_title">手机验证码</span>：</span>
                <input type="text" name="phoneCode" id="box_code" placeholder="手机验证码" class="code db fl pl5"/>
                <a class="send_code db fl" id="box_sendCode" href="javascript:void(0)" data-name="发送">发送</a>
            </p>
            <p id="box_voice_tips" class="voice_tips dn">
                收不到短信验证码，<a href="javascript:void(0)" id="box_sendVoiceCode">试试语音验证码</a>
            </p>
            <a id="box_login_btn" href="javascript:void(0);" class="confirm c_white">登录</a>
            <p class="f12 opertaion">
                <a href="/user/find_pwd.html" class="c_orange">忘记密码？</a> <span class="pl40">没有账号？</span> <a
                    href="/user/reg.html" class="c_orange">立即注册？</a>
            </p>
            <input type="hidden" id="forwardUrl" value="${empty forwardUrl ? "/" : forwardUrl}">
        </form>
    </div>
</div>
<%--<!-- 登录窗口结束 -->--%>
