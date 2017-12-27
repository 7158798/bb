package com.ruizton.main.service.admin;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ruizton.main.dao.FbetlogDAO;
import com.ruizton.main.dao.FbetlogDAO;
import com.ruizton.main.model.Fbetlog;
import com.ruizton.main.model.Fbetlog;

@Service
public class BetlogService {
	@Autowired
	private FbetlogDAO betlogDAO;

	public Fbetlog findById(int id) {
		return this.betlogDAO.findById(id);
	}

	public void saveObj(Fbetlog obj) {
		this.betlogDAO.save(obj);
	}

	public void deleteObj(int id) {
		Fbetlog obj = this.betlogDAO.findById(id);
		this.betlogDAO.delete(obj);
	}

	public void updateObj(Fbetlog obj) {
		this.betlogDAO.attachDirty(obj);
	}

	public List<Fbetlog> findByProperty(String name, Object value) {
		return this.betlogDAO.findByProperty(name, value);
	}

	public List<Fbetlog> findAll() {
		return this.betlogDAO.findAll();
	}

	public List<Fbetlog> list(int firstResult, int maxResults,
			String filter,boolean isFY) {
		return this.betlogDAO.list(firstResult, maxResults, filter,isFY);
	}
	
	public Map getTotalFees(boolean isToday) {
		return this.betlogDAO.getTotalFees(isToday);
	}
}