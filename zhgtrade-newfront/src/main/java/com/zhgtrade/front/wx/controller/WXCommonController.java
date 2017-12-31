package com.zhgtrade.front.wx.controller;

import com.alibaba.fastjson.JSONObject;
import com.ruizton.main.Enum.VirtualCoinTypeStatusEnum;
import com.ruizton.main.cache.data.RealTimeDataService;
import com.ruizton.main.comm.ConstantMap;
import com.ruizton.main.dto.LatestDealData;
import com.ruizton.main.model.Farticle;
import com.ruizton.main.model.Fregionconf;
import com.ruizton.main.model.Fuser;
import com.ruizton.main.model.Fvirtualcointype;
import com.ruizton.main.service.front.FrontOthersService;
import com.ruizton.main.service.front.FrontRegionConfService;
import com.ruizton.main.service.front.FrontVirtualCoinService;
import com.ruizton.util.CollectionUtils;
import com.ruizton.util.Constants;
import com.ruizton.util.DateUtils;
import com.ruizton.util.FormatUtils;
import com.ruizton.util.HttpUtils;
import com.ruizton.util.SignatureUtil;
import com.ruizton.util.StringUtils;
import com.ruizton.util.Utils;
import com.zhgtrade.front.controller.ApiBaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


/**
 * 非登陆情况下api
 *
 * 比特家
 * CopyRight : www.zhgtrade.com
 * Author : xuelin
 * Date： 2016/11/1
 */
@RestController
@RequestMapping("/wx")
public class WXCommonController extends ApiBaseController {
    @Autowired
    private ConstantMap constantMap;
    @Autowired
    private RealTimeDataService realTimeDataService;
    @Autowired
    private FrontRegionConfService frontRegionConfService;
    @Autowired
    private FrontVirtualCoinService frontVirtualCoinService;
    @Autowired
    private FrontOthersService frontOthersService;

    /**
     * banner图
     *
     * @return
     */
    @RequestMapping("/indexBanner")
    public Object indexBanner(){
        List imgs = new ArrayList<>(10);
        for(int i=1; i<=10; i++){
            String img = constantMap.getString("bigImage" + i);
            if(StringUtils.hasText(img) && !"#".equals(img)){
                Map map = new HashMap<>();
                map.put("img_url", img);
                imgs.add(map);
            }
        }
        return forSuccessResult(imgs);
    }

    private Object getDealResponseColumn(LatestDealData dealData){
        Map map = new HashMap<>();
        map.put("fid", dealData.getFid());
        map.put("fname", dealData.getFname());
        map.put("fShortName", dealData.getfShortName());
        map.put("furl", dealData.getFurl());
        map.put("fupanddown", dealData.getFupanddown());
        map.put("fupanddownweek", dealData.getFupanddownweek());
        map.put("fmarketValue", FormatUtils.formatCNYUnit(dealData.getFmarketValue()));
        map.put("fentrustValue", FormatUtils.formatCNYUnit(dealData.getFentrustValue()));
        map.put("volumn", FormatUtils.formatCoinUnit(dealData.getVolumn()));
        map.put("lastDealPrize", FormatUtils.formatCoin(dealData.getLastDealPrize()));
        map.put("lowestPrize24", FormatUtils.formatCoin(dealData.getLowestPrize24()));
        map.put("highestPrize24", FormatUtils.formatCoin(dealData.getHighestPrize24()));
        map.put("higestBuyPrize", FormatUtils.formatCoin(dealData.getHigestBuyPrize()));
        map.put("lowestSellPrize", FormatUtils.formatCoin(dealData.getLowestSellPrize()));
        return map;
    }

    /**
     * 首页币排行
     *
     * @return
     */
    @RequestMapping("/coinTradeRank")
    public Object coinTradeRank(){
        List<LatestDealData> dealDatas = realTimeDataService.getLatestDealDataList();
        List<LatestDealData> datas = new ArrayList<>(dealDatas);

        int listSize = 10;

        // 首页排序
        sortCoinRank(datas, 19);

        // 获取首页展示项目
        List list = new ArrayList<>(listSize);
        for(Iterator<LatestDealData> iter = datas.iterator(); iter.hasNext();){
            LatestDealData data = iter.next();
            if(data.isHomeShow() && list.size() < listSize){
                list.add(getDealResponseColumn(data));
                iter.remove();
            }
        }

        // 不足10个 从排序中获取补足
        if(!CollectionUtils.isEmpty(datas) && list.size() < listSize){
            // 剩下的整体排序
            sortCoinRank(datas, 0);
            int left = listSize - list.size();
            if(datas.size() > left){
                datas.subList(0, left).forEach(e -> list.add(getDealResponseColumn(e)));
            }else{
                datas.forEach(e -> list.add(getDealResponseColumn(e)));
            }
        }

        return forSuccessResult(list);
    }

    /**
     * 币币交易
     *
     * @param type
     * @param sort
     * @return
     */
    @RequestMapping("/coinBoardRank")
    public Object coinBoardRank(@RequestParam(required = false, defaultValue = "0")int type, @RequestParam(required = false, defaultValue = "20")int sort){
        List<LatestDealData> dealDatas = realTimeDataService.getLatestDealDataList();
        List<LatestDealData> datas = new ArrayList<>(dealDatas);

        List list = new ArrayList<>();
        if(0 == type){
            // 全部
            sortCoinRank(datas, sort);
            datas.forEach(e -> list.add(getDealResponseColumn(e)));
        }else if(3 == type){
            // 权益交易
            sortCoinRank(datas, sort);
//            datas.stream().filter(e -> 1 == e.getEquityType()).forEach(e -> list.add(getDealResponseColumn(e)));
        }else{
            sortCoinRank(datas, sort);
            datas.stream().filter(e -> type == e.getCoinTradeType()).forEach(e -> list.add(getDealResponseColumn(e)));
        }

        return forSuccessResult(list);
    }

    /**
     * 获取交易币信息
     *
     * @param symbol
     * @return
     */
    @RequestMapping("/coinTradeInfo")
    public Object coinTradeInfo(int symbol){
        LatestDealData dealData = realTimeDataService.getLatestDealData(symbol);

        Map map = new HashMap<>();
        if(null != dealData){
            map.put("fid", dealData.getFid());
            map.put("fname", dealData.getFname());
            map.put("fShortName", dealData.getfShortName());
            map.put("furl", dealData.getFurl());
        }

        return forSuccessResult(map);
    }

    /**
     * 省份
     *
     * @return
     */
    @RequestMapping("/provinces")
    public Object provinces(){
        List<Fregionconf> provinces = this.frontRegionConfService.findRegionByLevel(2);
        List list = new ArrayList<>(provinces.size());
        provinces.forEach(e -> {
            Map map = new HashMap();
            map.put("id", e.getId());
            map.put("name", e.getName());
            list.add(map);
        });
        return forSuccessResult(list);
    }

    /**
     * 启用的币
     *
     * @return
     */
    @RequestMapping("/coins")
    public Object coins(){
        List<Fvirtualcointype> conis = frontVirtualCoinService.findFvirtualCoinType(VirtualCoinTypeStatusEnum.Normal);
        List list = new ArrayList<>(conis.size());
        conis.stream().sorted((f1, f2) -> {
            //0总是在非0的后面
            if(f1.getTotalOrder() == 0 && f2.getTotalOrder() != 0){
                return 1;
            }else if(f1.getTotalOrder() != 0 && f2.getTotalOrder() == 0){
                return -1;
            } else {
                return Integer.valueOf(f1.getTotalOrder()).compareTo(f2.getTotalOrder());
            }
        }).forEach(e -> {
            Map map = new HashMap();
            map.put("fid", e.getFid());
            map.put("fname", e.getFname());
            map.put("furl", e.getFurl());
            map.put("fShortName", e.getfShortName());
            map.put("fisShare", e.isFisShare());
            map.put("FIsWithDraw", e.isFIsWithDraw());
            list.add(map);
        });
        return forSuccessResult(list);
    }

    @RequestMapping("/personalSession")
    public Object personalSession(HttpSession session){
        Fuser fuser = getLoginUser(session);
        if(null == fuser){
            // 未登录
            return forFailureResult(401);
        }

        Map map = new HashMap<>();
        map.put("id", fuser.getFid());
        map.put("loginName", fuser.getFloginName());
        map.put("nickname", fuser.getFnickName());
        map.put("mobile", fuser.getFtelephone());
        map.put("email", fuser.getFemail());
        if(StringUtils.hasText(fuser.getHeadImgUrl())){
            map.put("headImg", fuser.getHeadImgUrl());
        }else{
            map.put("headImg", "static/images/default_head.jpg");
        }
        map.put("realName", fuser.getFrealName());
        map.put("identifyNo", fuser.getFidentityNo());
        map.put("identifyType", fuser.getFidentityType());
        map.put("hasPayPwd", StringUtils.hasText(fuser.getFtradePassword()));
        map.put("identifyStatus", fuser.getfIdentityStatus());
        if(fuser.getFhasRealValidate()){
            // 已通过
            map.put("auth_status", 2);
            map.put("date", DateUtils.formatDate(fuser.getFhasRealValidateTime()));
        }else if(fuser.getFpostRealValidate()){
            // 已提交
            map.put("auth_status", 1);
        }else{
            // 未提交
            map.put("auth_status", 0);
        }

        return forSuccessResult(map);
    }

    @RequestMapping("/websiteInfo")
    public Object websiteInfo(){
        Map map = new HashMap<>();
        map.put("site_title", constantMap.getString("site_title"));
        map.put("site_keywords", constantMap.getString("site_keywords"));
        map.put("site_description", constantMap.getString("site_description"));
        map.put("site_kefu", constantMap.getString("site_kefu"));
        map.put("site_script", constantMap.getString("site_script"));
        map.put("news_script", constantMap.getString("news_script"));
        return forSuccessResult(map);
    }

    /**
     * 新手指南
     *
     * @param type
     * @param page
     * @return
     */
    @RequestMapping("/guideList")
    public Object guideList(@RequestParam(required = false, defaultValue = "2") int type, @RequestParam(required = false, defaultValue = "1") int page){
        List<Farticle> farticles = frontOthersService.findFarticle(type, (page - 1) * Constants.PAGE_ITEM_COUNT_20, Constants.PAGE_ITEM_COUNT_20);
        List list = new ArrayList<>(farticles.size());
        farticles.forEach(e -> {
            Map map = new HashMap();
            map.put("id", e.getFid());
            map.put("title", e.getFtitle());
            map.put("dateTime", e.getFcreateDate());
            map.put("shortContent", e.getFcontent_short());
            list.add(map);
        });

        int total = frontOthersService.findFarticleCount(type);
        int pageCount = total % Constants.PAGE_ITEM_COUNT_20 > 0 ? total / Constants.PAGE_ITEM_COUNT_20 + 1 : total / Constants.PAGE_ITEM_COUNT_20;
        return forSuccessResult(list, pageCount);
    }

    /**
     * 指南详情
     *
     * @param id
     * @return
     */
    @RequestMapping("/guideDetail")
    public Object guideDetail(int id){
        Farticle farticle = frontOthersService.findFarticleById(id);
        Map map = new HashMap<>();
        map.put("title", farticle.getFtitle());
        map.put("dateTime", farticle.getFcreateDate());
        map.put("content", farticle.getFcontent());
        return forSuccessResult(map);
    }

    /**
     * 微信token
     *
     * @return
     */
    @RequestMapping("/weChatAccessToken")
    public Object weChatAccessToken(){
        String token = realTimeDataService.getWeChatAccessToken();
        return forSuccessResult(token);
    }

    /**
     * js sdk配置信息
     *
     * @param url
     * @return
     */
    @RequestMapping("/weChatJsSdkConfig")
    public Object weChatJsSdkConfig(String url){
        String appId = constantMap.getString("weChatAppId");
        long timestamp = Utils.getTimeLong();
        String nonceStr = Utils.randomInteger(4);
        String token = realTimeDataService.getWeChatAccessToken();

        // ticket
        JSONObject json = HttpUtils.sendGetRequestForJson("https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=" + token + "&type=jsapi", null);
        if(!"0".equals(json.getString("errcode"))){
            return forFailureResult(0);
        }
        String ticket = json.getString("ticket");

        // 签名
        StringBuilder signStr = new StringBuilder("jsapi_ticket=");
        signStr.append(ticket).append("&noncestr=").append(nonceStr).append("&timestamp=").append(timestamp).append("&url=").append(url);
        String signature = SignatureUtil.getSign(signStr.toString());

        Map map = new HashMap<>();
        map.put("timestamp", timestamp);
        map.put("nonceStr", nonceStr);
        map.put("appId", appId);
        map.put("signature", signature);
        map.put("token", token);

        return forSuccessResult(map);
    }
}
