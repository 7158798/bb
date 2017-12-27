package com.ruizton.main.service.admin;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.ruizton.main.dao.FvirtualaddressDAO;
import com.ruizton.main.model.*;
import com.ruizton.util.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ruizton.main.dao.FaboutDAO;
import com.ruizton.main.dao.FadminDAO;
import com.ruizton.main.dao.FpoolDAO;

@Service
public class PoolService {
	@Autowired
	private FpoolDAO poolDAO;
	@Autowired
	private FvirtualaddressDAO fvirtualaddressDAO;

	private Logger logger = LoggerFactory.getLogger(PoolService.class);

	public Fpool findById(int id) {
		return this.poolDAO.findById(id);
	}

	public void saveObj(Fpool obj) {
		this.poolDAO.save(obj);
	}

	public void deleteObj(int id) {
		Fpool obj = this.poolDAO.findById(id);
		this.poolDAO.delete(obj);
	}

	public void updateObj(Fpool obj) {
		this.poolDAO.attachDirty(obj);
	}

	public List<Fpool> findByProperty(String name, Object value) {
		return this.poolDAO.findByProperty(name, value);
	}

	public List<Fpool> findAll() {
		return this.poolDAO.findAll();
	}
	
	public List list(int firstResult, int maxResults, String filter,boolean isFY) {
		return this.poolDAO.list(firstResult, maxResults, filter, isFY);
	}
	
	public int countForList(String filter) {
		return this.poolDAO.countForList(filter);
	}

	/**
	 * 给用户分配虚拟币充值地址
	 *
	 * @param fuser
	 * @param fvirtualcointype
     */
	public Fvirtualaddress addChargeAddress(Fuser fuser, Fvirtualcointype fvirtualcointype){
		if(null == fvirtualcointype || !fvirtualcointype.isFIsWithDraw()){
			return null;
		}

		BTCMessage btcMessage = new BTCMessage() ;
		btcMessage.setACCESS_KEY(fvirtualcointype.getFaccess_key()) ;
		btcMessage.setIP(fvirtualcointype.getFip()) ;
		btcMessage.setPORT(fvirtualcointype.getFport()) ;
		btcMessage.setSECRET_KEY(fvirtualcointype.getFsecrt_key()) ;
		if(btcMessage.getACCESS_KEY()==null
				||btcMessage.getIP()==null
				||btcMessage.getPORT()==null
				||btcMessage.getSECRET_KEY()==null){
			if(logger.isErrorEnabled()){
				logger.error(fvirtualcointype.getFname() + "虚拟币配置信息有误");
			}
			return null;
		}

		//BTCUtils btcUtils = new BTCUtils(btcMessage) ;
		//String address = btcUtils.getNewaddressValue(fuser.getFid()) ;
		Fpool fpool = this.poolDAO.getOneFpool(fvirtualcointype) ;
		if(null == fpool){
			if(logger.isErrorEnabled()){
				logger.error(fvirtualcointype.getFname() + "没有钱包地址了");
			}
			return null;
		}
		String address = fpool.getFaddress() ;
		Fvirtualaddress fvirtualaddress = new Fvirtualaddress() ;
		fvirtualaddress.setFadderess(address) ;
		fvirtualaddress.setFcreateTime(Utils.getTimestamp()) ;
		fvirtualaddress.setFuser(fuser) ;
		fvirtualaddress.setFvirtualcointype(fvirtualcointype) ;

		fpool.setFstatus(1) ;
		this.poolDAO.attachDirty(fpool) ;
		this.fvirtualaddressDAO.save(fvirtualaddress) ;
		return fvirtualaddress;
	}

}