package ins.platform.aggpay.trade.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author RipinYan
 * @ClassName: RespInfo
 * @Description: 返回码组件
 * @date 2018/9/18 上午11:01
 */
@Data
public class RespInfoVo implements Serializable{

	private static final long serialVersionUID = 1L;

	/**
	 * 处理状态-S：成功，F：失败，U：未知
	 * 注意：状态为U不代表交易失败，需要通过回查去处理，不能直接反馈客户交易失败。
	 */
	private String resultStatus;
	/**
	 * 返回码
	 * 当resultStatus为S时，该字段必定为0000
	 * 当resultStatus为F或U时，该字段可以为全局返回码，也可以为业务返回码。
	 * 如果为业务返回码，参见业务接口部分。
	 */
	private String resultCode;
	/**
	 * 返回码信息
	 * 当resultStatus为S时，该字段可为空
	 * 当resultStatus为F或U时，需要描述该错误的原因
	 */
	private String resultMsg;


	@Override
	public String toString() {
		return "RespInfo{" + "resultStatus='" + resultStatus + '\'' + ", resultCode='" + resultCode + '\'' + ", resultMsg='" + resultMsg + '\'' + '}';
	}
}
