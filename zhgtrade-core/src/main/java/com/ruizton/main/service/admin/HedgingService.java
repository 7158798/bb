package com.ruizton.main.service.admin;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ruizton.main.dao.FhedgingDAO;
import com.ruizton.main.dao.FhedgingDAO;
import com.ruizton.main.model.Fhedging;
import com.ruizton.main.model.Fhedging;

@Service
public class HedgingService {
	@Autowired
	private FhedgingDAO hedgingDAO;

	public Fhedging findById(int id) {
		return this.hedgingDAO.findById(id);
	}

	public void saveObj(Fhedging obj) {
		this.hedgingDAO.save(obj);
	}

	public void deleteObj(int id) {
		Fhedging obj = this.hedgingDAO.findById(id);
		this.hedgingDAO.delete(obj);
	}

	public void updateObj(Fhedging obj) {
		this.hedgingDAO.attachDirty(obj);
	}

	public List<Fhedging> findByProperty(String name, Object value) {
		return this.hedgingDAO.findByProperty(name, value);
	}

	public List<Fhedging> findAll() {
		return this.hedgingDAO.findAll();
	}

	public List<Fhedging> list(int firstResult, int maxResults,
			String filter,boolean isFY) {
		return this.hedgingDAO.list(firstResult, maxResults, filter,isFY);
	}
}