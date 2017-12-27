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
import com.ruizton.main.model.Fentrust;
import com.ruizton.main.model.Fkillhistory;

/**
 * A data access object (DAO) providing persistence and search support for
 * Fkillhistory entities. Transaction control of the save(), update() and
 * delete() operations can directly support Spring container-managed
 * transactions or they can be augmented to handle user-managed Spring
 * transactions. Each of these methods provides additional information for how
 * to configure it for the desired type of transaction control.
 * 
 * @see .Fkillhistory
 * @author MyEclipse Persistence Tools
 */
@Repository
public class FkillhistoryDAO extends HibernateDaoSupport {
	private static final Logger log = LoggerFactory
			.getLogger(FkillhistoryDAO.class);
	// property constants
	public static final String FUSER = "fuser";
	public static final String FTYPE = "ftype";
	public static final String FSTATUS = "fstatus";

	public void save(Fkillhistory transientInstance) {
		log.debug("saving Fkillhistory instance");
		try {
			getSession().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(Fkillhistory persistentInstance) {
		log.debug("deleting Fkillhistory instance");
		try {
			getSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public Fkillhistory findById(java.lang.Integer id) {
		log.debug("getting Fkillhistory instance with id: " + id);
		try {
			Fkillhistory instance = (Fkillhistory) getSession().get(
					"com.ruizton.main.model.Fkillhistory", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List<Fkillhistory> findByExample(Fkillhistory instance) {
		log.debug("finding Fkillhistory instance by example");
		try {
			List<Fkillhistory> results = (List<Fkillhistory>) getSession()
					.createCriteria("com.ruizton.main.model.Fkillhistory")
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
		log.debug("finding Fkillhistory instance with property: "
				+ propertyName + ", value: " + value);
		try {
			String queryString = "from Fkillhistory as model where model."
					+ propertyName + "= ?";
			Query queryObject = getSession().createQuery(queryString);
			queryObject.setParameter(0, value);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List<Fkillhistory> findByFuser(Object fuser) {
		return findByProperty(FUSER, fuser);
	}

	public List<Fkillhistory> findByFtype(Object ftype) {
		return findByProperty(FTYPE, ftype);
	}

	public List<Fkillhistory> findByFstatus(Object fstatus) {
		return findByProperty(FSTATUS, fstatus);
	}

	public List findAll() {
		log.debug("finding all Fkillhistory instances");
		try {
			String queryString = "from Fkillhistory";
			Query queryObject = getSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public Fkillhistory merge(Fkillhistory detachedInstance) {
		log.debug("merging Fkillhistory instance");
		try {
			Fkillhistory result = (Fkillhistory) getSession().merge(
					detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(Fkillhistory instance) {
		log.debug("attaching dirty Fkillhistory instance");
		try {
			getSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(Fkillhistory instance) {
		log.debug("attaching clean Fkillhistory instance");
		try {
			getSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}
	
	public List<Fkillhistory> list(int firstResult, int maxResults,
			String filter, boolean isFY) {
		List<Fkillhistory> list = null;
		log.debug("finding Fkillhistory instance with filter");
		try {
			String queryString = "from Fkillhistory " + filter;
			Query queryObject = getSession().createQuery(queryString);
			if (isFY) {
				queryObject.setFirstResult(firstResult);
				queryObject.setMaxResults(maxResults);
			}
			list = queryObject.list();
		} catch (RuntimeException re) {
			log.error("find Fkillhistory by filter name failed", re);
			throw re;
		}
		return list;
	}
}