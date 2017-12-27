package com.ruizton.main.dao;

import static org.hibernate.criterion.Example.create;

import java.util.List;

import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.ruizton.main.dao.comm.HibernateDaoSupport;
import com.ruizton.main.model.Fentrustlogscorelog;

/**
 * A data access object (DAO) providing persistence and search support for
 * Fentrustlogscorelog entities. Transaction control of the save(), update() and
 * delete() operations can directly support Spring container-managed
 * transactions or they can be augmented to handle user-managed Spring
 * transactions. Each of these methods provides additional information for how
 * to configure it for the desired type of transaction control.
 * 
 * @see com.ruizton.main.model.Fentrustlogscorelog
 * @author MyEclipse Persistence Tools
 */

@Repository
public class FentrustlogscorelogDAO extends HibernateDaoSupport {
	private static final Logger log = LoggerFactory
			.getLogger(FentrustlogscorelogDAO.class);
	// property constants
	public static final String FENTRUSTLOG_ID = "fentrustlogId";
	public static final String FUSER_ID = "fuserId";
	public static final String FSCORE = "fscore";

	public void save(Fentrustlogscorelog transientInstance) {
		log.debug("saving Fentrustlogscorelog instance");
		try {
			getSession().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(Fentrustlogscorelog persistentInstance) {
		log.debug("deleting Fentrustlogscorelog instance");
		try {
			getSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public Fentrustlogscorelog findById(java.lang.Integer id) {
		log.debug("getting Fentrustlogscorelog instance with id: " + id);
		try {
			Fentrustlogscorelog instance = (Fentrustlogscorelog) getSession()
					.get("com.ruizton.main.model.Fentrustlogscorelog", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List<Fentrustlogscorelog> findByExample(Fentrustlogscorelog instance) {
		log.debug("finding Fentrustlogscorelog instance by example");
		try {
			List<Fentrustlogscorelog> results = (List<Fentrustlogscorelog>) getSession()
					.createCriteria("com.ruizton.main.model.Fentrustlogscorelog")
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
		log.debug("finding Fentrustlogscorelog instance with property: "
				+ propertyName + ", value: " + value);
		try {
			String queryString = "from Fentrustlogscorelog as model where model."
					+ propertyName + "= ?";
			Query queryObject = getSession().createQuery(queryString);
			queryObject.setParameter(0, value);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List<Fentrustlogscorelog> findByFentrustlogId(Object fentrustlogId) {
		return findByProperty(FENTRUSTLOG_ID, fentrustlogId);
	}

	public List<Fentrustlogscorelog> findByFuserId(Object fuserId) {
		return findByProperty(FUSER_ID, fuserId);
	}

	public List<Fentrustlogscorelog> findByFscore(Object fscore) {
		return findByProperty(FSCORE, fscore);
	}

	public List findAll() {
		log.debug("finding all Fentrustlogscorelog instances");
		try {
			String queryString = "from Fentrustlogscorelog";
			Query queryObject = getSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public Fentrustlogscorelog merge(Fentrustlogscorelog detachedInstance) {
		log.debug("merging Fentrustlogscorelog instance");
		try {
			Fentrustlogscorelog result = (Fentrustlogscorelog) getSession()
					.merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(Fentrustlogscorelog instance) {
		log.debug("attaching dirty Fentrustlogscorelog instance");
		try {
			getSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(Fentrustlogscorelog instance) {
		log.debug("attaching clean Fentrustlogscorelog instance");
		try {
			getSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}
	
	public int getMaxFentrustLogScoreLogId(){
		try {
			String queryString = "select max(fentrustlogId) from Fentrustlogscorelog " ;
			SQLQuery queryObject = getSession().createSQLQuery(queryString) ;
			Object obj = queryObject.uniqueResult() ;
			if(obj==null){
				return 0 ;
			}else{
				return Integer.parseInt(obj.toString()) ;
			}
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}
	
}