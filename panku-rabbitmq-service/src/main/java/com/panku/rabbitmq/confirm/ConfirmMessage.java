package com.panku.rabbitmq.confirm;

import com.panku.rabbitmq.utils.RabbitMQUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmCallback;

import java.util.UUID;

/**
 * @description: 发布确认模式
 * 1.单个确认
 * 2.批量确认
 * 3.异步批量确认
 * @author: crazy.coder
 */
public class ConfirmMessage {

    public static final int MESSAGE_COUNT = 1000;

    public static void main(String[] args) throws Exception{

        //单个确认发送消息用时 --> 2071
        publicMessageSingle();
        //批量确认消息用时 --> 87
        publicMessageBatch();
        //异步确认消息用时 --> 46
        publicMessageAsync();

    }

    //单个确认
    public static void publicMessageSingle() throws Exception{
        Channel channel = RabbitMQUtils.getChannel();
        //队列名称
        String queueName = UUID.randomUUID().toString();
        channel.queueDeclare(queueName, true, false, false, null);
        //开启发布确认
        channel.confirmSelect();
        //开始时间
        long begin = System.currentTimeMillis();
        //批量发送消息
        for (int i = 1; i <= MESSAGE_COUNT; i++) {
            String message = i + " - > 个消息";
            channel.basicPublish("",queueName, null, message.getBytes());
            //单个消息确认
            boolean flag = channel.waitForConfirms();
            if (flag == true){
                System.out.println("第" + i + " - > 个消息发送成功");
            }
        }
        //结束时间
        long end = System.currentTimeMillis();
        System.out.println("单个确认发送消息用时 --> " + (end - begin));
    }

    //批量确认
    public static void publicMessageBatch() throws Exception{
        Channel channel = RabbitMQUtils.getChannel();
        //队列名称
        String queueName = UUID.randomUUID().toString();
        channel.queueDeclare(queueName, true, false, false, null);
        //开启发布确认
        channel.confirmSelect();
        //开始时间
        long begin = System.currentTimeMillis();
        //批量确认长度
        int batchSize = 100;
        //批量发送消息
        for (int i = 1; i <= MESSAGE_COUNT; i++) {
            String message = i + " - > 个消息";
            channel.basicPublish("",queueName, null, message.getBytes());
            //批量消息确认
            if (i%batchSize==0){
                boolean flag = channel.waitForConfirms();
                if (flag == true){
                    System.out.println("第" + i + " - > 个消息发送成功");
                }
            }
        }
        //结束时间
        long end = System.currentTimeMillis();
        System.out.println("批量确认消息用时 --> " + (end - begin));
    }

    //异步确认
    public static void publicMessageAsync() throws Exception{
        Channel channel = RabbitMQUtils.getChannel();
        //队列名称
        String queueName = UUID.randomUUID().toString();
        channel.queueDeclare(queueName, true, false, false, null);
        //开启发布确认
        channel.confirmSelect();
        //开始时间
        long begin = System.currentTimeMillis();
        //消息监听器，监听消息的成功与失败
        //消息确认成功 回调函数
        ConfirmCallback ackCallback = (deliveryTag, multiple) -> {
            System.out.println("确认的消息：：" + deliveryTag);
        };
        //消息确认失败 回调函数
        /*
        * 1.消息的标记
        * 2.是否未批量确认
        * */
        ConfirmCallback nackCallback = (deliveryTag, multiple) -> {
            System.out.println("未确认的消息：：" + deliveryTag);
        };
        /**
         * ackCallback 监听消息成功
         * nackCallback 监听消息失败
         */
        channel.addConfirmListener(ackCallback, nackCallback);

        //批量发送消息
        for (int i = 1; i <= MESSAGE_COUNT; i++) {
            String message = i + " - > 个消息";
            channel.basicPublish("",queueName, null, message.getBytes());
            //异步消息确认
        }
        //结束时间
        long end = System.currentTimeMillis();
        System.out.println("异步确认消息用时 --> " + (end - begin));
    }
}
