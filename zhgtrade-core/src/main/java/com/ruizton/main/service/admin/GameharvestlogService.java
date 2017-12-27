package com.ruizton.main.service.admin;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ruizton.main.dao.FgameharvestlogDAO;
import com.ruizton.main.dao.FgamelogDAO;
import com.ruizton.main.dao.FgameoperatelogDAO;
import com.ruizton.main.dao.FmessageDAO;
import com.ruizton.main.dao.FuserDAO;
import com.ruizton.main.dao.FvirtualwalletDAO;
import com.ruizton.main.model.Fgameharvestlog;
import com.ruizton.main.model.Fgamelog;
import com.ruizton.main.model.Fgameoperatelog;
import com.ruizton.main.model.Fmessage;
import com.ruizton.main.model.Fuser;
import com.ruizton.main.model.Fvirtualwallet;

@Service
public class GameharvestlogService {
	@Autowired
	private FgameharvestlogDAO gameharvestlogDAO;
	@Autowired
	private FgamelogDAO gamelogDAO;
	@Autowired
	private FmessageDAO messageDAO;
	@Autowired
	private FgameoperatelogDAO gameoperatelogDAO;
	@Autowired
	private FvirtualwalletDAO virtualwalletDAO;
	@Autowired
	private FuserDAO userDAO;

	public Fgameharvestlog findById(int id) {
		return this.gameharvestlogDAO.findById(id);
	}

	public void saveObj(Fgameharvestlog obj) {
		this.gameharvestlogDAO.save(obj);
	}

	public void deleteObj(int id) {
		Fgameharvestlog obj = this.gameharvestlogDAO.findById(id);
		this.gameharvestlogDAO.delete(obj);
	}

	public void updateObj(Fgameharvestlog obj) {
		this.gameharvestlogDAO.attachDirty(obj);
	}

	public List<Fgameharvestlog> findByProperty(String name, Object value) {
		return this.gameharvestlogDAO.findByProperty(name, value);
	}

	public List<Fgameharvestlog> findAll() {
		return this.gameharvestlogDAO.findAll();
	}

	public List<Fgameharvestlog> list(int firstResult, int maxResults,
			String filter,boolean isFY) {
		return this.gameharvestlogDAO.list(firstResult, maxResults, filter,isFY);
	}
	
	public void updateHarvestLog(Fuser fuser,Fgamelog gamelog,List<Fgameharvestlog> harvestLogs,
			Fgameoperatelog gameoperatelog1,Fgameoperatelog gameoperatelog2) {
		try {
			this.userDAO.attachDirty(fuser);
			this.gamelogDAO.attachDirty(gamelog);
			for(int i=0;i<harvestLogs.size();i++){
				this.gameharvestlogDAO.save(harvestLogs.get(i));
			}
			this.gameoperatelogDAO.save(gameoperatelog1);
			this.gameoperatelogDAO.save(gameoperatelog2);
		} catch (Exception e) {
			throw new RuntimeException();
		}
	}
	
	public double getFrozenQty(String field,String filter,boolean isToday) {
		return this.gameharvestlogDAO.getFrozenQty(field, filter,isToday);
	}
}