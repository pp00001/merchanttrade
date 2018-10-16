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

import java.util.Map;

import com.mybank.bkmerchant.base.AbstractReq;


public class PublicCall extends AbstractReq{
 
	public final static String FUNCTION_REGISTER = "ant.mybank.merchantprod.merchant.register";
	
	public final static String FUNCTION_MERCHANTQUERY = "ant.mybank.merchantprod.merchant.query";
	public final static String FUNCTION_FREEZE = "ant.mybank.merchantprod.merchant.freeze";
	public final static String FUNCTION_UNFREEZE = "ant.mybank.merchantprod.merchant.unfreeze";
	public final static String FUNCTION_PREPAY = "ant.mybank.bkmerchanttrade.prePay";

	
	private Map<String, String> body;
	
	public PublicCall(String function) {
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
