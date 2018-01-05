package com.ruizton.main.auto;

import com.google.common.base.Preconditions;
import com.ruizton.main.Enum.EntrustRobotStatusEnum;
import com.ruizton.main.Enum.EntrustTypeEnum;
import com.ruizton.main.Enum.VirtualCoinTypeStatusEnum;
import com.ruizton.main.controller.front.FrontTradeJsonController;
import com.ruizton.main.model.Fentrust;
import com.ruizton.main.model.Flimittrade;
import com.ruizton.main.model.Fuser;
import com.ruizton.main.model.Fvirtualcointype;
import com.ruizton.main.service.front.FrontTradeService;
import com.ruizton.main.service.front.FrontUserService;
import com.ruizton.main.service.front.FrontVirtualCoinService;
import com.ruizton.util.Constants;
import com.ruizton.util.RobotParser;
import com.ruizton.util.Utils;
import org.apache.commons.lang.math.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Calendar;

public class RobotDealGenerating implements Runnable {

	@SuppressWarnings("unused")
	private static final Logger log = LoggerFactory.getLogger(RobotDealGenerating.class);

	private RealTimeData realTimeData;
	private FrontTradeService frontTradeService;
	private FrontVirtualCoinService frontVirtualCoinService;
	private FrontUserService frontUserService;
	private RobotParser parser;
	private final Fvirtualcointype fvirtualcointype;

	public RobotDealGenerating(RealTimeData realTimeData, FrontTradeService frontTradeService, FrontVirtualCoinService frontVirtualCoinService ,
			FrontUserService frontUserService, RobotParser parser, Fvirtualcointype fvirtualcointype) {
		super();
		this.realTimeData = realTimeData;
		this.frontTradeService = frontTradeService;
		this.frontVirtualCoinService = frontVirtualCoinService;
		this.frontUserService = frontUserService;
		this.parser = parser;
		this.fvirtualcointype = fvirtualcointype;
	}

	@Override
	public void run() {

//		重启时取消所有的机器人挂单
		System.out.println("初始化取消");
		cancelEntrust(fvirtualcointype.getFid(), EntrustRobotStatusEnum.Robot1);
		System.out.println(fvirtualcointype.getFname() + "线程启动...");
		while (true) {

			boolean isUpdateCancel = false;
			try {
				long start = System.currentTimeMillis();
				System.out.println("机器人刷单状态：" + parser.isOpen("robotControl"));
				if (parser.isOpen("robotControl")) {
					int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
					int[] rets = parser.getIdleTimeZone("robotControl");
					System.out.println("现在时间是：" + hour);
					if (hour >= rets[0] && hour < rets[1])
						strategy(parser.getIdleAmount("robotControl"), parser.getAdd("robotControl"));
					else
						strategy(parser.getAmount("robotControl"), parser.getAdd("robotControl"));
					System.out.println("生成一个订单用时：" + (System.currentTimeMillis() - start));
					try {

						if (hour >= rets[0] && hour < rets[1])// 控制刷新频率
							Thread.sleep((long) (parser.getIdleInterval("robotControl") * 3000));
						else
							Thread.sleep((long) (parser.getInterval("robotControl") * 3000));
					} catch (InterruptedException e) {
						//用于中断死循环
						break;
					}
				} else {

					// 取消所有未成交的robotStatus=1的挂单
					if (!isUpdateCancel) {

						int vid = fvirtualcointype.getFid();
						cancelEntrust(vid, EntrustRobotStatusEnum.Robot1);
						isUpdateCancel = true;
					}
					try {
						Thread.sleep(10000L);
					} catch (InterruptedException e) {
						break;
					}
				}
			} catch (Exception e) {

				if (!isUpdateCancel) {

					int vid = fvirtualcointype.getFid();
					cancelEntrust(vid, EntrustRobotStatusEnum.Robot1);
					isUpdateCancel = true;
				}
				e.printStackTrace();

			}
		}
	}

	private void strategy(double amount, double add) {

		synchronized (fvirtualcointype) {

			int vid = fvirtualcointype.getFid();

			double highestBuyPrice = realTimeData.getHighestBuyPrizeExceptRobot(fvirtualcointype.getFid());// +
																											// 0.0001D;
			double lowestSellPrice = realTimeData.getLowestSellPrizeExceptRobot(fvirtualcointype.getFid());// -
																											// 0.0001D;

			double lowestBuyPriceByRobert = realTimeData.getLowestBuyPriceByRobot(vid);
			double highestSellPriceByRobert = realTimeData.getHighestSellPriceByRobot(vid);

			double buyCountByRobert = realTimeData.getBuyEntrustCountByRobot(vid);
			double sellCountByRobert = realTimeData.getSellEntrustCountByRobot(vid);


			double buyPrice = getPrice((highestBuyPrice + 2 * lowestSellPrice) / 3, lowestSellPrice);
			double buyCount = getCount(buyPrice, Utils.guassian(amount / 2, amount));// 100人民币以内

			double sellPrice = getPrice((highestBuyPrice + 2 * lowestSellPrice) / 3, lowestSellPrice);
			System.out.println("sellPrice : " + sellPrice + ", highestBuyPrice: " + highestBuyPrice + ", lowestSellPrice:" + lowestSellPrice);
			double sellCount = getCount(sellPrice, Utils.guassian(amount / 2, amount));// 100人民币以内

			if (buyCountByRobert > sellCountByRobert) {

				sellPrice = lowestBuyPriceByRobert > buyPrice ? buyPrice : lowestBuyPriceByRobert;
				System.out.println("lowestBuyPriceByRobert: " + lowestBuyPriceByRobert + ", buyPrice: " + buyPrice + ",lowestBuyPriceByRobert: " + lowestBuyPriceByRobert);
				sellCount = Utils.getDouble(buyCountByRobert + buyCount + Utils.guassian(add / 2, add), 4);

			} else if (buyCountByRobert < sellCountByRobert) {

				buyPrice = highestSellPriceByRobert < sellPrice ? sellPrice : highestSellPriceByRobert;
				buyCount = Utils.getDouble(sellCountByRobert + sellCount + Utils.guassian(add / 2, add), 4);
			}

			if (RandomUtils.nextBoolean()) {

				double[] buyCounts = splitCount(buyCount);
				double[] sellCounts = splitCount(sellCount);
				for (int i = 0; i < 2; i++) {

					btcTradeSubmit(0, vid, buyCounts[i], buyPrice + RandomUtils.nextDouble() / 50, EntrustTypeEnum.BUY, EntrustRobotStatusEnum.Robot1);
					btcTradeSubmit(0, vid, sellCounts[i], sellPrice + RandomUtils.nextDouble() / 50, EntrustTypeEnum.SELL, EntrustRobotStatusEnum.Robot1);
				}

				System.out.println("货币类型：" + vid + ", buyCount: " + buyCount + ", buyPrice: " + buyPrice + ", sellCount: " + sellCount + ", sellPrice: " + sellPrice);

			} else {

				double[] buyCounts = splitCount(buyCount);
				double[] sellCounts = splitCount(sellCount);
				for (int i = 0; i < 2; i++) {

					btcTradeSubmit(0, vid, sellCounts[i], sellPrice - RandomUtils.nextDouble() / 50, EntrustTypeEnum.SELL, EntrustRobotStatusEnum.Robot1);
					btcTradeSubmit(0, vid, buyCounts[i], buyPrice - RandomUtils.nextDouble() / 50, EntrustTypeEnum.BUY, EntrustRobotStatusEnum.Robot1);
				}

				System.out.println("货币类型：" + vid + ", buyCount: " + buyCount + ", buyPrice: " + buyPrice + ", sellCount: " + sellCount + ", sellPrice: " + sellPrice);
			}
		}
	}

	private double getPrice(double low, double high) {
		return Utils.getDouble(Math.random() * (high - low) + low, 4);
	}

	private double getCount(double price, double amount) {
		if (price <= 0)
			return 10;
		double ret = amount / price;
		return Utils.getDouble(ret, 4);
	}

	private Fentrust btcTradeSubmit(@RequestParam(required = false, defaultValue = "0") int limited, // 是否按照市场价买入
			@RequestParam(required = true) int symbol, // 币种
			@RequestParam(required = true) double tradeAmount, // 数量
			@RequestParam(required = true) double tradeCnyPrice, // 单价
			@RequestParam(required = true) int entrustType, // 买卖类型
			@RequestParam(required = true) int robotStatus // 机器人状态
	) {

		tradeAmount = Utils.getDouble(tradeAmount, 4);
		tradeCnyPrice = Utils.getDouble(tradeCnyPrice, 4);

		Fvirtualcointype fvirtualcointype = this.frontVirtualCoinService.findFvirtualCoinById(symbol);
		if (fvirtualcointype == null || !fvirtualcointype.isFisShare() || fvirtualcointype.getFstatus() == VirtualCoinTypeStatusEnum.Abnormal) {
			return null;
		}

		if (tradeAmount < 0.0001D) {
			return null;
		}

		if (tradeCnyPrice < 0.0001D) {
			return null;
		}

		Fuser fuser = frontUserService.findById(Constants.RobotID);
		try {
			if (entrustType == EntrustTypeEnum.BUY) {
				System.out.println("委托买入挂单：挂单种类：" + symbol);
				Fentrust buyFentrust = frontTradeService.updateEntrustBuy(symbol, tradeAmount, tradeCnyPrice, fuser, limited == 1, robotStatus, null);
				frontTradeService.sentToMongoAndMQBuy(limited == 1, symbol, buyFentrust);
				return buyFentrust;
			} else if (entrustType == EntrustTypeEnum.SELL) {
				System.out.println("委托卖出挂单：挂单种类：" + symbol);
				Fentrust sellFentrust = frontTradeService.updateEntrustSell(symbol, tradeAmount, tradeCnyPrice, fuser, limited == 1, robotStatus, null);
				frontTradeService.sentToMongoAndMQBuy(limited == 1, symbol, sellFentrust);
				return sellFentrust;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	// 取消订单
	private void cancelEntrust(int vid, int robotStatus) {
		realTimeData.updateCancelRobotEntrust(vid, robotStatus);
	}

	//把挂单分数量成两份
	private double[] splitCount(double total){
		Preconditions.checkArgument(total > 0);
		int partNum = (RandomUtils.nextInt(100) + 1);
		//把总量分成100份，取一个随机的份数
		double usedCount = total - (total / 100) * partNum;
		return new double[]{usedCount, total - usedCount};
	}
}
