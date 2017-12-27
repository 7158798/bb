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
import com.ruizton.main.model.Fhedginguserlog;

/**
 * A data access object (DAO) providing persistence and search support for
 * Fhedginguserlog entities. Transaction control of the save(), update() and
 * delete() operations can directly support Spring container-managed
 * transactions or they can be augmented to handle user-managed Spring
 * transactions. Each of these methods provides additional information for how
 * to configure it for the desired type of transaction control.
 * 
 * @see .Fhedginguserlog
 * @author MyEclipse Persistence Tools
 */
@Repository
public class FhedginguserlogDAO extends HibernateDaoSupport {
	private static final Logger log = LoggerFactory
			.getLogger(FhedginguserlogDAO.class);
	// property constants
	public static final String VERSION = "version";
	public static final String FQTY = "fqty";
	public static final String FSTATUS = "fstatus";
	public static final String FTAKETYPE = "ftaketype";
	public static final String FWINQTY = "fwinqty";

	public void save(Fhedginguserlog transientInstance) {
		log.debug("saving Fhedginguserlog instance");
		try {
			getSession().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(Fhedginguserlog persistentInstance) {
		log.debug("deleting Fhedginguserlog instance");
		try {
			getSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public Fhedginguserlog findById(java.lang.Integer id) {
		log.debug("getting Fhedginguserlog instance with id: " + id);
		try {
			Fhedginguserlog instance = (Fhedginguserlog) getSession().get(
					"com.ruizton.main.model.Fhedginguserlog", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List<Fhedginguserlog> findByExample(Fhedginguserlog instance) {
		log.debug("finding Fhedginguserlog instance by example");
		try {
			List<Fhedginguserlog> results = (List<Fhedginguserlog>) getSession()
					.createCriteria("com.ruizton.main.model.Fhedginguserlog").add(create(instance))
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
		log.debug("finding Fhedginguserlog instance with property: "
				+ propertyName + ", value: " + value);
		try {
			String queryString = "from Fhedginguserlog as model where model."
					+ propertyName + "= ?";
			Query queryObject = getSession().createQuery(queryString);
			queryObject.setParameter(0, value);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List<Fhedginguserlog> findByVersion(Object version) {
		return findByProperty(VERSION, version);
	}

	public List<Fhedginguserlog> findByFqty(Object fqty) {
		return findByProperty(FQTY, fqty);
	}

	public List<Fhedginguserlog> findByFstatus(Object fstatus) {
		return findByProperty(FSTATUS, fstatus);
	}

	public List<Fhedginguserlog> findByFtaketype(Object ftaketype) {
		return findByProperty(FTAKETYPE, ftaketype);
	}

	public List<Fhedginguserlog> findByFwinqty(Object fwinqty) {
		return findByProperty(FWINQTY, fwinqty);
	}

	public List findAll() {
		log.debug("finding all Fhedginguserlog instances");
		try {
			String queryString = "from Fhedginguserlog";
			Query queryObject = getSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public Fhedginguserlog merge(Fhedginguserlog detachedInstance) {
		log.debug("merging Fhedginguserlog instance");
		try {
			Fhedginguserlog result = (Fhedginguserlog) getSession().merge(
					detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(Fhedginguserlog instance) {
		log.debug("attaching dirty Fhedginguserlog instance");
		try {
			getSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(Fhedginguserlog instance) {
		log.debug("attaching clean Fhedginguserlog instance");
		try {
			getSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}
	
	public List<Fhedginguserlog> list(int firstResult, int maxResults, String filter,boolean isFY) {
		List<Fhedginguserlog> list = null;
		log.debug("finding Fhedginguserlog instance with filter");
		try {
			String queryString = "from Fhedginguserlog "+filter;
			Query queryObject = getSession().createQuery(queryString);
			queryObject.setCacheable(true);
			if(isFY){
				queryObject.setFirstResult(firstResult);
				queryObject.setMaxResults(maxResults);
			}
			list = queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by Fhedginguserlog name failed", re);
			throw re;
		}
		return list;
	}
}