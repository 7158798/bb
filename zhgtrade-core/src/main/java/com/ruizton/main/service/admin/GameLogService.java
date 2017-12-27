package com.ruizton.main.service.admin;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ruizton.main.dao.FgamelogDAO;
import com.ruizton.main.model.Fgame;
import com.ruizton.main.model.Fgamelog;

@Service
public class GameLogService {
	@Autowired
	private FgamelogDAO gamelogDao;

	public Fgamelog findById(int id) {
		return this.gamelogDao.findById(id);
	}

	public void saveObj(Fgamelog obj) {
		this.gamelogDao.save(obj);
	}

	public void deleteObj(int id) {
		Fgamelog obj = this.gamelogDao.findById(id);
		this.gamelogDao.delete(obj);
	}

	public void updateObj(Fgamelog obj) {
		this.gamelogDao.attachDirty(obj);
	}

	public List<Fgamelog> findByProperty(String name, Object value) {
		return this.gamelogDao.findByProperty(name, value);
	}

	public List<Fgamelog> findAll() {
		return this.gamelogDao.findAll();
	}

	public List<Fgamelog> list(int firstResult, int maxResults,
			String filter,boolean isFY) {
		List<Fgamelog> all = this.gamelogDao.list(firstResult, maxResults, filter,isFY);
		return all;
	}
}