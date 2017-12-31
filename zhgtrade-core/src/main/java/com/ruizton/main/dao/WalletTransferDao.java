package com.ruizton.main.dao;

import com.ruizton.main.Enum.MoneyType;
import com.ruizton.main.Enum.WalletTransferStatus;
import com.ruizton.main.dao.comm.HibernateDaoSupport;
import com.ruizton.main.model.WalletTransfer;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 比特家
 * CopyRight : www.btc58.cc
 * Author : xxp
 * Date： 2016/7/25
 */
@Repository
public class WalletTransferDao extends HibernateDaoSupport {

    public void save(WalletTransfer entity){
        getSession().save(entity);
    }

    public WalletTransfer findById(int id){
        Query query = getSession().createQuery("from com.ruizton.main.model.WalletTransfer where id = " + id);
        List list = query.list();
        return (WalletTransfer) list.get(0);
    }

    public void update(WalletTransfer entity){
        getSession().update(entity);
    }

    public int updateForRefund(int id){
        Query query = getSession().createQuery("update com.ruizton.main.model.WalletTransfer set status =:status, updateTime=:time where id=:id and status =:oldStatus");
        query.setParameter("status", WalletTransferStatus.Refund);
        query.setParameter("time", new Date());
        query.setParameter("id", id);
        query.setParameter("oldStatus", WalletTransferStatus.Paid);
        return query.executeUpdate();
    }

    public int updateForSuccess(int id){
        Query query = getSession().createQuery("update com.ruizton.main.model.WalletTransfer set status =:status, updateTime=:time where id=:id and status =:oldStatus");
        query.setParameter("status", WalletTransferStatus.Success);
        query.setParameter("time", new Date());
        query.setParameter("id", id);
        query.setParameter("oldStatus", WalletTransferStatus.Going);
        return query.executeUpdate();
    }

    public List<WalletTransfer> findForTransfer(int maxResult){
        Query query = getSession().createQuery("from com.ruizton.main.model.WalletTransfer where status =:status");
        query.setParameter("status", WalletTransferStatus.Paid);
        query.setMaxResults(maxResult);
        return query.list();
    }

    public List<WalletTransfer> findByUser(int userId, int firstResult, int maxResult){
        Query query = getSession().createQuery("from com.ruizton.main.model.WalletTransfer where userId = " + userId);
        query.setFirstResult(firstResult);
        query.setMaxResults(maxResult);
        return query.list();
    }

    public List<WalletTransfer> find(Integer userId, Integer coinId, Short moneyType, Short status, int firstResult, int maxResult){
        StringBuilder sqlBuf = new StringBuilder("from com.ruizton.main.model.WalletTransfer where 1=1");
        if(null != userId){
            sqlBuf.append(" and userId = ").append(userId);
        }
        if(null != coinId){
            sqlBuf.append(" and virtualCoinId = ").append(coinId);
        }
        if(null != moneyType){
            sqlBuf.append(" and moneyType = ").append(moneyType);
        }
        if(null != status){
            sqlBuf.append(" and status = ").append(status);
        }
        sqlBuf.append(" order by id desc");

        Query query = getSession().createQuery(sqlBuf.toString());

        query.setFirstResult(firstResult);
        query.setMaxResults(maxResult);

        return query.list();
    }

    public int count(Integer userId, Integer coinId, Short moneyType, Short status){
        StringBuilder sqlBuf = new StringBuilder("select count(id) from com.ruizton.main.model.WalletTransfer where 1=1");
        if(null != userId){
            sqlBuf.append(" and userId = ").append(userId);
        }
        if(null != coinId){
            sqlBuf.append(" and virtualCoinId = ").append(coinId);
        }
        if(null != moneyType){
            sqlBuf.append(" and moneyType = ").append(moneyType);
        }
        if(null != status){
            sqlBuf.append(" and status = ").append(status);
        }

        Query query = getSession().createQuery(sqlBuf.toString());

        return ((Long)query.uniqueResult()).intValue();
    }

    public List<WalletTransfer> listForAdmin(Integer userId, Integer coinId, Boolean active, Short moneyType, Short status, int firstResult, int maxResult, String orderField, String orderDirection){
        StringBuilder sqlBuf = new StringBuilder("from com.ruizton.main.model.WalletTransfer where 1=1");
        if(null != userId){
            sqlBuf.append(" and userId = ").append(userId);
        }
        if(null != coinId){
            sqlBuf.append(" and virtualCoinId = ").append(coinId);
        }
        if(null != active){
            sqlBuf.append(" and active = ").append(active);
        }
        if(null != moneyType){
            sqlBuf.append(" and moneyType = ").append(moneyType);
        }
        if(null != status){
            sqlBuf.append(" and status = ").append(status);
        }
        if(StringUtils.hasText(orderField)){
            sqlBuf.append(" order by " + orderField + " " + orderDirection);
        }else{
            sqlBuf.append(" order by id desc");
        }

        Query query = getSession().createQuery(sqlBuf.toString());

        if(maxResult != 0){
            query.setFirstResult(firstResult);
            query.setMaxResults(maxResult);
        }
        return query.list();
    }

    public int countForAdmin(Integer userId, Integer coinId, Boolean active, Short moneyType, Short status){
        StringBuilder sqlBuf = new StringBuilder("select count(id) from com.ruizton.main.model.WalletTransfer where 1=1");
        if(null != userId){
            sqlBuf.append(" and userId = ").append(userId);
        }
        if(null != coinId){
            sqlBuf.append(" and virtualCoinId = ").append(coinId);
        }
        if(null != active){
            sqlBuf.append(" and active = ").append(active);
        }
        if(null != moneyType){
            sqlBuf.append(" and moneyType = ").append(moneyType);
        }
        if(null != status){
            sqlBuf.append(" and status = ").append(status);
        }

        Query query = getSession().createQuery(sqlBuf.toString());

        return ((Long)query.uniqueResult()).intValue();
    }

    public Object[] transferReport(Date startDate, Date endDate, MoneyType moneyType, Integer virtualCoinId, Boolean active, WalletTransferStatus status){
        StringBuilder hqlBuf = new StringBuilder("select date_format(createTime, '%Y-%m-%d') as date, sum(amount) from com.ruizton.main.model.WalletTransfer where 1=1");
        if(null != startDate){
            hqlBuf.append(" and createTime>=:startDate");
        }
        if(null != endDate){
            hqlBuf.append(" and createTime<=:endDate");
        }
        if(null != moneyType){
            hqlBuf.append(" and moneyType=:moneyType");
        }
        if(null != status){
            hqlBuf.append(" and active=:active");
        }
        if(null != active){
            hqlBuf.append(" and status=:status");
        }
        if(null != virtualCoinId){
            hqlBuf.append(" and virtualCoinId=:virtualCoinId");
        }
        hqlBuf.append(" group by date_format(createTime, '%Y-%m-%d') order by date");

        Query query = getSession().createQuery(hqlBuf.toString());

        if(null != startDate){
            query.setParameter("startDate", startDate);
        }
        if(null != endDate){
            query.setParameter("endDate", endDate);
        }
        if(null != moneyType){
            query.setParameter("moneyType", moneyType);
        }
        if(null != active){
            query.setParameter("active", active);
        }
        if(null != status){
            query.setParameter("status", status);
        }
        if(null != virtualCoinId){
            query.setParameter("virtualCoinId", virtualCoinId);
        }

        List list = query.list();

        List dates = new ArrayList<>(list.size());
        List datas = new ArrayList<>(list.size());

        list.forEach(e -> {
            Object[] line = (Object[]) e;
            dates.add(line[0]);
            datas.add(line[1]);
        });

        return new Object[]{dates, datas};
    }
}
