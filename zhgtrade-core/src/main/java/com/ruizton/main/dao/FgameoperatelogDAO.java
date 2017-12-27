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
import com.ruizton.main.model.Fgameoperatelog;

/**
 * A data access object (DAO) providing persistence and search support for
 * Fgameoperatelog entities. Transaction control of the save(), update() and
 * delete() operations can directly support Spring container-managed
 * transactions or they can be augmented to handle user-managed Spring
 * transactions. Each of these methods provides additional information for how
 * to configure it for the desired type of transaction control.
 * 
 * @see .Fgameoperatelog
 * @author MyEclipse Persistence Tools
 */
@Repository
public class FgameoperatelogDAO extends HibernateDaoSupport {
	private static final Logger log = LoggerFactory
			.getLogger(FgameoperatelogDAO.class);
	// property constants
	public static final String FTYPE = "ftype";
	public static final String FQTY = "fqty";

	public void save(Fgameoperatelog transientInstance) {
		log.debug("saving Fgameoperatelog instance");
		try {
			getSession().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(Fgameoperatelog persistentInstance) {
		log.debug("deleting Fgameoperatelog instance");
		try {
			getSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public Fgameoperatelog findById(java.lang.Integer id) {
		log.debug("getting Fgameoperatelog instance with id: " + id);
		try {
			Fgameoperatelog instance = (Fgameoperatelog) getSession().get(
					"com.ruizton.main.model.Fgameoperatelog", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List<Fgameoperatelog> findByExample(Fgameoperatelog instance) {
		log.debug("finding Fgameoperatelog instance by example");
		try {
			List<Fgameoperatelog> results = (List<Fgameoperatelog>) getSession()
					.createCriteria("com.ruizton.main.model.Fgameoperatelog").add(create(instance))
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
		log.debug("finding Fgameoperatelog instance with property: "
				+ propertyName + ", value: " + value);
		try {
			String queryString = "from Fgameoperatelog as model where model."
					+ propertyName + "= ?";
			Query queryObject = getSession().createQuery(queryString);
			queryObject.setParameter(0, value);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List<Fgameoperatelog> findByFtype(Object ftype) {
		return findByProperty(FTYPE, ftype);
	}

	public List<Fgameoperatelog> findByFqty(Object fqty) {
		return findByProperty(FQTY, fqty);
	}

	public List findAll() {
		log.debug("finding all Fgameoperatelog instances");
		try {
			String queryString = "from Fgameoperatelog";
			Query queryObject = getSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public Fgameoperatelog merge(Fgameoperatelog detachedInstance) {
		log.debug("merging Fgameoperatelog instance");
		try {
			Fgameoperatelog result = (Fgameoperatelog) getSession().merge(
					detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(Fgameoperatelog instance) {
		log.debug("attaching dirty Fgameoperatelog instance");
		try {
			getSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(Fgameoperatelog instance) {
		log.debug("attaching clean Fgameoperatelog instance");
		try {
			getSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}
	
	public List<Fgameoperatelog> list(int firstResult, int maxResults, String filter,boolean isFY) {
		List<Fgameoperatelog> list = null;
		log.debug("finding Fgameoperatelog instance with filter");
		try {
			String queryString = "from Fgameoperatelog "+filter;
			Query queryObject = getSession().createQuery(queryString);
			queryObject.setCacheable(true);
			if(isFY){
				queryObject.setFirstResult(firstResult);
				queryObject.setMaxResults(maxResults);
			}
			list = queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by Fgameoperatelog name failed", re);
			throw re;
		}
		return list;
	}
	
	public Map getTotalQty(int type,boolean isToday) {
		Map map = new HashMap();
		StringBuffer sql = new StringBuffer();
		sql.append("select sum(fqty) totalQty from Fgameoperatelog where fType="+type+" \n");
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