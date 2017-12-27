package com.ruizton.main.service.admin;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ruizton.main.dao.FlendflowlogDAO;
import com.ruizton.main.model.Flendflowlog;

@Service
public class LendflowlogService {
	@Autowired
	private FlendflowlogDAO lendflowlogDAO;

	public Flendflowlog findById(int id) {
		return this.lendflowlogDAO.findById(id);
	}

	public void saveObj(Flendflowlog obj) {
		this.lendflowlogDAO.save(obj);
	}

	public void deleteObj(int id) {
		Flendflowlog obj = this.lendflowlogDAO.findById(id);
		this.lendflowlogDAO.delete(obj);
	}

	public void updateObj(Flendflowlog obj) {
		this.lendflowlogDAO.attachDirty(obj);
	}

	public List<Flendflowlog> findByProperty(String name, Object value) {
		return this.lendflowlogDAO.findByProperty(name, value);
	}

	public List<Flendflowlog> findAll() {
		return this.lendflowlogDAO.findAll();
	}

	public List<Flendflowlog> list(int firstResult, int maxResults,
			String filter,boolean isFY) {
		return this.lendflowlogDAO.list(firstResult, maxResults, filter,isFY);
	}
}