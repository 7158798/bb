<%--<!-- 首页 author:yujie 2016-04-20 -->--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="common/includes.jsp" %>
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
    <link rel="shortcut icon" href="/favicon.ico" type="image/x-icon"/>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8"/>
    <link rel="stylesheet" type="text/css" href="${resources}/static/css/common/reset.css">
    <link rel="stylesheet" type="text/css" href="${resources}/static/css/common/style.css">
    <link rel="stylesheet" href="${resources}/static/css/common/animate.css"/>
    <link rel="stylesheet" href="${resources}/static/css/index.css"/>
    <link rel="stylesheet" href="${resources}/static/css/coin_trade.css"/>
    <link rel="stylesheet" href="${resources}/static/css/announce.css"/>
    <script>var cdn = "${cdn}/";</script>
</head>
<body>
<c:set var="menu_index" value="1"/>
<%@include file="common/header.jsp" %>
<%--<!-- banner图开始 -->--%>
<input type="hidden" id="menuIndex" value="0">
<div id="cir" class="carousel">
    <c:set var="banner_count" value="10"/>
    <c:set var="first_index" value=""/>
    <ol class="carousel-indicators">
        <c:set var="b_index" value="0"/>
        <c:forEach begin="1" end="${banner_count}" var="i" varStatus="s">
            <c:set var="key">
                <c:out value="bigImage${i}"></c:out>
            </c:set>
            <c:if test="${!empty requestScope.constant[key] and requestScope.constant[key] != '#'}">

                <c:if test="${empty first_index}">
                    <c:set var="first_index" value="${i}"/>
                </c:if>
                <li data-to="${b_index}" class="<c:if test="${first_index == s.index}">on</c:if>"></li>
                <c:set var="b_index" value="${b_index + 1}"/>
            </c:if>
        </c:forEach>
    </ol>
    <div class="carousel-inner">
        <c:forEach begin="1" end="${banner_count}" var="i" varStatus="s">
            <c:set var="key">
                <c:out value="bigImage${i}"></c:out>
            </c:set>
            <c:if test="${!empty requestScope.constant[key] and requestScope.constant[key] != '#'}">
                <div class="item hand <c:choose><c:when test="${first_index == s.index}">active</c:when><c:otherwise>dn</c:otherwise></c:choose>" style="background-image: url(${cdn}${requestScope.constant[key]});"></div>
            </c:if>
        </c:forEach>
    </div>
</div>

<%--<!-- 已登录 -->--%>
<div id="loginedBox" class="center_page login_relative logined_box dn">
    <div class="login_wrapper f16">
        <div class="top pl20" style="padding-left: 5px">
            <dl>
                <dt class="ml25 ">
                    <span class="db fl">欢迎您：</span>
                    <a name="loginName" class="db fl tal" style="overflow: hidden; width: 200px; text-overflow: ellipsis;white-space:nowrap" title="" href="/account/personalinfo.html"></a>
                </dt>
                <dt class="ml25" style="clear: both">
                    ID：<a name="userId" href="/account/personalinfo.html"></a>
                </dt>
            </dl>
        </div>
        <div class="center">
            <div class="f18 fund">资产估值：￥<span id="assetSpan"></span></div>
            <div class="f18 enter_trade">
                <a href="/account/fund.html">进入财务中心</a>
            </div>
            <div class="rechrge_withdraw">
                <a href="/account/chargermb.html" class="f14 operation">人民币充值</a> <a
                    href="/account/withdrawCny.html"
                    class="f14 last operation">人民币提现</a>
            </div>
        </div>
    </div>
    <div class="yy_wrapper ">
        <div class="yy yy_top"></div>
        <%--<div class="yy yy_center"></div>--%>
    </div>
</div>
<%--<!-- 未登录 -->--%>
<div id="unloginBox" class="center_page login_relative logined_box dn">
    <div class="login_wrapper f16" style="height: 210px;" id="index_login_wrapper1">
        <div class="top pl20" style="padding-left: 30px">登录本站</div>
        <div class="center">
            <form id="indexLoginForm" action="/user/login.html" method="post">
                <p class="c_red f12 tac message" id="indexLoginTips1" style="margin-top: 10px;line-height:20px;height:20px;"></p>
                <p class="pt10">
                    <input id="indexLoginName" name="loginName" require="true" data-name="登录名"
                           class="pl5"
                           type="text" placeholder="邮箱/手机"/>
                </p>
                <p class="pt20">
                    <input name="password" class="pl5" type="password" require="true" data-name="密码"
                           data-min="6" data-max="20" placeholder="密码" id="indexLoginPwd"
                           onkeyup="onEnterClick('index_login_btn1');"/>
                </p>
                <p class="pt20">
                    <%--<a href="javascript:void(0);" onclick="indexLogin(document.getElementById('indexLoginForm'));"--%>
                       <%--class="login bg_blue"--%>
                       <%--id="login_btn">登录</a> <a--%>
                        <%--href="/user/to_reg" class="register bg_orange" id="button2">注册</a>--%>
                    <a style="border-radius: 0;width: 100%;" href="javascript:void(0);" id="index_login_btn1" class="login bg_orange">登录</a>

                 </p>
                 <p class="pt20">
                     <a class="f14 fl" style="display: inline;width: 72px;" href="/user/find_pwd.html">忘记密码？</a>
                     <a class="f14 fr" style="display: inline;width: 72px;" href="/user/to_reg">注册账号</a>
                 </p>
             </form>
         </div>
     </div>
    <div class="login_wrapper safe f16 dn" style="height: 210px;" id="index_login_wrapper2">
        <div class="top pl20" style="padding-left: 30px">安全验证</div>
        <div class="center">
            <p class="c_red f12 tac message" id="indexLoginTips2" style="margin-top: 10px;line-height:20px;height:20px;"></p>
            <p class="pt10">
                <input name="loginName"  id="index_img_code" class="pl5 img_code" type="text" placeholder="图形验证码"/>
                <img class="image_code" id="index_image_code" title="换一换"/>
            </p>
            <p class="pt20">
                <input name="password" id="index_code" class="pl5" type="text" placeholder="手机验证码" onkeyup="onEnterClick('index_login_btn2');"/>
                <a href="javascript:void(0)" class="code" id="index_sendCode" data-name="发送">发送</a>
            </p>
            <p class="voice_tips" id="index_voice_tips">
                <span>接收不到短信验证码？</span>
                <a href="javascript:void(0)" id="index_sendVoiceCode">试试语音验证码</a>
            </p>
            <p class="pt20">
                <a style="border-radius: 0;width: 100%;" href="javascript:void(0);" class="login bg_orange" id="index_login_btn2">登录</a>
            </p>
        </div>
    </div>
     <div class="yy_wrapper">
         <div class="yy yy_top"></div>
         <%--<div class="yy yy_center"></div>--%>
         <%--<div class="yy yy_edg"></div>--%>
    </div>
</div>

<%--<!-- banner图结束 -->--%>
<%--<p style="font-size:14px;text-align: center;color:#ff0000;height:50px;line-height:50px;background:#f5f5f5;position:relative;">--%>
    <%--<i class="iconfont" style="position:absolute;top:0px;left:50%;margin-left:-505px;color:#969696;">&#xe642;</i>--%>
    <%--众股网提醒您：请不要投入超过自身风险承受能力的资金，不投资不了解的数字资产，谨防虚假宣传、电信诈骗，坚决抵制传销、洗钱等违法行为。--%>
<%--</p>--%>
<%--<!-- 最火排行开始 -->--%>
<%--<div class="hot_coin center_page clear">--%>
    <%--<div class="hot_title">--%>
        <%--&lt;%&ndash;<ul>&ndash;%&gt;--%>
            <%--&lt;%&ndash;<li class="zh">本月十佳</li>&ndash;%&gt;--%>
            <%--&lt;%&ndash;<li class="en"><span class="left"></span><span class="middle">Ranking</span><span class="right"></span></li>&ndash;%&gt;--%>
        <%--&lt;%&ndash;</ul>&ndash;%&gt;--%>
    <%--</div>--%>
    <%--<div class="content fl">--%>
        <%--<div class="item item_fir clear">--%>
            <%--<div class="fl item_leap item_leap1">币种</div>--%>
            <%--<div class="fl item_leap item_leap2">--%>
                <%--<span class="db fr cp mt5">--%>
                    <%--<i data-sort="1" class="iconfont c_gray db">&#xe61c;</i>--%>
                    <%--<i data-sort="2" class="iconfont c_gray db">&#xe61d;</i>--%>
                <%--</span>--%>
                <%--<span class="db fr  pointer cannot_select">最新价格(CNY)</span>--%>
            <%--</div>--%>
            <%--<div class="fl item_leap item_leap3">--%>
                <%--<span class="db fr cp mt5">--%>
                    <%--<i data-sort="17" class="iconfont c_gray db">&#xe61c;</i>--%>
                    <%--<i data-sort="18" class="iconfont c_gray db">&#xe61d;</i>--%>
                <%--</span>--%>
                <%--<span class="db fr  pointer cannot_select">24h成交额(CNY)</span>--%>
            <%--</div>--%>
            <%--<div class="fl item_leap item_leap4">--%>
                <%--<span class="db fr cp mt5">--%>
                    <%--<i data-sort="3" class="iconfont c_gray db">&#xe61c;</i>--%>
                    <%--<i data-sort="4" class="iconfont c_gray db">&#xe61d;</i>--%>
                <%--</span>--%>
                <%--<span class="db fr  pointer cannot_select">24h成交量</span>--%>
            <%--</div>--%>
            <%--<div class="fl item_leap item_leap5">--%>
                <%--<span class="db fr cp mt5">--%>
                    <%--<i data-sort="5" class="iconfont c_gray db">&#xe61c;</i>--%>
                    <%--<i data-sort="6" class="iconfont c_gray db">&#xe61d;</i>--%>
                <%--</span>--%>
                <%--<span class="db fr  pointer cannot_select">总市值(CNY)</span>--%>
            <%--</div>--%>
            <%--<div class="fl item_leap item_leap6">--%>
                <%--<span class="db fr cp mt5">--%>
                    <%--<i data-sort="7" class="iconfont c_gray db">&#xe61c;</i>--%>
                    <%--<i data-sort="8" class="iconfont c_gray db">&#xe61d;</i>--%>
                <%--</span>--%>
                <%--<span class="db fr  pointer cannot_select">日涨跌幅</span>--%>
            <%--</div>--%>
            <%--<div class="fl item_leap item_leap7">--%>
                <%--<span class="db fr cp mt5">--%>
                    <%--<i data-sort="9" class="iconfont c_gray db">&#xe61c;</i>--%>
                    <%--<i data-sort="10" class="iconfont c_gray db">&#xe61d;</i>--%>
                <%--</span>--%>
                <%--<span class="db fr  pointer cannot_select">周涨跌幅</span>--%>
            <%--</div>--%>
            <%--&lt;%&ndash;<div class="fl item_leap item_leap8">价格趋势（3日）</div>--%>
            <%--<div class="fl item_leap item_leap9"></div>&ndash;%&gt;--%>
            <%--<div class="fl item_leap item_leap8">图表</div>--%>
        <%--</div>--%>
        <%--&lt;%&ndash;<!-- 循环开始 -->&ndash;%&gt;--%>
        <%--<div id="dealList">--%>
            <%--<%@include file="index_list_consult.jsp" %>--%>
        <%--</div>--%>
        <%--&lt;%&ndash;<!-- 循环结束 -->&ndash;%&gt;--%>
    <%--</div>--%>
    <%--&lt;%&ndash;<div class="consult fr slideBox">&ndash;%&gt;--%>
        <%--&lt;%&ndash;<ul class="consult_hd clear">&ndash;%&gt;--%>
            <%--&lt;%&ndash;<li class="fir on">资讯</li>&ndash;%&gt;--%>
            <%--&lt;%&ndash;<li class="" data-name="market">行情</li>&ndash;%&gt;--%>
            <%--&lt;%&ndash;<li class="">项目路演</li>&ndash;%&gt;--%>
        <%--&lt;%&ndash;</ul>&ndash;%&gt;--%>
        <%--&lt;%&ndash;<ul class="consult_bd">&ndash;%&gt;--%>
            <%--&lt;%&ndash;<li id="consult"></li>&ndash;%&gt;--%>
            <%--&lt;%&ndash;<li id="tm_market" class="dn">&ndash;%&gt;--%>
                <%--&lt;%&ndash;&lt;%&ndash;<span class="iconfont db" style="font-size:100px;margin-top:100px;color:#08a4d7;">&#xe63d;</span>&ndash;%&gt;&ndash;%&gt;--%>
                <%--&lt;%&ndash;<a href="http://news.zhgtrade.com/markets.html#btc"><div id="container" style="min-width: 310px; height: 420px; margin: 0 auto"></div></a>&ndash;%&gt;--%>
            <%--&lt;%&ndash;</li>&ndash;%&gt;--%>
            <%--&lt;%&ndash;<li id="pro" class="dn">&ndash;%&gt;--%>
                <%--&lt;%&ndash;<span class="iconfont db" style="font-size:100px;margin-top:100px;color:#08a4d7;">&#xe63d;</span>&ndash;%&gt;--%>
            <%--&lt;%&ndash;</li>&ndash;%&gt;--%>
        <%--&lt;%&ndash;</ul>&ndash;%&gt;--%>
    <%--&lt;%&ndash;</div>&ndash;%&gt;--%>
<%--</div>--%>

<div class="coin_wrapper center_page">
    <div class="coin_title">
        <ul>
            <li data-type="1" class="fl cur">
                <p class="f16 fb pt5">${name}交易区</p> <i class="iconfont">&#xe604;</i>
            </li>
            <%--<li data-type="2" class="fl">--%>
                <%--<p class="f16 fb pt5">币三板</p> <i class="iconfont">&#xe604;</i>--%>
            <%--</li>--%>
        </ul>
    </div>
    <div class="coin_list">
        <ul>
            <!-- 币主板 -->
            <li class="main">
                <div class="item item_fir">
                    <div class="fl item_leap">币种</div>
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
                <div id="dealList1">
                    <c:set var="coinType" value="1"/>
                    <%@include file="index_list.jsp" %>
                </div>
                <!-- 循环结束 -->
            </li>
            <!-- 币三板 -->
            <li class="dn">
                <div class="item item_fir">
                    <div class="fl item_leap">币种</div>
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
                <div id="dealList2">
                    <c:set var="coinType" value="2"/>
                    <%@include file="index_list.jsp" %>
                </div>
                <!-- 循环结束 -->
            </li>
        </ul>
    </div>
</div>

<script src="${resources}/static/js/highcharts/highcharts.js"></script>


<%@include file="common/announce.jsp" %>
<!--[if lte IE 8]>
<script type="text/javascript" src="${resources}/static/js/jquery/excanvas.min.js"></script>
<![endif]-->
<script type="text/javascript" src="${resources}/static/js/jquery/jquery.flot.min.js"></script>
<script type="text/javascript" src="${resources}/static/js/index.js?v=1.4"></script>
<script src="${resources}/static/js/trade/coin.js"></script>
<%@ include file="common/footer.jsp" %>
</body>
</html>
