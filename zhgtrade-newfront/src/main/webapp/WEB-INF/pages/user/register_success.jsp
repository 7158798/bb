<!-- 注册成功页面 author:yujie 2016-04-25 -->
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
	<div class="title  c_green  f24">
		<i class="iconfont">&#xe612;</i>
		<span>恭喜您！注册成功！</span>
	</div>
	<div class="infomation f14">
		<c:choose>
			<c:when test="${!empty sessionScope.login_user.femail}"><p>注册账号：${sessionScope.login_user.femail}</p></c:when>
			<c:when test="${!empty sessionScope.login_user.ftelephone}"><p>注册账号：${sessionScope.login_user.ftelephone}</p></c:when>
		</c:choose>
		<p>认证姓名：${sessionScope.login_user.frealName}</p>
		<p>证件类型：
			<c:choose>
				<c:when test="${0 == sessionScope.login_user.fidentityType}">身份证</c:when>
				<c:when test="${1 == sessionScope.login_user.fidentityType}">军官证</c:when>
				<c:when test="${2 == sessionScope.login_user.fidentityType}">护照</c:when>
				<c:when test="${3 == sessionScope.login_user.fidentityType}">台湾居民通行证</c:when>
				<c:when test="${4 == sessionScope.login_user.fidentityType}">港澳居民通行证</c:when>
			</c:choose>
		</p>
		<p>证件号码：${sessionScope.login_user.fidentityNo}</p>
		<a href="/market/trade.html" class="bg_blue db c_white">去交易</a>
	</div>
	<div class="f12 opertation">
		<span>您还可以：</span>
		<c:if test="${empty sessionScope.login_user.ftradePassword}"><a href="/account/security.html" class="c_blue">设置交易密码</a></c:if>
		<c:if test="${empty sessionScope.login_user.femail}"><a href="/account/security.html" class="c_blue ml5">绑定邮箱</a></c:if>
		<c:if test="${empty sessionScope.login_user.ftelephone}"><a href="/account/security.html" class="c_blue ml5">绑定手机</a></c:if>
		<a href="/account/chargermb.html" class="c_blue ml5">充值</a>
	</div>
</div>
<%@ include file="../common/footer.jsp" %>
</body>
</html>

