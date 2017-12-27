<!-- 虚拟币提现页面author:yujie 2016-04-23 -->
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
		<a href="javascript:void(0)" class="f12 c_gray">人民币提现</a>
	</div>
	<c:set var="dt_index" value="1"/>
	<c:set var="dd_index" value="3"/>
	<%@ include file="../common/account_left.jsp"%>
	<!-- 人民币提现开始 -->
	<input type="hidden" id="max_double" value="${max_double }">
	<input type="hidden" id="min_double" value="${min_double }">
	<input type="hidden" id="wallet_balance" value="${fns:formatCNY(fuser.fwallet.ftotalRmb)}">
	<div id="container" class="account_right fl">
		<div class="finance_wrapper">
			<div class="finance_container">
				<h1 class="ml40">人民币提现</h1>
				<div class="content">
					<p class="c_title f14 fb ml40 mr40 pl5">请选择一张银行卡</p>
					<div class="Tenbody">
						<table id="bank_table">
							<colgroup>
								<col width="70"/>
								<col width="220"/>
								<col width="160"/>
								<col width="170"/>
								<col width="170"/>
								<col width="80"/>
							</colgroup>
							<tr>
								<th>选择</th>
								<th>银行卡号</th>
								<th>银行</th>
								<th>开户地址</th>
								<th>开户银行网点</th>
								<th>操作</th>
							</tr>
							<c:forEach items="${bankCards}" var="bankCard" varStatus="s">
								<c:if test="${bankCard.init}">
									<tr class="bank_li">
										<c:choose>
											<c:when test="${0 == s.index}">
												<td><i class="iconfont cp choose c_blue" data-id="${bankCard.fid}" data-flag="true">&#xe600;</i></td>
											</c:when>
											<c:otherwise>
												<td><i class="iconfont cp choose c_gray" data-id="${bankCard.fid}" data-flag="false">&#xe602;</i></td>
											</c:otherwise>
										</c:choose>
										<%--<td>${fuser.frealName}</td>--%>
										<td>${bankCard.fbankNumber}</td>
										<td>${bankCard.fname}</td>
										<td>${bankCard.fprovince}${bankCard.fcity}</td>
										<td>${bankCard.fbranch}</td>
										<td>
											<a href="javascript:void(0)" data-id="${bankCard.fid}" class="c_orange delete">删除</a>
										</td>
									</tr>
								</c:if>
							</c:forEach>
							<tr>
								<td colspan="6" class="cp c_gray" id="add_bank_card">
									<i class="iconfont" >&#xe60a;</i>
									<span class="pl10">添加银行卡</span>
								</td>
							</tr>
						</table>
					</div>
					<p class="ml40 f14 c_title fb mr40 pl5">
						<span>填写提款信息</span><span class="f12">（可用余额：<span class="c_green">￥${fns:formatCNY(fuser.fwallet.ftotalRmb)}</span>）</span>
					</p>
					<div class="submit_form widthdraw_box">
						<input type="hidden" name="cardId" value="${bankCards[0].fid}" id="bankCardId">
						<p>
							<span class="fl db fir">提现金额：</span>
							<input type="number" name="withdrawBalance" id="withdrawBalance" onkeyup="drawNumber(this);" class="fl db pl5 ml5" />
						</p>
						<p>
							<span class="fl db fir">交易密码：</span>
							<input class="fl db pl5 ml5" type="text" name="tradePwd" onfocus="this.type='password'" id="tradePwd"/>
						</p>
						<p id="withdrawPhoneCodeLi">
							<span class="fl db fir">验证码：</span>
							<input id="withdrawPhoneCode" name="withdrawPhoneCode" class="fl db pl5 ml5" type="text"/>
							<span class="fl db pl10 c_gray">短信验证码接收不到？试试语音验证码</span>
							<c:choose>
								<c:when test="${fuser.fwallet.ftotalRmb >= min_double}">
									<div class="authorcode_wrapper authorcode_wrapper_page">
										<a id="drawCnySmsCodeBtn" onclick="sendSmsCaptcha(this);" href="javascript:;" style="width: auto;margin-left: 105px;" class="messagecode_wrapper db fl c_blue mt10">
											<i class="iconfont db fl ml5 mr5" style="width: 20px">&#xe645;</i>
											<span class="db fl" style="width: 98px;" data-name="发送短信验证码">发送短信验证码</span>
											<span class="dn">发送验证码(19)</span>
										</a>
										<a id="drawCnyVoiceCodeBtn" onclick="sendVoiceCaptcha(this);" href="javascript:;" style="width: auto;" class="voicecode_wrapper db fl c_orange mt10">
											<i class="iconfont db fl ml5 mr5" style="width: 20px">&#xe644;</i>
											<span class="db fl" style="width: 98px;" data-name="发送语音验证码">发送语音验证码</span>
											<span class="dn">发送验证码(19)</span>
										</a>
									</div>
								</c:when>
								<c:otherwise>
									<div class="authorcode_wrapper authorcode_wrapper_page">
										<a id="drawCnySmsCodeBtn" href="javascript:;" style="width: auto;margin-left: 105px;" class="messagecode_wrapper db fl c_blue mr10 mt10">
											<i class="iconfont db fl ml5 mr5 c_gray" style="width: 20px">&#xe645;</i>
											<span class="db fl c_gray" data-name="发送短信验证码">发送短信验证码</span>
											<span class="dn">发送验证码(19)</span>
										</a>
										<a id="drawCnyVoiceCodeBtn" href="javascript:;" style="width: auto;" class="voicecode_wrapper db fl c_orange mt10">
											<i class="iconfont db fl ml5 mr5 c_gray" style="width: 20px">&#xe644;</i>
											<span class="db fl c_gray" data-name="发送语音验证码">发送语音验证码</span>
											<span class="dn">发送验证码(19)</span>
										</a>
									</div>
								</c:otherwise>
							</c:choose>
						</p>
						<c:choose>
							<c:when test="${fuser.fwallet.ftotalRmb >= min_double}">
								<a onclick="javascript:submitWithdrawCnyForm('modifyResultTips');"
								   href="javascript:void(0);" id="withdrawCnyButton" class="bg_blue c_white submit">提现</a>
							</c:when>
							<c:otherwise>
								<a href="javascript:void(0);" class="bg_gray c_gray submit">提现</a>
							</c:otherwise>
						</c:choose>
					</div>
					<div class="info f12 mt20">
						<p class="pl40 c_gray" style="line-height: 20px;">提现说明：</p>
						<p class="pl40 c_gray pr40" style="line-height: 20px;">
							1、提现手续费5元+0.5%，100元提现手续费5.5元，10000元提现手续费55元；
						</p>
						<p class="pl40 c_gray pr40" style="line-height: 20px;">
							2、单笔提现限额100元—49999.99元 ；
						</p>
						<p class="pl40 c_gray pr40" style="line-height: 20px;">
							3、网站显示提现成功仅代表已提交给银行处理，款项将在显示成功后24小时内到账；
						</p>
						<p class="pl40 c_gray pr40" style="line-height: 20px;">
							4、提现工作日处理，周六周日等节假日的提现顺延到下一个工作日处理；
						</p>
						<p class="pl40 c_gray pr40" style="line-height: 20px;">
							5、当天18点之前的提现，当天处理，24小时内到账，18点后的提现顺延到第二天工作日处理；
						</p>
						<p class="pl40 c_gray pr40" style="line-height: 20px;">
							6、提现超过24小时未到账请联系QQ客服，点击导航“提现未到账处理流程”。
						</p>
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
					<col width="150"/>
					<col width="90"/>
					<col width="100"/>
					<col width="70"/>
					<col width="230"/>
					<col width="120"/>
					<col width="100"/>
				</colgroup>
				<tr>
					<th>提现时间</th>
					<th>提现方式</th>
					<th>提交金额</th>
					<th>手续费</th>
					<th>提现帐户</th>
					<th>备注号</th>
					<th>状态</th>
				</tr>
				<c:choose>
					<c:when test="${empty lines || 0 == fn:length(lines)}">
						<tr><td colspan="7">您暂时没有提现数据</td></tr>
					</c:when>
					<c:otherwise>
						<c:forEach items="${lines}" var="line">
							<tr>
								<td><fmt:formatDate value="${line.fcreateTime}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
								<td>${line.fBank}</td>
								<td><span class="fl pl20">￥${fns:formatCNY(line.famount)}</span></td>
								<td><span class="fl pl10">￥${fns:formatCNY(line.ffees)}</span></td>
								<td>姓名:${sessionScope.login_user.frealName}<br>帐号:${line.fAccount}</td>
								<td>${line.fid}</td>
								<td>
									<c:choose>
										<c:when test="${1 == line.fstatus}">
											<span class="c_orange">${line.fstatus_s}</span><span class="space5"></span>|<span class="space5"></span><a href="javascript:void(0)" onclick="cancelWithdrawCny(${line.fid});" class="c_orange">取消</a>
										</c:when>
										<c:when test="${2 == line.fstatus}">
											<span class="c_red" title="平台已将款汇往您的账户，如暂时没有收到款项，是银行系统延时，请稍作等待">${line.fstatus_s}</span>
										</c:when>
										<c:when test="${3 == line.fstatus}">
											<span class="c_green" title="平台已将款汇往您的账户，如暂时没有收到款项，是银行系统延时，请稍作等待">${line.fstatus_s}</span>
										</c:when>
										<c:when test="${4 == line.fstatus}">
											<span class="c_gray" >${line.fstatus_s}</span>
										</c:when>
										<c:otherwise>
											${line.fstatus_s}
										</c:otherwise>
									</c:choose>
								</td>
							</tr>
						</c:forEach>
					</c:otherwise>
				</c:choose>
			</table>
			<page:pagination pageSzie="${pageSize}" pageNow="${pageNow}" total="${total}" href="/account/withdrawCny.html?currentPage=#pageNumber"/>
		</div>
	</div>
	<div class="cb"></div>
	<!--  -->
	<!-- 添加银行卡信息 -->
	<div id="tm_yy" class="dn"></div>
	<!-- 添加银行卡弹出窗 -->
	<input type="hidden" value="1" name="cnyOutType" id="cnyOutType">
	<input type="hidden" id="addressType" value="1">
	<div id="bank_card_box" class="float_box dn" style="top:200px;left:500px;">
		<div class="f_title pl10">
			<h3 class="db fl">添加银行卡</h3>
			<i class="iconfont c_gray db fr close">&#xe609;</i>
		</div>
		<div class="f_content">
			<p id="openBankTypeAddrLi">
				<span class="db fl">银行：</span>
				<span id="selectBanks" class="selector fl">
					<span class="selector_item">
						<span class="sel_item">银行</span>
						<input type="hidden" id="openBankTypeAddr" name="openBankType" value="">
					</span>
					<span class="selector_items dn">
						<c:forEach items="${bankTypes}" var="v" varStatus="vs">
							<a data-val="${vs.index+1}">${v}</a>
						</c:forEach>
					</span>
				</span>
			</p>
			<p id="openBankAddrLi">
				<span class="db fl">开户所在地：</span>
				<span id="provinceSelector" class="selector fl">
					<span class="selector_item">
						<span class="sel_item">请选择省</span>
						<input type="hidden" id="openBankProvinceAddr" name="openBankProvince">
					</span>
					<span class="selector_items dn">
						<a data-val="-1" data-id="">请选择省</a>
						<c:forEach items="${provinces}" var="v" varStatus="vs">
							<a data-val="${v.id}" data-id="${v.id}" class="<c:if test="${v.name == province}">cur</c:if>">${v.name}</a>
						</c:forEach>
					</span>
				</span>
				<span id="citySelector" class="selector fl ml10">
					<span class="selector_item">
						<span class="sel_item">请选择城市</span>
						<input type="hidden" name="openBankCity" id="openBankCityAddr">
					</span>
					<span class="selector_items dn" id="cityList">
						<a data-val="-1">请选择城市</a>
					</span>
				</span>
			</p>
			<p>
				<span id="openBankAddr" class="db fl">银行开户网点：</span>
				<input type="text" name="openBranchPoint" id="openBankBranchAddr" onpaste="return false" class="db fl pl5" style="width: 160px;"/>
				<a target="_blank" href="http://www.cardbaobao.com/wangdian/" class="db fl send bg_gray ml10 border_gray">网点查询</a>
			</p>
			<p>
				<span class="db fl" id="cnyAccountAddr">银行卡账号：</span>
				<input name="withdrawAccount" id="withdrawAccountAddr" type="text" class="db fl pl5" style="width: 160px;"/>
			</p>
			<p>
				<span id="cnyAccountAddr2" class="db fl">再次输入银行卡号：</span>
				<input type="text" onpaste="return false" maxlength="30" class="db fl pl5" id="withdrawAccountAddr2" style="width: 160px;"/>
			</p>
			<p id="payeeAddrLi">
				<span class="db fl">收款人姓名：</span>
				<input type="text" readonly="readonly" value="${fuser.frealName }" name="payee" id="payeeAddr" class="db fl pl5" style="width: 160px;"/>
			</p>
			<a title="确定" id="withdrawCnyAddressButton" href="javascript:submitWithdrawCnyAddress();" class="confirm bg_blue c_white">确认</a>
		</div>
	</div>
	<!-- 人民币提现结束 -->
</div>
<script  src="${resources}/static/js/region.js"></script>
<script  src="${resources}/static/js/utils/selector.js"></script>
<script  src="${resources}/static/js/account/cny_withdraw.js"></script>
<script>
	function drawNumber(dom){
		var $this = $(dom);
		var val = $this.val() * 1;
		var hasNum = $('#wallet_balance').val() * 1;
		if(!val){
			$this.val('');
		}else if(val > hasNum){
			$this.val(parseInt(hasNum));
		}else{
			$this.val(parseInt(val));
		}
	}
</script>
<%@ include file="../common/footer.jsp"%>
</body>
</html>
