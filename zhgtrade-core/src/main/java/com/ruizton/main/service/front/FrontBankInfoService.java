package com.ruizton.main.service.front;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ruizton.main.Enum.SystemBankInfoEnum;
import com.ruizton.main.dao.SystembankinfoDAO;
import com.ruizton.main.model.Systembankinfo;

@Service
public class FrontBankInfoService {
	@Autowired
	private SystembankinfoDAO systembankinfoDAO ;
	
	public List<Systembankinfo> findAllSystemBankInfo() {
		return this.systembankinfoDAO.findByProperty("fstatus", SystemBankInfoEnum.NORMAL_VALUE) ;
	}
	
	public Systembankinfo findSystembankinfoById(int id) throws Exception{
		return this.systembankinfoDAO.findById(id) ;
	}
	/**
	* 根据状态查询Systembankinfo列表
	* 比特家
	* CopyRight : www.zhgtrade.com
	* Author : 俞杰（945351749@qq.com）
	* Date : 2016年4月18日 下午1:05:56
	*/
	public List<Systembankinfo> findSystembankinfoByStatus(int status) throws Exception{
		return this.systembankinfoDAO.findByFstatus(status) ;
	}
}
