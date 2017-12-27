package com.ruizton.main.service.admin;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ruizton.main.dao.FchargesectionDAO;
import com.ruizton.main.dao.FchargesectionDAO;
import com.ruizton.main.model.Fchargesection;
import com.ruizton.main.model.Fchargesection;

@Service
public class ChargesectionService {
	@Autowired
	private FchargesectionDAO chargesectionDAO;

	public Fchargesection findById(int id) {
		return this.chargesectionDAO.findById(id);
	}

	public void saveObj(Fchargesection obj) {
		this.chargesectionDAO.save(obj);
	}

	public void deleteObj(int id) {
		Fchargesection obj = this.chargesectionDAO.findById(id);
		this.chargesectionDAO.delete(obj);
	}

	public void updateObj(Fchargesection obj) {
		this.chargesectionDAO.attachDirty(obj);
	}

	public List<Fchargesection> findByProperty(String name, Object value) {
		return this.chargesectionDAO.findByProperty(name, value);
	}

	public List<Fchargesection> findAll() {
		return this.chargesectionDAO.findAll();
	}

	public List<Fchargesection> list(int firstResult, int maxResults,
			String filter,boolean isFY) {
		List<Fchargesection> lists =  this.chargesectionDAO.list(firstResult, maxResults, filter,isFY);
		for (Fchargesection fchargesection : lists) {
			if(fchargesection.getFendHour() != null){
				fchargesection.getFcreatetime();
			}
		}
		return lists;
	}
}