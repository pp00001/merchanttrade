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
import static ins.platform.aggpay.trade.constant.TradeConstant.OrderType.ORDER_TYPE_DYNAMIC;
import static ins.platform.aggpay.trade.constant.TradeConstant.OrderType.ORDER_TYPE_PAY;
import static ins.platform.aggpay.trade.constant.TradeConstant.OrderType.ORDER_TYPE_PREPAY;
import static ins.platform.aggpay.trade.constant.TradeConstant.TradeStatus.TRADE_STATUS_PAYING;
import static ins.platform.aggpay.trade.constant.TradeConstant.TradeStatus.TRADE_STATUS_SUCC;
import static ins.platform.aggpay.trade.constant.TradeConstant.Wx.ERR_CODE;
import static ins.platform.aggpay.trade.constant.TradeConstant.Wx.ERR_MSG;
import static ins.platform.aggpay.trade.constant.TradeConstant.Wx.GRANT_TYPE_CODE;
import static ins.platform.aggpay.trade.constant.TradeConstant.Wx.OPEN_ID;
import ins.platform.aggpay.trade.entity.GgMerchant;
import ins.platform.aggpay.trade.entity.GpTradeOrder;
import ins.platform.aggpay.trade.service.GgMerchantService;
import ins.platform.aggpay.trade.service.GpRefundOrderService;
import ins.platform.aggpay.trade.service.GpTradeOrderService;
import ins.platform.aggpay.trade.service.GpTradeService;
import ins.platform.aggpay.trade.util.IpUtils;
import ins.platform.aggpay.trade.vo.GgFeeParamVo;
import ins.platform.aggpay.trade.vo.GgMerchantVo;
import ins.platform.aggpay.trade.vo.GpRefundOrderVo;
import ins.platform.aggpay.trade.vo.GpTradeOrderVo;
import ins.platform.aggpay.trade.vo.RespInfoVo;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipaySystemOauthTokenRequest;
import com.alipay.api.response.AlipaySystemOauthTokenResponse;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.mybank.bkmerchant.constant.DeniedPayTool;
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
	private GpRefundOrderService gpRefundOrderService;
	@Autowired
	private GgMerchantService ggMerchantService;
	@Autowired
	private GpTradeService gpTradeService;
	@Autowired
	private TradeConfig tradeConfig;

	/**
	 * @Title: scanPay
	 * @Description: 移动刷卡支付（被扫）接口
	 * @param requestXml 请求报文
	 * @throws
	 * @author Ripin Yan
	 * @return java.lang.String 响应报文
	 */
	@RequestMapping(value = "/api/pay/scanPay", method = RequestMethod.POST)
	public String scanPay(@RequestBody GpTradeOrderVo tradeOrderVo) {
		String logPrefix = "【移动刷卡支付-被扫】";

		JSONObject jo = new JSONObject();
		String errorMessage = "";
		// TODO 测试

		// 终端IP
		if (StringUtils.isBlank(tradeOrderVo.getAuthCode())) {
			errorMessage = "第三方支付授权码不能为空！";
			logger.info("{}信息：{}", logPrefix, errorMessage);
			jo.put("resCode", "3001");
			jo.put("resMsg", errorMessage);
			return jo.toJSONString();
		}
		// 终端IP
		if (StringUtils.isBlank(tradeOrderVo.getBody())) {
			errorMessage = "商品描述不能为空！";
			logger.info("{}信息：{}", logPrefix, errorMessage);
			jo.put("resCode", "3002");
			jo.put("resMsg", errorMessage);
			return jo.toJSONString();
		}
		if (StringUtils.isBlank(tradeOrderVo.getMerchantId())) {
			errorMessage = "商户号不能为空！";
			logger.info("{}信息：{}", logPrefix, errorMessage);
			jo.put("resCode", "2002");
			jo.put("resMsg", errorMessage);
			return jo.toJSONString();
		}
		if (tradeOrderVo.getTotalAmount() == null || tradeOrderVo.getTotalAmount() <= 0) {
			errorMessage = "金额必须为正数！";
			logger.info("{}信息：{}", logPrefix, errorMessage);
			jo.put("resCode", "2003");
			jo.put("resMsg", errorMessage);
			return jo.toJSONString();
		}
		// 终端IP
		if (StringUtils.isBlank(tradeOrderVo.getChannelType())) {
			errorMessage = "支付渠道类型不能为空！";
			logger.info("{}信息：{}", logPrefix, errorMessage);
			jo.put("resCode", "3003");
			jo.put("resMsg", errorMessage);
			return jo.toJSONString();
		}
		// 终端IP
		if (StringUtils.isBlank(tradeOrderVo.getDeviceCreateIp())) {
			errorMessage = "终端IP不能为空！";
			logger.info("{}信息：{}", logPrefix, errorMessage);
			jo.put("resCode", "3004");
			jo.put("resMsg", errorMessage);
			return jo.toJSONString();
		}
		// 订单类型
		tradeOrderVo.setOrderType(ORDER_TYPE_PAY);

		String result;
		try {
			GgMerchantVo merchantVo = ggMerchantService.findMerchantByMerchantId(tradeOrderVo.getMerchantId());
			if (merchantVo == null) {
				errorMessage = "商户不存在！";
				logger.info("{}信息：{}", logPrefix, errorMessage);
				jo.put("resCode", "2005");
				jo.put("resMsg", errorMessage);
				return jo.toJSONString();
			}
			// 禁用支付方式
			tradeOrderVo.setPayLimit(transferPayLimit(merchantVo.getDeniedPayToolList()));

			// 支付渠道类型
			tradeOrderVo.setSettleType(transferSettleType(tradeOrderVo.getChannelType(), merchantVo.getFeeParamList()));

			// 字段[SubAppId]不能为空
			if (StringUtils.isBlank(tradeOrderVo.getSubAppId())) {
				tradeOrderVo.setSubAppId(tradeConfig.getSubAppId());
			}
			// 币种，默认CNY
			if (StringUtils.isBlank(tradeOrderVo.getCurrency())) {
				tradeOrderVo.setCurrency("CNY");
			}

			// 被扫支付api调用
			GpTradeOrderVo resultVo = gpTradeService.scanPay(tradeOrderVo);
			RespInfoVo respInfoVo = resultVo.getRespInfo();
			if (respInfoVo != null) {
				jo.put("resCode", respInfoVo.getResultCode());
				jo.put("resMsg", respInfoVo.getResultMsg());
				jo.put("prePayId", resultVo.getPrePayId());
				jo.put("payInfo", resultVo.getPayInfo());
			}


		} catch (Exception e) {
			result = "移动刷卡支付（被扫）支付异常!";
			logger.error(result + e.getMessage(), e);
			jo.put("resCode", "2009");
			jo.put("resMsg", result);
		}

		return jo.toJSONString();
	}

	/**
	 * @Title: prePay
	 * @Description: H5支付（主扫）
	 * @param requestXml 请求报文
	 * @throws
	 * @author Ripin Yan
	 * @return java.lang.String 响应报文
	 */
	@RequestMapping(value = "/api/pay/prePay", method = RequestMethod.POST)
	public String prePay(@RequestBody GpTradeOrderVo tradeOrderVo, HttpServletRequest request) {
		String logPrefix = "【H5支付-主扫】";
		JSONObject jo = new JSONObject();
		String errorMessage = "";
		String channelType = isWeixinOrAlipay(request);
		// TODO 测试
		tradeOrderVo.setOpenId("sdfdsf");
		if (TradeConstant.CHANNEL_TYPE_OTHER.equals(channelType)) {
			errorMessage = "请使用微信或支付宝扫描二维码！";
			logger.info("{}信息：{}", logPrefix, errorMessage);
			jo.put("resCode", "2001");
			jo.put("resMsg", errorMessage);
			return jo.toJSONString();
		}

		if (tradeOrderVo.getId() == null) {
			errorMessage = "商户号不能为空！";
			logger.info("{}信息：{}", logPrefix, errorMessage);
			jo.put("resCode", "2002");
			jo.put("resMsg", errorMessage);
			return jo.toJSONString();
		}

		if (tradeOrderVo.getTotalAmount() == null || tradeOrderVo.getTotalAmount() <= 0) {
			errorMessage = "金额必须为正数！";
			logger.info("{}信息：{}", logPrefix, errorMessage);
			jo.put("resCode", "2003");
			jo.put("resMsg", errorMessage);
			return jo.toJSONString();
		}

		if (StringUtils.isBlank(tradeOrderVo.getOpenId())) {
			errorMessage = "OpenId不能为空！";
			logger.info("{}信息：{}", logPrefix, errorMessage);
			jo.put("resCode", "2004");
			jo.put("resMsg", errorMessage);
			return jo.toJSONString();
		}

		// 支付渠道类型
		tradeOrderVo.setChannelType(channelType);
		// 订单类型
		tradeOrderVo.setOrderType(ORDER_TYPE_PREPAY);
		// 终端IP
		tradeOrderVo.setDeviceCreateIp(IpUtils.getIpAddr(request));

		String result;
		try {
			GgMerchantVo merchantVo = ggMerchantService.findMerchantById(tradeOrderVo.getId());
			if (merchantVo == null) {
				errorMessage = "商户不存在！";
				logger.info("{}信息：{}", logPrefix, errorMessage);
				jo.put("resCode", "2005");
				jo.put("resMsg", errorMessage);
				return jo.toJSONString();
			}
			tradeOrderVo.setMerchantId(merchantVo.getMerchantId());

			String body = tradeOrderVo.getBody();
			if (StringUtils.isBlank(body)) {
				errorMessage = "商品描述为空！";
				logger.info("{}信息：{}", logPrefix, errorMessage);
				tradeOrderVo.setBody(merchantVo.getGgMerchantDetailVo().getAlias()+"-消费");
			}else {
				tradeOrderVo.setBody(URLDecoder.decode(body, "UTF-8"));
			}

			// 禁用支付方式
			tradeOrderVo.setPayLimit(transferPayLimit(merchantVo.getDeniedPayToolList()));

			// 支付渠道类型
			tradeOrderVo.setSettleType(transferSettleType(channelType, merchantVo.getFeeParamList()));

			// 字段[SubAppId]不能为空
			if (StringUtils.isBlank(tradeOrderVo.getSubAppId())) {
				tradeOrderVo.setSubAppId(tradeConfig.getSubAppId());
			}
			// 币种，默认CNY
			if (StringUtils.isBlank(tradeOrderVo.getCurrency())) {
				tradeOrderVo.setCurrency("CNY");
			}

			// 主扫支付api调用
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
			jo.put("resCode", "2009");
			jo.put("resMsg", result);
		}

		return jo.toJSONString();
	}


	/**
	 * @Title: dynamicOrder
	 * @Description: 动态订单扫码支付接口
	 * @param requestXml 请求报文
	 * @throws
	 * @author Ripin Yan
	 * @return java.lang.String 响应报文
	 */
	@RequestMapping(value = "/api/pay/dynamicOrder", method = RequestMethod.POST)
	public String dynamicOrder(@RequestBody GpTradeOrderVo tradeOrderVo, HttpServletRequest request) {
		String logPrefix = "【动态订单扫码支付】";
		JSONObject jo = new JSONObject();
		String errorMessage = "";
		String channelType = isWeixinOrAlipay(request);
		// TODO 测试
		tradeOrderVo.setOpenId("sdfdsf");
		if (TradeConstant.CHANNEL_TYPE_OTHER.equals(channelType)) {
			errorMessage = "请使用微信或支付宝扫描二维码！";
			logger.info("{}信息：{}", logPrefix, errorMessage);
			jo.put("resCode", "2001");
			jo.put("resMsg", errorMessage);
			return jo.toJSONString();
		}

		if (tradeOrderVo.getId() == null) {
			errorMessage = "商户号不能为空！";
			logger.info("{}信息：{}", logPrefix, errorMessage);
			jo.put("resCode", "2002");
			jo.put("resMsg", errorMessage);
			return jo.toJSONString();
		}

		if (tradeOrderVo.getTotalAmount() == null || tradeOrderVo.getTotalAmount() <= 0) {
			errorMessage = "金额必须为正数！";
			logger.info("{}信息：{}", logPrefix, errorMessage);
			jo.put("resCode", "2003");
			jo.put("resMsg", errorMessage);
			return jo.toJSONString();
		}


		// 支付渠道类型
		tradeOrderVo.setChannelType(channelType);
		// 订单类型
		tradeOrderVo.setOrderType(ORDER_TYPE_DYNAMIC);
		// 终端IP
		tradeOrderVo.setDeviceCreateIp(IpUtils.getIpAddr(request));

		String result;
		try {
			GgMerchantVo merchantVo = ggMerchantService.findMerchantByMerchantId(tradeOrderVo.getMerchantId());
			if (merchantVo == null) {
				errorMessage = "商户不存在！";
				logger.info("{}信息：{}", logPrefix, errorMessage);
				jo.put("resCode", "2005");
				jo.put("resMsg", errorMessage);
				return jo.toJSONString();
			}

			String body = tradeOrderVo.getBody();
			if (StringUtils.isBlank(body)) {
				errorMessage = "商品描述为空！";
				logger.info("{}信息：{}", logPrefix, errorMessage);
				tradeOrderVo.setBody(merchantVo.getGgMerchantDetailVo().getAlias()+"-消费");
			}else {
				tradeOrderVo.setBody(URLDecoder.decode(body, "UTF-8"));
			}

			// 禁用支付方式
			tradeOrderVo.setPayLimit(transferPayLimit(merchantVo.getDeniedPayToolList()));

			// 支付渠道类型
			if(StringUtils.isBlank(tradeOrderVo.getSettleType())){
				tradeOrderVo.setSettleType(transferSettleType(channelType, merchantVo.getFeeParamList()));
			}

			// 字段[SubAppId]不能为空
			if (StringUtils.isBlank(tradeOrderVo.getSubAppId())) {
				tradeOrderVo.setSubAppId(tradeConfig.getSubAppId());
			}
			// 币种，默认CNY
			if (StringUtils.isBlank(tradeOrderVo.getCurrency())) {
				tradeOrderVo.setCurrency("CNY");
			}

			// 主扫支付api调用
			GpTradeOrderVo resultVo = gpTradeService.dynamicOrder(tradeOrderVo);
			RespInfoVo respInfoVo = resultVo.getRespInfo();
			if (respInfoVo != null) {
				jo.put("resCode", respInfoVo.getResultCode());
				jo.put("resMsg", respInfoVo.getResultMsg());
				jo.put("qrCodeUrl", resultVo.getQrCodeUrl());
			}

		} catch (Exception e) {
			result = "动态订单扫码支付异常!";
			logger.error(result + e.getMessage(), e);
			jo.put("resCode", "2010");
			jo.put("resMsg", result);
		}

		return jo.toJSONString();
	}

	@RequestMapping(value = "/api/pay/refund", method = RequestMethod.PUT)
	@ResponseStatus(value = HttpStatus.OK)
	@ResponseBody
	public String refund(@RequestBody GpRefundOrderVo refundVo, HttpServletRequest request) {

		String logPrefix = "【发起退款】";
		String errorMessage = "";
		JSONObject jo = new JSONObject();
		String outTradeNo = refundVo.getOutTradeNo();
		Integer refundAmount = refundVo.getRefundAmount();

		if (StringUtils.isBlank(outTradeNo)) {
			errorMessage = "原支付交易外部交易号不能为空！";
			logger.info("{}信息：{}", logPrefix, errorMessage);
			jo.put("resCode", "9001");
			jo.put("resMsg", errorMessage);
			return jo.toJSONString();
		}
		if (refundAmount == null || refundAmount <= 0) {
			errorMessage = "退款金额必须大于0！";
			logger.info("{}信息：{}", logPrefix, errorMessage);
			jo.put("resCode", "9002");
			jo.put("resMsg", errorMessage);
			return jo.toJSONString();
		}
		GpTradeOrderVo tradeOrderVo = gpTradeOrderService.findGpTradeOrderByOutTradeNo(outTradeNo);
		if (tradeOrderVo == null) {
			errorMessage = "该交易号无对应订单！";
			logger.info("{}信息：{}", logPrefix, errorMessage);
			jo.put("resCode", "9003");
			jo.put("resMsg", errorMessage);
			return jo.toJSONString();
		}
		if (tradeOrderVo == null) {
			errorMessage = "该交易号无对应订单！";
			logger.info("{}信息：{}", logPrefix, errorMessage);
			jo.put("resCode", "9003");
			jo.put("resMsg", errorMessage);
			return jo.toJSONString();
		}

		String tradeStatus = tradeOrderVo.getTradeStatus();
		if (TRADE_STATUS_PAYING.equals(tradeStatus)) {
			GpTradeOrderVo queryVo = gpTradeService.payQuery(tradeOrderVo.getMerchantId(), outTradeNo);
			String queryStatus = queryVo.getTradeStatus();
			if (!queryStatus.equals(tradeStatus)) {
				tradeStatus = queryStatus;
				tradeOrderVo = queryVo;
			}
		}

		if (!TRADE_STATUS_SUCC.equals(tradeStatus)) {
			errorMessage = "该订单未支付成功，无法发起退款！";
			logger.info("{}信息：{}", logPrefix, errorMessage);
			jo.put("resCode", "9004");
			jo.put("resMsg", errorMessage);
			return jo.toJSONString();
		}



		String channelType = tradeOrderVo.getChannelType();
		refundVo.setOrderNo(tradeOrderVo.getOrderNo());
		refundVo.setMerchantId(tradeOrderVo.getMerchantId());
		refundVo.setCurrency(tradeOrderVo.getCurrency());
		refundVo.setChannelType(channelType);
		refundVo.setReceiptAmount(tradeOrderVo.getReceiptAmount());
		refundVo.setBuyerPayAmount(tradeOrderVo.getBuyerPayAmount());

		List<GpRefundOrderVo> refundOrderVos = gpRefundOrderService.selectListByOutTradeNo(outTradeNo);
		// 已退款总金额
		int totalRefundAmount = 0;
		for (int i = 0; i < refundOrderVos.size(); i++) {
			totalRefundAmount += refundOrderVos.get(i).getRefundAmount();
		}

		// 可退款金额
		int remainderAmount = 0;
		if (TradeConstant.CHANNEL_TYPE_ALI.equals(channelType)) {
			Integer buyerPayAmount = tradeOrderVo.getBuyerPayAmount() == null ? 0 : tradeOrderVo.getBuyerPayAmount();
			remainderAmount = buyerPayAmount - totalRefundAmount;
		} else if (TradeConstant.CHANNEL_TYPE_WX.equals(channelType)) {
			int couponFee = tradeOrderVo.getCouponFee() == null ? 0 : tradeOrderVo.getCouponFee();
			remainderAmount = tradeOrderVo.getTotalAmount() - couponFee - totalRefundAmount;
		}

		if (refundAmount > remainderAmount) {
			errorMessage = "退款金额大于允许退款金额，无法发起退款！";
			logger.info("{}信息：{}", logPrefix, errorMessage);
			jo.put("resCode", "9005");
			jo.put("resMsg", errorMessage);
			return jo.toJSONString();
		}

		String clientIP = IpUtils.getIpAddr(request);
		refundVo.setDeviceCreateIp(clientIP);
		if (StringUtils.isBlank(refundVo.getRefundReason())) {
			refundVo.setRefundReason("交易撤销");
		}
		String result;
		try {
			GpRefundOrderVo resultVo = gpTradeService.refund(refundVo);
			RespInfoVo respInfoVo = resultVo.getRespInfo();
			if (respInfoVo != null) {
				jo.put("resCode", respInfoVo.getResultCode());
				jo.put("resMsg", respInfoVo.getResultMsg());
				jo.put("refundOrderNo", resultVo.getRefundOrderNo());
			}

		} catch (Exception e) {
			result = "退款异常!";
			logger.error(result + e.getMessage(), e);
			jo.put("resCode", "9009");
			jo.put("resMsg", result);
		}

		return jo.toJSONString();
	}


	@RequestMapping(value = "/api/pay/query/{merchantId}/{outTradeNo}", method = RequestMethod.GET)
	@ResponseStatus(value = HttpStatus.OK)
	@ResponseBody
	public String payQuery(@PathVariable(value = "merchantId") String merchantId, @PathVariable(value = "outTradeNo") String outTradeNo) {
		JSONObject jo = new JSONObject();
		String errorMessage = "";
		String logPrefix = "【订单查询】";

		try {
			GpTradeOrderVo queryVo = gpTradeService.payQuery(merchantId, outTradeNo);
			RespInfoVo respInfoVo = queryVo.getRespInfo();
			if (respInfoVo != null) {
				jo.put("resCode", respInfoVo.getResultCode());
				jo.put("resMsg", respInfoVo.getResultMsg());
				jo.put("tradeStatus", queryVo.getTradeStatus());
				jo.put("gmtPayment", queryVo.getGmtPayment());
			}
		} catch (Exception e) {
			errorMessage = logPrefix + "异常:" + e.getMessage();
			logger.error(errorMessage, e);
			jo.put("resCode", "8001");
			jo.put("resMsg", errorMessage);
		}
		return jo.toJSONString();
	}

	@RequestMapping(value = "/refund/query/{merchantId}/{outRefundNo}", method = RequestMethod.GET)
	@ResponseStatus(value = HttpStatus.OK)
	@ResponseBody
	public String refundQuery(@PathVariable(value = "merchantId") String merchantId, @PathVariable(value = "outRefundNo") String outRefundNo) {
		JSONObject jo = new JSONObject();
		String errorMessage = "";
		String logPrefix = "【退款订单查询】";

		try {
			GpRefundOrderVo queryVo = gpTradeService.refundQuery(merchantId, outRefundNo);
			RespInfoVo respInfoVo = queryVo.getRespInfo();
			if (respInfoVo != null) {
				jo.put("resCode", respInfoVo.getResultCode());
				jo.put("resMsg", respInfoVo.getResultMsg());
				jo.put("tradeStatus", queryVo.getTradeStatus());
				jo.put("gmtRefundment", queryVo.getGmtRefundment());
			}
		} catch (Exception e) {
			errorMessage = logPrefix + "异常信息:" + e.getMessage();
			logger.error(errorMessage, e);
			jo.put("resCode", "8002");
			jo.put("resMsg", errorMessage);
		}
		return jo.toJSONString();
	}

	/**
	 * 支付宝支付授权获取Code
	 *
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/oauth/ali", method = RequestMethod.GET)
	@ResponseStatus(value = HttpStatus.OK)
	@ResponseBody
	public String aliPayAuthorize(@RequestParam(value = "code") String authCode) {
		JSONObject jo = new JSONObject();
		String errorMessage = "";
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
			jo.put("resCode", "0000");
			jo.put("resMsg", "success");
			jo.put("openId", userId);
		} catch (AlipayApiException e) {
			errorMessage = logPrefix + "异常:" + e.getMessage();
			logger.error(errorMessage, e);
			jo.put("resCode", "2011");
			jo.put("resMsg", errorMessage);
		}

		return jo.toJSONString();
	}

	/**
	 * 微信支付授权获取Code
	 *
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/oauth/wx", method = RequestMethod.GET)
	@ResponseStatus(value = HttpStatus.OK)
	@ResponseBody
	public String wxAuthorize(@RequestParam(value = "code") String code) {
		JSONObject jo = new JSONObject();
		String errorMessage = "";
		String logPrefix = "【微信支付获取用户信息】";
		String openId = "";
		// TODO 测试公众号配置
		//String subAppId = "wx59df2ba56720ee7d";
		//String appSecret = "7a2790cf4235ca07ba40a8516047296c";

		try {
			StringBuffer sf = new StringBuffer(tradeConfig.getWxOauthUrl());
			sf.append("?appid=");
			sf.append(tradeConfig.getSubAppId());
			//sf.append(subAppId);
			sf.append("&secret=");
			sf.append(tradeConfig.getAppSecret());
			//sf.append(appSecret);
			sf.append("&code=");
			sf.append(code);
			sf.append("&grant_type=");
			sf.append(GRANT_TYPE_CODE);

			HttpRequest get = HttpUtil.createGet(sf.toString());
			HttpResponse execute = get.execute();
			String body = execute.body();
			// {"errcode":40029,"errmsg":"invalid code, hints: [ req_id: 2F1ygA07344124 ]"}
			JSONObject resJson = JSONObject.parseObject(body);
			openId = resJson.getString(OPEN_ID);
			if (StringUtils.isBlank(openId)) {
				logger.info("{} OpenId获取失败，错误代码：{}，错误信息：{}", logPrefix, resJson.getString(ERR_CODE), resJson.getString(ERR_MSG));
				jo.put("resCode", "2012");
				jo.put("resMsg", "OpenId获取失败.");
			} else {
				logger.info("{} OpenId获取成功，OpenId：{}", logPrefix, openId);
				jo.put("resCode", "0000");
				jo.put("resMsg", "success");
				jo.put("openId", openId);
			}
		} catch (Exception e) {
			errorMessage = logPrefix + "异常:" + e.getMessage();
			logger.error(errorMessage, e);
			jo.put("resCode", "2013");
			jo.put("resMsg", errorMessage);
		}

		return jo.toJSONString();
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


	/**
	 * 获取清算方式
	 *
	 * @param channelType 支付渠道类型
	 * @param feeParamList 商户手续费列表
	 * @return 清算方式
	 */
	public String transferSettleType(String channelType, List<GgFeeParamVo> feeParamList) {
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
		for (int i = 0; i < feeParamList.size(); i++) {
			if (feeParamList.get(i).getChannelType().equals(channelTypeV)) {
				feeType = feeParamList.get(i).getFeeType().equals("01") ? "T0" : "T1";
			}
		}
		return feeType;
	}

	/**
	 * 获取禁用支付方式
	 *
	 * @param deniedPayToolList 商户禁用支付方式列表，多个用","分隔
	 * @return
	 */
	public String transferPayLimit(String deniedPayToolList) {
		String result = null;
		if (StringUtils.isNotBlank(deniedPayToolList)) {
			String[] deniedPayTool = deniedPayToolList.split(",");
			String[] payList = new String[deniedPayTool.length];
			for (int i = 0; i < deniedPayTool.length; i++) {
				switch (deniedPayTool[i]) {
					case DeniedPayTool.CREDIT_CARD:
						payList[i] = "credit";
						break;
					case DeniedPayTool.HUABEI:
						payList[i] = "pcredit";
						break;
				}
			}
			result = StringUtils.join(payList, ',');
		}
		return result;
	}

	/**
	 * 授权获取Code
	 *
	 * @return
	 * @throws IOException
	 */
	@Deprecated
	@RequestMapping(value = "/authorizationCode", method = RequestMethod.GET)
	public String getAuthCode(HttpServletRequest request, HttpServletResponse response) {
		JSONObject jo = new JSONObject();
		String errorMessage = "";
		String logPrefix = "";
		String authCodeUrl = "";
		String channelType = isWeixinOrAlipay(request);
		if (TradeConstant.CHANNEL_TYPE_WX.equals(channelType)) {
			logPrefix = "【调用微信openApi】";
			authCodeUrl = generateWxRedirectURI();
		}else if (TradeConstant.CHANNEL_TYPE_ALI.equals(channelType)){
			logPrefix = "【调用支付宝openApi】";
			authCodeUrl = generateAliRedirectURI();
		} else {
			errorMessage = "请使用微信或支付宝扫描二维码！";
			logger.info("{}信息：{}", logPrefix, errorMessage);
			jo.put("resCode", "1001");
			jo.put("resMsg", errorMessage);
			return jo.toJSONString();
		}

		try {
			response.sendRedirect(authCodeUrl);
			logger.info("{}", logPrefix);
			jo.put("resCode", "0000");
			jo.put("resMsg", "success");

		} catch (Exception e) {
			errorMessage = "重定向服务器认证失败！" + e.getMessage();
			logger.info("{}信息：{}", logPrefix, errorMessage);
			jo.put("resCode", "1010");
			jo.put("resMsg", errorMessage);
		}

		return jo.toJSONString();

	}

	/**
	 * 生成支付宝授权code请求地址
	 * @return
	 */
	public String generateAliRedirectURI() {

		// https://openauth.alipay.com/oauth2/publicAppAuthorize
		// .htm?app_id=2018073160766860&scope=auth_base&redirect_uri=http%3A%2F%2Fripin925.ngrok.xiaomiqiu.cn%2Fauth%2Fali";

		StringBuffer url = new StringBuffer();

		try {
			url.append(tradeConfig.getAliOpenauthUrl());
			//url.append("?app_id=").append(tradeConfig.getAppId());
			// 本人阿里自研appId
			url.append("?app_id=").append("2018073160766860");
			url.append("&scope=").append(TradeConstant.Ali.SCOP_AUTH_BASE);
			url.append("&redirect_uri=").append(URLEncoder.encode(tradeConfig.getAliRedirectUrl(), TradeConstant.Ali.CHARSET));
			url.append("&state=").append(RandomUtil.randomString(4));
		} catch (UnsupportedEncodingException e) {
			logger.error("URLEncoder出错" + e.getMessage(), e);
		}
		return url.toString();
	}

	/**
	 * 生成微信授权code请求地址
	 * @return
	 */
	public String generateWxRedirectURI() {

		// https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx59df2ba56720ee7d&redirect_uri=http%3A%2F%2Fripin925.ngrok.xiaomiqiu
		// .cn%2Fauth%2Fwx&response_type=code&scope=snsapi_base&state=111#wechat_redirect";

		StringBuffer url = new StringBuffer();
		try {
			url.append(tradeConfig.getWxOpenauthUrl());
			//url.append("?appid=").append(tradeConfig.getSubAppId());
			url.append("?appid=").append("wx59df2ba56720ee7d");
			url.append("&redirect_uri=").append(URLEncoder.encode(tradeConfig.getWxRedirectUrl(), TradeConstant.Wx.CHARSET));
			url.append("&response_type=").append(TradeConstant.Wx.CODE);
			url.append("&scope=").append(TradeConstant.Wx.SCOP_AUTH_BASE);
			url.append("&state=").append(RandomUtil.randomString(4));
			url.append("#wechat_redirect");
		} catch (UnsupportedEncodingException e) {
			logger.error("URLEncoder出错" + e.getMessage(), e);
		}
		return url.toString();
	}



}
