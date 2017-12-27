package com.ruizton.main.service.admin;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ruizton.main.dao.FtaskLogDAO;
import com.ruizton.main.dao.FtaskLogDAO;
import com.ruizton.main.model.FtaskLog;
import com.ruizton.main.model.FtaskLog;

@Service
public class TasklogService {
	@Autowired
	private FtaskLogDAO taskLogDAO;

	public FtaskLog findById(int id) {
		return this.taskLogDAO.findById(id);
	}

	public void saveObj(FtaskLog obj) {
		this.taskLogDAO.save(obj);
	}

	public void deleteObj(int id) {
		FtaskLog obj = this.taskLogDAO.findById(id);
		this.taskLogDAO.delete(obj);
	}

	public void updateObj(FtaskLog obj) {
		this.taskLogDAO.attachDirty(obj);
	}

	public List<FtaskLog> findByProperty(String name, Object value) {
		return this.taskLogDAO.findByProperty(name, value);
	}

	public List<FtaskLog> findAll() {
		return this.taskLogDAO.findAll();
	}

	public List<FtaskLog> list(int firstResult, int maxResults,
			String filter,boolean isFY) {
		List<FtaskLog> all = this.taskLogDAO.list(firstResult, maxResults, filter,isFY);
		for (FtaskLog ftaskLog : all) {
			if(ftaskLog.getFuser() != null){
				ftaskLog.getFuser().getFnickName();
			}
		}
		return all;
	}
}