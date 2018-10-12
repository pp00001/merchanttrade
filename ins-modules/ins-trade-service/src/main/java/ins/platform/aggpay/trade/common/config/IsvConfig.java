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

package ins.platform.aggpay.trade.common.config;

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
@ConfigurationProperties(prefix = "isvConfig")
public class IsvConfig {

	/**
	 * 合作方机构号（网商银行分配）
	 */
	private String isvOrgId;

	/**
	 * 网商appid
	 */
	private String appId;
	/**
	 * 微信支付公众号
	 */
	private String subAppId;
	/**
	 * 微信交易子商户号
	 */
	private String subMerchId;
	/**
	 * 微信支付渠道号
	 */
	private String channelId;

	/**
	 * 是否开启验签
	 */
	private boolean isSign = true;

	/**
	 * 通用接口地址
	 */
	private String reqUrl;
	/**
	 * 支付相关接口地址
	 */
	private String payUrl;

	/**
	 * 图片上传接口地址
	 */
	private String uploadUrl;
	/**
	 * 获取用户id接口地址
	 */
	private String getUserIdUrl;
	/**
	 * 支付宝通用api地址
	 */
	private String openApiUrl;

	/**
	 * 联调阶段使用1.0.0的版本，上线前需要根据情况重新配置
	 */
	private String version = "1.0.0";

	public String getIsvOrgId() {
		return isvOrgId;
	}

	public void setIsvOrgId(String isvOrgId) {
		this.isvOrgId = isvOrgId;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getSubAppId() {
		return subAppId;
	}

	public void setSubAppId(String subAppId) {
		this.subAppId = subAppId;
	}

	public String getSubMerchId() {
		return subMerchId;
	}

	public void setSubMerchId(String subMerchId) {
		this.subMerchId = subMerchId;
	}

	public String getChannelId() {
		return channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

	public boolean isSign() {
		return isSign;
	}

	public String getReqUrl() {
		return reqUrl;
	}

	public void setReqUrl(String reqUrl) {
		this.reqUrl = reqUrl;
	}

	public String getPayUrl() {
		return payUrl;
	}

	public void setPayUrl(String payUrl) {
		this.payUrl = payUrl;
	}

	public String getUploadUrl() {
		return uploadUrl;
	}

	public void setUploadUrl(String uploadUrl) {
		this.uploadUrl = uploadUrl;
	}

	public String getGetUserIdUrl() {
		return getUserIdUrl;
	}

	public void setGetUserIdUrl(String getUserIdUrl) {
		this.getUserIdUrl = getUserIdUrl;
	}

	public String getOpenApiUrl() {
		return openApiUrl;
	}

	public void setOpenApiUrl(String openApiUrl) {
		this.openApiUrl = openApiUrl;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}
}
