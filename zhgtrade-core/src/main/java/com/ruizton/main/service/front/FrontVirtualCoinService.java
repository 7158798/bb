package com.ruizton.main.service.front;

import java.util.List;

import com.ruizton.main.dao.*;
import com.ruizton.main.model.*;
import com.ruizton.util.StringUtils;
import org.hibernate.dialect.FirebirdDialect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ruizton.main.Enum.VirtualCapitalOperationTypeEnum;
import com.ruizton.main.Enum.VirtualCapitalOperationInStatusEnum;
import com.ruizton.main.Enum.VirtualCapitalOperationOutStatusEnum;
import com.ruizton.main.Enum.VirtualCoinTypeStatusEnum;
import com.ruizton.util.Utils;

@Service
public class FrontVirtualCoinService {
	@Autowired
	private FvirtualcointypeDAO fvirtualcointypeDAO ;
	@Autowired
	private FfeesDAO ffeesDAO ;
	@Autowired
	private FvirtualaddressDAO fvirtualaddressDAO ;
	@Autowired
	private FvirtualaddressWithdrawDAO fvirtualaddressWithdrawDAO ;
	@Autowired
	private FvirtualcaptualoperationDAO fvirtualcaptualoperationDAO ;
	@Autowired
	private FvirtualwalletDAO fvirtualwalletDAO ;
	@Autowired
	private FsystemargsDAO systemargsDAO;
	@Autowired
	private FpoolDAO fpoolDAO;

	public List<Fvirtualcointype> findFvirtualCoinType(int status){
		List<Fvirtualcointype> list = this.fvirtualcointypeDAO.findByParam(0, 0, " where fstatus="+status+" order by fid asc ", false, "Fvirtualcointype") ;
		return list ;
	}

	public List<Fvirtualcointype> list(){
		return this.fvirtualcointypeDAO.list(0, 0, " where 1=1", false);
	}
	
	public Fvirtualcointype findFvirtualCoinById(int id){
		Fvirtualcointype fvirtualcointype = this.fvirtualcointypeDAO.findById(id) ;
		return fvirtualcointype ;
	}
	
	public Fvirtualcointype findFirstFirtualCoin(){
		Fvirtualcointype fvirtualcointype = null ;
		List<Fvirtualcointype> list = this.fvirtualcointypeDAO.findByProperty("fstatus", VirtualCoinTypeStatusEnum.Normal) ;
		if(list.size()>0){
			fvirtualcointype = list.get(0) ;
		}
		return fvirtualcointype ;
	}
	
	public Fvirtualcointype findFirstFirtualCoin_Wallet(){
		Fvirtualcointype fvirtualcointype = null ;
		String filter = "where fstatus="+VirtualCoinTypeStatusEnum.Normal+" and FIsWithDraw=1";
		List<Fvirtualcointype> list = this.fvirtualcointypeDAO.list(0, 0, filter, false);
		if(list.size()>0){
			fvirtualcointype = list.get(0) ;
		}
		return fvirtualcointype ;
	}
	
	public Fvirtualaddress findFvirtualaddress(Fuser fuser,Fvirtualcointype fvirtualcointype){
		return this.fvirtualaddressDAO.findFvirtualaddress(fuser, fvirtualcointype) ;
	}

	public Fvirtualaddress updateUserAddress(Fuser fuser, Fvirtualcointype fvirtualcointype) {
		try {
//			String address = this.fpoolDAO.getFaddress(fvirtualcointype.getFid()) ;

			Fpool fpool = fpoolDAO.getOneFpool(fvirtualcointype);
			String address = fpool.getFaddress();

			Fvirtualaddress fvirtualaddress = new Fvirtualaddress();
			fvirtualaddress.setFadderess(address) ;
			fvirtualaddress.setFcreateTime(Utils.getTimestamp());
			fvirtualaddress.setFuser(fuser) ;
			fvirtualaddress.setFvirtualcointype(fvirtualcointype);
			this.fvirtualaddressDAO.save(fvirtualaddress) ;

			fpool.setFstatus(1);
			fpoolDAO.save(fpool);

			return fvirtualaddress;
		} catch (Exception e) {
			return null;
		}
	}
	
	public List<Fvirtualaddress> findFvirtualaddress(Fvirtualcointype fvirtualcointype,String address){
		return this.fvirtualaddressDAO.findFvirtualaddress(fvirtualcointype, address) ;
	}
	
	public FvirtualaddressWithdraw findFvirtualaddressWithdraw(Fuser fuser,Fvirtualcointype fvirtualcointype){
		return this.fvirtualaddressWithdrawDAO.findFvirtualaddressWithdraw(fuser, fvirtualcointype) ;
	}
	
	public List<Fvirtualcaptualoperation> findFvirtualcaptualoperation(
			Fuser fuser,int type[],int status[],Fvirtualcointype[] fvirtualcointypes,String order,
			int firstResult,int maxResult){
		List<Fvirtualcaptualoperation> list = this.fvirtualcaptualoperationDAO.findFvirtualcaptualoperation(fuser, type, status, fvirtualcointypes, order, firstResult, maxResult) ;
		for (Fvirtualcaptualoperation fvirtualcaptualoperation : list) {
			fvirtualcaptualoperation.getFvirtualcointype().getFname() ;
		}
		return list ;
	}

	public int countFvirtualcaptualoperation(Fuser fuser,int type[],int status[],Fvirtualcointype[] fvirtualcointypes){
		return this.fvirtualcaptualoperationDAO.countFvirtualcaptualoperation(fuser, type, status, fvirtualcointypes) ;
	}

	public int findFvirtualcaptualoperationCount(
			Fuser fuser,int type[],int status[],Fvirtualcointype[] fvirtualcointypes,String order){
		return this.fvirtualcaptualoperationDAO.findFvirtualcaptualoperationCount(fuser, type, status, fvirtualcointypes, order) ;
	}
	
	public List<Fvirtualcaptualoperation> findFvirtualcaptualoperations(int firstResult, int maxResults,String filter, boolean isFY){
		return this.fvirtualcaptualoperationDAO.findByParam(firstResult, maxResults, filter, isFY, "Fvirtualcaptualoperation") ;
	}
	public int findFvirtualcaptualoperationsCount(String filter){
		return this.fvirtualcaptualoperationDAO.findByParamCount(filter, "Fvirtualcaptualoperation") ;
	}
	
	public void updateFvirtualaddressWithdraw(FvirtualaddressWithdraw fvirtualaddressWithdraw){
		this.fvirtualaddressWithdrawDAO.attachDirty(fvirtualaddressWithdraw) ;
	}
	
	public Ffees findFfees(int virtualCoinTypeId,int level){
		return this.ffeesDAO.findFfee(virtualCoinTypeId, level) ;
	}
	
	public int updateWithdrawBtc(FvirtualaddressWithdraw fvirtualaddressWithdraw,Fvirtualcointype fvirtualcointype,Fvirtualwallet fvirtualwallet ,double withdrawAmount,Fuser fuser){
		try {
			fvirtualwallet.setFtotal(fvirtualwallet.getFtotal()-withdrawAmount) ;
			fvirtualwallet.setFfrozen(fvirtualwallet.getFfrozen()+withdrawAmount) ;
			fvirtualwallet.setFlastUpdateTime(Utils.getTimestamp()) ;
			this.fvirtualwalletDAO.attachDirty(fvirtualwallet) ;
			
			double feeRate = this.ffeesDAO.findFfee(fvirtualcointype.getFid(), fuser.getFscore().getFlevel()).getWithdraw();
			Fvirtualcaptualoperation fvirtualcaptualoperation = new Fvirtualcaptualoperation() ;
			fvirtualcaptualoperation.setFamount(withdrawAmount*(1-feeRate)) ;
			fvirtualcaptualoperation.setFcreateTime(Utils.getTimestamp()) ;
			fvirtualcaptualoperation.setFfees(withdrawAmount*feeRate) ;
			fvirtualcaptualoperation.setFlastUpdateTime(Utils.getTimestamp()) ;
			fvirtualcaptualoperation.setFstatus(VirtualCapitalOperationOutStatusEnum.WaitForOperation) ;
			fvirtualcaptualoperation.setFtype(VirtualCapitalOperationTypeEnum.COIN_OUT) ;
			fvirtualcaptualoperation.setFuser(fuser) ;
			fvirtualcaptualoperation.setFvirtualcointype(fvirtualcointype) ;
			fvirtualcaptualoperation.setWithdraw_virtual_address(fvirtualaddressWithdraw.getFadderess()) ;
			this.fvirtualcaptualoperationDAO.save(fvirtualcaptualoperation) ;
			return fvirtualcaptualoperation.getFid();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}
	
	public void addFvirtualcaptualoperation(Fvirtualcaptualoperation fvirtualcaptualoperation){
		this.fvirtualcaptualoperationDAO.save(fvirtualcaptualoperation) ;
	}
	
	public List<Fvirtualcaptualoperation> findFvirtualcaptualoperationByProperty(String key,Object value){
		return this.fvirtualcaptualoperationDAO.findByProperty(key, value) ;
	}

	public List<Fvirtualcaptualoperation> findFvirtualcaptualoperationByProperties(String[] keys,Object[] values){
		return this.fvirtualcaptualoperationDAO.findByProperties(keys, values) ;
	}

	public Fvirtualcaptualoperation findFvirtualcaptualoperationById(int id){
		return this.fvirtualcaptualoperationDAO.findById(id) ;
	}
	
	//比特币自动充值并加币
	public void updateFvirtualcaptualoperationCoinIn(Fvirtualcaptualoperation fvirtualcaptualoperation) throws Exception{
		try {
			Fvirtualcaptualoperation real = this.fvirtualcaptualoperationDAO.findById(fvirtualcaptualoperation.getFid()) ;
			if(real!=null && real.getFstatus()!=VirtualCapitalOperationInStatusEnum.SUCCESS){
				real.setFstatus(fvirtualcaptualoperation.getFstatus()) ;
				real.setFconfirmations(fvirtualcaptualoperation.getFconfirmations()) ;
				real.setFlastUpdateTime(Utils.getTimestamp()) ;
				this.fvirtualcaptualoperationDAO.attachDirty(real) ;
				
				if(real.getFstatus()==VirtualCapitalOperationInStatusEnum.SUCCESS && real.isFhasOwner()){
					Fvirtualcointype fvirtualcointype = this.fvirtualcointypeDAO.findById(real.getFvirtualcointype().getFid()) ;
					Fvirtualwallet fvirtualwallet = this.fvirtualwalletDAO.findVirtualWallet(real.getFuser().getFid(), fvirtualcointype.getFid()) ;
					List<Fsystemargs> list = this.systemargsDAO.findByFkey("rechargeCOIN");
					double totalCoin = Utils.getDouble(Double.valueOf(list.get(0).getFvalue())*real.getFamount()+real.getFamount(), 4);
					fvirtualwallet.setFtotal(fvirtualwallet.getFtotal()+ totalCoin) ;
					fvirtualwallet.setFlastUpdateTime(Utils.getTimestamp()) ;
					this.fvirtualwalletDAO.attachDirty(fvirtualwallet) ;
				}
			}
		} catch (Exception e) {
			throw e;
		}
	}
	
	public List<Fvirtualaddress> findFvirtualaddressByProperty(String key,Object value){
		List<Fvirtualaddress> fvirtualaddresses = this.fvirtualaddressDAO.findByProperty(key, value) ;
		for (Fvirtualaddress fvirtualaddress : fvirtualaddresses) {
			fvirtualaddress.getFuser().getFnickName() ;
		}
		return fvirtualaddresses ;
	}
	
	public boolean isExistsCanWithdrawCoinType(){
		List<Fvirtualcointype> fvirtualcointypes = this.fvirtualcointypeDAO.findByParam(0, 0, " where FIsWithDraw=1 and fstatus=1 ", false, "Fvirtualcointype") ;
		return fvirtualcointypes.size()>0 ;
	}
	
	public double getOpenPrice(int id){
		Fvirtualcointype obj = fvirtualcointypeDAO.findById(id);
		if(obj!=null){
			return obj.getFopenPrice();
		}else{
			return 0.01f;
		}
	}
	
	//是否限制交易
	public String getOpenTrade(int symbol){
		Fvirtualcointype fvirtualcointype = fvirtualcointypeDAO.findById(symbol);
		return fvirtualcointype.getOpenTrade();
	}


	public List<Fvirtualcointype> findByOrder(boolean homeOrder){
		return fvirtualcointypeDAO.findByOrder(homeOrder);
	}

	public Fvirtualaddress updateAssignWalletAddress(Fuser fuser, Fvirtualcointype fvirtualcointype) throws Exception{
		String address = fvirtualaddressDAO.getAssignAddress(fvirtualcointype.getFid());
		if(StringUtils.isEmpty(address)){
			return null;
		}

		Fvirtualaddress fvirtualaddress = new Fvirtualaddress() ;
		fvirtualaddress.setFadderess(address) ;
		fvirtualaddress.setFcreateTime(Utils.getTimestamp()) ;
		fvirtualaddress.setFuser(fuser) ;
		fvirtualaddress.setFvirtualcointype(fvirtualcointype) ;
		fvirtualaddressDAO.save(fvirtualaddress);

		return fvirtualaddress;
	}

	public List<Fvirtualcointype> listByTotalOrder(){
		return fvirtualcointypeDAO.listByTotalOrder();
	}
	
}
