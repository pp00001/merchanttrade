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

package ins.platform.aggpay.trade.constant;

/**
 * @author RipinYan
 * @ClassName: TradeConstant
 * @Description: TODO
 * @date 2018/10/12 下午3:16
 */
public class TradeConstant {

	public static final String CHANNEL_TYPE_WX = "WX";
	public static final String CHANNEL_TYPE_ALI = "ALI";
	public static final String CHANNEL_TYPE_QQ = "QQ";
	public static final String CHANNEL_TYPE_JD = "JD";
	public static final String CHANNEL_TYPE_OTHER = "OTHER";


	/**
	 * 支付宝配置
	 */
	public interface Ali {

		/**
		 * 以auth_user为scope发起的网页授权，是用来获取用户的基本信息的（比如头像、昵称等）。
		 * 但这种授权需要用户手动同意，用户同意后，就可在授权后获取到该用户的基本信息
		 */
		public static final String SCOP_AUTH_USER = "auth_user";
		/**
		 * 以auth_base为scope发起的网页授权，是用来获取进入页面的用户的userId的，
		 * 并且是静默授权并自动跳转到回调页的。用户感知的就是直接进入了回调页（通常是业务页面）
		 */
		public static final String SCOP_AUTH_BASE = "auth_base";

		/**
		 * 授权码换取
		 */
		public static final String GRANT_TYPE_CODE = "authorization_code";
		/**
		 * 刷新access_token时使用
		 */
		public static final String GRANT_TYPE_TOKEN = "refresh_token";
		/**
		 * 签名类型
		 */
		public static final String SIGN_TYPE = "RSA2";
		/**
		 * 数据格式
		 */
		public static final String FORMAT = "json";
		/**
		 * 编码格式
		 */
		public static final String CHARSET = "UTF-8";


	}

	/**
	 * 微信配置
	 */
	public interface Wx {

		/**
		 * 弹出授权页面，可通过openid拿到昵称、性别、所在地。并且，即使在未关注的情况下，只要用户授权，也能获取其信息
		 */
		public static final String SCOP_AUTH_USER = "snsapi_userinfo ";
		/**
		 * 不弹出授权页面，直接跳转，只能获取用户openid
		 */
		public static final String SCOP_AUTH_BASE = "snsapi_base";

		/**
		 * 授权码换取
		 */
		public static final String GRANT_TYPE_CODE = "authorization_code";
		/**
		 * 错误代码
		 */
		public static final String ERR_CODE = "errcode";
		/**
		 * 错误信息
		 */
		public static final String ERR_MSG = "errmsg";
		/**
		 * 公众号下唯一标识
		 */
		public static final String OPEN_ID = "openid";
		/**
		 * 编码格式
		 */
		public static final String CHARSET = "UTF-8";
		/**
		 * 返回类型
		 */
		public static final String CODE = "code";


	}

	/**
	 * 返回码
	 */
	public interface RespInfo {

		/**
		 * 成功
		 */
		public static final String RESULT_STATUS_SUCCESS = "S";
		/**
		 * 失败
		 */
		public static final String RESULT_STATUS_FAILURE = "F";
		/**
		 * 未知
		 */
		public static final String RESULT_STATUS_UNKNOWN = "U";
		/**
		 * 接口调用成功返回码
		 */
		public static final String RESULT_CODE_SUCCESS = "0000";

	}

	/**
	 * 支付单类型
	 */
	public interface OrderType {

		/**
		 * 主扫
		 */
		public static final String ORDER_TYPE_PREPAY = "create";
		/**
		 * 被扫
		 */
		public static final String ORDER_TYPE_PAY = "pay";
		/**
		 * 动态扫描
		 */
		public static final String ORDER_TYPE_DYNAMIC = "create_dynamic";

	}

	/**
	 * 交易状态
	 */
	public interface TradeStatus {

		/**
		 * 支付成功
		 */
		public static final String TRADE_STATUS_SUCC = "succ";
		/**
		 * 支付失败
		 */
		public static final String TRADE_STATUS_FAIL = "fail";
		/**
		 * 支付中
		 */
		public static final String TRADE_STATUS_PAYING = "paying";
		/**
		 * 已关单
		 */
		public static final String TRADE_STATUS_CLOSED = "closed";
		/**
		 * 已撤销
		 */
		public static final String TRADE_STATUS_CANCEL = "cancel";
		/**
		 * 退款中
		 */
		public static final String TRADE_STATUS_REFUNDING = "refunding";

	}



}
