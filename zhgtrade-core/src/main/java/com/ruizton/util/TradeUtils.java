package com.ruizton.util;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.ruizton.main.Enum.MessageStatusEnum; 
import com.ruizton.main.model.Fmessage;
import com.ruizton.main.model.Fsubscription;
import com.ruizton.main.model.Fsubscriptionlog; 
import com.ruizton.main.model.Fvirtualwallet;
import com.ruizton.main.service.admin.SubscriptionLogService;
import com.ruizton.main.service.admin.SubscriptionService;
import com.ruizton.main.service.front.FrontUserService;

public class TradeUtils {
	@Autowired
	private FrontUserService frontUserService;
	@Autowired
	private SubscriptionService subscriptionService;
	@Autowired
	private SubscriptionLogService subscriptionLogService; 
	
	public void work() {
		try {
			String filter = "where flastqty >0";
			List<Fsubscriptionlog> fsubscriptionlogs = this.subscriptionLogService.list(0, 0, filter, false);
			for (Fsubscriptionlog fsubscriptionlog : fsubscriptionlogs) {
				if(fsubscriptionlog.getFlastQty() <=0) continue;
				int subId = fsubscriptionlog.getFsubscription().getFid();
				int userid = fsubscriptionlog.getFuser().getFid();
				double lastqty = fsubscriptionlog.getFlastQty();
				Fsubscription subscription = this.subscriptionService.findById(subId);
				int vid = subscription.getFvirtualcointype().getFid();
				if(subscription.getFdays() ==0) continue;
				double todayQty = Utils.getDouble(fsubscriptionlog.getFcount()/subscription.getFdays(), 4);
				if(lastqty <= todayQty){
					fsubscriptionlog.setFlastQty(0d);
					todayQty = lastqty;
				}else{
					fsubscriptionlog.setFlastQty(Utils.getDouble(fsubscriptionlog.getFlastQty()-todayQty, 4));
				}
				
				Fvirtualwallet fvirtualwallet = this.frontUserService.findVirtualWalletByUser(userid, vid);
				fvirtualwallet.setFtotal(fvirtualwallet.getFtotal()+todayQty);
				if(fvirtualwallet.getFfrozen() > todayQty){
					fvirtualwallet.setFfrozen(fvirtualwallet.getFfrozen()-todayQty);
				}else{
					fvirtualwallet.setFfrozen(0d);
				}
				
				Fmessage message = new Fmessage();
				message.setFcontent("您抢购的"+subscription.getFvirtualcointype().getFname()+"，今日解冻:"+todayQty+",已发放到帐户，请注意查收！");
				message.setFcreateTime(Utils.getTimestamp());
				message.setFreceiver(fsubscriptionlog.getFuser());
				message.setFstatus(MessageStatusEnum.NOREAD_VALUE);
				message.setFtitle("抢购解冻通知");
				
				try {
					this.subscriptionLogService.updateFrozenSub(fsubscriptionlog, fvirtualwallet, message);
				} catch (Exception e) {
					continue;
				}
			}
//			double price = Utils.getDouble(this.realTimeData.getLatestDealPrize(1),4);
//			if(price == 0.001){
//				price = 1;
//			}
//			Ftradehistory tradehistory = new Ftradehistory();
//			tradehistory.setFdate(new Date());
//			tradehistory.setFprice(price);
//			tradehistory.setFvid(1);
//			this.tradehistoryService.saveObj(tradehistory);
//			
///*			Fsystemargs systemargs1 = systemArgsService.findById(37);
//			Fsystemargs systemargs2 = systemArgsService.findById(38);
//			systemargs1.setFvalue("0");
//			systemargs2.setFvalue("0");
//			this.systemArgsService.updateObj(systemargs1);
//			this.systemArgsService.updateObj(systemargs2);*/
			 
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}