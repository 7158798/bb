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
import com.ruizton.main.model.Fhedginglog;

/**
 * A data access object (DAO) providing persistence and search support for
 * Fhedginglog entities. Transaction control of the save(), update() and
 * delete() operations can directly support Spring container-managed
 * transactions or they can be augmented to handle user-managed Spring
 * transactions. Each of these methods provides additional information for how
 * to configure it for the desired type of transaction control.
 * 
 * @see .Fhedginglog
 * @author MyEclipse Persistence Tools
 */
@Repository
public class FhedginglogDAO extends HibernateDaoSupport {
	private static final Logger log = LoggerFactory
			.getLogger(FhedginglogDAO.class);
	// property constants
	public static final String VERSION = "version";
	public static final String FTOTAL1 = "ftotal1";
	public static final String FTOTAL2 = "ftotal2";
	public static final String FTOTAL3 = "ftotal3";
	public static final String FSTARTPRICE = "fstartprice";
	public static final String FENDPRICE = "fendprice";
	public static final String FSTATUS = "fstatus";
	public static final String FRESULT = "fresult";
	public static final String FNUMBER = "fnumber";

	public void save(Fhedginglog transientInstance) {
		log.debug("saving Fhedginglog instance");
		try {
			getSession().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(Fhedginglog persistentInstance) {
		log.debug("deleting Fhedginglog instance");
		try {
			getSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public Fhedginglog findById(java.lang.Integer id) {
		log.debug("getting Fhedginglog instance with id: " + id);
		try {
			Fhedginglog instance = (Fhedginglog) getSession().get(
					"com.ruizton.main.model.Fhedginglog", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List<Fhedginglog> findByExample(Fhedginglog instance) {
		log.debug("finding Fhedginglog instance by example");
		try {
			List<Fhedginglog> results = (List<Fhedginglog>) getSession()
					.createCriteria("com.ruizton.main.model.Fhedginglog")
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
		log.debug("finding Fhedginglog instance with property: " + propertyName
				+ ", value: " + value);
		try {
			String queryString = "from Fhedginglog as model where model."
					+ propertyName + "= ?";
			Query queryObject = getSession().createQuery(queryString);
			queryObject.setParameter(0, value);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List<Fhedginglog> findByVersion(Object version) {
		return findByProperty(VERSION, version);
	}

	public List<Fhedginglog> findByFtotal1(Object ftotal1) {
		return findByProperty(FTOTAL1, ftotal1);
	}

	public List<Fhedginglog> findByFtotal2(Object ftotal2) {
		return findByProperty(FTOTAL2, ftotal2);
	}

	public List<Fhedginglog> findByFtotal3(Object ftotal3) {
		return findByProperty(FTOTAL3, ftotal3);
	}

	public List<Fhedginglog> findByFstartprice(Object fstartprice) {
		return findByProperty(FSTARTPRICE, fstartprice);
	}

	public List<Fhedginglog> findByFendprice(Object fendprice) {
		return findByProperty(FENDPRICE, fendprice);
	}

	public List<Fhedginglog> findByFstatus(Object fstatus) {
		return findByProperty(FSTATUS, fstatus);
	}

	public List<Fhedginglog> findByFresult(Object fresult) {
		return findByProperty(FRESULT, fresult);
	}

	public List<Fhedginglog> findByFnumber(Object fnumber) {
		return findByProperty(FNUMBER, fnumber);
	}

	public List findAll() {
		log.debug("finding all Fhedginglog instances");
		try {
			String queryString = "from Fhedginglog";
			Query queryObject = getSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public Fhedginglog merge(Fhedginglog detachedInstance) {
		log.debug("merging Fhedginglog instance");
		try {
			Fhedginglog result = (Fhedginglog) getSession().merge(
					detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(Fhedginglog instance) {
		log.debug("attaching dirty Fhedginglog instance");
		try {
			getSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(Fhedginglog instance) {
		log.debug("attaching clean Fhedginglog instance");
		try {
			getSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public List<Fhedginglog> list(int firstResult, int maxResults,
			String filter, boolean isFY) {
		List<Fhedginglog> list = null;
		log.debug("finding Fhedginglog instance with filter");
		try {
			String queryString = "from Fhedginglog " + filter;
			Query queryObject = getSession().createQuery(queryString);
			queryObject.setCacheable(true);
			if (isFY) {
				queryObject.setFirstResult(firstResult);
				queryObject.setMaxResults(maxResults);
			}
			list = queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by Fhedginglog name failed", re);
			throw re;
		}
		return list;
	}
}