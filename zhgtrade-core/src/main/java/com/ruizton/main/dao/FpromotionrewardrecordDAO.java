package com.ruizton.main.dao;

import static org.hibernate.criterion.Example.create;

import java.util.List;

import org.hibernate.LockMode;
import org.hibernate.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.ruizton.main.dao.comm.HibernateDaoSupport;
import com.ruizton.main.model.Factivity;
import com.ruizton.main.model.Fpromotionrewardrecord;

/**
 	* A data access object (DAO) providing persistence and search support for Fpromotionrewardrecord entities.
 			* Transaction control of the save(), update() and delete() operations 
		can directly support Spring container-managed transactions or they can be augmented	to handle user-managed Spring transactions. 
		Each of these methods provides additional information for how to configure it for the desired type of transaction control. 	
	 * @see ztmp.Fpromotionrewardrecord
  * @author MyEclipse Persistence Tools 
 */
@Repository
public class FpromotionrewardrecordDAO extends HibernateDaoSupport  {
	     private static final Logger log = LoggerFactory.getLogger(FpromotionrewardrecordDAO.class);
		//property constants
	public static final String FREWARD_DETAIL = "frewardDetail";
	public static final String FREWARD_REASON = "frewardReason";



    
    public void save(Fpromotionrewardrecord transientInstance) {
        log.debug("saving Fpromotionrewardrecord instance");
        try {
            getSession().save(transientInstance);
            log.debug("save successful");
        } catch (RuntimeException re) {
            log.error("save failed", re);
            throw re;
        }
    }
    
	public void delete(Fpromotionrewardrecord persistentInstance) {
        log.debug("deleting Fpromotionrewardrecord instance");
        try {
            getSession().delete(persistentInstance);
            log.debug("delete successful");
        } catch (RuntimeException re) {
            log.error("delete failed", re);
            throw re;
        }
    }
    
    public Fpromotionrewardrecord findById( java.lang.Integer id) {
        log.debug("getting Fpromotionrewardrecord instance with id: " + id);
        try {
            Fpromotionrewardrecord instance = (Fpromotionrewardrecord) getSession()
                    .get("com.ruizton.main.model.Fpromotionrewardrecord", id);
            return instance;
        } catch (RuntimeException re) {
            log.error("get failed", re);
            throw re;
        }
    }
    
    
    public List<Fpromotionrewardrecord> findByExample(Fpromotionrewardrecord instance) {
        log.debug("finding Fpromotionrewardrecord instance by example");
        try {
            List<Fpromotionrewardrecord> results = (List<Fpromotionrewardrecord>) getSession()
                    .createCriteria("com.ruizton.main.model.Fpromotionrewardrecord")
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
      log.debug("finding Fpromotionrewardrecord instance with property: " + propertyName
            + ", value: " + value);
      try {
         String queryString = "from Fpromotionrewardrecord as model where model." 
         						+ propertyName + "= ?";
         Query queryObject = getSession().createQuery(queryString);
		 queryObject.setParameter(0, value);
		 return queryObject.list();
      } catch (RuntimeException re) {
         log.error("find by property name failed", re);
         throw re;
      }
	}

	public List<Fpromotionrewardrecord> findByFrewardDetail(Object frewardDetail
	) {
		return findByProperty(FREWARD_DETAIL, frewardDetail
		);
	}
	
	public List<Fpromotionrewardrecord> findByFrewardReason(Object frewardReason
	) {
		return findByProperty(FREWARD_REASON, frewardReason
		);
	}
	

	public List findAll() {
		log.debug("finding all Fpromotionrewardrecord instances");
		try {
			String queryString = "from Fpromotionrewardrecord";
	         Query queryObject = getSession().createQuery(queryString);
			 return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}
	
    public Fpromotionrewardrecord merge(Fpromotionrewardrecord detachedInstance) {
        log.debug("merging Fpromotionrewardrecord instance");
        try {
            Fpromotionrewardrecord result = (Fpromotionrewardrecord) getSession()
                    .merge(detachedInstance);
            log.debug("merge successful");
            return result;
        } catch (RuntimeException re) {
            log.error("merge failed", re);
            throw re;
        }
    }

    public void attachDirty(Fpromotionrewardrecord instance) {
        log.debug("attaching dirty Fpromotionrewardrecord instance");
        try {
            getSession().saveOrUpdate(instance);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }
    
    public void attachClean(Fpromotionrewardrecord instance) {
        log.debug("attaching clean Fpromotionrewardrecord instance");
        try {
            getSession().lock(instance, LockMode.NONE);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }
    
	public List<Fpromotionrewardrecord> list(int firstResult, int maxResults, String filter,boolean isFY) {
		List<Fpromotionrewardrecord> list = null;
		log.debug("finding Fpromotionrewardrecord instance with filter");
		try {
			String queryString = "from Fpromotionrewardrecord "+filter;
			Query queryObject = getSession().createQuery(queryString);
			if(isFY){
				queryObject.setFirstResult(firstResult);
				queryObject.setMaxResults(maxResults);
			}
			list = queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by Fpromotionrewardrecord name failed", re);
			throw re;
		}
		return list;
	}
}