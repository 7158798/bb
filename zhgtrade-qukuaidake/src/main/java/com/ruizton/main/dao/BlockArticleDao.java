package com.ruizton.main.dao;

import com.ruizton.main.model.BlockArticle;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by sunpeng on 2016/7/26.
 */
@Repository
public class BlockArticleDao{

    @Autowired
    private HibernateTemplate hibernateTemplate;

    public BlockArticle findById(int id){
       return hibernateTemplate.get(BlockArticle.class, id);
    }

    public void save(BlockArticle blockArticle){
        hibernateTemplate.save(blockArticle);
    }

    public int deleteById(int id){
        String sql = "delete from BlockArticle where id = ?";
        return hibernateTemplate.execute(new HibernateCallback<Integer>() {
            @Override
            public Integer doInHibernate(Session session) throws HibernateException, SQLException {
                Query query = session.createQuery(sql);
                query.setInteger(0, id);
                return query.executeUpdate();
            }
        });
    }

    public void attachDirty(BlockArticle blockArticle){
        hibernateTemplate.saveOrUpdate(blockArticle);
    }

    /**
     *  按类型，并且按时间排序
     * @param typeId
     * @param firstResult
     * @param maxResults
     * @return
     */
    public List<BlockArticle> listByTypeId(int typeId, int firstResult, int maxResults){
        String sql = "from BlockArticle where blockArticleType.id = ? order by lastUpdateTime desc ";
        return hibernateTemplate.execute(new HibernateCallback<List<BlockArticle>>() {
            @Override
            public List<BlockArticle> doInHibernate(Session session) throws HibernateException, SQLException {
                Query query = session.createQuery(sql);
                query.setCacheable(true);
                query.setInteger(0, typeId);
                query.setFirstResult(firstResult);
                query.setMaxResults(maxResults);
                return query.list();
            }
        });
    }

    /**
     * 搜索
     * @param firstResult
     * @param maxResults
     * @param filter
     * @param isFY
     * @return
     */
    public List<BlockArticle> list(int firstResult, int maxResults, String filter, boolean isFY) {
        String sql = "from BlockArticle " + filter;

        return hibernateTemplate.execute(new HibernateCallback<List<BlockArticle>>() {

            @Override
            public List<BlockArticle> doInHibernate(Session session) throws HibernateException, SQLException {
                Query query = session.createQuery(sql);
                query.setCacheable(true);
                if(isFY){
                    query.setFirstResult(firstResult);
                    query.setMaxResults(maxResults);
                }
                return query.list();
            }
        });
    }

    public int countByTypeId(int typeId){
        String sql = "select count(*) from BlockArticle where blockArticleType.id= ? ";
        return hibernateTemplate.execute(new HibernateCallback<Integer>() {
            @Override
            public Integer doInHibernate(Session session) throws HibernateException, SQLException {
                Query query = session.createQuery(sql);
                query.setInteger(0, typeId);
                query.setCacheable(true);
                return ((Number) query.uniqueResult()).intValue();
            }
        });
    }

    public int countByFilter(String filter){
        String sql = "select count(*) from BlockArticle " + filter;
        return hibernateTemplate.execute(new HibernateCallback<Integer>() {
            @Override
            public Integer doInHibernate(Session session) throws HibernateException, SQLException {
                Query query = session.createQuery(sql);
                query.setCacheable(true);
                return ((Number) query.uniqueResult()).intValue();
            }
        });
    }

    /**
     * 取最近的5条
     */
    public List<BlockArticle> listForFive(int typeId){
        String sql = "from BlockArticle where blockArticleType.id = ? order by lastUpdateTime desc";
        return hibernateTemplate.execute(new HibernateCallback<List<BlockArticle>>() {
            @Override
            public List<BlockArticle> doInHibernate(Session session) throws HibernateException, SQLException {
                Query query = session.createQuery(sql);
                query.setInteger(0, typeId);
                query.setCacheable(true);
                if(typeId == 1){
                    query.setMaxResults(5);
                }else if (typeId == 2){
                    query.setMaxResults(4);
                }
                return query.list();
            }
        });
    }
}
