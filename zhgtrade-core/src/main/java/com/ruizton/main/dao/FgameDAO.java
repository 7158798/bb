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
import com.ruizton.main.model.Fgame;
import com.ruizton.main.model.Flog;

/**
 * A data access object (DAO) providing persistence and search support for Fgame
 * entities. Transaction control of the save(), update() and delete() operations
 * can directly support Spring container-managed transactions or they can be
 * augmented to handle user-managed Spring transactions. Each of these methods
 * provides additional information for how to configure it for the desired type
 * of transaction control.
 * 
 * @see .Fgame
 * @author MyEclipse Persistence Tools
 */
@Repository
public class FgameDAO extends HibernateDaoSupport {
	private static final Logger log = LoggerFactory.getLogger(FgameDAO.class);
	// property constants
	public static final String FNAME = "fname";
	public static final String FDESCRIPTION = "fdescription";
	public static final String FNOTICE = "fnotice";
	public static final String FMUSIC_URL = "fmusicUrl";
	public static final String FMIN_HARVEST_TIME = "fminHarvestTime";

	public void save(Fgame transientInstance) {
		log.debug("saving Fgame instance");
		try {
			getSession().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(Fgame persistentInstance) {
		log.debug("deleting Fgame instance");
		try {
			getSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public Fgame findById(java.lang.Integer id) {
		log.debug("getting Fgame instance with id: " + id);
		try {
			Fgame instance = (Fgame) getSession().get("com.ruizton.main.model.Fgame", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List<Fgame> findByExample(Fgame instance) {
		log.debug("finding Fgame instance by example");
		try {
			List<Fgame> results = (List<Fgame>) getSession()
					.createCriteria("com.ruizton.main.model.Fgame").add(create(instance)).list();
			log.debug("find by example successful, result size: "
					+ results.size());
			return results;
		} catch (RuntimeException re) {
			log.error("find by example failed", re);
			throw re;
		}
	}

	public List findByProperty(String propertyName, Object value) {
		log.debug("finding Fgame instance with property: " + propertyName
				+ ", value: " + value);
		try {
			String queryString = "from Fgame as model where model."
					+ propertyName + "= ?";
			Query queryObject = getSession().createQuery(queryString);
			queryObject.setParameter(0, value);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List<Fgame> findByFname(Object fname) {
		return findByProperty(FNAME, fname);
	}

	public List<Fgame> findByFdescription(Object fdescription) {
		return findByProperty(FDESCRIPTION, fdescription);
	}

	public List<Fgame> findByFnotice(Object fnotice) {
		return findByProperty(FNOTICE, fnotice);
	}

	public List<Fgame> findByFmusicUrl(Object fmusicUrl) {
		return findByProperty(FMUSIC_URL, fmusicUrl);
	}

	public List<Fgame> findByFminHarvestTime(Object fminHarvestTime) {
		return findByProperty(FMIN_HARVEST_TIME, fminHarvestTime);
	}

	public List findAll() {
		log.debug("finding all Fgame instances");
		try {
			String queryString = "from Fgame";
			Query queryObject = getSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public Fgame merge(Fgame detachedInstance) {
		log.debug("merging Fgame instance");
		try {
			Fgame result = (Fgame) getSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(Fgame instance) {
		log.debug("attaching dirty Fgame instance");
		try {
			getSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(Fgame instance) {
		log.debug("attaching clean Fgame instance");
		try {
			getSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}
	
	public List<Fgame> list(int firstResult, int maxResults, String filter,boolean isFY) {
		List<Fgame> list = null;
		log.debug("finding Fgame instance with filter");
		try {
			String queryString = "from Fgame "+filter;
			Query queryObject = getSession().createQuery(queryString);
			queryObject.setCacheable(true);
			if(isFY){
				queryObject.setFirstResult(firstResult);
				queryObject.setMaxResults(maxResults);
			}
			list = queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by Fgame name failed", re);
			throw re;
		}
		return list;
	}
}