<!-- 所有后台传输文章页面 author:xxp 2016-04-25 -->
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="../common/header.jsp"%>
<link rel="stylesheet" href="${resources}/static/css/guide.css" />
<div class="center_page">
	<div class="guide_nav">
		<a href="javascript:void(0)" class="f12 c_blue">首页</a>
		<i class="yjt">&gt;</i>
		<a href="javascript:void(0)" class="f12 c_blue">新手指南</a>
		<i class="yjt">&gt;</i>
		<a href="javascript:void(0)" class="f12 c_gray">风险提示</a>
	</div>
	<%@ include file="../test/guide_left.jsp"%>
	<div class="guide_right fl">
		<div class="title f14 fb">新手帮助</div>
		<div class="content">
			<a href="javascript:void(0)">
				<div class="list">
					<i class="iconfont c_gray db fl">&#xe616;</i>
					<span class="db fl pl10">风险提示</span>
					<span class="db fr">2014-11-11 11:11:11</span>
				</div>
			</a>
			<a href="javascript:void(0)">
				<div class="list">
					<i class="iconfont c_gray db fl">&#xe616;</i>
					<span class="db fl pl10">交易原则</span>
					<span class="db fr">2014-11-11 11:11:11</span>
				</div>
			</a>
			<a href="javascript:void(0)">
				<div class="list">
					<i class="iconfont c_gray db fl">&#xe616;</i>
					<span class="db fl pl10">充币提币</span>
					<span class="db fr">2014-11-11 11:11:11</span>
				</div>
			</a>
			<a href="javascript:void(0)">
				<div class="list">
					<i class="iconfont c_gray db fl">&#xe616;</i>
					<span class="db fl pl10">平台工作时间</span>
					<span class="db fr">2014-11-11 11:11:11</span>
				</div>
			</a>
			<a href="javascript:void(0)">
				<div class="list">
					<i class="iconfont c_gray db fl">&#xe616;</i>
					<span class="db fl pl10">个人财务</span>
					<span class="db fr">2014-11-11 11:11:11</span>
				</div>
			</a>
			<div class="page">
				<ul>
					<!-- 这里是分页 --><%-- ${pagin } --%>
					<li><a style="color:#FFFFFF;" class="current_ss" href="javascript:void(0)">1</a></li>
					<li><a class="" href="javascript:void(0)">2</a></li>
				</ul>
			</div>
		</div>
	</div>
	<div class="cb"></div>
</div>
<%@ include file="../common/footer.jsp"%>

