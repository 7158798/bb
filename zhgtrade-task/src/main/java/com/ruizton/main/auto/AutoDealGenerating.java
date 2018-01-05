package com.ruizton.main.auto;

import com.google.common.base.Preconditions;
import com.google.gson.Gson;
import com.ruizton.main.Enum.EntrustRobotStatusEnum;
import com.ruizton.main.Enum.EntrustStatusEnum;
import com.ruizton.main.Enum.EntrustTypeEnum;
import com.ruizton.main.Enum.VirtualCoinTypeStatusEnum;
import com.ruizton.main.controller.front.FrontTradeJsonController;
import com.ruizton.main.model.*;
import com.ruizton.main.service.front.FrontTradeService;
import com.ruizton.main.service.front.FrontUserService;
import com.ruizton.main.service.front.FrontVirtualCoinService;
import com.ruizton.util.Constants;
import com.ruizton.util.RobotParser;
import com.ruizton.util.Utils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.File;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

//机器人
public class AutoDealGenerating {

	private static final Logger log = LoggerFactory.getLogger(AutoDealGenerating.class);
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

	private boolean isUpdateCancel = false;

	@Autowired
	private ExecutorService service;

	public void init() {

		service.execute(new EntrustGeneratingWork());

	}

	class EntrustGeneratingWork implements Runnable {

		public void run() {

			System.out.println("机器人生成订单线程启动...");
			System.out.println("取消机器人先前的所有挂单");
			cancelRobotEntrustAll();
			while (true) {

				if (parser.isOpen("robotControl")) {
					System.out.println("机器人状态：开启");
					isUpdateCancel = false;
					int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
					int[] rets = parser.getIdleTimeZone("robotControl");
					System.out.println("现在时间是：" + hour + "点");

					// 加入在生成挂单的过程中出现异常，那么就会跳出死循环，然后开启另一个线程
					try {
						if (hour >= rets[0] && hour < rets[1])
							strategy(parser.getIdleAmount("robotControl"), parser.getAdd("robotControl"));
						else
							strategy(parser.getAmount("robotControl"), parser.getAdd("robotControl"));
					} catch (Exception e) {
						if (!isUpdateCancel) {// 取消所有未成交的robotStatus=1的挂单
							cancelRobotEntrustAll();
							isUpdateCancel = true;
							break;
							// 异常被捕获到了，线程不会被终止，为什么还要在开启一个
							// service.execute(new EntrustGeneratingWork());
						}
					}

					try {
						if (hour >= rets[0] && hour < rets[1])// 控制刷新频率
							TimeUnit.SECONDS.sleep((long) parser.getIdleInterval("robotControl"));
						else
							TimeUnit.SECONDS.sleep((long) parser.getInterval("robotControl"));
					} catch (InterruptedException e) {

						System.out.println("刷新中断");
						e.printStackTrace();
					}

				} else {

					System.out.println("机器人状态：关闭");
					// 取消所有未成交的robotStatus=1的挂单
					if (!isUpdateCancel) {
						cancelRobotEntrustAll();
						isUpdateCancel = true;
					}

					try {
						TimeUnit.MINUTES.sleep(5);
					} catch (InterruptedException e) {
						System.out.println("机器人关闭状态时中断睡眠");
						e.printStackTrace();
					}
				}

			}

			// 从循环退出，开启另一个线程，此线程终止
			service.execute(new EntrustGeneratingWork());
		}
	}

	private void strategy(double amount, double add) {

		List<Fvirtualcointype> list = frontVirtualCoinService.findFvirtualCoinType(VirtualCoinTypeStatusEnum.Normal);
		for (Fvirtualcointype fvirtualcointype : list) {
			//有可能会有两个线程存在
			synchronized (this) {

				System.out.println(fvirtualcointype.getFid() + "----" + fvirtualcointype.getFname());
				int vid = fvirtualcointype.getFid();

				// 除机器人的最高买价，最低卖价
				double highestBuyPrice = realTimeData.getHighestBuyPrizeExceptRobot(fvirtualcointype.getFid());// +
																												// 0.0001D;
				double lowestSellPrice = realTimeData.getLowestSellPrizeExceptRobot(fvirtualcointype.getFid());// -
																												// 0.0001D;

				// 机器人的最低买价，最高卖价，有什么意义
				double lowestBuyPriceByRobert = realTimeData.getLowestBuyPriceByRobot(vid);
				double highestSellPriceByRobert = realTimeData.getHighestSellPriceByRobot(vid);

				/*
				 * if(lowestBuyPriceByRobert<highestBuyPrice)//应对区域之外挂单
				 * lowestBuyPriceByRobert = highestBuyPrice;
				 * if(highestSellPriceByRobert>lowestSellPrice)
				 * highestSellPriceByRobert = lowestSellPrice;
				 */

				// 机器人买单和卖单的总量
				double buyCountByRobot = realTimeData.getBuyEntrustCountByRobot(vid);
				double sellCountByRobot = realTimeData.getSellEntrustCountByRobot(vid);

				/*
				 * double buyAmountByRobert =
				 * realTimeData.getBuyEntrustAmountByRobert(vid); double
				 * sellAmountByRobert =
				 * realTimeData.getSellEntrustAmountByRobert(vid);
				 */

				double buyPrice = getPrice((highestBuyPrice + 2 * lowestSellPrice) / 3, lowestSellPrice);
				double buyCount = getCount(buyPrice, Utils.guassian(amount / 2, amount));// 100人民币以内

				double sellPrice = getPrice((highestBuyPrice + 2 * lowestSellPrice) / 3, lowestSellPrice);
				double sellCount = getCount(sellPrice, Utils.guassian(amount / 2, amount));// 100人民币以内

				if (buyCountByRobot > sellCountByRobot) {

					sellPrice = lowestBuyPriceByRobert > buyPrice ? buyPrice : lowestBuyPriceByRobert;
					sellCount = Utils.getDouble(buyCountByRobot + buyCount + Utils.guassian(add / 2, add), 4);

				} else if (buyCountByRobot < sellCountByRobot) {

					buyPrice = highestSellPriceByRobert < sellPrice ? sellPrice : highestSellPriceByRobert;
					buyCount = Utils.getDouble(sellCountByRobot + sellCount + Utils.guassian(add / 2, add), 4);

				}

				System.out.println("提交数据前");
				if (RandomUtils.nextBoolean()) {

					btcTradeSubmit(0, vid, buyCount, buyPrice + RandomUtils.nextInt(10) / 50, EntrustTypeEnum.BUY, EntrustRobotStatusEnum.Robot1);
					double[] sellCounts = splitCount(sellCount);
					btcTradeSubmit(0, vid, sellCounts[0], sellPrice + RandomUtils.nextInt(10) / 50, EntrustTypeEnum.SELL, EntrustRobotStatusEnum.Robot1);
					btcTradeSubmit(0, vid, sellCounts[1], sellPrice + RandomUtils.nextInt(10) / 50, EntrustTypeEnum.SELL, EntrustRobotStatusEnum.Robot1);
				} else {

					btcTradeSubmit(0, vid, sellCount, sellPrice - RandomUtils.nextInt(10) / 50, EntrustTypeEnum.SELL, EntrustRobotStatusEnum.Robot1);
					double[] buyCounts = splitCount(buyCount);
					btcTradeSubmit(0, vid, buyCounts[0], buyPrice - RandomUtils.nextInt(10) / 50, EntrustTypeEnum.BUY, EntrustRobotStatusEnum.Robot1);
					btcTradeSubmit(0, vid, buyCounts[1], buyPrice - RandomUtils.nextInt(10) / 50, EntrustTypeEnum.BUY, EntrustRobotStatusEnum.Robot1);

				}
				System.out.println("提交数据后");
			}
			break;
		}
	}

	private double getPrice(double low, double high) {
		return Utils.getDouble(Math.random() * (high - low) + low, 4);
	}

	private double getCount(double price, double amount) {
		if (price <= 0)
			return 10;

		double ret = amount / price;
		/*
		 * if( ret >=10000 ) ret = 9999;
		 */

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
			File entrustDataFile = new File("E:/entrust.txt");
			Gson gson = new Gson();
			if (entrustType == EntrustTypeEnum.BUY) {
				System.out.println("委托买入挂单：挂单种类：" + symbol);
				Fentrust buyFentrust = frontTradeService.updateEntrustBuy(symbol, tradeAmount, tradeCnyPrice, fuser, limited == 1, robotStatus, null);
				frontTradeService.sentToMongoAndMQBuy(limited == 1, symbol, buyFentrust);
				//把数据添加到
				FileUtils.write(entrustDataFile, gson.toJson(buyFentrust));
				return buyFentrust;
			} else if (entrustType == EntrustTypeEnum.SELL) {
				System.out.println("委托卖出挂单：挂单种类：" + symbol);
				Fentrust sellFentrust = frontTradeService.updateEntrustSell(symbol, tradeAmount, tradeCnyPrice, fuser, limited == 1, robotStatus, null);
				FileUtils.write(entrustDataFile, gson.toJson(sellFentrust));
				frontTradeService.sentToMongoAndMQBuy(limited == 1, symbol, sellFentrust);
				return sellFentrust;
			} else {
				log.debug("挂单类别错误");
				throw new RuntimeException("挂单类别错误");
			}
		} catch (Exception e) {
			System.out.println("提交机器人订单时异常");
			e.printStackTrace();
		}
		return null;
	}

	// 把挂单总数量成两份
	private double[] splitCount(double total) {
		Preconditions.checkArgument(total > 0);
		int partNum = (RandomUtils.nextInt(10) + 1);
		// 把总量分成10份，取一个随机的份数
		double usedCount = total - (total / 10) * partNum;
		return new double[] { usedCount, total - usedCount };
	}

	// 取消所有的机器人挂单
	private void cancelRobotEntrustAll() {
		List<Fvirtualcointype> list = frontVirtualCoinService.findFvirtualCoinType(VirtualCoinTypeStatusEnum.Normal);
		for (Fvirtualcointype fvirtualcointype : list) {
			cancelRobotEntrustByFvirtualId(fvirtualcointype.getFid());
		}
	}

	// 取消某一种机器人挂单
	private void cancelRobotEntrustByFvirtualId(int vid) {
		realTimeData.updateCancelRobotEntrust(vid, EntrustRobotStatusEnum.Robot1);
	}

	@SuppressWarnings("unused")
	private void robertDealMaking(int id) {
		boolean rehandle = false;

		Fvirtualcointype fvirtualcointype = frontVirtualCoinService.findFvirtualCoinById(id);
		Object[] buyFentrusts = realTimeData.getEntrustBuyMap(id).toArray();
		Object[] sellFentrusts = realTimeData.getEntrustSellMap(id).toArray();
		if (buyFentrusts.length > 0 && sellFentrusts.length > 0) {

			first: for (int i = 0; i < buyFentrusts.length; i++) {
				Fentrust buy = (Fentrust) buyFentrusts[i];
				if (buy.getRobotStatus() != EntrustRobotStatusEnum.Robot1) {
					continue;
				}

				if (buy.getFstatus() == EntrustStatusEnum.AllDeal || buy.getFstatus() == EntrustStatusEnum.Cancel) {
					realTimeData.removeEntrustBuyMap(id, buy);
					continue;
				}

				second: for (int j = 0; j < sellFentrusts.length; j++) {
					Fentrust sell = (Fentrust) sellFentrusts[j];
					if (sell.getRobotStatus() != EntrustRobotStatusEnum.Robot1) {
						continue;
					}

					if (sell.getFstatus() == EntrustStatusEnum.AllDeal || sell.getFstatus() == EntrustStatusEnum.Cancel) {
						realTimeData.removeEntrustSellMap(id, sell);
						continue;
					}

					if (buy.getFuser().getFid() == sell.getFuser().getFid() && !Constants.TradeSelf) {
						continue;// 自己的订单，常量判断是不是自己可以和自己交易
					}

					if (buy.getFprize() < sell.getFprize()) {
						continue;
					}

					/*********************************************************/
					/*********************************************************/
					// 解决当卖单进入时本可以成交，但是此时又进入卖单，导致卖单以低价成交的bug
					/*
					 * 当买单的时间比卖单新时，需要判断是否有比卖单旧的价格可以成交，
					 * 当买单比卖单旧时，需要判断是否有比买单旧的价格可以成交
					 */
					Timestamp buyTime = buy.getFcreateTime();
					Timestamp sellTime = sell.getFcreateTime();
					if (buyTime.getTime() >= sellTime.getTime()) {
						// 当买单的时间比卖单新时，需要判断是否有比卖单旧的价格可以成交，
						boolean hasOld = false;
						for (int x = i + 1; x < buyFentrusts.length; x++) {
							if (hasOld == true)
								continue;

							Fentrust tmp = (Fentrust) buyFentrusts[x];
							if (tmp.getFprize() >= sell.getFprize() && tmp.getFcreateTime().getTime() < sell.getFcreateTime().getTime() && tmp.getRobotStatus() == EntrustRobotStatusEnum.Robot1) {
								hasOld = true;
							}
						}
						if (hasOld == true) {
							break second;
						}

					} else {
						// 当买单比卖单旧时，需要判断是否有比买单旧的价格可以成交
						boolean hasNew = false;
						for (int x = j + 1; x < sellFentrusts.length; x++) {
							if (hasNew == true)
								continue;

							Fentrust tmp = (Fentrust) sellFentrusts[x];
							if (buy.getFprize() >= tmp.getFprize() && tmp.getFcreateTime().getTime() < buy.getFcreateTime().getTime() && tmp.getRobotStatus() == EntrustRobotStatusEnum.Robot1) {
								hasNew = true;
							}
						}
						if (hasNew == true) {
							continue second;
						}
					}
					/*********************************************************/
					/*********************************************************/

					// begin

					double buyCount = buy.getFleftCount();
					buyCount = buyCount > sell.getFleftCount() ? sell.getFleftCount() : buyCount;
					double buyPrize = 0d;
					if (buy.getFcreateTime().getTime() < sell.getFcreateTime().getTime()) {
						buyPrize = buy.getFprize();
					} else {
						buyPrize = sell.getFprize();
					}
					// buyPrize = buy.getFprize() ;

					// protect
					if (buyCount > buy.getFleftCount() || buyCount > sell.getFleftCount() || buyPrize > buy.getFprize()) {
						log.error("dealmaking error!");
						return;
					}

					Fentrustlog buyFentrustlog = new Fentrustlog();
					buyFentrustlog.setFamount(buyCount * buyPrize);
					buyFentrustlog.setFcount(buyCount);
					buyFentrustlog.setFcreateTime(Utils.getTimestamp());
					buyFentrustlog.setFprize(buyPrize);
					buyFentrustlog.setIsactive(buy.getFcreateTime().getTime() > sell.getFcreateTime().getTime());
					buyFentrustlog.setFentrust(buy);
					buyFentrustlog.setfEntrustType(EntrustTypeEnum.BUY);
					buyFentrustlog.setFvirtualcointype(fvirtualcointype);

					Fentrustlog sellFentrustlog = new Fentrustlog();
					sellFentrustlog.setFamount(buyCount * buyPrize);
					sellFentrustlog.setFcount(buyCount);
					sellFentrustlog.setFcreateTime(Utils.getTimestamp());
					sellFentrustlog.setFprize(buyPrize);
					sellFentrustlog.setIsactive(sell.getFcreateTime().getTime() > buy.getFcreateTime().getTime());
					sellFentrustlog.setFentrust(sell);
					sellFentrustlog.setfEntrustType(EntrustTypeEnum.SELL);
					sellFentrustlog.setFvirtualcointype(fvirtualcointype);

					boolean ret = false;
					try {
						frontTradeService.updateDealMaking(buy, sell, buyFentrustlog, sellFentrustlog, id);
						ret = true;
					} catch (Exception e) {
						// e.printStackTrace();
					}

					if (ret) {
						// 加入成功交易
						realTimeData.addEntrustSuccessMap(id, sellFentrustlog);
						// 加入成功交易
						realTimeData.addEntrustSuccessMap(id, buyFentrustlog);

						if (buy.getFstatus() == EntrustStatusEnum.Going || buy.getFstatus() == EntrustStatusEnum.PartDeal) {
							if (buy.isFisLimit()) {
								realTimeData.addEntrustLimitBuyMap(id, buy);
							} else {
								realTimeData.addEntrustBuyMap(id, buy);
							}
						} else if (buy.getFstatus() == EntrustStatusEnum.AllDeal) {
							if (buy.isFisLimit()) {
								realTimeData.removeEntrustLimitBuyMap(id, buy);
							} else {
								realTimeData.removeEntrustBuyMap(id, buy);
							}
						}

						if (sell.getFstatus() == EntrustStatusEnum.Going || sell.getFstatus() == EntrustStatusEnum.PartDeal) {
							if (sell.isFisLimit()) {
								realTimeData.addEntrustLimitSellMap(id, sell);
							} else {
								realTimeData.addEntrustSellMap(id, sell);
							}
						} else if (sell.getFstatus() == EntrustStatusEnum.AllDeal) {
							if (sell.isFisLimit()) {
								realTimeData.removeEntrustLimitSellMap(id, sell);
							} else {
								realTimeData.removeEntrustSellMap(id, sell);
							}

						}

						rehandle = true;
					} else {
						buy = frontTradeService.findFentrustById(buy.getFid());
						sell = frontTradeService.findFentrustById(sell.getFid());

						if (buy == null || sell == null) {
							log.error("buy or sell null;");
							continue;
						}

						if (buy.getFstatus() == EntrustStatusEnum.Going || buy.getFstatus() == EntrustStatusEnum.PartDeal) {
							realTimeData.addEntrustBuyMap(id, buy);
						} else {
							realTimeData.removeEntrustBuyMap(id, buy);
						}
						if (sell.getFstatus() == EntrustStatusEnum.Going || sell.getFstatus() == EntrustStatusEnum.PartDeal) {
							realTimeData.addEntrustSellMap(id, sell);
						} else {
							realTimeData.removeEntrustSellMap(id, sell);
						}
					}

					break first;
					// end
				}
			}
			if (rehandle) {
				robertDealMaking(id);
			}

		}
	}

}
