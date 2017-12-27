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
import com.ruizton.main.model.Factivitycompletelog;
import com.ruizton.main.model.Fuser;

/**
 	* A data access object (DAO) providing persistence and search support for Factivitycompletelog entities.
 			* Transaction control of the save(), update() and delete() operations 
		can directly support Spring container-managed transactions or they can be augmented	to handle user-managed Spring transactions. 
		Each of these methods provides additional information for how to configure it for the desired type of transaction control. 	
	 * @see ztmp.Factivitycompletelog
  * @author MyEclipse Persistence Tools 
 */

@Repository
public class FactivitycompletelogDAO extends HibernateDaoSupport  {
	     private static final Logger log = LoggerFactory.getLogger(FactivitycompletelogDAO.class);
		//property constants
	public static final String FTYPE = "ftype";
	public static final String FSTATUS = "fstatus";



    
    public void save(Factivitycompletelog transientInstance) {
        log.debug("saving Factivitycompletelog instance");
        try {
            getSession().save(transientInstance);
            log.debug("save successful");
        } catch (RuntimeException re) {
            log.error("save failed", re);
            throw re;
        }
    }
    
	public void delete(Factivitycompletelog persistentInstance) {
        log.debug("deleting Factivitycompletelog instance");
        try {
            getSession().delete(persistentInstance);
            log.debug("delete successful");
        } catch (RuntimeException re) {
            log.error("delete failed", re);
            throw re;
        }
    }
    
    public Factivitycompletelog findById( java.lang.Integer id) {
        log.debug("getting Factivitycompletelog instance with id: " + id);
        try {
            Factivitycompletelog instance = (Factivitycompletelog) getSession()
                    .get("com.ruizton.main.model.Factivitycompletelog", id);
            return instance;
        } catch (RuntimeException re) {
            log.error("get failed", re);
            throw re;
        }
    }
    
    
    public List<Factivitycompletelog> findByExample(Factivitycompletelog instance) {
        log.debug("finding Factivitycompletelog instance by example");
        try {
            List<Factivitycompletelog> results = (List<Factivitycompletelog>) getSession()
                    .createCriteria("com.ruizton.main.model.Factivitycompletelog")
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
      log.debug("finding Factivitycompletelog instance with property: " + propertyName
            + ", value: " + value);
      try {
         String queryString = "from Factivitycompletelog as model where model." 
         						+ propertyName + "= ?";
         Query queryObject = getSession().createQuery(queryString);
		 queryObject.setParameter(0, value);
		 return queryObject.list();
      } catch (RuntimeException re) {
         log.error("find by property name failed", re);
         throw re;
      }
	}

	public List<Factivitycompletelog> findByFtype(Object ftype
	) {
		return findByProperty(FTYPE, ftype
		);
	}
	
	public List<Factivitycompletelog> findByFstatus(Object fstatus
	) {
		return findByProperty(FSTATUS, fstatus
		);
	}
	

	public List findAll() {
		log.debug("finding all Factivitycompletelog instances");
		try {
			String queryString = "from Factivitycompletelog";
	         Query queryObject = getSession().createQuery(queryString);
			 return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}
	
    public Factivitycompletelog merge(Factivitycompletelog detachedInstance) {
        log.debug("merging Factivitycompletelog instance");
        try {
            Factivitycompletelog result = (Factivitycompletelog) getSession()
                    .merge(detachedInstance);
            log.debug("merge successful");
            return result;
        } catch (RuntimeException re) {
            log.error("merge failed", re);
            throw re;
        }
    }

    public void attachDirty(Factivitycompletelog instance) {
        log.debug("attaching dirty Factivitycompletelog instance");
        try {
            getSession().saveOrUpdate(instance);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }
    
    public void attachClean(Factivitycompletelog instance) {
        log.debug("attaching clean Factivitycompletelog instance");
        try {
            getSession().lock(instance, LockMode.NONE);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }
    
    public List<Factivitycompletelog> findAll(Fuser fuser,int status){

		log.debug("finding all Factivitycompletelog instances");
		try {
			String queryString = "from Factivitycompletelog where fuser.fid=? and fstatus=?";
	         Query queryObject = getSession().createQuery(queryString);
	         queryObject.setParameter(0, fuser.getFid()) ;
	         queryObject.setParameter(1, status) ;
			 return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	
    }
    
    public List<Factivitycompletelog> findLast(Fuser fuser,Factivity factivity){

		log.debug("finding all Factivitycompletelog instances");
		try {
			String queryString = "from Factivitycompletelog where fuser.fid=? and factivity.fid=? order by fid desc";
	         Query queryObject = getSession().createQuery(queryString);
	         queryObject.setParameter(0, fuser.getFid()) ;
	         queryObject.setParameter(1, factivity.getFid()) ;
	         queryObject.setFirstResult(0) ;
	         queryObject.setMaxResults(1) ;
			 return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	
    }
}