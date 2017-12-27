package com.ruizton.main.service.front;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ruizton.main.Enum.VirtualCoinTypeStatusEnum;
import com.ruizton.main.dao.FlendentrustDAO;
import com.ruizton.main.dao.FlendflowlogDAO;
import com.ruizton.main.dao.FlendruleDAO;
import com.ruizton.main.dao.FlendsystemargsDAO;
import com.ruizton.main.dao.FvirtualcointypeDAO;
import com.ruizton.main.dao.FvirtualwalletDAO;
import com.ruizton.main.dao.FwalletDAO;
import com.ruizton.main.model.Flendentrust;
import com.ruizton.main.model.Flendflowlog;
import com.ruizton.main.model.Flendrule;
import com.ruizton.main.model.Flendsystemargs;
import com.ruizton.main.model.Fvirtualcointype;
import com.ruizton.main.model.Fvirtualwallet;
import com.ruizton.main.model.Fwallet;
import com.ruizton.main.service.BaseService;

@Service
public class FrontLendService extends BaseService {
	@Autowired
	private FlendsystemargsDAO flendsystemargsDAO ;
	@Autowired
	private FvirtualcointypeDAO fvirtualcointypeDAO ;
	@Autowired
	private FwalletDAO fwalletDAO ;
	@Autowired
	private FvirtualwalletDAO fvirtualwalletDAO ;
	@Autowired
	private FlendruleDAO flendruleDAO ;
	@Autowired
	private FlendflowlogDAO lendflowlogDAO;
	@Autowired
	private FlendentrustDAO flendentrustDAO ;
	
	public Flendsystemargs findFlendsystemargsById(int id){
		return this.flendsystemargsDAO.findById(id) ;
	}
	
	//可以借贷的虚拟币
	public List<Fvirtualcointype> findCanLendFvirtualcointypes(){
		List<Fvirtualcointype> fvirtualcointypes = this.fvirtualcointypeDAO.findByParam(0, 0, " where fstatus="+VirtualCoinTypeStatusEnum.Normal+" ", false, "Fvirtualcointype") ;
		return fvirtualcointypes ;
	}
	
	public void updateWallet(Fwallet fwallet,Flendflowlog lendflowlog){
		try {
			this.fwalletDAO.attachDirty(fwallet) ;
			this.lendflowlogDAO.save(lendflowlog);
		} catch (Exception e) {
			throw new RuntimeException();
		}
	}
	
	public void updateVirtualWallet(Fvirtualwallet fvirtualwallet,Flendflowlog lendflowlog){
		try {
			this.fvirtualwalletDAO.attachDirty(fvirtualwallet) ;
			this.lendflowlogDAO.save(lendflowlog);
		} catch (Exception e) {
			throw new RuntimeException();
		}
	}
	
	public List<Flendrule> findFlendrule(int type){
		return this.flendruleDAO.findByProperty("ftype", type) ;
	}
	
	public Flendrule findFlendruleById(int fid){
		return this.flendruleDAO.findById(fid) ;
	}
	
	public void updateFlendEntrustFwallet(Flendentrust flendentrust,Fwallet fwallet){
		try {
			this.flendentrustDAO.attachDirty(flendentrust) ;
			this.fwalletDAO.attachDirty(fwallet) ;
		} catch (Exception e) {
			throw new RuntimeException();
		}
	}
	
	public void updateFlendEntrustFvirtualwallet(Flendentrust flendentrust,Fvirtualwallet fvirtualwallet){
		try {
			this.flendentrustDAO.attachDirty(flendentrust) ;
			this.fvirtualwalletDAO.attachDirty(fvirtualwallet) ;
		} catch (Exception e) {
			throw new RuntimeException();
		}
	}
	
	public List<Flendentrust> findFlendentrusts(int firstResult, int maxResults, String filter,boolean isFY,String CLASS){
		return this.flendentrustDAO.findByParam(firstResult, maxResults, filter, isFY, CLASS) ;
	}
	
	public int findFlendentrustsCount(String filter,String CLASS){
		return this.flendentrustDAO.findByParamCount(filter, CLASS) ;
	}
	
}
