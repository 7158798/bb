var cellSecondes = 140, emailSeconds = 140;
var cellTimeHandler, emailTimeHandler;

$(function(){
	$("#cell_username").focus();
	/*切换tab*/
	$("#ul_fir").find("li").click(function(){
		var $this=$(this);
		var _index=$this.index();
		$this.addClass("cur").siblings().removeClass("cur");
		$("#ul_sec").find("li").eq(_index).show().siblings().hide();
		$("#ul_sec").find("li").eq(_index).find("input:eq(0)").focus();
	});
	$("#sendSmsCode").on("click", function(){
		var phone = $("#cell_username").val();
		if(cellTimeHandler || !phone) return;
		$("#sendSmsCode").html("<span>" + cellSecondes + "s</span>");
		cellTimeHandler = setInterval(function(){
			cellSecondes--;
			if(cellSecondes > 0){
				$("#sendSmsCode").html("<span>" + cellSecondes + "s</span>");
			}else{
				$("#sendSmsCode").html("<span>发送验证码</span>");
				clearInterval(cellTimeHandler);
			}
		}, 1000);
		
		$.post("/sms/code.html", {cellphone : phone, areaCode : "86"}, function(){});
	});
	register_validate(0);//手机注册
	register_validate(1);//邮箱注册
	/*点击注册*/
	registe();
});
function registe(){
	$(".confirm").click(function(){
		var type=0;
		$("#ul_fir").find("li").each(function(){
			if($(this).hasClass("cur")){
				type=$(this).data("type");
			}
		});
		var $this=$(this);
		var $parent=$this.closest("li");
		var isyes_num=0;
		$parent.find("input").each(function(){
			if($(this).data("isyes")=="1"){
				isyes_num++;
			}
		});
		if(isyes_num!=5){
			if($parent.find(".info").find(".c_red")){
				var text=$parent.find(".info").find(".c_red").eq(0).next().text();
				alert(text);
			}else{
				alert("清先填写注册信息");
			}
			return;
		}
		// type:0 为手机注册  1为邮箱注册
		var param={};
		var english_str="cell";
		if(type==1){
			english_str="mail";
		}
		param={
			"username":$("#"+english_str+"_username").val(),
			"password":$("#"+english_str+"_password").val(),
			"invite_code":$("#"+english_str+"_invite_code").val(),
			"vetify_code":$("#"+english_str+"_vetify_code").val(),
			type:type
		};
		alert(param);
		
		//注册ajax TODO
		location.href=basePath+"user/real.html";//写好ajax之后去掉
	});
}
/*各种验证*/
function register_validate(type){
	var english_str="cell";
	var str="手机";
	if(type==1){
		english_str="mail";
		str="邮箱";
	}
	/*聚焦*/
	$("#"+english_str+"_username").focus(function(){
		input_focus($(this),"请输入您的"+str+"号码");
	});
	$("#"+english_str+"_password").focus(function(){
		input_focus($(this),"请输入6-16位的密码");
	});
	$("#"+english_str+"_password_again").focus(function(){
		input_focus($(this),"请保持和密码一致");
	});
	$("#"+english_str+"_invite_code").focus(function(){
		input_focus($(this),"没有邀请人可不填");
	});
	$("#"+english_str+"_vetify_code").focus(function(){
		input_focus($(this),"请填写验证码");
	});
	/*失去焦点*/
	$("#"+english_str+"_username").blur(function(){
		var $this=$(this);
		var _value=$this.val();
		if(isEmpty(_value)){
			input_error($this,str+"不能为空");
			return;
		}
		if(type==1){
			if(!emailFormatCheck(_value)){
				input_error($this,"请输入正确的邮箱号码");
				return;
			}
			//TODO 验证邮箱唯一性的ajax  result:1 成功 
			/*if(data.result==1){
				input_right($this,"");
			}else{
				input_error($this,"邮箱已存在");
			}*/
		}else{
			if(!mobilephoneFormatCheck(_value)){
				input_error($this,"请输入正确的手机号码");
				return;
			}
			//TODO	验证手机唯一性的ajax result:1 成功 
			/*if(data.result==1){
				input_right($this,"");
			}else{
				input_error($this,"邮箱已存在");
			}*/
			
		}
		input_right($this,"");//ajax写好之后去掉
	});
	$("#"+english_str+"_password").blur(function(){
		var $this=$(this);
		var _value=$this.val();
		if(isEmpty(_value)){
			input_error($this,"密码不能为空");
			return;
		}
		if(_value.length<6||_value.length>16){
			input_error($this,"密码位数不符");
			return;
		}
		input_right($this,"");
	});
	$("#"+english_str+"_password_again").blur(function(){
		var $this=$(this);
		var _value=$this.val();
		if(isEmpty(_value)){
			input_error($this,"确认密码不能为空");
			return;
		}
		if(_value!=$("#"+english_str+"_password").val()){
			input_error($this,"两次输入密码不一致");
			return;
		}
		input_right($this,"");
	});
	$("#"+english_str+"_invite_code").blur(function(){
		var $this=$(this);
		input_right($this,"");
	});
	$("#"+english_str+"_vetify_code").blur(function(){
		var $this=$(this);
		var _value=$this.val();
		if(isEmpty(_value)){
			input_error($this,"验证码不能为空");
			return;
		}
		input_right($this,"");
	});
}
//表单验证  项目专有
function input_focus(obj,txt){
	$(obj).nextAll(".info").find(".iconfont").removeClass("c_red").removeClass("c_green").addClass("c_blue").html("&#xe601;");
	$(obj).nextAll(".info").find("span").text(txt);
	//$(obj).nextAll(".info").show();
}
function input_error(obj,txt){
	$(obj).nextAll(".info").find(".iconfont").removeClass("c_blue").removeClass("c_green").addClass("c_red").html("&#xe609;");
	$(obj).nextAll(".info").find("span").text(txt);
	$(obj).data("isyes","0");
	//$(obj).next().show();
}
function input_right(obj,txt){
	$(obj).nextAll(".info").find(".iconfont").removeClass("c_red").removeClass("c_blue").addClass("c_green").html("&#xe612;");
	$(obj).nextAll(".info").find("span").text(txt);
	$(obj).data("isyes","1");
}