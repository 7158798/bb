package com.ruizton.main.service.front;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ruizton.main.Enum.*;
import com.ruizton.main.comm.ValidateMap;
import com.ruizton.main.dao.*;
import com.ruizton.main.model.*;
import com.ruizton.main.mq.MessageQueueService;
import com.ruizton.main.mq.QueueConstants;
import com.ruizton.main.service.BaseService;
import com.ruizton.main.service.admin.LogService;
import com.ruizton.util.*;
import com.zhguser.UserClient;
import com.zhguser.service.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Pipeline;

import javax.servlet.http.HttpSession;
import java.sql.Timestamp;
import java.util.*;
import java.util.concurrent.ExecutorService;

@Service
public class FrontUserService extends BaseService {
	private static final Logger log = LoggerFactory.getLogger(FrontUserService.class);
	@Autowired
	private MessageQueueService messageQueueService ; 
	@Autowired
	private FuserDAO fuserDAO ; 
	@Autowired
	private EmailvalidateDAO emailvalidateDAO ;
	@Autowired
	private FvalidateemailDAO validateemailsDAO ;
	@Autowired
	private FwalletDAO fwalletDAO ;
	@Autowired
	private FbankinfoDAO fbankinfoDAO ;
	@Autowired
	private FscoreDAO fscoreDAO ;
	@Autowired
	private FvirtualcointypeDAO fvirtualcointypeDAO ;
	@Autowired
	private FvirtualwalletDAO fvirtualwalletDAO ;
	@Autowired
	private FvirtualaddressDAO fvirtualaddressDAO ;
	@Autowired
	private FvirtualaddressWithdrawDAO fvirtualaddressWithdrawDAO ;
	@Autowired
	private FbankinfoWithdrawDAO fbankinfoWithdrawDAO ;
	@Autowired
	private FsystemargsDAO fsystemargsDAO ;
	@Autowired
	private FapiDAO fapiDAO ;
	@Autowired
	private ValidateMap validateMap ;
	@Autowired
	private FlogDAO flogDAO ;
	@Autowired
	private FpoolDAO fpoolDAO ;
	@Autowired
	private FusersettingDAO fusersettingDAO ;
	@Autowired
	private FintrolinfoDAO introlinfoDAO;
	@Autowired
	private FvouchersDAO vouchersDAO;
	@Autowired
	private FmessageDAO messageDAO;
	@Autowired
	private JedisPool jedisPool;
	@Autowired
	private ExecutorService executorService;
	@Autowired
	private LogService logService;
	
	public boolean nickValidated(String name) throws Exception {
		boolean flag = false ;
		if(name!=null && !name.trim().equals("")){
			List<Fuser> list = fuserDAO.findByProperty("floginName", name) ;
			if(list.size()>0){
				flag = true ;
			}
		}
		return flag ;
	}

	/**
	 * 发送用户登录通知
	 */
	public void sendLoginNotice(String sessionId, int userId, Fuser fuser) {
		try (Jedis jedis = jedisPool.getResource(); Pipeline pip = jedis.pipelined()) {
			pip.hset("user:session", sessionId, userId + "");
			pip.hset("user:session:info", sessionId, toJSON(fuser));
			pip.publish("user:login", sessionId);
		} catch (Exception e) {
			log.error("sendLoginNotice fails " + e.getLocalizedMessage());
		}
	}

	private ObjectMapper objectMapper = new ObjectMapper();

	private String toJSON(Object obj) {
		try {
			return objectMapper.writeValueAsString(obj);
		} catch (JsonProcessingException e) {
			return null;
		}
	}

	/**
	 * 发送用户退出通知
	 */
	public void sendLogoutNotice(String sessionId, int userId) {
		try (Jedis jedis = jedisPool.getResource(); Pipeline pip = jedis.pipelined()) {
			pip.hdel("user:session", sessionId);
			pip.hdel("user:session:info", sessionId);
			pip.publish("user:logout", userId + "");
		} catch (Exception e) {
			log.error("sendLogoutNotice fails " + e.getLocalizedMessage());
		}
	}
	
	public boolean saveRegister(Fuser fuser) throws Exception{   
		fuser.setFloginName(fuser.getFloginName().toLowerCase()) ;
		fuser.setFregisterTime(Utils.getTimestamp());
		fuser.setFstatus(UserStatusEnum.NORMAL_VALUE);
		fuser.setFlastLoginTime(Utils.getTimestamp());
		fuser.setFlastUpdateTime(Utils.getTimestamp());
		fuser.setFlastUpdateZhongdouTime(Utils.getTimestamp());
		fuser.setFgrade(UserGradeEnum.LEVEL0);
		fuser.setFneedFee(true);

		//初始化钱包
		Fwallet fwallet = new Fwallet() ;
		fwallet.setFtotalRmb(0F) ;
		fwallet.setFfrozenRmb(0F) ;
		fwallet.setFlastUpdateTime(Utils.getTimestamp()) ;
		fwallet.setFborrowCny(0d);
		fwallet.setfHaveAppointBorrowCny(0d);
		fwallet.setFcanLendCny(0d);
		fwallet.setFfrozenLendCny(0d);
		fwallet.setFalreadyLendCny(0d);
		this.fwalletDAO.save(fwallet) ;
		fuser.setFwallet(fwallet) ;

		this.fuserDAO.save(fuser) ;

		updateInitAccount(fuser);

		return true ;
	}

	public void updateInitAccount(Fuser fuser){
		//用户基本设置信息表
		Fusersetting fusersetting = new Fusersetting() ;
		fusersetting.setFisAutoReturnToAccount(false) ;
		fusersetting.setFuser(fuser) ;
		fusersetting.setFticketQty(0d);
		fusersetting.setFsendDate(null);
		fusersetting.setFissend(false);
		this.fusersettingDAO.save(fusersetting) ;
		fuser.setFusersetting(fusersetting) ;

		//积分
		Fscore fscore = new Fscore() ;
		fscore.setFlevel(1) ;
		fscore.setFscore(0) ;
		fscore.setFgroupqty(0);
		fscore.setFisopen(false);
		fscore.setFfarmlevel(0);
//			fscore.setFtreeqty(0);
//			fscore.setFkillQty(0);
		fscore.setFissend(false);
		fscore.setFissign(false);
		this.fscoreDAO.save(fscore) ;
		fuser.setFscore(fscore) ;

		//虚拟钱包
		List<Fvirtualcointype> fvirtualcointypes = this.fvirtualcointypeDAO.findByProperty("fstatus", VirtualCoinTypeStatusEnum.Normal) ;
		for (Fvirtualcointype fvirtualcointype : fvirtualcointypes) {
			Fvirtualwallet fvirtualwallet = new Fvirtualwallet() ;
			fvirtualwallet.setFtotal(0F) ;
			fvirtualwallet.setFfrozen(0F) ;
			fvirtualwallet.setFvirtualcointype(fvirtualcointype) ;
			fvirtualwallet.setFuser(fuser) ;
			fvirtualwallet.setFlastUpdateTime(Utils.getTimestamp()) ;
			fvirtualwallet.setFborrowBtc(0d);
			fvirtualwallet.setfHaveAppointBorrowBtc(0d);
			fvirtualwallet.setFcanlendBtc(0d);
			fvirtualwallet.setFfrozenLendBtc(0d);
			fvirtualwallet.setFalreadyLendBtc(0d);
			this.fvirtualwalletDAO.save(fvirtualwallet) ;
		}
		//对外提现的虚拟地址
		for (Fvirtualcointype fvirtualcointype : fvirtualcointypes) {
			FvirtualaddressWithdraw fvirtualaddressWithdraw = new FvirtualaddressWithdraw() ;
			fvirtualaddressWithdraw.setFadderess(null) ;
			fvirtualaddressWithdraw.setFcreateTime(Utils.getTimestamp()) ;
			fvirtualaddressWithdraw.setFuser(fuser) ;
			fvirtualaddressWithdraw.setFvirtualcointype(fvirtualcointype) ;
			this.fvirtualaddressWithdrawDAO.save(fvirtualaddressWithdraw) ;
		}

		//充值的银行账号
		Fbankinfo fbankinfo = new Fbankinfo() ;
		fbankinfo.setFcreateTime(Utils.getTimestamp()) ;
		fbankinfo.setFstatus(BankInfoStatusEnum.NORMAL_VALUE) ;
		fbankinfo.setFuser(fuser) ;
		this.fbankinfoDAO.save(fbankinfo) ;

		//提现的银行账号
		FbankinfoWithdraw fbankinfoWithdraw = new FbankinfoWithdraw() ;
		fbankinfoWithdraw.setFcreateTime(Utils.getTimestamp()) ;
		fbankinfoWithdraw.setFstatus(BankInfoWithdrawStatusEnum.NORMAL_VALUE) ;
		fbankinfoWithdraw.setFuser(fuser) ;
		this.fbankinfoWithdrawDAO.save(fbankinfoWithdraw) ;

		//发送激活邮件
		String UUID = Utils.UUID() ;
		//发送给用户的邮件
		Fvalidateemail validateemails = new Fvalidateemail() ;
		validateemails.setFtitle(this.getSystemArgs(ConstantKeys.englishName)+"注册激活激活邮件") ;
		validateemails.setFcontent(
				this.getSystemArgs(ConstantKeys.mailValidateContent)
						.replace("#firstLevelDomain#", this.getSystemArgs(ConstantKeys.firstLevelDomain))
						.replace("#url#", Constants.Domain+"/validate/mail_validate.html?uid="+fuser.getFid()+"&&uuid="+UUID)
						.replace("#fulldomain#", this.getSystemArgs(ConstantKeys.fulldomain))
						.replace("#englishName#", this.getSystemArgs(ConstantKeys.englishName))) ;
		validateemails.setFcreateTime(Utils.getTimestamp()) ;
		validateemails.setFstatus(ValidateMailStatusEnum.Not_send) ;
		validateemails.setFuser(fuser) ;
		this.validateemailsDAO.save(validateemails) ;

		//验证并对应到用户的UUID
		Emailvalidate emailvalidate = new Emailvalidate() ;
		emailvalidate.setFcreateTime(Utils.getTimestamp()) ;
		emailvalidate.setFuser(fuser) ;
		emailvalidate.setFuuid(UUID) ;
		emailvalidate.setType(SendMailTypeEnum.ValidateMail) ;
		this.emailvalidateDAO.save(emailvalidate) ;

		//加入邮件队列
		//this.taskList.returnMailList(validateemails.getFid()) ;

		this.validateMap.putMailMap(fuser.getFid()+"_"+SendMailTypeEnum.ValidateMail, emailvalidate) ;

		//mq yujie
		Fuser new_fuser=new Fuser();
		new_fuser.setFid(fuser.getFid());
		validateemails.setFuser(new_fuser);
		messageQueueService.publish(QueueConstants.EMAIL_COMMON_QUEUE, validateemails);

		this.fuserDAO.attachDirty(fuser);
	}
	
	public Fuser updateCheckLogin(Fuser fuser,String ip){
		//先同步该用户名
		Fuser flag = null ;
		if(Constants.ConnectUserDbFlag){
			User user = UserClient.findByUsername(fuser.getFloginName());
			List<Fuser> fusers = fuserDAO.findByProperty("floginName", fuser.getFloginName());
			if(fusers!=null && fusers.size()>0){
				flag = fusers.get(0);
			}
			if(flag != null ){
				if(user != null){//判断是否更新本地信息
					if(flag.getGlobalUserId()<=0){
						flag.setGlobalUserId(user.getId());
					}
					boolean ret = Utils.isUserInfoEqual(user, flag);
					if(ret == false){
						flag.setFloginPassword(user.getPassword());
						if(user.getEmail()==null || user.getEmail().length()==0){
							fuser.setFisMailValidate(false);
						}
						flag.setFemail(user.getEmail());
						if(user.getMobile()==null || user.getMobile().length()==0){
							fuser.setFisTelephoneBind(false);
						}
						flag.setFtelephone(user.getMobile());
					}
					fuserDAO.attachDirty(flag);
					
				}else{//同步到远程服务器
					user =  new User();
					user.setUserName(flag.getFloginName());
					user.setPassword(flag.getFloginPassword());
					user.setEmail(flag.getFemail());
					user.setMobile(flag.getFtelephone());
					user.setSex(0);
					user.setRegisterIp(flag.getFregisterIp());
					user.setRegisterUrl("http://www.zhgtrade.com");
					if(flag.getFregisterTime()==null)
						user.setCreateTime(Utils.getTimeLong()/1000);
					else
						user.setCreateTime(flag.getFregisterTime().getTime()/1000);
					user.setSourceUrl(flag.getFsourceUrl());
					int globalUserId = UserClient.register(user);
					if(globalUserId>0){
						flag.setGlobalUserId(globalUserId); 
						fuserDAO.attachDirty(flag);
					}else{
						System.out.println("远程注册"+fuser.getFloginName()+"失败"); 
					}
				}
			}else{
				if(user!=null){//远程用户同步到本地
					flag = new Fuser(); 
					flag.setFloginName(user.getUserName());
					flag.setFloginPassword(user.getPassword());
					if(user.getEmail()!=null){
						flag.setFemail(user.getEmail()) ;
						flag.setFnickName(user.getEmail().split("@")[0]) ; 
					} else{
						flag.setFnickName(user.getUserName());
					}
					flag.setFregisterTime(new Timestamp(user.getCreateTime()*1000)) ;
					flag.setFloginPassword(user.getPassword()) ;
					flag.setFtradePassword(null) ;
					flag.setFstatus(UserStatusEnum.NORMAL_VALUE) ;
					flag.setFlastLoginTime(Utils.getTimestamp()) ;
					flag.setFlastUpdateTime(Utils.getTimestamp()) ;
					flag.setFlastUpdateZhongdouTime(Utils.getTimestamp()) ;
					flag.setFregisterIp(user.getRegisterIp());//注册IP 
					flag.setFsourceUrl(user.getSourceUrl());//来源url
					flag.setGlobalUserId(user.getId()); 
					boolean saveFlag = false ;
					try {
						saveFlag = saveRegister(fuser) ; 
	//					//推广
	//					Factivitytype factivitytype = this.frontActivityService.findFactivityTypeById(ActivityTypeEnum.REGISTER) ;
	//					this.frontActivityService.updateCompleteOneActivity(factivitytype, fuser) ;
					} catch (Exception e) {
						e.printStackTrace(); 
					}
				}else{//本地远程均不存在该用户
					
				}
			}
		}
		
		flag = null;
		try{
			Map<String, Object> map = new HashMap<String, Object>() ;
			if(fuser.getFloginName()!=null){
				map.put("floginName", fuser.getFloginName().toLowerCase()) ;
				map.put("floginPassword", Utils.MD5(fuser.getFloginPassword())) ;
				
				List<Fuser> list = this.fuserDAO.findByMap(map) ;
				if(list.size()>0){
					flag = list.get(0) ;
				}
			}else{
				map.put("femail", fuser.getFemail().toLowerCase()) ;
				map.put("floginPassword", Utils.MD5(fuser.getFloginPassword())) ;
				
				List<Fuser> list = this.fuserDAO.findByMap(map) ;
				if(list.size()>0){
					flag = list.get(0) ;
				}

			}
		}catch(Exception e){
			e.printStackTrace() ;
			fuser = null ;
			throw new RuntimeException() ;
		}
		return flag ;
	}
	
	public Map<Integer, Fvirtualwallet> findVirtualWallet(int fuid){
		TreeMap<Integer, Fvirtualwallet> treeMap = new TreeMap<Integer, Fvirtualwallet>(new Comparator<Integer>() {

			public int compare(Integer o1, Integer o2) {
				return o1.compareTo(o2) ;
			}
			
		}) ;
		List<Fvirtualwallet> fvirtualwallets = this.fvirtualwalletDAO.find(fuid, VirtualCoinTypeStatusEnum.Normal) ;
		for (Fvirtualwallet fvirtualwallet : fvirtualwallets) {
			treeMap.put(fvirtualwallet.getFvirtualcointype().getFid(), fvirtualwallet) ;
		}
		return treeMap ;
	}

	public Fvirtualwallet findVirtualWalletNative(int fuid,int fcoinId) {
		return fvirtualwalletDAO.findVirtualWalletNative(fuid, fcoinId);
	}

	public Fvirtualwallet findVirtualWalletByUser(int fuid,int virtualCoinId){
		Fvirtualwallet fvirtualwallet = this.fvirtualwalletDAO.findVirtualWallet(fuid, virtualCoinId) ;
		return fvirtualwallet ;
	}
	
	public Fuser findById(int id){
		Fuser fuser = this.fuserDAO.findById(id) ;
		return fuser ;
	}
	
	public int findIntroUserCount(Fuser fuser){
		return this.fuserDAO.findByProperty("fIntroUser_id.fid", fuser.getFid()).size() ;
	}
	
	public Fuser findById4WithDraw(int id) throws Exception{
		//提现，需要银行信息
		Fuser fuser = this.fuserDAO.findById(id) ;
		fuser.getFwallet().getFtotalRmb() ;
		return fuser ;
	}
	
	public Fbankinfo findUserBankInfo(int uid){
		Fbankinfo fbankinfo = this.fbankinfoDAO.findUserBankInfo(uid) ;
		return fbankinfo ;
	}
	
	public Fuser findByIdWithBankInfos(int id){
		Fuser fuser = this.fuserDAO.findById(id) ;
		fuser.getFbankinfos().size() ;
		return fuser ;
	}
	
	/*
	public boolean isEmailExists(String email) throws Exception{
		boolean flag = false ;
		List<Fuser> list = this.fuserDAO.findByProperty("femail", email) ;
		flag = list.size()>0 ;
		return flag ;
	}*/
	public boolean isEmailExists(String email){
		boolean flag = false;
		if(Constants.ConnectUserDbFlag)
			flag = UserClient.isEmailExists(email);
		else{
			List<Fuser> list = this.fuserDAO.findByProperty("femail", email) ;
			flag = list.size()>0 ;
		}
		return flag;
	}
	
	/*
	 * public boolean isTelephoneExists(String telephone) throws Exception{
		boolean flag = false ;
		List<Fuser> list = this.fuserDAO.findByProperty("ftelephone", telephone) ;
		flag = list.size()>0 ;
		return flag ;
	}
	*
	*/
	public boolean isTelephoneExists(String telephone){
		boolean flag = false;
		if(Constants.ConnectUserDbFlag)
			flag = UserClient.isMobileExists(telephone);
		else {
			List<Fuser> list = this.fuserDAO.findByProperty("ftelephone", telephone) ;
			flag = list.size()>0 ;
		}
		return flag;
	}
	
	
	public boolean isUserNameExists(String userName){
		boolean flag = false;
		if(Constants.ConnectUserDbFlag)
			flag = UserClient.isUserNameExists(userName);
		else {
			List<Fuser> list = this.fuserDAO.findByProperty("floginName", userName) ;
			flag = list.size()>0 ;
		}
		return flag;
	}
	
	public void updateFUser(Fuser fuser,HttpSession session){
		this.fuserDAO.attachDirty(fuser) ;
		if(session!=null && session.getAttribute(Constants.USER_LOGIN_SESSION)!=null){
			session.setAttribute(Constants.USER_LOGIN_SESSION, fuser) ;
		}
	}
	
	public void updateFUser(Fuser fuser,Fvouchers fvouchers,HttpSession session){
		try {
			this.fuserDAO.attachDirty(fuser) ;
			if(fvouchers != null){
				this.vouchersDAO.save(fvouchers);
				Fmessage message = new Fmessage();
				message.setFcontent("恭喜您绑定手机成功，系统赠送您50元代金券，请注册查收！");
				message.setFcreateTime(Utils.getTimestamp());
				message.setFreceiver(fuser);
				message.setFstatus(MessageStatusEnum.NOREAD_VALUE);
				message.setFtitle("赠送代金券通知");
				this.messageDAO.save(message);
			}
			if(session!=null && session.getAttribute(Constants.USER_LOGIN_SESSION)!=null){
				session.setAttribute(Constants.USER_LOGIN_SESSION, fuser) ;
			}
		} catch (Exception e) {
			throw new RuntimeException();
		}
	}
	
	public void updateFuser(Fuser fuser){
		this.fuserDAO.attachDirty(fuser) ;
	}
	
	public void updateFuser(Fuser fuser,Fintrolinfo introlInfo,
			Fvirtualwallet fvirtualwallet,Fscore fscore){
		try {
			if(fuser != null){
				this.fuserDAO.attachDirty(fuser) ;
			}
			if(introlInfo != null){
				this.introlinfoDAO.save(introlInfo);
			}
			if(fvirtualwallet != null){
				this.fvirtualwalletDAO.attachDirty(fvirtualwallet);
			}
			if(fscore != null){
				this.fscoreDAO.attachDirty(fscore);
			}
		} catch (Exception e) {
			throw new RuntimeException();
		}
	}
	
	
	public List<Fuser> findUserByProperty(String key,Object value){
		return this.fuserDAO.findByProperty(key, value) ;
	}
	
	public void addBankInfo(Fbankinfo fbankinfo,Fuser fuser){
		try {
			Fbankinfo example = new Fbankinfo() ;
			example.setFuser(fuser) ;
			example.setFstatus(BankInfoStatusEnum.NORMAL_VALUE) ;
			List<Fbankinfo> fbankinfos = this.fbankinfoDAO.findByExample(example) ;
			for (Fbankinfo fbankinfo2 : fbankinfos) {
				fbankinfo2.setFstatus(BankInfoStatusEnum.ABNORMAL_VALUE) ;
				this.fbankinfoDAO.attachDirty(fbankinfo2) ;
			}
			this.fbankinfoDAO.save(fbankinfo) ;
		} catch (Exception e) {
		     throw new RuntimeException();
		}
	}
	
	public void updateBankInfoWithdraw(FbankinfoWithdraw fbankinfoWithdraw){
		try {
			fbankinfoWithdrawDAO.attachDirty(fbankinfoWithdraw) ;
		} catch (Exception e) {
			throw new RuntimeException();
		}
	}
	
	public Fwallet findFwalletByIdNative(int id){
		return this.fwalletDAO.findByIdNative(id) ;
	}

	public Fwallet findFwalletById(int id){
		return this.fwalletDAO.findById(id) ;
	}
	
	public boolean deleteAllUser() throws Exception{
		List<Fuser> fusers = this.fuserDAO.findAll() ;
		for (Fuser fuser : fusers) {
			this.fuserDAO.delete(fuser) ;
		}
		return true ;
	}
	
	public String getSystemArgs(String key){
		String value = null ;
		List<Fsystemargs> list = this.fsystemargsDAO.findByFkey(key) ;
		if(list.size()>0){
			value = list.get(0).getFvalue() ;
		}
		return value ;
	}
	
	public Fapi insertGetFapi(Fuser fuser){
		fuser = this.fuserDAO.findById(fuser.getFid()) ;
		Fapi fapi = fuser.getFapi() ;
		if(fapi==null){
			fapi = new Fapi() ;
			fapi.setFpartner(ApiCoder.encode(fuser.getFid())) ;
			fapi.setFsecret(Utils.UUID()) ;
			this.fapiDAO.attachDirty(fapi) ;
		}
		fuser.setFapi(fapi) ;
		this.fuserDAO.attachDirty(fuser) ;
		return fapi ;
	}
	
	public Fapi findFapiById(int id){
		return this.fapiDAO.findById(id) ;
	}
	
	public FbankinfoWithdraw findFbankinfoWithdrawByFuser(int fuid){
		return (FbankinfoWithdraw) this.fbankinfoWithdrawDAO.findByProperty("fuser.fid", fuid).get(0) ;
	}

	public FbankinfoWithdraw findFbankinfoWithdrawByFid(int fid){
		return (FbankinfoWithdraw) this.fbankinfoWithdrawDAO.findByProperty("fid", fid).get(0) ;
	}

	public List<FbankinfoWithdraw> findByFuser(int fuid){
		return this.fbankinfoWithdrawDAO.findByProperty("fuser.fid", fuid);
	}
	
	public boolean saveValidateEmail(Fuser fuser,boolean force) throws RuntimeException{
		boolean flag = false ;
		try {
			if(!force){
				//半小时内只能发送一次
				Emailvalidate ev = this.validateMap.getMailMap(fuser.getFid()+"_"+SendMailTypeEnum.ValidateMail) ;
				if(ev!=null && Utils.getTimestamp().getTime()-ev.getFcreateTime().getTime()<30*60*1000L){
					flag = false ;
					return flag ;
				}
			}
			
			
			//发送激活邮件
			String UUID = Utils.UUID() ;
			//发送给用户的邮件
			Fvalidateemail validateemails = new Fvalidateemail() ;
			validateemails.setFtitle(this.getSystemArgs(ConstantKeys.englishName)+"注册激活激活邮件") ;
			validateemails.setFcontent(
					this.getSystemArgs(ConstantKeys.mailValidateContent)
					.replace("#firstLevelDomain#", this.getSystemArgs(ConstantKeys.firstLevelDomain))
					.replace("#url#", Constants.Domain+"/validate/mail_validate.html?uid="+fuser.getFid()+"&&uuid="+UUID)
					.replace("#fulldomain#", this.getSystemArgs(ConstantKeys.fulldomain))
					.replace("#englishName#", this.getSystemArgs(ConstantKeys.englishName))) ;
			validateemails.setFcreateTime(Utils.getTimestamp()) ;
			validateemails.setFstatus(ValidateMailStatusEnum.Not_send) ;
			validateemails.setFuser(fuser) ;
			this.validateemailsDAO.save(validateemails) ;
			
			
			//验证并对应到用户的UUID
			Emailvalidate emailvalidate = new Emailvalidate() ;
			emailvalidate.setFcreateTime(Utils.getTimestamp()) ;
			emailvalidate.setFuser(fuser) ;
			emailvalidate.setFuuid(UUID) ;
			emailvalidate.setType(SendMailTypeEnum.ValidateMail) ;
			this.emailvalidateDAO.save(emailvalidate) ;
			
			//加入邮件队列
			//this.taskList.returnMailList(validateemails.getFid()) ;
			
			this.validateMap.putMailMap(fuser.getFid()+"_"+SendMailTypeEnum.ValidateMail, emailvalidate) ;
			
			flag = true ;
			
			//mq  yujie
			System.out.println("enter email validate");
			Fuser new_fuser=new Fuser();
			new_fuser.setFid(fuser.getFid());
			validateemails.setFuser(new_fuser);
			messageQueueService.publish(QueueConstants.EMAIL_COMMON_QUEUE, validateemails);
			return flag ;
			
			
		} catch (Exception e) {
			throw new RuntimeException() ;
		}
		
	}
	
	public void updateDisabledApi(Fuser fuser){
		try {
			Fapi fapi = fuser.getFapi() ;
			fuser.setFapi(null) ;
			this.fuserDAO.attachDirty(fuser) ;
			
			if(fapi!=null){
				this.fapiDAO.delete(fapi) ;
			}
		} catch (Exception e) {
			throw new RuntimeException();
		}
	}
	
	public List<Fapi> findFapiByProperty(String key,Object value){
		return this.fapiDAO.findByProperty(key, value) ;
	}

	public Fuser findFuserByFapi(Fapi fapi){
		List<Fuser> fusers = this.fuserDAO.findByProperty("fapi.fid", fapi.getFid()) ;
		return fusers.get(0) ;
	}
	
	public Fuser findByQQlogin(String openId){
		List<Fuser> fusers = this.fuserDAO.findByProperty("qqlogin", openId) ;
		if(fusers.size()>0){
			return fusers.get(0) ;
		}else{
			return null ;
		}
	}
	
	public Fscore findFscoreById(int id){
		return this.fscoreDAO.findById(id) ;
	}
	
	public Fusersetting findFusersetting(int fid){
		return this.fusersettingDAO.findById(fid) ;
	}
	
	public void updateFusersetting(Fusersetting fusersetting){
		this.fusersettingDAO.attachDirty(fusersetting);
	}
	
	public void updateCleanScore(){
		this.fusersettingDAO.updateCleanScore() ;
	}

	public Fuser findByMobile(String mobile){
		Assert.hasText(mobile);
		List<Fuser> users = fuserDAO.findByFtelephone(mobile);
		if(CollectionUtils.isEmpty(users)){
			return null;
		}
		return users.get(0);
	}

	public Fuser findByEmail(String email){
		Assert.hasText(email);
		List<Fuser> users = fuserDAO.findByFemail(email);
		if(CollectionUtils.isEmpty(users)){
			return null;
		}
		return users.get(0);
	}

	public Fuser findByZhgOpenId(String openId){
		List list = this.findUserByProperty("zhgOpenId", openId);
		if(CollectionUtils.isEmpty(list)){
			return null;
		}
		return (Fuser) list.get(0);
	}

	public Fuser findByWxOpenId(String wxOpenId){
		List list = this.findUserByProperty("wxOpenId", wxOpenId);
		if(CollectionUtils.isEmpty(list)){
			return null;
		}
		return (Fuser) list.get(0);
	}

	/*public List<Fuser> listForAwarding(Timestamp start, Timestamp end){
		return fuserDAO.listForAwarding(start, end);
	}*/

}
