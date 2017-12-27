package com.ruizton.main.dao;

import static org.hibernate.criterion.Example.create;

import java.util.List;

import org.hibernate.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.ruizton.main.dao.comm.HibernateDaoSupport;
import com.ruizton.main.model.Fregionconf; 

/**
	* A data access object (DAO) providing persistence and search support for Fuser entities.
			* Transaction control of the save(), update() and delete() operations 
	can directly support Spring container-managed transactions or they can be augmented	to handle user-managed Spring transactions. 
	Each of these methods provides additional information for how to configure it for the desired type of transaction control. 	
 * @see com.ruizton.main.model.Fuser
* @author MyEclipse Persistence Tools 
*/
@Repository
public class FregionconfDAO extends HibernateDaoSupport  {
    private static final Logger log = LoggerFactory.getLogger(FregionconfDAO.class);
    
    public void save(Fregionconf transientInstance) {
        log.debug("saving Fregionconf instance");
        try {
            getSession().save(transientInstance);
            log.debug("save successful");
        } catch (RuntimeException re) {
            log.error("save failed", re);
            throw re;
        }
    }
    
	public void delete(Fregionconf persistentInstance) {
        log.debug("deleting Fregionconf instance");
        try {
            getSession().delete(persistentInstance);
            log.debug("delete successful");
        } catch (RuntimeException re) {
            log.error("delete failed", re);
            throw re;
        }
    }
    
    public Fregionconf findById( java.lang.Integer id) {
        log.debug("getting Fregionconf instance with id: " + id);
        try {
        	Fregionconf instance = (Fregionconf) getSession()
                    .get("com.ruizton.main.model.Fregionconf", id);
            return instance;
        } catch (RuntimeException re) {
            log.error("get failed", re);
            throw re;
        }
    }
    
    
    public List<Fregionconf> findByExample(Fregionconf instance) {
        log.debug("finding Fregionconf instance by example");
        try {
            List<Fregionconf> results = (List<Fregionconf>) getSession()
                    .createCriteria("com.ruizton.main.model.Fregionconf")
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
      log.debug("finding Fregionconf instance with property: " + propertyName
            + ", value: " + value);
      try {
         String queryString = "from Fregionconf as model where model." 
         						+ propertyName + "= ?";
         Query queryObject = getSession().createQuery(queryString);
		 queryObject.setParameter(0, value);
		 return queryObject.list();
      } catch (RuntimeException re) {
         log.error("find by property name failed", re);
         throw re;
      }
	}
    
}
