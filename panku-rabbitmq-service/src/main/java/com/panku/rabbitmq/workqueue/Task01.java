package com.panku.rabbitmq.workqueue;

import com.panku.rabbitmq.utils.RabbitMQUtils;
import com.rabbitmq.client.Channel;

import java.util.Scanner;

/**
 * @description:
 * @author: crazy.coder
 */
public class Task01 {

    //队列的名称
    public static final String QUEUE_NAME="hello";

    public static void main(String[] args) {

        Channel channel = RabbitMQUtils.getChannel();
        //队列的声明
        try {
            channel.queueDeclare(QUEUE_NAME, false, false, false, null);
            //从控制台中接受消息
            Scanner sc = new Scanner(System.in);
            while (sc.hasNext()){
                String message = sc.next();
                channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
                System.out.println("消息发送完毕！");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
