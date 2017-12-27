package com.ruizton.main.service.admin;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import com.ruizton.main.dao.*;
import com.ruizton.main.model.*;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.ruizton.main.Enum.ActivityTypeTypeEnum;
import com.ruizton.main.Enum.VirtualCoinTypeStatusEnum;
import com.ruizton.util.BTCUtils;
import com.ruizton.util.Utils;


@Service
public class VirtualCoinService {
	@Autowired
	private FvirtualcointypeDAO virtualcointypeDAO;
	@Autowired
	private FvirtualaddressDAO virtualaddressDAO;
	@Autowired
	private FvirtualaddressWithdrawDAO virtualaddressWithdrawDAO;
	@Autowired
	private FactivitytypeDAO activitytypeDAO;
	@Autowired
	private FuserDAO userDAO;
	@Autowired
	private FvirtualwalletDAO virtualwalletDAO;
	@Autowired
	private FwithdrawfeesDAO withdrawfeesDAO;
	@Autowired
	private FfeesDAO feesDAO;
	@Autowired
	private FvirtualCoinTypeDetailDAO fvirtualCoinTypeDetailDAO;

	public Fvirtualcointype findById(int id) {
		return this.virtualcointypeDAO.findById(id);
	}

	public void saveObj(Fvirtualcointype obj) {
		this.virtualcointypeDAO.save(obj);
	}

	public void deleteObj(int id) {
		Fvirtualcointype obj = this.virtualcointypeDAO.findById(id);
		this.virtualcointypeDAO.delete(obj);
	}

	public void updateObj(Fvirtualcointype obj) {
		this.virtualcointypeDAO.attachDirty(obj);
	}

	public List<Fvirtualcointype> findByProperty(String name, Object value) {
		return this.virtualcointypeDAO.findByProperty(name, value);
	}

	public List<Fvirtualcointype> findAll() {
		return this.virtualcointypeDAO.findAll();
	}

	public List<Fvirtualcointype> list(int firstResult, int maxResults,
			String filter,boolean isFY) {
		List<Fvirtualcointype> all = this.virtualcointypeDAO.list(firstResult, maxResults, filter,isFY);
		for (Fvirtualcointype fvirtualcointype : all) {
			Set<Ffees> set = fvirtualcointype.getFfees();
			for (Ffees ffees : set) {
				ffees.getWithdraw();
			}
		}
		return all;
	}
	
	public boolean updateCoinType(Fvirtualcointype virtualcointype,String password) throws RuntimeException{
		int fid = virtualcointype.getFid();
//		List<Fuser> all = this.userDAO.findAll();
		try {
			if(virtualcointype.isFIsWithDraw()){//分红币不判断
				BTCMessage btcMessage = new BTCMessage() ;
				btcMessage.setACCESS_KEY(virtualcointype.getFaccess_key()) ;
				btcMessage.setIP(virtualcointype.getFip()) ;
				btcMessage.setPORT(virtualcointype.getFport()) ;
				btcMessage.setSECRET_KEY(virtualcointype.getFsecrt_key()) ;
				btcMessage.setPASSWORD(password);
				if(btcMessage.getACCESS_KEY()==null
						||btcMessage.getIP()==null
						||btcMessage.getPORT()==null
						||btcMessage.getSECRET_KEY()==null){
					throw new RuntimeException() ;
				}
				BTCUtils btcUtils = new BTCUtils(btcMessage) ;
				btcUtils.getbalanceValue();
			}
		} catch (Exception e1) {
			e1.printStackTrace();
			throw new RuntimeException() ;
		}
		
		String result = "";
		try {
			result = this.virtualcointypeDAO.startCoinType(virtualcointype.getFid());
			if(!result.equals("SUCCESS")){
				throw new RuntimeException(result) ;
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(result) ;
		}
		
		List<Fwithdrawfees> allWithDrawFees = withdrawfeesDAO.findAll();
		for (Fwithdrawfees fwithdrawfees : allWithDrawFees) {
		    String filter = "where flevel="+fwithdrawfees.getFlevel()+" and fvirtualcointype.fid="+fid;
			List<Ffees> feesList = this.feesDAO.list(0, 0, filter, false);
			if(feesList == null || feesList.size() == 0){
				Ffees fees = new Ffees();
				fees.setWithdraw(0);
				fees.setFfee(0);
				fees.setFlevel(fwithdrawfees.getFlevel());
				fees.setFvirtualcointype(virtualcointype);
				feesDAO.attachDirty(fees);
			}
		}
		
/*		int[] value = {ActivityTypeTypeEnum.TRADE,ActivityTypeTypeEnum.WITHDRAW};
		for(int i=0;i<value.length;i++){
			String sql = "where fvirtualCoinType.fid="+virtualcointype.getFid()+" and type="+value[i];
			List<Factivitytype> allFactivitytype = this.activitytypeDAO.list(0, 0, sql, false);
			if(allFactivitytype == null || allFactivitytype.size() == 0){
				Factivitytype activitytype = new Factivitytype();
				activitytype.setFcreateTime(Utils.getTimestamp());
				activitytype.setFdescription(ActivityTypeTypeEnum.getEnumString(value[i]));
				activitytype.setFname(ActivityTypeTypeEnum.getEnumString(value[i]));
				activitytype.setFvirtualCoinType(virtualcointype);
				activitytype.setType(value[i]);
				this.activitytypeDAO.attachDirty(activitytype);
			}
		}*/
		
		this.virtualcointypeDAO.attachDirty(virtualcointype);
		return true;
	}
	
	public Fvirtualcointype findFirstFirtualCoin(){
		Fvirtualcointype fvirtualcointype = null ;
		List<Fvirtualcointype> list = this.virtualcointypeDAO.findByProperty("fstatus", VirtualCoinTypeStatusEnum.Normal) ;
		if(list.size()>0){
			fvirtualcointype = list.get(0) ;
		}
		return fvirtualcointype ;
	}

	public void saveDetail(FTypeDetail detail){
		fvirtualCoinTypeDetailDAO.save(detail);
	}

	public void updateMergeDetail(FTypeDetail detail){
		fvirtualCoinTypeDetailDAO.merge(detail);
	}

	public void updateDetail(FTypeDetail detail){
		fvirtualCoinTypeDetailDAO.attachDirty(detail);
	}

	public FTypeDetail findDetailById(int fid){
		return fvirtualCoinTypeDetailDAO.findById(fid);
	}

	public FTypeDetail findDetailByFvid(int fvid){
		return fvirtualCoinTypeDetailDAO.findByFvid(fvid);
	}

	public List<FTypeDetail> findAllDetail(){
		return fvirtualCoinTypeDetailDAO.findAll();
	}

	public List<Map<String, Object>> findUserVirtualCoinAddress(String keyword, Integer symbol, String orderField, String orderDirection, int offset, int length, boolean isPage){
		return virtualaddressDAO.findUserVirtualCoinAddress(keyword, symbol, orderField, orderDirection, offset, length, isPage);
	}

	public int countUserVirtualCoinAddress(String keyword, Integer symbol){
		return virtualaddressDAO.countUserVirtualCoinAddress(keyword, symbol);
	}
}












