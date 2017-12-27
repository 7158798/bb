package com.ruizton.main.dao;

/*import com.ruizton.main.Enum.AwardOrDeductTicketOriginEnum;*/
import com.ruizton.main.Enum.CapitalOperationOutStatus;
import com.ruizton.main.Enum.CapitalOperationTypeEnum;
import com.ruizton.main.Enum.ChargePayTypeEnum;
import com.ruizton.main.dao.comm.HibernateDaoSupport;
import com.ruizton.main.model.Fcapitaloperation;
import com.ruizton.main.model.Fuser;
import com.ruizton.util.StringUtils;
import com.ruizton.util.Utils;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hibernate.criterion.Example.create;

/**
 	* A data access object (DAO) providing persistence and search support for Fcapitaloperation entities.
 			* Transaction control of the save(), update() and delete() operations 
		can directly support Spring container-managed transactions or they can be augmented	to handle user-managed Spring transactions. 
		Each of these methods provides additional information for how to configure it for the desired type of transaction control. 	
	 * @see ztmp.Fcapitaloperation
  * @author MyEclipse Persistence Tools 
 */

@Repository
public class FcapitaloperationDAO extends HibernateDaoSupport  {
	     private static final Logger log = LoggerFactory.getLogger(FcapitaloperationDAO.class);
		//property constants
	public static final String FAMOUNT = "famount";
	public static final String FTYPE = "ftype";
	public static final String FSTATUS = "fstatus";
	public static final String FREMITTANCE_TYPE = "fremittanceType";
	public static final String FREMARK = "fremark";



    
    public void save(Fcapitaloperation transientInstance) {
        log.debug("saving Fcapitaloperation instance");
        try {
            getSession().save(transientInstance);
            log.debug("save successful");
        } catch (RuntimeException re) {
            log.error("save failed", re);
            throw re;
        }
    }
    
	public void delete(Fcapitaloperation persistentInstance) {
        log.debug("deleting Fcapitaloperation instance");
        try {
            getSession().delete(persistentInstance);
            log.debug("delete successful");
        } catch (RuntimeException re) {
            log.error("delete failed", re);
            throw re;
        }
    }
    
    public Fcapitaloperation findById( java.lang.Integer id) {
        log.debug("getting Fcapitaloperation instance with id: " + id);
        try {
            Fcapitaloperation instance = (Fcapitaloperation) getSession()
                    .get("com.ruizton.main.model.Fcapitaloperation", id);
            return instance;
        } catch (RuntimeException re) {
            log.error("get failed", re);
            throw re;
        }
    }
    
    
    public List<Fcapitaloperation> findByExample(Fcapitaloperation instance) {
        log.debug("finding Fcapitaloperation instance by example");
        try {
            List<Fcapitaloperation> results = (List<Fcapitaloperation>) getSession()
                    .createCriteria("com.ruizton.main.model.Fcapitaloperation")
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
      log.debug("finding Fcapitaloperation instance with property: " + propertyName
            + ", value: " + value);
      try {
         String queryString = "from Fcapitaloperation as model where model." 
         						+ propertyName + "= ?";
         Query queryObject = getSession().createQuery(queryString);
		 queryObject.setParameter(0, value);
		 return queryObject.list();
      } catch (RuntimeException re) {
         log.error("find by property name failed", re);
         throw re;
      }
	}

    public List findByProperties(String[] propertyNames, Object[] values) {
		StringBuilder hqlBuf = new StringBuilder("from Fcapitaloperation as model where 1=1");

		for(String propertyName : propertyNames){
			hqlBuf.append(" and ").append(propertyName).append(" = ?");
		}
		hqlBuf.append(" order by id desc");

		Query query = getSession().createQuery(hqlBuf.toString());

		for(int i=0; i<propertyNames.length; i++){
			query.setParameter(i, values[i]);
		}

		return query.list();
	}

	public List<Fcapitaloperation> findByFamount(Object famount
	) {
		return findByProperty(FAMOUNT, famount
		);
	}
	
	public List<Fcapitaloperation> findByFtype(Object ftype
	) {
		return findByProperty(FTYPE, ftype
		);
	}
	
	public List<Fcapitaloperation> findByFstatus(Object fstatus
	) {
		return findByProperty(FSTATUS, fstatus
		);
	}
	
	public List<Fcapitaloperation> findByFremittanceType(Object fremittanceType
	) {
		return findByProperty(FREMITTANCE_TYPE, fremittanceType
		);
	}
	
	public List<Fcapitaloperation> findByFremark(Object fremark
	) {
		return findByProperty(FREMARK, fremark
		);
	}
	

	public List findAll() {
		log.debug("finding all Fcapitaloperation instances");
		try {
			String queryString = "from Fcapitaloperation";
	         Query queryObject = getSession().createQuery(queryString);
			 return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}
	
    public Fcapitaloperation merge(Fcapitaloperation detachedInstance) {
        log.debug("merging Fcapitaloperation instance");
        try {
            Fcapitaloperation result = (Fcapitaloperation) getSession()
                    .merge(detachedInstance);
            log.debug("merge successful");
            return result;
        } catch (RuntimeException re) {
            log.error("merge failed", re);
            throw re;
        }
    }

    public void attachDirty(Fcapitaloperation instance) {
        log.debug("attaching dirty Fcapitaloperation instance");
        try {
            getSession().saveOrUpdate(instance);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }
    
    public void attachClean(Fcapitaloperation instance) {
        log.debug("attaching clean Fcapitaloperation instance");
        try {
            getSession().lock(instance, LockMode.NONE);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }
    
    public List findByParam(int cur_page,int max_page,Map<String, Object> param,String order) {
		log.debug("finding Fcapitaloperation findByParam");
		try {
			StringBuffer queryString = new StringBuffer("from Fcapitaloperation where ");
			
			Object[] keys = null ;
			if(param!=null){
				keys = param.keySet().toArray() ;
				for(int i=0;i<keys.length;i++){
					queryString.append(keys[i]+"=? and ") ;
				}
			}
			queryString.append(" 1=1 ") ;
			if(order!=null){
				queryString.append(" order by "+order) ;
			}
			
			Query queryObject = getSession().createQuery(queryString.toString());
			if(keys!=null){
				for(int i=0;i<keys.length;i++){
					queryObject.setParameter(i, param.get(keys[i])) ;
				}
			}
			queryObject.setMaxResults(max_page) ;
			queryObject.setFirstResult((cur_page-1)*max_page) ;
			 return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}
    
    public int findByParamCount(Map<String, Object> param) {
		log.debug("finding Fcapitaloperation findByParam");
		try {
			StringBuffer queryString = new StringBuffer("select count(f.fid) from Fcapitaloperation f where ");
			
			Object[] keys = null ;
			if(param!=null){
				keys = param.keySet().toArray() ;
				for(int i=0;i<keys.length;i++){
					queryString.append(keys[i]+"=? and ") ;
				}
			}
			
			queryString.append(" 1=1 ") ;
			
			Query queryObject = getSession().createQuery(queryString.toString());
			if(keys!=null){
				for(int i=0;i<keys.length;i++){
					queryObject.setParameter(i, param.get(keys[i])) ;
				}
			}
			
			List listResult =queryObject.list();
			if(listResult==null||listResult.size()==0){
				return 0 ;
			}else{
				return (int)((Long)listResult.get(0)).longValue() ;
			}
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}
    
	public List<Fcapitaloperation> list(int firstResult, int maxResults,
			String filter,boolean isFY) {
		List<Fcapitaloperation> list = null;
		log.debug("finding Fcapitaloperation instance with filter");
		try {
			String queryString = "from Fcapitaloperation "+filter;
			Query queryObject = getSession().createQuery(queryString);
			if(isFY){
				queryObject.setFirstResult(firstResult);
				queryObject.setMaxResults(maxResults);
			}
			list = queryObject.list();
		} catch (RuntimeException re) {
			log.error("find Fcapitaloperation by filter name failed", re);
			throw re;
		}
		return list;
	}
	
	public boolean updateCapital(String capitalSelectSQL,String capitalUpdateSQL) throws Exception {
		Query queryObject = getSession().createSQLQuery(capitalSelectSQL);
		if(queryObject.list().size() != 1){
			throw new Exception("数据被修改");
		}
		SQLQuery sQLQuery=getSession().createSQLQuery(capitalUpdateSQL);
		sQLQuery.executeUpdate();
		return true;
	}
	
	public boolean updateWallet(String walletSelectSQL,String walletUpdateSQL) throws Exception {
		Query queryObject = getSession().createSQLQuery(walletSelectSQL);
		if(queryObject.list().size() != 1){
			throw new Exception("数据被修改");
		}
		SQLQuery sQLQuery=getSession().createSQLQuery(walletUpdateSQL);
		sQLQuery.executeUpdate();
		return true;
	}
	
	
	public Map getTotalAmount(int type,String fstatus,boolean isToday) {
		Map map = new HashMap();
		StringBuffer sql = new StringBuffer();
		sql.append("select sum(famount) totalAmount from Fcapitaloperation where fType="+type+" and fstatus in"+fstatus+" \n");
		if(isToday){
			sql.append("and DATE_FORMAT(fLastUpdateTime,'%Y-%c-%d') = DATE_FORMAT(now(),'%Y-%c-%d') \n");
		}
		SQLQuery queryObject = getSession().createSQLQuery(sql.toString());
		List allList = queryObject.list();
		if(allList != null && allList.size() >0){
			map.put("totalAmount",allList.get(0));
		}
		return map;
	}
	
	public List getTotalGroup(String filter) {
		Map map = new HashMap();
		StringBuffer sql = new StringBuffer();
		sql.append("select round(sum(famount),0) totalAmount,DATE_FORMAT(fLastUpdateTime,'%Y-%c-%d') fcreateTime from Fcapitaloperation \n");
		sql.append(filter);
		sql.append("group by DATE_FORMAT(fLastUpdateTime,'%Y-%c-%d') \n");
		SQLQuery queryObject = getSession().createSQLQuery(sql.toString());
		return queryObject.list();
	}
			
	public int getTodayCnyWithdrawTimes(Fuser fuser){
		log.debug("getTodayCnyWithdrawTimes all Fcapitaloperation instances");
		try {
			String queryString = 
					"select count(*) from Fcapitaloperation where fuser.fid=? " +
					"and fcreateTime>? " +
					"and ftype=? " +
					"and fstatus!=?";
	         Query queryObject = getSession().createQuery(queryString);
	         queryObject.setParameter(0, fuser.getFid()) ;
	         queryObject.setParameter(1, new Date(Utils.getTimesmorning())) ;
	         queryObject.setParameter(2, CapitalOperationTypeEnum.RMB_OUT) ;
	         queryObject.setParameter(3, CapitalOperationOutStatus.Cancel) ;
			 return Integer.parseInt(queryObject.list().get(0).toString());
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}
	
	public List getTotalAmountForReport(String filter) {
		StringBuffer sql = new StringBuffer();
		sql.append("select sum(famount) totalAmount from Fcapitaloperation \n");
		sql.append(filter +" \n");
		SQLQuery queryObject = getSession().createSQLQuery(sql.toString());
		return queryObject.list();
	}
	
	public List getTotalOperationlog(String filter) {
		StringBuffer sql = new StringBuffer();
		sql.append("select sum(famount) totalAmount from Foperationlog \n");
		sql.append(filter +" \n");
		SQLQuery queryObject = getSession().createSQLQuery(sql.toString());
		return queryObject.list();
	}

	/**
	 * 列出活动时间内，成功充值的记录
	 * @return
     */
	/*public List<Fcapitaloperation> listForAwarding(Timestamp start, Timestamp end){
		String sql = "from Fcapitaloperation fc where ftype = ? AND fstatus = ? AND fcreateTime BETWEEN ? AND ? AND fc.fuser.fid not in (select log.ticket.fuser.fid from Ticketlog log where origin between ? and ?)";
		Query query = getSession().createQuery(sql);
		query.setCacheable(false);
		query.setInteger(0, 1);
		query.setInteger(1, 3);
		query.setTimestamp(2, start);
		query.setTimestamp(3, end);
		query.setInteger(4, AwardOrDeductTicketOriginEnum.ChargeLevel1);
		query.setInteger(5, AwardOrDeductTicketOriginEnum.ChargeLevel5);
		return query.list();
	}*/

	/**
	 * 过滤三天充值未完成
	 * @param cur_page
	 * @param max_page
	 * @param param
	 * @param order
     * @return
     */
	public List listByParam(int cur_page,int max_page,Map<String, Object> param,String order) {
		log.debug("finding Fcapitaloperation findByParam");
		try {
			StringBuffer queryString = new StringBuffer("from Fcapitaloperation where ");

			Object[] keys = null ;
			if(param!=null){
				keys = param.keySet().toArray() ;
				for(int i=0;i<keys.length;i++){
					queryString.append(keys[i]+"=? and ") ;
				}
			}
			queryString.append(" 1=1 ") ;
			Timestamp threeDayBefore = new Timestamp(Utils.getDateBefore(-3).getTime());
			queryString.append("and (fstatus = 3 or (fstatus = 2 and fcreateTime > '"+threeDayBefore+"'))");
			if(order!=null){
				queryString.append(" order by "+order) ;
			}

			Query queryObject = getSession().createQuery(queryString.toString());
			if(keys!=null){
				for(int i=0;i<keys.length;i++){
					queryObject.setParameter(i, param.get(keys[i])) ;
				}
			}
			queryObject.setMaxResults(max_page) ;
			queryObject.setFirstResult((cur_page-1)*max_page) ;
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	/**
	 * 过滤三天充值未完成
	 * @param param
	 * @return
     */
	public int countByParamCount(Map<String, Object> param) {
		log.debug("finding Fcapitaloperation findByParam");
		try {
			StringBuffer queryString = new StringBuffer("select count(f.fid) from Fcapitaloperation f where ");

			Object[] keys = null ;
			if(param!=null){
				keys = param.keySet().toArray() ;
				for(int i=0;i<keys.length;i++){
					queryString.append(keys[i]+"=? and ") ;
				}
			}

			queryString.append(" 1=1 ") ;

			//获得3天前的时间戳
			Timestamp threeDayBefore = new Timestamp(Utils.getDateBefore(-3).getTime());
			queryString.append("and (fstatus = 3 or (fstatus != 3 and fLastUpdateTime > '"+threeDayBefore+"'))");
			Query queryObject = getSession().createQuery(queryString.toString());
			if(keys!=null){
				for(int i=0;i<keys.length;i++){
					queryObject.setParameter(i, param.get(keys[i])) ;
				}
			}

			List listResult =queryObject.list();
			if(listResult==null||listResult.size()==0){
				return 0 ;
			}else{
				return (int)((Long)listResult.get(0)).longValue() ;
			}
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public Object[] capitalInReport(Date startDate, Date endDate, ChargePayTypeEnum payType, String bank){
		StringBuilder hqlBuf = new StringBuilder("select date_format(fLastUpdateTime, '%Y-%m-%d') as date, sum(famount) from com.ruizton.main.model.Fcapitaloperation where 1=1");

		if(null != startDate){
			hqlBuf.append(" and fLastUpdateTime>=:startDate");
		}
		if(null != endDate){
			hqlBuf.append(" and fLastUpdateTime<=:endDate");
		}
		if(null != payType){
			hqlBuf.append(" and payType=:payType");
		}
		if(StringUtils.hasText(bank)){
			hqlBuf.append(" and fBank like :bank");
		}
		hqlBuf.append(" and ftype=1 and fstatus=3 group by date_format(fLastUpdateTime, '%Y-%m-%d') order by fLastUpdateTime");

		Query query = getSession().createQuery(hqlBuf.toString());

		if(null != startDate){
			query.setParameter("startDate", startDate);
		}
		if(null != endDate){
			query.setParameter("endDate", endDate);
		}
		if(null != payType){
			query.setParameter("payType", payType);
		}
		if(StringUtils.hasText(bank)){
			query.setParameter("bank", "%" + bank + "%");
		}

		List list = query.list();

		List dates = new ArrayList<>(list.size());
		List datas = new ArrayList<>(list.size());

		list.forEach(e -> {
			Object[] line = (Object[]) e;
			dates.add(line[0]);
			datas.add(line[1]);
		});

		return new Object[]{dates, datas};
	}


	/**
	 * 列出时间范围内提现的手续费
	 * @param startDate
	 * @param endDate
	 * @param type 1充值，2提现
	 * @return
	 */
	public List<String[]> listForWithdrawFeeReport(Date startDate, Date endDate, int type, int status){
		List<String[]> result =  new ArrayList<String[]>();
		String filter = "select DATE_FORMAT(fLastUpdateTime, '%m-%d') as date, sum(ffees) as amount from fcapitaloperation where fLastUpdateTime >= ? and fLastUpdateTime <= ? and fType= ? AND fStatus = ? group by DATE_FORMAT(fLastUpdateTime, '%y-%m-%d') order by fLastUpdateTime";
		Query query = getSession().createSQLQuery(filter);
		query.setDate(0, startDate);
		query.setDate(1, endDate);
		query.setInteger(2, type);
		query.setInteger(3, status);
		List list = query.list();
		list.forEach(rows -> {
			Object[] objects = (Object[]) rows;
			result.add(new String[]{String.valueOf(objects[0]), String.valueOf(objects[1])});
		});
		return result;
	}
}
