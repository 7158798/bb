package com.ruizton.main.dao;

import static org.hibernate.criterion.Example.create;

import java.util.List;

import org.hibernate.LockMode;
import org.hibernate.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.ruizton.main.dao.comm.HibernateDaoSupport;
import com.ruizton.main.model.Factivityreward;
import com.ruizton.main.model.Factivitytype;

/**
 	* A data access object (DAO) providing persistence and search support for Factivityreward entities.
 			* Transaction control of the save(), update() and delete() operations 
		can directly support Spring container-managed transactions or they can be augmented	to handle user-managed Spring transactions. 
		Each of these methods provides additional information for how to configure it for the desired type of transaction control. 	
	 * @see ztmp.Factivityreward
  * @author MyEclipse Persistence Tools 
 */
@Repository
public class FactivityrewardDAO extends HibernateDaoSupport  {
	     private static final Logger log = LoggerFactory.getLogger(FactivityrewardDAO.class);
		//property constants
	public static final String FVIRTUAL_COIN_OR_CNY = "fvirtualCoinOrCny";
	public static final String FRATE_OR_REAL = "frateOrReal";
	public static final String FAMOUNT = "famount";



    
    public void save(Factivityreward transientInstance) {
        log.debug("saving Factivityreward instance");
        try {
            getSession().save(transientInstance);
            log.debug("save successful");
        } catch (RuntimeException re) {
            log.error("save failed", re);
            throw re;
        }
    }
    
	public void delete(Factivityreward persistentInstance) {
        log.debug("deleting Factivityreward instance");
        try {
            getSession().delete(persistentInstance);
            log.debug("delete successful");
        } catch (RuntimeException re) {
            log.error("delete failed", re);
            throw re;
        }
    }
    
    public Factivityreward findById( java.lang.Integer id) {
        log.debug("getting Factivityreward instance with id: " + id);
        try {
            Factivityreward instance = (Factivityreward) getSession()
                    .get("com.ruizton.main.model.Factivityreward", id);
            return instance;
        } catch (RuntimeException re) {
            log.error("get failed", re);
            throw re;
        }
    }
    
    
    public List<Factivityreward> findByExample(Factivityreward instance) {
        log.debug("finding Factivityreward instance by example");
        try {
            List<Factivityreward> results = (List<Factivityreward>) getSession()
                    .createCriteria("com.ruizton.main.model.Factivityreward")
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
      log.debug("finding Factivityreward instance with property: " + propertyName
            + ", value: " + value);
      try {
         String queryString = "from Factivityreward as model where model." 
         						+ propertyName + "= ?";
         Query queryObject = getSession().createQuery(queryString);
		 queryObject.setParameter(0, value);
		 return queryObject.list();
      } catch (RuntimeException re) {
         log.error("find by property name failed", re);
         throw re;
      }
	}

	public List<Factivityreward> findByFvirtualCoinOrCny(Object fvirtualCoinOrCny
	) {
		return findByProperty(FVIRTUAL_COIN_OR_CNY, fvirtualCoinOrCny
		);
	}
	
	public List<Factivityreward> findByFrateOrReal(Object frateOrReal
	) {
		return findByProperty(FRATE_OR_REAL, frateOrReal
		);
	}
	
	public List<Factivityreward> findByFamount(Object famount
	) {
		return findByProperty(FAMOUNT, famount
		);
	}
	

	public List findAll() {
		log.debug("finding all Factivityreward instances");
		try {
			String queryString = "from Factivityreward";
	         Query queryObject = getSession().createQuery(queryString);
			 return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}
	
    public Factivityreward merge(Factivityreward detachedInstance) {
        log.debug("merging Factivityreward instance");
        try {
            Factivityreward result = (Factivityreward) getSession()
                    .merge(detachedInstance);
            log.debug("merge successful");
            return result;
        } catch (RuntimeException re) {
            log.error("merge failed", re);
            throw re;
        }
    }

    public void attachDirty(Factivityreward instance) {
        log.debug("attaching dirty Factivityreward instance");
        try {
            getSession().saveOrUpdate(instance);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }
    
    public void attachClean(Factivityreward instance) {
        log.debug("attaching clean Factivityreward instance");
        try {
            getSession().lock(instance, LockMode.NONE);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }
    
	public List<Factivityreward> list(int firstResult, int maxResults, String filter,boolean isFY) {
		List<Factivityreward> list = null;
		log.debug("finding Factivityreward instance with filter");
		try {
			String queryString = "from Factivityreward "+filter;
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
}