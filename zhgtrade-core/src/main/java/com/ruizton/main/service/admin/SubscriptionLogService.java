package com.ruizton.main.service.admin;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ruizton.main.dao.FmessageDAO;
import com.ruizton.main.dao.FsubscriptionlogDAO;
import com.ruizton.main.dao.FvirtualwalletDAO;
import com.ruizton.main.model.Fmessage;
import com.ruizton.main.model.Fsubscriptionlog;
import com.ruizton.main.model.Fvirtualwallet;

@Service
public class SubscriptionLogService {
	@Autowired
	private FsubscriptionlogDAO subscriptionlogDAO;
	@Autowired
	private FvirtualwalletDAO fvirtualwalletDAO;
	@Autowired
	private FmessageDAO fmessageDAO;

	public Fsubscriptionlog findById(int id) {
		return this.subscriptionlogDAO.findById(id);
	}

	public void saveObj(Fsubscriptionlog obj) {
		this.subscriptionlogDAO.save(obj);
	}

	public void deleteObj(int id) {
		Fsubscriptionlog obj = this.subscriptionlogDAO.findById(id);
		this.subscriptionlogDAO.delete(obj);
	}

	public void updateObj(Fsubscriptionlog obj) {
		this.subscriptionlogDAO.attachDirty(obj);
	}

	public List<Fsubscriptionlog> findByProperty(String name, Object value) {
		return this.subscriptionlogDAO.findByProperty(name, value);
	}

	public List<Fsubscriptionlog> findAll() {
		return this.subscriptionlogDAO.findAll();
	}

	public List<Fsubscriptionlog> list(int firstResult, int maxResults,
			String filter,boolean isFY) {
		List<Fsubscriptionlog> all = this.subscriptionlogDAO.list(firstResult, maxResults, filter,isFY);
		for (Fsubscriptionlog fsubscriptionlog : all) {
			if(fsubscriptionlog.getFuser() != null){
				fsubscriptionlog.getFuser().getFnickName();
			}
		}
		return all;
	}
	
	public void updateFrozenSub(Fsubscriptionlog subscriptionlog,Fvirtualwallet fvirtualwallet,Fmessage fmessage) {
		try {
			this.subscriptionlogDAO.attachDirty(subscriptionlog);
			this.fvirtualwalletDAO.attachDirty(fvirtualwallet);
			this.fmessageDAO.save(fmessage);
		} catch (Exception e) {
			throw new RuntimeException();
		}
	}
}