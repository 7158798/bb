<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="../common/includes.jsp" %>
<!DOCTYPE HTML>
<html>
<head>
    <title>${fns:getProperty('site_title')}</title>
    <meta http-equiv=X-UA-Compatible content="IE=edge,chrome=1">
    <meta content=always name=referrer>
    <meta name='renderer' content='webkit' />
    <meta name="keywords" content="${fns:getProperty('site_keywords')}">
    <meta name="description" content="${fns:getProperty('site_description')}">
    <link rel="icon" href="/favicon.ico"/>
    <link rel="shortcut icon" href="/favicon.ico"/>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8"/>
    <link rel="stylesheet" type="text/css" href="${resources}/static/css/common/reset.css">
    <link rel="stylesheet" type="text/css" href="${resources}/static/css/common/style.css">
    <link rel="stylesheet" href="${resources}/static/css/common/animate.css"/>
</head>
<body>
<c:set var="menu_index" value="6"/>
<%@ include file="../common/header.jsp" %>
<link rel="stylesheet" href="${resources}/static/css/guide.css"/>
<div class="center_page">
    <div class="guide_nav">
        <a href="/" class="f12 c_blue">首页</a>
        <i class="yjt">&gt;</i>
        <a href="/guide/help.html" class="f12 c_blue">新手指南</a>
        <i class="yjt">&gt;</i>
        <a class="c_gray">${farticletype.fname}</a>
    </div>
    <%@ include file="../common/guide_left.jsp" %>

    <div class="guide_right fl assets_center clear po_re zin70">
        <%--<div class="title f14 fb" style="height: 35px; margin-top: 5px; margin-bottom: 0.3em">${farticletype.fname}</div>--%>
        <%--<div style="margin-top: 1em; border-bottom: dashed 1px #000"></div>--%>
        <div class="content" style="margin-top: 1.8em">
            <div class="typeId" style="display: none">${id}</div>
            <c:forEach var="v" items="${farticles}" varStatus="status">
                <div class="list">
                    <a href="/guide/article.html?id=${v.fid}">
                        <span style="float: left">${status.index +1}</span>
                        <%--<i class="iconfont c_gray db fl">&#xe616;</i>--%>
                        <span class="db fl pl10 f15">${v.ftitle}</span>
                    </a>
                    <span class="db fr c_gray" style="font-size: 13px"><fmt:formatDate value="${v.flastModifyDate }" pattern="yyyy-MM-dd HH:mm:ss"/></span>
                </div>
            </c:forEach>
        </div>
        <div style="float:right;">
            <%--<div class="page">--%>
                <%--<ul>${pagin}</ul>--%>
            <%--</div>--%>
                <page:pagination pageSzie="${pageSize}" pageNow="${pageNow}" total="${total}" href="/guide/index?id=${typeId}&currentPage=#pageNumber"/>
        </div>
    </div>
</div>

<div id="test">

</div>
<div class="cb"></div>
<%@ include file="../common/footer.jsp" %>
</body>
</html>