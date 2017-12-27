package com.ruizton.main.service.admin;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ruizton.main.dao.FaboutDAO;
import com.ruizton.main.dao.FentrustlogDAO;
import com.ruizton.main.model.Fabout;
import com.ruizton.main.model.Fentrustlog;

@Service
public class EntrustlogService {
	@Autowired
	private FentrustlogDAO entrustlogDAO;

	public List list(int firstResult, int maxResults,
			String filter,boolean isFY) {
		List all = this.entrustlogDAO.list(firstResult, maxResults, filter,isFY);
		return all;
	}
	
	public int countForList(String filter){
		return entrustlogDAO.countForList(filter);
	}
	
	/**
	 * 获取交易记录
	 * @param coinId
	 * @param sinceId
	 * @param maxResults
	 * @param isFY
	 * @return
	 */
	public List<Fentrustlog> findByViCoin(int coinId, int sinceId, int maxResults,boolean isFY) {
		List<Fentrustlog> all = this.entrustlogDAO.findByViCoin(coinId, sinceId, maxResults, isFY);
		return all;
	}
	
	public List<Fentrustlog> listxx(int firstResult, int maxResults,
			String filter,boolean isFY) {
		List<Fentrustlog> all = this.entrustlogDAO.listxx(firstResult, maxResults, filter,isFY);
		return all;
	}
	
	public double getFentrustAmt(){
		return this.entrustlogDAO.getFentrustAmt();
	}
	
	/**
	 * 统计每天成交金额
	 * 
	 * @param coinId
	 * @param entrustType
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public List<Object> findAmountByDay(Integer coinId, Short entrustType, String startDate, String endDate){
		return entrustlogDAO.findAmountByDay(coinId, entrustType, startDate, endDate);
	}
	
	/**
	 * 统计每天成交量
	 * 
	 * @param coinId
	 * @param entrustType
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public List<Object> findCountByDay(Integer coinId, Short entrustType, String startDate, String endDate){
		return entrustlogDAO.findCountByDay(coinId, entrustType, startDate, endDate);
	}

	/**
	 * 统计每小时成交平均价格
	 * @param fVid      虚拟币id
	 * @param startDate
	 * @param endDate
     * @return
     */
	public Map findAvgPriceByHour(int fVid, Date startDate, Date endDate){
		return entrustlogDAO.findAvgPriceByHour(fVid, startDate, endDate);
	}

	/**
	 * 跌涨幅
	 * @param coinId
	 * @param start
	 * @param end
     * @return
     */
	public double getUpAndDown(int coinId, Date start, Date end){

		return entrustlogDAO.getUpAndDown(coinId, start, end);
	}

	/**
	 * 交易额
	 * @param coinId
	 * @param start
	 * @param end
     * @return
     */
	public double getTradeCount(int coinId, Date start, Date end){

		return entrustlogDAO.getTradeCount(coinId, start, end);
	}

	public double getLastestDealPrice(int coinId, Date start, Date end){
		return entrustlogDAO.getLastestDealPrice(coinId, start, end);
	}
}