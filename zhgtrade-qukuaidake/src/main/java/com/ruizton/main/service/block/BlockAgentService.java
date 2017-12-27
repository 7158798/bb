package com.ruizton.main.service.block;

import com.ruizton.main.dao.BlockAgentDao;
import com.ruizton.main.model.BlockAgent;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 招股金服
 * CopyRight: www.zhgtrade.com
 * Author: xuelin
 * Date: 17-3-1 下午3:48
 */
@Service
public class BlockAgentService {
    @Autowired
    private BlockAgentDao blockAgentDao;

    public void saveApplyAgent(BlockAgent agent) {
        blockAgentDao.save(agent);
    }

    public BlockAgent findById(int id) {
        return blockAgentDao.findById(id);
    }

    public void updateAgent(BlockAgent agent) {
        blockAgentDao.attachDirty(agent);
    }

    public List<BlockAgent> list(int firstResult, int maxResults, String filter, boolean isFY) {
        return blockAgentDao.list(firstResult, maxResults, filter, isFY);
    }

    public int getAllCount(String tableName, String filter){
        return this.blockAgentDao.getAllCount(tableName, filter);
    }

}
