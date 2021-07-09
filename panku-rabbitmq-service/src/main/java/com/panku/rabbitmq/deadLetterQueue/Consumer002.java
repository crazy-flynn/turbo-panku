package com.panku.rabbitmq.deadLetterQueue;

import com.panku.rabbitmq.utils.RabbitMQUtils;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

import java.util.HashMap;
import java.util.Map;

/**
 * @description:
 * @author: crazy_coder
 */
public class Consumer002 {

    //队列名
    public static final String QUEUE_DEAD_NAME = "queue_dead";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMQUtils.getChannel();
        System.out.println("Consumer02 等待接收消息....");
        //接收消息 回调
        DeliverCallback deliverCallback = (consumerTag, message) ->{
            System.out.println("Consumer02 接收到消息：：" + new String(message.getBody()));
        };
        //取消消息 回调
        CancelCallback cancelCallback = (consumerTag) ->{
            //消费消息被中断
            System.out.println("Consumer02 消费消息被中断");
        };
        channel.basicConsume(QUEUE_DEAD_NAME, true, deliverCallback, cancelCallback);
    }

}
