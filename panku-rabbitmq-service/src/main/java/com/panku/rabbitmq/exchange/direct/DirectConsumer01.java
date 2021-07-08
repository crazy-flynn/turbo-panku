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
public class DirectConsumer01 {

    public static final String EXCHANGE_NAME = "exchange_direct_c1";
    public static final String QUEUE_NAME = "console";
    public static final String ROUTING_INFO = "info";
    public static final String ROUTING_WARNING = "warning";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMQUtils.getChannel();
        //声明一个交换机
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);
        //声明一个队列
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        /**
         * 绑定交换机与队列
         */
        channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, ROUTING_INFO);
        channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, ROUTING_WARNING);
        System.out.println("DirectConsumer01 等待接受消息，并将消息打印在屏幕上...");
        //接收消息 回调
        DeliverCallback deliverCallback = (consumerTag, message) ->{
            System.out.println("DirectConsumer01 接收到消息：：" + new String(message.getBody()));
        };
        //取消消息 回调
        CancelCallback cancelCallback = (consumerTag) ->{
            //消费消息被中断
            System.out.println("DirectConsumer01 消费消息被中断");
        };
        /**
         * 消费消息
         */
        channel.basicConsume(QUEUE_NAME, true, deliverCallback, cancelCallback);

    }

}
