package com.panku.rabbitmq.autoanswer;

import com.panku.rabbitmq.utils.RabbitMQUtils;
import com.panku.rabbitmq.utils.SleepUtils;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;

/**
 * @description: 消息自动应答 - > 消息在手动应答时不丢失，放回队列中重新消费
 * @author: crazy.coder
 */
public class AAConsumer01 {

    //队列名称
    public static final String TASK_QUEUE_NAME = "ack_queue";

    public static void main(String[] args) {
        Channel channel = RabbitMQUtils.getChannel();
        System.out.println("Consumer01 等待接收消息处理，短时间...");

        DeliverCallback deliverCallback = (consumerTag, message)->{
            SleepUtils.sleep(1);
            System.out.println("Consumer01 接收到的消息：：" + new String(message.getBody()));
            //设置不公平分发 1不公平 0公平轮训
            //若设置为其它数字，则为预取值
            int prefetchCount = 2;
            channel.basicQos(prefetchCount);
            /**
             * 采用手动应答
             * 1.消息的标记
             * 2.是否批量应答 false不批量应答信道中消息
             */
            channel.basicAck(message.getEnvelope().getDeliveryTag(), false);
        };
        CancelCallback cancelCallback = (consumerTag) ->{
            //消费消息被中断
            System.out.println("Consumer01 消费者取消消费调用");
        };
        boolean autoACK = false;
        try {
            channel.basicConsume(TASK_QUEUE_NAME, autoACK, deliverCallback, cancelCallback);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
