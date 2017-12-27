package com.ruizton.main.service.admin;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ruizton.main.dao.FtotalreportDAO;
import com.ruizton.main.model.Ftotalreport;

@Service
public class TotalreportService {
	@Autowired
	private FtotalreportDAO totalreportDAO;

	public Ftotalreport findById(int id) {
		return this.totalreportDAO.findById(id);
	}

	public void saveObj(Ftotalreport obj) {
		this.totalreportDAO.save(obj);
	}

	public void deleteObj(int id) {
		Ftotalreport obj = this.totalreportDAO.findById(id);
		this.totalreportDAO.delete(obj);
	}

	public void updateObj(Ftotalreport obj) {
		this.totalreportDAO.attachDirty(obj);
	}

	public List<Ftotalreport> findByProperty(String name, Object value) {
		return this.totalreportDAO.findByProperty(name, value);
	}

	public List<Ftotalreport> findAll() {
		return this.totalreportDAO.findAll();
	}

	public List<Ftotalreport> list(int firstResult, int maxResults,
			String filter,boolean isFY) {
		return this.totalreportDAO.list(firstResult, maxResults, filter,isFY);
	}
}