package com.ruizton.main.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ruizton.main.dao.FentrustDAO;
import com.ruizton.main.dao.FentrustlogDAO;
import com.ruizton.main.model.Fentrust;

@Service
public class BaseService {
	
	@Autowired
	FentrustDAO fentrustDAO;
	@Autowired
	FentrustlogDAO fentrustlogDAO;
	
	public Fentrust findByFid(int id){
		Fentrust fentrust = fentrustDAO.findById(id);
		return fentrust;
	}
}
