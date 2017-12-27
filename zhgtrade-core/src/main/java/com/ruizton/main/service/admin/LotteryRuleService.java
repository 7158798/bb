package com.ruizton.main.service.admin;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ruizton.main.dao.FlotterylogDAO;
import com.ruizton.main.dao.FlotteryruleDAO;
import com.ruizton.main.dao.FmessageDAO;
import com.ruizton.main.dao.FusersettingDAO;
import com.ruizton.main.dao.FvirtualwalletDAO;
import com.ruizton.main.model.Flotterylog;
import com.ruizton.main.model.Flotteryrule;
import com.ruizton.main.model.Fmessage;
import com.ruizton.main.model.Fusersetting;
import com.ruizton.main.model.Fvirtualwallet;

@Service
public class LotteryRuleService {
	@Autowired
	private FlotteryruleDAO lotteryruleDAO;
	@Autowired
	private FusersettingDAO usersettingDAO;
	@Autowired
	private FlotterylogDAO lotterylogDAO;
	@Autowired
	private FvirtualwalletDAO virtualwalletDAO;
	@Autowired
	private FmessageDAO messageDAO;

	public Flotteryrule findById(int id) {
		Flotteryrule lotteryrule = this.lotteryruleDAO.findById(id);
		if(lotteryrule.getFvirtualCoinTypeId() != null){
			lotteryrule.getFvirtualCoinTypeId().getFname();
		}
		return lotteryrule;
	}

	public void saveObj(Flotteryrule obj) {
		this.lotteryruleDAO.save(obj);
	}

	public void deleteObj(int id) {
		Flotteryrule obj = this.lotteryruleDAO.findById(id);
		this.lotteryruleDAO.delete(obj);
	}

	public void updateObj(Flotteryrule obj) {
		this.lotteryruleDAO.attachDirty(obj);
	}

	public List<Flotteryrule> findByProperty(String name, Object value) {
		return this.lotteryruleDAO.findByProperty(name, value);
	}

	public List<Flotteryrule> findAll() {
		return this.lotteryruleDAO.findAll();
	}

	public List<Flotteryrule> list(int firstResult, int maxResults,
			String filter,boolean isFY) {
		List<Flotteryrule> all = this.lotteryruleDAO.list(firstResult, maxResults, filter,isFY);
		for (Flotteryrule flotteryrule : all) {
			if(flotteryrule.getFvirtualCoinTypeId() != null){
				flotteryrule.getFvirtualCoinTypeId().getFname();
			}
		}
		return all;
	}
	
	public void updateLog(Fusersetting usersetting) {
		try {	
			this.usersettingDAO.attachDirty(usersetting);
		} catch (Exception e) {
			throw new RuntimeException("网络异常");
		}
	}
	
	public void updateLog(Fusersetting usersetting,Fvirtualwallet virtualwallet,Flotterylog lotterylog,Fmessage message) {
		try {
			this.usersettingDAO.attachDirty(usersetting);
			this.virtualwalletDAO.attachDirty(virtualwallet);
			this.lotterylogDAO.save(lotterylog);
			this.messageDAO.save(message);
		} catch (Exception e) {
			throw new RuntimeException("网络异常");
		}
	}
}