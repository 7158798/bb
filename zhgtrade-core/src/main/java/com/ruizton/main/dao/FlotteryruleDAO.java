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
import com.ruizton.main.model.Flotteryrule;

/**
 * A data access object (DAO) providing persistence and search support for
 * Flotteryrule entities. Transaction control of the save(), update() and
 * delete() operations can directly support Spring container-managed
 * transactions or they can be augmented to handle user-managed Spring
 * transactions. Each of these methods provides additional information for how
 * to configure it for the desired type of transaction control.
 * 
 * @see .Flotteryrule
 * @author MyEclipse Persistence Tools
 */
@Repository
public class FlotteryruleDAO extends HibernateDaoSupport {
	private static final Logger log = LoggerFactory
			.getLogger(FlotteryruleDAO.class);
	// property constants
	public static final String FTITLE = "ftitle";
	public static final String FAMOUNT = "famount";
	public static final String FQTY = "fqty";
	public static final String FPROBABILITY = "fprobability";

	public void save(Flotteryrule transientInstance) {
		log.debug("saving Flotteryrule instance");
		try {
			getSession().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(Flotteryrule persistentInstance) {
		log.debug("deleting Flotteryrule instance");
		try {
			getSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public Flotteryrule findById(java.lang.Integer id) {
		log.debug("getting Flotteryrule instance with id: " + id);
		try {
			Flotteryrule instance = (Flotteryrule) getSession().get(
					"com.ruizton.main.model.Flotteryrule", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List<Flotteryrule> findByExample(Flotteryrule instance) {
		log.debug("finding Flotteryrule instance by example");
		try {
			List<Flotteryrule> results = (List<Flotteryrule>) getSession()
					.createCriteria("com.ruizton.main.model.Flotteryrule").add(create(instance))
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
		log.debug("finding Flotteryrule instance with property: "
				+ propertyName + ", value: " + value);
		try {
			String queryString = "from Flotteryrule as model where model."
					+ propertyName + "= ?";
			Query queryObject = getSession().createQuery(queryString);
			queryObject.setParameter(0, value);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List<Flotteryrule> findByFtitle(Object ftitle) {
		return findByProperty(FTITLE, ftitle);
	}

	public List<Flotteryrule> findByFamount(Object famount) {
		return findByProperty(FAMOUNT, famount);
	}

	public List<Flotteryrule> findByFqty(Object fqty) {
		return findByProperty(FQTY, fqty);
	}

	public List<Flotteryrule> findByFprobability(Object fprobability) {
		return findByProperty(FPROBABILITY, fprobability);
	}

	public List findAll() {
		log.debug("finding all Flotteryrule instances");
		try {
			String queryString = "from Flotteryrule";
			Query queryObject = getSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public Flotteryrule merge(Flotteryrule detachedInstance) {
		log.debug("merging Flotteryrule instance");
		try {
			Flotteryrule result = (Flotteryrule) getSession().merge(
					detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(Flotteryrule instance) {
		log.debug("attaching dirty Flotteryrule instance");
		try {
			getSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(Flotteryrule instance) {
		log.debug("attaching clean Flotteryrule instance");
		try {
			getSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}
	
	public List<Flotteryrule> list(int firstResult, int maxResults, String filter,boolean isFY) {
		List<Flotteryrule> list = null;
		log.debug("finding Flotteryrule instance with filter");
		try {
			String queryString = "from Flotteryrule "+filter;
			Query queryObject = getSession().createQuery(queryString);
			if(isFY){
				queryObject.setFirstResult(firstResult);
				queryObject.setMaxResults(maxResults);
			}
			list = queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by Flotteryrule name failed", re);
			throw re;
		}
		return list;
	}
}