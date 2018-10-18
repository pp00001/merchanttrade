/*
 * Copyright (c) 2018-2020, Ripin Yan. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ins.platform.aggpay.trade.controller;

import ins.platform.aggpay.common.util.R;
import ins.platform.aggpay.common.web.BaseController;
import ins.platform.aggpay.trade.config.IsvConfig;
import static ins.platform.aggpay.trade.constant.TradeConstant.CHANNEL_TYPE_ALI;
import static ins.platform.aggpay.trade.constant.TradeConstant.CHANNEL_TYPE_JD;
import static ins.platform.aggpay.trade.constant.TradeConstant.CHANNEL_TYPE_QQ;
import static ins.platform.aggpay.trade.constant.TradeConstant.CHANNEL_TYPE_WX;
import static ins.platform.aggpay.trade.constant.TradeConstant.OrderType.ORDER_TYPE_PREPAY;
import ins.platform.aggpay.trade.service.impl.GpTradeServiceImpl;
import ins.platform.aggpay.trade.util.OAuthManager;
import ins.platform.aggpay.trade.constant.TradeConstant;
import ins.platform.aggpay.trade.service.GgMerchantService;
import ins.platform.aggpay.trade.service.GpTradeOrderService;
import ins.platform.aggpay.trade.service.GpTradeService;
import ins.platform.aggpay.trade.util.IpUtils;
import ins.platform.aggpay.trade.vo.GgFeeParamVo;
import ins.platform.aggpay.trade.vo.GgMerchantVo;
import ins.platform.aggpay.trade.vo.GpTradeOrderVo;
import ins.platform.aggpay.trade.vo.RespInfoVo;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipaySystemOauthTokenRequest;
import com.alipay.api.response.AlipaySystemOauthTokenResponse;
import com.mybank.bkmerchant.constant.DeniedPayTool;
import com.xiaoleilu.hutool.util.RandomUtil;

/**
 * @author RipinYan
 * @ClassName: GpTradeController
 * @Description: 交易类API
 * @date 2018/10/12 下午2:57
 */

@RestController
public class GpTradeController extends BaseController {

	@Autowired
	private GpTradeOrderService gpTradeOrderService;
	@Autowired
	private GgMerchantService ggMerchantService;
	@Autowired
	private GpTradeService gpTradeService;
	@Autowired
	private IsvConfig isvConfig;

	/**
	 * @Title: pay
	 * @Description: 移动刷卡支付（被扫）接口
	 * @param requestXml 请求报文
	 * @throws
	 * @author Ripin Yan
	 * @return java.lang.String 响应报文
	 */
	@RequestMapping(value = "/trade/pay/scanPay", method = RequestMethod.POST)
	public String scanPay(@RequestBody GpTradeOrderVo tradeOrderVo) {
		String logPrefix = "【移动刷卡支付-被扫】";

		String result;
		try {
			GgMerchantVo merchantVo = ggMerchantService.findMerchantById(tradeOrderVo.getId());
			String[] deniedPayTool = merchantVo.getDeniedPayToolList().split(",");
			String[] payList = new String[2];
			for (int i = 0; i < deniedPayTool.length; i++) {
				if ("credit".equals(deniedPayTool[i])) {
				}
				switch (deniedPayTool[i]) {
					case DeniedPayTool.CREDIT_CARD:
						payList[i] = "credit";
						break;
					case DeniedPayTool.HUABEI:
						payList[i] = "pcredit";
						break;
				}
			}
			String channelType = tradeOrderVo.getChannelType();
			switch (channelType) {
				case "ALI":
					channelType = "01";
					break;
				case "WX":
					channelType = "02";
					break;
				case "QQ":
					channelType = "03";
					break;
				case "JD":
					channelType = "04";
					break;

			}
			// 默认T+1清算
			String feeType = "T1";
			List<GgFeeParamVo> feeParamList = merchantVo.getFeeParamList();
			for (int i = 0; i < feeParamList.size(); i++) {
				if (feeParamList.get(i).getChanneltype().equals(channelType)) {
					feeType = feeParamList.get(i).getFeetype().equals("01") ? "T0" : "T1";
				}
			}
			tradeOrderVo.setPayLimit(StringUtils.join(payList, ','));
			tradeOrderVo.setSettleType(feeType);
			GpTradeOrderVo resultVo = gpTradeService.scanPay(tradeOrderVo);
			result = JSON.toJSONString(resultVo);

		} catch (Exception e) {
			result = "移动刷卡支付（被扫）支付异常!";
			logger.error(result + e.getMessage(), e);
		}

		return result;
	}

	/**
	 * @Title: notifyPayResult
	 * @Description: H5支付（主扫）
	 * @param requestXml 请求报文
	 * @throws
	 * @author Ripin Yan
	 * @return java.lang.String 响应报文
	 */
	@RequestMapping(value = "/trade/pay/prePay", method = RequestMethod.POST)
	public String prePay(@RequestBody GpTradeOrderVo tradeOrderVo, HttpServletRequest request) {
		String logPrefix = "【H5支付-主扫】";
		JSONObject jo = new JSONObject();
		String errorMessage = "";
		String channelType = isWeixinOrAlipay(request);
		// TODO 测试
		channelType = "ALI";
		tradeOrderVo.setTotalAmount(1);
		tradeOrderVo.setBody("主扫测试-支付宝01");
		tradeOrderVo.setExpireExpress(70);
		tradeOrderVo.setOpenId("otBP8wWRG63MS9IZkK27hhO0jYnM");
		tradeOrderVo.setOpenId("2088502287562373");
		if (TradeConstant.CHANNEL_TYPE_OTHER.equals(channelType)) {
			errorMessage = "请使用微信或支付宝扫描二维码！";
			logger.info("{}信息：{}", logPrefix, errorMessage);
			jo.put("resCode", "1001");
			jo.put("resMsg", errorMessage);
			return jo.toJSONString();
		}

		if (tradeOrderVo.getId() == null) {
			errorMessage = "商户号不能为空！";
			logger.info("{}信息：{}", logPrefix, errorMessage);
			jo.put("resCode", "1002");
			jo.put("resMsg", errorMessage);
			return jo.toJSONString();
		}

		if (tradeOrderVo.getTotalAmount() == null || tradeOrderVo.getTotalAmount() <= 0) {
			errorMessage = "金额必须为正数！";
			logger.info("{}信息：{}", logPrefix, errorMessage);
			jo.put("resCode", "1003");
			jo.put("resMsg", errorMessage);
			return jo.toJSONString();
		}

		if (StringUtils.isBlank(tradeOrderVo.getOpenId())) {
			errorMessage = "OpenId不能为空！";
			logger.info("{}信息：{}", logPrefix, errorMessage);
			jo.put("resCode", "1004");
			jo.put("resMsg", errorMessage);
			return jo.toJSONString();
		}

		// 支付渠道类型
		tradeOrderVo.setChannelType(channelType);
		tradeOrderVo.setOrderType(ORDER_TYPE_PREPAY);
		String url = request.getRequestURL().toString();
		logger.info("url:{}", url);

		// 终端IP
		tradeOrderVo.setDeviceCreateIp(IpUtils.getIpAddr(request));

		String result;
		try {
			GgMerchantVo merchantVo = ggMerchantService.findMerchantById(tradeOrderVo.getId());
			if (merchantVo == null) {
				errorMessage = "商户不存在！";
				logger.info("{}信息：{}", logPrefix, errorMessage);
				jo.put("resCode", "1005");
				jo.put("resMsg", errorMessage);
				return jo.toJSONString();
			}
			tradeOrderVo.setMerchantId(merchantVo.getMerchantId());

			String[] deniedPayTool = merchantVo.getDeniedPayToolList().split(",");
			String[] payList = new String[2];
			for (int i = 0; i < deniedPayTool.length; i++) {
				if ("credit".equals(deniedPayTool[i])) {
				}
				switch (deniedPayTool[i]) {
					case DeniedPayTool.CREDIT_CARD:
						payList[i] = "credit";
						break;
					case DeniedPayTool.HUABEI:
						payList[i] = "pcredit";
						break;
				}
			}
			tradeOrderVo.setPayLimit(StringUtils.join(payList, ','));

			String channelTypeV = "01";
			switch (channelType) {
				case CHANNEL_TYPE_ALI:
					channelTypeV = "01";
					break;
				case CHANNEL_TYPE_WX:
					channelTypeV = "02";
					break;
				case CHANNEL_TYPE_QQ:
					channelTypeV = "03";
					break;
				case CHANNEL_TYPE_JD:
					channelTypeV = "04";
					break;
			}

			// 默认T+1清算
			String feeType = "T1";
			List<GgFeeParamVo> feeParamList = merchantVo.getFeeParamList();
			for (int i = 0; i < feeParamList.size(); i++) {
				if (feeParamList.get(i).getChanneltype().equals(channelTypeV)) {
					feeType = feeParamList.get(i).getFeetype().equals("01") ? "T0" : "T1";
				}
			}
			tradeOrderVo.setSettleType(feeType);
			// 字段[SubAppId]不能为空
			if (StringUtils.isBlank(tradeOrderVo.getSubAppId())) {
				tradeOrderVo.setSubAppId(isvConfig.getSubAppId());
			}
			GpTradeOrderVo resultVo = gpTradeService.prePay(tradeOrderVo);
			RespInfoVo respInfoVo = resultVo.getRespInfo();
			if (respInfoVo != null) {
				jo.put("resCode", respInfoVo.getResultCode());
				jo.put("resMsg", respInfoVo.getResultMsg());
				jo.put("prePayId", resultVo.getPrePayId());
				jo.put("payInfo", resultVo.getPayInfo());
			}

		} catch (Exception e) {
			result = "H5扫码支付异常!";
			logger.error(result + e.getMessage(), e);
			jo.put("resCode", "1009");
			jo.put("resMsg", result);
		}

		return jo.toJSONString();
	}

	/**
	 * 支付宝支付授权获取Code
	 *
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/auth/ali", method = RequestMethod.GET)
	@ResponseStatus(value = HttpStatus.OK)
	@ResponseBody
	public String aliPayAuthorize(@RequestParam(value = "auth_code") String authCode) {

		String logPrefix = "【支付宝获取用户信息】";
		String APP_ID = "2018073160766860";
		//  APP_ID = "2018090700000286";

		AlipayClient alipayClient = new DefaultAlipayClient(isvConfig.getOpenApiUrl(), APP_ID, IsvConfig.APP_PRIVATE_KEY, TradeConstant.Ali.FORMAT,
				TradeConstant.Ali.CHARSET,
				IsvConfig.ALIPAY_PUBLIC_KEY, TradeConstant.Ali.SIGN_TYPE);
//		AlipayClient alipayClient = new DefaultAlipayClient(URL, isvConfig.getAppId(), IsvConfig.APP_PRIVATE_KEY, "json", "UTF-8", IsvConfig
//				.ALIPAY_PUBLIC_KEY, "RSA2");

		AlipaySystemOauthTokenRequest request = new AlipaySystemOauthTokenRequest();
		request.setCode(authCode);
		request.setGrantType("authorization_code");
		String userId = "";
		try {
			AlipaySystemOauthTokenResponse oauthTokenResponse = alipayClient.execute(request);
			userId = oauthTokenResponse.getUserId();
			logger.info("{}-UserId：{}", logPrefix, userId);
		} catch (AlipayApiException e) {
			logger.error(logPrefix + "异常:" + e.getMessage(), e);
		}

		return userId;

	}

	@RequestMapping(value = "/qrPay", method = RequestMethod.GET)
	@ResponseStatus(value = HttpStatus.OK)
	@ResponseBody
	public String test(HttpServletRequest request) {

		String logPrefix = "【支付宝获取用户信息】";
		String state = RandomUtil.randomString(4);
		String url = generateAliRedirectURI(state);
		logger.info("ip地址{}，{}调用{}", IpUtils.getIpAddr(request), logPrefix, url);
		return "redirect:" + url;
	}


	/**
	 * isWeixinOrAlipay(判断扫码APP类型)
	 *
	 * @Title: isWeixinOrAlipay
	 * @Description: 
	 * @param request
	 * @throws 
	 * @author Ripin Yan
	 * @return java.lang.String
	 */
	public String isWeixinOrAlipay(HttpServletRequest request) {
		if (request != null) {
			String userAgent = request.getHeader("User-Agent");
			if (StringUtils.isNotBlank(userAgent)) {
				userAgent = userAgent.toLowerCase();
				if (userAgent.indexOf("MicroMessenger") > -1) {// 微信客户端
					logger.info("微信扫码...");
					return CHANNEL_TYPE_WX;
				} else if (userAgent.indexOf("Alipay") > -1) {
					logger.info("支付扫码...");
					return CHANNEL_TYPE_ALI;
				}
			}
		}
		return "OTHER";
	}


	
	public ModelAndView createRedirectURL(String channelType) {
		if (CHANNEL_TYPE_WX.equals(channelType)) {// 来自微信
			String redirectUrl = OAuthManager.generateRedirectURI(isvConfig.getRedirectUri(), "", "");
			return new ModelAndView("redirect:" + redirectUrl);
		} else if (CHANNEL_TYPE_ALI.equals(channelType)) {// 来自支付宝
			String redirectUrl = this.generateAliRedirectURI("1221");
			return new ModelAndView("redirect:" + redirectUrl);
		}else {
			ModelAndView mv = new ModelAndView();
			mv.setViewName("view/index/unAllow");
			return mv;
		}
	}

	public String generateAliRedirectURI(String state) {

		StringBuffer url = new StringBuffer();
		try {
			url.append(isvConfig.getGetUserIdUrl());
			url.append("?app_id=").append("2018073160766860");
			//url.append("?appid=").append(isvConfig.getAppId());
			url.append("&scope=").append(TradeConstant.Ali.SCOP_AUTH_BASE);
			url.append("&redirect_uri=").append(URLEncoder.encode(isvConfig.getRedirectUri(), TradeConstant.Ali.CHARSET));
			url.append("&state=").append(state);
		} catch (UnsupportedEncodingException e) {
			logger.error("URLEncoder出错" + e.getMessage(), e);
		}
		return url.toString();
	}

	@Autowired
	private GpTradeServiceImpl gpTradeServiceImpl;

	@RequestMapping(value = "/downLoadBill", method = RequestMethod.GET)
	@ResponseStatus(value = HttpStatus.OK)
	@ResponseBody
	public R<String> downLoadBill(@RequestParam(value = "billDate") String billDate) {
		return new R<>(gpTradeServiceImpl.downLoadBill(billDate));
	}



}
