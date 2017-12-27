package com.ruizton.main.dao;

import static org.hibernate.criterion.Example.create;

import java.util.List;

import org.hibernate.LockMode;
import org.hibernate.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.ruizton.main.dao.comm.HibernateDaoSupport;
import com.ruizton.main.model.Fabout;
import com.ruizton.main.model.Fandroidpushment;

/**
 * A data access object (DAO) providing persistence and search support for
 * Fandroidpushment entities. Transaction control of the save(), update() and
 * delete() operations can directly support Spring container-managed
 * transactions or they can be augmented to handle user-managed Spring
 * transactions. Each of these methods provides additional information for how
 * to configure it for the desired type of transaction control.
 * 
 * @see com.ruizton.main.model.Fandroidpushment
 * @author MyEclipse Persistence Tools
 */

@Repository
public class FandroidpushmentDAO extends HibernateDaoSupport {
	private static final Logger log = LoggerFactory
			.getLogger(FandroidpushmentDAO.class);
	// property constants
	public static final String REGISTRATION_ID = "registrationId";
	public static final String PUSHMENT = "pushment";

	public void save(Fandroidpushment transientInstance) {
		log.debug("saving Fandroidpushment instance");
		try {
			getSession().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(Fandroidpushment persistentInstance) {
		log.debug("deleting Fandroidpushment instance");
		try {
			getSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public Fandroidpushment findById(java.lang.Integer id) {
		log.debug("getting Fandroidpushment instance with id: " + id);
		try {
			Fandroidpushment instance = (Fandroidpushment) getSession().get(
					"com.ruizton.main.model.Fandroidpushment", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List<Fandroidpushment> findByExample(Fandroidpushment instance) {
		log.debug("finding Fandroidpushment instance by example");
		try {
			List<Fandroidpushment> results = (List<Fandroidpushment>) getSession()
					.createCriteria("com.ruizton.main.model.Fandroidpushment")
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
		log.debug("finding Fandroidpushment instance with property: "
				+ propertyName + ", value: " + value);
		try {
			String queryString = "from Fandroidpushment as model where model."
					+ propertyName + "= ?";
			Query queryObject = getSession().createQuery(queryString);
			queryObject.setParameter(0, value);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List<Fandroidpushment> findByRegistrationId(Object registrationId) {
		return findByProperty(REGISTRATION_ID, registrationId);
	}

	public List<Fandroidpushment> findByPushment(Object pushment) {
		return findByProperty(PUSHMENT, pushment);
	}

	public List findAll() {
		log.debug("finding all Fandroidpushment instances");
		try {
			String queryString = "from Fandroidpushment";
			Query queryObject = getSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public Fandroidpushment merge(Fandroidpushment detachedInstance) {
		log.debug("merging Fandroidpushment instance");
		try {
			Fandroidpushment result = (Fandroidpushment) getSession().merge(
					detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(Fandroidpushment instance) {
		log.debug("attaching dirty Fandroidpushment instance");
		try {
			getSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(Fandroidpushment instance) {
		log.debug("attaching clean Fandroidpushment instance");
		try {
			getSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}
	
	public List<Fandroidpushment> list(int firstResult, int maxResults, String filter,boolean isFY) {
		List<Fandroidpushment> list = null;
		log.debug("finding Fandroidpushment instance with filter");
		try {
			String queryString = "from Fandroidpushment "+filter;
			Query queryObject = getSession().createQuery(queryString);
			if(isFY){
				queryObject.setFirstResult(firstResult);
				queryObject.setMaxResults(maxResults);
			}
			list = queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by Fandroidpushment name failed", re);
			throw re;
		}
		return list;
	}
}