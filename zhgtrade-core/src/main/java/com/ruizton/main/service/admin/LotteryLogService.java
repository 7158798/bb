package com.ruizton.main.service.admin;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ruizton.main.dao.FlotterylogDAO;
import com.ruizton.main.dao.FmessageDAO;
import com.ruizton.main.dao.FvirtualwalletDAO;
import com.ruizton.main.model.Flotterylog;
import com.ruizton.main.model.Fmessage;
import com.ruizton.main.model.Fvirtualwallet;

@Service
public class LotteryLogService {
	@Autowired
	private FlotterylogDAO lotterylogDAO;
	@Autowired
	private FmessageDAO messageDAO;
	@Autowired
	private FvirtualwalletDAO virtualwalletDAO;

	public Flotterylog findById(int id) {
		return this.lotterylogDAO.findById(id);
	}

	public void saveObj(Flotterylog obj) {
		this.lotterylogDAO.save(obj);
	}

	public void deleteObj(int id) {
		Flotterylog obj = this.lotterylogDAO.findById(id);
		this.lotterylogDAO.delete(obj);
	}

	public void updateObj(Flotterylog obj) {
		this.lotterylogDAO.attachDirty(obj);
	}

	public List<Flotterylog> findByProperty(String name, Object value) {
		return this.lotterylogDAO.findByProperty(name, value);
	}

	public List<Flotterylog> findAll() {
		return this.lotterylogDAO.findAll();
	}

	public List<Flotterylog> list(int firstResult, int maxResults,
			String filter,boolean isFY) {
		List<Flotterylog> all = this.lotterylogDAO.list(firstResult, maxResults, filter,isFY);
		for (Flotterylog flotterylog : all) {
			if(flotterylog.getFuser() != null){
				flotterylog.getFuser().getFnickName();
			}
		}
		return all;
	}
	
	public List<Flotterylog> list1(int firstResult, int maxResults,
			String filter,boolean isFY) {
		return this.lotterylogDAO.list(firstResult, maxResults, filter,isFY);
	}
	
	public void updatelog(Fmessage message,Flotterylog lotterylog) {
		try {
			this.messageDAO.save(message);
			this.lotterylogDAO.attachDirty(lotterylog);
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}
	
	public void updatelog(Fmessage message,Flotterylog lotterylog,Fvirtualwallet virtualwallet) {
		try {
			this.messageDAO.save(message);
			this.lotterylogDAO.attachDirty(lotterylog);
			this.virtualwalletDAO.attachDirty(virtualwallet);
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}
}