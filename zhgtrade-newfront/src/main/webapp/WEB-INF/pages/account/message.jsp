<!-- 消息中心author:yujie 2016-04-24 -->
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="../common/includes.jsp" %>
<!DOCTYPE HTML>
<html>
<head>
    <title>${fns:getProperty('site_title')}</title>
    <meta http-equiv=X-UA-Compatible content="IE=edge,chrome=1">
    <meta http-equiv="Pragma" content="no-cache" />
    <meta http-equiv="Cache-Control" content="no-cache" />
    <meta http-equiv="Expires" content="0" />
    <meta content=always name=referrer>
    <meta name='renderer' content='webkit' />
    <meta name="keywords" content="${fns:getProperty('site_keywords')}}">
    <meta name="description" content="${fns:getProperty('site_description')}">
    <link rel="icon" href="/favicon.ico"/>
    <link rel="shortcut icon" href="/favicon.ico"/>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8"/>
    <link rel="stylesheet" type="text/css" href="${resources}/static/css/common/reset.css">
    <link rel="stylesheet" type="text/css" href="${resources}/static/css/common/style.css">
    <link rel="stylesheet" href="${resources}/static/css/account.css" />
</head>
<body>
<c:set var="menu_index" value="4"/>
<%@include file="../common/header.jsp" %>
<div class="center_page">
    <div class="account_nav">
        <a href="/" class="f12 c_blue">首页</a>
        <i class="yjt">&gt;</i>
        <a href="javascript:void(0)" class="f12 c_blue">网站助手</a>
        <i class="yjt">&gt;</i>
        <a href="javascript:void(0)" class="f12 c_gray">消息中心</a>
    </div>
    <c:set var="dt_index" value="3"/>
    <c:set var="dd_index" value="15"/>
    <%@ include file="../common/account_left.jsp" %>
    <div class="account_right fl" id="msg-list">
        <%@include file="message-list.jsp"%>
    </div>


</div>

<div id="dialog" title="消息详情" style="display: none;">
</div>

<style>
    #news{table-layout: fixed}
    #news td{overflow: hidden; white-space: nowrap; text-overflow: ellipsis;}

    #news tr:first-child:hover{background-color: white}
    #news tr:nth-child(even){background:none;}
    #news tr:hover{background-color: #d9d9d9}
</style>
<div class="cb"></div>
<%@include file="../common/footer.jsp" %>
<link rel="stylesheet" href="${resources}/static/js/jquery-ui/css/ui-lightness/jquery-ui-1.10.4.custom.min.css"/>
<script src="${resources}/static/js/jquery-ui/js/jquery-ui-1.10.4.custom.min.js"></script>
<script src="${resources}/static/js/account/question.js"></script>
</body>
</html>
