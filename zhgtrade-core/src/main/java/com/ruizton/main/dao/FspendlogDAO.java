package com.ruizton.main.dao;

import static org.hibernate.criterion.Example.create;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.ruizton.main.model.Fspendlog;
import com.ruizton.main.model.Fuser;

import com.ruizton.main.dao.comm.HibernateDaoSupport;

/**
 * A data access object (DAO) providing persistence and search support for
 * Fspendlog entities. Transaction control of the save(), update() and delete()
 * operations can directly support Spring container-managed transactions or they
 * can be augmented to handle user-managed Spring transactions. Each of these
 * methods provides additional information for how to configure it for the
 * desired type of transaction control.
 * 
 * @see com.ruizton.main.model.Fspendlog
 * @author MyEclipse Persistence Tools
 */

@Repository
public class FspendlogDAO extends HibernateDaoSupport {
	private static final Logger log = LoggerFactory
			.getLogger(FspendlogDAO.class);
	// property constants
	public static final String FTYPE = "ftype";
	public static final String FGDOU_SPEND = "fgdouSpend";
	public static final String FSCORE = "fscore";

	public void save(Fspendlog transientInstance) {
		log.debug("saving Fspendlog instance");
		try {
			getSession().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(Fspendlog persistentInstance) {
		log.debug("deleting Fspendlog instance");
		try {
			getSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public Fspendlog findById(java.lang.Integer id) {
		log.debug("getting Fspendlog instance with id: " + id);
		try {
			Fspendlog instance = (Fspendlog) getSession().get("com.ruizton.main.model.Fspendlog",
					id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List<Fspendlog> findByExample(Fspendlog instance) {
		log.debug("finding Fspendlog instance by example");
		try {
			List<Fspendlog> results = (List<Fspendlog>) getSession()
					.createCriteria("com.ruizton.main.model.Fspendlog").add(create(instance))
					.list();
			log.debug("find by example successful, result size: "
					+ results.size());
			return results;
		} catch (RuntimeException re) {
			log.error("find by example failed", re);
			throw re;
		}
	}

	public List findByProperty(String propertyName, Object value) {
		log.debug("finding Fspendlog instance with property: " + propertyName
				+ ", value: " + value);
		try {
			String queryString = "from Fspendlog as model where model."
					+ propertyName + "= ?";
			Query queryObject = getSession().createQuery(queryString);
			queryObject.setParameter(0, value);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List<Fspendlog> findByFtype(Object ftype) {
		return findByProperty(FTYPE, ftype);
	}

	public List<Fspendlog> findByFgdouSpend(Object fgdouSpend) {
		return findByProperty(FGDOU_SPEND, fgdouSpend);
	}

	public List<Fspendlog> findByFscore(Object fscore) {
		return findByProperty(FSCORE, fscore);
	}

	public List findAll() {
		log.debug("finding all Fspendlog instances");
		try {
			String queryString = "from Fspendlog";
			Query queryObject = getSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public Fspendlog merge(Fspendlog detachedInstance) {
		log.debug("merging Fspendlog instance");
		try {
			Fspendlog result = (Fspendlog) getSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(Fspendlog instance) {
		log.debug("attaching dirty Fspendlog instance");
		try {
			getSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(Fspendlog instance) {
		log.debug("attaching clean Fspendlog instance");
		try {
			getSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}
	
	public List<Fspendlog> list(int firstResult, int maxResults,
			String filter,boolean isFY) {
		List<Fspendlog> list = null;
		log.debug("finding Fspendlog instance with filter");
		try {
			String queryString = "from Fspendlog "+filter;
			Query queryObject = getSession().createQuery(queryString);
			if(isFY){
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
	
	public Map getTotalQty(boolean isToday) {
		Map map = new HashMap();
		StringBuffer sql = new StringBuffer();
		sql.append("select sum(fgdouSpend) totalQty from Fspendlog where 1=1 \n");
		if(isToday){
			sql.append("and DATE_FORMAT(fcreatetime,'%Y-%c-%d') = DATE_FORMAT(now(),'%Y-%c-%d') \n");
		}
		SQLQuery queryObject = getSession().createSQLQuery(sql.toString());
		List allList = queryObject.list();
		if(allList != null && allList.size() >0){
			map.put("totalQty",allList.get(0));
		}
		return map;
	}
}