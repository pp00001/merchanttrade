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

import java.util.Date;
import java.util.Map;
import com.mybank.bkmerchant.base.AbstractReq;
import com.xiaoleilu.hutool.date.DatePattern;
import com.xiaoleilu.hutool.date.DateUtil;
import com.xiaoleilu.hutool.util.RandomUtil;


public class ApiCallUtil extends AbstractReq{
 
	public final static String FUNCTION_REGISTER = "ant.mybank.merchantprod.merchant.register";
	public final static String FUNCTION_MERCHANTQUERY = "ant.mybank.merchantprod.merchant.query";
	public final static String FUNCTION_FREEZE = "ant.mybank.merchantprod.merchant.freeze";
	public final static String FUNCTION_UNFREEZE = "ant.mybank.merchantprod.merchant.unfreeze";
	public final static String FUNCTION_PREPAY = "ant.mybank.bkmerchanttrade.prePay";
	public final static String FUNCTION_REFUND = "ant.mybank.bkmerchanttrade.refund";
	public static final String FUNCTION_PRE_PAY_NOTICE = "ant.mybank.bkmerchanttrade.prePayNotice";
	public static final String FUNCTION_NOTIFY_PAY_RESULT = "ant.mybank.bkmerchantsettle.notifyPayResult";

	/**
	 * 外部交易号生成器
	 * @return
	 */
	public static String generateOutTradeNo(){
		StringBuffer sf = new StringBuffer();
		sf.append("INS");
		sf.append(DateUtil.format(new Date(), DatePattern.PURE_DATETIME_PATTERN));
		sf.append(RandomUtil.randomNumbers(7));
		return sf.toString();
	}

	private Map<String, String> body;
	
	public ApiCallUtil(String function) {
		super(function);
		
	}

	@Override
	public Map<String, String> getBody() {
		return this.body;
	}

	public void setBody(Map<String, String> body){
		this.body = body;
	}
}
