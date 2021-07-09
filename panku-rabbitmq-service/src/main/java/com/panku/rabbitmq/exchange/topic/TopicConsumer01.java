package com.panku.rabbitmq.exchange.topic;

import com.panku.rabbitmq.utils.RabbitMQUtils;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

/**
 * @description: 主题交换机
 * @author: crazy.coder
 */
public class TopicConsumer01 {

    //交换机
    public static final String EXCHANGE_NAME = "exchange_topic";
    //队列名
    public static final String QUEUE_NAME = "queue_topic01";


    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMQUtils.getChannel();
        //声明交换机
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.TOPIC);
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, "*.orange.*");
        System.out.println("TopicConsumer01 等待接收消息...");
        //接收消息 回调
        DeliverCallback deliverCallback = (consumerTag, message) ->{
            System.out.println("TopicConsumer01 通过 " + message.getEnvelope().getRoutingKey() + " 接收到消息：：" + new String(message.getBody()));
        };
        //取消消息 回调
        CancelCallback cancelCallback = (consumerTag) ->{
            //消费消息被中断
            System.out.println("TopicConsumer01 消费消息被中断");
        };
        channel.basicConsume(QUEUE_NAME, true, deliverCallback, cancelCallback);
    }

}
