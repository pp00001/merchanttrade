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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lombok.Data;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import sun.misc.BASE64Encoder;

/**
 * @author RipinYan
 * @ClassName: GgFeeParamVo
 * @Description: TODO
 * @date 2018/10/11 下午4:48
 */
@Data
public class GgFeeParamVo implements Serializable {

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
	 * 渠道类型 - 01：支付宝，02：微信支付
	 */
	private String channeltype;
	/**
	 * 费用类型 - 01：t0收单手续费，02：t1收单手续费
	 */
	private String feetype;
	/**
	 * 费率最多支持小数点后5位
	 */
	private String feevalue;
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
	
	public static String genJsonBase64(List<GgFeeParamVo> feeParamList) throws JSONException{
	    List<JSONObject> arr = new ArrayList<JSONObject>();
	    for (GgFeeParamVo param: feeParamList) {
	      JSONObject obj = new JSONObject();
	      obj.put("ChannelType", param.getChanneltype());
	      obj.put("FeeType", param.getFeetype());
	      obj.put("FeeValue", param.getFeevalue());

	      arr.add(obj);

	      System.out.println(obj.toString());
	    }

	    return new BASE64Encoder().encode(new JSONArray(arr).toString().getBytes());
	}
	
	

}
