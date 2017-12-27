package com.zhgtrade.front.controller;

import com.ruizton.main.model.Fuser;
import com.ruizton.main.model.NewYearRedWrapper;
import com.ruizton.main.service.activity.NewYearRedWrapperService;
import com.ruizton.util.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 招股金服
 * CopyRight : www.zhgtrade.com
 * Author : xuelin
 * Date： 2016/12/27
 */
@RestController
public class NewYearRedWrapperController {
    @Autowired
    private JedisPool jedisPool;
    @Autowired
    private NewYearRedWrapperService newYearRedWrapperService;

    @RequestMapping("/account/drawChatNewYearRedWrapper")
    public Object drawChatNewYearRedWrapper(HttpSession session){
        Fuser fuser = (Fuser) session.getAttribute(Constants.USER_LOGIN_SESSION);
        double amount = newYearRedWrapperService.drawChatRedWrapper(fuser);

        Map map = new HashMap<>();
        map.put("code", 200);
        map.put("amount", amount);
        return map;
    }

    @RequestMapping("/account/drawCommentNewYearRedWrapper")
    public Object drawCommentNewYearRedWrapper(HttpSession session){
        Fuser fuser = (Fuser) session.getAttribute(Constants.USER_LOGIN_SESSION);
        double amount = newYearRedWrapperService.drawCommentRedWrapper(fuser);

        Map map = new HashMap<>();
        map.put("code", 200);
        map.put("amount", amount);
        return map;
    }

    @RequestMapping("/account/drawTradeNewYearRedWrapper")
    public Object drawTradeNewYearRedWrapper(HttpSession session){
        Fuser fuser = (Fuser) session.getAttribute(Constants.USER_LOGIN_SESSION);
        double amount = newYearRedWrapperService.drawTradeRedWrapper(fuser);

        Map map = new HashMap<>();
        map.put("code", 200);
        map.put("amount", amount);
        return map;
    }

    @RequestMapping("/red/newYearRedWrapperList")
    public Object newYearRedWrapperList(@RequestParam(defaultValue = "50") int size){
        if(size > 200){
            size = 200;
        }

        Map retMap = new HashMap<>();
        retMap.put("code", 200);
        retMap.put("list", newYearRedWrapperService.findRank(size));
        return retMap;
    }

    @RequestMapping("/account/newYearRedWrapperRecord")
    public Object newYearRedWrapperRecord(HttpSession session, @RequestParam(required = false, defaultValue = "100")int size){
        if(size > 100){
            size = 100;
        }

        Map retMap = new HashMap<>();
        Fuser fuser = (Fuser) session.getAttribute(Constants.USER_LOGIN_SESSION);
        if(null == fuser){
            retMap.put("code", 401);
            return retMap;
        }

        List<NewYearRedWrapper> list = newYearRedWrapperService.findByUser(fuser.getFid(), size);

        List retList = new ArrayList<>(list.size());
        HashMap map = null;
        for(NewYearRedWrapper redWrapper : list){
            if(null == map){
                map = new HashMap();
            }else if(!map.isEmpty()){
                map = (HashMap) map.clone();
            }
            map.put("id", redWrapper.getId());
            map.put("type", redWrapper.getType());
            map.put("amount", redWrapper.getAmount());
            map.put("drawTime", redWrapper.getCreateTime());
            retList.add(map);
        }

        retMap.put("code", 200);
        retMap.put("list", retList);
        return retMap;
    }
}
