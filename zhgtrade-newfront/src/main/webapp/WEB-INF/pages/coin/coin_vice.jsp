<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="../common/includes.jsp" %>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>${fns:getProperty('site_title')}</title>
    <meta charset="utf-8">
    <meta content=always name=referrer>
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="keywords" content="${fns:getProperty('site_keywords')}">
    <meta name="description" content="${fns:getProperty('site_description')}">
    <%--<link rel="stylesheet" href="//cdn.bootcss.com/bootstrap/3.3.6/css/bootstrap.min.css">--%>
    <link rel="stylesheet" type="text/css" href="${resources}/static/css/common/reset.css">
    <link rel="stylesheet" type="text/css" href="${resources}/static/css/common/style.css">
    <link rel="stylesheet" href="${resources}/static/css/coin-main.css"/>
</head>
<body>

<c:set var="menu_index" value="2"/>
<%@include file="../common/header.jsp" %>

<div class="container-fluid m1 text-center" style="background: url('${resources}/static/images/coin/bg4.jpg');">
    <h1>创新区块链资产的纳斯达克</h1>
    <h3>
        <span class="line"></span>
        币三板=区块链资产的“新三板和创业板”
        <span class="line"></span>
    </h3>
</div>

<div class="container-fluid m10 text-center">
    <h1>新区块链资产挂牌流程</h1>
    <div><img src="${resources}/static/images/coin/gp.png"/></div>
</div>

<%@include file="coin.jsp"%>

<div class="m3 m11">
    <%@include file="bank.jsp"%>
</div>

<%@include file="zichan.jsp"%>

<%@include file="biaozhun.jsp"%>

<div class="m12 text-center" style="background: #FFF">
    <h1>转板及退市</h1>
    <div class="line"></div>
    <p>每月两大子板块的币种在各自板块按单独交易额排名</p>
    <div class="m13">
        <div class="d1">
            <h1>币三板</h1>
            <p>币三板排名前两名在转板日转入币主板</p>
        </div>
        <div class="line"></div>
        <div class="d2">
            <h1>币主板</h1>
            <p>币主板排名后两名在转板日转入币三板</p>
        </div>
    </div>
</div>

<div class="m14 text-center" style="background: url('${resources}/static/images/coin/bg5.jpg')">
    <h1>关于区块链项目退市的方案</h1>
    <p>
        请注意，为了用户的资金安全和正常投资，我们会慎重考虑区块链资产退市下架，但若在众股平台已上线的区块链资产（或数字货币）出现下列情况之一，我们会通知项目开发团队改进，如仍拒不改进或不作为， 我们将会提前公告并下线该区块链资产（或币种）。因下线后导致众股平台与其他平台用户的任何损失均由该项目开发团队承担。项目开发团队须与众股平台签署协议即表示充分了解众股平台下币规则，并自愿承担因此产生的所有风险。
    </p>
    <p>
        （1）官方团队解散，或者在超过15个工作日的时间里不再维护该区块链资产（或该币种）（不再维护包括但不限于以下几种情形：a该币种的官网平台关闭、无法打开、发布公告不再运营；b甲方以实际行为表明其不再运营该币种；c持币用户长期无法联系到该币种的团队；d运营团队对该币种钱包功能出现的问题明示不予解决或者超过15个工作日后仍未解决）；

    </p>
    <p>
        （2）该币种人气低迷，连续5日没有用户进行交易或者连续7日交易总额低于1000元人民币；
    </p>
    <p>
        （3）该币种钱包出现技术问题，该技术问题影响该数字货币的交易与运营；
    </p>
    <p>
        （4）众股平台认为该币种开发团队涉嫌恶意操盘砸盘等欺诈行为的，众股平台有权采取查封涉嫌恶意操盘砸盘的用户注册号、冻结用于恶意操盘砸盘操作的资金帐户、暂停该币种交易、向国家有权管理的机关举报和投诉以及对该数字货币予以下线处理等措施；如众股平台或者其平台用户因该币种官方团队存在本条约定之行为给用户造成损失的，众股平台将从所冻结的资金帐户中的资金直接用于弥补各方损失，不足部分众股平台及用户有权向该币种官方团队进行追索。
    </p>
    <p>
        （5）有超过80%的持有该数字货币用户（帐户数或者持币数量二者有一项满足80%以上的标准即可）强烈要求下线该币种或者其它由于该币种而影响众股平台声誉的情况。
    </p>
</div>

<%@include file="remark.jsp"%>
<%@include file="../common/footer_edg.jsp"%>
</body>
</html>
