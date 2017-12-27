package com.ruizton.main.dao;

import com.ruizton.main.dao.comm.HibernateDaoSupport;
import com.ruizton.main.model.RedWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 * 招股金服
 * CopyRight : www.zhgtrade.com
 * Author : xuelin
 * Date : 2016/12/20
 */
@Repository
public class RedWrapperDao extends HibernateDaoSupport {
    private static final Logger log = LoggerFactory.getLogger(RedWrapperDao.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void save(RedWrapper transientInstance) {
        log.debug("saving RedWrapper instance");
        try {
            getSession().save(transientInstance);
            log.debug("save successful");
        } catch (RuntimeException re) {
            log.error("save failed", re);
            throw re;
        }
    }

    public void delete(RedWrapper persistentInstance) {
        log.debug("deleting RedWrapper instance");
        try {
            getSession().delete(persistentInstance);
            log.debug("delete successful");
        } catch (RuntimeException re) {
            log.error("delete failed", re);
            throw re;
        }
    }

    public void update(RedWrapper persistentInstance) {
        log.debug("updateing RedWrapper instance");
        try {
            getSession().update(persistentInstance);
            log.debug("update successful");
        } catch (RuntimeException re) {
            log.error("update failed", re);
            throw re;
        }
    }

    public RedWrapper findById(Integer id) {
        log.debug("getting RedWrapper instance with id: " + id);
        try {
            RedWrapper instance = (RedWrapper) getSession().get(
                    "com.ruizton.main.model.RedWrapper", id);
            return instance;
        } catch (RuntimeException re) {
            log.error("get failed", re);
            throw re;
        }
    }
}
