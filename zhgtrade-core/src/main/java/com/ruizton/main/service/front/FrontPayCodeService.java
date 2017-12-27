package com.ruizton.main.service.front;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ruizton.main.Enum.PayCodeStatusEnum;
import com.ruizton.main.dao.FpaycodeDAO;
import com.ruizton.main.dao.FuserDAO;
import com.ruizton.main.dao.FvirtualwalletDAO;
import com.ruizton.main.dao.FwalletDAO;
import com.ruizton.main.model.Fpaycode;
import com.ruizton.main.model.Fuser;
import com.ruizton.main.model.Fvirtualwallet;
import com.ruizton.main.model.Fwallet;
import com.ruizton.main.service.BaseService;

@Service
public class FrontPayCodeService extends BaseService {

	@Autowired
	private FpaycodeDAO fpaycodeDAO ;
	@Autowired
	private FuserDAO fuserDAO ;
	@Autowired
	private FvirtualwalletDAO fvirtualwalletDAO ;
	@Autowired
	private FwalletDAO fwalletDAO ;
	
	public Fpaycode findFpaycode(String key,String value){
		List<Fpaycode> fpaycodes = this.fpaycodeDAO.findByParam(0, 0, " where fkey='"+key+"' and fvalue='"+value+"' ", false, "Fpaycode") ;
		if(fpaycodes.size()>0){
			Fpaycode fpaycode = fpaycodes.get(0) ;
			if(fpaycode.getFstatus()==PayCodeStatusEnum.PAYCODE_CREATE){
				return fpaycode ;
			}
		}
		
		return null ;
	}
	
	public boolean findFpaycode(String key){

		List<Fpaycode> fpaycodes = this.fpaycodeDAO.findByParam(0, 0, " where fkey='"+key+"' ", false, "Fpaycode") ;
		if(fpaycodes.size()>0){
			Fpaycode fpaycode = fpaycodes.get(0) ;
			if(fpaycode.getFstatus()==PayCodeStatusEnum.PAYCODE_CREATE){
				return true ;
			}
		}
		
		return false ;
	}
	
	public void updateRecharge(String key,String value,Fuser fuser){
		List<Fpaycode> fpaycodes = this.fpaycodeDAO.findByParam(0, 0, " where fkey='"+key+"' ", false, "Fpaycode") ;
		if(fpaycodes.size()>0){
			Fpaycode fpaycode = fpaycodes.get(0) ;
			if(fpaycode.getFstatus()==PayCodeStatusEnum.PAYCODE_CREATE){
				fpaycode.setFuser(fuser) ;
				fpaycode.setFstatus(PayCodeStatusEnum.PAYCODE_USER_CONFIRM) ;
				this.fpaycodeDAO.attachDirty(fpaycode) ;
			}else{
				throw new RuntimeException() ;
			}
		}else{
			throw new RuntimeException() ;
		}
	}
	
	public List<Fpaycode> findFpaycodeByParam(int firstResult, int maxResult, String filter){
		return this.fpaycodeDAO.findByParam(firstResult, maxResult, filter, true, "Fpaycode") ;
	}
	
	public int findFpaycodeByParamCount(String filter){
		return this.fpaycodeDAO.findByParamCount(filter, "Fpaycode") ;
	}
}
