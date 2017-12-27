package com.ruizton.main.service.admin;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ruizton.main.dao.FactivityDAO;
import com.ruizton.main.dao.FactivityrewardDAO;
import com.ruizton.main.model.Factivity;
import com.ruizton.main.model.Factivityreward;

@Service
public class ActivityRewardService {
	@Autowired
	private FactivityrewardDAO activityrewardDAO;

	public Factivityreward findById(int id) {
		Factivityreward activityreward = this.activityrewardDAO.findById(id);
		if(activityreward.getFactivity() != null){
			activityreward.getFactivity().getFtitle();
		}
		return activityreward;
	}

	public void saveObj(Factivityreward obj) {
		this.activityrewardDAO.save(obj);
	}

	public void deleteObj(int id) {
		Factivityreward obj = this.activityrewardDAO.findById(id);
		this.activityrewardDAO.delete(obj);
	}

	public void updateObj(Factivityreward obj) {
		this.activityrewardDAO.attachDirty(obj);
	}

	public List<Factivityreward> findByProperty(String name, Object value) {
		return this.activityrewardDAO.findByProperty(name, value);
	}

	public List<Factivityreward> findAll() {
		return this.activityrewardDAO.findAll();
	}

	public List<Factivityreward> list(int firstResult, int maxResults,
			String filter,boolean isFY) {
		List<Factivityreward> all = this.activityrewardDAO.list(firstResult, maxResults, filter,isFY);
		for (Factivityreward factivityreward : all) {
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