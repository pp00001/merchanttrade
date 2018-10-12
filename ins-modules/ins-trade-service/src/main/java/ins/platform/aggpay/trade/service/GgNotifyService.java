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

package ins.platform.aggpay.trade.service;

/**
 * @author RipinYan
 * @ClassName: GgNotifyService
 * @Description: TODO
 * @date 2018/10/12 下午2:59
 */
public interface GgNotifyService {

	/**
	 * prePayNotice(主扫H5支付结果通知)
	 *
	 * @Title: prePayNotice
	 * @Description:
	 * @param requestXml 请求报文
	 * @throws
	 * @author Ripin Yan
	 * @return java.lang.String 响应报文
	 */
	String prePayNotice(String requestXml) throws Exception;

	/**
	 * notifyPayResult(打款失败通知)
	 *
	 * @Title: notifyPayResult
	 * @Description:
	 * @param requestXml 请求报文
	 * @throws
	 * @author Ripin Yan
	 * @return java.lang.String 响应报文
	 */
	String notifyPayResult(String requestXml) throws Exception;
}
