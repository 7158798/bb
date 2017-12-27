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
import com.ruizton.main.model.Fabout;
import com.ruizton.main.model.Fchancelog;

/**
 * A data access object (DAO) providing persistence and search support for
 * Fchancelog entities. Transaction control of the save(), update() and delete()
 * operations can directly support Spring container-managed transactions or they
 * can be augmented to handle user-managed Spring transactions. Each of these
 * methods provides additional information for how to configure it for the
 * desired type of transaction control.
 * 
 * @see .Fchancelog
 * @author MyEclipse Persistence Tools
 */
@Repository
public class FchancelogDAO extends HibernateDaoSupport {
	private static final Logger log = LoggerFactory
			.getLogger(FchancelogDAO.class);
	// property constants
	public static final String FQTY1 = "fqty1";
	public static final String FQTY2 = "fqty2";
	public static final String FTYPE = "ftype";

	public void save(Fchancelog transientInstance) {
		log.debug("saving Fchancelog instance");
		try {
			getSession().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(Fchancelog persistentInstance) {
		log.debug("deleting Fchancelog instance");
		try {
			getSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public Fchancelog findById(java.lang.Integer id) {
		log.debug("getting Fchancelog instance with id: " + id);
		try {
			Fchancelog instance = (Fchancelog) getSession().get("com.ruizton.main.model.Fchancelog",
					id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List<Fchancelog> findByExample(Fchancelog instance) {
		log.debug("finding Fchancelog instance by example");
		try {
			List<Fchancelog> results = (List<Fchancelog>) getSession()
					.createCriteria("com.ruizton.main.model.Fchancelog").add(create(instance)).list();
			log.debug("find by example successful, result size: "
					+ results.size());
			return results;
		} catch (RuntimeException re) {
			log.error("find by example failed", re);
			throw re;
		}
	}

	public List findByProperty(String propertyName, Object value) {
		log.debug("finding Fchancelog instance with property: " + propertyName
				+ ", value: " + value);
		try {
			String queryString = "from Fchancelog as model where model."
					+ propertyName + "= ?";
			Query queryObject = getSession().createQuery(queryString);
			queryObject.setParameter(0, value);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List<Fchancelog> findByFqty1(Object fqty1) {
		return findByProperty(FQTY1, fqty1);
	}

	public List<Fchancelog> findByFqty2(Object fqty2) {
		return findByProperty(FQTY2, fqty2);
	}

	public List<Fchancelog> findByFtype(Object ftype) {
		return findByProperty(FTYPE, ftype);
	}

	public List findAll() {
		log.debug("finding all Fchancelog instances");
		try {
			String queryString = "from Fchancelog";
			Query queryObject = getSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public Fchancelog merge(Fchancelog detachedInstance) {
		log.debug("merging Fchancelog instance");
		try {
			Fchancelog result = (Fchancelog) getSession().merge(
					detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(Fchancelog instance) {
		log.debug("attaching dirty Fchancelog instance");
		try {
			getSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(Fchancelog instance) {
		log.debug("attaching clean Fchancelog instance");
		try {
			getSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}
	
	public List<Fchancelog> list(int firstResult, int maxResults, String filter,boolean isFY) {
		List<Fchancelog> list = null;
		log.debug("finding Fchancelog instance with filter");
		try {
			String queryString = "from Fchancelog "+filter;
			Query queryObject = getSession().createQuery(queryString);
			if(isFY){
				queryObject.setFirstResult(firstResult);
				queryObject.setMaxResults(maxResults);
			}
			list = queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by Fchancelog name failed", re);
			throw re;
		}
		return list;
	}
}