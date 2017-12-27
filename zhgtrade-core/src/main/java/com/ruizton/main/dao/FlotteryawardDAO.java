package com.ruizton.main.dao;

import java.util.List;
import org.hibernate.LockMode;
import org.hibernate.Query;
import static org.hibernate.criterion.Example.create;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.ruizton.main.dao.comm.HibernateDaoSupport;
import com.ruizton.main.model.Flotteryaward;

/**
 * A data access object (DAO) providing persistence and search support for
 * Flotteryaward entities. Transaction control of the save(), update() and
 * delete() operations can directly support Spring container-managed
 * transactions or they can be augmented to handle user-managed Spring
 * transactions. Each of these methods provides additional information for how
 * to configure it for the desired type of transaction control.
 * 
 * @see .Flotteryaward
 * @author MyEclipse Persistence Tools
 */
@Repository
public class FlotteryawardDAO extends HibernateDaoSupport {
	private static final Logger log = LoggerFactory
			.getLogger(FlotteryawardDAO.class);
	// property constants
	public static final String FNAME = "fname";
	public static final String FANGLE = "fangle";
	public static final String FCHANCE = "fchance";

	public void save(Flotteryaward transientInstance) {
		log.debug("saving Flotteryaward instance");
		try {
			getSession().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(Flotteryaward persistentInstance) {
		log.debug("deleting Flotteryaward instance");
		try {
			getSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public Flotteryaward findById(java.lang.Integer id) {
		log.debug("getting Flotteryaward instance with id: " + id);
		try {
			Flotteryaward instance = (Flotteryaward) getSession().get(
					"com.ruizton.main.model.Flotteryaward", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List<Flotteryaward> findByExample(Flotteryaward instance) {
		log.debug("finding Flotteryaward instance by example");
		try {
			List<Flotteryaward> results = (List<Flotteryaward>) getSession()
					.createCriteria("com.ruizton.main.model.Flotteryaward")
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
		log.debug("finding Flotteryaward instance with property: "
				+ propertyName + ", value: " + value);
		try {
			String queryString = "from Flotteryaward as model where model."
					+ propertyName + "= ?";
			Query queryObject = getSession().createQuery(queryString);
			queryObject.setParameter(0, value);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List<Flotteryaward> findByFname(Object fname) {
		return findByProperty(FNAME, fname);
	}

	public List<Flotteryaward> findByFangle(Object fangle) {
		return findByProperty(FANGLE, fangle);
	}

	public List<Flotteryaward> findByFchance(Object fchance) {
		return findByProperty(FCHANCE, fchance);
	}

	public List findAll() {
		log.debug("finding all Flotteryaward instances");
		try {
			String queryString = "from Flotteryaward";
			Query queryObject = getSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public Flotteryaward merge(Flotteryaward detachedInstance) {
		log.debug("merging Flotteryaward instance");
		try {
			Flotteryaward result = (Flotteryaward) getSession().merge(
					detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(Flotteryaward instance) {
		log.debug("attaching dirty Flotteryaward instance");
		try {
			getSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(Flotteryaward instance) {
		log.debug("attaching clean Flotteryaward instance");
		try {
			getSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public List<Flotteryaward> list(int firstResult, int maxResults, String filter,boolean isFY) {
		List<Flotteryaward> list = null;
		log.debug("finding Flotteryaward instance with filter");
		try {
			String queryString = "from Flotteryaward "+filter;
			Query queryObject = getSession().createQuery(queryString);
			if(isFY){
				queryObject.setFirstResult(firstResult);
				queryObject.setMaxResults(maxResults);
			}
			list = queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by Flotteryaward name failed", re);
			throw re;
		}
		return list;
	}
}