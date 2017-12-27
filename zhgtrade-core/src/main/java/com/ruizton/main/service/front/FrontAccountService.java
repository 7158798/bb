package com.ruizton.main.service.front;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ruizton.main.Enum.CapitalOperationOutStatus;
import com.ruizton.main.Enum.CapitalOperationTypeEnum;
import com.ruizton.main.Enum.RemittanceTypeEnum;
import com.ruizton.main.Enum.VirtualCapitalOperationOutStatusEnum;
import com.ruizton.main.dao.FcapitaloperationDAO;
import com.ruizton.main.dao.FscoreDAO;
import com.ruizton.main.dao.FsystemargsDAO;
import com.ruizton.main.dao.FuserDAO;
import com.ruizton.main.dao.FvirtualcaptualoperationDAO;
import com.ruizton.main.dao.FvirtualwalletDAO;
import com.ruizton.main.dao.FwalletDAO;
import com.ruizton.main.dao.FwithdrawfeesDAO;
import com.ruizton.main.model.FbankinfoWithdraw;
import com.ruizton.main.model.Fcapitaloperation;
import com.ruizton.main.model.Fscore;
import com.ruizton.main.model.Fsystemargs;
import com.ruizton.main.model.Fuser;
import com.ruizton.main.model.Fvirtualcaptualoperation;
import com.ruizton.main.model.Fvirtualwallet;
import com.ruizton.main.model.Fwallet;
import com.ruizton.main.model.Fwithdrawfees;
import com.ruizton.util.Utils;

@Service
public class FrontAccountService {
	private static final Logger log = LoggerFactory.getLogger(FrontAccountService.class);
	@Autowired
	private FcapitaloperationDAO fcapitaloperationDAO ;
	@Autowired
	private FwalletDAO fwalletDAO ;
	@Autowired
	private FwithdrawfeesDAO fwithdrawfeesDAO ;
	@Autowired
	private FscoreDAO fscoreDAO ;
	@Autowired
	private FuserDAO fuserDAO ;
	@Autowired
	private FvirtualwalletDAO fvirtualwalletDAO ;
	@Autowired
	private FvirtualcaptualoperationDAO fvirtualcaptualoperationDAO ;
	@Autowired
	private FsystemargsDAO fsystemargsDAO ;
	
	public void addFcapitaloperation(Fcapitaloperation fcapitaloperation){
		this.fcapitaloperationDAO.save(fcapitaloperation) ;
	}
	
	public List<Fcapitaloperation> findCapitalList(int cur_page,int max_page,Map<String, Object> param,String order){
		return this.fcapitaloperationDAO.findByParam(cur_page, max_page, param,order) ;
	}
	
	public int findCapitalCount(Map<String, Object> param){
		return this.fcapitaloperationDAO.findByParamCount(param) ;
	}
	
	public Fcapitaloperation findCapitalOperationById(int id){
		Fcapitaloperation fcapitaloperation = this.fcapitaloperationDAO.findById(id) ;
		return fcapitaloperation ;
	}
	
	public void updateCapitalOperation(Fcapitaloperation fcapitaloperation){
		this.fcapitaloperationDAO.attachDirty(fcapitaloperation) ;
	}
	
	public Fwallet findWalletByFuser(Fuser fuser){
		List<Fwallet> list = this.fwalletDAO.findByProperty("fuser.fid", fuser.getFid()) ;
		if(list.size()<=0){
			log.error("Fuser:"+fuser.getFid()+" has no Fwallet.") ;
		}else if(list.size()>1){
			log.error("Fuser:"+fuser.getFid()+" has more than one Fwallet.") ;
		}
		return list.get(0) ;
	}
	
	public boolean updateWithdrawCNY(double withdrawBanlance,Fuser fuser,FbankinfoWithdraw fbankinfoWithdraw) throws Exception{
		boolean flag = false ;
		try {
			Fwallet fwallet = fuser.getFwallet() ;
			if(fwallet.getFtotalRmb()<withdrawBanlance){
				return false ;
			}
			
			
			List<Fwithdrawfees> fwithdrawfees = this.fwithdrawfeesDAO.findByProperty("flevel", fuser.getFscore().getFlevel()) ;
			double feesRate = fwithdrawfees.get(0).getFfee();
			double feesAmount = fwithdrawfees.get(0).getFamount();
			
			Fcapitaloperation fcapitaloperation = new Fcapitaloperation() ;
			fcapitaloperation.setfBank(fbankinfoWithdraw.getFname()) ;
			fcapitaloperation.setFProvince(fbankinfoWithdraw.getFprovince());
			fcapitaloperation.setFCity(fbankinfoWithdraw.getFcity());
			fcapitaloperation.setFBranch(fbankinfoWithdraw.getFbranch());
			fcapitaloperation.setfAccount(fbankinfoWithdraw.getFbankNumber()) ;
			double amt = Utils.getDouble(withdrawBanlance*(1.0F-feesRate),2);
			double fees = Utils.getDouble(withdrawBanlance*feesRate,2);
			fcapitaloperation.setFamount(amt-feesAmount) ;
			fcapitaloperation.setFfees(fees+feesAmount) ;
			fcapitaloperation.setFcreateTime(Utils.getTimestamp()) ;
			fcapitaloperation.setFtype(CapitalOperationTypeEnum.RMB_OUT) ;
			fcapitaloperation.setFuser(fuser) ;
			fcapitaloperation.setfLastUpdateTime(Utils.getTimestamp()) ;
			fcapitaloperation.setfPhone(fuser.getFtelephone()) ;
			fcapitaloperation.setfPayee(fuser.getFrealName()) ;
			fcapitaloperation.setFremittanceType(RemittanceTypeEnum.Type3) ;
			fcapitaloperation.setFstatus(CapitalOperationOutStatus.WaitForOperation) ;
			this.fcapitaloperationDAO.save(fcapitaloperation) ;
				
			fwallet.setFtotalRmb(fwallet.getFtotalRmb()-amt-fees) ;
			fwallet.setFfrozenRmb(fwallet.getFfrozenRmb()+amt+fees) ;
			this.fwalletDAO.attachDirty(fwallet) ;
		} catch (Exception e) {
			throw new RuntimeException();
		}
		flag = true ;
		return flag ;
	}
	
	public double findWithdrawFeesByLevel(int level){
		List<Fwithdrawfees> fwithdrawfees = this.fwithdrawfeesDAO.findByProperty("flevel", level) ;
		return fwithdrawfees.get(0).getFfee() ;
	}
	public Fscore findFscoreById(int id){
		return this.fscoreDAO.findById(id) ;
	}
	
	public void updateCancelWithdrawCny(Fcapitaloperation fcapitaloperation,Fuser fuser){
		fcapitaloperation.setFstatus(CapitalOperationOutStatus.Cancel) ;
		fcapitaloperation.setfLastUpdateTime(Utils.getTimestamp()) ;
		Fwallet fwallet = fuser.getFwallet() ;
		fwallet.setFfrozenRmb(fwallet.getFfrozenRmb()-fcapitaloperation.getFfees()-fcapitaloperation.getFamount()) ;
		fwallet.setFtotalRmb(fwallet.getFtotalRmb()+fcapitaloperation.getFfees()+fcapitaloperation.getFamount()) ;
		
		this.fwalletDAO.attachDirty(fwallet) ;
		this.fcapitaloperationDAO.attachDirty(fcapitaloperation) ;
	}
	
	public void updateCancelWithdrawBtc(Fvirtualcaptualoperation fvirtualcaptualoperation,Fuser fuser){
		fvirtualcaptualoperation.setFstatus(VirtualCapitalOperationOutStatusEnum.Cancel) ;
		fvirtualcaptualoperation.setFlastUpdateTime(Utils.getTimestamp()) ;
		
		double amount = fvirtualcaptualoperation.getFamount()+fvirtualcaptualoperation.getFfees() ;
		Fvirtualwallet fvirtualwallet = this.fvirtualwalletDAO.findVirtualWallet(fuser.getFid(), fvirtualcaptualoperation.getFvirtualcointype().getFid()) ;
		fvirtualwallet.setFtotal(fvirtualwallet.getFtotal()+amount) ;
		fvirtualwallet.setFfrozen(fvirtualwallet.getFfrozen()-amount) ;
		fvirtualwallet.setFlastUpdateTime(Utils.getTimestamp()) ;
		
		this.fvirtualwalletDAO.attachDirty(fvirtualwallet) ;
		this.fvirtualcaptualoperationDAO.attachDirty(fvirtualcaptualoperation) ;
	}
	
	public int getTodayCnyWithdrawTimes(Fuser fuser){
		return this.fcapitaloperationDAO.getTodayCnyWithdrawTimes(fuser) ;
	}
	public int getTodayVirtualCoinWithdrawTimes(Fuser fuser){
		return this.fvirtualcaptualoperationDAO.getTodayVirtualCoinWithdrawTimes(fuser) ;
	}

	/**
	 * 过滤三天充值未完成
	 * @param cur_page
	 * @param max_page
	 * @param param
	 * @param order
     * @return
     */
	public List<Fcapitaloperation> listCapitalList(int cur_page,int max_page,Map<String, Object> param,String order){
		return this.fcapitaloperationDAO.listByParam(cur_page, max_page, param,order) ;
	}

	/**
	 * 过滤三天充值未完成
	 * @param param
	 * @return
     */
	public int countCapitalCount(Map<String, Object> param){
		return this.fcapitaloperationDAO.countByParamCount(param) ;
	}
		
}
