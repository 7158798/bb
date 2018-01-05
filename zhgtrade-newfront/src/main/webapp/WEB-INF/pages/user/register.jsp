<!-- 注册页面 author:xxp 2016-04-25 -->
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
		<li class="fl cur" data-type="0">手机注册</li>
		<li class="fr" data-type="1">邮箱注册</li>
	</ul>
	<ul class="sec" id="ul_sec">
		<li>
			<form id="cellphoneSubmit" action="/user/register.html" method="post">
				<div class="item">
					<input type="hidden" name="type" value="1">
					<input type="hidden" class="token" name="token" value="${sessionScope.form_token}">
					<p class="mt20">
						<span class="db fl fir"><span class="c_red">*  </span>手机：</span>
						<select name="areaCode" class="db fl">
							<option value="0086">中国(+86)</option>
							<%--<option>海外</option>--%>
						</select>
						<input id="cell_username" name="cellphone" type="tel" data-type="mobileNumber" class="fir db fl" require="true" data-name="手机号码" data-max="15" data-min="11"/>
						<span class="info f12 db fl ml10">
							<i class="iconfont"></i>
							<span class="c_gray"></span>
						</span>
					</p>
					<p class="mt20">
						<span class="db fl fir"><span class="c_red">*  </span>设置密码：</span>
						<input id="cell_password" name="password" type="password" class="db fl" require="true" data-name="密码" data-max="20" data-min="6"/>
						<span class="info f12 db fl ml10">
							<i class="iconfont"></i>
							<span class="c_gray"></span>
						</span>
					</p>
					<p class="mt20">
						<span class="db fl fir"><span class="c_red">*  </span>确认密码：</span>
						<input id="cell_password_again" name="repassword" type="password" class="db fl" require="true" data-name="重复密码" />
						<span class="info f12 db fl ml10">
							<i class="iconfont"></i>
							<span class="c_gray"></span>
						</span>
					</p>
					<p class="mt20">
						<span class="db fl fir"><span class="c_red">*  </span>图形验证码：</span>
						<input id="mobile_image_code" name="imageCode" style="width: 100px;" type="text" class="db fl" size="6" data-name="图形验证码" />
						<span class="fl image_code_span"><img class="image_code" id="image_code" title="换一换" src=""></span>
						<%--<span class="db fl ml10" style="color: #999; font-size: 14px;">发送短信验证码需要填写图形验证码</span>--%>
						<span class="info f12 db fl ml10">
							<i class="iconfont"></i>
							<span class="c_gray"></span>
						</span>
					</p>
					<p class="mt20">
						<span class="db fl fir">邀请码：</span>
						<input data-isyes="1" id="cell_invite_code" name="inviteCode" type="text" class="db fl" value="${intro}"/>
						<span class="info f12 db fl ml10">
							<i class="iconfont"></i>
							<span class="c_gray"></span>
						</span>
					</p>
					<%--<input data-isyes="1" id="cell_invite_code" type="hidden" name="inviteCode" class="db fl" value="${intro}" />--%>
					<p class="mt20">
						<span class="db fl fir"><span class="c_red">*  </span>短信验证码：</span>
						<input id="cell_vetify_code" name="vetifyCode" type="text" class="db fl" require="true" data-name="验证码" data-min="4" data-max="6" onkeydown="onEnterClick('mobile_submit_btn');" />
						<span class="db fl ml10" style="color: #999; font-size: 14px;">短信验证码接收不到？试试语音验证码</span>
						<div class="authorcode_wrapper authorcode_wrapper_page mt20">
							<a id="regSmsBtn" href="javascript:;" class="messagecode_wrapper db fl c_blue mr10 pl5 pr5" style="margin-left: 120px">
								<i class="iconfont db fl ml5 mr5" style="width: 20px">&#xe645;</i>
								<span class="db fl" style="width: 98px" data-name="发送短信验证码">发送短信验证码</span>
								<span class="dn">发送验证码(19)</span>
							</a>
							<a id="regVoiceBtn" href="javascript:;" class="voicecode_wrapper db fl c_orange pl5 pr5">
								<i class="iconfont db fl ml5 mr5" style="width: 20px">&#xe644;</i>
								<span class="db fl" style="width: 98px" data-name="发送语音验证码">发送语音验证码</span>
								<span class="dn">发送验证码(19)</span>
							</a>
						</div>
						<%--<a id="regSmsBtn" class="db fl time ml10" href="javascript:void(0)"><span>发送验证码</span></a>--%>
						<span class="info f12 db fl ml10">
							<i class="iconfont"></i>
							<span class="c_gray"></span>
						</span>
					</p>
					<%--<p class="mt20 message_code">
						<span class="db fl code">短信验证码</span>
						<span class="db fl code ml10">语音验证码</span>
					</p>--%>
					<p class="f12 c_gray mt5 protocol">注册即视为同意  <a href="/about/index.html?id=9" class="c_blue">比特家注册协议</a></p>
					<a id="mobile_submit_btn" class="confirm bg_blue db" onclick="toRegister(document.getElementById('cellphoneSubmit'));" href="javascript:void(0);">下一步</a>
				</div>
			</form>
		</li>
		<li class="dn">
			<form id="emailForm" action="/user/register.html" method="post">
				<input type="hidden" name="type" value="2">
				<input type="hidden" class="token" name="token" value="${sessionScope.form_token}">
				<div class="item">
					<p class="mt20">
						<span class="db fl fir"><span class="c_red">*  </span>邮箱：</span>
						<input id="mail_username" name="email" type="email" class=" db fl" require="true" data-name="邮箱号" />
						<span class="info f12 db fl ml10">
							<i class="iconfont c_blue"></i>
							<span class="c_gray"></span>
						</span>
					</p>
					<p class="mt20">
						<span class="db fl fir"><span class="c_red">*  </span>设置密码：</span>
						<input id="mail_password" type="password" name="password" class="db fl" require="true" data-name="密码" data-max="20" data-min="6"/>
						<span class="info f12 db fl ml10">
							<i class="iconfont c_blue"></i>
							<span class="c_gray"></span>
						</span>
					</p>
					<p class="mt20">
						<span class="db fl fir"><span class="c_red">*  </span>确认密码：</span>
						<input id="mail_password_again" type="password" name="repassword" class="db fl" require="true" data-name="确认密码"/>
						<span class="info f12 db fl ml10">
							<i class="iconfont c_blue"></i>
							<span class="c_gray"></span>
						</span>
					</p>
					<p class="mt20">
						<span class="db fl fir"><span class="c_red">*  </span>图形验证码：</span>
						<input id="email_image_code" name="imageCode" style="width: 100px;" type="text" class="db fl" size="6" data-name="图形验证码" />
						<span class="fl image_code_span"></span>
						<%--<span class="db fl ml10" style="color: #999; font-size: 14px;">发送邮箱验证码需要填写图形验证码</span>--%>
						<span class="info f12 db fl ml10">
							<i class="iconfont"></i>
							<span class="c_gray"></span>
						</span>
					</p>
					<%--<p class="mt20">
						<span class="db fl fir">邀请码：</span>
						<input data-isyes="1" id="mail_invite_code" type="text" name="inviteCode" class="db fl" value="${intro}" />
						<span class="info f12 db fl ml10">
							<i class="iconfont c_blue"></i>
							<span class="c_gray"></span>
						</span>
					</p>--%>
					<input data-isyes="1" id="mail_invite_code" type="hidden" name="inviteCode" class="db fl" value="${intro}" />
					<p class="mt20">
						<span class="db fl fir"><span class="c_red">*  </span>邮箱验证码：</span>
						<input id="mail_vetify_code" type="text" name="vetifyCode" class="db fl" require="true" data-name="验证码" data-min="4" data-max="6" onkeydown="onEnterClick('email_submit_btn');"/>
						<a id="regEmailBtn" class="db fl time ml10" href="javascript:void(0)"><span>发送验证码</span></a>
						<span class="info f12 db fl ml10">
							<i class="iconfont c_blue"></i>
							<span class="c_gray"></span>
						</span>
					</p>
					<p class="f12 c_gray mt5 protocol">注册即视为同意  <a href="/about/index.html?id=9" class="c_blue">比特家注册协议</a></p>
					<a id="email_submit_btn" class="confirm bg_blue db" onclick="toRegister(document.getElementById('emailForm'));" href="javascript:void(0);">下一步</a>
				</div>
			</form>
		</li>
	</ul>
</div>
<script  src="${resources}/static/js/user/reg.js"></script>
<script>
	$(function(){
		$('span.image_code_span').on('click', 'img.image_code', function(){
			this.src = '/servlet/ValidateImageServlet?' + new Date().getTime();
		});
        $('#image_code').get(0).src = '/servlet/ValidateImageServlet?' + new Date().getTime();
	});
</script>
<%@ include file="../common/footer.jsp" %>
</body>
</html>

