<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="comm/include.inc.jsp"%>
<!DOCTYPE HTML>
<html>
<head>
</head>
<body>
	<div class="pageFormContent" layoutH="20">

		<fieldset>
			<legend>会员信息</legend>
			<dl>
				<dt>全站总会员数：</dt>
				<dd style="color:red;font-weight:bold;">
					<span class="unit">${totalUser}</span>
				</dd>
			</dl>
			<dl>
				<dt>全站已认证会员数：</dt>
				<dd style="color:red;font-weight:bold;">
					<span class="unit">${totalValidateUser}</span>
				</dd>
			</dl>
			<dl>
				<dt>今日新增会员数：</dt>
				<dd style="color:red;font-weight:bold;">
					<span class="unit">${todayTotalUser}</span>
				</dd>
			</dl>
			<dl>
				<dt>今日已认证会员数：</dt>
				<dd style="color:red;font-weight:bold;">
					<span class="unit">${todayValidateUser}</span>
				</dd>
			</dl>
		</fieldset>

		<fieldset>
			<legend>充值信息</legend>
			<dl>
				<dt>今日充值人民币金额：</dt>
				<dd style="color:red;font-weight:bold;">
					<span class="unit">${amountInMap.totalAmount}</span>
				</dd>
			</dl>
			<dl>
				<dt>累计充值人民币金额：</dt>
				<dd style="color:red;font-weight:bold;">
					<span class="unit">${totalAmountInMap.totalAmount}</span>
				</dd>
			</dl>
		</fieldset>

		<fieldset>
			<legend>当前累计待提现信息</legend>
			<dl>
				<dt>人民币金额：</dt>
				<dd style="color:red;font-weight:bold;">
					<span class="unit"><fmt:formatNumber
							value="${amountOutWaitingMap.totalAmount}" pattern="#0.######" />
					</span>
				</dd>
			</dl>
			<c:forEach items="${virtualOutWaitingMap}" var="virtualOutWaiting">
				<dl>
					<dt>${virtualOutWaiting.fName}数量：</dt>
					<dd style="color:red;font-weight:bold;">
						<span class="unit"><fmt:formatNumber
								value="${virtualOutWaiting.amount}" pattern="#0.######" /> </span>
					</dd>
				</dl>
			</c:forEach>
		</fieldset>

		<fieldset>
			<legend>提现信息</legend>
			<dl>
				<dt>今日提现金额：</dt>
				<dd style="color:red;font-weight:bold;">
					<span class="unit"><fmt:formatNumber
							value="${amountOutMap.totalAmount}" pattern="#0.######" /> </span>
				</dd>
			</dl>
			<dl>
				<dt>累计提现金额：</dt>
				<dd style="color:red;font-weight:bold;">
					<span class="unit"><fmt:formatNumber
							value="${amountOutMap1.totalAmount}" pattern="#0.######" /> </span>
				</dd>
			</dl>
			<c:forEach items="${virtualOutSuccessMap}" var="virtualOutSuccess">
				<dl>
					<dt>${virtualOutSuccess.fName}数量：</dt>
					<dd style="color:red;font-weight:bold;">
						<span class="unit"><fmt:formatNumber
								value="${virtualOutSuccess.amount}" pattern="#0.######" /> </span>
					</dd>
				</dl>
			</c:forEach>
		</fieldset>
	</div>
</body>
</html>

