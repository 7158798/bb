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
import com.ruizton.main.model.Fgamelog;

/**
 * A data access object (DAO) providing persistence and search support for
 * Fgamelog entities. Transaction control of the save(), update() and delete()
 * operations can directly support Spring container-managed transactions or they
 * can be augmented to handle user-managed Spring transactions. Each of these
 * methods provides additional information for how to configure it for the
 * desired type of transaction control.
 * 
 * @see .Fgamelog
 * @author MyEclipse Persistence Tools
 */
@Repository
public class FgamelogDAO extends HibernateDaoSupport {
	private static final Logger log = LoggerFactory
			.getLogger(FgamelogDAO.class);
	// property constants
	public static final String FPLAN_HARVEST_QTY = "fplanHarvestQty";
	public static final String FACTUAL_HARVEST_QTY = "factualHarvestQty";

	public void save(Fgamelog transientInstance) {
		log.debug("saving Fgamelog instance");
		try {
			getSession().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(Fgamelog persistentInstance) {
		log.debug("deleting Fgamelog instance");
		try {
			getSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public Fgamelog findById(java.lang.Integer id) {
		log.debug("getting Fgamelog instance with id: " + id);
		try {
			Fgamelog instance = (Fgamelog) getSession().get("com.ruizton.main.model.Fgamelog", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List<Fgamelog> findByExample(Fgamelog instance) {
		log.debug("finding Fgamelog instance by example");
		try {
			List<Fgamelog> results = (List<Fgamelog>) getSession()
					.createCriteria("com.ruizton.main.model.Fgamelog").add(create(instance)).list();
			log.debug("find by example successful, result size: "
					+ results.size());
			return results;
		} catch (RuntimeException re) {
			log.error("find by example failed", re);
			throw re;
		}
	}

	public List findByProperty(String propertyName, Object value) {
		log.debug("finding Fgamelog instance with property: " + propertyName
				+ ", value: " + value);
		try {
			String queryString = "from Fgamelog as model where model."
					+ propertyName + "= ?";
			Query queryObject = getSession().createQuery(queryString);
			queryObject.setParameter(0, value);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List<Fgamelog> findByFplanHarvestQty(Object fplanHarvestQty) {
		return findByProperty(FPLAN_HARVEST_QTY, fplanHarvestQty);
	}

	public List<Fgamelog> findByFactualHarvestQty(Object factualHarvestQty) {
		return findByProperty(FACTUAL_HARVEST_QTY, factualHarvestQty);
	}

	public List findAll() {
		log.debug("finding all Fgamelog instances");
		try {
			String queryString = "from Fgamelog";
			Query queryObject = getSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public Fgamelog merge(Fgamelog detachedInstance) {
		log.debug("merging Fgamelog instance");
		try {
			Fgamelog result = (Fgamelog) getSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(Fgamelog instance) {
		log.debug("attaching dirty Fgamelog instance");
		try {
			getSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(Fgamelog instance) {
		log.debug("attaching clean Fgamelog instance");
		try {
			getSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}
	
	public List<Fgamelog> list(int firstResult, int maxResults, String filter,boolean isFY) {
		List<Fgamelog> list = null;
		log.debug("finding Fgamelog instance with filter");
		try {
			String queryString = "from Fgamelog "+filter;
			Query queryObject = getSession().createQuery(queryString);
			queryObject.setCacheable(true);
			if(isFY){
				queryObject.setFirstResult(firstResult);
				queryObject.setMaxResults(maxResults);
			}
			list = queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by Fgamelog name failed", re);
			throw re;
		}
		return list;
	}
}