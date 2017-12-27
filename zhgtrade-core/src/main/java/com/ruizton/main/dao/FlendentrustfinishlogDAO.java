package com.ruizton.main.dao;

// default package

import static org.hibernate.criterion.Example.create;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.LockMode;
import org.hibernate.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.ruizton.main.Enum.LendEntrustLogStatusEnum;
import com.ruizton.main.Enum.LendEntrustStatus2Enum;
import com.ruizton.main.dao.comm.HibernateDaoSupport;
import com.ruizton.main.model.Flendentrust;
import com.ruizton.main.model.Flendentrustfinishlog;
import com.ruizton.main.model.Fuser;

/**
 * A data access object (DAO) providing persistence and search support for
 * Flendentrustfinishlog entities. Transaction control of the save(), update()
 * and delete() operations can directly support Spring container-managed
 * transactions or they can be augmented to handle user-managed Spring
 * transactions. Each of these methods provides additional information for how
 * to configure it for the desired type of transaction control.
 * 
 * @see .Flendentrustfinishlog
 * @author MyEclipse Persistence Tools
 */
@Repository
public class FlendentrustfinishlogDAO extends HibernateDaoSupport {
	private static final Logger log = LoggerFactory
			.getLogger(FlendentrustfinishlogDAO.class);
	// property constants
	public static final String FAMOUNT = "famount";
	public static final String FTYPE = "ftype";
	public static final String FSTATUS = "fstatus";

	public void save(Flendentrustfinishlog transientInstance) {
		log.debug("saving Flendentrustfinishlog instance");
		try {
			getSession().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(Flendentrustfinishlog persistentInstance) {
		log.debug("deleting Flendentrustfinishlog instance");
		try {
			getSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public Flendentrustfinishlog findById(java.lang.Integer id) {
		log.debug("getting Flendentrustfinishlog instance with id: " + id);
		try {
			Flendentrustfinishlog instance = (Flendentrustfinishlog) getSession()
					.get("com.ruizton.main.model.Flendentrustfinishlog", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List<Flendentrustfinishlog> findByExample(
			Flendentrustfinishlog instance) {
		log.debug("finding Flendentrustfinishlog instance by example");
		try {
			List<Flendentrustfinishlog> results = (List<Flendentrustfinishlog>) getSession()
					.createCriteria("com.ruizton.main.model.Flendentrustfinishlog")
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
		log.debug("finding Flendentrustfinishlog instance with property: "
				+ propertyName + ", value: " + value);
		try {
			String queryString = "from Flendentrustfinishlog as model where model."
					+ propertyName + "= ?";
			Query queryObject = getSession().createQuery(queryString);
			queryObject.setParameter(0, value);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List<Flendentrustfinishlog> findByFamount(Object famount) {
		return findByProperty(FAMOUNT, famount);
	}

	public List<Flendentrustfinishlog> findByFtype(Object ftype) {
		return findByProperty(FTYPE, ftype);
	}

	public List<Flendentrustfinishlog> findByFstatus(Object fstatus) {
		return findByProperty(FSTATUS, fstatus);
	}

	public List findAll() {
		log.debug("finding all Flendentrustfinishlog instances");
		try {
			String queryString = "from Flendentrustfinishlog";
			Query queryObject = getSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public Flendentrustfinishlog merge(Flendentrustfinishlog detachedInstance) {
		log.debug("merging Flendentrustfinishlog instance");
		try {
			Flendentrustfinishlog result = (Flendentrustfinishlog) getSession()
					.merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(Flendentrustfinishlog instance) {
		log.debug("attaching dirty Flendentrustfinishlog instance");
		try {
			getSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(Flendentrustfinishlog instance) {
		log.debug("attaching clean Flendentrustfinishlog instance");
		try {
			getSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}
	
	public List<Flendentrustfinishlog> list(int firstResult, int maxResults, String filter,boolean isFY) {
		List<Flendentrustfinishlog> list = null;
		log.debug("finding Flendentrustfinishlog instance with filter");
		try {
			String queryString = "from Flendentrustfinishlog "+filter;
			Query queryObject = getSession().createQuery(queryString);
			queryObject.setCacheable(true);
			if(isFY){
				queryObject.setFirstResult(firstResult);
				queryObject.setMaxResults(maxResults);
			}
			list = queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by Flendentrustfinishlog name failed", re);
			throw re;
		}
		return list;
	}
	
	/*
	 * 获取某笔放款记录还有多少钱未还
	 * **/
	public double getTotalNoReturn(int lendId) {
		double amt = 0d;
		String sql = "select SUM(famount-fReturnAmount) from flendentrustlog where fLendEntrustLendId="+lendId;
		Query queryObject = getSession().createSQLQuery(sql);
		List list = queryObject.list();
		if(list != null && list.size() >0 && list.get(0) != null){
			amt = Double.valueOf(list.get(0).toString());
		}
		return amt;
	}
	
	public List getAllBorrowUsers() {
		StringBuffer sf = new StringBuffer();
		sf.append("select DISTINCT b.fuserid from flendentrustlog a \n");
		sf.append("LEFT OUTER JOIN flendentrust b on a.fLendEntrustBorrowId = b.fid \n");
		sf.append("where a.fstatus in("+LendEntrustLogStatusEnum.NOT_RETURN+","+LendEntrustLogStatusEnum.PART_RETURN+") \n");
		Query queryObject = getSession().createSQLQuery(sf.toString());
		List list = queryObject.list();
		return list;
	}
	
	public List getAllBorrowUsers(Fuser fuser,int cnyOrCoin) {
		StringBuffer sf = new StringBuffer();
		sf.append("select DISTINCT a.fid from flendentrustlog a \n");
		sf.append("LEFT OUTER JOIN flendentrust b on a.fLendEntrustBorrowId = b.fid \n");
		sf.append("where a.fstatus in("+LendEntrustLogStatusEnum.NOT_RETURN+","+LendEntrustLogStatusEnum.PART_RETURN+") \n");
		sf.append("and b.fcnyOrCoin="+cnyOrCoin+" \n");
		sf.append("and b.fuserid="+fuser.getFid()+" \n");
		Query queryObject = getSession().createSQLQuery(sf.toString());
		List list = queryObject.list();
		return list;
	}
	
}













