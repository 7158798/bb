<!-- 风险提示 author:xxp 2016-04-20 -->
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
	<link rel="stylesheet" href="${resources}/static/css/register.css"/>
</head>
<body>
<%@include file="../common/header.jsp" %>
<div class="center_page registe_risk f14">
	<h1 class="f20 tac">风险提示</h1>
	<p class="c_red fir">注意：根椐国家有关部委的规定，比特币等区块链加密数字资产属于虚拟商品，用户在自担风险的前提下可以自由参与。然而区块链加密数字资产交易具有诸多不可控的风险因素（如预挖、暴涨暴跌、庄家操控、团队解散、技术缺陷等），从而导致交易具有极高的风险。比特家交易网作为第三方平台，仅为区块链加密数字资产的爱好者提供一个自由的网上交换中介场所，对在平台上展示和交换的区块链加密数字资产不承担任何审查、担保、赔偿的法律责任。如果您不能接受，请不要使用比特家交易网所提供的服务！谢谢！</p>
	<p class="mt20 text">1.区块链加密数字资产主要由投机者大量使用，零售和商业市场使用相对较少，因此其价格易产生波动，并进而对区块链加密数字资产投资产生不利影响，用户需自行评估和承担风险。</p>
	<p class="text">2.区块链加密数字资产市场没有像中国股市那样的涨跌停限制，同时交易是24小时开放的。区块链加密数字资产由于筹码较少，价格易受到庄家控制，有可能出现一天价格涨几倍的情况，同时也可能出现一天内价格跌去一半的情况，甚至可能出现价值归零的情况。</p>
	<p class="text">3.对于项目运营方的市场承诺，包括所呈现的内容，需要慎重判断，目前并没有相关法律能保证其兑现承诺，比特家平台不会对任何区块链加密数字资产进行承诺保证等。</p>
	<p class="text">4.依《中华人民共和国反洗钱法》等有关法律法规的规定，严禁利用本平台从事洗钱、诈骗、商业贿赂等违法犯罪活动，一经发现，比特家平台将立即报送公安机关处理，并配合相关执法部门进行取证。</p>
	<p class="text">5.严厉打击传销组织，请比特家平台用户杜绝参与此类违法活动，否则造成的一切后果自负，一经发现，比特家平台将立即报送公安机关处理，并配合相关执法部门进行取证。</p>
	<p class="text">6.在比特家注册参与区块链加密数字资产交易的用户，务必保证所注册身份信息的真实性和准确性，因注册信息不真实而造成的任何问题，本平台概不负责。</p>
	<p class="text">7.如因国家法律法规及政策性文件规定，导致区块链加密数字资产的交易被禁止，由此导致的全部损失由用户自行承担，请谨慎参与。</p>
	<p class="text">8.请控制风险，不要投入超过您风险承受能力的资金，不要购买您所不了解的区块链加密数字资产，不要参与您所不了解的项目。</p>
	<p class="text">9.比特家对平台上区块链加密数字资产不做任何担保和负担赔偿等责任，它们可能出现的风险（如团队解散、技术故障、庄家或团队恶意操盘等）均需有用户自行承担，请用户控制风险、谨慎投资！</p>
	<p class="text c_red">10.新注册的用户，Bithome和比特家用户可以互通使用，无需重复注册。</p>
	<div class="sel mt30 f14">
		<i class="iconfont cp" id="choose" data-flag="0">&#xe602;</i>
		<span>我已经认真阅读以上风险提示，并已同意比特家<a href="/about/index.html?id=2" class="c_blue">服务条款</a>，同意在自担风险，自担损失的情况下参与交易。</span>
	</div>
	<a href="javascript:void(0);" id="confirm" class="confirm f22 bg_ccc c_white tac">继续注册</a>
</div>
<script>
	$(function(){
		$("#choose").click(function(){
			var flag=$(this).data("flag");
			var $this=$(this);
			if(flag){
				$this.data("flag",0);
				$this.html("&#xe602;");
				$("#confirm").removeClass("bg_blue").addClass("bg_ccc");
			}else{
				$this.data("flag",1);
				$this.html("&#xe600;");
				$("#confirm").removeClass("bg_ccc").addClass("bg_blue");
			}
		});
		$("#confirm").click(function(){
			var flag=$("#choose").data("flag");
			if(flag==0){
				return;
			}
			location.href="/user/reg.html" + location.search;
		});
	});
</script>
<%@ include file="../common/footer.jsp" %>
</body>
</html>

