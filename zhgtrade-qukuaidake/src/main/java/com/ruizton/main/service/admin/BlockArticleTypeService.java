package com.ruizton.main.service.admin;
import com.ruizton.main.dao.BlockArticleTypeDao;
import com.ruizton.main.model.BlockArticleType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by sunpeng on 2016/7/26.
 */
@Service
public class BlockArticleTypeService {

    @Autowired
    private BlockArticleTypeDao blockArticleTypeDao;

    public void saveBlockArticleType(BlockArticleType blockArticleType){
        blockArticleTypeDao.save(blockArticleType);
    }

    public int deleteBlockArticleTypeById(int id){
        return blockArticleTypeDao.deleteById(id);
    }

    public void updateBlockArticleType(BlockArticleType blockArticleType){
        blockArticleTypeDao.attachDirty(blockArticleType);
    }

    public BlockArticleType findBlockArticleTypeById(int id){
        return blockArticleTypeDao.findById(id);
    }

    public List<BlockArticleType> listBlockArticleTypes(int firstResult, int maxResults, String filter, boolean isFY){
        return blockArticleTypeDao.list(firstResult, maxResults, filter, isFY);
    }

    public int countBlockArticleTypes(String filter){
        return blockArticleTypeDao.count(filter);
    }

    public List<BlockArticleType> findAllBlockArticleTypes(){
        return blockArticleTypeDao.findAll();
    }

}
