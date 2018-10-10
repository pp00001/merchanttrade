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
 * @ClassName: RegistResVo
 * @Description: TODO
 * @date 2018/9/18 下午4:23
 */
@Data
public class RegistResVo {

	private RespInfoVo respInfo;//返回码组件。Result Status 返回S不代表入驻成功，只代表入驻申请已受理。需要回查。
	private String outMerchantId;//外部商户号
	private String orderNo;//申请单号。合作方可通过此单号回查商户进件结果。
	private String outTradeNo;//外部交易号
	
}
