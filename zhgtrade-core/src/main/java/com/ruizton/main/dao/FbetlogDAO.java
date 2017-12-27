package com.ruizton.main.dao;

// default package

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

import com.ruizton.main.dao.comm.HibernateDaoSupport;
import com.ruizton.main.model.Fabout;
import com.ruizton.main.model.Fbetlog;

/**
 * A data access object (DAO) providing persistence and search support for
 * Fbetlog entities. Transaction control of the save(), update() and delete()
 * operations can directly support Spring container-managed transactions or they
 * can be augmented to handle user-managed Spring transactions. Each of these
 * methods provides additional information for how to configure it for the
 * desired type of transaction control.
 * 
 * @see .Fbetlog
 * @author MyEclipse Persistence Tools
 */
@Repository
public class FbetlogDAO extends HibernateDaoSupport {
	private static final Logger log = LoggerFactory.getLogger(FbetlogDAO.class);
	// property constants
	public static final String FQTY = "fqty";

	public void save(Fbetlog transientInstance) {
		log.debug("saving Fbetlog instance");
		try {
			getSession().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(Fbetlog persistentInstance) {
		log.debug("deleting Fbetlog instance");
		try {
			getSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public Fbetlog findById(java.lang.Integer id) {
		log.debug("getting Fbetlog instance with id: " + id);
		try {
			Fbetlog instance = (Fbetlog) getSession().get("com.ruizton.main.model.Fbetlog", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List<Fbetlog> findByExample(Fbetlog instance) {
		log.debug("finding Fbetlog instance by example");
		try {
			List<Fbetlog> results = (List<Fbetlog>) getSession()
					.createCriteria("com.ruizton.main.model.Fbetlog").add(create(instance)).list();
			log.debug("find by example successful, result size: "
					+ results.size());
			return results;
		} catch (RuntimeException re) {
			log.error("find by example failed", re);
			throw re;
		}
	}

	public List findByProperty(String propertyName, Object value) {
		log.debug("finding Fbetlog instance with property: " + propertyName
				+ ", value: " + value);
		try {
			String queryString = "from Fbetlog as model where model."
					+ propertyName + "= ?";
			Query queryObject = getSession().createQuery(queryString);
			queryObject.setParameter(0, value);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List<Fbetlog> findByFqty(Object fqty) {
		return findByProperty(FQTY, fqty);
	}

	public List findAll() {
		log.debug("finding all Fbetlog instances");
		try {
			String queryString = "from Fbetlog";
			Query queryObject = getSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public Fbetlog merge(Fbetlog detachedInstance) {
		log.debug("merging Fbetlog instance");
		try {
			Fbetlog result = (Fbetlog) getSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(Fbetlog instance) {
		log.debug("attaching dirty Fbetlog instance");
		try {
			getSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(Fbetlog instance) {
		log.debug("attaching clean Fbetlog instance");
		try {
			getSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}
	
	public List<Fbetlog> list(int firstResult, int maxResults, String filter,boolean isFY) {
		List<Fbetlog> list = null;
		log.debug("finding Fbetlog instance with filter");
		try {
			String queryString = "from Fbetlog "+filter;
			Query queryObject = getSession().createQuery(queryString);
			if(isFY){
				queryObject.setFirstResult(firstResult);
				queryObject.setMaxResults(maxResults);
			}
			list = queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by Fbetlog name failed", re);
			throw re;
		}
		return list;
	}
	
	public Map getTotalFees(boolean isToday) {
		Map map = new HashMap();
		StringBuffer sql = new StringBuffer();
		sql.append("select sum(IFNULL(fwinqty,0)/100) totalFees from Fbetlog where 1=1 \n");
		if(isToday){
			sql.append("and DATE_FORMAT(fcreatetime,'%Y-%c-%d') = DATE_FORMAT(now(),'%Y-%c-%d') \n");
		}
		SQLQuery queryObject = getSession().createSQLQuery(sql.toString());
		List allList = queryObject.list();
		if(allList != null && allList.size() >0){
			map.put("totalFees",allList.get(0));
		}
		return map;
	}
	
	
}