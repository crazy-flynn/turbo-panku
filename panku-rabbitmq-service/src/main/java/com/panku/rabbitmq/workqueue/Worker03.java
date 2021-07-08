package com.panku.rabbitmq.workqueue;

import com.panku.rabbitmq.utils.RabbitMQUtils;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;

/**
 * @description: 接收消息 -- > 工作线程3
 * @author: crazy.coder
 */
public class Worker03 {

    //队列的名称
    public static final String QUEUE_NAME="hello";

    public static void main(String[] args) {

        Channel channel = RabbitMQUtils.getChannel();
        //接收到消息时的回调
        DeliverCallback deliverCallback = (consumerTag, message)->{
            System.out.println("接收到的消息：：" + new String(message.getBody()));
        };
        //消息接受被取消时 执行下面内容
        CancelCallback cancelCallback = (consumerTag) ->{
            System.out.println(consumerTag + "消费者取消消费接口回调逻辑");
        };
        System.out.println("C3等待接收消息...");
        try {
            channel.basicConsume(QUEUE_NAME, true, deliverCallback, cancelCallback);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
