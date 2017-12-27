package com.ruizton.main.service.admin;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ruizton.main.dao.FsubscriptionDAO;
import com.ruizton.main.model.Fsubscription;

@Service
public class SubscriptionService {
	@Autowired
	private FsubscriptionDAO subscriptionDAO;

	public Fsubscription findById(int id) {
		return this.subscriptionDAO.findById(id);
	}

	public void saveObj(Fsubscription obj) {
		this.subscriptionDAO.save(obj);
	}

	public void deleteObj(int id) {
		Fsubscription obj = this.subscriptionDAO.findById(id);
		this.subscriptionDAO.delete(obj);
	}

	public void updateObj(Fsubscription obj) {
		this.subscriptionDAO.attachDirty(obj);
	}

	public List<Fsubscription> findByProperty(String name, Object value) {
		return this.subscriptionDAO.findByProperty(name, value);
	}

	public List<Fsubscription> findAll() {
		return this.subscriptionDAO.findAll();
	}

	public List<Fsubscription> list(int firstResult, int maxResults,
			String filter,boolean isFY) {
		List<Fsubscription> all =  this.subscriptionDAO.list(firstResult, maxResults, filter,isFY);
		for (Fsubscription fsubscription : all) {
			if(fsubscription.getFvirtualcointype() != null){
				fsubscription.getFvirtualcointype().getFname();
			}
			
			if(fsubscription.getFvirtualcointypeCost() != null){
				fsubscription.getFvirtualcointypeCost().getFname();
			}
		}
		return all;
	}
}