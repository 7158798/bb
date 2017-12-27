package com.ruizton.main.service.admin;

import java.util.List; 

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ruizton.main.dao.FvouchersDAO; 
import com.ruizton.main.model.Fvouchers; 

@Service
public class VouchersService {
	@Autowired
	private FvouchersDAO vouchersDAO;

	public Fvouchers findById(int id) {
		return this.vouchersDAO.findById(id);
	}

	public void saveObj(Fvouchers obj) {
		this.vouchersDAO.save(obj);
	}

	public void deleteObj(int id) {
		Fvouchers obj = this.vouchersDAO.findById(id);
		this.vouchersDAO.delete(obj);
	}

	public void updateObj(Fvouchers obj) {
		this.vouchersDAO.attachDirty(obj);
	}

	public List<Fvouchers> findByProperty(String name, Object value) {
		return this.vouchersDAO.findByProperty(name, value);
	}

	public List<Fvouchers> findAll() {
		return this.vouchersDAO.findAll();
	}

	public List<Fvouchers> list(int firstResult, int maxResults,
			String filter,boolean isFY) {
		return this.vouchersDAO.list(firstResult, maxResults, filter,isFY);
	}
}