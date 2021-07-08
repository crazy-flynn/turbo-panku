package com.panku.rabbitmq.exchange.direct;

import com.panku.rabbitmq.utils.RabbitMQUtils;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

/**
 * @description:
 * @author: crazy.coder
 */
public class DirectConsumer02 {

    public static final String EXCHANGE_NAME = "exchange_direct_c1";
    public static final String QUEUE_NAME = "disk";
    public static final String ROUTING_ERROR = "error";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMQUtils.getChannel();
        //声明一个交换机
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);
        //声明一个队列
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        /**
         * 绑定交换机与队列
         */
        channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, ROUTING_ERROR);
        System.out.println("DirectConsumer02 等待接受消息，并将消息打印在屏幕上...");
        //接收消息 回调
        DeliverCallback deliverCallback = (consumerTag, message) ->{
            System.out.println("DirectConsumer02 接收到消息：：" + new String(message.getBody()));
        };
        //取消消息 回调
        CancelCallback cancelCallback = (consumerTag) ->{
            //消费消息被中断
            System.out.println("DirectConsumer02 消费消息被中断");
        };
        /**
         * 消费消息
         */
        channel.basicConsume(QUEUE_NAME, true, deliverCallback, cancelCallback);

    }

}
