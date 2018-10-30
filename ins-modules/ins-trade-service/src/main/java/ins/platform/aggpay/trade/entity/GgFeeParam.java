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
 * 手续费信息表
 * </p>
 *
 * @author ripin
 * @since 2018-09-18
 */
@Data
@Accessors(chain = true)
@TableName("gg_fee_param")
public class GgFeeParam extends Model<GgFeeParam> {

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
	 * 渠道类型 - 01：支付宝，02：微信支付
	 */
    @TableField("channel_type")
	private String channelType;
	/**
	 * 费用类型 - 01：t0收单手续费，02：t1收单手续费
	 */
    @TableField("fee_type")
	private String feeType;
	/**
	 * 费率最多支持小数点后5位
	 */
    @TableField("fee_value")
	private String feeValue;
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
