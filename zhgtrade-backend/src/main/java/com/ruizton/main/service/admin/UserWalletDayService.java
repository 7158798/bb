package com.ruizton.main.service.admin;

import com.ruizton.main.dao.UserWalletDayDao;
import com.ruizton.main.model.UserWalletDay;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by sunpeng on 2017/4/17.
 */
@Service
public class UserWalletDayService {
    @Autowired
    private UserWalletDayDao  userWalletDayDao;

    public  List<UserWalletDay> findByUserId(int userId , int page, int size){
        List<UserWalletDay> list=userWalletDayDao.findByUserId(userId,page,size);
        if(list.size()>0){
            return list;
        }
        return null;
    }
    /*生成用户每日金额*/
    public int updateWallet(){
        return  userWalletDayDao.updateWallet();
    }
    public int totalCount(int userId){
        return userWalletDayDao.getAllCount(userId);
    }


    public int deleteMonthAgo(){
        return userWalletDayDao.deleteMonthAgo();
    }

}
