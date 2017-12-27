package com.ruizton.main.dao;

import static org.hibernate.criterion.Example.create;

import java.util.List;

import org.hibernate.LockMode;
import org.hibernate.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.ruizton.main.dao.comm.HibernateDaoSupport;
import com.ruizton.main.model.FtaskLog;
import com.ruizton.main.model.FtaskLog;

/**
 * A data access object (DAO) providing persistence and search support for
 * FtaskLog entities. Transaction control of the save(), update() and delete()
 * operations can directly support Spring container-managed transactions or they
 * can be augmented to handle user-managed Spring transactions. Each of these
 * methods provides additional information for how to configure it for the
 * desired type of transaction control.
 * 
 * @see com.ruizton.main.model.FtaskLog
 * @author MyEclipse Persistence Tools
 */

@Repository
public class FtaskLogDAO extends HibernateDaoSupport {
	private static final Logger log = LoggerFactory
			.getLogger(FtaskLogDAO.class);
	// property constants
	public static final String REMARK = "remark";

	public void save(FtaskLog transientInstance) {
		log.debug("saving FtaskLog instance");
		try {
			getSession().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(FtaskLog persistentInstance) {
		log.debug("deleting FtaskLog instance");
		try {
			getSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public FtaskLog findById(java.lang.Integer id) {
		log.debug("getting FtaskLog instance with id: " + id);
		try {
			FtaskLog instance = (FtaskLog) getSession()
					.get("com.ruizton.main.model.FtaskLog", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List<FtaskLog> findByExample(FtaskLog instance) {
		log.debug("finding FtaskLog instance by example");
		try {
			List<FtaskLog> results = (List<FtaskLog>) getSession()
					.createCriteria("com.ruizton.main.model.FtaskLog").add(create(instance))
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
		log.debug("finding FtaskLog instance with property: " + propertyName
				+ ", value: " + value);
		try {
			String queryString = "from FtaskLog as model where model."
					+ propertyName + "= ?";
			Query queryObject = getSession().createQuery(queryString);
			queryObject.setParameter(0, value);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List<FtaskLog> findByRemark(Object remark) {
		return findByProperty(REMARK, remark);
	}

	public List findAll() {
		log.debug("finding all FtaskLog instances");
		try {
			String queryString = "from FtaskLog";
			Query queryObject = getSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public FtaskLog merge(FtaskLog detachedInstance) {
		log.debug("merging FtaskLog instance");
		try {
			FtaskLog result = (FtaskLog) getSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(FtaskLog instance) {
		log.debug("attaching dirty FtaskLog instance");
		try {
			getSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(FtaskLog instance) {
		log.debug("attaching clean FtaskLog instance");
		try {
			getSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}
	
	public List<FtaskLog> list(int firstResult, int maxResults, String filter,boolean isFY) {
		List<FtaskLog> list = null;
		log.debug("finding FtaskLog instance with filter");
		try {
			String queryString = "from FtaskLog "+filter;
			Query queryObject = getSession().createQuery(queryString);
			queryObject.setCacheable(true);
			if(isFY){
				queryObject.setFirstResult(firstResult);
				queryObject.setMaxResults(maxResults);
			}
			list = queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by FtaskLog name failed", re);
			throw re;
		}
		return list;
	}
}