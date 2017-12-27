package com.ruizton.main.service.front;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ruizton.main.Enum.MessageStatusEnum;
import com.ruizton.main.dao.FfreeLotteryDAO;
import com.ruizton.main.dao.FfreeLotteryRuleDAO;
import com.ruizton.main.dao.FmessageDAO;
import com.ruizton.main.dao.FvirtualwalletDAO;
import com.ruizton.main.model.FfreeLottery;
import com.ruizton.main.model.FfreeLotteryRule;
import com.ruizton.main.model.Fmessage;
import com.ruizton.main.model.Fuser;
import com.ruizton.main.model.Fvirtualcointype;
import com.ruizton.main.model.Fvirtualwallet;
import com.ruizton.main.service.BaseService;
import com.ruizton.util.Utils;

@Service
public class FrontFreeLotteryService extends BaseService {

	@Autowired
	private FfreeLotteryDAO ffreeLotteryDAO ;
	@Autowired
	private FfreeLotteryRuleDAO ffreeLotteryRuleDAO ;
	@Autowired
	private FvirtualwalletDAO fvirtualwalletDAO ;
	@Autowired
	private FmessageDAO fmessageDAO ;
	
	public List<FfreeLottery> listFfreeLotteries(int firstResult, int maxResults, String filter, boolean isFY){
		return this.ffreeLotteryDAO.findByParam(firstResult, maxResults, filter, isFY, "FfreeLottery") ;
	}
	public int listFfreeLotteriesCount(String filter){
		return this.ffreeLotteryDAO.findByParamCount(filter, "FfreeLottery") ;
	}
	
	public List<FfreeLotteryRule> listFfreeLotteryRules(int firstResult, int maxResults, String filter, boolean isFY){
		return this.ffreeLotteryRuleDAO.findByParam(firstResult, maxResults, filter, isFY, "FfreeLotteryRule") ;
	}
	public int listFfreeLotteryRulesCount(String filter){
		return this.ffreeLotteryRuleDAO.findByParamCount(filter, "FfreeLotteryRule") ;
	}
	
	public void updateFfreeLotteryRule(List<FfreeLotteryRule> ffreeLotteryRules){
		for (FfreeLotteryRule ffreeLotteryRule : ffreeLotteryRules) {
			this.ffreeLotteryRuleDAO.attachDirty(ffreeLotteryRule) ;
		}
	}
	
	public FfreeLottery updateLottery(Fuser fuser,Fvirtualcointype fvirtualcointype){
		
		try {
			List<FfreeLottery> ffs = this.ffreeLotteryDAO.findByParam(0, 1,  " where fuser.fid="+fuser.getFid()+" order by fid desc ", true, "FfreeLottery") ;
			if(ffs.size()==1){
				FfreeLottery ff = ffs.get(0) ;
				if(Utils.getTimestamp().getTime() - ff.getFcreatetime().getTime() <60*60*1000L ){//一小时内不能重复抽奖
					return null ;
				}
				ff.setRemark(ff.getRemark()+" ") ;
				this.ffreeLotteryDAO.attachDirty(ff) ;//防止多次快速点击
			}
			
			int num = new Random().nextInt(10000) ;
			FfreeLottery ffreeLottery = new FfreeLottery() ;
			FfreeLotteryRule ffreeLotteryRule = null ;
			List<FfreeLotteryRule> ffreeLotteryRules = this.ffreeLotteryRuleDAO.findAll() ;
			for (FfreeLotteryRule r : ffreeLotteryRules) {
				if(num>=r.getFfrom() && num<=r.getFto()){
					ffreeLotteryRule = r ;
					break ;
				}
			}
			if(ffreeLotteryRule==null || ffreeLotteryRule.getFreward()==0D){
				ffreeLottery.setRemark("很遗憾，您抽中的数字为:"+num+",没有中奖！");
				return ffreeLottery ;
			}
			
			ffreeLottery.setFcreatetime(Utils.getTimestamp()) ;
			ffreeLottery.setFiswin(true) ;
			ffreeLottery.setFreward(ffreeLotteryRule.getFreward()) ;
			ffreeLottery.setFuser(fuser) ;
			ffreeLottery.setFvirtualcointype(fvirtualcointype) ;
			DecimalFormat s=new DecimalFormat("###0.000000");
			ffreeLottery.setRemark("抽中"+num+",恭喜获得"+ffreeLotteryRule.getFname()+"，奖励："+s.format(ffreeLotteryRule.getFreward())+fvirtualcointype.getfShortName()) ;
			this.ffreeLotteryDAO.save(ffreeLottery) ;
			
			Fmessage fmessage = new Fmessage() ;
			fmessage.setFcontent("抽中"+num+",奖励："+s.format(ffreeLotteryRule.getFreward())+fvirtualcointype.getfShortName()) ;
			fmessage.setFcreateTime(Utils.getTimestamp()) ;
			fmessage.setFreceiver(fuser) ;
			fmessage.setFstatus(MessageStatusEnum.NOREAD_VALUE) ;
			fmessage.setFtitle("抽奖奖励通知") ;
			this.fmessageDAO.save(fmessage) ;
			
			Fvirtualwallet fvirtualwallet = this.fvirtualwalletDAO.findVirtualWallet(fuser.getFid(), fvirtualcointype.getFid()) ;
			fvirtualwallet.setFtotal(fvirtualwallet.getFtotal()+ffreeLotteryRule.getFreward()) ;
			this.fvirtualwalletDAO.attachDirty(fvirtualwallet) ;
			
			return ffreeLottery ;
		} catch (Exception e) {
			throw new RuntimeException();
		}
	}
	
	public FfreeLotteryRule findFfreeLotteryRule(int fid){
		return this.ffreeLotteryRuleDAO.findById(fid) ;
	}
	
	public void updateFfreeLotteryRule(FfreeLotteryRule ffreeLotteryRule){
		this.ffreeLotteryRuleDAO.attachDirty(ffreeLotteryRule) ;
	}
	
}
