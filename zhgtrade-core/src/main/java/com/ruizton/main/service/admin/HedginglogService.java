package com.ruizton.main.service.admin;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ruizton.main.dao.FhedginglogDAO;
import com.ruizton.main.dao.FhedginglogDAO;
import com.ruizton.main.model.Fhedginglog;
import com.ruizton.main.model.Fhedginglog;

@Service
public class HedginglogService {
	@Autowired
	private FhedginglogDAO hedginglogDAO;

	public Fhedginglog findById(int id) {
		return this.hedginglogDAO.findById(id);
	}

	public void saveObj(Fhedginglog obj) {
		this.hedginglogDAO.save(obj);
	}

	public void deleteObj(int id) {
		Fhedginglog obj = this.hedginglogDAO.findById(id);
		this.hedginglogDAO.delete(obj);
	}

	public void updateObj(Fhedginglog obj) {
		this.hedginglogDAO.attachDirty(obj);
	}

	public List<Fhedginglog> findByProperty(String name, Object value) {
		return this.hedginglogDAO.findByProperty(name, value);
	}

	public List<Fhedginglog> findAll() {
		return this.hedginglogDAO.findAll();
	}

	public List<Fhedginglog> list(int firstResult, int maxResults,
			String filter,boolean isFY) {
		return this.hedginglogDAO.list(firstResult, maxResults, filter,isFY);
	}
}