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

import ins.platform.aggpay.trade.config.IsvConfig;
import ins.platform.aggpay.trade.constant.TradeConstant;
import static ins.platform.aggpay.trade.constant.TradeConstant.RespInfo.RESULT_STATUS_SUCCESS;
import ins.platform.aggpay.trade.entity.GgXmlLog;
import ins.platform.aggpay.trade.entity.GpTradeOrder;
import ins.platform.aggpay.trade.mapper.GpTradeOrderMapper;
import ins.platform.aggpay.trade.service.GgXmlLogService;
import ins.platform.aggpay.trade.service.GpTradeService;
import ins.platform.aggpay.trade.util.ApiCallUtil;
import ins.platform.aggpay.trade.util.MapUtil;
import ins.platform.aggpay.trade.vo.GpTradeOrderVo;
import ins.platform.aggpay.trade.vo.RespInfoVo;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.mybank.bkmerchant.base.HttpsMain;
import com.mybank.bkmerchant.util.XmlSignUtil;
import com.mybank.bkmerchant.util.XmlUtil;
import com.xiaoleilu.hutool.date.DatePattern;

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
	private IsvConfig isvConfig;
	@Autowired
	private GgXmlLogService ggXmlLogService;

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

		GgXmlLog xmlLog = new GgXmlLog();
		GpTradeOrderVo rs = new GpTradeOrderVo();
		try {
			String function = ApiCallUtil.FUNCTION_PREPAY;
			xmlLog.setFunction(function);
			xmlLog.setReqTime(new Date());
			tradeOrderVo.setOutTradeNo(ApiCallUtil.generateOutTradeNo());

			ApiCallUtil prePay = new ApiCallUtil(function);
			prePay.setBody(new HashMap<String, String>() {
				{
					put("IsvOrgId", HttpsMain.IsvOrgId);
					put("OutTradeNo", tradeOrderVo.getOutTradeNo());
					put("Body", tradeOrderVo.getBody());
					put("GoodsTag", "test");
					put("GoodsDetail", "test");
					put("TotalAmount", String.valueOf(tradeOrderVo.getTotalAmount()));
					put("Currency", "CNY");
					put("MerchantId", tradeOrderVo.getMerchantId());
					put("IsvOrgId", isvConfig.getIsvOrgId());
					put("ChannelType", tradeOrderVo.getChannelType());
					put("OpenId", tradeOrderVo.getOpenId());
					put("OperatorId", tradeOrderVo.getOperatorId());
					put("StoreId", tradeOrderVo.getStoreId());
					put("DeviceId", tradeOrderVo.getDeviceId());
					put("DeviceCreateIp", tradeOrderVo.getDeviceCreateIp());
					if (tradeOrderVo.getExpireExpress() != null && tradeOrderVo.getExpireExpress() > 0) {
						put("ExpireExpress", String.valueOf(tradeOrderVo.getExpireExpress()));
					} else {
						put("ExpireExpress", isvConfig.getExpireExpress());
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

			Map<String, Object> resMap = prePay.call(isvConfig.getPayUrl());

			// 插入报文日志表

			xmlLog.setRequestXml((String) resMap.get("requestXml"));
			xmlLog.setResponseXml((String) resMap.get("responseXml"));
			xmlLog.setRespTime(DateUtils.parseDate((String) resMap.get("RespTime"), DatePattern.PURE_DATETIME_PATTERN));

			// 数据转换
			rs = MapUtil.map2Obj(resMap, GpTradeOrderVo.class);
			RespInfoVo respInfoVo = rs.getRespInfo();
			xmlLog.setResultStatus(respInfoVo.getResultStatus());
			xmlLog.setResultCode(respInfoVo.getResultCode());
			xmlLog.setResultMsg(respInfoVo.getResultMsg());

			tradeOrderVo.setRespInfo(respInfoVo);
			if (respInfoVo != null && RESULT_STATUS_SUCCESS.equals(respInfoVo.getResultStatus())) {
				logger.info("订单创建成功!外部交易号：{}，订单号：{}", rs.getOutTradeNo(), rs.getOrderNo());
				tradeOrderVo.setOrderNo(rs.getOrderNo());
				tradeOrderVo.setPrePayId(rs.getPrePayId());
				tradeOrderVo.setPayInfo(rs.getPayInfo());
			} else {
				logger.info("订单创建失败!外部交易号：{}", rs.getOutTradeNo());
				tradeOrderVo.setValidInd("0");
			}

			GpTradeOrder gpTradeOrder = new GpTradeOrder();
			BeanUtils.copyProperties(tradeOrderVo, gpTradeOrder);
			gpTradeOrderMapper.insert(gpTradeOrder);

		} catch (Exception e) {
			xmlLog.setFlag("0");
			logger.error("主扫支付异常！" + e.getMessage()+"##", e);
			throw new RuntimeException("主扫支付异常" + e.getMessage());
		} finally {
			ggXmlLogService.insert(xmlLog);

		}

		/*switch (result.getTradeStatus()) {
			case SUCCESS:
				log.info("支付宝支付成功: )");
				break;

			case FAILED:
				log.error("支付宝支付失败!!!");
				break;

			case UNKNOWN:
				log.error("系统异常，订单状态未知!!!");
				break;

			default:
				log.error("不支持的交易状态，交易返回异常!!!");
				break;
		}*/

		return rs;
	}


}
