package com.zhgtrade.front.controller;

import com.alibaba.fastjson.JSON;
import com.ruizton.main.Enum.RedWrapperStatus;
import com.ruizton.main.cache.data.RealTimeDataService;
import com.ruizton.main.dto.LatestDealData;
import com.ruizton.main.dto.SubRedWrapperDTO;
import com.ruizton.main.model.Fuser;
import com.ruizton.main.model.Fvirtualwallet;
import com.ruizton.main.model.Fwallet;
import com.ruizton.main.model.RedWrapper;
import com.ruizton.main.model.SubRedWrapper;
import com.ruizton.main.mq.MessageQueueService;
import com.ruizton.main.mq.QueueConstants;
import com.ruizton.main.service.front.FrontUserService;
import com.ruizton.main.service.roadshow.RedWrapperService;
import com.ruizton.main.service.roadshow.SubRedWrapperService;
import com.ruizton.util.CollectionUtils;
import com.ruizton.util.Constants;
import com.ruizton.util.StringUtils;
import com.ruizton.util.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Pipeline;

import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 招股金服
 * CopyRight : www.zhgtrade.com
 * Author : xuelin
 * Date： 2016/12/20
 */
@RestController
@RequestMapping("/account")
public class RedWrapperController extends ApiBaseController {
    @Autowired
    private FrontUserService frontUserService;
    @Autowired
    private RealTimeDataService realTimeDataService;
    @Autowired
    private RedWrapperService redWrapperService;
    @Autowired
    private SubRedWrapperService subRedWrapperService;
    @Autowired
    private MessageQueueService messageQueueService;
    @Autowired
    private JedisPool jedisPool;

    private static String catchRedWrapperLuaScript;
    private static String openRedWrapperLuaScript;

    /**
     * 创建红包 150/s
     *
     * @param session
     * @param amount        金额
     * @param count         份额
     * @param coinType      0人民币|虚拟币
     * @param type          1拼手气|2普通
     * @param title         标题
     * @return
     */
    @RequestMapping(value = "/createRedWrapper", method = RequestMethod.GET)
    public Object createRedWrapper(HttpSession session, double amount, String tradePassword,
                                   @RequestParam(required = false, defaultValue = "1")int count,
                                   @RequestParam(required = false, defaultValue = "0")int coinType,
                                   @RequestParam(required = false, defaultValue = "1")short type,
                                   @RequestParam(required = false, defaultValue = "0")int roadShowId,
                                   @RequestParam(required = false)String title){
        if(amount <= 0 || count <= 0 || coinType < 0 || (1 != type && 2 != type)){
            // 非法操作
            return forFailureResult(101);
        }

        Fuser fuser = getLoginUser(session);
        fuser = frontUserService.findById(fuser.getFid());
        RedWrapper redWrapper = new RedWrapper(fuser.getFid(), title, amount, count, RedWrapperStatus.Normal, coinType, type, Utils.getTimestamp());
        redWrapper.setRoadShowId(roadShowId);

        if(!Utils.MD5(tradePassword).equals(fuser.getFtradePassword())){
            return forFailureResult(102);
        }

        if(redWrapper.isRMB()){
            // 人民币红包
            if(amount < 0.01 * count){
                // 红包金额不足以分配
                return forFailureResult(103);
            }

            if(2 == type){
                // 普通红包
                if(amount * 100 % count > 0){
                    // 分配不均
                    return forFailureResult(104);
                }
            }

            Fwallet fwallet = fuser.getFwallet();
            if(fwallet.getFtotalRmb() < amount){
                // 余额不足
                return forFailureResult(105);
            }

            this.redWrapperService.addRedWrapperForRMB(redWrapper, fuser);
        }else{
            if(amount < 0.0001 * count){
                // 红包金额不足以分配
                return forFailureResult(106);
            }
            if(2 == type){
                // 普通红包
                if(amount * 10000 % count > 0){
                    // 分配不均
                    return forFailureResult(107);
                }
            }

            // 虚拟币
            LatestDealData dealData = realTimeDataService.getLatestDealData(coinType);
            if(null == dealData){
                return forFailureResult(108);
            }

            Fvirtualwallet fvirtualwallet = frontUserService.findVirtualWalletByUser(fuser.getFid(), coinType);
            if(fvirtualwallet.getFtotal() < amount){
                // 余额不足
                return forFailureResult(109);
            }

            this.redWrapperService.addRedWrapperForCoin(redWrapper, fuser, fvirtualwallet);
        }

        return forSuccessResult(redWrapper.getId());
    }

    private String getCatchRedWrapperLuaScript(){
        if(StringUtils.hasText(catchRedWrapperLuaScript)){
            return catchRedWrapperLuaScript;
        }

        /**
         * ARGV[1] key前缀
         * ARGV[2] 红包id
         * ARGV[3] 已领取用户key
         * ARGV[4] 用户id
         * ARGV[5] 是否过期key
         *
         */

        StringBuilder scriptBuf = new StringBuilder();
        scriptBuf.append("local keyPrefix = ARGV[1]..ARGV[2]\n");

        // 是否已领取过
        scriptBuf.append("local isCatched = redis.call('hget', keyPrefix..ARGV[3], ARGV[4])\n");
        scriptBuf.append("if isCatched then\n");
        scriptBuf.append("  return '101'\n");
        scriptBuf.append("end\n");

        // 是否过期
        scriptBuf.append("local isExpire = redis.call('exists', keyPrefix..ARGV[5])\n");
        scriptBuf.append("if isExpire then\n");
        scriptBuf.append("  return '102'\n");
        scriptBuf.append("end\n");

        // 是否已领完
        scriptBuf.append("local leftCount = redis.call('llen', keyPrefix..ARGV[6])\n");
        scriptBuf.append("leftCount = leftCount and tonumber(leftCount) or 0\n");
        scriptBuf.append("if leftCount <= 0 then\n");
        scriptBuf.append("  return '103'\n");
        scriptBuf.append("end\n");

        catchRedWrapperLuaScript = scriptBuf.toString();
        return catchRedWrapperLuaScript;
    }

    /**
     * 280/s
     * 抢红包
     *
     * @param session
     * @param id    红包id
     * @return
     */
    @RequestMapping("/catchRedWrapper")
    public Object catchRedWrapper(HttpSession session, int id){
        Fuser fuser = getLoginUser(session);

        try(Jedis jedis = jedisPool.getResource()){
            boolean isExpire = !jedis.exists(StringUtils.string2UTF8Bytes(RedWrapper.CACHE_RED_WRAPPER_PREFIX, id, RedWrapper.CACHE_RED_WRAPPER_EXPIRE));
            if(isExpire){
                // 红包过期
                return forFailureResult(101);
            }

            byte[] userBytes = jedis.hget(StringUtils.string2UTF8Bytes(RedWrapper.CACHE_RED_WRAPPER_PREFIX, id, RedWrapper.CACHE_RED_WRAPPER_USERS), StringUtils.string2UTF8Bytes(fuser.getFid()));
            if(!ObjectUtils.isEmpty(userBytes)){
                // 已领取过红包
                return forFailureResult(102);
            }

            byte[] countBytes = jedis.get(StringUtils.string2UTF8Bytes(RedWrapper.CACHE_RED_WRAPPER_PREFIX, id, RedWrapper.CACHE_RED_WRAPPER_COUNT));
            if(Integer.valueOf(new String(countBytes, Constants.UTF8_CHARSET)) <= 0){
                // 红包已领取完
                return forFailureResult(103);
            }
        }catch (Exception e){
            e.printStackTrace();
            return forFailureResult(500);
        }

        return forSuccessResult(id);
    }

    /**
     * 300/s
     * 拆红包
     *
     * @param session
     * @param id    红包id
     * @return
     */
    @RequestMapping(value = "/openRedWrapper", method = RequestMethod.GET)
    public Object openRedWrapper(HttpSession session, int id){
        Fuser fuser = getLoginUser(session);

        try(Jedis jedis = jedisPool.getResource(); Pipeline pipeline = jedis.pipelined()){
            boolean isExpire = !jedis.exists(StringUtils.string2UTF8Bytes(RedWrapper.CACHE_RED_WRAPPER_PREFIX, id, RedWrapper.CACHE_RED_WRAPPER_EXPIRE));
            if(isExpire){
                // 红包过期
                return forFailureResult(101);
            }

//            byte[] userBytes = jedis.hget(StringUtils.string2UTF8Bytes(RedWrapper.CACHE_RED_WRAPPER_PREFIX, id, RedWrapper.CACHE_RED_WRAPPER_USERS), StringUtils.string2UTF8Bytes(fuser.getFid()));
//            if(!ObjectUtils.isEmpty(userBytes)){
//                // 已领取过红包
//                return forFailureResult(102);
//            }

            Long count = jedis.decr(StringUtils.string2UTF8Bytes(RedWrapper.CACHE_RED_WRAPPER_PREFIX, id, RedWrapper.CACHE_RED_WRAPPER_COUNT));
            if(count < 0){
                // 抢红包失败
                return forFailureResult(103);
            }

            // 获取子红包(左进右出)
            byte[] subBytes = jedis.rpop(StringUtils.string2UTF8Bytes(RedWrapper.CACHE_RED_WRAPPER_PREFIX, id, RedWrapper.CACHE_RED_WRAPPER_SUB));
            if(ObjectUtils.isEmpty(subBytes)){
                // 抢红包失败
                return forFailureResult(104);
            }

            // 添加抢红包记录
            pipeline.hset(StringUtils.string2UTF8Bytes(RedWrapper.CACHE_RED_WRAPPER_PREFIX, id, RedWrapper.CACHE_RED_WRAPPER_USERS), StringUtils.string2UTF8Bytes(fuser.getFid()), StringUtils.string2UTF8Bytes(Boolean.TRUE));

            // 红包记录
            String subString = new String(subBytes, Constants.UTF8_CHARSET);
            SubRedWrapperDTO dto = JSON.parseObject(subString, SubRedWrapperDTO.class);
            dto.setUserId(fuser.getFid());
            dto.setUsername(fuser.getFnickName());
            dto.setHeadImage(fuser.getHeadImgUrl());
            dto.setCatchTime(Utils.getTimestamp());
            pipeline.zadd(StringUtils.string2UTF8Bytes(RedWrapper.CACHE_RED_WRAPPER_PREFIX, id, RedWrapper.CACHE_RED_WRAPPER_USERS_SUB), dto.getCatchTime().getTime(), StringUtils.string2UTF8Bytes(JSON.toJSONString(dto)));

            // 异步入账
            messageQueueService.publish(QueueConstants.RED_WRAPPER_DRAW_MONEY_QUEUE, dto);
        }catch (Exception e){
            e.printStackTrace();
            return forFailureResult(500);
        }

        return forSuccessResult(id);
    }

    /**
     * 抢红包详情
     *
     * @param session
     * @param id    红包id
     * @return
     */
    @RequestMapping("/redWrapperDetail")
    public Object redWrapperDetail(HttpSession session, int id){
        RedWrapper redWrapper = redWrapperService.findById(id);
        if(null == redWrapper){
            return forFailureResult(101);
        }

        Map<String, Object> retMap = new HashMap<>();
        boolean fromDb = false;
        List<SubRedWrapperDTO> dtos = new ArrayList<>(redWrapper.getCount());
        Fuser fuser = getLoginUser(session);
        try(Jedis jedis = jedisPool.getResource()){
            if(!jedis.exists(StringUtils.string2UTF8Bytes(RedWrapper.CACHE_RED_WRAPPER_PREFIX, id, RedWrapper.CACHE_RED_WRAPPER_EXPIRE))){
                fromDb = true;
            }else{
                // 从缓存中获取红包记录
                Set<byte[]> byteSet = jedis.zrange(StringUtils.string2UTF8Bytes(RedWrapper.CACHE_RED_WRAPPER_PREFIX, id, RedWrapper.CACHE_RED_WRAPPER_USERS_SUB), 0, -1);
                if(!CollectionUtils.isEmpty(byteSet)){
                    byteSet.forEach(e -> {
                        try {
                            String subStr = new String(e, Constants.UTF8_CHARSET);
                            SubRedWrapperDTO dto = JSON.parseObject(subStr, SubRedWrapperDTO.class);
                            dtos.add(dto);

                            if(fuser.getFid() == dto.getUserId()){
                                // 本人抢到金额
                                retMap.put("catchAmount", dto.getAmount());
                            }
                        } catch (UnsupportedEncodingException e1) {
                            e1.printStackTrace();
                        }
                    });
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            fromDb = true;
        }

        if(fromDb){
            // 从db获取数据
            List<SubRedWrapper> subs = subRedWrapperService.findByProperty("redWrapperId", redWrapper.getId());
            subs.forEach(e -> {
                SubRedWrapperDTO dto = new SubRedWrapperDTO();

                Fuser dbUser = frontUserService.findById(e.getUserId());
                dto.setAmount(e.getAmount());
                dto.setCatchTime(e.getCreateTime());
                dto.setHeadImage(dbUser.getHeadImgUrl());
                dto.setUsername(dbUser.getFnickName());
                dto.setUserId(fuser.getFid());
                dtos.add(dto);

                if(fuser.getFid() == dto.getUserId()){
                    // 本人抢到金额
                    retMap.put("catchAmount", dto.getAmount());
                }
            });
        }

        // 标识最佳手气
        if(!CollectionUtils.isEmpty(dtos)){
            int maxIndex = 0;
            double maxAmount = 0;
            for(int i=0, len=dtos.size(); i<len; i++){
                SubRedWrapperDTO dto = dtos.get(i);
                if(dto.getAmount() > maxAmount){
                    maxAmount = dto.getAmount();
                    maxIndex = i;
                }
            }
            dtos.get(maxIndex).setBastOfLuck(true);
        }

        Fuser ownerFuser;
        if(redWrapper.getUserId() == fuser.getFid()){
            ownerFuser = fuser;
        }else{
            ownerFuser = frontUserService.findById(redWrapper.getUserId());
        }

        retMap.put("id", id);
        retMap.put("amount", redWrapper.getAmount());
        retMap.put("status", redWrapper.getStatus());
        retMap.put("title", redWrapper.getTitle());
        retMap.put("username", ownerFuser.getFnickName());
        retMap.put("headerUrl", ownerFuser.getHeadImgUrl());
        retMap.put("list", dtos);
        return forSuccessResult(retMap);
    }

    /**
     * 退回金额
     *
     * @param session
     * @param id
     * @return
     */
    @RequestMapping("/refundRedWrapper")
    public Object refundRedWrapper(HttpSession session, int id){
        RedWrapper redWrapper = redWrapperService.findById(id);
        if(null == redWrapper){
            return forFailureResult(101);
        }

        Fuser fuser = getLoginUser(session);
        if(fuser.getFid() != redWrapper.getUserId()){
            return forFailureResult(102);
        }

        if(!redWrapper.isExpire()){
            // 未过期不能退回
            return forFailureResult(103);
        }

        if(RedWrapperStatus.Normal != redWrapper.getStatus()){
            // 非退款状态
            return forFailureResult(104);
        }

        redWrapperService.refundRedWrapper(redWrapper);
        return forSuccessResult(id);
    }
}





















