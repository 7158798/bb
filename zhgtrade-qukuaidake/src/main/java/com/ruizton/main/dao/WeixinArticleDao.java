package com.ruizton.main.dao;

import com.ruizton.main.model.WeixinArticle;
import com.ruizton.main.service.admin.ArticleService;
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
 * Created by sunpeng on 2016/7/29.
 */
@Repository
public class WeixinArticleDao {

    @Autowired
    private HibernateTemplate hibernateTemplate;

    public void save(WeixinArticle weixinArticle) {
        hibernateTemplate.save(weixinArticle);
    }

    public void delete(WeixinArticle weixinArticle) {
        hibernateTemplate.delete(weixinArticle);
    }

    public void update(WeixinArticle weixinArticle) {
        hibernateTemplate.saveOrUpdate(weixinArticle);
    }

    public List<WeixinArticle> list(int firstResult, int maxResults) {
        String sql = "from WeixinArticle order by createTime desc";
        return hibernateTemplate.execute(new HibernateCallback<List<WeixinArticle>>() {
            @Override
            public List<WeixinArticle> doInHibernate(Session session) throws HibernateException, SQLException {
                Query query = session.createQuery(sql);
                query.setFirstResult(firstResult);
                query.setMaxResults(maxResults);
                return query.list();
            }
        });
    }

    public int count() {
        String sql = "select count(*) from WeixinArticle";
        return hibernateTemplate.execute(new HibernateCallback<Integer>() {
            @Override
            public Integer doInHibernate(Session session) throws HibernateException, SQLException {
                Query query = session.createQuery(sql);
                return ((Number) query.uniqueResult()).intValue();
            }
        });
    }

    /**
     * 是否已存在文章
     * @param wenxinId
     * @return
     */
    public boolean isExist(int wenxinId) {
        String sql = "from WeixinArticle where weixinId = ?";
        List list = hibernateTemplate.find(sql, wenxinId);
        return list.size() > 0 ? true : false;
    }
}
