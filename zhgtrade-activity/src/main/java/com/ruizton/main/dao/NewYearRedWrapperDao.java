package com.ruizton.main.dao;

import com.ruizton.main.dao.comm.HibernateDaoSupport;
import com.ruizton.main.model.NewYearRedWrapper;
import com.ruizton.util.CollectionUtils;
import com.ruizton.util.StringUtils;
import org.hibernate.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * 招股金服
 * CopyRight : www.zhgtrade.com
 * Author : xuelin
 * Date： 2016/12/27
 */
@Repository
public class NewYearRedWrapperDao extends HibernateDaoSupport {
    private Logger log = LoggerFactory.getLogger(NewYearRedWrapperDao.class);

    public void save(NewYearRedWrapper transientInstance) {
        log.debug("saving NewYearRedWrapper instance");
        try {
            getSession().save(transientInstance);
            log.debug("save successful");
        } catch (RuntimeException re) {
            log.error("save failed", re);
            throw re;
        }
    }

    public void delete(NewYearRedWrapper persistentInstance) {
        log.debug("deleting NewYearRedWrapper instance");
        try {
            getSession().delete(persistentInstance);
            log.debug("delete successful");
        } catch (RuntimeException re) {
            log.error("delete failed", re);
            throw re;
        }
    }

    public NewYearRedWrapper findById( java.lang.Integer id) {
        log.debug("getting NewYearRedWrapper instance with id: " + id);
        try {
            NewYearRedWrapper instance = (NewYearRedWrapper) getSession()
                    .get("com.ruizton.main.model.NewYearRedWrapper", id);
            return instance;
        } catch (RuntimeException re) {
            log.error("get failed", re);
            throw re;
        }
    }

    public void update(NewYearRedWrapper redWrapper){
        log.debug("updateing NewYearRedWrapper instance");
        try {
            getSession().update(redWrapper);
        } catch (RuntimeException re) {
            log.error("updateing failed", re);
            throw re;
        }
    }

    @SuppressWarnings("unchecked")
    public List list(String date, Short type, Integer firstResult, Integer maxResult, String orderField, String orderDirection){
        StringBuilder sqlBuf = new StringBuilder();
        sqlBuf.append("select u.floginName, u.frealName, u.ftelephone, u.femail, w.id, w.amount, w.type, w.create_time from new_year_red_wrapper w ");
        sqlBuf.append("left join fuser u on u.fid = w.user_id where 1=1");

        if(StringUtils.hasText(date)){
            sqlBuf.append(" and date_format(w.create_time, '%Y-%m-%d') = :date");
        }
        if(null != type){
            sqlBuf.append(" and w.type = :type");
        }

        sqlBuf.append(" order by ").append(orderField).append(" ").append(orderDirection);

        Query query = getSession().createSQLQuery(sqlBuf.toString());

        if(StringUtils.hasText(date)){
            query.setParameter("date", date);
        }
        if(null != type){
            query.setParameter("type", type);
        }

        if(null != maxResult && null != firstResult){
            query.setMaxResults(maxResult);
            query.setFirstResult(firstResult);
        }

        List list = query.list();

        List datas = new ArrayList<>(list.size());
        HashMap map = new HashMap();
        for(Object o : list){
            Object[] objs = (Object[]) o;
            map = (HashMap) map.clone();
            map.put("floginName", objs[0]);
            map.put("frealName",  objs[1]);
            map.put("ftelephone", objs[2]);
            map.put("femail",     objs[3]);
            map.put("id",         objs[4]);
            map.put("amount",     objs[5]);
            map.put("type",       objs[6]);
            map.put("createTime",objs[7]);
            datas.add(map);
        }

        return datas;
    }

    public int count(String date, Short type){
        StringBuilder sqlBuf = new StringBuilder();
        sqlBuf.append("select count(id) from new_year_red_wrapper where 1=1");

        if(StringUtils.hasText(date)){
            sqlBuf.append(" and date_format(create_time, '%Y-%m-%d') = :date");
        }
        if(null != type){
            sqlBuf.append(" and type = :type");
        }

        Query query = getSession().createSQLQuery(sqlBuf.toString());

        if(StringUtils.hasText(date)){
            query.setParameter("date", date);
        }
        if(null != type){
            query.setParameter("type", type);
        }

        return ((BigInteger)query.uniqueResult()).intValue();
    }

    public List<NewYearRedWrapper> findByUser(int userId, int size){
        Query query = getSession().createQuery("from com.ruizton.main.model.NewYearRedWrapper where userId = :userId order by id desc");
        query.setParameter("userId", userId);
        query.setMaxResults(size);
        return query.list();
    }

    public List findRank(int size){
        String sql = "select u.floginName as username, sum(r.amount) as amount from new_year_red_wrapper as r left join fuser as u on u.fid=r.user_id group by r.user_id order by amount desc";
        Query query = getSession().createSQLQuery(sql);
        query.setMaxResults(size);

        List list = query.list();
        if(CollectionUtils.isEmpty(list)){
            return Collections.emptyList();
        }

        List data = new ArrayList<>(list.size());
        HashMap map = null;
        for(Object obj : list){
            Object[] objArr = (Object[]) obj;
            if(null == map){
                map = new HashMap();
            }else if(!map.isEmpty()){
                map = (HashMap) map.clone();
            }
            map.put("username", objArr[0]);
            map.put("amount", objArr[1]);
            data.add(map);
        }
        return data;
    }
}
