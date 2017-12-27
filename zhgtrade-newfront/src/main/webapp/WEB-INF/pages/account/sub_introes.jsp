<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="../common/includes.jsp" %>
<table>
    <colgroup>
        <col width="50" />
        <col width="50" />
    </colgroup>
    <tr>
        <th class="">会员UID</th>
        <th>推荐时间</th>
    </tr>
    <c:if test="${empty intros || 0 == fn:length(intros)}">
        <tr>
            <td colspan="2">您暂时没有推荐记录</td>
        </tr>
    </c:if>
    <c:forEach items="${intros}" var="intro">
        <tr>
            <td class="">${intro.fid}</td>
            <td><fmt:formatDate value="${intro.fregisterTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
        </tr>
    </c:forEach>
</table>
<page:pagination pageSzie="${pageSize}" pageNow="${pageNow}" total="${total}" href="/account/incomes.html?currentPage=#pageNumber"></page:pagination>