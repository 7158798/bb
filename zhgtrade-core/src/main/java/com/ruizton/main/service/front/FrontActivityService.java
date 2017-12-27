package com.ruizton.main.service.front;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ruizton.main.Enum.ActivityCompleteStatusEnum;
import com.ruizton.main.Enum.ActivityRewardStatusEnum;
import com.ruizton.main.Enum.ActivityStatusEnum;
import com.ruizton.main.dao.FactivityDAO;
import com.ruizton.main.dao.FactivitycompletelogDAO;
import com.ruizton.main.dao.FactivitypromotioncompletelogDAO;
import com.ruizton.main.dao.FactivityrewardDAO;
import com.ruizton.main.dao.FactivitytypeDAO;
import com.ruizton.main.dao.FpromotionactivityrewardDAO;
import com.ruizton.main.dao.FpromotionrewardrecordDAO;
import com.ruizton.main.dao.FrewardrecordDAO;
import com.ruizton.main.dao.FuserDAO;
import com.ruizton.main.dao.FvirtualcointypeDAO;
import com.ruizton.main.dao.FvirtualwalletDAO;
import com.ruizton.main.dao.FwalletDAO;
import com.ruizton.main.model.Factivity;
import com.ruizton.main.model.Factivitycompletelog;
import com.ruizton.main.model.Factivitypromotioncompletelog;
import com.ruizton.main.model.Factivityreward;
import com.ruizton.main.model.Factivitytype;
import com.ruizton.main.model.Fpromotionactivityreward;
import com.ruizton.main.model.Fpromotionrewardrecord;
import com.ruizton.main.model.Frewardrecord;
import com.ruizton.main.model.Fuser;
import com.ruizton.main.model.Fvirtualcointype;
import com.ruizton.main.model.Fvirtualwallet;
import com.ruizton.main.model.Fwallet;
import com.ruizton.util.Utils;

@Service
public class FrontActivityService {

	@Autowired
	private FactivitycompletelogDAO factivitycompletelogDAO ;
	@Autowired
	private FactivitypromotioncompletelogDAO factivitypromotioncompletelogDAO ;
	@Autowired
	private FactivityrewardDAO factivityrewardDAO ;
	@Autowired
	private FpromotionactivityrewardDAO fpromotionactivityrewardDAO ;
	@Autowired
	private FvirtualwalletDAO fvirtualwalletDAO ;
	@Autowired
	private FwalletDAO fwalletDAO ;
	@Autowired
	private FactivityDAO factivityDAO ;
	@Autowired
	private FuserDAO fuserDAO ;
	@Autowired
	private FactivitytypeDAO factivitytypeDAO ;
	@Autowired
	private FrewardrecordDAO frewardrecordDAO ;
	@Autowired
	private FpromotionrewardrecordDAO fpromotionrewardrecordDAO ;
	@Autowired
	private FvirtualcointypeDAO fvirtualcointypeDAO ;
	
	//虚拟币相关的类型
	public Factivitytype findFactivityType(int type ,Fvirtualcointype fvirtualcointype){
		return this.factivitytypeDAO.findFactivityType(type, fvirtualcointype) ;
	}
	public Factivitytype findFactivityTypeById(int id){
		return this.factivitytypeDAO.findById(id) ;
	}
	
	
	public void updateCompleteOneActivity(Factivitytype factivitytype,Fuser fuser){//完成一项任务调用
		if(factivitytype==null){
			System.out.println("factivity type null!");
			return ;
		}
		
		Factivity factivity = this.findActivityById(factivitytype, fuser) ;
		if(factivity!=null){
			this.updateCompleteActivity(factivity, fuser) ;
			this.updateCheckReward(factivity, fuser) ;
		}
	}
	
	private Factivity findActivityById(Factivitytype factivitytype,Fuser fuser){
		String filter = "where factivitytype.fid="+factivitytype.getFid()+" and fstatus="+ActivityStatusEnum.ACTIVE ;
		List<Factivity> factivities = this.factivityDAO.list(0, 0, filter, false) ;
		
		Factivity factivity = null ;
		if(factivities.size()>0){
			factivity = factivities.get(0) ;
		}
		
		long now = Utils.getTimestamp().getTime() ;
		if(factivity!=null){
			boolean status = factivity.getFstatus()==ActivityStatusEnum.ACTIVE ;
			boolean active = 
					factivity.isFisActiveForever()
					|| (!factivity.isFisActiveForever() && (now>factivity.getfBeginTime().getTime()&&now<factivity.getfEndTime().getTime()) ) ;
			
			if(status&&active){
				
				String filter_1 = " where factivity.fid ="+factivity.getFid()+" and fuser.fid="+fuser.getFid()+" order by fid desc" ;
				List<Factivitycompletelog> factivitycompletelogs = this.factivitycompletelogDAO.findByParam(0, 1, filter_1, true, "Factivitycompletelog") ;
				
				if(factivity.getFisMultiple()){
					if(factivitycompletelogs.size()==0 || now-factivitycompletelogs.get(0).getFcreateTime().getTime()>factivity.getFtimeInterval()*1000L){
						return factivity ;
					}
				}else{
					if(factivitycompletelogs.size()==0){
						return factivity ;
					}
				}
			}
		}
		
		return null ;
	}
	
	//增加完成任务
	private void updateCompleteActivity(Factivity factivity,Fuser fuser){
			Timestamp now = Utils.getTimestamp() ;
			Factivitycompletelog factivitycompletelog = new Factivitycompletelog() ;
			Factivitypromotioncompletelog factivitypromotioncompletelog = new Factivitypromotioncompletelog() ;
			
			factivitycompletelog.setFactivity(factivity) ;
			factivitycompletelog.setFcreateTime(now) ;
			factivitycompletelog.setFlastUpdateTime(now) ;
			factivitycompletelog.setFstatus(ActivityCompleteStatusEnum.NOT_GET_REWARD) ;
			factivitycompletelog.setFuser(fuser) ;
			this.factivitycompletelogDAO.save(factivitycompletelog) ;
			
			if(fuser.getfIntroUser_id()!=null){
				factivitypromotioncompletelog.setFactivity(factivity) ;
				factivitypromotioncompletelog.setFcreateTime(now) ;
				factivitypromotioncompletelog.setFlastUpdateTime(now) ;
				factivitypromotioncompletelog.setFstatus(ActivityCompleteStatusEnum.NOT_GET_REWARD) ;
				factivitypromotioncompletelog.setFuser(fuser.getfIntroUser_id()) ;
				this.factivitypromotioncompletelogDAO.save(factivitypromotioncompletelog) ;
			}
			
		}
		
		//是否达到领奖要求
		/**
		 * @param factivity
		 * @param fuser
		 */
	private void updateCheckReward(Factivity factivity,Fuser fuser){
			int rewardCount = factivity.getFrewardPerCount() ;
			int promotionRewardCount = factivity.getFrewardPromotionPerCount() ;
			Fuser intro = fuser.getfIntroUser_id() ;
			if(intro!=null){
				intro = this.fuserDAO.findById(intro.getFid()) ;
			}
			
			

			if(rewardCount>0){
				//完成任务本人奖励
				String filter = " where factivity.fid ="+factivity.getFid()+" and fuser.fid="+fuser.getFid()+" and fstatus="+ActivityCompleteStatusEnum.NOT_GET_REWARD+" order by fid desc" ;
				List<Factivitycompletelog> factivitycompletelogs = this.factivitycompletelogDAO.findByParam(0, rewardCount, filter, true, "Factivitycompletelog") ;

				if(factivitycompletelogs.size()>=rewardCount){
					//有奖励
					for (Factivitycompletelog factivitycompletelog : factivitycompletelogs) {
						factivitycompletelog.setFstatus(ActivityCompleteStatusEnum.GET_REWARD) ;
						factivitycompletelog.setFlastUpdateTime(Utils.getTimestamp()) ;
						this.factivitycompletelogDAO.attachDirty(factivitycompletelog) ;
					}
					
					Frewardrecord frewardrecord = new Frewardrecord() ;
					frewardrecord.setFactivity(factivity) ;
					frewardrecord.setFuser(fuser) ;
					frewardrecord.setFcreateTime(Utils.getTimestamp()) ;
					frewardrecord.setFlastUpdatetime(Utils.getTimestamp()) ;
					frewardrecord.setFstatus(ActivityRewardStatusEnum.NOT_REWARD) ;
					
					StringBuffer rewardReason = new StringBuffer("奖励清单如下：<br/>") ;
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
							rewardReason.append(fvirtualcointype.getfShortName()+":"+fvirtualcointype.getfSymbol()+reward+"<br/>") ;
							
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
							
							rewardReason.append("CNY:￥"+reward+"<br/>") ;
						}
					}
					
					frewardrecord.setFrewardReason(rewardReason.toString()) ;
					this.frewardrecordDAO.save(frewardrecord) ;
					
				}
			}
			
			
			
			if(promotionRewardCount>0 && intro!=null){
				//完成任务奖励给推广人的
				String filter = " where factivity.fid ="+factivity.getFid()+" and fuser.fid="+intro.getFid()+" and fstatus="+ActivityCompleteStatusEnum.NOT_GET_REWARD+" order by fid desc" ;
				List<Factivitypromotioncompletelog> factivitypromotioncompletelogs = this.factivitypromotioncompletelogDAO.findByParam(0, promotionRewardCount, filter, true, "Factivitypromotioncompletelog") ;
				if(factivitypromotioncompletelogs.size()>=promotionRewardCount){
					//有奖励
					for (Factivitypromotioncompletelog factivitypromotioncompletelog : factivitypromotioncompletelogs) {
						factivitypromotioncompletelog.setFstatus(ActivityCompleteStatusEnum.GET_REWARD) ;
						factivitypromotioncompletelog.setFlastUpdateTime(Utils.getTimestamp()) ;
						this.factivitypromotioncompletelogDAO.attachDirty(factivitypromotioncompletelog) ;
					}
					
					
					Fpromotionrewardrecord fpromotionrewardrecord = new Fpromotionrewardrecord() ;
					fpromotionrewardrecord.setFactivity(factivity) ;
					fpromotionrewardrecord.setFcreateTime(Utils.getTimestamp()) ;
					fpromotionrewardrecord.setFlastUpdatetime(Utils.getTimestamp()) ;
					fpromotionrewardrecord.setFstatus(ActivityRewardStatusEnum.NOT_REWARD) ;
					fpromotionrewardrecord.setFuser(intro) ;
					
					StringBuffer rewardReason = new StringBuffer("奖励清单如下：<br/>") ;
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
							rewardReason.append(fvirtualcointype.getfShortName()+":"+fvirtualcointype.getfSymbol()+reward+"<br/>") ;
							
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
							
							rewardReason.append("CNY:￥"+reward+"<br/>") ;
						}
						
						fpromotionrewardrecord.setFrewardReason(rewardReason.toString()) ;
						this.fpromotionrewardrecordDAO.save(fpromotionrewardrecord) ;
						
					}
				}
			
			}
			
			
		}
}
