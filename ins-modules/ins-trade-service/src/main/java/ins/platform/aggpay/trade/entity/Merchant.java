/*
 * Copyright (c) 2018-2020, Ripin Yan. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ins.platform.aggpay.trade.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.enums.IdType;

/**
 * <p>
 * 商户信息主表
 * </p>
 *
 * @author ripin
 * @since 2018-09-18
 */
@Data
public class Merchant extends Model<Merchant> {

	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	@TableId(value = "id", type = IdType.AUTO)
	private Long id;
	/**
	 * 外部商户号
	 */
	@TableField("out_merchant_id")
	private String outMerchantId;
	/**
	 * 申请单号
	 */
	@TableField("order_no")
	private String orderNo;
	/**
	 * 商户号
	 */
	@TableField("merchant_id")
	private String merchantId;
	/**
	 * 商户名称
	 */
	@TableField("merchant_name")
	private String merchantName;
	/**
	 * 商户类型 - 01:自然人,02:个体工商户,03:企业商户
	 */
	@TableField("merchant_type")
	private String merchantType;
	/**
	 * 商户经营类型 - 01:实体特约商户, 02:网络特约商户, 03:实体兼网络特约商户
	 */
	@TableField("deal_type")
	private String dealType;
	/**
	 * 商户清算资金是否支持T+0到账 - Y：支持, N：不支持
	 */
	@TableField("support_prepayment")
	private String supportPrepayment;
	/**
	 * 结算方式 - 01：结算到他行卡, 02：结算到余利宝, 03：结算到活期户（暂不开放）, 04：自提打款（暂不开放）
	 */
	@TableField("settle_mode")
	private String settleMode;
	/**
	 * 经营类目
	 */
	private String mcc;
	/**
	 * 支持交易类型列表 - 01：正扫交易, 02：反扫交易, 06：退款交易, 08: 动态订单扫码
	 */
	@TableField("trade_type_list")
	private String tradeTypeList;
	/**
	 * 支持支付渠道列表 - 01：支付宝, 02：微信支付, 03：手机QQ（暂未开放）
	 */
	@TableField("pay_channel_list")
	private String payChannelList;
	/**
	 * 禁用支付方式 - 02：信用卡, 03：花呗（仅支付宝）
	 */
	@TableField("denied_pay_tool_list")
	private String deniedPayToolList;
	/**
	 * 手机验证码
	 */
	@TableField("auth_code")
	private String authCode;
	/**
	 * 外部交易号
	 */
	@TableField("out_trade_no")
	private String outTradeNo;
	/**
	 * 花呗分期目前仅支持用户承担手续费模式 - Y：支持, N：不支持(默认值)
	 */
	@TableField("support_stage")
	private String supportStage;
	/**
	 * 商户在进行微信支付H5支付时所使用的公众号相关信息的类型 - 01：商户自有公众号,  03：ISV公众号
	 */
	@TableField("partner_type")
	private String partnerType;
	/**
	 * 记录商户推荐方PID
	 */
	@TableField("alipay_source")
	private String alipaySource;
	/**
	 * 指定微信渠道号入驻
	 */
	@TableField("wechat_channel")
	private String wechatChannel;
	/**
	 * 支付宝费率版本 - RS：标准费率, R0：0费率, R1：千一费率, RBLUE：蓝海行动餐饮0费率
	 */
	@TableField("rate_version")
	private String rateVersion;
	/**
	 * 入驻状态 - 0：审核中，1：成功，2：失败
	 */
	@TableField("register_status")
	private String registerStatus;
	/**
	 * 入驻失败原因
	 */
	@TableField("fail_reason")
	private String failReason;
	/**
	 * Smid
	 */
	private String smid;
	/**
	 * 微信支付渠道号
	 */
	@TableField("channel_id")
	private String channelId;
	/**
	 * 微信商户号
	 */
	@TableField("wechat_merch_id")
	private String wechatMerchId;
	/**
	 * 微信入驻状态 - 0：处理中，1：成功，2：失败
	 */
	@TableField("wechat_status")
	private String wechatStatus;
	/**
	 * 微信入驻失败原因
	 */
	@TableField("wechat_fail_reason")
	private String wechatFailReason;
	/**
	 * 是否有效 - 1：有效，0：无效
	 */
	@TableField("valid_ind")
	private String validInd;
	/**
	 * 是否删除  -1：已删除  0：正常
	 */
	@TableField("del_flag")
	private String delFlag;
	/**
	 * 创建人代码
	 */
	@TableField("creator_code")
	private String creatorCode;
	/**
	 * 创建时间
	 */
	@TableField("create_time")
	private Date createTime;
	/**
	 * 更新人代码
	 */
	@TableField("updater_code")
	private String updaterCode;
	/**
	 * 更新时间
	 */
	@TableField("update_time")
	private Date updateTime;

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

	@Override
	public String toString() {
		return "Merchant{" + ", id=" + id + ", outMerchantId=" + outMerchantId + ", orderNo=" + orderNo + ", merchantId=" + merchantId + ", " + "merchantName=" + merchantName + ", merchantType=" + merchantType + ", dealType=" + dealType + ", supportPrepayment=" +
                supportPrepayment + ", settleMode=" + settleMode + ", mcc=" + mcc + ", tradeTypeList=" + tradeTypeList + ", payChannelList=" +
                payChannelList + ", deniedPayToolList=" + deniedPayToolList + ", authCode=" + authCode + ", outTradeNo=" + outTradeNo + ", " +
                "supportStage=" + supportStage + ", partnerType=" + partnerType + ", alipaySource=" + alipaySource + ", wechatChannel=" +
                wechatChannel + ", rateVersion=" + rateVersion + ", registerStatus=" + registerStatus + ", failReason=" + failReason + ", smid=" +
                smid + ", channelId=" + channelId + ", wechatMerchId=" + wechatMerchId + ", wechatStatus=" + wechatStatus + ", wechatFailReason=" +
                wechatFailReason + ", validInd=" + validInd + ", delFlag=" + delFlag + ", creatorCode=" + creatorCode + ", createTime=" +
                createTime + ", updaterCode=" + updaterCode + ", updateTime=" + updateTime + "}";
	}
}
