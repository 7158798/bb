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
import com.ruizton.main.model.Fvouchers;
import com.ruizton.main.model.Fvouchers;

/**
 * A data access object (DAO) providing persistence and search support for
 * Fvouchers entities. Transaction control of the save(), update() and delete()
 * operations can directly support Spring container-managed transactions or they
 * can be augmented to handle user-managed Spring transactions. Each of these
 * methods provides additional information for how to configure it for the
 * desired type of transaction control.
 * 
 * @see .Fvouchers
 * @author MyEclipse Persistence Tools
 */
@Repository
public class FvouchersDAO extends HibernateDaoSupport {
	private static final Logger log = LoggerFactory
			.getLogger(FvouchersDAO.class);
	// property constants
	public static final String FAMOUNT = "famount";
	public static final String FSTATUS = "fstatus";
	public static final String FLASTAMOUNT = "flastamount";

	public void save(Fvouchers transientInstance) {
		log.debug("saving Fvouchers instance");
		try {
			getSession().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(Fvouchers persistentInstance) {
		log.debug("deleting Fvouchers instance");
		try {
			getSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public Fvouchers findById(java.lang.Integer id) {
		log.debug("getting Fvouchers instance with id: " + id);
		try {
			Fvouchers instance = (Fvouchers) getSession().get(
					"com.ruizton.main.model.Fvouchers", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List<Fvouchers> findByExample(Fvouchers instance) {
		log.debug("finding Fvouchers instance by example");
		try {
			List<Fvouchers> results = (List<Fvouchers>) getSession()
					.createCriteria("com.ruizton.main.model.Fvouchers")
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
		log.debug("finding Fvouchers instance with property: " + propertyName
				+ ", value: " + value);
		try {
			String queryString = "from Fvouchers as model where model."
					+ propertyName + "= ?";
			Query queryObject = getSession().createQuery(queryString);
			queryObject.setParameter(0, value);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List<Fvouchers> findByFamount(Object famount) {
		return findByProperty(FAMOUNT, famount);
	}

	public List<Fvouchers> findByFstatus(Object fstatus) {
		return findByProperty(FSTATUS, fstatus);
	}

	public List<Fvouchers> findByFlastamount(Object flastamount) {
		return findByProperty(FLASTAMOUNT, flastamount);
	}

	public List findAll() {
		log.debug("finding all Fvouchers instances");
		try {
			String queryString = "from Fvouchers";
			Query queryObject = getSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public Fvouchers merge(Fvouchers detachedInstance) {
		log.debug("merging Fvouchers instance");
		try {
			Fvouchers result = (Fvouchers) getSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(Fvouchers instance) {
		log.debug("attaching dirty Fvouchers instance");
		try {
			getSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(Fvouchers instance) {
		log.debug("attaching clean Fvouchers instance");
		try {
			getSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}
	
	public List<Fvouchers> list(int firstResult, int maxResults, String filter,boolean isFY) {
		List<Fvouchers> list = null;
		log.debug("finding Fvouchers instance with filter");
		try {
			String queryString = "from Fvouchers "+filter;
			Query queryObject = getSession().createQuery(queryString);
			if(isFY){
				queryObject.setFirstResult(firstResult);
				queryObject.setMaxResults(maxResults);
			}
			list = queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by Fvouchers name failed", re);
			throw re;
		}
		return list;
	}
}