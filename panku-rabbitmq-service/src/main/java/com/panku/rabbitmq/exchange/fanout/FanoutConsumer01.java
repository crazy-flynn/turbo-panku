package com.panku.rabbitmq.exchange.fanout;

import com.panku.rabbitmq.utils.RabbitMQUtils;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

/**
 * @description: 消息接收
 * @author: crazy.coder
 */
public class FanoutConsumer01 {

    public static final String EXCHANGE_NAME = "exchange_c1";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMQUtils.getChannel();
        //创建交换机
        channel.exchangeDeclare(EXCHANGE_NAME, "fanout");

        /**
         * 声明一个临时队列、队列的名称是随机的
         * 当消费者断开与队列连接的时候，队列将自动删除
         */
        String queueName = channel.queueDeclare().getQueue();
        /**
         * 绑定交换机与队列
         *
         */
        channel.queueBind(queueName, EXCHANGE_NAME, "");
        System.out.println("FanoutConsumer01 等待接受消息，并将消息打印在屏幕上...");
        //接收消息 回调
        DeliverCallback deliverCallback = (consumerTag, message) ->{
            System.out.println("FanoutConsumer01 接收到消息：：" + new String(message.getBody()));
        };
        //取消消息 回调
        CancelCallback cancelCallback = (consumerTag) ->{
            //消费消息被中断
            System.out.println("FanoutConsumer01 消费消息被中断");
        };
        channel.basicConsume(queueName, true, deliverCallback, cancelCallback);

    }

}
