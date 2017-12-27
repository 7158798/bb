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
import com.ruizton.main.model.Fchargesection;
import com.ruizton.main.model.Fsalespercent;

/**
 * A data access object (DAO) providing persistence and search support for
 * Fsalespercent entities. Transaction control of the save(), update() and
 * delete() operations can directly support Spring container-managed
 * transactions or they can be augmented to handle user-managed Spring
 * transactions. Each of these methods provides additional information for how
 * to configure it for the desired type of transaction control.
 * 
 * @see .Fsalespercent
 * @author MyEclipse Persistence Tools
 */
@Repository
public class FsalespercentDAO extends HibernateDaoSupport {
	private static final Logger log = LoggerFactory
			.getLogger(FsalespercentDAO.class);
	// property constants
	public static final String FLEVEL = "flevel";
	public static final String FQTY = "fqty";
	public static final String FTOTALPERCENT = "ftotalpercent";
	public static final String FEGGPERCENT = "feggpercent";

	public void save(Fsalespercent transientInstance) {
		log.debug("saving Fsalespercent instance");
		try {
			getSession().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(Fsalespercent persistentInstance) {
		log.debug("deleting Fsalespercent instance");
		try {
			getSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public Fsalespercent findById(java.lang.Integer id) {
		log.debug("getting Fsalespercent instance with id: " + id);
		try {
			Fsalespercent instance = (Fsalespercent) getSession().get(
					"com.ruizton.main.model.Fsalespercent", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List<Fsalespercent> findByExample(Fsalespercent instance) {
		log.debug("finding Fsalespercent instance by example");
		try {
			List<Fsalespercent> results = (List<Fsalespercent>) getSession()
					.createCriteria("com.ruizton.main.model.Fsalespercent")
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
		log.debug("finding Fsalespercent instance with property: "
				+ propertyName + ", value: " + value);
		try {
			String queryString = "from Fsalespercent as model where model."
					+ propertyName + "= ?";
			Query queryObject = getSession().createQuery(queryString);
			queryObject.setParameter(0, value);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List<Fsalespercent> findByFlevel(Object flevel) {
		return findByProperty(FLEVEL, flevel);
	}

	public List<Fsalespercent> findByFqty(Object fqty) {
		return findByProperty(FQTY, fqty);
	}

	public List<Fsalespercent> findByFtotalpercent(Object ftotalpercent) {
		return findByProperty(FTOTALPERCENT, ftotalpercent);
	}

	public List<Fsalespercent> findByFeggpercent(Object feggpercent) {
		return findByProperty(FEGGPERCENT, feggpercent);
	}

	public List findAll() {
		log.debug("finding all Fsalespercent instances");
		try {
			String queryString = "from Fsalespercent";
			Query queryObject = getSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public Fsalespercent merge(Fsalespercent detachedInstance) {
		log.debug("merging Fsalespercent instance");
		try {
			Fsalespercent result = (Fsalespercent) getSession().merge(
					detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(Fsalespercent instance) {
		log.debug("attaching dirty Fsalespercent instance");
		try {
			getSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(Fsalespercent instance) {
		log.debug("attaching clean Fsalespercent instance");
		try {
			getSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}
	
	public List<Fsalespercent> list(int firstResult, int maxResults, String filter,boolean isFY) {
		List<Fsalespercent> list = null;
		log.debug("finding Fsalespercent instance with filter");
		try {
			String queryString = "from Fsalespercent "+filter;
			Query queryObject = getSession().createQuery(queryString);
			if(isFY){
				queryObject.setFirstResult(firstResult);
				queryObject.setMaxResults(maxResults);
			}
			list = queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by Fsalespercent name failed", re);
			throw re;
		}
		return list;
	}
}