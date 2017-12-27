package com.ruizton.main.dao;

// default package

import static org.hibernate.criterion.Example.create;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.LockMode;
import org.hibernate.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.ruizton.main.Enum.LendEntrustStatus1Enum;
import com.ruizton.main.Enum.LendEntrustStatus2Enum;
import com.ruizton.main.Enum.LendEntrustStatus3Enum;
import com.ruizton.main.Enum.LendEntrustTypeEnum;
import com.ruizton.main.Enum.ReturnTypeEnum;
import com.ruizton.main.dao.comm.HibernateDaoSupport;
import com.ruizton.main.model.Flendentrust;
import com.ruizton.util.Utils;

/**
 * A data access object (DAO) providing persistence and search support for
 * Flendentrust entities. Transaction control of the save(), update() and
 * delete() operations can directly support Spring container-managed
 * transactions or they can be augmented to handle user-managed Spring
 * transactions. Each of these methods provides additional information for how
 * to configure it for the desired type of transaction control.
 * 
 * @see .Flendentrust
 * @author MyEclipse Persistence Tools
 */
@Repository
public class FlendentrustDAO extends HibernateDaoSupport {
	private static final Logger log = LoggerFactory
			.getLogger(FlendentrustDAO.class);
	// property constants
	public static final String VERSION = "version";
	public static final String FCNY_OR_COIN = "fcnyOrCoin";
	public static final String FAMOUNT = "famount";
	public static final String FSUCCESS_AMOUNT = "fsuccessAmount";
	public static final String FDAILY_RATE = "fdailyRate";
	public static final String FTIME_LENGTH = "ftimeLength";
	public static final String FIS_BUY_INSURANCE = "fisBuyInsurance";
	public static final String FTYPE = "ftype";
	public static final String FSUB_TYPE = "fsubType";
	public static final String FSTATUS = "fstatus";

	public void save(Flendentrust transientInstance) {
		log.debug("saving Flendentrust instance");
		try {
			getSession().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(Flendentrust persistentInstance) {
		log.debug("deleting Flendentrust instance");
		try {
			getSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public Flendentrust findById(java.lang.Integer id) {
		log.debug("getting Flendentrust instance with id: " + id);
		try {
			Flendentrust instance = (Flendentrust) getSession().get(
					"com.ruizton.main.model.Flendentrust", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List<Flendentrust> findByExample(Flendentrust instance) {
		log.debug("finding Flendentrust instance by example");
		try {
			List<Flendentrust> results = (List<Flendentrust>) getSession()
					.createCriteria("com.ruizton.main.model.Flendentrust").add(create(instance))
					.list();
			log.debug("find by example successful, result size: "
					+ results.size());
			return results;
		} catch (RuntimeException re) {
			log.error("find by example failed", re);
			throw re;
		}
	}

	public List findByProperty(String propertyName, Object value) {
		log.debug("finding Flendentrust instance with property: "
				+ propertyName + ", value: " + value);
		try {
			String queryString = "from Flendentrust as model where model."
					+ propertyName + "= ?";
			Query queryObject = getSession().createQuery(queryString);
			queryObject.setParameter(0, value);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List<Flendentrust> findByVersion(Object version) {
		return findByProperty(VERSION, version);
	}

	public List<Flendentrust> findByFcnyOrCoin(Object fcnyOrCoin) {
		return findByProperty(FCNY_OR_COIN, fcnyOrCoin);
	}

	public List<Flendentrust> findByFamount(Object famount) {
		return findByProperty(FAMOUNT, famount);
	}

	public List<Flendentrust> findByFsuccessAmount(Object fsuccessAmount) {
		return findByProperty(FSUCCESS_AMOUNT, fsuccessAmount);
	}

	public List<Flendentrust> findByFdailyRate(Object fdailyRate) {
		return findByProperty(FDAILY_RATE, fdailyRate);
	}

	public List<Flendentrust> findByFtimeLength(Object ftimeLength) {
		return findByProperty(FTIME_LENGTH, ftimeLength);
	}

	public List<Flendentrust> findByFisBuyInsurance(Object fisBuyInsurance) {
		return findByProperty(FIS_BUY_INSURANCE, fisBuyInsurance);
	}

	public List<Flendentrust> findByFtype(Object ftype) {
		return findByProperty(FTYPE, ftype);
	}

	public List<Flendentrust> findByFsubType(Object fsubType) {
		return findByProperty(FSUB_TYPE, fsubType);
	}

	public List<Flendentrust> findByFstatus(Object fstatus) {
		return findByProperty(FSTATUS, fstatus);
	}

	public List findAll() {
		log.debug("finding all Flendentrust instances");
		try {
			String queryString = "from Flendentrust";
			Query queryObject = getSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public Flendentrust merge(Flendentrust detachedInstance) {
		log.debug("merging Flendentrust instance");
		try {
			Flendentrust result = (Flendentrust) getSession().merge(
					detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(Flendentrust instance) {
		log.debug("attaching dirty Flendentrust instance");
		try {
			getSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(Flendentrust instance) {
		log.debug("attaching clean Flendentrust instance");
		try {
			getSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}
	
	public List<Flendentrust> list(int firstResult, int maxResults, String filter,boolean isFY) {
		List<Flendentrust> list = null;
		log.debug("finding Flendentrust instance with filter");
		try {
			String queryString = "from Flendentrust "+filter;
			Query queryObject = getSession().createQuery(queryString);
			if(isFY){
				queryObject.setFirstResult(firstResult);
				queryObject.setMaxResults(maxResults);
			}
			list = queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by Flendentrust name failed", re);
			throw re;
		}
		return list;
	}
	
	public double getBorrowAmt(int userId,int cnyOrCoin) {
		double amt = 0d;
		try {
			StringBuffer sf = new StringBuffer();
			sf.append("SELECT SUM(fAmount-fSuccessAmount) amt from flendentrust where \n");
			sf.append("fuserid="+userId+" and ftype= "+LendEntrustTypeEnum.BORROW+" \n");
			sf.append("and fstatus1 in("+LendEntrustStatus1Enum.NOT_DEAL+","+LendEntrustStatus1Enum.PART_DEAL+") \n");
			sf.append("and fcnyOrCoin= "+cnyOrCoin+" \n");
			Query queryObject = getSession().createSQLQuery(sf.toString());
			List list = queryObject.list();
			if(list != null && list.size() >0 && list.get(0) != null){
				amt = Double.valueOf(list.get(0).toString());
			}
		} catch (RuntimeException re) {
			throw re;
		}
		return amt;
	}
	
	public Map<String,Double> getBorrowFees(int userId,int cnyOrCoin) {
		Map<String,Double> map = new HashMap<String,Double>();
		try {
			StringBuffer sf = new StringBuffer();
			sf.append("select SUM(a.ftodayfees) ftodayfees,SUM(a.ftotalfees) ftotalfees from flendentrustlog a \n");
			sf.append("LEFT OUTER JOIN flendentrust b on a.fLendEntrustBorrowId=b.fid \n");
			sf.append("where b.fuserid="+userId+" \n");
			sf.append("and b.fcnyOrCoin="+cnyOrCoin+" \n");
			Query queryObject = getSession().createSQLQuery(sf.toString());
			List list = queryObject.list();
			if(list != null && list.size() >0 && list.get(0) != null){
				Object[] o = (Object[])list.get(0);
				
				map.put("todayFees", Double.valueOf(o[0] == null?"0":o[0].toString()));
				map.put("totalFees", Double.valueOf(o[1] == null?"0":o[1].toString()));
			}else{
				map.put("todayFees", 0d);
				map.put("totalFees", 0d);
			}
		} catch (RuntimeException re) {
			throw re;
		}
		return map;
	}

	/**
	 * 借贷深度
	 * **/
	public List<Map> getHistoryList(int type,int cnyOrCoin) {
		try {
			List<Map> all = new ArrayList();
			StringBuffer sf = new StringBuffer();
			sf.append("select * from ( \n");
			sf.append("select min(fTimeLength) minLength,max(fTimeLength) maxLength, \n");
			sf.append("fDailyRate,SUM(fAmount-fSuccessAmount) totalAmt \n");
			sf.append(",count(fid) totalCount from flendentrust \n");
			sf.append("where ftype="+type+" and fcnyOrCoin="+cnyOrCoin+" and freturnType ="+ReturnTypeEnum.USER_VALUE+"\n");
			sf.append("and fstatus1 in("+LendEntrustStatus1Enum.NOT_DEAL+","+LendEntrustStatus1Enum.PART_DEAL+") \n");
			sf.append("GROUP BY fDailyRate \n");
			if(type == LendEntrustTypeEnum.LEND){
				sf.append(",fTimeLength \n");
			}
			sf.append(")as t order by t.fDailyRate \n");
			if(type == LendEntrustTypeEnum.LEND){
				sf.append(" asc \n");
			}else{
				sf.append(" desc \n");
			}
			sf.append(" LIMIT 0,10 \n");
			Query queryObject = getSession().createSQLQuery(sf.toString());
			List list = queryObject.list();
			for (int i = 0; i < list.size(); i++) {
				Object[] o = (Object[])list.get(i);
				Map map = new HashMap();
				int minLength = Double.valueOf(o[0].toString()).intValue();
				int maxLength = Double.valueOf(o[1].toString()).intValue();
				String fTimeLength = "";
				if(minLength == maxLength){
					fTimeLength = maxLength+"";
				}else{
					fTimeLength =minLength+"-"+maxLength;
				}
				map.put("fTimeLength",fTimeLength);
				map.put("fDailyRate", o[2]);
				map.put("totalAmt", o[3]);
				map.put("totalCount", o[4]);
				all.add(map);
			}
			return all;
		} catch (RuntimeException re) {
			throw re;
		}
	}
	
	/**
	 * 等待还款记录，用于借款
	 * */
	public List<Map> getWaitReturnHistoryList(int firstResult, int maxResults,String filter,boolean isFY,boolean isAll) {
		try {
			List<Map> all = new ArrayList<Map>();
			StringBuffer sf = new StringBuffer();
			sf.append("select a.fid,a.fCreateTime,a.fAmount,a.fSuccessAmount,a.fstatus2, \n");
			if(isAll){
				sf.append("SUM(b.ftotalfees+IFNULL(c.totalfees,0)) totalfees, \n");
			}else{
				sf.append("SUM(b.ftotalfees) totalfees, \n");
			}
			sf.append("SUM(b.famount) totalBorrow,SUM(b.famount-b.fReturnAmount) noReturn,a.freturntype \n");
			sf.append("from flendentrust a LEFT OUTER JOIN flendentrustlog b on a.fid=b.fLendEntrustBorrowId \n");
			if(isAll){
				sf.append("left outer join (select flendEntrustId,SUM(fAmount) fAmount,SUM(fowerFees) ffees,SUM(ffees) totalfees from flendEntrustFinishLog c group by flendEntrustId) c on b.fid = c.flendEntrustId \n");
			}
			sf.append(filter +" \n");
			sf.append("GROUP BY a.fid,a.fCreateTime,a.fAmount,a.fSuccessAmount,a.fstatus2,a.freturntype \n");
			Query queryObject = getSession().createSQLQuery(sf.toString());
			if(isFY){
				queryObject.setFirstResult(firstResult);
				queryObject.setMaxResults(maxResults);
			}
			List list = queryObject.list();
			for (int i = 0; i < list.size(); i++) {
				Object[] o = (Object[])list.get(i);
				Map map = new HashMap();
				map.put("fid",o[0]);
				map.put("fCreateTime", o[1]);
				map.put("fAmount", o[2]);
				map.put("fSuccessAmount", o[3]);
				map.put("fstatus2", o[4]);
				if(o[5] == null){
					map.put("totalfees", 0);
				}else{
					map.put("totalfees", o[5]);
				}
				map.put("totalBorrow", o[6]);
				map.put("noReturn", o[7]);
				int status = Integer.parseInt(o[4].toString());
				String status_s = LendEntrustStatus2Enum.getEnumString(status);
				map.put("status_s",status_s);
				
				int returntype = Integer.parseInt(o[8].toString());
				map.put("returntype_s",ReturnTypeEnum.getEnumString(returntype));
				all.add(map);
			}
			return all;
		} catch (RuntimeException re) {
			throw re;
		}
	}
	
	/**
	 * 等待还款记录，用于放款
	 * 放款时间 	放款金额 	成交金额 	已还本金 	已还手续费 	平均费率 	手续费 	状态
	 * */
	public List<Map> getWaitBorrowHistoryList(int firstResult, int maxResults,String filter,boolean isFY) {
		try {
			List<Map> all = new ArrayList<Map>();
			StringBuffer sf = new StringBuffer();
			sf.append("select a.fid,a.fCreateTime,a.fAmount,a.fSuccessAmount,a.fstatus2, \n");
			sf.append("SUM(b.ftotalfees+IFNULL(c.totalfees,0)) totalfees, \n");
			sf.append("SUM(IFNULL(c.famount,0)) totalReturn,SUM(IFNULL(c.totalfees,0)) totalReturnFees,round(avg(b.fdailyRate),4) fdailyRate, \n");
			sf.append("SUM(IFNULL(c.ffees,0)) ownerFees \n");
			sf.append("from flendentrust a LEFT OUTER JOIN flendentrustlog b on a.fid=b.fLendEntrustLendId \n");
			sf.append("left outer join (select flendEntrustId,SUM(fAmount) fAmount,SUM(fowerFees) ffees,SUM(ffees) totalfees from flendEntrustFinishLog c group by flendEntrustId) c on b.fid = c.flendEntrustId \n");
			sf.append(filter +" \n");
			sf.append("GROUP BY a.fid,a.fCreateTime,a.fAmount,a.fSuccessAmount,a.fstatus2 \n");
			Query queryObject = getSession().createSQLQuery(sf.toString());
			if(isFY){
				queryObject.setFirstResult(firstResult);
				queryObject.setMaxResults(maxResults);
			}
			List list = queryObject.list();
			for (int i = 0; i < list.size(); i++) {
				Object[] o = (Object[])list.get(i);
				Map map = new HashMap();
				map.put("fid",o[0]);
				map.put("fCreateTime", o[1]);
				map.put("fAmount", o[2]);
				map.put("fSuccessAmount", o[3]);
				map.put("fstatus2", o[4]);
				if(o[5] == null){
					map.put("totalfees", 0);
				}else{
					map.put("totalfees", o[5]);
				}
				map.put("totalReturn", o[6]);
				map.put("totalReturnFees", o[7]);
				map.put("fdailyRate", o[8]);
				if(o[9] == null){
					map.put("ownerFees", 0);
				}else{
					map.put("ownerFees", o[9]);
				}
				int status = Integer.parseInt(o[4].toString());
				String status_s = LendEntrustStatus3Enum.getEnumString(status);
				map.put("status_s",status_s);
				all.add(map);
			}
			return all;
		} catch (RuntimeException re) {
			throw re;
		}
	}
	
	/**
	 * 贷款汇总
	 * **/
	public List<Map> getUserLends(int firstResult, int maxResults,String filter,boolean isFY) {
		try {
			List<Map> all = new ArrayList<Map>();
			StringBuffer sf = new StringBuffer();
			sf.append("select \n");
			sf.append("b.fuserid, \n");
			sf.append("c.floginName, \n");
			sf.append("c.fRealName, \n");
			sf.append("case when b.fcnyorcoin=1 then 'CNY' ELSE 'DOU' END, \n");
			sf.append("SUM(b.fAmount), \n");
			sf.append("SUM(b.fSuccessAmount), \n");
			sf.append("SUM(b.fAmount-b.fSuccessAmount), \n");
			sf.append("SUM(a.famount), \n");
			sf.append("SUM(a.fReturnAmount), \n");
			sf.append("SUM(a.famount-a.fReturnAmount), \n");
			sf.append("sum(a.totalFees), \n");
			sf.append("sum(a.returnFees), \n");
			sf.append("sum(a.totalFees-a.returnFees), \n");
			sf.append("sum(a.owerFee) \n");
			sf.append("from \n");
			sf.append("(select  \n");
			sf.append("b.fLendEntrustLendId, \n");
			sf.append("SUM(b.famount) famount, \n");
			sf.append("SUM(b.fReturnAmount) fReturnAmount, \n");
			sf.append("(SUM(b.ftotalfees) + IFNULL(c.totalfees,0))totalFees, \n");
			sf.append("IFNULL(c.totalfees,0) returnFees, \n");
			sf.append("IFNULL(c.owerFees,0) owerFee \n");
			sf.append(" from flendentrustlog  b \n");
			sf.append("LEFT OUTER JOIN ( \n");
			sf.append("select flendEntrustId,SUM(fAmount) fAmount,SUM(fowerFees) owerFees,SUM(ffees) totalfees \n");
			sf.append(" from flendEntrustFinishLog c group by flendEntrustId \n");
			sf.append(") c on b.fid = c.flendEntrustId  \n");
			sf.append("GROUP BY b.fLendEntrustLendId)as a LEFT OUTER JOIN flendentrust b on a.fLendEntrustLendId=b.fid \n");
			sf.append("LEFT OUTER JOIN fuser c on b.fuserid=c.fid \n");
			sf.append(filter+" \n");
			sf.append("GROUP BY fuserid,b.fcnyorcoin \n");
			Query queryObject = getSession().createSQLQuery(sf.toString());
			if(isFY){
				queryObject.setFirstResult(firstResult);
				queryObject.setMaxResults(maxResults);
			}
			List list = queryObject.list();
			for (int i = 0; i < list.size(); i++) {
				Object[] o = (Object[])list.get(i);
				Map map = new HashMap();
				map.put("fuserid",o[0]);
				map.put("floginName",o[1]);
				map.put("fRealName",o[2]);
				map.put("fcnyOrCoin",o[3]);
				map.put("fAmount",o[4]);
				map.put("fSuccessAmount",o[5]);
				map.put("noSuccessAmount",o[6]);
				map.put("fTotalAmount",o[7]);
				map.put("fReturnAmount",o[8]);
				map.put("noReturnAmount",o[9]);
				map.put("ftotalfees",o[10]);
				map.put("returnFees",o[11]);
				map.put("noReturnFees",o[12]);
				map.put("ownerFees",o[13]);
				all.add(map);
			}
			return all;
		} catch (RuntimeException re) {
			throw re;
		}
	}
	
	/**
	 * 借款汇总
	 * **/
	public List<Map> getUserBorrows(int firstResult, int maxResults,String filter,boolean isFY) {
		try {
			List<Map> all = new ArrayList<Map>();
			StringBuffer sf = new StringBuffer();
			sf.append("select \n");
			sf.append("b.fuserid, \n");
			sf.append("c.floginName, \n");
			sf.append("c.fRealName, \n");
			sf.append("case when b.fcnyorcoin=1 then 'CNY' ELSE 'DOU' END, \n");
			sf.append("SUM(b.fAmount), \n");
			sf.append("SUM(b.fSuccessAmount), \n");
			sf.append("SUM(b.fAmount-b.fSuccessAmount), \n");
			sf.append("SUM(a.famount), \n");
			sf.append("SUM(a.fReturnAmount), \n");
			sf.append("SUM(a.famount-a.fReturnAmount), \n");
			sf.append("sum(a.totalFees), \n");
			sf.append("sum(a.returnFees), \n");
			sf.append("sum(a.totalFees-a.returnFees) \n");
			sf.append("from \n");
			sf.append("(select  \n");
			sf.append("b.fLendEntrustBorrowId, \n");
			sf.append("SUM(b.famount) famount, \n");
			sf.append("SUM(b.fReturnAmount) fReturnAmount, \n");
			sf.append("(SUM(b.ftotalfees) + IFNULL(c.totalfees,0))totalFees, \n");
			sf.append("IFNULL(c.totalfees,0) returnFees, \n");
			sf.append("IFNULL(c.owerFees,0) owerFee \n");
			sf.append(" from flendentrustlog  b \n");
			sf.append("LEFT OUTER JOIN ( \n");
			sf.append("select flendEntrustId,SUM(fAmount) fAmount,SUM(fowerFees) owerFees,SUM(ffees) totalfees \n");
			sf.append(" from flendEntrustFinishLog c group by flendEntrustId \n");
			sf.append(") c on b.fid = c.flendEntrustId  \n");
			sf.append("GROUP BY b.fLendEntrustBorrowId)as a LEFT OUTER JOIN flendentrust b on a.fLendEntrustBorrowId=b.fid \n");
			sf.append("LEFT OUTER JOIN fuser c on b.fuserid=c.fid \n");
			sf.append(filter+" \n");
			sf.append("GROUP BY fuserid,b.fcnyorcoin \n");
			Query queryObject = getSession().createSQLQuery(sf.toString());
			if(isFY){
				queryObject.setFirstResult(firstResult);
				queryObject.setMaxResults(maxResults);
			}
			List list = queryObject.list();
			for (int i = 0; i < list.size(); i++) {
				Object[] o = (Object[])list.get(i);
				Map map = new HashMap();
				map.put("fuserid",o[0]);
				map.put("floginName",o[1]);
				map.put("fRealName",o[2]);
				map.put("fcnyOrCoin",o[3]);
				map.put("fAmount",o[4]);
				map.put("fSuccessAmount",o[5]);
				map.put("noSuccessAmount",o[6]);
				map.put("fTotalAmount",o[7]);
				map.put("fReturnAmount",o[8]);
				map.put("noReturnAmount",o[9]);
				map.put("ftotalfees",o[10]);
				map.put("returnFees",o[11]);
				map.put("noReturnFees",o[12]);
				all.add(map);
			}
			return all;
		} catch (RuntimeException re) {
			throw re;
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<Flendentrust> findUnrepaymentByUser(Integer fuserId, Integer coinTypeId, int firstResult, int maxResult){
		StringBuilder hqlBuf = new StringBuilder();
		hqlBuf.append("from com.ruizton.main.model.Flendentrust where 1=1");
		if(null != fuserId){
			hqlBuf.append(" and fuser.fid=:fuid");
		}
		if(null != coinTypeId){
			hqlBuf.append(" and fvirtualcointype.fid=:coinId");
		}
		hqlBuf.append(" and ftype=").append(LendEntrustTypeEnum.BORROW);
		hqlBuf.append(" and (fstatus2=").append(LendEntrustTypeEnum.BORROW).append(" or fstatus2=").append(LendEntrustStatus2Enum.NOT_REPAY).append(")");
		hqlBuf.append(" order by fcreateTime desc");
		
		Query query = getSession().createQuery(hqlBuf.toString());
		if(null != fuserId){
			query.setParameter("fuid", fuserId);
		}
		if(null != coinTypeId){
			query.setParameter("coinId", coinTypeId);
		}
		query.setFirstResult(firstResult);
		query.setMaxResults(maxResult);
		
		return query.list();
	}
	
	public int countUnrepaymentByUser(Integer fuserId, Integer coinTypeId){
		StringBuilder hqlBuf = new StringBuilder();
		hqlBuf.append("select count(*) from com.ruizton.main.model.Flendentrust where 1=1");
		if(null != fuserId){
			hqlBuf.append(" and fuser.fid=:fuid");
		}
		if(null != coinTypeId){
			hqlBuf.append(" and fvirtualcointype.fid=:coinId");
		}
		hqlBuf.append(" and ftype=").append(LendEntrustTypeEnum.BORROW);
		hqlBuf.append(" and freturnType=").append(ReturnTypeEnum.USER_VALUE);
		hqlBuf.append(" and (fstatus2=").append(LendEntrustTypeEnum.BORROW).append(" or fstatus2=").append(LendEntrustStatus2Enum.NOT_REPAY).append(")");
		
		Query query = getSession().createQuery(hqlBuf.toString());
		if(null != fuserId){
			query.setParameter("fuid", fuserId);
		}
		if(null != coinTypeId){
			query.setParameter("coinId", coinTypeId);
		}
		
		return ((Long)query.uniqueResult()).intValue();
	}
	
	/**
	 * 借贷深度
	 * @param type 1 放款 / 2 借款
	 * @param fuserId
	 * @param cnyOrCoin 现金还是虚拟币
	 * @param coinTypeId
	 * @return
	 */
	public List<Map<String, Object>> lendBorrowDepth(int type, Integer fuserId, Short cnyOrCoin, Integer coinTypeId){
		StringBuilder hqlBuf = new StringBuilder();
		hqlBuf.append("select * from (select min(ftimeLength) minLength,max(ftimeLength) maxLength, fdailyRate,SUM(famount-fsuccessAmount) totalAmt,");
		hqlBuf.append("count(fid) totalCount from flendentrust where ftype=").append(type).append(" and freturnType=").append(ReturnTypeEnum.USER_VALUE);
		if(null != coinTypeId){
			hqlBuf.append(" and FvirtualCointypeId=").append(coinTypeId);
		}
		if(null != fuserId){
			hqlBuf.append(" and fuserid=").append(fuserId);
		}
		if(null != cnyOrCoin){
			hqlBuf.append(" and fcnyOrCoin=").append(cnyOrCoin);
		}
		hqlBuf.append(" and (fstatus1="+LendEntrustStatus1Enum.NOT_DEAL+" or fstatus1="+LendEntrustStatus1Enum.PART_DEAL+")");
		hqlBuf.append(" group by fdailyRate");
		if(type == LendEntrustTypeEnum.LEND){
			hqlBuf.append(", ftimeLength");
		}
		hqlBuf.append(")as t order by t.fdailyRate");
		if(type == LendEntrustTypeEnum.LEND){
			hqlBuf.append(" asc");
		}else{
			hqlBuf.append(" desc");
		}
		hqlBuf.append(" limit 0,10");
		
		Query queryObject = getSession().createSQLQuery(hqlBuf.toString());
		List list = queryObject.list();
		
		List<Map<String, Object>> dataList = new ArrayList<>(list.size());
		for (int i = 0; i < list.size(); i++) {
			Object[] o = (Object[])list.get(i);
			Map map = new HashMap();
			int minLength = Double.valueOf(o[0].toString()).intValue();
			int maxLength = Double.valueOf(o[1].toString()).intValue();
			String fTimeLength = "";
			if(minLength == maxLength){
				fTimeLength = maxLength+"";
			}else{
				fTimeLength =minLength+"-"+maxLength;
			}
			map.put("days",fTimeLength);
			map.put("rate", Utils.getDouble(((BigDecimal)o[2]).doubleValue(), 2));
			map.put("amount", Utils.getDouble(((BigDecimal)o[3]).doubleValue(), 2));
			map.put("num", o[4]);
			dataList.add(map);
		}
		
		return dataList;
	}
	
}


