package com.ruizton.main.service.admin;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ruizton.main.dao.FlendentrustDAO;
import com.ruizton.main.dao.FlendentrustfinishlogDAO;
import com.ruizton.main.dao.FlendflowlogDAO;
import com.ruizton.main.dao.FmessageDAO;
import com.ruizton.main.dao.FvirtualwalletDAO;
import com.ruizton.main.dao.FwalletDAO;
import com.ruizton.main.model.Flendentrust;
import com.ruizton.main.model.Flendentrustfinishlog;
import com.ruizton.main.model.Flendflowlog;
import com.ruizton.main.model.Fmessage;
import com.ruizton.main.model.Fuser;
import com.ruizton.main.model.Fvirtualwallet;
import com.ruizton.main.model.Fwallet;

@Service
public class LendEntrustFinishLogService {
	@Autowired
	private FlendentrustfinishlogDAO lendentrustfinishlogDAO;
	@Autowired
	private FmessageDAO messageDAO;
	@Autowired
	private FvirtualwalletDAO virtualwalletDAO;
	@Autowired
	private FwalletDAO walletDAO;
	@Autowired
	private FlendentrustDAO lendentrustDAO;
	@Autowired
	private FlendflowlogDAO lendflowlogDAO;

	public Flendentrustfinishlog findById(int id) {
		return this.lendentrustfinishlogDAO.findById(id);
	}

	public void saveObj(Flendentrustfinishlog obj) {
		this.lendentrustfinishlogDAO.save(obj);
	}

	public void deleteObj(int id) {
		Flendentrustfinishlog obj = this.lendentrustfinishlogDAO.findById(id);
		this.lendentrustfinishlogDAO.delete(obj);
	}

	public void updateObj(Flendentrustfinishlog obj) {
		this.lendentrustfinishlogDAO.attachDirty(obj);
	}

	public List<Flendentrustfinishlog> findByProperty(String name, Object value) {
		return this.lendentrustfinishlogDAO.findByProperty(name, value);
	}

	public List<Flendentrustfinishlog> findAll() {
		return this.lendentrustfinishlogDAO.findAll();
	}

	public List<Flendentrustfinishlog> list(int firstResult, int maxResults,
			String filter,boolean isFY) {
		return this.lendentrustfinishlogDAO.list(firstResult, maxResults, filter,isFY);
	}
	
	public void updateBackLog(Flendentrustfinishlog lendentrustfinishlog,Fwallet fwallet,
			Fmessage fmessage,Flendentrust lendentrust) {
		try {
			this.lendentrustfinishlogDAO.attachDirty(lendentrustfinishlog);
			this.walletDAO.attachDirty(fwallet);
			this.messageDAO.save(fmessage);
			this.lendentrustDAO.attachDirty(lendentrust);
		} catch (Exception e) {
			throw new RuntimeException();
		}
	}
	
	public void updateBackLog(Flendentrustfinishlog lendentrustfinishlog,Fvirtualwallet virtualwallet
			,Fmessage fmessage,Flendentrust lendentrust) {
		try {
			this.lendentrustfinishlogDAO.attachDirty(lendentrustfinishlog);
			this.virtualwalletDAO.attachDirty(virtualwallet);
			this.messageDAO.save(fmessage);
			this.lendentrustDAO.attachDirty(lendentrust);
		} catch (Exception e) {
			throw new RuntimeException();
		}
	}
	
	public void updateLendlog(Fvirtualwallet virtualwallet,Fmessage message,Flendflowlog lendflowlog) {
		try {
			this.virtualwalletDAO.attachDirty(virtualwallet);
			this.messageDAO.save(message);
			this.lendflowlogDAO.save(lendflowlog);
		} catch (Exception e) {
			throw new RuntimeException();
		}
	}
	
	public void updateLendlog(Fwallet wallet,Fmessage message,Flendflowlog lendflowlog) {
		try {
			this.walletDAO.attachDirty(wallet);
			this.messageDAO.save(message);
			this.lendflowlogDAO.save(lendflowlog);
		} catch (Exception e) {
			throw new RuntimeException();
		}
	}
	
	public double getTotalNoReturn(int lendId) {
		return this.lendentrustfinishlogDAO.getTotalNoReturn(lendId);
	}
	
	public List getAllBorrowUsers() {
		return this.lendentrustfinishlogDAO.getAllBorrowUsers();
	}
	
	public List getAllBorrowUsers(Fuser fuser,int cnyOrCoin) {
		return this.lendentrustfinishlogDAO.getAllBorrowUsers(fuser, cnyOrCoin);
	}
}