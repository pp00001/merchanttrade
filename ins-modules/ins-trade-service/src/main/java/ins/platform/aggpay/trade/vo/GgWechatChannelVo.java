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
 * <p>
 * 商户微信渠道表
 * </p>
 *
 * @author ripin
 * @since 2018-10-30
 */
@Data
public class GgWechatChannelVo implements Serializable {

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
     * 微信支付渠道号
     */
    private String channelId;
    /**
     * 微信商户号
     */
    private String wechatMerchId;
    /**
     * 微信入驻状态 - 0：处理中，1：成功，2：失败
     */
    private String status;
    /**
     * 微信入驻失败原因
     */
    private String failReason;
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
