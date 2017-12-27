package com.ruizton.main.service.front;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ruizton.main.dao.AppDao;
import com.ruizton.main.dao.FandroidpushmentDAO;
import com.ruizton.main.model.Fandroidpushment;
import com.ruizton.main.model.Fuser;
import com.ruizton.main.service.BaseService;

@Service
public class FrontApiService extends BaseService {
	@Autowired
	private AppDao appDao ;
	@Autowired
	private FandroidpushmentDAO fandroidpushmentDAO ;
	
	public int findTradeRecordllCount(Fuser fuser){
		return this.appDao.findTradeRecordAllCount(fuser) ;
	}
	
	public List<Object[]> findTradeRecordAll(Fuser fuser,int firstResult,int maxResult,boolean isFY){
		return this.appDao.findTradeRecordAll( fuser, firstResult, maxResult, isFY) ;
	}
	
	public Fandroidpushment findFandroidpushment(String RegistrationID){
		List<Fandroidpushment> list = this.fandroidpushmentDAO.findByProperty("registrationId", RegistrationID) ;
		if(list.size()>0){
			return list.get(0) ;
		}
		return null ;
	}
	
	public void updateFandroidpushment(Fandroidpushment fandroidpushment){
		this.fandroidpushmentDAO.attachDirty(fandroidpushment) ;
	}
	public void saveFandroidpushment(Fandroidpushment fandroidpushment){
		this.fandroidpushmentDAO.save(fandroidpushment) ;
	}
	
}
