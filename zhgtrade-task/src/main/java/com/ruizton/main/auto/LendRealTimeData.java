package com.ruizton.main.auto;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import org.springframework.beans.factory.annotation.Autowired;

import com.ruizton.main.Enum.CnyOrCoinEnum;
import com.ruizton.main.Enum.LendEntrustStatus1Enum;
import com.ruizton.main.Enum.LendEntrustTypeEnum;
import com.ruizton.main.model.Flendentrust;
import com.ruizton.main.model.Flendsystemargs;
import com.ruizton.main.model.Fvalidatemessage;
import com.ruizton.main.service.admin.LendEntrustService;
import com.ruizton.main.service.admin.LendSystemArgsService;
import com.ruizton.util.FlendSystemargsIds;

//实时数据
public class LendRealTimeData {
	
	@Autowired
	private LendEntrustService lendEntrustService;
	@Autowired
	private LendSystemArgsService lendSystemArgsService;
	
	
	private static boolean m_is_init = true ;
	//借贷深度
	private List<Map> lendList_CNY = new ArrayList<Map>();
	private List<Map> lendList_COIN = new ArrayList<Map>();
	private List<Map> borrowList_CNY = new ArrayList<Map>();
	private List<Map> borrowList_COIN = new ArrayList<Map>();
	
	//借贷集合
	private Map<Integer, TreeSet<Flendentrust>> borrowMap = new HashMap<Integer, TreeSet<Flendentrust>>();
	private Map<Integer, TreeSet<Flendentrust>> lendMap = new HashMap<Integer, TreeSet<Flendentrust>>();
	
	//短信集合
	private LinkedList<Fvalidatemessage> fvalidatemessages = new LinkedList<Fvalidatemessage>();
	
	
	public LinkedList<Fvalidatemessage> getFvalidatemessages() {
		return fvalidatemessages;
	}

	public void addValidatemessage(Fvalidatemessage fvalidatemessage){
		synchronized (this.fvalidatemessages) {
			 this.getFvalidatemessages().addLast(fvalidatemessage);
		}
	}
	
	public Fvalidatemessage getOneMessage() {
		synchronized (fvalidatemessages) {
			Fvalidatemessage fvalidatemessage = null ;
			if(fvalidatemessages.size()>0){
				fvalidatemessage = fvalidatemessages.pop() ;
			}
			return fvalidatemessage ;
		}
	}
	
	
	Comparator<Flendentrust> dailyRateDESC = new Comparator<Flendentrust>() {
		public int compare(Flendentrust o1, Flendentrust o2) {
			boolean flag = o1.getFid()==o2.getFid() && o1.getFid()!=0 ;
			if(flag){
				return 0 ;
			}
			int ret = Double.valueOf(o2.getFdailyRate()).compareTo(Double.valueOf(o1.getFdailyRate())) ;
			if(ret==0){
				return Double.valueOf(o1.getFid()).compareTo(Double.valueOf(o2.getFid())) ;
			}else{
				return ret ;
			}
		}
	} ;
	
	Comparator<Flendentrust> dailyRateASC = new Comparator<Flendentrust>() {
		public int compare(Flendentrust o1, Flendentrust o2) {
			boolean flag = o1.getFid()==o2.getFid() && o1.getFid()!=0 ;
			if(flag){
				return 0 ;
			}
			int ret = Double.valueOf(o1.getFdailyRate()).compareTo(Double.valueOf(o2.getFdailyRate())) ;
			if(ret==0){
				return Double.valueOf(o1.getFid()).compareTo(Double.valueOf(o2.getFid())) ;
			}else{
				return ret ;
			}
		}
	} ;
	
	public TreeSet<Flendentrust> getBorrowMap(int symbol) {
		TreeSet<Flendentrust> flendentrusts = borrowMap.get(symbol);
		if(flendentrusts==null){
			flendentrusts = new TreeSet<Flendentrust>() ;
		}
		return flendentrusts ;
	}

	public void addBorrowMap(int symbol,Flendentrust flendentrust) {
		synchronized (borrowMap) {
			TreeSet<Flendentrust> treeSet = borrowMap.get(symbol) ;
			if(treeSet==null){
				treeSet = new TreeSet<Flendentrust>(dailyRateDESC) ;
			}
			if(treeSet.contains(flendentrust)){
				treeSet.remove(flendentrust) ;
			}
			treeSet.add(flendentrust) ;
			borrowMap.put(symbol, treeSet) ;
			if(!m_is_init){
				updateData(symbol,flendentrust.getFtype());
			}
		}
	}

	public  void removeBorrowMap(int symbol,Flendentrust flendentrust){
		synchronized (borrowMap) {
			TreeSet<Flendentrust> treeSet = borrowMap.get(symbol) ;
			if(treeSet!=null){
				treeSet.remove(flendentrust) ;
				borrowMap.put(symbol, treeSet) ;
				if(!m_is_init){
					updateData(symbol,flendentrust.getFtype());
				}
			}
		}
	}
	
	public Integer[] getBorrowMapKeys(){
		Object[] objs = borrowMap.keySet().toArray() ;
		Integer[] ints = new Integer[objs.length] ;
		for (int i = 0; i < objs.length; i++) {
			ints[i]=(Integer) objs[i] ;
		}
		return ints ;
	}
	
	public TreeSet<Flendentrust> getLendMap(int symbol) {
		TreeSet<Flendentrust> flendentrusts = lendMap.get(symbol);
		if(flendentrusts==null){
			flendentrusts = new TreeSet<Flendentrust>() ;
		}
		return flendentrusts ;
	}

	public void addLendMap(int symbol,Flendentrust flendentrust) {
		synchronized (lendMap) {
			TreeSet<Flendentrust> treeSet = lendMap.get(symbol) ;
			if(treeSet==null){
				treeSet = new TreeSet<Flendentrust>(dailyRateASC) ;
			}
			if(treeSet.contains(flendentrust)){
				treeSet.remove(flendentrust) ;
			}
			treeSet.add(flendentrust) ;
			lendMap.put(symbol, treeSet) ;
			if(!m_is_init){
				updateData(symbol,flendentrust.getFtype());
			}
		}
	}

	public  void removeLendMap(int symbol,Flendentrust flendentrust){
		synchronized (lendMap) {
			TreeSet<Flendentrust> treeSet = lendMap.get(symbol) ;
			if(treeSet!=null){
				treeSet.remove(flendentrust) ;
				lendMap.put(symbol, treeSet) ;
				if(!m_is_init){
					updateData(symbol,flendentrust.getFtype());
				}
			}
		}
	}
	
	public Integer[] getLendMapKeys(){
		Object[] objs = lendMap.keySet().toArray() ;
		Integer[] ints = new Integer[objs.length] ;
		for (int i = 0; i < objs.length; i++) {
			ints[i]=(Integer) objs[i] ;
		}
		return ints ;
	}
	
	public List<Map> getLendList_CNY() {
		return lendList_CNY;
	}

	public void setLendList_CNY(List<Map> list) {
		synchronized (lendList_CNY) {
			this.lendList_CNY = list;
		}
	}

	public List<Map> getLendList_COIN() {
		return lendList_COIN;
	}

	public void setLendList_COIN(List<Map> list) {
		synchronized (lendList_COIN) {
			this.lendList_COIN = list;
		}
	}

	public List<Map> getBorrowList_CNY() {
		return borrowList_CNY;
	}

	public void setBorrowList_CNY(List<Map> list) {
		synchronized (borrowList_CNY) {
			this.borrowList_CNY = list;
		}
	}

	public List<Map> getBorrowList_COIN() {
		return borrowList_COIN;
	}

	public void setBorrowList_COIN(List<Map> list) {
		synchronized (borrowList_COIN) {
			this.borrowList_COIN = list;
		}
	}
	
	public void init(){
		try {
			//平仓参数
			Flendsystemargs lendsystemargs = this.lendSystemArgsService.findById(FlendSystemargsIds.isOpenSell);
			lendsystemargs.setFvalue("0");
			this.lendSystemArgsService.updateObj(lendsystemargs);
			
			this.setBorrowList_CNY(this.lendEntrustService.getHistoryList(LendEntrustTypeEnum.BORROW, CnyOrCoinEnum.CNY));
			this.setBorrowList_COIN(this.lendEntrustService.getHistoryList(LendEntrustTypeEnum.BORROW, CnyOrCoinEnum.COIN));
			this.setLendList_CNY(this.lendEntrustService.getHistoryList(LendEntrustTypeEnum.LEND, CnyOrCoinEnum.CNY));
			this.setLendList_COIN(this.lendEntrustService.getHistoryList(LendEntrustTypeEnum.LEND, CnyOrCoinEnum.COIN));
			
			StringBuffer sf = new StringBuffer();
			sf.append("where fstatus1 in("+LendEntrustStatus1Enum.NOT_DEAL+","+LendEntrustStatus1Enum.PART_DEAL+") \n");
			sf.append("and (famount-fsuccessAmount) > 10 \n");
			List<Flendentrust> flendentrusts = this.lendEntrustService.list(0, 0, sf.toString(), false);
			for (Flendentrust flendentrust : flendentrusts) {
				int symbol = flendentrust.getFvirtualcointype() == null?0:flendentrust.getFvirtualcointype().getFid();
				if(flendentrust.getFtype() == LendEntrustTypeEnum.LEND){
					this.addLendMap(symbol, flendentrust);
				}else{
					this.addBorrowMap(symbol, flendentrust);
				}
			}
			m_is_init = false;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void updateData(int symbol,int type) {
		//更新借贷深度
		if(symbol ==0){
			if(type == LendEntrustTypeEnum.BORROW){
				setBorrowList_CNY(this.lendEntrustService.getHistoryList(LendEntrustTypeEnum.BORROW, CnyOrCoinEnum.CNY));
			}else{
				setLendList_CNY(this.lendEntrustService.getHistoryList(LendEntrustTypeEnum.LEND, CnyOrCoinEnum.CNY));
			}
		}else{
			if(type == LendEntrustTypeEnum.BORROW){
				setBorrowList_COIN(this.lendEntrustService.getHistoryList(LendEntrustTypeEnum.BORROW, CnyOrCoinEnum.COIN));
			}else{
				setLendList_COIN(this.lendEntrustService.getHistoryList(LendEntrustTypeEnum.LEND, CnyOrCoinEnum.COIN));
			}
		}
	}
	
}