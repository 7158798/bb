<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="../common/includes.jsp" %>
<table>
    <colgroup>
        <col width="50" />
        <col width="50" />
    </colgroup>
    <tr>
        <th class="">收益内容</th>
        <th>创建时间</th>
    </tr>
    <c:if test="${empty incomes || 0 == fn:length(incomes)}">
        <tr>
            <td colspan="2">您暂时没有收益记录</td>
        </tr>
    </c:if>
    <c:forEach items="${incomes}" var="in">
        <tr>
            <td class="">${in.ftitle}</td>
            <td><fmt:formatDate value="${in.fcreatetime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
        </tr>
    </c:forEach>
</table>
<page:pagination pageSzie="${pageSize}" pageNow="${pageNow}" total="${total}" href="/account/incomes.html?currentPage=#pageNumber"/>