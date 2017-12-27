package com.ruizton.main.service.admin;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.ruizton.main.Enum.CapitalOperationInStatus;
import com.ruizton.main.Enum.CapitalOperationTypeEnum;
import com.ruizton.main.Enum.ChargePayTypeEnum;
import com.ruizton.util.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ruizton.main.Enum.MessageStatusEnum;
import com.ruizton.main.dao.FcapitaloperationDAO;
import com.ruizton.main.dao.FintrolinfoDAO;
import com.ruizton.main.dao.FmessageDAO;
import com.ruizton.main.dao.FsystemargsDAO;
import com.ruizton.main.dao.FuserDAO;
import com.ruizton.main.dao.FusersettingDAO;
import com.ruizton.main.dao.FwalletDAO;
import com.ruizton.main.model.Fcapitaloperation;
import com.ruizton.main.model.Fintrolinfo;
import com.ruizton.main.model.Fmessage;
import com.ruizton.main.model.Fsystemargs;
import com.ruizton.main.model.Fuser;
import com.ruizton.main.model.Fwallet;
import com.ruizton.util.MathUtils;
import com.ruizton.util.Utils;

@Service
public class CapitaloperationService {
	@Autowired
	private FcapitaloperationDAO fcapitaloperationDAO;
	@Autowired
	private FwalletDAO walletDAO;
	@Autowired
	private FmessageDAO messageDAO;
	@Autowired
	private FsystemargsDAO systemargsDAO;
	@Autowired
	private FusersettingDAO usersettingDAO;
	@Autowired
	private FuserDAO userDAO;
	@Autowired
	private FintrolinfoDAO introlinfoDAO;
	@Autowired
	private IntrolinfoService introlinfoService;

	public Fcapitaloperation findById(int id) {
		Fcapitaloperation fcapitaloperation = this.fcapitaloperationDAO.findById(id);
		if(fcapitaloperation.getFuser().getFwallet() != null){
			fcapitaloperation.getFuser().getFwallet().getFfrozenRmb();
		}
		return fcapitaloperation;
	}

	public void saveObj(Fcapitaloperation obj) {
		this.fcapitaloperationDAO.save(obj);
	}

	public void deleteObj(int id) {
		Fcapitaloperation obj = this.fcapitaloperationDAO.findById(id);
		this.fcapitaloperationDAO.delete(obj);
	}

	public void updateObj(Fcapitaloperation obj) {
		this.fcapitaloperationDAO.attachDirty(obj);
	}

	public List<Fcapitaloperation> findByProperty(String name, Object value) {
		return this.fcapitaloperationDAO.findByProperty(name, value);
	}

	public List<Fcapitaloperation> findAll() {
		return this.fcapitaloperationDAO.findAll();
	}

	public List<Fcapitaloperation> list(int firstResult, int maxResults,
			String filter,boolean isFY) {
		List<Fcapitaloperation> all = this.fcapitaloperationDAO.list(firstResult, maxResults, filter,isFY);
		for (Fcapitaloperation fcapitaloperation : all) {
			fcapitaloperation.getFuser().getFemail() ;
			if(fcapitaloperation.getfAuditee_id() != null){
				fcapitaloperation.getfAuditee_id().getFname();
			}
		}
		return all;
	}

	public void updateCapital(Fcapitaloperation capitaloperation,Fwallet wallet,boolean isRecharge) throws RuntimeException {
		try {
			this.fcapitaloperationDAO.attachDirty(capitaloperation);
			this.walletDAO.attachDirty(wallet);
			String title = "您的充值或提现已审核，请核对，谢谢！";
			Fmessage msg = new Fmessage();
			msg.setFcreateTime(Utils.getTimestamp());
			msg.setFcontent(title);
			msg.setFreceiver(capitaloperation.getFuser());
			msg.setFcreator(capitaloperation.getfAuditee_id());
			msg.setFtitle(title);
			msg.setFstatus(MessageStatusEnum.NOREAD_VALUE);
			this.messageDAO.save(msg);
			
			if(isRecharge){
				int userId = capitaloperation.getFuser().getFid();
				Fuser fuser = this.userDAO.findById(userId);
				if(fuser.getfIntroUser_id() != null && (fuser.getFischarge() == null || fuser.getFischarge().length()==0)){
					Fuser fintrolUser = this.userDAO.findById(fuser.getfIntroUser_id().getFid());
					fuser.setFischarge("true");
					this.userDAO.attachDirty(fuser);
					Fsystemargs args = this.systemargsDAO.findByFkey("introlRegAmt").get(0);
					String[] tt = args.getFvalue().split("#");
					Fwallet fwallet = fintrolUser.getFwallet();
					double amt = 0d;
					if(tt[0].equals("1")){
						amt = Double.valueOf(tt[1]);
						fwallet.setFtotalRmb(MathUtils.add(fwallet.getFtotalRmb(), amt));
					}else{
						amt = Utils.getDouble(Double.valueOf(tt[1])*capitaloperation.getFamount(), 2);
						fwallet.setFtotalRmb(fwallet.getFtotalRmb()+amt);
					}
					Fintrolinfo intro = new Fintrolinfo();
					intro.setFcreatetime(Utils.getTimestamp());
					intro.setFiscny(true);
					intro.setFuser(fintrolUser);
					intro.setFqty(amt);
					intro.setFtitle("下线UID:"+fuser.getFid()+"首次充值，奖励￥"+amt);
					this.introlinfoDAO.save(intro);
					this.walletDAO.attachDirty(fwallet);
				}
			}
		} catch (Exception e) {
			throw new RuntimeException();
		}
	}
	
	public Map getTotalAmount(int type,String fstatus,boolean isToday) {
		return this.fcapitaloperationDAO.getTotalAmount(type, fstatus,isToday);
	}
	
	public List getTotalGroup(String filter) {
		return this.fcapitaloperationDAO.getTotalGroup(filter);
	}
	
	public List getTotalAmountForReport(String filter) {
		return this.fcapitaloperationDAO.getTotalAmountForReport(filter);
	}
	
	public List getTotalOperationlog(String filter) {
		return this.fcapitaloperationDAO.getTotalOperationlog(filter);
	}

	/**
	 * 充值成功
	 *
	 * @param billId 订单号
	 * @param amount 充值金额
	 */
	public void updateChargeSuccess(int billId, double amount, double fee, String outTradeNo) {
		List<Fcapitaloperation> list = fcapitaloperationDAO.findByProperties(new String[]{"fid", "fstatus"}, new Object[]{billId, CapitalOperationInStatus.WaitForComing});
		if (CollectionUtils.isEmpty(list) || !isValidChargeBill(list.get(0), amount)) {
			// 订单不匹配
			return;
		}

		// 到账
		double actualAmount = amount - fee;

		Fcapitaloperation fcapitaloperation = list.get(0);
		fcapitaloperation.setOutTradeNo(outTradeNo);
		fcapitaloperation.setFfees(fee);
		fcapitaloperation.setActualAmount(actualAmount);
		fcapitaloperation.setFstatus(CapitalOperationInStatus.Come);
		fcapitaloperation.setfLastUpdateTime(Utils.getTimestamp());
		fcapitaloperationDAO.attachDirty(fcapitaloperation);
		Fwallet fwallet = fcapitaloperation.getFuser().getFwallet();
		fwallet.setFtotalRmb(fwallet.getFtotalRmb() + actualAmount);
		this.walletDAO.attachDirty(fwallet);

		// 发送消息
		String title = "您的充值已到账，请核对，谢谢！";
		Fmessage msg = new Fmessage();
		msg.setFcreateTime(Utils.getTimestamp());
		msg.setFcontent(title);
		msg.setFreceiver(fcapitaloperation.getFuser());
		msg.setFtitle(title);
		msg.setFstatus(MessageStatusEnum.NOREAD_VALUE);
		this.messageDAO.save(msg);

		// 推广人充值奖励
		introlinfoService.updateChargeAward(fcapitaloperation);
	}

	/**
	 * 充值失败
	 *
	 * @param billId 订单号
	 * @param amount 充值金额
	 */
	public void updateChargeFailure(int billId, double amount){
		Fcapitaloperation fcapitaloperation = fcapitaloperationDAO.findById(billId);
		if (!isValidChargeBill(fcapitaloperation, amount)) {
			// 订单不匹配
			return;
		}

		fcapitaloperation.setFstatus(CapitalOperationInStatus.Invalidate);
		fcapitaloperation.setfLastUpdateTime(Utils.getTimestamp());
		fcapitaloperationDAO.attachDirty(fcapitaloperation);
	}

	private boolean isValidChargeBill(Fcapitaloperation fcapitaloperation, double amount){
		return CapitalOperationTypeEnum.RMB_IN == fcapitaloperation.getFtype() && amount == fcapitaloperation.getFamount() && CapitalOperationInStatus.WaitForComing == fcapitaloperation.getFstatus();
	}

	/**
	 * 列出活动时间内充值成功的记录
	 * @param start
	 * @param end
     * @return
     */
	/*public List<Fcapitaloperation> listForAwarding(Timestamp start, Timestamp end){
		return fcapitaloperationDAO.listForAwarding(start, end);
	}*/

	public Object[] capitalInReport(Date startDate, Date endDate, ChargePayTypeEnum payType, String bank){
		return this.fcapitaloperationDAO.capitalInReport(startDate, endDate, payType, bank);
	}

	public List<String[]> listForWithdrawFeeReport(Date startDate, Date endDate, int type, int status){
		return fcapitaloperationDAO.listForWithdrawFeeReport(startDate, endDate, type, status);
	}

}