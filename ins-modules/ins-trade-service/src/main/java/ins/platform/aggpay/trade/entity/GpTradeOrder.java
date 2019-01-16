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
 * 交易订单表
 * </p>
 *
 * @author ripin
 * @since 2018-10-11
 */
@Data
@Accessors(chain = true)
@TableName("gp_trade_order")
public class GpTradeOrder extends Model<GpTradeOrder> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 支付单类型 - create：主扫，pay：被扫，create_dynamic：动态扫码
     */
    @TableField("order_type")
    private String orderType;
    /**
     * 订单号
     */
    @TableField("order_no")
    private String orderNo;
    /**
     * 第三方支付授权码
     */
    @TableField("auth_code")
    private String authCode;
    /**
     * 外部交易号
     */
    @TableField("out_trade_no")
    private String outTradeNo;
    /**
     * 商品描述
     */
    private String body;
    /**
     * 商品id，此id为二维码中包含的商品ID，商户自行定义。
     */
    @TableField("goods_id")
    private String goodsId;
    /**
     * 商品标记
     */
    @TableField("goods_tag")
    private String goodsTag;
    /**
     * 商品详情列表
     */
    @TableField("goods_detail")
    private String goodsDetail;
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
     * 商户号
     */
    @TableField("merchant_id")
    private String merchantId;
    /**
     * 支付渠道类型
     */
    @TableField("channel_type")
    private String channelType;
    /**
     * 消费者用户标识
     */
    @TableField("open_id")
    private String openId;
    /**
     * 操作员ID
     */
    @TableField("operator_id")
    private String operatorId;
    /**
     * 门店ID
     */
    @TableField("store_id")
    private String storeId;
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
     * 订单有效期（分钟1-1440）
     */
    @TableField("expire_express")
    private Integer expireExpress;
    /**
     * 清算方式 - T0，T1
     */
    @TableField("settle_type")
    private String settleType;
    /**
     * 附加信息
     */
    private String attach;
    /**
     * 支付异步通知回调地址
     */
    @TableField("notify_url")
    private String notifyUrl;
    /**
     * 商户微信支付时需要指定的微信支付公众号appid
     */
    @TableField("sub_app_id")
    private String subAppId;
    /**
     * 是否指定微信支付请求的SubMerchIdY:指定N:不指定
     */
    @TableField("specify_sub_merch_id")
    private String specifySubMerchId;
    /**
     * 微信支付渠道号
     */
    @TableField("channel_id")
    private String channelId;
    /**
     * 微信交易子商户号
     */
    @TableField("sub_merch_id")
    private String subMerchId;
    /**
     * 禁用支付方式
     */
    @TableField("pay_limit")
    private String payLimit;
    /**
     * 可打折金额
     */
    @TableField("discountable_amount")
    private Integer discountableAmount;
    /**
     * 不可打折金额
     */
    @TableField("undiscountable_amount")
    private Integer undiscountableAmount;
    /**
     * 支付宝的店铺编号
     */
    @TableField("alipay_store_id")
    private String alipayStoreId;
    /**
     * 返佣PID
     */
    @TableField("sys_service_provider_id")
    private String sysServiceProviderId;
    /**
     * 花呗交易分期数
     */
    @TableField("check_later_nm")
    private String checkLaterNm;
    /**
     * 预付单信息
     */
    @TableField("pre_pay_id")
    private String prePayId;
    /**
     * 原生态js支付信息
     */
    @TableField("pay_info")
    private String payInfo;
    /**
     * 二维码串值，用以生成支付二维码
     */
    @TableField("qr_code_url")
    private String qrCodeUrl;
    /**
     * 支付完成时间
     */
    @TableField("gmt_payment")
    private Date gmtPayment;
    /**
     * 付款银行
     */
    @TableField("bank_type")
    private String bankType;
    /**
     * 支付宝或微信端的订单号
     */
    @TableField("pay_channel_order_no")
    private String payChannelOrderNo;
    /**
     * 商户订单号
     */
    @TableField("merchant_order_no")
    private String merchantOrderNo;
    /**
     * 现金券金额
     */
    @TableField("coupon_fee")
    private Integer couponFee;
    /**
     * 是否关注公众账号 - 1：关注，2：未关注
     */
    @TableField("is_subscribe")
    private String isSubscribe;
    /**
     * 买家支付宝登录账号
     */
    @TableField("buyer_logon_id")
    private String buyerLogonId;
    /**
     * 买家支付宝用户id
     */
    @TableField("buyer_user_id")
    private String buyerUserId;
    /**
     * 借贷标识 - credit：信用卡，pcredit：花呗（仅支付宝），debit：借记卡，balance：余额，unknown：未知
     */
    private String credit;
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
     * 开票金额
     */
    @TableField("invoice_amount")
    private Integer invoiceAmount;
    /**
     * 支付状态 - succ：支付成功，fail：失败，paying：支付中，closed：已关单，cancel：已撤消
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
