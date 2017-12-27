<!-- 账单明细页面author:yujie 2016-04-23 -->
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
            <%--<div class="title">
                <ul class="clear">
                    <li id="bills_li" class="cur fl f16 fb">账单明细</li>
                    <li id="deduct_li" class="fl f16 fb">提成明细</li>
                </ul>
            </div>--%>
            <h1 class="ml40">账单明细</h1>
            <div class="content">
                <div class="tm_account" style="padding: 0px" id="tm_bills">
                    <form action="/account/bills.html" method="get" class="sel_box" id="recordFilterForm">
                        <select onchange="javascript:recordFilter();" id="recordType">
                            <c:forEach items="${filters }" var="v">
                                <c:choose>
                                    <c:when test="${select==v.value}">
                                        <option value="${v.key }" selected="selected">${v.value }</option>
                                    </c:when>
                                    <c:otherwise>
                                        <option value="${v.key }">${v.value }</option>
                                    </c:otherwise>
                                </c:choose>
                            </c:forEach>
                        </select>
                    </form>
                    <div class="Tenbody">
                        <table>
                            <colgroup>
                                <col width="160"/>
                                <col width="110"/>
                                <col width="130"/>
                                <col width="85"/>
                                <col width="165"/>
                                <col width="110"/>
                                <col width="110"/>
                            </colgroup>
                            <tr>
                                <th>交易时间</th>
                                <th>类型</th>
                                <th>FC数量</th>
                                <th>手续费</th>
                                <th>人民币数量</th>
                                <th>平均成交价</th>
                                <th>状态</th>
                            </tr>
                            <c:if test="${empty list || 0 == fn:length(list)}">
                                <tr>
                                    <td colspan="7">您暂时没有充值数据</td>
                                </tr>
                            </c:if>
                            <c:choose>
                                <c:when test="${recordType==1 || recordType==2 }">
                                    <%--人民币充值、提现--%>
                                    <c:forEach items="${list }" var="v">
                                        <tr>
                                            <td><fmt:formatDate value="${v.fcreateTime }" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                                            <td>${select}</td>
                                            <td class="c_red"></td>
                                            <td>￥<fmt:formatNumber value="${v.ffees }" pattern="#.##"/></td>
                                            <td class="c_red">￥<fmt:formatNumber value="${v.famount }" pattern="##.##"/></td>
                                            <td></td>
                                            <c:choose>
                                                <c:when test="${1 == v.fstatus || 2 == v.fstatus}">
                                                    <td class="c_red">${v.fstatus_s}</td>
                                                </c:when>
                                                <c:when test="${3 == v.fstatus}">
                                                    <td class="c_green">${v.fstatus_s}</td>
                                                </c:when>
                                                <c:otherwise>
                                                    <td class="c_gray">${v.fstatus_s}</td>
                                                </c:otherwise>
                                            </c:choose>
                                        </tr>
                                    </c:forEach>
                                </c:when>
                                <c:when test="${recordType==3 || recordType==4 }">
                                    <%--虚拟币充值、提现--%>
                                    <c:forEach items="${list }" var="v">
                                        <tr>
                                            <td><fmt:formatDate value="${v.fcreateTime }"
                                                                pattern="yyyy-MM-dd HH:mm:ss"/></td>
                                            <td>${select}</td>
                                            <td class="c_red">${v.fvirtualcointype.fSymbol }<fmt:formatNumber value="${v.famount }" pattern="##.####"/></td>
                                            <td class="c_red">${v.fvirtualcointype.fSymbol }${v.ffees }</td>
                                            <td></td>
                                            <td></td>
                                            <c:choose>
                                                <c:when test="${1 == v.fstatus || 2 == v.fstatus}">
                                                    <td class="c_red">${v.fstatus_s}</td>
                                                </c:when>
                                                <c:when test="${3 == v.fstatus}">
                                                    <td class="c_green">${v.fstatus_s}</td>
                                                </c:when>
                                                <c:otherwise>
                                                    <td class="c_gray">${v.fstatus_s}</td>
                                                </c:otherwise>
                                            </c:choose>
                                        </tr>
                                    </c:forEach>
                                </c:when>
                                <c:otherwise>
                                    <%--交易--%>
                                    <c:forEach items="${list }" var="v">
                                        <tr>
                                            <td><fmt:formatDate value="${v.fcreateTime }"
                                                                pattern="yyyy-MM-dd HH:mm:ss"/></td>
                                            <td>${select }</td>
                                            <td class="c_red">${v.fvirtualcointype.fSymbol }<fmt:formatNumber value="${v.fcount }" pattern="##.####" /></td>
                                            <td class="c_red">￥<fmt:formatNumber value="${v.ffees}" pattern="##.##"/></td>
                                            <td>￥<fmt:formatNumber value="${v.famount}" pattern="#.####" /></td>
                                            <td>
                                                ￥<fmt:formatNumber
                                                    value="${((v.fcount-v.fleftCount)==0)?0:(v.famount/(v.fcount-v.fleftCount)) }"
                                                    pattern="##.##" maxIntegerDigits="10" maxFractionDigits="4"/>
                                            </td>
                                            <c:choose>
                                                <c:when test="${1 == v.fstatus}">
                                                    <td class="c_red">${v.fstatus_s}</td>
                                                </c:when>
                                                <c:when test="${2 == v.fstatus || 3 == v.fstatus}">
                                                    <td class="c_green">${v.fstatus_s}</td>
                                                </c:when>
                                                <c:otherwise>
                                                    <td class="c_gray">${v.fstatus_s}</td>
                                                </c:otherwise>
                                            </c:choose>
                                        </tr>
                                    </c:forEach>
                                </c:otherwise>
                            </c:choose>
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
        $("#choose").click(function () {
            var flag = $(this).data("flag");
            var $this = $(this);
            if (flag) {
                $this.data("flag", 0);
                $this.html("&#xe602;");
            } else {
                $this.data("flag", 1);
                $this.html("&#xe600;");
            }
        });
       /* $("#deduct_li").click(function () {
            window.location.href = "/account/deduct.html";
        });*/
    });

    function recordFilter() {
        var url = document.getElementById("recordType").value;
        window.location.href = url;
    }
</script>
<%@ include file="../common/footer.jsp" %>
</body>
