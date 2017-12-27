package com.ruizton.main.dao;

// default package

import static org.hibernate.criterion.Example.create;

import java.util.List;

import org.hibernate.LockMode;
import org.hibernate.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.ruizton.main.Enum.LendEntrustLogStatusEnum;
import com.ruizton.main.dao.comm.HibernateDaoSupport;
import com.ruizton.main.model.Flendentrustlog;
import com.ruizton.main.model.Flendrule;

/**
 * A data access object (DAO) providing persistence and search support for
 * Flendentrustlog entities. Transaction control of the save(), update() and
 * delete() operations can directly support Spring container-managed
 * transactions or they can be augmented to handle user-managed Spring
 * transactions. Each of these methods provides additional information for how
 * to configure it for the desired type of transaction control.
 * 
 * @see .Flendentrustlog
 * @author MyEclipse Persistence Tools
 */
@Repository
public class FlendentrustlogDAO extends HibernateDaoSupport {
	private static final Logger log = LoggerFactory
			.getLogger(FlendentrustlogDAO.class);
	// property constants
	public static final String FUSER = "fuser";
	public static final String FAMOUNT = "famount";
	public static final String FRETURN_AMOUNT = "freturnAmount";
	public static final String FTYPE = "ftype";
	public static final String FSTATUS = "fstatus";

	public void save(Flendentrustlog transientInstance) {
		log.debug("saving Flendentrustlog instance");
		try {
			getSession().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(Flendentrustlog persistentInstance) {
		log.debug("deleting Flendentrustlog instance");
		try {
			getSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public Flendentrustlog findById(java.lang.Integer id) {
		log.debug("getting Flendentrustlog instance with id: " + id);
		try {
			Flendentrustlog instance = (Flendentrustlog) getSession().get(
					"com.ruizton.main.model.Flendentrustlog", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List<Flendentrustlog> findByExample(Flendentrustlog instance) {
		log.debug("finding Flendentrustlog instance by example");
		try {
			List<Flendentrustlog> results = (List<Flendentrustlog>) getSession()
					.createCriteria("com.ruizton.main.model.Flendentrustlog").add(create(instance))
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
		log.debug("finding Flendentrustlog instance with property: "
				+ propertyName + ", value: " + value);
		try {
			String queryString = "from Flendentrustlog as model where model."
					+ propertyName + "= ?";
			Query queryObject = getSession().createQuery(queryString);
			queryObject.setParameter(0, value);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List<Flendentrustlog> findByFuser(Object fuser) {
		return findByProperty(FUSER, fuser);
	}

	public List<Flendentrustlog> findByFamount(Object famount) {
		return findByProperty(FAMOUNT, famount);
	}

	public List<Flendentrustlog> findByFreturnAmount(Object freturnAmount) {
		return findByProperty(FRETURN_AMOUNT, freturnAmount);
	}

	public List<Flendentrustlog> findByFtype(Object ftype) {
		return findByProperty(FTYPE, ftype);
	}

	public List<Flendentrustlog> findByFstatus(Object fstatus) {
		return findByProperty(FSTATUS, fstatus);
	}

	public List findAll() {
		log.debug("finding all Flendentrustlog instances");
		try {
			String queryString = "from Flendentrustlog";
			Query queryObject = getSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public Flendentrustlog merge(Flendentrustlog detachedInstance) {
		log.debug("merging Flendentrustlog instance");
		try {
			Flendentrustlog result = (Flendentrustlog) getSession().merge(
					detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(Flendentrustlog instance) {
		log.debug("attaching dirty Flendentrustlog instance");
		try {
			getSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(Flendentrustlog instance) {
		log.debug("attaching clean Flendentrustlog instance");
		try {
			getSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}
	
	public List<Flendentrustlog> list(int firstResult, int maxResults, String filter,boolean isFY) {
		List<Flendentrustlog> list = null;
		log.debug("finding Flendentrustlog instance with filter");
		try {
			String queryString = "from Flendentrustlog "+filter;
			Query queryObject = getSession().createQuery(queryString);
			queryObject.setCacheable(true);
			if(isFY){
				queryObject.setFirstResult(firstResult);
				queryObject.setMaxResults(maxResults);
			}
			list = queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by Flendentrustlog name failed", re);
			throw re;
		}
		return list;
	}
	
	public double getTotalBorrowAmtByParent(int parentId) {
		double amt = 0d;
		StringBuffer sf = new StringBuffer();
		sf.append("select SUM(famount-fReturnAmount+ftotalfees) amt from flendentrustlog \n");
        sf.append("where fLendEntrustBorrowId="+parentId+" and fstatus in("+LendEntrustLogStatusEnum.NOT_RETURN+","+LendEntrustLogStatusEnum.PART_RETURN+") \n");
        Query queryObject = getSession().createSQLQuery(sf.toString());
        List list = queryObject.list();
        if(list != null && list.get(0) != null){
        	amt = Double.valueOf(list.get(0).toString());
        }
        return amt;
	}
	 
}