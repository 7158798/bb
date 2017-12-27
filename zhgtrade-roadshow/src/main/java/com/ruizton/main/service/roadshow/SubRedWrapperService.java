package com.ruizton.main.service.roadshow;

import com.ruizton.main.dao.SubRedWrapperDao;
import com.ruizton.main.model.SubRedWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 招股金服
 * CopyRight : www.zhgtrade.com
 * Author : xuelin
 * Date： 2016/12/20
 */
@Service
public class SubRedWrapperService {
    @Autowired
    private SubRedWrapperDao subRedWrapperDao;

    public void save(SubRedWrapper entity) {
        this.subRedWrapperDao.save(entity);
    }

    public void delete(SubRedWrapper entity) {
        this.delete(entity);
    }

    public SubRedWrapper findById(int id) {
        return this.subRedWrapperDao.findById(id);
    }

    public List<SubRedWrapper> findByProperty(String propertyName, Object value) {
        return this.subRedWrapperDao.findByProperty(propertyName, value);
    }

}
