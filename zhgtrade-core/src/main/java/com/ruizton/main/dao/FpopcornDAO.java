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
import com.ruizton.main.model.Fpopcorn;
import com.ruizton.main.model.Fuser;

/**
 * A data access object (DAO) providing persistence and search support for
 * Fpopcorn entities. Transaction control of the save(), update() and delete()
 * operations can directly support Spring container-managed transactions or they
 * can be augmented to handle user-managed Spring transactions. Each of these
 * methods provides additional information for how to configure it for the
 * desired type of transaction control.
 * 
 * @see .Fpopcorn
 * @author MyEclipse Persistence Tools
 */
@Repository
public class FpopcornDAO extends HibernateDaoSupport {
	private static final Logger log = LoggerFactory
			.getLogger(FpopcornDAO.class);
	// property constants
	public static final String FTITLE = "ftitle";
	public static final String FTIME = "ftime";

	public void save(Fpopcorn transientInstance) {
		log.debug("saving Fpopcorn instance");
		try {
			getSession().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(Fpopcorn persistentInstance) {
		log.debug("deleting Fpopcorn instance");
		try {
			getSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public Fpopcorn findById(java.lang.Integer id) {
		log.debug("getting Fpopcorn instance with id: " + id);
		try {
			Fpopcorn instance = (Fpopcorn) getSession().get("com.ruizton.main.model.Fpopcorn", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List<Fpopcorn> findByExample(Fpopcorn instance) {
		log.debug("finding Fpopcorn instance by example");
		try {
			List<Fpopcorn> results = (List<Fpopcorn>) getSession()
					.createCriteria("com.ruizton.main.model.Fpopcorn").add(create(instance)).list();
			log.debug("find by example successful, result size: "
					+ results.size());
			return results;
		} catch (RuntimeException re) {
			log.error("find by example failed", re);
			throw re;
		}
	}

	public List findByProperty(String propertyName, Object value) {
		log.debug("finding Fpopcorn instance with property: " + propertyName
				+ ", value: " + value);
		try {
			String queryString = "from Fpopcorn as model where model."
					+ propertyName + "= ?";
			Query queryObject = getSession().createQuery(queryString);
			queryObject.setParameter(0, value);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List<Fpopcorn> findByFtitle(Object ftitle) {
		return findByProperty(FTITLE, ftitle);
	}

	public List<Fpopcorn> findByFtime(Object ftime) {
		return findByProperty(FTIME, ftime);
	}

	public List findAll() {
		log.debug("finding all Fpopcorn instances");
		try {
			String queryString = "from Fpopcorn";
			Query queryObject = getSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public Fpopcorn merge(Fpopcorn detachedInstance) {
		log.debug("merging Fpopcorn instance");
		try {
			Fpopcorn result = (Fpopcorn) getSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(Fpopcorn instance) {
		log.debug("attaching dirty Fpopcorn instance");
		try {
			getSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(Fpopcorn instance) {
		log.debug("attaching clean Fpopcorn instance");
		try {
			getSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}
	
	public List<Fpopcorn> list(int firstResult, int maxResults,
			String filter,boolean isFY) {
		List<Fpopcorn> list = null;
		log.debug("finding Fpopcorn instance with filter");
		try {
			String queryString = "from Fpopcorn "+filter;
			Query queryObject = getSession().createQuery(queryString);
			if(isFY){
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