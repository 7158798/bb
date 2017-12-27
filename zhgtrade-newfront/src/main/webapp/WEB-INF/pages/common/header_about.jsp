<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div id="top_menubar" class="top_nav fr">
    <ul>
        <li <c:if test="${'1' == menu_index}">class="current"</c:if>>
            <a href="/index.html" title=""><span>首页</span></a>
        </li>
        <li <c:if test="${'2' == menu_index}">class="current"</c:if>>
            <a href="/about/events.html" title=""><span>大事记</span></a>
        </li>
        <li <c:if test="${'3' == menu_index}">class="current"</c:if>><a href="/about/managerteam.html?id=5"
                                                                        title=""><span>管理团队</span></a></li>
        <li style="padding-right: 0" <c:if test="${'4' == menu_index}">class="current"</c:if>><a href="/about/newsmedia.html"
                                                                        title=""><span>媒体报道</span></a></li>
        <li style="padding-right: 0" <c:if test="${'5' == menu_index}">class="current"</c:if>><a href="/about/contact.html"
                                                                        title=""><span>联系我们</span></a></li>
    </ul>
</div>