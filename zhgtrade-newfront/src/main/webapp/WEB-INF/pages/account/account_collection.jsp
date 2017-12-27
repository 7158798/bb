<!-- 安全中心页面author:yujie 2016-04-24 -->
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="../common/includes.jsp" %>
<%@ taglib uri="/WEB-INF/tlds/activity.tld" prefix="ac" %>
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
</head>
<body>
<c:set var="menu_index" value="4"/>
<%@include file="../common/header.jsp" %>
<div class="center_page clear">
    <div class="account_nav">
        <a href="/" class="f12 c_blue">首页</a>
        <i class="yjt">&gt;</i>
        <a href="/account/collection.html" class="f12 c_blue">我的收藏</a>
        <i class="yjt">&gt;</i>
        <a href="javascript:void(0)" class="f12 c_gray">关注币种</a>
    </div>
    <c:set var="dt_index" value="4"/>
    <c:set var="dd_index" value="16"/>
    <%@ include file="../common/account_left.jsp" %>
    <div class="account_right fl">
        <div class="finance_wrapper collection">
            <div class="safe_center fill_right content_wrapper">
                <h1 class="ml40">关注币种</h1>
                <div class="col_content">
                    <ul class="clear" id="collections">
                        <c:forEach items="${collectionlist}" var="map" varStatus="l">
                        <li <c:if test="${l.index%4==0}">class="fir"</c:if>>
                            <a href="/market/trade.html?symbol=${map.cid}" class="link">
                            <div class="pic">
                                <img title="" alt="" height="50" width="50" src="${cdn}${map.furl}">
                                <div class="current_price c_blue">${map.lastDealPrize}</div>
                                <p class="clear">
                                    <span class="db fl">买一价</span>
                                    <span class="db fl sec">${map.higestBuyPrize}</span>
                                </p>
                                <p class="clear">
                                    <span class="db fl">卖一价</span>
                                    <span class="db fl sec">${map.lowestSellPrize}</span>
                                </p>
                                <p class="clear">
                                    <span class="db fl">今日成交量</span>
                                    <span class="db fl sec">${map.volumn}</span>
                                </p>
                                <p class="clear">
                                    <span class="db fl">涨跌幅</span>
                                    <c:choose>
                                    <c:when test="${map.fupanddown>=0}"><span class="db fl sec" style="color:#fd0202;">${map.fupanddown}%↑</span></c:when>
                                    <c:when test="${map.fupanddown<0}"><span class="db fl sec" style="color:#1f963d;">${map.fupanddown}%↓</span></c:when>
                                    </c:choose>
                                </p>
                            </div>
                            <div class="details">
                                <p class="name">${map.fname}</p>
                                <c:choose>
                                    <c:when test="${map.fupanddown>0}"><p class="status c_red">今天您关注的${map.fname}上涨了</p></c:when>
                                    <c:when test="${map.fupanddown==0}"><p class="status c_blue">今天您关注的${map.fname}持平</p></c:when>
                                    <c:when test="${map.fupanddown<0}"><p class="status c_green">今天您关注的${map.fname}下跌了</p></c:when>
                                </c:choose>
                            </div>
                            <div class="time">
                                <span class="c_gray">收藏于${ac:getTimeFormat(map.createTime)}</span>
                                <a class="iconfont c_blue fr delete" href="javascript:void(0)" data-id="${map.cid}">&#xe62e;</a>
                            </div>
                            </a>
                        </li>
                        </c:forEach>
                        <li id="add">
                            <i class="icon add db"></i>
                            <span class="db tac f12 add_txt">添加币种</span>
                        </li>
                    </ul>
                </div>
            </div>
        </div>
    </div>
    <!-- 弹出窗 -->
    <div id="tm_yy" class="dn"></div>
    <!-- 短信验证弹出窗 -->
    <div id="add_box" class="float_box dn">
        <div class="f_title pl10">
            <h3 class="db fl">关注币种</h3>
            <i class="iconfont c_gray db fr close">&#xe609;</i>
        </div>
        <div class="f_content">
            <form  action="/account/unMobile.html" method="post">
                <p>
                    <select id="add_collection">
                        <c:forEach items="${uncollectionlist}" var="list">
                            <option value="${list.fid}">${list.fname}</option>
                        </c:forEach>
                    </select>
                </p>
                <a href="javascript:void(0);" class="confirm bg_blue c_white">确认</a>
            </form>
        </div>
    </div>
    <script src="${resources}/static/js/account/collection.js"></script>
</div>
<%@ include file="../common/footer.jsp" %>
</body>
</html>
