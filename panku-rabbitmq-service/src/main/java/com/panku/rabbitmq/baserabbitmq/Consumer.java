package com.panku.rabbitmq.baserabbitmq;

import com.rabbitmq.client.*;


/**
 * @description:消费者 -- > 接收消息
 * @author: crazy.coder
 */
public class Consumer {

    public static final String QUEUE_NAME = "hello";

    public static void main(String[] args) {

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("127.0.0.1");
        factory.setUsername("admin");
        factory.setPassword("admin");
        try {
            Connection connection = factory.newConnection();
            Channel channel = connection.createChannel();
            //声明
            DeliverCallback deliverCallback = (consumerTag, message)->{
                System.out.println(new String(message.getBody()));
            };

            CancelCallback cancelCallback = (consumerTag) ->{
              //消费消息被中断
                System.out.println("消费消息被终端");
            };

            /**
             * 消费者消费消息
             * 1.消费哪个队列
             * 2.消费成功后是否自动应答 true自动 false手动
             * 3.消费者未成功消费的回调
             * 4.消费者取消 消费回调
             */
            channel.basicConsume(QUEUE_NAME,true, deliverCallback, cancelCallback);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
