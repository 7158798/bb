package com.ruizton.main.service.admin;

import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import com.ruizton.main.Enum.LogTypeEnum;
import com.ruizton.main.model.Fuser;
import com.ruizton.main.mq.MessageQueueService;
import com.ruizton.util.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ruizton.main.dao.FaboutDAO;
import com.ruizton.main.dao.FadminDAO;
import com.ruizton.main.dao.FlogDAO;
import com.ruizton.main.model.Fabout;
import com.ruizton.main.model.Fadmin;
import com.ruizton.main.model.Flog;

@Service
public class LogService {
	@Autowired
	private FlogDAO logDAO;
	@Autowired
	private MessageQueueService messageQueueService;

	public Flog findById(int id) {
		return this.logDAO.findById(id);
	}

	public void saveObj(Flog obj) {
		this.logDAO.save(obj);
	}

	public void deleteObj(int id) {
		Flog obj = this.logDAO.findById(id);
		this.logDAO.delete(obj);
	}

	public void updateObj(Flog obj) {
		this.logDAO.attachDirty(obj);
	}

	public List<Flog> findByProperty(String name, Object value) {
		return this.logDAO.findByProperty(name, value);
	}

	public List<Flog> findAll() {
		return this.logDAO.findAll();
	}

	public List<Flog> list(int firstResult, int maxResults,
			String filter,boolean isFY) {
		return this.logDAO.list(firstResult, maxResults, filter,isFY);
	}

	public void addLoginLog(Fuser fuser, String ip, String browser,String deviceBeforeCode){
		Flog flog = new Flog() ;
		flog.setFcreateTime(Utils.getTimestamp()) ;
		flog.setFtype(LogTypeEnum.User_LOGIN) ;
		flog.setFkey1(String.valueOf(fuser.getFid())) ;
		flog.setFkey2(fuser.getFloginName()) ;
		flog.setFkey3(ip);
		flog.setFkey4(browser);
		flog.setFkey5(deviceBeforeCode);
		this.saveObj(flog) ;

	}

	/**
	 * 查询用户最后登录的设备
	 * @param fuserId
	 * @return
	 */
	public String lastDeviceBeforeCode(int fuserId){
		return logDAO.lastDeviceBeforeCode(fuserId);
	}

	public static void main(String[] args) {
		Pattern pattern = Pattern.compile(";\\s?(\\S*?\\s?\\S*?)\\s?(Build)?/");
		Matcher matcher = pattern.matcher("Samsung P6200(GALAXY Tab)   Android 3.2     UC HD 2.3   Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10_6_3) AppleWebKit/534.31 (KHTML, like Gecko) Chrome/17.0.558.0 Safari/534.31 UCBrowser/2.3.1.257");
		String model = null;
		if (matcher.find()) {
			model = matcher.group(1).trim();
		}
		System.out.println(model);
	}


}