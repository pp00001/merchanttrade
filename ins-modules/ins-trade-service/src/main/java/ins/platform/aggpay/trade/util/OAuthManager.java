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

package ins.platform.aggpay.trade.util;

import ins.platform.aggpay.trade.config.IsvConfig;

import java.net.URLEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author RipinYan
 * @ClassName: OAuthManager
 * @Description: TODO
 * @date 2018/10/16 上午11:18
 */
public class OAuthManager {
	@Autowired
	private IsvConfig isvConfig;

	private static Logger logger = LoggerFactory.getLogger(OAuthManager.class);

	/* 生成OAuth重定向URI（用户同意授权，获取code） */
	private static final String HTTPS_OPEN_WEIXIN_QQ_COM_CONNECT_OAUTH2_AUTHORIZE = "https://open.weixin.qq.com/connect/oauth2/authorize";
	/* 通过code换取网页授权access_token */
	private static final String HTTPS_API_WEIXIN_QQ_COM_SNS_OAUTH2_ACCESS_TOKEN = "https://api.weixin.qq.com/sns/oauth2/access_token";
	/* 刷新access_token（如果需要） */
	private static final String HTTPS_API_WEIXIN_QQ_COM_SNS_OAUTH2_REFRESH_TOKEN = "https://api.weixin.qq.com/sns/oauth2/refresh_token";
	/* 拉取用户信息(需scope为 snsapi_userinfo) */
	private static final String HTTPS_API_WEIXIN_QQ_COM_SNS_USERINFO = "https://api.weixin.qq.com/sns/userinfo";
	/* 检验授权凭证（access_token）是否有效 */
	private static final String HTTPS_API_WEIXIN_QQ_COM_SNS_AUTH = "https://api.weixin.qq.com/sns/auth";

	/**
	 * 生成OAuth重定向URI（用户同意授权，获取code）
	 * <p>
	 * 参考<a href="http://mp.weixin.qq.com/wiki/17/c0f37d5704f0b64713d5d2c37b468d75.html#.E7.AC.AC.E4.B8.80.E6.AD.A5.EF.BC.9A.E7.94.A8.E6.88.B7.E5.90.8C.E6.84.8F.E6.8E.88.E6.9D.83.EF.BC.8C.E8.8E.B7.E5.8F.96code">开发文档</a>
	 * </p>
	 *
	 * @param redirectURI
	 * @param scope
	 * @param state
	 * @return
	 */
	public static String generateRedirectURI(String redirectURI, String scope, String state) {
		StringBuffer url = new StringBuffer();
		url.append(HTTPS_OPEN_WEIXIN_QQ_COM_CONNECT_OAUTH2_AUTHORIZE);
//		url.append("?appid=").append(urlEncode(Config.instance().getAppid()));
//		url.append("&redirect_uri=").append(urlEncode(redirectURI));
//		url.append("&response_type=code");
//		url.append("&scope=").append(urlEncode(scope));
//		url.append("&state=").append(urlEncode(state));
		url.append("#wechat_redirect");
		return url.toString();
	}

//	/**
//	 * 通过code换取网页授权access_token
//	 * <p>
//	 * 参考<a href="http://mp.weixin.qq.com/wiki/17/c0f37d5704f0b64713d5d2c37b468d75.html#.E7.AC.AC.E4.BA.8C.E6.AD.A5.EF.BC.9A.E9.80.9A.E8.BF.87code.E6.8D.A2.E5.8F.96.E7.BD.91.E9.A1.B5.E6.8E.88.E6.9D.83access_token">开发文档</a>
//	 * </p>
//	 *
//	 * @param request
//	 * @return
//	 */
//	public static GetAccessTokenResponse getAccessToken(GetAccessTokenRequest request) throws OAuthException {
//		String response = post(HTTPS_API_WEIXIN_QQ_COM_SNS_OAUTH2_ACCESS_TOKEN, request);
//		check(response);
//		return JSONObject.parseObject(response, GetAccessTokenResponse.class);
//	}

	/**
	 * 拉取用户信息(需scope为 snsapi_userinfo)
	 * <p>
	 * 参考<a href="http://mp.weixin.qq.com/wiki/17/c0f37d5704f0b64713d5d2c37b468d75.html#.E7.AC.AC.E5.9B.9B.E6.AD.A5.EF.BC.9A.E6.8B.89.E5.8F.96.E7.94.A8.E6.88.B7.E4.BF.A1.E6.81.AF.28.E9.9C.80scope.E4.B8.BA_snsapi_userinfo.29">开发文档</a>
	 * </p>
	 *
	 * @param request
	 * @return
	 */
//	public static GetUserinfoResponse getUserinfo(GetUserinfoRequest request) throws OAuthException {
//		String response = post(HTTPS_API_WEIXIN_QQ_COM_SNS_USERINFO, request);
//		check(response);
//		return JSONObject.parseObject(response, GetUserinfoResponse.class);
//
//	}

//	public static String generateAliRedirectURI(String redirectUri, String s, String s1) {
//		StringBuffer url = new StringBuffer();
//		url.append(IsvConfig.APP_PRIVATE_KEY);
//		url.append("?appid=").append(isvConfig.getAppId());
//		url.append("&redirect_uri=").append(urlEncode(redirectURI));
//		url.append("&response_type=code");
//		url.append("&scope=").append(urlEncode(scope));
//		url.append("&state=").append(urlEncode(state));
//		url.append("#wechat_redirect");
//	}
	public static void main(String[] args) throws Exception {
		String encode = URLEncoder.encode("https://baidu.com?merchantId=232323&amount=32", "utf-8");
		System.out.println(encode);
	}
}
