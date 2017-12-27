package com.ruizton.main.dao;
// default package

import static org.hibernate.criterion.Example.create;

import java.util.List;

import org.hibernate.LockMode;
import org.hibernate.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.ruizton.main.dao.comm.HibernateDaoSupport;
import com.ruizton.main.model.Fhedging;

/**
 * A data access object (DAO) providing persistence and search support for
 * Fhedging entities. Transaction control of the save(), update() and delete()
 * operations can directly support Spring container-managed transactions or they
 * can be augmented to handle user-managed Spring transactions. Each of these
 * methods provides additional information for how to configure it for the
 * desired type of transaction control.
 * 
 * @see .Fhedging
 * @author MyEclipse Persistence Tools
 */
@Repository
public class FhedgingDAO extends HibernateDaoSupport {
	private static final Logger log = LoggerFactory
			.getLogger(FhedgingDAO.class);
	// property constants
	public static final String VERSION = "version";
	public static final String FMINQTY = "fminqty";
	public static final String FMAXQTY = "fmaxqty";
	public static final String FRATE1 = "frate1";
	public static final String FRATE2 = "frate2";
	public static final String FRATE3 = "frate3";
	public static final String FPRICEURL = "fpriceurl";

	public void save(Fhedging transientInstance) {
		log.debug("saving Fhedging instance");
		try {
			getSession().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(Fhedging persistentInstance) {
		log.debug("deleting Fhedging instance");
		try {
			getSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public Fhedging findById(java.lang.Integer id) {
		log.debug("getting Fhedging instance with id: " + id);
		try {
			Fhedging instance = (Fhedging) getSession().get("com.ruizton.main.model.Fhedging", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List<Fhedging> findByExample(Fhedging instance) {
		log.debug("finding Fhedging instance by example");
		try {
			List<Fhedging> results = (List<Fhedging>) getSession()
					.createCriteria("com.ruizton.main.model.Fhedging").add(create(instance)).list();
			log.debug("find by example successful, result size: "
					+ results.size());
			return results;
		} catch (RuntimeException re) {
			log.error("find by example failed", re);
			throw re;
		}
	}

	public List findByProperty(String propertyName, Object value) {
		log.debug("finding Fhedging instance with property: " + propertyName
				+ ", value: " + value);
		try {
			String queryString = "from Fhedging as model where model."
					+ propertyName + "= ?";
			Query queryObject = getSession().createQuery(queryString);
			queryObject.setParameter(0, value);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List<Fhedging> findByVersion(Object version) {
		return findByProperty(VERSION, version);
	}

	public List<Fhedging> findByFminqty(Object fminqty) {
		return findByProperty(FMINQTY, fminqty);
	}

	public List<Fhedging> findByFmaxqty(Object fmaxqty) {
		return findByProperty(FMAXQTY, fmaxqty);
	}

	public List<Fhedging> findByFrate1(Object frate1) {
		return findByProperty(FRATE1, frate1);
	}

	public List<Fhedging> findByFrate2(Object frate2) {
		return findByProperty(FRATE2, frate2);
	}

	public List<Fhedging> findByFrate3(Object frate3) {
		return findByProperty(FRATE3, frate3);
	}

	public List<Fhedging> findByFpriceurl(Object fpriceurl) {
		return findByProperty(FPRICEURL, fpriceurl);
	}

	public List findAll() {
		log.debug("finding all Fhedging instances");
		try {
			String queryString = "from Fhedging";
			Query queryObject = getSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public Fhedging merge(Fhedging detachedInstance) {
		log.debug("merging Fhedging instance");
		try {
			Fhedging result = (Fhedging) getSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(Fhedging instance) {
		log.debug("attaching dirty Fhedging instance");
		try {
			getSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(Fhedging instance) {
		log.debug("attaching clean Fhedging instance");
		try {
			getSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}
	
	public List<Fhedging> list(int firstResult, int maxResults, String filter,boolean isFY) {
		List<Fhedging> list = null;
		log.debug("finding Fhedging instance with filter");
		try {
			String queryString = "from Fhedging "+filter;
			Query queryObject = getSession().createQuery(queryString);
			queryObject.setCacheable(true);
			if(isFY){
				queryObject.setFirstResult(firstResult);
				queryObject.setMaxResults(maxResults);
			}
			list = queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by Fhedging name failed", re);
			throw re;
		}
		return list;
	}
}