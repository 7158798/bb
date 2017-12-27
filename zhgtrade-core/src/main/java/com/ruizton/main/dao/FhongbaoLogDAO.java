package com.ruizton.main.dao;

import static org.hibernate.criterion.Example.create;

import java.util.List;

import org.hibernate.LockMode;
import org.hibernate.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.ruizton.main.dao.comm.HibernateDaoSupport;
import com.ruizton.main.model.FhongbaoLog;

/**
 * A data access object (DAO) providing persistence and search support for
 * FhongbaoLog entities. Transaction control of the save(), update() and
 * delete() operations can directly support Spring container-managed
 * transactions or they can be augmented to handle user-managed Spring
 * transactions. Each of these methods provides additional information for how
 * to configure it for the desired type of transaction control.
 * 
 * @see com.ruizton.main.model.FhongbaoLog
 * @author MyEclipse Persistence Tools
 */

@Repository
public class FhongbaoLogDAO extends HibernateDaoSupport {
	private static final Logger log = LoggerFactory
			.getLogger(FhongbaoLogDAO.class);
	// property constants
	public static final String VERSION = "version";
	public static final String FAMOUNT = "famount";

	public void save(FhongbaoLog transientInstance) {
		log.debug("saving FhongbaoLog instance");
		try {
			getSession().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(FhongbaoLog persistentInstance) {
		log.debug("deleting FhongbaoLog instance");
		try {
			getSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public FhongbaoLog findById(java.lang.Integer id) {
		log.debug("getting FhongbaoLog instance with id: " + id);
		try {
			FhongbaoLog instance = (FhongbaoLog) getSession().get(
					"com.ruizton.main.model.FhongbaoLog", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List<FhongbaoLog> findByExample(FhongbaoLog instance) {
		log.debug("finding FhongbaoLog instance by example");
		try {
			List<FhongbaoLog> results = (List<FhongbaoLog>) getSession()
					.createCriteria("com.ruizton.main.model.FhongbaoLog").add(create(instance))
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
		log.debug("finding FhongbaoLog instance with property: " + propertyName
				+ ", value: " + value);
		try {
			String queryString = "from FhongbaoLog as model where model."
					+ propertyName + "= ?";
			Query queryObject = getSession().createQuery(queryString);
			queryObject.setParameter(0, value);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List<FhongbaoLog> findByVersion(Object version) {
		return findByProperty(VERSION, version);
	}

	public List<FhongbaoLog> findByFamount(Object famount) {
		return findByProperty(FAMOUNT, famount);
	}

	public List findAll() {
		log.debug("finding all FhongbaoLog instances");
		try {
			String queryString = "from FhongbaoLog";
			Query queryObject = getSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public FhongbaoLog merge(FhongbaoLog detachedInstance) {
		log.debug("merging FhongbaoLog instance");
		try {
			FhongbaoLog result = (FhongbaoLog) getSession().merge(
					detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(FhongbaoLog instance) {
		log.debug("attaching dirty FhongbaoLog instance");
		try {
			getSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(FhongbaoLog instance) {
		log.debug("attaching clean FhongbaoLog instance");
		try {
			getSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}
}