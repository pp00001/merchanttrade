package ins.platform.aggpay.trade.vo;

import lombok.Data;

/**
 * 5.1.2	图片上传接口应答报文
 */
@Data
public class UploadPhotoVo {
	
	private RespInfoVo respInfoVo;//返回码组件
	
	private String photoUrl;//文件唯一编号，非地址，公网不可访问
	
	private String outTradeNo;//外部交易号
	
}
