package com.ruizton.main.dao;

import com.ruizton.main.model.BlockArticleType;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Block;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by sunpeng on 2016/7/26.
 */
@Repository
public class BlockArticleTypeDao {

    @Autowired
    private HibernateTemplate hibernateTemplate;

    public void save(BlockArticleType blockArticleType){
        hibernateTemplate.save(blockArticleType);
    }

    public int deleteById(int id){
        String sql = "delete from BlockArticleType where id = ?";
        return hibernateTemplate.execute(new HibernateCallback<Integer>() {
            @Override
            public Integer doInHibernate(Session session) throws HibernateException, SQLException {
                Query query = session.createQuery(sql);
                query.setInteger(0, id);
                return query.executeUpdate();
            }
        });
    }

    public void attachDirty(BlockArticleType blockArticleType){
        hibernateTemplate.saveOrUpdate(blockArticleType);
    }

    public BlockArticleType findById(int id){
        return hibernateTemplate.get(BlockArticleType.class, id);
    }

    public List<BlockArticleType> list(int firstResult, int maxResult, String filter, boolean isFy){
        String sql = "from BlockArticleType " + filter;
        return hibernateTemplate.execute(new HibernateCallback<List<BlockArticleType>>() {
            @Override
            public List<BlockArticleType> doInHibernate(Session session) throws HibernateException, SQLException {
                Query query = session.createQuery(sql);
                if(isFy){
                    query.setFirstResult(firstResult);
                    query.setMaxResults(maxResult);
                }
                return query.list();
            }
        });
    }

    public int count(String filter){
        String sql = "select count(*) from BlockArticleType " + filter;
        return hibernateTemplate.execute(new HibernateCallback<Integer>() {
            @Override
            public Integer doInHibernate(Session session) throws HibernateException, SQLException {
                Query query = session.createQuery(sql);
                return ((Number) query.uniqueResult()).intValue();
            }
        });
    }

    public List<BlockArticleType> findAll() {
        String sql = "from BlockArticleType order by id desc ";
        return hibernateTemplate.execute(new HibernateCallback<List<BlockArticleType>>() {
            @Override
            public List<BlockArticleType> doInHibernate(Session session) throws HibernateException, SQLException {
                Query query = session.createQuery(sql);
                query.setCacheable(true);
                return query.list();
            }
        });
    }
}
