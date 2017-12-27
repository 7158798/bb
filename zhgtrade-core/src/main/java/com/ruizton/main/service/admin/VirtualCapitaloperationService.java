package com.ruizton.main.service.admin;

import java.util.List;
import java.util.Map;

import com.ruizton.main.dao.FvirtualaddressDAO;
import com.ruizton.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ruizton.main.dao.FvirtualcaptualoperationDAO;
import com.ruizton.main.dao.FvirtualwalletDAO;
import com.ruizton.main.model.Fvirtualcaptualoperation;
import com.ruizton.main.model.Fvirtualwallet;
import com.ruizton.util.BTCUtils;

@Service
public class VirtualCapitaloperationService {
	@Autowired
	private FvirtualcaptualoperationDAO virtualcaptualoperationDAO;
	@Autowired
	private FvirtualwalletDAO virtualwalletDAO;
	private static Logger logger = LoggerFactory.getLogger(VirtualCapitaloperationService.class);

	public Fvirtualcaptualoperation findById(int id) {
		Fvirtualcaptualoperation info = this.virtualcaptualoperationDAO.findById(id);
		info.getFuser().getFnickName();
		info.getFvirtualcointype().getFname();
		return info;
	}

	public void saveObj(Fvirtualcaptualoperation obj) {
		this.virtualcaptualoperationDAO.save(obj);
	}

	public void deleteObj(int id) {
		Fvirtualcaptualoperation obj = this.virtualcaptualoperationDAO
				.findById(id);
		this.virtualcaptualoperationDAO.delete(obj);
	}

	public void updateObj(Fvirtualcaptualoperation obj) {
		this.virtualcaptualoperationDAO.attachDirty(obj);
	}

	public List<Fvirtualcaptualoperation> findByProperty(String name,
			Object value) {
		return this.virtualcaptualoperationDAO.findByProperty(name, value);
	}

	public List<Fvirtualcaptualoperation> findAll() {
		return this.virtualcaptualoperationDAO.findAll();
	}

	public List<Fvirtualcaptualoperation> list(int firstResult, int maxResults,
			String filter, boolean isFY) {
		List<Fvirtualcaptualoperation> all = this.virtualcaptualoperationDAO
				.list(firstResult, maxResults, filter, isFY);
		for (Fvirtualcaptualoperation virtualcaptualoperation : all) {
			if(virtualcaptualoperation.getFuser() != null){
				virtualcaptualoperation.getFuser().getFemail();
			}
			if(virtualcaptualoperation.getFvirtualcointype() != null){
				virtualcaptualoperation.getFvirtualcointype().getfShortName();
			}
			
		}
		return all;
	}

	public void updateCapital(Fvirtualcaptualoperation virtualcaptualoperation,
			Fvirtualwallet virtualwallet,BTCUtils btcUtils) throws RuntimeException {
		this.virtualcaptualoperationDAO.attachDirty(virtualcaptualoperation);
		this.virtualwalletDAO.attachDirty(virtualwallet);

		try {
			// 优先更新数据库，再转账，保证最终一致性问题
			String address = virtualcaptualoperation.getWithdraw_virtual_address();
			double amount = virtualcaptualoperation.getFamount();
			String flag = btcUtils.sendtoaddressValue(address,amount,virtualcaptualoperation.getFid().toString());
			if(StringUtils.hasText(flag)){
				try{
					virtualcaptualoperation = this.virtualcaptualoperationDAO.findById(virtualcaptualoperation.getFid());
					virtualcaptualoperation.setFtradeUniqueNumber(flag);
					this.virtualcaptualoperationDAO.attachDirty(virtualcaptualoperation);
				}catch (Exception e){
					e.printStackTrace();
					if(logger.isErrorEnabled()){
						logger.error(virtualcaptualoperation.getFvirtualcointype().getFname() + "提现订单" + virtualcaptualoperation.getFid() + "更新交易号" + flag + "失败", e);
					}
				}
			}else{
				throw new RuntimeException("提现失败");
			}
		} catch (Exception e) {
			e.printStackTrace();
			String msg = e.getMessage();
			if (StringUtils.isEmpty(msg)) {
				msg = "系统繁忙";
			}
			throw new RuntimeException(msg);
		}
	}
	
	public List<Map> getTotalAmount(int type,String fstatus,boolean isToday) {
		return this.virtualcaptualoperationDAO.getTotalQty(type, fstatus,isToday);
	}
	
	public List getTotalGroup(String filter) {
		return this.virtualcaptualoperationDAO.getTotalGroup(filter);
	}

}