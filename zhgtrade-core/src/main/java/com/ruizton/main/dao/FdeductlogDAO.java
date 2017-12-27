package com.ruizton.main.dao;

// default package

import static org.hibernate.criterion.Example.create;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.ruizton.main.dao.comm.HibernateDaoSupport;
import com.ruizton.main.model.Fchargesection;
import com.ruizton.main.model.Fdeductlog;

/**
 * A data access object (DAO) providing persistence and search support for
 * Fdeductlog entities. Transaction control of the save(), update() and delete()
 * operations can directly support Spring container-managed transactions or they
 * can be augmented to handle user-managed Spring transactions. Each of these
 * methods provides additional information for how to configure it for the
 * desired type of transaction control.
 * 
 * @see .Fdeductlog
 * @author MyEclipse Persistence Tools
 */
@Repository
public class FdeductlogDAO extends HibernateDaoSupport {
	private static final Logger log = LoggerFactory
			.getLogger(FdeductlogDAO.class);
	// property constants
	public static final String FTYPE = "ftype";
	public static final String FAMT = "famt";
	public static final String FFEES = "ffees";
	public static final String FTAKE_AMT = "ftakeAmt";
	public static final String FIS_MONEY = "fisMoney";
	public static final String FSOURCE_ID = "fsourceId";

	public void save(Fdeductlog transientInstance) {
		log.debug("saving Fdeductlog instance");
		try {
			getSession().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(Fdeductlog persistentInstance) {
		log.debug("deleting Fdeductlog instance");
		try {
			getSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public Fdeductlog findById(java.lang.Integer id) {
		log.debug("getting Fdeductlog instance with id: " + id);
		try {
			Fdeductlog instance = (Fdeductlog) getSession().get(
					"com.ruizton.main.model.Fdeductlog", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List<Fdeductlog> findByExample(Fdeductlog instance) {
		log.debug("finding Fdeductlog instance by example");
		try {
			List<Fdeductlog> results = (List<Fdeductlog>) getSession()
					.createCriteria("com.ruizton.main.model.Fdeductlog")
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
		log.debug("finding Fdeductlog instance with property: " + propertyName
				+ ", value: " + value);
		try {
			String queryString = "from Fdeductlog as model where model."
					+ propertyName + "= ?";
			Query queryObject = getSession().createQuery(queryString);
			queryObject.setParameter(0, value);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List<Fdeductlog> findByFtype(Object ftype) {
		return findByProperty(FTYPE, ftype);
	}

	public List<Fdeductlog> findByFamt(Object famt) {
		return findByProperty(FAMT, famt);
	}

	public List<Fdeductlog> findByFfees(Object ffees) {
		return findByProperty(FFEES, ffees);
	}

	public List<Fdeductlog> findByFtakeAmt(Object ftakeAmt) {
		return findByProperty(FTAKE_AMT, ftakeAmt);
	}

	public List<Fdeductlog> findByFisMoney(Object fisMoney) {
		return findByProperty(FIS_MONEY, fisMoney);
	}

	public List<Fdeductlog> findByFsourceId(Object fsourceId) {
		return findByProperty(FSOURCE_ID, fsourceId);
	}

	public List findAll() {
		log.debug("finding all Fdeductlog instances");
		try {
			String queryString = "from Fdeductlog";
			Query queryObject = getSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public Fdeductlog merge(Fdeductlog detachedInstance) {
		log.debug("merging Fdeductlog instance");
		try {
			Fdeductlog result = (Fdeductlog) getSession().merge(
					detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(Fdeductlog instance) {
		log.debug("attaching dirty Fdeductlog instance");
		try {
			getSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(Fdeductlog instance) {
		log.debug("attaching clean Fdeductlog instance");
		try {
			getSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}
	
	public List<Fdeductlog> list(int firstResult, int maxResults, String filter,boolean isFY) {
		List<Fdeductlog> list = null;
		log.debug("finding Fdeductlog instance with filter");
		try {
			String queryString = "from Fdeductlog "+filter;
			Query queryObject = getSession().createQuery(queryString);
			if(isFY){
				queryObject.setFirstResult(firstResult);
				queryObject.setMaxResults(maxResults);
			}
			list = queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by Fdeductlog name failed", re);
			throw re;
		}
		return list;
	}
	
	public List getTotalDeductlog(String filter) {
		StringBuffer sf = new StringBuffer();
		sf.append("select fuserId \n");
		sf.append(",SUM(case when fisMoney=1 then ftakeAmt ELSE 0 END) takeAmt \n");
		sf.append(",SUM(case when fisMoney=0 then ftakeAmt ELSE 0 END) takeQty \n");
		sf.append("from fdeductlog \n");
		sf.append(filter+" \n");
		sf.append("GROUP BY fuserId \n");
		SQLQuery queryObject = getSession().createSQLQuery(sf.toString());
		return queryObject.list();
	}
	
	public void updateDeductlog(String sql) {
		SQLQuery queryObject = getSession().createSQLQuery(sql);
		queryObject.executeUpdate();
	}
}