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
import com.ruizton.main.model.Flendflowlog;
import com.ruizton.main.model.Fwallet;

/**
 * A data access object (DAO) providing persistence and search support for
 * Flendflowlog entities. Transaction control of the save(), update() and
 * delete() operations can directly support Spring container-managed
 * transactions or they can be augmented to handle user-managed Spring
 * transactions. Each of these methods provides additional information for how
 * to configure it for the desired type of transaction control.
 * 
 * @see .Flendflowlog
 * @author MyEclipse Persistence Tools
 */
@Repository
public class FlendflowlogDAO extends HibernateDaoSupport {
	private static final Logger log = LoggerFactory
			.getLogger(FlendflowlogDAO.class);
	// property constants
	public static final String FTYPE = "ftype";
	public static final String FISCNY = "fiscny";
	public static final String FAMOUNT = "famount";

	public void save(Flendflowlog transientInstance) {
		log.debug("saving Flendflowlog instance");
		try {
			getSession().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(Flendflowlog persistentInstance) {
		log.debug("deleting Flendflowlog instance");
		try {
			getSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public Flendflowlog findById(java.lang.Integer id) {
		log.debug("getting Flendflowlog instance with id: " + id);
		try {
			Flendflowlog instance = (Flendflowlog) getSession().get(
					"com.ruizton.main.model.Flendflowlog", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List<Flendflowlog> findByExample(Flendflowlog instance) {
		log.debug("finding Flendflowlog instance by example");
		try {
			List<Flendflowlog> results = (List<Flendflowlog>) getSession()
					.createCriteria("com.ruizton.main.model.Flendflowlog")
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
		log.debug("finding Flendflowlog instance with property: "
				+ propertyName + ", value: " + value);
		try {
			String queryString = "from Flendflowlog as model where model."
					+ propertyName + "= ?";
			Query queryObject = getSession().createQuery(queryString);
			queryObject.setParameter(0, value);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List<Flendflowlog> findByFtype(Object ftype) {
		return findByProperty(FTYPE, ftype);
	}

	public List<Flendflowlog> findByFiscny(Object fiscny) {
		return findByProperty(FISCNY, fiscny);
	}

	public List<Flendflowlog> findByFamount(Object famount) {
		return findByProperty(FAMOUNT, famount);
	}

	public List findAll() {
		log.debug("finding all Flendflowlog instances");
		try {
			String queryString = "from Flendflowlog";
			Query queryObject = getSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public Flendflowlog merge(Flendflowlog detachedInstance) {
		log.debug("merging Flendflowlog instance");
		try {
			Flendflowlog result = (Flendflowlog) getSession().merge(
					detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(Flendflowlog instance) {
		log.debug("attaching dirty Flendflowlog instance");
		try {
			getSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(Flendflowlog instance) {
		log.debug("attaching clean Flendflowlog instance");
		try {
			getSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}
	
	public List<Flendflowlog> list(int firstResult, int maxResults, String filter,
			boolean isFY) {
		List<Flendflowlog> list = null;
		log.debug("finding Flendflowlog instance with filter");
		try {
			String queryString = "from Flendflowlog " + filter;
			Query queryObject = getSession().createQuery(queryString);
			queryObject.setCacheable(true);
			if (isFY) {
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