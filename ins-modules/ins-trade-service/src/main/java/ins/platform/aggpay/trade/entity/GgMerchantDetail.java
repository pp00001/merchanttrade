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
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

/**
 * <p>
 * 商户详情表
 * </p>
 *
 * @author ripin
 * @since 2018-09-18
 */
@Data
@Accessors(chain = true)
@TableName("gg_merchant_detail")
public class GgMerchantDetail extends Model<GgMerchantDetail> {

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
	 * 商户简称
	 */
	private String alias;
	/**
	 * 联系人手机号
	 */
	@TableField("contact_mobile")
	private String contactMobile;
	/**
	 * 联系人姓名
	 */
	@TableField("contact_name")
	private String contactName;
	/**
	 * 省份
	 */
	private String province;
	/**
	 * 城市
	 */
	private String city;
	/**
	 * 区
	 */
	private String district;
	/**
	 * 地址
	 */
	private String address;
	/**
	 * 商户客服电话，注：不支持-
	 */
	@TableField("service_phone_no")
	private String servicePhoneNo;
	/**
	 * 邮箱，注：不支持-
	 */
	private String email;
	/**
	 * 企业法人名称
	 */
	@TableField("legal_person")
	private String legalPerson;
	/**
	 * 负责人手机号,自然人或个体工商户上送，企业可不上送
	 */
	@TableField("principal_mobile")
	private String principalMobile;
	/**
	 * 负责人证件类型 - 01：身份证
	 */
	@TableField("principal_cert_type")
	private String principalCertType;
	/**
	 * 负责人证件号码
	 */
	@TableField("principal_cert_no")
	private String principalCertNo;
	/**
	 * 负责人名称或企业法人代表姓名
	 */
	@TableField("principal_person")
	private String principalPerson;
	/**
	 * 负责人名称或企业法人代表姓名
	 */
	@TableField("buss_auth_num")
	private String bussAuthNum;
	/**
	 * 组织机构代码证号
	 */
	@TableField("cert_org_code")
	private String certOrgCode;
	/**
	 * 负责人或企业法人代表的身份证图片正面
	 */
	@TableField("cert_photo_a")
	private String certPhotoA;
	/**
	 * 负责人或企业法人代表的身份证图片反面
	 */
	@TableField("cert_photo_b")
	private String certPhotoB;
	/**
	 * 营业执照图片
	 */
	@TableField("license_photo")
	private String licensePhoto;
	/**
	 * 组织机构代码证图片
	 */
	@TableField("prg_photo")
	private String prgPhoto;
	/**
	 * 开户许可证照片
	 */
	@TableField("industry_license_photo")
	private String industryLicensePhoto;
	/**
	 * 门头照
	 */
	@TableField("shop_photo")
	private String shopPhoto;
	/**
	 * 其他图片
	 */
	@TableField("other_photo")
	private String otherPhoto;
	/**
	 * 需关注的公众号appid
	 */
	@TableField("subscribe_app_id")
	private String subscribeAppId;
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

}
