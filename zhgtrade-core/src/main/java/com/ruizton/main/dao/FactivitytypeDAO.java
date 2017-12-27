package com.ruizton.main.dao;

import static org.hibernate.criterion.Example.create;

import java.util.List;

import org.hibernate.LockMode;
import org.hibernate.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.ruizton.main.dao.comm.HibernateDaoSupport;
import com.ruizton.main.model.Fabout;
import com.ruizton.main.model.Factivitytype;
import com.ruizton.main.model.Fvirtualcointype;

/**
 	* A data access object (DAO) providing persistence and search support for Factivitytype entities.
 			* Transaction control of the save(), update() and delete() operations 
		can directly support Spring container-managed transactions or they can be augmented	to handle user-managed Spring transactions. 
		Each of these methods provides additional information for how to configure it for the desired type of transaction control. 	
	 * @see ztmp.Factivitytype
  * @author MyEclipse Persistence Tools 
 */
@Repository
public class FactivitytypeDAO extends HibernateDaoSupport  {
	     private static final Logger log = LoggerFactory.getLogger(FactivitytypeDAO.class);
		//property constants
	public static final String FNAME = "fname";
	public static final String FDESCRIPTION = "fdescription";



    
    public void save(Factivitytype transientInstance) {
        log.debug("saving Factivitytype instance");
        try {
            getSession().save(transientInstance);
            log.debug("save successful");
        } catch (RuntimeException re) {
            log.error("save failed", re);
            throw re;
        }
    }
    
	public void delete(Factivitytype persistentInstance) {
        log.debug("deleting Factivitytype instance");
        try {
            getSession().delete(persistentInstance);
            log.debug("delete successful");
        } catch (RuntimeException re) {
            log.error("delete failed", re);
            throw re;
        }
    }
    
    public Factivitytype findById( java.lang.Integer id) {
        log.debug("getting Factivitytype instance with id: " + id);
        try {
            Factivitytype instance = (Factivitytype) getSession()
                    .get("com.ruizton.main.model.Factivitytype", id);
            return instance;
        } catch (RuntimeException re) {
            log.error("get failed", re);
            throw re;
        }
    }
    
    
    public List<Factivitytype> findByExample(Factivitytype instance) {
        log.debug("finding Factivitytype instance by example");
        try {
            List<Factivitytype> results = (List<Factivitytype>) getSession()
                    .createCriteria("com.ruizton.main.model.Factivitytype")
                    .add( create(instance) )
            .list();
            log.debug("find by example successful, result size: " + results.size());
            return results;
        } catch (RuntimeException re) {
            log.error("find by example failed", re);
            throw re;
        }
    }    
    
    public List findByProperty(String propertyName, Object value) {
      log.debug("finding Factivitytype instance with property: " + propertyName
            + ", value: " + value);
      try {
         String queryString = "from Factivitytype as model where model." 
         						+ propertyName + "= ?";
         Query queryObject = getSession().createQuery(queryString);
		 queryObject.setParameter(0, value);
		 return queryObject.list();
      } catch (RuntimeException re) {
         log.error("find by property name failed", re);
         throw re;
      }
	}

	public List<Factivitytype> findByFname(Object fname
	) {
		return findByProperty(FNAME, fname
		);
	}
	
	public List<Factivitytype> findByFdescription(Object fdescription
	) {
		return findByProperty(FDESCRIPTION, fdescription
		);
	}
	

	public List findAll() {
		log.debug("finding all Factivitytype instances");
		try {
			String queryString = "from Factivitytype";
	         Query queryObject = getSession().createQuery(queryString);
			 return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}
	
    public Factivitytype merge(Factivitytype detachedInstance) {
        log.debug("merging Factivitytype instance");
        try {
            Factivitytype result = (Factivitytype) getSession()
                    .merge(detachedInstance);
            log.debug("merge successful");
            return result;
        } catch (RuntimeException re) {
            log.error("merge failed", re);
            throw re;
        }
    }

    public void attachDirty(Factivitytype instance) {
        log.debug("attaching dirty Factivitytype instance");
        try {
            getSession().saveOrUpdate(instance);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }
    
    public void attachClean(Factivitytype instance) {
        log.debug("attaching clean Factivitytype instance");
        try {
            getSession().lock(instance, LockMode.NONE);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }
    
	public List<Factivitytype> list(int firstResult, int maxResults, String filter,boolean isFY) {
		List<Factivitytype> list = null;
		log.debug("finding Factivitytype instance with filter");
		try {
			String queryString = "from Factivitytype "+filter;
			Query queryObject = getSession().createQuery(queryString);
			if(isFY){
				queryObject.setFirstResult(firstResult);
				queryObject.setMaxResults(maxResults);
			}
			list = queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by Factivitytype name failed", re);
			throw re;
		}
		return list;
	}
	
	public Factivitytype findFactivityType(int type,Fvirtualcointype fvirtualcointype) {
		log.debug("finding all Factivitytype instances");
		try {
			String queryString = "from Factivitytype where type=? and fvirtualCoinType.fid=? ";
	         Query queryObject = getSession().createQuery(queryString);
	         queryObject.setParameter(0, type) ;
	         queryObject.setParameter(1, fvirtualcointype.getFid()) ;
			 List<Factivitytype> factivitytypes = queryObject.list();
			 if(factivitytypes.size()>0){
				 return factivitytypes.get(0) ;
			 }else{
				 return null ;
			 }
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}
}