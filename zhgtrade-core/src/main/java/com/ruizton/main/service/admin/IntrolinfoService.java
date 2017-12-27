package com.ruizton.main.service.admin;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.ruizton.main.dao.*;
import com.ruizton.main.model.*;
import com.ruizton.util.MathUtils;
import com.ruizton.util.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ruizton.main.dao.FintrolinfoDAO;
import com.ruizton.main.model.Fintrolinfo;

@Service
public class IntrolinfoService {
	@Autowired
	private FintrolinfoDAO introlinfoDAO;
	@Autowired
	private FuserDAO fuserDAO;
	@Autowired
	private FsystemargsDAO fsystemargsDAO;
	@Autowired
	private FwalletDAO fwalletDAO;

	public Fintrolinfo findById(int id) {
		return this.introlinfoDAO.findById(id);
	}

	public void saveObj(Fintrolinfo obj) {
		this.introlinfoDAO.save(obj);
	}

	public void deleteObj(int id) {
		Fintrolinfo obj = this.introlinfoDAO.findById(id);
		this.introlinfoDAO.delete(obj);
	}

	public void updateObj(Fintrolinfo obj) {
		this.introlinfoDAO.attachDirty(obj);
	}

	public List<Fintrolinfo> findByProperty(String name, Object value) {
		return this.introlinfoDAO.findByProperty(name, value);
	}

	public List<Fintrolinfo> findAll() {
		return this.introlinfoDAO.findAll();
	}

	public List<Fintrolinfo> list(int firstResult, int maxResults,
			String filter,boolean isFY) {
		return this.introlinfoDAO.list(firstResult, maxResults, filter,isFY);
	}
	
	public List<Map> getAllIntrol(int firstResult, int maxResults, String filter,boolean isFY) {
		return this.introlinfoDAO.getAllIntrol(firstResult, maxResults, filter, isFY);
	}

	public void updateChargeAward(Fcapitaloperation fcapitaloperation){
		// 首次充值奖励推广人
		Fuser fuser = fcapitaloperation.getFuser();
		if(fuser.getfIntroUser_id() != null && (fuser.getFischarge() == null || fuser.getFischarge().length()==0)){
			Fuser fintrolUser = this.fuserDAO.findById(fuser.getfIntroUser_id().getFid());
			fuser.setFischarge("true");
			this.fuserDAO.attachDirty(fuser);
			Fsystemargs args = this.fsystemargsDAO.findByFkey("introlRegAmt").get(0);
			String[] tt = args.getFvalue().split("#");
			Fwallet introFwallet = fintrolUser.getFwallet();
			double amt = 0d;
			if(tt[0].equals("1")){
				amt = Double.valueOf(tt[1]);
				introFwallet.setFtotalRmb(MathUtils.add(introFwallet.getFtotalRmb(), amt));
			}else{
				amt = Utils.getDouble(Double.valueOf(tt[1])*fcapitaloperation.getFamount(), 2);
				introFwallet.setFtotalRmb(introFwallet.getFtotalRmb()+amt);
			}
			Fintrolinfo intro = new Fintrolinfo();
			intro.setFcreatetime(Utils.getTimestamp());
			intro.setFiscny(true);
			intro.setFuser(fintrolUser);
			intro.setFqty(amt);
			intro.setFtitle("下线UID:"+fuser.getFid()+"首次充值，奖励￥"+amt);
			this.introlinfoDAO.save(intro);
			this.fwalletDAO.attachDirty(introFwallet);
		}
	}
}