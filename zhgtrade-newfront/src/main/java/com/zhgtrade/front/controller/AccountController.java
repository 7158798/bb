package com.zhgtrade.front.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/account")
public class AccountController {
	/* 个人财务页面 */
	@RequestMapping("/finance")
	public String account(){
		return "account/account_finance";
	}
	/* 委托管理页面 */
	@RequestMapping("/entrust")
	public String entrust(){
		return "account/account_entrust";
	}
//	/* 消息中心页面 */
//	@RequestMapping("/newscenter")
//	public String newscenter(){
//		return "account/account_newscenter";
//	}
//	/* 我的提问页面 */
//	@RequestMapping("/questions")
//	public String questions(){
//
//		return "account/account_questions";
//	}
	/* 虚拟币充值页面 */
	@RequestMapping("/rechargebtc")
	public String rechargebtc(){
		return "account/account_rechargebtc";
	}
	/* 人民币充值页面 */
	@RequestMapping("/rechargecny")
	public String rechargecny(){
		return "account/account_rechargecny";
	}
	/* 账单明细页面 */
	@RequestMapping("/record")
	public String record(){
		return "account/account_record";
	}
	/* 个人中心页面 */
	@RequestMapping("/safecenter")
	public String safecenter(){
		return "account/account_safecenter";
	}
	/* 推广中心 */
	@RequestMapping("/spreadcenter")
	public String spreadcenter(){
		return "account/account_spreadcenter";
	}
	/* 虚拟币提现 */
	@RequestMapping("/withdrawbtc")
	public String withdrawbtc(){
		return "account/account_withdrawbtc";
	}
	/* 人民币提现 */
	@RequestMapping("/withdrawcny")
	public String withdrawcny(){
		return "account/account_withdrawcny";
	}
}
