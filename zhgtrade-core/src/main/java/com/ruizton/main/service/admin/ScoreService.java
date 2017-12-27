package com.ruizton.main.service.admin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ruizton.main.dao.FcapitaloperationDAO;
import com.ruizton.main.dao.FentrustlogDAO;
import com.ruizton.main.dao.FscoreDAO;
import com.ruizton.main.model.Fcapitaloperation;
import com.ruizton.main.model.Fentrustlog;
import com.ruizton.main.model.Fscore;

@Service
public class ScoreService {
	@Autowired
	private FscoreDAO scoreDAO;
	@Autowired
	private FcapitaloperationDAO capitaloperationDAO;
	@Autowired
	private FentrustlogDAO entrustlogDAO;

	public Fscore findById(int id) {
		return this.scoreDAO.findById(id);
	}

	public void saveObj(Fscore obj) {
		this.scoreDAO.save(obj);
	}

	public void deleteObj(int id) {
		Fscore obj = this.scoreDAO.findById(id);
		this.scoreDAO.delete(obj);
	}

	public void updateObj(Fscore obj) {
		this.scoreDAO.attachDirty(obj);
	}
	
	public void updateObj1(Fscore obj,Fscore obj1) {
		try {
			this.scoreDAO.attachDirty(obj);
			if(obj1 != null){
				this.scoreDAO.attachDirty(obj1);
			}
		} catch (Exception e) {
			throw new RuntimeException();
		}
	}

	public List<Fscore> findByProperty(String name, Object value) {
		return this.scoreDAO.findByProperty(name, value);
	}

	public List<Fscore> findAll() {
		return this.scoreDAO.findAll();
	}
	
	public void updateVip(Fscore fscore,Fcapitaloperation capitaloperation) {
		try {
			this.scoreDAO.attachDirty(fscore);
			this.capitaloperationDAO.attachDirty(capitaloperation);
		} catch (Exception e) {
			throw new RuntimeException();
		}
	}
	
	public void updateVip(Fscore fscore,Fentrustlog entrustlog) {
		try {
			this.scoreDAO.attachDirty(fscore);
			this.entrustlogDAO.attachDirty(entrustlog);
		} catch (Exception e) {
			throw new RuntimeException();
		}
	}
}