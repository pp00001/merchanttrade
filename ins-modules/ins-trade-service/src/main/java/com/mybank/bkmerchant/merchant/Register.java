package com.mybank.bkmerchant.merchant;

import ins.platform.aggpay.trade.vo.GgBankCardParamVo;
import ins.platform.aggpay.trade.vo.GgFeeParamVo;
import ins.platform.aggpay.trade.vo.GgMerchantDetailVo;
import lombok.Data;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.json.JSONException;
import com.mybank.bkmerchant.base.AbstractReq;
import com.mybank.bkmerchant.base.HttpsMain;
import com.xiaoleilu.hutool.date.DatePattern;
import com.xiaoleilu.hutool.date.DateUtil;
import com.xiaoleilu.hutool.util.RandomUtil;

/**
 *  商户入驻申请接口（不开银行账户）
 *
 *  <p/>
 *  对接网商银行移动支付平台的合作方需要将委托收单的商户信息通过本接口申请入驻网商平台。
 *  入驻申请后，网商银行会进行商户资质的审查，在5分钟~24小时内返回入驻申请处理结果。
 *  合作方可通过商户入驻申请回查接口获知处理结果。
 *  <li>
 *    注意：同一个商户，入驻不成功再次入驻不要更换外部商户号！！
 *  </li>
 *
 * 测试环境银行账号、人名、身份证号码限定如下
 *  "accountType":  "01",
 *  "bankCardNo":  "6212261202002415210",
 *  "bankCertName":  "名称",
 *  "branchCity":  "330100",
 *  "branchName":  "工商银行",
 *  "branchProvince":  "330000",
 *  "cardHolderAddress":  "杭州",
 *  "certNo":  "202436199402060494",
 *  "certType":  "01",
 *  "contactLine":  "102331022094"
 *
 *
 *  @author jingzhu.zr
 */
@Data
public class Register extends AbstractReq {

  /**
   * 合作方机构号（网商银行分配）
   */
  private String isvOrgId = HttpsMain.IsvOrgId;

  /**
   * 外部商户号。合作商对商户的自定义编码，要求同一个合作商下保持唯一
   */
  private String outMerchantId;

  /**
   * 商户名称。有营业执照的，要求与营业执照上的名称一致
   */
  private String merchantName;

  /**
   * 商户类型
   */
  private String merchantType;

  /**
   * 商户经营类型
   */
  private String dealtype;

  /**
   * 商户清算资金是否支持T+0到账
   */
  private String supportPrepayment;

  /**
   * 结算方式
   */
  private String settleMode;

  /**
   * 经营类目
   */
  private String mcc;

  /**
   * 商户详情
   */
  private GgMerchantDetailVo merchantDetail;

  /**
   * 支持交易类型列表
   */
  private String tradeTypeList;

  /**
   * 支持支付渠道列表
   */
  private String payChannelList;

  /**
   * 禁用支付方式
   */
  private String deniedPayToolList;

  /**
   * 手续费列表
   */
  private List<GgFeeParamVo> feeParamList;

  /**
   * 清算卡参数
   */
  private GgBankCardParamVo bankCardParam;

  /**
   * 手机验证码。
   * 备注：合作方系统须先调网商短信验证码发送接口，BizType=6，商户获取验证码后在申请页面回填验证码，合作方系统通过本接口统一上送
   */
  private String authCode;

  /**
   * 外部交易号。
   * 合作方系统生成的外部交易号，同一交易号被视为同一笔交易
   */
  private String outTradeNo;

  /**************2018-09-12 新增**************/
  /**
   * 商户是否可自主选择让其买家使用花呗分期，包括商户承担手续费和用户承担手续费两种模式。目前仅支持用户承担手续费模式。
   * 可选值：
   * Y：支持
   * N：不支持(默认值)
   */
  private String supportStage;
  /**
   * 商户在进行微信支付H5支付时所使用的公众号相关信息的类型
   * 01：商户自有公众号。适合商户本身有公众号的情况。
   * 03：合作机构对接网商时留存的公众号。适合商户本身没有公众号，关注合作机构的公众号的情况。
   * 备注：
   * 1、	由于微信支付要求商户名称和公众号的认证主体要严格一致，商户需要根据自身情况选择一种方式去报送公众号支付相关的信息。
   */
  private String partnerType;
  /**
   * 记录商户由哪一个渠道接入支付宝的，该字段用于营销激励、风控等方面。是谁带来的商户就填写谁的PID。
   * （重要：通过签约支付宝返佣协议且配合该字段，商户的推荐方可获得支付宝的返佣资金，请千万不要填错该字段！）
   */
  private String alipaySource;
  /**
   * ISV可线下联系网商运营申请支持指定微信渠道号入驻，该字段用于指定在哪个微信渠道号下进件
   */
  private String wechatChannel;
  /**
   * 支付宝费率版本。可选值：
   *  RS：标准费率
   *  R0：0费率
   *  R1：千一费率
   *  RBLUE：蓝海行动餐饮0费率
   *  说明：参数为非必填项。不传或传RS，都是走支付宝默认费率；选择R0、R1时需要按照支付宝低费率进件要求，
   *       联系网商银行先完成线下审核，才能进件；传递RBLUE时，为参加蓝海行动餐饮0费率报备
   */
  private String rateVersion;




  private Map<String, String> body;
  
  
  
  public Register(){
	  super("ant.mybank.merchantprod.merchant.register");
  };
  
  public Register(
    String merchantName,
    String merchantType,
    String dealtype,
    String supportPrepayment,
    String settleMode,
    String mcc,
    GgMerchantDetailVo merchantDetail,
    String tradeTypeList,
    String payChannelList,
    String deniedPayToolList,
    List<GgFeeParamVo> feeParamList,
    GgBankCardParamVo bankCardParam,
    String authCode,
    String outTradeNo,
    String supportStage,
    String partnerType,
    String alipaySource,
    String wechatChannel,
    String rateVersion
  ) throws JSONException {
    super("ant.mybank.merchantprod.merchant.register");
    this.outMerchantId = DateUtil.format(new Date(), DatePattern.PURE_DATE_FORMAT) + RandomUtil.simpleUUID();
    this.merchantName = merchantName;
    this.merchantType = merchantType;
    this.dealtype = dealtype;
    this.supportPrepayment = supportPrepayment;
    this.settleMode = settleMode;
    this.mcc = mcc;
    this.merchantDetail = merchantDetail;
    this.tradeTypeList = tradeTypeList;
    this.payChannelList = payChannelList;
    this.deniedPayToolList = deniedPayToolList;
    this.feeParamList = feeParamList;
    this.bankCardParam = bankCardParam;
    this.authCode = authCode;
    this.outTradeNo = outTradeNo;
    this.supportStage = supportStage;
    this.partnerType = partnerType;
    this.alipaySource = alipaySource;
    this.wechatChannel = wechatChannel;
    this.rateVersion = rateVersion;

    this.body = new HashMap<>();
    this.body.put("IsvOrgId", this.isvOrgId);
    this.body.put("OutMerchantId", this.outMerchantId);
    this.body.put("MerchantName", this.merchantName);
    this.body.put("MerchantType", this.merchantType);
    this.body.put("DealType", this.dealtype);
    this.body.put("SupportPrepayment", this.supportPrepayment);
    this.body.put("SettleMode", this.settleMode);
    this.body.put("Mcc", this.mcc);
    this.body.put("MerchantDetail", this.merchantDetail.genJsonBase64());
    this.body.put("TradeTypeList", this.tradeTypeList);
    this.body.put("PayChannelList", this.payChannelList);
    this.body.put("DeniedPayToolList", this.deniedPayToolList);
    this.body.put("FeeParamList", GgFeeParamVo.genJsonBase64(this.feeParamList));
    this.body.put("BankCardParam", this.bankCardParam.genJsonBase64());
    this.body.put("AuthCode", this.authCode);
    this.body.put("OutTradeNo", this.outTradeNo);
    this.body.put("SupportStage", this.supportStage);
    this.body.put("PartnerType", this.partnerType);
    this.body.put("AlipaySource", this.alipaySource);
    this.body.put("WechatChannel", this.wechatChannel);
    this.body.put("RateVersion", this.rateVersion);
  }

  @Override
  public Map<String, String> getBody() {
    return this.body;
  }


  public static void main(String[] args) throws Exception {
    //nature();
    company();

  }

  /**
   * 企业 - 226801000000142576686
   * WechatMerchId - 242972555
   * ChannelId - 240824008
   *
   * @throws Exception
   */
  private static void company() throws Exception {
//    MerchantDetail merchantDetail = new MerchantDetail(
//            "中科软-企业1号",
//            "17620358925",
//            "严水平",
//            "", "", "", "",
//            "01040304230",
//            "yanshuiping925@163.com",
//            "名称",
//            "",
//            PrincipalCertTypeEnum.IdentityCard,
//            "430723198809102650",
//            "海南新视线人力资源服务有限公司",
//            "123232322111112",
//            "",
//            "d9a8f406-b3ee-44f5-be5e-78cbfafd77e3",
//            "5b80e0d2-728f-43a8-ba50-1e76e9796b73",
//            "e63f55bc-8d87-4b98-818c-9f87e1f3bc0e", "ff8a44ca-cc1d-4fc9-a51e-9e6ad0509883", "b9467b2e-a095-4d0e-8c21-f63503fe356e", "", "",""
//    );
//
//    List<TradeTypeEnum> tradeTypeEnumList = new ArrayList<TradeTypeEnum>();
//    tradeTypeEnumList.add(TradeTypeEnum.Forward);
//    tradeTypeEnumList.add(TradeTypeEnum.Backward);
//    tradeTypeEnumList.add(TradeTypeEnum.Refund);
//
//    List<PayChannelEnum> payChannelEnumList = new ArrayList<PayChannelEnum>();
//    payChannelEnumList.add(PayChannelEnum.Ali);
//    payChannelEnumList.add(PayChannelEnum.WX);
//
//    List<DeniedPayToolEnum> deniedPayToolEnumList = new ArrayList<DeniedPayToolEnum>();
//    deniedPayToolEnumList.add(DeniedPayToolEnum.Huabei);
//
//    List<FeeParam> feeParamList = new ArrayList<FeeParam>();
//    feeParamList.add(new FeeParam(PayChannelEnum.Ali, FeeTypeEnum.T1, "0.0035"));
//    feeParamList.add(new FeeParam(PayChannelEnum.WX, FeeTypeEnum.T1, "0.0035"));
//
//    BankCardParam bankCardParam = new BankCardParam(
//            "6212261202002415210",
//            "名称",
//            AccountType.ORIENTED_PUBLIC,
//            "102331022094", "工商银行", "330000", "330100",
//            "01",
//            "202436199402060494",
//            "杭州"
//    );
//
//    // 订单号：2018091811150710010000000000000000165092
//    bankCardParam = new BankCardParam(
//            "6212261202002415210",
//            "名称",
//            AccountType.ORIENTED_PUBLIC,
//            "102331022094", "", "", "",
//            "01",
//            "202436199402060494",
//            "12"
//    );
//
//    Register register = new Register(
//            "名称",
//            //MerchantTypeEnum.Enterprising
//            "03",
//            //DealTypeEnum.Entity,
//            "01",
//            //SupportPrepaymentEnum.NotSupport,
//            "N",
//            //SettleModeEnum.BankCard,
//            "01",
////            MccEnum.Cate,
//            "2015050700000000",
//            merchantDetail,
////            tradeTypeEnumList,
//            "01,02,06",
////            payChannelEnumList,
//            "01,02",
////            deniedPayToolEnumList,
//            "03",
//            feeParamList,
//            bankCardParam,
//            "888888",
//            RandomUtil.simpleUUID(),
//            "N",
//            "03",
//            "2018090700000286",
//            "",
//            "RS"
//    );
//    
//    System.out.println("post Json: "+JSON.toJSONString(register));
//    
//    Map<String, Object> call = register.call();
//    System.out.println("#######" + call.toString());
  }


  /**
   * 自然人
   * WechatMerchId - 242080272
   * ChannelId - 240824008
   * MerchantId - 226801000000141472259
   * @throws Exception
   */
  private static void nature() throws Exception {
//    MerchantDetail merchantDetail = new MerchantDetail(
//      "中科软1号",
//      "17620358925",
//      "严水平",
//      "", "", "", "",
//      "01040304230",
//      "yanshuiping925@163.com",
//      "",
//      "17620358925",
//      PrincipalCertTypeEnum.IdentityCard,
//      "202436199402060494",
//      "名称",
//      "",
//	  "",
//      "d9a8f406-b3ee-44f5-be5e-78cbfafd77e3",
//      "5b80e0d2-728f-43a8-ba50-1e76e9796b73",
//      "", "", "", "", "",""
//    );
//
//    List<TradeTypeEnum> tradeTypeEnumList = new ArrayList<TradeTypeEnum>();
//    tradeTypeEnumList.add(TradeTypeEnum.Forward);
//    tradeTypeEnumList.add(TradeTypeEnum.Backward);
//    tradeTypeEnumList.add(TradeTypeEnum.Refund);
//
//    List<PayChannelEnum> payChannelEnumList = new ArrayList<PayChannelEnum>();
//    payChannelEnumList.add(PayChannelEnum.Ali);
//    payChannelEnumList.add(PayChannelEnum.WX);
//
//    List<DeniedPayToolEnum> deniedPayToolEnumList = new ArrayList<DeniedPayToolEnum>();
//    deniedPayToolEnumList.add(DeniedPayToolEnum.Huabei);
//
//    List<FeeParam> feeParamList = new ArrayList<FeeParam>();
//    feeParamList.add(new FeeParam(PayChannelEnum.Ali, FeeTypeEnum.T1, "0.0035"));
//    feeParamList.add(new FeeParam(PayChannelEnum.WX, FeeTypeEnum.T1, "0.0035"));
//
//    BankCardParam bankCardParam = new BankCardParam(
//      "6212261202002415210",
//      "名称",
//      AccountType.ORIENTED_PRIVATE,
//      "",
//            "",
//            "",
//            "",
//      PrincipalCertTypeEnum.IdentityCard.getCertCode(),
//      "202436199402060494",
//      "杭州"
//    );
//
//    Register register = new Register(
//      "中科软1号",
////      MerchantTypeEnum.Natural,
//      "01",
////      DealTypeEnum.Entity,
//      "01",
////      SupportPrepaymentEnum.NotSupport,
//      "N",
////      SettleModeEnum.BankCard,
//      "01",
////      MccEnum.Cate,
//      "2015050700000000",
//      merchantDetail,
////      tradeTypeEnumList,
//      "01,02,06",
////      payChannelEnumList,
//      "01,02",
////      deniedPayToolEnumList,
//      "03",
//      feeParamList,
//      bankCardParam,
//		    "888888",
//      UUID.randomUUID().toString().replace("-",""),
//		    "N",
//		    "03",
//		    "2018090700000286",
//            "",
//            "RS"
//    );
//
//    Map<String, Object> call = register.call();
//    System.out.println("#######" + call.toString());
  }

}
