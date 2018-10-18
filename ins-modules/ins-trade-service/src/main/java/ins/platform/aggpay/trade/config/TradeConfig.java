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

package ins.platform.aggpay.trade.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author RipinYan
 * @ClassName: TradeConfig
 * @Description: TODO
 * @date 2018/10/11 上午11:53
 */
@Configuration
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "trade")
public class TradeConfig {


	public static class Sftp{
		private String host;

		public String getHost() {
			return host;
		}

		public void setHost(String host) {
			this.host = host;
		}
	}
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
	 * 微信公众号secret
	 */
	private String appSecret= "db103c301a738509947262c73b895615";
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
	 * 微信获取用户openId
	 */
	private String wxOauthUrl;
	/**
	 * 支付宝通用api地址
	 */
	private String aliOauthUrl;

	public static final String APP_PRIVATE_KEY =
			"MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCV3Ju7rfIehCjimtzIeMb8lrZszv2w0QDdbtjNmKcvl3yuzTMzKddSs7X" +
			"+ESPyydnKITLphExpliP/smqP0nfJFg7qFaNJTQxXxq3lLTZW82Bh8pEN2ZcsLUgkstLiTDv+NoQjkDw7Kw9NcRcgOlZseeO1zlemIE6u4zM8QIHNQYXcrDq1uaOcyU3LG02pq1" +
			"/du2XljdQIN8mEj7deFnfs4XFLlB/GbG+4HbiQGfD/nh75qWvqdOHGu9UZUwxgpkaBMIboKZXXPVSOt6HR0KvEQcKnmbf+Ozavbb005Y8mR3qrhtSWC5TzIt" +
			"/wtfSVO2Vq5I223CgfyFMNy/fPxO8FAgMBAAECggEAfqUolrqxWkilpJrq6h/nSZ60G8/xZHO8H2WFonnXD8kdfTu8gQhjB2kH6+XgU1Vxz/euZtl/GdvgID5O" +
			"/6wFvtH9WKVgkJmkTKmCW6KRwXl7gkrTerjfoF3EEf9taAC00miP0t4ZiIcjL3ba7Elgr9tyLDpsp9+1nuYybkC1cRcXNKz63euk" +
			"+zhyBrYwMoROBzchFfyaTOHlGy1GXe2cLLz78t0PywWglHu/lHGTDHIBBZKHTrXM0AM3LVaigUVX82m9Cjo9+IW/uV+ckJzM/Vo0qpLUlgi90wQbGW8DcGljfK" +
			"/efPQWuzohOgG7QMUQtwwd0fR+JbspFXRcBsEHAQKBgQDlTI+XOVY+5sR6y2B9pul3DyWacRSLmNkVqSm0POq9ziuW9A21ABg7Tj3I83A6mjCyVSwtGL7k0wtAs9okf+nhw" +
			"/jo45EN/yQlOCqcVa+2ZwszUsMt8MOV2UFc67fvwguEKkQxeIJptJx7uX59v7OkaKMu4LzOoTs+uELGpi7OeQKBgQCnUAJi3yB8h7vqe" +
			"+bI3uef6Bz8UQW5i2FIJzFZIEPTvCh7a2onSgUqa3Hev7/5x5TH6BsepHaEacLQTGlDxORcfU+mpwtY1QMSIR7WAPwtHgVoE1ClJ1PQx6tBykvZJ3Tto5j3cLmObENUVTqxp6ON" +
			"/zNoPN/ImoAkfaHQCxfR7QKBgQCxWlBNxTliGZeq6pdNWMaHIh4RoJkliCmQSXFKSTu/ZzHr5gScFOCpLlE3lqMdkJlNtfcfQl6UGnA/sVxukslRqARkDW" +
			"/qhYdtik3a8aOgz36oScFRTUHaK1oVBvUT6uCBbzejk/Q60kmeoNCnbpkB9zUQUx92KtrqHhb4Ex1s8QKBgC61b3UaxX7+hb" +
			"+Yh31cfV1u92iZVffOqYHzLxuqnkTmKocKHcCKMZb+F/QPpBCfXzHP4oJTd6LPw8tTCbAZr4cClNH5oHlUPl85T9p+u+f8kZXUjpcMu6F1nKHpT/N3yHTvTy0FE0hngQRyJsih" +
			"/E8QB9H57J+cQlntmNbzwdqhAoGANo/a54HZNsig31Qu6Ngls6eLMFxPB6Y61XxOb8+y2uoB4QYbnL+6as2NDEzZOmvlOlgPDF+eXsFMltWZnXvFQeNCvfu5a9" +
			"/Gy8CbHGywXP4wyajV8IiXLVFqjCNwd62Mo6szxS1JkRUcstFdpfRhyupVzke/Yu9s/i9SBvV2liE=";
	public static final String ALIPAY_PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAoDyE8wbAUt4jwVkzp69Mv1uoxLa3at9y7ecFvq+ZpNOO0uTcAuYFNuWlegi" +
			"/rlMPtZUBZ8quyRGKt87DcIfdqKeoEf7B62N759nj2yB5svfF7iRTgdzk6aipLa9C2PET/rg5akxg2cqA0lwBlLe7Np6ZgLmmYL" +
			"+Ck6P4fqolUCuf9lxVdbAK0OCtb1iFi78VSBIEgUyYXvWVDNMcw83ow0YYZBRF/8KZhr0/4puIp6+XX50PGL/zZOwXWbgAaCa5OELo+Y/gwcFgo" +
			"/YlhaEBGmu5kEzpYO4R1IhEjEtaWa0zuik8J0Cjpki4dosPCrKmUeciFEzEwB2wMEhAXpKQKQIDAQAB";



	/**
	 * 订单有效期（1-1440分钟）
	 */
	private String expireExpress = "60";
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

	public String getAppSecret() {
		return appSecret;
	}

	public void setAppSecret(String appSecret) {
		this.appSecret = appSecret;
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

	public void setSign(boolean sign) {
		isSign = sign;
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

	public String getWxOauthUrl() {
		return wxOauthUrl;
	}

	public void setWxOauthUrl(String wxOauthUrl) {
		this.wxOauthUrl = wxOauthUrl;
	}

	public String getAliOauthUrl() {
		return aliOauthUrl;
	}

	public void setAliOauthUrl(String aliOauthUrl) {
		this.aliOauthUrl = aliOauthUrl;
	}

	public String getAppPrivateKey() {
		return APP_PRIVATE_KEY;
	}

	public String getAlipayPublicKey() {
		return ALIPAY_PUBLIC_KEY;
	}

	public String getExpireExpress() {
		return expireExpress;
	}

	public void setExpireExpress(String expireExpress) {
		this.expireExpress = expireExpress;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}
}
