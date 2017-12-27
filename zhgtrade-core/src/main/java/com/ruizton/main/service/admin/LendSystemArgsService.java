package com.ruizton.main.service.admin;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ruizton.main.dao.FlendsystemargsDAO;
import com.ruizton.main.model.Flendsystemargs;

@Service
public class LendSystemArgsService {
	@Autowired
	private FlendsystemargsDAO lendsystemargsDAO;

	public Flendsystemargs findById(int id) {
		return this.lendsystemargsDAO.findById(id);
	}

	public void saveObj(Flendsystemargs obj) {
		this.lendsystemargsDAO.save(obj);
	}

	public void deleteObj(int id) {
		Flendsystemargs obj = this.lendsystemargsDAO.findById(id);
		this.lendsystemargsDAO.delete(obj);
	}

	public void updateObj(Flendsystemargs obj) {
		this.lendsystemargsDAO.attachDirty(obj);
	}

	public List<Flendsystemargs> findByProperty(String name, Object value) {
		return this.lendsystemargsDAO.findByProperty(name, value);
	}

	public List<Flendsystemargs> findAll() {
		return this.lendsystemargsDAO.findAll();
	}

	public List<Flendsystemargs> list(int firstResult, int maxResults,
			String filter,boolean isFY) {
		return this.lendsystemargsDAO.list(firstResult, maxResults, filter,isFY);
	}
}