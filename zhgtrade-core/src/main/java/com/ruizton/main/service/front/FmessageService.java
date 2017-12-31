package com.ruizton.main.service.front;

import com.ruizton.main.dao.FmessageDAO;
import com.ruizton.main.model.Fmessage;
import com.ruizton.main.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 比特家
 * CopyRight : www.btc58.cc
 * Author : xxp
 * Date： 2016/10/25
 */
@Service
public class FmessageService extends BaseService {
    @Autowired
    private FmessageDAO fmessageDAO;

    public void saveMessage(Fmessage fmessage){
        fmessageDAO.save(fmessage);
    }

}
