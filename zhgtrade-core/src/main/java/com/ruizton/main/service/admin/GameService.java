package com.ruizton.main.service.admin;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ruizton.main.dao.FaboutDAO;
import com.ruizton.main.dao.FadminDAO;
import com.ruizton.main.dao.FgameDAO;
import com.ruizton.main.model.Fabout;
import com.ruizton.main.model.Fadmin;
import com.ruizton.main.model.Fgame;

@Service
public class GameService {
	@Autowired
	private FgameDAO gameDao;

	public Fgame findById(int id) {
		Fgame game = this.gameDao.findById(id);
		if(game.getFvirtualcointype() != null){
			game.getFvirtualcointype().getFname();
		}
		return game;
	}

	public void saveObj(Fgame obj) {
		this.gameDao.save(obj);
	}

	public void deleteObj(int id) {
		Fgame obj = this.gameDao.findById(id);
		this.gameDao.delete(obj);
	}

	public void updateObj(Fgame obj) {
		this.gameDao.attachDirty(obj);
	}

	public List<Fgame> findByProperty(String name, Object value) {
		return this.gameDao.findByProperty(name, value);
	}

	public List<Fgame> findAll() {
		return this.gameDao.findAll();
	}

	public List<Fgame> list(int firstResult, int maxResults,
			String filter,boolean isFY) {
		List<Fgame> all = this.gameDao.list(firstResult, maxResults, filter,isFY);
		return all;
	}
}