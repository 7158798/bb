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
        <a href="javascript:void(0)" class="f12 c_gray">资金转移</a>
    </div>
    <c:set var="dt_index" value="1"/>
    <c:set var="dd_index" value="20"/>
    <%@ include file="../common/account_left.jsp" %>
    <!-- 虚拟币提现开始 -->
    <input type="hidden" id="isEmptyAuth"
           value="${(fuser.fisTelephoneBind==false && fuser.fgoogleBind==false )?'1':'0' }">
    <input type="hidden" id="symbol" value="${fvirtualcointype.fid}">
    <input type="hidden" id="withdrawAddr" value="${fvirtualaddressWithdraw.fadderess}">
    <div id="container" class="account_right fl">
        <div class="finance_wrapper">
            <div class="finance_container">
                <input type="hidden" id="wallet_balance" name="wallet_balance" value="${fns:formatCoin(wallet.ftotal)}">
                <h1 class="ml40">资金转移到Bithome</h1>
                <div class="content">
                    <div class="coin_wrapper">
                        <div class="coin_container  fl">
                            <img class="coin_img db fl" alt="" height="20" width="20" title="${fvirtualcointype.fname}"
                                 src="${cdn}${fvirtualcointype.furl}"/>
                            <span class="db fl pl5 ellipsis coinname">${fvirtualcointype.fname}</span>
                            <i class="db fl iconfont rotate_icon c_blue ml40 cp" id="coin_sel">&#xe611;</i>
                            <div class="coin_sel_box bg_white dn">
                                <ul data-size="${fn:length(fvirtualcointypes)}">
                                    <c:forEach items="${fvirtualcointypes}" var="type">
                                        <%--<c:if test="${type.FIsWithDraw}">--%>
                                            <li>
                                                <a data-pjax="#container" href="/account/transfer.html?symbol=${type.fid}">
                                                    <img class="coin_img db fl" alt="" height="20" width="20"
                                                         title="${type.fname}" src="${cdn}${type.furl}"/>
                                                    <span class="db fl pl5 ellipsis coinname">${type.fname}</span>
                                                </a>
                                            </li>
                                        <%--</c:if>--%>
                                    </c:forEach>
                                </ul>
                            </div>
                        </div>
                        <span class="db fl pl40">可用：</span>
                        <span class="c_green db fl pl5" id="totalAmount">${fns:formatCoin(wallet.ftotal)}</span>
                        <span class="db fl pl40">冻结：</span>
                        <span class="c_red db fl pl5">${fns:formatCoin(wallet.ffrozen)}</span>
                    </div>
                    <div class="submit_form widthdraw_box">
                        <p>
                            <span class="fl db fir">转移数量：</span>
                            <input type="tel" autocomplete="off" class="fl db pl5 ml5" id="withdrawAmount" name="withdrawAmount" onblur="drawNumber(this);" />
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
                                <c:when test="${wallet.ftotal > 0}">
                                    <div class="authorcode_wrapper authorcode_wrapper_page">
                                        <a id="transferCoinSmsCodeBtn" onclick="sendSmsCaptcha(this);" href="javascript:;" style="width: auto;margin-left: 105px;" class="messagecode_wrapper db fl c_blue mt10">
                                            <i class="iconfont db fl ml5 mr5" style="width: 20px">&#xe645;</i>
                                            <span class="db fl" style="width: 98px;" data-name="发送短信验证码">发送短信验证码</span>
                                            <span class="dn">发送验证码(19)</span>
                                        </a>
                                        <a id="transferCoinVoiceCodeBtn" onclick="sendVoiceCaptcha(this);" href="javascript:;" style="width: auto;" class="voicecode_wrapper db fl c_orange mt10">
                                            <i class="iconfont db fl ml5 mr5" style="width: 20px">&#xe644;</i>
                                            <span class="db fl" style="width: 98px;" data-name="发送语音验证码">发送语音验证码</span>
                                            <span class="dn">发送验证码(19)</span>
                                        </a>
                                    </div>
                                </c:when>
                                <c:otherwise>
                                    <div class="authorcode_wrapper authorcode_wrapper_page">
                                        <a id="transferCoinSmsCodeBtn" href="javascript:;" style="width: auto;margin-left: 105px;" class="messagecode_wrapper db fl c_blue mr10 mt10">
                                            <i class="iconfont db fl ml5 mr5 c_gray" style="width: 20px">&#xe645;</i>
                                            <span class="db fl c_gray" data-name="发送短信验证码">发送短信验证码</span>
                                            <span class="dn">发送验证码(19)</span>
                                        </a>
                                        <a id="transferCoinVoiceCodeBtn" href="javascript:;" style="width: auto;" class="voicecode_wrapper db fl c_orange mt10">
                                            <i class="iconfont db fl ml5 mr5 c_gray" style="width: 20px">&#xe644;</i>
                                            <span class="db fl c_gray" data-name="发送语音验证码">发送语音验证码</span>
                                            <span class="dn">发送验证码(19)</span>
                                        </a>
                                    </div>
                                </c:otherwise>
                            </c:choose>
                        </p>
                        <c:choose>
                            <c:when test="${wallet.ftotal > 0}">
                                <a onclick="javascript:submitWithdrawBtcForm('${fvirtualcointype.fid}');"
                                   href="javascript:void(0);" id="withdrawBtcButton" class="bg_blue c_white submit">确认转移</a>
                            </c:when>
                            <c:otherwise>
                                <a href="javascript:void(0);" class="bg_gray c_gray submit">确认转移</a>
                            </c:otherwise>
                        </c:choose>
                    </div>

                    <div class="info ml40 f12 c_gray">
                        <p style="line-height: 20px;">重要提示：</p>
                        <p style="line-height: 20px;">1、资金转移手续费为0； </p>
                        <p style="line-height: 20px;">2、系统自动将资金转移到Bithome对应账户。</p>
                    </div>
                    <div class="warning f12 c_red p1em" style="left: 590px;">
                        <i class="iconfont c_red">&#xe601;</i>
                        本站客服不会要求用QQ远程控制您的电脑,所有要求远程的都是骗子。<span class="c_red">短信验证码非常重要，请勿透露给任何人，包括本站的客服。</span>
                    </div>
                </div>
            </div>
        </div>
        <!-- 转移记录 -->
        <div class="Tentitle ml20">
            转移记录
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
                <c:if test="${empty lines || 0 == fn:length(lines)}">
                    <tr>
                        <td colspan="6">您暂时没有资金转移记录</td>
                    </tr>
                </c:if>
                <c:forEach items="${lines}" var="line">
                    <tr>
                        <td><span>${line.id}</span></td>
                        <td><fmt:formatDate value="${line.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                        <td>
                            <span class="">
                                <c:choose>
                                    <c:when test="${line.active}">
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
                                <c:when test="${line.active}">
                                    -
                                </c:when>
                                <c:otherwise>
                                    +
                                </c:otherwise>
                            </c:choose>
                            ${fns:formatCoin(line.amount)}
                        </span></td>
                        <td><span class="">${fns:formatCNY(line.fee)}</td>
                        <td>
                            <c:choose>
                                <c:when test="${0 == line.status.index}">
                                    <span class="c_red">${line.status.name}</span>
                                </c:when>
                                <c:when test="${1 == line.status.index}">
                                    <span class="c_orange">${line.status.name}</span>
                                </c:when>
                                <c:when test="${2 == line.status.index}">
                                    <span class="c_green">${line.status.name}</span>
                                </c:when>
                                <c:otherwise>
                                    <span class="c_gray">${line.status.name}</span>
                                </c:otherwise>
                            </c:choose>
                        </td>
                    </tr>
                </c:forEach>
            </table>
            <page:pagination pageSzie="${pageSize}" pageNow="${pageNow}" total="${total}" href="/account/transfer.html?currentPage=#pageNumber&symbol=${fvirtualcointype.fid}"/>
        </div>
    </div>
    <div class="cb"></div>
    <!-- 虚拟币提现结束 -->
    <script>
        function drawNumber(dom){
            var $this = $(dom);
            var val = $this.val() * 1;
            var hasNum = $('#wallet_balance').val() * 1;
            if(!val){
                $this.val('');
            }else if(val > hasNum){
                $this.val(hasNum);
            }else{
                $this.val(val.toFixed(4));
            }
        }
        function submitWithdrawBtcForm(symbol){
            if(!symbol) return;

            var total = $('#totalAmount').html() * 1;
            var amount = $("#withdrawAmount").val() * 1;
            var password = $("#tradePwd").val();
            var phoneCode = $("#withdrawPhoneCode").val();

            if(total < amount){
                alert("您的余额不足");
                return;
            }

            if(!amount){
                alert("请输入转移金额");
                return;
            }

            if(!password || $.trim(password).length < 6){
                alert("请输入正确的交易密码");
                return;
            }

            if(!phoneCode || $.trim(phoneCode).length < 4){
                alert("请输入正确验证码");
                return;
            }

            $.post("/account/transfer_coin.html", {symbol : symbol, amount : amount, password : password, phoneCode : phoneCode}, function(data){
                if(200 == data.code){
                    window.location.reload();
                }else if(201 == data.code){
                    alert('不支持该系统的资金转移');
                }else if(202 == data.code){
                    alert('非法操作');
                }else if(203 == data.code){
                    alert('资金转移不能小于0.0001');
                }else if(204 == data.code){
                    alert("交易密码错误");
                }else if(205 == data.code){
                    alert("不支持该虚拟币的资金转移");
                }else if(206 == data.code){
                    alert("您还不支持资金转移，请联系客服");
                }else if(207 == data.code){
                    alert('您的余额不足');
                }else if(208 == data.code){
                    alert("验证码错误多次，请2小时后再试！");
                }else if(209 == data.code){
                    alert("验证码错误！您还有"+data.leftCount+"次机会");
                }else{
                    alert('转移失败');
                }
            }, "json");
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
</div>
<%@ include file="../common/footer.jsp" %>
</body>
</html>
