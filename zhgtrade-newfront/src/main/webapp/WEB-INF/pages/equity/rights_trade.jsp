<!-- 权益交易页面author:xxp 2016-04-21 -->
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
    <link rel="stylesheet" href="${resources}/static/css/common/animate.css"/>
    <link rel="stylesheet" href="${resources}/static/css/coin_trade.css"/>
    <link rel="stylesheet" href="${resources}/static/css/announce.css"/>
</head>
<body>
<c:set var="menu_index" value="3"/>
<%@include file="../common/header.jsp" %>
<!-- 大图开始 -->
<div class="carousel">
    <div class="rights_img_show img_show" onclick="window.location.href = '/equity/intro.html';">
        <div class="center_page">
            <div class="item3">
                <p class="fr animated flipInY show_light"><a class="f22" href="javascript:void(0)">了解权益交易</a></p>
            </div>
        </div>
    </div>
</div>


<!-- 大图结束 -->
<div class="coin_wrapper center_page">
    <div class="coin_title">
        <ul>
            <li class="fl cur">
                <p class="f16 fb pt5">权益交易</p>
                <i class="iconfont">&#xe604;</i>
            </li>
        </ul>
    </div>
    <div class="coin_list">
        <ul>
            <!-- 权益交易 -->
            <li>
                <div class="item item_fir">
                    <div class="fl item_leap">权益资产</div>
                    <div class="fl item_leap">
                        <span class="db fl pl15 pointer cannot_select">最新价格(CNY)</span>
                        <span class="db fl cp mt5">
                            <i data-sort="1" class="iconfont c_gray db">&#xe61c;</i>
                            <i data-sort="2" class="iconfont c_gray db">&#xe61d;</i>
                        </span>
                    </div>
                    <div class="fl item_leap">
                        <span class="db fl pl10 pointer cannot_select">24h成交额(CNY)</span>
                        <span class="db fl cp mt5">
                            <i data-sort="17" class="iconfont c_gray db">&#xe61c;</i>
                            <i data-sort="18" class="iconfont c_gray db">&#xe61d;</i>
                        </span>
                    </div>
                    <div class="fl item_leap">
                        <span class="db fl pl25 pointer cannot_select">24h成交量</span>
                        <span class="db fl cp mt5">
                            <i data-sort="3" class="iconfont c_gray db">&#xe61c;</i>
                            <i data-sort="4" class="iconfont c_gray db">&#xe61d;</i>
                        </span>
                    </div>
                    <div class="fl item_leap">
                        <span class="db fl pl20 pointer cannot_select">总市值(CNY)</span>
                        <span class="db fl cp mt5">
                            <i data-sort="5" class="iconfont c_gray db">&#xe61c;</i>
                            <i data-sort="6" class="iconfont c_gray db">&#xe61d;</i>
                        </span>
                    </div>
                    <div class="fl item_leap">
                        <span class="db fl pl30 pointer cannot_select">日涨跌幅</span>
                        <span class="db fl cp mt5">
                            <i data-sort="7" class="iconfont c_gray db">&#xe61c;</i>
                            <i data-sort="8" class="iconfont c_gray db">&#xe61d;</i>
                        </span>
                    </div>
                    <div class="fl item_leap">
                        <span class="db fl pl30 pointer cannot_select">周涨跌幅</span>
                        <span class="db fl cp mt5">
                            <i data-sort="9" class="iconfont c_gray db">&#xe61c;</i>
                            <i data-sort="10" class="iconfont c_gray db">&#xe61d;</i>
                        </span>
                    </div>
                    <div class="fl item_leap">价格趋势（3日）</div>
                    <div class="fl item_leap"></div>
                </div>
                <!-- 循环开始 -->
                <div id="dealList">
                    <%@include file="../index_list.jsp" %>
                </div>
                <!-- 循环结束 -->
            </li>
        </ul>
    </div>
</div>
<%@include file="../common/announce.jsp" %>
<!--[if lte IE 8]>
<script type="text/javascript" src="${resources}/static/js/jquery/excanvas.min.js"></script>
<![endif]-->
<script type="text/javascript" src="${resources}/static/js/jquery/jquery.flot.min.js"></script>
<script src="${resources}/static/js/trade/equity.js"></script>
<%@ include file="../common/footer.jsp" %>
</body>
</html>

