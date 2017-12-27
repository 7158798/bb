package com.ruizton.main.service.admin;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ruizton.main.dao.FaboutDAO;
import com.ruizton.main.dao.FlotteryawardDAO;
import com.ruizton.main.dao.FlotterylogDAO;
import com.ruizton.main.dao.FvirtualwalletDAO;
import com.ruizton.main.model.Fabout;
import com.ruizton.main.model.Flotteryaward;
import com.ruizton.main.model.Flotterylog;
import com.ruizton.main.model.Fvirtualwallet;

@Service
public class LotteryawardService {
	@Autowired
	private FlotteryawardDAO lotteryawardDAO;
	@Autowired
	private FlotterylogDAO lotterylogDAO;
	@Autowired
	private FvirtualwalletDAO virtualwalletDAO;

	public Flotteryaward findById(int id) {
		return this.lotteryawardDAO.findById(id);
	}

	public void saveObj(Flotteryaward obj) {
		this.lotteryawardDAO.save(obj);
	}

	public void deleteObj(int id) {
		Flotteryaward obj = this.lotteryawardDAO.findById(id);
		this.lotteryawardDAO.delete(obj);
	}

	public void updateObj(Flotteryaward obj) {
		this.lotteryawardDAO.attachDirty(obj);
	}

	public List<Flotteryaward> findByProperty(String name, Object value) {
		return this.lotteryawardDAO.findByProperty(name, value);
	}

	public List<Flotteryaward> findAll() {
		return this.lotteryawardDAO.findAll();
	}

	public List<Flotteryaward> list(int firstResult, int maxResults,
			String filter,boolean isFY) {
		return this.lotteryawardDAO.list(firstResult, maxResults, filter,isFY);
	}
	
	public void updateLotteryLog(Flotterylog lotterylog,Fvirtualwallet virtualwallet) {
        try {
			this.lotterylogDAO.save(lotterylog);
			this.virtualwalletDAO.attachDirty(virtualwallet);
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}
	
	public void updateLotteryLog(Fvirtualwallet virtualwallet) {
		try {
			this.virtualwalletDAO.attachDirty(virtualwallet);
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}
}