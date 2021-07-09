package com.panku.rabbitmq.deadLetterQueue;

import com.panku.rabbitmq.utils.RabbitMQUtils;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;

/**
 * @description:
 * @author: crazy_coder
 */
public class Producer001 {

    //交换机
    public static final String EXCHANGE_NORMAL_NAME = "exchange_normal";
    //routingKEY
    public static final String ROUTING_KEY = "normal_key";

    public static void main(String[] args) throws Exception {
        //连接信道
        Channel channel = RabbitMQUtils.getChannel();
        //设置消息过期时间 TTL ms
//        AMQP.BasicProperties properties = new AMQP.BasicProperties().builder().expiration("10000").build();
        for (int i = 1; i <= 10; i++) {
            String message = "第 " + i + " 个消息！";
            channel.basicPublish(EXCHANGE_NORMAL_NAME, ROUTING_KEY, null, message.getBytes("UTF-8"));
//            channel.basicPublish(EXCHANGE_NORMAL_NAME, ROUTING_KEY, properties, message.getBytes("UTF-8"));
            System.out.println("发送" + message);
        }
        System.out.println("消息发送完毕！");

    }

}
