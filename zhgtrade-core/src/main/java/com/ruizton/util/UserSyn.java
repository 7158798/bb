package com.ruizton.util;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
 
import com.ruizton.main.Enum.UserStatusEnum;
import com.ruizton.main.model.Fuser;
import com.ruizton.main.service.admin.AdminService;
import com.ruizton.main.service.admin.UserService;
import com.ruizton.main.service.front.FrontUserService;
import com.zhguser.UserClient;
import com.zhguser.service.User;


//同步用户数据
public class UserSyn { 
	private static boolean isOccupied = false;
	
	@Autowired
	private UserService userService;
	@Autowired 
	private AdminService adminService;
	@Autowired 
	private FrontUserService frontUserService;
	 
	public int synToGlobalUserDb(){
		if(isOccupied){
			return -1;//正在被进行数据同步
		}
		isOccupied = true; 
		
		int count = adminService.getAllCount("Fuser", "where globalUserId<=0");
		int numPerSyn = 1000;
		
		int ret = 0;
		for(int i=0; i<count; i+=numPerSyn){
			List<Fuser> users = userService.simpleList( 0, numPerSyn, " where globalUserId<=0 ", true );//每次读取未同步的前1000名用户
			for(Fuser fuser : users){ 
				try{
					boolean flag = synFuserToGlobal(fuser);
					if(flag)
						ret++;
				}catch(Exception e){
					e.printStackTrace();
					isOccupied = false;
					return ret;
				}
			} 
		}
		
		isOccupied = false;
		return ret; 
	}
	
	public int synToLocalUserDb(int from){ 
		if(isOccupied){
			return -1;//正在被进行数据同步
		}
		isOccupied = true;
		
		String filter = "where id>"+from;
		Long count = UserClient.getUserCountWithFilter(filter);
		int numPerSyn = 1000;
		
		int ret = 0;
		for(int i=0; i<count; i+=numPerSyn){
			List<User> users = UserClient.findUsers(i, numPerSyn, filter);
			for(User user:users){
				try{
					boolean flag = synUserToLocal(user);
					/*if(flag)
						ret++;*/
					ret++;
				}catch(Exception e){
					isOccupied = false;
					return ret;
				}
			}
		}
		isOccupied = false;
		return ret;
	}
	
	//传入一条全局用户记录
	public boolean synUserToLocal(User user){
		if(user==null)
			return false;
		
		List<Fuser> fusers = userService.findByProperty("floginName", user.getUserName()); 
		if(fusers!=null && fusers.size()>0){
			Fuser fuser = fusers.get(0);
			if(fuser.getGlobalUserId()<=0){
				fuser.setGlobalUserId(user.getId());
			}
			boolean flag = Utils.isUserInfoEqual(user, fuser);
			if(!flag){
				fuser.setFloginPassword(user.getPassword());
				fuser.setFemail(user.getEmail());
				if(user.getEmail()==null || user.getEmail().length()==0){
					fuser.setFisMailValidate(false);
				}
				fuser.setFtelephone(user.getMobile());
				if(user.getMobile()==null || user.getMobile().length()==0){
					fuser.setFisTelValidate(false);
				}
			}
			userService.updateObj(fuser);  
		}else{//远程用户在本地注册
			Fuser fuser = new Fuser(); 
			fuser.setFloginName(user.getUserName());
			fuser.setFloginPassword(user.getPassword());
			if(user.getEmail()!=null){
				fuser.setFemail(user.getEmail()) ;
				fuser.setFnickName(user.getEmail().split("@")[0]) ; 
			} else{
				fuser.setFnickName(user.getUserName());
			}
			fuser.setFregisterTime(new Timestamp(user.getCreateTime()*1000)) ;
			fuser.setFloginPassword(user.getPassword()) ;
			fuser.setFtradePassword(null) ;
			fuser.setFstatus(UserStatusEnum.NORMAL_VALUE) ;
			fuser.setFlastLoginTime(Utils.getTimestamp()) ;
			fuser.setFlastUpdateTime(Utils.getTimestamp()) ;
			fuser.setFlastUpdateZhongdouTime(Utils.getTimestamp()) ;
			fuser.setFregisterIp(user.getRegisterIp());//注册IP 
			fuser.setFsourceUrl(user.getSourceUrl());//来源url
			fuser.setGlobalUserId(user.getId()); 
			boolean saveFlag = false ;
			try {
				saveFlag = frontUserService.saveRegister(fuser) ; 
//				//推广
//				Factivitytype factivitytype = this.frontActivityService.findFactivityTypeById(ActivityTypeEnum.REGISTER) ;
//				this.frontActivityService.updateCompleteOneActivity(factivitytype, fuser) ;
				return saveFlag;
			} catch (Exception e) {
				e.printStackTrace(); 
			}
		}
		return true;
	
	}
	
	//传入本地一条用户记录
	public boolean synFuserToGlobal(Fuser fuser){
		if(fuser == null)
			return false;
		
		User user = UserClient.findByUsername( fuser.getFloginName() );
		if(user!=null){ 
			if(fuser.getGlobalUserId()<=0){
				fuser.setGlobalUserId(user.getId());
			}
			boolean flag = Utils.isUserInfoEqual(user, fuser);
			if(!flag){
				fuser.setFloginPassword(user.getPassword());
				fuser.setFemail(user.getEmail());
				if(user.getEmail()==null || user.getEmail().length()==0){
					fuser.setFisMailValidate(false);
				}
				fuser.setFtelephone(user.getMobile());
				if(user.getMobile()==null || user.getMobile().length()==0){
					fuser.setFisTelephoneBind(false);
				}
			}
			userService.updateObj(fuser); 
		}else{
			user =  new User();
			user.setUserName(fuser.getFloginName());
			user.setPassword(fuser.getFloginPassword());
			user.setEmail(fuser.getFemail());
			user.setMobile(fuser.getFtelephone());
			user.setSex(0);
			user.setRegisterIp(fuser.getFregisterIp());
			user.setRegisterUrl("http://www.zhgtrade.com");
			if(fuser.getFregisterTime()==null)
				user.setCreateTime(Utils.getTimeLong()/1000);
			else
				user.setCreateTime(fuser.getFregisterTime().getTime()/1000);
			user.setSourceUrl(fuser.getFsourceUrl());
			int globalUserId = UserClient.register(user);
			if(globalUserId>0){
				fuser.setGlobalUserId(globalUserId);
				userService.updateObj(fuser); 
			}else{
				System.out.println("远程注册"+fuser.getFloginName()+"失败");
				return false;
			}
		} 
		return true;
	}
	
}
