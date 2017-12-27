package com.ruizton.main.service.admin;

import com.ruizton.main.dao.BlockArticleDao;
import com.ruizton.main.model.BlockArticle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by sunpeng on 2016/7/26.
 */
@Service
public class BlockArticleService {

    @Autowired
    private BlockArticleDao blockArticleDao;

    public void save(BlockArticle blockArticle){
        blockArticleDao.save(blockArticle);
    }

    public int deleteBlockArticleById(int id){
        return blockArticleDao.deleteById(id);
    }

    public void updateBlockArticle(BlockArticle blockArticle){
        blockArticleDao.attachDirty(blockArticle);
    }

    public BlockArticle findBlockArticleById(int id){
        return blockArticleDao.findById(id);
    }

    public List<BlockArticle> listBlockArticlesByType(int typeId, int firstResult, int maxResults){
        return blockArticleDao.listByTypeId(typeId, firstResult, maxResults);
    }

    public List<BlockArticle> listBlockArticlesByFilter(int firstResult, int maxResult, String filter, boolean isFY){
        return blockArticleDao.list(firstResult, maxResult, filter, isFY);
    }

    public List<BlockArticle> listFiveBlockArticles(int typeId){
        return blockArticleDao.listForFive(typeId);
    }

    public int countBlockArticleByType(int typeId){
        return blockArticleDao.countByTypeId(typeId);
    }

    public int countBlockArticleByFilter(String filter){
        return blockArticleDao.countByFilter(filter);
    }
}
