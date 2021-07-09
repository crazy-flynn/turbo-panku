package com.panku.rabbitmq.deadLetterQueue;

import com.panku.rabbitmq.utils.RabbitMQUtils;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

import java.util.HashMap;
import java.util.Map;

/**
 * @description:
 * @author: crazy_coder
 */
public class Consumer001 {

    //交换机
    public static final String EXCHANGE_NORMAL_NAME = "exchange_normal";
    public static final String EXCHANGE_DEAD_NAME = "exchange_dead";
    //队列名
    public static final String QUEUE_NORMAL_NAME = "queue_normal";
    public static final String QUEUE_DEAD_NAME = "queue_dead";
    //routingKEY
    public static final String ROUTING_KEY = "normal_key";
    public static final String ROUTING_DEAD_KEY = "dead_key";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMQUtils.getChannel();
        //声明交换机
        channel.exchangeDeclare(EXCHANGE_NORMAL_NAME, BuiltinExchangeType.DIRECT);
        channel.exchangeDeclare(EXCHANGE_DEAD_NAME, BuiltinExchangeType.DIRECT);
        //普通队列
        Map<String, Object> arguments = new HashMap<>();
        //设置过期时间，  可以在生产方发送设置时间
        //arguments.put("x-message-tll", 10000);
        // 设置死信交换机
        arguments.put("x-dead-letter-exchange", EXCHANGE_DEAD_NAME);
        //设置死信routingkey
        arguments.put("x-dead-letter-routing-key", ROUTING_DEAD_KEY);
        //正常队列的最大长度
        //arguments.put("x-max-length",6);
        channel.queueDeclare(QUEUE_NORMAL_NAME, false, false, false, arguments);
        //死信队列
        channel.queueDeclare(QUEUE_DEAD_NAME, false, false, false, null);
        //绑定普通交换机与队列
        channel.queueBind(QUEUE_NORMAL_NAME,EXCHANGE_NORMAL_NAME, ROUTING_KEY);
        //绑定死信交换机与队列
        channel.queueBind(QUEUE_DEAD_NAME, EXCHANGE_DEAD_NAME, ROUTING_DEAD_KEY);
        System.out.println("Consumer01 等待接收消息....");
        //接收消息 回调
        DeliverCallback deliverCallback = (consumerTag, message) ->{
            String messages = new String(message.getBody());
            //设置拒绝消息
            if ("第 6 个消息！".equals(messages)){
                System.out.println("此消息被拒绝：-> " + messages );
                //处理被拒绝
                /**
                 * 1.标签
                 * 2.是否塞回队列 如果为false 则不放回，进入死信队列
                 */
                channel.basicReject(message.getEnvelope().getDeliveryTag(), false);
            } else {
                System.out.println("Consumer01 接收到消息：：" + messages);
                channel.basicAck(message.getEnvelope().getDeliveryTag(), false);
            }
        };
        //取消消息 回调
        CancelCallback cancelCallback = (consumerTag) ->{
            //消费消息被中断
            System.out.println("Consumer01 消费消息被中断");
        };
        /**
         * 拒绝时，开启手动应答
         */
        boolean isAck = false;
        channel.basicConsume(QUEUE_NORMAL_NAME, isAck, deliverCallback, cancelCallback);
    }

}
