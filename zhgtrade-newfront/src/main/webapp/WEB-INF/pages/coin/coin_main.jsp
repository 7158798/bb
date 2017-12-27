<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="../common/includes.jsp" %>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>${fns:getProperty('site_title')}</title>
    <meta charset="utf-8">
    <meta content=always name=referrer>
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="keywords" content="${fns:getProperty('site_keywords')}">
    <meta name="description" content="${fns:getProperty('site_description')}">
    <%--<link rel="stylesheet" href="//cdn.bootcss.com/bootstrap/3.3.6/css/bootstrap.min.css">--%>
    <link rel="stylesheet" type="text/css" href="${resources}/static/css/common/reset.css">
    <link rel="stylesheet" type="text/css" href="${resources}/static/css/common/style.css">
    <link rel="stylesheet" href="${resources}/static/css/coin-main.css"/>
</head>
<body>

<c:set var="menu_index" value="2"/>
<%@include file="../common/header.jsp" %>

<div class="container-fluid m1 text-center" style="background: url('${resources}/static/images/coin/bg1.jpg');">
    <h1>创新区块链资产的纳斯达克</h1>
    <h3>
        <span class="line"></span>
        币主板=区块链资产的“主板市场”
        <span class="line"></span>
    </h3>
</div>

<%@include file="coin.jsp"%>

<div class="m3">
    <%@include file="bank.jsp"%>
</div>

<%@include file="zichan.jsp"%>

<%@include file="biaozhun.jsp"%>

<div class="m6 text-center">
    <ul class="con">
        <li class="d1"><span>最佳展示位置</span></li>
        <li class="d2"><span>优先品牌营销和推广</span></li>
        <li class="d2"><span style="margin-top: 260px;">交易最为活跃</span></li>
        <li class="d1"><span style="margin-top: 260px;">优先对接创新商业模式</span></li>
    </ul>
    <div style="position: absolute;top: 244px;width: 100%;">
        <div class="mid"><span>币主板核心优势</span></div>
    </div>
</div>

<div class="m7 text-center" style="background: url('${resources}/static/images/coin/bg3.jpg')">
    <h1>转板机制</h1>
    <div class="line"></div>
    <p>每月两大子板块的币种在各自板块按单独交易额排名</p>
    <img src="${resources}/static/images/coin/zhuanban.png"/>
</div>

<%@include file="remark.jsp"%>
<%@include file="../common/footer_edg.jsp"%>
</body>
</html>
