package com.ruizton.main.controller.admin;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ruizton.util.Constants;
import com.ruizton.util.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;

/**
 * 比特家
 * CopyRight : www.btc58.cc
 * Author : xxp
 * Date： 2016/9/29
 */
@Controller
@RequestMapping("/ssadmin")
public class ChatMessageController {
    @Autowired
    private JedisPool jedisPool;

    private static String CHIT_CHAT_KEY = "news:irrList";
    private static String SENSITIVE_WORDS_KEY = "news:config:keywords";

    /**
     * 七嘴八舌列表
     *
     * @param modelMap
     * @return
     */
    @RequestMapping("/chitChatList")
    @RequiresPermissions(("ssadmin/chitChatList.html"))
    public Object chitChatList(ModelMap modelMap){
        try(Jedis jedis = jedisPool.getResource()) {
            List<String> msgList = jedis.lrange(CHIT_CHAT_KEY, 0, -1);
            List list = new ArrayList<>(msgList.size());
            msgList.forEach(e -> {
                JSONObject obj = JSON.parseObject(e);
                try {
                    String encodeMsg = Base64.getEncoder().encodeToString(e.getBytes(Constants.UTF8_CHARSET));
                    obj.put("msg_sources", URLEncoder.encode(encodeMsg, Constants.UTF8_CHARSET));
                } catch (UnsupportedEncodingException e1) {
                    e1.printStackTrace();
                }
                list.add(obj);
            });
            modelMap.put("list", list);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "ssadmin/chitChatList";
    }

    /**
     * 删除七嘴八舌聊天信息
     *
     * @param modelMap
     * @return
     */
    @RequestMapping("/delChitChatMessage")
    @RequiresPermissions("ssadmin/delChitChatMessage.html")
    public Object delChitChatMessage(ModelMap modelMap, String message){
        boolean result;
        try(Jedis jedis = jedisPool.getResource()) {
            System.out.println("message: " + message);
            result = jedis.lrem(CHIT_CHAT_KEY.getBytes(), 1, Base64.getDecoder().decode(message.getBytes(Constants.UTF8_CHARSET))) > 0;
        } catch (Exception e) {
            e.printStackTrace();
            result = false;
        }

        if(result){
            modelMap.put("statusCode",200);
            modelMap.put("message","删除成功");
        }else{
            modelMap.put("statusCode",300);
            modelMap.put("message","删除失败");
        }

        return "ssadmin/comm/ajaxDone";
    }

    /**
     * 敏感词汇列表
     *
     * @param modelMap
     * @return
     */
    @RequestMapping("/sensitiveWords")
    @RequiresPermissions("ssadmin/sensitiveWords.html")
    public Object sensitiveWords(ModelMap modelMap){

        try(Jedis jedis = jedisPool.getResource()){
            Map<byte[], byte[]> dbMap = jedis.hgetAll(SENSITIVE_WORDS_KEY.getBytes(Constants.UTF8_CHARSET));
            List list = new ArrayList<>(dbMap.size());
            dbMap.forEach((keyBytes, valBytes) -> {
                JSONObject json = new JSONObject();
                try {
                    json.put("key", new String(keyBytes, Constants.UTF8_CHARSET));
                    json.put("val", new String(valBytes, Constants.UTF8_CHARSET));
                    json.put("id_key", URLEncoder.encode(Base64.getEncoder().encodeToString(keyBytes), Constants.UTF8_CHARSET));
                    list.add(json);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            });
            modelMap.put("words", list);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "ssadmin/sensitiveWords";
    }

    @RequestMapping("/editSensitiveWord")
    public Object editSensitiveWord(ModelMap modelMap, @RequestParam(required = false)String key){
        if(null != key){
            try(Jedis jedis = jedisPool.getResource()) {
                byte[] keyBytes = Base64.getDecoder().decode(key.getBytes(Constants.UTF8_CHARSET));
                byte[] valBytes = jedis.hget(SENSITIVE_WORDS_KEY.getBytes(Constants.UTF8_CHARSET), keyBytes);
                modelMap.put("key", new String(keyBytes, Constants.UTF8_CHARSET));
                modelMap.put("val", new String(valBytes, Constants.UTF8_CHARSET));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return "ssadmin/editSensitiveWord";
    }

    @RequestMapping(value = {"/addSensitiveWord", "/updateSensitiveWord"})
    @RequiresPermissions(value = {"ssadmin/addSensitiveWord.html", "ssadmin/updateSensitiveWord.html"})
    public Object updateSensitiveWord(ModelMap modelMap, String key, String val){
        boolean result = false;
        try(Jedis jedis = jedisPool.getResource()) {
            result = jedis.hset(SENSITIVE_WORDS_KEY.getBytes(Constants.UTF8_CHARSET), key.getBytes(Constants.UTF8_CHARSET), val.getBytes(Constants.UTF8_CHARSET)) >= 0;
        } catch (Exception e) {
            e.printStackTrace();
        }

        if(result){
            modelMap.put("statusCode",200);
            modelMap.put("message","保存成功");
            modelMap.put("callbackType","closeCurrent");
        }else{
            modelMap.put("statusCode",300);
            modelMap.put("message","保存失败");
        }

        return "ssadmin/comm/ajaxDone";
    }

    @RequestMapping("/delSensitiveWord")
    @RequiresPermissions("ssadmin/delSensitiveWord.html")
    public Object delSensitiveWord(ModelMap modelMap, String keys){
        boolean result = false;
        try(Jedis jedis = jedisPool.getResource()){
            keys = URLDecoder.decode(keys, Constants.UTF8_CHARSET);
            String[] keyArr = StringUtils.delimitedListToStringArray(keys, ",");
            byte[][] keyBytes = new byte[keyArr.length][1];
            for(int i=0,len=keyArr.length; i<len; i++){
                keyBytes[i] = Base64.getDecoder().decode(keyArr[i].getBytes(Constants.UTF8_CHARSET));
            }
            result = jedis.hdel(SENSITIVE_WORDS_KEY.getBytes(Constants.UTF8_CHARSET), keyBytes) > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }

        if(result){
            modelMap.put("statusCode",200);
            modelMap.put("message","删除成功");
        }else{
            modelMap.put("statusCode",300);
            modelMap.put("message","删除失败");
        }

        return "ssadmin/comm/ajaxDone";
    }
}
