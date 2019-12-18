/*
 * Copyright (c) 2018. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package ins.platform.aggpay.trade.vo;

import lombok.Data;

/**
 * @author RipinYan
 * @ClassName: ResponseBodyVo
 * @Description: TODO
 * @date 2018/9/18 下午2:04
 */
@Data
public class ResponseBodyVo {

	private static final long serialVersionUID = 1L;

	/**
	 * 返回码组件
	 */
	private RespInfoVo respInfo;
	/**
	 * 外部交易号
	 */
	private String outTradeNo;

	/**
	 * 文件唯一编号，非地址，公网不可访问
	 */
	private String photoUrl;

	/**
	 * 外部商户号
	 */
	private String outMerchantId;
	/**
	 * 申请单号。合作方可通过此单号回查商户进件结果
	 */
	private String orderNo;

	/**
	 * 商户号
	 */
	private String merchantId;
	/**
	 * 入驻结果。可选值：0：审核中，1：成功，2：失败
	 * 当入驻结果为审核中，请间隔时间回查
	 */
	private String registerStatus;
	/**
	 * 入驻失败原因返回
	 */
	private String failReason;
	/**
	 * 二类户卡号
	 */
	private String accountNo;
	/**
	 * 支付宝进驻后的Smid
	 */
	private String smid;
	/**
	 * 标识进驻微信相关信息
	 */
	private WechatChannelVo wechatChannelList;

	/**
	 * 商户信息
	 */
	private GgMerchantVo ggMerchantVo;

	private String ChannelType;


}
