package com.ruizton.main.service.admin;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ruizton.main.dao.FdeductlogDAO;
import com.ruizton.main.dao.FdeductlogDAO;
import com.ruizton.main.model.Fdeductlog;
import com.ruizton.main.model.Fdeductlog;

@Service
public class DeductlogService {
	@Autowired
	private FdeductlogDAO deductlogDAO;

	public Fdeductlog findById(int id) {
		return this.deductlogDAO.findById(id);
	}

	public void saveObj(Fdeductlog obj) {
		this.deductlogDAO.save(obj);
	}

	public void deleteObj(int id) {
		Fdeductlog obj = this.deductlogDAO.findById(id);
		this.deductlogDAO.delete(obj);
	}

	public void updateObj(Fdeductlog obj) {
		this.deductlogDAO.attachDirty(obj);
	}

	public List<Fdeductlog> findByProperty(String name, Object value) {
		return this.deductlogDAO.findByProperty(name, value);
	}

	public List<Fdeductlog> findAll() {
		return this.deductlogDAO.findAll();
	}

	public List<Fdeductlog> list(int firstResult, int maxResults,
			String filter,boolean isFY) {
		List<Fdeductlog> list = this.deductlogDAO.list(firstResult, maxResults, filter,isFY);
		for (Fdeductlog fdeductlog : list) {
			if(fdeductlog.getFuser() != null){
				fdeductlog.getFuser().getFbankinfos();
			}
			if(fdeductlog.getFsourceUserId() != null){
				fdeductlog.getFsourceUserId().getFnickName();
			}
		}
		return list;
	}
	
	public List getTotalDeductlog(String filter) {
		return this.deductlogDAO.getTotalDeductlog(filter);
	}
	
	public void updateDeductlog(String sql) {
		this.deductlogDAO.updateDeductlog(sql);
	}
}