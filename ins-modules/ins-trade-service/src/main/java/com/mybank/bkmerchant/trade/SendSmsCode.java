/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2016 All Rights Reserved.
 */
package com.mybank.bkmerchant.trade;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;

import com.mybank.bkmerchant.base.AbstractReq;
import com.mybank.bkmerchant.base.HttpsMain;
import com.mybank.bkmerchant.constant.BizTypeEnum;
import com.mybank.bkmerchant.constant.ReturnCodeEnum;
import com.mybank.bkmerchant.util.XmlSignUtil;
import com.mybank.bkmerchant.util.XmlUtil;

/**
 * 4.1.1    短信验证码发送接口<ant.mybank.merchantprod.sendsmscode>
 *
 * @author simon.xxm
 * @version $Id: SendSmsCode.java, v 0.1 2017年7月26日 下午4:04:57 simon.xxm Exp $
 */
public class SendSmsCode {

	private String isvOrgId;
	private String bizType;
	private String merchantId;
	private String mobile;
	private String outTradeNo;
	
	/**
	 * @param args
	 * @author simon.xxm
	 * @version $Id: BalanceQuery.java, v 0.1 2016年3月3日 下午6:33:49 simon.xxm Exp $
	 */
	public static void main(String[] args) throws Exception {
		System.out.println("执行结果：" + sendSmsCode(HttpsMain.merchantId, UUID.randomUUID().toString().replace("-",""), BizTypeEnum.MerchantdApply,
				"17620358925"));

	}

	public static Map<String, Object> sendSmsCodeReturnMap(String merchantId, String outTradeNo, BizTypeEnum bizType, String mobile) throws Exception {
		//短信验证码发送
		String function = "ant.mybank.merchantprod.sendsmscode";

		XmlUtil xmlUtil = new XmlUtil();
		Map<String, String> form = new HashMap<String, String>();
		form.put("Function", function);
		form.put("ReqTime", new Timestamp(System.currentTimeMillis()).toString());
		//reqMsgId每次报文必须都不一样
		form.put("ReqMsgId", UUID.randomUUID().toString());
		//form.put("MerchantId", merchantId);
		form.put("OutTradeNo", outTradeNo);
		form.put("BizType", bizType.getBizCode());
		form.put("Mobile", mobile);
		form.put("IsvOrgId", HttpsMain.IsvOrgId);

		//封装报文
		String param = xmlUtil.format(form, function);
		if (HttpsMain.isSign) {//生产环境需进行rsa签名
			param = XmlSignUtil.sign(param);
		}
		System.out.println("-------------------------");
		System.out.println("---------REQUEST---------");
		System.out.println("-------------------------");
		System.out.println(param);
		//发送请求
		String response = HttpsMain.httpsReq(HttpsMain.reqUrl, param);
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
//		Map<String, Object> resMap = xmlUtil.parse(response, function);
		return xmlUtil.parse(response, function);
	}
	
	@SuppressWarnings("rawtypes")
	public static boolean sendSmsCode(String merchantId, String outTradeNo, BizTypeEnum bizType, String mobile) throws Exception {
		//解析报文
		Map<String, Object> resMap = sendSmsCodeReturnMap(merchantId,outTradeNo,bizType,mobile);
		//外部交易号
		outTradeNo = (String) resMap.get("OutTradeNo");
		System.out.println(outTradeNo);
		if (StringUtils.equals(ReturnCodeEnum.SUCCESS.getReturnCode(), (String) ((Map) resMap.get("RespInfo")).get("ResultCode"))) {
			return true;
		}
		return false;
	}

	public String getIsvOrgId() {
		return isvOrgId;
	}

	public void setIsvOrgId(String isvOrgId) {
		this.isvOrgId = isvOrgId;
	}
	
	public String getBizType() {
		return bizType;
	}

	public void setBizType(String bizType) {
		this.bizType = bizType;
	}

	public String getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getOutTradeNo() {
		return outTradeNo;
	}

	public void setOutTradeNo(String outTradeNo) {
		this.outTradeNo = outTradeNo;
	}
}
