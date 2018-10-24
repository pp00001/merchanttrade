package ins.platform.aggpay.trade.service;

import ins.platform.aggpay.trade.entity.GpTradeOrder;
import ins.platform.aggpay.trade.vo.GpTradeOrderVo;

import com.baomidou.mybatisplus.service.IService;

/**
 * <p>
 * 交易订单表 服务类
 * </p>
 *
 * @author ripin
 * @since 2018-10-11
 */
public interface GpTradeOrderService extends IService<GpTradeOrder> {

	GpTradeOrderVo findGpTradeOrderByOutTradeNo(String outTradeNo);

	boolean update(GpTradeOrderVo gpTradeOrderVo);
}
