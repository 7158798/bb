//package com.ruizton.main.controller.admin;
//
//import com.ruizton.main.service.activity.NewYearRedWrapperService;
//import com.ruizton.util.Constants;
//import com.ruizton.util.DateUtils;
//import org.apache.shiro.authz.annotation.RequiresPermissions;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.ModelMap;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import redis.clients.jedis.Jedis;
//import redis.clients.jedis.JedisPool;
//
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.Set;
//
///**
// * 比特家
// * CopyRight : www.btc58.cc
// * Author : xxp
// * Date： 2017/1/4
// */
//@Controller
//@RequestMapping("/ssadmin")
//public class NewYearRedWrapperController {
//    @Autowired
//    private JedisPool jedisPool;
//    @Autowired
//    private NewYearRedWrapperService newYearRedWrapperService;
//
//    /**
//     * 新春红包发送情况
//     *
//     * @param modelMap
//     * @return
//     */
//    @RequestMapping("/newYearRedWrapperDetail")
//    @RequiresPermissions("ssadmin/newYearRedWrapperDetail.html")
//    public Object newYearRedWrapperDetail(ModelMap modelMap){
//        try(Jedis jedis = jedisPool.getResource()){
//            Set<byte[]> set = jedis.keys(("cache:redWrapper:" + DateUtils.formatDate(new Date(), "yyyy-") + "*").getBytes(Constants.UTF8_CHARSET));
//            List list = new ArrayList<>(set.size());
//
//            for(byte[] bytes : set){
//                byte[] amountBytes = jedis.hget(bytes, "amount".getBytes(Constants.UTF8_CHARSET));
//                byte[] countBytes = jedis.hget(bytes, "count".getBytes(Constants.UTF8_CHARSET));
//                Map map = new HashMap<>();
//                map.put("amount", new String(amountBytes, Constants.UTF8_CHARSET));
//                map.put("count", new String(countBytes, Constants.UTF8_CHARSET));
//                map.put("date", new String(bytes, Constants.UTF8_CHARSET).replace("cache:redWrapper:", ""));
//                list.add(map);
//            }
//
//            modelMap.put("list", list);
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//
//        return "ssadmin/newYearRedWrapperDetail";
//    }
//
//    /**
//     * 新春红包获取列表
//     *
//     *
//     * @param modelMap
//     * @return
//     */
//    @RequestMapping("/newYearRedWrapperList")
//    @RequiresPermissions("ssadmin/newYearRedWrapperList.html")
//    public Object newYearRedWrapperList(@RequestParam(required = false)String date,
//                                        @RequestParam(required = false)Short type,
//                                        @RequestParam(required = false, defaultValue = "1")int pageNum,
//                                        @RequestParam(required = false, defaultValue = "w.id")String orderField,
//                                        @RequestParam(required = false, defaultValue = "desc")String orderDirection,
//                                        ModelMap modelMap){
//
//        int total = newYearRedWrapperService.count(date, type);
//        modelMap.put("total", total);
//
//        List list = newYearRedWrapperService.list(date, type, (pageNum - 1) * Constants.PAGE_ITEM_COUNT_40, Constants.PAGE_ITEM_COUNT_40, orderField, orderDirection);
//        modelMap.put("list", list);
//        modelMap.put("list", list);
//        modelMap.put("numPerPage", Constants.PAGE_ITEM_COUNT_40);
//
//        return "ssadmin/newYearRedWrapperList";
//    }
//}
