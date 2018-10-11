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

/**
 * @author RipinYan
 * @ClassName: GgMerchantDetailVo
 * @Description: TODO
 * @date 2018/10/11 下午4:44
 */
@Data
public class GgMerchantDetailVo implements Serializable{

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
	 * 商户简称
	 */
	private String alias;
	/**
	 * 联系人手机号
	 */
	private String contactMobile;
	/**
	 * 联系人姓名
	 */
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
	private String servicePhoneNo;
	/**
	 * 邮箱，注：不支持-
	 */
	private String email;
	/**
	 * 企业法人名称
	 */
	private String legalPerson;
	/**
	 * 负责人手机号,自然人或个体工商户上送，企业可不上送
	 */
	private String principalMobile;
	/**
	 * 负责人证件类型 - 01：身份证
	 */
	private String principalCertType;
	/**
	 * 负责人证件号码
	 */
	private String principalCertNo;
	/**
	 * 负责人名称或企业法人代表姓名
	 */
	private String principalPerson;
	/**
	 * 负责人名称或企业法人代表姓名
	 */
	private String bussAuthNum;
	/**
	 * 组织机构代码证号
	 */
	private String certOrgCode;
	/**
	 * 负责人或企业法人代表的身份证图片正面
	 */
	private String certPhotoA;
	/**
	 * 负责人或企业法人代表的身份证图片反面
	 */
	private String certPhotoB;
	/**
	 * 营业执照图片
	 */
	private String licensePhoto;
	/**
	 * 组织机构代码证图片
	 */
	private String prgPhoto;
	/**
	 * 开户许可证照片
	 */
	private String industryLicensePhoto;
	/**
	 * 门头照
	 */
	private String shopPhoto;
	/**
	 * 其他图片
	 */
	private String otherPhoto;
	/**
	 * 需关注的公众号appid
	 */
	private String subscribeAppiD;
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

}
