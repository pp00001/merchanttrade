package ins.platform.aggpay.trade.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 5.1.2	图片上传接口应答报文
 */
@Data
public class UploadPhotoVo implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 外部交易号
	 */
	private String outTradeNo;
	/**
	 * 图片类型
	 * 01 身份证正面
	 * 02 身份证反面
	 * 03 营业执照
	 * 04 组织机构代码证
	 * 05 开户许可证
	 * 06 门头照
	 * 07 其他
	 */
	private String photoType;
	/**
	 * 图片地址
	 */
	private String picture;
	/**
	 * 返回码组件
	 */
	private RespInfoVo respInfo;

	/**
	 * 文件唯一编号，非地址，公网不可访问
	 */
	private String photoUrl;
}
