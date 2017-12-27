<!-- 虚拟币充值页面author:yujie 2016-04-23 -->
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
	<link rel="stylesheet" href="${resources}/static/css/common/reset.css">
	<link rel="stylesheet" href="${resources}/static/css/common/style.css">
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
		<a href="javascript:void(0)" class="f12 c_gray">虚拟币充值</a>
	</div>
	<c:set var="dt_index" value="1"/>
	<c:set var="dd_index" value="4"/>
	<%@ include file="../common/account_left.jsp"%>
	<!-- 虚拟币充值开始 -->
	<div id="container" class="account_right fl">
		<div class="finance_wrapper">
			<div class="finance_container">
				<h1 class="ml40">虚拟币充值</h1>
				<div class="content">
					<div class="coin_wrapper">
						<!-- 二维码开始 -->
						<input type="hidden" id="virtualCoinAddress" name="virtualCoinAddress" value="${fvirtualaddress.fadderess}">
						<div id="qrcodeCanvas" style="display: none;"></div>
						<img id="qrcode" class="erweima" alt="" title="" height="110" width="110" />
						<!-- 二维码结束 -->
						<div class="coin_container  fl">
							<img class="coin_img db fl" alt="" height="20" width="20" title="${fvirtualcointype.fname}" src="${cdn}${fvirtualcointype.furl}" />
							<span class="db fl pl5 ellipsis coinname">${fvirtualcointype.fname}</span>
							<i class="db fl iconfont rotate_icon c_blue ml40 cp" id="coin_sel">&#xe611;</i>
							<div class="coin_sel_box bg_white dn">
								<ul>
									<c:forEach items="${fvirtualcointypes}" var="type">
										<c:if test="${type.FIsWithDraw}">
											<li>
												<a data-pjax="#container" href="/account/chargeBtc.html?symbol=${type.fid}">
													<img class="coin_img db fl" alt="" height="20" width="20" title="${type.fname}" src="${cdn}${type.furl}" />
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
					<p class="c_title_other">请将虚拟币转至如下地址：</p>
					<p class="address pl20 f24 ml40 c_blue">
						${fvirtualaddress.fadderess}
					</p>
					<div class="info ml40 f12">
						<p class="c_gray">重要提示：本站已经支持往同一地址多次充值，我们将在收到<span class="c_red">3次确认</span>以后为您充值成功。</p>
					</div>
				</div>
			</div>
		</div>
		<!-- 充值记录 -->
		<div class="Tentitle ml20">
			充值记录
		</div>
		<div id="Tenbody" class="Tenbody">
			<table>
				<colgroup>
					<col width="360" />
					<col width="165" />
					<col width="220" />
					<col width="130" />
				</colgroup>
				<tr>
					<th>充值地址</th>
					<th>充值金额</th>
					<th>最后更新</th>
					<th>充值状态</th>
				</tr>
				<c:if test="${empty fvirtualcaptualoperations || 0 == fn:length(fvirtualcaptualoperations)}"><tr><td colspan="4">您暂时没有充值数据</td></tr></c:if>
				<c:forEach items="${fvirtualcaptualoperations}" var="line">
					<tr>
						<td><span class="fl pl50">${line.recharge_virtual_address}</span></td>
						<td><span class="fl pl50">${fns:formatCoin(line.famount)}</span></td>
						<td><fmt:formatDate value="${line.flastUpdateTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
						<c:choose>
							<c:when test="${3 == line.fstatus}">
								<td class="c_green">${line.fstatus_s}</td>
							</c:when>
							<c:otherwise>
								<td class="c_red">${line.fstatus_s}</td>
							</c:otherwise>
						</c:choose>
					</tr>
				</c:forEach>
			</table>
			<page:pagination pageSzie="${pageSize}" pageNow="${pageNow}" total="${total}" href="/account/chargeBtc.html?currentPage=#pageNumber&symbol=${fvirtualcointype.fid}"/>
		</div>
	</div>
	<div class="cb"></div>
	<!-- 虚拟币充值结束 -->
</div>
<script type="text/javascript" src="${resources}/static/js/jquery/jquery.qrcode.min.js?rand=20140226b"></script>
<%@ include file="../common/footer.jsp"%>
<script>
	function loadQrcode(){
		var address = $("#virtualCoinAddress").val();
		if(navigator.userAgent.indexOf("MSIE")>0) {
			jQuery('#qrcodeCanvas').qrcode({text:address,width:"110",height:"110",render:"table"});
		} else{
			jQuery('#qrcodeCanvas').qrcode({text:address,width:"110",height:"110"});
		}
		try{
			var image = document.getElementById("qrcode");
			var canvas = document.getElementById("qrcodeCanvas").getElementsByTagName("canvas")[0];
			image.src = canvas.toDataURL("image/png");
		}catch (e){
			console.log(e);
		}
	}

	$(function(){
		$("#container").on("mouseenter",".coin_container",function(){
			$(".coin_sel_box").show();
		});
		$("#container").on("mouseleave",".coin_container",function(){
			$(".coin_sel_box").hide();
		});

		loadQrcode();

		if($.support.pjax){
			$(document).on('pjax:end', function(){
				loadQrcode();
			});
		}
	});
</script>
</body>
</html>
