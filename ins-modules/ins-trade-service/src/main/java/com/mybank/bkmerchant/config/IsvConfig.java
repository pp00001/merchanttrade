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

package com.mybank.bkmerchant.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author RipinYan
 * @ClassName: IsvConfig
 * @Description: TODO
 * @date 2018/10/11 上午11:53
 */
@Configuration
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "isv.config")
public class IsvConfig {

	/**
	 * 是否dev环境
	 */
	public static final boolean isDev        = false;
	/**
	 * 是否开启验签
	 */
	public static final boolean isSign       = true;

	/**
	 * 身份证
	 */
	public static String        certNo       = "512734195005252263";

	/**
	 * 商户ID
	 * 1. 自然人-226801000000141472259
	 * 2. 企业商户-226801000000142576686
	 */
	public static String        merchantId   = "226801000000142576686";

	/**
	 * 渠道
	 */
	public static final String  channel      = "MYBANK";

	/**
	 * 短信验证码
	 */
	public static final String  smsCode      = "888888";

	/**
	 * 币种
	 */
	public static final String  currencyCode = "156";

	public static String        reqUrl       = "https://fcsupergw.dl.alipaydev.com/open/api/common/request2.htm";

	public static String        payUrl       = "https://fcsupergw.dl.alipaydev.com/open/api/common/requestasync.htm";

	public static String        uploadUrl    = "https://fcsupergwlite.dl.alipaydev.com/ant/mybank/merchantprod/merchant/uploadphoto.htm";

	public static String        oauthUrl    = "https://openauth.alipay.com/oauth2/publicAppAuthorize.htm?app_id=2018073160766860&scope=auth_base&redirect_uri=https://baidu.com";

	public static String        openApiUrl    = "https://openapi.alipay.com/gateway.do";
	/**
	 * partner
	 */
	public static String        IsvOrgId     = "202210000000000001316";

	/**
	 * 联调阶段使用1.0.0的版本，上线前需要根据情况重新配置
	 */
	public static String        version      = "1.0.0";

	public static String        appId        = "2018090700000286";

}
