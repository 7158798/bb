package com.ruizton.main.service.admin;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ruizton.main.dao.FhedginglogDAO;
import com.ruizton.main.dao.FhedginguserlogDAO;
import com.ruizton.main.dao.FhedginguserlogDAO;
import com.ruizton.main.dao.FvirtualwalletDAO;
import com.ruizton.main.model.Fhedginglog;
import com.ruizton.main.model.Fhedginguserlog;
import com.ruizton.main.model.Fhedginguserlog;
import com.ruizton.main.model.Fvirtualwallet;

@Service
public class HedginguserlogService {
	@Autowired
	private FhedginguserlogDAO hedginguserlogDAO;
	@Autowired
	private FvirtualwalletDAO virtualwalletDAO;
	@Autowired
	private FhedginglogDAO hedginglogDAO;

	public Fhedginguserlog findById(int id) {
		return this.hedginguserlogDAO.findById(id);
	}

	public void saveObj(Fhedginguserlog obj) {
		this.hedginguserlogDAO.save(obj);
	}

	public void deleteObj(int id) {
		Fhedginguserlog obj = this.hedginguserlogDAO.findById(id);
		this.hedginguserlogDAO.delete(obj);
	}

	public void updateObj(Fvirtualwallet fvirtualwallet,Fhedginguserlog obj) {
		try {
			this.hedginguserlogDAO.attachDirty(obj);
			this.virtualwalletDAO.attachDirty(fvirtualwallet);
		} catch (Exception e) {
			throw new RuntimeException();
		}
	}
	
	public void updateObj(Fhedginguserlog obj) {
		this.hedginguserlogDAO.attachDirty(obj);
	}

	public List<Fhedginguserlog> findByProperty(String name, Object value) {
		return this.hedginguserlogDAO.findByProperty(name, value);
	}

	public List<Fhedginguserlog> findAll() {
		return this.hedginguserlogDAO.findAll();
	}

	public List<Fhedginguserlog> list(int firstResult, int maxResults,
			String filter,boolean isFY) {
		return this.hedginguserlogDAO.list(firstResult, maxResults, filter,isFY);
	}
	
	public void updateFilllog(Fvirtualwallet fvirtualwallet,Fhedginguserlog hedginguserlog,Fhedginglog hedginglog) {
		try {
			this.virtualwalletDAO.attachDirty(fvirtualwallet);
			this.hedginguserlogDAO.save(hedginguserlog);
			this.hedginglogDAO.attachDirty(hedginglog);
		} catch (Exception e) {
			throw new RuntimeException();
		}

	}
}