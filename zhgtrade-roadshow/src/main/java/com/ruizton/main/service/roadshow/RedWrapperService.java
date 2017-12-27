package com.ruizton.main.service.roadshow;

import com.alibaba.fastjson.JSON;
import com.ruizton.main.Enum.RedWrapperStatus;
import com.ruizton.main.dao.FuserDAO;
import com.ruizton.main.dao.FvirtualwalletDAO;
import com.ruizton.main.dao.FwalletDAO;
import com.ruizton.main.dao.RedWrapperDao;
import com.ruizton.main.dao.SubRedWrapperDao;
import com.ruizton.main.dto.SubRedWrapperDTO;
import com.ruizton.main.model.Fuser;
import com.ruizton.main.model.Fvirtualwallet;
import com.ruizton.main.model.Fwallet;
import com.ruizton.main.model.RedWrapper;
import com.ruizton.main.model.SubRedWrapper;
import com.ruizton.util.StringUtils;
import com.ruizton.util.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Pipeline;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

/**
 * 招股金服
 * CopyRight : www.zhgtrade.com
 * Author : xuelin
 * Date： 2016/12/20
 */
@Service
public class RedWrapperService {
    @Autowired
    private RedWrapperDao redWrapperDao;
    @Autowired
    private SubRedWrapperDao subRedWrapperDao;
    @Autowired
    private FwalletDAO fwalletDAO;
    @Autowired
    private FvirtualwalletDAO fvirtualwalletDAO;
    @Autowired
    private FuserDAO fuserDAO;
    @Autowired
    private JedisPool jedisPool;

    public void save(RedWrapper entity){
        this.redWrapperDao.save(entity);
    }

    public void delete(RedWrapper entity) {
        this.redWrapperDao.delete(entity);
    }

    public RedWrapper findById(int id) {
        return this.redWrapperDao.findById(id);
    }

    public void addRedWrapperForRMB(RedWrapper redWrapper, Fuser fuser){
        Fwallet dbFwallet = fuser.getFwallet();

        // 冻结金额
        Fwallet fwallet = new Fwallet();
        fwallet.setFtotalRmb(redWrapper.getAmount());
        fwallet.setFid(dbFwallet.getFid());
        fwallet.setFlastUpdateTime(Utils.getTimestamp());
        fwalletDAO.updateRmb(fwallet);

        this.save(redWrapper);

        // 解冻金额
        fwalletDAO.updateFwalletFrozen(dbFwallet.getFid(), redWrapper.getAmount());

        // 分解红包
        this.assignSubAndCache(redWrapper);
    }

    public void addRedWrapperForCoin(RedWrapper redWrapper, Fuser fuser, Fvirtualwallet fvirtualwallet){
        // 冻结金额
        fvirtualwalletDAO.updateRmb(fuser.getFid(), redWrapper.getCoinType(), redWrapper.getAmount(), Utils.getTimestamp());

        this.save(redWrapper);

        fvirtualwalletDAO.updateVirtualWalletFrozen(fvirtualwallet.getFid(), redWrapper.getAmount());

        // 分解红包
        this.assignSubAndCache(redWrapper);
    }

    /**
     * 普通红包
     *
     * @param redWrapper
     * @return
     */
    protected static List<SubRedWrapperDTO> getNormalSub(RedWrapper redWrapper){
        double remainMoney = redWrapper.getAmount();
        int remainSize = redWrapper.getCount();
        List<SubRedWrapperDTO> dtos = new ArrayList<>(remainSize);

        double amount = remainMoney / remainSize;
        BigDecimal decimal = new BigDecimal(amount);

        // 小数处理
        if(redWrapper.isRMB()){
            amount = decimal.setScale(2, RoundingMode.HALF_UP).doubleValue();
        }else{
            amount = decimal.setScale(4, RoundingMode.HALF_UP).doubleValue();
        }

        SubRedWrapperDTO dto;
        do{
            dto = new SubRedWrapperDTO();
            dto.setRedWrapperId(redWrapper.getId());
            dto.setAmount(amount);
            dtos.add(dto);
            remainMoney -= amount;
        }while (--remainSize > 0);

        return dtos;
    }

    protected static List<SubRedWrapperDTO> getSubRedWrapper(RedWrapper redWrapper) {
        if(2 == redWrapper.getType()){
            // 普通红包
            return getNormalSub(redWrapper);
        }

        // remainSize 剩余的红包数量
        // remainMoney 剩余的钱
        double min = redWrapper.isRMB() ? 0.01 : 0.0001;
        double max;
        double amount;
        double remainMoney = redWrapper.getAmount();
        int remainSize = redWrapper.getCount();

        SubRedWrapperDTO dto;
        List<SubRedWrapperDTO> dtos = new ArrayList<>(remainSize);

        do{
            if(1 == remainSize){
                BigDecimal decimal = new BigDecimal(remainMoney);
                if(redWrapper.isRMB()){
                    amount = decimal.setScale(2, RoundingMode.HALF_UP).doubleValue();
                }else{
                    amount = decimal.setScale(4, RoundingMode.HALF_UP).doubleValue();
                }
                dto = new SubRedWrapperDTO();
                dto.setRedWrapperId(redWrapper.getId());
                dto.setAmount(amount);
                dtos.add(dto);
            }else{
                // 拼手气
                SecureRandom random = new SecureRandom();
                max = remainMoney / remainSize * 2;
                amount = random.nextDouble() * max;
                amount = amount <= min ? min: amount;

                // 小数处理
                BigDecimal decimal = new BigDecimal(amount);
                if(redWrapper.isRMB()){
                    amount = decimal.setScale(2, RoundingMode.FLOOR).doubleValue();
                }else{
                    amount = decimal.setScale(4, RoundingMode.FLOOR).doubleValue();
                }

                remainMoney -= amount;

                dto = new SubRedWrapperDTO();
                dto.setRedWrapperId(redWrapper.getId());
                dto.setAmount(amount);
                dtos.add(dto);
            }
        }while (--remainSize > 0);

        return dtos;
    }

    /**
     * 将大红包分配为若干小红包
     *
     * @param redWrapper
     */
    public void assignSubAndCache(RedWrapper redWrapper){
        List<SubRedWrapperDTO> dtos = getSubRedWrapper(redWrapper);

        try(Jedis jedis = jedisPool.getResource();Pipeline pipeline = jedis.pipelined()) {
            pipeline.multi();

            // 红包个数
            pipeline.set(StringUtils.string2UTF8Bytes(RedWrapper.CACHE_RED_WRAPPER_PREFIX, redWrapper.getId(), RedWrapper.CACHE_RED_WRAPPER_COUNT), StringUtils.string2UTF8Bytes(redWrapper.getCount()));

            // 子红包
            byte[] subsKey = StringUtils.string2UTF8Bytes(RedWrapper.CACHE_RED_WRAPPER_PREFIX, redWrapper.getId(), RedWrapper.CACHE_RED_WRAPPER_SUB);
            for(SubRedWrapperDTO dto : dtos){
                pipeline.lpush(subsKey, StringUtils.string2UTF8Bytes(JSON.toJSONString(dto)));
            }

            // 红包过期参数
            byte[] expireKey = StringUtils.string2UTF8Bytes(RedWrapper.CACHE_RED_WRAPPER_PREFIX, redWrapper.getId(), RedWrapper.CACHE_RED_WRAPPER_EXPIRE);
            pipeline.set(expireKey, StringUtils.string2UTF8Bytes(Boolean.FALSE));
            pipeline.expire(expireKey, RedWrapper.EXPIRE_SECONDES);

            pipeline.exec();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("cache red wrapper failure");
        }
    }

    /**
     * 退回金额
     *
     * @param redWrapper
     */
    public void refundRedWrapper(RedWrapper redWrapper){
        // cache更新
        try(Jedis jedis = jedisPool.getResource()){
            jedis.set(StringUtils.string2UTF8Bytes(RedWrapper.CACHE_RED_WRAPPER_PREFIX, redWrapper.getId(), RedWrapper.CACHE_RED_WRAPPER_COUNT), StringUtils.string2UTF8Bytes(0));
        }catch (Exception e){
            e.printStackTrace();
        }

        // 更红包信息
        double totalAmount = subRedWrapperDao.sumAmount(redWrapper.getId());
        redWrapper.setRefundAmount(redWrapper.getAmount() - totalAmount);
        if(totalAmount > 0){
            redWrapper.setStatus(RedWrapperStatus.Part_Refunded);
        }else {
            redWrapper.setStatus(RedWrapperStatus.Refunded);
        }
        redWrapperDao.update(redWrapper);

        Fuser fuser = fuserDAO.findById(redWrapper.getUserId());
        if(redWrapper.isRMB()){
            // 退回人民币金额
            fwalletDAO.updateFwalletMoney(fuser.getFwallet().getFid(), redWrapper.getRefundAmount());
        }else {
            // 退回虚拟币
            fvirtualwalletDAO.updateFwalletMoney(redWrapper.getCoinType(), fuser.getFid(), redWrapper.getRefundAmount(), 0, 0);
        }
    }

    /**
     * 红包入账
     *
     */
    public void addRedWrapperMoneyToWallet(SubRedWrapper subRedWrapper){
        RedWrapper redWrapper = redWrapperDao.findById(subRedWrapper.getRedWrapperId());
        Fuser fuser = fuserDAO.findById(redWrapper.getUserId());
        if(redWrapper.isRMB()){
            // 人民币
            fwalletDAO.updateFwalletMoney(fuser.getFwallet().getFid(), subRedWrapper.getAmount());
        }else {
            // 虚拟币
            fvirtualwalletDAO.updateFwalletMoney(redWrapper.getCoinType(), fuser.getFid(), subRedWrapper.getAmount(), 0, 0);
        }

        subRedWrapper.setUpdateTime(Utils.getTimestamp());
        subRedWrapperDao.save(subRedWrapper);

        double subTotalAmount = subRedWrapperDao.sumAmount(redWrapper.getId());
        if(subTotalAmount == redWrapper.getAmount()){
            // 红包已抢完
            redWrapper.setStatus(RedWrapperStatus.Finished);
            redWrapperDao.update(redWrapper);
        }
    }
}

























