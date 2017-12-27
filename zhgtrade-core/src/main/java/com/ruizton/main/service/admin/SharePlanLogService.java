package com.ruizton.main.service.admin;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ruizton.main.dao.FshareplanlogDAO;
import com.ruizton.main.model.Fshareplanlog;

@Service
public class SharePlanLogService {
	@Autowired
	private FshareplanlogDAO shareplanlogDAO;

	public Fshareplanlog findById(int id) {
		return this.shareplanlogDAO.findById(id);
	}

	public void saveObj(Fshareplanlog obj) {
		this.shareplanlogDAO.save(obj);
	}

	public void deleteObj(int id) {
		Fshareplanlog obj = this.shareplanlogDAO.findById(id);
		this.shareplanlogDAO.delete(obj);
	}

	public void updateObj(Fshareplanlog obj) {
		this.shareplanlogDAO.attachDirty(obj);
	}

	public List<Fshareplanlog> findByProperty(String name, Object value) {
		return this.shareplanlogDAO.findByProperty(name, value);
	}

	public List<Fshareplanlog> findAll() {
		return this.shareplanlogDAO.findAll();
	}

	public List<Fshareplanlog> list(int firstResult, int maxResults,
			String filter,boolean isFY) {
		List<Fshareplanlog> all = this.shareplanlogDAO.list(firstResult, maxResults, filter,isFY);
		for (Fshareplanlog fshareplanlog : all) {
			if(fshareplanlog.getFuser() != null){
				fshareplanlog.getFuser().getFnickName();
				if(fshareplanlog.getFuser().getFwallet() != null){
					fshareplanlog.getFuser().getFwallet().getFfrozenRmb();
				}
				if(fshareplanlog.getFshareplan() != null){
					fshareplanlog.getFshareplan().getFamount();
					if(fshareplanlog.getFshareplan().getFvirtualcointype() != null){
						fshareplanlog.getFshareplan().getFvirtualcointype().getFname();
					}
				}
			}
		}
		return all;
	}
	
}