package com.zhgtrade.front.controller.account;

import com.ruizton.main.Enum.*;
import com.ruizton.main.cache.data.RealTimeDataService;
import com.ruizton.main.code.Captcha;
import com.ruizton.main.comm.KeyValues;
import com.ruizton.main.dto.LatestDealData;
import com.ruizton.main.model.Fdeduct;
import com.ruizton.main.model.Fuser;
import com.ruizton.main.model.Fvirtualcointype;
import com.ruizton.main.model.ProjectCollection;
import com.ruizton.main.service.admin.DeductService;
import com.ruizton.main.service.front.*;
import com.ruizton.util.Constants;
import com.ruizton.util.Utils;
import com.zhgtrade.front.controller.BaseController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * 我的收藏
 * 比特家
 * CopyRight : www.zhgtrade.com
 * Author : yujie（945351749@qq.com）
 * Date：2016/07/04
 */
@Controller
@RequestMapping("/account")
public class CollectionController extends BaseController{

    private final Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private ProjectCollectionService projectCollectionService;
    @Autowired
    private RealTimeDataService realTimeDataService;
    @Autowired
    private FrontVirtualCoinService frontVirtualCoinService;

    @RequestMapping("/collection")
    public ModelAndView collection(HttpServletRequest request) {
        ModelAndView modelAndView=new ModelAndView();
        Fuser fuser = getSessionUser(request);

        if (fuser != null){

            //已收藏
            List<ProjectCollection> projectList = projectCollectionService.listByUser(fuser.getFid());
            List<Map<String, Object>> collectionList = new ArrayList<Map<String, Object>>();
            for (ProjectCollection projectCollection:projectList) {
                Map<String, Object> map = new HashMap<String, Object>();
                int cid = projectCollection.getCid();
                LatestDealData lastestDealData = realTimeDataService.getLatestDealData(cid);
                if(lastestDealData != null){
                    map.put("lastDealPrize", lastestDealData.getLastDealPrize());
                    map.put("higestBuyPrize", lastestDealData.getHigestBuyPrize());
                    map.put("lowestSellPrize", lastestDealData.getLowestSellPrize());
                    map.put("volumn", lastestDealData.getVolumn());
                    map.put("fupanddown", lastestDealData.getFupanddown());
                }
                map.put("cid", cid);
                map.put("fname", lastestDealData.getFname());
                map.put("furl", lastestDealData.getFurl());
                map.put("createTime", projectCollection.getCreateTime());
                collectionList.add(map);

            }
            modelAndView.addObject("collectionlist", collectionList);

            //未收藏列表
            List<Object> unCollectionList = new ArrayList<Object>();
            List<Integer> intList = projectCollectionService.listUncollectedProjectId(fuser.getFid());
            for (Integer cid : intList){
                LatestDealData lastestDealData = realTimeDataService.getLatestDealData(cid);
                if(lastestDealData != null){
                    unCollectionList.add(lastestDealData);
                }else {
                    unCollectionList.add(frontVirtualCoinService.findFvirtualCoinById(cid));
                }
            }
            modelAndView.addObject("uncollectionlist", unCollectionList);
            modelAndView.setViewName("account/account_collection");
        }else {
            modelAndView.setViewName("index");
        }
        return modelAndView;
    }

    @ResponseBody
    @RequestMapping("/cancel_collection")
    public Map<String, Object> cancelCollection(HttpServletRequest request, int id){
        Map<String, Object> map = new HashMap<String, Object>();
        Fuser fuser = getSessionUser(request);
        if(fuser != null){
            int updateRow = 0;
            try {
                updateRow = projectCollectionService.deleteByUserAndCoin(fuser.getFid(), id);
                if(updateRow == 1){
                    map.put("msg", "取消成功");
                    map.put("code", "200");
                }else {
                    map.put("msg", "取消失败");
                    map.put("code", "400");
                }
            }catch (Exception e){
                logger.error(e.getMessage());
                throw e;
            }
        }
        return map;
    }


    @ResponseBody
    @RequestMapping("/add_collection")
    public Map<String, Object> addCollection(HttpServletRequest request, int id){
        Fuser fuser = getSessionUser(request);
        Map<String, Object> map = new HashMap<String, Object>();
        if(fuser != null){
            if(isValidProject(id)){

                ProjectCollection projectCollection = projectCollectionService.findByUserAndCoin(fuser.getFid(), id);
                if(projectCollection == null){
                    projectCollection = new ProjectCollection(fuser.getFid(), id, Utils.getTimestamp());
                    projectCollectionService.save(projectCollection);
                    map.put("msg", "收藏成功");
                    map.put("code", "200");
                }else {
                    map.put("msg", "已经收藏");
                    map.put("code", "403");
                }
            }else {
                map.put("msg", "项目无效");
                map.put("code", "400");
            }
        }
        return map;
    }


    /**
     * 判断项目id是否有效，存在且状态为1的时候有效
     * @param id
     * @return
     */
    private boolean isValidProject(int id){
        Fvirtualcointype fvirtualcointype = frontVirtualCoinService.findFvirtualCoinById(id);
        if(fvirtualcointype != null && fvirtualcointype.getFstatus() == 1){
            return true;
        }
        return false;
    }

}
