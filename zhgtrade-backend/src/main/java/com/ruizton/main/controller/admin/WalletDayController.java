package com.ruizton.main.controller.admin;

import com.ruizton.main.model.Fuser;
import com.ruizton.main.model.UserWalletDay;
import com.ruizton.main.service.admin.UserWalletDayService;
import com.ruizton.util.Utils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by liuyuanbo on 2017/4/17.
 */
@Controller
public class WalletDayController {
    @Autowired
    private UserWalletDayService userWalletDayService;

//    private int size = Utils.getNumPerPage();

    @RequestMapping("/ssadmin/dayWallet")
    @RequiresPermissions("ssadmin/dayWallet.html")
    public ModelAndView Index(HttpServletRequest request) throws Exception{
        int size=15;
        ModelAndView modelAndView = new ModelAndView() ;
        modelAndView.setViewName("ssadmin/dayWallet") ;
        int page = 1;
        if(request.getParameter("pageNum") != null){
            page = Integer.parseInt(request.getParameter("pageNum"));
            System.out.println("page="+page);
        }
        String user=request.getParameter("user");
        String loginName=user.split(":")[1];
        int userId=Integer.parseInt(user.split(":")[0]);
        List<UserWalletDay> list= userWalletDayService.findByUserId(userId,(page-1)*size,size);
        System.out.println("size="+size);
        modelAndView.addObject("numPerPage", size);
        modelAndView.addObject("currentPage", page);
        modelAndView.addObject("loginName", loginName);
        modelAndView.addObject("walletList",list);
        int count=userWalletDayService.totalCount(userId);
        System.out.println(count);
        modelAndView.addObject("totalCount",userWalletDayService.totalCount(userId));
        return modelAndView ;
    }
}
