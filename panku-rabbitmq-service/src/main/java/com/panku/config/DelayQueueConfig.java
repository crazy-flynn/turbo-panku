package com.panku.config;

import com.rabbitmq.client.BuiltinExchangeType;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.CustomExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * @description:
 * @author: crazy.coder
 * @create: 2021-07-09
 */
@Configuration
public class DelayQueueConfig {

    //普通交换机
    public static final String DELAY_EXCHANGE = "delayed.exchange";
    //普通队列
    public static final String DELAY_QUEUE = "delayed.queue";
    //rountingKey
    public static final String DELAY_KEY = "delayed.key";

    //声明延迟队列
    @Bean
    public Queue delayedQueue(){
        return new Queue(DELAY_QUEUE);
    }

    //基于插件声明交换机
    @Bean
    public CustomExchange delayExchange() {
        Map<String,Object> arguments = new HashMap<>();
        arguments.put("x-delayed-message", BuiltinExchangeType.DIRECT);
        /**
         * 1.交换机的名称
         * 2.交换机的类型
         * 3.是否需要持久化
         * 4.是否需要自动删除
         * 5.其他的参数
         */
        return new CustomExchange(DELAY_EXCHANGE, "x-delayed-message",true, false, arguments);
    }

    //队列和交换机绑定
    @Bean
    public Binding delayedQueueBindDelayExchange(@Qualifier("delayedQueue") Queue delayedQueue,
                                                 @Qualifier("delayExchange") CustomExchange delayExchange){

        return BindingBuilder.bind(delayedQueue).to(delayExchange).with(DELAY_KEY).noargs();
    }
}
