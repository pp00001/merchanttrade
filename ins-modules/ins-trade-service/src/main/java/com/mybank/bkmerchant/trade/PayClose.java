/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2016 All Rights Reserved.
 */
package com.mybank.bkmerchant.trade;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;

import com.mybank.bkmerchant.base.HttpsMain;
import com.mybank.bkmerchant.constant.ReturnCodeEnum;
import com.mybank.bkmerchant.constant.TradeStatusEnum;
import com.mybank.bkmerchant.util.XmlSignUtil;
import com.mybank.bkmerchant.util.XmlUtil;

/**
 * 4.2.11   订单关闭接口<ant.mybank.bkmerchanttrade.payClose>
 * 
 * @author simon.xxm
 * @version $Id: PayClose.java, v 0.1 2017年7月26日 下午4:04:57 simon.xxm Exp $
 */
public class PayClose {

    /**
     * @author simon.xxm
     * @version $Id: BalanceQuery.java, v 0.1 2016年3月3日 下午6:33:49 simon.xxm Exp $
     * @param args      
     */
    public static void main(String[] args) throws Exception {
        Map<String, Object> map = PayQuery.payQuery(HttpsMain.merchantId,
            "bdc73f7d8c3d4998a816847a2ede7818");
        //非处理中订单不能关闭
        if (MapUtils.isEmpty(map)
            || !StringUtils.equals(TradeStatusEnum.paying.getStatusCode(), map.get("tradeStatus")
                .toString())) {
            System.out.println(map.get("tradeStatus").toString() + "订单状态不是支付中无法关单");
            return;
        }
        payClose(HttpsMain.merchantId, "bdc73f7d8c3d4998a816847a2ede7818");

    }

    @SuppressWarnings("rawtypes")
    public static boolean payClose(String merchantId, String outTradeNo) throws Exception {
        //账户余额查询
        String function = "ant.mybank.bkmerchanttrade.payClose";

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
        String orderNo = (String) resMap.get("OrderNo");
        System.out.println(orderNo);
        String resultCode = (String) ((Map) resMap.get("RespInfo")).get("ResultCode");
        if (StringUtils.equals(ReturnCodeEnum.SUCCESS.getReturnCode(), resultCode)
            || StringUtils.equals(ReturnCodeEnum.ORDER_CLOSED.getReturnCode(), resultCode)) {
            return true;
        }
        return false;
    }

}
