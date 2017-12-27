package com.ruizton.main.service.admin;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ruizton.main.dao.FaboutDAO;
import com.ruizton.main.dao.FactivitytypeDAO;
import com.ruizton.main.dao.FadminDAO;
import com.ruizton.main.model.Fabout;
import com.ruizton.main.model.Factivitytype;
import com.ruizton.main.model.Fadmin;

@Service
public class ActivityTypeService {
	@Autowired
	private FactivitytypeDAO activitytypeDAO;

	public Factivitytype findById(int id) {
		return this.activitytypeDAO.findById(id);
	}

	public void saveObj(Factivitytype obj) {
		this.activitytypeDAO.save(obj);
	}

	public void deleteObj(int id) {
		Factivitytype obj = this.activitytypeDAO.findById(id);
		this.activitytypeDAO.delete(obj);
	}

	public void updateObj(Factivitytype obj) {
		this.activitytypeDAO.attachDirty(obj);
	}

	public List<Factivitytype> findByProperty(String name, Object value) {
		return this.activitytypeDAO.findByProperty(name, value);
	}

	public List<Factivitytype> findAll() {
		return this.activitytypeDAO.findAll();
	}

	public List<Factivitytype> list(int firstResult, int maxResults,
			String filter,boolean isFY) {
		List<Factivitytype> all = this.activitytypeDAO.list(firstResult, maxResults, filter,isFY);
		for (Factivitytype factivitytype : all) {
			if(factivitytype.getFvirtualCoinType() != null){
				factivitytype.getFvirtualCoinType().getFname();
			}
		}
		return all;
	}
}