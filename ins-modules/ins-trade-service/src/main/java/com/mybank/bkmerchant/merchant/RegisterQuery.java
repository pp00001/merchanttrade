package com.mybank.bkmerchant.merchant;

import ins.platform.aggpay.trade.util.MapUtil;
import ins.platform.aggpay.trade.vo.RegisterQueryVo;
import ins.platform.aggpay.trade.vo.RespInfoVo;
import sun.misc.BASE64Decoder;

import java.util.HashMap;
import java.util.Map;
import com.mybank.bkmerchant.base.AbstractReq;
import com.mybank.bkmerchant.base.HttpsMain;

/**
 * 4.2.2	商户入驻结果查询
 *
 * 该接口可查询商户入驻申请结果。当合作方请求商户入驻后，网商银行将在一定时间内（5分钟到24小时不等）。合作方可通过该接口回查商户入驻申请结果
 *
 * 请求报文
 IsvOrgId	合作方机构号（网商银行分配）
 OrderNo	申请单号

 * 应答报文
 RespInfo	返回码组件。Result Status 返回S不代表入驻成功，只代表申请单据已经查询到，结果需要看RegisterStatus字段。
 MerchantId	商户号。网商为商户分配的商户号，通过商户入驻结果查询接口获取。
 RegisterStatus	入驻结果。可选值：
                 0：审核中
                 1：成功
                 2：失败
                当入驻结果为审核中，请间隔时间回查。
 OutTradeNo	外部交易号
 OrderNo	申请单号

 *
 * @author jingzhu.zr
 */
public class RegisterQuery extends AbstractReq {
  private String isvOrgId = HttpsMain.IsvOrgId;
  private String orderNo;
  private Map<String, String> body;

  /**
   *
   * @param orderNo 申请单号
   */
  public RegisterQuery(String orderNo) {
    super("ant.mybank.merchantprod.merchant.register.query");
    this.orderNo = orderNo;

    //build body
    this.body = new HashMap<>();
    this.body.put("IsvOrgId", this.isvOrgId);
    this.body.put("OrderNo", this.orderNo);
  }

  @Override
  public Map<String, String> getBody() {
    return this.body;
  }

  public static void main(String[] args) throws Exception {
    RegisterQuery registerQuery = new RegisterQuery("2018091311150710010000000000000000163746");

    Map<String, Object> rst = registerQuery.call();
    RegisterQueryVo vo = MapUtil.map2Obj(rst,RegisterQueryVo.class);
	System.out.println(vo.toString());
    
    System.out.println("#########" + rst.toString());

    RespInfoVo respInfo = new RespInfoVo();
    org.apache.commons.beanutils.BeanUtils.populate(respInfo,(Map<String,Object>)rst.get("respInfo"));
    System.out.println("###respInfo:"+ respInfo.toString());
    //将base加密的相关字段解密
    BASE64Decoder decoder = new BASE64Decoder();
    rst.put("wechatChannelList", new String(decoder.decodeBuffer((String) rst.get("wechatChannelList"))));
    rst.put("alipayChannelList", new String(decoder.decodeBuffer((String) rst.get("alipayChannelList"))));
    System.out.println("wechatChannelList#######"+ rst.get("wechatChannelList"));
    System.out.println("alipayChannelList#######"+ rst.get("alipayChannelList"));
  }
}
