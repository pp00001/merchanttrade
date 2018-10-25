package ins.platform.aggpay.trade.service;

import ins.platform.aggpay.trade.entity.GpRefundOrder;
import ins.platform.aggpay.trade.vo.GpRefundOrderVo;

import java.util.List;
import com.baomidou.mybatisplus.service.IService;

/**
 * <p>
 * 退款订单表 服务类
 * </p>
 *
 * @author ripin
 * @since 2018-10-18
 */
public interface GpRefundOrderService extends IService<GpRefundOrder> {

	/**
	 * selectListByOutTradeNo(查询退款订单记录)
	 *
	 * @Title: selectListByOutTradeNo
	 * @Description:
	 * @param outTradeNo 外部交易号
	 * @throws
	 * @author Ripin Yan
	 * @return java.util.List<ins.platform.aggpay.trade.vo.GpRefundOrderVo>
	 */
	List<GpRefundOrderVo> selectListByOutTradeNo(String outTradeNo);

}
