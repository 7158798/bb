package com.ruizton.main.service.front;

import com.ruizton.main.model.ProjectCollection;

import java.util.List;

/**
 * Created by sunpeng on 2016/7/6.
 */
public interface ProjectCollectionService {

    void save(ProjectCollection projectCollection);
    ProjectCollection findByUserAndCoin(int uid, int cid);
    int deleteByUserAndCoin(int uid, int cid);
    List<ProjectCollection> listByUser(int uid);
    List<Integer> listUncollectedProjectId(int uid);
    List<ProjectCollection> paginList(int firstResult, int maxResults, String filter);

}
