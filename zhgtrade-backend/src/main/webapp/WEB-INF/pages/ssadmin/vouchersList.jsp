<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="comm/include.inc.jsp"%>
<form id="pagerForm" method="post" action="ssadmin/vouchersList.html">
	<input type="hidden" name="status" value="${param.status}"> <input
		type="hidden" name="keywords" value="${keywords}" /> <input
		type="hidden" name="type" value="${ftype}" /><input type="hidden"
		name="pageNum" value="${currentPage}" /> <input type="hidden"
		name="numPerPage" value="${numPerPage}" /> <input type="hidden"
		name="orderField" value="${param.orderField}" /><input type="hidden"
		name="orderDirection" value="${param.orderDirection}" />
</form>


<div class="pageHeader">
	<form onsubmit="return navTabSearch(this);"
		action="ssadmin/vouchersList.html" method="post">
		<div class="searchBar">
			<table class="searchContent">
				<tr>
					<td>会员信息：<input type="text" name="keywords"
						value="${keywords}" size="60" /></td>
				</tr>
			</table>
			<div class="subBar">
				<ul>
					<li><div class="buttonActive">
							<div class="buttonContent">
								<button type="submit">查询</button>
							</div>
						</div>
					</li>
				</ul>
			</div>
		</div>
	</form>
</div>
<div class="pageContent">
	<div class="panelBar">
		<ul class="toolBar">

		</ul>
	</div>
	<table class="table" width="120%" layoutH="138">
		<thead>
			<tr>
				<th width="20">序号</th>
				<th width="60" orderField="fuser.floginName"
					<c:if test='${param.orderField == "fuser.floginName" }'> class="${param.orderDirection}"  </c:if>>会员登陆名</th>
				<th width="60" orderField="fuser.fnickName"
					<c:if test='${param.orderField == "fuser.fnickName" }'> class="${param.orderDirection}"  </c:if>>会员昵称</th>
				<th width="60" orderField="fuser.frealName"
					<c:if test='${param.orderField == "fuser.frealName" }'> class="${param.orderDirection}"  </c:if>>会员真实姓名</th>
				<th width="60">状态</th>
				<th width="60">总金额</th>
				<th width="60">已激活金额</th>
				<th width="60">未激活金额</th>
				<th width="60">有效截止日期</th>
				<th width="60">创建时间</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${vouchersList}" var="vouchers" varStatus="num">
				<tr target="sid_user" rel="${vouchers.fid}">
					<td>${num.index +1}</td>
					<td>${vouchers.fuser.floginName}</td>
					<td>${vouchers.fuser.fnickName}</td>
					<td>${vouchers.fuser.frealName}</td>
					<td>${vouchers.fstatus_s}</td>
					<td>${vouchers.famount}</td>
					<td>${vouchers.famount-vouchers.flastamount}</td>
					<td>${vouchers.flastamount}</td>
					<td>${vouchers.fendate}</td>
					<td>${vouchers.fcreatetime}</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>

	<div class="panelBar">
		<div class="pages">
			<span>总共: ${totalCount}条</span>
		</div>
		<div class="pagination" targetType="navTab" totalCount="${totalCount}"
			numPerPage="${numPerPage}" pageNumShown="5"
			currentPage="${currentPage}"></div>
	</div>
</div>
