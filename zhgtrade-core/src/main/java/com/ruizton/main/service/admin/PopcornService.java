package com.ruizton.main.service.admin;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ruizton.main.dao.FactivityDAO;
import com.ruizton.main.dao.FpopcornDAO;
import com.ruizton.main.model.Factivity;
import com.ruizton.main.model.Fpopcorn;

@Service
public class PopcornService {
	@Autowired
	private FpopcornDAO popcornDAO;

	public Fpopcorn findById(int id) {
		return this.popcornDAO.findById(id);
	}

	public void saveObj(Fpopcorn obj) {
		this.popcornDAO.save(obj);
	}

	public void deleteObj(int id) {
		Fpopcorn obj = this.popcornDAO.findById(id);
		this.popcornDAO.delete(obj);
	}

	public void updateObj(Fpopcorn obj) {
		this.popcornDAO.attachDirty(obj);
	}

	public List<Factivity> findByProperty(String name, Object value) {
		return this.popcornDAO.findByProperty(name, value);
	}

	public List<Factivity> findAll() {
		return this.popcornDAO.findAll();
	}

	public List<Fpopcorn> list(int firstResult, int maxResults,
			String filter,boolean isFY) {
		return this.popcornDAO.list(firstResult, maxResults, filter,isFY);		
	}
}