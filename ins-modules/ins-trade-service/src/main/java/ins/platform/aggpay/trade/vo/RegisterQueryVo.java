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

@Data
public class RegisterQueryVo {
	
	private RespInfoVo respInfoVo;//返回码组件。Result Status 返回S不代表入驻成功，只代表申请单据已经查询到，结果需要看RegisterStatus字段。
	
	private String merchantId;//商户号。网商为商户分配的商户号，通过商户入驻结果查询接口获取。
	
	private String registerStatus;//入驻结果。可选值 0：审核中 1：成功  2：失败 当入驻结果为审核中，请间隔时间回查。

	private String outTradeNo;//外部交易号
	
	private String orderNo;//申请单号
	
	private String failReason;//入驻失败原因返回。当商户入驻结果为失败时返回。可能出现的失败原因描述见附录。
	
	private String accountNo;//二类户卡号
	
	private String wechatChannelList;//标识进驻的微信渠道号、进驻结果、微信子商户号信息、进驻失败原因 注：该字段2017.11月16日上线
	
	private String smid;//支付宝进驻后的Smid 注：该字段2017.11月16日上线

}
