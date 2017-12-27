package com.ruizton.main.service.admin;

import com.ruizton.main.Enum.CnyOrCoinEnum;
import com.ruizton.main.Enum.LendEntrustLogStatusEnum;
import com.ruizton.main.Enum.LendEntrustStatus2Enum;
import com.ruizton.main.Enum.ReturnTypeEnum;
import com.ruizton.main.dao.*;
import com.ruizton.main.model.*;
import com.ruizton.util.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;

@Service
public class LendEntrustService {
	@Autowired
	private FlendentrustDAO lendentrustDAO;
	@Autowired
	private FlendentrustlogDAO lendentrustlogDAO;
	@Autowired
	private FuserDAO userDAO;
	@Autowired
	private FwalletDAO walletDAO;
	@Autowired
	private FvirtualwalletDAO virtualwalletDAO;
//	@Autowired
//	private RealTimeData realTimeData;
	@Autowired
	private FlendentrustfinishlogDAO lendentrustfinishlogDAO;

	public Flendentrust findById(int id) {
		return this.lendentrustDAO.findById(id);
	}
	
	public Flendentrust findById1(int id) {
		Flendentrust flendentrust = this.lendentrustDAO.findById(id);
		if(flendentrust.getFuser() != null){
			flendentrust.getFuser().getFnickName();
		}
		return flendentrust;
	}

	public void saveObj(Flendentrust obj) {
		this.lendentrustDAO.save(obj);
	}

	public void deleteObj(int id) {
		Flendentrust obj = this.lendentrustDAO.findById(id);
		this.lendentrustDAO.delete(obj);
	}

	public void updateObj(Flendentrust obj) {
		this.lendentrustDAO.attachDirty(obj);
	}

	public List<Flendentrust> findByProperty(String name, Object value) {
		return this.lendentrustDAO.findByProperty(name, value);
	}

	public List<Flendentrust> findAll() {
		return this.lendentrustDAO.findAll();
	}

	public List<Flendentrust> list(int firstResult, int maxResults,
			String filter,boolean isFY) {
		List<Flendentrust> all = this.lendentrustDAO.list(firstResult, maxResults, filter,isFY);
		for (Flendentrust flendentrust : all) {
			if(flendentrust.getFuser() != null){
				flendentrust.getFuser().getFnickName();
			}
			if(flendentrust.getFvirtualcointype() != null){
				flendentrust.getFvirtualcointype().getFname();
			}
		}
		
		return all;
	}
	
	public double getBorrowAmt(int userId,int cnyOrCoin) {
		return this.lendentrustDAO.getBorrowAmt(userId,cnyOrCoin);
	}
	
	public List<Map> getHistoryList(int type,int cnyOrCoin) {
		return this.lendentrustDAO.getHistoryList(type, cnyOrCoin);
	}
	
	public void updateDealLendLog(Flendentrustlog lendentrustlog,Flendentrust borrow,Flendentrust lend) {
		try {
			this.lendentrustDAO.attachDirty(lend);
			this.lendentrustDAO.attachDirty(borrow);
			this.lendentrustlogDAO.save(lendentrustlog);
			if(lend.getFcnyOrCoin() == CnyOrCoinEnum.CNY){
				int lendUserId = lend.getFuser().getFid();
				Fuser lenduser = this.userDAO.findById(lendUserId);
				Fwallet lendwallet = lenduser.getFwallet();
				lendwallet.setFfrozenLendCny(Utils.getDouble(lendwallet.getFfrozenLendCny()-lendentrustlog.getFamount(),2));
				lendwallet.setFalreadyLendCny(Utils.getDouble(lendwallet.getFalreadyLendCny()+lendentrustlog.getFamount(),2));
				this.walletDAO.attachDirty(lendwallet);
				
				if(borrow.getFreturnType() == ReturnTypeEnum.USER_VALUE){
					int borrowUserId = borrow.getFuser().getFid();
					Fuser borrowuser = this.userDAO.findById(borrowUserId);
					Fwallet borrowwallet = borrowuser.getFwallet();
					borrowwallet.setFborrowCny(Utils.getDouble(borrowwallet.getFborrowCny()+lendentrustlog.getFamount(),2));
					borrowwallet.setFtotalRmb(Utils.getDouble(borrowwallet.getFtotalRmb()+lendentrustlog.getFamount(),2));
					this.walletDAO.attachDirty(borrowwallet);
				}else{
					int borrowUserId = borrow.getFuser().getFid();
					Fuser borrowuser = this.userDAO.findById(borrowUserId);
					Fwallet borrowwallet = borrowuser.getFwallet();
					borrowwallet.setFborrowCny(Utils.getDouble(borrowwallet.getFborrowCny()+lendentrustlog.getFamount(),2));
					double amt = lendentrustlog.getFamount();//这个钱用来还款
					int lendentrustlogId = borrow.getFlendentrustlogId();
					returnBackMoney(lendentrustlogId, amt,borrowwallet);
				}
			}else{
				int vid = lend.getFvirtualcointype().getFid();
				int lenduserId = lend.getFuser().getFid();
				String filter = "where fuser.fid="+lenduserId+" and fvirtualcointype.fid="+vid;
				Fvirtualwallet virtualwallet = this.virtualwalletDAO.list(0, 0, filter, false).get(0);
				virtualwallet.setFfrozenLendBtc(Utils.getDouble(virtualwallet.getFfrozenLendBtc()-lendentrustlog.getFamount(),2));
				virtualwallet.setFalreadyLendBtc(Utils.getDouble(virtualwallet.getFalreadyLendBtc()+lendentrustlog.getFamount(),2));
				this.virtualwalletDAO.attachDirty(virtualwallet);
				
				if(borrow.getFreturnType() == ReturnTypeEnum.USER_VALUE){
					int borrowuserId = borrow.getFuser().getFid();
					String filter1 = "where fuser.fid="+borrowuserId+" and fvirtualcointype.fid="+vid;
					Fvirtualwallet virtualwallet1 = this.virtualwalletDAO.list(0, 0, filter1, false).get(0);
					virtualwallet1.setFborrowBtc(Utils.getDouble(virtualwallet1.getFborrowBtc()+lendentrustlog.getFamount(),2));
					virtualwallet1.setFtotal(Utils.getDouble(virtualwallet1.getFtotal()+lendentrustlog.getFamount(),2));
					this.virtualwalletDAO.attachDirty(virtualwallet1);
				}else{
					int borrowuserId = borrow.getFuser().getFid();
					String filter1 = "where fuser.fid="+borrowuserId+" and fvirtualcointype.fid="+vid;
					Fvirtualwallet virtualwallet1 = this.virtualwalletDAO.list(0, 0, filter1, false).get(0);
					virtualwallet1.setFborrowBtc(Utils.getDouble(virtualwallet1.getFborrowBtc()+lendentrustlog.getFamount(),2));
					double amt = lendentrustlog.getFamount();//这个钱用来还款
					int lendentrustlogId = borrow.getFlendentrustlogId();
					returnBackMoney(lendentrustlogId, amt,virtualwallet1);
				}
			}
		} catch (Exception e) {
			throw new RuntimeException();
		}
	}
	
	public List<Map> getWaitReturnHistoryList(int firstResult, int maxResults, String filter,boolean isFY,boolean isAll) {
		return this.lendentrustDAO.getWaitReturnHistoryList(firstResult, maxResults, filter, isFY,isAll);
	}
	
	public List<Map> getWaitBorrowHistoryList(int firstResult, int maxResults,String filter,boolean isFY) {
		return this.lendentrustDAO.getWaitBorrowHistoryList(firstResult, maxResults, filter, isFY);
	}
	
	public Map<String,Double> getBorrowFees(int userId,int cnyOrCoin) {
		return this.lendentrustDAO.getBorrowFees(userId, cnyOrCoin);
	}
	
	/*
	 * 强制还款，ID指还款记录的ID
	 * */
	public void returnBackMoney(int id,double money,Fwallet fwallet) throws RuntimeException{
		Flendentrustlog flendentrustlog = this.lendentrustlogDAO.findById(id);
		if(flendentrustlog == null){
			throw new RuntimeException();
		}
		if(flendentrustlog.getFamount()-flendentrustlog.getFreturnAmount() <= 0){
			throw new RuntimeException();
		}
		if(flendentrustlog.getFstatus() == LendEntrustLogStatusEnum.ALL_RETURN){
			throw new RuntimeException();
		}
		int borrowId = flendentrustlog.getfLendEntrustBorrowId().getFid();
		Flendentrust borrowEntrust = this.lendentrustDAO.findById(borrowId);
		if(borrowEntrust.getFstatus2() != LendEntrustStatus2Enum.NOT_REPAY
				&& borrowEntrust.getFstatus2() != LendEntrustStatus2Enum.PART_REPAY){
			throw new RuntimeException();
		}
		//总欠款
		double totalWaitReturnAmt = this.lendentrustlogDAO.getTotalBorrowAmtByParent(borrowId);
		double totalFees = flendentrustlog.getFtotalfees();
		double borrowAmt = flendentrustlog.getFamount()-flendentrustlog.getFreturnAmount();
		double totalBorrow = Utils.getDouble(totalFees+borrowAmt,2);
		if(money - totalBorrow >0.01){
			throw new RuntimeException();
		}
		double ben = 0d;
		double fee = 0d;
		if(money <= totalFees){
			fee = money;
			flendentrustlog.setFtotalfees(Utils.getDouble(flendentrustlog.getFtotalfees()-money,2));
			flendentrustlog.setFstatus(LendEntrustLogStatusEnum.PART_RETURN);
		}else{
			fee = totalFees;
			ben = money-totalFees;
			flendentrustlog.setFtotalfees(0d);
			flendentrustlog.setFreturnAmount(Utils.getDouble(flendentrustlog.getFreturnAmount()+money-totalFees,2));
			if(flendentrustlog.getFamount()-flendentrustlog.getFreturnAmount() <= 0){//全部还完
				flendentrustlog.setFstatus(LendEntrustLogStatusEnum.ALL_RETURN);
				flendentrustlog.setFtodayfees(0d);
				if(totalWaitReturnAmt <= money){
					borrowEntrust.setFstatus2(LendEntrustStatus2Enum.ALL_REPAY);
				}else{
					borrowEntrust.setFstatus2(LendEntrustStatus2Enum.PART_REPAY);
				}
			}else{
				flendentrustlog.setFstatus(LendEntrustLogStatusEnum.PART_RETURN);
				borrowEntrust.setFstatus2(LendEntrustStatus2Enum.PART_REPAY);
			}
		}
		fwallet.setFborrowCny(Utils.getDouble(fwallet.getFborrowCny()-ben,2));
		Flendentrustfinishlog lendentrustfinishlog = new Flendentrustfinishlog();
		lendentrustfinishlog.setFamount(ben);
		lendentrustfinishlog.setFcreateTime(Utils.getTimestamp());
		lendentrustfinishlog.setFfees(fee);
		lendentrustfinishlog.setFlendentrustlog(flendentrustlog);
		lendentrustfinishlog.setFissend(false);
		lendentrustfinishlog.setFischarge(false);
		lendentrustfinishlog.setFreturnType(ReturnTypeEnum.SYSTEM_VALUE);
		try {
			this.lendentrustfinishlogDAO.save(lendentrustfinishlog);
			this.lendentrustlogDAO.attachDirty(flendentrustlog);
			this.walletDAO.attachDirty(fwallet);
			this.lendentrustDAO.attachDirty(borrowEntrust);
		} catch (Exception e) {
			throw new RuntimeException();
		}
	}
	
	/*
	 * 强制还款，ID指还款记录的ID
	 * */
	public void returnBackMoney(int id,double money,Fvirtualwallet virtualwallet) throws RuntimeException{
		Flendentrustlog flendentrustlog = this.lendentrustlogDAO.findById(id);
		if(flendentrustlog == null){
			throw new RuntimeException();
		}
		if(flendentrustlog.getFamount()-flendentrustlog.getFreturnAmount() <= 0){
			throw new RuntimeException();
		}
		if(flendentrustlog.getFstatus() == LendEntrustLogStatusEnum.ALL_RETURN){
			throw new RuntimeException();
		}
		int borrowId = flendentrustlog.getfLendEntrustBorrowId().getFid();
		Flendentrust borrowEntrust = this.lendentrustDAO.findById(borrowId);
		if(borrowEntrust.getFstatus2() != LendEntrustStatus2Enum.NOT_REPAY
				&& borrowEntrust.getFstatus2() != LendEntrustStatus2Enum.PART_REPAY){
			throw new RuntimeException();
		}
		//总欠款
		double totalWaitReturnAmt = this.lendentrustlogDAO.getTotalBorrowAmtByParent(borrowId);
		double totalFees = flendentrustlog.getFtotalfees();
		double borrowAmt = flendentrustlog.getFamount()-flendentrustlog.getFreturnAmount();
		double totalBorrow = Utils.getDouble(totalFees+borrowAmt, 2);
		if(money - totalBorrow > 0.01d){
			throw new RuntimeException();
		}
		double ben = 0d;
		double fee = 0d;
		if(money <= totalFees){
			fee = money;
			flendentrustlog.setFtotalfees(Utils.getDouble(flendentrustlog.getFtotalfees()-money, 2));
			flendentrustlog.setFstatus(LendEntrustLogStatusEnum.PART_RETURN);
		}else{
			fee = totalFees;
			ben = Utils.getDouble(money-totalFees,2);
			flendentrustlog.setFtotalfees(0d);
			flendentrustlog.setFreturnAmount(Utils.getDouble(flendentrustlog.getFreturnAmount()+money-totalFees,2));
			if(flendentrustlog.getFamount()-flendentrustlog.getFreturnAmount() <= 0){//全部还完
				flendentrustlog.setFtodayfees(0d);
				flendentrustlog.setFstatus(LendEntrustLogStatusEnum.ALL_RETURN);
				if(totalWaitReturnAmt <= money){
					borrowEntrust.setFstatus2(LendEntrustStatus2Enum.ALL_REPAY);
				}else{
					borrowEntrust.setFstatus2(LendEntrustStatus2Enum.PART_REPAY);
				}
			}else{
				flendentrustlog.setFstatus(LendEntrustLogStatusEnum.PART_RETURN);
				borrowEntrust.setFstatus2(LendEntrustStatus2Enum.PART_REPAY);
			}
		}
		virtualwallet.setFborrowBtc(Utils.getDouble(virtualwallet.getFborrowBtc()-ben,2));
		Flendentrustfinishlog lendentrustfinishlog = new Flendentrustfinishlog();
		lendentrustfinishlog.setFamount(ben);
		lendentrustfinishlog.setFcreateTime(Utils.getTimestamp());
		lendentrustfinishlog.setFfees(fee);
		lendentrustfinishlog.setFlendentrustlog(flendentrustlog);
		lendentrustfinishlog.setFissend(false);
		lendentrustfinishlog.setFischarge(false);
		lendentrustfinishlog.setFreturnType(ReturnTypeEnum.SYSTEM_VALUE);
		try {
			this.lendentrustfinishlogDAO.save(lendentrustfinishlog);
			this.lendentrustlogDAO.attachDirty(flendentrustlog);
			this.virtualwalletDAO.attachDirty(virtualwallet);
			this.lendentrustDAO.attachDirty(borrowEntrust);
		} catch (Exception e) {
			throw new RuntimeException();
		}
	}
	
//	
//	public List<Map> getWaitReturnHistoryDetail(int id) {
//		return this.lendentrustDAO.getWaitReturnHistoryDetail(id);
//	}
	
/*	public Map getNetInfo(Fuser fuser,int cnyOrCoin) {
		Map map = new HashMap();
		//正常+冻结+放款-已借-手续费
		Fwallet fwallet = this.walletDAO.findById(fuser.getFwallet().getFid());
		double borrowCny = fwallet.getFborrowCny();
		double applyCNY = this.lendentrustDAO.getBorrowAmt(fuser.getFid(),CnyOrCoinEnum.CNY);
		Map<String,Double> mapCNY = this.lendentrustDAO.getBorrowFees(fuser.getFid(),CnyOrCoinEnum.CNY);
		double todayFeesCNY = mapCNY.get("todayFees");
		double totalFeesCNY = mapCNY.get("totalFees");
		double cny = fwallet.getFtotalRmb()+fwallet.getFfrozenRmb()-totalFeesCNY;
		
		String filter = "where fuser.fid="+fuser.getFid()+" and fvirtualcointype.fid=1";
		Fvirtualwallet virtualwallet = this.virtualwalletDAO.list(0, 0, filter, false).get(0);
		double borrowBtc = virtualwallet.getFborrowBtc();
		double applyCOIN = this.lendentrustDAO.getBorrowAmt(fuser.getFid(),CnyOrCoinEnum.COIN);
		Map<String,Double> mapCOIN = this.lendentrustDAO.getBorrowFees(fuser.getFid(),CnyOrCoinEnum.COIN);
		double todayFeesCOIN = mapCOIN.get("todayFees");
		double totalFeesCOIN = mapCOIN.get("totalFees");
		double coin = virtualwallet.getFfrozen()+virtualwallet.getFtotal()-totalFeesCOIN;
		double price = this.realTimeData.getHighestBuyPrize(1);
		//净资产
		double amtCNY = Utils.getDouble(cny+price*coin, 2);
		double amtCOIN = Utils.getDouble(coin+cny/price, 2);
		
		double totalApply = Utils.getDouble(applyCNY+applyCOIN*price, 2);//总共申请
		double totalBorrow = Utils.getDouble(borrowCny+borrowBtc*price, 2);//总共已借

		map.put("totalBorrow", totalBorrow);
		map.put("totalApply", totalApply);
		map.put("amtCOIN", amtCOIN);
		map.put("amtCNY", amtCNY);//净资产
		map.put("todayFeesCNY", todayFeesCNY);//手续费
		map.put("totalFeesCNY", totalFeesCNY);
		map.put("todayFeesCOIN", todayFeesCOIN);
		map.put("totalFeesCOIN", totalFeesCOIN);
		map.put("applyCOIN", applyCOIN);//已申请金额
		map.put("applyCNY", applyCNY);
		map.put("fwallet", fwallet);
		map.put("fvirtualwallet", virtualwallet);
		return map;
	}*/
	
	public List<Map> getUserBorrows(int firstResult, int maxResults,String filter,boolean isFY) {
		return this.lendentrustDAO.getUserBorrows(firstResult, maxResults, filter, isFY);
	}
	
	public List<Map> getUserLends(int firstResult, int maxResults,String filter,boolean isFY) {
		return this.lendentrustDAO.getUserLends(firstResult, maxResults, filter, isFY);
	}
	
	/**
	 * 包括懒加载属性
	 * @param fid
	 * @return
	 */
	public Flendentrust findAllById(int fid){
		Flendentrust flendentrust = this.findAllById(fid);
		if(null != flendentrust){
			flendentrust.getFuser().getFid();
			flendentrust.getFvirtualcointype().getfShortName();
		}
		
		return flendentrust;
	}
	
	/**
	 * 获取用户未还清的借款记录
	 * @param fuserId
	 * @param pageNow
	 * @param pageSize
	 * @return
	 */
	public List<Flendentrust> findUnrepaymentByUser(int fuserId, Integer coinTypeId, int pageNow, int pageSize){
		if(pageNow < 1){
			pageNow = 1;
		}
		if(pageSize < 0 || pageSize > 50){
			pageSize = 20;
		}
		
		List<Flendentrust> list = lendentrustDAO.findUnrepaymentByUser(fuserId, coinTypeId, (pageNow - 1) * pageSize, pageSize);
		for(Flendentrust flendentrust : list){
			flendentrust.getFvirtualcointype().getFid();
		}
		
		return list;
	}
	
	public int countUnrepaymentByUser(int fuserId, Integer coinTypeId){
		return lendentrustDAO.countUnrepaymentByUser(fuserId, coinTypeId);
	}
	
	/**
	 * 借贷深度
	 * @param type 1 放款 / 2 借款
	 * @param fuserId
	 * @param cnyOrCoin 现金还是虚拟币
	 * @param coinTypeId
	 * @return
	 */
	public List<Map<String, Object>> lendBorrowDepth(int type, Integer fuserId, Short cnyOrCoin, Integer coinTypeId){
		return lendentrustDAO.lendBorrowDepth(type, fuserId, cnyOrCoin, coinTypeId);
	}
}








