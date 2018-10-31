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

package ins.platform.aggpay.trade.service.impl;

import com.mybank.bkmerchant.base.HttpsMain;
import com.mybank.bkmerchant.util.XmlSignUtil;
import com.mybank.bkmerchant.util.XmlUtil;
import ins.platform.aggpay.trade.config.TradeConfig;
import ins.platform.aggpay.trade.constant.TradeConstant;
import ins.platform.aggpay.trade.entity.GpRefundOrder;
import ins.platform.aggpay.trade.entity.GpTradeOrder;
import ins.platform.aggpay.trade.mapper.GpRefundOrderMapper;
import ins.platform.aggpay.trade.mapper.GpTradeOrderMapper;
import ins.platform.aggpay.trade.service.GpTradeService;
import ins.platform.aggpay.trade.util.ApiCallUtil;
import ins.platform.aggpay.trade.util.MapUtil;
import ins.platform.aggpay.trade.vo.GpRefundOrderVo;
import ins.platform.aggpay.trade.vo.GpTradeOrderVo;
import ins.platform.aggpay.trade.vo.RespInfoVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static ins.platform.aggpay.trade.constant.TradeConstant.RespInfo.RESULT_STATUS_SUCCESS;
import static ins.platform.aggpay.trade.constant.TradeConstant.TradeStatus.*;

/**
 * <p>
 * 交易服务实现类
 * </p>
 *
 * @author ripin
 * @since 2018-10-11
 */
@Service
public class GpTradeServiceImpl implements GpTradeService {

	private static final Logger logger = LoggerFactory.getLogger(GpTradeServiceImpl.class);

	@Autowired
	private GpTradeOrderMapper gpTradeOrderMapper;
	@Autowired
	private GpRefundOrderMapper gpRefundOrderMapper;
	@Autowired
	private TradeConfig tradeConfig;

	@Override
	public GpTradeOrderVo scanPay(GpTradeOrderVo tradeOrderVo) {
		//账户余额查询
		String function = "ant.mybank.bkmerchanttrade.pay";

		XmlUtil xmlUtil = new XmlUtil();
		Map<String, String> form = new HashMap<String, String>();
		form.put("Function", function);
		form.put("ReqTime", new Timestamp(System.currentTimeMillis()).toString());
		//reqMsgId每次报文必须都不一样
		form.put("ReqMsgId", UUID.randomUUID().toString());
		// 成功authcode
		// 28763443825664394
		// 微信ok-134621141753364349-134723186961316101
		form.put("AuthCode", "134621141753364349");
		form.put("AuthCode", "286664990659481080");
		form.put("OutTradeNo", UUID.randomUUID().toString().replace("-", ""));
		form.put("Body", "反扫测试-碧螺春9");
		form.put("GoodsTag", "test");
		form.put("GoodsDetail", "test");
		form.put("TotalAmount", "3");
		form.put("Currency", "CNY");
		form.put("MerchantId", HttpsMain.merchantId);
		form.put("IsvOrgId", HttpsMain.IsvOrgId);
		form.put("ChannelType", "WX");
		form.put("ChannelType", "ALI");
		//        form.put("OperatorId","test");
		//        form.put("StoreId","test");
		//        form.put("DeviceId","test");
		form.put("DeviceCreateIp", "112.97.59.21");
		form.put("ExpireExpress", "120");
		form.put("SettleType", "T1");
		form.put("Attach", "附加信息");
		form.put("PayLimit", "pcredit");
		form.put("DiscountableAmount", "1");
		form.put("UndiscountableAmount", "2");
		//form.put("AlipayStoreId","支付宝的店铺编号");
		form.put("SysServiceProviderId", "2018090700000286");
		//form.put("CheckLaterNm","");
		form.put("SubAppId", "wx62a55dbdd041bb1d");
		//        form.put("SpecifySubMerchId","N");
		//        form.put("ChannelId","240824008");
		//        form.put("SubMerchId","242972555");

		//封装报文
		try {
			String param = xmlUtil.format(form, function);
			if (HttpsMain.isSign) {//生产环境需进行rsa签名
				param = XmlSignUtil.sign(param);
			}
			System.out.println("-------------------------");
			System.out.println("---------REQUEST---------");
			System.out.println("-------------------------");
			System.out.println(param);

			//发送请求
			String response = HttpsMain.httpsReq(HttpsMain.payUrl, param);

			System.out.println("-------------------------");
			System.out.println("---------RESPONSE--------");
			System.out.println("-------------------------");
			System.out.println(response);
			if (HttpsMain.isSign) {//生产环境需进行rsa验签
				if (!XmlSignUtil.verify(response)) {
					throw new Exception("验签失败");
				}
			}
			//解析报文
			Map<String, Object> resMap = xmlUtil.parse(response, function);
			//结果消息
			String b = (String) resMap.get("OutTradeNo");
			System.out.println(b);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	@Override
	public GpTradeOrderVo prePay(GpTradeOrderVo tradeOrderVo) {

		GpTradeOrderVo rs = new GpTradeOrderVo();
		try {
			tradeOrderVo.setOutTradeNo(ApiCallUtil.generateOutTradeNo());
			ApiCallUtil prePay = new ApiCallUtil(ApiCallUtil.FUNCTION_PRE_PAY);
			prePay.setBody(new HashMap<String, String>() {
				{
					put("IsvOrgId", tradeConfig.getIsvOrgId());
					put("OutTradeNo", tradeOrderVo.getOutTradeNo());
					put("Body", tradeOrderVo.getBody());
					put("GoodsTag", tradeOrderVo.getGoodsTag());
					put("GoodsDetail", tradeOrderVo.getGoodsDetail());
					put("TotalAmount", String.valueOf(tradeOrderVo.getTotalAmount()));
					put("Currency", tradeOrderVo.getCurrency());
					put("MerchantId", tradeOrderVo.getMerchantId());
					put("IsvOrgId", tradeConfig.getIsvOrgId());
					put("ChannelType", tradeOrderVo.getChannelType());
					put("OpenId", tradeOrderVo.getOpenId());
					put("OperatorId", tradeOrderVo.getOperatorId());
					put("StoreId", tradeOrderVo.getStoreId());
					put("DeviceId", tradeOrderVo.getDeviceId());
					put("DeviceCreateIp", tradeOrderVo.getDeviceCreateIp());
					if (tradeOrderVo.getExpireExpress() != null && tradeOrderVo.getExpireExpress() > 0) {
						put("ExpireExpress", String.valueOf(tradeOrderVo.getExpireExpress()));
					} else {
						tradeOrderVo.setExpireExpress(Integer.valueOf(tradeConfig.getExpireExpress()));
						put("ExpireExpress", tradeConfig.getExpireExpress());
					}
					put("SettleType", tradeOrderVo.getSettleType());
					put("Attach", tradeOrderVo.getAttach());
					put("NotifyUrl", tradeOrderVo.getNotifyUrl());
					put("PayLimit", tradeOrderVo.getPayLimit());
					if (TradeConstant.CHANNEL_TYPE_WX.equals(tradeOrderVo.getChannelType())) {
						put("SubAppId", tradeOrderVo.getSubAppId());
						put("SpecifySubMerchId", tradeOrderVo.getSpecifySubMerchId());
						put("ChannelId", tradeOrderVo.getChannelId());
						put("SubMerchId", tradeOrderVo.getSubMerchId());
					} else {
						put("DiscountableAmount", tradeOrderVo.getDiscountableAmount() == null ? null : String.valueOf(tradeOrderVo
								.getDiscountableAmount()));
						put("UndiscountableAmount", tradeOrderVo.getUndiscountableAmount() == null ? null : String.valueOf(tradeOrderVo
								.getUndiscountableAmount()));
						put("AlipayStoreId", tradeOrderVo.getAlipayStoreId());
						put("SysServiceProviderId", tradeOrderVo.getSysServiceProviderId());
						put("CheckLaterNm", tradeOrderVo.getCheckLaterNm());

					}
				}
			});

			logger.info("开始调用prePay接口, url={}", tradeConfig.getPayUrl());
			Map<String, Object> resMap = prePay.call(tradeConfig.getPayUrl());

			// 数据转换
			rs = MapUtil.map2Obj(resMap, GpTradeOrderVo.class);
			RespInfoVo respInfoVo = rs.getRespInfo();

			if (respInfoVo != null && RESULT_STATUS_SUCCESS.equals(respInfoVo.getResultStatus())) {
				logger.info("订单创建成功!外部交易号：{}，订单号：{}", rs.getOutTradeNo(), rs.getOrderNo());
				tradeOrderVo.setOrderNo(rs.getOrderNo());
				tradeOrderVo.setPrePayId(rs.getPrePayId());
				tradeOrderVo.setPayInfo(rs.getPayInfo());
				tradeOrderVo.setTradeStatus(TRADE_STATUS_PAYING);
			} else {
				logger.info("订单创建失败!外部交易号：{}", rs.getOutTradeNo());
				tradeOrderVo.setValidInd("0");
			}

			GpTradeOrder gpTradeOrder = new GpTradeOrder();
			BeanUtils.copyProperties(tradeOrderVo, gpTradeOrder);
			gpTradeOrderMapper.insert(gpTradeOrder);

		} catch (Exception e) {
			logger.error("主扫支付异常！" + e.getMessage()+"##", e);
			throw new RuntimeException("主扫支付异常" + e.getMessage());
		}

		return rs;
	}

	@Override
	public GpRefundOrderVo refund(GpRefundOrderVo refundVo) {
		GpRefundOrderVo rs = new GpRefundOrderVo();
		try {
			refundVo.setOutRefundNo(ApiCallUtil.generateOutRefundNo());
			ApiCallUtil prePay = new ApiCallUtil(ApiCallUtil.FUNCTION_REFUND);
			prePay.setBody(new HashMap<String, String>() {
				{
					put("OutTradeNo", refundVo.getOutTradeNo());
					put("MerchantId", refundVo.getMerchantId());
					put("IsvOrgId", tradeConfig.getIsvOrgId());
					put("OutRefundNo", refundVo.getOutRefundNo());
					put("RefundAmount", String.valueOf(refundVo.getRefundAmount()));
					put("RefundReason",refundVo.getRefundReason());
					put("OperatorId", refundVo.getOperatorId());
					put("DeviceId", refundVo.getDeviceId());
					put("DeviceCreateIp", refundVo.getDeviceCreateIp());
				}
			});

			logger.info("开始调用refund接口, url={}", tradeConfig.getPayUrl());
			Map<String, Object> resMap = prePay.call(tradeConfig.getPayUrl());

			// 数据转换
			rs = MapUtil.map2Obj(resMap, GpRefundOrderVo.class);
			RespInfoVo respInfoVo = rs.getRespInfo();

			if (respInfoVo != null && RESULT_STATUS_SUCCESS.equals(respInfoVo.getResultStatus())) {
				logger.info("退款成功!退款外部交易号：{}，退款订单号：{}", rs.getOutRefundNo(), rs.getRefundOrderNo());
				refundVo.setRefundOrderNo(rs.getRefundOrderNo());
				refundVo.setRefundAmount(rs.getRefundAmount());
				refundVo.setTradeStatus(TRADE_STATUS_REFUNDING);
			} else {
				logger.info("退款失败!退款外部交易号：{}", rs.getOutRefundNo());
				refundVo.setValidInd("0");
			}

			GpRefundOrder gpRefundOrder = new GpRefundOrder();
			BeanUtils.copyProperties(refundVo, gpRefundOrder);
			gpRefundOrderMapper.insert(gpRefundOrder);

		} catch (Exception e) {
			logger.error("退款发起异常！");
			throw new RuntimeException(e);
		}
		return rs;
	}

	@Override
	public GpTradeOrderVo payQuery(String merchantId, String outTradeNo) {

		GpTradeOrderVo rs = new GpTradeOrderVo();
		try {
			ApiCallUtil prePay = new ApiCallUtil(ApiCallUtil.FUNCTION_PAY_QUERY);
			prePay.setBody(new HashMap<String, String>() {
				{
					put("IsvOrgId", tradeConfig.getIsvOrgId());
					put("MerchantId", merchantId);
					put("OutTradeNo", outTradeNo);
				}
			});

			logger.info("开始调用payQuery接口, url={}", tradeConfig.getReqUrl());
			Map<String, Object> resMap = prePay.call(tradeConfig.getReqUrl());

			// 数据转换
			rs = MapUtil.map2Obj(resMap, GpTradeOrderVo.class);
			RespInfoVo respInfoVo = rs.getRespInfo();

			if (respInfoVo != null && RESULT_STATUS_SUCCESS.equals(respInfoVo.getResultStatus())) {
				logger.info("订单查询成功!外部交易号：{}，交易状态：{}", outTradeNo, rs.getTradeStatus());
				GpTradeOrder tradeOrderVo = new GpTradeOrder().setOutTradeNo(outTradeNo).setMerchantId(merchantId);
				tradeOrderVo = gpTradeOrderMapper.selectOne(tradeOrderVo);
				if(tradeOrderVo != null){
					String tradeStatus = tradeOrderVo.getTradeStatus();
					String queryStatus = rs.getTradeStatus();
					if (!queryStatus.equals(tradeStatus)) {
						tradeStatus = queryStatus;
						GpTradeOrder update = new GpTradeOrder();
						update.setId(tradeOrderVo.getId());
						update.setTradeStatus(queryStatus);
						if (TRADE_STATUS_SUCC.equals(queryStatus)) {
							update.setGmtPayment(rs.getGmtPayment());
							update.setBankType(rs.getBankType());
							update.setIsSubscribe(rs.getIsSubscribe());
							update.setPayChannelOrderNo(rs.getPayChannelOrderNo());
							update.setMerchantOrderNo(rs.getMerchantOrderNo());
							update.setSubAppId(rs.getSubAppId());
							update.setCouponFee(rs.getCouponFee());
							update.setBuyerLogonId(rs.getBuyerLogonId());
							update.setBuyerUserId(rs.getBuyerUserId());
							update.setCredit(rs.getCredit());
							update.setReceiptAmount(rs.getReceiptAmount());
							update.setBuyerPayAmount(rs.getBuyerPayAmount());
							update.setInvoiceAmount(rs.getInvoiceAmount());
						}
						try {
							gpTradeOrderMapper.updateById(update);
						} catch (Exception e) {
							logger.error("更新订单状态失败！", e);
						}
					}
				}
			} else {
				logger.info("订单查询失败!外部交易号：{}", outTradeNo);
			}

		} catch (Exception e) {
			logger.error("订单查询异常！", e);
		}

		return rs;
	}

	@Override
	public GpRefundOrderVo refundQuery(String merchantId, String outRefundNo) {
		GpRefundOrderVo rs = new GpRefundOrderVo();
		try {
			ApiCallUtil prePay = new ApiCallUtil(ApiCallUtil.FUNCTION_REFUND_QUERY);
			prePay.setBody(new HashMap<String, String>() {
				{
					put("IsvOrgId", tradeConfig.getIsvOrgId());
					put("MerchantId", merchantId);
					put("OutRefundNo", outRefundNo);
				}
			});

			logger.info("开始调用refundQuery接口, url={}", tradeConfig.getPayUrl());
			Map<String, Object> resMap = prePay.call(tradeConfig.getPayUrl());

			// 数据转换
			rs = MapUtil.map2Obj(resMap, GpRefundOrderVo.class);
			RespInfoVo respInfoVo = rs.getRespInfo();

			if (respInfoVo != null && RESULT_STATUS_SUCCESS.equals(respInfoVo.getResultStatus())) {
				logger.info("【退款订单查询】- 查询成功!退款外部交易号：{}，退款订单号：{}，退款状态：{}", outRefundNo, rs.getRefundOrderNo(), rs.getTradeStatus());
				GpRefundOrder gpRefundOrder = new GpRefundOrder().setOutRefundNo(outRefundNo).setMerchantId(merchantId);
				gpRefundOrder = gpRefundOrderMapper.selectOne(gpRefundOrder);
				if(gpRefundOrder != null){
					String tradeStatus = gpRefundOrder.getTradeStatus();
					String queryTradeStatus = rs.getTradeStatus();
					if(!tradeStatus.equals(queryTradeStatus)){
						GpRefundOrder update = new GpRefundOrder();
						update.setId(gpRefundOrder.getId());
						update.setTradeStatus(queryTradeStatus);
						if(TRADE_STATUS_SUCC.equals(queryTradeStatus)){
							update.setGmtRefundment(rs.getGmtRefundment());
						}
						try {
							gpRefundOrderMapper.updateById(update);
						} catch (Exception e) {
							logger.error("【退款订单查询】- 更新失败！退款外部交易号：{}", outRefundNo, e);
						}
					}
				}
			} else {
				logger.info("【退款订单查询】- 查询失败!退款外部交易号：{}", outRefundNo);
			}

		} catch (Exception e) {
			logger.error("退款订单查询异常！", e);
		}

		return rs;
	}


}

