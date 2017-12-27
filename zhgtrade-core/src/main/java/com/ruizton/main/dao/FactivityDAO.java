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
import com.ruizton.main.model.Factivitytype;

/**
 	* A data access object (DAO) providing persistence and search support for Factivity entities.
 			* Transaction control of the save(), update() and delete() operations 
		can directly support Spring container-managed transactions or they can be augmented	to handle user-managed Spring transactions. 
		Each of these methods provides additional information for how to configure it for the desired type of transaction control. 	
	 * @see ztmp.Factivity
  * @author MyEclipse Persistence Tools 
 */
@Repository
public class FactivityDAO extends HibernateDaoSupport  {
	     private static final Logger log = LoggerFactory.getLogger(FactivityDAO.class);
		//property constants
	public static final String FTITLE = "ftitle";
	public static final String FCONTENT = "fcontent";
	public static final String FBANNER_IMAGE = "fbannerImage";
	public static final String FSTATUS = "fstatus";
	public static final String FIS_MULTIPLE = "fisMultiple";
	public static final String FTIME_INTERVAL = "ftimeInterval";
	public static final String FREWARD_PER_COUNT = "frewardPerCount";
	public static final String FREWARD_PROMOTION_PER_COUNT = "frewardPromotionPerCount";



    
    public void save(Factivity transientInstance) {
        log.debug("saving Factivity instance");
        try {
            getSession().save(transientInstance);
            log.debug("save successful");
        } catch (RuntimeException re) {
            log.error("save failed", re);
            throw re;
        }
    }
    
	public void delete(Factivity persistentInstance) {
        log.debug("deleting Factivity instance");
        try {
            getSession().delete(persistentInstance);
            log.debug("delete successful");
        } catch (RuntimeException re) {
            log.error("delete failed", re);
            throw re;
        }
    }
    
    public Factivity findById( java.lang.Integer id) {
        log.debug("getting Factivity instance with id: " + id);
        try {
            Factivity instance = (Factivity) getSession()
                    .get("com.ruizton.main.model.Factivity", id);
            return instance;
        } catch (RuntimeException re) {
            log.error("get failed", re);
            throw re;
        }
    }
    
    
    public List<Factivity> findByExample(Factivity instance) {
        log.debug("finding Factivity instance by example");
        try {
            List<Factivity> results = (List<Factivity>) getSession()
                    .createCriteria("com.ruizton.main.model.Factivity")
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
      log.debug("finding Factivity instance with property: " + propertyName
            + ", value: " + value);
      try {
         String queryString = "from Factivity as model where model." 
         						+ propertyName + "= ?";
         Query queryObject = getSession().createQuery(queryString);
		 queryObject.setParameter(0, value);
		 return queryObject.list();
      } catch (RuntimeException re) {
         log.error("find by property name failed", re);
         throw re;
      }
	}

	public List<Factivity> findByFtitle(Object ftitle
	) {
		return findByProperty(FTITLE, ftitle
		);
	}
	
	public List<Factivity> findByFcontent(Object fcontent
	) {
		return findByProperty(FCONTENT, fcontent
		);
	}
	
	public List<Factivity> findByFbannerImage(Object fbannerImage
	) {
		return findByProperty(FBANNER_IMAGE, fbannerImage
		);
	}
	
	public List<Factivity> findByFstatus(Object fstatus
	) {
		return findByProperty(FSTATUS, fstatus
		);
	}
	
	public List<Factivity> findByFisMultiple(Object fisMultiple
	) {
		return findByProperty(FIS_MULTIPLE, fisMultiple
		);
	}
	
	public List<Factivity> findByFtimeInterval(Object ftimeInterval
	) {
		return findByProperty(FTIME_INTERVAL, ftimeInterval
		);
	}
	
	public List<Factivity> findByFrewardPerCount(Object frewardPerCount
	) {
		return findByProperty(FREWARD_PER_COUNT, frewardPerCount
		);
	}
	
	public List<Factivity> findByFrewardPromotionPerCount(Object frewardPromotionPerCount
	) {
		return findByProperty(FREWARD_PROMOTION_PER_COUNT, frewardPromotionPerCount
		);
	}
	

	public List findAll() {
		log.debug("finding all Factivity instances");
		try {
			String queryString = "from Factivity";
	         Query queryObject = getSession().createQuery(queryString);
			 return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}
	
    public Factivity merge(Factivity detachedInstance) {
        log.debug("merging Factivity instance");
        try {
            Factivity result = (Factivity) getSession()
                    .merge(detachedInstance);
            log.debug("merge successful");
            return result;
        } catch (RuntimeException re) {
            log.error("merge failed", re);
            throw re;
        }
    }

    public void attachDirty(Factivity instance) {
        log.debug("attaching dirty Factivity instance");
        try {
            getSession().saveOrUpdate(instance);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }
    
    public void attachClean(Factivity instance) {
        log.debug("attaching clean Factivity instance");
        try {
            getSession().lock(instance, LockMode.NONE);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }
    
	public List<Factivity> list(int firstResult, int maxResults, String filter,boolean isFY) {
		List<Factivity> list = null;
		log.debug("finding Factivity instance with filter");
		try {
			String queryString = "from Factivity "+filter;
			Query queryObject = getSession().createQuery(queryString);
			if(isFY){
				queryObject.setFirstResult(firstResult);
				queryObject.setMaxResults(maxResults);
			}
			list = queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by Factivity name failed", re);
			throw re;
		}
		return list;
	}
}