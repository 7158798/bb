<!-- 消息中心author:yujie 2016-04-24 -->
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
		<a href="javascript:void(0)" class="f12 c_blue">首页</a>
		<i class="yjt">&gt;</i>
		<a href="javascript:void(0)" class="f12 c_blue">财务中心</a>
		<i class="yjt">&gt;</i>
		<a href="javascript:void(0)" class="f12 c_gray">个人中心</a>
	</div>
	<%@ include file="../common/account_left.jsp"%>
	<div class="account_right fl">
		<div class="fill_right account_newscenter">
			<h2 class="ml40">消息中心</h2>
			<div class="news_operation f12">
				<a href="javascript:void(0)" id="delete_news">删除</a>
				<a href="javascript:void(0)" id="flag_all">全部标记为已读</a>
			</div>
			<div class="content">
				<div class="Tenbody">
					<table id="news">
						<tr>
							<th>选择</th>
							<th>状态</th>
							<th>标题</th>
							<th>时间</th>
							<th>操作</th>
						</tr>
						<tr><td colspan="5">您暂时没有消息</td></tr>		
						<tr data-id="1">
							<td width="50">
								<i class="iconfont c_gray cp">&#xe620;</i>
							</td>
							<td width="50">
								<i class="iconfont c_blue">&#xe61e;</i>
							</td>
							<td>赠送代金券通知</td>
							<td>2016-01-22 12:20:48</td>
							<td>
								<a href="javascript:void(0)" class="c_blue">查看</a>
							</td>
						</tr>
						<tr data-id="2">
							<td width="50">
								<i class="iconfont c_gray cp">&#xe620;</i>
							</td>
							<td width="50">
								<i class="iconfont c_gray">&#xe61e;</i>
							</td>
							<td>赠送代金券通知</td>
							<td>2016-01-22 12:20:48</td>
							<td>
								<a href="javascript:void(0)" class="c_blue">查看</a>
							</td>
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
		</div>
	</div>
	<div class="cb"></div>
</div>
<script>
	$(function(){
		$("#news").find("tr").each(function(){
			$(this).find("td:eq(0)").find(".iconfont").click(function(){
				var $this=$(this);
				if($this.data("flag")!=1){
					$this.data("flag",1);
					$this.addClass("c_blue").removeClass("c_gray");
					$this.html("&#xe621;");
				}else{
					$this.data("flag",0);
					$this.removeClass("c_blue").addClass("c_gray");
					$this.html("&#xe620;");
				}
			});
		});
		$("#delete_news").click(function(){
			var $this=$(this);
			if($this.text("删除")){
				alert("删除中，请稍等");
				return;
			}
			var id=[];
			$("#news").find("tr").each(function(){
				var $this=$(this);
				if($this.find("td:eq(0)").find(".iconfont").data("flag")==1){
					id.push($this.data("id"));
				}
			});
			alert("要删除的id有："+id);
		});
		$("#flag_all").click(function(){
			alert("请补充全部标记为已读的ajax");
		});
	});
</script>
<%@ include file="../common/footer.jsp"%>
</body>
</html>
