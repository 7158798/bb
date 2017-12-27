package com.ruizton.main.service.admin;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ruizton.main.Enum.ActivityCompleteStatusEnum;
import com.ruizton.main.Enum.MessageStatusEnum;
import com.ruizton.main.dao.FactivityDAO;
import com.ruizton.main.dao.FactivitypromotioncompletelogDAO;
import com.ruizton.main.dao.FmessageDAO;
import com.ruizton.main.dao.FpromotionactivityrewardDAO;
import com.ruizton.main.dao.FpromotionrewardrecordDAO;
import com.ruizton.main.dao.FrewardrecordDAO;
import com.ruizton.main.dao.FuserDAO;
import com.ruizton.main.dao.FvirtualcointypeDAO;
import com.ruizton.main.dao.FvirtualwalletDAO;
import com.ruizton.main.dao.FwalletDAO;
import com.ruizton.main.model.Factivity;
import com.ruizton.main.model.Factivitypromotioncompletelog;
import com.ruizton.main.model.Fadmin;
import com.ruizton.main.model.Fmessage;
import com.ruizton.main.model.Fpromotionactivityreward;
import com.ruizton.main.model.Fpromotionrewardrecord;
import com.ruizton.main.model.Frewardrecord;
import com.ruizton.main.model.Fuser;
import com.ruizton.main.model.Fvirtualcointype;
import com.ruizton.main.model.Fvirtualwallet;
import com.ruizton.main.model.Fwallet;
import com.ruizton.util.Utils;

@Service
public class PromotionRewardRecordService {
	@Autowired
	private FpromotionrewardrecordDAO promotionrewardrecordDAO;
	@Autowired
	private FmessageDAO messageDAO;
	@Autowired(required = false)
	private HttpServletRequest request;
	@Autowired
	private FactivityDAO factivityDAO ;
	@Autowired
	private FuserDAO fuserDAO ;
	@Autowired
	private FactivitypromotioncompletelogDAO factivitypromotioncompletelogDAO ;
	@Autowired
	private FpromotionactivityrewardDAO fpromotionactivityrewardDAO ;
	@Autowired
	private FvirtualcointypeDAO fvirtualcointypeDAO ;
	@Autowired
	private FvirtualwalletDAO fvirtualwalletDAO ;
	@Autowired
	private FwalletDAO fwalletDAO ;

	public Fpromotionrewardrecord findById(int id) {
		Fpromotionrewardrecord activityreward = this.promotionrewardrecordDAO
				.findById(id);
		if (activityreward.getFactivity() != null) {
			activityreward.getFactivity().getFtitle();
		}
		if (activityreward.getFuser() != null) {
			activityreward.getFuser().getFnickName();
		}
		return activityreward;
	}

	public void saveObj(Fpromotionrewardrecord obj) {
		this.promotionrewardrecordDAO.save(obj);
	}

	public void deleteObj(int id) {
		Fpromotionrewardrecord obj = this.promotionrewardrecordDAO.findById(id);
		this.promotionrewardrecordDAO.delete(obj);
	}

	public void updateObj(Fpromotionrewardrecord obj) {
		this.promotionrewardrecordDAO.attachDirty(obj);
	}

	public List<Fpromotionrewardrecord> findByProperty(String name, Object value) {
		return this.promotionrewardrecordDAO.findByProperty(name, value);
	}

	public List<Fpromotionrewardrecord> findAll() {
		return this.promotionrewardrecordDAO.findAll();
	}

	public List<Fpromotionrewardrecord> list(int firstResult, int maxResults,
			String filter, boolean isFY) {
		List<Fpromotionrewardrecord> all = this.promotionrewardrecordDAO.list(
				firstResult, maxResults, filter, isFY);
		for (Fpromotionrewardrecord fpromotionrewardrecord : all) {
			if(fpromotionrewardrecord.getFactivity() != null){
				fpromotionrewardrecord.getFactivity().getFtitle();
			}
			if(fpromotionrewardrecord.getFuser() != null){
				fpromotionrewardrecord.getFuser().getFnickName();
			}
		}
		return all;
	}
	
	public void updateRecord(Fpromotionrewardrecord rewardrecord) throws RuntimeException{
		//更新
		this.promotionrewardrecordDAO.attachDirty(rewardrecord);
        Factivity factivity = this.factivityDAO.findById(rewardrecord.getFactivity().getFid()) ;
        Fuser fuser = this.fuserDAO.findById(rewardrecord.getFuser().getFid()) ;
		//更新钱包，此操作待实现
        String filter = " where factivity.fid ="+factivity.getFid()+" and fuser.fid="+fuser.getFid()+" order by fid desc" ;
		List<Factivitypromotioncompletelog> factivitypromotioncompletelogs = this.factivitypromotioncompletelogDAO.findByParam(0, 1, filter, true, "Factivitypromotioncompletelog") ;
        Factivitypromotioncompletelog factivitypromotioncompletelog = factivitypromotioncompletelogs.get(0) ;
		List<Fpromotionactivityreward> fpromotionactivityrewards = this.fpromotionactivityrewardDAO.findByProperty("factivity.fid", factivity.getFid()) ;
		for (Fpromotionactivityreward fpromotionactivityreward : fpromotionactivityrewards) {
			if(fpromotionactivityreward.getFvirtualCoinOrCny()){
				//虚拟币
				Fvirtualcointype fvirtualcointype = this.fvirtualcointypeDAO.findById(fpromotionactivityreward.getFvirtualcointype().getFid()) ;
				
				double reward = 0 ;
				if(fpromotionactivityreward.getFrateOrReal()){
					//按比率
					reward = factivitypromotioncompletelog.getFamount()*fpromotionactivityreward.getFamount() ;
				}else{
					//实数
					reward = fpromotionactivityreward.getFamount() ;
				}
				
				Fvirtualwallet fvirtualwallet = this.fvirtualwalletDAO.findVirtualWallet(fuser.getFid(), fvirtualcointype.getFid()) ;
				fvirtualwallet.setFtotal(fvirtualwallet.getFtotal()+reward) ;
				this.fvirtualwalletDAO.attachDirty(fvirtualwallet) ;
			}else{
				//CNY
				double reward = 0 ;
				if(fpromotionactivityreward.getFrateOrReal()){
					//按比率
					reward = factivitypromotioncompletelog.getFamount()*fpromotionactivityreward.getFamount() ;
				}else{
					//实数
					reward = fpromotionactivityreward.getFamount() ;
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
		msg.setFtitle("推荐人"+factivity.getFtitle()+"奖励信息,请注意查收");
		msg.setFstatus(MessageStatusEnum.NOREAD_VALUE);
		this.messageDAO.save(msg);
	}
}