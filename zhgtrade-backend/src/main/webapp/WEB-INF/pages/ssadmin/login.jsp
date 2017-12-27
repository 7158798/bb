<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="comm/include.inc.jsp"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path;
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>众股管理平台</title>
<link href="/static/ssadmin/js/themes/css/login.css" rel="stylesheet" type="text/css" />
<script type="text/javascript">
<c:if test="${error != null}">
alert("${error}") ;
</c:if>
</script>
</head>
<body>
	<div id="login">
		<div id="login_header">
			<!-- <h1 class="login_logo">
				<a href="/"><img src="/static/ssadmin/js/themes/default/images/login_logo.jpg" /></a>
			</h1> -->
			<div class="login_headerContent">
				<div class="navList">

				</div>
				<!-- <h2 class="login_title"><img src="/static/ssadmin/js/themes/default/images/login_title.png" /></h2> -->
			</div>
		</div>
		<script type="text/javascript">
			function validate(f){
				f.src = "/servlet/ValidateImageServlet?"+Math.random() ;
			}
		</script>
		<div id="login_content">
			<div class="loginForm">
				<form action="/ssadmin/smsLogin.html" method="post">
					<%--<p>--%>
						<%--<label>用户名：</label>--%>
						<%--<input type="text" name="name" size="20" class="login_input" />--%>
					<%--</p>--%>
					<%--<p>--%>
						<%--<label>密码：</label>--%>
						<%--<input type="password" name="password" size="20" class="login_input" />--%>
					<%--</p>--%>
					<%--<p>--%>
						<%--<label>验证码：</label>--%>
						<%--<input class="code" type="text" size="5" name="vcode" />--%>
						<%--<span><img src="/servlet/ValidateImageServlet" alt="" width="75" height="24" onclick="validate(this);"/></span>--%>
					<%--</p>--%>
					<p>
						<label>手机号：</label>
						<input id="mobile" type="text" name="mobile" size="20" class="login_input" />
					</p>
					<p>
						<label>密码：</label>
						<input type="password" name="password" size="20" class="login_input" />
					</p>
					<p>
						<label>验证码：</label>
						<input class="code" type="text" size="7" name="code" />
						<span><a id="sendBtn" class="sms_btn" onclick="sendSmsCode();" href="javascript:void(0);">发送验证码</a></span>
					</p>
					<div class="login_bar">
						<input class="sub" type="submit" value="" />
					</div>
				</form>
			</div>
			<div class="login_banner"><img src="/static/ssadmin/js/themes/default/images/login_banner.jpg" /></div>
			<div class="login_main">
				<ul class="helpList">

				</ul>
				<div class="login_inner">
					<p>实时短信提醒，确保用户账户和资金安全</p>
					<p>比特币钱包多层加密，离线存储，保障资产安全 </p>
					<p>HTTPS高级安全加密协议，客户资料全加密传输，防止通过网络泄漏 ……</p>
				</div>
			</div>
		</div>
		<div id="login_footer">
			Copyright &copy; 2015 - 2016 ZHAOGUKEJI.COM <a
			href="http://www.zhaogukeji.com" title="深圳招股科技有限公司" target="_blank">深圳招股科技有限公司</a>
		</div>
	</div>
<script src="/static/ssadmin/js/js/jquery-1.7.2.min.js"></script>
<script src="/static/ssadmin/js/js/jquery.cookie.js"></script>
<script src="/static/ssadmin/js/verify.code.js"></script>
<script>
	function sendSmsCode(){
		var mobile = document.getElementById('mobile').value;
		if(!mobile){
			alert('请输入手机号！');
			return;
		}

		var id = 'sendBtn';
		var $send_btn = $('#' + id);
		var flag = $send_btn.data(_const.SUBMIT_FLAG);
		if(false == flag){
			return;
		}

		$send_btn.data(_const.SUBMIT_FLAG, false);
		$.post('/ssadmin/sendLoginSmsCode.html', {mobile : mobile}, function(data) {
			if('200' == data){
				_handler.addTipsHandler(_const.SMS_CAPTCHA_KEY, id);
				_handler.updateTipsSeconds(id, _const.CAPTCHA_SECONDS);
			}else if('101' == data){
				alert('请输入正确的手机号！');
				$send_btn.data(_const.SUBMIT_FLAG, true);
			}else if('102' == data){
				alert('手机号不存在！');
				$send_btn.data(_const.SUBMIT_FLAG, true);
			}else{
				alert('发送验证码失败！');
				$send_btn.data(_const.SUBMIT_FLAG, true);
			}
		});
	}
</script>
</body>
</html>
