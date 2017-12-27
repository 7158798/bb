package com.ruizton.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.ruizton.main.Enum.OperationlogEnum;
import com.ruizton.main.model.Ftotalreport;
import com.ruizton.main.service.admin.CapitaloperationService;
import com.ruizton.main.service.admin.GameOperateLogService;
import com.ruizton.main.service.admin.TotalreportService;
import com.ruizton.main.service.admin.VirtualWalletService;
import com.ruizton.main.service.admin.WalletService;


public class ReportUtil {
	@Autowired
	private WalletService walletService;
	@Autowired
	private VirtualWalletService virtualWalletService;
	@Autowired
	private CapitaloperationService capitaloperationService;
	@Autowired
	private GameOperateLogService gameOperateLogService;
	@Autowired
	private TotalreportService totalreportService;
	
	public void work(){
		try {
			Date startDate = new Date();
			String fileter = "order by fid desc";
			List<Ftotalreport> ftotalreportList = totalreportService.list(0, 1, fileter, true);
			if(ftotalreportList.size() == 1){
				startDate = ftotalreportList.get(0).getFdate();
			}
			Calendar c = Calendar.getInstance();
			c.setTime(new Date());
			c.add(Calendar.DAY_OF_MONTH, -1);
			Date endDate = c.getTime();
			int diffDays = DateUtils.getDays(startDate, endDate);
			if(diffDays < 1){
				return;
			}
			Calendar startCalendar = Calendar.getInstance();
			startCalendar.setTime(startDate);
			for (int i = 0; i < diffDays; i++) {
				startCalendar.add(Calendar.DAY_OF_MONTH, 1);
				SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
				String date = sdf1.format(startCalendar.getTime());

				Ftotalreport totalreport = new Ftotalreport();
				totalreport.setFdate(startCalendar.getTime());
				//人民币总数
				Map allAmts = this.walletService.getTotalMoney();
				double totalCanRmb = Utils.getDouble(Double.valueOf(allAmts.get("totalRmb").toString()),2);
				double frozenRmb = Utils.getDouble(Double.valueOf(allAmts.get("frozenRmb").toString()),2);
				double totalRmb = Utils.getDouble(totalCanRmb+frozenRmb, 2);
				totalreport.setFtotalAmt(totalRmb);
				totalreport.setFtotalCanAmt(totalCanRmb);
				totalreport.setFtotalFrozenAmt(frozenRmb);

				Map allDous = this.virtualWalletService.getTotalQty().get(0);
				double totalDou = Utils.getDouble(Double.valueOf(allDous.get("totalQty").toString()),2);
				double totalFrozenDou = Utils.getDouble(Double.valueOf(allDous.get("frozenQty").toString()),2);
				double totalCanDou = Utils.getDouble(totalDou-totalFrozenDou, 2);
				totalreport.setFtotalDou(totalDou);
				totalreport.setFtotalCanDou(totalCanDou);
				totalreport.setFtotalFrozenDou(totalFrozenDou);

				String filter1 = "where ftype=1 and fstatus=3 and DATE_FORMAT(fLastUpdateTime,'%Y-%m-%d') = '"+date+"'";
				List inAmts = this.capitaloperationService.getTotalAmountForReport(filter1);
				double inAmt = 0d;
				if(inAmts.get(0) != null){
					inAmt = Double.valueOf(inAmts.get(0).toString());
				}

				String filter3 = "where fstatus="+OperationlogEnum.AUDIT+" and DATE_FORMAT(flastUpdateTime,'%Y-%m-%d') = '"+date+"'";
				List opAmts = this.capitaloperationService.getTotalOperationlog(filter3);
				if(opAmts.get(0) != null){
					inAmt = inAmt + Double.valueOf(opAmts.get(0).toString());
				}
				inAmt = Utils.getDouble(inAmt, 2);
				totalreport.setFtotalRechargeAmt(inAmt);

				String filter2 = "where ftype=2 and fstatus=3 and DATE_FORMAT(fLastUpdateTime,'%Y-%m-%d') = '"+date+"'";
				List outAmts = this.capitaloperationService.getTotalAmountForReport(filter2);
				double outAmt = 0d;
				if(outAmts.get(0) != null){
					outAmt = Double.valueOf(outAmts.get(0).toString());
				}
				totalreport.setFtotalWithdrawAmt(outAmt);

				this.totalreportService.saveObj(totalreport);
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
	}
}
