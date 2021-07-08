package com.panku.rabbitmq.baserabbitmq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * @description:生产者 ： 发消息
 * @author: crazy.coder
 */
public class Producer {

    public static final String QUEUE_NAME="hello";

    public static void main(String[] args) {
        //创建一个连接工厂
        ConnectionFactory factory = new ConnectionFactory();
        //工厂ip 连接rabbitmq的队列
        factory.setHost("127.0.0.1");
        factory.setUsername("admin");
        factory.setPassword("admin");
        try {
            //创建连接
            Connection connection = factory.newConnection();
            //获取信道 channel
            Channel channel = connection.createChannel();
            /*
            * 1.队列名称
            * 2.队列里面消息是否持久化，默认情况消息存储在内存中
            * 3.该队列是否只供一个消费者进行消费，是否进行消息共享，true可以多个消费者消费
            * 4.是否自动删除 最后一个消费者端开连接以后，该队列是否自动删除，true自动删除
            * 5.其他参数
            * */
            channel.queueDeclare(QUEUE_NAME, false, false, false, null);
            //发消息
            String message = "Hello PanKu ！";
            /*
            * 1.发送到哪个交换机
            * 2.路由的key值是哪个，本次是队列的名称
            * 3.其他参数消息
            * 4.发送消息的消息提
            * */
            channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
            System.out.println("消息发送完毕！");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
