package com.ruizton.main.service.admin;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ruizton.main.dao.FlendruleDAO;
import com.ruizton.main.model.Flendrule;

@Service
public class LendRuleService {
	@Autowired
	private FlendruleDAO lendruleDAO;

	public Flendrule findById(int id) {
		return this.lendruleDAO.findById(id);
	}

	public void saveObj(Flendrule obj) {
		this.lendruleDAO.save(obj);
	}

	public void deleteObj(int id) {
		Flendrule obj = this.lendruleDAO.findById(id);
		this.lendruleDAO.delete(obj);
	}

	public void updateObj(Flendrule obj) {
		this.lendruleDAO.attachDirty(obj);
	}

	public List<Flendrule> findByProperty(String name, Object value) {
		return this.lendruleDAO.findByProperty(name, value);
	}

	public List<Flendrule> findAll() {
		return this.lendruleDAO.findAll();
	}

	public List<Flendrule> list(int firstResult, int maxResults,
			String filter,boolean isFY) {
		return this.lendruleDAO.list(firstResult, maxResults, filter,isFY);
	}
}