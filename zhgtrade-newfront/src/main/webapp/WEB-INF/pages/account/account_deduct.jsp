<!-- 账单明细页面author:xxp 2016-04-23 -->
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
</head>
<body>
<c:set var="menu_index" value="4"/>
<%@include file="../common/header.jsp" %>
<div class="center_page">
    <div class="account_nav">
        <a href="/" class="f12 c_blue">首页</a>
        <i class="yjt">&gt;</i>
        <a href="/account/fund.html" class="f12 c_blue">财务中心</a>
        <i class="yjt">&gt;</i>
        <a href="javascript:void(0)" class="f12 c_gray">账单明细</a>
    </div>
    <c:set var="dt_index" value="1"/>
    <c:set var="dd_index" value="6"/>
    <%@ include file="../common/account_left.jsp" %>
    <div class="account_right fl">
        <div class="finance_wrapper">
            <div class="title">
                <ul class="clear">
                    <li id="bills_li" class="fl f16 fb">账单明细</li>
                    <li id="deduct_li" class="cur fl f16 fb">提成明细</li>
                </ul>
            </div>
            <div class="content">
                <div class="tm_account" style="padding: 0px" id="tm_deduct">
                    <div class="Tenbody">
                        <table>
                            <colgroup>
                                <col width="20%">
                                <col width="15%">
                                <col width="10%">
                                <col width="10%">
                                <col width="15%">
                                <col width="20%">
                            </colgroup>
                            <tr>
                                <th>结算日期</th>
                                <th>结算区间</th>
                                <th>总人民币金额</th>
                                <th>FC总数量</th>
                                <th>状态</th>
                                <th>创建时间</th>
                            </tr>
                            <c:forEach items="${deducts }" var="v">
                                <tr>
                                    <td><fmt:formatDate value="${v.fchargeDate }" pattern="yyyy-MM-dd"/>
                                    </td>
                                    <td>${v.fchargesection.fstartHour }-${v.fchargesection.fendHour }</td>
                                    <td class="c_red">￥<fmt:formatNumber value="${v.ftotalAmt }"
                                                                       pattern="##.##" maxIntegerDigits="10"
                                                                       maxFractionDigits="4"/>
                                    </td>
                                    <td class="c_red">D<fmt:formatNumber value="${v.ftotalQty }"
                                                                       pattern="##.##" maxIntegerDigits="10"
                                                                       maxFractionDigits="4"/>
                                    </td>
                                    <c:choose>
                                        <c:when test="${1 == v.fstatus || 2 == v.fstatus}">
                                            <td class="c_red">${v.fstatus_s}</td>
                                        </c:when>
                                        <c:otherwise>
                                            <td class="c_green">${v.fstatus_s}</td>
                                        </c:otherwise>
                                    </c:choose>
                                    <td><fmt:formatDate value="${v.fcreatetime }" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                                </tr>
                            </c:forEach>
                            <c:if test="${fn:length(deducts)==0 }">
                                <td colspan="6" class="gray">暂无提成记录！</td>
                            </c:if>
                        </table>
                        <div class="page">
                            <ul>
                                ${pagin }
                            </ul>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <div class="cb"></div>
</div>
<script>
    $(function () {
        $("#bills_li").click(function () {
            window.location.href = "/account/bills.html";
        });
    });
</script>
<%@ include file="../common/footer.jsp" %>
</body>
</html>
