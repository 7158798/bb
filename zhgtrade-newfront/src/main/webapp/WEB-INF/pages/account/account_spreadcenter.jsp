<!-- 推广中心author:xxp 2016-04-24 -->
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
    <link rel="stylesheet" href="${resources}/static/css/account.css?v=2.0"/>
</head>
<body>
<c:set var="menu_index" value="4"/>
<%@include file="../common/header.jsp" %>
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
    <%@ include file="../common/account_left.jsp" %>
    <div class="account_right fl">
        <div class="fill_right account_spreadcenter">
            <div class="title">
                <ul id="title_lis" class="clear">
                    <li data-index="0" class="cur fl f16 fb db">推广中心</li>
                    <li class="fl li_separator"></li>
                    <li data-index="1" class="fl f16 fb db">成功推荐列表</li>
                    <li class="fl li_separator"></li>
                    <li data-index="2" class="fl f16 fb db">收益列表</li>
                    <li class="fl li_separator"></li>
                    <li data-index="3" class="fl f16 fb db">推广排行榜</li>
                </ul>
            </div>
            <div id="contents" class="content">
                <div class="li_item lasttenorder clear">
                    <div id="tuigang">
                        <ul class="share_content">
                            <li class="title">
                                邀请链接
                            </li>
                            <li class="mt5 share">
                                <input type="text" readonly value="${spreadLink}" class="pr5"/>
                                <span class="message c_green dn" id="message">复制成功</span>
                                <input type="hidden" id="content" value="${spreadLink}">
                                <a id="button"  data-clipboard-target="content" class="copy_btn bg_blue c_white" href="javascript:void(0);">点击复制</a>
                                <span class="txt c_gray f12 pl10">如果复制不成功，请手动复制链接内容</span>
                            </li>
                            <li class="title">
                                一键分享
                            </li>
                            <li class="mt5">
                                <p class="share_intro">
                                    你可以通过邀请朋友来扩大你的投资人脉，交流投资心得。可以将邀请链接发送给朋友或者分享到微信，QQ空间，新浪微博等社交平台，邀请朋友注册。
                                </p>
                            </li>
                            <div class="bdsharebuttonbox">
                                <a href="#" class="bds_more" data-cmd="more"></a>
                                <a href="#" class="bds_weixin" data-cmd="weixin" title="分享到微信"></a>
                                <a href="#" class="bds_qzone" data-cmd="qzone" title="分享到QQ空间"></a>
                                <a href="#" class="bds_tsina" data-cmd="tsina" title="分享到新浪微博"></a>
                            </div>
                        </ul>
                    </div>
                </div>
                <!-- 推广列表 -->
                <div class="li_item Tenbody mt20 dn"></div>
                <!-- 收益列表 -->
                <div class="li_item Tenbody mt20 dn"></div>
                <!-- 排行榜列表 -->
                <div class="li_item Tenbody mt20 dn"></div>
            </div>
        </div>
    </div>
    <div class="cb"></div>
</div>
<script type="text/javascript" src="${resources}/static/js/user/ZeroClipboard.min.js"></script>
<%@ include file="../common/footer.jsp" %>
<script type="text/javascript" src="${resources}/static/js/account/spreadcenter.js?v=2.0"></script>
</body>
</html>
