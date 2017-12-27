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
import com.ruizton.main.model.Fpopcornlog;
import com.ruizton.main.model.Fuser;

/**
 * A data access object (DAO) providing persistence and search support for
 * Fpopcornlog entities. Transaction control of the save(), update() and
 * delete() operations can directly support Spring container-managed
 * transactions or they can be augmented to handle user-managed Spring
 * transactions. Each of these methods provides additional information for how
 * to configure it for the desired type of transaction control.
 * 
 * @see .Fpopcornlog
 * @author MyEclipse Persistence Tools
 */
@Repository
public class FpopcornlogDAO extends HibernateDaoSupport {
	private static final Logger log = LoggerFactory
			.getLogger(FpopcornlogDAO.class);
	// property constants
	public static final String FSTATUS = "fstatus";
	public static final String FRESULT_QTY = "fresultQty";
	public static final String FRESULT1 = "fresult1";
	public static final String FRESULT2 = "fresult2";

	public void save(Fpopcornlog transientInstance) {
		log.debug("saving Fpopcornlog instance");
		try {
			getSession().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(Fpopcornlog persistentInstance) {
		log.debug("deleting Fpopcornlog instance");
		try {
			getSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public Fpopcornlog findById(java.lang.Integer id) {
		log.debug("getting Fpopcornlog instance with id: " + id);
		try {
			Fpopcornlog instance = (Fpopcornlog) getSession().get(
					"com.ruizton.main.model.Fpopcornlog", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List<Fpopcornlog> findByExample(Fpopcornlog instance) {
		log.debug("finding Fpopcornlog instance by example");
		try {
			List<Fpopcornlog> results = (List<Fpopcornlog>) getSession()
					.createCriteria("com.ruizton.main.model.Fpopcornlog").add(create(instance)).list();
			log.debug("find by example successful, result size: "
					+ results.size());
			return results;
		} catch (RuntimeException re) {
			log.error("find by example failed", re);
			throw re;
		}
	}

	public List findByProperty(String propertyName, Object value) {
		log.debug("finding Fpopcornlog instance with property: " + propertyName
				+ ", value: " + value);
		try {
			String queryString = "from Fpopcornlog as model where model."
					+ propertyName + "= ?";
			Query queryObject = getSession().createQuery(queryString);
			queryObject.setParameter(0, value);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List<Fpopcornlog> findByFstatus(Object fstatus) {
		return findByProperty(FSTATUS, fstatus);
	}

	public List<Fpopcornlog> findByFresultQty(Object fresultQty) {
		return findByProperty(FRESULT_QTY, fresultQty);
	}

	public List<Fpopcornlog> findByFresult1(Object fresult1) {
		return findByProperty(FRESULT1, fresult1);
	}

	public List<Fpopcornlog> findByFresult2(Object fresult2) {
		return findByProperty(FRESULT2, fresult2);
	}

	public List findAll() {
		log.debug("finding all Fpopcornlog instances");
		try {
			String queryString = "from Fpopcornlog";
			Query queryObject = getSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public Fpopcornlog merge(Fpopcornlog detachedInstance) {
		log.debug("merging Fpopcornlog instance");
		try {
			Fpopcornlog result = (Fpopcornlog) getSession().merge(
					detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(Fpopcornlog instance) {
		log.debug("attaching dirty Fpopcornlog instance");
		try {
			getSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(Fpopcornlog instance) {
		log.debug("attaching clean Fpopcornlog instance");
		try {
			getSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}
	
	public List<Fpopcornlog> list(int firstResult, int maxResults,
			String filter,boolean isFY) {
		List<Fpopcornlog> list = null;
		log.debug("finding Fpopcornlog instance with filter");
		try {
			String queryString = "from Fpopcornlog "+filter;
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
}