package com.ruizton.main.service.admin;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ruizton.main.dao.FaboutDAO;
import com.ruizton.main.dao.FadminDAO;
import com.ruizton.main.dao.FmessageDAO;
import com.ruizton.main.dao.FpaycodeDAO;
import com.ruizton.main.dao.FproxyDAO;
import com.ruizton.main.dao.FwalletDAO;
import com.ruizton.main.model.Fabout;
import com.ruizton.main.model.Fadmin;
import com.ruizton.main.model.Fmessage;
import com.ruizton.main.model.Fpaycode;
import com.ruizton.main.model.Fproxy;
import com.ruizton.main.model.Fwallet;

@Service
public class PaycodeService {
	@Autowired
	private FpaycodeDAO paycodeDAO;
	@Autowired
	private FmessageDAO messageDAO;
	@Autowired
	private FwalletDAO walletDAO;
	@Autowired
	private FproxyDAO fproxyDAO ;
 
	public Fpaycode findById(int id) {
		Fpaycode paycode = this.paycodeDAO.findById(id);
		if(paycode.getFuser() != null){
			paycode.getFuser().getFwallet().getFlastUpdateTime();
		}
		return paycode;
	}

	public void saveObj(Fpaycode obj) {
		this.paycodeDAO.save(obj);
	}

	public void deleteObj(int id) {
		Fpaycode obj = this.paycodeDAO.findById(id);
		this.paycodeDAO.delete(obj);
	}

	public void updateObj(Fpaycode obj) {
		this.paycodeDAO.attachDirty(obj);
	}

	public List<Fpaycode> findByProperty(String name, Object value) {
		return this.paycodeDAO.findByProperty(name, value);
	}

	public List<Fpaycode> findAll() {
		return this.paycodeDAO.findAll();
	}

	public List<Fproxy> listFproxy(){
		return this.fproxyDAO.findAll() ;
	}
	public Fproxy findFproxyById(int fid){
		return this.fproxyDAO.findById(fid) ;
	}
	public void saveFproxy(Fproxy fproxy){
		this.fproxyDAO.save(fproxy) ;
	}
	
	public void updateFproxy(Fproxy fproxy){
		this.fproxyDAO.attachDirty(fproxy) ;
	}
	
	public void deleteFproxy(Fproxy fproxy){
		this.fproxyDAO.delete(fproxy) ;
	}
	
	public List<Fpaycode> list(int firstResult, int maxResults,
			String filter,boolean isFY) {
		List<Fpaycode> list = this.paycodeDAO.list(firstResult, maxResults, filter,isFY);
		for (Fpaycode fpaycode : list) {
			if(fpaycode.getFuser() != null){
				fpaycode.getFuser().getFemail();
			}
		}
		return list;
	}
	public void updatelog(Fmessage message, Fpaycode paycode ,Fwallet wallet) {
		try {
			this.messageDAO.save(message);
			this.paycodeDAO.attachDirty(paycode);
			this.walletDAO.attachDirty(wallet);
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}
}