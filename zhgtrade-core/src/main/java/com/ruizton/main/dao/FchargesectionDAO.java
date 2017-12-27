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
import com.ruizton.main.model.Fchargesection;
import com.ruizton.main.model.Flog;

/**
 * A data access object (DAO) providing persistence and search support for
 * Fchargesection entities. Transaction control of the save(), update() and
 * delete() operations can directly support Spring container-managed
 * transactions or they can be augmented to handle user-managed Spring
 * transactions. Each of these methods provides additional information for how
 * to configure it for the desired type of transaction control.
 * 
 * @see .Fchargesection
 * @author MyEclipse Persistence Tools
 */
@Repository
public class FchargesectionDAO extends HibernateDaoSupport {
	private static final Logger log = LoggerFactory
			.getLogger(FchargesectionDAO.class);
	// property constants
	public static final String FSTART_HOUR = "fstartHour";
	public static final String FEND_HOUR = "fendHour";
	public static final String FTIMESPAN = "ftimespan";

	public void save(Fchargesection transientInstance) {
		log.debug("saving Fchargesection instance");
		try {
			getSession().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(Fchargesection persistentInstance) {
		log.debug("deleting Fchargesection instance");
		try {
			getSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public Fchargesection findById(java.lang.Integer id) {
		log.debug("getting Fchargesection instance with id: " + id);
		try {
			Fchargesection instance = (Fchargesection) getSession().get(
					"com.ruizton.main.model.Fchargesection", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List<Fchargesection> findByExample(Fchargesection instance) {
		log.debug("finding Fchargesection instance by example");
		try {
			List<Fchargesection> results = (List<Fchargesection>) getSession()
					.createCriteria("com.ruizton.main.model.Fchargesection")
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
		log.debug("finding Fchargesection instance with property: "
				+ propertyName + ", value: " + value);
		try {
			String queryString = "from Fchargesection as model where model."
					+ propertyName + "= ?";
			Query queryObject = getSession().createQuery(queryString);
			queryObject.setParameter(0, value);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List<Fchargesection> findByFstartHour(Object fstartHour) {
		return findByProperty(FSTART_HOUR, fstartHour);
	}

	public List<Fchargesection> findByFendHour(Object fendHour) {
		return findByProperty(FEND_HOUR, fendHour);
	}

	public List<Fchargesection> findByFtimespan(Object ftimespan) {
		return findByProperty(FTIMESPAN, ftimespan);
	}

	public List findAll() {
		log.debug("finding all Fchargesection instances");
		try {
			String queryString = "from Fchargesection";
			Query queryObject = getSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public Fchargesection merge(Fchargesection detachedInstance) {
		log.debug("merging Fchargesection instance");
		try {
			Fchargesection result = (Fchargesection) getSession().merge(
					detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(Fchargesection instance) {
		log.debug("attaching dirty Fchargesection instance");
		try {
			getSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(Fchargesection instance) {
		log.debug("attaching clean Fchargesection instance");
		try {
			getSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}
	
	public List<Fchargesection> list(int firstResult, int maxResults, String filter,boolean isFY) {
		List<Fchargesection> list = null;
		log.debug("finding Fchargesection instance with filter");
		try {
			String queryString = "from Fchargesection "+filter;
			Query queryObject = getSession().createQuery(queryString);
			if(isFY){
				queryObject.setFirstResult(firstResult);
				queryObject.setMaxResults(maxResults);
			}
			list = queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by Fchargesection name failed", re);
			throw re;
		}
		return list;
	}
}