package com.ruizton.main.service.front;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ruizton.main.dao.UtilsDAO;
import com.ruizton.main.model.Fcoinvotelog;
import com.ruizton.main.model.Fuser;

@Service
public class UtilsService {

	@Autowired
	private UtilsDAO utilsDAO ;
	
	public List list(int firstResult, int maxResults, String filter, boolean isFY,Class c){
		return this.utilsDAO.findByParam(firstResult, maxResults, filter, isFY, c.getName()) ;
	}
	public int count(String filter, Class c){
		return this.utilsDAO.findByParamCount(filter, c.getName()) ;
	}
	public double sum(String filter, String field, Class c){
		return this.utilsDAO.sum(filter, field, c) ;
	}
	
	public List<Fcoinvotelog> list2(int firstResult, int maxResults, String filter, boolean isFY,Class c){
		 List<Fcoinvotelog> list = this.utilsDAO.findByParam(firstResult, maxResults, filter, isFY, c.getName()) ;
		 for (Fcoinvotelog fcoinvotelog : list) {
			fcoinvotelog.getFcoinvote().getFcnName() ;
			fcoinvotelog.getFuser().getFnickName() ;
		}
		 return list ;
	}
}
