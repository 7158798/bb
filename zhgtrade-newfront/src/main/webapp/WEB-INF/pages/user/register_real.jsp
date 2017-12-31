<%@ page import="com.ruizton.main.model.Fuser" %>
<!-- 实名认证页面 author:xxp 2016-04-25 -->
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="../common/includes.jsp" %>
<%
	Fuser fuser = (Fuser) session.getAttribute("login_user");
	if (fuser == null) {
		response.sendRedirect("/");
	} else if (fuser.getFpostRealValidate()) {
		response.sendRedirect("/account/fund.html");
	}

%>
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
	<div class="f24 title">实名认证</div>
	<div class="mt20 c_blue f12 tac">*真实姓名一经提交后不能修改，人民币提现只能提到该姓名的银行卡上!</div>
	<div class="sec" >
		<div class="item">
			<form id="regForm" action="/user/auth.html" method="post">
				<p class="mt20">
					<span class="db fl fir">证件类型：</span>
					<select name="authType" class="db fl">
						<option value="0">身份证</option>
						<%--<option value="2">护照</option>--%>
					</select>
				</p>
				<p class="mt20">
					<span class="db fl fir">真实姓名：</span>
					<input id="realName" type="text" name="realName" class="db fl" require="true" data-name="真实姓名" data-max="15" data-min="2"/>
					<span class="info f12 db fl ml10">
						<i class="iconfont"></i>
						<span class="c_gray" data-val="请如实填写!"></span>
					</span>
				</p>
				<p class="mt20">
					<span class="db fl fir">证件号码：</span>
					<input id="cardId" type="text" name="cardId" class="db fl" require="true" data-name="证件号码" data-max="30" data-min="10"/>
					<span class="info f12 db fl ml10">
						<i class="iconfont"></i>
						<span class="c_gray" data-val="请如实填写!"></span>
					</span>
				</p>
				<!-- <p class="mt20">
					<span class="db fl fir">验证码：</span>
					<input type="text" class="db fl"/>
					<a class="db fl time ml10" href="javascript:void(0)"><span>140</span>s</a>
				</p> -->
				<!-- <p class="mt20">
					<span class="db fl fir">验证码：</span>
					这里要放图片滑动验证码？？
				</p> -->
				<a class="confirm bg_blue db mt20" href="javascript:doRegister(document.getElementById('regForm'));">确定</a>
			</form>
		</div>
	</div>
</div>
<script type="text/javascript">
	function doRegister(){
		formObj.submitForm(document.getElementById('regForm'), {ajaxCallback : function(data){
			if(data && 200 === data.code){
				window.location.replace('/user/reg_ok.html');
			}else{
				alert(data.msg);
			}
		},valFun : function(_input){
			if('cardId' != $(_input).attr("name"))return true;
			if(!/^(\d{15}$|^\d{18}$|^\d{17}(\d|X|x))$/.test($(_input).val())){
				formObj.setRedIconTips(_input);
				formObj.setFormTips(_input, "证件号码格式不正确");
				return false;
			}
			return true;
		}});
	}
</script>
<%@ include file="../common/footer.jsp" %>
</body>
</html>

