package com.panku.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * @description: rabbitmq 配置
 * @author: crazy.coder
 */
@Configuration
public class TtlQueueConfig {

    //普通交换机
    public static final String NORMAL_EXCHANGE = "normal_exchange";
    //死信交换机
    public static final String DEAD_EXCHANGE = "dead_exchange";
    //普通队列
    public static final String NORMAL_QUEUE_A = "normal_queue_a";
    public static final String NORMAL_QUEUE_B = "normal_queue_b";
    //自定义TTL队列
    public static final String CUSTOMER_QUEUE = "QC";
    //死信队列
    public static final String DEAD_QUEUE = "dead_letter_queue";
    //rountingKey
    public static final String ROUTING_KEY_A = "RA";
    public static final String ROUTING_KEY_B = "RB";
    public static final String ROUTING_KEY_D = "RD";
    public static final String ROUTING_KEY_C = "RC";

    //声明普通交换机 直接交换机
    @Bean("nExchange")
    public DirectExchange xExchange(){
        return new DirectExchange(NORMAL_EXCHANGE);
    }
    //声明死信交换机 直接交换机
    @Bean("dExchange")
    public DirectExchange dExchange(){
        return new DirectExchange(DEAD_EXCHANGE);
    }

    //声明队列
    @Bean("customerQueue")
    public Queue customerQueue(){
        Map<String, Object> arguments = new HashMap<>();
        //设置死信队列交换机
        arguments.put("x-dead-letter-exchange",DEAD_EXCHANGE);
        //设置死新routingkey
        arguments.put("x-dead-letter-routing-key",ROUTING_KEY_D);
        //TTL省略，根据业务定义
        return QueueBuilder.durable(CUSTOMER_QUEUE).withArguments(arguments).build();
    }
    @Bean("nAQueue")
    public Queue nAQueue(){
        Map<String, Object> arguments = new HashMap<>();
        //设置死信队列
        arguments.put("x-dead-letter-exchange",DEAD_EXCHANGE);
        //设置死新routingkey
        arguments.put("x-dead-letter-routing-key",ROUTING_KEY_D);
        //设置消息过期时间
        arguments.put("x-message-ttl",10000);
        return QueueBuilder.durable(NORMAL_QUEUE_A).withArguments(arguments).build();
    }

    @Bean("nBQueue")
    public Queue nBQueue(){
        Map<String, Object> arguments = new HashMap<>();
        //设置死信队列
        arguments.put("x-dead-letter-exchange",DEAD_EXCHANGE);
        //设置死新routingkey
        arguments.put("x-dead-letter-routing-key",ROUTING_KEY_D);
        //设置消息过期时间
        arguments.put("x-message-ttl",40000);
        return QueueBuilder.durable(NORMAL_QUEUE_B).withArguments(arguments).build();
    }
    //声明死信队列
    @Bean("dQueue")
    public Queue dQueue(){
        return QueueBuilder.durable(DEAD_QUEUE).build();
    }

    //绑定
    @Bean
    public Binding queueCBindingN(@Qualifier("customerQueue") Queue customerQueue,
                                  @Qualifier("nExchange") DirectExchange nExchange){
        return BindingBuilder.bind(customerQueue).to(nExchange).with(ROUTING_KEY_C);
    }

    @Bean
    public Binding queueABindingN(@Qualifier("nAQueue") Queue nAQueue,
                                 @Qualifier("nExchange") DirectExchange nExchange){
        return BindingBuilder.bind(nAQueue).to(nExchange).with(ROUTING_KEY_A);
    }

    @Bean
    public Binding queueBBindingN(@Qualifier("nBQueue") Queue nBQueue,
                                  @Qualifier("nExchange") DirectExchange nExchange){
        return BindingBuilder.bind(nBQueue).to(nExchange).with(ROUTING_KEY_B);
    }

    @Bean
    public Binding queueDBindingD(@Qualifier("dQueue") Queue dQueue,
                                  @Qualifier("dExchange") DirectExchange dExchange){
        return BindingBuilder.bind(dQueue).to(dExchange).with(ROUTING_KEY_D);
    }
}
