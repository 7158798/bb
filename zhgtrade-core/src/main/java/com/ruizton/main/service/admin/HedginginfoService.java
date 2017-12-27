package com.ruizton.main.service.admin;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ruizton.main.dao.FhedginginfoDAO;
import com.ruizton.main.dao.FhedginginfoDAO;
import com.ruizton.main.model.Fhedginginfo;
import com.ruizton.main.model.Fhedginginfo;

@Service
public class HedginginfoService {
	@Autowired
	private FhedginginfoDAO hedginginfoDAO;

	public Fhedginginfo findById(int id) {
		return this.hedginginfoDAO.findById(id);
	}

	public void saveObj(Fhedginginfo obj) {
		this.hedginginfoDAO.save(obj);
	}

	public void deleteObj(int id) {
		Fhedginginfo obj = this.hedginginfoDAO.findById(id);
		this.hedginginfoDAO.delete(obj);
	}

	public void updateObj(Fhedginginfo obj) {
		this.hedginginfoDAO.attachDirty(obj);
	}

	public List<Fhedginginfo> findByProperty(String name, Object value) {
		return this.hedginginfoDAO.findByProperty(name, value);
	}

	public List<Fhedginginfo> findAll() {
		return this.hedginginfoDAO.findAll();
	}

	public List<Fhedginginfo> list(int firstResult, int maxResults,
			String filter,boolean isFY) {
		return this.hedginginfoDAO.list(firstResult, maxResults, filter,isFY);
	}
}