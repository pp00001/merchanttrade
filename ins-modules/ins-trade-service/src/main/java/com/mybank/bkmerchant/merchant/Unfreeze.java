package com.mybank.bkmerchant.merchant;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.mybank.bkmerchant.base.AbstractReq;
import com.mybank.bkmerchant.base.HttpsMain;

/**
 * 4.2.6	商户开启接口
 *
 * 合作方在确认商户问题解除后，可通过商户开启接口重新开启商户收单服务
 *
 * 请求报文
 IsvOrgId	合作方机构号（网商银行分配）
 MerchantId	商户号。网商为商户分配的商户号，通过商户入驻结果查询接口获取。
 UnfreezeReason	商户开启原因
 OutTradeNo	外部交易号

 * 应答报文
 RespInfo	返回码组件
 IsvOrgId	合作方机构号（网商银行分配）
 MerchantId	商户号。网商为商户分配的商户号，通过商户入驻结果查询接口获取。

 *
 * @author jingzhu.zr
 */
public class Unfreeze extends AbstractReq {
  private String isvOrgId = HttpsMain.IsvOrgId;
  private String merchantId = HttpsMain.merchantId;
  private String unfreezeReason;
  private String outTradeNo;
  private Map<String, String> body;

  /**
   *
   * @param unfreezeReason 商户开启原因
   * @param outTradeNo 外部交易号
   */
  public Unfreeze(String unfreezeReason, String outTradeNo) {
    super("ant.mybank.merchantprod.merchant.unfreeze");
    this.unfreezeReason = unfreezeReason;
    this.outTradeNo = outTradeNo;

    this.body = new HashMap<String, String>();
    this.body.put("IsvOrgId", isvOrgId);
    this.body.put("MerchantId", merchantId);
    this.body.put("UnfreezeReason", unfreezeReason);
    this.body.put("OutTradeNo", outTradeNo);
  }

  @Override
  public Map<String, String> getBody() {
    return this.body;
  }

  public static void main(String[] args) throws Exception {
    Unfreeze unfreeze = new Unfreeze("解除风险", UUID.randomUUID().toString());
    unfreeze.call(HttpsMain.reqUrl);
  }
}
