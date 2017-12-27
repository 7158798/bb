<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="common/includes.jsp" %>
<c:forEach items="${!empty dealDatas ? dealDatas : '1' == coinType ? mainDatas : leftDatas}" var="deal" varStatus="s">
    <a href="/market/trade.html?symbol=${deal.fid}">
    <div class="item trdeHref clear">
        <div class="coinid dn">${deal.fid}</div>
        <div class="fl item_leap item_leap1">
            <c:if test="${!empty deal.furl}">
                <img src="${cdn}${deal.furl}" width="22" alt="${deal.fname}${deal.fShortName}" title="${deal.fname}${deal.fShortName}" class="db fl"/>
            </c:if>
            <b title="${deal.fname}${deal.fShortName}" class="coinname db fl ellipsis f12">${deal.fname}${deal.fShortName}</b>
        </div>
        <div class="fl item_leap item_leap2" >
            <span class="fr c_red ">${fns:formatCoin(deal.lastDealPrize)}</span>
        </div>
        <div class="fl item_leap item_leap3" >
            <c:choose>
                <c:when test="${deal.fentrustValue > 100000000}">
                    <span class="fr "><fmt:formatNumber value="${deal.fentrustValue/100000000}" pattern="0.00"/>亿</span>
                </c:when>
                <c:when test="${deal.fentrustValue > 10000}">
                    <span class="fr "><fmt:formatNumber value="${deal.fentrustValue/10000}" pattern="0.00"/>万</span>
                </c:when>
                <c:otherwise>
                    <span class="fr ">${fns:formatCNY(deal.fentrustValue)}</span>
                </c:otherwise>
            </c:choose>
        </div>
        <div class="fl item_leap item_leap4">
            <c:choose>
                <c:when test="${deal.volumn > 100000000}">
                    <span class="fr "><fmt:formatNumber value="${deal.volumn/100000000}" pattern="0.00"/>亿</span>
                </c:when>
                <c:when test="${deal.volumn > 10000}">
                    <span class="fr "><fmt:formatNumber value="${deal.volumn/10000}" pattern="0.00"/>万</span>
                </c:when>
                <c:otherwise>
                    <span class="fr ">${fns:formatCoin(deal.volumn)}</span>
                </c:otherwise>
            </c:choose>
        </div>
        <div class="fl item_leap item_leap5">
            <c:choose>
                <c:when test="${deal.fmarketValue > 100000000}">
                    <span class="fr "><fmt:formatNumber value="${deal.fmarketValue/100000000}" pattern="0.00"/>亿</span>
                </c:when>
                <c:when test="${deal.fmarketValue > 10000}">
                    <span class="fr "><fmt:formatNumber value="${deal.fmarketValue/10000}" pattern="0.00"/>万</span>
                </c:when>
                <c:otherwise>
                    <span class="fr ">${fns:formatCNY(deal.fmarketValue)}</span>
                </c:otherwise>
            </c:choose>
        </div>
        <c:choose>
            <c:when test="${deal.fupanddown < 0}">
                <div class="fl item_leap c_green item_leap6" >
                    <span class="fr pr35">${deal.fupanddown}%<i class="db fr">↓</i></span>
                </div>
            </c:when>
            <c:otherwise>
                <div class="fl item_leap c_red item_leap6" >
                    <span class="fr pr35">+${deal.fupanddown}%<i class="db fr">↑</i></span>
                </div>
            </c:otherwise>
        </c:choose>
        <c:choose>
            <c:when test="${deal.fupanddownweek < 0}">
                <div class="fl item_leap c_green item_leap7" >
                    <span class="fr pr35">${deal.fupanddownweek}%<i class="db fr">↓</i></span>
                </div>
            </c:when>
            <c:otherwise>
                <div class="fl item_leap c_red item_leap7" >
                    <span class="fr pr35">+${deal.fupanddownweek}%<i class="db fr">↑</i></span>
                </div>
            </c:otherwise>
        </c:choose>
        <%--<div class="fl item_leap trend item_leap8" data-id="${deal.fid}" style="">
        </div>--%>
        <%--<div class="fl item_leap item_leap9">
            <a href="/market/trade.html?symbol=${deal.fid}" class="bg_blue">去交易</a>
        </div>--%>
        <div class="fl item_leap chart_pic item_leap8">
            <div class="trend dn" data-id="${deal.fid}">
            </div>
            <i class="iconfont">&#xe639;</i>
        </div>
    </div>
    </a>
</c:forEach>

<script>
    $(function () {
        $(".trdeHref").on('click', function () {
            $this = $(this);
            var vid = $this.find(".coinid").text();
            window.location.href = "/market/trade.html?symbol=" + vid;
        })
    })
</script>