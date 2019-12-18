/*
 * Copyright (c) 2018. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package ins.platform.aggpay.trade.config;

import java.util.HashMap;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static ins.platform.aggpay.common.constant.MqQueueConstant.DINGTALK_SERVICE_STATUS_CHANGE;
import static ins.platform.aggpay.common.constant.MqQueueConstant.LOG_QUEUE;
import static ins.platform.aggpay.common.constant.MqQueueConstant.MOBILE_CODE_QUEUE;
import static ins.platform.aggpay.common.constant.MqQueueConstant.MOBILE_SERVICE_STATUS_CHANGE;
import static ins.platform.aggpay.common.constant.MqQueueConstant.PAY_NOTICE_15_QUEUE;
import static ins.platform.aggpay.common.constant.MqQueueConstant.PAY_NOTICE_30_QUEUE;
import static ins.platform.aggpay.common.constant.MqQueueConstant.PAY_NOTICE_QUEUE;
import static ins.platform.aggpay.common.constant.MqQueueConstant.ROUTE_CONFIG_CHANGE;
import static ins.platform.aggpay.common.constant.MqQueueConstant.ZIPKIN_NAME_QUEUE;

/**
 * @author lengleng
 * @date 2017/11/16
 * rabbit初始化配置
 */
@Configuration
public class RabbitConfig {


    public static final String EXCHANGE_TTL = "exchange_ttl";
    public static final String EXCHANGE_DEFAULT = "exchange_default";

//    @Bean
//    public DirectExchange defaultExchange() {
//        return new DirectExchange(EXCHANGE_DEFAULT);
//    }

    @Bean
    public DirectExchange ttlExchange() {
        return new DirectExchange(EXCHANGE_TTL);
    }

    /**
     * 正常通知队列
     * @return
     */
    @Bean
    public Queue payNoticeQueue() {
        return new Queue(PAY_NOTICE_QUEUE);
    }

    /**
     * 延迟15秒通知队列
     * @return
     */
    @Bean
    public Queue payNotice15Queue() {
        HashMap<String, Object> args = new HashMap<>(3);
        args.put("x-dead-letter-exchange", EXCHANGE_TTL);
        args.put("x-dead-letter-routing-key", PAY_NOTICE_QUEUE);
        args.put("x-message-ttl", 15000);
        return new Queue(PAY_NOTICE_15_QUEUE, true, false, false, args);
    }

    /**
     * 延迟30秒通知队列
     * @return
     */
    @Bean
    public Queue payNotice30Queue() {
        HashMap<String, Object> args = new HashMap<>(3);
        args.put("x-dead-letter-exchange", EXCHANGE_TTL);
        args.put("x-dead-letter-routing-key", PAY_NOTICE_QUEUE);
        args.put("x-message-ttl", 30000);
        return new Queue(PAY_NOTICE_30_QUEUE, true, false, false, args);
    }



    /**
     * 初始化 log队列
     *
     * @return
     */
    @Bean
    public Queue initLogQueue() {
        return new Queue(LOG_QUEUE);
    }

    /**
     * 初始化 手机验证码通道
     *
     * @return
     */
    @Bean
    public Queue initMobileCodeQueue() {
        return new Queue(MOBILE_CODE_QUEUE);
    }

    /**
     * 初始化服务状态改变队列
     *
     * @return
     */
    @Bean
    public Queue initMobileServiceStatusChangeQueue() {
        return new Queue(MOBILE_SERVICE_STATUS_CHANGE);
    }

    /**
     * 初始化钉钉状态改变队列
     *
     * @return
     */
    @Bean
    public Queue initDingTalkServiceStatusChangeQueue() {
        return new Queue(DINGTALK_SERVICE_STATUS_CHANGE);
    }

    /**
     * 初始化zipkin队列
     *
     * @return
     */
    @Bean
    public Queue initZipkinQueue() {
        return new Queue(ZIPKIN_NAME_QUEUE);
    }

    /**
     * 初始化路由配置状态队列
     *
     * @return
     */
    @Bean
    public Queue initRouteConfigChangeQueue() {
        return new Queue(ROUTE_CONFIG_CHANGE);
    }


    @Bean
    public Binding ttlBinding() {
        return BindingBuilder.bind(payNoticeQueue()).to(ttlExchange()).with(PAY_NOTICE_QUEUE);
    }

//    @Bean
//    public Binding defaultBinding() {
//        return BindingBuilder.bind(initLogQueue()).to(defaultExchange()).with(LOG_QUEUE);
//    }

}
