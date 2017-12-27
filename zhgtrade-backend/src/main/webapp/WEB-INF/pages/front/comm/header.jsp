<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="include.inc.jsp"%>
<script type="text/javascript" src="${staticFileUrl}/static/front/js/script.js"></script>

<div class="foBox" id="leftQQ">
	<ul>
	<c:if test="${isSign == false }">
	<li><a href="javascript:userSign();"><img src="${staticFileUrl}/static/front/images/sign.png" alt="" height="65" width="65"></a></li>
	</c:if>
		<li><a target="_blank" href="http://wpa.qq.com/msgrd?v=3&uin=${requestScope.constant['serviceQQ'] }&menu=yes" title=""><img src="${staticFileUrl}/static/front/images/fo_qq.png" alt="" height="65" width="65"></a></li>
		<li><a href="#" title=""><img src="${staticFileUrl}/static/front/images/fo_ewm.png" alt="" height="65" width="65"></a><div style="left: 80px;display: block;" class="ewm"><img src="${staticFileUrl}/static/front/images/fo_ewm_d.png" alt="" height="221" width="221"></div></li>
	
	</ul>
</div>

<div id="logox">
<div id="logo">
  <ul style="width:725px;">
    <li class="${selectMenu=='index'?'cur':'' }"><span><a href="/">首页</a></span></li>
	<li class="${selectMenu=='trade'?'cur':'' }"><a id="deal_center"><span onclick="javascript:NavMenu()">交易中心</span>
	</a>
	<div style="display: none;" class="deal_list " id="deal_list1">
		<dl>
			<c:forEach items="${realTimePrize }" var="v" varStatus="vs">
			<c:if test="${v.fisShare==true }">		
			<dd>
			<i class="deal_list_pic_drk" 
			style="float: left;
			width: 25px;
			height: 25px;
			margin-top: 11px;
			background: transparent url('${staticFileUrl}${v.furl}') no-repeat scroll 0% 0%;"></i>
			<a href="/trade/coin.html?coinType=${v.fid }&tradeType=0">${v.fname_sn }</a>
			</dd>
			</c:if>
			</c:forEach>			
		</dl>
		<div class="sj"></div>
		<div class="nocontent"></div>
	</div>
	</li>

    <li class="${selectMenu=='financial'?'cur':'' }"><span><a href="/financial/index.html">财务中心</a></span></li>
    <!--<li class="${selectMenu=='appcenter'?'cur':'' }"><span><a href="/trade/subscriptionrmb.html">点我众筹</a></span></li>-->
    <li class="${selectMenu=='shop'?'cur':'' }"><span><a href="/shop/index.html">新品抢购</a></span></li>
    <li class="${selectMenu=='appcenter'?'cur':'' }"><span><a href="/gamecenter/game.html">应用中心</a></span></li>
    <li class="${selectMenu=='appcenter'?'cur':'' }"><span><a href="http://www.zcfunding.com">众筹平台</a></span></li>
    <li class="${selectMenu=='market'?'cur':'' }"><span><a href="/market.html">实时行情</a></span></li>
    <li class="${selectMenu=='vote'?'cur':'' }"><span><a href="/voteList.html">新币投票</a></span></li>
    <li class="${selectMenu=='service'?'cur':'' }"><span><a href="/service/ourService.html">信息中心</a></span></li> 
  </ul>
<a href="/"><img src="${staticFileUrl}${requestScope.constant['logoImage'] }" width="230" height="80"></a>
</div>
</div>
