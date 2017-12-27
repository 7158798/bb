<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="../common/includes.jsp"%>
<style>
    li{cursor: auto;}
</style>
<div class="details">
    <div class="title b_l_blue pl10 f16">${type.fname}（${type.fShortName}）介绍</div>
    <div class="content">
        <%--<div class="fl">--%>
            <%--<img alt="${type.fname}" height="70" width="70" src="${cdn}${type.furl}" title="${type.fname}" />--%>
        <%--</div>--%>
        <div class="fl text pl20 f14">
            ${detail.desc}
        </div>
        <div class="cb"></div>
    </div>
    <div class="title b_l_blue pl10 f16">常用链接</div>
    <div class="content">
        <c:if test="${!empty detail.walletLink}"><a href="${detail.walletLink}" target="_blank" class="f16">钱包下载</a></c:if>
        <c:if test="${!empty detail.sourceLink}"><a href="${detail.sourceLink}" target="_blank" class="f16 ml20">源码下载</a></c:if>
        <c:if test="${!empty detail.whitePaperLink}"><a href="${detail.whitePaperLink}" target="_blank" class="f16 ml20">白皮书链接</a></c:if>
        <c:if test="${!empty detail.blockBrowserLink}"><a href="${detail.blockBrowserLink}" target="_blank" class="f16 ml20">区块浏览器</a></c:if>
    </div>
    <div class="title b_l_blue pl10 f16">详细参数</div>
    <div class="content">
        <table class="f14">
            <tr>
                <td>中文名：${detail.name}</td>
                <td>英文名：${detail.ename}</td>
                <td>英文简称：${type.fShortName}</td>
            </tr>
            <tr>
                <td>研发者：${detail.developers}</td>
                <td>核心算法：${detail.algorithm}</td>
                <td>发布日期：<fmt:formatDate value="${detail.releaseTime}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
            </tr>
            <tr>
                <td>区块时间：${detail.blocksTime}</td>
                <td>区块奖励：${detail.blocksReward}</td>
                <td>货币数量：${detail.amount}</td>
            </tr>
            <tr>
                <td colspan="3">主要特色：${detail.feature}</td>
            </tr>
            <tr>
                <td colspan="3">不足之处：${detail.defects}</td>
            </tr>
        </table>
    </div>
</div>