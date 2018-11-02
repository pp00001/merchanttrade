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

import ins.platform.aggpay.trade.config.TradeConfig;
import ins.platform.aggpay.trade.util.ApiCallUtil;
import ins.platform.aggpay.trade.util.MapUtil;
import ins.platform.aggpay.trade.entity.GgXmlLog;
import ins.platform.aggpay.trade.entity.GpTradeOrder;
import ins.platform.aggpay.trade.mapper.GpTradeOrderMapper;
import ins.platform.aggpay.trade.service.GgNotifyService;
import ins.platform.aggpay.trade.service.GgXmlLogService;
import ins.platform.aggpay.trade.vo.GpTradeOrderVo;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.mybank.bkmerchant.base.HttpsMain;
import com.mybank.bkmerchant.util.XmlSignUtil;
import com.mybank.bkmerchant.util.XmlUtil;

/**
 * @author RipinYan
 * @ClassName: GgNotifyServiceImpl
 * @Description: TODO
 * @date 2018/10/12 下午3:11
 */
@Service
public class GgNotifyServiceImpl implements GgNotifyService {

	private static final Logger logger = LoggerFactory.getLogger(GgNotifyServiceImpl.class);

	@Autowired
	private GpTradeOrderMapper gpTradeOrderMapper;
	@Autowired
	private GgXmlLogService ggXmlLogService;
	@Autowired
	private TradeConfig tradeConfig;

	@Override
	public String prePayNotice(String requestXml) throws Exception {

		String function = ApiCallUtil.FUNCTION_PRE_PAY_NOTICE;
		// 插入报文日志表
		GgXmlLog xmlLog = new GgXmlLog();
		xmlLog.setFunction(function);
		xmlLog.setRequestXml(requestXml);
		xmlLog.setReqTime(new Date());
		ggXmlLogService.insert(xmlLog);

		// 验签
		if (HttpsMain.isSign) {//生产环境需进行rsa验签
			if (!XmlSignUtil.verify(requestXml)) {
				logger.error("验签失败！请求报文如下：" + requestXml);
				throw new Exception("验签失败！");
			}
		}

		// 解析报文
		XmlUtil xmlUtil = new XmlUtil();
		Map<String, Object> resMap = xmlUtil.parseReceive(requestXml, function);

		// 更新订单表
		GpTradeOrderVo orderVo = MapUtil.map2Obj(resMap, GpTradeOrderVo.class);
		GpTradeOrder order = new GpTradeOrder();
		order.setOrderNo(orderVo.getOrderNo());
		order.setMerchantId(orderVo.getMerchantId());
		order = gpTradeOrderMapper.selectOne(order);
		if (order != null) {
			if(order.getTotalAmount() != orderVo.getTotalAmount()){
				logger.error("=====ERROR=====通知报文中订单金额与商户的订单金额不一致，存在假通知！请注意检查数据是否泄漏！");
			}else if (order.getGmtPayment() != null) {
				order.setGmtPayment(orderVo.getGmtPayment());
				order.setBankType(orderVo.getBankType());
				order.setIsSubscribe(orderVo.getIsSubscribe());
				order.setPayChannelOrderNo(orderVo.getPayChannelOrderNo());
				order.setMerchantOrderNo(orderVo.getMerchantOrderNo());
				order.setCouponFee(orderVo.getCouponFee());
				order.setBuyerLogonId(orderVo.getBuyerLogonId());
				order.setBuyerUserId(orderVo.getBuyerUserId());
				order.setCredit(orderVo.getCredit());
				order.setReceiptAmount(orderVo.getReceiptAmount());
				order.setBuyerPayAmount(orderVo.getBuyerPayAmount());
				order.setInvoiceAmount(orderVo.getInvoiceAmount());
			}
			gpTradeOrderMapper.updateById(order);
		}


		String resMsgId = (String) resMap.get("resMsgId");
		StringBuffer sf = new StringBuffer();
		sf.append("<document><response id=\"response\"><head><Version>1.0.0</Version><Appid>");
		sf.append(tradeConfig.getAppId());
		sf.append("</Appid><Function>ant.mybank.bkmerchanttrade.prePayNotice</Function><ResTime>");
		sf.append(new Timestamp(System.currentTimeMillis()));
		sf.append("</ResTime><RespTimeZone>UTC+8</RespTimeZone><ResMsgId>");
		sf.append(resMsgId);
		sf.append("</ResMsgId><Reserve></Reserve><SignType>RSA</SignType><InputCharset>UTF-8</InputCharset></head>");
		sf.append("<body><RespInfo><ResultStatus>S</ResultStatus><ResultCode>0000</ResultCode>");
		sf.append("<ResultMsg></ResultMsg></RespInfo></body></response></document>");

		String responseXml;
		//对response进行加签
		/*if (HttpsMain.isSign) {//生产环境需进行rsa签名
			responseXml = XmlSignUtil.sign(sf.toString());
		}*/

		return sf.toString();
	}

	@Override
	public String notifyPayResult(String requestXml) throws Exception {
		String function = ApiCallUtil.FUNCTION_NOTIFY_PAY_RESULT;

		// 插入报文日志表
		GgXmlLog xmlLog = new GgXmlLog();
		xmlLog.setFunction(function);
		xmlLog.setRequestXml(requestXml);
		//xmlLog.setReqTime(DateUtils.parseDate((String) resMap.get("reqTime"), DatePattern.PURE_DATETIME_PATTERN));
		xmlLog.setReqTime(new Date());
		ggXmlLogService.insert(xmlLog);

		// 验签
		if (HttpsMain.isSign) {//生产环境需进行rsa验签
			if (!XmlSignUtil.verify(requestXml)) {
				logger.error("验签失败！###requestXml###" + requestXml);
				throw new Exception("验签失败");
			}
		}

		// 解析报文
		XmlUtil xmlUtil = new XmlUtil();
		Map<String, Object> resMap = xmlUtil.parse(requestXml, function);

		String resMsgId = (String) resMap.get("resMsgId");
		StringBuffer sf = new StringBuffer();
		sf.append("<document><response id=\"response\"><head><Version>1.0.0</Version><Appid>");
		sf.append(tradeConfig.getAppId());
		sf.append("</Appid>");
		sf.append("<Function>ant.mybank.bkmerchantsettle.notifyPayResult</Function>");
		sf.append("<ResTime>");
		sf.append(new Timestamp(System.currentTimeMillis()));
		sf.append("</ResTime>");
		sf.append("<RespTimeZone>UTC+8</RespTimeZone><ResMsgId>");
		sf.append(resMsgId);
		sf.append("</ResMsgId><Reserve></Reserve><SignType>RSA</SignType><InputCharset>UTF-8</InputCharset></head>");
		sf.append("<body><RespInfo><ResultStatus>S</ResultStatus><ResultCode>0000</ResultCode>");
		sf.append("<ResultMsg></ResultMsg></RespInfo></body></response></document>");

		/*String responseXml;
		//对response进行加签
		if (HttpsMain.isSign) {//生产环境需进行rsa签名
			responseXml = XmlSignUtil.sign(sf.toString());
		}*/

		return sf.toString();
	}
}
