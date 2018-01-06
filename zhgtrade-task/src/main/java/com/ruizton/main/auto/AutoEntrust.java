package com.ruizton.main.auto;

import antlr.collections.impl.LList;
import com.ruizton.main.Enum.EntrustRobotStatusEnum;
import com.ruizton.main.Enum.EntrustTypeEnum;
import com.ruizton.main.Enum.VirtualCoinTypeStatusEnum;
import com.ruizton.main.cache.data.RealTimeDataService;
import com.ruizton.main.cache.data.impl.RealTimeEntrustDepthServiceImpl;
import com.ruizton.main.model.Fentrust;
import com.ruizton.main.model.Fuser;
import com.ruizton.main.model.Fvirtualcointype;
import com.ruizton.main.model.Market;
import com.ruizton.main.service.front.FrontTradeService;
import com.ruizton.main.service.front.FrontUserService;
import com.ruizton.main.service.front.FrontVirtualCoinService;
import com.ruizton.main.service.front.MarketService;
import com.ruizton.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//机器人自动挂单
public class AutoEntrust {
	private static final Logger log = LoggerFactory
			.getLogger(AutoEntrust.class);
	@Autowired
	private RealTimeData realTimeData;
	@Autowired
	private FrontTradeService frontTradeService;
	@Autowired
	private FrontVirtualCoinService frontVirtualCoinService;
	@Autowired
	private FrontUserService frontUserService;
	@Autowired
	private RobotParser parser;
	@Autowired
	private MarketService marketService;

	@Autowired
	private RealTimeEntrustDepthServiceImpl realTimeEntrustDepthService;

	@Autowired
	private RealTimeDataService realTimeDataService;

	private boolean isUpdateCancel = false;

	//机器人机动变化的单子
	private Map<Integer,List<Fentrust>> buyEntrust;
	private Map<Integer,List<Fentrust>> sellEntrust;

	public void init() {
		new Thread(new entrustWork()).start();
	}

	//自动在价格区间外挂大单和撤大单:已取消，造成不必要麻烦
	class entrustWork implements Runnable{

		public void run() {
			while(true){

				if(parser.isOpen("robotControl2")){
					Date date = new Date();
					int hour = date.getHours();

					entrustStrategy(parser.getAmount("robotControl2"));

					int[] rets = parser.getIdleTimeZone("robotControl2");
					try {
						if(hour>=rets[0] && hour<rets[1])//
							Thread.sleep((long) (parser.getIdleInterval("robotControl2")*1000));
						else
							Thread.sleep((long) (parser.getInterval("robotControl2")*1000));
					} catch (InterruptedException e) {
						e.printStackTrace();
					}

				}else{

					if(!isUpdateCancel){//取消所有未成交的robotStatus=2的挂单
						List<Market> markets = marketService.findByStatus(1);
						Fuser fuser = frontUserService.findById(3699) ;

						//
						List<Fentrust> fentrusts = frontTradeService.findByUserId(3699);
						for (Fentrust fentrust : fentrusts){
							frontTradeService.updateCancelFentrust(fentrust,fuser);
							realTimeDataService.removeEntrustBuyMap(fentrust.getMarket().getId(), fentrust);
						}

						isUpdateCancel =  true;
					}

					try {
						Thread.sleep(10000L);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}

			}
		}

	}

	private Map<String, Object> getMap(int id, int status, double prize , double leftCount, double amount , int type){
		Map<String, Object> map = new HashMap<>();
		map.put("id",id);
		map.put("status",status);
		map.put("prize",prize);
		map.put("leftCount",leftCount);
		map.put("amount",amount);
		map.put("type",type);
		return map;
	}

	private void entrustStrategy(double amount){

		try{

			List<Market> markets = marketService.findByStatus(1);
			for (Market market : markets){
				int vid = market.getId();
				double highestBuyPrice = realTimeEntrustDepthService.getHighestBuyPrizeExceptRobot(vid) - 0.0001D;
				double lowestSellPrice = realTimeEntrustDepthService.getLowestSellPrizeExceptRobot(vid) + 0.0001D;
				double latest20BuyPrice = realTimeEntrustDepthService.getLastestNBuyPrice(vid,10);
				double latest20SellPrice = realTimeEntrustDepthService.getLastestNSellPrice(vid,10);

				int N = (int) (Math.random()*10);
				for(int i=0; i<N; i++){
					double sellPrice = getPriceBiggerThan(lowestSellPrice,latest20SellPrice-lowestSellPrice);
					double sellCount = getCount(sellPrice,Utils.guassian(amount/2,amount));
					Fentrust fentrust = btcTradeSubmit(0,vid,sellCount,sellPrice,EntrustTypeEnum.SELL,EntrustRobotStatusEnum.Robot2);
					//发送到redis
					frontTradeService.sendToQueue(false, vid, fentrust);
//					double total = MathUtils.multiply(sellCount,sellPrice);
//					HttpUtils.sendPostRequest("http://127.0.0.1:1001", getMap(vid,1, sellPrice,sellCount,total,EntrustTypeEnum.SELL));
				}
				for(int i=0; i<N; i++){
					double buyPrice = getPriceLessThan(highestBuyPrice,highestBuyPrice-latest20BuyPrice);
					double buyCount = getCount(buyPrice,Utils.guassian(amount/2,amount));
					Fentrust fentrust = btcTradeSubmit(0,vid,buyCount,buyPrice,EntrustTypeEnum.BUY,EntrustRobotStatusEnum.Robot2);

					frontTradeService.sendToQueue(false, vid, fentrust);
//					double total = MathUtils.multiply(buyPrice,buyCount);
//					HttpUtils.sendPostRequest("http://127.0.0.1:1001", getMap(vid,1, buyPrice,buyCount,total,EntrustTypeEnum.BUY));
				}
			}
		}catch(Exception e){
			if(!isUpdateCancel){//取消所有未成交的robotStatus=2的挂单
				List<Market> markets = marketService.findByStatus(1);
				Fuser fuser = frontUserService.findById(3699) ;

				//
				List<Fentrust> fentrusts = frontTradeService.findByUserId(3699);
				for (Fentrust fentrust : fentrusts){
					frontTradeService.updateCancelFentrust(fentrust,fuser);
					realTimeDataService.removeEntrustBuyMap(fentrust.getMarket().getId(), fentrust);
				}

				isUpdateCancel =  true;
			}
			new Thread(new entrustWork()).start();
		}
	}

	private double getPriceBiggerThan(double price,double limit){
		return Utils.getDouble(Math.random()*limit + price,4);
	}

	private double getPriceLessThan(double price,double limit){
		double ret = Utils.getDouble(price-Math.random()*limit ,4);
		if(ret<=0)
			ret = 0.0001;
		return ret;
	}

	private double getCount(double price,double amount){
		if(price<=0)
			return 10;

		double ret = amount/price;
		/*if( ret >=10000 )
			ret = 9999;*/

		return Utils.getDouble(ret,4);
	}

	private Fentrust btcTradeSubmit(@RequestParam(required=false,defaultValue="0")int limited,//是否按照市场价买入
									@RequestParam(required=true)int symbol,//币种
									@RequestParam(required=true)double tradeAmount,//数量
									@RequestParam(required=true)double tradeCnyPrice,//单价
									@RequestParam(required=true)int entrustType, //买卖类型
									@RequestParam(required=true)int robotStatus //机器人状态
	){
		tradeAmount = Utils.getDouble(tradeAmount, 4) ;
		tradeCnyPrice = Utils.getDouble(tradeCnyPrice, 4) ;
		Market market = marketService.findById(symbol);
		if (market == null || market.getStatus() == Market.STATUS_Abnormal) {
			return null;
		}

		if (market.getTradeStatus() == Market.TRADE_STATUS_Abnormal) {
			return null;
		}

		//是否开放交易
		if (!MarketUtils.openTrade(market.getTradeTime())) {
			return null;
		}

		Fuser fuser = frontUserService.findById(3699) ;
		try {

			if(entrustType==EntrustTypeEnum.BUY){
				return this.frontTradeService.updateEntrustBuy2(tradeAmount, tradeCnyPrice, fuser, limited==1,robotStatus,market) ;
			}else if(entrustType==EntrustTypeEnum.SELL){
				return this.frontTradeService.updateEntrustSell2(symbol, tradeAmount, tradeCnyPrice, fuser, limited==1,robotStatus,market) ;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

}
