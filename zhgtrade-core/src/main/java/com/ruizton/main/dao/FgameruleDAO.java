package com.ruizton.main.dao;
// default package

import static org.hibernate.criterion.Example.create;

import java.sql.Timestamp;
import java.util.List;

import org.hibernate.LockMode;
import org.hibernate.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.ruizton.main.dao.comm.HibernateDaoSupport;
import com.ruizton.main.model.Fgamelog;
import com.ruizton.main.model.Fgamerule;
import com.ruizton.main.model.Fuser;
import com.ruizton.util.Utils;

/**
 * A data access object (DAO) providing persistence and search support for
 * Fgamerule entities. Transaction control of the save(), update() and delete()
 * operations can directly support Spring container-managed transactions or they
 * can be augmented to handle user-managed Spring transactions. Each of these
 * methods provides additional information for how to configure it for the
 * desired type of transaction control.
 * 
 * @see .Fgamerule
 * @author MyEclipse Persistence Tools
 */
@Repository
public class FgameruleDAO extends HibernateDaoSupport {
	private static final Logger log = LoggerFactory
			.getLogger(FgameruleDAO.class);
	// property constants
	public static final String FSCOREID = "fscoreid";
	public static final String FGROUND_QTY = "fgroundQty";
	public static final String FCAN_ZDTIMES = "fcanZdtimes";
	public static final String FHARVEST_QTY = "fharvestQty";
	public static final String FHARVEST_RATE = "fharvestRate";
	public static final String FTIME_SPAN = "ftimeSpan";
	public static final String FUPGRADE_NEED_QTY = "fupgradeNeedQty";
	public static final String FUPGRADE_NEED_RE_QTY = "fupgradeNeedReQty";

	public void save(Fgamerule transientInstance) {
		log.debug("saving Fgamerule instance");
		try {
			getSession().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(Fgamerule persistentInstance) {
		log.debug("deleting Fgamerule instance");
		try {
			getSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public Fgamerule findById(java.lang.Integer id) {
		log.debug("getting Fgamerule instance with id: " + id);
		try {
			Fgamerule instance = (Fgamerule) getSession().get("com.ruizton.main.model.Fgamerule", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List<Fgamerule> findByExample(Fgamerule instance) {
		log.debug("finding Fgamerule instance by example");
		try {
			List<Fgamerule> results = (List<Fgamerule>) getSession()
					.createCriteria("com.ruizton.main.model.Fgamerule").add(create(instance)).list();
			log.debug("find by example successful, result size: "
					+ results.size());
			return results;
		} catch (RuntimeException re) {
			log.error("find by example failed", re);
			throw re;
		}
	}

	public List findByProperty(String propertyName, Object value) {
		log.debug("finding Fgamerule instance with property: " + propertyName
				+ ", value: " + value);
		try {
			String queryString = "from Fgamerule as model where model."
					+ propertyName + "= ?";
			Query queryObject = getSession().createQuery(queryString);
			queryObject.setParameter(0, value);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List<Fgamerule> findByFscoreid(Object fscoreid) {
		return findByProperty(FSCOREID, fscoreid);
	}

	public List<Fgamerule> findByFgroundQty(Object fgroundQty) {
		return findByProperty(FGROUND_QTY, fgroundQty);
	}

	public List<Fgamerule> findByFcanZdtimes(Object fcanZdtimes) {
		return findByProperty(FCAN_ZDTIMES, fcanZdtimes);
	}

	public List<Fgamerule> findByFharvestQty(Object fharvestQty) {
		return findByProperty(FHARVEST_QTY, fharvestQty);
	}

	public List<Fgamerule> findByFharvestRate(Object fharvestRate) {
		return findByProperty(FHARVEST_RATE, fharvestRate);
	}

	public List<Fgamerule> findByFtimeSpan(Object ftimeSpan) {
		return findByProperty(FTIME_SPAN, ftimeSpan);
	}

	public List<Fgamerule> findByFupgradeNeedQty(Object fupgradeNeedQty) {
		return findByProperty(FUPGRADE_NEED_QTY, fupgradeNeedQty);
	}

	public List<Fgamerule> findByFupgradeNeedReQty(Object fupgradeNeedReQty) {
		return findByProperty(FUPGRADE_NEED_RE_QTY, fupgradeNeedReQty);
	}

	public List findAll() {
		log.debug("finding all Fgamerule instances");
		try {
			String queryString = "from Fgamerule";
			Query queryObject = getSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public Fgamerule merge(Fgamerule detachedInstance) {
		log.debug("merging Fgamerule instance");
		try {
			Fgamerule result = (Fgamerule) getSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(Fgamerule instance) {
		log.debug("attaching dirty Fgamerule instance");
		try {
			getSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(Fgamerule instance) {
		log.debug("attaching clean Fgamerule instance");
		try {
			getSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}
	
	public List<Fgamerule> list(int firstResult, int maxResults, String filter,boolean isFY) {
		List<Fgamerule> list = null;
		log.debug("finding Fgamerule instance with filter");
		try {
			String queryString = "from Fgamerule "+filter;
			Query queryObject = getSession().createQuery(queryString);
			queryObject.setCacheable(true);
			if(isFY){
				queryObject.setFirstResult(firstResult);
				queryObject.setMaxResults(maxResults);
			}
			list = queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by Fgamerule name failed", re);
			throw re;
		}
		return list;
	}
	

}