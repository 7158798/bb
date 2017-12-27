package com.ruizton.main.service.admin;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ruizton.main.Enum.MessageStatusEnum;
import com.ruizton.main.Enum.SharePlanLogStatusEnum;
import com.ruizton.main.Enum.SharePlanTypeEnum;
import com.ruizton.main.dao.FentrustDAO;
import com.ruizton.main.dao.FentrustlogDAO;
import com.ruizton.main.dao.FmessageDAO;
import com.ruizton.main.dao.FshareplanDAO;
import com.ruizton.main.dao.FshareplanlogDAO;
import com.ruizton.main.dao.FuserDAO;
import com.ruizton.main.dao.FvirtualwalletDAO;
import com.ruizton.main.dao.FwalletDAO;
import com.ruizton.main.model.Fmessage;
import com.ruizton.main.model.Fshareplan;
import com.ruizton.main.model.Fshareplanlog;
import com.ruizton.main.model.Fvirtualcointype;
import com.ruizton.main.model.Fvirtualwallet;
import com.ruizton.main.model.Fwallet;
import com.ruizton.util.Utils;

@Service
public class SharePlanService {
	@Autowired
	private FshareplanDAO shareplanDAO;
	@Autowired
	private FvirtualwalletDAO virtualwalletDAO;
	@Autowired
	private FmessageDAO messageDAO;
	@Autowired
	private FuserDAO userDAO;
	@Autowired
	private FwalletDAO walletDAO;
	@Autowired
	private FshareplanlogDAO shareplanlogDAO;
	@Autowired
	private FentrustlogDAO entrustlogDAO;
	@Autowired
	private VirtualCoinService virtualCoinService;
	@Autowired
	private FentrustDAO fentrustDAO ;

	public Fshareplan findById(int id) {
		return this.shareplanDAO.findById(id);
	}

	public void saveObj(Fshareplan obj) {
		this.shareplanDAO.save(obj);
	}

	public void deleteObj(int id) {
		Fshareplan obj = this.shareplanDAO.findById(id);
		this.shareplanDAO.delete(obj);
	}

	public void updateObj(Fshareplan obj) {
		this.shareplanDAO.attachDirty(obj);
	}

	public List<Fshareplan> findByProperty(String name, Object value) {
		return this.shareplanDAO.findByProperty(name, value);
	}

	public List<Fshareplan> findAll() {
		return this.shareplanDAO.findAll();
	}

	public List<Fshareplan> list(int firstResult, int maxResults,
			String filter,boolean isFY) {
		List<Fshareplan> all = this.shareplanDAO.list(firstResult, maxResults, filter,isFY);
		for (Fshareplan fshareplan : all) {
			if(fshareplan.getFcreator() != null){
				fshareplan.getFcreator().getFname();
			}
			if(fshareplan.getFvirtualcointype() != null){
				fshareplan.getFvirtualcointype().getFname();
			}
			int sharePlanId = fshareplan.getFid();
			String filter1 = "where fshareplan.fid="+sharePlanId+" and fstatus="+SharePlanLogStatusEnum.NOSEND;
			List<Fshareplanlog> noSendAll = this.shareplanlogDAO.list(0, 0, filter1, false);
			fshareplan.setNoSend(noSendAll.size());
			
			String filter2 = "where fshareplan.fid="+sharePlanId+" and fstatus="+SharePlanLogStatusEnum.HASSEND;
			List<Fshareplanlog> hasSendAll = this.shareplanlogDAO.list(0, 0, filter2, false);
			fshareplan.setHasSend(hasSendAll.size());
		}
		return all;
	}
	
	public void updateSharePlanLog(Fwallet wallet,Fshareplanlog fshareplanlog)  throws RuntimeException{
		try {
			this.walletDAO.attachDirty(wallet);
			this.shareplanlogDAO.attachDirty(fshareplanlog);
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}
	
	public boolean updateSharePlanLog1(Fshareplanlog fshareplanlog)  throws RuntimeException{
        this.shareplanlogDAO.attachDirty(fshareplanlog);
		return true;
	}
	
	public boolean updateSharePlanLog1(Fvirtualwallet virtualwallet,Fshareplanlog fshareplanlog)  throws RuntimeException{
		this.virtualwalletDAO.attachDirty(virtualwallet);
        this.shareplanlogDAO.attachDirty(fshareplanlog);
		return true;
	}
	
	public void update(Fshareplan sharePlan) throws RuntimeException{
		int vid = sharePlan.getFvirtualcointype().getFid();
		//分红总金额
		BigDecimal amount = sharePlan.getFamount();
		BigDecimal totalQty = this.virtualwalletDAO.getTotalQty(vid);
		BigDecimal oneAmount = BigDecimal.ZERO;
		String filter = "";
		oneAmount = amount.divide(totalQty, 8, RoundingMode.HALF_UP);
		filter = "where fvirtualcointype.fid="+vid+" and ftotal >0";
		if(oneAmount.compareTo(BigDecimal.ZERO) == 0){
			throw new RuntimeException("xxx");
		}

		try {
			List<Fvirtualwallet> all = this.virtualwalletDAO.list(0, 0, filter, false);
			for (Fvirtualwallet fvirtualwallet : all) {
				BigDecimal totalShareAmt = BigDecimal.ZERO;
				double total = fvirtualwallet.getFtotal()+fvirtualwallet.getFfrozen();
				totalShareAmt = new BigDecimal(total).multiply(oneAmount).setScale(2, RoundingMode.HALF_UP);
				
				if(totalShareAmt.compareTo(BigDecimal.ZERO) == 0) continue;
				Fshareplanlog sharePlanLog = new Fshareplanlog();
				sharePlanLog.setFamount(totalShareAmt);//最后分到多少
				sharePlanLog.setFselfAmount(total);
				sharePlanLog.setfTotalAmount(Double.valueOf(amount.toString()));
				sharePlanLog.setFoneAmount(oneAmount.doubleValue());
				sharePlanLog.setfTotalQty(totalQty.doubleValue());

				sharePlanLog.setFcreatetime(Utils.getTimestamp());
				sharePlanLog.setFshareplan(sharePlan);
				sharePlanLog.setFuser(fvirtualwallet.getFuser());
				sharePlanLog.setFstatus(SharePlanLogStatusEnum.NOSEND);
				this.shareplanlogDAO.attachDirty(sharePlanLog);
				
				Fmessage msg = new Fmessage();
				msg.setFcontent("网站分红，每个分红币分红："+oneAmount+",参与分红数量:"+total+",平台总币数："+totalQty+",一共分得人民币："+totalShareAmt);
				msg.setFtitle("网站分红，一共分得人民币："+totalShareAmt+"，请注意查收");
				msg.setFcreateTime(Utils.getTimestamp());
				msg.setFreceiver(fvirtualwallet.getFuser());
				msg.setFstatus(MessageStatusEnum.NOREAD_VALUE);
				this.messageDAO.save(msg);
			}
			this.shareplanDAO.attachDirty(sharePlan);
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}

}