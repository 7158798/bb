/**
 * 提交btc提现表单
 */
function submitWithdrawBtcForm(coinName){
	var withdrawAddr = trim(document.getElementById("withdrawAddr").value);
	var withdrawAmount = trim(document.getElementById("withdrawAmount").value);
	var tradePwd = trim(document.getElementById("tradePwd").value);
	var  totpCode = 0;
	var  phoneCode = 0;
	
	var symbol = document.getElementById("symbol").value;
	
	if(document.getElementById("btcbalance")!=null && document.getElementById("btcbalance").value==0){
		alertTipsSpan("您的余额不足！");
		return;
	}else{
		clearTipsSpan();
	}
	if(withdrawAddr == ""){
		alertTipsSpan("请设置提现地址");
		return;
	}else{
		clearTipsSpan();
	}
    var reg = new RegExp("^[0-9]+\.{0,1}[0-9]{0,8}$");
    if(!reg.test(withdrawAmount) ){
    	alertTipsSpan("请输入提现金额");
		return;
    }else{
		clearTipsSpan();
	}
	if(withdrawAmount < 0.1){
		alertTipsSpan("最小提现金额为：0.1"+coinName);
		return;
	}else{
		clearTipsSpan();
	}
	if(tradePwd == ""){
		alertTipsSpan("请输入交易密码");
		return;
	}else{
		clearTipsSpan();
	}
	if(document.getElementById("withdrawTotpCode") != null){
		totpCode = trim(document.getElementById("withdrawTotpCode").value);
		if(!/^[0-9]{6}$/.test(totpCode)){
			alertTipsSpan("请输入谷歌验证码");
			return;
		}else{
			document.getElementById("modifyResultTips").innerHTML="&nbsp;";
		}
	}
	if(document.getElementById("withdrawPhoneCode") != null){
		phoneCode = trim(document.getElementById("withdrawPhoneCode").value);
		if(!/^[0-9]{6}$/.test(phoneCode)){
			alertTipsSpan("请输入短信验证码");
			return;
		}else{
			document.getElementById("modifyResultTips").innerHTML="&nbsp;";
		}
	}
	if(document.getElementById("withdrawTotpCode") == null && document.getElementById("withdrawPhoneCode") == null){
		document.getElementById("modifyResultTips").innerHTML= "您没有绑定手机或谷歌验证，请去<a href='/user/security.html'>安全中心</a>绑定手机或谷歌验证后提现。";
		return;
	}
	var url = "/account/withdrawBtcSubmit.html?random="+Math.round(Math.random()*100);
	var param={withdrawAddr:withdrawAddr,withdrawAmount:withdrawAmount,tradePwd:tradePwd,totpCode:totpCode,phoneCode:phoneCode,symbol:symbol};
	jQuery.post(url,param,function(data){
		var result = eval('(' + data + ')');
		if(result!=null){
			 if(result.resultCode == -1){//
				 alertTipsSpan("最小提现数量为："+result.minbtcWithdraw);
			 }else if(result.resultCode == -33){//
				 alertTipsSpan("最大提现数量为："+result.maxbtcWithdraw);
			 }else if(result.resultCode == -2){//
				 if(result.errorNum == 0){
					 alertTipsSpan("交易密码错误多次，请2小时后再试！");
				 }else{
					 alertTipsSpan("交易密码不正确！您还有"+result.errorNum+"次机会");
				 }
			 }else if(result.resultCode == -11){//
				 document.getElementById("modifyResultTips").innerHTML= "您没有设置交易密码，请去<a href='/user/security.html'>安全中心</a>设置交易密码后提现。";
			 }else if(result.resultCode == -3){
				 alertTipsSpan("提现地址不能为空！");
			 }else if(result.resultCode == -4){//
				 alertTipsSpan("您的余额不足！");
			 }else if(result.resultCode == -8){//
				 if(result.errorNum == 0){
					 alertTipsSpan("谷歌验证码错误多次，请2小时后再试！");
				 }else{
					 alertTipsSpan("谷歌验证码错误！您还有"+result.errorNum+"次机会");
				 }
			 }else if(result.resultCode == -9){//
				 if(result.errorNum == 0){
					 alertTipsSpan("短信验证码错误多次，请2小时后再试！");
				 }else{
					 alertTipsSpan("短信验证码错误！您还有"+result.errorNum+"次机会");
				 }
			 }else if(result.resultCode == -99){//
				 alertTipsSpan("每日虚拟币提现最多"+result.errorNum+"次，请明天提现");
			 }
			 else if(result.resultCode == -13){//
				 document.getElementById("modifyResultTips").innerHTML= "您没有绑定手机或谷歌验证，请去<a href='/user/security.html'>安全中心</a>绑定手机或谷歌验证后提现。";
			 }else if(result.resultCode == -14){//
				 document.getElementById("modifyResultTips").innerHTML= "网络错误，请稍后重试。";
			 }else if(result.resultCode == 0){
				 document.getElementById("withdrawBtcButton").disabled="true";
				 window.location.href="/account/withdrawBtc.html?symbol="+symbol+"&success=1";
			 }
		}
	});	
}

function closeEmailAlert(){
	var symbol = document.getElementById("symbol").value;
	dialogBoxHidden();
	document.getElementById("emailAlert").style.display = "none";
	window.location.href="/account/withdrawBtc.html?symbol="+symbol+"&success";
}

function clearTipsSpan(){
	document.getElementById("modifyResultTips").innerHTML="&nbsp;";
}

function alertTipsSpan(tips){
	document.getElementById("modifyResultTips").innerHTML=tips;
}

/**
 * 鼠标点击显示
 */
function onclickbox(e){
	var banknumber = document.getElementById("withdrawAccountAddr").value;
	document.getElementById("displayBankNumberAddr").innerHTML = banknumber;
	if("银行卡账号" == banknumber){
		document.getElementById("withdrawAccountAddr").value = "";
	}
	if(document.getElementById("outType") != null){
		var type = document.getElementById("outType").value;
		if(type == 1)
			document.getElementById("displayBankNumberAddr").style.display="";
	}
}

function moveclickbox(e){
	var banknumber = document.getElementById("displayBankNumberAddr").innerHTML;
	document.getElementById("withdrawAccountAddr").value = banknumber;
	document.getElementById("displayBankNumberAddr").style.display="none";
	if(banknumber == ""){
		document.getElementById("withdrawAccountAddr").value = "银行卡账号";
	}
}

function onkeyupbox(e){
	var banknumber = document.getElementById("withdrawAccountAddr").value;
	banknumber = banknumber.replace(new RegExp(" ","gm"),'');
	banknumber = plusSpace(banknumber);
	document.getElementById("displayBankNumberAddr").innerHTML = banknumber;
	document.getElementById("withdrawAccountAddr").value = banknumber;
}

function onclickbox2(e){
	var banknumber = document.getElementById("withdrawAccountAddr2").value;
	document.getElementById("displayBankNumberAddr2").innerHTML = banknumber;
	if("银行卡账号" == banknumber){
		document.getElementById("withdrawAccountAddr2").value = "";
	}
	if(document.getElementById("outType") != null){
		var type = document.getElementById("outType").value;
		if(type == 1)
			document.getElementById("displayBankNumberAddr2").style.display="";
	}
}

function moveclickbox2(e){
	var banknumber = document.getElementById("displayBankNumberAddr2").innerHTML;
	document.getElementById("withdrawAccountAddr2").value = banknumber;
	document.getElementById("displayBankNumberAddr2").style.display="none";
	if(banknumber == ""){
		document.getElementById("withdrawAccountAddr2").value = "银行卡账号";
	}
}

function onkeyupbox2(e){
	var banknumber = document.getElementById("withdrawAccountAddr2").value;
	banknumber = banknumber.replace(new RegExp(" ","gm"),'');
	banknumber = plusSpace(banknumber);
	document.getElementById("displayBankNumberAddr2").innerHTML = banknumber;
	document.getElementById("withdrawAccountAddr2").value = banknumber;
}

function plusSpace(banknumber){
	var length = banknumber.length;
	var newbanknumber = "";
	if(length > 4){
		var size = parseInt(length/4);
		for (var i = 0; i < size; i++) {
			var start = i*4;
			var end = (i+1)*4;
			if((i+1)*4 > length){
				end = length;
			}
			var str = banknumber.substring(start,end);
			newbanknumber += str+" ";
		};
		if(length%4 != 0){
			newbanknumber += banknumber.substring(size*4,length);
		}else{
			var endstr = newbanknumber.substring(newbanknumber.length-1,newbanknumber.length);
			if(endstr == " "){
				newbanknumber = newbanknumber.substring(0,newbanknumber.length-1);
			}
		}
	}else{
		return banknumber;
	}
	return newbanknumber;
}



/**
 * 修改比特币提现地址
 */
function modifyWithdrawBtcAddr(){
	if(document.getElementById("isEmptyAuth").value==1){
		bindAuth();
		return;
	}
	dialogBoxShadow(false);
	addMoveEvent("dialog_title_btcaddr","dialog_content_btcaddr");
	document.getElementById("withdrawBtcAddrDiv").style.display="";
	document.getElementById("withdrawBtcAddr").focus();
	document.getElementById("withdrawBtcAddrTips").innerHTML="";
	document.getElementById("withdrawBtcAddr").value="";
	if(document.getElementById("withdrawBtcAddrCode") != null){
		document.getElementById("withdrawBtcAddrCode").value="";
	}
	document.getElementById("withdrawBtcAddr").focus();
	callbackEnter(submitWithdrawBtcAddrForm);
}

function closeWithdrawBtcAddr(){
	dialogBoxHidden();
	document.getElementById("withdrawBtcAddrDiv").style.display="none";
}

function submitWithdrawBtcAddrForm(coinName){
	var withdrawAddr = trim(document.getElementById("withdrawBtcAddr").value);
	var  withdrawBtcAddrTotpCode = 0;
	var  withdrawBtcAddrPhoneCode = 0;
	var symbol = document.getElementById("symbol").value;
	if(withdrawAddr == ""){
		document.getElementById("withdrawBtcAddrTips").innerHTML="请设置提现地址";
		return;
	}else{
		document.getElementById("withdrawBtcAddrTips").innerHTML="";
	}
	var start = withdrawAddr.substring(0,1);
	if(withdrawAddr.length < 10 || withdrawAddr.length > 50){
		document.getElementById("withdrawBtcAddrTips").innerHTML = "输入的地址不是一个合法的"+coinName+"地址";
		return;
	}
	if(document.getElementById("withdrawBtcAddrTotpCode") != null){
		withdrawBtcAddrTotpCode = trim(document.getElementById("withdrawBtcAddrTotpCode").value);
		if(!/^[0-9]{6}$/.test(withdrawBtcAddrTotpCode)){
			document.getElementById("withdrawBtcAddrTips").innerHTML="谷歌验证码格式不正确";
			document.getElementById("withdrawBtcAddrTotpCode").value = "";
			return;
		}else{
			document.getElementById("withdrawBtcAddrTips").innerHTML="";
		}
	}
	if(document.getElementById("withdrawBtcAddrPhoneCode") != null){
		withdrawBtcAddrPhoneCode = trim(document.getElementById("withdrawBtcAddrPhoneCode").value);
		if(!/^[0-9]{6}$/.test(withdrawBtcAddrPhoneCode)){
			document.getElementById("withdrawBtcAddrTips").innerHTML="短信验证码格式不正确";
			document.getElementById("withdrawBtcAddrPhoneCode").value = "";
			return;
		}else{
			document.getElementById("withdrawBtcAddrTips").innerHTML="";
		}
	}
	var url = "/user/modifyWithdrawBtcAddr.html?random="+Math.round(Math.random()*100);
	var param={withdrawAddr:withdrawAddr,totpCode:withdrawBtcAddrTotpCode,phoneCode:withdrawBtcAddrPhoneCode,symbol:symbol};
	jQuery.post(url,param,function(data){
		var result = eval('(' + data + ')');
		if(result!=null){
			if(result.resultCode == -1){
				bindAuth();
			}else if(result.resultCode == -4){
				document.getElementById("withdrawBtcAddrTips").innerHTML="网络超时，请稍后重试！";
			}else if(result.resultCode == -2){
				 if(result.errorNum == 0){
					 document.getElementById("withdrawBtcAddrTips").innerHTML="谷歌验证码错误多次，请2小时后再试！";
				 }else{
					 document.getElementById("withdrawBtcAddrTips").innerHTML="谷歌验证码错误！您还有"+result.errorNum+"次机会";
					 document.getElementById("withdrawBtcAddrTotpCode").value = "";
				 }
			 }else if(result.resultCode == -3){
				 if(result.errorNum == 0){
					 document.getElementById("withdrawBtcAddrTips").innerHTML="短信验证码错误多次，请2小时后再试！";
				 }else{
					 document.getElementById("withdrawBtcAddrTips").innerHTML="短信验证码错误！您还有"+result.errorNum+"次机会";
					 document.getElementById("withdrawBtcAddrPhoneCode").value = "";
				 }
			 }else if(result.resultCode == 0){
				 closeWithdrawBtcAddr();
				 window.location.href="/account/withdrawBtc.html";
			 }
		}
	 });
}

function cancelWithdrawCny(outId){
	if(confirm('确定撤销提现么？')){
		var url = "/account/cancelWithdrawCny.html?random="+Math.round(Math.random()*100);
		var param={outId:outId};
		jQuery.post(url,param,function(data){
			window.location.href="/account/withdrawCny.html";
		 });
	}
}
function cancelWithdrawBtc(id){
	if(confirm('确定撤销提现么？')){
		var url = "/account/cancelWithdrawBtc.html?random="+Math.round(Math.random()*100);
		var param={id:id};
		jQuery.post(url,param,function(data){
			window.location.reload(true) ;
		 });
	}else{
		return false ;
	}
}

function checkPayee(payee){
	payee = payee.replace(new RegExp("　","gm"),'');
	payee = payee.replace(/^\s+|\s+$/g,"");
	var re = new RegExp();   
	re = /^[0-9]/; 
	if (re.test(payee)) {
		document.getElementById("addrMsgSpan").innerHTML="收款人姓名不合法";
		return ;
    }else{
    	document.getElementById("addrMsgSpan").innerHTML="&nbsp;";
	}
}

function showUpadteAddress(outType,addressType){
	if(document.getElementById("anthtrade").value==0){
		bindAuth();
		return;
	}
	if(outType == 2){
		document.getElementById("openBankTypeAddrLi").style.display = "none";
		document.getElementById("cnyAccountAddr").innerHTML = "财付通账户:";
		document.getElementById("displayBankNumberAddr").style.display = "none";
		document.getElementById("withdrawAccountAddr").value = "";
		document.getElementById("withdrawAccountAddr").onkeyup=null;
		document.getElementById("withdrawAccountAddr").onfocus=null;
		document.getElementById("withdrawAccountAddr").onblur=null;
		document.getElementById("displayBankNumberAddr2").style.display = "none";
		document.getElementById("withdrawAccountAddr2").value = "";
		document.getElementById("withdrawAccountAddr2").onkeyup=null;
		document.getElementById("withdrawAccountAddr2").onfocus=null;
		document.getElementById("withdrawAccountAddr2").onblur=null;
		document.getElementById("cnyAccountAddr2").innerHTML="请再次输入财付通账户:";
		
	}else{
		document.getElementById("openBankTypeAddrLi").style.display = "";
		var onkeyupboxFun = function (e){
			onkeyupbox(e);
		};
		var onclickboxFun = function (e){
			onclickbox(e);
		};
		var moveclickboxFun = function (e){
			moveclickbox(e);
		};
		var onkeyupboxFun2 = function (e){
			onkeyupbox2(e);
		};
		var onclickboxFun2 = function (e){
			onclickbox2(e);
		};
		var moveclickboxFun2 = function (e){
			moveclickbox2(e);
		};
		document.getElementById("withdrawAccountAddr").onkeyup=onkeyupboxFun;
		document.getElementById("withdrawAccountAddr").onfocus=onclickboxFun;
		document.getElementById("withdrawAccountAddr").onblur=moveclickboxFun;
		document.getElementById("cnyAccountAddr").innerHTML = "银行卡账号:";
		document.getElementById("withdrawAccountAddr").value = "银行卡账号";
		document.getElementById("withdrawAccountAddr2").onkeyup=onkeyupboxFun2;
		document.getElementById("withdrawAccountAddr2").onfocus=onclickboxFun2;
		document.getElementById("withdrawAccountAddr2").onblur=moveclickboxFun2;
		document.getElementById("withdrawAccountAddr2").value = "银行卡账号";
		document.getElementById("cnyAccountAddr2").innerHTML="请再次输入银行卡账号:";
	}
	dialogBoxShadow(false);
	addMoveEvent("dialog_title_CnyAddress","dialog_content_CnyAddress");
	document.getElementById("withdrawCnyAddress").style.display="";
	document.getElementById("outType").value=outType;
	document.getElementById("addressType").value=addressType;
	if(document.getElementById("addressPhoneCode") != null){
		document.getElementById("addressPhoneCode").value="";
	}
	if(document.getElementById("addressTotpCode") != null){
		document.getElementById("addressTotpCode").value="";
	}
	document.getElementById("addrMsgSpan").innerHTML = "&nbsp;";
	
}

function closeAddress(){
	dialogBoxHidden();
	document.getElementById("withdrawCnyAddress").style.display="none";
}
var secs = 121;
function sendWithdrawCnyAddressMsgCode(type){
	var msgType = 0;
	if(document.getElementById("WithdrawCnyAddressMsgCodeBtnSign")!=null){
		msgType=document.getElementById("WithdrawCnyAddressMsgCodeBtnSign").value;
	}
	if(msgType==1){
		document.getElementById("WithdrawCnyAddressMsgCodeBtn").disabled = true;
		for(var num=1;num<=secs;num++) {
			  window.setTimeout("updateCnyAddrNumber(" + num + ")", num * 1000);
		  }
	}
	var url = "/user/sendMsg.html?random="+Math.round(Math.random()*100);
	var param = {type:type,msgType:msgType};
	jQuery.post(url,param,function(data){
		if(data == 0){
			if(msgType!=1){
				document.getElementById("WithdrawCnyAddressMsgCodeBtn").disabled = true;
				for(var num=1;num<=secs;num++) {
					window.setTimeout("updateCnyAddrNumber(" + num + ")", num * 1000);
				}
			}
		}else if(data == -2){
			document.getElementById("addrMsgSpan").innerHTML="您没有绑定手机";
		}else if(data == -3){
			document.getElementById("addrMsgSpan").innerHTML="短信验证码错误多次，请2小时后再试！";
		}
	});
}
function updateCnyAddrNumber(num){
	if(secs == num){
		document.getElementById("WithdrawCnyAddressMsgCodeBtn").disabled = false;
		document.getElementById("WithdrawCnyAddressMsgCodeBtn").value="发送验证码" ;
	}else{
		var printnr = secs - num;
		document.getElementById("WithdrawCnyAddressMsgCodeBtn").value= printnr +"秒后可重发";
	}
}
function submitWithdrawCnyAddress(type){
	var cnyOutType = parseFloat(document.getElementById("cnyOutType").value);
	var openBankTypeAddr = document.getElementById("openBankTypeAddr").value;
	var withdrawAccount = trim(document.getElementById("withdrawAccountAddr").value);
	var pId = parseInt(document.getElementById("openBankProvinceAddr").value);
	var cId = parseInt(document.getElementById("openBankCityAddr").value);
	var branch = trim(document.getElementById("openBankBranchAddr").value);
	var totpCode = 0;
	var phoneCode = 0;
	var type =  document.getElementById("addressType").value;
	
	var regType = new RegExp("^[0-9]*$");
	if(!regType.test(cnyOutType) ){
		document.getElementById("addrMsgSpan").innerHTML = "请选择提现方式";
		return;
	}else{
		document.getElementById("addrMsgSpan").innerHTML = "&nbsp;";
	}
	if(cnyOutType == 1 && openBankTypeAddr == -1){
		document.getElementById("addrMsgSpan").innerHTML = "请选择银行类型";
		return;
	}else{
		document.getElementById("addrMsgSpan").innerHTML = "&nbsp;";
	}
	if( pId == -1 || cId == -1){
		document.getElementById("addrMsgSpan").innerHTML = "请选择开户所在地";
		return;
	}else{
		document.getElementById("addrMsgSpan").innerHTML = "&nbsp;";
	}
	if(branch == "" || branch.length>200){
		document.getElementById("addrMsgSpan").innerHTML = "请填写开户网点，限制200字内";
		return;
	}else{
		document.getElementById("addrMsgSpan").innerHTML = "&nbsp;";
	}
	if(withdrawAccount == "" || withdrawAccount.length>200 || withdrawAccount == "银行卡账号"){
		document.getElementById("addrMsgSpan").innerHTML = "请输入提现帐户";
		return;
	}else{
		document.getElementById("addrMsgSpan").innerHTML = "&nbsp;";
	}
	var withdrawAccount2 = trim(document.getElementById("withdrawAccountAddr2").value);
	if(withdrawAccount != withdrawAccount2){
		document.getElementById("addrMsgSpan").innerHTML = "两次输入的帐户不一致";
		return;
	}else{
		document.getElementById("addrMsgSpan").innerHTML = "&nbsp;";
	}
	
	/*if(document.getElementById("addressTotpCode") != null){
		totpCode = trim(document.getElementById("addressTotpCode").value);
		if(!/^[0-9]{6}$/.test(totpCode)){
			document.getElementById("addrMsgSpan").innerHTML="谷歌验证码格式不正确";
			document.getElementById("addressTotpCode").value = "";
			return;
		}else{
			document.getElementById("addrMsgSpan").innerHTML="&nbsp;";
		}
	}
	if(document.getElementById("addressPhoneCode") != null){
		phoneCode = trim(document.getElementById("addressPhoneCode").value);
		if(!/^[0-9]{6}$/.test(phoneCode)){
			document.getElementById("addrMsgSpan").innerHTML="短信验证码格式不正确";
			document.getElementById("addressPhoneCode").value = "";
			return;
		}else{
			document.getElementById("addrMsgSpan").innerHTML="&nbsp;";
		}
	}*/
	

	var url = "/user/updateOutAddress.html?random="+Math.round(Math.random()*100);
	var param={type:type,cnyOutType:cnyOutType,account:withdrawAccount,openBankType:openBankTypeAddr,pId:pId,cId:cId,branch:branch};
	jQuery.post(url,param,function(data){
		var result = eval('(' + data + ')');
		if(result!=null){
			 if(result.resultCode == -2){
				 document.getElementById("addrMsgSpan").innerHTML="非法提交";
			 }else if(result.resultCode == -8){
				 if(result.errorNum == 0){
					 document.getElementById("addrMsgSpan").innerHTML="谷歌验证码错误多次，请2小时后再试！";
				 }else{
					 document.getElementById("addrMsgSpan").innerHTML="谷歌验证码错误！您还有"+result.errorNum+"次机会";
				 }
			 }else if(result.resultCode == -9){
				 if(result.errorNum == 0){
					 document.getElementById("addrMsgSpan").innerHTML="短信验证码错误多次，请2小时后再试！";
				 }else{
					 document.getElementById("addrMsgSpan").innerHTML="短信验证码错误！您还有"+result.errorNum+"次机会";
				 }
			 }else if(result.resultCode == 0){
				 window.location.href="/account/withdrawCny.html?outCnyType="+cnyOutType;
			 }else if(result.resultCode == -13){
				 document.getElementById("addrMsgSpan").innerHTML= "您没有绑定手机或谷歌验证，请去<a href='/user/security.html'>安全中心</a>绑定手机或谷歌验证后提现。";
			 }
		}
	});	
}

function sendEmail(id,symbol){
	var url = "/account/sendEmail.html?random="+Math.round(Math.random()*100);
	var param = {id:id,symbol:symbol};
	jQuery.post(url,param,function(data){
		if(data == 0){
			dialogBoxShadow(false);
			 document.getElementById("emailAlert").style.display = "";
			 document.getElementById("emailSuc").style.display = "";
			 document.getElementById("emailError").style.display = "none";
		}else if(data == -2){
			dialogBoxShadow(false);
			document.getElementById("emailAlert").style.display = "";
			document.getElementById("emailSuc").style.display = "none";
			document.getElementById("emailError").style.display = "";
		}else if(data == -4){
			okcoinAlert("请求过于频繁，请30分钟后重试。", null, null, "");
		}
	});
}




////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////
/**
 * 验证人民币提现金额
 * modify by hank
 */
function withdrawAmountBlur(tipElement_id,withdrawAmount_id){
	var tipElement = document.getElementById(tipElement_id) ;
	var withdrawAmount = parseFloat(document.getElementById(withdrawAmount_id).value);

	var reg = new RegExp("^[0-9]+\.{0,1}[0-9]{0,8}$");
    if(!reg.test(withdrawAmount) ){
    	tipElement.innerHTML = "请输入提现金额";
		return;
    }else{
    	tipElement.innerHTML = "";
	}
    var url = "/account/withdrawAmountBlur.html?random="+Math.round(Math.random()*100);
    var min = document.getElementById("min_double").value ;
    var max = document.getElementById("max_double").value ;
	var param={withdrawAmount:withdrawAmount};
	jQuery.post(url,param,function(data){
		if(data == -3){
			tipElement.innerHTML = "最小提现金额为："+min+"元";
		}else if(data == -5){
			tipElement.innerHTML =  "您的余额不足！" ;
		}else if(data == -200){
			tipElement.innerHTML =  "最大提现金额为"+max+"元！" ;
		}
	});
}

function cancelWithdrawcny(id){
var url = "/account/cancelWithdrawcny.html?random="+Math.round(Math.random()*100);
	var param={id:id};
	jQuery.post(url,param,function(data){
		window.location.reload(true) ;
	});
}


/*
 * 验证虚拟币提现数量是否余额不足
 * */
function withdrawBtcAmountBlur(tipElement_id,withdrawAmount_id,symbol){
	var tipElement = document.getElementById(tipElement_id) ;
	var withdrawAmount = parseFloat(document.getElementById(withdrawAmount_id).value);
	
	var reg = new RegExp("^[0-9]+\.{0,1}[0-9]{0,8}$");
	if(!reg.test(withdrawAmount) ){
		tipElement.innerHTML = "请输入提现数量";
		return;
	}else{
		tipElement.innerHTML = "";
	}
	var url = "/account/withdrawBtcAmountBlur.html?random="+Math.round(Math.random()*100);
	var param={withdrawAmount:withdrawAmount,symbol:symbol};
	jQuery.post(url,param,function(data){
		if(data == -3){
			tipElement.innerHTML = "最小提现数量为：0.1个";
		}else if(data == -5){
			tipElement.innerHTML =  "您的余额不足！" ;
		}else if(data == -1){
			tipElement.innerHTML =  "网络超时，请稍后重试！" ;
		}
	});
}

/**
 * 提交cny提现表单
 * modify by hank
 */
function submitWithdrawCnyForm(tipElement_id){
	var tipElement = document.getElementById(tipElement_id) ;
	var withdrawBalance = trim(document.getElementById('withdrawBalance').value) ;
	var tradePwd = trim(document.getElementById("tradePwd").value);
	var  totpCode = 0;
	var  phoneCode = 0;
	var min = document.getElementById("min_double").value ;
    var max = document.getElementById("max_double").value ;
	tipElement.innerHTML = "" ;
	if(document.getElementById("cnyadd") != null){
		tipElement.innerHTML = "请设置提现信息";
		return;
	}
    var reg = new RegExp("^[0-9]+\.{0,1}[0-9]{0,8}$");
    if(!reg.test(withdrawBalance) ){
    	tipElement.innerHTML = "请输入提现金额" ;
		return;
    }

	if(parseFloat(withdrawBalance) < parseFloat(min)){
		tipElement.innerHTML = " 最小提现金额为：￥"+min ;
		return;
	}

	if(parseFloat(withdrawBalance) > parseFloat(max)){
		tipElement.innerHTML = " 最大提现金额为：￥"+max ;
		return;
	}

	if(tradePwd == ""  || tradePwd.length>200){
		tipElement.innerHTML = "请输入交易密码" ;
		return;
	}

	if(document.getElementById("withdrawTotpCode") != null){
		totpCode = trim(document.getElementById("withdrawTotpCode").value);
		if(!/^[0-9]{6}$/.test(totpCode)){
			tipElement.innerHTML = "请输入正确谷歌验证码" ;
			return;
		}

	}
	if(document.getElementById("withdrawPhoneCode") != null){
		phoneCode = trim(document.getElementById("withdrawPhoneCode").value);
		if(!/^[0-9]{6}$/.test(phoneCode)){
			tipElement.innerHTML = "请输入正确短信验证码" ;
			return;
		}

	}
	if(document.getElementById("withdrawTotpCode") == null && document.getElementById("withdrawPhoneCode") == null){
		tipElement.innerHTML = "您没有绑定手机或谷歌验证，请去<a href='/user/security.html'>安全中心</a>绑定手机或谷歌验证后提现。";
		return;
	}
	var url = "/account/withdrawCnySubmit.html?random="+Math.round(Math.random()*100);
	var param={tradePwd:tradePwd,withdrawBalance:withdrawBalance,phoneCode:phoneCode,totpCode:totpCode};
	jQuery.post(url,param,function(data){
		var result = eval('(' + data + ')');
		if(result!=null){
			if(result.resultCode == -200){
				 alertTipsSpan("最大提现金额为："+max+"元");
			 }else if(result.resultCode == -1){
				 alertTipsSpan("最小提现金额为："+min+"元");
			 }else if(result.resultCode == -15){
				 tipElement.innerHTML= "您没有设置交易密码，请去<a href='/user/security.html'>安全中心</a>绑定手机或谷歌验证后提现。";
			 }else if(result.resultCode == -13){
				 tipElement.innerHTML= "您没有绑定手机或谷歌验证，请去<a href='/user/security.html'>安全中心</a>绑定手机或谷歌验证后提现。";
			 }else if(result.resultCode == -16){
				 tipElement.innerHTML= "您的余额不足！";
			 }else if(result.resultCode == -30){
				 tipElement.innerHTML= "您申请了借款，不允许提现！";
			 }else if(result.resultCode == -31){
				 tipElement.innerHTML= "您有借款未还清，不允许提现！";
			 }else if(result.resultCode == -2){
				 if(result.errorNum == 0){
					 alertTipsSpan("谷歌认证错误多次，请2小时后再试！");
				 }else{
					 alertTipsSpan("谷歌认证不正确！您还有"+result.errorNum+"次机会");
				 }
			 }else if(result.resultCode == -9){
				 if(result.errorNum == 0){
					 alertTipsSpan("短信验证码错误多次，请2小时后再试！");
				 }else{
					 alertTipsSpan("短信验证码错误！您还有"+result.errorNum+"次机会");
				 }
			 }else if(result.resultCode == -3){
				 if(result.errorNum == 0){
					 alertTipsSpan("交易密码错误多次，请2小时后再试！");
				 }else{
					 alertTipsSpan("交易密码错误！您还有"+result.errorNum+"次机会");
				 }
			 }else if(result.resultCode == -99){
				alertTipsSpan("每日人民币提现最多"+result.errorNum+"次，请明天提现");
			 }else if(result.resultCode == -10){
				 tipElement.innerHTML= "请先设置您的提现银行卡信息。";
			 }else if(result.resultCode == 0){
				 document.getElementById("withdrawCnyButton").disabled="true";
				 window.location.href="/account/withdrawCny.html?success";
			}
		}
	});
}