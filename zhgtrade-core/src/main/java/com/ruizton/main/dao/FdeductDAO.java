package com.ruizton.main.dao;

// default package

import static org.hibernate.criterion.Example.create;

import java.util.List;

import javax.annotation.Resource;

import org.hibernate.LockMode;
import org.hibernate.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.ruizton.main.dao.comm.HibernateDaoSupport;
import com.ruizton.main.model.Fchargesection;
import com.ruizton.main.model.Fdeduct;

/**
 * A data access object (DAO) providing persistence and search support for
 * Fdeduct entities. Transaction control of the save(), update() and delete()
 * operations can directly support Spring container-managed transactions or they
 * can be augmented to handle user-managed Spring transactions. Each of these
 * methods provides additional information for how to configure it for the
 * desired type of transaction control.
 * 
 * @see .Fdeduct
 * @author MyEclipse Persistence Tools
 */
@Repository
public class FdeductDAO extends HibernateDaoSupport {
	private static final Logger log = LoggerFactory.getLogger(FdeductDAO.class);
	// property constants
	public static final String FTOTAL_AMT = "ftotalAmt";
	public static final String FTOTAL_QTY = "ftotalQty";
	public static final String FSTATUS = "fstatus";

	public void save(Fdeduct transientInstance) {
		log.debug("saving Fdeduct instance");
		try {
			getSession().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(Fdeduct persistentInstance) {
		log.debug("deleting Fdeduct instance");
		try {
			getSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public Fdeduct findById(java.lang.Integer id) {
		log.debug("getting Fdeduct instance with id: " + id);
		try {
			Fdeduct instance = (Fdeduct) getSession().get(
					"com.ruizton.main.model.Fdeduct", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List<Fdeduct> findByExample(Fdeduct instance) {
		log.debug("finding Fdeduct instance by example");
		try {
			List<Fdeduct> results = (List<Fdeduct>) getSession()
					.createCriteria("com.ruizton.main.model.Fdeduct")
					.add(create(instance)).list();
			log.debug("find by example successful, result size: "
					+ results.size());
			return results;
		} catch (RuntimeException re) {
			log.error("find by example failed", re);
			throw re;
		}
	}

	public List findByProperty(String propertyName, Object value) {
		log.debug("finding Fdeduct instance with property: " + propertyName
				+ ", value: " + value);
		try {
			String queryString = "from Fdeduct as model where model."
					+ propertyName + "= ?";
			Query queryObject = getSession().createQuery(queryString);
			queryObject.setParameter(0, value);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List<Fdeduct> findByFtotalAmt(Object ftotalAmt) {
		return findByProperty(FTOTAL_AMT, ftotalAmt);
	}

	public List<Fdeduct> findByFtotalQty(Object ftotalQty) {
		return findByProperty(FTOTAL_QTY, ftotalQty);
	}

	public List<Fdeduct> findByFstatus(Object fstatus) {
		return findByProperty(FSTATUS, fstatus);
	}

	public List findAll() {
		log.debug("finding all Fdeduct instances");
		try {
			String queryString = "from Fdeduct";
			Query queryObject = getSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public Fdeduct merge(Fdeduct detachedInstance) {
		log.debug("merging Fdeduct instance");
		try {
			Fdeduct result = (Fdeduct) getSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(Fdeduct instance) {
		log.debug("attaching dirty Fdeduct instance");
		try {
			getSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(Fdeduct instance) {
		log.debug("attaching clean Fdeduct instance");
		try {
			getSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}
	
	public List<Fdeduct> list(int firstResult, int maxResults, String filter,boolean isFY) {
		List<Fdeduct> list = null;
		log.debug("finding Fdeduct instance with filter");
		try {
			String queryString = "from Fdeduct "+filter;
			Query queryObject = getSession().createQuery(queryString);
			if(isFY){
				queryObject.setFirstResult(firstResult);
				queryObject.setMaxResults(maxResults);
			}
			list = queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by Fdeduct name failed", re);
			throw re;
		}
		return list;
	}
	
	public List deductTotalList(int firstResult, int maxResults, String filter,
			boolean isFY) {
		List list = null;
		log.debug("finding Fdeduct instance with filter");
		try {
			StringBuffer sf = new StringBuffer();
			sf.append("SELECT a.fchargeDate,b.fstartHour,b.fendHour,SUM(a.ftotalAmt) totalAmt,SUM(a.ftotalQty) totalQty \n");
			sf.append("from fdeduct a left outer JOIN fchargesection b on a.fchargesectionId=b.fid \n");
			sf.append(filter+"\n");
			sf.append("GROUP BY a.fchargeDate,b.fstartHour,b.fendHour \n");
			Query queryObject = getSession().createSQLQuery(sf.toString());
			if (isFY) {
				queryObject.setFirstResult(firstResult);
				queryObject.setMaxResults(maxResults);
			}
			list = queryObject.list();
		} catch (RuntimeException re) {
			log.error("find Fdeduct by filter name failed", re);
			throw re;
		}
		return list;
	}

	public int getAllCount(String tableName,String filter){
		String queryString = "select count(*) from " + tableName+" "+filter;
		Query queryObject = getSession().createQuery(queryString);
		return Integer.parseInt(queryObject.list().get(0).toString());
	}
}