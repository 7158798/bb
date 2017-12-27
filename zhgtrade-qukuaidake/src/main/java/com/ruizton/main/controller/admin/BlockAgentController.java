package com.ruizton.main.controller.admin;

import com.ruizton.main.model.BlockAgent;
import com.ruizton.main.model.type.BlockAgentStatusEnum;
import com.ruizton.main.service.block.BlockAgentService;
import com.ruizton.util.Constants;
import com.ruizton.util.StringUtils;
import com.ruizton.util.Utils;
import com.zhgtrade.front.controller.BaseController;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * 招股金服
 * CopyRight: www.zhgtrade.com
 * Author: xuelin
 * Date: 17-3-1 下午4:25
 */
@Controller
@RequestMapping("/ssadmin")
public class BlockAgentController extends BaseController{
    @Autowired
    private BlockAgentService blockAgentService;

    @RequestMapping("/blockAgentList")
    @RequiresPermissions("ssadmin/blockAgentList.html")
    public ModelAndView blockAgentList(HttpServletRequest request){
        ModelAndView modelAndView = new ModelAndView() ;
        modelAndView.setViewName("ssadmin/blockAgentList") ;
        int currentPage = 1;
        String keyWord = request.getParameter("keywords");
        String orderField = request.getParameter("orderField");
        String orderDirection = request.getParameter("orderDirection");
        String status = request.getParameter("status");
        if(request.getParameter("pageNum") != null){
            currentPage = Integer.parseInt(request.getParameter("pageNum"));
        }

        StringBuffer filter = new StringBuffer();
        filter.append("where 1=1 \n");
        if(keyWord != null && keyWord.trim().length() >0){
            filter.append("and (realName like '%"+keyWord+"%' or mobile like '%"+keyWord+"%' or email like '%"+keyWord+"%' or identifyNo like '%"+keyWord+"%') \n");
            modelAndView.addObject("keywords", keyWord);
        }

        if(StringUtils.hasText(status)){
            filter.append("and status=").append(status).append(" \n");
        }

        if(orderField != null && orderField.trim().length() >0){
            filter.append("order by "+orderField+"\n");
        }else{
            filter.append("order by id \n");
        }

        if(orderDirection != null && orderDirection.trim().length() >0){
            filter.append(orderDirection+"\n");
        }else{
            filter.append("desc \n");
        }

        List<BlockAgent> list = this.blockAgentService.list((currentPage-1)* Constants.PAGE_ITEM_COUNT_40, Constants.PAGE_ITEM_COUNT_40, filter+"",true);
        modelAndView.addObject("list", list);
        modelAndView.addObject("status", status);
        modelAndView.addObject("numPerPage", Constants.PAGE_ITEM_COUNT_40);
        modelAndView.addObject("currentPage", currentPage);
        modelAndView.addObject("rel", "blockAgentList");
        //总数量
        modelAndView.addObject("totalCount", this.blockAgentService.getAllCount("BlockAgent", filter+""));

        return modelAndView;
    }

    @RequestMapping("/auditBlockAgent")
    @RequiresPermissions("ssadmin/auditBlockAgent.html")
    public String auditAgent(int id, int status, ModelMap modelMap){
        BlockAgent agent = blockAgentService.findById(id);
        agent.setStatus(BlockAgentStatusEnum.get(status));
        agent.setUpdateTime(Utils.getTimestamp());
        blockAgentService.updateAgent(agent);

        modelMap.put("statusCode", 200);
        modelMap.put("message", "审核成功");
//        modelMap.put("callbackType", "closeCurrent");

        return "ssadmin/comm/ajaxDone";
    }

    @RequestMapping("/exportBlockAgentList")
    @RequiresPermissions("ssadmin/exportBlockAgentList.html")
    public ModelAndView exportAgentList(HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView() ;
        modelAndView.setViewName("ssadmin/excel/exportBlockAgentList") ;
        String keyWord = request.getParameter("keywords");
        String status = request.getParameter("status");

        StringBuffer filter = new StringBuffer();
        filter.append("where 1=1 \n");
        if(keyWord != null && keyWord.trim().length() >0){
            filter.append("and (realName like '%"+keyWord+"%' or mobile like '%"+keyWord+"%' or email like '%"+keyWord+"%' or identifyNo like '%"+keyWord+"%') \n");
            modelAndView.addObject("keywords", keyWord);
        }
        if(StringUtils.hasText(status)){
            filter.append("and status=").append(status).append(" \n");
        }
        filter.append("order by id \n");

        List<BlockAgent> list = this.blockAgentService.list(0, 0, filter+"",false);
        modelAndView.addObject("list", list);

        return modelAndView;
    }
}
