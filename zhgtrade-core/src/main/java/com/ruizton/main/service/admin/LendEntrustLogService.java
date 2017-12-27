package com.ruizton.main.service.admin;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ruizton.main.dao.FlendentrustDAO;
import com.ruizton.main.dao.FlendentrustfinishlogDAO;
import com.ruizton.main.dao.FlendentrustlogDAO;
import com.ruizton.main.dao.FvirtualwalletDAO;
import com.ruizton.main.dao.FwalletDAO;
import com.ruizton.main.model.Flendentrust;
import com.ruizton.main.model.Flendentrustfinishlog;
import com.ruizton.main.model.Flendentrustlog;
import com.ruizton.main.model.Fvirtualwallet;
import com.ruizton.main.model.Fwallet;

@Service
public class LendEntrustLogService {
	@Autowired
	private FlendentrustlogDAO lendentrustlogDAO;
	@Autowired
	private FwalletDAO walletDAO;
	@Autowired
	private FvirtualwalletDAO virtualwalletDAO;
	@Autowired
	private FlendentrustfinishlogDAO lendentrustfinishlogDAO;
	@Autowired
	private FlendentrustDAO lendentrustDAO;

	public Flendentrustlog findById(int id) {
		return this.lendentrustlogDAO.findById(id);
	}

	public void saveObj(Flendentrustlog obj) {
		this.lendentrustlogDAO.save(obj);
	}

	public void deleteObj(int id) {
		Flendentrustlog obj = this.lendentrustlogDAO.findById(id);
		this.lendentrustlogDAO.delete(obj);
	}

	public void updateObj(Flendentrustlog obj) {
		this.lendentrustlogDAO.attachDirty(obj);
	}

	public List<Flendentrustlog> findByProperty(String name, Object value) {
		return this.lendentrustlogDAO.findByProperty(name, value);
	}

	public List<Flendentrustlog> findAll() {
		return this.lendentrustlogDAO.findAll();
	}

	public List<Flendentrustlog> list(int firstResult, int maxResults,
			String filter,boolean isFY) {
		return this.lendentrustlogDAO.list(firstResult, maxResults, filter,isFY);
	}
	
	public double getTotalBorrowAmtByParent(int parentId) {
		return this.lendentrustlogDAO.getTotalBorrowAmtByParent(parentId);
	}
	
	public void updateReturnLog(Flendentrustfinishlog lendentrustfinishlog,Flendentrustlog lendentrustlog
			,Fwallet wallet,Flendentrust borrowentrust) {
		try {
			this.lendentrustfinishlogDAO.save(lendentrustfinishlog);
			this.lendentrustlogDAO.attachDirty(lendentrustlog);
			this.walletDAO.attachDirty(wallet);
			this.lendentrustDAO.attachDirty(borrowentrust);
		} catch (Exception e) {
			throw new RuntimeException();
		}
	}
	
	public void updateReturnLog(Flendentrustfinishlog lendentrustfinishlog,Flendentrustlog lendentrustlog
			,Fvirtualwallet virtualwallet,Flendentrust borrowentrust) {
		try {
			this.lendentrustfinishlogDAO.save(lendentrustfinishlog);
			this.lendentrustlogDAO.attachDirty(lendentrustlog);
			this.virtualwalletDAO.attachDirty(virtualwallet);
			this.lendentrustDAO.attachDirty(borrowentrust);
		} catch (Exception e) {
			throw new RuntimeException();
		}
	}
	
	public void updateReturnLog(List<Flendentrustfinishlog> lendentrustfinishlogs,List<Flendentrustlog> lendentrustlogs
			,Fwallet wallet,Flendentrust borrowentrust) {
		try {
			for(int i=0;i<lendentrustfinishlogs.size();i++){
				this.lendentrustfinishlogDAO.save(lendentrustfinishlogs.get(i));
			}
			for(int i=0;i<lendentrustlogs.size();i++){
				this.lendentrustlogDAO.attachDirty(lendentrustlogs.get(i));
			}
			this.walletDAO.attachDirty(wallet);
			this.lendentrustDAO.attachDirty(borrowentrust);
		} catch (Exception e) {
			throw new RuntimeException();
		}
	}
	
	public void updateReturnLog(List<Flendentrustfinishlog> lendentrustfinishlogs,List<Flendentrustlog> lendentrustlogs
			,Fvirtualwallet virtualwallet,Flendentrust borrowentrust) {
		try {
			for(int i=0;i<lendentrustfinishlogs.size();i++){
				this.lendentrustfinishlogDAO.save(lendentrustfinishlogs.get(i));
			}
			for(int i=0;i<lendentrustlogs.size();i++){
				this.lendentrustlogDAO.attachDirty(lendentrustlogs.get(i));
			}
			this.virtualwalletDAO.attachDirty(virtualwallet);
			this.lendentrustDAO.attachDirty(borrowentrust);
		} catch (Exception e) {
			throw new RuntimeException();
		}
	}

}