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
import com.ruizton.main.model.Ftotalreport;
import com.ruizton.main.model.Fuser;

/**
 * A data access object (DAO) providing persistence and search support for
 * Ftotalreport entities. Transaction control of the save(), update() and
 * delete() operations can directly support Spring container-managed
 * transactions or they can be augmented to handle user-managed Spring
 * transactions. Each of these methods provides additional information for how
 * to configure it for the desired type of transaction control.
 * 
 * @see .Ftotalreport
 * @author MyEclipse Persistence Tools
 */
@Repository
public class FtotalreportDAO extends HibernateDaoSupport {
	private static final Logger log = LoggerFactory
			.getLogger(FtotalreportDAO.class);
	// property constants
	public static final String FTOTAL_AMT = "ftotalAmt";
	public static final String FTOTAL_CAN_AMT = "ftotalCanAmt";
	public static final String FTOTAL_FROZEN_AMT = "ftotalFrozenAmt";
	public static final String FTOTAL_RECHARGE_AMT = "ftotalRechargeAmt";
	public static final String FTOTAL_WITHDRAW_AMT = "ftotalWithdrawAmt";
	public static final String FTOTAL_DOU = "ftotalDou";
	public static final String FTOTAL_CAN_DOU = "ftotalCanDou";
	public static final String FTOTAL_FROZEN_DOU = "ftotalFrozenDou";
	public static final String FTOTAL_CREATE_DOU = "ftotalCreateDou";

	public void save(Ftotalreport transientInstance) {
		log.debug("saving Ftotalreport instance");
		try {
			getSession().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(Ftotalreport persistentInstance) {
		log.debug("deleting Ftotalreport instance");
		try {
			getSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public Ftotalreport findById(java.lang.Integer id) {
		log.debug("getting Ftotalreport instance with id: " + id);
		try {
			Ftotalreport instance = (Ftotalreport) getSession().get(
					"com.ruizton.main.model.Ftotalreport", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List<Ftotalreport> findByExample(Ftotalreport instance) {
		log.debug("finding Ftotalreport instance by example");
		try {
			List<Ftotalreport> results = (List<Ftotalreport>) getSession()
					.createCriteria("com.ruizton.main.model.Ftotalreport")
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
		log.debug("finding Ftotalreport instance with property: "
				+ propertyName + ", value: " + value);
		try {
			String queryString = "from Ftotalreport as model where model."
					+ propertyName + "= ?";
			Query queryObject = getSession().createQuery(queryString);
			queryObject.setParameter(0, value);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List<Ftotalreport> findByFtotalAmt(Object ftotalAmt) {
		return findByProperty(FTOTAL_AMT, ftotalAmt);
	}

	public List<Ftotalreport> findByFtotalCanAmt(Object ftotalCanAmt) {
		return findByProperty(FTOTAL_CAN_AMT, ftotalCanAmt);
	}

	public List<Ftotalreport> findByFtotalFrozenAmt(Object ftotalFrozenAmt) {
		return findByProperty(FTOTAL_FROZEN_AMT, ftotalFrozenAmt);
	}

	public List<Ftotalreport> findByFtotalRechargeAmt(Object ftotalRechargeAmt) {
		return findByProperty(FTOTAL_RECHARGE_AMT, ftotalRechargeAmt);
	}

	public List<Ftotalreport> findByFtotalWithdrawAmt(Object ftotalWithdrawAmt) {
		return findByProperty(FTOTAL_WITHDRAW_AMT, ftotalWithdrawAmt);
	}

	public List<Ftotalreport> findByFtotalDou(Object ftotalDou) {
		return findByProperty(FTOTAL_DOU, ftotalDou);
	}

	public List<Ftotalreport> findByFtotalCanDou(Object ftotalCanDou) {
		return findByProperty(FTOTAL_CAN_DOU, ftotalCanDou);
	}

	public List<Ftotalreport> findByFtotalFrozenDou(Object ftotalFrozenDou) {
		return findByProperty(FTOTAL_FROZEN_DOU, ftotalFrozenDou);
	}

	public List<Ftotalreport> findByFtotalCreateDou(Object ftotalCreateDou) {
		return findByProperty(FTOTAL_CREATE_DOU, ftotalCreateDou);
	}

	public List findAll() {
		log.debug("finding all Ftotalreport instances");
		try {
			String queryString = "from Ftotalreport";
			Query queryObject = getSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public Ftotalreport merge(Ftotalreport detachedInstance) {
		log.debug("merging Ftotalreport instance");
		try {
			Ftotalreport result = (Ftotalreport) getSession().merge(
					detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(Ftotalreport instance) {
		log.debug("attaching dirty Ftotalreport instance");
		try {
			getSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(Ftotalreport instance) {
		log.debug("attaching clean Ftotalreport instance");
		try {
			getSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public List<Ftotalreport> list(int firstResult, int maxResults,
			String filter, boolean isFY) {
		List<Ftotalreport> list = null;
		log.debug("finding Ftotalreport instance with filter");
		try {
			String queryString = "from Ftotalreport " + filter;
			Query queryObject = getSession().createQuery(queryString);
			if (isFY) {
				queryObject.setFirstResult(firstResult);
				queryObject.setMaxResults(maxResults);
			}
			list = queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by filter name failed", re);
			throw re;
		}
		return list;
	}
}