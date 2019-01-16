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

package ins.platform.aggpay.trade.util;

import ins.platform.aggpay.trade.entity.GgXmlLog;
import ins.platform.aggpay.trade.service.impl.GgXmlLogServiceImpl;

import java.util.Date;
import java.util.Map;
import org.apache.commons.lang3.time.DateUtils;
import com.mybank.bkmerchant.base.AbstractReq;
import com.xiaoleilu.hutool.date.DatePattern;
import com.xiaoleilu.hutool.date.DateUtil;
import com.xiaoleilu.hutool.util.RandomUtil;


public class ApiCallUtil extends AbstractReq {

	public final static String FUNCTION_SEND_SMS_CODE = "ant.mybank.merchantprod.sendsmscode";
	public final static String FUNCTION_UPLOAD_PHOTO = "ant.mybank.merchantprod.merchant.uploadphoto";
	public final static String FUNCTION_REGISTER = "ant.mybank.merchantprod.merchant.register";
	public final static String FUNCTION_REGISTER_QUERY = "ant.mybank.merchantprod.merchant.register.query";
	public final static String FUNCTION_MERCHANT_QUERY = "ant.mybank.merchantprod.merchant.query";
	public final static String FUNCTION_MERCHANT_UPDATE = "ant.mybank.merchantprod.merchant.updateMerchant";
	public final static String FUNCTION_FREEZE = "ant.mybank.merchantprod.merchant.freeze";
	public final static String FUNCTION_UNFREEZE = "ant.mybank.merchantprod.merchant.unfreeze";

	public final static String FUNCTION_PAY = "ant.mybank.bkmerchanttrade.pay";
	public final static String FUNCTION_PRE_PAY = "ant.mybank.bkmerchanttrade.prePay";
	public final static String FUNCTION_DYNAMIC_ORDER = "ant.mybank.bkmerchanttrade.dynamicOrder";
	public final static String FUNCTION_REFUND = "ant.mybank.bkmerchanttrade.refund";
	public final static String FUNCTION_PAY_QUERY = "ant.mybank.bkmerchanttrade.payQuery";
	public final static String FUNCTION_REFUND_QUERY = "ant.mybank.bkmerchanttrade.refundQuery";
	public static final String FUNCTION_PRE_PAY_NOTICE = "ant.mybank.bkmerchanttrade.prePayNotice";
	public static final String FUNCTION_NOTIFY_PAY_RESULT = "ant.mybank.bkmerchantsettle.notifyPayResult";

	private Map<String, String> body;

	public ApiCallUtil(String function) {
		super(function);
	}

	@Override
	public Map<String, String> getBody() {
		return this.body;
	}

	@Override
	public Map<String, Object> call(String reqUrl) throws Exception {
		GgXmlLog xmlLog = new GgXmlLog();
		Map<String, Object> resMap = super.call(reqUrl);
		xmlLog.setFunction(this.function);
		xmlLog.setReqTime(new Date());
		xmlLog.setRequestXml((String) resMap.get("requestXml"));
		xmlLog.setResponseXml(((String) resMap.get("responseXml")).replaceAll("\n", ""));
		xmlLog.setRespTime(DateUtils.parseDate((String) resMap.get("RespTime"), DatePattern.PURE_DATETIME_PATTERN));
		Object obj = resMap.get("respInfo");
		if (obj != null && obj instanceof Map) {
			Map<String, String> respInfo = (Map<String, String>) obj;
			xmlLog.setResultCode(respInfo.get("resultCode"));
			xmlLog.setResultStatus(respInfo.get("resultStatus"));
			xmlLog.setResultMsg(respInfo.get("resultMsg"));
		}
		GgXmlLogServiceImpl ggXmlLogService = (GgXmlLogServiceImpl) BeanManager.getBean("ggXmlLogServiceImpl");
		ggXmlLogService.insert(xmlLog);
		return resMap;
	}

	/**
	 * 外部交易号生成器
	 *
	 * @return
	 */
	public static String generateOutTradeNo() {
		StringBuffer sf = new StringBuffer();
		sf.append("INS");
		sf.append(DateUtil.format(new Date(), DatePattern.PURE_DATETIME_PATTERN));
		sf.append(RandomUtil.randomNumbers(7));
		return sf.toString();
	}

	/**
	 * 退款外部交易号生成器
	 *
	 * @return
	 */
	public static String generateOutRefundNo() {
		StringBuffer sf = new StringBuffer();
		sf.append("INSRF");
		sf.append(DateUtil.format(new Date(), DatePattern.PURE_DATETIME_PATTERN));
		sf.append(RandomUtil.randomNumbers(5));
		return sf.toString();
	}

	public void setBody(Map<String, String> body) {
		this.body = body;
	}
}
