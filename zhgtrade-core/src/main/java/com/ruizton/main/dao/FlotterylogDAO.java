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
import com.ruizton.main.model.Flotterylog;

/**
 * A data access object (DAO) providing persistence and search support for
 * Flotterylog entities. Transaction control of the save(), update() and
 * delete() operations can directly support Spring container-managed
 * transactions or they can be augmented to handle user-managed Spring
 * transactions. Each of these methods provides additional information for how
 * to configure it for the desired type of transaction control.
 * 
 * @see .Flotterylog
 * @author MyEclipse Persistence Tools
 */
@Repository
public class FlotterylogDAO extends HibernateDaoSupport {
	private static final Logger log = LoggerFactory
			.getLogger(FlotterylogDAO.class);
	// property constants
	public static final String FTITLE = "ftitle";

	public void save(Flotterylog transientInstance) {
		log.debug("saving Flotterylog instance");
		try {
			getSession().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(Flotterylog persistentInstance) {
		log.debug("deleting Flotterylog instance");
		try {
			getSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public Flotterylog findById(java.lang.Integer id) {
		log.debug("getting Flotterylog instance with id: " + id);
		try {
			Flotterylog instance = (Flotterylog) getSession().get(
					"com.ruizton.main.model.Flotterylog", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List<Flotterylog> findByExample(Flotterylog instance) {
		log.debug("finding Flotterylog instance by example");
		try {
			List<Flotterylog> results = (List<Flotterylog>) getSession()
					.createCriteria("com.ruizton.main.model.Flotterylog")
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
		log.debug("finding Flotterylog instance with property: " + propertyName
				+ ", value: " + value);
		try {
			String queryString = "from Flotterylog as model where model."
					+ propertyName + "= ?";
			Query queryObject = getSession().createQuery(queryString);
			queryObject.setParameter(0, value);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List<Flotterylog> findByFtitle(Object ftitle) {
		return findByProperty(FTITLE, ftitle);
	}

	public List findAll() {
		log.debug("finding all Flotterylog instances");
		try {
			String queryString = "from Flotterylog";
			Query queryObject = getSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public Flotterylog merge(Flotterylog detachedInstance) {
		log.debug("merging Flotterylog instance");
		try {
			Flotterylog result = (Flotterylog) getSession().merge(
					detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(Flotterylog instance) {
		log.debug("attaching dirty Flotterylog instance");
		try {
			getSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(Flotterylog instance) {
		log.debug("attaching clean Flotterylog instance");
		try {
			getSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}
	
	public List<Flotterylog> list(int firstResult, int maxResults, String filter,boolean isFY) {
		List<Flotterylog> list = null;
		log.debug("finding Flotterylog instance with filter");
		try {
			String queryString = "from Flotterylog "+filter;
			Query queryObject = getSession().createQuery(queryString);
			if(isFY){
				queryObject.setFirstResult(firstResult);
				queryObject.setMaxResults(maxResults);
			}
			list = queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by Flotterylog name failed", re);
			throw re;
		}
		return list;
	}
	
	
}