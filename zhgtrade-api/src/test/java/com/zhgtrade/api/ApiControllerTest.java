package com.zhgtrade.api;

import com.alibaba.fastjson.JSONObject;
import com.ruizton.util.HttpUtils;
import com.ruizton.util.SignatureUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.annotation.Repeat;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Map;
import java.util.TreeMap;

/**
 * 比特家
 * CopyRight : www.btc58.cc
 * Author : xxp
 * Date： 2016/7/19
 */
//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class ApiControllerTest {

    private String key = "ac0cd9f2304844ccaa0606bc4c6d95e7f";
    private String secret = "3c4af8d048c35daedf8fb4086e500122";
    private String host = "http://127.0.0.1:8090";
//    private String host = "http://www.btc58.cc";
//    private String host = "http://www.btc58.cc";

    @Test
    public void test(){

    }

    protected String getSignature(Map<String, Object> paramsMap){
        StringBuilder signBuf = new StringBuilder();
        for(String key : paramsMap.keySet()){
            if(!key.equals("sign")){
                signBuf.append(key).append("=").append(paramsMap.get(key)).append("&");
            }
        }
        signBuf.deleteCharAt(signBuf.length() - 1);

        return SignatureUtil.getSign(signBuf.toString());
    }

    @Test
    public void tickerTest(){
        Map<String, Object> map = new TreeMap<>();
//        map.put("symbol", "2");
        map.put("key", key);
        map.put("secret", secret);
        map.put("sign", getSignature(map));

//        System.out.println(map);
        JSONObject json = HttpUtils.sendGetRequestForJson(host + "/api/v1/tickers", map);
        System.out.println(json);
    }

    @Test
    public void tickersTest(){
        Map<String, Object> map = new TreeMap<>();
        map.put("key", key);
        map.put("secret", secret);
        map.put("sign", getSignature(map));

//        System.out.println(map);
        JSONObject json = HttpUtils.sendGetRequestForJson(host + "/api/v1/tickers", map);
        System.out.println(json);
    }

    @Test
    public void depthTest(){
        Map<String, Object> map = new TreeMap<>();
        map.put("symbol", "1");
        map.put("merge", "4");
        map.put("key", key);
        map.put("secret", secret);
        map.put("sign", getSignature(map));

        System.out.println(map);
        JSONObject json = HttpUtils.sendGetRequestForJson(host + "/api/v1/depth", map);
        System.out.println(json);
    }

    @Test
    public void tradesTest(){
        Map<String, Object> map = new TreeMap<>();
        map.put("symbol", "15");
        map.put("size", "100");
        map.put("key", key);
        map.put("secret", secret);
        map.put("sign", getSignature(map));

        System.out.println(map);
        JSONObject json = HttpUtils.sendGetRequestForJson(host + "/api/v1/orders", map);
        System.out.println(json);
    }

    @Test
    public void userInfoTest(){
        Map<String, Object> map = new TreeMap<>();
        map.put("key", key);
        map.put("secret", secret);
        map.put("sign", getSignature(map));

        System.out.println(map);
        JSONObject json = HttpUtils.sendGetRequestForJson(host + "/api/v1/balance", map);
        System.out.println(json);
    }

    @Test
    public void walletTest(){
        Map<String, Object> map = new TreeMap<>();
        map.put("symbol", "3");
        map.put("key", key);
        map.put("secret", secret);
        map.put("sign", getSignature(map));

        System.out.println(map);
        JSONObject json = HttpUtils.sendGetRequestForJson(host + "/api/v1/wallet", map);
        System.out.println(json);
    }

    @Test
    public void tradeListTest(){
        Map<String, Object> map = new TreeMap<>();
        map.put("symbol", "28");
        map.put("size", "50");
        map.put("type", "0");
//        map.put("since", DateUtils.formatDate("2016-06-15").getTime() / 1000);
        map.put("key", key);
        map.put("secret", secret);
        map.put("sign", getSignature(map));

        System.out.println(map);
        JSONObject json = HttpUtils.sendGetRequestForJson(host + "/api/v1/trade_list", map);
        System.out.println(json);
    }

    @Test
    public void tradeInfoTest(){
        Map<String, Object> map = new TreeMap<>();
        map.put("id", "6308173");
        map.put("key", key);
        map.put("secret", secret);
        map.put("sign", getSignature(map));

        System.out.println(map);
        JSONObject json = HttpUtils.sendGetRequestForJson(host + "/api/v1/trade_view", map);
        System.out.println(json);
    }

    @Test
//    @Repeat(100)
    public void cancelEntrustTest(){
        Map<String, Object> map = new TreeMap<>();
        map.put("id", "6308173");
        map.put("key", key);
        map.put("secret", secret);
        map.put("sign", getSignature(map));

        System.out.println(map);
        JSONObject json = HttpUtils.sendPostRequestForJson(host + "/api/v1/cancel_trade", map);
        System.out.println(json);
    }

    @Test
//    @Repeat(2)
    public void tradeTest(){
        Map<String, Object> map = new TreeMap<>();
        map.put("symbol", "32");
        map.put("type", "sell");
//        map.put("type", "buy");
        map.put("price", "70");
        map.put("amount", "1000");
        map.put("key", key);
        map.put("secret", secret);
        map.put("sign", getSignature(map));

        System.out.println(map);
        JSONObject json = HttpUtils.sendPostRequestForJson(host + "/api/v1/trade", map);
        System.out.println(json);
    }

}
