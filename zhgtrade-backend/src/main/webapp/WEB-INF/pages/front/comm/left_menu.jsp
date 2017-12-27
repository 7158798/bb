<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="include.inc.jsp" %>
			<div class="leftMenu">
			<c:if test="${selectMenu == 'gamecenter' || selectMenu == 'trade'}">
			  <div  class="bor">
				<div class="${selectGroup==1?'tradeTitlecur':'tradeTitle' }">
					<span class="${selectGroup==1?'trade1cur':'trade1'}">交易中心</span>
				</div>
				<ul>
					<c:forEach items="${requestScope.constant['virtualCoinType'] }" var="v" varStatus="vs">
						<c:if test="${v.fisShare==true }">
						<li class="${(leftMenu==v.fid_s||(leftMenu=='0'&&vs.index==0))?'cur':'' }"><a href="/trade/coin.html?coinType=${v.fid }&tradeType=0" class="i1" >${v.fShortName }交易</a></li>
						</c:if>
					</c:forEach>
					<li class="${leftMenu=='entrust'?'cur':'' }"><a href="/trade/entrust.html" class="items3">委托管理</a></li>
				</ul>
			  </div>
			 </c:if>
			 <c:if test="${selectMenu == 'appcenter'}">
			  <div  class="bor">
				<div class="${selectGroup==6?'tradeTitlecur':'tradeTitle' }">
					<span class="${selectGroup==6?'trade1cur':'trade1'}">应用中心</span>
				</div>
				<ul>
					<li class="${leftMenu=='subscriptionrmb'?'cur':'' }"><a href="/trade/subscriptionrmb.html" class="items3">抢购中心</a></li>
					<li class="${leftMenu=='subscriptioncoin'?'cur':'' }"><a href="/trade/subscriptioncoin.html" class="items3">换购中心</a></li>
					<li class="${leftMenu=='game'?'cur':'' }"><a href="/gamecenter/game.html" class="items3">疯狂农场</a></li>
					<li class="${leftMenu=='hedging'?'cur':'' }"><a href="/hedging/index.html" class="items7">币对冲</a></li>
					
				</ul>
			  </div>
			 </c:if>
			 <c:if test="${selectMenu == 'financial'}">
			  <div class="bor">
				<div class="${selectGroup==2?'tradeTitlecur':'tradeTitle' }">
					<span class="${selectGroup==2?'acountManage1cur':'acountManage1'}">财务管理</span>
				</div>
				<ul>
					<li class="${leftMenu=='rechargeCny'?'cur':'' }"><a href="/account/rechargeCny.html" class="items5">人民币充值</a></li>
					<li class="${leftMenu=='withdrawCny'?'cur':'' }"><a href="/account/withdrawCny.html" class="items6">人民币提现</a></li>
					<li class="${leftMenu=='rechargeBtc'?'cur':'' }"><a href="/account/rechargeBtc.html" class="items7">虚拟币充值</a></li>
					<li class="${leftMenu=='withdrawBtc'?'cur':'' }"><a href="/account/withdrawBtc.html" class="items8">虚拟币提现</a></li>
					<li class="${leftMenu=='financial'?'cur':'' }"><a href="/financial/index.html" class="items5">个人财务</a></li>
					<!--<li class="${leftMenu=='vouchers'?'cur':'' }"><a href="/vouchers/index.html" class="items5">代金券</a></li>-->
					<li class="${leftMenu=='record'?'cur':'' }"><a href="/account/record.html" class="items9">账单明细</a></li>
					<li class="${leftMenu=='mysell'?'cur':'' }"><a href="/shop/sell.html" class="items9">商家中心</a></li>
		            <li class="${leftMenu=='myorder'?'cur':'' }"><a href="/shop/myorder.html" class="items9">我的订单</a></li>
		            <li class="${leftMenu=='balance'?'cur':'' }"><a href="/balance/index.html" class="items7">币生币理财</a></li>
				</ul>
			  </div>
			  </c:if>
			  <div class="bor">
				<div class="${selectGroup==3?'tradeTitlecur':'tradeTitle' }">
					<span class="${selectGroup==3?'basicSetting1cur':'basicSetting1'}">基本设置</span>
				</div>
				<ul>
					<li class="${leftMenu=='security'?'cur':'' }"><a href="/user/security.html" class="items10">安全中心</a></li>
					<li class="${leftMenu=='userinfo'?'cur':'' }"><a href="/user/userinfo.html" class="items11">个人信息</a></li>
					<li class="${leftMenu=='intro'?'cur':'' }"><a href="/user/intro.html" class="items11">推广中心</a></li>
					<li class="${leftMenu=='introl'?'cur':'' }"><a href="/introl/mydivide.html" class="items9">推广收益</a></li>
				</ul>
			  </div>

				  <div class="bor">
					<div class="${selectGroup==4?'tradeTitlecur':'tradeTitle' }">
						<span class="${selectGroup==4?'questionAnswercur':'questionAnswer'}">网站助手</span>
					</div>
					<ul>
					    <%-- <li class="${leftMenu=='apply'?'cur':'' }"><a href="/user/apply.html" class="items12">申请F币圈子</a></li> --%>
						<li class="${leftMenu=='question'?'cur':'' }"><a href="/question/question.html" class="items12">发起提问</a></li>
						<li class="${leftMenu=='questionColumn'?'cur':'' }"><a href="/question/questionColumn.html" class="items13">问题列表</a></li>
						<li class="${leftMenu=='message'?'cur':'' }"><a href="/question/message.html" class="items13">消息中心</a></li>
					</ul>
				  </div>

			  <div class="bor">
				<div class="tradeTitle">
				</div>
			  </div>
			</div>
	