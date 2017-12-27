package com.ruizton.main.service.admin;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ruizton.main.dao.FapplyinfoDAO;
import com.ruizton.main.dao.FuserDAO;
import com.ruizton.main.model.Fapplyinfo;
import com.ruizton.main.model.Fuser;

@Service
public class ApplyinfoService {
	@Autowired
	private FapplyinfoDAO applyinfoDAO;
	@Autowired
	private FuserDAO userDAO;

	public Fapplyinfo findById(int id) {
		return this.applyinfoDAO.findById(id);
	}

	public void saveObj(Fapplyinfo obj) {
		this.applyinfoDAO.save(obj);
	}

	public void deleteObj(int id) {
		Fapplyinfo obj = this.applyinfoDAO.findById(id);
		this.applyinfoDAO.delete(obj);
	}

	public void updateObj(Fapplyinfo obj) {
		this.applyinfoDAO.attachDirty(obj);
	}

	public List<Fapplyinfo> findByProperty(String name, Object value) {
		return this.applyinfoDAO.findByProperty(name, value);
	}

	public List<Fapplyinfo> findAll() {
		return this.applyinfoDAO.findAll();
	}

	public List<Fapplyinfo> list(int firstResult, int maxResults,
			String filter,boolean isFY) {
		return this.applyinfoDAO.list(firstResult, maxResults, filter,isFY);
	}
	
	public void updateApplyinfo(Fuser fuser,Fapplyinfo applyinfo) {
		try {
			this.userDAO.attachDirty(fuser);
			this.applyinfoDAO.save(applyinfo);
		} catch (Exception e) {
			throw new RuntimeException();
		}
	}
	
	public void updateAuditApplyinfo(Fuser fuser,Fapplyinfo applyinfo) {
		try {
			this.userDAO.attachDirty(fuser);
			this.applyinfoDAO.attachDirty(applyinfo);
		} catch (Exception e) {
			throw new RuntimeException();
		}
	}
	
}