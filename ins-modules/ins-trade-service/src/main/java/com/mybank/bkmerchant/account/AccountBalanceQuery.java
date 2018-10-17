package com.mybank.bkmerchant.account;

import java.util.HashMap;
import java.util.Map;

import com.mybank.bkmerchant.base.AbstractReq;
import com.mybank.bkmerchant.base.HttpsMain;

/**
 * 4.3.6	活期账户余额查询
 *
 * 查询客户银行账户的活期余额
 *
 * 请求报文
 IsvOrgId	ISV机构编号
 MerchantId	商户号。网商为商户分配的商户号，通过商户入驻结果查询接口获取。
 CardNo	卡号。卡号与商户号必须匹配，否则返回错误。
 Currency	币种, 消费金额的币种，只支持人民币金额，参考《附录-币种》
 CashExCode	钞汇标识：CSH钞; EXG汇。本场景不存在汇，请送CSH

 * 应答报文
 RespInfo	返回码组件
 CustomerType	客户类型
 OpenOrg	开户机构
 ArrangementId	存款合约编号
 AccountId	资金帐号
 CashExCode	钞汇标识，CSH钞; EXG汇
 OpenDate	开户时间
 CloseDate	销户时间
 TermUnit	存期单位。通常合作方不用关心此字段。
 Term	存期。通常合作方不用关心此字段。
 ArrangementChineseName	合约中文名称。通常合作方不用关心此字段。
 ArrangementEnglishName	合约英文名称。通常合作方不用关心此字段。
 BusinessType	业务类型，0: 活期存款; 1: 整存整取; 2: 通知存款; 3: 零存整取; 4: 存本取息;5: 定活两便;6: 协定存款;7: 过渡户;8: 教育储蓄;9: 组合账户;10: 协议存款。通常送0即可。
 AccountPurpose	帐户用途，100: 一般存款; 101: 贷款还款; 103: 信用卡还款; 104: 代发工资; 105: 购买理财; 106: 承兑保证金; 107: 担保保证金;108: 信用证保证金;109: 信用卡保证金;110: 外汇买卖保证金; 113: 其他用途
 AccountAttribute	帐户属性，1100:经常项目--结算账户
 AccountStatus	账户状态。枚举值：
 0：不存在
 1：可用
 2：不可用
 AvailableBalance	可用余额
 ActualBalance	实际余额。实际余额=可用余额+账户冻结金额。
 FreezeAmount	账户冻结金额。因出现司法冻结等问题冻结的账户金额。
 Currency	币种
 CreditFreeze	贷记/流入冻结情况
 DebtFreeze	借记/流出冻结情况

 *
 * @author jingzhu.zr
 */
public class AccountBalanceQuery extends AbstractReq {
  private String isvOrgId = HttpsMain.IsvOrgId;
  private String merchantId = HttpsMain.merchantId;
  private String cardNo;
  private String currency = "CNY";
  private String cashExCode = "CSH";
  private Map<String, String> body;

  /**
   * @param cardNo 卡号。卡号与商户号必须匹配，否则返回错误
   */
  public AccountBalanceQuery(String cardNo) {
    super("ant.mybank.depbuss.accountbalance.query");
    this.cardNo = cardNo;

    this.body = new HashMap<String, String>();
    this.body.put("IsvOrgId", this.isvOrgId);
    this.body.put("MerchantId", this.merchantId);
    this.body.put("CardNo", this.cardNo);
    this.body.put("Currency", this.currency);
    this.body.put("CashExCode", this.cashExCode);
  }

  @Override
  public Map<String, String> getBody() {
    return this.body;
  }

  public static void main(String[] args) throws Exception {
    AccountBalanceQuery accountBalanceQuery = new AccountBalanceQuery(
      "8888886531660936"
    );

    accountBalanceQuery.call(HttpsMain.reqUrl);
  }
}
