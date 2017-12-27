package com.ruizton.main.service.admin;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ruizton.main.dao.FchancelogDAO;
import com.ruizton.main.dao.FchancelogDAO;
import com.ruizton.main.model.Fchancelog;
import com.ruizton.main.model.Fchancelog;

@Service
public class ChancelogService {
	@Autowired
	private FchancelogDAO chancelogDAO;

	public Fchancelog findById(int id) {
		return this.chancelogDAO.findById(id);
	}

	public void saveObj(Fchancelog obj) {
		this.chancelogDAO.save(obj);
	}

	public void deleteObj(int id) {
		Fchancelog obj = this.chancelogDAO.findById(id);
		this.chancelogDAO.delete(obj);
	}

	public void updateObj(Fchancelog obj) {
		this.chancelogDAO.attachDirty(obj);
	}

	public List<Fchancelog> findByProperty(String name, Object value) {
		return this.chancelogDAO.findByProperty(name, value);
	}

	public List<Fchancelog> findAll() {
		return this.chancelogDAO.findAll();
	}

	public List<Fchancelog> list(int firstResult, int maxResults,
			String filter,boolean isFY) {
		return this.chancelogDAO.list(firstResult, maxResults, filter,isFY);
	}
}