package com.ruizton.main.service.front;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ruizton.main.dao.FcoinvoteDAO;
import com.ruizton.main.dao.FcoinvotelogDAO;
import com.ruizton.main.dao.FvirtualwalletDAO;
import com.ruizton.main.model.Fcoinvote;
import com.ruizton.main.model.Fcoinvotelog;
import com.ruizton.main.model.Fvirtualwallet;
import com.ruizton.main.service.BaseService;

@Service
public class CoinVoteService extends BaseService {

	@Autowired
	private FcoinvoteDAO fcoinvoteDAO ;
	@Autowired
	private FcoinvotelogDAO fcoinvotelogDAO ;
	@Autowired
	private FvirtualwalletDAO fvirtualwalletDAO ;
	
	public void saveFcoinvote(Fcoinvote fcoinvote){
		this.fcoinvoteDAO.save(fcoinvote) ;
	}
	
	public Fcoinvote findFcoinvote(int fid){
		return this.fcoinvoteDAO.findById(fid) ;
	}
	
	public void updateFcoinvote(Fcoinvote fcoinvote){
		this.fcoinvoteDAO.attachDirty(fcoinvote) ;
	}
	
	public void deleteFcoinvote(Fcoinvote fcoinvote){
		this.fcoinvoteDAO.delete(fcoinvote) ;
	}
	
	public void updateFcoinvote(Fvirtualwallet fvirtualwallet,Fcoinvote fcoinvote,Fcoinvotelog fcoinvotelog){
		this.fvirtualwalletDAO.attachDirty(fvirtualwallet) ;
		this.fcoinvoteDAO.attachDirty(fcoinvote) ;
		this.fcoinvotelogDAO.save(fcoinvotelog) ;
	}
	
	
	
}
