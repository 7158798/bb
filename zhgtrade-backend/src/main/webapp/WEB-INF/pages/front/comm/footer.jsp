<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="include.inc.jsp"%>

<!-- constant -->
<input id="borrow" type="hidden" value="借款">
<input id="lend" type="hidden" value="放款">
<!-- lend.js -->
<input id="lendjs1" type="hidden" value="还款金额不能为0">
<input id="lendjs2" type="hidden" value="放款账户">
<input id="lendjs3" type="hidden" value="余额不足">
<input id="lendjs4" type="hidden" value="公测中，仅供部分用户测试。">
<input id="lendjs5" type="hidden" value="转入交易账户">
<input id="lendjs6" type="hidden" value="转入放款账户">
<input id="lendjs7_1" type="hidden" value="最低转入金额是${minInMoney }">
<input id="lendjs7_2" type="hidden" value="最低转入FC是${minInDou }">
<input id="lendjs8" type="hidden" value="账户出现安全隐患被冻结，请尽快联系客服。">
<input id="lendjs9" type="hidden" value="您有借款订单未还清或撤销">
<input id="lendjs10" type="hidden" value="可借">
<input id="lendjs11" type="hidden" value="放款账户">
<input id="lendjs12" type="hidden" value="数量不能低于">
<input id="lendjs13" type="hidden" value="利率不能低于0.01%">
<input id="lendjs14" type="hidden" value="利率不能高于100%">
<input id="lendjs15" type="hidden" value="借入数量超过最大值">
<input id="lendjs16" type="hidden" value="根据市场行情波动和风控参数，已经暂时停止新的">
<input id="lendjs17" type="hidden" value="BTC融币">
<input id="lendjs18" type="hidden" value="LTC融币">
<input id="lendjs19" type="hidden" value="CNY融资">
<input id="lendjs20" type="hidden" value="您目前设置的借款日利率为">
<input id="lendjs21" type="hidden" value="高于利率">
<input id="lendjs22" type="hidden" value="低于利率">
<input id="lendjs23" type="hidden" value="是否确定下单？">
<input id="lendjs24" type="hidden" value="利率低于0.01%">
<input id="lendjs25" type="hidden" value="余额不足">


<input type="hidden" id="coinMainUrl" value="<%=basePath%>" />
<input id="anthtrade" type="hidden"
	value="${(fuser.fisTelephoneBind==true ||fuser.fgoogleBind==true )?1:0 }">
<input id="ftradePassword" type="hidden"
	value="${(empty(fuser.ftradePassword) || fuser.ftradePassword == null || fuser.ftradePassword == '')?0:1 }">
<script type="text/javascript"
	src="${staticFileUrl}/static/front/js/jquery/jquery-1.8.2.js"></script>
<script type="text/javascript"
	src="${staticFileUrl}/static/front/js/jquery-ui/js/jquery-ui-1.10.4.custom.min.js"></script>
<script type="text/javascript" src="${staticFileUrl}/static/front/js/coincommon.js"></script>
<script type="text/javascript" src="${staticFileUrl}/static/front/js/dataKline.js"></script>
<script type="text/javascript"
	src="${staticFileUrl}/static/front/js/highstock/highstock.js"></script>
<script type="text/javascript"
	src="${staticFileUrl}/static/front/js/highstock/exporting.js"></script>

<script type="text/javascript" src="${staticFileUrl}/static/front/js/account/account.js"></script>
<script type="text/javascript" src="${staticFileUrl}/static/front/js/user/user.js"></script>
<script src="${staticFileUrl}/static/front/js/account/fin.js" type="text/javascript"></script>
<script type="text/javascript" src="${staticFileUrl}/static/front/js/lend/lend.js"></script>
<script type="text/javascript">
	jQuery('#deal_center').hover(function(){jQuery('#deal_list1').show();},function(){jQuery('#deal_list1').hide();});
	jQuery('#tm_deal').hover(function(){jQuery('#deal_list2').show();},function(){jQuery('#deal_list2').hide();});
	jQuery('.deal_list').hover(function(){jQuery(this).show();},function(){jQuery(this).hide();});
</script> 

<div id="allFooter"> 
	<div id="di">
		<div id="dibu">
			<span class="ziti6">友情链接：</span><br> <br>
			 <a href="http://www.zhaogukeji.com" target="_blank"><img src="${staticFileUrl}/static/front/images/img_44.jpg" width="196" height="61"></a>
			 <a href="http://www.zcfunding.com" target="_blank"><img src="${staticFileUrl}/static/front/images/img_45.jpg" width="196" height="61"></a>
			 <a href="http://www.btcside.com/" target="_blank"><img src="${staticFileUrl}/static/front/images/img_46.jpg" width="196" height="61"></a>
			 <a href="http://www.huobi.com/" target="_blank"><img src="${staticFileUrl}/static/front/images/img_47.jpg" width="196" height="61"></a>
			 <a href="http://beta.bter.com/" target="_blank"><img src="${staticFileUrl}/static/front/images/img_48.jpg" width="196" height="61"></a>
			 <a href="http://www.yibite.com/" target="_blank"><img src="${staticFileUrl}/static/front/images/img_49.jpg" width="196" height="61"></a><br>
			 <a href="http://www.zhongchou.cn" target="_blank"><img src="${staticFileUrl}/static/front/images/img_51.jpg" width="196" height="62"></a>
			 <a href="http://www.renrentou.com" target="_blank"><img src="${staticFileUrl}/static/front/images/img_52.jpg" width="196" height="62"></a>
			 <a href="http://www.actwe.com/" target="_blank"><img src="${staticFileUrl}/static/front/images/img_53.jpg" width="196" height="62"></a>
			 <a href="http://lyb.qinminwang.cn/" target="_blank"><img src="${staticFileUrl}/static/front/images/img_54.jpg" width="196" height="62"></a>
			 <a href="http://www.zczj.com" target="_blank"><img src="${staticFileUrl}/static/front/images/img_55.jpg" width="196" height="62"></a>
			 <a href="http://www.yunchou.com/" target="_blank"><img src="${staticFileUrl}/static/front/images/img_56.jpg" width="196" height="62"></a>
		</div>
		<div id="di1">
			<div id="di11">
				<a href="http://wpa.qq.com/msgrd?v=3&uin=${requestScope.constant['serviceQQ']}&site=qq&menu=yes" target="_blank">
				<img src="${staticFileUrl}/static/front/images/1.png" width="39" height="39">
				</a>
				<ul>
					<li>${requestScope.constant['serviceQQ']}</li>
					<li><a href="http://wpa.qq.com/msgrd?v=3&uin=${requestScope.constant['serviceQQ']}&site=qq&menu=yes" target="_blank">在线QQ客服</a>
					</li>
				</ul>
			</div>
			<div id="di11">
				<img src="${staticFileUrl}/static/front/images/2.png" width="39" height="39">
				<ul>
					<li>${requestScope.constant['telephone']}</li>
					<li>5*8小时服务热线</li>
				</ul>
			</div>
			<div id="di11">
				<a href="http://${requestScope.constant['weibo']}" target="_blank"><img src="${staticFileUrl}/static/front/images/3.png" width="39" height="39">
				</a>
				<ul>
					<li>${requestScope.constant['weibo']}</li>
					<li><a href="http://${requestScope.constant['weibo']}" target="_blank">新浪微博</a>
					</li>
				</ul>
			</div>
			<div id="di11">
				<a href="http://${requestScope.constant['companyWeb']}" target="_blank"><img src="${staticFileUrl}/static/front/images/4.png" width="39" height="39">
				</a>
				<ul>
					<li>${requestScope.constant['companyWeb']}</li>
					<li><a href="http://${requestScope.constant['companyWeb']}" target="_blank">公司官网</a>
					</li>
				</ul>
			</div>
		</div>
		<div id="di2">
			<div id="di21">
				<ul>
					<li>
						<h1>认识众股</h1></li>
					<li><a href="${requestScope.constant['footerArticle1']}">公司简介</a>
					</li>
					<li><a href="${requestScope.constant['footerArticle2']}">法律声明</a>
					</li>
					<li><a href="${requestScope.constant['footerArticle3']}">用户协议</a>
					</li>
					<li><a href="${requestScope.constant['footerArticle4']}">关于我们</a>
					</li>
				</ul>
			</div>
			<div id="di22">
				<ul>
					<li>
						<h1>新手帮助</h1></li>
					<li><a href="${requestScope.constant['footerArticle5']}">充值提现</a>
					</li>
					<li><a href="${requestScope.constant['footerArticle6']}">充币提币</a>
					</li>
					<li><a href="${requestScope.constant['footerArticle7']}">交易规则</a>
					</li>
					<li><a href="${requestScope.constant['footerArticle8']}">风险提示</a>
					</li>
				</ul>
			</div>
			<div id="di23">
				<ul>
					<li>
						<h1>下载中心</h1></li>
					<li><a href="${requestScope.constant['footerArticle9']}">钱包下载</a>
					</li>
					<li><a href="${requestScope.constant['footerArticle10']}">上币申请</a>
					</li>
					<li><a href="${requestScope.constant['footerArticle11']}">代理协议</a>
					</li>
					<li><a href="${requestScope.constant['footerArticle12']}">安卓APP</a>
					</li>
				</ul>
			</div>
			<div id="di24">
				<h2>
					<img src="${staticFileUrl}/static/front/images/QQ.png" width="35" height="36">QQ群交流
				</h2>
				<p>D群：436873356</p>
				<p>E群：261908190</p>
				<p>F群：433100822</p>
			</div>
			<div id="di25">
				<p>
					<img src="${staticFileUrl}/static/front/images/img_72.jpg" width="115" height="115">
				</p>
				<p>招股科技</p>
				<p>微信号：${requestScope.constant['weixin']}</p>
			</div>
		</div>
	</div>
	<div id="didi">${requestScope.constant['webinfo'].fcopyRights } <br/> <br/></div>
   
	<iframe src="" id="ifs" width="0" height="0" style="display:none"></iframe>
	<div class="okcoinPop" id="okcoinPop"></div>
</div>

<div id="tm_alert">
	<div class="float_layer" id="miaov_float_layer" style="z-index:10001;">
		<h2>
			<b>今日提示</b>
			<a id="btn_min" href="javascript:;" class="min"></a>
			<a id="btn_close" href="javascript:;" class="close"></a>
		</h2>
		<div class="content2">
			<div class="wrap2">
				<ul>
					<!--<p style="font-size:12px;padding:0 10px;">参与众筹的用户请先前往众创园 "
							<span style="color:#ee0000;">个人设置</span>—<span style="color:#ee0000;">绑定众股</span>
							" 绑定众股用户ID和用户名</p>-->
					<!-- <p style="font-size:10px;padding:0 10px;">1、放假期间大家可以从众创园在线充值提现到众股；</p>	
					<p style="font-size:10px;padding:0 10px;">2、众股A群已满，欢迎大家加入B,C群；</p>	
					<p style="font-size:10px;padding:0 10px;">3、招股提前给各位拜年啦，祝大家猴年发大财！过年别忘了炒币哦。</p>
					<a href="http://www.zcfunding.com">前往查看吧</a> -->
					<p style="font-size:10px;padding:0 10px;">
						虚拟币存在暴涨暴跌的风险，希望广 大用户注意投资风险。平台将坚决打击利用平台进行洗钱，传销等非法活动！
					</p>
				</ul>
			</div>
		</div>
	</div>
</div>
<!-- 右下角弹窗  --> 
<script type="text/javascript" src="${staticFileUrl}/static/front/js/floatingwindow/js/cookie.js"></script> 
<script type="text/javascript" src="${staticFileUrl}/static/front/js/floatingwindow/js/move.js"></script> 
<script type="text/javascript">
$(function (){
     if($.cookie("isClose") != 'yes')
     {
      TanChuang();
      setTimeout("minTanChuang()",5000); //测试5秒自动关闭
     }
});   

function minTanChuang()
{
  $("#btn_min").click();
}

function TanChuang() {
    var oDiv = document.getElementById('miaov_float_layer');
    var oBtnMin = document.getElementById('btn_min');
    var oBtnClose = document.getElementById('btn_close');
    var oDivContent = oDiv.getElementsByTagName('div')[0];

    var iMaxHeight = 0;

    var isIE6 = window.navigator.userAgent.match(/MSIE 6/ig) && !window.navigator.userAgent.match(/MSIE 7|8/ig);

    oDiv.style.display = 'block';
    iMaxHeight = oDivContent.offsetHeight;

    if (isIE6) {
        oDiv.style.position = 'absolute';
        repositionAbsolute();
        miaovAddEvent(window, 'scroll', repositionAbsolute);
        miaovAddEvent(window, 'resize', repositionAbsolute);
    }
    else {
        oDiv.style.position = 'fixed';
        repositionFixed();
        miaovAddEvent(window, 'resize', repositionFixed);
    }

    oBtnMin.timer = null;
    oBtnMin.isMax = true;
    oBtnMin.onclick = function() {
        startMove
    (
      oDivContent, (this.isMax = !this.isMax) ? iMaxHeight : 0,
      function() {
          oBtnMin.className = oBtnMin.className == 'min' ? 'max' : 'min';
      }
    );
    };

    oBtnClose.onclick = function() {
        oDiv.style.display = 'none';
        oDiv.innerHTML = "";
     $.cookie("isClose",'yes',{ expires:1/8640});  //测试关闭十秒后才能再次弹出
    };
  
};
</script>