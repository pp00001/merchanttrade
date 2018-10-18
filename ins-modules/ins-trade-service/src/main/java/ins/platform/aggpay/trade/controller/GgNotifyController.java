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

package ins.platform.aggpay.trade.controller;

import ins.platform.aggpay.common.web.BaseController;
import ins.platform.aggpay.trade.config.TradeConfig;
import ins.platform.aggpay.trade.service.GgNotifyService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author RipinYan
 * @ClassName: GgNotifyController
 * @Description: TODO
 * @date 2018/10/12 下午2:57
 */

@RestController
@RequestMapping("/notify")
public class GgNotifyController extends BaseController {

	@Autowired
	private GgNotifyService ggNotifyService;
	@Autowired
	private TradeConfig tradeConfig;

	/**
	 * @Title: prePayNotice
	 * @Description: 支付结果异步通知
	 * @param requestXml 请求报文
	 * @throws
	 * @author Ripin Yan
	 * @return java.lang.String 响应报文
	 */
	@RequestMapping(value = "/pay/paynotifyres", method = RequestMethod.POST)
	public String prePayNotice(@RequestBody String requestXml) {

		System.out.println("appId=" + tradeConfig.getAppId());
		String responseXml;
		try {
			responseXml = ggNotifyService.prePayNotice(requestXml);
		} catch (Exception e) {
			responseXml = "支付结果异步通知处理异常!";
			logger.error(responseXml + e.getMessage(), e);
		}

		return responseXml;
	}

	/**
	 * @Title: notifyPayResult
	 * @Description: 打款结果通知接口
	 * @param requestXml 请求报文
	 * @throws
	 * @author Ripin Yan
	 * @return java.lang.String 响应报文
	 */
	@RequestMapping(value = "/pay/pay", method = RequestMethod.POST)
	public String notifyPayResult(@RequestBody String requestXml) {

		String responseXml;
		try {
			responseXml = ggNotifyService.notifyPayResult(requestXml);
		} catch (Exception e) {
			responseXml = "打款结果通知处理异常!";
			logger.error(responseXml + e.getMessage(), e);
		}

		return responseXml;
	}

}
