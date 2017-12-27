package com.ruizton.main.service.admin;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ruizton.main.dao.FactivityDAO;
import com.ruizton.main.model.Factivity;

@Service
public class ActivityService {
	@Autowired
	private FactivityDAO activityDAO;

	public Factivity findById(int id) {
		Factivity activity = this.activityDAO.findById(id);
		if(activity.getFactivitytype() != null){
			activity.getFactivitytype().getFname();
			if(activity.getFactivitytype().getFvirtualCoinType() != null){
				activity.getFactivitytype().getFvirtualCoinType().getFname();
			}
		}
		return activity;
	}

	public void saveObj(Factivity obj) {
		this.activityDAO.save(obj);
	}

	public void deleteObj(int id) {
		Factivity obj = this.activityDAO.findById(id);
		this.activityDAO.delete(obj);
	}

	public void updateObj(Factivity obj) {
		this.activityDAO.attachDirty(obj);
	}

	public List<Factivity> findByProperty(String name, Object value) {
		return this.activityDAO.findByProperty(name, value);
	}

	public List<Factivity> findAll() {
		return this.activityDAO.findAll();
	}

	public List<Factivity> list(int firstResult, int maxResults,
			String filter,boolean isFY) {
		List<Factivity> all = this.activityDAO.list(firstResult, maxResults, filter,isFY);
		for (Factivity factivity : all) {
			if(factivity.getFactivitytype() != null){
				factivity.getFactivitytype().getFname();
				if(factivity.getFactivitytype().getFvirtualCoinType() != null){
					factivity.getFactivitytype().getFvirtualCoinType().getFname();
				}
			}
		}
		return all;
	}
}