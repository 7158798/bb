<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="comm/include.inc.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!doctype html>
<html>
<head>
<meta name="baidu-site-verification" content="hKAGFTwiQF" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<%@include file="comm/link.inc.jsp"%>
<link href="${staticFileUrl}/static/front/css/new_index.css" rel="stylesheet"
	type="text/css" media="screen, projection" />
<link href="${staticFileUrl}/static/front/css/index.css" rel="stylesheet"
	type="text/css" media="screen, projection" />
</head>
<body>

<%@include file="comm/header.jsp" %>

<div class="bigImage1 clear" id="bigImage1">
			<div>
			<ul class="J-slide" >
 				<li class="slide" style="display: ; position: absolute;" id="bannerBackgroundVote"> 
 					<a class="banner" style="background:url('${staticFileUrl}${requestScope.constant['bigImage1'] }') center no-repeat scroll;"></a> 
				</li> 
 				<li class="slide" style="display:none ; position: absolute;" id="bannerBackgroundVote"> 
 					<a class="banner" style="background:url('${staticFileUrl}${requestScope.constant['bigImage2'] }') center no-repeat scroll;"></a> 
				</li> 
 				<li class="slide" style="display:none ; position: absolute;" id="bannerBackgroundVote"> 
 					<a class="banner" style="background:url('${staticFileUrl}${requestScope.constant['bigImage3'] }') center no-repeat scroll;"></a> 
				</li>
			</ul>
		</div>
			<c:choose>
				<c:when test="${!empty(sessionScope.login_user)}">
					<div class="bge">
						<div class="dzbg"></div>
					<div class="dz">
						
							<div class="deng">欢迎您,${login_user.fnickName }
							</div>
							<div class="guzhi">
								<label for="textfield"></label> 资产估值：￥<fmt:formatNumber value="${totalCapital }" pattern="##.##"
		                          	maxIntegerDigits="10" maxFractionDigits="4" />
							</div>
							<div class="deng">
								<a href="/trade/coin.html" style="text-decoration: none;">
								<div class="access">进入交易中心</div>
								</a>
							</div>
							<div class="chongti">
								<span class="chongzhi"><a href="/account/rechargeCny.html">人民币充值</a></span>
								<span class="chongzhi"><a href="/account/withdrawCny.html">人民币提现</a></span>
							</div>
							<span class="tuichu"><a href="/user/logout.html">退出登录</a>
							</span>

							<div class="div">
								<a href="http://${requestScope.constant['weibo']}"
									target="_blank">
									<div class="weibo"></div> </a> <span class="activeEnter clear">
									<span class="okLink okLinkImg">
										<div class="weixin"></div> </span> <span style="display: none;"
									class="code codeImg"></span> </span>
							</div>




					</div>
					</div>
				</c:when>
				<c:when test="${!empty(sessionScope.second_login_user)}">
					<div class="bge">
						<div class="dz">
						
								  <div class="after-d">
								   		<span class="ttitle1">二次登录密码验证</span>
										<span class="ttitle2">${second_login_user.fnickName }</span>
										<span class="ttitle4">请输入您的谷歌验证码</span>
										<input type="text" onblur="trimValue(this);" onkeydown="callbackEnter(submitTotpCode);" class="secondVerification" name="totpCode" id="totpCode">
										<div class="errorbgDiv" id=""><span class="totpCodeError" style="display:none;" id="errorSpan">&nbsp;</span>&nbsp;</div>
										<a onclick="javascript:submitTotpCode();" title="" href="javascript:void(0);" class="button-blue1" id="">确&nbsp;&nbsp;认</a>
										<br>
										<a style="line-height:50px;" href="/index.html?index=1" class="lightorange2"><span style="font-size:12px;">切换账号登录</span></a>
								   </div>
							   
						</div>
					</div>
				</c:when>
				<c:otherwise>
				<div class="bge">
				<div class="dzbg"></div>
					<div class="dz">
						<div id="deng">
							<div class="deng">登录本站 </div>
							<div class="deng">
								<label for="textfield"></label> <input name="indexLoginName"
									placeholder="请输入邮箱地址" type="text" onfocus="javascript:indexLoginNameOnfocus();" onblur="indexLoginNameOnblur(this)" onkeydown="javascript:clearTig(this)" onkeyup="javascript:emailOnkeyUp(this);" class="wenben" id="indexLoginName" />
							</div>
							<div class="deng">
								<input name="indexLoginPwd" type="password" class="wenben" placeholder="请输入密码"
									id="indexLoginPwd"  onfocus="javascript:indexLoginOnblur();" 
									onkeydown="callbackEnter(loginIndexSubmit);"/>
							</div>
							<div class="deng">
								<a  onclick="javascript:loginIndexSubmit();">
								<div  class="Button-register" id="button"></div>
								</a>
								<a href="javascript:showlogin(1);" >
								<div class="Button-login" id="button2"></div>
								</a>
							</div>
							<div class="deng"><div id="indexLoginTips" class="errorbg" style="color:red;"></div></div>
							
						  <div class="div">
							<a href="http://${requestScope.constant['weibo']}" target="_blank">						  
							    <div class="weibo"></div>
							</a>
							<span class="activeEnter clear">
							<span  class="okLink okLinkImg">
							<div class="weixin"></div>
							</span>
							<span style="display: none;" class="code codeImg"></span>	
							</span>
						  </div>
							
							
						</div>
					</div>
				</div>
				
			</c:otherwise>
			</c:choose>
			
			<div class="controlBackground">
				<div id="J-slide-number" class="slide-number">
					<a class="slide-number-active" data-slide-to="0" data-target="#myCarousel">1</a>
					<a class="" data-slide-to="1" data-target="#myCarousel">2</a>
					<a class="" data-slide-to="2" data-target="#myCarousel">3</a> 
				</div>
			</div>
		</div>




	<div class="indexInfo">
		<div class="immediateVote">
			<div class="immediateVote">
				<span class="newsIcon"></span> <span class="">最新动态:</span> <a
					href="/service/article.html?id=${newArticle.fid }"
					class="newsContent">${newArticle.ftitle }</a> <a
					href="/service/ourService.html" class="newsMore"
					style="margin-left:10px;">更多...</a>
			</div>
		</div>
	</div>
	
<div id="zhong" style="height:280px;background: #F0F2F2 none repeat scroll 0% 0%;">
  <div id="zhong2">
    <h1>选择众股，安全可信赖</h1>
    <br/>
    <p>截止到目前，众股注册人数<span class="ziti">${userQty }</span>人,累计交易额<span class="ziti">￥${tradeAmt }</span></p>
    <div id="zhong21">
    <c:forEach items="${xx }" var="v">
    <img src="${staticFileUrl}/static/front/images/img_${v }.png" width="60" height="70">
    </c:forEach>
    </div>
  </div>
</div>
<div id="jiao1">
  <div id="jiao11">
    <ul>
      <li>币种</li>
      <li>最新成交价</li>
      <li>买价</li>
      <li>卖价</li>
      <li>24h成交量</li> 
      <li>总市值</li> 
      <li>日涨跌幅</li>
      <li>周涨跌幅</li>
    </ul>
  </div>
</div>

<c:forEach items="${realTimePrize }" var="v" varStatus="n"> 
<c:if test="${v.fisShare == true }">
<div id="jiao${(n.index+1)%2 ==0?'3':'2' }">
  <div id="jiao21">
    <ul>
      
      <li style="text-align: left;">
      <img src="${staticFileUrl}${v.furl }" style="width:25px;float: left;margin-right:5px;"/>
      <p>
      <!-- ${v.fname }(${v.fShortName }) -->
      ${v.fname_sn }
      </p>
      </li>
      <li>￥<fmt:formatNumber value="${v.lastDealPrize }" pattern="##.##"
			maxIntegerDigits="10" maxFractionDigits="4" /></li>
      <li>￥<fmt:formatNumber value="${v.higestBuyPrize }" pattern="##.##"
			maxIntegerDigits="10" maxFractionDigits="4" /></li>
      <li>￥<fmt:formatNumber value="${v.lowestSellPrize }" pattern="##.##"
			maxIntegerDigits="10" maxFractionDigits="4" /></li>
	  <li>
	  	<span title="合人民币￥${v.fentrustValue}元"> 
	  	฿<fmt:formatNumber value="${v.volumn }" pattern="##.##"
			maxIntegerDigits="10" maxFractionDigits="4"   />
		</span> 
	  </li>
	  <li>￥<fmt:formatNumber value="${v.fmarketValue }" pattern="##.##"
			maxIntegerDigits="10" maxFractionDigits="4" /></li>	
	  <li>
	  <c:choose>
	  	<c:when  test="${v.fupanddown>0}">
		  	<span style="color:red;">+${v.fupanddown}%</span>
		</c:when >
		<c:when  test="${v.fupanddown == 0}">
		  	<span style="color:red;"> +0.00%</span>
		</c:when >
		<c:otherwise>
			<span style="color:green;">${v.fupanddown}%</span>
		</c:otherwise >
		</c:choose>
		</li>
		<li>
	  	<c:choose>
	  	<c:when  test="${v.fupanddownweek>0}">
		  	<span style="color:red;">+${v.fupanddownweek}%</span>
		</c:when >
		<c:when  test="${v.fupanddownweek == 0}">
		  	<span style="color:red;"> +0.00%</span>
		</c:when >
		<c:otherwise>
			<span style="color:green;">${v.fupanddownweek}%</span>
		</c:otherwise >
		</c:choose>
		</li>
     
      <li><a href="/trade/coin.html?coinType=${v.fid }&tradeType=0" class="button-blue2" style="width:130px;">去交易</a></li>
    </ul>
  </div>
</div>
</c:if>
</c:forEach>

<div id="guang">
  <div id="guang1">
  <a href="${requestScope.constant['advImage1URL'] }" target="_blank">
  <img src="${staticFileUrl}${requestScope.constant['advImage1'] }" width="65%" height="98px">
  </a>
   <a href="${requestScope.constant['advImage2URL'] }" target="_blank">
  <img src="${staticFileUrl}${requestScope.constant['advImage2'] }" width="34%" height="98px">
  </a>
  </div>
</div>


<!--<div id="tupian">
  <div id="tupian1">
    <div id="tupian11">2015-2016发展计划</div>
  </div>
</div>-->


<div id="tupian2">
<div id="picText">
<span style="font-size: 27px;">战略合作机构</span><br/><br/>
<span style="font-size: 15px;">元宝网、比特帮、谷壳宝、清华校友会</span>
</div>
  <div id="tupian21">
    <a href="http://www.tsinghua.edu.cn/publish/xyh/5618/index.html"><img src="${staticFileUrl}/static/front/images/link3.png"></a>
    <a href="http://www.btcside.com/"><img src="${staticFileUrl}/static/front/images/link4.png" style="padding-left: 50px;"></a>
    <a href="https://yuanbaohui.com"><img src="${staticFileUrl}/static/front/images/link1.png" style="padding-left: 50px;"></a>
    <a href="http://www.goocoin.com/"><img src="${staticFileUrl}/static/front/images/link2.png" style="padding-left: 50px;"></a>
  </div>
</div>

  
<div id="xia"> <span class="ziti3">风险提示：</span><br>
  <br>
  <span class="ziti4">数字资产的交易存在极高风险，作为全球的虚拟数字货币，他们都是全天24小时交易，没有涨跌限制，价格容易因为庄家，全球政府的政策影响而大幅波动，我们强烈建议你在自身能承受的风险范围内，参与虚拟货币交易</span>
</div>
	
<%@include file="comm/footer.jsp" %>		
<script type="text/javascript">
jQuery(function () {
    var $allItems = jQuery('.J-slide li');
    var $allIndicators = jQuery('#J-slide-number a');
    var currentIndex = 0;
    var currentItem = null;
    var nextItem = null;
    var time;
    jQuery("#bigImage1").hover(function () {
      time = window.clearInterval(time);//清除自动播放
    },function () {
      time = setInterval(function () {
        currentItem = $allItems.filter('.active');
        if (currentIndex + 1 === $allItems.length) {
          nextItem = $allItems.eq(0);
          currentIndex = 0;
        } else {
          nextItem = $allItems.eq(currentIndex + 1);
          currentIndex += 1;
        }
        nextItem.addClass('active').fadeIn(500);
        $allIndicators.removeClass('slide-number-active').eq(currentIndex).addClass('slide-number-active');
        currentItem.removeClass('active').fadeOut(1000);
      }, 5000);
    }).trigger("mouseleave");

    $allIndicators.click(function () {
      var nextIndex =parseInt(jQuery(this).attr('data-slide-to'));
      //点击的当前激活元素
      if (nextIndex == currentIndex) {
        return false;
      }
      currentIndex = nextIndex;
      currentItem = $allItems.filter('.active');
      currentItem.removeClass('active').fadeOut(1000);
      $allItems.eq(currentIndex).addClass('active').fadeIn(500);
      $allIndicators.removeClass('slide-number-active').eq(currentIndex).addClass('slide-number-active');
      return false;
    }) ;
  });
  		//OKLink活动入口
	jQuery(".activeEnter .okLink").on("mouseover",function(){
		jQuery(".activeEnter .code").show();
	})
	//OKLink活动入口
	jQuery(".activeEnter .okLink").on("mouseout",function(){
		jQuery(".activeEnter .code").hide();
	})
	
</script> 
</body>
</html>
<script type="text/javascript">
	isForward();
</script>