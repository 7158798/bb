package com.ruizton.main.service.admin;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ruizton.main.dao.FwalletDAO;
import com.ruizton.main.model.Fwallet;

@Service
public class WalletService {
	@Autowired
	private FwalletDAO fwalletDAO;

	public Fwallet findById(int id) {
		return this.fwalletDAO.findById(id);
	}

	public void saveObj(Fwallet obj) {
		this.fwalletDAO.save(obj);
	}

	public void deleteObj(int id) {
		Fwallet obj = this.fwalletDAO.findById(id);
		this.fwalletDAO.delete(obj);
	}

	public void updateObj(Fwallet obj) {
		this.fwalletDAO.attachDirty(obj);
	}

	public List<Fwallet> findByProperty(String name, Object value) {
		return this.fwalletDAO.findByProperty(name, value);
	}

	public List<Fwallet> findAll() {
		return this.fwalletDAO.findAll();
	}

	public List<Fwallet> list(int firstResult, int maxResults,
			String filter,boolean isFY) {
		List<Fwallet> all = this.fwalletDAO.list(firstResult, maxResults, filter,isFY);
		return all;
	}
	
	public Map getTotalMoney(){
		return this.fwalletDAO.getTotalMoney();
	}
	
	/**
	 * 获取总资金大于0的
	 * 
	 * @return
	 */
	public List<Map<String, Object>> findGtZeroByTotal(String keyWord){
		return fwalletDAO.findForExport(keyWord, 0, 65535, true);
	}

	public List<Map<String, Object>> findWalletErrorList(Integer symbol, String keyword, String orderField, String orderDirection, int offset, int length, boolean isPage){
		return fwalletDAO.findWalletErrorList(symbol, keyword, orderField, orderDirection, offset, length, isPage);
	}

	public int countWalletErrorList(Integer symbol, String keyword){
		int count = 0;
		if(null == symbol || 0 == symbol){
			count += fwalletDAO.countRMBWalletrError(keyword);
		}
		if(null == symbol || symbol > 0){
			count += fwalletDAO.countCoinWalletError(symbol, keyword);
		}
		return count;
	}
}


















