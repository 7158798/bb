<!-- 安全中心页面author:yujie 2016-04-24 -->
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
    <link rel="stylesheet" href="${resources}/static/css/account.css" />
    <link rel="stylesheet" href="${resources}/static/css/selector.css" />
</head>
<body>
<c:set var="menu_index" value="4"/>
<%@include file="../common/header.jsp" %>
<div class="center_page">
    <div class="account_nav">
        <a href="/" class="f12 c_blue">首页</a>
        <i class="yjt">&gt;</i>
        <a href="/account/hedging.html" class="f12 c_blue">币对冲</a>
        <i class="yjt">&gt;</i>
        <a href="javascript:void(0)" class="f12 c_gray">我的对冲</a>
    </div>
    <c:set var="dt_index" value="5"/>
    <c:set var="dd_index" value="17"/>
    <%@ include file="../common/account_left.jsp" %>
    <div class="account_right fl">
        <div class="finance_wrapper">
            <div class="safe_center fill_right content_wrapper hedging">
                <h1 class="ml40">我的对冲</h1>
                <div class="hedging_content">
                    <div class="total f12">累计参加对冲：
                        <span class="c_blue">20次</span>
                    </div>
                    <form id="form" action="/account/hedging.html">
                    <span id="symbol" class="selector">
                        <span class="selector_item">
                            <span class="sel_item">虚拟币类型</span>
                            <input type="hidden" name="symbol" value="${symbol}">
                        </span>
                        <span class="selector_items dn">
                            <a data-val="${coin.fid}" <c:if test="${symbol == coin.fid}">class="cur"</c:if>>企鹅币（QEC）</a>
                            <a data-val="${coin.fid}" <c:if test="${symbol == coin.fid}">class=""</c:if>>众创币（CIC）</a>
                        </span>
                    </span>
                    </form>
                </div>
                <div class="coin_acccount">
                    <div class="fir item">
                        <p class="fl">币种名称</p>
                        <p class="fl">我的对冲</p>
                        <p class="fl">我的获得</p>
                        <p class="fl">手续费</p>
                        <p class="fl">时间</p>
                        <p class="fl">总量</p>
                    </div>
                    <div class="item">
                        <p class="fl">
                            <img class="coin_img db fl" alt="" width="20" height="20" title="${vWallet.value.fvirtualcointype.fname}" src="${cdn}${vWallet.value.fvirtualcointype.furl}"/>
                            <span class="b fl pl10 coin_name ellipsis" title="${vWallet.value.fvirtualcointype.fname}">众创币</span>
                        </p>
                        <p class="fl">20</p>
                        <p class="fl">20</p>
                        <p class="fl">1</p>
                        <p class="fl">2015-05-05 11:11:11</p>
                        <p class="fl">100000000</p>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <script  src="${resources}/static/js/utils/selector.js"></script>
    <script>
        $("#symbol").custom_selector({onChange : function(){
            $("#currentPage").val("1");
            $("#form").submit();
        }, boxClass : "selector_width150"});
    </script>
<%@ include file="../common/footer.jsp" %>
</body>
</html>
