    <!-- 工单列表页面author:xxp 2016-04-24 -->
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
	<link rel="stylesheet" href="${resources}/static/css/common/animate.css"/>
	<link rel="stylesheet" href="${resources}/static/css/account.css" />
</head>
<body>
<c:set var="menu_index" value="4"/>
<%@include file="../common/header.jsp" %>
<div class="center_page">
	<div class="account_nav">
		<a href="javascript:void(0)" class="f12 c_blue">首页</a>
		<i class="yjt">&gt;</i>
		<a href="javascript:void(0)" class="f12 c_blue">财务中心</a>
		<i class="yjt">&gt;</i>
		<a href="javascript:void(0)" class="f12 c_gray">个人中心</a>
	</div>
	<%@ include file="../common/account_left.jsp"%>
	<div class="account_right fl">
		<div class="fill_right account_questions">
			<!-- <h2 class="ml40">未解决</h2> -->
			<div class="title">
				<ul id="title" class="clear">
					<li class="cur fl f16 fb">未解决</li>
					<li class="fl"></li>
					<li class="fl f16 fb">已解决</li>
				</ul>
				<div class="send_message cp">
					<i class="icon db fl"></i>
					<span class="db fl">提交工单</span>
				</div>
			</div>
			<ul id="content" class="mt20">
				<li>
					<div class="content">
						<div class="Tenbody">
							<table>
								<tr>
									<th>问题编号</th>
									<th>问题类型</th>
									<th>问题描述</th>
									<th>问题回复</th>
									<th>提交时间</th>
									<th>问题状态</th>
									<th>操作</th>
								</tr>
								<tr><td colspan="7">您暂时没有提问记录</td></tr>		
								<tr>
									<td>10010</td>
									<td>充值问题</td>
									<td>这是个问题，重点bug</td>
									<td>感谢您的提问我们会加紧解决问题</td>
									<td>2016-04-24 15:49:13</td>
									<td>未解决</td>
									<td>删除</td>
								</tr>
							</table>
							<div class="page">
								<ul>
									<!-- 这里是分页 --><%-- ${pagin } --%>
									<li><a style="color:#FFFFFF;" class="current_ss" href="javascript:void(0)">1</a></li>
									<li><a class="" href="javascript:void(0)">2</a></li>
								</ul>
							</div>
						</div>						
					</div>
				</li>
				<li class="dn">
					<div class="content">
						<div class="Tenbody">
							<table>
								<tr>
									<th>问题编号</th>
									<th>问题类型</th>
									<th>问题描述</th>
									<th>问题回复</th>
									<th>提交时间</th>
									<th>问题状态</th>
									<th>操作</th>
								</tr>
								<tr><td colspan="7">您暂时没有已解决提问记录</td></tr>		
								<tr>
									<td>1435</td>
									<td>充值问题</td>
									<td>这是个问题，重点bug</td>
									<td>感谢您的提问我们会加紧解决问题</td>
									<td>2016-04-24 15:49:13</td>
									<td>未解决</td>
									<td>删除</td>
								</tr>
							</table>
							<div class="page">
								<ul>
									<!-- 这里是分页 --><%-- ${pagin } --%>
									<li><a style="color:#FFFFFF;" class="current_ss" href="javascript:void(0)">1</a></li>
									<li><a class="" href="javascript:void(0)">2</a></li>
								</ul>
							</div>
						</div>
					</div>
				</li>
			</ul>
			<!-- <div class="Tentitle"></div>
			<h2 class="ml40">已解决</h2> -->
		</div>
	</div>
	<div class="cb"></div>
</div>
<script>
	$(".send_message").hover(function(){
		$(this).find("span").addClass("cur");
		$(this).find(".icon").addClass("cur");
	},function(){
		$(this).find("span").removeClass("cur");
		$(this).find(".icon").removeClass("cur");
	});
</script>
<%@ include file="../common/footer.jsp"%>
</body>
</html>
