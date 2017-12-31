package com.zhgtrade.api.aop;

import com.alibaba.fastjson.JSON;
import com.ruizton.main.model.ApiInvokeLog;
import com.ruizton.main.model.TradeApi;
import com.ruizton.main.mq.MessageQueueService;
import com.ruizton.main.mq.QueueConstants;
import com.ruizton.main.service.front.TradeApiService;
import com.ruizton.util.StringUtils;
import com.ruizton.util.Utils;
import com.zhgtrade.api.utils.ApiConstants;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * 比特家
 * CopyRight : www.zhgtrade.com
 * Author : xuelin
 * Date： 2016/7/22
 */
public class ApiInvokeLogger {
    @Autowired
    private MessageQueueService messageQueueService;
    @Autowired
    private TradeApiService tradeApiService;

    /**
     * 确保除接口方法，其余都不是public的
     *
     * @param pjp
     * @return
     */
    public Object doAround(ProceedingJoinPoint pjp) {
        Object retObj;
        try {
            retObj = pjp.proceed();
        } catch (Throwable throwable) {
            throwable.printStackTrace();

            Map<String, Object > map = new HashMap();
            map.put(ApiConstants.RESULT_KEY, false);
            map.put(ApiConstants.RESULT_CODE_KEY, 402);
            retObj = map;
        }

        try{
            // 记录调用日志
            if(retObj instanceof Map){
                ApiInvokeLog log = new ApiInvokeLog();
                HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
                Map<String, Object> retMap = (Map<String, Object>) retObj;
                Map<String, String> paramMap = Utils.getParameterMap(request);
                String apiKey = paramMap.get(ApiConstants.KEY_KEY);
                if (retMap.containsKey(ApiConstants.RESULT_KEY) && StringUtils.hasText(apiKey)) {
                    TradeApi tradeApi = tradeApiService.findByKey(apiKey);

                    // 生成记录
                    log.setApiId(tradeApi.getId());
                    log.setUserId(tradeApi.getUserId());
                    log.setIp(Utils.getIpAddr(request));
                    log.setParams(JSON.toJSONString(paramMap));
                    log.setUrl(request.getRequestURI());
                    log.setIsSucceed((boolean) retMap.get(ApiConstants.RESULT_KEY));

                    Object retCode = retMap.get(ApiConstants.RESULT_CODE_KEY);
                    if (null != retCode) {
                        log.setResultCode(retCode.toString());
                    }

                    messageQueueService.publish(QueueConstants.API_INVOKE_LOG_QUEUE, log);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return retObj;
    }

}
