<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="../common/includes.jsp" %>
<!DOCTYPE HTML>
<html>
<head>
    <title>${fns:getProperty('site_title')}</title>
    <meta http-equiv=X-UA-Compatible content="IE=edge,chrome=1">
    <meta content=always name=referrer>
    <meta name='renderer' content='webkit'/>
    <meta name="keywords" content="${fns:getProperty('site_keywords')}">
    <meta name="description" content="${fns:getProperty('site_description')}">
    <link rel="icon" href="/favicon.ico"/>
    <link rel="shortcut icon" href="/favicon.ico"/>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8"/>
    <link rel="stylesheet" type="text/css" href="${resources}/static/css/common/reset.css">
    <link rel="stylesheet" type="text/css" href="${resources}/static/css/common/style.css">
    <link rel="stylesheet" href="${resources}/static/css/account.css"/>
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
        <a href="javascript:void(0)" class="f12 c_gray">问题列表</a>
    </div>
    <c:set var="dt_index" value="3"/>
    <c:set var="dd_index" value="14"/>
    <%@ include file="../common/account_left.jsp" %>
    <div class="account_right fl" id="question-list">
        <%@ include file="question-list.jsp" %>
    </div>
    <div class="cb"></div>
</div>

<style>
    .questionlist {
        table-layout: fixed
    }

    .questionlist tr:first-child:hover {
        background-color: white
    }

    .questionlist td {
        background-color: white;
        word-wrap: break-word;
    }

    .questionlist .singlerow {
        overflow: hidden;
        white-space: nowrap;
        text-overflow: ellipsis;
        -moz-text-overflow: ellipsis;
    }
</style>

<script>
    $(".send_message").hover(function () {
        $(this).find("span").addClass("cur");
        $(this).find(".icon").addClass("cur");
    }, function () {
        $(this).find("span").removeClass("cur");
        $(this).find(".icon").removeClass("cur");
    });

</script>
<script src="${resources}/static/js/account/question.js"></script>
<%@ include file="../common/footer.jsp" %>
</body>
</html>