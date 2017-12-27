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
import com.ruizton.main.model.Flendrule;
import com.ruizton.main.model.Flendsystemargs;

/**
 * A data access object (DAO) providing persistence and search support for
 * Flendrule entities. Transaction control of the save(), update() and delete()
 * operations can directly support Spring container-managed transactions or they
 * can be augmented to handle user-managed Spring transactions. Each of these
 * methods provides additional information for how to configure it for the
 * desired type of transaction control.
 * 
 * @see .Flendrule
 * @author MyEclipse Persistence Tools
 */
@Repository
public class FlendruleDAO extends HibernateDaoSupport {
	private static final Logger log = LoggerFactory
			.getLogger(FlendruleDAO.class);
	// property constants
	public static final String FTYPE = "ftype";
	public static final String FLEVEL = "flevel";
	public static final String FAMOUNT = "famount";
	public static final String FRATE = "frate";

	public void save(Flendrule transientInstance) {
		log.debug("saving Flendrule instance");
		try {
			getSession().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(Flendrule persistentInstance) {
		log.debug("deleting Flendrule instance");
		try {
			getSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public Flendrule findById(java.lang.Integer id) {
		log.debug("getting Flendrule instance with id: " + id);
		try {
			Flendrule instance = (Flendrule) getSession().get("com.ruizton.main.model.Flendrule", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List<Flendrule> findByExample(Flendrule instance) {
		log.debug("finding Flendrule instance by example");
		try {
			List<Flendrule> results = (List<Flendrule>) getSession()
					.createCriteria("com.ruizton.main.model.Flendrule").add(create(instance)).list();
			log.debug("find by example successful, result size: "
					+ results.size());
			return results;
		} catch (RuntimeException re) {
			log.error("find by example failed", re);
			throw re;
		}
	}

	public List findByProperty(String propertyName, Object value) {
		log.debug("finding Flendrule instance with property: " + propertyName
				+ ", value: " + value);
		try {
			String queryString = "from Flendrule as model where model."
					+ propertyName + "= ?";
			Query queryObject = getSession().createQuery(queryString);
			queryObject.setParameter(0, value);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List<Flendrule> findByFtype(Object ftype) {
		return findByProperty(FTYPE, ftype);
	}

	public List<Flendrule> findByFlevel(Object flevel) {
		return findByProperty(FLEVEL, flevel);
	}

	public List<Flendrule> findByFamount(Object famount) {
		return findByProperty(FAMOUNT, famount);
	}

	public List<Flendrule> findByFrate(Object frate) {
		return findByProperty(FRATE, frate);
	}

	public List findAll() {
		log.debug("finding all Flendrule instances");
		try {
			String queryString = "from Flendrule";
			Query queryObject = getSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public Flendrule merge(Flendrule detachedInstance) {
		log.debug("merging Flendrule instance");
		try {
			Flendrule result = (Flendrule) getSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(Flendrule instance) {
		log.debug("attaching dirty Flendrule instance");
		try {
			getSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(Flendrule instance) {
		log.debug("attaching clean Flendrule instance");
		try {
			getSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}
	
	public List<Flendrule> list(int firstResult, int maxResults, String filter,boolean isFY) {
		List<Flendrule> list = null;
		log.debug("finding Flendrule instance with filter");
		try {
			String queryString = "from Flendrule "+filter;
			Query queryObject = getSession().createQuery(queryString);
			queryObject.setCacheable(true);
			if(isFY){
				queryObject.setFirstResult(firstResult);
				queryObject.setMaxResults(maxResults);
			}
			list = queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by Flendrule name failed", re);
			throw re;
		}
		return list;
	}
}