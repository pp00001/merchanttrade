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
 * 报文往来日志表
 * </p>
 *
 * @author ripin
 * @since 2018-10-11
 */
@Data
@Accessors(chain = true)
@TableName("gg_xml_log")
public class GgXmlLog extends Model<GgXmlLog> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 接口代码
     */
    private String function;
    /**
     * 请求报文
     */
    @TableField("request_xml")
    private String requestXml;
    /**
     * 响应报文
     */
    @TableField("response_xml")
    private String responseXml;
    /**
     * 处理状态
     */
    @TableField("result_status")
    private String resultStatus;
    /**
     * 返回码 - S：成功，F：失败，U：未知
     */
    @TableField("result_code")
    private String resultCode;
    /**
     * 返回码信息
     */
    @TableField("result_msg")
    private String resultMsg;
    /**
     * 请求时间
     */
    @TableField("req_time")
    private Date reqTime;
    /**
     * 响应时间
     */
    @TableField("resp_time")
    private Date respTime;
    /**
     * 调用状态 - 1：成功，0：失败
     */
    private String flag;
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
