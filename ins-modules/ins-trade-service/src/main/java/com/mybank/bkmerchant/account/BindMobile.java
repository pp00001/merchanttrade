package com.mybank.bkmerchant.account;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.mybank.bkmerchant.base.AbstractReq;
import com.mybank.bkmerchant.base.HttpsMain;

/**
 * 4.3.3	商户账户换绑手机
 *
 * 合作方可通过本接口为商户修改银行账户的预留手机号。预留手机号的修改，需要对新、老手机号动态验证码的校验。如遇上老手机丢失的情况，可由合作方代替客户通过工单的方式请求网商银行进行手机号更换。
 *
 * 请求报文
 IsvOrgId	合作方机构号
 MerchantId	商户号。网商为商户分配的商户号，通过商户入驻结果查询接口获取。
 NewMobile	更新后的手机号
 OldMobileAuthCode	老手机验证码
 NewMobileAuthCode	新手机验证码
 OutTradeNo	外部交易号

 * 应答报文
 RespInfo	返回码组件
 OutTradeNo	外部交易号

 *
 * @author jingzhu.zr
 */
public class BindMobile extends AbstractReq {
  private String isvOrgId = HttpsMain.IsvOrgId;
  private String merchantId = HttpsMain.merchantId;
  private String newMobile;
  private String oldMobileAuthCode;
  private String newMobileAuthCode;
  private String outTradeNo;
  private Map<String, String> body;

  /**
   *
   * @param newMobile 更新后的手机号
   * @param oldMobileAuthCode 老手机验证码
   * @param newMobileAuthCode 新手机验证码
   * @param outTradeNo 外部交易号
   */
  public BindMobile(String newMobile, String oldMobileAuthCode, String newMobileAuthCode, String outTradeNo) {
    super("ant.mybank.merchantprod.account.bindmobile");
    this.newMobile = newMobile;
    this.oldMobileAuthCode = oldMobileAuthCode;
    this.newMobileAuthCode = newMobileAuthCode;
    this.outTradeNo = outTradeNo;

    this.body = new HashMap<String, String>();
    this.body.put("IsvOrgId", this.isvOrgId);
    this.body.put("MerchantId", this.merchantId);
    this.body.put("NewMobile", this.newMobile);
    this.body.put("OldMobileAuthCode", this.oldMobileAuthCode);
    this.body.put("NewMobileAuthCode", this.newMobileAuthCode);
    this.body.put("OutTradeNo", this.outTradeNo);

  }


  @Override
  public Map<String, String> getBody() {
    return this.body;
  }

  public static void main(String[] args) throws Exception {
    BindMobile bindMobile = new BindMobile(
      "13345670987",
      "888888",
      "888888",
      UUID.randomUUID().toString()
    );

    bindMobile.call(HttpsMain.reqUrl);
  }
}
