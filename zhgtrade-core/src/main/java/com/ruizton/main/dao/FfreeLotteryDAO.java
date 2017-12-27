package com.ruizton.main.dao;

import static org.hibernate.criterion.Example.create;

import java.util.List;

import org.hibernate.LockMode;
import org.hibernate.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.ruizton.main.dao.comm.HibernateDaoSupport;
import com.ruizton.main.model.FfreeLottery;
import com.ruizton.main.model.FfreeLottery;

/**
 * A data access object (DAO) providing persistence and search support for
 * FfreeLottery entities. Transaction control of the save(), update() and
 * delete() operations can directly support Spring container-managed
 * transactions or they can be augmented to handle user-managed Spring
 * transactions. Each of these methods provides additional information for how
 * to configure it for the desired type of transaction control.
 * 
 * @see com.ruizton.main.model.FfreeLottery
 * @author MyEclipse Persistence Tools
 */

@Repository
public class FfreeLotteryDAO extends HibernateDaoSupport {
	private static final Logger log = LoggerFactory
			.getLogger(FfreeLotteryDAO.class);
	// property constants
	public static final String VERSION = "version";
	public static final String FREWARD = "freward";
	public static final String FISWIN = "fiswin";
	public static final String REMARK = "remark";

	public void save(FfreeLottery transientInstance) {
		log.debug("saving FfreeLottery instance");
		try {
			getSession().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(FfreeLottery persistentInstance) {
		log.debug("deleting FfreeLottery instance");
		try {
			getSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public FfreeLottery findById(java.lang.Integer id) {
		log.debug("getting FfreeLottery instance with id: " + id);
		try {
			FfreeLottery instance = (FfreeLottery) getSession().get(
					"com.ruizton.main.model.FfreeLottery", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List<FfreeLottery> findByExample(FfreeLottery instance) {
		log.debug("finding FfreeLottery instance by example");
		try {
			List<FfreeLottery> results = (List<FfreeLottery>) getSession()
					.createCriteria("com.ruizton.main.model.FfreeLottery").add(create(instance))
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
		log.debug("finding FfreeLottery instance with property: "
				+ propertyName + ", value: " + value);
		try {
			String queryString = "from FfreeLottery as model where model."
					+ propertyName + "= ?";
			Query queryObject = getSession().createQuery(queryString);
			queryObject.setParameter(0, value);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List<FfreeLottery> findByVersion(Object version) {
		return findByProperty(VERSION, version);
	}

	public List<FfreeLottery> findByFreward(Object freward) {
		return findByProperty(FREWARD, freward);
	}

	public List<FfreeLottery> findByFiswin(Object fiswin) {
		return findByProperty(FISWIN, fiswin);
	}

	public List<FfreeLottery> findByRemark(Object remark) {
		return findByProperty(REMARK, remark);
	}

	public List findAll() {
		log.debug("finding all FfreeLottery instances");
		try {
			String queryString = "from FfreeLottery";
			Query queryObject = getSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public FfreeLottery merge(FfreeLottery detachedInstance) {
		log.debug("merging FfreeLottery instance");
		try {
			FfreeLottery result = (FfreeLottery) getSession().merge(
					detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(FfreeLottery instance) {
		log.debug("attaching dirty FfreeLottery instance");
		try {
			getSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(FfreeLottery instance) {
		log.debug("attaching clean FfreeLottery instance");
		try {
			getSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}
	
	public List<FfreeLottery> list(int firstResult, int maxResults, String filter,boolean isFY) {
		List<FfreeLottery> list = null;
		log.debug("finding FfreeLottery instance with filter");
		try {
			String queryString = "from FfreeLottery "+filter;
			Query queryObject = getSession().createQuery(queryString);
			queryObject.setCacheable(true);
			if(isFY){
				queryObject.setFirstResult(firstResult);
				queryObject.setMaxResults(maxResults);
			}
			list = queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by FfreeLottery name failed", re);
			throw re;
		}
		return list;
	}
}