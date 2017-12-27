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
import com.ruizton.main.model.Fapplyinfo;
import com.ruizton.main.model.Farticle;

/**
 * A data access object (DAO) providing persistence and search support for
 * Fapplyinfo entities. Transaction control of the save(), update() and delete()
 * operations can directly support Spring container-managed transactions or they
 * can be augmented to handle user-managed Spring transactions. Each of these
 * methods provides additional information for how to configure it for the
 * desired type of transaction control.
 * 
 * @see .Fapplyinfo
 * @author MyEclipse Persistence Tools
 */
@Repository
public class FapplyinfoDAO extends HibernateDaoSupport {
	private static final Logger log = LoggerFactory
			.getLogger(FapplyinfoDAO.class);
	// property constants
	public static final String VERSION = "version";
	public static final String FSTATUS = "fstatus";
	public static final String FREASON = "freason";
	public static final String FGRADE = "fgrade";
	public static final String FREALNAME = "frealname";
	public static final String FPHONE = "fphone";

	public void save(Fapplyinfo transientInstance) {
		log.debug("saving Fapplyinfo instance");
		try {
			getSession().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(Fapplyinfo persistentInstance) {
		log.debug("deleting Fapplyinfo instance");
		try {
			getSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public Fapplyinfo findById(java.lang.Integer id) {
		log.debug("getting Fapplyinfo instance with id: " + id);
		try {
			Fapplyinfo instance = (Fapplyinfo) getSession().get(
					"com.ruizton.main.model.Fapplyinfo", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List<Fapplyinfo> findByExample(Fapplyinfo instance) {
		log.debug("finding Fapplyinfo instance by example");
		try {
			List<Fapplyinfo> results = (List<Fapplyinfo>) getSession()
					.createCriteria("com.ruizton.main.model.Fapplyinfo")
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
		log.debug("finding Fapplyinfo instance with property: " + propertyName
				+ ", value: " + value);
		try {
			String queryString = "from Fapplyinfo as model where model."
					+ propertyName + "= ?";
			Query queryObject = getSession().createQuery(queryString);
			queryObject.setParameter(0, value);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List<Fapplyinfo> findByVersion(Object version) {
		return findByProperty(VERSION, version);
	}

	public List<Fapplyinfo> findByFstatus(Object fstatus) {
		return findByProperty(FSTATUS, fstatus);
	}

	public List<Fapplyinfo> findByFreason(Object freason) {
		return findByProperty(FREASON, freason);
	}

	public List<Fapplyinfo> findByFgrade(Object fgrade) {
		return findByProperty(FGRADE, fgrade);
	}

	public List<Fapplyinfo> findByFrealname(Object frealname) {
		return findByProperty(FREALNAME, frealname);
	}

	public List<Fapplyinfo> findByFphone(Object fphone) {
		return findByProperty(FPHONE, fphone);
	}

	public List findAll() {
		log.debug("finding all Fapplyinfo instances");
		try {
			String queryString = "from Fapplyinfo";
			Query queryObject = getSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public Fapplyinfo merge(Fapplyinfo detachedInstance) {
		log.debug("merging Fapplyinfo instance");
		try {
			Fapplyinfo result = (Fapplyinfo) getSession().merge(
					detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(Fapplyinfo instance) {
		log.debug("attaching dirty Fapplyinfo instance");
		try {
			getSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(Fapplyinfo instance) {
		log.debug("attaching clean Fapplyinfo instance");
		try {
			getSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}
	
	public List<Fapplyinfo> list(int firstResult, int maxResults,
			String filter,boolean isFY) {
		List<Fapplyinfo> list = null;
		log.debug("finding Fapplyinfo instance with filter");
		try {
			String queryString = "from Fapplyinfo "+filter;
			Query queryObject = getSession().createQuery(queryString);
			if(isFY){
				queryObject.setFirstResult(firstResult);
				queryObject.setMaxResults(maxResults);
			}
			list = queryObject.list();
		} catch (RuntimeException re) {
			log.error("find Fapplyinfo by filter name failed", re);
			throw re;
		}
		return list;
	}
}