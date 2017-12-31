package com.zhgtrade.front.controller.account;

import com.ruizton.main.model.Fintrolinfo;
import com.ruizton.main.model.Fuser;
import com.ruizton.main.service.admin.AdminService;
import com.ruizton.main.service.admin.IntrolinfoService;
import com.ruizton.main.service.admin.UserService;
import com.ruizton.util.Constants;
import com.zhgtrade.front.controller.BaseController;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

/**
 * DESC:
 * <p/>
 * 比特家
 * CopyRight : www.btc58.cc
 * Author : xxp(xxly68@qq.com)
 * Date： 2016-05-12 14:38
 */
@Controller
@RequestMapping("/account")
class SpreadController extends BaseController {
    @Autowired
    private AdminService adminService;
    @Autowired
    private UserService userService;
    @Autowired
    private IntrolinfoService introlinfoService;

    @RequestMapping("/spread")
    public String spreadCenter(HttpServletRequest request, Map<String, Object> map) {
        String userId = String.valueOf(getSessionUser(request).getFid());
        map.put("type", 1);
        try {
            map.put("spreadLink", Constants.Domain + "/index.html?intro=" + URLEncoder.encode(new String(Base64.encodeBase64(userId.getBytes())), "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "account/account_spreadcenter";
    }

    /**
     * 推广用户
     *
     * @param currentPage
     * @return
     * @throws Exception
     */
    @RequestMapping("/intros")
    public String introl(HttpServletRequest request, Map<String, Object> map,
                         @RequestParam(required = false, defaultValue = "1") int currentPage
    ) throws Exception {
        Fuser fuser = getSessionUser(request);
        String filter = "where fIntroUser_id.fid=" + fuser.getFid() + " and fpostRealValidate = true order by fid desc";
        int total = this.adminService.getAllCount("Fuser", filter);
        List<Fuser> fusers = this.userService.list((currentPage - 1) * Constants.PAGE_ITEM_COUNT_10, Constants.PAGE_ITEM_COUNT_10, filter, true);

        map.put("type", 2);
        map.put("intros", fusers);

        map.put("pageSize", Constants.PAGE_ITEM_COUNT_10);
        map.put("pageNow", currentPage);
        map.put("total", total);

        return "account/sub_introes";
    }

    /**
     * 推广收益
     *
     * @param request
     * @param map
     * @param currentPage
     * @return
     * @throws Exception
     */
    @RequestMapping("/incomes")
    public String income(HttpServletRequest request, Map<String, Object> map,
                         @RequestParam(required = false, defaultValue = "1") int currentPage
    ) throws Exception {
        Fuser fuser = getSessionUser(request);
        String filter = "where fuser.fid=" + fuser.getFid() + " order by fid desc";
        int total = this.adminService.getAllCount("Fintrolinfo", filter);
        List<Fintrolinfo> fintrolinfos = this.introlinfoService.list((currentPage - 1) * Constants.PAGE_ITEM_COUNT_10, Constants.PAGE_ITEM_COUNT_10, filter, true);

        map.put("type", 3);
        map.put("incomes", fintrolinfos);

        map.put("pageSize", Constants.PAGE_ITEM_COUNT_10);
        map.put("pageNow", currentPage);
        map.put("total", total);

        return "account/sub_incomes";
    }

    /**
     * 推广排行榜
     *
     * @param map
     * @return
     * @throws Exception
     */
    @RequestMapping("/topn")
    public String ranking(Map<String, Object> map) throws Exception {
        List<Object[]> fusers = this.userService.getRankingMap();
        map.put("type", 4);
        map.put("tops", fusers);
        return "account/sub_topn";
    }

}
