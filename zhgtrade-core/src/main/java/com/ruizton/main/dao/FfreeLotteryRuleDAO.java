package com.ruizton.main.dao;

import static org.hibernate.criterion.Example.create;

import java.util.List;

import org.hibernate.LockMode;
import org.hibernate.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.ruizton.main.dao.comm.HibernateDaoSupport;
import com.ruizton.main.model.FfreeLotteryRule;

/**
 * A data access object (DAO) providing persistence and search support for
 * FfreeLotteryRule entities. Transaction control of the save(), update() and
 * delete() operations can directly support Spring container-managed
 * transactions or they can be augmented to handle user-managed Spring
 * transactions. Each of these methods provides additional information for how
 * to configure it for the desired type of transaction control.
 * 
 * @see com.ruizton.main.model.FfreeLotteryRule
 * @author MyEclipse Persistence Tools
 */

@Repository
public class FfreeLotteryRuleDAO extends HibernateDaoSupport {
	private static final Logger log = LoggerFactory
			.getLogger(FfreeLotteryRuleDAO.class);
	// property constants
	public static final String FNAME = "fname";
	public static final String FFROM = "ffrom";
	public static final String FTO = "fto";
	public static final String FREWARD = "freward";

	public void save(FfreeLotteryRule transientInstance) {
		log.debug("saving FfreeLotteryRule instance");
		try {
			getSession().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(FfreeLotteryRule persistentInstance) {
		log.debug("deleting FfreeLotteryRule instance");
		try {
			getSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public FfreeLotteryRule findById(java.lang.Integer id) {
		log.debug("getting FfreeLotteryRule instance with id: " + id);
		try {
			FfreeLotteryRule instance = (FfreeLotteryRule) getSession().get(
					"com.ruizton.main.model.FfreeLotteryRule", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List<FfreeLotteryRule> findByExample(FfreeLotteryRule instance) {
		log.debug("finding FfreeLotteryRule instance by example");
		try {
			List<FfreeLotteryRule> results = (List<FfreeLotteryRule>) getSession()
					.createCriteria("com.ruizton.main.model.FfreeLotteryRule")
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
		log.debug("finding FfreeLotteryRule instance with property: "
				+ propertyName + ", value: " + value);
		try {
			String queryString = "from FfreeLotteryRule as model where model."
					+ propertyName + "= ?";
			Query queryObject = getSession().createQuery(queryString);
			queryObject.setParameter(0, value);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List<FfreeLotteryRule> findByFname(Object fname) {
		return findByProperty(FNAME, fname);
	}

	public List<FfreeLotteryRule> findByFfrom(Object ffrom) {
		return findByProperty(FFROM, ffrom);
	}

	public List<FfreeLotteryRule> findByFto(Object fto) {
		return findByProperty(FTO, fto);
	}

	public List<FfreeLotteryRule> findByFreward(Object freward) {
		return findByProperty(FREWARD, freward);
	}

	public List findAll() {
		log.debug("finding all FfreeLotteryRule instances");
		try {
			String queryString = "from FfreeLotteryRule";
			Query queryObject = getSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public FfreeLotteryRule merge(FfreeLotteryRule detachedInstance) {
		log.debug("merging FfreeLotteryRule instance");
		try {
			FfreeLotteryRule result = (FfreeLotteryRule) getSession().merge(
					detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(FfreeLotteryRule instance) {
		log.debug("attaching dirty FfreeLotteryRule instance");
		try {
			getSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(FfreeLotteryRule instance) {
		log.debug("attaching clean FfreeLotteryRule instance");
		try {
			getSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}
}