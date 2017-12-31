<!-- author:xxp 2016-04-22  -->
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!-- 左边导航开始 -->
<div class="account_left fl">
	<dl>
		<!-- 财务管理 选中效果只需加cur样式-->
		<dt <c:if test="${'1' == dt_index}">class="cur"</c:if>>
			<i class="icon icon_1 fl db"></i>
			<span class="pl20 fl f16 db">财务管理</span>
			<i class="fr dn iconfont c_blue">&#xe60c;</i>
		</dt>
		<dd <c:if test="${'2' == dd_index}">class="cur"</c:if>>
			<a href="/account/chargermb.html">
				<%--<i class="icon icon_1_2 db fl"></i>--%>
				<span class="pl10 db f14 tac">人民币充值</span>
			</a>
		</dd>
		<dd <c:if test="${'3' == dd_index}">class="cur"</c:if>>
			<a href="/account/withdrawCny.html">
				<%--<i class="icon icon_1_1 db fl"></i>--%>
				<span class="pl10 db f14 tac">人民币提现</span>
			</a>
		</dd>
		<dd <c:if test="${'4' == dd_index}">class="cur"</c:if>>
			<a href="/account/chargeBtc.html">
				<%--<i class="icon icon_1_4 db fl"></i>--%>
				<span class="pl10 db f14 tac">虚拟币充值</span>
			</a>
		</dd>
		<dd <c:if test="${'5' == dd_index}">class="cur"</c:if>>
			<a href="/account/withdrawBtc.html">
				<%--<i class="icon icon_1_3 db fl"></i>--%>
				<span class="pl10 db f14 tac">虚拟币提现</span>
			</a>
		</dd>
		<dd <c:if test="${'20' == dd_index}">class="cur"</c:if>>
			<a href="/account/transfer.html">
				<%--<i class="icon icon_1_3 db fl"></i>--%>
				<span class="pl10 db f14 tac">资金转移</span>
			</a>
		</dd>
		<dd <c:if test="${'1' == dd_index}">class="cur"</c:if>>
			<a href="/account/fund.html">
				<%--<i class="icon icon_1_5 db fl"></i>--%>
				<span class="pl10 db f14 tac">个人财务</span>
			</a>
		</dd>
		<%--<dd <c:if test="${'6' == dd_index}">class="cur"</c:if>>
			<a href="/account/bills.html">
				&lt;%&ndash;<i class="icon icon_1_6 db fl"></i>&ndash;%&gt;
				<span class="pl10 db f14 tac">账单明细</span>
			</a>
		</dd>--%>
		<dd <c:if test="${'7' == dd_index}">class="cur"</c:if>>
			<a href="/account/entrusts.html">
				<%--<i class="icon icon_1_8 db fl"></i>--%>
				<span class="pl10 db f14 tac">委托管理</span>
			</a>
		</dd>
		<!-- 基本设置开始 -->
		<dt <c:if test="${'2' == dt_index}">class="cur"</c:if>>
			<i class="icon icon_2 fl db"></i>
			<span class="pl20 fl db f16 ">基本设置</span>
			<i class="fr dn iconfont c_blue">&#xe60c;</i>
		</dt>
		<dd <c:if test="${'8' == dd_index}">class="cur"</c:if>>
			<a href="/account/security.html">
				<%--<i class="icon icon_2_1 db fl"></i>--%>
				<span class="pl10 db f14 tac">安全中心</span>
			</a>
		</dd>
		<dd <c:if test="${'9' == dd_index}">class="cur"</c:if>>
			<a href="/account/personalinfo.html">
				<%--<i class="icon icon_2_2 db fl"></i>--%>
				<span class="pl10 db tac f14">个人信息</span>
			</a>
		</dd>
		<dd <c:if test="${'10' == dd_index}">class="cur"</c:if>>
			<a href="/account/spread.html">
				<%--<i class="icon icon_2_3 db fl"></i>--%>
				<span class="pl10 db tac f14">推广中心</span>
			</a>
		</dd>
		<%--<dd <c:if test="${'11' == dd_index}">class="cur"</c:if>>
			<a href="/account/intros.html">
				&lt;%&ndash;<i class="icon icon_2_4 db fl"></i>&ndash;%&gt;
				<span class="pl10 db tac f14">推广收益</span>
			</a>
		</dd>--%>

		<!-- 网站助手开始 -->
		<dt <c:if test="${'3' == dt_index}">class="cur"</c:if>>
			<i class="icon icon_3 fl db"></i>
			<span class="pl20 fl db f16 ">网站助手</span>
			<i class="fr dn iconfont c_blue">&#xe60c;</i>
		</dt>
		<%--<dd <c:if test="${'13' == dd_index}">class="cur"</c:if>>--%>
			<%--<a href="account/question.html">--%>
				<%--&lt;%&ndash;<i class="icon icon_3_1 db fl"></i>&ndash;%&gt;--%>
				<%--<span class="pl10 db tac f14">发起提问</span>--%>
			<%--</a>--%>
		<%--</dd>--%>
		<dd <c:if test="${'14' == dd_index}">class="cur"</c:if>>
			<a href="/account/questionColumn.html">
				<%--<i class="icon icon_3_2 db fl"></i>--%>
				<span class="pl10 db tac f14">问题列表</span>
			</a>
		</dd>
		<dd <c:if test="${'15' == dd_index}">class="cur"</c:if>>
			<a href="/account/message.html">
				<%--<i class="icon icon_3_3 db fl"></i>--%>
				<span class="pl10 db tac f14">消息中心</span>
			</a>
		</dd>
		<dd <c:if test="${'18' == dd_index}">class="cur"</c:if>>
			<a href="/account/api.html">
				<%--<i class="icon icon_3_3 db fl"></i>--%>
				<span class="pl10 db tac f14">API申请</span>
			</a>
		</dd>
		<!-- 我的收藏开始 -->
		<dt <c:if test="${'4' == dt_index}">class="cur"</c:if>>
			<i class="icon icon_4 fl db"></i>
			<span class="pl20 fl db f16 ">我的收藏</span>
			<i class="fr dn iconfont c_blue">&#xe60c;</i>
		</dt>
		<dd <c:if test="${'16' == dd_index}">class="cur"</c:if>>
			<a href="/account/collection.html">
				<span class="pl10 db tac f14">关注币种</span>
			</a>
		</dd>
		<!-- 币对冲开始 -->
		<%--<dt <c:if test="${'5' == dt_index}">class="cur"</c:if>>
			<i class="icon icon_5 fl db"></i>
			<span class="pl20 fl db f16 ">币对冲</span>
			<i class="fr dn iconfont c_blue">&#xe60c;</i>
		</dt>
		<dd <c:if test="${'17' == dd_index}">class="cur"</c:if>>
			<a href="/account/hedging.html">
				<span class="pl10 db tac f14">我的对冲</span>
			</a>
		</dd>--%>
	</dl>
	<%--<div id="verticle_line"></div>--%>
</div>
<!-- 左边导航结束 -->