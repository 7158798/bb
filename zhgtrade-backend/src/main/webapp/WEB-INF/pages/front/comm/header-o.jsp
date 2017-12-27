<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="include.inc.jsp"%>
<div class="nav1 clear" id="nav1">
	<div class="nav1-sub clear">
		 <ul style="float:center;">
					<li class="price white" ><span>最新成交价</span></li>
					
					<c:forEach items="${realTimePrize }" var="v" varStatus="vs">
						<c:if test="${v.fisShare==true }">
						<li class="price white" style="font-size: 10px;">${v.fShortName } : <b><span style="color: red;font-size: 10px;">￥</span><span style="color: red;font-size: 10px;" id="LastDealPrize-${v.fShortName }"><fmt:formatNumber value="${v.lastDealPrize }" pattern="##.##" maxFractionDigits="4" maxIntegerDigits="10"/></span></b><span>&nbsp;<img id="LastDealPrize-${v.fShortName }img" src="${staticFileUrl}/static/front/images/blank.png"/></span></li>
						</c:if>
					</c:forEach>
				</ul>
		<div class="nav-quick">

			<div class="rightArea">
				<c:if test="${sessionScope.login_user==null }">
					<a class="loadMessage" href="javascript:showlogin(0);">登录</a>
					<a class="loadMessage" href="javascript:showlogin(1);">注册</a>
				</c:if>
				<c:if test="${sessionScope.login_user!=null }">
					<span class="welcome" id="accountlink">欢迎您,
						UID:<font color="#fff">${sessionScope.login_user.fid }</font>,&nbsp;&nbsp;<font color="#fff">
							${sessionScope.login_user.fnickName }
						</font><a class="triangle"><img src="${staticFileUrl}/static/front/images/new_v1/triangle.png"></a></span>

					<div class="accountpop" id="accountpop" style="display: none;">
						<div class="mycoinmenu">
							<div
								style="border-bottom:1px solid #e6e6e6;width:152px;margin:0px 10px;height:30px;">
								<span style="padding:0px;" class="welcomeNew lightWhite">
									<span> <span style="display:inline-block;width:128px;">
											<span style="color:#333333;max-width:105px;"
											class="nickNameTitle">${sessionScope.login_user.fnickName
												}</span> </span> <a class="triangleNew"></a> </span> </span>
							</div>
							<div class="clear">
								<ul>
									<li style="height:35px;line-height:35px;" class=""><a
										style="padding-left:25px;font-size:14px;"
										class="bannerTradeContent" href="/trade/coin.html">交易中心</a>
									</li>
								</ul>
							</div>
							<div
								style="border-top:1px solid #e6e6e6;width:152px;margin:0px 10px;"></div>
							<div style="padding-top:5px;" class="clear">
								<div
									style="height:30px;line-height:30px;padding-left:19px;color:#565c66;">
									<span class="fwq basicSetting">基本设置</span>
								</div>
								<ul>
									<li class=""><a class="bannerSecurity"
										href="/user/security.html">安全中心</a>
									</li>

									<li class=""><a class="bannerAccount"
										href="/user/userinfo.html">账户信息</a>
									</li>
								</ul>
							</div>
							<div
								style="border-top:1px solid #e6e6e6;width:152px;margin:0px 10px;"></div>
							<div style="padding-top:5px;color:#565c66;" class="clear">
								<div style="height:30px;line-height:30px;padding-left:19px;">
									<span class="fwq questionAnswer1">网站助手</span>
								</div>
								<ul class="">
									<!--   <li><a class="bannerFaq" href="/user/apply.html">申请F币圈子</a></li> -->
									<li><a class="bannerQuestion"
										href="/question/question.html">发起提问</a>
									</li>
									<li><a class="bannerQuestionList"
										href="/question/questionColumn.html">问题列表</a>
									</li>
									<li><a class="bannerQuestionList"
										href="/question/message.html">消息中心</a>
									</li>
								</ul>
							</div>
							<div
								style="border-top:1px solid #e6e6e6;width:152px;margin:0px 10px;"></div>
							<div class="clear">
								<ul>
									<li style="height:35px;line-height:35px;"><a
										style="padding-left:25px;font-size:14px;height:35px;line-height:35px;"
										href="/user/logout.html" class="loginOut">退出账号</a>
									</li>
								</ul>
							</div>
						</div>
					</div>












				</c:if>

			</div>
		</div>
	</div>
</div>


<div class="nav_bg2" id="nav_bg2">
	<div class="head clear">
		<div class="loaded-logo1">
			<a href="/"> <img alt=""
				src="${staticFileUrl}/static/front/images/new_v1/logo.png" width="193" height="59" />
			</a>
		</div>

		<div class="accountinfo1">

			<div class="nav2-up1">

				<div style="height:30px;float:right;width:700px;">
					<c:if test="${sessionScope.login_user!=null }">
						<ul>
							<li><span class="gray2 fontsize-12 dataTitle">可用</span>
							</li>
							<li class="text"><span class="gray2">CNY:</span><span
								id="available.cny" class="money gray2"><fmt:formatNumber
										value="${fwallet.ftotalRmb }" pattern="##.##"
										maxIntegerDigits="10" maxFractionDigits="4" />
							</span>&nbsp;&nbsp;&nbsp;<a class="" href="/account/rechargeCny.html"></a>
							</li>

							<li class="frozen"><span class="gray2 fontsize-12 dataTitle">冻结</span>
							</li>
							<li class="text"><span class="gray2">CNY:</span><span
								id="frozen.cny" class="money gray2"><fmt:formatNumber
										value="${fwallet.ffrozenRmb }" pattern="##.##"
										maxIntegerDigits="10" maxFractionDigits="4" />
							</span>
							</li>

							<li class="frozen"><span class="gray2 fontsize-12 dataTitle">预估总资产</span>
							</li>
							<li class="text"><span class="gray2">CNY:</span><span
								id="frozen.cny" class="money gray2">￥<fmt:formatNumber
										value="${totalCapital }" pattern="##.##" maxIntegerDigits="10"
										maxFractionDigits="4" />
							</span>
							</li>

						</ul>
					</c:if>
				</div>

			</div>

			<div class="nav-bar rr">
				<ul>
					<li class="${selectMenu=='index'?'cur':'' }"><a title="首页"
						href="/">首页</a>
					</li>
					<li class="${selectMenu=='trade'?'cur':'' }"><a title="交易大厅"
						href="/trade/coin.html">交易中心</a>
					</li>
					<li class="${selectMenu=='financial'?'cur':'' }"><a
						title="财务管理" href="/financial/index.html">财务中心</a>
					</li>
					<li class="${selectMenu=='shop'?'cur':'' }"><a title="在线商城"
						href="/shop/index.html">在线商城</a>
					</li>
					<li class="${selectMenu=='shop'?'cur':'' }"><a title="Z-Watch抢购"
						href="/shop/view.html?id=1">Z-Watch抢购</a>
					</li>
					<li class="${selectMenu=='appcenter'?'cur':'' }"><a title="应用中心"
						href="/appcenter/index.html">应用中心</a>
					</li>
					<li class="${selectMenu=='market'?'cur':'' }"><a title="实时行情"
						href="/market.html">实时行情</a>
					</li>
					<li class="${selectMenu=='service'?'cur':'' }"><a title="官方公告"
						href="/service/ourService.html">官方公告</a>
					</li>
					<%-- <li class="${selectMenu=='download'?'cur':'' }"><a title="下载" href="/download/index.html">下载</a></li> --%>
				</ul>
			</div>

		</div>
	</div>
</div>