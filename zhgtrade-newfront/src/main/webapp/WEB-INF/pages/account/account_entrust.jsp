<!-- 委托管理页面author:xxp 2016-04-23 -->
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
    <style>
        .sel_w85{width: 85px;}
        .sel_w120{width: 120px;}
        .sel_w130{width: 130px;}
    </style>
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
        <a href="javascript:void(0)" class="f12 c_gray">委托管理</a>
    </div>
    <c:set var="dt_index" value="1"/>
    <c:set var="dd_index" value="7"/>
    <%@ include file="../common/account_left.jsp" %>
    <div class="account_right fl">
        <div class="finance_wrapper">
            <div class="finance_container">
                <h1 class="ml40">委托管理</h1>
                <div class="content">
                    <form id="form" class="ml35" action="/account/entrusts.html">
                        <span id="symbol" class="selector fl">
                            <span class="selector_item">
                                <span class="sel_item">虚拟币类型</span>
                                <input type="hidden" name="symbol" value="${symbol}">
                            </span>
                            <span class="selector_items dn">
                                <c:forEach items="${coins}" var="coin">
                                    <c:if test="${1 == coin.fstatus}">
                                        <a data-val="${coin.fid}" <c:if test="${symbol == coin.fid}">class="cur"</c:if>>${coin.fname}(${coin.fShortName})</a>
                                    </c:if>
                                </c:forEach>
                            </span>
                        </span>
                        <span id="entrustType" class="selector fl ml5">
                            <span class="selector_item">
                                <span class="sel_item">全部</span>
                                <input type="hidden" name="type" value="${type}">
                            </span>
                            <span class="selector_items dn">
                                <a <c:if test="${-1 == type}">class="cur"</c:if> data-val="-1">全部</a>
                                <a <c:if test="${0 == type}">class="cur"</c:if> data-val="0">买入</a>
                                <a <c:if test="${1 == type}">class="cur"</c:if> data-val="1">卖出</a>
                            </span>
                        </span>
                        <span id="status" class="selector fl ml5">
                            <span class="selector_item">
                                <span class="sel_item">全部</span>
                                <input type="hidden" name="status" value="${status}">
                            </span>
                            <span class="selector_items dn">
                                <a <c:if test="${-1 == status}">class="cur"</c:if> data-val="-1">全部</a>
                                <a <c:if test="${1 == status}">class="cur"</c:if> data-val="1">未成交</a>
                                <a <c:if test="${2 == status}">class="cur"</c:if> data-val="2">部分成交</a>
                                <a <c:if test="${3 == status}">class="cur"</c:if> data-val="3">完全成交</a>
                                <a <c:if test="${4 == status}">class="cur"</c:if> data-val="4">已撤销</a>
                            </span>
                        </span>
                        <input id="currentPage" type="hidden" name="currentPage">
                        <input id="total" type="hidden" name="total" value="${total}">
                        <%--<input type="text" class="f1 ml40" style="height: 30px; width: 70px;" name="startDate" value="${param.startDate}" readonly>至
                        <input type="text" class="f1 ml40" style="height: 30px; width: 70px;" name="endDate" value="${param.endDate}" readonly>
                        <a class="bg_blue">查询</a>--%>
                    </form>
                    <div class="Tenbody" id="entrustList">
                        <table>
                            <colgroup>
                                <col width="130"/>
                                <col width="60"/>
                                <col width="120"/>
                                <col width="100"/>
                                <col width="50"/>
                                <col width="80"/>
                                <col width="100"/>
                                <col width="100"/>
                                <col width="80"/>
                                <col width="75"/>
                            </colgroup>
                            <tr>
                                <th>委托时间</th>
                                <th>委托类别</th>
                                <th>委托数量</th>
                                <th>委托金额</th>
                                <th>手续费</th>
                                <th>委托价格</th>
                                <th>成交数量</th>
                                <th>成交金额</th>
                                <th>平均成交价</th>
                                <th>状态/操作</th>
                            </tr>
                            <c:if test="${empty entrusts || 0 == fn:length(entrusts)}">
                                <tr><td colspan="10">您暂时没有该币种的委托单</td></tr>
                            </c:if>
                            <c:forEach items="${entrusts}" var="e">
                                <tr>
                                    <td><fmt:formatDate value="${e.fcreateTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                                    <td class="<c:choose><c:when test="${0 == e.fentrustType}">c_red</c:when><c:otherwise>c_green</c:otherwise></c:choose>">${e.fentrustType_s}</td>
                                    <td><span class="fl pl15">${fns:formatCoin(e.fcount)}</span></td>
                                    <td><span class="fl pl15">￥${fns:formatCNY(e.famount)} </span></td>
                                    <td><span class="fl pl10">${fns:formatCNY(e.ffees - e.fleftfees)}</span></td>
                                    <td><span class="fl pl15 <c:choose><c:when test="${0 == e.fentrustType}">c_red</c:when><c:otherwise>c_green</c:otherwise></c:choose>">￥${fns:formatCoin(e.fprize)}</span></td>
                                    <td><span class="fl pl20">${fns:formatCoin(e.fcount - e.fleftCount)}</span></td>
                                    <td><span class="fl pl15">￥${fns:formatCNY(e.fsuccessAmount)}</span></td>
                                    <td>
                                        <span class="fl pl10">
                                            <c:choose>
                                                <c:when test="${empty avgs[e.fid]}">￥0</c:when>
                                                <c:otherwise>
                                                    ￥${fns:formatCoin(avgs[e.fid])}
                                                </c:otherwise>
                                            </c:choose>
                                        </span>
                                    </td>
                                    <c:choose>
                                        <c:when test="${1 == e.fstatus || 2 == e.fstatus}">
                                            <td><a style="text-decoration: none;" data-id="${e.fid}" class="c_orange cancel_btn" href="javascript:void(0);">取消</a></td>
                                        </c:when>
                                        <c:when test="${3 == e.fstatus}">
                                            <td class="c_green">${e.fstatus_s}</td>
                                        </c:when>
                                        <c:otherwise>
                                            <td class="c_gray">${e.fstatus_s}</td>
                                        </c:otherwise>
                                    </c:choose>
                                </tr>
                            </c:forEach>
                        </table>
                        <page:pagination pageSzie="${pageSize}" pageNow="${pageNow}" total="${total}" href="/account/entrusts.html?currentPage=#pageNumber&symbol=${symbol}&type=${type}&status=${status}"/>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <div class="cb"></div>
</div>
<script  src="${resources}/static/js/utils/selector.js"></script>
<script>
    $(function () {
        $("#symbol").custom_selector({onChange : function(){
            $("#currentPage").val("1");
            $("#total").val("0");
            $("#form").submit();
        }, boxClass : "selector_width150"});
        $("#symbol, #entrustType, #status").custom_selector({onChange : function(){
            $("#currentPage").val("1");
            $("#total").val("0");
            $("#form").submit();
        }});
        $(".sel_box").on("change", function () {
            $("#currentPage").val("1");
            $("#total").val("0");
            $("#form").submit();
        });
        $("#pager ul li").on("click", function () {
            var _this = $(this);
            var page = _this.find("a").attr("href").split("\?")[1].replace("currentPage=", "");
            $("#currentPage").val(page);
            $("#form").submit();
            return false;
        });
        $("#entrustList table a").on("click", function(){
            $.post("/market/cancelEntrust.html", {id : $(this).data("id")}, function(data){
                window.location.reload(true);
            }, "json");
        });
    });
</script>
<%@ include file="../common/footer.jsp" %>
</body>
</html>
