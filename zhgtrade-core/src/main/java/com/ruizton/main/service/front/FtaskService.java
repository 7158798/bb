package com.ruizton.main.service.front;

import com.ruizton.main.controller.BaseController;
import com.ruizton.main.dao.*;
import com.ruizton.main.model.Fmessage;
import com.ruizton.main.model.Ftask;
import com.ruizton.main.model.FtaskLog;
import com.ruizton.main.model.Fvirtualwallet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FtaskService extends BaseController {

	@Autowired
	private FtaskDAO ftaskDAO ;
	@Autowired
	private FtaskLogDAO ftaskLogDAO ;
	@Autowired
	private FuserDAO fuserDAO ;
	@Autowired
	private FmessageDAO fmessageDAO ;
	@Autowired
	private FwalletDAO fwalletDAO ;
	@Autowired
	private FvirtualwalletDAO fvirtualwalletDAO ;
	
	public Ftask findFtask(int id){
		return ftaskDAO.findById(id) ;
	}
	
	public void saveFtask(Ftask ftask){
		this.ftaskDAO.save(ftask) ;
	}
	public void updateFtask(Ftask ftask){
		this.ftaskDAO.attachDirty(ftask) ;
	}
	
	public void saveFtaskLog(FtaskLog ftaskLog){
		this.ftaskLogDAO.save(ftaskLog) ;
	}
	public void updateFtaskLog(FtaskLog ftaskLog){
		this.ftaskLogDAO.attachDirty(ftaskLog) ;
	}
	
	public void updateFinishTask(FtaskLog ftaskLog,Fmessage fmessage,Fvirtualwallet fvirtualwallet){
		this.ftaskLogDAO.save(ftaskLog) ;
		this.fmessageDAO.save(fmessage) ;
		this.fvirtualwalletDAO.attachDirty(fvirtualwallet) ;
		
	}
}
