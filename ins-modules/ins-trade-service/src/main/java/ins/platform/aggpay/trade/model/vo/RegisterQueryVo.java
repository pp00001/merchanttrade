package ins.platform.aggpay.trade.model.vo;

import lombok.Data;

@Data
public class RegisterQueryVo {
	private String resultStatus;
	private String resultCode;
	private String resultMsg;
	private String merchantId;
	private String registerStatus;
	private String failReason;
	private String outTradeNo;
	private String orderNo;
	private String accountNo;
	private String smid;
	private String wechatChannelList;
	private String onlineSmid;
	private String alipayChannelList;
	
	 
	
//	private RegisterQueryVo a;
}
