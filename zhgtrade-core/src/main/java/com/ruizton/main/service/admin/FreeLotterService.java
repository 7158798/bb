package com.ruizton.main.service.admin;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ruizton.main.dao.FfreeLotteryDAO;
import com.ruizton.main.dao.FfreeLotteryDAO;
import com.ruizton.main.model.FfreeLottery;
import com.ruizton.main.model.FfreeLottery;

@Service
public class FreeLotterService {
	@Autowired
	private FfreeLotteryDAO freeLotteryDAO;

	public FfreeLottery findById(int id) {
		return this.freeLotteryDAO.findById(id);
	}

	public void saveObj(FfreeLottery obj) {
		this.freeLotteryDAO.save(obj);
	}

	public void deleteObj(int id) {
		FfreeLottery obj = this.freeLotteryDAO.findById(id);
		this.freeLotteryDAO.delete(obj);
	}

	public void updateObj(FfreeLottery obj) {
		this.freeLotteryDAO.attachDirty(obj);
	}

	public List<FfreeLottery> findByProperty(String name, Object value) {
		return this.freeLotteryDAO.findByProperty(name, value);
	}

	public List<FfreeLottery> findAll() {
		return this.freeLotteryDAO.findAll();
	}

	public List<FfreeLottery> list(int firstResult, int maxResults,
			String filter,boolean isFY) {
		List<FfreeLottery> all = this.freeLotteryDAO.list(firstResult, maxResults, filter,isFY);
		for (FfreeLottery ffreeLottery : all) {
			if(ffreeLottery.getFuser() != null){
				ffreeLottery.getFuser().getFnickName();
			}
			if(ffreeLottery.getFvirtualcointype() != null){
				ffreeLottery.getFvirtualcointype().getFname();
			}
		}
		return all;
	}
}