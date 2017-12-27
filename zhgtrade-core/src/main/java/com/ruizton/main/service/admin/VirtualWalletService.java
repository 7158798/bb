package com.ruizton.main.service.admin;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ruizton.main.dao.FgameharvestlogDAO;
import com.ruizton.main.dao.FmessageDAO;
import com.ruizton.main.dao.FvirtualcointypeDAO;
import com.ruizton.main.dao.FvirtualwalletDAO;
import com.ruizton.main.model.Fgameharvestlog;
import com.ruizton.main.model.Fmessage;
import com.ruizton.main.model.Fvirtualcointype;
import com.ruizton.main.model.Fvirtualwallet;

@Service
public class VirtualWalletService {
	@Autowired
	private FvirtualwalletDAO virtualwalletDAO;
	@Autowired
	private FmessageDAO messageDAO;
	@Autowired
	private FgameharvestlogDAO gameharvestlogDAO;
	@Autowired
	private FvirtualcointypeDAO virtualcointypeDAO;

	public Fvirtualwallet findById(int id) {
		return this.virtualwalletDAO.findById(id);
	}

	public void saveObj(Fvirtualwallet obj) {
		this.virtualwalletDAO.save(obj);
	}

	public void deleteObj(int id) {
		Fvirtualwallet obj = this.virtualwalletDAO.findById(id);
		this.virtualwalletDAO.delete(obj);
	}

	public void updateObj(Fvirtualwallet obj) {
		this.virtualwalletDAO.attachDirty(obj);
	}

	public List<Fvirtualwallet> findByProperty(String name, Object value) {
		return this.virtualwalletDAO.findByProperty(name, value);
	}
	
	public List findByTwoProperty(String propertyName1, Object value1,String propertyName2, Object value2) {
		return this.virtualwalletDAO.findByTwoProperty(propertyName1,value1,propertyName2, value2);
	}

	public List<Fvirtualwallet> findAll() {
		return this.virtualwalletDAO.findAll();
	}

	public List<Fvirtualwallet> list(int firstResult, int maxResults,
			String filter,boolean isFY) {
		List<Fvirtualwallet> all = this.virtualwalletDAO.list(firstResult, maxResults, filter,isFY);
		for (Fvirtualwallet fvirtualwallet : all) {
			fvirtualwallet.getFuser().getFlastLoginTime();
			if(fvirtualwallet.getFvirtualcointype() != null){
				fvirtualwallet.getFvirtualcointype().getFname();
			}
		}
		return all;
	}
	
	public List<Map> getTotalQty() {
		return this.virtualwalletDAO.getTotalQty();
	}
	
	public BigDecimal getTotalQty(int vid) {
		return this.virtualwalletDAO.getTotalQty(vid);
	}
	
	public void updateLog(Fvirtualwallet virtualwallet,Fmessage message,Fgameharvestlog fgameharvestlog) {
		try {
			this.virtualwalletDAO.attachDirty(virtualwallet);
			this.messageDAO.save(message);
			this.gameharvestlogDAO.attachDirty(fgameharvestlog);
		} catch (Exception e) {
			throw new RuntimeException();
		}
	}
	
	/**
	 * 导出虚拟币钱包大于0.01的会员信息
	 * 
	 * @param symbol
	 * @return
	 */
	public List<Map<String, Object>> findViWalletForExport(String keyword, Integer symbol){
		return virtualwalletDAO.findForExport(keyword, symbol, 0, 65535, true);
	}
}