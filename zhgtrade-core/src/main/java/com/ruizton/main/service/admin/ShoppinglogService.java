package com.ruizton.main.service.admin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ruizton.main.dao.FgoodsDAO;
import com.ruizton.main.dao.FmessageDAO;
import com.ruizton.main.dao.FscoreDAO;
import com.ruizton.main.dao.FshoppinglogDAO;
import com.ruizton.main.dao.FuserDAO;
import com.ruizton.main.dao.FvirtualwalletDAO;
import com.ruizton.main.dao.FwalletDAO;
import com.ruizton.main.model.Fgoods;
import com.ruizton.main.model.Fmessage;
import com.ruizton.main.model.Fscore;
import com.ruizton.main.model.Fshoppinglog;
import com.ruizton.main.model.Fuser;
import com.ruizton.main.model.Fvirtualwallet;
import com.ruizton.main.model.Fwallet;

@Service
public class ShoppinglogService {
	@Autowired
	private FshoppinglogDAO shoppinglogDAO;
	@Autowired
	private FwalletDAO fwalletDAO;
	@Autowired
	private FuserDAO fuserDAO;
	@Autowired
	private FgoodsDAO goodsDAO;
	@Autowired
	private FvirtualwalletDAO virtualwalletDAO;
	@Autowired
	private FscoreDAO scoreDAO;
	@Autowired
	private FmessageDAO messageDAO;

	public Fshoppinglog findById(int id) {
		return this.shoppinglogDAO.findById(id);
	}

	public void saveObj(Fshoppinglog obj) {
		this.shoppinglogDAO.save(obj);
	}

	public void deleteObj(int id) {
		Fshoppinglog obj = this.shoppinglogDAO.findById(id);
		this.shoppinglogDAO.delete(obj);
	}

	public void updateObj(Fshoppinglog obj) {
		this.shoppinglogDAO.attachDirty(obj);
	}

	public List<Fshoppinglog> findByProperty(String name, Object value) {
		return this.shoppinglogDAO.findByProperty(name, value);
	}

	public List<Fshoppinglog> findAll() {
		return this.shoppinglogDAO.findAll();
	}

	public List<Fshoppinglog> list(int firstResult, int maxResults,
			String filter,boolean isFY) {
		return this.shoppinglogDAO.list(firstResult, maxResults, filter,isFY);
	}
	
	public Fshoppinglog checkMessage(Fshoppinglog fshoppinglog) {
		Fshoppinglog shoppinglog = null;
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("fpasswordA", fshoppinglog.getFpasswordA());
			map.put("fpasswordB", fshoppinglog.getFpasswordB());
			List<Fshoppinglog> all = this.shoppinglogDAO.findByMap(map);
			if(all != null && all.size() >0 && all.get(0) != null){
				shoppinglog = all.get(0);
			}
		} catch (Exception e) {
			e.printStackTrace();
			shoppinglog = null;
			throw new RuntimeException();
		}
		return shoppinglog;
	}
	
	public void updateUseLog(Fuser fuser,Fshoppinglog fshoppinglog) {
		try {
			this.shoppinglogDAO.attachDirty(fshoppinglog);
			this.fuserDAO.attachDirty(fuser);
		} catch (Exception e) {
			throw new RuntimeException();
		}

	}
	
	public void updateBuylog(Fwallet fwallet,Fwallet fwallet_supplier,
			Fgoods fgoods,Fshoppinglog shoppinglog) {
		try {
			this.fwalletDAO.attachDirty(fwallet);
			this.fwalletDAO.attachDirty(fwallet_supplier);
			this.goodsDAO.attachDirty(fgoods);
			this.shoppinglogDAO.save(shoppinglog);
		} catch (Exception e) {
			throw new RuntimeException();
		}
	}
	
	public void updateBuylog(Fscore fscore,Fgoods fgoods,Fshoppinglog shoppinglog) {
		try {
			this.scoreDAO.attachDirty(fscore);
			this.goodsDAO.attachDirty(fgoods);
			this.shoppinglogDAO.save(shoppinglog);
		} catch (Exception e) {
			throw new RuntimeException();
		}
	}
	
	public void updateCancellog(Fwallet fwallet,Fwallet fwallet_supplier,Fshoppinglog shoppinglog) {
		try {
			this.fwalletDAO.attachDirty(fwallet);
			this.fwalletDAO.attachDirty(fwallet_supplier);
			this.shoppinglogDAO.attachDirty(shoppinglog);
		} catch (Exception e) {
			throw new RuntimeException();
		}
	}
	
	public void updateCancellog(Fvirtualwallet virtualwallet,Fvirtualwallet virtualwallet_supplier,
			Fshoppinglog shoppinglog) {
		try {
			this.virtualwalletDAO.attachDirty(virtualwallet);
			this.virtualwalletDAO.attachDirty(virtualwallet_supplier);
			this.shoppinglogDAO.attachDirty(shoppinglog);
		} catch (Exception e) {
			throw new RuntimeException();
		}
	}
	
	public void updateBuylog(Fvirtualwallet virtualwallet,Fvirtualwallet virtualwallet_supplier,
			Fgoods fgoods,Fshoppinglog shoppinglog) {
		try {
			this.virtualwalletDAO.attachDirty(virtualwallet);
			this.virtualwalletDAO.attachDirty(virtualwallet_supplier);
			this.goodsDAO.attachDirty(fgoods);
			this.shoppinglogDAO.save(shoppinglog);
		} catch (Exception e) {
			throw new RuntimeException();
		}
	}
	
	public void updateSendObj(Fshoppinglog obj,Fmessage message,Fwallet fwallet) {
		try {
			this.messageDAO.save(message);
			this.fwalletDAO.attachDirty(fwallet);
			this.shoppinglogDAO.attachDirty(obj);
		} catch (Exception e) {
			throw new RuntimeException();
		}
	}
	
	public double getBuyQty(int goodsid,int userid) {
		return this.shoppinglogDAO.getBuyQty(goodsid,userid);
	}
}