package com.zhgtrade.front.controller.account;

import com.ruizton.main.Enum.*;
import com.ruizton.main.comm.KeyValues;
import com.ruizton.main.model.Fdeduct;
import com.ruizton.main.model.Fuser;
import com.ruizton.main.model.Fvirtualcointype;
import com.ruizton.main.service.admin.DeductService;
import com.ruizton.main.service.front.FrontAccountService;
import com.ruizton.main.service.front.FrontTradeService;
import com.ruizton.main.service.front.FrontUserService;
import com.ruizton.main.service.front.FrontVirtualCoinService;
import com.ruizton.util.Constants;
import com.zhgtrade.front.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 币对冲
 * 比特家
 * CopyRight : www.zhgtrade.com
 * Author : yujie（945351749@qq.com）
 * Date：
 */
@Controller
@RequestMapping("/account")
public class HedgingController extends BaseController {
    @RequestMapping("/hedging")
    public ModelAndView hedging() {
        ModelAndView modelAndView=new ModelAndView();
        modelAndView.setViewName("account/account_hedging");
        return modelAndView;
    }
}
