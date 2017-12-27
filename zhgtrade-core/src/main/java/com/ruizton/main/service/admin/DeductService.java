package com.ruizton.main.service.admin;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ruizton.main.dao.FdeductDAO;
import com.ruizton.main.dao.FdeductlogDAO;
import com.ruizton.main.dao.FmessageDAO;
import com.ruizton.main.dao.FvirtualwalletDAO;
import com.ruizton.main.dao.FwalletDAO;
import com.ruizton.main.model.Fdeduct;
import com.ruizton.main.model.Fdeductlog;
import com.ruizton.main.model.Fmessage;
import com.ruizton.main.model.Fvirtualwallet;
import com.ruizton.main.model.Fwallet;

@Service
public class DeductService {
	@Autowired
	private FdeductDAO deductDAO;
	@Autowired
	private FmessageDAO messageDAO;
	@Autowired
	private FvirtualwalletDAO virtualwalletDAO;
	@Autowired
	private FwalletDAO walletDAO;

	public Fdeduct findById(int id) {
		Fdeduct deduct = this.deductDAO.findById(id);
		if(deduct.getFuser().getFwallet() != null){
			deduct.getFuser().getFwallet().getFlastUpdateTime();
		}
		return deduct;
	}

	public void saveObj(Fdeduct obj) {
		this.deductDAO.save(obj);
	}

	public void deleteObj(int id) {
		Fdeduct obj = this.deductDAO.findById(id);
		this.deductDAO.delete(obj);
	}

	public void updateObj(Fdeduct obj) {
		this.deductDAO.attachDirty(obj);
	}

	public List<Fdeduct> findByProperty(String name, Object value) {
		return this.deductDAO.findByProperty(name, value);
	}

	public List<Fdeduct> findAll() {
		return this.deductDAO.findAll();
	}

	public List<Fdeduct> list(int firstResult, int maxResults,
			String filter,boolean isFY) {
		List<Fdeduct> deduct = this.deductDAO.list(firstResult, maxResults, filter,isFY);
		for (Fdeduct fdeduct : deduct) {
			if(fdeduct.getFuser() != null){
				fdeduct.getFuser().getFaddress();
			}
			if(fdeduct.getFuser().getFwallet() != null){
				fdeduct.getFuser().getFwallet().getFlastUpdateTime();
			}
			if(fdeduct.getFchargesection() != null){
				fdeduct.getFchargesection().getFendHour();
			}
			if(fdeduct.getFadmin() != null){
				fdeduct.getFadmin().getFcreateTime();
			}
		}
		return deduct;
	}
	public void updatelog(Fmessage message,Fdeduct deduct) {
		try {
			this.messageDAO.save(message);
			this.deductDAO.attachDirty(deduct);
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}
	
	public void updatelog(Fmessage message,Fdeduct deduct,Fvirtualwallet virtualwallet,Fwallet wallet) {
		try {
			this.messageDAO.save(message);
			this.deductDAO.attachDirty(deduct);
			this.virtualwalletDAO.attachDirty(virtualwallet);
			this.walletDAO.attachDirty(wallet);
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}
	
	public List deductTotalList(int firstResult, int maxResults,
			String filter,boolean isFY) {
		List all = this.deductDAO.deductTotalList(firstResult, maxResults, filter,isFY);
		return all;
	}

	public int getAllCount(String tableName,String filter){
		return this.deductDAO.getAllCount(tableName,filter);
	}
}