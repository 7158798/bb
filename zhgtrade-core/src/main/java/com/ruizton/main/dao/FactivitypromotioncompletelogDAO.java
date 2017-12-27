package com.ruizton.main.dao;

import static org.hibernate.criterion.Example.create;

import java.util.List;

import org.hibernate.LockMode;
import org.hibernate.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.ruizton.main.dao.comm.HibernateDaoSupport;
import com.ruizton.main.model.Factivitycompletelog;
import com.ruizton.main.model.Factivitypromotioncompletelog;
import com.ruizton.main.model.Fuser;

/**
 	* A data access object (DAO) providing persistence and search support for Factivitypromotioncompletelog entities.
 			* Transaction control of the save(), update() and delete() operations 
		can directly support Spring container-managed transactions or they can be augmented	to handle user-managed Spring transactions. 
		Each of these methods provides additional information for how to configure it for the desired type of transaction control. 	
	 * @see ztmp.Factivitypromotioncompletelog
  * @author MyEclipse Persistence Tools 
 */

@Repository
public class FactivitypromotioncompletelogDAO extends HibernateDaoSupport  {
	     private static final Logger log = LoggerFactory.getLogger(FactivitypromotioncompletelogDAO.class);
		//property constants
	public static final String FTYPE = "ftype";
	public static final String FSTATUS = "fstatus";



    
    public void save(Factivitypromotioncompletelog transientInstance) {
        log.debug("saving Factivitypromotioncompletelog instance");
        try {
            getSession().save(transientInstance);
            log.debug("save successful");
        } catch (RuntimeException re) {
            log.error("save failed", re);
            throw re;
        }
    }
    
	public void delete(Factivitypromotioncompletelog persistentInstance) {
        log.debug("deleting Factivitypromotioncompletelog instance");
        try {
            getSession().delete(persistentInstance);
            log.debug("delete successful");
        } catch (RuntimeException re) {
            log.error("delete failed", re);
            throw re;
        }
    }
    
    public Factivitypromotioncompletelog findById( java.lang.Integer id) {
        log.debug("getting Factivitypromotioncompletelog instance with id: " + id);
        try {
            Factivitypromotioncompletelog instance = (Factivitypromotioncompletelog) getSession()
                    .get("com.ruizton.main.model.Factivitypromotioncompletelog", id);
            return instance;
        } catch (RuntimeException re) {
            log.error("get failed", re);
            throw re;
        }
    }
    
    
    public List<Factivitypromotioncompletelog> findByExample(Factivitypromotioncompletelog instance) {
        log.debug("finding Factivitypromotioncompletelog instance by example");
        try {
            List<Factivitypromotioncompletelog> results = (List<Factivitypromotioncompletelog>) getSession()
                    .createCriteria("com.ruizton.main.model.Factivitypromotioncompletelog")
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
      log.debug("finding Factivitypromotioncompletelog instance with property: " + propertyName
            + ", value: " + value);
      try {
         String queryString = "from Factivitypromotioncompletelog as model where model." 
         						+ propertyName + "= ?";
         Query queryObject = getSession().createQuery(queryString);
		 queryObject.setParameter(0, value);
		 return queryObject.list();
      } catch (RuntimeException re) {
         log.error("find by property name failed", re);
         throw re;
      }
	}

	public List<Factivitypromotioncompletelog> findByFtype(Object ftype
	) {
		return findByProperty(FTYPE, ftype
		);
	}
	
	public List<Factivitypromotioncompletelog> findByFstatus(Object fstatus
	) {
		return findByProperty(FSTATUS, fstatus
		);
	}
	

	public List findAll() {
		log.debug("finding all Factivitypromotioncompletelog instances");
		try {
			String queryString = "from Factivitypromotioncompletelog";
	         Query queryObject = getSession().createQuery(queryString);
			 return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}
	
    public Factivitypromotioncompletelog merge(Factivitypromotioncompletelog detachedInstance) {
        log.debug("merging Factivitypromotioncompletelog instance");
        try {
            Factivitypromotioncompletelog result = (Factivitypromotioncompletelog) getSession()
                    .merge(detachedInstance);
            log.debug("merge successful");
            return result;
        } catch (RuntimeException re) {
            log.error("merge failed", re);
            throw re;
        }
    }

    public void attachDirty(Factivitypromotioncompletelog instance) {
        log.debug("attaching dirty Factivitypromotioncompletelog instance");
        try {
            getSession().saveOrUpdate(instance);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }
    
    public void attachClean(Factivitypromotioncompletelog instance) {
        log.debug("attaching clean Factivitypromotioncompletelog instance");
        try {
            getSession().lock(instance, LockMode.NONE);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }
    
    public List<Factivitypromotioncompletelog> findAll(Fuser fuser,int status){

		log.debug("finding all Factivitypromotioncompletelog instances");
		try {
			String queryString = "from Factivitypromotioncompletelog where fuser.fid=? and fstatus=?";
	         Query queryObject = getSession().createQuery(queryString);
	         queryObject.setParameter(1, fuser.getFid()) ;
	         queryObject.setParameter(2, status) ;
			 return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	
    }
}