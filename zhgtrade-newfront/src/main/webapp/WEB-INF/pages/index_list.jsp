<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="common/includes.jsp" %>
<c:forEach items="${!empty dealDatas ? dealDatas : '1' == coinType ? mainDatas : leftDatas}" var="deal" varStatus="s">
    <div class="item trdeHref">
        <div class="coinid dn">${deal.fid}</div>
        <div class="fl item_leap">
            <%--<c:if test="${!empty deal.furl}">--%>
                <%--<img src="${cdn}${deal.furl}" width="22" alt="${deal.fname}${deal.fShortName}" title="${deal.fname}${deal.fShortName}" class="db fl"/>--%>
            <%--</c:if>--%>
            <b title="${deal.fname}${deal.fShortName}" class="coinname db fl ellipsis f12">${deal.fname}${deal.fShortName}</b>
        </div>
        <div class="fl item_leap" >
            <span class="fr c_red pr15">${fns:formatCoin(deal.lastDealPrize)}</span>
        </div>
        <div class="fl item_leap" >
            <span class="fr pr10">${fns:formatCNYUnit(deal.fentrustValue)}</span>
        </div>
        <div class="fl item_leap">
            <span class="fr pr30">${fns:formatCoinUnit(deal.volumn)}</span>
        </div>
        <div class="fl item_leap">
            <span class="fr pr25">${fns:formatCNYUnit(deal.fmarketValue)}</span>
        </div>
        <c:choose>
            <c:when test="${deal.fupanddown < 0}">
                <div class="fl item_leap c_green" >
                    <span class="fr pr35">${deal.fupanddown}%<i class="db fr pl5">↓</i></span>
                </div>
            </c:when>
            <c:otherwise>
                <div class="fl item_leap c_red" >
                    <span class="fr pr35">+${deal.fupanddown}%<i class="db fr pl5">↑</i></span>
                </div>
            </c:otherwise>
        </c:choose>
        <c:choose>
            <c:when test="${deal.fupanddownweek < 0}">
                <div class="fl item_leap c_green" >
                    <span class="fr pr35">${deal.fupanddownweek}%<i class="db fr pl5">↓</i></span>
                </div>
            </c:when>
            <c:otherwise>
                <div class="fl item_leap c_red" >
                    <span class="fr pr35">+${deal.fupanddownweek}%<i class="db fr pl5">↑</i></span>
                </div>
            </c:otherwise>
        </c:choose>
        <div class="fl item_leap trend" data-id="${deal.fid}" style="">
        </div>
        <div class="fl item_leap">
            <a href="/market/trade.html?symbol=${deal.fid}" class="bg_blue">去交易</a>
        </div>
    </div>
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