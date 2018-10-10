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

package ins.platform.aggpay.trade.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

/**
 * <p>
 * 清算卡信息表
 * </p>
 *
 * @author ripin
 * @since 2018-09-18
 */
@TableName("bank_card_param")
@Data
public class BankCardParam extends Model<BankCardParam> {

	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	@TableId(value = "id", type = IdType.AUTO)
	private Long id;
	/**
	 * 外部商户号
	 */
	@TableField("out_merchant_id")
	private String outMerchantId;
	/**
	 * 银行卡号
	 */
	@TableField("bank_card_no")
	private String bankCardNo;
	/**
	 * 银行账户户名
	 */
	@TableField("bank_cert_name")
	private String bankCertName;
	/**
	 * 账户类型 - 01：对私账户（自然人、个体工商户），02：对公账户（企业类型）
	 */
	@TableField("account_type")
	private String accountType;
	/**
	 * 联行号
	 */
	@TableField("contact_line")
	private String contactLine;
	/**
	 * 开户支行名称
	 */
	@TableField("branch_name")
	private String branchName;
	/**
	 * 开户支行所在省
	 */
	@TableField("branch_province")
	private String branchProvince;
	/**
	 * 开户支行所在市
	 */
	@TableField("branch_city")
	private String branchCity;
	/**
	 * 持卡人证件类型 - 01：身份证（对公账户不需要填写）
	 */
	@TableField("cert_type")
	private String certType;
	/**
	 * 持卡人证件号码（对公账户不需要填写）
	 */
	@TableField("cert_no")
	private String certNo;
	/**
	 * 持卡人地址（对公账户不需要填写）
	 */
	@TableField("card_holder_address")
	private String cardHolderAddress;
	/**
	 * 是否有效 - 1：有效，0：无效
	 */
	@TableField("valid_ind")
	private String validInd;
	/**
	 * 是否删除  -1：已删除  0：正常
	 */
	@TableField("del_flag")
	private String delFlag;
	/**
	 * 创建人代码
	 */
	@TableField("creator_code")
	private String creatorCode;
	/**
	 * 创建时间
	 */
	@TableField("create_time")
	private Date createTime;
	/**
	 * 更新人代码
	 */
	@TableField("updater_code")
	private String updaterCode;
	/**
	 * 更新时间
	 */
	@TableField("update_time")
	private Date updateTime;

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

	@Override
	public String toString() {
		return "BankCardParam{" + ", id=" + id + ", outMerchantId=" + outMerchantId + ", bankCardNo=" + bankCardNo + ", bankCertName=" +
				bankCertName + ", accountType=" + accountType + ", contactLine=" + contactLine + ", branchName=" + branchName + ", branchProvince="
				+ branchProvince + ", branchCity=" + branchCity + ", certType=" + certType + ", certNo=" + certNo + ", cardHolderAddress=" +
				cardHolderAddress + ", validInd=" + validInd + ", delFlag=" + delFlag + ", creatorCode=" + creatorCode + ", createTime=" +
				createTime + ", updaterCode=" + updaterCode + ", updateTime=" + updateTime + "}";
	}
}
