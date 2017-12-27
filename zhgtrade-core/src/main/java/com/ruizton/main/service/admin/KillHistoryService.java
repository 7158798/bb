package com.ruizton.main.service.admin;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ruizton.main.dao.FkillhistoryDAO;
import com.ruizton.main.model.Fkillhistory;

@Service
public class KillHistoryService {
	@Autowired
	private FkillhistoryDAO killhistoryDAO;

	public Fkillhistory findById(int id) {
		return this.killhistoryDAO.findById(id);
	}

	public void saveObj(Fkillhistory obj) {
		this.killhistoryDAO.save(obj);
	}

	public void deleteObj(int id) {
		Fkillhistory obj = this.killhistoryDAO.findById(id);
		this.killhistoryDAO.delete(obj);
	}

	public void updateObj(Fkillhistory obj) {
		this.killhistoryDAO.attachDirty(obj);
	}

	public List<Fkillhistory> findByProperty(String name, Object value) {
		return this.killhistoryDAO.findByProperty(name, value);
	}

	public List<Fkillhistory> findAll() {
		return this.killhistoryDAO.findAll();
	}

	public List<Fkillhistory> list(int firstResult, int maxResults,
			String filter,boolean isFY) {
		return this.killhistoryDAO.list(firstResult, maxResults, filter,isFY);
	}
}