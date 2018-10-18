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

/**
 * @author RipinYan
 * @ClassName: RefundVo
 * @Description: TODO
 * @date 2018/10/18 下午7:25
 */
@Data
public class RefundVo {

	/**
	 * 原支付交易外部交易号
	 */
	private String outTradeNo;
	/**
	 * 商户号
	 */
	private String merchantId;
	/**
	 * 退款外部交易号
	 */
	private String outRefundNo;
	/**
	 * 退款金额。币种同原交易
	 */
	private String refundAmount;
	/**
	 * 退款原因。支付宝交易须填写
	 */
	private String refundReason;
	/**
	 * 操作员ID
	 */
	private String operatorId;
	/**
	 * 终端设备号(门店号或收银设备ID)
	 */
	private String deviceId;
	/**
	 * 创建订单终端的IP
	 */
	private String deviceCreateIp;

}
