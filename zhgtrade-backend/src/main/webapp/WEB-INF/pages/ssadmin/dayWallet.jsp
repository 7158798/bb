<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="comm/include.inc.jsp"%>
<form id="pagerForm" method="post" action="ssadmin/dayWallet.html?user=${walletList[0].userId}:${loginName}">
    <input type="hidden" name="keywords" value="${keywords}" />
    <input type="hidden" name="pageNum" value="${currentPage}" />
    <input type="hidden" name="numPerPage" value="${numPerPage}" />
    <input type="hidden" name="orderField" value="${param.orderField}" />
    <input type="hidden" name="orderDirection" value="${param.orderDirection}" />
</form>

<div class="pageContent">
    <table class="table" width="100%" layoutH="50">
        <thead>
        <tr>
            <th width="20">序号</th>
            <th width="60">会员登录名称</th>
            <th width="60">剩余资金</th>
            <th width="60">剩余冻结资金</th>
            <th width="60">创建时间</th>
            <th width="60">创建日期</th>

        </tr>
        </thead>
        <tbody>
        <c:forEach items="${walletList}" var="wallet" varStatus="num">
            <tr target="sid_user" rel="${wallet.userId}:${loginName}">
                <td>${num.index +1}</td>
                <td>${loginName}</td>
                <td><fmt:formatNumber value="${wallet.restBalance}" pattern="##.######" maxIntegerDigits="10" maxFractionDigits="4"/></td>
                <td><fmt:formatNumber value="${wallet.restFreeze}" pattern="##.######" maxIntegerDigits="10" maxFractionDigits="4"/></td>
                <td><fmt:formatDate value="${wallet.createTime}" pattern="yyyy-MM-dd HH:mm:ss" ></fmt:formatDate></td>
                <td><fmt:formatDate value="${wallet.createDate}" pattern="yyyy-MM-dd" ></fmt:formatDate></td>
            </tr>
        </c:forEach>
        </tbody>
    </table>

    <div class="panelBar">
        <div class="pages">
            <span>总共: ${totalCount}条</span>
        </div>
        <div class="pagination" targetType="dialog" totalCount="${totalCount}"
             numPerPage="${numPerPage}" pageNumShown="5"
             currentPage="${currentPage}"></div>
    </div>
</div>

