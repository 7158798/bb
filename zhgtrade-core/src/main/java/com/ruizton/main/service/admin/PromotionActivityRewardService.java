package com.ruizton.main.service.admin;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ruizton.main.dao.FactivityDAO;
import com.ruizton.main.dao.FactivityrewardDAO;
import com.ruizton.main.dao.FpromotionactivityrewardDAO;
import com.ruizton.main.model.Fpromotionactivityreward;

@Service
public class PromotionActivityRewardService {
	@Autowired
	private FpromotionactivityrewardDAO promotionactivityrewardDAO;

	public Fpromotionactivityreward findById(int id) {
		Fpromotionactivityreward promotionactivityreward = this.promotionactivityrewardDAO.findById(id);
		if(promotionactivityreward.getFactivity() != null){
			promotionactivityreward.getFactivity().getFtitle();
		}
		return promotionactivityreward;
	}

	public void saveObj(Fpromotionactivityreward obj) {
		this.promotionactivityrewardDAO.save(obj);
	}

	public void deleteObj(int id) {
		Fpromotionactivityreward obj = this.promotionactivityrewardDAO.findById(id);
		this.promotionactivityrewardDAO.delete(obj);
	}

	public void updateObj(Fpromotionactivityreward obj) {
		this.promotionactivityrewardDAO.attachDirty(obj);
	}

	public List<Fpromotionactivityreward> findByProperty(String name, Object value) {
		return this.promotionactivityrewardDAO.findByProperty(name, value);
	}

	public List<Fpromotionactivityreward> findAll() {
		return this.promotionactivityrewardDAO.findAll();
	}

	public List<Fpromotionactivityreward> list(int firstResult, int maxResults,
			String filter,boolean isFY) {
		List<Fpromotionactivityreward> all = this.promotionactivityrewardDAO.list(firstResult, maxResults, filter,isFY);
		for (Fpromotionactivityreward factivityreward : all) {
			if(factivityreward.getFactivity() != null){
				factivityreward.getFactivity().getFtitle();
			}
			if(factivityreward.getFvirtualcointype() != null){
				factivityreward.getFvirtualcointype().getFname();
			}
		}
		return all;
	}
}