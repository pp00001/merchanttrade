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
 * <p>
 * 退款订单表
 * </p>
 *
 * @author ripin
 * @since 2018-10-18
 */
@Data
public class GpRefundOrderVo {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    private Long id;
    /**
     * 支付返回的订单号
     */
    private String orderNo;
    /**
     * 外部交易号
     */
    private String outTradeNo;
    /**
     * 商户号
     */
    private String merchantId;
    /**
     * 退款订单号
     */
    private String refundOrderNo;
    /**
     * 退款交易号
     */
    private String outRefundNo;
    /**
     * 交易总金额
     */
    private Integer totalAmount;
    /**
     * 币种
     */
    private String currency;
    /**
     * 支付渠道类型
     */
    private String channelType;
    /**
     * 操作员ID
     */
    private String operatorId;
    /**
     * 终端设备号
     */
    private String deviceId;
    /**
     * 终端IP
     */
    private String deviceCreateIp;
    /**
     * 退款金额
     */
    private Integer refundAmount;
    /**
     * 退款原因。支付宝交易须填写
     */
    private String refundReason;
    /**
     * 实收金额
     */
    private Integer receiptAmount;
    /**
     * 用户实付金额
     */
    private Integer buyerPayAmount;
    /**
     * 退款完成时间
     */
    private Date gmtRefundment;
    /**
     * 支付状态 - succ：支付成功，fail：失败，refunding：退款中
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

    /**
     * 返回码对象
     */
    private RespInfoVo respInfo;


}
