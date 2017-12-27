package com.zhgtrade.front.transfer;

import com.ruizton.util.HttpUtils;
import com.ruizton.util.SignatureUtil;
import org.junit.Test;

import java.util.Map;
import java.util.TreeMap;

/**
 * 招股金服
 * CopyRight : www.zhgtrade.com
 * Author : xuelin
 * Date： 2016/7/26
 */
public class TransferControllerTest {

    @Test
    public void transferNotifyTest(){
        Map<String, Object> map = new TreeMap<>();
        map.put("orderId", 10);
        map.put("tradeNo", "bwsetheb");
        map.put("amount", 50.0);
        map.put("from", "zhg");
        map.put("to", "zhg");
        map.put("secret", "ab370b48842017c5a4b8c45f29ac8b20f300d5d2");
        map.put("sign", SignatureUtil.getSign(map));

        String result = HttpUtils.sendPostRequest("http://127.0.0.1:8880/account/transfer_notify.html", map);
        System.out.println(result);
    }

    @Test
    public void receiveTransferTest(){
        Map<String, Object> map = new TreeMap<>();
        map.put("openId", "2E87C88912774303B8C993EA4F1648A7");
        map.put("tradeNo", "bwsthe");
        map.put("amount", 50.0);
        map.put("moneyType", 0);
        map.put("coinId", 1);
        map.put("from", "zhg");
        map.put("to", "zhg");
        map.put("secret", "ab370b48842017c5a4b8c45f29ac8b20f300d5d2");
        map.put("sign", SignatureUtil.getSign(map));

        String result = HttpUtils.sendPostRequest("http://127.0.0.1:8880/account/receive_transfer.html", map);
        System.out.println(result);
    }

}
