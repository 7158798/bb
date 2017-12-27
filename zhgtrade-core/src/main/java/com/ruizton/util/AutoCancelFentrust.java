//package com.ruizton.util;
//
//import java.util.List;
//
//import org.springframework.beans.factory.annotation.Autowired;
//
//import com.ruizton.main.Enum.EntrustStatusEnum;
//import com.ruizton.main.Enum.EntrustTypeEnum;
//import com.ruizton.main.auto.RealTimeData;
//import com.ruizton.main.model.Fentrust;
//import com.ruizton.main.model.Fintrolinfo;
//import com.ruizton.main.model.Fuser;
//import com.ruizton.main.model.Fvirtualcointype;
//import com.ruizton.main.model.Fvirtualwallet;
//import com.ruizton.main.model.Fwallet;
//import com.ruizton.main.service.admin.EntrustService;
//import com.ruizton.main.service.admin.SystemArgsService;
//import com.ruizton.main.service.admin.TradehistoryService;
//import com.ruizton.main.service.admin.UserService;
//import com.ruizton.main.service.admin.VirtualWalletService;
//import com.ruizton.main.service.admin.WalletService;
//import com.ruizton.main.service.front.FrontTradeService;
//import com.ruizton.main.service.front.FrontUserService;
//import com.ruizton.main.service.front.FrontVirtualCoinService;
//
//public class AutoCancelFentrust {
//	@Autowired
//	private FrontTradeService frontTradeService ;
//	@Autowired
//	private EntrustService entrustService ;
//	@Autowired
//	private RealTimeData realTimeData;
//
//	public void work() {
//		synchronized (this) {
//			String filter = "where (fstatus="+EntrustStatusEnum.Going+" or fstatus="+EntrustStatusEnum.PartDeal+")";
//			List<Fentrust> fentrust = this.entrustService.list(0, 0, filter, false);
//			for (Fentrust fentrust2 : fentrust) {
//				if(fentrust2.getFstatus()==EntrustStatusEnum.Going || fentrust2.getFstatus()==EntrustStatusEnum.PartDeal){
//					boolean flag = false ;
//					try {
//						this.frontTradeService.updateCancelFentrust(fentrust2, fentrust2.getFuser()) ;
//						flag = true ;
//					} catch (Exception e) {
//						e.printStackTrace();
//					}
//					if(flag==true){
//						if(fentrust2.getFentrustType()==EntrustTypeEnum.BUY){
//							//买
//							if(fentrust2.isFisLimit()){
//								this.realTimeData.removeEntrustLimitBuyMap(fentrust2.getFvirtualcointype().getFid(), fentrust2) ;
//							}else{
//								this.realTimeData.removeEntrustBuyMap(fentrust2.getFvirtualcointype().getFid(), fentrust2) ;
//							}
//						}else{
//							//卖
//							if(fentrust2.isFisLimit()){
//								this.realTimeData.removeEntrustLimitSellMap(fentrust2.getFvirtualcointype().getFid(), fentrust2) ;
//							}else{
//								this.realTimeData.removeEntrustSellMap(fentrust2.getFvirtualcointype().getFid(), fentrust2) ;
//							}
//
//						}
//					}
//				}
//			}
//		}
//	}
//
//}
