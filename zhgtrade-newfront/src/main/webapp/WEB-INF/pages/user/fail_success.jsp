<!-- 注册成功页面 author:xxp 2016-04-25 -->
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
	<link rel="stylesheet" href="${resources}/static/css/register.css" />
</head>
<body>
<%@include file="../common/header.jsp" %>
<div class="center_page register_wrapper f16">
	<div class="title  c_green  f24">
		<i class="iconfont">&#xe612;</i>
		<span>恭喜您！重设成功！</span>
	</div>
	<!-- <div class="title  c_red  f24">
		<i class="iconfont">&#xe60b;</i>
		<span>真失望！链接失效！</span>
	</div> -->
	<div class="f14 opertation">
		<span>请重新</span><a id="reLogin" href="javascript:void(0)" class="c_blue">登录</a>！
		<%--<span>您还可以：</span>
		<a href="javascript:void(0)" class="c_blue">设置交易密码</a>
		<a href="javascript:void(0)" class="c_blue">绑定手机</a>
		<a href="javascript:void(0)" class="c_blue">充值</a>--%>
	</div>
</div>
<%@ include file="../common/footer.jsp" %>
</body>
</html>

