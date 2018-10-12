package com.mybank.bkmerchant.merchant;

import sun.misc.BASE64Decoder;

import java.util.HashMap;
import java.util.Map;

import com.mybank.bkmerchant.base.AbstractReq;
import com.mybank.bkmerchant.base.HttpsMain;

/**
 * 4.2.4	商户信息查询接口
 *
 * 合作方可通过该接口查询本机构名下进驻的商户信息
 *
 * 请求报文
 IsvOrgId	合作方机构号（网商银行分配）
 MerchantId	商户号。网商为商户分配的商户号，通过商户入驻结果查询接口获取。

 * 应答报文
 RespInfo	返回码组件
 MerchantName	商户名称。有营业执照的，要求与营业执照上的名称一致。
 MerchantId	商户号。网商为商户分配的商户号，通过商户入驻结果查询接口获取。
 MerchantType	商户类型。可选值：
               01:自然人
               02:个体工商户
               03:企业商户
               DealType	商户经营类型。可选值：
               01:实体特约商户
               02:网络特约商户
               03:实体兼网络特约商户
 SupportPrepayment	商户清算资金是否支持T+0到账。可选值：
                     Y：支持
                     N：不支持
 SettleMode	结算方式。商户清算资金结算方式，可选值：
             01：结算到他行卡
             02：结算到余利宝
             03：结算到活期户（暂不开放）
 Mcc	经营类目。参见附录的经营类目上送。
 MerchantDetail	商户详情，json格式base64编码，具体报文定义参考下面的商户详情
 TradeTypeList	支持交易类型列表。该商户能支持的交易类型，多个用逗号隔开。可选值：
                 01：正扫交易
                 02：反扫交易
                 06：退款交易
 PayChannelList	支持支付渠道列表。该商户能支持的第三方支付渠道。多个用逗号隔开。可选值：
                 01：支付宝
                 02：微信支付
                 03：手机QQ（暂未开放）
                 04：京东钱包（暂未开放）
 DeniedPayToolList	禁用支付方式。商户禁受理支付方式列表，多个用逗号隔开。可选值：
                     02：信用卡
                     03：花呗（仅支付宝）
 FeeParamList	手续费列表。json格式base64编码，具体报文定义参考下面的手续费列表
 BankCardParam	清算卡参数。json格式base64编码，具体报文定义参考下面的清算卡，仅结算方式为“01结算到他行卡”需要填写

 *
 * @author jingzhu.zr
 */
public class MerchantQuery extends AbstractReq {
  private String isvOrgId = HttpsMain.IsvOrgId;
  private String merchantId = HttpsMain.merchantId;
  private Map<String, String> body;

  public MerchantQuery(String merchantId) {
    super("ant.mybank.merchantprod.merchant.query");

    this.body = new HashMap<String, String>();
    this.body.put("IsvOrgId", isvOrgId);
    this.body.put("MerchantId", merchantId);
  }
  
  public MerchantQuery(){
	  super("ant.mybank.merchantprod.merchant.query");
  }
  

  @Override
  public Map<String, String> getBody() {
    return this.body;
  }

  public static void main(String[] args) throws Exception {
    BASE64Decoder decoder = new BASE64Decoder();
    MerchantQuery merchantQuery = new MerchantQuery("226801000000142576686");
    Map<String, Object> rst = merchantQuery.call();

    //将base加密的相关字段解密
    rst.put("merchantDetail", new String(decoder.decodeBuffer((String) rst.get("merchantDetail")), "UTF-8"));
    rst.put("feeParamList", new String(decoder.decodeBuffer((String) rst.get("feeParamList"))));
    rst.put("bankCardParam", new String(decoder.decodeBuffer((String) rst.get("bankCardParam"))));
    rst.put("wechatChannelList", new String(decoder.decodeBuffer((String) rst.get("wechatChannelList"))));
    System.out.println((String) rst.get("merchantDetail"));
    System.out.println((String) rst.get("feeParamList"));
    System.out.println((String) rst.get("bankCardParam"));
    System.out.println((String) rst.get("wechatChannelList"));
  }
}
