<%--
  Created by IntelliJ IDEA.
  User: sunpeng
  Date: 2016/5/24 0024
  Time: 19:47
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!-- 公告/动态开始 -->
<div class="cb" style="border-top: 1px dashed #eee;margin-top: 20px;"></div>
<div class="announce center_page">
    <div class="cms_title">
        <ul>
            <li class="zh">公告中心</li>
            <li class="en"><span class="left"></span><span class="middle">News</span><span class="right"></span></li>
        </ul>
    </div>
    <div class="fl item" style="margin-left: 100px">
        <div class="last_news">
            <a href="/guide/index.html?id=2"><span class="title">官方公告</span></a>
            <div class="seek_more">
                <a href="/guide/index.html?id=2" class="c_blue"> <span>更多</span> <i class="yjt">&gt;</i>
                </a>
            </div>
        </div>
        <div class="cb"></div>
        <div class="content">
            <c:forEach items="${nContents}" var="content" varStatus="s">
                <p class="pointer">
                    <a class="ellipsis db fl text <c:if test="${content.top}">c_red</c:if>" title="${content.ftitle}"
                       href="/guide/article.html?id=${content.fid}">${content.ftitle_short}</a>
                    <span class="db fr time"><fmt:formatDate value="${content.fcreateDate}" pattern="MM月dd日"/></span>
                </p>
            </c:forEach>
        </div>
    </div>
    <div class="fl item ml30" style="margin-left: 215px">
        <div class="market_news" >
            <a href="/guide/index.html?id=9"><span class="title">新闻媒体</span></a>
            <div class="seek_more">
                <a href="/guide/index.html?id=9" class="c_blue"> <span>更多</span> <i class="yjt">&gt;</i>
                </a>
            </div>
        </div>
        <div class="cb"></div>
        <div class="content">
            <c:forEach items="${marketContents}" var="content" varStatus="s">
                <p class="pointer">
                    <a class="ellipsis db fl text <c:if test="${content.top}">c_red</c:if>" title="${content.ftitle}"
                       href="/guide/article.html?id=${content.fid}">${content.ftitle_short}</a>
                    <span class="db fr time"><fmt:formatDate value="${content.fcreateDate}" pattern="MM月dd日"/></span>
                </p>
            </c:forEach>
        </div>
    </div>
    <%--<div class="fl item ml30">--%>
        <%--<div class="official_news">--%>
            <%--<a href="/guide/index.html?id=8"><span class="title">项目展示</span></a>--%>
            <%--<div class="seek_more">--%>
                <%--<a href="/guide/index.html?id=8" class="c_blue"> <span>更多</span> <i class="yjt">&gt;</i>--%>
                <%--</a>--%>
            <%--</div>--%>
        <%--</div>--%>
        <%--<div class="cb"></div>--%>
        <%--<div class="content">--%>
            <%--<c:forEach items="${oContents}" var="content" varStatus="s">--%>
                <%--<p class="pointer">--%>
                    <%--<a class="ellipsis db fl text <c:if test="${content.top}">c_red</c:if>" title="${content.ftitle}"--%>
                       <%--href="/guide/article.html?id=${content.fid}">${content.ftitle_short}</a>--%>
                    <%--<span class="db fr time"><fmt:formatDate value="${content.fcreateDate}" pattern="MM月dd日"/></span>--%>
                <%--</p>--%>
            <%--</c:forEach>--%>
        <%--</div>--%>
    <%--</div>--%>
</div>
<div class="cb"></div>
