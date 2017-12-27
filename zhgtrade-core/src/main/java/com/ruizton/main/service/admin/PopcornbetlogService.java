package com.ruizton.main.service.admin;

import java.util.List;
import java.util.Map;

import javax.management.RuntimeErrorException;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ruizton.main.dao.FactivityDAO;
import com.ruizton.main.dao.FmessageDAO;
import com.ruizton.main.dao.FpopcornDAO;
import com.ruizton.main.dao.FpopcornbetlogDAO;
import com.ruizton.main.dao.FuserDAO;
import com.ruizton.main.dao.FusersettingDAO;
import com.ruizton.main.dao.FvirtualwalletDAO;
import com.ruizton.main.model.Factivity;
import com.ruizton.main.model.Flotterylog;
import com.ruizton.main.model.Fmessage;
import com.ruizton.main.model.Fpopcorn;
import com.ruizton.main.model.Fpopcornbetlog;
import com.ruizton.main.model.Fuser;
import com.ruizton.main.model.Fusersetting;
import com.ruizton.main.model.Fvirtualwallet;

@Service
public class PopcornbetlogService {
	@Autowired
	private FpopcornbetlogDAO popcornbetlogDAO;
	@Autowired
	private FmessageDAO messageDAO;
	@Autowired
	private FuserDAO userDAO;
	@Autowired
	private FusersettingDAO usersettingDAO;
	@Autowired
	private FvirtualwalletDAO virtualwalletDAO;

	public Fpopcornbetlog findById(int id) {
		Fpopcornbetlog popcornbetlog = this.popcornbetlogDAO.findById(id);
		
		if(popcornbetlog.getFuser() != null){
			popcornbetlog.getFuser().getFapi();
		}
		if(popcornbetlog.getFvirtualcointypeId() != null){
			popcornbetlog.getFvirtualcointypeId().getFname();
		}
		return popcornbetlog;
	}

	public void saveObj(Fpopcornbetlog obj) {
		this.popcornbetlogDAO.save(obj);
	}

	public void deleteObj(int id) {
		Fpopcornbetlog obj = this.popcornbetlogDAO.findById(id);
		this.popcornbetlogDAO.delete(obj);
	}

	public void updateObj(Fpopcornbetlog obj) {
		this.popcornbetlogDAO.attachDirty(obj);
	}

	public List<Fpopcornbetlog> findByProperty(String name, Object value) {
		return this.popcornbetlogDAO.findByProperty(name, value);
	}

	public List<Fpopcornbetlog> findAll() {
		return this.popcornbetlogDAO.findAll();
	}

	public List<Fpopcornbetlog> list(int firstResult, int maxResults,
			String filter,boolean isFY) {
		List<Fpopcornbetlog> all = this.popcornbetlogDAO.list(firstResult, maxResults, filter,isFY);	
		for (Fpopcornbetlog fpopcornbetlog : all) {
			if(fpopcornbetlog.getFuser() != null){
				fpopcornbetlog.getFuser().getFemail();
			}
			if(fpopcornbetlog.getFpopcornlog() != null){
				fpopcornbetlog.getFpopcornlog().getFcreatetime();
			}
			if(fpopcornbetlog.getFvirtualcointypeId() != null){
				fpopcornbetlog.getFvirtualcointypeId().getFdescription();
			}
			
		}
		return all;
	}

	
	public List getTotalBet(String filter,String field) {
		return this.popcornbetlogDAO.getTotalBet(filter, field);
	}

	public void updatelog(Fmessage message,Fpopcornbetlog popcornbetlog,Fusersetting usersetting) {
		try {
			this.messageDAO.save(message);
			this.popcornbetlogDAO.attachDirty(popcornbetlog);
			this.usersettingDAO.attachDirty(usersetting);
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}
	
	public void updateDoBet(Fusersetting usersetting,Fuser fuser,Fpopcornbetlog popcornbetlog) {
		try {
			this.usersettingDAO.attachDirty(usersetting);
			this.userDAO.attachDirty(fuser);
			if(popcornbetlog.getFid() != null){
				this.popcornbetlogDAO.attachDirty(popcornbetlog);
			}else{
				this.popcornbetlogDAO.save(popcornbetlog);
			}
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}

	
	public void updateBetLog(int popcornId,int lastResulet1,int lastResulet2) {
		this.popcornbetlogDAO.updateBetLog(popcornId, lastResulet1, lastResulet2);
	}
	
	public void updateBetLostLog(int popcornId,int lastResulet1,int lastResulet2,double winAmt,double lostAmt) {
		this.popcornbetlogDAO.updateBetLostLog(popcornId, lastResulet1, lastResulet2,winAmt,lostAmt);
	}
	
	public void updateBetWinLog(int popcornId,int lastResulet1,int lastResulet2,double winAmt,double lostAmt) {
		this.popcornbetlogDAO.updateBetWinLog(popcornId, lastResulet1, lastResulet2,winAmt,lostAmt);
	}
	
	public void updateBetWinLog1(int popcornId,int lastResulet1,int lastResulet2,double lostAmt,double winAmt) {
		this.popcornbetlogDAO.updateBetWinLog1(popcornId, lastResulet1, lastResulet2, lostAmt, winAmt);
	}
	
	public void updateBetWinLog2(int popcornId,int lastResulet1,int lastResulet2,double lostAmt,double winAmt) {
		this.popcornbetlogDAO.updateBetWinLog2(popcornId, lastResulet1, lastResulet2, lostAmt, winAmt);
	}
	
	public void updateBetWinLog3(int popcornId,int lastResulet1,int lastResulet2,double lostAmt,double winAmt) {
		this.popcornbetlogDAO.updateBetWinLog3(popcornId, lastResulet1, lastResulet2, lostAmt, winAmt);
	}
	
	public List getTotalWin(boolean isTodady) {
		return this.popcornbetlogDAO.getTotalWin(isTodady);
	}
	
	public Map getTotalFees(boolean isToday) {
		return this.popcornbetlogDAO.getTotalFees(isToday);
	}
	
	public List getTotalBetById(int popcornlogId) {
		return this.popcornbetlogDAO.getTotalBetById(popcornlogId);
	}
	
	public List getTotalFeesById(int popcornlogId) {
		return this.popcornbetlogDAO.getTotalFeesById(popcornlogId);
	}
}