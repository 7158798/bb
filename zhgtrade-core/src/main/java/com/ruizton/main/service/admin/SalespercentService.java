package com.ruizton.main.service.admin;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ruizton.main.dao.FsalespercentDAO;
import com.ruizton.main.model.Fsalespercent;

@Service
public class SalespercentService {
	@Autowired
	private FsalespercentDAO salesPercentDAO;

	public Fsalespercent findById(int id) {
		Fsalespercent salespercent = this.salesPercentDAO.findById(id);
		if(salespercent.getFlevel() != null){
			salespercent.getFlevel();
		}
		return salespercent;
	}

	public void saveObj(Fsalespercent obj) {
		this.salesPercentDAO.save(obj);
	}

	public void deleteObj(int id) {
		Fsalespercent obj = this.salesPercentDAO.findById(id);
		this.salesPercentDAO.delete(obj);
	}

	public void updateObj(Fsalespercent obj) {
		this.salesPercentDAO.attachDirty(obj);
	}

	public List<Fsalespercent> findByProperty(String name, Object value) {
		return this.salesPercentDAO.findByProperty(name, value);
	}

	public List<Fsalespercent> findAll() {
		return this.salesPercentDAO.findAll();
	}

	public List<Fsalespercent> list(int firstResult, int maxResults,
			String filter,boolean isFY) {
		return this.salesPercentDAO.list(firstResult, maxResults, filter,isFY);
	}
}