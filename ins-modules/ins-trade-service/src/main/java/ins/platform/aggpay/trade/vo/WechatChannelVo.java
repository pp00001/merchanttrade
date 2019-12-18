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

/**
 * @author RipinYan
 * @ClassName: WechatChannelVo
 * @Description: TODO
 * @date 2018/9/18 下午2:08
 */
@Data
public class WechatChannelVo implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 微信渠道号
	 */
	private String channelId;
	/**
	 * 进驻结果：0处理中，1成功，2失败
	 */
	private String status;
	/**
	 * 微信子商户号信息
	 */
	private String wechatMerchId;
	/**
	 * 进驻失败原因
	 */
	private String failReason;

}
