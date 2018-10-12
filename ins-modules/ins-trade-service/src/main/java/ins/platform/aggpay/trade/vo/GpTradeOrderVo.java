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

package ins.platform.aggpay.trade.vo;

import lombok.Data;

import java.util.Date;

/**
 * @author RipinYan
 * @ClassName: GpTradeOrderVo
 * @Description: TODO
 * @date 2018/10/12 下午3:53
 */
@Data
public class GpTradeOrderVo {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    private Long id;
    /**
     * 支付单类型 - create：主扫，pay：被扫，create_dynamic：动态扫码
     */
    private String orderType;
    /**
     * 订单号
     */
    private String orderNo;
    /**
     * 第三方支付授权码
     */
    private String authCode;
    /**
     * 外部交易号
     */
    private String outTradeNo;
    /**
     * 商品描述
     */
    private String body;
    /**
     * 商品标记
     */
    private String goodsTag;
    /**
     * 商品详情列表
     */
    private String goodsDetail;
    /**
     * 交易总金额
     */
    private Integer totalAmount;
    /**
     * 币种
     */
    private String currency;
    /**
     * 商户号
     */
    private String merchantId;
    /**
     * 支付渠道类型
     */
    private String channelType;
    /**
     * 消费者用户标识
     */
    private String openId;
    /**
     * 操作员ID
     */
    private String operatorId;
    /**
     * 门店ID
     */
    private String storeId;
    /**
     * 终端设备号
     */
    private String deviceId;
    /**
     * 终端IP
     */
    private String deviceCreateIp;
    /**
     * 订单有效期（分钟1-1440）
     */
    private Integer expireExpress;
    /**
     * 清算方式 - T0，T1
     */
    private String settleType;
    /**
     * 附加信息
     */
    private String attach;
    /**
     * 支付异步通知回调地址
     */
    private String notifyUrl;
    /**
     * 商户微信支付时需要指定的微信支付公众号appid
     */
    private String subAppId;
    /**
     * 是否指定微信支付请求的SubMerchIdY:指定N:不指定
     */
    private String specifySubMerchId;
    /**
     * 微信支付渠道号
     */
    private String channelId;
    /**
     * 微信交易子商户号
     */
    private String subMerchId;
    /**
     * 禁用支付方式
     */
    private String payLimit;
    /**
     * 可打折金额
     */
    private Integer discountableAmount;
    /**
     * 不可打折金额
     */
    private Integer undiscountableAmount;
    /**
     * 支付宝的店铺编号
     */
    private String alipayStoreId;
    /**
     * 返佣PID
     */
    private String sysServiceProviderId;
    /**
     * 花呗交易分期数
     */
    private String checkLaterNm;
    /**
     * 预付单信息
     */
    private String prePayId;
    /**
     * 原生态js支付信息
     */
    private String payInfo;
    /**
     * 支付完成时间
     */
    private Date gmtPayment;
    /**
     * 付款银行
     */
    private String bankType;
    /**
     * 支付宝或微信端的订单号
     */
    private String payChannelOrderNo;
    /**
     * 商户订单号
     */
    private String merchantOrderNo;
    /**
     * 现金券金额
     */
    private Integer couponFee;
    /**
     * 是否关注公众账号 - 1：关注，2：未关注
     */
    private String isSubscribe;
    /**
     * 买家支付宝登录账号
     */
    private String buyerLogonId;
    /**
     * 买家支付宝用户id
     */
    private String buyerUserId;
    /**
     * 借贷标识 - credit：信用卡，pcredit：花呗（仅支付宝），debit：借记卡，balance：余额，unknown：未知
     */
    private String credit;
    /**
     * 实收金额
     */
    private Integer receiptAmount;
    /**
     * 用户实付金额
     */
    private Integer buyerPayAmount;
    /**
     * 开票金额
     */
    private Integer invoiceAmount;
    /**
     * 支付状态 - succ：支付成功，fail：失败，paying：支付中，closed：已关单，cancel：已撤消
     */
    private String tradeStatus;
    /**
     * 是否有效 - 1：有效，0：无效
     */
    private String validInd;
    /**
     * 是否删除  -1：已删除  0：正常
     */
    private String delFlag;
    /**
     * 创建人代码
     */
    private String creatorCode;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 更新人代码
     */
    private String updaterCode;
    /**
     * 更新时间
     */
    private Date updateTime;




}
