<!-- 人民币提现页面author:xxp 2016-04-23 -->
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
        <a href="javascript:void(0)" class="f12 c_gray">虚拟币提现</a>
    </div>
    <c:set var="dt_index" value="1"/>
    <c:set var="dd_index" value="5"/>
    <%@ include file="../common/account_left.jsp" %>
    <!-- 虚拟币提现开始 -->
    <div id="container" class="account_right fl">
        <div class="finance_wrapper">
            <div class="finance_container">
                <input type="hidden" id="wallet_balance" name="wallet_balance" value="${fns:formatCoin(wallet.ftotal)}">
                <h1 class="ml40">虚拟币提现</h1>
                <div class="content">
                    <div class="coin_wrapper">
                        <div class="coin_container  fl">
                            <img class="coin_img db fl" alt="" height="20" width="20" title="${fvirtualcointype.fname}"
                                 src="${cdn}${fvirtualcointype.furl}"/>
                            <span class="db fl pl5 ellipsis coinname">${fvirtualcointype.fname}</span>
                            <i class="db fl iconfont rotate_icon c_blue ml40 cp" id="coin_sel">&#xe611;</i>
                            <div class="coin_sel_box bg_white dn">
                                <ul>
                                    <c:forEach items="${fvirtualcointypes}" var="type">
                                        <c:if test="${type.FIsWithDraw}">
                                            <li>
                                                <a data-pjax="#container" href="/account/withdrawBtc.html?symbol=${type.fid}">
                                                    <img class="coin_img db fl" alt="" height="20" width="20"
                                                         title="${type.fname}" src="${cdn}${type.furl}"/>
                                                    <span class="db fl pl5 ellipsis coinname">${type.fname}</span>
                                                </a>
                                            </li>
                                        </c:if>
                                    </c:forEach>
                                </ul>
                            </div>
                        </div>
                        <span class="db fl pl40">可用：</span>
                        <span class="c_green db fl pl5">${fns:formatCoin(wallet.ftotal)}</span>
                        <span class="db fl pl40">冻结：</span>
                        <span class="c_red db fl pl5">${fns:formatCoin(wallet.ffrozen)}</span>
                    </div>
                    <div class="submit_form widthdraw_box">
                        <p>
                            <span class="fl db fir">提现地址：</span>
							<span id="withdrawAddrSpan">
								<c:choose>
                                    <c:when test="${ fvirtualaddressWithdraw.init==true}">
                                        <span style="float: left;">${fvirtualaddressWithdraw.fadderess }</span>&nbsp;&nbsp;<a style="float: left;height: 34px;line-height: 34px;" class="c_blue" onclick="javascript:modifyWithdrawBtcAddr();" href="javascript:void(0);">修改</a>
                                    </c:when>
                                    <c:otherwise>
                                        <a onclick="javascript:modifyWithdrawBtcAddr();" style="float: left;height: 34px;line-height: 34px;" class="c_blue"
                                           href="javascript:void(0);">设置</a>
                                    </c:otherwise>
                                </c:choose>
							</span>
                        </p>
                        <p>
                            <span class="fl db fir">提现数量：</span>
                            <input type="tel" autocomplete="off" class="fl db pl5 ml5" id="withdrawAmount" name="withdrawAmount" />
                        </p>
                        <p>
                            <span class="fl db fir">交易密码：</span>
                            <input type="password" class="fl db pl5 ml5" onkeydown="callbackEnter(submitWithdrawBtcForm);"
                                   id="tradePwd" name="tradePwd"/>
                        </p>
                        <p id="withdrawPhoneCodeLi">
                            <span class="fl db fir">验证码：</span>
                            <input type="text" class="fl db pl5 ml5" id="withdrawPhoneCode" name="withdrawPhoneCode"/>
                            <c:choose>
                                <c:when test="${wallet.ftotal > 0 && fvirtualaddressWithdraw.init==true}">
                                <div class="authorcode_wrapper authorcode_wrapper_page">
                                    <a id="drawCoinSmsCodeBtn" onclick="sendSmsCaptcha(this);" href="javascript:;" style="width: auto;margin-left: 105px;" class="messagecode_wrapper db fl c_blue mt10">
                                        <i class="iconfont db fl ml5 mr5" style="width: 20px">&#xe645;</i>
                                        <span class="db fl" style="width: 98px;" data-name="发送短信验证码">发送短信验证码</span>
                                        <span class="dn">发送验证码(19)</span>
                                    </a>
                                    <a id="drawCoinVoiceCodeBtn" onclick="sendVoiceCaptcha(this);" href="javascript:;" style="width: auto;" class="voicecode_wrapper db fl c_orange mt10">
                                        <i class="iconfont db fl ml5 mr5" style="width: 20px">&#xe644;</i>
                                        <span class="db fl" style="width: 98px;" data-name="发送语音验证码">发送语音验证码</span>
                                        <span class="dn">发送验证码(19)</span>
                                    </a>
                                </div>
                                </c:when>
                                <c:otherwise>
                                    <div class="authorcode_wrapper authorcode_wrapper_page">
                                        <a id="drawCoinSmsCodeBtn" href="javascript:;" style="width: auto;margin-left: 105px;" class="messagecode_wrapper db fl c_blue mr10 mt10">
                                            <i class="iconfont db fl ml5 mr5 c_gray" style="width: 20px">&#xe645;</i>
                                            <span class="db fl c_gray" data-name="发送短信验证码">发送短信验证码</span>
                                            <span class="dn">发送验证码(19)</span>
                                        </a>
                                        <a id="drawCoinVoiceCodeBtn" href="javascript:;" style="width: auto;" class="voicecode_wrapper db fl c_orange mt10">
                                            <i class="iconfont db fl ml5 mr5 c_gray" style="width: 20px">&#xe644;</i>
                                            <span class="db fl c_gray" data-name="发送语音验证码">发送语音验证码</span>
                                            <span class="dn">发送验证码(19)</span>
                                        </a>
                                    </div>
                                </c:otherwise>
                            </c:choose>
                        </p>
                        <c:choose>
                            <c:when test="${wallet.ftotal > 0 && fvirtualaddressWithdraw.init==true}">
                                <a onclick="javascript:submitWithdrawBtcForm('${fvirtualcointype.fShortName}');"
                                   href="javascript:void(0);" id="withdrawBtcButton" class="bg_blue c_white submit">提现</a>
                            </c:when>
                            <c:otherwise>
                                <a href="javascript:void(0);" class="bg_gray c_gray submit">提现</a>
                            </c:otherwise>
                        </c:choose>
                    </div>

                    <div class="info ml40 f12 c_gray">
                        <p style="line-height: 20px;">重要提示：</p>
                        <p style="line-height: 20px;">1、提币手续费为0.5%，请仔细确认后再操作； </p>
                        <p style="line-height: 20px;">2、每天提币次数最多为3次。</p>
                    </div>
                    <div class="warning f12 c_red p1em">
                        <i class="iconfont c_red">&#xe601;</i>
                        本站客服不会要求用QQ远程控制您的电脑,所有要求远程的都是骗子。<span class="c_red">短信验证码非常重要，请勿透露给任何人，包括本站的客服。</span>
                    </div>
                </div>
            </div>
        </div>
        <!-- 提现记录 -->
        <div class="Tentitle ml20">
            提现记录
        </div>
        <div id="Tenbody" class="Tenbody">
            <table>
                <colgroup>
                    <col width="160"/>
                    <col width="200"/>
                    <col width="90"/>
                    <col width="320"/>
                    <col width="180"/>
                </colgroup>
                <tr>
                    <th>提现时间</th>
                    <th>提现数量</th>
                    <th>手续费</th>
                    <th>提现地址</th>
                    <th>提现状态</th>
                </tr>
                <c:if test="${empty fvirtualcaptualoperations || 0 == fn:length(fvirtualcaptualoperations)}">
                    <tr>
                        <td colspan="6">您暂时没有充值数据</td>
                    </tr>
                </c:if>
                <c:forEach items="${fvirtualcaptualoperations}" var="line">
                    <tr>
                        <td><fmt:formatDate value="${line.fcreateTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                        <td><span class="fl pl60">${fns:formatCoin(line.famount)}</span></td>
                        <td><span class="fl pl20">${fns:formatCoin(line.ffees)}</td>
                        <td><span class="fl pl20">${line.withdraw_virtual_address}</span></td>
                        <td>
                            <c:choose>
                                <c:when test="${1 == line.fstatus}">
                                    <span class="c_red">${line.fstatus_s}</span>
                                    &nbsp;|&nbsp;
                                    <a href="javascript:cancelWithdrawBtc(${line.fid});" class="c_orange">取消</a>
                                </c:when>
                                <c:when test="${3 == line.fstatus}">
                                    <span class="c_green">${line.fstatus_s}</span>
                                </c:when>
                                <c:when test="${4 == line.fstatus}">
                                    <span class="c_gray">${line.fstatus_s}</span>
                                </c:when>
                                <c:otherwise>
                                    <span class="c_red">${line.fstatus_s}</span>
                                </c:otherwise>
                            </c:choose>
                        </td>
                    </tr>
                </c:forEach>
            </table>
            <page:pagination pageSzie="${pageSize}" pageNow="${pageNow}" total="${total}" href="/account/withdrawBtc.html?currentPage=#pageNumber&symbol=${fvirtualcointype.fid}"/>
        </div>

        <div id="withdrawBtcAddrDiv" class="float_box dn" style="top:300px;left:700px;">
            <div class="f_title pl10">
                <h3 class="db fl">设置提现地址</h3>
                <i onclick="closeModAddressDialog();" class="iconfont c_gray db fr close">&#xe609;</i>
            </div>
            <div class="f_content">
                <p>
                    <input type="hidden" id="symbol" value="${fvirtualcointype.fid}">
                    <input type="hidden" id="withdrawAddr" value="${fvirtualaddressWithdraw.fadderess}">
                    <span class="db fl">提现地址：</span>
                    <input type="text" id="withdrawBtcAddr" style="width: 300px;"></span>
                </p>
                <p id="withdrawBtcAddrPhoneCodeLi">
                    <span class="db fl">短信验证码：</span>
                    <input class="fl" type="text" id="withdrawBtcAddrPhoneCode" name="withdrawBtcAddrPhoneCode">
                    <div class="authorcode_wrapper authorcode_wrapper_page">
                        <a id="modAddrSmsCodeBtn" onclick="sendSmsCaptcha(this);" href="javascript:;" style="width: auto;margin-left: 165px;" class="messagecode_wrapper db fl c_blue mt10 pl5 pr5">
                            <i class="iconfont db fl ml5 mr5" style="width: 20px">&#xe645;</i>
                            <span class="db fl" style="width: 98px;text-align: center;" data-name="发送短信验证码">发送短信验证码</span>
                            <span class="dn">发送验证码(19)</span>
                        </a>
                        <a id="modAddrVoiceCodeBtn" onclick="sendVoiceCaptcha(this);" href="javascript:;" style="width: auto;" class="voicecode_wrapper db fl c_orange mt10 pl5 pr5 ml20">
                            <i class="iconfont db fl ml5 mr5" style="width: 20px">&#xe644;</i>
                            <span class="db fl" style="width: 98px;text-align: center;" data-name="发送语音验证码">发送语音验证码</span>
                            <span class="dn">发送验证码(19)</span>
                        </a>
                    </div>
                </p>
                <a title="确定提交" style="cursor: pointer;" id="withdrawBtcAddrBtn"
                   onclick="javascript:submitWithdrawBtcAddrForm('${fvirtualcointype.fShortName}');"
                   class="confirm bg_blue c_white">确定提交</a>
            </div>
        </div>
    </div>
    <div class="cb"></div>
    <!-- 虚拟币提现结束 -->
</div>
<%--<div style="display: none;"  class="okcoinPop">
    <div id="dialog_content_btcaddr" class="dialog_content">
        <div id="dialog_title_btcaddr" class="dialog_title"> <span>提现地址</span>
            <a title="关闭" class="dialog_closed" href="javascript:closeWithdrawBtcAddr();"></a>
        </div>
        <div class="dialog_body">
            <div class="center">
                <ul class="connphone">
                    <li><span class="c1">提现地址：</span><span><input type="text" id="withdrawBtcAddr"></span></li>

                    <c:if test="${fuser.fisTelephoneBind==true }">
                        <li id="withdrawBtcAddrPhoneCodeLi"><span class="c1">短信验证码:</span><span><input type="text" id="withdrawBtcAddrPhoneCode" name="withdrawBtcAddrPhoneCode"></span>
                            &nbsp;&nbsp;<input type="button" class="sendmsg" value="发送验证码" onclick="javascript:sendMsgCode(8,'modifyResultTips','msgCodeAddrBtn');" id="msgCodeAddrBtn" style="width:88px;"></li>
                        <input type="hidden" value="0" id="msgCodeAddrBtnSign">
                        </li>
                    </c:if>

                    <c:if test="${fuser.fgoogleBind==true }">
                        <li id="withdrawBtcAddrTotpCodeLi"><span class="c1">谷歌验证码:</span><span><input type="text" id="withdrawBtcAddrTotpCode" name="withdrawBtcAddrTotpCode"></span></li>
                    </c:if>

                    <li id="withdrawBtcAddrTipsLi"><span class="c1">&nbsp;</span><span id="withdrawBtcAddrTips" class="fred"></span></li>

                    <li class="submit"><input type="button" value="确定提交" id="withdrawBtcAddrBtn" onclick="javascript:submitWithdrawBtcAddrForm('${fvirtualcointype.fShortName}');"></li>
                </ul>
            </div>
        </div>
    </div>
</div>--%>
<script src="${resources}/static/js/coincommon.js"></script>
<script src="${resources}/static/js/account/btc_withdraw.js"></script>
<script>
    function drawNumber(dom){
        var $this = $(dom);
        var val = $this.val() * 1.0;
        var hasNum = $('#wallet_balance').val() * 1.0;
        if(!val){
            $this.val('');
        }else if(val > hasNum){
            $this.val(hasNum);
        }else{
            $this.val(val.toFixed(4));
        }
    }
    $(function () {
        $("#container").on("mouseenter",".coin_container",function(){
            $(".coin_sel_box").show();
        });
        $("#container").on("mouseleave",".coin_container",function(){
            $(".coin_sel_box").hide();
        });
    });
</script>
<%@ include file="../common/footer.jsp" %>
</body>
</html>
