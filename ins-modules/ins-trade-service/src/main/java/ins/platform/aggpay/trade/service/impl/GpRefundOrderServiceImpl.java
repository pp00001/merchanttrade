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

package ins.platform.aggpay.trade.service.impl;

import ins.platform.aggpay.trade.entity.GpRefundOrder;
import ins.platform.aggpay.trade.mapper.GpRefundOrderMapper;
import ins.platform.aggpay.trade.service.GpRefundOrderService;
import ins.platform.aggpay.trade.vo.GpRefundOrderVo;

import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;

/**
 * <p>
 * 退款订单表 服务实现类
 * </p>
 *
 * @author ripin
 * @since 2018-10-18
 */
@Service
public class GpRefundOrderServiceImpl extends ServiceImpl<GpRefundOrderMapper, GpRefundOrder> implements GpRefundOrderService {

	private static final Logger logger = LoggerFactory.getLogger(GpRefundOrderServiceImpl.class);

	@Autowired
	private GpRefundOrderMapper gpRefundOrderMapper;

	@Override
	public List<GpRefundOrderVo> selectListByOutTradeNo(String outTradeNo) {
		logger.info("通过外部交易号{}查询退款订单数据。", outTradeNo);
		List<GpRefundOrderVo> refundOrderVos = new ArrayList<>();
		try {
			List<GpRefundOrder> refundOrderList = gpRefundOrderMapper.selectList(new EntityWrapper<GpRefundOrder>().eq("out_trade_no", outTradeNo).eq("valid_ind", "1"));
			for (int i = 0; i < refundOrderList.size(); i++) {
				GpRefundOrderVo refundOrderVo = new GpRefundOrderVo();
				BeanUtils.copyProperties(refundOrderList.get(i), refundOrderVo);
				refundOrderVos.add(refundOrderVo);
			}

		} catch (Exception e) {
			logger.error("查询退款订单异常，异常信息：" + e.getMessage(), e);
			refundOrderVos = null;
		}

		return refundOrderVos;
	}
}
