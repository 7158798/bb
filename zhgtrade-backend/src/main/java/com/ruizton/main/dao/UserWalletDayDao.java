package com.ruizton.main.dao;

import com.ruizton.main.dao.comm.HibernateDaoSupport;
import com.ruizton.main.model.UserWalletDay;
import com.ruizton.util.DateUtils;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by liuyuanbo on 2017/4/17.
 */
@Repository
public class UserWalletDayDao extends HibernateDaoSupport {
    private static final Logger LOGGER= LoggerFactory.getLogger(UserWalletDayDao.class);
    //分页查询用户每日剩余资金
    public List<UserWalletDay> findByUserId(int userId , int page, int size){
        Query query = getSession().createQuery("from com.ruizton.main.model.UserWalletDay where userId=? order by createTime desc ");
        query.setParameter(0, userId);
        query.setFirstResult(page);
        query.setMaxResults(size);
        return query.list();
    }

    public void save(UserWalletDay user){
        getSession().save(user);
    }

    //更新用户每日金额
    public int updateWallet(){
        String sql="INSERT INTO " +
                "user_wallet_day(rest_balance,rest_freeze,create_time,create_date,user_id) " +
                "SELECT " +
                "b.fTotalRMB as rest_balance, " +
                "b.fFrozenRMB as rest_freeze, " +
                "NOW() as update_time, " +
                "NOW() as create_date, " +
                "a.fid as user_id " +
                "FROM " +
                "fuser a, fwallet b where a.FWa_fId=b.fid";
        return getSession().createSQLQuery(sql).executeUpdate();

    }


    //查询总数量
    public int getAllCount(int userId){

        Query query = getSession().createQuery("select count(*) from com.ruizton.main.model.UserWalletDay u where u.userId=? ");
        query.setParameter(0, userId);
        return Integer.parseInt(query.list().get(0).toString());
    }


    //删除一个月前数据
    //delete from user_wallet_day where create_date < date_add(curdate(),INTERVAL -1 month);
    public int  deleteMonthAgo(){
       int count= getSession().createSQLQuery("delete from user_wallet_day where create_date < date_add(curdate(),INTERVAL -1 month)").executeUpdate();
//        getSession().createQuery("delete from com.ruizton.main.model.UserWalletDay u where u.createDate < date_add(curdate(),INTERVAL -1 month)");
        return count;
    }
}
