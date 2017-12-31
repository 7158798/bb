<!-- api申请页面author:xxp 2016-04-23 -->
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
<div class="center_page clear">
	<div class="account_nav">
		<a href="/" class="f12 c_blue">首页</a>
		<i class="yjt">&gt;</i>
		<a href="/account/fund.html" class="f12 c_blue">财务中心</a>
		<i class="yjt">&gt;</i>
		<a href="javascript:void(0)" class="f12 c_gray">API申请</a>
	</div>
	<c:set var="dt_index" value="3"/>
	<c:set var="dd_index" value="18"/>
	<%@ include file="../common/account_left.jsp"%>
	<!-- api申请开始开始 -->
	<div class="account_right fl account_api">
		<div class="finance_wrapper">
			<div class="finance_container">
				<h1 class="ml40">API申请</h1>
				<div class="content" id="add_form">
					<p class="clear">
						<span class="db fl">API名称：</span>
						<input type="text" autocomplete="off" placeholder="API名称" class="api_name db fl" />
						<span class="info f12 db fl ml10" style="text-align: left; width: 160px;">
							<i class="iconfont"></i>
							<span class="c_gray"></span>
						</span>
					</p>
					<p class="clear">
						<span class="db fl">权限：</span>
						<label class="db fl">
						<input type="radio" checked="checked" class="db fl" name="permission" value="0"/>全部权限
						</label>
						<label class="db fl">
						<input type="radio" class="db fl" name="permission" value="1"/>只读权限
						</label>
					</p>
					<p class="clear">
						<span class="db fl">交易密码：</span>
						<input type="password" autocomplete="off" class="password db fl" placeholder="交易密码"/>
						<span class="info f12 db fl ml10" style="text-align: left; width: 160px;">
							<i class="iconfont"></i>
							<span class="c_gray"></span>
						</span>
					</p>
					<a href="javascript:void(0)" class="submit" id="add_btn">确认</a>
				</div>
			</div>
		</div>
		<!-- 申请记录 -->
		<div class="Tentitle ml20">
			API申请记录
		</div>
		<div id="Tenbody" class="Tenbody">
			<table>
				<colgroup>
					<col width="190">
					<col width="90">
					<col width="170">
					<col width="170">
					<col width="100">
					<col width="170">
				</colgroup>
				<tbody>
				<tr>
					<th>名称</th>
					<th>权限</th>
					<th>创建日期</th>
					<th>更新日期</th>
					<th>状态</th>
					<th>操作</th>
				</tr>
				<%@ include file="api-list.jsp"%>
				</tbody>
			</table>
			<%--<page:pagination pageSzie="${pageSize}" pageNow="${pageNow}" total="${total}" href="/account/withdrawCny.html?currentPage=#pageNumber"/>--%>
		</div>
	</div>
</div>
<!-- 弹出窗 -->
<div id="tm_yy" class="dn"></div>
<!-- 编辑框 -->
<div id="edit_box" class="dn float_box ">
	<div class="f_title pl10">
		<h3 class="db fl">编辑API</h3>
		<i class="iconfont c_gray db fr close">&#xe609;</i>
	</div>
	<div class="f_content" id="edit_form">
		<p>
			<span class="db fl">API名称：</span>
			<input placeholder="API名称" type="text" class="pl5 db fl api_name"  style="width: 150px;"/>
			<span class="info f12 db fl ml10" style="text-align: left; width: 160px;">
				<i class="iconfont"></i>
				<span class="c_gray"></span>
			</span>
		</p>
		<p>
			<span class="db fl">权限：</span>
			<label class="db fl">
				<input type="radio" checked class="db fl" name="permission1" value="0">全部权限
			</label>
			<label class="db fl">
				<input type="radio" class="db fl" name="permission1" value="1">只读权限
			</label>
			<label class="db fl">
				<input type="radio" class="db fl" name="permission1" value="2">关闭
			</label>
		</p>
		<p>
			<span class="db fl">交易密码：</span>
			<input  placeholder="交易密码" type="password" class="pl5 db fl password" style="width: 150px;"/>
			<span class="info f12 db fl ml10" style="text-align: left; width: 160px;">
				<i class="iconfont"></i>
				<span class="c_gray"></span>
			</span>
		</p>
		<a href="javascript:void(0)" style="width: 159px;" class="confirm bg_blue c_white" id="edit_btn">确认</a>
	</div>
</div>
<%--查看公钥--%>
<div id="look_box1" class="dn float_box ">
	<div class="f_title pl10">
		<h3 class="db fl">准确输入密码才能继续</h3>
		<i class="iconfont c_gray db fr close">&#xe609;</i>
	</div>
	<div class="f_content" id="look_form">
		<p>
			<span class="db fl">交易密码：</span>
			<input  placeholder="交易密码" type="password" class="pl5 db fl password"/>
			<span class="info f12 db fl ml10" style="text-align: left; width: 160px;">
				<i class="iconfont"></i>
				<span class="c_gray"></span>
			</span>
		</p>
		<a href="javascript:void(0);" class="confirm bg_blue c_white" id="look_btn">确认</a>
	</div>
</div>
<div id="look_box2" class="float_box dn">
	<div class="f_title pl10">
		<h3 class="db fl">API key和秘钥</h3>
		<i class="iconfont c_gray db fr close">&#xe609;</i>
	</div>
	<div class="f_message f14 c_gray" style="padding:20px 40px;">
		<span>API key:</span>
		<span id="f_message1"></span>
	</div>
	<div class="f_message f14 c_gray" style="padding:20px 40px;">
		<span>API Secret:</span>
		<span id="f_message2"></span>
	</div>
</div>
<script src="${resources}/static/js/account/api.js"></script>
<%@ include file="../common/footer.jsp"%>
</body>
</html>
