<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div id="top_menubar" class="top_nav fr">
    <ul>
        <li <c:if test="${'1' == menu_index}">class="current"</c:if>>
            <a href="/index.html" title=""><span>${menu_index}11首页</span></a>
        </li>
        <li <c:if test="${'9' == menu_index}">class="current"</c:if>>
            <a href="/activity/btc_actor.html" title=""><span>票选演员</span></a>
        </li>
        <li <c:if test="${'7' == menu_index}">class="current"</c:if>><a href="/activity/btc_guess.html"
                                                                        title=""><span>票房竞猜</span></a></li>
        <li <c:if test="${'8' == menu_index}">class="current"</c:if>><a href="/activity/btc_fetch.html" title=""><span>选票获取</span></a>
            <%--<li <c:if test="${'10' == menu_index}">class="current"</c:if>><a href="/answer/btc_answer.html" title=""><span>答题活动</span></a>--%>
        </li>
    </ul>
</div>>