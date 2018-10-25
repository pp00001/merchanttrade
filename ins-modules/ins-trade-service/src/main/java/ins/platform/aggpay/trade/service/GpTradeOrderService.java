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

	/**
	 * findGpTradeOrderByOutTradeNo(根据外部交易号查询订单记录)
	 *
	 * @Title: findGpTradeOrderByOutTradeNo
	 * @Description:
	 * @param outTradeNo 外部交易号
	 * @author Ripin Yan
	 * @return ins.platform.aggpay.trade.vo.GpTradeOrderVo
	 */
	GpTradeOrderVo findGpTradeOrderByOutTradeNo(String outTradeNo);

	/**
	 * updateById(更新订单记录)
	 *
	 * @Title: updateById
	 * @Description:
	 * @param gpTradeOrderVo
	 * @throws
	 * @author Ripin Yan
	 * @return boolean
	 */
	boolean updateById(GpTradeOrderVo gpTradeOrderVo);
}
