package com.ruizton.main.dao;

import static org.hibernate.criterion.Example.create;

import java.util.List;

import org.hibernate.LockMode;
import org.hibernate.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.ruizton.main.dao.comm.HibernateDaoSupport;
import com.ruizton.main.model.Fproxy;

/**
 * A data access object (DAO) providing persistence and search support for
 * Fproxy entities. Transaction control of the save(), update() and delete()
 * operations can directly support Spring container-managed transactions or they
 * can be augmented to handle user-managed Spring transactions. Each of these
 * methods provides additional information for how to configure it for the
 * desired type of transaction control.
 * 
 * @see com.ruizton.main.model.Fproxy
 * @author MyEclipse Persistence Tools
 */

@Repository
public class FproxyDAO extends HibernateDaoSupport {
	private static final Logger log = LoggerFactory.getLogger(FproxyDAO.class);
	// property constants
	public static final String NAME = "name";
	public static final String AMOUNT = "amount";
	public static final String QQ = "qq";
	public static final String REALNAME = "realname";
	public static final String ACCOUNT = "account";

	public void save(Fproxy transientInstance) {
		log.debug("saving Fproxy instance");
		try {
			getSession().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(Fproxy persistentInstance) {
		log.debug("deleting Fproxy instance");
		try {
			getSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public Fproxy findById(java.lang.Integer id) {
		log.debug("getting Fproxy instance with id: " + id);
		try {
			Fproxy instance = (Fproxy) getSession().get("com.ruizton.main.model.Fproxy", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List<Fproxy> findByExample(Fproxy instance) {
		log.debug("finding Fproxy instance by example");
		try {
			List<Fproxy> results = (List<Fproxy>) getSession()
					.createCriteria("com.ruizton.main.model.Fproxy").add(create(instance)).list();
			log.debug("find by example successful, result size: "
					+ results.size());
			return results;
		} catch (RuntimeException re) {
			log.error("find by example failed", re);
			throw re;
		}
	}

	public List findByProperty(String propertyName, Object value) {
		log.debug("finding Fproxy instance with property: " + propertyName
				+ ", value: " + value);
		try {
			String queryString = "from Fproxy as model where model."
					+ propertyName + "= ?";
			Query queryObject = getSession().createQuery(queryString);
			queryObject.setParameter(0, value);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List<Fproxy> findByName(Object name) {
		return findByProperty(NAME, name);
	}

	public List<Fproxy> findByAmount(Object amount) {
		return findByProperty(AMOUNT, amount);
	}

	public List<Fproxy> findByQq(Object qq) {
		return findByProperty(QQ, qq);
	}

	public List<Fproxy> findByRealname(Object realname) {
		return findByProperty(REALNAME, realname);
	}

	public List<Fproxy> findByAccount(Object account) {
		return findByProperty(ACCOUNT, account);
	}

	public List findAll() {
		log.debug("finding all Fproxy instances");
		try {
			String queryString = "from Fproxy";
			Query queryObject = getSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public Fproxy merge(Fproxy detachedInstance) {
		log.debug("merging Fproxy instance");
		try {
			Fproxy result = (Fproxy) getSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(Fproxy instance) {
		log.debug("attaching dirty Fproxy instance");
		try {
			getSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(Fproxy instance) {
		log.debug("attaching clean Fproxy instance");
		try {
			getSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}
}