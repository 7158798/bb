package com.zhgtrade.front.controller;

import com.alibaba.fastjson.JSON;
import com.ruizton.main.cache.data.RealTimeDataService;
import com.ruizton.main.comm.KeyValues;
import com.ruizton.main.dto.LatestDealData;
import com.ruizton.main.model.Farticle;
import com.ruizton.main.model.Farticletype;
import com.ruizton.main.service.front.FrontOthersService;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.ruizton.util.Constants;
import com.ruizton.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.HtmlUtils;

@Controller
@RequestMapping({"/guide", "/service"})
public class GuideController {

	@Autowired
	private FrontOthersService frontOthersService;
	@Autowired
	private RealTimeDataService realTimeDataService;

	private final int NUMBER_PER_SEARCH = 10;

//	/* 帮助中心页面 */
//	@RequestMapping("/help")
//	public String account(){
//		return "guide/help";
//	}


	@RequestMapping("/help")
	public ModelAndView help() throws Exception {
		return ourService(4, 1);
	}


	/**
	 * 列出5个文章，假如当前文章的位置在前5个，返回前五个，假如在后五个，返回后五个
	 * @param typeId
	 * @param currentPage
	 * @param indexPage,当前文章在当前页的位置
     * @return 五个文章标题
     */
	@ResponseBody
	@RequestMapping(value = "/articleIndex", produces = "application/json;charset=UTF-8" )
	public String articleIndex(int typeId, @RequestParam(required = false, defaultValue = "1") int currentPage, int indexPage){

		List<Farticle> list = frontOthersService.findFarticle(typeId, (currentPage - 1) * 10, 10);
		try{

			if(indexPage <= 5 && list.size() <= 5){
				list = list.subList(0, list.size());
			}else if(indexPage <=5 && list.size() > 5){
				list = list.subList(0, 5);
			}else if(indexPage > 5){
				list = list.subList(list.size() - 5, list.size());
			}
		}catch (Exception e){
			e.printStackTrace();
		}
		int count = frontOthersService.findFarticle(typeId);
		Map<String, String> map = new HashMap<String, String>();
		for (Farticle farticle: list) {
			map.put(farticle.getFid() + "", farticle.getFtitle());
		}
		if(count > 5){
			map.put("isPaging", "true");
		}
		String result = JSON.toJSONString(map);
//		result = "[" + result + "]";
		return result;
	}

	/**
	 * yujie guide api
	 * @return
     */
	@RequestMapping("/api")
	public ModelAndView api(){
		ModelAndView modelAndView = new ModelAndView() ;
		List<LatestDealData> conis = realTimeDataService.getLatestDealDataList();
		List<Farticletype> farticletypes = this.frontOthersService.findFarticleTypeAll() ;
		modelAndView.addObject("farticletypes", farticletypes);
		modelAndView.addObject("conis", conis);
		modelAndView.setViewName("guide/net_api");
		return modelAndView ;
	}

	/**
	 * 文章索引
	 * @param id
	 * @param currentPage
	 * @return
	 * @throws Exception
     */
	@RequestMapping("/index")
	public ModelAndView ourService(@RequestParam(required=false,defaultValue="4")int id, @RequestParam(required=false,defaultValue="1")int currentPage) throws Exception{//12,5,5
		ModelAndView modelAndView = new ModelAndView() ;
		List<KeyValues> articles = new ArrayList<KeyValues>() ;
		Farticletype farticletype = frontOthersService.findFarticleTypeById(id) ;
		List<Farticle> farticles = frontOthersService.findFarticle(id, (currentPage-1)*10, 10) ;
		List<Farticletype> farticletypes = this.frontOthersService.findFarticleTypeAll() ;
		for (int i = 0; i < farticletypes.size(); i++) {
			Farticletype type = farticletypes.get(i) ;
			List<Farticle> value = this.frontOthersService.findFarticle(type.getFid(), 0, 5) ;
			KeyValues kvs = new KeyValues() ;
			kvs.setKey(type) ;
			kvs.setValue(value) ;
			articles.add(kvs) ;
		}
		int total = this.frontOthersService.findFarticleCount(id) ;
		String pagin = generatePagin(total/10+(total%10==0?0:1), currentPage, "/guide/index.html?id="+id+"&") ;

		modelAndView.addObject("pageSize", 10);
		modelAndView.addObject("total", total);
		modelAndView.addObject("pageNow", currentPage);
		modelAndView.addObject("pagin",pagin) ;
		modelAndView.addObject("farticletype",farticletype) ;
		modelAndView.addObject("farticles", farticles) ;
		modelAndView.addObject("typeId", id) ;
		modelAndView.addObject("articles", articles);
		modelAndView.addObject("currentPage", currentPage);
		modelAndView.addObject("farticletypes", farticletypes);
		modelAndView.setViewName("guide/index");
		return modelAndView ;
	}

	@RequestMapping({"/article"})
	public ModelAndView article(int id) throws Exception{
		ModelAndView modelAndView = new ModelAndView();
		Farticle farticle = frontOthersService.findFarticleById(id);
		List<KeyValues> articles = new ArrayList<KeyValues>();
		Farticletype farticletype = frontOthersService.findFarticleTypeById(farticle.getFarticletype().getFid()) ;
		List<Farticletype> farticletypes = frontOthersService.findFarticleTypeAll() ;
		for (int i = 0; i < farticletypes.size(); i++) {
			Farticletype type = farticletypes.get(i) ;
			List<Farticle> value = frontOthersService.findFarticle(type.getFid(), 0, 5) ;
			KeyValues kvs = new KeyValues() ;
			kvs.setKey(type) ;
			kvs.setValue(value) ;
		}

		List<Farticle> list = frontOthersService.findArticleByProperty("farticletype.fid", farticletype.getFid());
		Collections.reverse(list);
		int index = indexArticle(list, id);

		//假如文章总数不到5个，把所有返回所有文章
		if(list.size() <= 5){
			modelAndView.addObject("articleSubling", list);
		}

		//返回当前文章的前一后三，不足由另一侧补齐5个
		if(list.size() > 5){
			if(index == 0){
				modelAndView.addObject("articleSubling", list.subList(0, 5));
			}else if(index > 0 && index < (list.size() - 3)){
				modelAndView.addObject("articleSubling", list.subList(index - 1, index + 4));
			}else{
				modelAndView.addObject("articleSubling", list.subList(index - (4 - (list.size() - index - 1)), list.size()));
			}
		}

		modelAndView.addObject("farticletype",farticletype) ;
		modelAndView.addObject("typeId",farticletype.getFid()) ;
		modelAndView.addObject("articles", articles) ;
		modelAndView.addObject("farticle", farticle) ;
		modelAndView.addObject("count", list.size());
		modelAndView.addObject("farticletypes", farticletypes);
		modelAndView.setViewName("guide/service") ;
		return modelAndView;
	}

	@RequestMapping("/result")
	public ModelAndView search(String keyword, @RequestParam(required = false, defaultValue = "1") int currentPage){
		ModelAndView modelAndView = new ModelAndView();
		keyword = keyword.trim();

		modelAndView.addObject("keyword", HtmlUtils.htmlEscape(keyword));
		String[] fbsArr = { "\\", "$", "(", ")", "*", "+", ".", "[", "]", "?", "^", "{", "}", "|" };
		for (String key : fbsArr) {
			if (keyword.contains(key)) {
				keyword = keyword.replace(key, "\\" + key);
			}
		}
		int total = 0;
		String[] keywords;
		if(keyword.contains(" ")){
			keywords = keyword.split(" ");
		}else{
			keywords = new String[]{keyword};
		}
		total = frontOthersService.countByArticleKeywords(keywords);
		List<Farticle> list = frontOthersService.findArticleByKeywords((currentPage - 1) * NUMBER_PER_SEARCH, NUMBER_PER_SEARCH, keywords);
		modelAndView.addObject("list", list);
		List<Farticletype> farticletypes = frontOthersService.findFarticleTypeAll() ;
		modelAndView.addObject("farticletypes", farticletypes);
		modelAndView.addObject("currentPage", currentPage);
		modelAndView.addObject("total", total);
		modelAndView.addObject("pageSize", NUMBER_PER_SEARCH);
		modelAndView.setViewName("guide/result");
		return modelAndView;
	}

	private String generatePagin(int total,int currentPage,String path){

		if(total<=0){
			return "" ;
		}

		StringBuffer sb = new StringBuffer() ;

		if(currentPage==1){
			sb.append("<li><a style='color:#FFFFFF;' class='current_ss' href='javascript:void(0)'>1</a></li>") ;
		}else{
			sb.append("<li><a href='"+path+"currentPage=1'>&lt</a></li>") ;
			sb.append("<li><a href='"+path+"currentPage=1'>1</a></li>") ;
		}

		if(currentPage==2){
			sb.append("<li><a style='color:#FFFFFF;' class='current_ss' href='javascript:void(0)'>2</a></li>") ;
		}else if(total>=2){
			sb.append("<li><a href='"+path+"currentPage=2'>2</a></li>") ;
		}

		if(currentPage>=7){
			sb.append("<li><a href='javascript:void(0)'>...</a></li>") ;
		}

		//前三页
		int begin = currentPage-3 ;
		begin = begin<=2?3:begin ;
		for(int i=begin;i<currentPage;i++){
			sb.append("<li><a href='"+path+"currentPage="+i+"'>"+i+"</a></li>") ;
		}

		if(currentPage!=1&&currentPage!=2){
			sb.append("<li><a style='color:#FFFFFF;' class='current_ss' href='javascript:void(0)'>"+currentPage+"</a></li>") ;
		}

		//后三页
		begin = currentPage+1;
		begin = begin<=2?3:begin ;
		int end = currentPage+4 ;
		if(currentPage<6){
			int tInt = 6- currentPage ;
			end = end+((tInt>3?3:tInt)) ;
		}
		for(int i=begin;i<end&&i<=total;i++){
			sb.append("<li><a href='"+path+"currentPage="+i+"'>"+i+"</a></li>") ;
		}


		if(total-currentPage==4){
			sb.append("<li><a href='"+path+"currentPage="+total+"'>"+total+"</a></li>") ;
		}else if(total-currentPage>3){
			sb.append("<li><a href='javascript:void(0)'>...</a></li>") ;
		}

		if(total>=11&&total-currentPage>4){
			sb.append("<li><a href='"+path+"currentPage="+total+"'>"+total+"</a></li>") ;
		}

		if(currentPage<total){
			sb.append("<li><a href='"+path+"currentPage="+total+"'>&gt</a></li>") ;
		}

		return sb.toString() ;
	}


	/**
	 * 文章在此类型所有文章的倒序位置
	 * @param list
	 * @param articleId
	 * @return
     */
	private int indexArticle(List<Farticle> list, int articleId){
		int index = 0;
		for(int i = 0; i < list.size(); i++){
			if(list.get(i).getFid() == articleId){
				return i;
			}
		}
		return -1;

	}

}
