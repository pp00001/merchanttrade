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

import java.io.Serializable;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import sun.misc.BASE64Encoder;

/**
 * @author RipinYan
 * @ClassName: GgBankCardParamVo
 * @Description: TODO
 * @date 2018/10/11 下午4:49
 */
@Data
public class GgBankCardParamVo implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	private Long id;
	/**
	 * 外部商户号
	 */
	private String outMerchantId;
	/**
	 * 银行卡号
	 */
	private String bankCardNo;
	/**
	 * 银行账户户名
	 */
	private String bankCertName;
	/**
	 * 账户类型 - 01：对私账户（自然人、个体工商户），02：对公账户（企业类型）
	 */
	private String accountType;
	/**
	 * 联行号
	 */
	private String contactLine;
	/**
	 * 开户支行名称
	 */
	private String branchName;
	/**
	 * 开户支行所在省
	 */
	private String branchProvince;
	/**
	 * 开户支行所在市
	 */
	private String branchCity;
	/**
	 * 持卡人证件类型 - 01：身份证（对公账户不需要填写）
	 */
	private String certType;
	/**
	 * 持卡人证件号码（对公账户不需要填写）
	 */
	private String certNo;
	/**
	 * 持卡人地址（对公账户不需要填写）
	 */
	private String cardHolderAddress;
	/**
	 * 是否有效 - 1：有效，0：无效
	 */
	private String validInd;
	/**
	 * 是否删除  -1：已删除  0：正常
	 */
	private String delFlag;
	/**
	 * 创建人代码
	 */
	private String creatorCode;
	/**
	 * 创建时间
	 */
	private Date createTime;
	/**
	 * 更新人代码
	 */
	private String updaterCode;
	/**
	 * 更新时间
	 */
	private Date updateTime;
	
	public String genJsonBase64() throws JSONException {
	    JSONObject obj = new JSONObject();
	
	    obj.put("BankCardNo", bankCardNo);
	    obj.put("BankCertName", bankCertName);
	    obj.put("AccountType", accountType);
	    obj.put("ContactLine", contactLine);
	    obj.put("BranchName", branchName);
	    obj.put("BranchProvince", branchProvince);
	    obj.put("BranchCity", branchCity);
	    obj.put("CertType", certType);
	    obj.put("CertNo", certNo);
	    obj.put("CardHolderAddress", cardHolderAddress);
	
	    System.out.println("BankCardParam：" + obj.toString());
	    
	    return new BASE64Encoder().encode(obj.toString().getBytes());
	}

}
