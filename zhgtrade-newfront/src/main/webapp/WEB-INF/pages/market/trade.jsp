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
	<link rel="stylesheet" type="text/css" href="${resources}/static/css/exchange.css?v=11111">
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
	<%--<c:if test="${!empty tradeTips}">
		<div class="info mt10" style="height: 20px;line-height: 20px;">
			<i class="db fl iconfont c_red"></i>
			<span class="db fl f12 c_red ml5">${tradeTips}</span>
		</div>
	</c:if>--%>
	<div class="cb"></div>
	<ul id="title_sel" class="f16 mt5">
		<li class="fl cur">交易${vdata.fname}</li>
		<%--<li class="fl">${vdata.fname}行情</li>
		<li class="fl">了解${vdata.fname}</li>
		<li class="fl">市场动态</li>--%>
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
		<%--<!-- 币行情 -->
		<li class="dn">
			<div class="quotes tac " style="margin-bottom: 40px; line-height:500px;padding-top: 20px;" id="market-charts">

			</div>
		</li>
		<!-- 了解币 -->
		<li class="dn" id="detail-div">

		</li>
		<!-- 市场动态 -->
		<li class="dn" id="news-div">

		</li>--%>
		<!-- 行情对比 -->
		<%--<li class="dn" id="compare-div">
			<%@ include file="compare.jsp"%>
		</li>--%>
		<!-- 币对冲 -->
		<%--<li class="dn" id="hedging-div">
			<%@ include file="hedging.jsp"%>
		</li>--%>
	</ul>

		<%--新增样式，搞不定直接注释掉--%>
		<div class="trade_wrap"><!-- unlogin trade_panel -->
			<div class="trade_panel trade_panel_logout">
				<div class="mod mod_trade" id="mod_trade_logout">
					<div class="mod_hd clear_fix">
						<div class="fees_rule"><p><a href="/about/fee" target="_blank">费率</a></p></div>
					</div>
					<div class="mod_bd clear_fix">
						<div class="panel">
							<div class="hd"><a class="logout" href="/zh-cn/login/">登录</a> 或 <a class="logout"
																							   href="/zh-cn/register/">注册</a>
								开始交易
							</div>
							<div class="bd">
								<div>
									<div class="input_text"><b class="label">买入价</b><label><input type="text"
									> <span
											class="upper unit" unit="show_buy_quote_logout">usdt</span></label><strong
											class="msg"></strong><!--<p class="math_price"></p>--></div>
									<div class="input_text input_text_amount"><b class="label">买入量</b><label><input type="text"
									>
                                <span class="unit u"><em class="uppercase"
														 lazyfill="">etc</em></span></label><strong
											class="msg"></strong></div>
									<div class="input_range limit_buy_logout buy_color"></div>
									<div class="amount_range uppercase"><span class="min"><span class="min_num">0</span><em
											lazyfill="" data-template=""> etc</em></span> <span
											class="max"><span class="max_num">0.0000</span><em lazyfill=""
																							   data-template=""> etc</em></span>
									</div>
									<div class="total"><p>交易额 <span></span></p>
										<p class="transform_total"></p></div>
									<div class="submit">
										<button  class="btn_buy color_buy_bg"
												 lazyfill="买入">
											买入etc
										</button>
									</div>
								</div>
							</div>
						</div>
						<div class="panel sell_panel">
							<div class="hd"><a class="logout" href="/zh-cn/login/">登录</a> 或 <a class="logout"
																							   href="/zh-cn/register/">注册</a>
								开始交易
							</div>
							<div class="bd">
								<div>
									<div class="input_text"><b class="label">卖出价</b><label><input type="text"
									> <span
											class="upper unit" unit="show_sell_quote_logout">usdt</span></label><strong
											class="msg"></strong><!--<p class="math_price"></p>--></div>
									<div class="input_text input_text_amount"><b class="label">卖出量</b><label><input type="text"
									>
                                <span class="unit"><em class="uppercase"
													   lazyfill="">etc</em></span></label><strong
											class="msg"></strong></div>
									<div class="input_range limit_sell_logout sell_color"></div>
									<div class="amount_range uppercase"><span class="min"><span class="min_num">0</span><em
											lazyfill="" data-template=""> etc</em></span> <span
											class="max"><span class="max_num">0.0000</span><em lazyfill=""
																							   data-template=" "> etc</em></span>
									</div>
									<div class="total"><p>交易额 <span></span></p>
										<p class="transform_total"></p></div>
									<div class="submit">
										<button  class="btn_sell color_sell_bg"
												 lazyfill="卖出">
											卖出etc
										</button>
									</div>
								</div>
							</div>
						</div>
					</div>

				</div>
			</div><!-- end unlogin trade_panel -->

			<div class="order_book table-container">
				<div class="head"><p class="title">最新价 <span id="tickerClose">33.53</span> <em class="uppercase"
																							   lazyfill="">usdt</em>
					<span id="tickerCny" class="ticker-transform">≈ 217.56 CNY</span></p></div>
				<div class="ex-depth">
					<div id="market_depth">
						<dl>
							<dt class="header"><span class="title"></span> <span class="price">价格(USDT)</span> <span
									class="amount">数量<em class="uppercase">(etc)</em></span> <span>累计<em
									class="uppercase">(etc)</em></span></dt>
							<dd data-info="33.84">
								<div class="inner"><span class="title color-sell">卖 7</span> <span class="price">33.84</span>
									<span class="amount">19.0000</span> <span>176.3547</span> <b class="color-sell-bg"
																								 style="width: 16.8858869534305%"></b>
								</div>
							</dd>
							<dd data-info="33.80">
								<div class="inner"><span class="title color-sell">卖 6</span> <span class="price">33.80</span>
									<span class="amount">13.3552</span> <span>157.3547</span> <b class="color-sell-bg"
																								 style="width: 11.869178812655528%"></b>
								</div>
							</dd>
							<dd data-info="33.78">
								<div class="inner"><span class="title color-sell">卖 5</span> <span class="price">33.78</span>
									<span class="amount">7.8795</span> <span>143.9995</span> <b class="color-sell-bg"
																								style="width: 7.002755065766086%"></b>
								</div>
							</dd>
							<dd data-info="33.77">
								<div class="inner"><span class="title color-sell">卖 4</span> <span class="price">33.77</span>
									<span class="amount">6.4000</span> <span>136.1200</span> <b class="color-sell-bg"
																								style="width: 5.6878777106292215%"></b>
								</div>
							</dd>
							<dd data-info="33.70">
								<div class="inner"><span class="title color-sell">卖 3</span> <span class="price">33.70</span>
									<span class="amount">13.0000</span> <span>129.7200</span> <b class="color-sell-bg"
																								 style="width: 11.553501599715608%"></b>
								</div>
							</dd>
							<dd data-info="33.69">
								<div class="inner"><span class="title color-sell">卖 2</span> <span class="price">33.69</span>
									<span class="amount">4.2000</span> <span>116.7200</span> <b class="color-sell-bg"
																								style="width: 3.732669747600427%"></b>
								</div>
							</dd>
							<dd data-info="33.68">
								<div class="inner"><span class="title color-sell">卖 1</span> <span class="price">33.68</span>
									<span class="amount">112.5200</span> <span>112.5200</span> <b class="color-sell-bg"
																								  style="width: 100%"></b></div>
							</dd>
						</dl>
						<div class="dl-hr">
							<hr>
							<hr>
						</div>
						<dl>
							<dd name="depth-item" data-info="33.48">
								<div class="inner"><span class="title color-buy">买 1</span> <span class="price">33.48</span>
									<span class="amount">59.3055</span> <span>59.3055</span> <b class="color-buy-bg"
																								style="width: 52.70662993245646%"></b>
								</div>
							</dd>
							<dd name="depth-item" data-info="33.47">
								<div class="inner"><span class="title color-buy">买 2</span> <span class="price">33.47</span>
									<span class="amount">26.0081</span> <span>85.3136</span> <b class="color-buy-bg"
																								style="width: 23.11420191965873%"></b>
								</div>
							</dd>
							<dd name="depth-item" data-info="33.45">
								<div class="inner"><span class="title color-buy">买 3</span> <span class="price">33.45</span>
									<span class="amount">9.6576</span> <span>94.9712</span> <b class="color-buy-bg"
																							   style="width: 8.583007465339495%"></b>
								</div>
							</dd>
							<dd name="depth-item" data-info="33.42">
								<div class="inner"><span class="title color-buy">买 4</span> <span class="price">33.42</span>
									<span class="amount">35.0081</span> <span>129.9793</span> <b class="color-buy-bg"
																								 style="width: 31.11277995023107%"></b>
								</div>
							</dd>
							<dd name="depth-item" data-info="33.41">
								<div class="inner"><span class="title color-buy">买 5</span> <span class="price">33.41</span>
									<span class="amount">26.8000</span> <span>156.7793</span> <b class="color-buy-bg"
																								 style="width: 23.817987913259866%"></b>
								</div>
							</dd>
							<dd name="depth-item" data-info="33.40">
								<div class="inner"><span class="title color-buy">买 6</span> <span class="price">33.40</span>
									<span class="amount">0.3836</span> <span>157.1629</span> <b class="color-buy-bg"
																								style="width: 0.34091717028083895%"></b>
								</div>
							</dd>
							<dd name="depth-item" data-info="33.38">
								<div class="inner"><span class="title color-buy">买 7</span> <span class="price">33.38</span>
									<span class="amount">6.4000</span> <span>163.5629</span> <b class="color-buy-bg"
																								style="width: 5.6878777106292215%"></b>
								</div>
							</dd>
						</dl>
					</div>
					<div class="link-group clearFix">
						<div id="depth_select" class="depth-select">深度 <span id="depth_step">0.000001</span>
							<ul>
								<li class="active" data-depth="0">0.000001</li>
								<li data-depth="1">0.00001</li>
								<li data-depth="2">0.0001</li>
							</ul>
						</div>
						<a href="../depth/?trade=exchange" lazyfill="attr" data-attr="href"
						   data-template="../depth/?trade=>">更多</a></div>
				</div>
			</div>
		</div>


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

