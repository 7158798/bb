package com.ruizton.main.service.admin;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ruizton.main.dao.FspendlogDAO;
import com.ruizton.main.dao.FspendlogDAO;
import com.ruizton.main.model.Fspendlog;
import com.ruizton.main.model.Fspendlog;

@Service
public class SpendlogService {
	@Autowired
	private FspendlogDAO spendlogDAO;

	public Fspendlog findById(int id) {
		return this.spendlogDAO.findById(id);
	}

	public void saveObj(Fspendlog obj) {
		this.spendlogDAO.save(obj);
	}

	public void deleteObj(int id) {
		Fspendlog obj = this.spendlogDAO.findById(id);
		this.spendlogDAO.delete(obj);
	}

	public void updateObj(Fspendlog obj) {
		this.spendlogDAO.attachDirty(obj);
	}

	public List<Fspendlog> findByProperty(String name, Object value) {
		return this.spendlogDAO.findByProperty(name, value);
	}

	public List<Fspendlog> findAll() {
		return this.spendlogDAO.findAll();
	}

	public List<Fspendlog> list(int firstResult, int maxResults,
			String filter,boolean isFY) {
		List<Fspendlog> all = this.spendlogDAO.list(firstResult, maxResults, filter,isFY);
		for (Fspendlog fspendlog : all) {
			if(fspendlog.getFuser() != null){
				fspendlog.getFuser().getFnickName();
			}
		}
		return all;
	}
	
	
	public Map getTotalQty(boolean isToday) {
		return this.spendlogDAO.getTotalQty(isToday);
	}
}