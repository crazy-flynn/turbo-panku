package com.panku.rabbitmq.autoanswer;

import com.panku.rabbitmq.utils.RabbitMQUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.MessageProperties;

import java.io.IOException;
import java.util.Scanner;

/**
 * @description: 消息自动应答 - > 消息在手动应答时不丢失，放回队列中重新消费
 * @author: crazy.coder
 */
public class AAProducer {

    //队列名称
    public static final String TASK_QUEUE_NAME = "ack_queue";

    public static void main(String[] args) {

        Channel channel = RabbitMQUtils.getChannel();

        try {
            //声明队列
            //消息队列持久化
            boolean durable = true;
            //已经创建持久化为false的队列，直接更改未true则运行时报错，应先移除
//            channel.queueDeclare(TASK_QUEUE_NAME, false, false, false, null);
            channel.queueDeclare(TASK_QUEUE_NAME, false, false, false, null);
            //控制台输入信息
            Scanner sc = new Scanner(System.in);
            while (sc.hasNext()){
                String message = sc.next();
                //消息设置持久化消息 保存在磁盘上，否则是保存在内存上 MessageProperties.PERSISTENT_TEXT_PLAIN
                channel.basicPublish("", TASK_QUEUE_NAME, MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes());
                System.out.println("生产者发出消息：：" + message);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }


}
