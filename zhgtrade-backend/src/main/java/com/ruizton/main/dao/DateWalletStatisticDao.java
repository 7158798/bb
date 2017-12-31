package com.ruizton.main.dao;

import com.ruizton.main.dao.comm.HibernateDaoSupport;
import com.ruizton.main.model.DateWalletStatistic;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * 比特家
 * CopyRight : www.btc58.cc
 * Author : xxp
 * Date： 2017/1/11
 */
@Repository
public class DateWalletStatisticDao extends HibernateDaoSupport {

    public void save(DateWalletStatistic entity){
        getSession().save(entity);
    }

    public void update(DateWalletStatistic entity){
        getSession().update(entity);
    }

    public void saveOrUpdate(DateWalletStatistic entity){
        getSession().saveOrUpdate(entity);
    }

    public List<DateWalletStatistic> findByProperty(String propertyName, Object propertyValue){
        Query query = getSession().createQuery("from com.ruizton.main.model.DateWalletStatistic where " + propertyName + "=? order by id");
        query.setParameter(0, propertyValue);

        return query.list();
    }

    public List<DateWalletStatistic> findByProperties(String[] propertyNames, Object[] propertyValues){
        StringBuilder hqlBuf = new StringBuilder("from com.ruizton.main.model.DateWalletStatistic where 1=1");

        for(String propertyName : propertyNames){
            hqlBuf.append(" and ").append(propertyName).append(" = ?");
        }
        hqlBuf.append(" order by id");

        Query query = getSession().createQuery(hqlBuf.toString());

        for(int i=0; i<propertyNames.length; i++){
            query.setParameter(i, propertyValues[i]);
        }

        return query.list();
    }

    public List<DateWalletStatistic> find(Integer coinType, Date startDate, Date endDate){
        StringBuilder hqlBuf = new StringBuilder("from com.ruizton.main.model.DateWalletStatistic where 1=1");

        if(null != coinType){
            hqlBuf.append(" and coinType = :coinType");
        }
        if(null != startDate){
            hqlBuf.append(" and date >= :startDate");
        }
        if(null != endDate){
            hqlBuf.append(" and date <= :endDate");
        }
        hqlBuf.append(" order by date");

        Query query = getSession().createQuery(hqlBuf.toString());

        if(null != coinType){
            query.setParameter("coinType", coinType);
        }
        if(null != startDate){
            query.setParameter("startDate", startDate);
        }
        if(null != endDate){
            query.setParameter("endDate", endDate);
        }

        return query.list();
    }
}














