package com.mybank.bkmerchant.account;

import java.util.HashMap;
import java.util.Map;

import com.mybank.bkmerchant.base.AbstractReq;
import com.mybank.bkmerchant.base.HttpsMain;

/**
 * 4.3.4	商户账户查询接口
 *
 * 合作方通过本接口客户查询商户银行卡相关的信息，包括姓名、预留手机号、绑定的银行卡号等。
 *
 * 请求报文
 IsvOrgId	合作方机构号
 MerchantId	商户号。网商为商户分配的商户号，通过商户入驻结果查询接口获取。

 * 应答报文
 RespInfo	返回码组件
 CertName	银行账户户主姓名
 Mobile	手机号。网商银行业务预留手机号
 BankCardNo	绑定银行卡号

 * @author jingzhu.zr
 */
public class AccountQuery extends AbstractReq {
  private String isvOrgId = HttpsMain.IsvOrgId;
  private String merchantId = HttpsMain.merchantId;
  private Map<String, String> body;

  public AccountQuery() {
    super("ant.mybank.merchantprod.account.query");

    this.body = new HashMap<String, String>();
    this.body.put("IsvOrgId", isvOrgId);
    this.body.put("MerchantId", merchantId);
  }

  @Override
  public Map<String, String> getBody() {
    return this.body;
  }

  public static void main(String[] args) throws Exception {
    AccountQuery accountQuery = new AccountQuery();
    accountQuery.call(HttpsMain.reqUrl);
  }
}
