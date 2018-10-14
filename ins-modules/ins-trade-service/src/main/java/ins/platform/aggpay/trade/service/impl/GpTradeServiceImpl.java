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

import ins.platform.aggpay.trade.service.GpTradeService;
import ins.platform.aggpay.trade.vo.GpTradeOrderVo;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import com.mybank.bkmerchant.base.HttpsMain;
import com.mybank.bkmerchant.util.XmlSignUtil;
import com.mybank.bkmerchant.util.XmlUtil;

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
		form.put("OutTradeNo",UUID.randomUUID().toString().replace("-",""));
		form.put("Body","反扫测试-碧螺春9");
		form.put("GoodsTag","test");
		form.put("GoodsDetail","test");
		form.put("TotalAmount","3");
		form.put("Currency","CNY");
		form.put("MerchantId", HttpsMain.merchantId);
		form.put("IsvOrgId", HttpsMain.IsvOrgId);
		form.put("ChannelType","WX");
		form.put("ChannelType","ALI");
		//        form.put("OperatorId","test");
		//        form.put("StoreId","test");
		//        form.put("DeviceId","test");
		form.put("DeviceCreateIp","112.97.59.21");
		form.put("ExpireExpress","120");
		form.put("SettleType","T1");
		form.put("Attach","附加信息");
		form.put("PayLimit","pcredit");
		form.put("DiscountableAmount","1");
		form.put("UndiscountableAmount","2");
		//form.put("AlipayStoreId","支付宝的店铺编号");
		form.put("SysServiceProviderId","2018090700000286");
		//form.put("CheckLaterNm","");
		form.put("SubAppId","wx62a55dbdd041bb1d");
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
			String b=(String)resMap.get("OutTradeNo");
			System.out.println(b);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	@Override
	public GpTradeOrderVo prePay(GpTradeOrderVo tradeOrderVo) {
		try {
        /* String URL = "https://openapi.alipay.com/gateway.do";
        String APP_ID = "2018073160766860";
      //  APP_ID = "2018090700000286";
        String APP_PRIVATE_KEY = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCV3Ju7rfIehCjimtzIeMb8lrZszv2w0QDdbtjNmKcvl3yuzTMzKddSs7X
        +ESPyydnKITLphExpliP/smqP0nfJFg7qFaNJTQxXxq3lLTZW82Bh8pEN2ZcsLUgkstLiTDv+NoQjkDw7Kw9NcRcgOlZseeO1zlemIE6u4zM8QIHNQYXcrDq1uaOcyU3LG02pq1
        /du2XljdQIN8mEj7deFnfs4XFLlB/GbG+4HbiQGfD/nh75qWvqdOHGu9UZUwxgpkaBMIboKZXXPVSOt6HR0KvEQcKnmbf+Ozavbb005Y8mR3qrhtSWC5TzIt
        /wtfSVO2Vq5I223CgfyFMNy/fPxO8FAgMBAAECggEAfqUolrqxWkilpJrq6h/nSZ60G8/xZHO8H2WFonnXD8kdfTu8gQhjB2kH6+XgU1Vxz/euZtl/GdvgID5O
        /6wFvtH9WKVgkJmkTKmCW6KRwXl7gkrTerjfoF3EEf9taAC00miP0t4ZiIcjL3ba7Elgr9tyLDpsp9+1nuYybkC1cRcXNKz63euk
        +zhyBrYwMoROBzchFfyaTOHlGy1GXe2cLLz78t0PywWglHu/lHGTDHIBBZKHTrXM0AM3LVaigUVX82m9Cjo9+IW/uV+ckJzM/Vo0qpLUlgi90wQbGW8DcGljfK
        /efPQWuzohOgG7QMUQtwwd0fR+JbspFXRcBsEHAQKBgQDlTI+XOVY+5sR6y2B9pul3DyWacRSLmNkVqSm0POq9ziuW9A21ABg7Tj3I83A6mjCyVSwtGL7k0wtAs9okf+nhw/jo45EN
        /yQlOCqcVa+2ZwszUsMt8MOV2UFc67fvwguEKkQxeIJptJx7uX59v7OkaKMu4LzOoTs+uELGpi7OeQKBgQCnUAJi3yB8h7vqe
        +bI3uef6Bz8UQW5i2FIJzFZIEPTvCh7a2onSgUqa3Hev7/5x5TH6BsepHaEacLQTGlDxORcfU+mpwtY1QMSIR7WAPwtHgVoE1ClJ1PQx6tBykvZJ3Tto5j3cLmObENUVTqxp6ON
        /zNoPN/ImoAkfaHQCxfR7QKBgQCxWlBNxTliGZeq6pdNWMaHIh4RoJkliCmQSXFKSTu/ZzHr5gScFOCpLlE3lqMdkJlNtfcfQl6UGnA/sVxukslRqARkDW
        /qhYdtik3a8aOgz36oScFRTUHaK1oVBvUT6uCBbzejk/Q60kmeoNCnbpkB9zUQUx92KtrqHhb4Ex1s8QKBgC61b3UaxX7+hb+Yh31cfV1u92iZVffOqYHzLxuqnkTmKocKHcCKMZb+F
        /QPpBCfXzHP4oJTd6LPw8tTCbAZr4cClNH5oHlUPl85T9p+u+f8kZXUjpcMu6F1nKHpT/N3yHTvTy0FE0hngQRyJsih/E8QB9H57J+cQlntmNbzwdqhAoGANo
        /a54HZNsig31Qu6Ngls6eLMFxPB6Y61XxOb8+y2uoB4QYbnL+6as2NDEzZOmvlOlgPDF+eXsFMltWZnXvFQeNCvfu5a9
        /Gy8CbHGywXP4wyajV8IiXLVFqjCNwd62Mo6szxS1JkRUcstFdpfRhyupVzke/Yu9s/i9SBvV2liE=";
        String ALIPAY_PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAoDyE8wbAUt4jwVkzp69Mv1uoxLa3at9y7ecFvq+ZpNOO0uTcAuYFNuWlegi
        /rlMPtZUBZ8quyRGKt87DcIfdqKeoEf7B62N759nj2yB5svfF7iRTgdzk6aipLa9C2PET/rg5akxg2cqA0lwBlLe7Np6ZgLmmYL
        +Ck6P4fqolUCuf9lxVdbAK0OCtb1iFi78VSBIEgUyYXvWVDNMcw83ow0YYZBRF/8KZhr0/4puIp6+XX50PGL/zZOwXWbgAaCa5OELo+Y/gwcFgo
        /YlhaEBGmu5kEzpYO4R1IhEjEtaWa0zuik8J0Cjpki4dosPCrKmUeciFEzEwB2wMEhAXpKQKQIDAQAB";

        AlipayClient alipayClient = new DefaultAlipayClient(URL, APP_ID, APP_PRIVATE_KEY, "json", "UTF-8", ALIPAY_PUBLIC_KEY, "RSA2");

        AlipaySystemOauthTokenRequest request = new AlipaySystemOauthTokenRequest();
        request.setCode("edf39ce11cb444549736b2022c98SX37");
        request.setCode("87df25d4b23c4637ba7b85ee0020XX44");
        request.setGrantType("authorization_code");
        String userId = "";
        try {
            AlipaySystemOauthTokenResponse oauthTokenResponse = alipayClient.execute(request);
            userId = oauthTokenResponse.getUserId();
            System.out.println("userId="+userId);
            System.out.println(oauthTokenResponse.getAccessToken());
        } catch (AlipayApiException e) {
            //处理异常
            e.printStackTrace();
        }*/

			//账户余额查询
			String function = "ant.mybank.bkmerchanttrade.prePay";

			XmlUtil xmlUtil = new XmlUtil();
			Map<String, String> form = new HashMap<String, String>();
			form.put("Function", function);
			form.put("ReqTime", new Timestamp(System.currentTimeMillis()).toString());
			//reqMsgId每次报文必须都不一样
			form.put("ReqMsgId", UUID.randomUUID().toString());
			form.put("OutTradeNo", UUID.randomUUID().toString().replace("-", ""));
			form.put("Body", "主扫测试-无人机12");
			form.put("GoodsTag", "test");
			form.put("GoodsDetail", "test");
			form.put("TotalAmount", "1");
			form.put("Currency", "CNY");
			form.put("MerchantId", HttpsMain.merchantId);
			form.put("IsvOrgId", HttpsMain.IsvOrgId);
			form.put("ChannelType", "WX");
			form.put("ChannelType", "ALI");
			// form.put("OpenId",userId);
			form.put("OpenId", "otBP8wWRG63MS9IZkK27hhO0jYnM");
			form.put("OpenId", "2088412422162444");

			form.put("OperatorId", "test");
			form.put("StoreId", "test");
			form.put("DeviceId", "test");
			form.put("DeviceCreateIp", "112.97.59.21");
			form.put("ExpireExpress", "1440");
			form.put("SettleType", "T1");
			form.put("Attach", "test");

			//form.put("NotifyUrl","http://notifyurl");
			form.put("SubAppId", "wx62a55dbdd041bb1d");
			form.put("SpecifySubMerchId", "N");
			form.put("ChannelId", "");
			form.put("SubMerchId", "");
			form.put("PayLimit", "pcredit");
			form.put("DiscountableAmount", "1");
			form.put("UndiscountableAmount", "1");
			form.put("AlipayStoreId", "支付宝的店铺编号");
			form.put("SysServiceProviderId", "2018090700000286");
			form.put("CheckLaterNm", "0");
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
			logger.error("主扫支付异常！" + e.getMessage(), e);
		}

		return null;
	}
}
