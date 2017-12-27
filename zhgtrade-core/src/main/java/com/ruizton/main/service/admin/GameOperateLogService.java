package com.ruizton.main.service.admin;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ruizton.main.dao.FaboutDAO;
import com.ruizton.main.dao.FgameoperatelogDAO;
import com.ruizton.main.model.Fabout;
import com.ruizton.main.model.Fgameoperatelog;

@Service
public class GameOperateLogService {
	@Autowired
	private FgameoperatelogDAO gameoperatelogDAO;

	public Fgameoperatelog findById(int id) {
		return this.gameoperatelogDAO.findById(id);
	}

	public void saveObj(Fgameoperatelog obj) {
		this.gameoperatelogDAO.save(obj);
	}

	public void deleteObj(int id) {
		Fgameoperatelog obj = this.gameoperatelogDAO.findById(id);
		this.gameoperatelogDAO.delete(obj);
	}

	public void updateObj(Fgameoperatelog obj) {
		this.gameoperatelogDAO.attachDirty(obj);
	}

	public List<Fgameoperatelog> findByProperty(String name, Object value) {
		return this.gameoperatelogDAO.findByProperty(name, value);
	}

	public List<Fgameoperatelog> findAll() {
		return this.gameoperatelogDAO.findAll();
	}

	public List<Fgameoperatelog> list(int firstResult, int maxResults,
			String filter,boolean isFY) {
		List<Fgameoperatelog> all = this.gameoperatelogDAO.list(firstResult, maxResults, filter,isFY);
		for (Fgameoperatelog fgameoperatelog : all) {
		     if(fgameoperatelog.getFgamerule() != null){
		    	 fgameoperatelog.getFgamerule().getFexpendQty();
		     }
		     if(fgameoperatelog.getFuser() != null){
		    	 fgameoperatelog.getFuser().getFnickName();
		     }
		}
		return all;
	}
	
	public Map getTotalQty(int type,boolean isToday) {
		return this.gameoperatelogDAO.getTotalQty(type, isToday);
	}
}