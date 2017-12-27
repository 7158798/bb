package com.ruizton.main.dao;

import com.ruizton.main.dao.comm.HibernateDaoSupport;
import com.ruizton.main.model.FTypeDetail;
import com.ruizton.util.CollectionUtils;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * DESC:
 * <p/>
 * 招股金服
 * CopyRight : www.zhgtrade.com
 * Author : xuelin(1186270005@qq.com)
 * Date： 2016-05-18 11:03
 */
@Repository
public class FvirtualCoinTypeDetailDAO extends HibernateDaoSupport {
    private static final Logger log = LoggerFactory.getLogger(FvirtualcointypeDAO.class);

    public void save(FTypeDetail transientInstance) {
        log.debug("saving FTypeDetail instance");
        try {
            getSession().save(transientInstance);
            log.debug("save successful");
        } catch (RuntimeException re) {
            log.error("save failed", re);
            throw re;
        }
    }

    public void delete(FTypeDetail persistentInstance) {
        log.debug("deleting FTypeDetail instance");
        try {
            getSession().delete(persistentInstance);
            log.debug("delete successful");
        } catch (RuntimeException re) {
            log.error("delete failed", re);
            throw re;
        }
    }

    public FTypeDetail findById(java.lang.Integer id) {
        log.debug("getting FTypeDetail instance with id: " + id);
        try {
            FTypeDetail instance = (FTypeDetail) getHibernateTemplate().get(
                    "com.ruizton.main.model.FTypeDetail", id);
            return instance;
        } catch (RuntimeException re) {
            log.error("get failed", re);
            throw re;
        }
    }

    public FTypeDetail findByFvid(java.lang.Integer fVid) {
        log.debug("getting FTypeDetail instance with fVid: " + fVid);
        try {
            List list = getSession().createCriteria(FTypeDetail.class).add(Restrictions.eq("fviFid", fVid)).list();
            if(CollectionUtils.isEmpty(list)){
                return null;
            }
            return (FTypeDetail) list.get(0);
        } catch (RuntimeException re) {
            log.error("get failed", re);
            throw re;
        }
    }

    public List<FTypeDetail> findAll() {
        log.debug("getting all FTypeDetail instance");
        try {
           return getSession().createQuery("from com.ruizton.main.model.FTypeDetail where 1=1").list();
        } catch (RuntimeException re) {
            log.error("get failed", re);
            throw re;
        }
    }

    public FTypeDetail merge(FTypeDetail detachedInstance) {
        log.debug("merging FTypeDetail instance");
        try {
            FTypeDetail result = (FTypeDetail) getSession().merge(
                    detachedInstance);
            log.debug("merge successful");
            return result;
        } catch (RuntimeException re) {
            log.error("merge failed", re);
            throw re;
        }
    }

    public void attachDirty(FTypeDetail instance) {
        log.debug("attaching dirty FTypeDetail instance");
        try {
            getSession().saveOrUpdate(instance);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }
}
