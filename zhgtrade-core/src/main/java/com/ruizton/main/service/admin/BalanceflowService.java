package com.ruizton.main.service.admin;

import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ruizton.main.dao.FbalanceflowDAO;
import com.ruizton.main.dao.FbalancelogDAO;
import com.ruizton.main.dao.FuserDAO;
import com.ruizton.main.dao.FvirtualwalletDAO;
import com.ruizton.main.dao.FwalletDAO;
import com.ruizton.main.model.Fbalanceflow;
import com.ruizton.main.model.Fbalancelog;
import com.ruizton.main.model.Fuser;
import com.ruizton.main.model.Fvirtualwallet;
import com.ruizton.main.model.Fwallet;

@Service
public class BalanceflowService {
	@Autowired
	private FvirtualwalletDAO virtualwalletDAO;
	@Autowired
	private FuserDAO userDAO;
	@Autowired
	private FbalancelogDAO balancelogDAO;
	@Autowired
	private FbalanceflowDAO balanceflowDAO;
	@Autowired
	private FwalletDAO fwalletDAO;

	public Fbalanceflow findById(int id) {
		return this.balanceflowDAO.findById(id);
	}

	public void saveObj(Fbalanceflow obj) {
		this.balanceflowDAO.save(obj);
	}

	public void deleteObj(int id) {
		Fbalanceflow obj = this.balanceflowDAO.findById(id);
		this.balanceflowDAO.delete(obj);
	}

	public void updateObj(Fbalanceflow obj) {
		this.balanceflowDAO.attachDirty(obj);
	}

	public List<Fbalanceflow> findByProperty(String name, Object value) {
		return this.balanceflowDAO.findByProperty(name, value);
	}

	public List<Fbalanceflow> findAll() {
		return this.balanceflowDAO.findAll();
	}

	public List<Fbalanceflow> list(int firstResult, int maxResults,
			String filter,boolean isFY) {
		List<Fbalanceflow> list = this.balanceflowDAO.list(firstResult, maxResults, filter,isFY);
		return list;
	}
	
	public void updateBalanceFlowIn(Fvirtualwallet virtualwallet,
			Fuser fuser,Fbalancelog fbalancelog,Fbalanceflow fbalanceflow) {
          try {
			  this.virtualwalletDAO.attachDirty(virtualwallet);
			  this.userDAO.attachDirty(fuser);
			  this.balanceflowDAO.save(fbalanceflow);
			  this.balancelogDAO.save(fbalancelog);
		} catch (Exception e) {
			throw new RuntimeException();
		}
	}
	
	public void updateBalanceFlowIn(Fwallet fwallet,
			Fuser fuser,Fbalancelog fbalancelog,Fbalanceflow fbalanceflow) {
          try {
			  this.fwalletDAO.attachDirty(fwallet);
			  this.userDAO.attachDirty(fuser);
			  this.balanceflowDAO.save(fbalanceflow);
			  this.balancelogDAO.save(fbalancelog);
		} catch (Exception e) {
			throw new RuntimeException();
		}
	}
	
	public void updateBalanceFlowOut(Fuser fuser,List allLog,Fbalanceflow fbalanceflow,Fvirtualwallet fvirtualwallet) {
          try {
			  this.userDAO.attachDirty(fuser);
			  this.balanceflowDAO.save(fbalanceflow);
			  this.virtualwalletDAO.attachDirty(fvirtualwallet);
			  if(allLog.size() >0){
				    Iterator it = allLog.iterator();
				    while(it.hasNext()){
				    	Fbalancelog fbalancelog = (Fbalancelog)it.next();
					    this.balancelogDAO.attachDirty(fbalancelog);
				    }
			  }
		} catch (Exception e) {
			throw new RuntimeException();
		}
	}
	
	public void updateBalanceFlowOut(Fuser fuser,List allLog,Fbalanceflow fbalanceflow,Fwallet fwallet) {
          try {
			  this.userDAO.attachDirty(fuser);
			  this.balanceflowDAO.save(fbalanceflow);
			  this.fwalletDAO.attachDirty(fwallet);
			  if(allLog.size() >0){
				    Iterator it = allLog.iterator();
				    while(it.hasNext()){
				    	Fbalancelog fbalancelog = (Fbalancelog)it.next();
					    this.balancelogDAO.attachDirty(fbalancelog);
				    }
			  }
		} catch (Exception e) {
			throw new RuntimeException();
		}
	}
}