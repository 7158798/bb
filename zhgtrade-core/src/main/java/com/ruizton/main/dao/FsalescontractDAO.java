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
import com.ruizton.main.model.Fsalescontract;

/**
 * A data access object (DAO) providing persistence and search support for
 * Fsalescontract entities. Transaction control of the save(), update() and
 * delete() operations can directly support Spring container-managed
 * transactions or they can be augmented to handle user-managed Spring
 * transactions. Each of these methods provides additional information for how
 * to configure it for the desired type of transaction control.
 * 
 * @see .Fsalescontract
 * @author MyEclipse Persistence Tools
 */
@Repository
public class FsalescontractDAO extends HibernateDaoSupport {
	private static final Logger log = LoggerFactory
			.getLogger(FsalescontractDAO.class);
	// property constants
	public static final String FLEVEL = "flevel";
	public static final String FCASH_QTY = "fcashQty";
	public static final String FSTATUS = "fstatus";

	public void save(Fsalescontract transientInstance) {
		log.debug("saving Fsalescontract instance");
		try {
			getSession().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(Fsalescontract persistentInstance) {
		log.debug("deleting Fsalescontract instance");
		try {
			getSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public Fsalescontract findById(java.lang.Integer id) {
		log.debug("getting Fsalescontract instance with id: " + id);
		try {
			Fsalescontract instance = (Fsalescontract) getSession().get(
					"com.ruizton.main.model.Fsalescontract", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List<Fsalescontract> findByExample(Fsalescontract instance) {
		log.debug("finding Fsalescontract instance by example");
		try {
			List<Fsalescontract> results = (List<Fsalescontract>) getSession()
					.createCriteria("com.ruizton.main.model.Fsalescontract")
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
		log.debug("finding Fsalescontract instance with property: "
				+ propertyName + ", value: " + value);
		try {
			String queryString = "from Fsalescontract as model where model."
					+ propertyName + "= ?";
			Query queryObject = getSession().createQuery(queryString);
			queryObject.setParameter(0, value);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List<Fsalescontract> findByFlevel(Object flevel) {
		return findByProperty(FLEVEL, flevel);
	}

	public List<Fsalescontract> findByFcashQty(Object fcashQty) {
		return findByProperty(FCASH_QTY, fcashQty);
	}

	public List<Fsalescontract> findByFstatus(Object fstatus) {
		return findByProperty(FSTATUS, fstatus);
	}

	public List findAll() {
		log.debug("finding all Fsalescontract instances");
		try {
			String queryString = "from Fsalescontract";
			Query queryObject = getSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public Fsalescontract merge(Fsalescontract detachedInstance) {
		log.debug("merging Fsalescontract instance");
		try {
			Fsalescontract result = (Fsalescontract) getSession().merge(
					detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(Fsalescontract instance) {
		log.debug("attaching dirty Fsalescontract instance");
		try {
			getSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(Fsalescontract instance) {
		log.debug("attaching clean Fsalescontract instance");
		try {
			getSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}
	
	public List<Fsalescontract> list(int firstResult, int maxResults, String filter,boolean isFY) {
		List<Fsalescontract> list = null;
		log.debug("finding Fsalescontract instance with filter");
		try {
			String queryString = "from Fsalescontract "+filter;
			Query queryObject = getSession().createQuery(queryString);
			if(isFY){
				queryObject.setFirstResult(firstResult);
				queryObject.setMaxResults(maxResults);
			}
			list = queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by Fsalescontract name failed", re);
			throw re;
		}
		return list;
	}
}