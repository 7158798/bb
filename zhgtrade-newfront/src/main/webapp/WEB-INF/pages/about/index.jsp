<%--
  Created by IntelliJ IDEA.
  User: sunpeng
  Date: 2016/5/27 0027
  Time: 13:56
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="../common/includes.jsp" %>
<!DOCTYPE HTML>
<html>
<head>
    <title>${fns:getProperty('site_title')}</title>
    <meta name="viewport" content="width=device-width,initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=no"/>
    <meta http-equiv=X-UA-Compatible content="IE=edge,chrome=1">
    <meta http-equiv="Pragma" content="no-cache" />
    <meta http-equiv="Cache-Control" content="no-cache" />
    <meta http-equiv="Expires" content="0" />
    <meta content=always name=referrer>
    <meta name="keywords" content="${fns:getProperty('site_keywords')}">
    <meta name="description" content="${fns:getProperty('site_description')}">
    <link rel="icon" href="/favicon.ico"/>
    <link rel="shortcut icon" href="/favicon.ico"/>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8"/>
    <link rel="stylesheet" type="text/css" href="${resources}/static/css/common/reset.css">
    <link rel="stylesheet" type="text/css" href="${resources}/static/css/common/style.css">
    <link rel="stylesheet" href="${resources}/static/css/common/animate.css"/>
    <link rel="stylesheet" href="${resources}/static/css/account.css" />
</head>
<body>
<c:set var="menu_index" value="4"/>
<%@include file="../common/header.jsp" %>
<div class="line_area">
    <div class="subtit mb10">
        <h2 class="fl"><span class="icojt"></span>${fabout.ftitle }</h2>
        <span class="fr"><a class="c_blue" href="/">首页</a> &gt;  ${fabout.ftitle }</span></div>
    <br style="border-top: 1px solid #0A0A0A">
    <div class="area_pd25 doc">
        <p>${fabout.fcontent }</p>
    </div>
</div>
<style>
    .line_area{width: 1200px;margin: auto; padding: 3em; line-height: 2em}
</style>
<%@include file="../common/footer.jsp" %>
</body>
</html>
