package com.zhgtrade.front.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ruizton.main.cache.data.RealTimeCmsDataService;
import com.ruizton.main.cache.data.RealTimeDataService;
import com.ruizton.main.cache.data.impl.RealTimePriceServiceImpl;
import com.ruizton.main.comm.ConstantMap;
import com.ruizton.main.dto.LatestDealData;
import com.ruizton.main.model.Farticle;
import com.ruizton.main.model.Fuser;
import com.ruizton.main.model.Fvirtualwallet;
import com.ruizton.main.model.Fwallet;
import com.ruizton.main.service.front.FrontUserService;
import com.ruizton.util.CollectionUtils;
import com.ruizton.util.Constants;
import com.ruizton.util.FormatUtils;
import com.ruizton.util.HttpClientUtils;
import com.ruizton.util.StringUtils;
import com.ruizton.util.Utils;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 招股金服 CopyRight : www.zhgtrade.com Author : 林超（362228416@qq.com） Date：
 * 2016-04-26 15:50
 */
@Controller
public class IndexController extends BaseController {
	@Autowired
	private RealTimeDataService realTimeDataService;
	@Autowired
	private ConstantMap constantMap;
	@Autowired
	private FrontUserService frontUserService;
	@Autowired
	private RealTimePriceServiceImpl realTimePriceService;
	@Resource
	private RealTimeCmsDataService realTimeCmsDataService;

	/**
	 *
	 * @param request
	 * @param map
	 * @param sort
	 *            1最新成交价升序/2最新成交价降序，3 24h成交量升序/4
	 *            24h成交量降序，5总市值升序/6总市值降序，7日涨跌幅升序/8日涨跌幅降序，9周涨跌幅升序/10周涨跌幅降序
	 * @return
	 */
	@RequestMapping({"/index", "/"})
	public String index(HttpServletRequest request, HttpServletResponse response, Map<String, Object> map,
						@RequestParam(value = "sort", required = false, defaultValue = "19")int sort,
						@RequestParam(value = "intro", required = false, defaultValue = "")String intro) {
		if(StringUtils.hasText(intro)){
			response.addCookie(new Cookie(Constants.INTRO_COOKIE, intro));
		}

		loadConstants(map);

		// 最新排行
		dealDatas(request, map, sort);

		loadCmsContent(request);

		return "index";
	}

	private void loadCmsContent(HttpServletRequest request){
		// 资讯内容
		Map<String, List<Farticle>> map = realTimeCmsDataService.getCmsContentList();
		// 官方公告
		request.setAttribute("nContents", map.get("2"));

		// 市场动态
		request.setAttribute("marketContents", map.get("9"));

		// 项目展示
		request.setAttribute("oContents", map.get("8"));
	}

	/**
	 * 首页资产估值
	 *
	 * @param request
	 */
	private Map<String, Object> getUserAsset(HttpServletRequest request, Fwallet fwallet) {
		if (!isLogin(request))
			return Collections.emptyMap();// 用户没登陆不需执行以下内容

		// 如果Session已存在则不再查询
		if (null != request.getSession().getAttribute(Constants.USER_ASSET)) {
			long lastUpdateTime = NumberUtils.toLong(request.getSession().getAttribute(Constants.USER_ASSET_TIME) + "");
			if (System.currentTimeMillis() - lastUpdateTime < 59 * 1000) {
				return (Map<String, Object>) request.getSession().getAttribute(Constants.USER_ASSET);
			}
		}

		Map<String, Object> ret = new HashMap<>(5);
		// 用户钱包
//		Fwallet fwallet = this.frontUserService.findFwalletById(getSessionUser(request).getFwallet().getFid());
//		request.setAttribute("fwallet", fwallet);
		// 虚拟钱包
		Map<Integer, Fvirtualwallet> fvirtualwallets = this.frontUserService.findVirtualWallet(getSessionUser(request).getFid());
//		request.setAttribute("fvirtualwallets", fvirtualwallets);

		// 估计总资产
		// //CNY+冻结CNY+（币种+冻结币种）*最高买价
		double totalCapital = 0F;
		totalCapital += fwallet.getFtotalRmb() + fwallet.getFfrozenRmb();
		// 总借金额
		// double totalBorrow = 0f;
		// totalBorrow = totalBorrow+fwallet.getFborrowCny();
//		double totalLend = 0f;
//		totalLend = totalLend + fwallet.getFalreadyLendCny() + fwallet.getFcanLendCny() + fwallet.getFfrozenLendCny();
		for (Map.Entry<Integer, Fvirtualwallet> entry : fvirtualwallets.entrySet()) {
			double highestBuyPrize = realTimeDataService.getHighestBuyPrize(entry.getValue().getFvirtualcointype().getFid());
			totalCapital += (entry.getValue().getFfrozen() + entry.getValue().getFtotal()) * highestBuyPrize;
			// totalBorrow += entry.getValue().getFborrowBtc()*
			// realTimeDataService.getHighestBuyPrize(entry.getValue().getFvirtualcointype().getFid());
//			totalLend += (entry.getValue().getFalreadyLendBtc() + entry.getValue().getFcanlendBtc() + entry.getValue().getFfrozenLendBtc()) * highestBuyPrize;
		}
		// P2P手续费
		// Map<String,Double> mapCOIN =
		// this.lendEntrustService.getBorrowFees(GetSession().getFid(),CnyOrCoinEnum.COIN);
		// double totalFeesCOIN = mapCOIN.get("totalFees");
		// Map<String,Double> mapCNY =
		// this.lendEntrustService.getBorrowFees(GetSession().getFid(),CnyOrCoinEnum.CNY);
		// double totalFeesCNY = mapCNY.get("totalFees");
		// 借+手续费
		// totalBorrow=totalBorrow+totalFeesCNY+totalFeesCOIN*
		// realTimeDataService.getHighestBuyPrize(1);

		// getRequest().setAttribute("totalNet",
		// Utils.getDouble(totalCapital+totalLend-totalBorrow, 2)) ;
		// 资产评估，缓存到Session
		ret.put("totalCapital", FormatUtils.formatCNY(totalCapital));
		ret.put("totalCapitalTrade", FormatUtils.formatCNY(totalCapital));

		// getRequest().setAttribute("totalNetTrade",
		// Utils.getDouble(totalCapital-totalBorrow, 2)) ;
		request.getSession().setAttribute(Constants.USER_ASSET, ret);
		request.getSession().setAttribute(Constants.USER_ASSET_TIME, System.currentTimeMillis());
		return ret;
	}

	/**
	 * 指定10条 不够成交量排序补上
	 *
	 * @param map
	 * @param sort
	 * @return
	 */
	@RequestMapping(value = "/index/dealDatas", method = RequestMethod.GET)
	public String dealDatas(HttpServletRequest request, Map<String, Object> map, @RequestParam(value = "sort", required = false, defaultValue = "19") int sort) {
		List<LatestDealData> dealDatas = realTimeDataService.getLatestDealDataList();
		List<LatestDealData> datas = new ArrayList<>(dealDatas);

		int listSize = 10;

		// 先排序获取10条
		List<LatestDealData> list = new ArrayList<>(listSize);
		for(int i=0; i<datas.size(); i++){
			LatestDealData data = datas.get(i);
			if(data.isHomeShow() && list.size() < listSize){
				list.add(data);
				i --;
				i %= datas.size();
				datas.remove(data);
			}
		}

		if(19 == sort){
			// 首页默认排序
			sortCoinRank(list, 19);
		}

		// 不足10个 从排序中获取补足
		if(!CollectionUtils.isEmpty(datas) && list.size() < listSize){
			if(19 == sort){
				// 默认排序
				sortCoinRank(datas, 0);
			}
			int left = listSize - list.size();
			if(datas.size() > left){
				list.addAll(datas.subList(0, left));
			}else{
				list.addAll(datas);
			}
		}

		if(19 != sort){
			sortCoinRank(list, sort);
		}

		map.put("dealDatas", list);
		if(Utils.isAjax(request)){
			return "index_list_consult";
		}
		return "index";
	}

	/**
	 * 用户个人信息
	 *
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/index/userinfo")
	public Object userInfo(HttpServletRequest request){
		Fuser fuser = getSessionUser(request);
		if(null == fuser){
			Map<String, Object> map = new HashedMap(2);
			map.put("code", -1);
			return map;
		}

		// 个人资产
		Fwallet fwallet = frontUserService.findFwalletByIdNative(fuser.getFwallet().getFid());
		Map<String, Object> map = getUserAsset(request, fwallet);
		map.put("code", 200);
		map.put("id", fuser.getFid());
		map.put("loginName", fuser.getFloginName());
		map.put("nickName", fuser.getFnickName());
		map.put("headImgUrl", fuser.getHeadImgUrl());
		map.put("totalRmb", FormatUtils.formatCNY(fwallet.getFtotalRmb()));
		map.put("frozenRmb", FormatUtils.formatCNY(fwallet.getFfrozenRmb()));
		return map;
	}

	/**
	 * 趋势图数据
	 *
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/price/trend")
	public Object priceTrend(){
		Map<String, List<Object[]>> trendMap = realTimePriceService.getHourPriceTrendData();
		return trendMap;
	}

	/**
	 * 币币交易
	 *
	 * @param type
	 * @return
	 */
	@RequestMapping({"/coin/index"})
	public String index(HttpServletRequest request, Map<String, Object> map,
						@RequestParam(value = "type", required = false, defaultValue = "1") short type,
						@RequestParam(value = "sort", required = false, defaultValue = "20") int sort){
		List<LatestDealData> dealDatas = realTimeDataService.getLatestDealDataList();
		List<LatestDealData> ajaxDatas = new ArrayList<>(dealDatas.size());
		List<LatestDealData> mainDatas = new ArrayList<>(dealDatas.size());
		List<LatestDealData> leftDatas = new ArrayList<>(dealDatas.size());
		for(LatestDealData dealData : dealDatas){
			if(type == dealData.getCoinTradeType()){
				ajaxDatas.add(dealData);
			}
			if(1 == dealData.getCoinTradeType()){
				mainDatas.add(dealData);
			}else if(2 == dealData.getCoinTradeType()){
				leftDatas.add(dealData);
			}
		}

		if(Utils.isAjax(request)){
			sortCoinRank(ajaxDatas, sort);
			map.put("dealDatas", dealDatas);
			return "index_list";
		}else{
			sortCoinRank(mainDatas, sort);
			map.put("mainDatas", dealDatas);

			sortCoinRank(leftDatas, sort);
			map.put("leftDatas", leftDatas);
		}

		loadCmsContent(request);

		return "coin/coin_trade";
	}

	@RequestMapping("/coin/main")
	public String main() {
		return "coin/coin_main";
	}

	@RequestMapping("/coin/vice")
	public String vice() {
		return "coin/coin_vice";
	}



	/**
	 * 权益交易
	 *
	 * @param map
	 * @return
	 */
	@RequestMapping("/equity/index")
	public String equityTrade(HttpServletRequest request, Map<String, Object> map,
							  @RequestParam(value = "sort", required = false, defaultValue = "20")short sort){
		List<LatestDealData> dealDatas = realTimeDataService.getLatestDealDataList();
		List<LatestDealData> datas = new ArrayList<>(dealDatas.size());


		sortCoinRank(datas, sort);
		map.put("dealDatas", datas);
		if(Utils.isAjax(request)){
			return "index_list";
		}
		loadCmsContent(request);
		return "equity/rights_trade";
	}

	@RequestMapping("/equity/intro")
	public String equityIntro(){
		return "equity/rights_intro";
	}

	/**
	 * 众股资讯
	 * by yujie
	 * @param size
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/index/consult", method = RequestMethod.GET)
	public Object consult(@RequestParam(required = false, defaultValue = "5") String size) {
		Map<String,Object> map = new HashMap();
		Map<String,String> consult_param = new HashMap();
		consult_param.put("size",size);
		String url = constantMap.getString("informationUrl") + "/api/getNewsList";
//		JSONObject consult = JSONObject.parseObject(HttpClientUtils.get(url,consult_param));
//		map.put("consult",consult.get("data"));
        consult_param.put("modelName","startTime");
        consult_param.put("checkStatus","2");
        consult_param.put("isDesc","false");
		String pro_url = constantMap.getString("informationUrl") + "/api/getRoadShowList";
		JSONObject pro = JSONObject.parseObject(HttpClientUtils.get(pro_url,consult_param));
        pro.getJSONArray("data").size();
		JSONArray arr = pro.getJSONArray("data");
		int length = arr.size();
 		for (int i = 0 ;i< length ; i++){
			int show_id = arr.getJSONObject(i).getInteger("id");
            String pic_url = constantMap.getString("informationUrl") + "/api/getFilePath";
            Map<String,String> pic_param = new HashMap();
            pic_param.put("roadShowId",show_id+"");
            JSONObject pic = JSONObject.parseObject(HttpClientUtils.get(pic_url,pic_param));
            int len = pic.getJSONArray("data").size();
            if (len>0 ){
                arr.getJSONObject(i).put("imgPath",pic.getJSONArray("data").getJSONObject(0).get("path"));
            }else{
                String article_url = constantMap.getString("informationUrl") + "/api/selectProjectByArticleId";
                Map<String,String> article_param = new HashMap();
                article_param.put("articleId",arr.getJSONObject(i).getInteger("articleId")+"");
                JSONObject article = JSONObject.parseObject(HttpClientUtils.get(article_url,article_param));
                arr.getJSONObject(i).put("imgPath",article.getJSONObject("data").get("headImgPath"));
            }
		}
		map.put("pro",pro.get("data"));
		return map;
	}
	/**
	 * 行情（btc）
	 * by yujie
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/index/conjuncture", method = RequestMethod.GET)
	public Object conjuncture() {
		String url = "https://www.okcoin.cn/api/v1/kline.do?symbol=btc_cny&type=1hour&size=24";
		JSONArray result = JSONObject.parseArray(HttpClientUtils.get(url));
		int length = result.size();
		Object[] res = new Object[length];
		for(int i=0;i<length;i++){
			JSONArray arr = result.getJSONArray(i);
			String[] brr = new String[2];
			brr[0] = arr.getString(0);
			brr[1] = arr.getString(4);
			res[i] = brr;
		}
        return res;
	}
}
