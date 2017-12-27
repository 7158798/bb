package com.ruizton.main.service.admin;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ruizton.main.dao.FactivityDAO;
import com.ruizton.main.dao.FchancelogDAO;
import com.ruizton.main.dao.FpopcornDAO;
import com.ruizton.main.dao.FpopcornbetlogDAO;
import com.ruizton.main.dao.FpopcornlogDAO;
import com.ruizton.main.dao.FusersettingDAO;
import com.ruizton.main.dao.FvirtualwalletDAO;
import com.ruizton.main.model.Factivity;
import com.ruizton.main.model.Fchancelog;
import com.ruizton.main.model.Fpopcorn;
import com.ruizton.main.model.Fpopcornbetlog;
import com.ruizton.main.model.Fpopcornlog;
import com.ruizton.main.model.Fusersetting;
import com.ruizton.main.model.Fvirtualwallet;

@Service
public class PopcornlogService {
	@Autowired
	private FpopcornlogDAO popcornlogDAO;
	@Autowired
	private FusersettingDAO usersettingDAO;
	@Autowired
	private FvirtualwalletDAO virtualwalletDAO;
	@Autowired
	private FchancelogDAO chancelogDAO;

	public Fpopcornlog findById(int id) {
		Fpopcornlog popcornlog = this.popcornlogDAO.findById(id);
		
		return popcornlog;
	}

	public void saveObj(Fpopcornlog obj) {
		this.popcornlogDAO.save(obj);
	}

	public void deleteObj(int id) {
		Fpopcornlog obj = this.popcornlogDAO.findById(id);
		this.popcornlogDAO.delete(obj);
	}

	public void updateObj(Fpopcornlog obj) {
		this.popcornlogDAO.attachDirty(obj);
	}

	public List<Fpopcornlog> findByProperty(String name, Object value) {
		return this.popcornlogDAO.findByProperty(name, value);
	}

	public List<Fpopcornlog> findAll() {
		return this.popcornlogDAO.findAll();
	}

	public List<Fpopcornlog> list(int firstResult, int maxResults,
			String filter,boolean isFY) {
		return this.popcornlogDAO.list(firstResult, maxResults, filter,isFY);		
	}
	
	public void updateExendJQ(Fusersetting usersetting,Fvirtualwallet virtualwallet,Fchancelog chancelog) {
		try {
			this.usersettingDAO.attachDirty(usersetting);
			this.virtualwalletDAO.attachDirty(virtualwallet);
			this.chancelogDAO.save(chancelog);
		} catch (Exception e) {
			throw new RuntimeException();
		}
	}
}