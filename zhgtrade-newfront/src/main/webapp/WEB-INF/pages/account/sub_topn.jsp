<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="../common/includes.jsp" %>
<table>
    <colgroup>
        <col width="50" />
        <col width="50" />
    </colgroup>
    <tr>
        <th class="">UID</th>
        <th>总推广人数</th>
    </tr>
    <c:if test="${empty tops || 0 == fn:length(tops)}">
        <tr>
            <td colspan="2">暂无排行</td>
        </tr>
    </c:if>
    <c:forEach items="${tops}" var="u">
        <tr>
            <td class="">${u[0]}</td>
            <td>${u[1]}</td>
        </tr>
    </c:forEach>
</table>
