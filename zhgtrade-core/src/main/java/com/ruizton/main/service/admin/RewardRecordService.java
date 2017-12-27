package com.ruizton.main.service.admin;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ruizton.main.Enum.ActivityCompleteStatusEnum;
import com.ruizton.main.Enum.MessageStatusEnum;
import com.ruizton.main.dao.FactivityDAO;
import com.ruizton.main.dao.FactivitycompletelogDAO;
import com.ruizton.main.dao.FactivityrewardDAO;
import com.ruizton.main.dao.FmessageDAO;
import com.ruizton.main.dao.FrewardrecordDAO;
import com.ruizton.main.dao.FuserDAO;
import com.ruizton.main.dao.FvirtualcointypeDAO;
import com.ruizton.main.dao.FvirtualwalletDAO;
import com.ruizton.main.dao.FwalletDAO;
import com.ruizton.main.model.Factivity;
import com.ruizton.main.model.Factivitycompletelog;
import com.ruizton.main.model.Factivityreward;
import com.ruizton.main.model.Fadmin;
import com.ruizton.main.model.Fmessage;
import com.ruizton.main.model.Fpromotionrewardrecord;
import com.ruizton.main.model.Frewardrecord;
import com.ruizton.main.model.Fuser;
import com.ruizton.main.model.Fvirtualcointype;
import com.ruizton.main.model.Fvirtualwallet;
import com.ruizton.main.model.Fwallet;
import com.ruizton.util.Utils;

@Service
public class RewardRecordService {
	@Autowired
	private FrewardrecordDAO rewardrecordDAO;
	@Autowired
	private FmessageDAO messageDAO;
	@Autowired(required = false)
	private HttpServletRequest request;
	@Autowired
	private FactivityDAO factivityDAO ;
	@Autowired
	private FactivityrewardDAO factivityrewardDAO ;
	@Autowired
	private FvirtualcointypeDAO fvirtualcointypeDAO ;
	@Autowired
	private FactivitycompletelogDAO factivitycompletelogDAO ;
	@Autowired
	private FuserDAO fuserDAO ;
	@Autowired
	private FvirtualwalletDAO fvirtualwalletDAO ;
	@Autowired
	private FwalletDAO fwalletDAO ;

	public Frewardrecord findById(int id) {
		Frewardrecord activityreward = this.rewardrecordDAO.findById(id);
		if (activityreward.getFactivity() != null) {
			activityreward.getFactivity().getFtitle();
		}
		if (activityreward.getFuser() != null) {
			activityreward.getFuser().getFnickName();
		}
		return activityreward;
	}

	public void saveObj(Frewardrecord obj) {
		this.rewardrecordDAO.save(obj);
	}

	public void deleteObj(int id) {
		Frewardrecord obj = this.rewardrecordDAO.findById(id);
		this.rewardrecordDAO.delete(obj);
	}

	public void updateObj(Frewardrecord obj) {
		this.rewardrecordDAO.attachDirty(obj);
	}

	public List<Frewardrecord> findByProperty(String name, Object value) {
		return this.rewardrecordDAO.findByProperty(name, value);
	}

	public List<Frewardrecord> findAll() {
		return this.rewardrecordDAO.findAll();
	}

	public List<Frewardrecord> list(int firstResult, int maxResults,
			String filter, boolean isFY) {
		List<Frewardrecord> all = this.rewardrecordDAO.list(firstResult,
				maxResults, filter, isFY);
		for (Frewardrecord frewardrecord : all) {
			if (frewardrecord.getFactivity() != null) {
				frewardrecord.getFactivity().getFtitle();
			}
			if (frewardrecord.getFuser() != null) {
				frewardrecord.getFuser().getFnickName();
			}
		}
		return all;
	}
	
	public void updateRecord(Frewardrecord rewardrecord) throws RuntimeException{
		try {

			//更新
			this.rewardrecordDAO.attachDirty(rewardrecord);
			Factivity factivity = this.factivityDAO.findById(rewardrecord.getFactivity().getFid()) ;
			Fuser fuser = this.fuserDAO.findById(rewardrecord.getFuser().getFid()) ;
			//更新钱包，此操作待实现
			String filter = " where factivity.fid ="+factivity.getFid()+" and fuser.fid="+fuser.getFid()+" order by fid desc" ;
			List<Factivitycompletelog> factivitycompletelogs = this.factivitycompletelogDAO.findByParam(0, 1, filter, true, "Factivitycompletelog") ;
			List<Factivityreward> factivityrewards = this.factivityrewardDAO.findByProperty("factivity.fid", factivity.getFid()) ;
			Factivitycompletelog factivitycompletelog = factivitycompletelogs.get(0) ;
			for (Factivityreward factivityreward : factivityrewards) {
				if(factivityreward.getFvirtualCoinOrCny()){
					//虚拟币
					Fvirtualcointype fvirtualcointype = this.fvirtualcointypeDAO.findById(factivityreward.getFvirtualcointype().getFid()) ;
					
					double reward = 0 ;
					if(factivityreward.getFrateOrReal()){
						//按比率
						reward = factivitycompletelog.getFamount()*factivityreward.getFamount() ;
					}else{
						//实数
						reward = factivityreward.getFamount() ;
					}
					
					Fvirtualwallet fvirtualwallet = this.fvirtualwalletDAO.findVirtualWallet(fuser.getFid(), fvirtualcointype.getFid()) ;
					fvirtualwallet.setFtotal(fvirtualwallet.getFtotal()+reward) ;
					this.fvirtualwalletDAO.attachDirty(fvirtualwallet) ;
				}else{
					//CNY
					double reward = 0 ;
					if(factivityreward.getFrateOrReal()){
						//按比率
						reward = factivitycompletelog.getFamount()*factivityreward.getFamount() ;
					}else{
						//实数
						reward = factivityreward.getFamount() ;
					}
					
					Fwallet fwallet = this.fwalletDAO.findById(fuser.getFwallet().getFid()) ;
					fwallet.setFtotalRmb(fwallet.getFtotalRmb()+reward) ;
					this.fwalletDAO.attachDirty(fwallet) ;
				}
			}
			
			
			//发消息
			Fmessage msg = new Fmessage();
			msg.setFcreateTime(Utils.getTimestamp());
			msg.setFcontent(rewardrecord.getFrewardReason());
			msg.setFreceiver(rewardrecord.getFuser());
			if(request.getSession().getAttribute("login_admin") != null){
				Fadmin admin = (Fadmin)request.getSession().getAttribute("login_admin");
				msg.setFcreator(admin);
			}
			msg.setFtitle(factivity.getFtitle()+"奖励,请注意查收");
			msg.setFstatus(MessageStatusEnum.NOREAD_VALUE);
			this.messageDAO.save(msg);
		
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace() ;
			throw new RuntimeException(); 
		}
		
	}
}