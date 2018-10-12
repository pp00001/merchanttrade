/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2016 All Rights Reserved.
 */
package com.mybank.bkmerchant.trade;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.mybank.bkmerchant.base.HttpsMain;
import com.mybank.bkmerchant.util.XmlSignUtil;
import com.mybank.bkmerchant.util.XmlUtil;

/**
 * 4.2.10   订单查询接口<ant.mybank.bkmerchanttrade.payQuery>
 * 
 * @author simon.xxm
 * @version $Id: PayQuery.java, v 0.1 2017年7月26日 下午4:04:57 simon.xxm Exp $
 */
public class PayQuery {

    /**
     * @author simon.xxm
     * @version $Id: BalanceQuery.java, v 0.1 2016年3月3日 下午6:33:49 simon.xxm Exp $
     * @param args      
     */
    public static void main(String[] args) throws Exception {
        //payQuery(HttpsMain.merchantId, "8da48e91ec074585a5a9bae81241829e");
        //微信成功
        // payQuery(HttpsMain.merchantId, "0651e38c0fa243dfab6f99a4f7ed21e5");
        // payQuery(HttpsMain.merchantId, "a64ae87aba43413f9e4b78584c3dfc4f");
        // 微信主扫成功-993edfe6a2844edb854cb845be3019ba
        payQuery(HttpsMain.merchantId, "993edfe6a2844edb854cb845be3019ba");

    }

    public static Map<String, Object> payQuery(String merchantId, String outTradeNo)
                                                                                    throws Exception {
        //订单查询接口
        String function = "ant.mybank.bkmerchanttrade.payQuery";

        XmlUtil xmlUtil = new XmlUtil();
        Map<String, String> form = new HashMap<String, String>();
        form.put("Function", function);
        form.put("ReqTime", new Timestamp(System.currentTimeMillis()).toString());
        //reqMsgId每次报文必须都不一样
        form.put("ReqMsgId", UUID.randomUUID().toString());
        form.put("IsvOrgId", HttpsMain.IsvOrgId);
        form.put("MerchantId", merchantId);
        form.put("OutTradeNo", outTradeNo);

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
        Map<String, Object> resMap = xmlUtil.parse(response, function);
        //支付订单号
        //String orderNo = (String) resMap.get("OrderNo");
        System.out.println(resMap);
        return resMap;
    }

}
