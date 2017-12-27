package com.ruizton.main.dao;

// default package

import static org.hibernate.criterion.Example.create;

import java.util.List;

import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.ruizton.main.Enum.GameharvestlogStatusEnum;
import com.ruizton.main.dao.comm.HibernateDaoSupport;
import com.ruizton.main.model.Fchancelog;
import com.ruizton.main.model.Fgameharvestlog;

/**
 * A data access object (DAO) providing persistence and search support for
 * Fgameharvestlog entities. Transaction control of the save(), update() and
 * delete() operations can directly support Spring container-managed
 * transactions or they can be augmented to handle user-managed Spring
 * transactions. Each of these methods provides additional information for how
 * to configure it for the desired type of transaction control.
 * 
 * @see .Fgameharvestlog
 * @author MyEclipse Persistence Tools
 */
@Repository
public class FgameharvestlogDAO extends HibernateDaoSupport {
	private static final Logger log = LoggerFactory
			.getLogger(FgameharvestlogDAO.class);
	// property constants
	public static final String VERSION = "version";
	public static final String FUSERID = "fuserid";
	public static final String FGROUPQTY = "fgroupqty";
	public static final String FHARVESTQTY = "fharvestqty";
	public static final String FSTATUS = "fstatus";
	public static final String FTYPE = "ftype";

	public void save(Fgameharvestlog transientInstance) {
		log.debug("saving Fgameharvestlog instance");
		try {
			getSession().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(Fgameharvestlog persistentInstance) {
		log.debug("deleting Fgameharvestlog instance");
		try {
			getSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public Fgameharvestlog findById(java.lang.Integer id) {
		log.debug("getting Fgameharvestlog instance with id: " + id);
		try {
			Fgameharvestlog instance = (Fgameharvestlog) getSession().get(
					"com.ruizton.main.model.Fgameharvestlog", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List<Fgameharvestlog> findByExample(Fgameharvestlog instance) {
		log.debug("finding Fgameharvestlog instance by example");
		try {
			List<Fgameharvestlog> results = (List<Fgameharvestlog>) getSession()
					.createCriteria("com.ruizton.main.model.Fgameharvestlog")
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
		log.debug("finding Fgameharvestlog instance with property: "
				+ propertyName + ", value: " + value);
		try {
			String queryString = "from Fgameharvestlog as model where model."
					+ propertyName + "= ?";
			Query queryObject = getSession().createQuery(queryString);
			queryObject.setParameter(0, value);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List<Fgameharvestlog> findByVersion(Object version) {
		return findByProperty(VERSION, version);
	}

	public List<Fgameharvestlog> findByFuserid(Object fuserid) {
		return findByProperty(FUSERID, fuserid);
	}

	public List<Fgameharvestlog> findByFgroupqty(Object fgroupqty) {
		return findByProperty(FGROUPQTY, fgroupqty);
	}

	public List<Fgameharvestlog> findByFharvestqty(Object fharvestqty) {
		return findByProperty(FHARVESTQTY, fharvestqty);
	}

	public List<Fgameharvestlog> findByFstatus(Object fstatus) {
		return findByProperty(FSTATUS, fstatus);
	}

	public List<Fgameharvestlog> findByFtype(Object ftype) {
		return findByProperty(FTYPE, ftype);
	}

	public List findAll() {
		log.debug("finding all Fgameharvestlog instances");
		try {
			String queryString = "from Fgameharvestlog";
			Query queryObject = getSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public Fgameharvestlog merge(Fgameharvestlog detachedInstance) {
		log.debug("merging Fgameharvestlog instance");
		try {
			Fgameharvestlog result = (Fgameharvestlog) getSession().merge(
					detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(Fgameharvestlog instance) {
		log.debug("attaching dirty Fgameharvestlog instance");
		try {
			getSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(Fgameharvestlog instance) {
		log.debug("attaching clean Fgameharvestlog instance");
		try {
			getSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}
	
	public List<Fgameharvestlog> list(int firstResult, int maxResults, String filter,boolean isFY) {
		List<Fgameharvestlog> list = null;
		log.debug("finding Fgameharvestlog instance with filter");
		try {
			String queryString = "from Fgameharvestlog "+filter;
			Query queryObject = getSession().createQuery(queryString);
			if(isFY){
				queryObject.setFirstResult(firstResult);
				queryObject.setMaxResults(maxResults);
			}
			list = queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by Fgameharvestlog name failed", re);
			throw re;
		}
		return list;
	}
	
	public double getFrozenQty(String field,String filter,boolean isToday) {
		StringBuffer sf = new StringBuffer();
		sf.append("select sum("+field+") "+ field+" from Fgameharvestlog \n");
		sf.append(filter);
		if(isToday){
			sf.append(" and  DATE_FORMAT(fcreatetime,'%Y-%c-%d') = DATE_FORMAT(now(),'%Y-%c-%d') \n");
		}
		Query queryObject = getSession().createSQLQuery(sf.toString());
		if(queryObject.list() == null || queryObject.list().get(0) == null) return 0d;
		return Double.valueOf(queryObject.list().get(0).toString());
	}
	
	public void updateFrozenLog(String filter) {
		StringBuffer sf = new StringBuffer();
		sf.append("update Fgameharvestlog set fharvestqty=fharvestqty+ffrozenqty,ffrozenqty=0,fstatus="+GameharvestlogStatusEnum.COMPLETE+"\n");
		sf.append(filter);
		SQLQuery queryObject = getSession().createSQLQuery(sf.toString());
		queryObject.executeUpdate();
	}
}