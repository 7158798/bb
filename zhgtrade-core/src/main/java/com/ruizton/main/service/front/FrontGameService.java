package com.ruizton.main.service.front;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ruizton.main.dao.FgameDAO;
import com.ruizton.main.dao.FgamelogDAO;
import com.ruizton.main.dao.FgameoperatelogDAO;
import com.ruizton.main.dao.FgameruleDAO;
import com.ruizton.main.dao.FintrolinfoDAO;
import com.ruizton.main.dao.FmessageDAO;
import com.ruizton.main.dao.FscoreDAO;
import com.ruizton.main.dao.FuserDAO;
import com.ruizton.main.dao.FvirtualwalletDAO;
import com.ruizton.main.dao.FwalletDAO;
import com.ruizton.main.model.Fgame;
import com.ruizton.main.model.Fgamelog;
import com.ruizton.main.model.Fgameoperatelog;
import com.ruizton.main.model.Fgamerule;
import com.ruizton.main.model.Fintrolinfo;
import com.ruizton.main.model.Fmessage;
import com.ruizton.main.model.Fscore;
import com.ruizton.main.model.Fuser;
import com.ruizton.main.model.Fvirtualwallet;
import com.ruizton.main.model.Fwallet;
import com.ruizton.main.service.BaseService;

@Service
public class FrontGameService extends BaseService {
	@Autowired
	private FgameDAO fgameDAO ;
	@Autowired
	private FgamelogDAO fgamelogDAO ;
	@Autowired
	private FgameruleDAO fgameruleDAO ;
	@Autowired
	private FgameoperatelogDAO gameoperatelogDAO ;
	@Autowired
	private FvirtualwalletDAO fvirtualwalletDAO ;
	@Autowired
	private FuserDAO fuserDAO ;
	@Autowired
	private FmessageDAO messageDAO;
	@Autowired
	private FscoreDAO fscoreDAO ;
	@Autowired
	private FintrolinfoDAO introlinfoDAO;
	@Autowired
	private FwalletDAO fwalletDAO;
	
	public Fgame findGameById(int id){
		return this.fgameDAO.findById(id) ;
	}
	
	public Fgame findFirstGame(){
		Fgame fgame = this.fgameDAO.findById(1);
		return fgame ;
	}
	
	public List<Fgame> findAllGame(){
		List<Fgame> fgames = this.fgameDAO.findByParam(0, 0, " order by fid asc ", false, "Fgame") ;
		return fgames ;
	}
	
	
	public List<Fgamerule> findAllGamerule(){
		List<Fgamerule> fgamerules = this.fgameruleDAO.findByParam(0, 0, " order by flevel asc ", false, "Fgamerule") ;
		return fgamerules ;
	}
	
	public List<Fgamelog> findGameLog(Fuser fuser,int firstResult,int maxResult){
		List<Fgamelog> fgamelogs = this.fgamelogDAO.findByParam(firstResult, maxResult, " where fuser.fid="+fuser.getFid()+" order by fid desc ", true, "Fgamelog") ;
		return fgamelogs ;
	}
	public int findGameLogCount(Fuser fuser){
		return this.fgamelogDAO.findByParamCount(" where fuser.fid="+fuser.getFid()+" order by fid desc ", "Fgamelog") ;
	}
	
	public Fgamerule findGameRule(Fgame fgame,Fuser fuser){
		Fgamerule fgamerule = null ;
		List<Fgamerule> fgamerules = this.fgameruleDAO.findByParam(0, 1, " where flevel="+fuser.getFscore().getFfarmlevel(), true, "Fgamerule") ;
		if(fgamerules.size()>0){
			fgamerule = fgamerules.get(0) ;
		}
		return fgamerule ;
	}
	
	public Fgamerule findGameRuleUp(Fgame fgame,Fuser fuser){
		Fgamerule fgamerule = null ;
		List<Fgamerule> fgamerules = this.fgameruleDAO.findByParam(0, 1, " where flevel="+(fuser.getFscore().getFfarmlevel()+1), true, "Fgamerule") ;
		if(fgamerules.size()>0){
			fgamerule = fgamerules.get(0) ;
		}
		return fgamerule ;
	}
	
	public void saveGameRule(Fgamerule fgamerule){
		this.fgameruleDAO.save(fgamerule) ;
	}
	
	public void saveGameLog(Fgamelog fgamelog,Fvirtualwallet fvirtualwallet,Fgameoperatelog gameoperatelog,Fuser fuser){
		try {
			this.fvirtualwalletDAO.attachDirty(fvirtualwallet) ;
			this.fgamelogDAO.save(fgamelog) ;
			this.gameoperatelogDAO.save(gameoperatelog);
			this.fuserDAO.attachDirty(fuser);
		} catch (Exception e) {
			throw new RuntimeException();
		}
	}
	
	public void updateHarvest(Fgamelog fgamelog,Fvirtualwallet fvirtualwallet,
			Fgameoperatelog gameoperatelog,Fvirtualwallet fvirtualwallet1,Fmessage message,Fintrolinfo introlinfo){
		try {
			this.fgamelogDAO.attachDirty(fgamelog) ;
			this.fvirtualwalletDAO.attachDirty(fvirtualwallet) ;
			this.fvirtualwalletDAO.attachDirty(fvirtualwallet1) ;
			this.messageDAO.save(message);
			this.gameoperatelogDAO.save(gameoperatelog);
			this.introlinfoDAO.save(introlinfo);
		} catch (Exception e) {
			throw new RuntimeException();
		}
	}
	
	public void updateHarvest(Fgamelog fgamelog,Fvirtualwallet fvirtualwallet,
			Fgameoperatelog gameoperatelog,Fvirtualwallet fintrolVirtualwallet,Fintrolinfo fintrolinfo){
		try {
			this.fgamelogDAO.attachDirty(fgamelog) ;
			this.fvirtualwalletDAO.attachDirty(fvirtualwallet) ;
			this.gameoperatelogDAO.save(gameoperatelog);
			if(fintrolVirtualwallet != null){
				this.fvirtualwalletDAO.attachDirty(fintrolVirtualwallet);
			}
			if(fintrolinfo != null){
				this.introlinfoDAO.save(fintrolinfo);
			}
		} catch (Exception e) {
			throw new RuntimeException();
		}
	}
	
	public void updateCleanGrass(Fgamelog fgamelog,Fvirtualwallet fvirtualwallet,Fgameoperatelog gameoperatelog){
		try {
			this.fgamelogDAO.attachDirty(fgamelog) ;
			this.fvirtualwalletDAO.attachDirty(fvirtualwallet);
			this.gameoperatelogDAO.save(gameoperatelog);
		} catch (Exception e) {
			throw new RuntimeException();
		}
	}
	
	public void updateUgrade(Fvirtualwallet fvirtualwallet,Fuser fuser,Fscore fscore,Fgameoperatelog gameoperatelog){
		try {
			this.fvirtualwalletDAO.attachDirty(fvirtualwallet) ;
			this.fuserDAO.attachDirty(fuser) ;
			this.fscoreDAO.attachDirty(fscore) ;
			this.gameoperatelogDAO.save(gameoperatelog);
		} catch (Exception e) {
			throw new RuntimeException();
		}
	}
	
	public void updateOpenMeadow(Fwallet fwallet,Fuser fuser){
		try {
			this.fwalletDAO.attachDirty(fwallet);
			this.fuserDAO.attachDirty(fuser) ;
		} catch (Exception e) {
			throw new RuntimeException();
		}
	}
	
	public void updateOpenMeadow(Fvirtualwallet virtualwallet1,Fvirtualwallet virtualwallet2,Fscore fscore){
		try {
			this.fvirtualwalletDAO.attachDirty(virtualwallet1);
			this.fvirtualwalletDAO.attachDirty(virtualwallet2);
			this.fscoreDAO.attachDirty(fscore);
		} catch (Exception e) {
			throw new RuntimeException();
		}
	}
	
	public void updateCloseMeadow(Fvirtualwallet virtualwallet1,Fscore fscore){
		try {
			this.fvirtualwalletDAO.attachDirty(virtualwallet1);
			this.fscoreDAO.attachDirty(fscore);
		} catch (Exception e) {
			throw new RuntimeException();
		}
	}
	
	public void updateOpenMeadow(Fuser fuser){
		try {
			this.fuserDAO.attachDirty(fuser) ;
		} catch (Exception e) {
			throw new RuntimeException();
		}
	}
}
