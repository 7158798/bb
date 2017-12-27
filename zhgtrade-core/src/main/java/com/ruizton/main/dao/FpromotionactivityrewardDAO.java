package com.ruizton.main.dao;

import static org.hibernate.criterion.Example.create;

import java.util.List;

import org.hibernate.LockMode;
import org.hibernate.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.ruizton.main.dao.comm.HibernateDaoSupport;
import com.ruizton.main.model.Factivityreward;
import com.ruizton.main.model.Fpromotionactivityreward;

/**
 * A data access object (DAO) providing persistence and search support for
 * Fpromotionactivityreward entities. Transaction control of the save(),
 * update() and delete() operations can directly support Spring
 * container-managed transactions or they can be augmented to handle
 * user-managed Spring transactions. Each of these methods provides additional
 * information for how to configure it for the desired type of transaction
 * control.
 * 
 * @see ztmp.Fpromotionactivityreward
 * @author MyEclipse Persistence Tools
 */
@Repository
public class FpromotionactivityrewardDAO extends HibernateDaoSupport {
	private static final Logger log = LoggerFactory
			.getLogger(FpromotionactivityrewardDAO.class);
	// property constants
	public static final String FVIRTUAL_COIN_OR_CNY = "fvirtualCoinOrCny";
	public static final String FRATE_OR_REAL = "frateOrReal";
	public static final String FAMOUNT = "famount";

	public void save(Fpromotionactivityreward transientInstance) {
		log.debug("saving Fpromotionactivityreward instance");
		try {
			getSession().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(Fpromotionactivityreward persistentInstance) {
		log.debug("deleting Fpromotionactivityreward instance");
		try {
			getSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public Fpromotionactivityreward findById(java.lang.Integer id) {
		log.debug("getting Fpromotionactivityreward instance with id: " + id);
		try {
			Fpromotionactivityreward instance = (Fpromotionactivityreward) getSession()
					.get("com.ruizton.main.model.Fpromotionactivityreward", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List<Fpromotionactivityreward> findByExample(
			Fpromotionactivityreward instance) {
		log.debug("finding Fpromotionactivityreward instance by example");
		try {
			List<Fpromotionactivityreward> results = (List<Fpromotionactivityreward>) getSession()
					.createCriteria(
							"com.ruizton.main.model.Fpromotionactivityreward")
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
		log.debug("finding Fpromotionactivityreward instance with property: "
				+ propertyName + ", value: " + value);
		try {
			String queryString = "from Fpromotionactivityreward as model where model."
					+ propertyName + "= ?";
			Query queryObject = getSession().createQuery(queryString);
			queryObject.setParameter(0, value);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List<Fpromotionactivityreward> findByFvirtualCoinOrCny(
			Object fvirtualCoinOrCny) {
		return findByProperty(FVIRTUAL_COIN_OR_CNY, fvirtualCoinOrCny);
	}

	public List<Fpromotionactivityreward> findByFrateOrReal(Object frateOrReal) {
		return findByProperty(FRATE_OR_REAL, frateOrReal);
	}

	public List<Fpromotionactivityreward> findByFamount(Object famount) {
		return findByProperty(FAMOUNT, famount);
	}

	public List findAll() {
		log.debug("finding all Fpromotionactivityreward instances");
		try {
			String queryString = "from Fpromotionactivityreward";
			Query queryObject = getSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public Fpromotionactivityreward merge(
			Fpromotionactivityreward detachedInstance) {
		log.debug("merging Fpromotionactivityreward instance");
		try {
			Fpromotionactivityreward result = (Fpromotionactivityreward) getSession()
					.merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(Fpromotionactivityreward instance) {
		log.debug("attaching dirty Fpromotionactivityreward instance");
		try {
			getSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(Fpromotionactivityreward instance) {
		log.debug("attaching clean Fpromotionactivityreward instance");
		try {
			getSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}
	
	public List<Fpromotionactivityreward> list(int firstResult, int maxResults, String filter,boolean isFY) {
		List<Fpromotionactivityreward> list = null;
		log.debug("finding Fpromotionactivityreward instance with filter");
		try {
			String queryString = "from Fpromotionactivityreward "+filter;
			Query queryObject = getSession().createQuery(queryString);
			if(isFY){
				queryObject.setFirstResult(firstResult);
				queryObject.setMaxResults(maxResults);
			}
			list = queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by Fpromotionactivityreward name failed", re);
			throw re;
		}
		return list;
	}
}