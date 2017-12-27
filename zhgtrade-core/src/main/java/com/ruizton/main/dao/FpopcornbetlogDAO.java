package com.ruizton.main.dao;
// default package

import static org.hibernate.criterion.Example.create;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.ruizton.main.Enum.PopcornBetStatusEnum;
import com.ruizton.main.dao.comm.HibernateDaoSupport;
import com.ruizton.main.model.Fpopcornbetlog;
import com.ruizton.main.model.Fuser;

/**
 * A data access object (DAO) providing persistence and search support for
 * Fpopcornbetlog entities. Transaction control of the save(), update() and
 * delete() operations can directly support Spring container-managed
 * transactions or they can be augmented to handle user-managed Spring
 * transactions. Each of these methods provides additional information for how
 * to configure it for the desired type of transaction control.
 * 
 * @see .Fpopcornbetlog
 * @author MyEclipse Persistence Tools
 */
@Repository
public class FpopcornbetlogDAO extends HibernateDaoSupport {
	private static final Logger log = LoggerFactory
			.getLogger(FpopcornbetlogDAO.class);
	// property constants
	public static final String FBETRESULT1 = "fbetresult1";
	public static final String FBETRESULT2 = "fbetresult2";
	public static final String FACTUAL_RESULT = "factualResult";
	public static final String FBET_SCORE = "fbetScore";
	public static final String FWIN_SCORE = "fwinScore";

	public void save(Fpopcornbetlog transientInstance) {
		log.debug("saving Fpopcornbetlog instance");
		try {
			getSession().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(Fpopcornbetlog persistentInstance) {
		log.debug("deleting Fpopcornbetlog instance");
		try {
			getSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public Fpopcornbetlog findById(java.lang.Integer id) {
		log.debug("getting Fpopcornbetlog instance with id: " + id);
		try {
			Fpopcornbetlog instance = (Fpopcornbetlog) getSession().get(
					"com.ruizton.main.model.Fpopcornbetlog", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List<Fpopcornbetlog> findByExample(Fpopcornbetlog instance) {
		log.debug("finding Fpopcornbetlog instance by example");
		try {
			List<Fpopcornbetlog> results = (List<Fpopcornbetlog>) getSession()
					.createCriteria("com.ruizton.main.model.Fpopcornbetlog").add(create(instance))
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
		log.debug("finding Fpopcornbetlog instance with property: "
				+ propertyName + ", value: " + value);
		try {
			String queryString = "from Fpopcornbetlog as model where model."
					+ propertyName + "= ?";
			Query queryObject = getSession().createQuery(queryString);
			queryObject.setParameter(0, value);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List<Fpopcornbetlog> findByFbetresult1(Object fbetresult1) {
		return findByProperty(FBETRESULT1, fbetresult1);
	}

	public List<Fpopcornbetlog> findByFbetresult2(Object fbetresult2) {
		return findByProperty(FBETRESULT2, fbetresult2);
	}

	public List<Fpopcornbetlog> findByFactualResult(Object factualResult) {
		return findByProperty(FACTUAL_RESULT, factualResult);
	}

	public List<Fpopcornbetlog> findByFbetScore(Object fbetScore) {
		return findByProperty(FBET_SCORE, fbetScore);
	}

	public List<Fpopcornbetlog> findByFwinScore(Object fwinScore) {
		return findByProperty(FWIN_SCORE, fwinScore);
	}

	public List findAll() {
		log.debug("finding all Fpopcornbetlog instances");
		try {
			String queryString = "from Fpopcornbetlog";
			Query queryObject = getSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public Fpopcornbetlog merge(Fpopcornbetlog detachedInstance) {
		log.debug("merging Fpopcornbetlog instance");
		try {
			Fpopcornbetlog result = (Fpopcornbetlog) getSession().merge(
					detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(Fpopcornbetlog instance) {
		log.debug("attaching dirty Fpopcornbetlog instance");
		try {
			getSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(Fpopcornbetlog instance) {
		log.debug("attaching clean Fpopcornbetlog instance");
		try {
			getSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}
	
	public List<Fpopcornbetlog> list(int firstResult, int maxResults,
			String filter,boolean isFY) {
		List<Fpopcornbetlog> list = null;
		log.debug("finding Fpopcornbetlog instance with filter");
		try {
			String queryString = "from Fpopcornbetlog "+filter;
			Query queryObject = getSession().createQuery(queryString);
			if(isFY){
				queryObject.setFirstResult(firstResult);
				queryObject.setMaxResults(maxResults);
			}
			list = queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by filter name failed", re);
			throw re;
		}
		return list;
	}
	
	public List getTotalBet(String filter,String field) {
		List all = new ArrayList();
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT SUM("+field+") "+field+" from fpopcornbetlog \n");
		sql.append(filter+"\n");
		SQLQuery queryObject = getSession().createSQLQuery(sql.toString());
		return queryObject.list();
	}
	
	public void updateBetLog(int popcornId,int lastResulet1,int lastResulet2) {
		StringBuffer sql = new StringBuffer();
		sql.append(" update fpopcornbetlog set factualResult1 ="+lastResulet1+",factualResult2 = "+lastResulet2+" \n");
		sql.append(" where fpopcornlogId="+popcornId);
		SQLQuery queryObject = getSession().createSQLQuery(sql.toString());
		queryObject.executeUpdate();
	}
	
	public void updateBetLostLog(int popcornId,int lastResulet1,int lastResulet2,double winAmt,double lostAmt) {
		StringBuffer sql = new StringBuffer();
		sql.append(" update fpopcornbetlog set fstatus ="+PopcornBetStatusEnum.LOST_VALUE+",fwinQty=0,fallWinQty="+winAmt+",fallLostQty="+lostAmt+" \n");
		sql.append(",ffees=0,factualWinQty=0 \n");
		sql.append(" where fpopcornlogId="+popcornId+" and ifnull(fbetresult1,0)<>"+lastResulet1+" and ifnull(fbetresult2,0)<>"+lastResulet2);
		SQLQuery queryObject = getSession().createSQLQuery(sql.toString());
		queryObject.executeUpdate();
	}
	
	public void updateBetWinLog(int popcornId,int lastResulet1,int lastResulet2,double winAmt,double lostAmt) {
		StringBuffer sql = new StringBuffer();
		sql.append(" update fpopcornbetlog set fstatus ="+PopcornBetStatusEnum.WAIT_VALUE+",fallWinQty="+winAmt+",fallLostQty="+lostAmt+"  \n");
		sql.append(" where fpopcornlogId="+popcornId+" and (ifnull(fbetresult1,0)="+lastResulet1+" or ifnull(fbetresult2,0)="+lastResulet2+") \n");
		SQLQuery queryObject = getSession().createSQLQuery(sql.toString());
		queryObject.executeUpdate();
	}
	
	public void updateBetWinLog1(int popcornId,int lastResulet1,int lastResulet2,double lostAmt,double winAmt) {
		StringBuffer sql = new StringBuffer();
		sql.append("update fpopcornbetlog set fwinQty=round((fbetQty1/"+winAmt+"*"+lostAmt+"+fbetQty1),2) \n");
		sql.append(" where fpopcornlogId="+popcornId+" and ifnull(fbetresult1,0)="+lastResulet1+" and ifnull(fbetresult2,0) <> "+lastResulet2);
		SQLQuery queryObject = getSession().createSQLQuery(sql.toString());
		queryObject.executeUpdate();
	}
	
	public void updateBetWinLog2(int popcornId,int lastResulet1,int lastResulet2,double lostAmt,double winAmt) {
		StringBuffer sql = new StringBuffer();
		sql.append("update fpopcornbetlog set fwinQty=round((fbetQty2/"+winAmt+"*"+lostAmt+"+fbetQty2),2) \n");
		sql.append(" where fpopcornlogId="+popcornId+" and ifnull(fbetresult1,0)<>"+lastResulet1+" and ifnull(fbetresult2,0) = "+lastResulet2);
		SQLQuery queryObject = getSession().createSQLQuery(sql.toString());
		queryObject.executeUpdate();
	}
	
	public void updateBetWinLog3(int popcornId,int lastResulet1,int lastResulet2,double lostAmt,double winAmt) {
		StringBuffer sql = new StringBuffer();
		sql.append("update fpopcornbetlog set fwinQty=round(((fbetQty1+fbetQty2)/"+winAmt+"*"+lostAmt+"+(fbetQty1+fbetQty2)),2) \n");
		sql.append(" where fpopcornlogId="+popcornId+" and ifnull(fbetresult1,0)="+lastResulet1+" and ifnull(fbetresult2,0) = "+lastResulet2);
		SQLQuery queryObject = getSession().createSQLQuery(sql.toString());
		queryObject.executeUpdate();
	}

	public List getTotalWin(boolean isTodady) {
		List all = new ArrayList();
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT ROUND(SUM(IFNULL(fwinQty,0))*0.1,2) betQty from fpopcornbetlog \n");
		if(isTodady){
			sql.append("where fstatus >1 and  DATE_FORMAT(fcreatetime,'%Y-%c-%d') = DATE_FORMAT(now(),'%Y-%c-%d') \n");
		}else{
			sql.append("where fstatus >1 \n");
		}
		SQLQuery queryObject = getSession().createSQLQuery(sql.toString());
		return queryObject.list();
	}
	
	public Map getTotalFees(boolean isToday) {
		Map map = new HashMap();
		StringBuffer sql = new StringBuffer();
		sql.append("select sum(IFNULL(ffees,0)/100) totalFees from fpopcornbetlog where 1=1 \n");
		if(isToday){
			sql.append("and DATE_FORMAT(fcreatetime,'%Y-%c-%d') = DATE_FORMAT(now(),'%Y-%c-%d') \n");
		}
		SQLQuery queryObject = getSession().createSQLQuery(sql.toString());
		List allList = queryObject.list();
		if(allList != null && allList.size() >0){
			map.put("totalFees",allList.get(0));
		}
		return map;
	}
	
	public List getTotalBetById(int popcornlogId) {
		List all = new ArrayList();
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT SUM(IFNULL(fbetQty1,0)+IFNULL(fbetQty2,0)) betQty from fpopcornbetlog \n");
		sql.append("where fpopcornlogId="+popcornlogId+" \n");
		SQLQuery queryObject = getSession().createSQLQuery(sql.toString());
		return queryObject.list();
	}
	
	public List getTotalFeesById(int popcornlogId) {
		List all = new ArrayList();
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT SUM(IFNULL(ffees,0)) ffees from fpopcornbetlog \n");
		sql.append("where fpopcornlogId="+popcornlogId+" \n");
		SQLQuery queryObject = getSession().createSQLQuery(sql.toString());
		return queryObject.list();
	}
	
}