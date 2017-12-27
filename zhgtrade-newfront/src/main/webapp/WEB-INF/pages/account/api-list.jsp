<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="../common/includes.jsp" %>
<c:choose>
    <c:when test="${empty list || 0 == fn:length(list)}">
        <tr class="nodata" data-empty="true"><td colspan="7">您暂时没有提现数据</td></tr>
    </c:when>
    <c:otherwise>
        <c:forEach items="${list}" var="line">
            <tr data-id="${line.id}">
                <td class="api_name">${line.name}</td>
                <td class="permission" data-permission="${line.type.index}">${line.type.name}</td>
                <td><fmt:formatDate value="${line.createTime}" pattern="yyyy-MM-dd" /></td>
                <td>
                    <c:choose>
                        <c:when test="${!empty line.updateTime}">
                            <fmt:formatDate value="${line.updateTime}" pattern="yyyy-MM-dd" />
                        </c:when>
                        <c:otherwise>-</c:otherwise>
                    </c:choose>
                </td>
                <td data-status="${line.status.index}">
                    <c:choose>
                        <c:when test="${0 == line.status.index}"><span class="c_green">正常</span></c:when>
                        <c:when test="${1 == line.status.index}"><span class="c_orange">禁用</span></c:when>
                    </c:choose>
                </td>
                <td>
                    <a href="javascript:void(0)" class="c_blue refresh">更新密钥</a>
                    <a href="javascript:void(0)" class="c_blue edit">编辑</a>
                    <a href="javascript:void(0)" class="c_blue look">查看</a>
                </td>
            </tr>
        </c:forEach>
    </c:otherwise>
</c:choose>