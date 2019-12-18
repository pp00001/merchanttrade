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

import java.io.Serializable;

/**
 * @author RipinYan
 * @ClassName: SmsVo
 * @Description: 短信Vo
 * @date 2018/10/30 7:33 PM
 */
@Data
public class SmsVo implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 短信业务类型。本短信验证码对应的业务。可选值：
	 * 01：开通银行账户
	 * 02：换绑银行卡
	 * 03：更换银行预留手机号
	 * 04：商户入驻申请
	 * 05：余利宝提现
	 */
	private String bizType;
	/**
	 * 商户号。当BizType为02、03、05时必填，其余情况不可填
	 */
	private String merchantId;
	/**
	 * 手机号。短信验证码接受手机号。当BizType为01、03、04时必填，其余情况不可填。
	 */
	private String mobile;
	/**
	 * 外部交易号
	 */
	private String outTradeNo;

}
