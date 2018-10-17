package com.mybank.bkmerchant.merchant;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.mybank.bkmerchant.base.AbstractReq;
import com.mybank.bkmerchant.base.HttpsMain;

/**
 * 4.2.5	商户关闭接口
 *
 * 合作方一旦认定商户存在风险或存在其他的问题，可通过商户关闭接口关闭商户的收单服务。
 * 商户关闭前完成的收单交易，资金照常清算，需要截留资金需线下联系。
 *
 * 请求报文
 IsvOrgId	合作方机构号（网商银行分配）
 MerchantId	商户号。网商为商户分配的商户号，通过商户入驻结果查询接口获取。
 FreezeReason	商户关闭原因
 OutTradeNo	外部交易号

 * 应答报文
 RespInfo	返回码组件
 OutTradeNo	外部交易号

 *
 * @author jingzhu.zr
 */
public class Freeze extends AbstractReq {
  private String isvOrgId = HttpsMain.IsvOrgId;
  private String merchantId = HttpsMain.merchantId;
  private String freezeReason;
  private String outTradeNo;
  private Map<String, String> body;

  /**
   *
   * @param freezeReason 商户关闭原因
   * @param outTradeNo 外部交易号
   */
  public Freeze(String freezeReason, String outTradeNo) {
    super("ant.mybank.merchantprod.merchant.freeze");
    this.freezeReason = freezeReason;
    this.outTradeNo = outTradeNo;

    this.body = new HashMap<String, String>();
    this.body.put("IsvOrgId", isvOrgId);
    this.body.put("MerchantId", merchantId);
    this.body.put("FreezeReason", freezeReason);
    this.body.put("OutTradeNo", outTradeNo);
  }
  
  public Freeze(){
	  super("ant.mybank.merchantprod.merchant.freeze");
  }
  
  @Override
  public Map<String, String> getBody() {
    return this.body;
  }

  public static void main(String[] args) throws Exception {
    Freeze freeze = new Freeze("存在欺诈风险", UUID.randomUUID().toString());
    freeze.call(HttpsMain.reqUrl);
  }
}
