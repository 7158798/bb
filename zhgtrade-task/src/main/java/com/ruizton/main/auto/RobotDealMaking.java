//package com.ruizton.main.auto;
//
//import java.util.List;
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.TimeUnit;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.mongodb.core.MongoTemplate;
//import org.springframework.data.mongodb.core.query.Criteria;
//import org.springframework.data.mongodb.core.query.Query;
//
//import com.mongodb.BasicDBObject;
//import com.mongodb.DBCursor;
//import com.mongodb.DBObject;
//import com.ruizton.main.Enum.EntrustRobotStatusEnum;
//import com.ruizton.main.Enum.EntrustTypeEnum;
//import com.ruizton.main.Enum.VirtualCoinTypeStatusEnum;
//import com.ruizton.main.dto.Constants;
//import com.ruizton.main.dto.FentrustData;
//import com.ruizton.main.model.Fvirtualcointype;
//import com.ruizton.main.service.BaseService;
//import com.ruizton.main.service.front.FrontTradeService;
//import com.ruizton.main.service.front.FrontVirtualCoinService;
//
//import org.springframework.data.mongodb.core.query.Update;
//
//import com.ruizton.main.dao.FvirtualcointypeDAO;
//import com.ruizton.main.mq.MessageListener;
//import com.ruizton.main.mq.MessageQueueService;
//import com.ruizton.main.mq.QueueConstants;
//
//public class RobotDealMaking {
//
//	@Autowired
//	private static final Logger log = LoggerFactory.getLogger(RobotDealMaking.class);
//
//	@Autowired
//	private FrontTradeService frontTradeService;
//	@Autowired
//	private MongoTemplate mongoTemplate;
//	@Autowired
//	MessageQueueService messageQueueService;
//	@Autowired
//	BaseService baseService;
//	@Autowired
//	FvirtualcointypeDAO fvirtualcointypeDAO;
//
//	@Autowired
//	private FrontVirtualCoinService frontVirtualCoinService;
//
//	@Autowired
//	private ExecutorService executorService;
//	@Autowired
//	private RealTimeData realTimeData;
//
//	public void init() {
//
//		System.out.println("监听....");
//		executorService.execute(new StartListener());
//	}
//
//	class StartListener implements Runnable {
//
//		@Override
//		public void run() {
//
//			List<Fvirtualcointype> list = frontVirtualCoinService.findFvirtualCoinType(VirtualCoinTypeStatusEnum.Normal);
//			for (Fvirtualcointype fvirtualcointype : list) {
//				System.out.println("监听" + fvirtualcointype.getFname() + "消息队列");
//				startMatchListener(fvirtualcointype.getFid());
//			}
//
//			while (true) {
//
//				List<Fvirtualcointype> reList = frontVirtualCoinService.findFvirtualCoinType(VirtualCoinTypeStatusEnum.Normal);
//				if (!list.containsAll(reList)) {
//					System.out.println("有新的货币类型要添加");
//					for (Fvirtualcointype fvirtualcointype : reList) {
//						// 重新获取的list中的元素不在原有的list中，那么就开启一个监听，并且把新增加的币种加入到原有list中
//						if (!list.contains(fvirtualcointype)) {
//							startMatchListener(fvirtualcointype.getFid());
//							list.add(fvirtualcointype);
//						}
//					}
//
//				} else {
//					try {
//						TimeUnit.MINUTES.sleep(5);
//					} catch (InterruptedException e) {
//						e.printStackTrace();
//					}
//				}
//			}
//		}
//
//	}
//
//	/**
//	 *
//	 * 买卖单添加消息队列监听
//	 *
//	 * @author 石湘祁
//	 */
//	private void startMatchListener(int id) {
//
//		System.out.println("===============监听=================");
//		messageQueueService.subscribe(QueueConstants.ROBOT_ENTRUST_QUEUE + id, new MessageListener<FentrustData>() {
//
//			@Override
//			public void onMessage(FentrustData message) {
//				long start = System.currentTimeMillis();
//				if (message.getRobotStatus() == EntrustRobotStatusEnum.Robot1) {
//					System.out.println("接收到消息：挂单ID：" + message.getFid() + "价格：" + message.getFprize() + "数量：" + message.getFleftCount());
//					// 确认此挂单是否过时
//					DBCursor cursor = mongoTemplate.getCollection(Constants.FENTRUST_DATA).find(new BasicDBObject("fid", message.getFid()), new BasicDBObject("fleftCount", 1));
//					if (cursor.hasNext()) {
//						double now = (double) cursor.next().get("fleftCount");
//						System.out.println(now + "VS" + message.getFleftCount());
//						if (now != message.getFleftCount()) {
//							// 更新挂单剩余量
//							message.setFleftCount(now);
//							System.out.println("挂单剩余量变更为" + now);
//						}
//						cursor.close();
//						final int fentrustType = message.getFentrustType();
//
//						// 买单匹配
//						if (fentrustType == EntrustTypeEnum.BUY) {
//							matchBuy(message);
//						}
//						// 卖单匹配
//						else if (fentrustType == EntrustTypeEnum.SELL) {
//							matchSell(message);
//						} else {
//							log.error("挂单类型错误");
//						}
//						System.out.println("此挂单" + message.getFid() + "撮合完毕");
//						System.out.println("撮合一个挂单用时：" + (System.currentTimeMillis() - start));
//					}
//					cursor.close();
//				}
//			}
//		}, FentrustData.class);
//
//	}
//
//	private void matchBuy(FentrustData message) {
//
//		int fviFid = message.getFviFid();
//		double fCount = message.getFleftCount();
//		double fleftCount = fCount;
//		int fid = message.getFid();
//		double fprice = message.getFprize();
//
//		boolean flag = true;
//		System.out.println("==================进入买单匹配==================");
//		while (flag) {
//
//			// 卖价小于等于买价的最小最早卖家
//			DBCursor cursor = mongoTemplate.getCollection(Constants.FENTRUST_DATA).find(new BasicDBObject("robotStatus", EntrustRobotStatusEnum.Robot1).append("fentrustType", EntrustTypeEnum.SELL).append("fviFid", fviFid).append("fprize", new BasicDBObject("$lte", fprice)), new BasicDBObject("fprize", 1).append("fid", 1).append("fleftCount", 1)).sort(new BasicDBObject("fprize", 1).append("fid", 1)).limit(1);
//			if (cursor.hasNext()) {
//
//				DBObject dbObject = cursor.next();
//				System.out.println("匹配到一条市场卖单：" + dbObject.toString());
//				double successPrice = (double) dbObject.get("fprize");
//				int sellFid = (int) dbObject.get("fid");
//				double sellCount = (double) dbObject.get("fleftCount");
//				double successCount = 0;// 单笔成交量
//				FentrustData sellFentrustData = mongoTemplate.findOne(Query.query(Criteria.where("fid").is(sellFid)), FentrustData.class, Constants.FENTRUST_DATA);
//
//				// 买单和卖单有三种关系，（买单数-卖单数）的值会落在数轴中，---------（-0.001）-------------------（0.001）-------------
//				if ((fleftCount - sellCount) >= 0.0001) {
//					// 剩余买单数>卖单数
//					successCount = sellCount;
//					fleftCount -= successCount;
//					realTimeData.removeEntrustSellMap(fviFid, sellFentrustData);
////					realTimeData.updateMongo(fid, fviFid, successCount, successPrice, EntrustTypeEnum.BUY);
//				} else if (sellCount - fleftCount >= 0.0001) {
//					// 卖单数>剩余买单数
//					fleftCount = 0;
//					successCount = fleftCount;
//					realTimeData.removeEntrustBuyMap(fviFid, message);
////					realTimeData.updateMongo(sellFid, fviFid, successCount, successPrice, EntrustTypeEnum.SELL);
//					flag = false;
//				} else {
//					// |买单数-卖单数| < 0.001
//					if (fleftCount > sellCount) {
//						successCount = sellCount;
//					} else {
//						successCount = fleftCount;
//					}
//					fleftCount = 0;
//					realTimeData.removeEntrustBuyMap(fviFid, message);
//					realTimeData.removeEntrustSellMap(fviFid, sellFentrustData);
//					flag = false;
//				}
////				frontTradeService.updateDealMaking(fid, sellFid, fviFid, successCount, successPrice);
//				System.out.println("一个市场卖单撮合完毕，此笔成交量为：" + successCount + "此笔成交价：" + successPrice + "总成交量为：" + (fCount - fleftCount));
//			} else {
//
//				log.info("此单" + fid + "未发现匹配数据");
//				System.out.println("此单" + fid + "未发现匹配数据");
//				flag = false;
//			}
//			cursor.close();
//		}
//	}
//
//	private void matchSell(FentrustData message) {
//
//		int fviFid = message.getFviFid();
//		double fCount = message.getFleftCount();
//		double fleftCount = fCount;
//		int fid = message.getFid();
//		double fprice = message.getFprize();
//
//		boolean flag = true;
//		System.out.println("==================进入卖单匹配==================");
//		while (flag) {
//
//			// 大于卖价的最早买家
//			DBCursor cursor = mongoTemplate.getCollection(Constants.FENTRUST_DATA).find(new BasicDBObject("fentrustType", EntrustTypeEnum.BUY).append("robotStatus", EntrustRobotStatusEnum.Robot1)
//					.append("fviFid", fviFid).append("fprize", new BasicDBObject("$gte", fprice)), new BasicDBObject("fprize", 1).append("fid", 1).append("fleftCount", 1))
//					.sort(new BasicDBObject("fid", 1)).limit(1);
//			if (cursor.hasNext()) {
//				DBObject dbObject = cursor.next();
//				System.out.println("匹配到一条市场卖单：" + dbObject.toString());
//				double successPrice = fprice;
//				int buyFid = (int) dbObject.get("fid");
//				double buyCount = (double) dbObject.get("fleftCount");
//				double successCount = 0;// 单笔成交量
//				FentrustData buyFentrustData = mongoTemplate.findOne(Query.query(Criteria.where("fid").is(buyFid)), FentrustData.class, Constants.FENTRUST_DATA);
//
//				// 买单和卖单有三种关系，（买单数-卖单数）的值会落在数轴中，---------（0.001）-------------------（-0.001）-------------
//				if ((fleftCount - buyCount) >= 0.001) {
//					// 剩余卖单数>买单数
//					successCount = buyCount;
//					fleftCount -= successCount;
//					realTimeData.removeEntrustSellMap(fviFid, buyFentrustData);
////					realTimeData.updateMongo(fid, fviFid, successCount, successPrice, EntrustTypeEnum.SELL);
//				} else if (buyCount - fleftCount >= 0.001) {
//					// 买单数>剩余卖单数
//					successCount = fleftCount;
//					fleftCount = 0;
//					realTimeData.removeEntrustBuyMap(fviFid, message);
////					realTimeData.updateMongo(buyFid, fviFid, successCount, successPrice, EntrustTypeEnum.BUY);
//					flag = false;
//				} else {
//					// |卖单数-买单数| < 0.001
//					if (fleftCount > buyCount) {
//						successCount = buyCount;
//					} else {
//						successCount = fleftCount;
//					}
//					fleftCount = 0;
//					realTimeData.removeEntrustBuyMap(fviFid, message);
//					realTimeData.removeEntrustSellMap(fviFid, buyFentrustData);
//					flag = false;
//				}
////				frontTradeService.updateDealMaking(buyFid, fid, fviFid, successCount, successPrice);
//				System.out.println("一个市场卖单撮合完毕，此笔成交量为：" + successCount + "此笔成交价：" + successPrice + "总成交量为：" + (fCount - fleftCount));
//			} else {
//				log.info("此单" + fid + "未发现匹配数据");
//				System.out.println("此单" + fid + "未发现匹配数据");
//				flag = false;
//			}
//			cursor.close();
//		}
//	}
//
//	/**
//	 *
//	 * 买单匹配
//	 *
//	 * @author 石湘祁
//	 * @param fCount
//	 *            总需求量
//	 * @param fviFid
//	 *            虚拟币ID
//	 * @param fprice
//	 *            挂单价格
//	 * @param fid
//	 *            挂单ID
//	 * @return
//	 */
//	public void matchBuy2(FentrustData message) {
//
//		int fviFid = message.getFviFid();
//		double fCount = message.getFleftCount();
//		int fid = message.getFid();
//		double fprice = message.getFprize();
//
//		boolean flag = true;
//		double tradeAmount = 0;// 总成交量
//		System.out.println("==================进入买单匹配==================");
//		while (flag) {
//			DBCursor cursor = mongoTemplate
//					.getCollection(Constants.FENTRUST_DATA).find(new BasicDBObject("fentrustType", EntrustTypeEnum.SELL).append("robotStatus", EntrustRobotStatusEnum.Robot1).append("fviFid", fviFid)
//							.append("fprize", new BasicDBObject("$lte", fprice)), new BasicDBObject("fprize", 1).append("fid", 1).append("fleftCount", 1))
//					.sort(new BasicDBObject("fprize", 1).append("fid", 1)).limit(1);
//			if (cursor.hasNext()) {
//				DBObject dbObject = cursor.next();
//				System.out.println("匹配到一条市场卖单：" + dbObject.toString());
//				double successPrice = (double) dbObject.get("fprize");
//				int sellFid = (int) dbObject.get("fid");
//				double sellCount = (double) dbObject.get("fleftCount");
//				double successCount;// 单笔成交量
//				tradeAmount += sellCount;
//				Criteria buyCriteria = Criteria.where("fid").is(fid);
//				Criteria sellCriteria = Criteria.where("fid").is(sellFid);
//				if (tradeAmount >= fCount) {
//					// 卖单有剩
//					successCount = sellCount - (tradeAmount - fCount);
//					flag = false;
//					tradeAmount = fCount;
//					realTimeData.removeEntrustBuyMap(fid, message);
//				} else {
//					// 卖单不够，不改变flag，继续匹配
//					successCount = sellCount;
//					if (fCount - tradeAmount < 0.001) {
//						// 挂单剩余量少于0.001时不再匹配
//						flag = false;
//						realTimeData.removeEntrustBuyMap(fid, message);
//					} else {
//						mongoTemplate.updateFirst(new Query(buyCriteria), Update.update("fleftCount", fCount - tradeAmount), Constants.FENTRUST_DATA);
//					}
//				}
//				// 每单匹配完成之后更新交易及挂单信息
//				if (sellCount - successCount < 0.001) {
//					// 卖单剩余极少
//					realTimeData.removeEntrustBuyMap(fid, message);
//				} else {
//					mongoTemplate.updateFirst(new Query(sellCriteria), Update.update("fleftCount", sellCount - successCount), Constants.FENTRUST_DATA);
//				}
////				frontTradeService.updateDealMaking(fid, sellFid, fviFid, successCount, successPrice);
//				System.out.println("一个市场卖单撮合完毕，此笔成交量为：" + successCount + "此笔成交价：" + successPrice + "总成交量为：" + tradeAmount);
//			} else {
//				log.info("此单" + fid + "未发现匹配数据");
//				System.out.println("此单" + fid + "未发现匹配数据");
//				flag = false;
//			}
//			cursor.close();
//		}
//	}
//
//	/**
//	 *
//	 * 卖单匹配
//	 *
//	 * @author 石湘祁
//	 * @param fCount
//	 *            总需求量
//	 * @param fviFid
//	 *            虚拟币ID
//	 * @param fprice
//	 *            挂单价格
//	 * @param fid
//	 *            挂单ID
//	 * @return
//	 */
//	public void matchSell2(FentrustData message) {
//
//		int fviFid = message.getFviFid();
//		double fCount = message.getFleftCount();
//		int fid = message.getFid();
//		double fprice = message.getFprize();
//
//		boolean flag = true;
//		double tradeAmount = 0;// 总成交量
//		System.out.println("==================进入卖单匹配==================");
//		while (flag) {
//			DBCursor cursor = mongoTemplate.getCollection(Constants.FENTRUST_DATA).find(new BasicDBObject("fentrustType", EntrustTypeEnum.BUY).append("robotStatus", EntrustRobotStatusEnum.Robot1)
//					.append("fviFid", fviFid).append("fprize", new BasicDBObject("$gte", fprice)), new BasicDBObject("fprize", 1).append("fid", 1).append("fleftCount", 1))
//					.sort(new BasicDBObject("fid", 1)).limit(1);
//			if (cursor.hasNext()) {
//
//				DBObject dbObject = cursor.next();
//				System.out.println("匹配到一条市场卖单：" + dbObject.toString());
//				double successPrice = fprice;
//				double buyCount = (double) dbObject.get("fleftCount");
//				int buyFid = (int) dbObject.get("fid");
//				double successCount; // 单笔成交量
//				tradeAmount += buyCount;
//				Criteria sellCriteria = Criteria.where("fid").is(fid);
//				Criteria buyCriteria = Criteria.where("fid").is(buyFid);
//				if (tradeAmount >= fCount) {
//					// 买单有剩
//					successCount = buyCount - (tradeAmount - fCount);
//					flag = false;
//					tradeAmount = fCount;
//					realTimeData.removeEntrustBuyMap(fid, message);
//				} else {
//					// 买单不够，继续匹配
//					successCount = buyCount;
//					if (fCount - tradeAmount < 0.001) {
//						// 挂单剩余量少于0.001时不再匹配
//						flag = false;
//						realTimeData.removeEntrustBuyMap(fid, message);
//					} else {
//						mongoTemplate.updateFirst(new Query(sellCriteria), Update.update("fleftCount", fCount - tradeAmount), Constants.FENTRUST_DATA);
//					}
//				}
//				// 每单匹配完更新交易及挂单信息
//				if (buyCount - successCount < 0.001) {
//					// 卖单剩余极少
//					realTimeData.removeEntrustBuyMap(fid, message);
//				} else {
//					mongoTemplate.updateFirst(new Query(buyCriteria), Update.update("fleftCount", buyCount - successCount), Constants.FENTRUST_DATA);
//				}
//
////				frontTradeService.updateDealMaking(buyFid, fid, fviFid, successCount, successPrice);
//				System.out.println("一个市场卖单撮合完毕，此笔成交量为：" + successCount + "此笔成交价：" + successPrice + "总成交量为：" + tradeAmount);
//
//			} else {
//
//				log.info("此单" + fid + "未发现匹配数据");
//				System.out.println("此单" + fid + "未发现匹配数据");
//				flag = false;
//			}
//			cursor.close();
//		}
//	}
//}
