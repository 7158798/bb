package com.ruizton.main.dao;

import com.ruizton.main.dao.comm.HibernateDaoSupport;
import com.ruizton.main.model.ProjectCollection;
import org.hibernate.Query;
import org.hibernate.transform.Transformers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * Created by sunpeng on 2016/7/5.
 */
@Repository
public class ProjectCollectionDao extends HibernateDaoSupport{

    private final Logger log = LoggerFactory.getLogger(getClass());
    public void save(ProjectCollection projectCollection){
        log.debug("saving projectcollection instance");
        saveLog(projectCollection);
    }


    /**
     * 通过uid，cid查找收藏
     * @param uid
     * @param cid
     * @return
     */
    public ProjectCollection findByUserAndCoin(int uid, int cid){
        String sql = "select pc.id as id, pc.cid as cid, pc.uid as uid, pc.createTime as createTime, fv.fname as fname, fv.furl as furl from ProjectCollection pc, Fvirtualcointype fv where fv.fid = pc.cid AND pc.uid = ? and pc.cid = ?";
        Query query = getSession().createQuery(sql).setResultTransformer(Transformers.aliasToBean(ProjectCollection.class));
        query.setInteger(0, uid);
        query.setInteger(1, cid);
        query.setCacheable(true);
        return  (ProjectCollection) query.uniqueResult();
    }


    public int deleteByUserAndCoin(int uid, int cid){
        log.debug("delete projectcolloection instance");
        int updateRow = 0;
        try{
            String sql = "delete from ProjectCollection where uid = ? and cid = ?";
            Query query = getSession().createQuery(sql);
            query.setInteger(0, uid);
            query.setInteger(1, cid);
            updateRow = query.executeUpdate();
            log.debug("delete projectcollection success");
            return updateRow;
        }catch (Exception e){
            log.error("delete projectcollection fail");
            throw e;
        }
    }

    /**
     * 列出每个用户的所有收藏的项目
     * @param uid
     * @return
     */
    public List<ProjectCollection> listByUser(int uid){
        String sql = "select pc.id as id, pc.cid as cid, pc.uid as uid, pc.createTime as createTime, fv.fname as fname, fv.furl as furl from ProjectCollection pc, Fvirtualcointype fv where fv.fid = pc.cid AND pc.uid = ? order by createTime desc ";
        Query query = getSession().createQuery(sql).setResultTransformer(Transformers.aliasToBean(ProjectCollection.class));
        query.setInteger(0, uid);
        return query.list();
    }

    /**
     * 列出用户为收藏项目的ID
     * @param uid
     * @return
     */
    public List<Integer> listUncollectedProjectId(int uid){
        String sql = "select fid from Fvirtualcointype where fstatus = ? and fid not in (select coin_id from project_collection where user_id = ?)";
        Query query = getSession().createSQLQuery(sql);
        query.setInteger(0, 1);
        query.setInteger(1, uid);
        return query.list();
    }
//    /**
//     * 项目收藏排行榜
//     * @return
//     */
//    public List<ProjectCollection> listRank(){
//        String sql = "from ProjectCollection pc, Fvirtualcointype fv where fv.fid = pc.cid group by cid order by ";
//        Query query = getSession().createQuery(sql);
//        return null;
//    }

    /**
     * save对象记录日志
     * @param projectCollection
     */
    public void saveLog(ProjectCollection projectCollection){
        try {
            getSession().save(projectCollection);
            log.debug("save successful");
        } catch (Exception re) {
            log.error("save failed", re);
            throw re;
        }
    }

    /**
     * 可以分页的List
     * @param firstResult
     * @param maxResults
     * @param filter
     * @return
     */
    public List<ProjectCollection> paginList(int firstResult, int maxResults, String filter) {
        String sql = "select pc.id as id, pc.cid as cid, pc.uid as uid, pc.createTime as createTime, fv.fname as fname, fv.furl as furl from ProjectCollection pc, Fvirtualcointype fv where fv.fid = pc.cid " + filter;
        Query query = getSession().createQuery(sql).setResultTransformer(Transformers.aliasToBean(ProjectCollection.class));
        query.setFirstResult(firstResult);
        query.setMaxResults(maxResults);
        return query.list();
    }
}
