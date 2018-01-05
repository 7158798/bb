<%--
  Created by IntelliJ IDEA.
  User: sunpeng
  Date: 2016/5/28 0028
  Time: 14:33
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="../common/includes.jsp" %>
<!DOCTYPE HTML>
<html>
<head>
    <title>${fns:getProperty('site_title')}</title>
    <meta http-equiv=X-UA-Compatible content="IE=edge,chrome=1">
    <meta content=always name=referrer>
    <%--<meta name="viewport" content="width=device-width, initial-scale=14.0">--%>
    <meta name="keywords" content="${fns:getProperty('site_keywords')}">
    <meta name="description" content="${fns:getProperty('site_description')}">
    <link rel="icon" href="/favicon.ico"/>
    <link rel="shortcut icon" href="/favicon.ico"/>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8"/>
    <link rel="stylesheet" type="text/css" href="${resources}/static/css/common/reset.css">
    <link rel="stylesheet" type="text/css" href="${resources}/static/css/common/style.css">
    <link rel="stylesheet" href="${resources}/static/css/common/animate.css"/>
    <link rel="stylesheet" href="${resources}/static/css/aboutzhg/contactus.css"/>
</head>
<body>
<c:set var="menu_index" value="5"/>
<c:set var="header_style" value="header_about"/>
<%@include file="../common/header.jsp" %>

<div class="center">

    <div class="top">
        <hr id="lefthorizontal">
        <div class="leftRect"></div>
        <span class="title">联系我们</span>
        <hr id="righthorizontal">
        <div class="rightRect"></div>
    </div>

    <div class="cb"></div>

    <div class="left fl">
        <img src="${resources}/static/images/index/weixin.jpg" width="116" height="116">
        <p>微信扫一扫<br>关注我们更多的信息</p>
    </div>

    <div class="middle fl">
        <p><i class="iconfont c_blue mr15">&#xe633;</i><span>服务热线：${requestScope.constant['telephone']}</span></p>
        <%--<p><i class="iconfont c_blue mr15">&#xe632;</i><span>集团邮箱：${requestScope.constant['email']}</span></p>
        <p><i class="iconfont c_blue mr15">&#xe630;</i><span>邮政编码：${requestScope.constant['post']}</span></p>--%>
        <p><i class="iconfont c_blue mr15">&#xe631;</i><span>集团地址：${requestScope.constant['address']}</span></p>
    </div>

    <div class="right fr">
        <img src="${resources}/static/images/aboutzhg/map.jpg" height="188px">
    </div>

    <div class="bottom cb">

    </div>

</div>
<%@include file="../common/footer_edg.jsp"%>
<style>

</style>
</body>
</html>
