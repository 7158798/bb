<!-- 所有后台传输文章页面 author:yujie 2016-04-25 -->
<%--<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>--%>
<%--<%@ include file="../common/header.jsp" %>--%>

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
<%@include file="../common/header.jsp" %>
<link rel="stylesheet" href="${resources}/static/css/guide.css"/>
<%--<div class="center_page guide_wrapper f14">--%>
<div class="center_page">
    <div class="guide_nav">
        <a href="/" class="f12 c_blue">首页</a>
        <i class="yjt">&gt;</i>
        <a href="/guide/help.html" class="f12 c_blue">新手指南</a>
        <i class="yjt">&gt;</i>
        <a class="c_gray">${farticletype.fname}</a>
    </div>
    <%@ include file="../common/guide_left.jsp" %>
    <div class="guide_right fl">
        <p class="title fl">${farticle.ftitle}</p>
    <span style="float: right">管理员&nbsp;&nbsp;&nbsp;<fmt:formatDate value="${farticle.flastModifyDate }" pattern="yyyy-MM-dd HH:mm:ss"/></span>
        <div class="cb"></div>
        <article class="content">
            <p style="display: block">${farticle.fcontent}</p>
            <c:if test="${farticle.fdocurl != null &&  farticle.fdocurl != ''}">
                <p><a href="${cdn}${farticle.fdocurl}">文件下载链接</a></p>
            </c:if>
        </article>
    </div>
</div>
<div class="cb"></div>

<%--</div>--%>
<%@ include file="../common/footer.jsp" %>
</body>
</html>

