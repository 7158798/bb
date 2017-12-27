package com.ruizton.main.dao;

import static org.hibernate.criterion.Example.create;

import java.util.List;

import org.hibernate.LockMode;
import org.hibernate.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.ruizton.main.dao.comm.HibernateDaoSupport;
import com.ruizton.main.model.Fpaycode;

/**
 * A data access object (DAO) providing persistence and search support for
 * Fpaycode entities. Transaction control of the save(), update() and delete()
 * operations can directly support Spring container-managed transactions or they
 * can be augmented to handle user-managed Spring transactions. Each of these
 * methods provides additional information for how to configure it for the
 * desired type of transaction control.
 * 
 * @see com.ruizton.main.model.Fpaycode
 * @author MyEclipse Persistence Tools
 */

@Repository
public class FpaycodeDAO extends HibernateDaoSupport {
	private static final Logger log = LoggerFactory
			.getLogger(FpaycodeDAO.class);
	// property constants
	public static final String FKEY = "fkey";
	public static final String FVALUE = "fvalue";
	public static final String FAMOUNT = "famount";
	public static final String FSTATUS = "fstatus";

	public void save(Fpaycode transientInstance) {
		log.debug("saving Fpaycode instance");
		try {
			getSession().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(Fpaycode persistentInstance) {
		log.debug("deleting Fpaycode instance");
		try {
			getSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public Fpaycode findById(java.lang.Integer id) {
		log.debug("getting Fpaycode instance with id: " + id);
		try {
			Fpaycode instance = (Fpaycode) getSession()
					.get("com.ruizton.main.model.Fpaycode", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List<Fpaycode> findByExample(Fpaycode instance) {
		log.debug("finding Fpaycode instance by example");
		try {
			List<Fpaycode> results = (List<Fpaycode>) getSession()
					.createCriteria("com.ruizton.main.model.Fpaycode").add(create(instance))
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
		log.debug("finding Fpaycode instance with property: " + propertyName
				+ ", value: " + value);
		try {
			String queryString = "from Fpaycode as model where model."
					+ propertyName + "= ?";
			Query queryObject = getSession().createQuery(queryString);
			queryObject.setParameter(0, value);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List<Fpaycode> findByFkey(Object fkey) {
		return findByProperty(FKEY, fkey);
	}

	public List<Fpaycode> findByFvalue(Object fvalue) {
		return findByProperty(FVALUE, fvalue);
	}

	public List<Fpaycode> findByFamount(Object famount) {
		return findByProperty(FAMOUNT, famount);
	}

	public List<Fpaycode> findByFstatus(Object fstatus) {
		return findByProperty(FSTATUS, fstatus);
	}

	public List findAll() {
		log.debug("finding all Fpaycode instances");
		try {
			String queryString = "from Fpaycode";
			Query queryObject = getSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public Fpaycode merge(Fpaycode detachedInstance) {
		log.debug("merging Fpaycode instance");
		try {
			Fpaycode result = (Fpaycode) getSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}
	public List<Fpaycode> list(int firstResult, int maxResults, String filter,boolean isFY) {
		List<Fpaycode> list = null;
		log.debug("finding Fpaycode instance with filter");
		try {
			String queryString = "from Fpaycode "+filter;
			Query queryObject = getSession().createQuery(queryString);
			if(isFY){
				queryObject.setFirstResult(firstResult);
				queryObject.setMaxResults(maxResults);
			}
			list = queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by Fpaycode name failed", re);
			throw re;
		}
		return list;
	}
	public void attachDirty(Fpaycode instance) {
		log.debug("attaching dirty Fpaycode instance");
		try {
			getSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(Fpaycode instance) {
		log.debug("attaching clean Fpaycode instance");
		try {
			getSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}
}