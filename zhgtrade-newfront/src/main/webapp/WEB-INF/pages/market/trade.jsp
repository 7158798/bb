<!-- 实时行情 author:xxp 2016-04-25 -->
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="../common/includes.jsp" %>
<!DOCTYPE HTML>
<html>
<head>
	<title>${vdata.fname}￥${last} - ${fns:getProperty('site_title')}</title>
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
	<%--<link rel="stylesheet" href="${resources}/static/css/common/animate.css"/>--%>
	<link rel="stylesheet" href="${resources}/static/css/market.css"/>
	<style>
		::selection{background:transparent;}
		#detail-div ::selection{background:#08a3d7;color:#fff;}
		input::selection{background:#08a3d7;color:#fff;}
	</style>
</head>
<body>
<%@ include file="../common/header.jsp"%>
<div class="market_wrapper">
	<input type="hidden" id="symbol" value="${vdata.fid}"/>
	<div class="center_page">
		<div class="coin_wrapper f14">
			<div class="coin_container cp fl">
				<div class="coin_title">
					<img class="coin_img db fl" style="margin: 20px;" height="47" width="47" src="${cdn}${vdata.furl}" />
					<p class="db fl pl5 ellipsis coinname">
						<span class="db text text1 ellipsis" title="${vdata.fname_sn}">${vdata.fname_sn}</span>
						<span class="db text f12 text2 <c:if test="${isCollected==1}">cur</c:if>" id="collect" <c:choose><c:when test="${isCollected==1}">data-flag="1"</c:when><c:otherwise>data-flag="0"</c:otherwise></c:choose>>
							<i class="iconfont">&#xe638;</i>
							<span><c:choose><c:when test="${isCollected==1}">已收藏</c:when><c:otherwise>收藏</c:otherwise></c:choose></span>
						</span>
					</p>
					<i class="db fl iconfont rotate_icon c_blue mr25 cp" id="coin_sel">&#xe611;</i>
				</div>
				<div class="coin_sel_box dn">
					<ul>
						<c:set var="vlen" value="${fn:length(vlist)}"/>
						<c:forEach items="${vlist}" var="v" varStatus="s">
							<c:set var="sty" value=""/>
							<c:if test="${s.index >= (vlen%4 == 0 ? vlen-4 : vlen-vlen%4)}">
								<c:set var="sty" value="style='border: 0'"/>
							</c:if>
							<li ${sty} i="${(vlen%4 == 0 ? vlen-4 : vlen-vlen%4)}">
								<a href="/market/trade.html?symbol=${v.fid}" title="${v.fname_sn}" >
									<img class="coin_img db fl" alt="" height="47" width="47" data-src="${cdn}${v.furl}" />
									<p class="db pl5 ellipsis coinname" style="line-height: 22px;padding-top: 15px;">${v.fname_sn}</p>
									<span class="db pl5 ellipsis coinname" style="line-height: 20px;color: #e55600;">￥${v.lastDealPrize}</span>
								</a>
							</li>
						</c:forEach>
					</ul>
				</div>
			</div>

			<div class="price c_blue f30 fl" id="last-price">${last}</div>
			<div class="data fl">
				<ul>
					<li>
						<p class="f22 c_red" id="high-price">${high}</p>
						<p>最高价</p>
					</li>
					<li>
						<p class="f22 c_green" id="low-price">${low}</p>
						<p>最低价</p>
					</li>
					<li>
						<p class="f22" id="buy-price">${buy}</p>
						<p>买价</p>
					</li>
					<li>
						<p class="f22" id="sell-price">${sell}</p>
						<p>卖价</p>
					</li>
					<li>
						<p class="f22" id="vol-price">${vol}</p>
						<p>成交量</p>
					</li>
				</ul>
			</div>
		</div>
	</div>
</div>
<div class="center_page">
	<c:if test="${!empty tradeTips}">
		<div class="info mt10" style="height: 20px;line-height: 20px;">
			<i class="db fl iconfont c_red"></i>
			<span class="db fl f12 c_red ml5">${tradeTips}</span>
		</div>
	</c:if>
	<div class="cb"></div>
	<ul id="title_sel" class="f16 mt5">
		<li class="fl cur">交易${vdata.fname}</li>
		<li class="fl">${vdata.fname}行情</li>
		<li class="fl">了解${vdata.fname}</li>
		<li class="fl">市场动态</li>
		<%--<li class="fl">行情对比</li>
		<li class="fl">币对冲</li>--%>
	</ul>
	<ul id="content_sel">
		<!-- 交易币 -->
		<li style="cursor:pointer;">
			<div class="kline">
				<div id="container" style="height:490px; min-width:310px; padding: 6px 0px;">
					<div class="marketImageNew" id="marketImageNew" style="display: block;">
						<iframe frameborder="0" border="0" width="100%" height="490" id="klineFullScreen" data-src="/market/kline.html?symbol=${symbol }"></iframe>
						<%--<a class="openfullscreen" id="openfullscreen" href="javascript:void(0)" onclick="javascropt:klineFullScreenOpen()" title="全屏" style="display:block;"></a>--%>
						<%--<a class="closefullscreen" id="closefullscreen" href="javascript:void(0)" onclick="javascropt:klineFullScreenClose()" title="退出全屏" style="display:none"></a>--%>
					</div>
				</div>
			</div>
			<div class="trade_wrapper fl">
				<div class="item buy_item fl" id="buy-content">

				</div>
				<div class="item sell_item fl" id="sell-content">

				</div>
			</div>
			<div class="info fr">
				<!-- 金钱信息 -->
				<div class="money_info" id="user-wallet">

				</div>
				<!-- 未成交 -->
				<div class="dealing deal tac" id="not-deal">

				</div>
				<!-- 已成交 -->
				<div class="dealed deal tac" id="deal-log">

				</div>
			</div>
			<div class="cb"></div>
		</li>
		<!-- 币行情 -->
		<li class="dn">
			<div class="quotes tac " style="margin-bottom: 40px; line-height:500px;padding-top: 20px;" id="market-charts">

			</div>
		</li>
		<!-- 了解币 -->
		<li class="dn" id="detail-div">

		</li>
		<!-- 市场动态 -->
		<li class="dn" id="news-div">

		</li>
		<!-- 行情对比 -->
		<%--<li class="dn" id="compare-div">
			<%@ include file="compare.jsp"%>
		</li>--%>
		<!-- 币对冲 -->
		<%--<li class="dn" id="hedging-div">
			<%@ include file="hedging.jsp"%>
		</li>--%>
	</ul>
	<!-- 买盘 买盘 成交记录 -->
	<div class="entrust_wrapper tac">
		<div class="fl entrust_container">
			<div class="title f20">委托信息</div>

			<div style="position: absolute;margin-top: -50px;margin-left: 188px;">
				<span style="padding:14px 5px;float:left;font-size: 14px;">深度合并</span>
				<select style="margin-top:10px;float:left;font-size: 14px;" id="deep_select_area">
					<option value="4" selected="">默认</option>
					<option value="3">保留三位小数</option>
					<option value="2">保留两位小数</option>
					<option value="1">保留一位小数</option>
					<option value="0">取整</option>
				</select>
			</div>

			<div class="content">
				<p class="fir bg_gray">
					<span class="db t1 fl">买入</span>
					<span class="db t2 fl">价格</span>
					<span class="db t3 fl">数量</span>
					<span class="db t4 fl">折合人民币</span>
				</p>
				<div id="buyList">
				</div>
			</div>
		</div>
		<div class="fl entrust_container">
			<div class="title f20">

			</div>

			<div class="content">
				<p class="fir bg_gray">
					<span class="db t1 fl">卖出</span>
					<span class="db t2 fl">价格</span>
					<span class="db t3 fl">数量</span>
					<span class="db t4 fl">折合人民币</span>
				</p>
				<div id="sellList">
				</div>
			</div>
		</div>
		<div class="fl entrust_container entrust_log">
			<div class="title f20">成交历史</div>
			<div class="content">
				<p class="fir bg_gray">
					<span class="db lt1 fl">成交时间</span>
					<span class="db lt2 fl">买/卖</span>
					<span class="db lt3 fl">成交价</span>
					<span class="db lt4 fl">成交量</span>
					<span class="db lt5 fl">总金额</span>
				</p>
				<div id="logList">
				</div>
			</div>
		</div>
		<div class="cb"></div>
	</div>
</div>
<script type="text/javascript">
	var vtype = {
		fname: '${vdata.fname}',
		sname: '${vdata.fShortName}',
		symbol: '${vdata.fid}'
	}
</script>

<%--<script type="text/javascript" src="//cdn.bootcss.com/react/0.14.7/react.js"></script>--%>
<%--<script type="text/javascript" src="//cdn.bootcss.com/react/0.14.7/react-dom.js"></script>--%>

<%--<script src="${resources}/static/js/market/common.js"></script>--%>

<%--<script src="http://cdn.bootcss.com/socket.io/1.4.6/socket.io.min.js"></script>--%>

<%--<script src="${resources}/static/js/year_red.js"></script>--%>
<script src="${resources}/static/js/market/trade.js?v=1.7"></script>

<%--<script src="http://localhost/1.0.0/trade.js"></script>--%>

<script type="text/javascript">
	!function(){
		var $iframe = document.getElementById('klineFullScreen');
		$iframe.setAttribute('src', $iframe.getAttribute('data-src'));
	}()
</script>
<script>
	!function(){
	    var is_load_icons = false
        $(".coin_container").hover(function(){
            if (!is_load_icons) {
                is_load_icons = true
				$(this).find('img').each(function(){
				    $(this).attr('src', $(this).attr('data-src'))
				})
			}
		})
	}()
</script>
<script src="${resources}/static/js/market/collect.js"></script>


<%--<script src="http://localhost/js/lib/react.js"></script>--%>
<%--<script src="http://localhost/js/lib/react-dom.js"></script>--%>
<%--<script src="http://localhost/1.0.0/common.js"></script>--%>


<%--<script src="${resources}/static/js/kline/highstock.js"></script>--%>

<%--<script src="/static/js/market/trade.js"></script>--%>

<%--<script src="static/js/kline/kline.js"></script>--%>

<%@ include file="../common/footer.jsp"%>
</body>
</html>

