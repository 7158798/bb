package com.ruizton.main.service.front;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ruizton.main.dao.FhongbaoDAO;
import com.ruizton.main.dao.FhongbaoLogDAO;
import com.ruizton.main.dao.FmessageDAO;
import com.ruizton.main.dao.FvirtualwalletDAO;
import com.ruizton.main.model.Fhongbao;
import com.ruizton.main.model.FhongbaoLog;
import com.ruizton.main.model.Fmessage;
import com.ruizton.main.model.Fvirtualwallet;
import com.ruizton.main.service.BaseService;

@Service
public class FrontHongbaoService extends BaseService {

	@Autowired
	private FhongbaoDAO fhongbaoDAO ;
	@Autowired
	private FhongbaoLogDAO fhongbaoLogDAO ;
	@Autowired
	private FvirtualwalletDAO fvirtualwalletDAO ;
	@Autowired
	private FmessageDAO fmessageDAO ;
	
	public Fhongbao findFhongbaoById(int fid){
		return this.fhongbaoDAO.findById(fid) ;
	}
	
	//发红包
	public void updateFahongbao(
			Fvirtualwallet fvirtualwallet,
			Fhongbao fhongbao
			) throws Exception {
		try {
			this.fvirtualwalletDAO.attachDirty(fvirtualwallet) ;
			this.fhongbaoDAO.attachDirty(fhongbao) ;
		} catch (Exception e) {
			throw new RuntimeException();
		}
	}
	
	//抢红包
	public void updateGetHongbao(Fhongbao fhongbao,FhongbaoLog fhongbaoLog,Fvirtualwallet fvirtualwallet,Fmessage m1,Fmessage m2){
		try {
			this.fhongbaoDAO.attachDirty(fhongbao) ;
			this.fhongbaoLogDAO.save(fhongbaoLog) ;
			this.fvirtualwalletDAO.attachDirty(fvirtualwallet) ;
			
			this.fmessageDAO.save(m1) ;
			this.fmessageDAO.save(m2) ;
		} catch (Exception e) {
			throw new RuntimeException();
		}
	}
	
	public List<Fhongbao> listFhongbao(int firstResult, int maxResults, String filter,boolean isFY){
		List<Fhongbao> list = this.fhongbaoDAO.findByParam(firstResult, maxResults, filter, isFY, "Fhongbao") ;
		return list ;
	}
	public int listFhongbaoCount(String filter){
		return this.fhongbaoDAO.findByParamCount( filter , "Fhongbao") ;
	}
	
	
	public List<FhongbaoLog> listFhongbaoLog(int firstResult, int maxResults, String filter,boolean isFY){
		List<FhongbaoLog> list = this.fhongbaoDAO.findByParam(firstResult, maxResults, filter, isFY, "FhongbaoLog") ;
		return list ;
	}
	
	public int listFhongbaoLogCount( String filter ){
		return this.fhongbaoDAO.findByParamCount( filter ,"FhongbaoLog") ;
	}
	
	
}
