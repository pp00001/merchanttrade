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

package ins.platform.aggpay.trade.listener;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ins.platform.aggpay.trade.service.GgNotifyService;
import ins.platform.aggpay.trade.vo.GpTradeOrderVo;
import lombok.extern.slf4j.Slf4j;

import static ins.platform.aggpay.common.constant.MqQueueConstant.PAY_NOTICE_QUEUE;

/**
 * @author RipinYan
 * @ClassName: PayNoticeReceiveListener
 * @Description: TODO
 * @date 2019/1/21 11:36 AM
 */
@Slf4j
@Component
@RabbitListener(queues = PAY_NOTICE_QUEUE)
public class PayNoticeReceiveListener {

	@Autowired
	private GgNotifyService ggNotifyService;

	@RabbitHandler
	public void receive(GpTradeOrderVo orderVo) {
		long startTime = System.currentTimeMillis();
		log.info("支付成功结果通知消息。订单号：{}，支付成功时间: {}。 ", orderVo.getOrderNo(), orderVo.getGmtPayment());
		//ggNotifyService.merchantPayResultNotice(orderVo);
		long useTime = System.currentTimeMillis() - startTime;
		log.info("调用钉钉网关处理完毕，耗时 {}毫秒", useTime);
	}
}
