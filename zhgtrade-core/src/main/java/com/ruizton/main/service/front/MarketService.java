package com.ruizton.main.service.front;

import com.ruizton.main.dao.MarketDAO;
import com.ruizton.main.model.Market;
import com.ruizton.util.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 比特家
 * CopyRight : www.btc58.cc
 * Author : xxp
 * Date： 2017-09-26 17:09
 */
@Service
public class MarketService {

    @Autowired
    private MarketDAO marketDAO;

    public void save(Market transientInstance) {
        marketDAO.save(transientInstance);
    }

    public void delete(Market persistentInstance) {
        marketDAO.delete(persistentInstance);
    }

    public Market findById(Integer id) {
        return marketDAO.findById(id);
    }

    public List findAll() {
        return marketDAO.findAll();
    }

    public void update(Market market) {
        this.marketDAO.update(market);
    }

    public List<Market> findByStatus(int status) {
        return list(0, 0, "where model.status = " + status, false);
    }

    public List<Market> list(int firstResult, int maxResults, String filter,boolean isFY) {
        return marketDAO.list(firstResult, maxResults, filter, isFY);
    }

    public int count(String filter) {
        return marketDAO.count(filter);
    }


    public Market findFirst() {
        List<Market> list = this.marketDAO.list(0, 1, "where model.status=1 and model.tradeStatus=1 ", true);
        return CollectionUtils.isEmpty(list) ? null : list.get(0);
    }
}
