/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2016 All Rights Reserved.
 */
package com.mybank.bkmerchant.merchant;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;

import com.mybank.bkmerchant.base.HttpsMain;
import com.mybank.bkmerchant.constant.AccountType;
import com.mybank.bkmerchant.constant.DealTypeEnum;
import com.mybank.bkmerchant.constant.DeniedPayToolEnum;
import com.mybank.bkmerchant.constant.FeeTypeEnum;
import com.mybank.bkmerchant.constant.MccEnum;
import com.mybank.bkmerchant.constant.PayChannelEnum;
import com.mybank.bkmerchant.constant.PrincipalCertTypeEnum;
import com.mybank.bkmerchant.constant.ReturnCodeEnum;
import com.mybank.bkmerchant.constant.SettleModeEnum;
import com.mybank.bkmerchant.constant.SupportPrepaymentEnum;
import com.mybank.bkmerchant.constant.TradeTypeEnum;
import com.mybank.bkmerchant.models.BankCardParam;
import com.mybank.bkmerchant.models.FeeParam;
import com.mybank.bkmerchant.models.MerchantDetail;
import com.mybank.bkmerchant.util.XmlSignUtil;
import com.mybank.bkmerchant.util.XmlUtil;

/**
 * 5.2.4    商户信息修改<ant.mybank.merchantprod.merchant.updateMerchant>
 * 本接口修改商户信息。商户信息修改后会立即生效，修改不当可能影响商户正常收单交易及资金清算。
 * @author simon.xxm
 * @version $Id: PayCancle.java, v 0.1 2017年7月26日 下午4:04:57 simon.xxm Exp $
 */
public class UpdateMerchant {

	private String isvOrgId = HttpsMain.IsvOrgId;

	private String outMerchantId;
	
	private DealTypeEnum dealtype;
	
	private SupportPrepaymentEnum supportPrepayment;
	
	private SettleModeEnum settleMode;
	
	private MccEnum mcc;
	
	private MerchantDetail merchantDetail;
	
	private List<TradeTypeEnum> tradeTypeList;
	
	private List<PayChannelEnum> payChannelList;
	
	private List<DeniedPayToolEnum> deniedPayToolList;
	
	private List<FeeParam> feeParamList;
	
	private BankCardParam bankCardParam;
	
	private String outTradeNo;
	
	private String supportStage;
	
	private String alipaySource;
		
    /**
     * @author simon.xxm
     * @version $Id: BalanceQuery.java, v 0.1 2016年3月3日 下午6:33:49 simon.xxm Exp $
     * @param args      
     */
    public static void main(String[] args) throws Exception {
        updateMerchant(HttpsMain.merchantId, UUID.randomUUID().toString());

    }

    public static boolean updateMerchant(String merchantId, String outTradeNo) throws Exception {
        //商户信息修改
        String function = "ant.mybank.merchantprod.merchant.updateMerchant";

        XmlUtil xmlUtil = new XmlUtil();
        Map<String, String> form = new HashMap<String, String>();
        form.put("Function", function);
        form.put("ReqTime", new Timestamp(System.currentTimeMillis()).toString());
        //reqMsgId每次报文必须都不一样
        form.put("ReqMsgId", UUID.randomUUID().toString());
        form.put("OutTradeNo", outTradeNo);
        form.put("IsvOrgId", HttpsMain.IsvOrgId);
        form.put("MerchantId", merchantId);
        form.put("DealType", DealTypeEnum.EntityAndInternet.getDealCode());
        //form.put("SupportPrepayment", SupportPrepaymentEnum.NotSupport.getSupCode());
        //form.put("SettleMode", SettleModeEnum.BankCard.getSettleCode());
        form.put("Mcc", MccEnum.HealthCare.getMccId());
//        MerchantDetail merchantDetail = new MerchantDetail("小商",
//            "18609320193", "小商老板", "", "", "", "", "010-40304230", "xiaoshang@qq.com", "",
//            "18609320193", PrincipalCertTypeEnum.IdentityCard, "211103198007194934", "小商老板", "",
//            "", "身份证正面.jpg", "身份证反面.jpg", "", "", "", "", "","");
        MerchantDetail merchantDetail = new MerchantDetail(
                "中科软1号",
                "17620358925",
                "严水平",
                "", "", "", "",
                "01040304230",
                "yanshuiping925@163.com",
                "",
                "",
                PrincipalCertTypeEnum.IdentityCard,
                "202436199402060494",
                "名称",
                "",
                "",
                "d9a8f406-b3ee-44f5-be5e-78cbfafd77e3",
                "5b80e0d2-728f-43a8-ba50-1e76e9796b73",
                "", "", "", "", "",""
        );
        merchantDetail = new MerchantDetail();
//        merchantDetail.setContactName("水电费");
        // 已测
        merchantDetail.setAlias("中科软2号");
        //merchantDetail.setCertPhotoB("2461eae4-4813-4022-811f-4be94c41a398");
        form.put("MerchantDetail", merchantDetail.genJsonBase64());
        List<TradeTypeEnum> tradeTypeEnumList = new ArrayList<TradeTypeEnum>();
        tradeTypeEnumList.add(TradeTypeEnum.Forward);
        tradeTypeEnumList.add(TradeTypeEnum.Backward);
        tradeTypeEnumList.add(TradeTypeEnum.Refund);
        //form.put("TradeTypeList", TradeTypeEnum.genTradeTypeList(tradeTypeEnumList));
        List<PayChannelEnum> payChannelEnumList = new ArrayList<PayChannelEnum>();
        payChannelEnumList.add(PayChannelEnum.Ali);
        payChannelEnumList.add(PayChannelEnum.WX);
        //form.put("PayChannelList", PayChannelEnum.genPayChannelList(payChannelEnumList));
        List<DeniedPayToolEnum> deniedPayToolEnumList = new ArrayList<DeniedPayToolEnum>();
        deniedPayToolEnumList.add(DeniedPayToolEnum.CreditCard);
        //form.put("DeniedPayToolList", DeniedPayToolEnum.genDenniedPayToolList(deniedPayToolEnumList));

        List<FeeParam> feeParamList = new ArrayList<FeeParam>();
        feeParamList.add(new FeeParam(PayChannelEnum.Ali, FeeTypeEnum.T1, "0.0031"));
        feeParamList.add(new FeeParam(PayChannelEnum.WX, FeeTypeEnum.T1, "0.0031"));
        // 已测试
        // form.put("FeeParamList", FeeParam.genJsonBase64(feeParamList));
//        BankCardParam bankCardParam = new BankCardParam("23423244243242423", "工商银行",
//            AccountType.ORIENTED_PRIVATE, "", "", "", "",
//            PrincipalCertTypeEnum.IdentityCard.getCertCode(), "211032349", "杭州市");

        BankCardParam bankCardParam = new BankCardParam(
                "6212261202002415210",
                "名称",
                AccountType.ORIENTED_PRIVATE,
                "",
                "",
                "",
                "",
                PrincipalCertTypeEnum.IdentityCard.getCertCode(),
                "202436199402060494",
                "杭州"
        );
        //form.put("BankCardParam", bankCardParam.genJsonBase64());

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
        @SuppressWarnings("rawtypes")
        String resultCode = (String) ((Map) resMap.get("RespInfo")).get("ResultCode");
        if (StringUtils.equals(ReturnCodeEnum.SUCCESS.getReturnCode(), resultCode)) {
            return true;
        }
        return false;
    }

}
