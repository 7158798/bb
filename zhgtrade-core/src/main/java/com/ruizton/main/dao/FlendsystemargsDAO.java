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
import com.ruizton.main.model.Flendsystemargs;
import com.ruizton.main.model.Flog;

/**
 * A data access object (DAO) providing persistence and search support for
 * Flendsystemargs entities. Transaction control of the save(), update() and
 * delete() operations can directly support Spring container-managed
 * transactions or they can be augmented to handle user-managed Spring
 * transactions. Each of these methods provides additional information for how
 * to configure it for the desired type of transaction control.
 * 
 * @see .Flendsystemargs
 * @author MyEclipse Persistence Tools
 */
@Repository
public class FlendsystemargsDAO extends HibernateDaoSupport {
	private static final Logger log = LoggerFactory
			.getLogger(FlendsystemargsDAO.class);
	// property constants
	public static final String FKEY = "fkey";
	public static final String FVALUE = "fvalue";
	public static final String FDESC = "fdesc";

	public void save(Flendsystemargs transientInstance) {
		log.debug("saving Flendsystemargs instance");
		try {
			getSession().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(Flendsystemargs persistentInstance) {
		log.debug("deleting Flendsystemargs instance");
		try {
			getSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public Flendsystemargs findById(java.lang.Integer id) {
		log.debug("getting Flendsystemargs instance with id: " + id);
		try {
			Flendsystemargs instance = (Flendsystemargs) getSession().get(
					"com.ruizton.main.model.Flendsystemargs", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List<Flendsystemargs> findByExample(Flendsystemargs instance) {
		log.debug("finding Flendsystemargs instance by example");
		try {
			List<Flendsystemargs> results = (List<Flendsystemargs>) getSession()
					.createCriteria("com.ruizton.main.model.Flendsystemargs").add(create(instance))
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
		log.debug("finding Flendsystemargs instance with property: "
				+ propertyName + ", value: " + value);
		try {
			String queryString = "from Flendsystemargs as model where model."
					+ propertyName + "= ?";
			Query queryObject = getSession().createQuery(queryString);
			queryObject.setParameter(0, value);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List<Flendsystemargs> findByFkey(Object fkey) {
		return findByProperty(FKEY, fkey);
	}

	public List<Flendsystemargs> findByFvalue(Object fvalue) {
		return findByProperty(FVALUE, fvalue);
	}

	public List<Flendsystemargs> findByFdesc(Object fdesc) {
		return findByProperty(FDESC, fdesc);
	}

	public List findAll() {
		log.debug("finding all Flendsystemargs instances");
		try {
			String queryString = "from Flendsystemargs";
			Query queryObject = getSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public Flendsystemargs merge(Flendsystemargs detachedInstance) {
		log.debug("merging Flendsystemargs instance");
		try {
			Flendsystemargs result = (Flendsystemargs) getSession().merge(
					detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(Flendsystemargs instance) {
		log.debug("attaching dirty Flendsystemargs instance");
		try {
			getSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(Flendsystemargs instance) {
		log.debug("attaching clean Flendsystemargs instance");
		try {
			getSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}
	
	public List<Flendsystemargs> list(int firstResult, int maxResults, String filter,boolean isFY) {
		List<Flendsystemargs> list = null;
		log.debug("finding Flendsystemargs instance with filter");
		try {
			String queryString = "from Flendsystemargs "+filter;
			Query queryObject = getSession().createQuery(queryString);
			if(isFY){
				queryObject.setFirstResult(firstResult);
				queryObject.setMaxResults(maxResults);
			}
			list = queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by Flendsystemargs name failed", re);
			throw re;
		}
		return list;
	}
}