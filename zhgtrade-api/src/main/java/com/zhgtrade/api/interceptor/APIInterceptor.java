package com.zhgtrade.api.interceptor;

import com.alibaba.fastjson.JSON;
import com.ruizton.main.Enum.TradeApiType;
import com.ruizton.main.Enum.UseStatus;
import com.ruizton.main.api.APIResultCode;
import com.ruizton.main.model.ApiInvokeLog;
import com.ruizton.main.model.TradeApi;
import com.ruizton.main.mq.MessageQueueService;
import com.ruizton.main.mq.QueueConstants;
import com.ruizton.main.service.front.TradeApiService;
import com.ruizton.util.Constants;
import com.ruizton.util.SignatureUtil;
import com.ruizton.util.StringUtils;
import com.ruizton.util.Utils;
import com.zhgtrade.api.utils.ApiConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * API拦截器 认证签约
 * 
 * @author xuelin
 * @date 2016年4月15日
 * @desc
 */
public class APIInterceptor implements HandlerInterceptor {
	/**
	 * 只读权限
	 *
	 */
	private Set<String> readOnlyUrls = new HashSet<>();

	@Autowired
	private TradeApiService tradeApiService;
	@Autowired
	private MessageQueueService messageQueueService;

	public void setReadOnlyUrls(Set<String> readOnlyUrls) {
		this.readOnlyUrls = readOnlyUrls;
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {

		// 1、获取参数
		Map<String, String[]> arrMap = request.getParameterMap();
		Map<String, String> paramsMap = new TreeMap();
		for(String key : arrMap.keySet()){
			paramsMap.put(key, arrMap.get(key)[0]);
		}

		// 2、获取api
		String apiKey = paramsMap.get(ApiConstants.KEY_KEY);
		String sign = paramsMap.get(ApiConstants.SIGN_KEY);
		if(StringUtils.isEmpty(apiKey) || StringUtils.isEmpty(sign)){
			// 必填参数
			invokeLog(request, null, paramsMap, APIResultCode.Code_101);
			renderMessage(response, APIResultCode.Code_101, false);
			return false;
		}
		/*-------------------根据key判断是否合法-------------------*/
		TradeApiType apiType = TradeApiType.getByPrefix(apiKey.charAt(0));
		if(null == apiType){
			// 非法api
			invokeLog(request, null, paramsMap, APIResultCode.Code_102);
			renderMessage(response, APIResultCode.Code_102, false);
			return false;
		}
		if(TradeApiType.Closed.equals(apiType)){
			// api已被关闭
			invokeLog(request, null, paramsMap, APIResultCode.Code_104);
			renderMessage(response, APIResultCode.Code_104, false);
			return false;
		}
		/*-------------------end 根据key判断是否合法-------------------*/

		TradeApi tradeApi = tradeApiService.findCacheByKey(apiKey);
		if(null == tradeApi){
			// api不存在
			invokeLog(request, null, paramsMap, APIResultCode.Code_102);
			renderMessage(response, APIResultCode.Code_102, false);
			return false;
		}
		if(UseStatus.Forbidden.equals(tradeApi.getStatus())){
			// api已禁用
			invokeLog(request, tradeApi, paramsMap, APIResultCode.Code_103);
			renderMessage(response, APIResultCode.Code_103, false);
			return false;
		}

		// 3、判断api
		apiType = tradeApi.getType();
		String uri = request.getRequestURI();
		if(TradeApiType.Closed.equals(apiType)){
			// api已被关闭
			invokeLog(request, tradeApi, paramsMap, APIResultCode.Code_104);
			renderMessage(response, APIResultCode.Code_104, false);
			return false;
		}else if(TradeApiType.Read_Only.equals(apiType) && !readOnlyUrls.contains(uri)){
			// 权限不足
			invokeLog(request, tradeApi, paramsMap, APIResultCode.Code_105);
			renderMessage(response, APIResultCode.Code_105, false);
			return false;
		}

		// 4、验证签名
		paramsMap.put(ApiConstants.SECRET_KEY, tradeApi.getSecret());
		if(!getSignature(paramsMap).equals(sign)){
			// 签名不匹配
			invokeLog(request, tradeApi, paramsMap, APIResultCode.Code_106);
			renderMessage(response, APIResultCode.Code_106, false);
			return false;
		}

		// 5、设置用户id
		request.setAttribute(Constants.USER_ID, tradeApi.getUserId());
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
	}

	protected void invokeLog(HttpServletRequest request, TradeApi tradeApi, Map<String, String> paramMap, APIResultCode code){
		ApiInvokeLog log = new ApiInvokeLog();

		if(null != tradeApi){
			log.setApiId(tradeApi.getId());
			log.setUserId(tradeApi.getUserId());
		}

		log.setIp(Utils.getIpAddr(request));
		log.setParams(JSON.toJSONString(paramMap));
		log.setUrl(request.getRequestURI());
		log.setIsSucceed(false);
		log.setResultCode(code.getCode());

		messageQueueService.publish(QueueConstants.API_INVOKE_LOG_QUEUE, log);
	}

	protected void renderMessage(HttpServletResponse response, APIResultCode code, boolean result){
		Map map = new HashMap<>();
		map.put("result", result);
		map.put("resultCode", code.getCode());

		response.setContentType("application/json;charset=utf-8");
		try(Writer out = response.getWriter()) {
			out.write(JSON.toJSONString(map));
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	protected String getSignature(Map<String, String> paramsMap){
		StringBuilder signBuf = new StringBuilder();
		for(String key : paramsMap.keySet()){
			if(!key.equals(ApiConstants.SIGN_KEY)){
				signBuf.append(key).append("=").append(paramsMap.get(key)).append("&");
			}
		}
		signBuf.deleteCharAt(signBuf.length() - 1);

		return SignatureUtil.getSign(signBuf.toString());
	}
}
