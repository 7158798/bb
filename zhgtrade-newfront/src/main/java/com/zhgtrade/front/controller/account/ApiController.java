package com.zhgtrade.front.controller.account;

import com.ruizton.main.Enum.TradeApiType;
import com.ruizton.main.Enum.UseStatus;
import com.ruizton.main.model.Fuser;
import com.ruizton.main.model.TradeApi;
import com.ruizton.main.service.front.FrontUserService;
import com.ruizton.main.service.front.TradeApiService;
import com.ruizton.util.DateUtils;
import com.ruizton.util.Utils;
import com.zhgtrade.front.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * api
 *
 * 招股金服
 * CopyRight : www.zhgtrade.com
 * Author : xuelin（1186270005@qq.com）
 * Date：
 */
@Controller
@RequestMapping("/account")
public class ApiController extends BaseController {
    @Autowired
    private TradeApiService tradeApiService;
    @Autowired
    private FrontUserService frontUserService;

    // 最多5个api
    private final static int API_NUM = 5;

    /**
     * yujie
     * @return
     */
    @RequestMapping("/api")
    public ModelAndView api(HttpServletRequest request){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("account/account_api");

        Fuser fuser = getSessionUser(request);
        List<TradeApi> list = tradeApiService.listByUser(fuser.getFid());
        modelAndView.addObject("list", list);

        return modelAndView;
    }

    /**
     * api申请
     *
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/apply_api", method = RequestMethod.POST)
    public Object apply(HttpServletRequest request,
                        @RequestParam(value = "name") String name,
                        @RequestParam(value = "type") int type,
                        @RequestParam(value = "password") String password){

        Fuser fuser = getSessionUser(request);

        Map<String, Object> retMap = new HashMap<>(2);
        TradeApiType apiType = TradeApiType.get(type);
        if(null == apiType){
            retMap.put("code", 101);
            return retMap;
        }

        if(!Utils.MD5(password).equals(fuser.getFtradePassword())){
            // 交易密码不正确
            retMap.put("code", 201);
            return retMap;
        }

        if(tradeApiService.countByUser(fuser.getFid()) >= API_NUM){
            // api分配过多
            retMap.put("code", 203);
            return retMap;
        }

        TradeApi tradeApi = new TradeApi();
        tradeApi.setUserId(fuser.getFid());
        tradeApi.setName(name);
        tradeApi.setType(apiType);
        tradeApiService.insertApplyApi(tradeApi);

        retMap.put("code", 200);
        retMap.put("id", tradeApi.getId());
        retMap.put("name", tradeApi.getName());
        retMap.put("key", tradeApi.getApiKey());
        retMap.put("secret", tradeApi.getSecret());
        retMap.put("status", tradeApi.getStatus().getIndex());
        retMap.put("type", tradeApi.getType().getIndex());
        retMap.put("type_name", tradeApi.getType().getName());
        retMap.put("create_time", DateUtils.formatDate(tradeApi.getCreateTime()));
        return retMap;
    }

    /**
     * api更新
     * @param request
     * @param name
     * @param type
     * @param password
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/update_api", method = RequestMethod.POST)
    public Object refreshApi(HttpServletRequest request,
                             @RequestParam(value = "id") int id,
                             @RequestParam(value = "name") String name,
                             @RequestParam(value = "type") int type,
                             @RequestParam(value = "password") String password){
        Fuser fuser = getSessionUser(request);

        Map<String, Object> retMap = new HashMap<>(2);
        TradeApiType apiType = TradeApiType.get(type);
        if(null == apiType){
            retMap.put("code", 101);
            return retMap;
        }
        if(!Utils.MD5(password).equals(fuser.getFtradePassword())){
            // 交易密码不正确
            retMap.put("code", 201);
            return retMap;
        }

        TradeApi tradeApi = tradeApiService.findById(id);
        if(tradeApi.getUserId() != fuser.getFid()){
            // 非法操作
            retMap.put("code", 101);
            return retMap;
        }

        tradeApi.setName(name);
        tradeApi.setType(apiType);
        tradeApiService.update(tradeApi);

        retMap.put("code", 200);
        retMap.put("id", tradeApi.getId());
        retMap.put("name", tradeApi.getName());
        retMap.put("status", tradeApi.getStatus().getIndex());
        retMap.put("type", tradeApi.getType().getIndex());
        retMap.put("type_name", tradeApi.getType().getName());
        retMap.put("create_time", DateUtils.formatDate(tradeApi.getCreateTime()));
        retMap.put("update_time", DateUtils.formatDate(tradeApi.getUpdateTime()));
        return retMap;
    }

    /**
     * 刷新api （key secret）
     *
     * @param request
     * @param id
     * @param password
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/refresh_api")
    public Object refresh(HttpServletRequest request,
                          @RequestParam(value = "id") int id,
                          @RequestParam(value = "password") String password){
        Fuser fuser = getSessionUser(request);

        Map<String, Object> retMap = new HashMap<>(2);
        fuser = frontUserService.findById(fuser.getFid());
        if(!Utils.MD5(password).equals(fuser.getFtradePassword())){
            // 交易密码不正确
            retMap.put("code", 201);
            return retMap;
        }

        TradeApi tradeApi = tradeApiService.findById(id);
        if(tradeApi.getUserId() != fuser.getFid()){
            // 非法操作
            retMap.put("code", 101);
            return retMap;
        }

        tradeApiService.updateRefreshApi(tradeApi);

        retMap.put("code", 200);
        retMap.put("id", tradeApi.getId());
        retMap.put("name", tradeApi.getName());
        retMap.put("key", tradeApi.getApiKey());
        retMap.put("secret", tradeApi.getSecret());
        retMap.put("status", tradeApi.getStatus().getIndex());
        retMap.put("type", tradeApi.getType().getIndex());
        retMap.put("type_name", tradeApi.getType().getName());
        retMap.put("create_time", DateUtils.formatDate(tradeApi.getCreateTime()));
        retMap.put("update_time", DateUtils.formatDate(tradeApi.getUpdateTime()));
        return retMap;
    }

    /**
     * 查看api密钥
     *
     * @param request
     * @param id
     * @param password
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/view_api")
    public Object view(HttpServletRequest request,
                       @RequestParam(value = "id") int id,
                       @RequestParam(value = "password") String password){
        Fuser fuser = getSessionUser(request);

        Map<String, Object> retMap = new HashMap<>(5);
        fuser = frontUserService.findById(fuser.getFid());
        if(!Utils.MD5(password).equals(fuser.getFtradePassword())){
            // 交易密码不正确
            retMap.put("code", 201);
            return retMap;
        }

        TradeApi tradeApi = tradeApiService.findById(id);
        if(tradeApi.getUserId() != fuser.getFid()){
            // 非法操作
            retMap.put("code", 101);
            return retMap;
        }

        retMap.put("code", 200);
        retMap.put("key", tradeApi.getApiKey());
        retMap.put("secret", tradeApi.getSecret());
        return retMap;
    }
}
