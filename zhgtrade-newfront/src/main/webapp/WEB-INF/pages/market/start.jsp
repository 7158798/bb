<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="../common/includes.jsp"%>
<%
	String basePath2 = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort();
%>
<!doctype html>
<html>
<head> 
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>${fns:getProperty('site_title')}</title>
	<meta http-equiv=X-UA-Compatible content="IE=edge,chrome=1">
	<meta content=always name=referrer>
	<meta name='renderer' content='webkit' />
<meta name="description" content="${fns:getProperty('site_keywords')}"/>
<meta name="keywords" content="${fns:getProperty('site_description')}" />
<link href="${resources}/static/css/kline/app_1.css" rel=stylesheet type=text/css />
<link href="${resources}/static/css/kline/font-awesome.css" rel=stylesheet type=text/css />
</head>
<body>
<div id="header_outer"><div id="header">

	<div class="navbar navbar-static-top"><div class="navbar-inner"><div class="container">

		<div id="warning"></div>
	</div></div></div>
	<div id="qr"><img /></div>

	<div id="control"><div class="inner"><ul id="periods" class="horiz"><li style='line-height:26px'>K线时间段:</li><li class="period"><a>1周</a></li><li class="subsep"></li><li class="period"><a>3天</a></li><li class="period"><a>1天</a></li><li class="subsep"></li><li class="period"><a>12小时</a></li><li class="period"><a>6小时</a></li><li class="period"><a>4小时</a></li><li class="period"><a>2小时</a></li><li class="period"><a>1小时</a></li><li class="subsep"></li><li class="period"><a>30分</a></li><li class="period"><a>15分</a></li><li class="period"><a>5分</a></li><li class="period"><a>3分</a></li><li class="period"><a>1分</a></li><li class="subsep"></li><li>
	<div class="dropdown">
	<div class="t">
		页面设置
	</div>
	<div class="dropdown-data"><table class="nowrap simple settings">
		<tr class=main_lines>
			<td>均线设置</td>
			<td><ul id="setting_main_lines">
			<li value=mas>MA</li>
			<li value=emas>EMA</li>
			<li value=none>关闭均线</li></ul></td>
		</tr>
		<tr class=stick_style>
			<td>图线样式</td>
			<td><ul id="setting_stick_style">
			<li value=candle_stick>K线-OHLC</li>
			<li value=candle_stick_hlc>K线-HLC</li>
			<li value=ohlc>OHLC</li>
			<li value=line>单线</li>
			<li value=line_o>单线-o</li>
			<li value=none>关闭线图</li>
			</ul></td>
		</tr>
		<tr class=line_style>
			<td>Line Style</td>
			<td><ul id="setting_ls">
			<li value=c>Close</li>
			<li value=m>Median Price</li>
			</ul></td>
		</tr>
		<tr class=indicator>
			<td>技术指标</td>
			<td><ul id="setting_indicator">
			<li value=macd>MACD</li>
			<li value=kdj>KDJ</li>
			<li value=stoch_rsi>StochRSI</li>
			<li value=none>关闭指标</li>
			</ul></td>
		</tr>
		<tr class=scale>
			<td>线图比例</td>
			<td><ul id="setting_scale">
			<li value=normal>普通K线</li>
			<li value=logarithmic>对数K线</li>
			</ul></td>
		</tr>
	<!-- 	<tr class=theme>
			<td>Theme</td>
			<td><ul id="setting_theme"><li value=dark>Dark</li><li value=light>Light</li></ul></td>
		</tr> -->
		<tr>
			<td></td>
			<td colspan=2><a id="btn_settings">指标参数设置</a></td>
		</tr>
	</table></div>
</div>
<div class="dropdown">
	<div class="t">
		工具
	</div>

	<div class="dropdown-data"><table class="nowrap simple">
		<tr><td><a class="link_estimate_trading">估计交易</a></td></tr>
		<tr><td>
			<a class="mode" mode=draw_line>画线</a><br />
			<a class="mode" mode=draw_fhline>绘制斐波那契回调线</a>
			<br /><a class="mode" mode=draw_ffan>绘制斐波那契面</a><br />
			<a class="mode" mode=draw_fhlineex>绘制斐波那契扩展</a>
			<div class="hint">
				点击左键画点/线
				<br />点击右键清除
			</div>
		</td></tr>
	</table></div>
</div></li><li class="sep"></li><li id="mode">
	<a id="mode_cross" class="mode selected" title='Cross Cursor' mode=cross><img src="${cdn}/static/front/images/kline/shape-cross.png" /></a><a id="mode_draw_line" class="mode" title='Draw lines' mode=draw_line><img src="${cdn}/static/front/images/kline/shape-line.png" /></a><a id="mode_draw_fhline" class="mode" title='Draw Fibonacci Retracements' mode=draw_fhline><img src="${cdn}/static/front/images/kline/shape-fr.png" /></a><a id="mode_draw_ffan" class="mode" title='Draw Fibonacci Fans' mode=draw_ffan><img src="${cdn}/static/front/images/kline/shape-ffan.png" /></a>
</li><li class="sep"></li><li>
	已更新
	<span id="change">0</span>
	秒
	<span id="realtime_error" style='display:none'>in
	<abbr title="Realtime(WebSocket) connection failed. Orderbook update every 1 minute, Trades update every 10 seconds.">Slow Mode</abbr></span>
</li>
</ul></div></div>
</div></div>


<div id="loading"><div class="inner"><div class="text">Loading...</div></div></div>
<div id="notify" class="notify"><div class="inner"></div></div>
<div id="main">
	<div id="sidebar_outer" style="display:none"><div id="sidebar" style="display:none">
		<div id="before_trades" style="display:none">
			<div id="market"></div>
			<div id="orderbook"><div class="orderbook"><div id="asks"><div class="table"></div></div><div id="gasks"><div class="table"></div></div></div><div id="price" class="red">
</div><div class="orderbook"><div id="bids"><div class="table"></div></div><div id="gbids"><div class="table"></div></div></div></div>
		</div>

		<div id="trades"></div>
	</div></div>
	<div id="wrapper" class="hide_cursor"><canvas id="canvas_main"></canvas><canvas id="canvas_shapes" class="ab"></canvas><canvas id="canvas_cross" class="ab"></canvas><div id="chart_info"></div></div>
</div>


<div id="footer_outer"><div id="footer"><!-- <ul class="horiz donate"><li>
</li><li id="now"></li></ul> --></div></div>

<div id="assist" style='display:none'></div>


<div id="settings">
	<h2>技术指标参数设定</h2>
	<table>
		<tr id="indicator_price_mas">
			<th>
EMA / MA
				<abbr title="Up to 4 different indicators
Set field blank to remove one of the indicators.">?</abbr>
			</th>
			<td>
				<input name=price_mas />
				<input name=price_mas />
				<input name=price_mas />
				<input name=price_mas />
			</td>
			<td><button>默认值</button></td>
		</tr>
		<tr id="indicator_macd">
			<th>
MACD
				<abbr title="Short, Long, Move">?</abbr>
			</th>
			<td>
				<input name=macd />
				<input name=macd />
				<input name=macd />
			</td>
			<td><button>默认值</button></td>
		</tr>
		<tr id="indicator_kdj">
			<th>
KDJ
				<abbr title="rsv, k, d">?</abbr>
			</th>
			<td>
				<input name=kdj />
				<input name=kdj />
				<input name=kdj />
			</td>
			<td><button>默认值</button></td>
		</tr>
		<tr id="indicator_stoch_rsi">
			<th>
Stoch RSI``
				<abbr title="Params: Stochastic Length, RSI Length, K, D">?</abbr>
			</th>
			<td>
				<input name=stoch_rsi />
				<input name=stoch_rsi />
				<input name=stoch_rsi />
				<input name=stoch_rsi />
			</td>
			<td><button>默认值</button></td>
		</tr>
	</table>

	<div id="close_settings"><a>[ 关闭    ]</a></div>
</div>
<input type="hidden" id="hostHidden" value="<%=basePath2%>"> 

<script type=text/javascript>
	(function() {
	  window.$sid = "0c8aa717";

	  window.$time_fix = 60*1000;

  	var hostHidden = document.getElementById("hostHidden").value;
	  
	  window.$host = hostHidden;

	  window.$test = false;

	  <%--window.$symbol = "${fvirtualcointype.fid}";--%>
	  window.$symbol = location.search.replace('?symbol=','');

	  window.$hsymbol = "RUIZCON ${fvirtualcointype.fShortName}\/CNY";

//	  window.$theme_name = "dark";

        window.$theme_name = "dark";

	  window.$debug = false;

	  window.$settings = {"main_lines":{"id":"main_lines","name":"Main Indicator","default":"mas","options":{"MA":"mas","EMA":"emas","None":"none"}},"stick_style":{"id":"stick_style","name":"Chart Style","options":{"CandleStick":"candle_stick","CandleStickHLC":"candle_stick_hlc","OHLC":"ohlc","Line":"line","Line-o":"line_o","None":"none"}},"line_style":{"id":"ls","name":"Line Style","options":{"Close":"c","Median Price":"m"}},"indicator":{"id":"indicator","name":"Indicator","default":"none","options":{"MACD":"macd","KDJ":"kdj","StochRSI":"stoch_rsi","None":"none"}},"scale":{"id":"scale","name":"Scale","options":{"Normal":"normal","Logarithmic":"logarithmic"}},"theme":{"id":"theme","name":"Theme","options":{"Dark":"dark","Light":"light"},"refresh":true}};

	  window.$p = true;

	  window.$c_usdcny = 6.0463;

	  setTimeout(function() {
	    if (!window.$script_loaded) {
	      return document.getElementById('loading').innerHTML = "<div class=inner>正在加载脚本，请稍后...</div>";
	    }
	  }, 1000);

	}).call(this);
</script>
<script type="text/javascript">
	function showDepth(flag){
		if(flag==0){
			document.getElementById("sidebar_outer").style.display="";
			document.getElementById("sidebar").style.display="";
			document.getElementById("before_trades").style.display="";
		}else{
			document.getElementById("sidebar_outer").style.display="none";
			document.getElementById("sidebar").style.display="none";
			document.getElementById("before_trades").style.display="none";
		}
		
	}
</script>
<script type="text/javascript" src="${resources}/static/js/kline/kline1.js?"></script>
<%--<c:choose>--%>
	<%--<c:when test="${sessionScope['login_user'].fid == 38336}">--%>
		<script type="text/javascript" src="${resources}/static/js/kline/kline2.js"></script>
	<%--</c:when>--%>
	<%--<c:otherwise>--%>
	<%--</c:otherwise>--%>
<%--</c:choose>--%>
</body>
</html>

