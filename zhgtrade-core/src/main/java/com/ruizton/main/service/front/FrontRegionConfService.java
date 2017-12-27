package com.ruizton.main.service.front;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ruizton.main.dao.FregionconfDAO; 
import com.ruizton.main.model.Fregionconf;

@Service
public class FrontRegionConfService {
	private static final Logger log = LoggerFactory
			.getLogger(FrontRegionConfService.class);
	
	@Autowired
	FregionconfDAO fregionconfDAO;
	
	public List<Fregionconf> findRegionByLevel(int regionLevel) {
		return this.fregionconfDAO.findByProperty("regionLevel",regionLevel);
	}
	
	public List<Fregionconf> findRegionByPid(int pid) {
		return this.fregionconfDAO.findByProperty("pid",pid);
	}
	
	public Fregionconf findRegionById(int id){
		return this.fregionconfDAO.findById(id);
	}

}
