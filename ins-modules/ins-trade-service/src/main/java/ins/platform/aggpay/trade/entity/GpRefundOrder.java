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
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

/**
 * <p>
 * 退款订单表
 * </p>
 *
 * @author ripin
 * @since 2018-10-18
 */
@Data
@Accessors(chain = true)
@TableName("gp_refund_order")
public class GpRefundOrder extends Model<GpRefundOrder> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 支付返回的订单号
     */
    @TableField("order_no")
    private String orderNo;
    /**
     * 外部交易号
     */
    @TableField("out_trade_no")
    private String outTradeNo;
    /**
     * 商户号
     */
    @TableField("merchant_id")
    private String merchantId;
    /**
     * 退款订单号
     */
    @TableField("refund_order_no")
    private String refundOrderNo;
    /**
     * 退款交易号
     */
    @TableField("out_refund_no")
    private String outRefundNo;
    /**
     * 交易总金额
     */
    @TableField("total_amount")
    private Integer totalAmount;
    /**
     * 币种
     */
    private String currency;
    /**
     * 支付渠道类型
     */
    @TableField("channel_type")
    private String channelType;
    /**
     * 操作员ID
     */
    @TableField("operator_id")
    private String operatorId;
    /**
     * 终端设备号
     */
    @TableField("device_id")
    private String deviceId;
    /**
     * 终端IP
     */
    @TableField("device_create_ip")
    private String deviceCreateIp;
    /**
     * 退款金额
     */
    @TableField("refund_amount")
    private Integer refundAmount;
    /**
     * 退款原因
     */
    @TableField("refund_reason")
    private String refundReason;
    /**
     * 实收金额
     */
    @TableField("receipt_amount")
    private Integer receiptAmount;
    /**
     * 用户实付金额
     */
    @TableField("buyer_pay_amount")
    private Integer buyerPayAmount;
    /**
     * 退款完成时间
     */
    @TableField("gmt_refundment")
    private Date gmtRefundment;
    /**
     * 支付状态 - succ：支付成功，fail：失败，refunding：退款中
     */
    @TableField("trade_status")
    private String tradeStatus;
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

}
