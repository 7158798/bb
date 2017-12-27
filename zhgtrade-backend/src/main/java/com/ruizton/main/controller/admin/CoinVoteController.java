package com.ruizton.main.controller.admin;

import com.ruizton.main.Enum.CoinVoteStatusEnum;
import com.ruizton.main.controller.BaseController;
import com.ruizton.main.model.Fcoinvote;
import com.ruizton.main.model.Fcoinvotelog;
import com.ruizton.main.service.admin.*;
import com.ruizton.main.service.front.CoinVoteService;
import com.ruizton.main.service.front.UtilsService;
import com.ruizton.util.Utils;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class CoinVoteController extends BaseController {
	@Autowired
	private VirtualCoinService virtualCoinService ;
	@Autowired
	private WithdrawFeesService withdrawFeesService ;
	@Autowired
	private AdminService adminService ;
	@Autowired
	private FeeService feeService;
	@Autowired
	private PoolService poolService;
	@Autowired
	private UtilsService utilsService ;
	@Autowired
	private CoinVoteService coinVoteService ;
	
	//每页显示多少条数据
	private int numPerPage = Utils.getNumPerPage();
	
	@RequestMapping("/ssadmin/coinVoteLogList")
	@RequiresPermissions("ssadmin/coinVoteLogList.html")
	public ModelAndView coinVoteLogList(HttpServletRequest request) throws Exception{
		
		ModelAndView modelAndView = new ModelAndView() ;
		modelAndView.setViewName("ssadmin/coinvote/coinVoteLogList") ;
		//当前页
		int currentPage = 1;
		//搜索关键字
		String keyWord = request.getParameter("keywords");
		String orderField = request.getParameter("orderField");
		String orderDirection = request.getParameter("orderDirection");
		
		if(request.getParameter("pageNum") != null){
			currentPage = Integer.parseInt(request.getParameter("pageNum"));
		}
		StringBuffer filter = new StringBuffer();
		filter.append(" where 1=1 \n");
		if(keyWord != null && keyWord.trim().length() >0){
			filter.append("and (fuser.floginName like '%"+keyWord+"%' OR \n");
			filter.append("fuser.fnickName like '%"+keyWord+"%' OR \n");
			filter.append("fuser.fid like '%"+keyWord+"%') \n");
			modelAndView.addObject("keywords", keyWord);
		}
		
		if(orderField != null && orderField.trim().length() >0){
			filter.append("order by "+orderField+"\n");
		}else{
			filter.append("order by fid \n");
		}
		if(orderDirection != null && orderDirection.trim().length() >0){
			filter.append(orderDirection+"\n");
		}else{
			filter.append("desc \n");
		}
		List<Fcoinvotelog> list = this.utilsService.list2((currentPage-1)*numPerPage, numPerPage, filter+"",true,Fcoinvotelog.class);
		modelAndView.addObject("coinVoteLogList", list);
		modelAndView.addObject("numPerPage", numPerPage);
		modelAndView.addObject("rel", "coinVoteLogList");
		modelAndView.addObject("currentPage", currentPage);
		//总数量
		modelAndView.addObject("totalCount",this.utilsService.count(filter+"",Fcoinvotelog.class));
		return modelAndView ;
	}
	
	
	@RequestMapping("/ssadmin/coinVoteList")
	@RequiresPermissions("ssadmin/coinVoteList.html")
	public ModelAndView Index(HttpServletRequest request) throws Exception{
		
		ModelAndView modelAndView = new ModelAndView() ;
		modelAndView.setViewName("ssadmin/coinvote/coinVoteList") ;
		//当前页
		int currentPage = 1;
		//搜索关键字
		String keyWord = request.getParameter("keywords");
		String orderField = request.getParameter("orderField");
		String orderDirection = request.getParameter("orderDirection");
		
		if(request.getParameter("pageNum") != null){
			currentPage = Integer.parseInt(request.getParameter("pageNum"));
		}
		StringBuffer filter = new StringBuffer();
		filter.append(" where 1=1 \n");
		if(keyWord != null && keyWord.trim().length() >0){
			filter.append("and (fcnName like '%"+keyWord+"%' OR \n");
			filter.append("fenName like '%"+keyWord+"%' OR \n");
			filter.append("fshortName like '%"+keyWord+"%' )\n");
			modelAndView.addObject("keywords", keyWord);
		}
		if(orderField != null && orderField.trim().length() >0){
			filter.append("order by "+orderField+"\n");
		}else{
			filter.append("order by fid \n");
		}
		if(orderDirection != null && orderDirection.trim().length() >0){
			filter.append(orderDirection+"\n");
		}else{
			filter.append("desc \n");
		}
		List<Fcoinvote> list = this.utilsService.list((currentPage-1)*numPerPage, numPerPage, filter+"",true,Fcoinvote.class);
		modelAndView.addObject("coinVoteList", list);
		modelAndView.addObject("numPerPage", numPerPage);
		modelAndView.addObject("rel", "coinVoteList");
		modelAndView.addObject("currentPage", currentPage);
		//总数量
		modelAndView.addObject("totalCount",this.utilsService.count(filter+"",Fcoinvote.class));
		return modelAndView ;
	}
	
	
	@RequestMapping("ssadmin/goVirtualCoinVoteJSP")
	public ModelAndView goVirtualCoinVoteJSP(HttpServletRequest request) throws Exception{
		String url = request.getParameter("url");
		ModelAndView modelAndView = new ModelAndView() ;
		modelAndView.setViewName(url) ;
		if(request.getParameter("uid") != null){
			int fid = Integer.parseInt(request.getParameter("uid"));
			Fcoinvote fcoinvote = this.coinVoteService.findFcoinvote(fid) ;
			modelAndView.addObject("fcoinvote", fcoinvote);
		}
		return modelAndView;
	}
	
	@RequestMapping("ssadmin/saveCoinVote")
	@RequiresPermissions("ssadmin/saveCoinVote.html")
	public ModelAndView saveCoinVote(HttpServletRequest request,
			String fcnName,
			String fenName,
			String fshortName,
			String furl
			) throws Exception{
		Fcoinvote fcoinvote = new Fcoinvote() ;
		
		fcoinvote.setFcnName(fcnName) ;
		fcoinvote.setFenName(fenName) ;
		fcoinvote.setFurl(furl) ;
		fcoinvote.setFshortName(fshortName) ;
		fcoinvote.setFstatus(CoinVoteStatusEnum.ABNORMAL_VALUE) ;
		this.coinVoteService.saveFcoinvote(fcoinvote) ;
		
		ModelAndView modelAndView = new ModelAndView() ;
		modelAndView.setViewName("ssadmin/comm/ajaxDone") ;
		modelAndView.addObject("statusCode",200);
		modelAndView.addObject("message","新增成功");
		modelAndView.addObject("callbackType","closeCurrent");
		return modelAndView;
	}
	
	@RequestMapping("ssadmin/updateCoinVote")
	@RequiresPermissions("ssadmin/updateCoinVote.html")
	public ModelAndView updateCoinVote(HttpServletRequest request,
			String fcnName,
			String fenName,
			String fshortName,
			String furl
			) throws Exception{
		int fid = Integer.parseInt(request.getParameter("uid"));
		ModelAndView modelAndView = new ModelAndView() ;

		Fcoinvote fcoinvote = this.coinVoteService.findFcoinvote(fid) ;
		fcoinvote.setFcnName(fcnName) ;
		fcoinvote.setFenName(fenName) ;
		fcoinvote.setFurl(furl) ;
		fcoinvote.setFshortName(fshortName) ;
		this.coinVoteService.updateFcoinvote(fcoinvote) ;
		
		modelAndView.setViewName("ssadmin/comm/ajaxDone") ;
		modelAndView.addObject("statusCode",200);
		modelAndView.addObject("message","更新成功");
		modelAndView.addObject("callbackType","closeCurrent");
		return modelAndView;
	}
	
	@RequestMapping("ssadmin/updateCoinVoteStatus1")
	@RequiresPermissions("ssadmin/updateCoinVoteStatus1.html")
	public ModelAndView updateCoinVoteStatus1(HttpServletRequest request) throws Exception{
		int fid = Integer.parseInt(request.getParameter("uid"));
		ModelAndView modelAndView = new ModelAndView() ;
		
		Fcoinvote fcoinvote = this.coinVoteService.findFcoinvote(fid) ;
		fcoinvote.setFstatus(CoinVoteStatusEnum.NORMAL_VALUE) ;
		this.coinVoteService.updateFcoinvote(fcoinvote) ;
		
		modelAndView.setViewName("ssadmin/comm/ajaxDone") ;
		modelAndView.addObject("statusCode",200);
		modelAndView.addObject("message","更新成功");
		/*modelAndView.addObject("callbackType","closeCurrent");*/
		return modelAndView;
	}
	
	@RequestMapping("ssadmin/updateCoinVoteStatus2")
	@RequiresPermissions("ssadmin/updateCoinVoteStatus2.html")
	public ModelAndView updateCoinVoteStatus2(HttpServletRequest request) throws Exception{
		int fid = Integer.parseInt(request.getParameter("uid"));
		ModelAndView modelAndView = new ModelAndView() ;
		
		Fcoinvote fcoinvote = this.coinVoteService.findFcoinvote(fid) ;
		fcoinvote.setFstatus(CoinVoteStatusEnum.ABNORMAL_VALUE) ;
		this.coinVoteService.updateFcoinvote(fcoinvote) ;
		
		modelAndView.setViewName("ssadmin/comm/ajaxDone") ;
		modelAndView.addObject("statusCode",200);
		modelAndView.addObject("message","更新成功");
		/*modelAndView.addObject("callbackType","closeCurrent");*/
		return modelAndView;
	}


}
