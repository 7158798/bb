package com.ruizton.main.service.winthdraw;

import com.ruizton.main.dao.FbankinfoWithdrawDAO;
import com.ruizton.main.model.FbankinfoWithdraw;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 招股金服
 * CopyRight : www.zhgtrade.com
 * Author : xuelin(1186270005@qq.com)
 * Date： 2016-05-07 17:35
 */
@Service
public class FbankinfoWinthdrawService {
    @Autowired
    private FbankinfoWithdrawDAO fbankinfoWithdrawDAO;

    public FbankinfoWithdraw findById(int fid){
        return fbankinfoWithdrawDAO.findById(fid);
    }

    public void delete(FbankinfoWithdraw info){
        fbankinfoWithdrawDAO.delete(info);
    }

    public void save(FbankinfoWithdraw info){
        fbankinfoWithdrawDAO.save(info);
    }

}
