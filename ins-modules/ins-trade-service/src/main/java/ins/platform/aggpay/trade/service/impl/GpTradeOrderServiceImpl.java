package ins.platform.aggpay.trade.service.impl;

import ins.platform.aggpay.trade.entity.GpTradeOrder;
import ins.platform.aggpay.trade.mapper.GpTradeOrderMapper;
import ins.platform.aggpay.trade.service.GpTradeOrderService;
import ins.platform.aggpay.trade.vo.GpTradeOrderVo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;

/**
 * <p>
 * 交易订单表 服务实现类
 * </p>
 *
 * @author ripin
 * @since 2018-10-11
 */
@Service
public class GpTradeOrderServiceImpl extends ServiceImpl<GpTradeOrderMapper, GpTradeOrder> implements GpTradeOrderService {

	private static final Logger logger = LoggerFactory.getLogger(GpTradeOrderServiceImpl.class);

	@Override
	public GpTradeOrderVo findGpTradeOrderByOutTradeNo(String outTradeNo) {
		logger.info("通过外部交易号{}查询交易订单数据。", outTradeNo);
		GpTradeOrderVo vo = null;
		try {
			GpTradeOrder gpTradeOrder = selectOne(new EntityWrapper<GpTradeOrder>().eq("out_trade_no", outTradeNo));
			if(gpTradeOrder != null){
				vo = new GpTradeOrderVo();
				BeanUtils.copyProperties(gpTradeOrder, vo);
			}
		} catch (Exception e) {
			logger.error("查询交易订单异常，异常信息：" + e.getMessage(), e);
			vo = null;
		}
		return vo;
	}

	@Override
	public boolean update(GpTradeOrderVo gpTradeOrderVo) {

		return false;
	}
}
