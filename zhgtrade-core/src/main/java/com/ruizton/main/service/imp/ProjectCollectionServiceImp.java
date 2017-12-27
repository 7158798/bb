package com.ruizton.main.service.imp;

import com.ruizton.main.dao.ProjectCollectionDao;
import com.ruizton.main.model.ProjectCollection;
import com.ruizton.main.service.front.ProjectCollectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by sunpeng on 2016/7/6.
 */
@Service
public class ProjectCollectionServiceImp implements ProjectCollectionService{

    @Autowired
    private ProjectCollectionDao projectCollectionDao;

    @Override
    public void save(ProjectCollection projectCollection) {
        projectCollectionDao.save(projectCollection);
    }

    @Override
    public ProjectCollection findByUserAndCoin(int uid, int cid) {
        return projectCollectionDao.findByUserAndCoin(uid, cid);
    }

    @Override
    public int deleteByUserAndCoin(int uid, int cid) {
        return projectCollectionDao.deleteByUserAndCoin(uid, cid);
    }

    @Override
    public List<ProjectCollection> listByUser(int uid) {
        return projectCollectionDao.listByUser(uid);
    }

    @Override
    public List<Integer> listUncollectedProjectId(int uid) {
        return projectCollectionDao.listUncollectedProjectId(uid);
    }

    @Override
    public List<ProjectCollection> paginList(int firstResult, int maxResults, String filter) {
        return projectCollectionDao.paginList(firstResult, maxResults, filter);
    }
}
