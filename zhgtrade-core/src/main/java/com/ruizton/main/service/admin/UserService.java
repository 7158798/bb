package com.ruizton.main.service.admin;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ruizton.main.dao.FcapitaloperationDAO;
import com.ruizton.main.dao.FgameoperatelogDAO;
import com.ruizton.main.dao.FuserDAO;
import com.ruizton.main.dao.FvouchersDAO;
import com.ruizton.main.dao.FwalletDAO;
import com.ruizton.main.model.Fcapitaloperation;
import com.ruizton.main.model.Fuser;
import com.ruizton.main.model.Fvouchers;
import com.ruizton.main.model.Fwallet;

@Service
public class UserService {
	@Autowired
	private FuserDAO fuserDAO;
	@Autowired
	private FvouchersDAO vouchersDAO;
	@Autowired
	private FwalletDAO fwalletDAO;
	@Autowired
	private FcapitaloperationDAO fcapitaloperationDAO;

	public Fuser findById(int id) {
		Fuser fuser = this.fuserDAO.findById(id);
		if (fuser.getFwallet() != null) {
			fuser.getFwallet().getFfrozenRmb();
		}
		if (fuser.getFscore() != null) {
			fuser.getFscore().getFlevel();
		}
		if (fuser.getfIntroUser_id() != null) {
			fuser.getfIntroUser_id().getFlastLoginTime();
		}
		return fuser;
	}

	public void saveObj(Fuser obj) {
		this.fuserDAO.save(obj);
	}

	public void deleteObj(int id) {
		Fuser obj = this.fuserDAO.findById(id);
		this.fuserDAO.delete(obj);
	}

	public void updateObj(Fuser obj) {
		this.fuserDAO.attachDirty(obj);
	}

	public List<Fuser> findByProperty(String name, Object value) {
		return this.fuserDAO.findByProperty(name, value);
	}

	public List<Fuser> findAll() {
		return this.fuserDAO.findAll();
	}

	public List<Fuser> list(int firstResult, int maxResults, String filter,
			boolean isFY) {
		List<Fuser> all = this.fuserDAO.list(firstResult, maxResults, filter,
				isFY);
		for (Fuser fuser : all) {
			if (fuser.getFwallet() != null) {
				fuser.getFwallet().getFfrozenRmb();
			}
			if (fuser.getFscore() != null) {
				fuser.getFscore().getFlevel();
			}
			if(fuser.getfIntroUser_id() != null){
		        fuser.getfIntroUser_id().getFnickName();
			}
		}
		return all;
	}
	
	public List<Fuser> specialList(int firstResult, int maxResults, String filter,
			boolean isFY,String startDate) {
		List<Fuser> all = this.fuserDAO.list(firstResult, maxResults, filter,
				isFY);
		for (Fuser fuser : all) {
			if (fuser.getFwallet() != null) {
				fuser.getFwallet().getFfrozenRmb();
			}
			if (fuser.getFscore() != null) {
				fuser.getFscore().getFlevel();
			}
			if(fuser.getfIntroUser_id() != null){
		        fuser.getfIntroUser_id().getFnickName();
			}
		}
		return all;
	}
	
	public List<Fuser> simpleList(int firstResult, int maxResults, String filter,
			boolean isFY) {
		List<Fuser> all = this.fuserDAO.list(firstResult, maxResults, filter,
				isFY);
		return all;
	}

	public List findByDate(String propertyName, Date value) {
		return this.fuserDAO.findByDate(propertyName, value);
	}

	public List getUserGroup(String filter) {
		return this.fuserDAO.getUserGroup(filter);
	}

	public List<Fuser> listUserForAudit(int firstResult, int maxResults,
			String filter, boolean isFY) {
		return this.fuserDAO.listUserForAudit(firstResult, maxResults, filter,
				isFY);
	}
	
	public List listUsers(int firstResult, int maxResults,
			String filter, boolean isFY) {
		return this.fuserDAO.listUsers(firstResult, maxResults, filter,
				isFY);
	}
	
	public List getAllUserInfo(int firstResult, int maxResults,
 			String filter,boolean isFY) {
		return this.fuserDAO.getAllUserInfo(firstResult, maxResults, filter, isFY);
	}
	
	public String getIntrolString(int fuserid) {
		return this.fuserDAO.getIntrolString(fuserid);
	}
	
	public Map getTotalBalanceUser() {
		return this.fuserDAO.getTotalBalanceUser();
	}
	
	public List<Object[]> getRankingMap(){
		return this.fuserDAO.getRankingMap();
	}
	
	public void updateUserVouchers(Fvouchers fvouchers,Fwallet fwallet,Fcapitaloperation fcapitaloperation) {
		try {
			this.vouchersDAO.attachDirty(fvouchers);
			this.fwalletDAO.attachDirty(fwallet);
			this.fcapitaloperationDAO.attachDirty(fcapitaloperation);
		} catch (Exception e) {
			throw new RuntimeException();
		}
	}
	
	public int getMaxGlobalUserId(){ 
		return fuserDAO.findMaxGlobalUserId();
	}
	
}