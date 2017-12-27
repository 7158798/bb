package com.ruizton.main.dao;

import com.ruizton.main.dao.comm.HibernateDaoSupport;
import com.ruizton.main.model.SubRedWrapper;
import org.hibernate.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 招股金服
 * CopyRight : www.zhgtrade.com
 * Author : xuelin
 * Date： 2016/12/20
 */
@Repository
public class SubRedWrapperDao extends HibernateDaoSupport {
    private static final Logger log = LoggerFactory.getLogger(SubRedWrapperDao.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void save(SubRedWrapper transientInstance) {
        log.debug("saving SubRedWrapper instance");
        try {
            getSession().save(transientInstance);
            log.debug("save successful");
        } catch (RuntimeException re) {
            log.error("save failed", re);
            throw re;
        }
    }

    public void delete(SubRedWrapper persistentInstance) {
        log.debug("deleting SubRedWrapper instance");
        try {
            getSession().delete(persistentInstance);
            log.debug("delete successful");
        } catch (RuntimeException re) {
            log.error("delete failed", re);
            throw re;
        }
    }

    public SubRedWrapper findById(Integer id) {
        log.debug("getting SubRedWrapper instance with id: " + id);
        try {
            SubRedWrapper instance = (SubRedWrapper) getSession().get(
                    "com.ruizton.main.model.SubRedWrapper", id);
            return instance;
        } catch (RuntimeException re) {
            log.error("get failed", re);
            throw re;
        }
    }

    @SuppressWarnings("unchecked")
    public List<SubRedWrapper> findByProperty(String propertyName, Object value) {
        log.debug("finding SubRedWrapper instance with property: " + propertyName
                + ", value: " + value);
        try {
            String queryString = "from SubRedWrapper as model where model."
                    + propertyName + "= ?";
            Query queryObject = getSession().createQuery(queryString);
            queryObject.setParameter(0, value);
            return queryObject.list();
        } catch (RuntimeException re) {
            log.error("find by property name failed", re);
            throw re;
        }
    }

    public double sumAmount(int redWrapperId){
        Query query = getSession().createQuery("select sum(model.amount) from com.ruizton.main.model.SubRedWrapper as model where model.redWrapperId=:redWrapperId");
        query.setParameter("redWrapperId", redWrapperId);
        Double amount = (Double) query.uniqueResult();
        if(null == amount){
            return 0;
        }
        return amount.doubleValue();
    }
}
