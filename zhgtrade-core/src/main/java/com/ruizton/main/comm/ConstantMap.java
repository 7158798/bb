package com.ruizton.main.comm;

//import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

//import com.ruizton.main.model.Question;
//import com.ruizton.main.service.front.QuestionService;
import com.ruizton.util.SpringContextUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.ruizton.main.Enum.LinkTypeEnum;
import com.ruizton.main.Enum.VirtualCoinTypeStatusEnum;
import com.ruizton.main.model.Ffriendlink;
import com.ruizton.main.model.Fsystemargs;
import com.ruizton.main.model.Fvirtualcointype;
import com.ruizton.main.service.admin.FriendLinkService;
import com.ruizton.main.service.admin.SystemArgsService;
import com.ruizton.main.service.front.FrontSystemArgsService;
import com.ruizton.main.service.front.FrontVirtualCoinService;

public class ConstantMap {
	private static final Logger log = LoggerFactory.getLogger(ConstantMap.class);
	@Autowired
	private FrontSystemArgsService frontSystemArgsService ;
	@Autowired
	private FrontVirtualCoinService frontVirtualCoinService ;
//	@Autowired
//	private FriendLinkService friendLinkService;
	@Autowired
	private SystemArgsService systemArgsService;
//	@Autowired
//	private QuestionService questionService;
	private Map<String, Object> map = new HashMap<String, Object>() ;

	private static ConstantMap GLOBAL_INSTANCE = null;
//	private static Comparator<Question> comparator = (q1, q2) ->{
//		if(q1.hashCode() > q2.hashCode()){
//			return 1;
//		}else {
//			return -1;
//		}
//	};

	@PostConstruct
	public void init(){
//		log.info("Init allquestion ==> ConstantMap.");
//		List<Question> slist = questionService.findAllQuestion(false);
//		slist.sort(comparator);
//		map.put("singleQuestions", slist);
//
//		List<Question> mlist = questionService.findAllQuestion(true);
//		mlist.sort(comparator);
//		map.put("mutipleQuestions", mlist);

		log.info("Init SystemArgs ==> ConstantMap.") ;
		Map<String, String> tMap = this.frontSystemArgsService.findAllMap() ;
		for (Map.Entry<String, String> entry : tMap.entrySet()) {
			this.put(entry.getKey(), entry.getValue()) ;
		}
		log.info("Init virtualCoinType ==> ConstantMap.") ;
		List<Fvirtualcointype> fvirtualcointypes= this.frontVirtualCoinService.findFvirtualCoinType(VirtualCoinTypeStatusEnum.Normal) ;
		map.put("virtualCoinType", fvirtualcointypes) ;
		map.put("webinfo", this.frontSystemArgsService.findFwebbaseinfoById(1)) ;

//		String filter = "where ftype="+LinkTypeEnum.QQ_VALUE;
//		List<Ffriendlink> ffriendlinks = this.friendLinkService.list(0, 0, filter, false);
//		map.put("ffriendlinks", ffriendlinks) ;

//		Fsystemargs arg1 = this.systemArgsService.findById(170);
//		Fsystemargs arg2 = this.systemArgsService.findById(171);
//		this.put(arg1.getFkey()+"URL", arg1.getFurl()) ;
//		this.put(arg2.getFkey()+"URL", arg2.getFurl()) ;
	}
	
	public Map<String, Object> getMap(){
		return this.map ;
	}
	
	public synchronized void put(String key,Object value){
		log.info("ConstantMap put key:"+key+",value:"+value+".") ;
		map.put(key, value) ;
	}

	public static Object getProperty(String key) {
		if (GLOBAL_INSTANCE == null) {
			GLOBAL_INSTANCE = SpringContextUtils.getBean(ConstantMap.class);
		}
		if (GLOBAL_INSTANCE != null) {
			return GLOBAL_INSTANCE.map.get(key);
		}
		return null;
	}

	public Object get(String key){
		return map.get(key) ;
	}
	
	public String getString(String key){
		return (String)map.get(key) ;
	}

	public int getInt(String key){
		return Integer.valueOf(this.getString(key));
	}

	public double getDouble(String key){
		return Double.valueOf(this.getString(key));
	}
}
