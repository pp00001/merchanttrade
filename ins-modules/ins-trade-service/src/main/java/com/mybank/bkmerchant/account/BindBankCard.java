package com.mybank.bkmerchant.account;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.mybank.bkmerchant.base.AbstractReq;
import com.mybank.bkmerchant.base.HttpsMain;

/**
 * 4.3.2	商户账户换绑卡
 *
 * 商户修改开户时绑定的银行卡。银行账户绑定卡更换后，余利宝提现的银行卡随之调整。
 * 换绑卡的卡要求与商户本人同人（姓名、身份证号码一致）。交易时，银行会做预留手机号动态验证码校验，须商户先调用短信发送接口发送验证码，获取到客户回填的短信验证码后才提交换绑卡请求。

 * 请求报文
 IsvOrgId	合作方机构号
 MerchantId	商户号。网商为商户分配的商户号，通过商户入驻结果查询接口获取。
 NewBankCardNo	新绑定卡卡号
 NewMobile	新绑定卡在发卡银行的预留手机号。如果该号码与网商预留手机号不一致，须先更换网商预留手机号。
 MobileAuthCode	网商银行业务预留手机号验证码。特指商户开户时使用的手机号，可更换。
 OutTradeNo	外部交易号

 * 应答报文
 RespInfo	返回码组件
 OutTradeNo	外部交易号

 *
 * @author jingzhu.zr
 */
public class BindBankCard extends AbstractReq {
  private String isvOrgId = HttpsMain.IsvOrgId;
  private String merchantId = HttpsMain.merchantId;
  private String newBankCardNo;
  private String newMobile;
  private String mobileAuthCode;
  private String outTradeNo;
  private Map<String, String> body;

  /**
   *
   * @param newBankCardNo 新绑定卡卡号
   * @param newMobile 新绑定卡在发卡银行的预留手机号。如果该号码与网商预留手机号不一致，须先更换网商预留手机号
   * @param mobileAuthCode 网商银行业务预留手机号验证码。特指商户开户时使用的手机号，可更换
   * @param outTradeNo 外部交易号
   */
  public BindBankCard(String newBankCardNo, String newMobile, String mobileAuthCode, String outTradeNo) {
    super("ant.mybank.merchantprod.account.bindbankcard");
    this.newBankCardNo = newBankCardNo;
    this.newMobile = newMobile;
    this.mobileAuthCode = mobileAuthCode;
    this.outTradeNo = outTradeNo;

    this.body = new HashMap<String, String>();
    this.body.put("IsvOrgId", this.isvOrgId);
    this.body.put("MerchantId", this.merchantId);
    this.body.put("NewBankCardNo", this.newBankCardNo);
    this.body.put("NewMobile", this.newMobile);
    this.body.put("MobileAuthCode", this.mobileAuthCode);
    this.body.put("OutTradeNo", this.outTradeNo);

  }


  @Override
  public Map<String, String> getBody() {
    return this.body;
  }

  public static void main(String[] args) throws Exception {
    BindBankCard bindBankCard = new BindBankCard(
      "6228480402564890018",
      "13345670987",
      "888888",
      UUID.randomUUID().toString()
    );

    bindBankCard.call(HttpsMain.reqUrl);
  }
}
