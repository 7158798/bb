<!-- 人民币充值页面author:xxp 2016-04-23 -->
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
        .tm_title .close:hover{color: #F00;}
        .banks i{width: 125px;height:40px;background-color: #fff;border: 1px solid #ccc;}
        .sel_bank i{width: 125px;height:40px;background-color: #fff;}
        .sel_bank{padding: 0px 11px;border: 1px solid #ccc;}
        .sel_bank_hover{position:relative;background-color: #fff;z-index: 6;border-left: 1px solid #ccc;border-right: 1px solid #ccc;border-top: 1px solid #ccc;border-bottom: 0px;}
        .banks i:hover{border: 1px solid #FE741F;}
        .banks .cur{border: 1px solid #FE741F;}
        .banks .cur:after{content: '\e624';color: #FE741F;font-size: 20px;float: right;margin-right: -3px;margin-top:22px;font-family: iconfont;font-style: normal;}
        .bank_bocsh{background: url("${resources}/static/images/account/bank.jpg") no-repeat 0 2px;}
        .bank_abc{background: url("${resources}/static/images/account/bank.jpg") no-repeat 0 -32px;}
        .bank_icbc{background: url("${resources}/static/images/account/bank.jpg") no-repeat 0 -66px;}
        .bank_ccb{background: url("${resources}/static/images/account/bank.jpg") no-repeat 0 -102px;}
        .bank_bocom{background: url("${resources}/static/images/account/bank.jpg") no-repeat 0 -137px;}
        .bank_cncb{background: url("${resources}/static/images/account/bank.jpg") no-repeat 0 -173px;}
        .bank_hxb{background: url("${resources}/static/images/account/bank.jpg") no-repeat 0 -208px;}
        .bank_cmb{background: url("${resources}/static/images/account/bank.jpg") no-repeat 0 -242px;}
        .bank_cib{background: url("${resources}/static/images/account/bank.jpg") no-repeat 0 -278px;}
        .bank_ceb{background: url("${resources}/static/images/account/bank.jpg") no-repeat 0 -313px;}
        .bank_spdb{background: url("${resources}/static/images/account/bank.jpg") no-repeat 0 -345px;}
        .bank_psbc{background: url("${resources}/static/images/account/bank.jpg") no-repeat 0 -558px;}
        .bank_cmbc{background: url("${resources}/static/images/account/bank.jpg") no-repeat 0 -380px;}
        .bank_njcb{background: url("${resources}/static/images/account/bank.jpg") no-repeat -8px -417px;}
        .bank_cbhb{background: url("${resources}/static/images/account/bank.jpg") no-repeat -6px -453px;}
        .bank_brcb{background: url("${resources}/static/images/account/bank.jpg") no-repeat 0 -487px;}
        .bank_hzcb{background: url("${resources}/static/images/account/bank.jpg") no-repeat -8px -524px;}
        .bank_gdb{background: url("${resources}/static/images/account/bank.jpg") no-repeat 0 -592px;}
        .bank_pab{background: url("${resources}/static/images/account/bank.jpg") no-repeat -8px -626px;}
        .bank_bos{background: url("${resources}/static/images/account/bank.jpg") no-repeat -9px -662px;}
        .bank_nbbank{background: url("${resources}/static/images/account/bank.jpg") no-repeat -9px -697px;}
        .bank_fdb{background: url("${resources}/static/images/account/bank.jpg") no-repeat -15px -732px;}
        .bank_bccb{background: url("${resources}/static/images/account/bank.jpg") no-repeat 0 -767px;}
        .bank_srcb{background: url("${resources}/static/images/account/bank.jpg") no-repeat 0 -802px;}
        .bank_hfcbcnsh{background: url("${resources}/static/images/account/bank.jpg") no-repeat 0 -837px;}
        .bank_czbank{background: url("${resources}/static/images/account/bank.jpg") no-repeat -6px -907px;}

        .banks_box{position:absolute;left:200px;top:124px;width: 538px;z-index: 5;background-color: #FFF;padding: 10px;border: 1px solid #ccc;}
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
        <a href="javascript:void(0)" class="f12 c_gray">人民币充值</a>
    </div>
    <c:set var="dt_index" value="1"/>
    <c:set var="dd_index" value="2"/>
    <%@ include file="../common/account_left.jsp"%>
    <!-- 人民币充值开始 -->
    <div id="container" class="account_right fl">
        <div class="finance_wrapper">
            <div class="finance_container">
                <!-- <h1 class="ml40">人民币充值</h1> -->
                <div class="title">
                    <ul id="title_lis" class="clear">
                        <%--<li id="ebank_li" data-index="0" class="db fl f16 fb <c:if test="${empty type || 0 == type}">cur</c:if>"><a data-pjax="#container" href="/account/chargermb.html?type=0">在线网银充值</a></li>--%>
                        <%--<li class="fl li_separator"></li>--%>
                        <li id="weixin_li" data-index="4" class="db fl f16 fb <c:if test="${0 == type || 4 == type}">cur</c:if>"><a data-pjax="#container" href="/account/chargermb.html?type=4">微信扫码充值（秒到）</a></li>
                        <li class="fl li_separator"></li>
                        <li id="ali_li" data-index="1" class="db fl f16 fb <c:if test="${1 == type}">cur</c:if>"><a data-pjax="#container" href="/account/chargermb.html?type=1">手动支付宝充值</a></li>
                        <li class="fl li_separator"></li>
                        <li id="bank_li" data-index="2" class="db fl f16 fb <c:if test="${2 == type}">cur</c:if>"><a data-pjax="#container" href="/account/chargermb.html?type=2">手动银行卡充值</a></li>
                        <li class="fl li_separator"></li>
                        <li data-index="3" class="db fl f16 fb <c:if test="${3 == type}">cur</c:if>"><a data-pjax="#container" href="/account/chargermb.html?type=3">Bithome转账</a></li>
                    </ul>
                </div>
                <input type="hidden" id="minMoney" value="<fmt:formatNumber value="${minRecharge}" pattern="#.##" />" />
                <input type="hidden" id="maxRMB" value="<fmt:formatNumber value="${maxRMB}" pattern="#.##" />" />
                <div id="contents" class="content mt20">
                    <!-- 网上银行 -->
                    <%--<div class="tm_account <c:if test="${!empty type && 0 != type}">dn</c:if>">--%>
                        <%--<form action="/epay/ebank.html" method="post" target="_blank">--%>
                            <%--<p class="tm_money">--%>
                                <%--<span class="db fl fir">充值金额： </span>--%>
                                <%--<input id="ebank_money_input" name="amount" type="text" placeholder="<fmt:formatNumber value="${minRecharge}" pattern="#.##" />"--%>
                                       <%--onkeyup="this.value=this.value.replace(/\D/g,'')" data-minval="<fmt:formatNumber value="${minRecharge}" pattern="#.##" />" class="db fl"/>--%>
                                <%--<span class="pl15 db fl">最小充值金额<fmt:formatNumber value="${minRecharge}" pattern="#.##" />元</span>--%>
                            <%--</p>--%>
                            <%--<p class="tm_bank">--%>
                                <%--<input id="ebank_input" type="hidden" name="bank" value="CMB">--%>
                                <%--<span class="db fl fir">付款银行： </span>--%>
                                <%--<span id="sel_bank" class="fl sel_bank">--%>
                                    <%--<i id="seled_ebank" class="bank_cmb db pointer"></i>--%>
                                <%--</span>--%>
                            <%--</p>--%>
                            <%--<div id="banks_list" class="banks banks_box dn">--%>
                                <%--<i data-bank="BOCSH" title="中国银行" class="bank_bocsh db pointer fl"></i>--%>
                                <%--<i data-bank="ABC" title="农业银行" class="bank_abc db pointer fl ml10"></i>--%>
                                <%--<i data-bank="ICBC" title="工商银行" class="bank_icbc db pointer fl ml10"></i>--%>
                                <%--<i data-bank="CCB" title="建设银行" class="bank_ccb db pointer fl ml10"></i>--%>
                                <%--<i data-bank="BOCOM" title="交通银行" class="bank_bocom db pointer fl mt10"></i>--%>
                                <%--<i data-bank="CNCB" title="中信银行" class="bank_cncb db pointer fl ml10 mt10"></i>--%>
                                <%--<i data-bank="HXB" title="华夏银行" class="bank_hxb db pointer fl ml10 mt10"></i>--%>
                                <%--<i data-bank="CMB" title="招商银行" class="bank_cmb db pointer fl ml10 mt10 cur"></i>--%>
                                <%--<i data-bank="CIB" title="兴业银行" class="bank_cib db pointer fl mt10"></i>--%>
                                <%--<i data-bank="CEB" title="光大银行" class="bank_ceb db pointer fl ml10 mt10"></i>--%>
                                <%--<i data-bank="SPDB" title="上海浦东发展银行" class="bank_spdb db pointer fl ml10 mt10"></i>--%>
                                <%--<i data-bank="PSBC" title="邮政储蓄银行" class="bank_psbc db pointer fl ml10 mt10"></i>--%>
                                <%--<i data-bank="CMBC" title="民生银行" class="bank_cmbc db pointer fl mt10"></i>--%>
                                <%--<i data-bank="NJCB" title="南京银行" class="bank_njcb db pointer fl ml10 mt10"></i>--%>
                                <%--<i data-bank="CBHB" title="渤海银行" class="bank_cbhb db pointer fl ml10 mt10"></i>--%>
                                <%--<i data-bank="BRCB" title="北京农村商业银行" class="bank_brcb db pointer fl ml10 mt10"></i>--%>
                                <%--<i data-bank="HZCB" title="杭州银行" class="bank_hzcb db pointer fl mt10"></i>--%>
                                <%--<i data-bank="GDB" title="广发银行" class="bank_gdb db pointer fl ml10 mt10"></i>--%>
                                <%--<i data-bank="PAB" title="平安银行" class="bank_pab db pointer fl ml10 mt10"></i>--%>
                                <%--<i data-bank="BOS" title="上海银行" class="bank_bos db pointer fl ml10 mt10"></i>--%>
                                <%--<i data-bank="NBBANK" title="宁波银行" class="bank_nbbank db pointer fl mt10"></i>--%>
                                <%--<i data-bank="FDB" title="富滇银行" class="bank_fdb db pointer fl ml10 mt10"></i>--%>
                                <%--<i data-bank="BCCB" title="北京银行" class="bank_bccb db pointer fl ml10 mt10"></i>--%>
                                <%--<i data-bank="SRCB" title="上海农村商业银行" class="bank_srcb db pointer fl ml10 mt10"></i>--%>
                                <%--<i data-bank="HFCBCNSH" title="微商银行" class="bank_hfcbcnsh db pointer fl mt10"></i>--%>
                                <%--<i data-bank="CZBANK" title="浙商银行" class="bank_czbank db pointer fl ml10 mt10"></i>--%>
                            <%--</div>--%>
                            <%--<a id="ebank_fir_confirm" href="javascript:void(0);" class="tm_confirm">确认充值</a>--%>
                        <%--</form>--%>
                        <%--<div class="info f12 mt20">--%>
                            <%--<p class="pl40 c_gray" style="line-height: 20px;height: 20px;margin-top: 0px;">温馨提示：</p>--%>
                            <%--<p class="pl40 c_gray pr40" style="line-height: 20px;height: 20px;margin-top: 0px;">--%>
                                <%--1、在线充值即时到账，免手续费；--%>
                            <%--</p>--%>
                            <%--<p class="pl40 c_gray pr40" style="line-height: 20px;height: 20px;margin-top: 0px;">--%>
                                <%--2、充值未到账请联系网站右侧QQ客服。--%>
                            <%--</p>--%>
                        <%--</div>--%>
                    <%--</div>--%>
                    <!-- 扫码支付 -->
                    <div class="tm_account <c:if test="${0 != type && 4 != type}">dn</c:if>">
                        <form action="/account/wechatScanCodePay.html" method="post" >
                            <p class="tm_money">
                                <span class="db fl fir">充值金额： </span>
                                <input autocomplete="off" id="weixin_money_input" name="amount" type="text" placeholder="<fmt:formatNumber value="${minRecharge}" pattern="#.##" />"
                                        data-minval="<fmt:formatNumber value="${minRecharge}" pattern="#.##" />" class="db fl"/>
                                <span class="pl15 db fl">最小充值金额<fmt:formatNumber value="${minRecharge}" pattern="#.##" />元</span>
                            </p>
                            <div class="tm_money_total c_blue">
                                <div class="tm_money_info fl">
                                    <span class="ml5">实际到账：</span>
                                    <span id="weixin_cut_fee" class="f16">0</span>
                                </div>
                                <div class="pl15 fl c_333 f12" style="line-height:40px;">手续费0.5%</div>
                                <div class="cb"></div>
                            </div>
                            <a id="weixin_fir_confirm" href="javascript:void(0);" class="tm_confirm">确认充值</a>
                        </form>
                        <div class="info f12 mt20">
                            <p class="pl40 c_gray" style="line-height: 20px;height: 20px;margin-top: 0px;">温馨提示：</p>
                            <p class="pl40 c_gray pr40" style="line-height: 20px;height: 20px;margin-top: 0px;">
                                1、扫码充值即时到账，手续费0.5%，由第三方收取；
                            </p>
                            <p class="pl40 c_gray pr40" style="line-height: 20px;height: 20px;margin-top: 0px;">
                                2、充值未到账请联系网站右侧QQ客服。
                            </p>
                        </div>
                    </div>
                    <!-- 支付宝支付 -->
                    <div class="tm_account <c:if test="${1 != type}">dn</c:if>" id="tm_alipay">
                        <p style="display: none;">
                            <span class="db fl fir">汇入账户类型： </span>
                            <span class="selector fl">
                                <span class="selector_item">
                                    <span>汇入账户</span>
                                    <input type="hidden" id="ali_bank_type" name="bankType" value="">
                                </span>
                                <span class="selector_items dn">
                                    <c:forEach items="${bankInfo}" var="bank">
                                        <c:if test="${fn:indexOf(bank.fbankName, '支付宝') > -1}"><a data-val="${bank.fid}" >${bank.fownerName}-${bank.fbankName}</a></c:if>
                                    </c:forEach>
                                </span>
                            </span>
                        </p>
                        <p class="tm_money">
                            <span class="db fl fir">充值金额： </span>
                            <input id="ali_money_input" type="text" placeholder="<fmt:formatNumber value="${minRecharge}" pattern="#.##" />" onpaste="this.value=this.value.replace(/\D/g,'')" onkeyup="this.value=this.value.replace(/\D/g,'')"  class="db fl" />
                            <span class="pl15 db fl">最小充值金额<fmt:formatNumber value="${minRecharge}" pattern="#.##" />元</span>
                            <input type="hidden" id="ali_minMoney" value="<fmt:formatNumber value="${minRecharge}" pattern="#.##" />" />
                        </p>
                        <div class="tm_money_total c_blue">
                            <div class="tm_money_info fl">
                                <span>汇款金额：</span>
								<span id="ali_random_money">
									<span class="f20" id="ali_money_show">0</span>.${random}
								</span>
                            </div>
                            <div class="c_red pl15 fl" style="line-height:40px;">请严格按照上述金额汇款，包括小数点后两位</div>
                            <div class="cb"></div>
                        </div>
                        <p class="tm_name">
                            <span class="db fl fir">汇款人姓名：</span>
                            <input class="db fl" type="text"  id="ali_payee" value="${sessionScope.login_user.frealName }"/>
                            <span class="c_red pl15 db fl">请确保汇款人姓名与此一致</span>
                        </p>
                        <p class="tm_bank_num">
                            <span class="db fl fir">支付宝账号：</span>
                            <input class="db fl" type="text" id="ali_bank_num" />
                            <span class="c_red pl15 db fl">请准确填写支付宝账号</span>
                        </p>
                        <a id="ali_fir_confirm" href="javascript:void(0);" class="tm_confirm" >确认充值</a>
                        <input type="hidden" value="6" name="type" id="ali_finType">
                        <%--<p class="tm_info ">新用户可以前往Bithome注册用户并绑定比特家用户，在线充值后，提现到比特家平台。<a href="/guide/article.html?id=34&currentPage=1&pageIndex=5" target="_blank" class="c_blue">在线充值教程链接</a></p>--%>
                        <div class="info f12 mt20">
                            <p class="pl40 c_gray" style="line-height: 20px;height: 20px;margin-top: 0px;">温馨提示：</p>
                            <p class="pl40 c_gray pr40" style="line-height: 20px;height: 20px;margin-top: 0px;">
                                1、充值免收手续费；
                            </p>
                            <p class="pl40 c_gray pr40" style="line-height: 20px;height: 20px;margin-top: 0px;">
                                2、手动充值切记备注充值单号；
                            </p>
                            <p class="pl40 c_gray pr40" style="line-height: 20px;height: 20px;margin-top: 0px;">
                                3、手动充值需人工审核，一般30分钟内到账；
                            </p>
                            <p class="pl40 c_gray pr40" style="line-height: 20px;height: 20px;margin-top: 0px;">
                                4、充值未到账请联系网站右侧QQ客服，或者查看网站底部“新手帮助—充值提现”。
                            </p>
                        </div>
                    </div>
                    <!-- 银行支付 -->
                    <div class="tm_account <c:if test="${2 != type}">dn</c:if>" id="tm_bank">
                        <p style="display: none;">
                            <span class="db fl fir">汇入账户类型： </span>
                            <span class="selector fl">
                                <span class="selector_item">
                                    <span style="width: 90px">汇入账户</span>
                                    <input type="hidden" id="fir_bank_type" name="bankType" value="">
                                </span>
                                <span class="selector_items dn">
                                    <c:forEach items="${bankInfo}" var="bank">
                                        <c:if test="${fn:indexOf(bank.fbankName, '银行') > -1}"><a data-val="${bank.fid}" >${bank.fownerName}-${bank.fbankName}</a></c:if>
                                    </c:forEach>
                                </span>
                            </span>
                        </p>
                        <p class="tm_money">
                            <span class="db fl fir">充值金额： </span>
                            <input id="money_input" type="text" placeholder="<fmt:formatNumber value="${minRecharge}" pattern="#.##" />" onpaste="this.value=this.value.replace(/\D/g,'')" onkeyup="this.value=this.value.replace(/\D/g,'')"  class="db fl" />
                            <span class="pl15 db fl">最小充值金额<fmt:formatNumber value="${minRecharge}" pattern="#.##" />元</span>
                        </p>
                        <div class="tm_money_total c_blue">
                            <div class="tm_money_info fl">
                                <span>汇款金额：</span>
								<span id="random_money">
									<span class="f20" id="money_show">0</span>.${random}
								</span>
                            </div>
                            <div class="c_red pl15 fl" style="line-height:40px;">请严格按照上述金额汇款，包括小数点后两位</div>
                            <div class="cb"></div>
                        </div>
                        <p class="tm_name">
                            <span class="db fl fir">汇款人姓名：</span>
                            <input class="db fl" type="text"  id="payee" value="${sessionScope.login_user.frealName }"/>
                            <span class="c_red pl15 db fl">请确保汇款人姓名与此一致</span>
                        </p>
                        <p class="tm_bank">
                            <span class="db fl fir">汇款银行：</span>
                            <span class="selector fl">
                                <span class="selector_item">
                                    <span>汇款银行</span>
                                    <input type="hidden" id="bank_sel" name="bank" value="">
                                </span>
                                <span class="selector_items dn">
                                    <a data-val="中国农业银行">中国农业银行</a>
                                    <a data-val="中国工商银行">中国工商银行</a>
                                    <a data-val="中国银行">中国银行</a>
                                    <a data-val="中国建设银行">中国建设银行</a>
                                    <a data-val="交通银行">交通银行</a>
                                    <a data-val="中信银行">中信银行</a>
                                    <a data-val="中国光大银行">中国光大银行</a>
                                    <a data-val="华夏银行">华夏银行</a>
                                    <a data-val="广发银行">广发银行</a>
                                    <a data-val="平安银行">平安银行</a>
                                    <a data-val="招商银行">招商银行</a>
                                    <a data-val="兴业银行">兴业银行</a>
                                    <a data-val="民生银行">民生银行</a>
                                    <a data-val="中国邮政储蓄银行">中国邮政储蓄银行</a>
                                    <a data-val="上海浦东发展银行">上海浦东发展银行</a>
                                    <a data-val="其它">其它</a>
                                </span>
                            </span>
                            <span class="pl15 db fl">提示：部分银行周末汇款不能及时到账</span>
                        </p>
                        <p class="tm_bank_num">
                            <span class="db fl fir">汇款银行卡号：</span>
                            <input class="db fl" type="text" id="bank_num" />
                            <span class="pl15 db fl last">请准确填写汇款银行账号，否则不能及时到账，需要人工联系客服排队处理，时间无法保证。</span>
                        </p>
                        <a id="fir_confirm" href="javascript:void(0);" class="tm_confirm" >确认充值</a>
                        <input type="hidden" value="4" name="type" id="finType">
                        <%--<p class="tm_info ">新用户可以前往Bithome注册用户并绑定比特家用户，在线充值后，提现到比特家平台。<a href="/guide/article.html?id=34&currentPage=1&pageIndex=5" target="_blank" class="c_blue">在线充值教程链接</a></p>--%>
                        <div class="info f12 mt20">
                            <p class="pl40 c_gray" style="line-height: 20px;height: 20px;margin-top: 0px;">温馨提示：</p>
                            <p class="pl40 c_gray pr40" style="line-height: 20px;height: 20px;margin-top: 0px;">
                                1、充值免收手续费；
                            </p>
                            <p class="pl40 c_gray pr40" style="line-height: 20px;height: 20px;margin-top: 0px;">
                                2、手动充值需人工审核，一般30分钟内到账；
                            </p>
                            <p class="pl40 c_gray pr40" style="line-height: 20px;height: 20px;margin-top: 0px;">
                                3、充值未到账请联系网站右侧QQ客服，或者查看网站底部“新手帮助—充值提现”；
                            </p>
                            <p class="pl40 c_gray pr40" style="line-height: 20px;height: 20px;margin-top: 0px;">
                                4、由于周末部分银行不提供汇款到账业务，导致用户充值不能及时到账。建议周末（周五17:00至周一09:00）请尽量避免使用：工商银行、农业银行、建设银行、交
                                <br/><span style="display:inline-block;width: 18px;"></span>通银行、中国银行、民生银行等银行卡进行汇款，否则可能会延迟至周一到账。
                            </p>
                        </div>
                    </div>
                    <%--<div class="tm_account <c:if test="${3 != type}">dn</c:if>">
                        <p class="tm_money ml40">
                            <span class="db fl f14">跳转到： </span>
                            <a target="_blank" href="http://www.zcfunding.com/index.php?ctl=transfer" class="c_blue f14">Bithome</a>
                        </p>
                    </div>--%>
                    <!-- 支付宝结果 -->
                    <div id="ali_tm_float_window" class="dn float_window" style="width: 600px;">
                        <div class="tm_title">转账</div>
                        <div class="tm_warning c_blue" style="width: auto;">请您打开您的支付宝按照汇款金额汇款</div>
                        <div class="tm_content">
                            <p class="ml50">
                                <span class="fir db fl">汇入支付宝账号:</span>
                                <span class="db fl" id="ali_bankNumber"></span>
                            </p>
                            <p class="ml50">
                                <span class="fir db fl">收款人:</span>
                                <span class="db fl" id="ali_ownerName"> </span>
                            </p>
                            <p class="ml50">
                                <span class="fir db fl" >充值金额:</span>
                                <span class="c_red db fl" id="ali_bankMoney"></span>
                                <span style="cursor: pointer;" class="c_red db fl">（汇款时请严格按照此金额转账，否则不能到账）</span>
                            </p>
                            <p class="ml50">
                                <span class="fir db fl">备注充值单号:</span>
                                <span class="c_red db fl" id="ali_bankInfo"></span>
                            </p>
                            <p style="margin-bottom: 0;" class="pl20 c_gray f12">温馨提示：</p>
                            <p style="margin-bottom: 0;" class="pl20 c_gray f12">1、请在汇款【备注/附言/摘要】中严格按照要求填写充值单号：<span class="c_red" id="ali_bankDesc"></span></p>
                            <p style="margin-bottom: 0;" class="pl20 c_gray f12">2、本系统非自动扣款，请通过您的支付宝汇款！</p>
                            <p class="pl20 c_gray f12">3、如您已汇款，系统将在收到汇款后20分钟内入账。</p>
                            <a href="/account/chargermb.html?type=${type}" class="tm_confirm_again">确认</a>
                        </div>
                    </div>
                    <!-- 银行卡汇款结果 -->
                    <div id="tm_float_window" class="dn float_window" style="width: 700px;">
                        <div class="tm_title">转账</div>
                        <div class="tm_warning c_blue" style="width: auto;">请您打开您的网上银行或手机银行按照汇款金额汇款</div>
                        <div class="tm_content">
                            <p class="ml50">
                                <span class="fir db fl">汇入银行:</span>
                                <span class="db fl" id="bankAddress"></span>
                            </p>
                            <p class="ml50">
                                <span class="fir db fl">开户行:</span>
                                <span class="db fl" id="bankAddressDetail"></span>
                            </p>
                            <p class="ml50">
                                <span class="fir db fl">汇入银行账号:</span>
                                <span class="db fl" id="bankNumber"></span>
                            </p>
                            <p class="ml50">
                                <span class="fir db fl">收款人:</span>
                                <span class="db fl" id="ownerName"> </span>
                            </p>
                            <p class="ml50">
                                <span class="fir db fl" >充值金额:</span>
                                <span class="c_red db fl" id="bankMoney"></span>
                                <span style="cursor: pointer;" class="c_red db fl">（汇款时请严格按照此金额转账，否则不能到账）</span>
                            </p>
                            <p class="ml50">
                                <span class="fir db fl">备注充值单号:</span>
                                <span class="c_red db fl" id="bankInfo"></span>
                            </p>
                            <p style="margin-bottom: 0;" class="pl20 c_gray f12">温馨提示：</p>
                            <p style="margin-bottom: 0;" class="pl20 c_gray f12">1、请在汇款【备注/附言/摘要】中严格按照要求填写充值单号：<span class="c_red" id="bankDesc"></span></p>
                            <p style="margin-bottom: 0;" class="pl20 c_gray f12">2、本系统非自动扣款，请通过您的网上银行或手机银行汇款！</p>
                            <p class="pl20 c_gray f12">3、如您已汇款，系统将在收到汇款后20分钟内入账。</p>
                            <a href="/account/chargermb.html?type=${type}" class="tm_confirm_again">确认</a>
                        </div>
                    </div>
                    <div id="charge_success" class="dn float_window" style="width: 300px;">
                        <div class="tm_title">
                            <span class="fl">在线充值</span>
                            <span class="fr"><i onclick="closeWindow('charge_success');" class="db iconfont c_gray f12 pointer">&#xe60b;</i></span>
                        </div>
                        <div class="tm_content">
                            <p style="padding: 20px 0px;">
                                <span class="fir db fl"><i class="db iconfont c_green f20">&#xe612;</i></span>
                                <span class="db fl f16">充值成功</span>
                            </p>
                            <a onclick="closeWindow('charge_success');" href="javascript:void(0);" class="tm_confirm_again">确认</a>
                        </div>
                    </div>
                    <div id="charge_failure" class="dn float_window" style="width: 300px;">
                        <div class="tm_title">
                            <span class="fl">在线充值</span>
                            <span class="fr"><i onclick="closeWindow('charge_failure');" class="db iconfont c_gray f12 pointer">&#xe60b;</i></span>
                        </div>
                        <div class="tm_content">
                            <p style="padding: 20px 0px;">
                                <span class="fir db fl"><i class="db iconfont c_red">&#xe609;</i></span>
                                <span class="db fl f16">充值失败</span>
                            </p>
                            <a onclick="closeWindow('charge_failure');" href="javascript:void(0);" class="tm_confirm_again">确认</a>
                        </div>
                    </div>
                    <div id="weixin_pay_qrcode" class="dn float_window" style="">
                        <div class="tm_title">
                            <span class="fl f18">微信扫一扫</span>
                            <span class="fr"><i onclick="cancelWecahtOrder();" class="db iconfont c_gray f12 pointer">&#xe60b;</i></span>
                        </div>
                        <div class="tm_content">
                            <div class="fl" style="height: 300px;width: 50%;">
                                <ul id="weixin_pay_info" style="margin-top: 70px;">
                                    <li style="text-align: left;line-height: 50px;"><span class="pl50 f20">微信扫描二维码完成支付</span></li>
                                    <li style="text-align: left;line-height: 25px;"><span class="pl50 f14">订单编号：<span class="order_no"></span></span></li>
                                    <li style="text-align: left;line-height: 25px;"><span class="pl50 f14">充值金额：<span class="order_amount"></span></span></li>
                                    <li style="text-align: left;line-height: 25px;"><span class="pl50 f14">到账金额：<span class="order_actual c_red"></span></span></li>
                                    <li style="text-align: left;line-height: 50px;"><span class="pl50 f12 c_red">注：充值期间请勿关闭窗口，否则无法到帐</span></li>
                                </ul>
                            </div>
                            <div class="fl" style="height: 300px;width: 50%;position: relative;">
                                <i id="wechat_pay_success_logo" class="dn" style="position: absolute;left: 0;top: 0;right: 0;bottom: 0;margin: auto;z-index: 5000;width: 30px;height: 30px;background: url(${cdn}/static/images/wechat_pay_success.png) no-repeat;"></i>
                                <img id="payQrcode" style="position: absolute;left: 0;top: 0;right: 0;bottom: 0;margin: auto;" src="${cdn}/images/big-loading.gif">
                            </div>
                        </div>
                    </div>
                    <div id="charge_confirm" class="dn float_window" style="width: 400px;">
                        <div class="tm_title">
                            <span class="fl">在线充值</span>
                            <span class="fr"><i onclick="closeWindow('charge_confirm');" class="db iconfont c_gray f12 pointer">&#xe60b;</i></span>
                        </div>
                        <div class="tm_content">
                            <p class="pt40" style="margin: 0 auto; text-align: center;">
                                <span>请您在新打开的网银或第三方支付页面上完成付款</span>
                            </p>
                            <p class="pt40">
                                <a id="epaySuccessBtn" href="javascript:void(0);" style="margin-left: 100px;" class="tm_confirm_again fl">支付成功</a>
                                <a onclick="closeWindow('charge_confirm');" href="javascript:void(0);" style="margin-right: 100px;background-color: #FF634D;"
                                   class="tm_confirm_again fr">遇到问题</a>
                            </p>
                            <p class="pt10"></p>
                        </div>
                    </div>
                    <div id="ebank_float_window" class="dn float_window" style="width: 640px;">
                        <div class="tm_title">
                            <span class="fl">请选择付款银行</span>
                            <span class="fr"><i onclick="closeWindow('ebank_float_window');" class="db iconfont c_gray f12 pointer close">&#xe60b;</i></span>
                        </div>
                        <div class="tm_content" style="height: 370px;">
                            <p id="ebanks" class="banks ml20 mt10">
                                <i data-bank="BOCSH" title="中国银行" class="bank_bocsh db pointer fl"></i>
                                <i data-bank="ABC" title="农业银行" class="bank_abc db pointer fl ml10"></i>
                                <i data-bank="ICBC" title="工商银行" class="bank_icbc db pointer fl ml10"></i>
                                <i data-bank="CCB" title="建设银行" class="bank_ccb db pointer fl ml10"></i>
                                <i data-bank="BOCOM" title="交通银行" class="bank_bocom db pointer fl mt10"></i>
                                <i data-bank="CNCB" title="中信银行" class="bank_cncb db pointer fl ml10 mt10"></i>
                                <i data-bank="HXB" title="华夏银行" class="bank_hxb db pointer fl ml10 mt10"></i>
                                <i data-bank="CMB" title="招商银行" class="bank_cmb db pointer fl ml10 mt10 cur"></i>
                                <i data-bank="CIB" title="兴业银行" class="bank_cib db pointer fl mt10"></i>
                                <i data-bank="CEB" title="光大银行" class="bank_ceb db pointer fl ml10 mt10"></i>
                                <i data-bank="SPDB" title="上海浦东发展银行" class="bank_spdb db pointer fl ml10 mt10"></i>
                                <i data-bank="PSBC" title="邮政储蓄银行" class="bank_psbc db pointer fl ml10 mt10"></i>
                                <i data-bank="CMBC" title="民生银行" class="bank_cmbc db pointer fl mt10"></i>
                                <i data-bank="NJCB" title="南京银行" class="bank_njcb db pointer fl ml10 mt10"></i>
                                <i data-bank="CBHB" title="渤海银行" class="bank_cbhb db pointer fl ml10 mt10"></i>
                                <i data-bank="BRCB" title="北京农村商业银行" class="bank_brcb db pointer fl ml10 mt10"></i>
                                <i data-bank="HZCB" title="杭州银行" class="bank_hzcb db pointer fl mt10"></i>
                                <i data-bank="GDB" title="广发银行" class="bank_gdb db pointer fl ml10 mt10"></i>
                                <i data-bank="PAB" title="平安银行" class="bank_pab db pointer fl ml10 mt10"></i>
                                <i data-bank="BOS" title="上海银行" class="bank_bos db pointer fl ml10 mt10"></i>
                                <i data-bank="NBBANK" title="宁波银行" class="bank_nbbank db pointer fl mt10"></i>
                                <i data-bank="FDB" title="富滇银行" class="bank_fdb db pointer fl ml10 mt10"></i>
                                <i data-bank="BCCB" title="北京银行" class="bank_bccb db pointer fl ml10 mt10"></i>
                                <i data-bank="SRCB" title="上海农村商业银行" class="bank_srcb db pointer fl ml10 mt10"></i>
                                <i data-bank="HFCBCNSH" title="微商银行" class="bank_hfcbcnsh db pointer fl mt10"></i>
                                <i data-bank="CZBANK" title="浙商银行" class="bank_czbank db pointer fl ml10 mt10"></i>
                            </p>
                        </div>
                    </div>
                    <!-- 修改自2016-4-11 by xxp end-->
                </div>
            </div>
        </div>
        <!-- 充值记录 -->
        <c:choose>
            <c:when test="${3 == type}">
                <div class="Tentitle ml20">
                    转账记录
                </div>
                <div id="Tenbody" class="Tenbody">
                    <table>
                        <colgroup>
                            <col width="130"/>
                            <col width="160"/>
                            <col width="120"/>
                            <col width="160"/>
                            <col width="130"/>
                            <col width="130"/>
                        </colgroup>
                        <tr>
                            <th>订单号</th>
                            <th>转移时间</th>
                            <th>转移类型</th>
                            <th>转移金额</th>
                            <th>手续费</th>
                            <th>转移状态</th>
                        </tr>
                        <c:choose>
                            <c:when test="${empty lines || 0 == fn:length(lines)}">
                                <tr><td colspan="6">您暂时没有转账数据</td></tr>
                            </c:when>
                            <c:otherwise>
                                <c:forEach items="${lines}" var="v">
                                    <tr>
                                        <td class="gray">${v.id }</td>
                                        <td><fmt:formatDate value="${v.createTime }" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                                        <td>
                                            <span class="">
                                                <c:choose>
                                                    <c:when test="${v.active}">
                                                        转出
                                                    </c:when>
                                                    <c:otherwise>
                                                        转入
                                                    </c:otherwise>
                                                </c:choose>
                                            </span>
                                        </td>
                                        <td><span class="fl pl60">
                                            <c:choose>
                                                <c:when test="${v.active}">
                                                    -
                                                </c:when>
                                                <c:otherwise>
                                                    +
                                                </c:otherwise>
                                            </c:choose>
                                                ${fns:formatCNY(v.amount)}</span>
                                        </td>
                                        <td><span class="">${fns:formatCNY(v.fee)}</td>
                                        <c:choose>
                                            <c:when test="${0 == v.status.index}">
                                                <td class="c_red">${v.status.name}</td>
                                            </c:when>
                                            <c:when test="${1 == v.status.index}">
                                                <td class="c_orange">${v.status.name}</td>
                                            </c:when>
                                            <c:when test="${2 == v.status.index}">
                                                <td class="c_green">${v.status.name}</td>
                                            </c:when>
                                            <c:otherwise>
                                                <td class="c_gray">${v.status.name}</td>
                                            </c:otherwise>
                                        </c:choose>
                                    </tr>
                                </c:forEach>
                            </c:otherwise>
                        </c:choose>

                    </table>
                    <page:pagination pageSzie="${pageSize}" pageNow="${pageNow}" total="${total}" href="/account/chargermb.html?currentPage=#pageNumber&type=${type}"/>
                </div>
            </c:when>
            <c:otherwise>
                <div class="Tentitle ml20">
                    充值记录
                </div>
                <div id="Tenbody" class="Tenbody">
                    <table>
                        <colgroup>
                            <col width="100"/>
                            <col width="240"/>
                            <col width="165"/>
                            <col width="120"/>
                            <col width="120"/>
                            <col width="165"/>
                            <col width="70"/>
                        </colgroup>
                        <tr>
                            <th>订单号</th>
                            <th>时间</th>
                            <th>充值方式</th>
                            <th>金额</th>
                            <th>手续费</th>
                            <th>状态</th>
                            <th>操作</th>
                        </tr>
                        <c:choose>
                            <c:when test="${empty lines || 0 == fn:length(lines)}">
                                <tr><td colspan="7">您暂时没有充值数据</td></tr>
                            </c:when>
                            <c:otherwise>
                                <c:forEach items="${lines}" var="v">
                                    <tr>
                                        <td class="gray">${v.fid }</td>
                                        <td><fmt:formatDate value="${v.fcreateTime }" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                                        <td><c:if test="${0 != v.payType.index}">在线支付-</c:if>${v.fBank }</td>
                                        <td><span class="fl pl40">￥${fns:formatCNY(v.famount)}</span></td>
                                        <td><span class="fl pl40">￥${fns:formatCNY(v.ffees)}</span></td>
                                        <c:choose>
                                            <c:when test="${1 == v.fstatus || 2 == v.fstatus}">
                                                <td class="c_red">${v.fstatus_s }</td>
                                            </c:when>
                                            <c:when test="${3 == v.fstatus}">
                                                <td class="c_green">${v.fstatus_s }</td>
                                            </c:when>
                                            <c:otherwise>
                                                <td class="c_gray">${v.fstatus_s }</td>
                                            </c:otherwise>
                                        </c:choose>
                                        <td >
                                            <c:choose>
                                                <c:when test="${2 == v.payType.index && 2 == v.fstatus && curTime.time - v.fcreateTime.time > 10 * 60 * 1000}"><span class="c_gray">已过期</span></c:when>
                                                <c:when test="${0 != v.payType.index && 3 == v.fstatus}">
                                                    <!--<a href="javascript:void(0);" class="c_blue" data-type="${v.payType.index}" data-bank="${banks[v.fBank].enName}" data-num="<fmt:formatNumber value="${v.famount}" pattern="#.##" />" data-actual="<fmt:formatNumber value="${v.actualAmount}" pattern="#.##" />">已成功</a>-->
                                                    <span class="c_blue">已成功</span>
                                                </c:when>
                                                <c:when test="${1 == v.fstatus || 2 == v.fstatus}">
                                                    <a href="javascript:void(0);" class="c_orange" onclick="javascript:cancelChargeCny(${v.fid });">取消</a>
                                                </c:when>
                                                <c:otherwise>
                                                    <a href="javascript:void(0);" data-fbank="${v.fBank }" class="look_again c_blue" data-fid="${v.fid }" data-famount="${v.famount }" data-bankname="${v.systembankinfo.fbankName }" data-bankaddress="${v.systembankinfo.fbankAddress }" data-ownername="${v.systembankinfo.fownerName }" data-banknum="${v.systembankinfo.fbankNumber }">查看</a>
                                                </c:otherwise>
                                            </c:choose>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </c:otherwise>
                        </c:choose>

                    </table>
                    <page:pagination pageSzie="${pageSize}" pageNow="${pageNow}" total="${total}" href="/account/chargermb.html?currentPage=#pageNumber&type=${type}"/>
                </div>
            </c:otherwise>
        </c:choose>
        <!-- 转账记录 -->
    </div>
    <div class="cb"></div>
    <div id="qrcodeCanvas" style="display: none;"></div>
</div>
<%@ include file="../common/footer.jsp"%>
<!-- 人民币充值结束 -->
<script  src="${resources}/static/js/utils/selector.js"></script>
<script src="${resources}/static/js/account/charge_cny.js"></script>
<script type="text/javascript" src="${resources}/static/js/jquery/jquery.qrcode.min.js?rand=20140226b"></script>
<script>
    function closeWindow(id) {
        $("#tm_yy").addClass("dn");
        $("#" + id).addClass("dn");
    }
    function showWindow(id) {
        center_size(id);
        $(window).resize(function () {
            center_size(id);
        });
        $("#tm_yy").removeClass("dn");
        $("#" + id).removeClass("dn");
    }

    function showPayQrcode(codeUrl, orderId){
        $('#weixin_pay_info span.order_no').text(orderId);
        $('#qrcodeCanvas').html('');
        if(navigator.userAgent.indexOf("MSIE")>0) {
            jQuery('#qrcodeCanvas').qrcode({text:codeUrl,width:"200",height:"200",render:"table"});
        } else{
            jQuery('#qrcodeCanvas').qrcode({text:codeUrl,width:"200",height:"200"});
        }
        try{
            var image = document.getElementById("payQrcode");
            var canvas = document.getElementById("qrcodeCanvas").getElementsByTagName("canvas")[0];
            image.src = canvas.toDataURL("image/png");
        }catch (e){
//            console.log(e);
        }
        showWindow("weixin_pay_qrcode");
    }

    function showPayQrcodeLoading(){
        var $info = $('#weixin_pay_info');
        $info.find('span.order_amount').text($('#weixin_money_input').val());
        $info.find('span.order_actual').text($('#weixin_cut_fee').text());

        var image = document.getElementById("payQrcode");
        image.src = '${cdn}/images/big-loading.gif';
        showWindow("weixin_pay_qrcode");
    }

    function cancelWecahtOrder(){
        var id = $('#weixin_pay_info span.order_no').text();
        if(id){
//            var url = "/account/cancelChargeCny.html?random="+Math.round(Math.random()*100);
//            var param={id:id};
//            $.post(url,param,function(data){
//                window.location.reload(true);
//            });
        }
        window.location.reload(true);
    }

    function paidToReloadPage(orderId){
        setTimeout(function(){
            setInterval(function(){
                $.getJSON("/account/payOrderInfo.html", {orderId : orderId}, function(data){
                    if(200 == data.code && 3 == data.status){
                        $('#wechat_pay_success_logo').show();
                        setTimeout(window.location.reload(true), 1000);
                    }
                });
            }, 1000);
        }, 1000);
    }
</script>
<c:choose>
    <c:when test="${200 == param.code}">
        <script type="text/javascript">
            showWindow("charge_success");
        </script>
    </c:when>
    <c:when test="${-1 == param.code}">
        <script type="text/javascript">
            showWindow("charge_failure");
        </script>
    </c:when>
</c:choose>
</body>
</html>
