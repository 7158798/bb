package com.ruizton.main.controller.admin;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.alibaba.fastjson.JSON;
import com.ruizton.main.model.Fvirtualcointype;
import com.ruizton.main.service.admin.EntrustlogService;
import com.ruizton.main.service.admin.VirtualCoinService;
import com.ruizton.util.DateUtils;
import com.ruizton.util.StringUtils;
import com.ruizton.util.Utils;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 报表统计
 * 
 * @author xuelin
 * @date   2016年4月18日
 * @desc
 */
@Controller
@RequestMapping("/ssadmin")
public class ReportController{
	@Autowired
	private VirtualCoinService virtualCoinService;
	@Autowired
	private EntrustlogService entrustlogService;

	/**
	 * 虚拟币成交报表(默认15天)
	 *
	 * @param coinId
	 * @param startDate
	 * @param endDate
	 * @param lineType	1成交量/2成交人民币金额
	 * @param param
	 * @return
	 */
	@RequestMapping("/viCoinTradeReport")
	@RequiresPermissions("ssadmin/viCoinTradeReport.html")
	public String viCoinTradeReport(@RequestParam(value = "coinType", required = false) Integer coinId,
			@RequestParam(value = "startDate", required = false) String startDate,
			@RequestParam(value = "endDate", required = false) String endDate,
			@RequestParam(value = "lineType", required = false) Short lineType,
			Map<String, Object> param){

		// 币种
		if(null == coinId){
			Fvirtualcointype type = virtualCoinService.findFirstFirtualCoin();
			coinId = type.getFid();
		}
		// 数据类型
		if(null == lineType){
			lineType = 1;
		}
		// 时间段
		if(StringUtils.hasText(startDate) && StringUtils.hasText(endDate)){
			Date start = DateUtils.formatDate(startDate);
			Date end = DateUtils.formatDate(endDate);
			int days = DateUtils.getDays(start, end);
			if(days > 15){
				param.put("startDate", startDate);
				param.put("endDate", endDate);
				param.put("statusCode", 300);
				param.put("message", "日期间隔不能大于15天");
				return "ssadmin/comm/ajaxDone";
			}
		}
		if(!StringUtils.hasText(startDate) || !StringUtils.hasText(endDate)){
			// 默认15天
			startDate = DateUtils.formatDate(DateUtils.getDaysBefore(15));
			endDate = DateUtils.formatDate(new Date());
		}

		// 虚拟币列表
		List<Fvirtualcointype> types = virtualCoinService.findAll();
		param.put("coinType", coinId);
		param.put("lineType", lineType);
		param.put("startDate", startDate);
		param.put("endDate", endDate);
		param.put("types", types);

		// 数据
		List<Object> list = Collections.emptyList();
		if(1 == lineType){
			list = entrustlogService.findCountByDay(coinId, null, startDate, endDate);
		}else{
			list = entrustlogService.findAmountByDay(coinId, null, startDate, endDate);
		}

		int dataSize = list.size();
		Map<String, Double> retMap = new HashMap<>(list.size());
		for(int i=0; i<dataSize; i++){
			Object[] obj = (Object[]) list.get(i);
			retMap.put(obj[0].toString(), Utils.getDouble(((BigDecimal) obj[1]).doubleValue(), 2));
		}

		Date start = DateUtils.formatDate(startDate);
		Date end = DateUtils.formatDate(endDate);

		// 组合数据
		int days = DateUtils.getDays(start, end);
		List<String> dates = new ArrayList<>(dataSize);
		List<Double> datas = new ArrayList<>(dataSize);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(start);
		for(int i=0; i<=days; i++){
			String dateStr = DateUtils.formatDate(calendar.getTime());
			dates.add(DateUtils.formatDate(calendar.getTime(), "MM-dd"));
			Double num = retMap.get(dateStr);
			if(null == num){
				datas.add(0.0);
			}else{
				datas.add(num);
			}

			calendar.add(Calendar.DAY_OF_MONTH, 1);
		}

		param.put("dates", JSON.toJSONString(dates));
		param.put("datas", JSON.toJSONString(datas));

		return "ssadmin/charts/viCoinTrade";
	}



}






