package com.ruizton.main.dao;

import com.ruizton.main.dao.comm.HibernateDaoSupport;
import com.ruizton.main.model.Market;
import org.hibernate.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;

/**
 * 比特家
 * CopyRight : www.zhgtrade.com
 * Author : 林超（362228416@qq.com）
 * Date： 2017-09-26 17:00
 */
@Repository
public class MarketDAO extends HibernateDaoSupport {

    private static final Logger log = LoggerFactory.getLogger(MarketDAO.class);

    public void save(Market transientInstance) {
        log.debug("saving Market instance");
        try {
            getSession().save(transientInstance);
            log.debug("save successful");
        } catch (RuntimeException re) {
            log.error("save failed", re);
            throw re;
        }
    }

    public void delete(Market persistentInstance) {
        log.debug("deleting Market instance");
        try {
            getSession().delete(persistentInstance);
            log.debug("delete successful");
        } catch (RuntimeException re) {
            log.error("delete failed", re);
            throw re;
        }
    }

    public Market findById(Integer id) {
        log.debug("getting Market instance with id: " + id);
        try {
            Market instance = (Market) getHibernateTemplate().get(
                    Market.class, id);
            return instance;
        } catch (RuntimeException re) {
            log.error("get failed", re);
            throw re;
        }
    }

    public List findAll() {
        log.debug("finding all Market instances");
        try {
            String queryString = "from Market";
            Query queryObject = getSession().createQuery(queryString);
            return queryObject.list();
        } catch (RuntimeException re) {
            log.error("find all failed", re);
            throw re;
        }
    }

    public void update(Market entity) {
        log.debug("update Market instances");
        try {
            getSession().update(entity);
        } catch (RuntimeException re) {
            log.error("find all failed", re);
            throw re;
        }
    }

    public int count(String filter) {
        Query query = getSession().createQuery("select count(*) from Market as model " + (Objects.nonNull(filter) ? filter : ""));
        return ((Long)(query.uniqueResult())).intValue();
    }

    public List<Market> list(int firstResult, int maxResults,
                                       String filter,boolean isFY) {
        List<Market> list = null;
        log.debug("finding Market instance with filter");
        try {
            String queryString = "from Market as model "+filter + ")";
            Query queryObject = getSession().createQuery(queryString);
            if(isFY){
                queryObject.setFirstResult(firstResult);
                queryObject.setMaxResults(maxResults);
            }
            list = queryObject.list();
        } catch (RuntimeException re) {
            log.error("find Fvirtualcointype by filter name failed", re);
            throw re;
        }
        return list;
    }
}
