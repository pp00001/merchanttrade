package ins.platform.aggpay.trade.entity;

import com.baomidou.mybatisplus.enums.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

import com.baomidou.mybatisplus.annotations.Version;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * <p>
 * 账单时间表
 * </p>
 *
 * @author zhangyu
 * @since 2018-10-23
 */
@Data
@Accessors(chain = true)
@TableName("gg_bill")
public class GgBill extends Model<GgBill> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 商户号
     */
    @TableField("merchant_id")
    private String merchantId;
    /**
     * 账单日期
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @TableField("bill_date")
    private Date billDate;
    /**
     * 文件名
     */
    @TableField("file_name")
    private String fileName;
    /**
     * 生成时间
     */
    @TableField("generate_time")
    private String generateTime;
    /**
     * 文件路径
     */
    private String directory;
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
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
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
