package com.ruizton.main.service.admin;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ruizton.main.dao.FandroidpushmentDAO; 
import com.ruizton.main.model.Fandroidpushment; 

@Service
public class AndroidpustmentService {
	@Autowired
	private FandroidpushmentDAO androidpushmentDAO;

	public Fandroidpushment findById(int id) {
		return this.androidpushmentDAO.findById(id);
	}

	public void saveObj(Fandroidpushment obj) {
		this.androidpushmentDAO.save(obj);
	}

	public void deleteObj(int id) {
		Fandroidpushment obj = this.androidpushmentDAO.findById(id);
		this.androidpushmentDAO.delete(obj);
	}

	public void updateObj(Fandroidpushment obj) {
		this.androidpushmentDAO.attachDirty(obj);
	}

	public List<Fandroidpushment> findByProperty(String name, Object value) {
		return this.androidpushmentDAO.findByProperty(name, value);
	}

	public List<Fandroidpushment> findAll() {
		return this.androidpushmentDAO.findAll();
	}

	public List<Fandroidpushment> list(int firstResult, int maxResults,
			String filter,boolean isFY) {
		return this.androidpushmentDAO.list(firstResult, maxResults, filter,isFY);
	}
}