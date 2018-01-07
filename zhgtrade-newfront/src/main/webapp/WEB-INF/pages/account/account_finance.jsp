<!-- 个人账户页面author:xxp 2016-04-22 -->
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
    <link rel="stylesheet" href="${resources}/static/css/account.css"/>
</head>
<body>
<c:set var="menu_index" value="4"/>
<%@include file="../common/header.jsp" %>
<div class="center_page">
    <div class="account_nav">
        <a href="/" class="f12 c_blue">首页</a>
        <i class="yjt">&gt;</i>
        <a href="javascript:void(0)" class="f12 c_blue">财务中心</a>
        <i class="yjt">&gt;</i>
        <a href="javascript:void(0)" class="f12 c_gray">个人财务</a>
    </div>
    <c:set var="dt_index" value="1"/>
    <c:set var="dd_index" value="1"/>
    <%@ include file="../common/account_left.jsp" %>
    <!-- 个人财务开始 -->
    <div class="account_right fl">
        <div class="finance_wrapper fill_right">
            <%--<!-- 人民币余额 -->
            <div class="finance_container">
                <h1 class="ml50">个人财务</h1>
                <div class="content">
                    <div class="section fl ml20">
                        <i class="icon icon_1 db fl"></i>
                        <span class="db fl">人民币（CNY）</span>
                    </div>
                    <div class="section fl">
                        <span>可用：</span>
                        <span class="c_green">￥${fns:formatCNY(wallet.ftotalRmb)}</span>
                    </div>
                    <div class="section fl">
                        <span>冻结：</span>
                        <span class="c_red">￥${fns:formatCNY(wallet.ffrozenRmb)}</span>
                    </div>
                    <div class="section fl">
                        <a href="/account/chargermb.html">
                            <i class="icon icon_2 db fl"></i>
                            <span class="orange_button mt35 db fl">充值</span>
                        </a>
                    </div>
                    <div class="cb"></div>
                </div>
            </div>--%>
            <!-- 虚拟币余额 -->
            <div class="coin_acccount">
                <div class="fir item">
                    <p class="fl ml15">币种名称</p>
                    <p class="fl ml10">可用数量</p>
                    <p class="fl">冻结数量</p>
                    <p class="fl">总量</p>
                </div>
                <c:forEach items="${coinWallet}" var="vWallet" varStatus="s">
                    <div class="item">
                        <p class="fl ml40">
                            <img class="coin_img db fl" alt="" height="20" title="${vWallet.fvirtualcointype.fname}" src="${cdn}${vWallet.fvirtualcointype.furl}"/>
                            <span class="b fl pl10 coin_name ellipsis" title="${vWallet.fvirtualcointype.fname}">${vWallet.fvirtualcointype.fname}</span>
                        </p>
                        <p class="fl c_green">
                            <span class="fl pl30">
                                ${fns:formatCoin(vWallet.ftotal)}
                            </span>
                        </p>
                        <p class="fl c_red">
                            <span class="fl pl40">
                                ${fns:formatCoin(vWallet.ffrozen)}
                            </span>
                        </p>
                        <p class="fl">
                            <span class="fl pl30">
                                ${fns:formatCoin(vWallet.ftotal + vWallet.ffrozen)}
                            </span>
                        </p>
                        <p class="fl">
                            <c:choose>
                                <c:when test="${vWallet.fvirtualcointype.FIsWithDraw}">
                                    <a href="/account/withdrawBtc.html?symbol=${vWallet.fvirtualcointype.fid}" class="mr5 c_blue">提现</a>
                                    <a href="/account/chargeBtc.html?symbol=${vWallet.fvirtualcointype.fid}" class="mr5 c_blue">充值</a>
                                </c:when>
                                <c:otherwise>
                                    <a href="javascript:void(0);" class="mr5 c_gray">提现</a>
                                    <a href="javascript:void(0);" class="mr5 c_gray">充值</a>
                                </c:otherwise>
                            </c:choose>
                            <c:choose>
                                <c:when test="${vWallet.fvirtualcointype.fisShare}">
                                    <a href="/market/trade.html?symbol=${vWallet.fvirtualcointype.fid}" class="trade bg_blue">去交易</a>
                                </c:when>
                                <c:otherwise>
                                    <a href="javascript:void(0);" class="trade c_gray bg_ccc">去交易</a>
                                </c:otherwise>
                            </c:choose>
                        </p>
                    </div>
                </c:forEach>
            </div>
        </div>
    </div>
    <div class="cb"></div>
    <!-- 个人财务结束 -->
</div>
<%@ include file="../common/footer.jsp" %>
</body>
</html>

