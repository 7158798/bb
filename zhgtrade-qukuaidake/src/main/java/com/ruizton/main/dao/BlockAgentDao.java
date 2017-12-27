package com.ruizton.main.dao;

import com.ruizton.main.dao.comm.HibernateDaoSupport;
import com.ruizton.main.model.BlockAgent;
import java.util.List;
import org.hibernate.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class BlockAgentDao extends HibernateDaoSupport {
    private Logger logger = LoggerFactory.getLogger(BlockAgentDao.class);

    public void save(BlockAgent agent) {
        getSession().save(agent);
    }

    public BlockAgent findById(int id){
        return (BlockAgent) getSession().get(BlockAgent.class, id);
    }

    public void attachDirty(BlockAgent agent){
        getSession().saveOrUpdate(agent);
    }

    public List<BlockAgent> list(int firstResult, int maxResults, String filter, boolean isFY) {
        List<BlockAgent> list = null;
        logger.debug("finding BlockAgent instance with filter");
        try {
            String queryString = "from BlockAgent " + filter;
            Query queryObject = getSession().createQuery(queryString);
            if (isFY) {
                queryObject.setFirstResult(firstResult);
                queryObject.setMaxResults(maxResults);
            }
            list = queryObject.list();
        } catch (RuntimeException re) {
            logger.error("find BlockAgent by filter name failed", re);
            throw re;
        }
        return list;
    }

    public int getAllCount(String tableName, String filter){
        String queryString = "select count(*) from " + tableName+" "+filter;
        Query queryObject = getSession().createQuery(queryString);
        return Integer.parseInt(queryObject.list().get(0).toString());
    }

}
