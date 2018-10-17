package com.mybank.bkmerchant.account;

import sun.misc.BASE64Decoder;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import com.mybank.bkmerchant.base.AbstractReq;
import com.mybank.bkmerchant.base.HttpsMain;
import com.mybank.bkmerchant.constant.TradeInStatusEnum;


/**
 * 4.3.5	商户账户交易明细查询
 *
 * 本接口提供查询银行账户相关的交易明细的能力，合作方可为客户查询客户银行账户发生过的交易明细，包括转账、理财等。
 *
 * 请求报文
 IsvOrgId	ISV机构编号
 MerchantId	商户号。网商为商户分配的商户号，通过商户入驻结果查询接口获取。
 QueryStartDate	查询起始日期
 QueryEndDate	查询结束日期。
 PageSize	每页大小。每页返回的交易历史条数，最大不能超过30，超出默认返回30条，不填写默认是10条。
 PageIndex	页码。起始页为0。
 CardNo	客户卡号
 BizTypeList	业务类型，为空默认全部。枚举值：
 01: 转账;
 02: 贷款;
 03: 消费;
 04：结息;
 05: 理财;
 06: 同业;
 07: 票据
 选择多个使用英文逗号隔开
 TradeInStatusList	交易状态。为空默认全部。枚举值：
 Inprocess： 处理中        success： 成功
 Failure： 失败
 Reexchange：退汇（转账成功后发卡行无法入账导致退回）
 选择多个使用英文逗号隔开
 TradeAmtMin	交易最小金额
 TradeAmtMax	交易最大金额

 * 应答报文
 RespInfo	返回码组件
 Currency	币种，只支持人民币金额，参考《附录-币种》
 HasNextPage	是否还有下一页。可选值：
 true：有
 false：没有
 TranHisList	收支明细列表，Json格式base64编码

 * TranHisList
 TradeAmt	交易金额
 TradeGmtCreate	交易创建时间
 TradeGmtModified	交易最后更新时间
 TradeNo	交易编号，用于申请下载回单接口
 BizNo	交易流水号
 BizType	业务类型，填空为默认全部。
           "01", "转账"
           "02", "贷款"
           "03", "消费"
           "04", "结息"
           "05", "理财"
           "06", "同业"
 TradeType	交易类型
             "01", "转入"
             "02", "转出"
             "06", "结息"
             "08", "退汇"
             "11", "份额强增"
             "12", "正常申购"
             "13", "份额强减"
             "14", "正常赎回"
             "15", "正常收益"
 TradeInStatus	交易内部状态, 数据平台单据状态。
                 inprocess 处理中
                 success 成功
                 failure 失败
                 reexchange退汇
 TradeOutStatus	交易外部状态,。银行各业务平台单据状态。一般不使用。
 Currency	币种。默认人民币。
 TradeInOutFlag	交易收支标记，("0", "收入"),, ("1", "支出");
 TradeExpectEnd	交易预计完成日期
 TradeActionChannel	交易发生渠道。通常合作方不用关心此字段。
 TradeName	交易名称。通常合作方不用关心此字段。
 TradeRemark	交易备注
 OwnerOrgName	本方机构名称
 OwnerName	本方名称
 OwnerCardNo	本方卡号
 OwnerAcctType	本方资金账户类型。通常合作方不用关心此字段。
 OwnerAcctNo	本方资金账户编号
 OppositeOrgName	对方机构名称
 OppositeName	对方名称
 OppositeCardNo	对方卡号
 OppositeAcctType	对方资金账户类型。通常合作方不用关心此字段。
 OppositeAcctNo	对方资金账户编号

 *
 * @author jingzhu.zr
 */
public class AccountInfoQuery extends AbstractReq {
  private String isvOrgId = HttpsMain.IsvOrgId;
  private String merchantId = HttpsMain.merchantId;
  private String queryStartDate;
  private String queryEndDate;
  private int pageSize;
  private int pageIndex;
  private String cardNo;
  private Set<String> bizTypeList;
  private List<TradeInStatusEnum> tradeInStatusList;
  private Long tradeAmtMin;
  private Long tradeAmtMax;
  private Map<String, String> body;

  /**
   *
   * @param queryStartDate 查询起始日期
   * @param queryEndDate 查询结束日期
   * @param pageSize 每页大小。每页返回的交易历史条数，最大不能超过30，超出默认返回30条，不填写默认是10条
   * @param pageIndex 页码
   * @param cardNo 客户卡号
   * @param bizTypeList 业务类型，为空默认全部。枚举值：
                          01: 转账;
                          02: 贷款;
                          03: 消费;
                          04：结息;
                          05: 理财;
                          06: 同业;
                          07: 票据
   * @param tradeInStatusList 交易状态。为空默认全部。枚举值：
                                inprocess： 处理中
                                success： 成功
                                failure： 失败
                                reexchange：退汇（转账成功后发卡行无法入账导致退回）
   * @param tradeAmtMin 交易最小金额
   * @param tradeAmtMax 交易最大金额
   */
  public AccountInfoQuery(String queryStartDate, String queryEndDate, int pageSize, int pageIndex, String cardNo, Set<String> bizTypeList, List<TradeInStatusEnum> tradeInStatusList, Long tradeAmtMin, Long tradeAmtMax) {
    super("ant.mybank.stmtcore.accountinfo.query");
    this.queryStartDate = queryStartDate;
    this.queryEndDate = queryEndDate;
    this.pageSize = pageSize;
    this.pageIndex = pageIndex;
    this.cardNo = cardNo;
    this.bizTypeList = bizTypeList;
    this.tradeInStatusList = tradeInStatusList;
    this.tradeAmtMin = tradeAmtMin;
    this.tradeAmtMax = tradeAmtMax;

    this.body = new HashMap<String, String>();
    this.body.put("IsvOrgId", this.isvOrgId);
    this.body.put("MerchantId", this.merchantId);
    this.body.put("QueryStartDate", this.queryStartDate);
    this.body.put("QueryEndDate", this.queryEndDate);
    this.body.put("PageSize", this.pageSize + "");
    this.body.put("PageIndex", this.pageIndex + "");
    this.body.put("CardNo", this.cardNo);
    if(this.bizTypeList != null && !this.bizTypeList.isEmpty()) this.body.put("BizTypeList", getBizTypeList(this.bizTypeList));
    if(this.tradeInStatusList != null && !this.tradeInStatusList.isEmpty()) this.body.put("TradeInStatusList", TradeInStatusEnum.genTradeInStatusList(this.tradeInStatusList));
    if(this.tradeAmtMin != null) this.body.put("TradeAmtMin", this.tradeAmtMin + "");
    if(this.tradeAmtMax != null) this.body.put("TradeAmtMax", this.tradeAmtMax + "");
  }
  
  public String getBizTypeList(Set<String> bizTypeList){
      StringBuilder sb = new StringBuilder();
      if(bizTypeList==null||bizTypeList.size()<=0){
          return sb.toString();
      }
      Iterator<String> it = bizTypeList.iterator();
      while(it.hasNext()){
          sb.append(it.next()).append(",");
      }
      return sb.toString().substring(0,sb.toString().lastIndexOf(","));
  }


  @Override
  public Map<String, String> getBody() {
    return this.body;
  }

  public static void main(String[] args) throws Exception {
    BASE64Decoder decoder = new BASE64Decoder();

    AccountInfoQuery accountInfoQuery = new AccountInfoQuery(
      "20170729",
      "20170729",
      10,
      1,
      "8888886531660936",
      null,
      null,
      null,
      null
    );

    Map<String, Object> rst = accountInfoQuery.call(HttpsMain.reqUrl);
    //将base加密的相关字段解密
    rst.put("TranHisList", new String(decoder.decodeBuffer((String) rst.get("TranHisList")), "UTF-8"));
    System.out.println((String) rst.get("TranHisList"));
  }
}
