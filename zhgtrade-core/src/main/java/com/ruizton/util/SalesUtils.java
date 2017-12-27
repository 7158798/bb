package com.ruizton.util;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.ruizton.main.Enum.VouchersStatusEnum;
import com.ruizton.main.model.Fcapitaloperation;
import com.ruizton.main.model.Fentrust;
import com.ruizton.main.model.Fentrustlog;
import com.ruizton.main.model.Fscore;
import com.ruizton.main.model.Fuser;
import com.ruizton.main.model.Fvouchers;
import com.ruizton.main.model.Fwallet;
import com.ruizton.main.service.admin.CapitaloperationService;
import com.ruizton.main.service.admin.EntrustService;
import com.ruizton.main.service.admin.EntrustlogService;
import com.ruizton.main.service.admin.ScoreService;
import com.ruizton.main.service.admin.SystemArgsService;
import com.ruizton.main.service.admin.UserService;
import com.ruizton.main.service.admin.VouchersService;

public class SalesUtils {
	@Autowired
	private CapitaloperationService capitaloperationService;
	@Autowired
	private UserService userService;
	@Autowired
	private VouchersService vouchersService;
	@Autowired
	private SystemArgsService systemArgsService;
	@Autowired
	private ScoreService scoreService;
	@Autowired
	private EntrustlogService entrustlogService;
	@Autowired
	private EntrustService entrustService;

	public void work() {
	    synchronized (this) {
	    	String filter = "where ftype=1 and fstatus=3 and fischarge=0";
			List<Fcapitaloperation> fcapitaloperations = this.capitaloperationService.list(0, 100, filter, true);
			for (Fcapitaloperation fcapitaloperation : fcapitaloperations) {
				if(fcapitaloperation.isFischarge()) continue;
				int userid = fcapitaloperation.getFuser().getFid();
				Fuser fuser = this.userService.findById(userid);
				//代金券
				String ss = "where fuser.fid="+userid+" and fstatus not in (3,4)";
				List<Fvouchers> fvoucherss = this.vouchersService.list(0, 0, ss, false);
				double vouchersAmt = Double.valueOf(this.systemArgsService.getValue("vouchersAmt"));
				if(fvoucherss != null && fvoucherss.size() >0 && fcapitaloperation.getFamount()>=vouchersAmt){
					Fwallet fwallet = fuser.getFwallet();
					Fvouchers fvouchers = fvoucherss.get(0);
					double lastamt = fvouchers.getFlastamount();
					if(lastamt <= 10d){
						fvouchers.setFlastamount(0d);
						fvouchers.setFstatus(VouchersStatusEnum.USED);
					}else{
						fvouchers.setFlastamount(fvouchers.getFlastamount()-10d);
						fvouchers.setFstatus(VouchersStatusEnum.DOING);
					}
					fwallet.setFtotalRmb(fwallet.getFtotalRmb()+10d);
					fcapitaloperation.setFischarge(true);
					
					try {
						this.userService.updateUserVouchers(fvouchers, fwallet, fcapitaloperation);
					} catch (Exception e) {
						continue;
					}
				}
				
				//充值送积分
				try {
					fcapitaloperation.setFischarge(true);
					String[] signSendScore = this.systemArgsService.getValue("vipScore").split("#");
					if(signSendScore.length != 4){
						continue;
					}
					Fscore fscore = fuser.getFscore();
					double s = Utils.getDouble(Double.valueOf(signSendScore[2])*fcapitaloperation.getFamount(), 4);
					fscore.setFscore(fscore.getFscore()+s);
					String[] vipGrade = this.systemArgsService.getValue("vipGrade").split("#");
					if(vipGrade.length != 5){
						continue;
					}
					double vip1 = Double.valueOf(vipGrade[0]);
					double vip2 = Double.valueOf(vipGrade[1]);
					double vip3 = Double.valueOf(vipGrade[2]);
					double vip4 = Double.valueOf(vipGrade[3]);
					double vip5 = Double.valueOf(vipGrade[4]);
					if(fscore.getFscore() < vip1){
						fscore.setFlevel(1);
					}else if(fscore.getFscore() >= vip1 && fscore.getFscore() < vip2){
						fscore.setFlevel(2);
					}else if(fscore.getFscore() >= vip2 && fscore.getFscore() < vip3){
						fscore.setFlevel(3);
					}else if(fscore.getFscore() >= vip3 && fscore.getFscore() < vip4){
						fscore.setFlevel(4);
					}else if(fscore.getFscore() >= vip4 && fscore.getFscore() < vip5){
						fscore.setFlevel(5);
					}else if(fscore.getFscore() >= vip5){
						fscore.setFlevel(6);
					}
					this.scoreService.updateVip(fscore,fcapitaloperation);
				} catch (Exception e) {
					continue;
				}
			}
			
			//交易送积分
			String s = "where fEntrustType=0 and (fischarge is null or fischarge='')";
			List<Fentrustlog> fentrustlogs = this.entrustlogService.listxx(0, 100, s, true);
			for (Fentrustlog fentrustlog : fentrustlogs) {
				double amt = fentrustlog.getFamount();
				int enid = fentrustlog.getFentrust().getFid();
				Fentrust entrust = this.entrustService.findById(enid);
				Fscore fscore = entrust.getFuser().getFscore();
				fentrustlog.setFischarge("true");
				String[] signSendScore = this.systemArgsService.getValue("vipScore").split("#");
				if(signSendScore.length != 4){
					continue;
				}
				double tt = Utils.getDouble(Double.valueOf(signSendScore[3])*amt, 4);
				fscore.setFscore(fscore.getFscore()+tt);
				String[] vipGrade = this.systemArgsService.getValue("vipGrade").split("#");
				if(vipGrade.length != 5){
					continue;
				}
				double vip1 = Double.valueOf(vipGrade[0]);
				double vip2 = Double.valueOf(vipGrade[1]);
				double vip3 = Double.valueOf(vipGrade[2]);
				double vip4 = Double.valueOf(vipGrade[3]);
				double vip5 = Double.valueOf(vipGrade[4]);
				if(fscore.getFscore() < vip1){
					fscore.setFlevel(1);
				}else if(fscore.getFscore() >= vip1 && fscore.getFscore() < vip2){
					fscore.setFlevel(2);
				}else if(fscore.getFscore() >= vip2 && fscore.getFscore() < vip3){
					fscore.setFlevel(3);
				}else if(fscore.getFscore() >= vip3 && fscore.getFscore() < vip4){
					fscore.setFlevel(4);
				}else if(fscore.getFscore() >= vip4 && fscore.getFscore() < vip5){
					fscore.setFlevel(5);
				}else if(fscore.getFscore() >= vip5){
					fscore.setFlevel(6);
				}
				this.scoreService.updateVip(fscore,fentrustlog);
			}
		}
	}
	
}
