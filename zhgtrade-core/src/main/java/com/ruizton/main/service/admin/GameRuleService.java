package com.ruizton.main.service.admin;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ruizton.main.Enum.GameLogStatusEnum;
import com.ruizton.main.dao.FadminDAO;
import com.ruizton.main.dao.FgamelogDAO;
import com.ruizton.main.dao.FgameruleDAO;
import com.ruizton.main.dao.FmessageDAO;
import com.ruizton.main.dao.FuserDAO;
import com.ruizton.main.dao.FvirtualwalletDAO;
import com.ruizton.main.model.Fgamelog;
import com.ruizton.main.model.Fgamerule;
import com.ruizton.main.model.Fmessage;
import com.ruizton.main.model.Fuser;
import com.ruizton.main.model.Fvirtualwallet;
import com.ruizton.util.Utils;

@Service
public class GameRuleService {
	@Autowired
	private FgameruleDAO gameruleDAO;
	@Autowired
	private FgamelogDAO gamelogDAO;
	@Autowired
	private FuserDAO userDAO;
	@Autowired
	private FmessageDAO messageDAO;
	@Autowired
	private FvirtualwalletDAO virtualwalletDAO;
	@Autowired
	private FadminDAO adminDAO;

	public Fgamerule findById(int id) {
		return this.gameruleDAO.findById(id);
	}

	public void saveObj(Fgamerule obj) {
		this.gameruleDAO.save(obj);
	}

	public void deleteObj(int id) {
		Fgamerule obj = this.gameruleDAO.findById(id);
		this.gameruleDAO.delete(obj);
	}

	public void updateObj(Fgamerule obj) {
		this.gameruleDAO.attachDirty(obj);
	}

	public List<Fgamerule> findByProperty(String name, Object value) {
		return this.gameruleDAO.findByProperty(name, value);
	}

	public List<Fgamerule> findAll() {
		return this.gameruleDAO.findAll();
	}

	public List<Fgamerule> list(int firstResult, int maxResults,
			String filter,boolean isFY) {
		List<Fgamerule> all = this.gameruleDAO.list(firstResult, maxResults, filter,isFY);
		return all;
	}
	
	public double getPlanHarvestQty(Fuser user,int groupqty) {
		String filter = "where flevel="+user.getFscore().getFfarmlevel();
		List<Fgamerule> all = this.gameruleDAO.list(0, 0, filter, false);
		if(all == null || all.size() == 0) return 0d;
		Fgamerule gamerule = all.get(0);
		double canZdtimes = gamerule.getFcanZdtimes().doubleValue();
		double harvestQty = gamerule.getFharvestQty().doubleValue();
		double harvest = 0d ;
		harvest = Utils.getDouble(harvestQty*canZdtimes*groupqty,4);
		return harvest ;
	}
	
	public double getActualHarvestQty(Fgamelog gamelog,int groupqty) {
		double harvestRate = 1d ;//收获率
		int gameRuleId = gamelog.getFgamerule().getFid();
		Fgamerule gamerule = this.gameruleDAO.findById(gameRuleId);
		Timestamp nowTime = Utils.getTimestamp();
		Timestamp startTime = gamelog.getFstartTime();
		double canZdtimes = gamerule.getFcanZdtimes().doubleValue();
		double harvestQty = gamerule.getFharvestQty().doubleValue();
		double harvest = 0d ;
		double zdTimeRate = 1.0d*(nowTime.getTime()- startTime.getTime())/(canZdtimes*3600*1000L) ;
		harvest = Utils.getDouble(harvestQty*canZdtimes*groupqty*( zdTimeRate<1d?zdTimeRate:1 ) * harvestRate,4);
		return harvest ;
	}
	
	public void updateLotter(Fuser user,Fvirtualwallet virtualwallet,Fmessage message) {
		try {
			this.userDAO.attachDirty(user);
			this.messageDAO.save(message);
			this.virtualwalletDAO.attachDirty(virtualwallet);
		} catch (Exception e) {
			throw new RuntimeException();
		}
	}
}