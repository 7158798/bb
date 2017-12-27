package com.ruizton.main.service.admin;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ruizton.main.dao.FsalescontractDAO;
import com.ruizton.main.dao.FvirtualwalletDAO;
import com.ruizton.main.model.Fsalescontract;
import com.ruizton.main.model.Fvirtualwallet;

@Service
public class SalescontractService {
	@Autowired
	private FsalescontractDAO salescontractDAO;
	@Autowired
	private FvirtualwalletDAO fvirtualwalletDAO;

	public Fsalescontract findById(int id) {
		Fsalescontract salescontract = this.salescontractDAO.findById(id);
		if(salescontract.getFuser() != null){
			salescontract.getFuser().getFaddress();
		}
		if(salescontract.getFsalespercent() != null){
			salescontract.getFsalespercent().getFlevel();
		}
		return salescontract;
	}

	public void saveObj(Fsalescontract obj) {
		this.salescontractDAO.save(obj);
	}

	public void deleteObj(int id) {
		Fsalescontract obj = this.salescontractDAO.findById(id);
		this.salescontractDAO.delete(obj);
	}

	public void updateObj(Fsalescontract obj) {
		this.salescontractDAO.attachDirty(obj);
	}

	public List<Fsalescontract> findByProperty(String name, Object value) {
		return this.salescontractDAO.findByProperty(name, value);
	}

	public List<Fsalescontract> findAll() {
		return this.salescontractDAO.findAll();
	}

	public List<Fsalescontract> list(int firstResult, int maxResults,
		String filter,boolean isFY) {
		List<Fsalescontract> all = this.salescontractDAO.list(firstResult, maxResults, filter,isFY);
		for (Fsalescontract fsalescontract : all) {
             if(fsalescontract.getFsalespercent() != null){
            	 fsalescontract.getFsalespercent().getFlevel();
             }
             if(fsalescontract.getFuser() != null){
               	 fsalescontract.getFuser().getFaddress();
             }
		}
		return all;
	}
	
	
	public void updateSalesman(Fvirtualwallet fvirtualwallet,Fsalescontract fsalescontract) {
	    try {
			this.fvirtualwalletDAO.attachDirty(fvirtualwallet);
			this.salescontractDAO.attachDirty(fsalescontract);
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}
}