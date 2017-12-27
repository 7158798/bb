package com.ruizton.main.service.activity;

import com.ruizton.main.Enum.MessageStatusEnum;
import com.ruizton.main.comm.ConstantMap;
import com.ruizton.main.dao.FmessageDAO;
import com.ruizton.main.dao.FuserDAO;
import com.ruizton.main.dao.FwalletDAO;
import com.ruizton.main.dao.NewYearRedWrapperDao;
import com.ruizton.main.model.Fmessage;
import com.ruizton.main.model.Fuser;
import com.ruizton.main.model.NewYearRedWrapper;
import com.ruizton.main.mq.MessageQueueService;
import com.ruizton.util.DateUtils;
import com.ruizton.util.StringUtils;
import com.ruizton.util.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * 春节红包活动
 *
 * 招股金服
 * CopyRight : www.zhgtrade.com
 * Author : xuelin
 * Date： 2016/12/27
 */
@Service
public class NewYearRedWrapperService {
    @Autowired
    private FuserDAO fuserDAO;
    @Autowired
    private FwalletDAO fwalletDAO;
    @Autowired
    private FmessageDAO fmessageDAO;
    @Autowired
    private JedisPool jedisPool;
    @Autowired
    private ConstantMap constantMap;
    @Autowired
    private NewYearRedWrapperDao newYearRedWrapperDao;
    @Autowired
    private MessageQueueService messageQueueService;

    public void save(NewYearRedWrapper redWrapper){
        newYearRedWrapperDao.save(redWrapper);
    }

    public void update(NewYearRedWrapper redWrapper){
        newYearRedWrapperDao.update(redWrapper);
    }

    public NewYearRedWrapper findById(int id){
        return newYearRedWrapperDao.findById(id);
    }

    private final static String RANDOM_SAMPLE_SPACE_KEY = "newYearRandomSampleSpace";       // 新春红包随机抽样最大空间
    private final static String RED_WRAPPER_START_TIME = "newYearRedWrapperStartTime";
    private final static String RED_WRAPPER_END_TIME = "newYearRedWrapperEndTime";
    private final static String RED_WRAPPER_DRAW_TIMES = "newYearRedWrapperDrawTimes";

    private final static String RED_WRAPPER_CHAT_RATE = "newYearRedWrapperChatRate";
    private final static String RED_WRAPPER_COMMENT_RATE = "newYearRedWrapperCommentRate";
    private final static String RED_WRAPPER_TRADE_RATE = "newYearRedWrapperTradeRate";

    private static String luaScript;

    static {
        luaScript = buildLuaScript();
    }

    /**
     * 聊天抽取红包
     *
     * @param fuser
     * @return
     */
    public double drawChatRedWrapper(Fuser fuser){
        if(100 - new SecureRandom().nextInt(100) > constantMap.getInt(RED_WRAPPER_CHAT_RATE)){
            return 0;
        }
        return drawRedWrapper(fuser, NewYearRedWrapper.CHAT_TYPE, false);
    }

    /**
     * 新闻评论 抽取红包
     *
     * @param fuser
     * @return
     */
    public double drawCommentRedWrapper(Fuser fuser){
        if(100 - new SecureRandom().nextInt(100) > constantMap.getInt(RED_WRAPPER_COMMENT_RATE)){
            return 0;
        }
        return drawRedWrapper(fuser, NewYearRedWrapper.NEWS_COMMENT_TYPE, false);
    }

    /**
     * 交易抽取红包
     *
     * @param fuser
     * @return
     */
    public double drawTradeRedWrapper(Fuser fuser){
        if(100 - new SecureRandom().nextInt(100) > constantMap.getInt(RED_WRAPPER_TRADE_RATE)){
            return 0;
        }
        return drawRedWrapper(fuser, NewYearRedWrapper.TRADE_TYPE, true);
    }

    /**
     * 分配红包脚本
     *
     * @return
     */
    private static String buildLuaScript() {
        StringBuilder scriptBuf = new StringBuilder();

        scriptBuf.append("local cachePrefix = 'cache:redWrapper:' .. ARGV[1]\n");
        scriptBuf.append("local limitTimes = tonumber(ARGV[10])\n");

        // 用户抽取次数限制(10次)
        scriptBuf.append("local count = redis.call('hget', cachePrefix , 'users:' .. ARGV[4])\n");
        scriptBuf.append("count = count and count * 1 or 0\n");
        scriptBuf.append("if limitTimes > 0 and count >= limitTimes then\n");
        scriptBuf.append("  return '0'\n");
        scriptBuf.append("end\n");

        // 剩余金额判断
        scriptBuf.append("local remainAmount = redis.call('hget', cachePrefix, 'amount')\n");
        scriptBuf.append("remainAmount = remainAmount and remainAmount * 1 or 0\n");
        scriptBuf.append("if remainAmount <= 0 then\n");
        scriptBuf.append("  return '0'\n");
        scriptBuf.append("end\n");

        // 剩余份数判断
        scriptBuf.append("local remainCount = redis.call('hget', cachePrefix, 'count')\n");
        scriptBuf.append("remainCount = remainCount and remainCount * 1 or 0\n");
        scriptBuf.append("if remainCount <= 0 then\n");
        scriptBuf.append("  return '0'\n");
        scriptBuf.append("end\n");

        // 红包分配算法
        scriptBuf.append("local amount\n");
        scriptBuf.append("if 1 == remainCount then\n");
        scriptBuf.append("  amount = remainAmount\n");
        scriptBuf.append("else\n");
        scriptBuf.append("  local randomRate = tonumber(ARGV[3])\n");
        scriptBuf.append("  if 'true' == ARGV[6] then\n");
        scriptBuf.append("      amount = tonumber(ARGV[8]) + tonumber(ARGV[9]) * randomRate\n");
        scriptBuf.append("  else\n");
        scriptBuf.append("      amount = remainAmount / remainCount * randomRate * 2\n");
        scriptBuf.append("  end\n");
        scriptBuf.append("end\n");

        // 金额格式化
        scriptBuf.append("local min = 0.01\n");
        scriptBuf.append("local numStr = tostring(amount * 100)\n");
        scriptBuf.append("local _, index = string.find(numStr, '%.')\n");
        scriptBuf.append("numStr = string.sub(numStr, 0, index - 1)\n");
        scriptBuf.append("amount = tonumber(numStr) / 100\n");
        scriptBuf.append("amount = amount < min and min or amount\n");
        scriptBuf.append("amount = amount > remainAmount and remainAmount or amount\n");

        // 用户数据保存
        scriptBuf.append("redis.call('hset', cachePrefix, 'users:' .. ARGV[4], count + 1)\n");
        scriptBuf.append("redis.call('zadd', 'cache:redWrapper:newyear:list', numStr * 1, cjson.encode({tonumber(ARGV[5]), amount, tonumber(ARGV[2]), ARGV[7], string.sub(ARGV[3], -5)}))\n");

        // 剩余金额及份数保存
        scriptBuf.append("redis.call('hset', cachePrefix, 'amount', remainAmount - amount)\n");
        scriptBuf.append("redis.call('hset', cachePrefix, 'count', remainCount - 1)\n");
        scriptBuf.append("return tostring(amount)\n");

        return scriptBuf.toString();
    }

    /**
     * base[0] 基数
     * base[1] 偏移量
     *
     * 大红包概率模型 base[0] + base[1] * random
     *
     * @return
     */
    private float[] getRandomBaseAmount(){
        int num = new SecureRandom().nextInt(constantMap.getInt(RANDOM_SAMPLE_SPACE_KEY));

        float[] base = new float[2];
        if(0 == num){
            base[0] = 75;
            base[1] = 25;
        }else if(num <= 3){
            base[0] = 50;
            base[1] = 25;
        }else if(num <= 8){
            base[0] = 30;
            base[1] = 20;
        }else if(num <= 13){
            base[0] = 15;
            base[1] = 15;
        }else if(num <= 20){
            base[0] = 5;
            base[1] = 10;
        }else if(num <= 30){
            base[0] = 1;
            base[1] = 4;
        }else if(num <= 40){
            base[0] = 0.1f;
            base[1] = 0.99f;
        }else{
            base[0] = 0.01f;
            base[1] = 0.09f;
        }

        return base;
    }

    public double drawRedWrapper(Fuser fuser, short type, boolean isBig){
        Date curTime = new Date();
        if(curTime.before(DateUtils.formatDate(constantMap.getString(RED_WRAPPER_START_TIME), "yyyy-MM-dd HH:mm:ss")) || curTime.after(DateUtils.formatDate(constantMap.getString(RED_WRAPPER_END_TIME), "yyyy-MM-dd HH:mm:ss"))){
            // 不在抽奖时间内
            return 0;
        }

        double amount = 0;

        try(Jedis jedis = jedisPool.getResource()) {
            List args = new ArrayList<>(10);
            args.add(StringUtils.string2UTF8Bytes(DateUtils.formatDate(curTime)));
            args.add(StringUtils.string2UTF8Bytes(String.valueOf(curTime.getTime())));
            double nn = 1 - new SecureRandom().nextDouble();
            nn = nn > 0.999 ? 1: nn;
            args.add(StringUtils.string2UTF8Bytes(String.valueOf(nn)));
            args.add(StringUtils.string2UTF8Bytes(String.valueOf(fuser.getFid())));
            args.add(StringUtils.string2UTF8Bytes(String.valueOf(type)));
            args.add(StringUtils.string2UTF8Bytes(String.valueOf(isBig)));
            args.add(StringUtils.string2UTF8Bytes(fuser.getFloginName()));
            if(isBig){
                float base[] = getRandomBaseAmount();
                args.add(StringUtils.string2UTF8Bytes(String.valueOf(base[0])));
                args.add(StringUtils.string2UTF8Bytes(String.valueOf(base[1])));
            }else{
                args.add(StringUtils.string2UTF8Bytes("0"));
                args.add(StringUtils.string2UTF8Bytes("0"));
            }
            args.add(StringUtils.string2UTF8Bytes(constantMap.getString(RED_WRAPPER_DRAW_TIMES)));

            byte[] retBytes = (byte[]) jedis.eval(StringUtils.string2UTF8Bytes(luaScript), Collections.emptyList(), args);
            amount = Double.valueOf(StringUtils.bytes2UTF8String(retBytes));
        } catch (Exception e) {
            e.printStackTrace();
        }

        if(amount > 0){
            // 入账
            NewYearRedWrapper redWrapper = new NewYearRedWrapper(fuser.getFid(), amount, type, curTime);
            messageQueueService.publish(NewYearRedWrapper.NEW_YEAR_RED_WRAPPER_QUEUE, redWrapper);
        }

        return amount;
    }

    /**
     * 钱包入账
     *
     * @param redWrapper
     */
    public void updateDrawWrapperMoneyToWallet(NewYearRedWrapper redWrapper){
        redWrapper.setUpdateTime(Utils.getTimestamp());
        newYearRedWrapperDao.save(redWrapper);

        Fuser fuser = fuserDAO.findById(redWrapper.getUserId());
        fwalletDAO.updateFwalletMoney(fuser.getFwallet().getFid(), redWrapper.getAmount());

        // 消息通知
        Fmessage fmessage = new Fmessage();
        if(NewYearRedWrapper.CHAT_TYPE == redWrapper.getType()){
            fmessage.setFtitle("恭喜您，参与众股资讯七嘴八舌聊天获得" + redWrapper.getAmount() + "元新春红包！");
        }else if(NewYearRedWrapper.NEWS_COMMENT_TYPE == redWrapper.getType()){
            fmessage.setFtitle("恭喜您，参与众股资讯新闻评论获得" + redWrapper.getAmount() + "元新春红包！");
        }else if(NewYearRedWrapper.TRADE_TYPE == redWrapper.getType()){
            fmessage.setFtitle("恭喜您，参与众股交易获得" + redWrapper.getAmount() + "元新春红包！");
        }
        fmessage.setFstatus_s(fmessage.getFtitle());
        fmessage.setFcontent(fmessage.getFtitle());
        fmessage.setFstatus(MessageStatusEnum.NOREAD_VALUE);
        fmessage.setFcreateTime(Utils.getTimestamp());
        fmessage.setFreceiver(fuser);
        fmessageDAO.save(fmessage);
    }

    public List list(String date, Short type, Integer firstResult, Integer maxResult, String orderField, String orderDirection){
        return newYearRedWrapperDao.list(date, type, firstResult, maxResult, orderField, orderDirection);
    }

    public int count(String date, Short type){
        return newYearRedWrapperDao.count(date, type);
    }

    public static void main(String[] args) {
        System.out.println(new SecureRandom().nextDouble());
    }

    public List<NewYearRedWrapper> findByUser(int userId, int size){
        return newYearRedWrapperDao.findByUser(userId, size);
    }

    public List findRank(int size){
        return newYearRedWrapperDao.findRank(size);
    }
}













