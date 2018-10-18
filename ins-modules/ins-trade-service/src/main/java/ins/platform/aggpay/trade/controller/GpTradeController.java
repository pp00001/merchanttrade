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
import ins.platform.aggpay.trade.config.TradeConfig;
import ins.platform.aggpay.trade.constant.TradeConstant;
import static ins.platform.aggpay.trade.constant.TradeConstant.Ali.CHARSET;
import static ins.platform.aggpay.trade.constant.TradeConstant.Ali.FORMAT;
import static ins.platform.aggpay.trade.constant.TradeConstant.Ali.SIGN_TYPE;
import static ins.platform.aggpay.trade.constant.TradeConstant.CHANNEL_TYPE_ALI;
import static ins.platform.aggpay.trade.constant.TradeConstant.CHANNEL_TYPE_JD;
import static ins.platform.aggpay.trade.constant.TradeConstant.CHANNEL_TYPE_QQ;
import static ins.platform.aggpay.trade.constant.TradeConstant.CHANNEL_TYPE_WX;
import static ins.platform.aggpay.trade.constant.TradeConstant.OrderType.ORDER_TYPE_PREPAY;
import static ins.platform.aggpay.trade.constant.TradeConstant.Wx.ERR_CODE;
import static ins.platform.aggpay.trade.constant.TradeConstant.Wx.ERR_MSG;
import static ins.platform.aggpay.trade.constant.TradeConstant.Wx.GRANT_TYPE_CODE;
import static ins.platform.aggpay.trade.constant.TradeConstant.Wx.OPEN_ID;
import ins.platform.aggpay.trade.entity.GpTradeOrder;
import ins.platform.aggpay.trade.service.GgMerchantService;
import ins.platform.aggpay.trade.service.GpTradeOrderService;
import ins.platform.aggpay.trade.service.GpTradeService;
import ins.platform.aggpay.trade.util.IpUtils;
import ins.platform.aggpay.trade.vo.GgFeeParamVo;
import ins.platform.aggpay.trade.vo.GgMerchantVo;
import ins.platform.aggpay.trade.vo.GpTradeOrderVo;
import ins.platform.aggpay.trade.vo.RefundVo;
import ins.platform.aggpay.trade.vo.RespInfoVo;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
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
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.mybank.bkmerchant.constant.DeniedPayTool;
import com.mybank.bkmerchant.constant.TradeStatusEnum;
import com.xiaoleilu.hutool.http.HttpRequest;
import com.xiaoleilu.hutool.http.HttpResponse;
import com.xiaoleilu.hutool.http.HttpUtil;
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
	private TradeConfig tradeConfig;

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
			// 禁用支付方式
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
			// 支付渠道类型
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
			// 默认清算方式 T+1
			String feeType = "T1";
			List<GgFeeParamVo> feeParamList = merchantVo.getFeeParamList();
			for (int i = 0; i < feeParamList.size(); i++) {
				if (feeParamList.get(i).getChanneltype().equals(channelType)) {
					feeType = feeParamList.get(i).getFeetype().equals("01") ? "T0" : "T1";
				}
			}
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
		channelType = "WX";
		tradeOrderVo.setTotalAmount(1);
		tradeOrderVo.setBody("主扫测试-支付宝01");
		tradeOrderVo.setBody("主扫测试-微信01");
		tradeOrderVo.setExpireExpress(70);
		tradeOrderVo.setOpenId("2088502287562373");
		tradeOrderVo.setOpenId("otBP8wWRG63MS9IZkK27hhO0jYnM");
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

			// 禁用支付方式
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
			// 支付渠道类型
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

			// 默认清算方式 T+1
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
				tradeOrderVo.setSubAppId(tradeConfig.getSubAppId());
			}
			GpTradeOrderVo resultVo = gpTradeService.prePay(tradeOrderVo);
			RespInfoVo respInfoVo = resultVo.getRespInfo();
			if (respInfoVo != null) {
				jo.put("resCode", respInfoVo.getResultCode());
				jo.put("resMsg", respInfoVo.getResultMsg());

				jo.put("prePayId", resultVo.getPrePayId());
				jo.put("payInfo", resultVo.getPayInfo());
//				jo.put("resCode", "0000");
//				jo.put("prePayId", "wx201410272009395522657a690389285100");
			}

		} catch (Exception e) {
			result = "H5扫码支付异常!";
			logger.error(result + e.getMessage(), e);
			jo.put("resCode", "1009");
			jo.put("resMsg", result);
			jo.put("resCode", "0000");
//			jo.put("prePayId", "wx201410272009395522657a690389285100");
			jo.put("payInfo", "{\"appId\":\"wxdace645e0bc2c424\",\"nonceStr\":\"03144cfa93ead27f958431953b70efb0\"," +
					"\"package\":\"prepay_id=wx1212551978400940cf7e59343856862982\",\"paySign\":\"EF74B4C7219ADA508E4B18BA37F8C833\"," +
					"\"signType\":\"MD5\",\"timeStamp\":\"1539320119\"}");

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
		String appId = tradeConfig.getAppId();
		appId = "2018073160766860";

		AlipayClient alipayClient = new DefaultAlipayClient(tradeConfig.getAliOauthUrl(), appId, tradeConfig.getAppPrivateKey(), FORMAT, CHARSET,
				tradeConfig.getAlipayPublicKey(), SIGN_TYPE);

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

	/**
	 * 微信支付授权获取Code
	 *
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/auth/wx", method = RequestMethod.GET)
	@ResponseStatus(value = HttpStatus.OK)
	@ResponseBody
	public String wxAuthorize(@RequestParam(value = "code") String code) {

		String logPrefix = "【微信支付获取用户信息】";
		String openId = "";
		// TODO 测试公众号配置
//		String subAppId = "wx59df2ba56720ee7d";
//		String appSecret = "7a2790cf4235ca07ba40a8516047296c";

		try {
			StringBuffer sf = new StringBuffer(tradeConfig.getWxOauthUrl());
			sf.append("?appid=");
			sf.append(tradeConfig.getSubAppId());
			sf.append("&secret=");
			sf.append(tradeConfig.getAppSecret());
			sf.append("&code=");
			sf.append(code);
			sf.append("&grant_type=");
			sf.append(GRANT_TYPE_CODE);

			HttpRequest get = HttpUtil.createGet(sf.toString());
			HttpResponse execute = get.execute();
			String body = execute.body();//{"errcode":40029,"errmsg":"invalid code, hints: [ req_id: 2F1ygA07344124 ]"}
			JSONObject resJson = JSONObject.parseObject(body);
			openId = resJson.getString(OPEN_ID);
			if (StringUtils.isBlank(openId)) {
				logger.info("{} OpenId获取失败，错误代码：{}，错误信息：{}", logPrefix, resJson.getString(ERR_CODE), resJson.getString(ERR_MSG));
			} else {
				logger.info("{} OpenId获取成功，OpenId：{}", logPrefix, openId);
			}
		} catch (Exception e) {
			logger.error(logPrefix + "异常:" + e.getMessage(), e);
		}

		return openId;
	}

	@RequestMapping(value = "/qrPay", method = RequestMethod.GET)
	@ResponseStatus(value = HttpStatus.OK)
	@ResponseBody
	public String qrPay(HttpServletRequest request) {

		String logPrefix = "【支付宝获取用户信息】";
		String state = RandomUtil.randomString(4);
		String url = "";
		logger.info("ip地址{}，{}调用{}", IpUtils.getIpAddr(request), logPrefix, url);
		return "redirect:" + url;
	}

	@RequestMapping(value = "/trade/rf", method = RequestMethod.GET)
	@ResponseStatus(value = HttpStatus.OK)
	@ResponseBody
	public String refund(@RequestBody RefundVo refundVo, HttpServletRequest request) {

		String logPrefix = "【发起退款】";
		String errorMessage = "";
		JSONObject jo = new JSONObject();
		String outTradeNo = refundVo.getOutTradeNo();

		if(StringUtils.isBlank(outTradeNo)){
			errorMessage = "原支付交易外部交易号不能为空！";
			logger.info("{}信息：{}", logPrefix, errorMessage);
			jo.put("resCode", "9001");
			jo.put("resMsg", errorMessage);
			return jo.toJSONString();
		}
		GpTradeOrder gpTradeOrder = gpTradeOrderService.selectOne(new EntityWrapper<GpTradeOrder>().eq("out_trade_no", outTradeNo));
		if(gpTradeOrder == null){
			errorMessage = "该交易号无对应订单！";
			logger.info("{}信息：{}", logPrefix, errorMessage);
			jo.put("resCode", "9002");
			jo.put("resMsg", errorMessage);
			return jo.toJSONString();
		}

		if(!TradeStatusEnum.succ.getStatusCode().equals(gpTradeOrder.getTradeStatus())){
			errorMessage = "该交易号无对应订单！";
			logger.info("{}信息：{}", logPrefix, errorMessage);
			jo.put("resCode", "9002");
			jo.put("resMsg", errorMessage);
			return jo.toJSONString();
		}

		String clientIP = IpUtils.getIpAddr(request);
		refundVo.setDeviceCreateIp(clientIP);


		String url = "";
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


	@RequestMapping(value = "/downLoadBill", method = RequestMethod.GET)
	@ResponseStatus(value = HttpStatus.OK)
	@ResponseBody
	public R<String> downLoadBill(@RequestParam(value = "billDate") String billDate) {
		return new R<>(gpTradeService.downLoadBill(billDate));
	}



}
