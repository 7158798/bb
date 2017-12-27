package com.ruizton.main.service.front;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ruizton.main.dao.FaboutDAO;
import com.ruizton.main.dao.FarticleDAO;
import com.ruizton.main.dao.FarticletypeDAO;
import com.ruizton.main.dao.FentrustlogDAO;
import com.ruizton.main.dao.FfriendlinkDAO;
import com.ruizton.main.dao.FperiodDAO;
import com.ruizton.main.model.Fabout;
import com.ruizton.main.model.Farticle;
import com.ruizton.main.model.Farticletype;
import com.ruizton.main.model.Fentrustlog;
import com.ruizton.main.model.Ffriendlink;
import com.ruizton.main.model.Fperiod;
import com.ruizton.main.model.Fvirtualcointype;

@Service
public class FrontOthersService {
	@Autowired
	private FfriendlinkDAO ffriendlinkDAO ;
	@Autowired
	private FarticleDAO farticleDAO ;
	@Autowired
	private FaboutDAO faboutDAO ;
	@Autowired
	private FarticletypeDAO farticletypeDAO ;
	@Autowired
	private FperiodDAO fperiodDAO ;
	@Autowired
	private FentrustlogDAO fentrustlogDAO ;
	
	public List<Ffriendlink> findFfriendLink(int type){
		return this.ffriendlinkDAO.findByProperty("ftype",type) ;
	}
	
	public Fabout findFabout(int fid){
		return this.faboutDAO.findById(fid) ;
	}
	
	public List<Fabout> findFaboutAll(){
		return this.faboutDAO.findAll() ;
	}
	
	public List<Farticle> findFarticle(int farticletype,int firstResult,int maxResult){
		List<Farticle> farticles = this.farticleDAO.findFarticle(farticletype, firstResult, maxResult);
		return farticles ;
	}
	public int findFarticleCount(int farticletype){
		return this.farticleDAO.findFarticleCount(farticletype) ;
	}
	
	public int findFarticle(int farticletype){
		return this.farticleDAO.findFarticleCount(farticletype) ;
	}
	
	public List<Ffriendlink> findFriendLink(int type,int firstResult,int maxResult){
		return this.ffriendlinkDAO.findFriendLink(type, firstResult, maxResult) ;
	}
	
	public Farticle findFarticleById(int id){
		Farticle farticle = this.farticleDAO.findById(id) ;
		farticle.getFarticletype().getFname() ;
		return farticle ;
	}
	
	public Farticletype findFarticleTypeById(int id){
		return this.farticletypeDAO.findById(id) ;
	}
	
	public List<Farticletype> findFarticleTypeAll(){
		return this.farticletypeDAO.findAll() ;
	}
	
	public List<Fperiod> findAllFperiod(long fromTime,int fvirtualCoinType){
		return this.fperiodDAO.findAllFperiod(fromTime, fvirtualCoinType) ;
	}
	
	public void addFperiod(Fperiod fperiod){
		this.fperiodDAO.save(fperiod) ;
	}
	
	public Fperiod getLastFperiod(Fvirtualcointype fvirtualcointype){
		return this.fperiodDAO.getLastFperiod(fvirtualcointype) ;
	}
	
	public Fentrustlog getLastFpeFentrustlog(int fvirtualcointype){
		return this.fentrustlogDAO.getLastFpeFentrustlog(fvirtualcointype) ;
	}

	public List<Farticle> findArticleByProperty(String key, Object value){
		return farticleDAO.findByProperty(key, value);
	}

	public List<Farticle> findArticleByCoinId(int coinId, int firstResult, int maxResults){
		return farticleDAO.findArticleByCoinId(coinId, firstResult, maxResults);
	}

	public int countArticleByCoinId(int coinId){
		return farticleDAO.countByCoinId(coinId);
	}

	/**
	 * 通过搜索的关键字查询文章
	 * @param keyword
	 * @return
     */
	public List<Farticle> findArticleByKeyword(int firstResult, int maxResults, String keyword){
		return farticleDAO.findByKeyword(firstResult, maxResults, keyword);
	}
	public List<Farticle> findArticleByKeywordsAnd(int firstResult, int maxResults, String[] keyword){
		return farticleDAO.findByKeywordsAnd(firstResult, maxResults, keyword);
	}
	public List<Farticle> findArticleByKeywords(int firstResult, int maxResults, String[] keyword){
		return farticleDAO.findByKeywords(firstResult, maxResults, keyword);
	}
	public int countArticleByKeyword(String keyword){
		return farticleDAO.countByKeyword(keyword);
	}
	public int countByArticleKeywords(String[] keywords){
		return farticleDAO.countByKeywords(keywords);
	}
}
