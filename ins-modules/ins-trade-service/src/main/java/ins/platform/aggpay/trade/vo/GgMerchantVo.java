/*
 * Copyright (c) 2018. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package ins.platform.aggpay.trade.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

import com.mybank.bkmerchant.base.HttpsMain;
import com.mybank.bkmerchant.models.BankCardParam;
import com.mybank.bkmerchant.models.MerchantDetail;

/**
 * @author RipinYan
 * @ClassName: GgMerchantVo
 * @Description: TODO
 * @date 2018/9/18 下午2:32
 */
@Data
public class GgMerchantVo implements Serializable {

	private static final long serialVersionUID = 1L;
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
	private GgMerchantDetailVo ggMerchantDetailVo;

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
	private GgBankCardParamVo ggBankCardParamVo;

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
	 * RS：标准费率
	 * R0：0费率
	 * R1：千一费率
	 * RBLUE：蓝海行动餐饮0费率
	 * 说明：参数为非必填项。不传或传RS，都是走支付宝默认费率；选择R0、R1时需要按照支付宝低费率进件要求，
	 * 联系网商银行先完成线下审核，才能进件；传递RBLUE时，为参加蓝海行动餐饮0费率报备
	 */
	private String rateVersion;

	private MerchantDetail merchantDetail;
	
	private BankCardParam bankCardParam;
	
}
