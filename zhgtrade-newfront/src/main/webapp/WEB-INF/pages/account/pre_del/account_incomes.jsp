<!-- 推广中心author:xxp 2016-04-24 -->
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="../../common/includes.jsp" %>
<!DOCTYPE HTML>
<html>
<head>
    <title>比特家 - 推广收益</title>
    <meta http-equiv=X-UA-Compatible content="IE=edge,chrome=1">
    <meta content=always name=referrer>
    <meta name='renderer' content='webkit' />
    <meta name="keywords" content="${requestScope.constant['webinfo'].fdescription }">
    <meta name="description" content="${requestScope.constant['webinfo'].fkeywords }">
    <link rel="icon" href="/favicon.ico"/>
    <link rel="shortcut icon" href="/favicon.ico"/>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8"/>
    <link rel="stylesheet" type="text/css" href="${resources}/static/css/common/reset.css">
    <link rel="stylesheet" type="text/css" href="${resources}/static/css/common/style.css">
    <link rel="stylesheet" href="${resources}/static/css/common/animate.css"/>
    <link rel="stylesheet" href="${resources}/static/css/account.css" />
</head>
<body>
<c:set var="menu_index" value="4"/>
<%@include file="../../common/header.jsp" %>
<div class="center_page">
    <div class="account_nav">
        <a href="/" class="f12 c_blue">首页</a>
        <i class="yjt">&gt;</i>
        <a href="/account/security.html" class="f12 c_blue">基本设置</a>
        <i class="yjt">&gt;</i>
        <a href="javascript:void(0)" class="f12 c_gray">推广中心</a>
    </div>
    <c:set var="dt_index" value="2"/>
    <c:set var="dd_index" value="10"/>
    <%@ include file="../../common/account_left.jsp" %>
    <div class="account_right fl">
        <div class="fill_right account_spreadcenter">
            <div class="title">
                <ul id="title" class="clear">
                    <li onclick="window.location.href = '/account/spread.html'" class="<c:if test="${1 == type}">cur</c:if> fl f16 fb">推广中心</li>
                    <li class="fl li_separator"></li>
                    <li onclick="window.location.href = '/account/intros.html'" class="<c:if test="${2 == type}">cur</c:if> fl f16 fb">成功推荐列表</li>
                    <li class="fl li_separator"></li>
                    <li class="<c:if test="${3 == type}">cur</c:if> fl f16 fb">收益列表</li>
                    <li class="fl li_separator"></li>
                    <li onclick="window.location.href = '/account/topn.html'" class="<c:if test="${4 == type}">cur</c:if> fl f16 fb">推广排行榜</li>
                </ul>
            </div>
            <div class="content mt20">
                <div class="Tenbody">
                    <table>
                        <colgroup>
                            <col width="50" />
                            <col width="50" />
                        </colgroup>
                        <tr>
                            <th class="">收益内容</th>
                            <th>创建时间</th>
                        </tr>
                        <c:if test="${empty incomes || 0 == fn:length(incomes)}">
                            <tr>
                                <td colspan="2">您暂时没有收益记录</td>
                            </tr>
                        </c:if>
                        <c:forEach items="${incomes}" var="in">
                            <tr>
                                <td class="">${in.ftitle}</td>
                                <td><fmt:formatDate value="${in.fcreatetime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                            </tr>
                        </c:forEach>
                    </table>
                    <div class="page">
                        <ul>
                            ${in_pagin}
                        </ul>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <div class="cb"></div>
</div>
<%@ include file="../../common/footer.jsp" %>
</body>
</html>
